/**
 * 파일 업로드
 *
 * fileUploader.createDropZone(param) 호출로 사용한다.
 * param 타입은 object 다.
 *
 * @param param.extra 추가로 사용할 매개변수
 *        - param.extra.ownId 파일소유자
 *        - param.extra.editor true, false 에 따라 업로드 기능을 영역을 컨트롤한다. (false 일 때 hide)
 *        - param.extra.clickable 파일 추가 버튼을 사용할 element class name
 */
const portalFileUploader = (function () {
    "use strict";

    let extraParam, fileAttrName, delFileAttrName, dragAndDropZoneId,
        addFileBtnWrapClassName;

    const createUid = function () {
        function s4() {
            return ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
        }
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
    }

    const getExtension = function(fileName) {
        let dot = fileName.lastIndexOf('.')
        return fileName.substring(dot+1, fileName.length).toLowerCase()
    }

    const setFileIcon = function (fileName, isView) {
        if (isView) {
            return 'url("/assets/media/icons/dropzone/icon_document_' + getExtension(fileName) + '_s.svg")';
        } else {
            return 'url("/assets/media/icons/dropzone/icon_document_' + getExtension(fileName) + '.svg")';
        }
    }

    /**
     * 호출하는 곳에서 전달하는 파라미터를 내부에서 사용할 수 있도록 셋업한다.
     * @param param
     */
    const setExtraParam = function (param) {
        delFileAttrName = 'delFileSeq'; // 서버로 전달하여 삭제할 fileSeq input hidden 의 속성 이름
        fileAttrName = 'fileSeq';       // 서버로 전달하여 업로드 할 fileSeq input hidden 의 속성 이름
        dragAndDropZoneId = 'dropZoneFileUpload'; // 드래그앤드랍으로 업로드가 가능한 구역
        addFileBtnWrapClassName = 'add-file-button-wrap'; // 업로드 버튼 클릭 구역 wrapper

        extraParam = param;
        if (extraParam.dropZoneMaxFileSize === undefined) {
            extraParam.dropZoneMaxFileSize = 100;
        }

        if (extraParam.dropZoneFilesId === undefined) {
            extraParam.dropZoneFilesId = 'dropZoneFiles';
        }

        if (extraParam.dropZoneUploadedFilesId === undefined) {
            extraParam.dropZoneUploadedFilesId = 'dropZoneUploadedFiles';
        }

        if (extraParam.dropZoneUrl === undefined) {
            extraParam.dropZoneUrl = '/fileupload'
        }

        if (extraParam.dropZoneMaxFiles === undefined) {
            extraParam.dropZoneMaxFiles = null;
        }

        if (extraParam.clickable === undefined) {
            extraParam.clickable = 'add-file-button';
        }

        if (extraParam.acceptedFiles === undefined) {
            extraParam.acceptedFiles = null;
        }

        extraParam.type = (extraParam.type === undefined) ? 'dropzone':'dropzone ' + extraParam.type
        if (extraParam.type === 'avatar') {
            extraParam.enableImageThumbnails = true;
        }

        if (extraParam.editor === undefined) {
            extraParam.editor = true;
        }

        if (extraParam.enableImageThumbnails === undefined) {
            extraParam.enableImageThumbnails = false;
        }

        if (extraParam.dictDefaultMessage === undefined) {
            extraParam.dictDefaultMessage = i18n.msg('file.msg.upload');
        }

        if (extraParam.clickableLineMessage === undefined) {
            extraParam.clickableLineMessage = i18n.msg('file.label.or') + ' ';
        }

        if (extraParam.clickableMessage === undefined) {
            extraParam.clickableMessage = i18n.msg('file.msg.browser');
        }

        if (extraParam.isView === undefined) {
            extraParam.isView = false;
        }
        // dropzone 영역이 아래에 나오게 하고싶은 경우
        if (extraParam.isDropzoneUnder === undefined) {
            extraParam.isDropzoneUnder = false;
        }
    };


    /**
     * 파일을 드래그앤드랍, 클릭으로 업로드하는 영역을 정의한다.
     * @param dropZoneFiles
     */
    const createDragAndDropZone = function (dropZoneFiles) {
        const dragAndDropZone = document.createElement('div');
        dragAndDropZone.id = dragAndDropZoneId;
        dragAndDropZone.className = extraParam.type;
        if (extraParam.isView) {
            dragAndDropZone.classList.add('view');
        }
        dropZoneFiles.appendChild(dragAndDropZone);

        // 파일을 업로드하기 위한 별도의 버튼 기능을 정의하고 추가
        const justText = document.createElement('span');
        justText.textContent = extraParam.clickableLineMessage;

        const addFileBtn = document.createElement('span');
        addFileBtn.className = extraParam.clickable;
        addFileBtn.textContent = extraParam.clickableMessage;

        const addFileBtnWrap = document.createElement('div');
        addFileBtnWrap.className = addFileBtnWrapClassName
        addFileBtnWrap.appendChild(justText);
        addFileBtnWrap.appendChild(addFileBtn);
        dragAndDropZone.appendChild(addFileBtnWrap);
    }

    /**
     * 업로드 영역의 dropzone template 을 생성하여 리턴한다.
     * @returns {string}
     */
    const createTemplate = function () {
        // 파일 템플릿 생성
        const thumbnailData = document.createElement('img');
        thumbnailData.dataset.dzThumbnail = '';
        const filenameData = document.createElement('span');
        filenameData.dataset.dzName = '';
        const sizeData = document.createElement('span');
        sizeData.dataset.dzSize = '';
        const errorMsgData = document.createElement('span');
        errorMsgData.dataset.dzErrormessage = '';
        const progressData = document.createElement('span');
        progressData.dataset.dzUploadprogress = '';
        progressData.className = 'dz-upload';

        const fileType = document.createElement('div');
        fileType.className = 'dz-file-type';
        const remove = document.createElement('div');
        remove.className = 'dz-remove';
        remove.dataset.dzRemove = '';
        const filename = document.createElement('div');
        filename.className = 'dz-filename';
        filename.appendChild(filenameData);
        const size = document.createElement('div');
        size.className = 'dz-size';
        size.appendChild(sizeData);

        const detail = document.createElement('div');
        detail.className = 'dz-details';
        detail.appendChild(fileType);
        detail.appendChild(filename);
        detail.appendChild(size);
        detail.appendChild(remove);

        const thumbnail = document.createElement('div');
        thumbnail.className = 'dz-image';
        thumbnail.appendChild(thumbnailData);
        const progress = document.createElement('div');
        progress.className = 'dz-progress';
        progress.appendChild(progressData);
        const errorMsg = document.createElement('div');
        errorMsg.className = 'dz-error-message';
        errorMsg.appendChild(errorMsgData);
        const successMark = document.createElement('div');
        successMark.className = 'dz-success-mark';
        const successMarkSpan = document.createElement('span');
        successMarkSpan.innerText = 'SUCCESS';
        successMark.appendChild(successMarkSpan);
        const errorMark = document.createElement('div');
        errorMark.className = 'dz-error-mark';
        const errorMarkSpan = document.createElement('span');
        errorMarkSpan.innerText = 'FAILED';
        errorMark.appendChild(errorMarkSpan);

        const fileViewTemplate = document.createElement('div');
        fileViewTemplate.setAttribute('id', 'fileTemplate');
        fileViewTemplate.setAttribute('name', 'fileTemplate');
        fileViewTemplate.className = 'dz-preview dz-file-preview';
        fileViewTemplate.appendChild(thumbnail);
        fileViewTemplate.appendChild(detail);
        fileViewTemplate.appendChild(progress);
        fileViewTemplate.appendChild(errorMsg);
        fileViewTemplate.appendChild(successMark);
        fileViewTemplate.appendChild(errorMark);
        const fileView = document.createElement('div');
        fileView.appendChild(fileViewTemplate);

        return fileView.innerHTML
    }

    /**
     * 업로드된 파일 다운로드 이벤트 핸들러
     */
    const fileDownloadHandler = function(e) {
        let $this = e.target;
        if (!$this.parentElement.classList.contains('dz-details')) {
            $this = $this.parentElement;
        }
        const fileDownOpt = {
            method: 'get',
            url: '/rest/portal/filedownload?seq=' + Number($this.parentElement.querySelector('input[name=loadedFileSeq]').value),
            callbackFunc: function (xhr) {
                const a = document.createElement('a');
                const url = window.URL.createObjectURL(xhr.response);
                a.href = url;
                a.download = $this.parentElement.querySelector('div[name=loadedFileNames] span').textContent;
                document.body.append(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            },
            params: '',
            async: true,
            responseType: 'blob'
        }
        aliceJs.sendXhr(fileDownOpt);
    }

    /**
     * 파일업로드 드랍존 생성
     */
    const createFileUploader = function () {
        // 드랍존 영역 가져오기.
        const dropZoneFiles = document.getElementById(extraParam.dropZoneFilesId);
        const dropZoneUploadedFiles = document.getElementById(extraParam.dropZoneUploadedFilesId);

        createDragAndDropZone(dropZoneFiles);

        // 파일 업로드 기능 정의
        let dropzoneId = '#'+extraParam.dropZoneFilesId+' #' + dragAndDropZoneId;
        const myDropZone = new Dropzone(dropzoneId, {
            paramName: "file", // file 매개변수명
            params: extraParam || null, // 추가 매개변수
            maxFilesize: extraParam.dropZoneMaxFileSize, // 첨부파일 용량 제한
            url: extraParam.dropZoneUrl,
            maxThumbnailFilesize: 10, // MB, 썸네일 생성 최소 기준값, 초과시 썸네일 생성 안함
            maxFiles: extraParam.dropZoneMaxFiles, // 첨부파일 개수 제한
            autoProcessQueue: true, //자동업로드, processQueue() 사용
            addRemoveLinks: false,
            acceptedFiles: extraParam.acceptedFiles,
            previewTemplate: createTemplate(), // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // Make sure the files aren't queued until manually added
            clickable: '.' + extraParam.clickable, // Define the element that should be used as click trigger to select files.
            createImageThumbnails: false,
            dictDefaultMessage: extraParam.dictDefaultMessage,
            init: function () { // 드랍존 초기화시 사용할 이벤트 리스너 등록
                let _this = this;
                // 등록된 파일이 있으면 조회.
                const opt = {
                    method: 'get',
                    url: '/rest/portal/filelist?ownId=' + ((extraParam.hasOwnProperty('ownId')) ? extraParam.ownId : '')
                                  +'&fileDataId='+((extraParam.hasOwnProperty('fileDataIds')) ? extraParam.fileDataIds : ''),
                    callbackFunc: function (response) {
                        const files = JSON.parse(response.responseText);
                        files.forEach(function (fileMap) {
                            let file = fileMap.fileLocDto;
                            const fileBytes = file.fileSize;
                            const logValueDigit = 1024;
                            const unit = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
                            const fileSizeLogValue = Math.floor(Math.log(fileBytes) / Math.log(logValueDigit));
                            let convertedFileSize;
                            if (fileSizeLogValue === Number.NEGATIVE_INFINITY) {
                                convertedFileSize = "0 " + unit[0];
                            } else {
                                convertedFileSize = (fileBytes / Math.pow(logValueDigit, Math.floor(fileSizeLogValue))).toFixed(2) + " " + unit[fileSizeLogValue];
                            }

                            // 파일 목록 생성
                            const fileType = document.createElement('div');
                            fileType.className = 'dz-file-type';
                            fileType.style.backgroundImage = setFileIcon(file.originName, extraParam.isView);

                            const fileName = document.createElement('div');
                            fileName.className = 'dz-filename';
                            fileName.setAttribute('name', 'loadedFileNames');
                            const fileNameStr = document.createElement('span');
                            fileNameStr.textContent = file.originName;
                            fileName.appendChild(fileNameStr);
                            const fileSize = document.createElement('div');
                            fileSize.className = 'dz-size';
                            fileSize.setAttribute('name', 'loadedFileSize');
                            const fileSizeStr = document.createElement('span');
                            fileSizeStr.textContent = convertedFileSize;
                            fileSize.appendChild(fileSizeStr);
                            const remove = document.createElement('div');
                            remove.className = 'dz-remove';
                            const fileSeq = document.createElement('input');
                            fileSeq.setAttribute('type', 'hidden');
                            fileSeq.setAttribute('name', 'loadedFileSeq');
                            fileSeq.value = file.fileSeq;
                            const fileDetails = document.createElement('div');
                            fileDetails.className = 'dz-details';
                            fileDetails.append(fileType);
                            fileDetails.append(fileName);
                            fileDetails.append(fileSize);
                            fileDetails.append(remove);
                            fileDetails.append(fileSeq);
                            const uploadedFileView = document.createElement('div');
                            uploadedFileView.className = 'dz-preview dz-file-preview';
                            uploadedFileView.appendChild(fileDetails);

                            if (extraParam.isView) { // view 일때
                                fileName.style.cursor = 'pointer';
                                //파일 다운로드
                                fileName.addEventListener('click', fileDownloadHandler);

                                dropZoneUploadedFiles.className = 'dropzone dz-uploaded';
                                dropZoneUploadedFiles.appendChild(uploadedFileView);

                            } else { // edit 일때
                                document.querySelector(dropzoneId).appendChild(uploadedFileView);

                                // 파일 다운로드
                                download.addEventListener('click', fileDownloadHandler);

                                // 파일삭제 : 첨부파일 목록에서 제외, 삭제 flag 추가
                                remove.addEventListener('click', function (e) {
                                    const delFile = this.parentElement.querySelector('input[name=loadedFileSeq]');
                                    delFile.setAttribute('name', delFileAttrName);
                                    delFile.closest('.dz-preview').style.display = 'none';
                                });
                            }
                        });
                    },
                    params: '',
                    async: false
                };
                aliceJs.sendXhr(opt);

                if (extraParam.editor) {
                    const addFileBtn = _this.element.querySelector('.' + addFileBtnWrapClassName);
                    _this.element.querySelector('.dz-message').appendChild(addFileBtn);

                    //파일 확장자 목록 관련 출력
                    let fileNameExtensionList;
                    const opt2 = {
                        method: 'GET',
                        url: '/rest/portal/fileNameExtensionList',
                        callbackFunc: function (response) {
                            fileNameExtensionList = JSON.parse(response.responseText);
                        }
                    };
                    aliceJs.sendXhr(opt2);

                    //파일접근시 사용.
                    //all accepted files: .getAcceptedFiles()
                    //all rejected files: .getRejectedFiles()
                    //all queued files: .getQueuedFiles()
                    //all uploading files: .getUploadingFiles()

                    this.on("addedfile", function (file) {
                        const dropzoneMessage = _this.element.querySelector('.dz-message');
                        if (extraParam.isDropzoneUnder) {
                            dropzoneMessage.style.display = 'none';
                        }
                        let fileName = file.name;
                        let fileNameLength = file.name.length;
                        let lastDot = fileName.lastIndexOf('.');
                        file.previewElement.querySelector('.dz-file-type').style.backgroundImage = setFileIcon(fileName, extraParam.isView);

                        let extensionValueArr = [];
                        for (let i = 0; i < fileNameExtensionList.length; i++)  {
                            extensionValueArr[i] = fileNameExtensionList[i].fileNameExtension;
                        }

                        if (!(extensionValueArr.includes(getExtension(fileName).toUpperCase()))) {
                            this.removeFile(file);
                            if (extraParam.isDropzoneUnder) {
                                dropzoneMessage.style.display = 'block';
                            }
                            aliceJs.alertWarning(i18n.get('fileupload.msg.extensionNotAvailable'));
                        }
                    });

                    this.on("removedfile", function (file) {
                    });

                    this.on("sending", function (file, xhr, formData) {
                        // Show the total progress bar when upload starts
                        //document.querySelector("#total-progress").style.opacity = "1";
                        // And disable the start button
                        //file.previewElement.querySelector(".start").setAttribute("disabled", "disabled");
                    });

                    // Update the total progress bar
                    this.on("totaluploadprogress", function (progress) {
                        //document.querySelector("#total-progress .progress-bar").style.width = progress + "%";
                    });

                    this.on("success", function (file, response) {
                        const seq = document.createElement('input');
                        seq.setAttribute('type', 'hidden');
                        seq.setAttribute('name', fileAttrName);
                        seq.value = response.file.fileSeq;
                        file.previewElement.appendChild(seq);
                    });

                    this.on("error", function (file, errorMsg, xhr) {
                        const res = JSON.parse(xhr.response);
                        file.previewElement.querySelector('.dz-error-message').innerText = res.message;
                        // file.previewElement.querySelector('.dz-success-mark').style.display = '';
                        // file.previewElement.querySelector('.dz-success-mark').style.display = 'none';
                        // aliceJs.xhrErrorResponse()
                        // file.previewElement.querySelector('.dz-error-message').addClass("dz-error");
                    });

                    this.on("complete", function (file) {
                        if (extraParam.isDropzoneUnder) {
                            const dropzoneMessage = _this.element.querySelector('.dz-message');
                            document.getElementById(dragAndDropZoneId).appendChild(dropzoneMessage);
                            dropzoneMessage.style.display = 'block';
                        }
                    });

                    // Hide the total progress bar when nothing's uploading anymore
                    this.on("queuecomplete", function (progress) {
                        //document.querySelector("#total-progress").style.opacity = "0";
                    });

                    this.on("canceled", function () {
                    });

                    this.on("maxfilesexceeded", function (file, maxFiles) {
                        this.removeFile(file);
                        aliceJs.alertWarning(i18n.get('fileupload.msg.maxFileCount', maxFiles));
                    });

                    this.on("maxfilesizeexceeded", function (file, maxFileSize) {
                        this.removeFile(file);
                        aliceJs.alertWarning(i18n.get('fileupload.msg.maxFileSize', maxFileSize));
                    });
                } else {
                    dropZoneFiles.remove();
                    dropZoneUploadedFiles.querySelectorAll('.dz-remove').forEach(element => {
                        element.style.setProperty('background-image', 'none');
                    });
                }
            },
            accept: function (file, done) { // done 함수 호출시 인수없이 호출해야 정상 업로드 진행
                if (file.name === "justinbieber.jpg") {
                    done("Naha, you don't.");
                } else {
                    done();
                }
            }
        });
    };

    /**
     * 아바타 파일업로드를 위한 드랍존 생성.
     */
    const createAvatarUploader = function () {
        // 드랍존 영역 가져오기.
        const dropZoneFiles = document.getElementById(extraParam.dropZoneFilesId);
        const dropZoneUploadedFiles = document.getElementById(extraParam.dropZoneUploadedFilesId);

        createDragAndDropZone(dropZoneFiles);

        // 파일 업로드 기능 정의
        let dropzoneId = '#'+extraParam.dropZoneFilesId+' #' + dragAndDropZoneId;
        const myDropZone = new Dropzone(dropzoneId, {
            paramName: "file", // file 매개변수명
            params: extraParam || null, // 추가 매개변수
            maxFilesize: extraParam.dropZoneMaxFileSize, // 첨부파일 용량 제한
            url: extraParam.dropZoneUrl,
            maxThumbnailFilesize: 10, // MB, 썸네일 생성 최소 기준값, 초과시 썸네일 생성 안함
            maxFiles: extraParam.dropZoneMaxFiles, // 첨부파일 개수 제한
            autoProcessQueue: true, //자동업로드, processQueue() 사용
            addRemoveLinks: false,
            acceptedFiles: extraParam.acceptedFiles,
            previewTemplate: createTemplate(), // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // Make sure the files aren't queued until manually added
            clickable: '.' + extraParam.clickable, // Define the element that should be used as click trigger to select files.
            createImageThumbnails: true,
            thumbnailWidth: extraParam.thumbnailWidth,
            thumbnailHeight: extraParam.thumbnailHeight,
            dictDefaultMessage: extraParam.dictDefaultMessage,
            headers: {
                'X-CSRF-Token': document.querySelector('meta[name="_csrf"]').getAttribute("content")
            },
            init: function () { // 드랍존 초기화시 사용할 이벤트 리스너 등록
                let _this = this;

                const addFileBtn = document.querySelector('.' + addFileBtnWrapClassName);
                document.querySelector('.dz-message').appendChild(addFileBtn);

                if (extraParam.avatar.id !== null && extraParam.avatar.size > 0) {
                    let mockFile = {
                        id: extraParam.avatar.id,
                        name: extraParam.avatar.value,
                        size: extraParam.avatar.size,
                        dataURL: extraParam.avatar.path,
                        status: Dropzone.ADDED,
                        accepted: true,
                        isNew: false
                    };
                    extraParam.fileName = extraParam.avatar.id
                    document.getElementById('avatarUUID').value = extraParam.fileName;
                    _this.files.push(mockFile);
                    _this.emit("addedfile", mockFile);
                    _this.createThumbnailFromUrl(mockFile,
                        _this.options.thumbnailWidth,
                        _this.options.thumbnailHeight,
                        _this.options.thumbnailMethod, true, function (thumbnail) {
                            _this.emit('thumbnail', mockFile, thumbnail);
                        });
                    _this.emit('complete', mockFile);
                }

                //파일 확장자 목록 관련 출력
                let fileNameExtensionList;
                const opt2 = {
                    method: 'GET',
                    url: '/rest/portal/fileNameExtensionList',
                    callbackFunc: function (response) {
                        fileNameExtensionList = JSON.parse(response.responseText);
                    }
                };
                aliceJs.sendXhr(opt2);

                this.on("addedfile", function (file) {
                    extraParam.fileName = createUid();
                    document.getElementById('avatarUUID').value = extraParam.fileName;

                    let fileName = file.name;
                    let fileNameLength = file.name.length;
                    let lastDot = fileName.lastIndexOf('.');
                    let extensionValueArr = [];

                    for (var i = 0; i < fileNameExtensionList.length; i++)  {
                        extensionValueArr[i] = fileNameExtensionList[i].fileNameExtension;
                    }

                    if (!(extensionValueArr.includes(getExtension(fileName)))) {
                        this.removeFile(file);
                        aliceJs.alertWarning(i18n.get('fileupload.msg.extensionNotAvailable'));
                    }

                });

                this.on("removedfile", function (file) {
                    extraParam.fileName = '';
                    document.getElementById('avatarUUID').value = extraParam.fileName;
                });

                this.on("sending", function (file, xhr, formData) {
                    // Show the total progress bar when upload starts
                    //document.querySelector("#total-progress").style.opacity = "1";
                    // And disable the start button
                    //file.previewElement.querySelector(".start").setAttribute("disabled", "disabled");
                });

                // Update the total progress bar
                this.on("totaluploadprogress", function (progress) {
                    //document.querySelector("#total-progress .progress-bar").style.width = progress + "%";
                });

                this.on("success", function (file, response) {
                    const seq = document.createElement('input');
                    seq.setAttribute('type', 'hidden');
                    seq.setAttribute('name', fileAttrName);
                    seq.value = response.file.fileSeq;
                    file.previewElement.appendChild(seq);
                });

                this.on("error", function (file, errorMsg, xhr) {
                    const res = JSON.parse(xhr.response);
                    file.previewElement.querySelector('.dz-error-message').innerText = res.message;
                    // file.previewElement.querySelector('.dz-success-mark').style.display = '';
                    // file.previewElement.querySelector('.dz-success-mark').style.display = 'none';
                    // aliceJs.xhrErrorResponse()
                    // file.previewElement.querySelector('.dz-error-message').addClass("dz-error");
                });

                this.on("complete", function (file) {
                    // const dropzoneMessage = document.querySelector('.dz-message')
                    // document.getElementById(dragAndDropZoneId).appendChild(dropzoneMessage);
                    // dropzoneMessage.style.display = 'block';
                });

                // Hide the total progress bar when nothing's uploading anymore
                this.on("queuecomplete", function (progress) {
                    //document.querySelector("#total-progress").style.opacity = "0";
                });

                this.on("canceled", function () {
                });

                this.on("maxfilesexceeded", function (file, maxFiles) {
                    this.removeFile(file);
                    aliceJs.alertWarning(i18n.get('fileupload.msg.maxFileCount', maxFiles));
                });

                this.on("maxfilesizeexceeded", function (file, maxFileSize) {
                    this.removeFile(file);
                    aliceJs.alertWarning(i18n.get('fileupload.msg.maxFileSize', maxFileSize));
                });
            },
            accept: function (file, done) { // done 함수 호출시 인수없이 호출해야 정상 업로드 진행
                if (file.name === "justinbieber.jpg") {
                    done("Naha, you don't.");
                } else {
                    done();
                }
            }
        });
    };

    return {
        init: function (param) {
            setExtraParam(param.extra);
            createFileUploader();
        },
        avatar: function (param) {
            setExtraParam(param.extra);
            createAvatarUploader();
        }
    }
}());
