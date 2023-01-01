// ************************ Drag and drop ***************** //
let dropArea = document.getElementById("drop-area")

// Prevent default drag behaviors
;['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, preventDefaults, false)
    document.body.addEventListener(eventName, preventDefaults, false)
})

// Highlight drop area when item is dragged over it
;['dragenter', 'dragover'].forEach(eventName => {
    dropArea.addEventListener(eventName, highlight, false)
})

;['dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, unhighlight, false)
})

// Handle dropped files
dropArea.addEventListener('drop', handleDrop, false)

function preventDefaults (e) {
    e.preventDefault()
    e.stopPropagation()
}

function highlight(e) {
    dropArea.classList.add('highlight')
}

function unhighlight(e) {
    dropArea.classList.remove('active')
}

function handleDrop(e) {
    var dt = e.dataTransfer
    var files = dt.files

    console.log("e: ");
    console.log(e);

    console.log("dt: ");
    console.log(dt);

    console.log(dt.getData("text/plain"));
    console.log(dt.getData("text/x-moz-url"));
    console.log(dt.getData("text/html"));

    console.log("files: ");
    console.log(files);

    let reader = new FileReader()
    console.log(reader);

    handleFiles(files)
}

let uploadProgress = []
let progressBar = document.getElementById('progress-bar')

function initializeProgress(numFiles) {
    progressBar.value = 0
    uploadProgress = []

    for(let i = numFiles; i > 0; i--) {
        uploadProgress.push(0)
    }
}

function updateProgress(fileNumber, percent) {
    uploadProgress[fileNumber] = percent
    let total = uploadProgress.reduce((tot, curr) => tot + curr, 0) / uploadProgress.length
    progressBar.value = total
}

function handleFiles(files) {
    files = [...files]
    initializeProgress(files.length)
    files.forEach(uploadFile)
    files.forEach(previewFile)
}

// @TODO Add preview from website url added. A screenshot from Puppeteer.
// @TODO Preview for video and audio too. Need to add support of tracking where the user is at at playing the audio or the video.
/**
 *
 * @param file
 */
function previewFile(file) {
    let reader = new FileReader()

    console.log(reader);

    reader.readAsDataURL(file)
    reader.onloadend = function() {
        let img = document.createElement('img')
        console.log("reader.result");
        console.log(reader.result);
        img.src = reader.result
        document.getElementById('gallery').appendChild(img)
    }
}

/**
 *
 * @param file
 * @param i
 */
function uploadFile(file, i) {
    const basedUrl = 'http://localhost:8080';
    const sliceUploadPath = '/sliceUpload';
    const basicUploadPath = '/upload';

    if (window.FileReader) {
        let fileReader = new FileReader();
        //fileReader.
    }

    let xhr = new XMLHttpRequest();
    let formData = new FormData();

    xhr.open('POST', url, true);
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

    // For slice upload
    xhr.setRequestHeader('Cache-Control', 'no-cache');
    xhr.setRequestHeader('X-File-Name', file.name);
    xhr.setRequestHeader('X-File-Size', file.size);
    xhr.setRequestHeader('X-File-Type', ""); // @TODO Try to get the file type with javascript

    // Update progress (can be used to show progress indicator)
    xhr.upload.addEventListener("progress", function(e) {
        updateProgress(i, (e.loaded * 100.0 / e.total) || 100)
    })

    xhr.addEventListener('readystatechange', function(e) {
        if (xhr.readyState === 4 && xhr.status === 200) {
            updateProgress(i, 100) // <- Add this
        }
        else if (xhr.readyState === 4 && xhr.status !== 200) {
            // Error. Inform the user
        }
    })

    formData.append('upload_preset', 'ujpu6gyk')
    formData.append('file', file)
    xhr.send(formData)
}

/**
 * Format file size.
 * @param {Number} bytes
 */
const formatSize = function(bytes) {
    let str = ['bytes', 'kb', 'MB', 'GB', 'TB', 'PB'];
    let num = Math.floor(Math.log(bytes) / Math.log(1024));
    return bytes === 0 ? 0 : (bytes / Math.pow(1024, Math.floor(num))).toFixed(1) + ' ' + str[num];
}

/**
 *
 * @param file
 * @returns {boolean}
 */
const hasBlobSliceSupport = function(file) {
    // file.slice is not supported by FF3.6 and is prefixed in FF5 now
    return ('slice' in file || 'mozSlice' in file);
}