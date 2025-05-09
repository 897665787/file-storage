// 配置AWS区域
AWS.config.update({
	region: 'cn-north-1' // 替换为你的区域
});

// 分片大小设置为5MB（最小5MB）
const CHUNK_SIZE = 5 * 1024 * 1024;
let uploadId = null;
let fileKey = null;
let uploadedParts = [];

async function startUpload() {
	const fileInput = document.getElementById('fileInput');
	const file = fileInput.files[0];

	if (!file) {
		updateStatus('请选择文件');
		return;
	}

	try {
		updateStatus('正在初始化上传...');

		// 1. 从后端获取上传凭证和初始化信息
		const initResponse = await fetch('http://localhost:4001/init-upload', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				fileName: file.name,
				fileType: file.type,
				fileSize: file.size
			})
		});

		const { credentials, uploadInfo } = await initResponse.json();
		uploadId = uploadInfo.uploadId;
		fileKey = uploadInfo.fileKey;

		// 配置AWS临时凭证
		AWS.config.update({
			credentials: new AWS.Credentials(
				credentials.accessKeyId,
				credentials.secretAccessKey,
				credentials.sessionToken
			)
		});

		updateStatus(`准备上传 ${file.name} (${formatFileSize(file.size)})...`);
		updateUploadDetails(uploadInfo);

		// 2. 计算分片数量
		const chunkCount = Math.ceil(file.size / CHUNK_SIZE);
		uploadedParts = [];

		// 3. 上传所有分片（可并行）
		for (let partNumber = 1; partNumber <= chunkCount; partNumber++) {
			const start = (partNumber - 1) * CHUNK_SIZE;
			const end = Math.min(start + CHUNK_SIZE, file.size);
			const chunk = file.slice(start, end);

			const partResponse = await uploadPart(partNumber, chunk);
			uploadedParts.push({
				PartNumber: partNumber,
				ETag: partResponse.ETag
			});

			// 更新进度
			const progress = Math.round((partNumber / chunkCount) * 100);
			updateProgress(progress);
		}

		// 4. 完成上传
		await completeUpload();
		updateStatus(`上传完成! 文件地址: ${fileKey}`);

	} catch (error) {
		console.error('上传失败:', error);
		updateStatus(`上传失败: ${error.message}`);
		// 可以添加中止上传的逻辑
	}
}

async function uploadPart(partNumber, chunk) {
	const s3 = new AWS.S3();

	const params = {
		Bucket: 's3-cn-test', // 替换为你的bucket
		Key: fileKey,
		PartNumber: partNumber,
		UploadId: uploadId,
		Body: chunk
	};

	try {
		const data = await s3.uploadPart(params).promise();
		return data;
	} catch (error) {
		console.error(`分片 ${partNumber} 上传失败:`, error);
		throw error;
	}
}

async function completeUpload() {
	const s3 = new AWS.S3();

	// 按PartNumber排序
	uploadedParts.sort((a, b) => a.PartNumber - b.PartNumber);

	const params = {
		Bucket: 's3-cn-test', // 替换为你的bucket
		Key: fileKey,
		UploadId: uploadId,
		MultipartUpload: {
			Parts: uploadedParts
		}
	};

	await s3.completeMultipartUpload(params).promise();
}

// 辅助函数
function updateStatus(message) {
	document.getElementById('status').innerText = message;
}

function updateProgress(percent) {
	document.getElementById('progressBar').value = percent;
	document.getElementById('percentage').innerText = `${percent}%`;
}

function updateUploadDetails(info) {
	const detailsDiv = document.getElementById('uploadDetails');
	detailsDiv.innerHTML = `
        <h3>上传详情</h3>
        <p><strong>文件Key:</strong> ${info.fileKey}</p>
        <p><strong>Upload ID:</strong> ${info.uploadId}</p>
        <p><strong>分片大小:</strong> ${formatFileSize(CHUNK_SIZE)}</p>
    `;
}

function formatFileSize(bytes) {
	if (bytes === 0) return '0 Bytes';
	const k = 1024;
	const sizes = ['Bytes', 'KB', 'MB', 'GB'];
	const i = Math.floor(Math.log(bytes) / Math.log(k));
	return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}