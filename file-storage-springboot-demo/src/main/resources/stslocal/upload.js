// 配置
const CHUNK_SIZE = 5 * 1024 * 1024; // 5MB
const API_BASE_URL = 'http://10.1.56.39:4001/api/upload';

// 全局变量
let uploadId = null;
let bucket = null;
let objectKey = null;
let file = null;
let fileKey = null;
let uploadedParts = [];
let uploadInProgress = false;
let s3 = null;
let abortController = new AbortController();

// DOM 元素
const fileInput = document.getElementById('file-input');
const uploadBtn = document.getElementById('upload-btn');
const pauseBtn = document.getElementById('pause-btn');
const resumeBtn = document.getElementById('resume-btn');
const progressContainer = document.getElementById('progress-container');
const progressBar = document.getElementById('progress-bar');
const progressText = document.getElementById('progress-text');
const fileNameSpan = document.getElementById('file-name');

// 事件监听
uploadBtn.addEventListener('click', startUpload);
pauseBtn.addEventListener('click', pauseUpload);
resumeBtn.addEventListener('click', resumeUpload);

// 获取STS临时凭证
async function getStsToken() {
	const response = await fetch(`${API_BASE_URL}/getTempCredentials`);
	if (!response.ok) {
		throw new Error('Failed to get STS token');
	}
	return await response.json();
}

// 初始化AWS S3客户端
async function initS3Client() {
	const stsToken = await getStsToken();

	bucket = stsToken.bucketName;

	s3 = new AWS.S3({
		region: stsToken.region, // 替换为你的区域
		credentials: new AWS.Credentials({
			accessKeyId: stsToken.accessKeyId,
			secretAccessKey: stsToken.secretAccessKey,
			sessionToken: stsToken.sessionToken
		}),
		signatureVersion: 'v4'
	});
}

// 初始化分片上传
async function initMultipartUpload(fileName) {
	const response = await fetch(`${API_BASE_URL}/initMultipartUpload?fileName=${encodeURIComponent(fileName)}`, {
		method: 'POST'
	});

	if (!response.ok) {
		throw new Error('Failed to initialize multipart upload');
	}

	return await response.json();
}

// 获取已上传的分片列表
async function listUploadedParts(uploadId, objectKey) {
	const response = await fetch(`${API_BASE_URL}/listParts?uploadId=${uploadId}&objectKey=${encodeURIComponent(objectKey)}`);

	if (!response.ok) {
		throw new Error('Failed to list uploaded parts');
	}

	return await response.json();
}

// 上传分片
async function uploadPart(bucket, partNumber, chunk, uploadId, objectKey) {
	const params = {
		Bucket: bucket, // 替换为你的bucket名称
		Key: objectKey,
		PartNumber: partNumber,
		UploadId: uploadId,
		Body: chunk
	};

	try {
		const data = await s3.uploadPart(params).promise();
		return data.ETag;
	} catch (error) {
		console.error('Error uploading part:', error);
		throw error;
	}
}

// 完成分片上传
async function completeMultipartUpload(uploadId, objectKey, parts) {
	const partETags = parts.map((part, index) => ({
		PartNumber: index + 1,
		ETag: part.ETag
	}));

	const response = await fetch(`${API_BASE_URL}/completeMultipartUpload?uploadId=${uploadId}&objectKey=${encodeURIComponent(objectKey)}`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(partETags)
	});

	if (!response.ok) {
		throw new Error('Failed to complete multipart upload');
	}

	return await response.text();
}

// 开始上传
async function startUpload() {
	if (!fileInput.files.length) {
		alert('请选择文件');
		return;
	}

	file = fileInput.files[0];
	fileKey = `${file.name}-${Date.now()}`;

	// 显示上传信息
	fileNameSpan.textContent = file.name;
	progressContainer.style.display = 'block';

	// 初始化客户端
	if (!s3) {
		await initS3Client();
	}

	// 初始化分片上传
	const initResponse = await initMultipartUpload(file.name);
	uploadId = initResponse.uploadId;
	objectKey = initResponse.objectKey;

	// 检查是否有已上传的分片
	uploadedParts = await listUploadedParts(uploadId, objectKey);

	// 开始上传
	uploadInProgress = true;
	uploadBtn.disabled = true;
	pauseBtn.disabled = false;

	await uploadFile();
}

// 上传文件
async function uploadFile() {
	const totalParts = Math.ceil(file.size / CHUNK_SIZE);
	let partNumber = uploadedParts.length + 1;

	try {
		for (; partNumber <= totalParts && uploadInProgress; partNumber++) {
			const start = (partNumber - 1) * CHUNK_SIZE;
			const end = Math.min(start + CHUNK_SIZE, file.size);
			const chunk = file.slice(start, end);

			const etag = await uploadPart(bucket, partNumber, chunk, uploadId, objectKey);

			uploadedParts.push({
				PartNumber: partNumber,
				ETag: etag
			});

			// 更新进度
			const progress = Math.round((end / file.size) * 100);
			progressBar.style.width = `${progress}%`;
			progressText.textContent = `${progress}%`;
		}

		if (uploadInProgress && uploadedParts.length === totalParts) {
			// 所有分片上传完成
			await completeMultipartUpload(uploadId, objectKey, uploadedParts);
			alert('文件上传完成!');
			resetUpload();
		}
	} catch (error) {
		if (error.name !== 'AbortError') {
			console.error('上传出错:', error);
			alert('上传出错: ' + error.message);
			resetUpload();
		}
	}
}

// 暂停上传
function pauseUpload() {
	uploadInProgress = false;
	abortController.abort();
	abortController = new AbortController();
	pauseBtn.disabled = true;
	resumeBtn.disabled = false;
}

// 恢复上传
async function resumeUpload() {
	uploadInProgress = true;
	resumeBtn.disabled = true;
	pauseBtn.disabled = false;
	await uploadFile();
}

// 重置上传状态
function resetUpload() {
	uploadInProgress = false;
	uploadId = null;
	objectKey = null;
	file = null;
	uploadedParts = [];
	uploadBtn.disabled = false;
	pauseBtn.disabled = true;
	resumeBtn.disabled = true;
	progressContainer.style.display = 'none';
	progressBar.style.width = '0%';
	progressText.textContent = '0%';
}