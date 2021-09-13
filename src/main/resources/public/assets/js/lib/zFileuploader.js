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
const zFileUploader = (function () {
    'use strict';

    let extraParam,
        fileAttrName,
        delFileAttrName,
        dragAndDropZoneId,
        addFileBtnWrapClassName,
        exportFile;

    //외부로 file 정보를 내보냄.
    const getFile = function() {
        return exportFile;
    };

    const createUid = function () {
        function s4() {
            return ((1 + Math.random()) * 0x10000 | 0).toString(16).substring(1);
        }
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
    };

    const getExtension = function(fileName) {
        let dot = fileName.lastIndexOf('.');
        return fileName.substring(dot+1, fileName.length).toLowerCase();
    };

    const setFileIcon = function (fileName) {
        return '/assets/media/icons/fileUploader/icon_document_' + getExtension(fileName) + '.svg';
    };

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
            extraParam.dropZoneUrl = '/fileupload';
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

        extraParam.type = (extraParam.type === undefined) ? 'dropzone':'dropzone ' + extraParam.type;
        if (extraParam.type === 'avatar') {
            extraParam.enableImageThumbnails = true;
        }
        // edit 모드일때
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
        // view 모드일때
        if (extraParam.isView === undefined) {
            extraParam.isView = false;
        }
        // 폼 디자이너, 신청서인지 여부
        if (extraParam.isForm === undefined) {
            extraParam.isForm = false;
        }

        if (extraParam.defaultUrl === undefined) {
            extraParam.defaultUrl = '';
        }

        // 헤더 추가 여부
        if (extraParam.isHeaders === undefined) {
            extraParam.isHeaders = true;
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
        dropZoneFiles.appendChild(dragAndDropZone);

        // 파일을 업로드하기 위한 별도의 버튼 기능을 정의하고 추가
        const justText = document.createElement('span');
        justText.textContent = extraParam.clickableLineMessage;

        const addFileBtn = document.createElement('span');
        addFileBtn.classList.add('underline');
        if (typeof extraParam.clickable !== 'boolean') {
            addFileBtn.classList.add(extraParam.clickable);
        }
        addFileBtn.textContent = extraParam.clickableMessage;

        const addFileBtnWrap = document.createElement('div');
        addFileBtnWrap.className = addFileBtnWrapClassName;
        addFileBtnWrap.appendChild(justText);
        addFileBtnWrap.appendChild(addFileBtn);
        dragAndDropZone.appendChild(addFileBtnWrap);
    };

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

        const fileType = document.createElement('img');
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

        return fileView.innerHTML;
    };

    /**
     * 업로드된 파일 다운로드 이벤트 핸들러
     */
    const fileDownloadHandler = function(e) {
        let $this = e.target;
        if (!$this.parentElement.classList.contains('dz-details')) {
            $this = $this.parentElement;
        }
        aliceJs.fetchBlob(extraParam.defaultUrl + '/filedownload?seq=' +
            Number($this.parentElement.querySelector('input[name=loadedFileSeq]').value), {
            method: 'GET',
            showProgressbar: true
        }).then((blob) => {
            if (typeof blob === 'object') {
                const a = document.createElement('a');
                const url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = $this.parentElement.querySelector('div[name=loadedFileNames] span').textContent;
                document.body.append(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            } else {
                aliceAlert.alertWarning(i18n.msg('file.msg.noAttachFile'));
            }
        });
    };

    /**
     * 파일업로드 validation (파일확장자, 최대 파일 수, 최대 파일 사이즈)
     * dropzone : 현재 dropzone 객체
     * file : 첨부한 파일
     * uploaderType : 어느 uploader가 호출 했는지(fileUploader, avatarUploader)
     */
    const validation = async function(dropzone, file, uploaderType) {
        //파일 확장자 목록 관련 출력
        let extensionValueArr = [];
        // 수용 파일 확장자가 없다면 기본 파일 확장자 제한(DB)에서 확인 한다.
        if (extraParam.acceptedFiles === null) {
            const fileNameExtensionList = await aliceJs.fetchJson((extraParam.defaultUrl === '' ? '/rest' : extraParam.defaultUrl) + '/filenameextensions', {
                method: 'GET'
            });
            for (let i = 0; i < fileNameExtensionList.length; i++) {
                extensionValueArr[i] = fileNameExtensionList[i].fileNameExtension;
            }
        } else {
            let acceptedFiles = extraParam.acceptedFiles.split('.');
            for (let i = 0; i < acceptedFiles.length; i++) {
                extensionValueArr[i] = acceptedFiles[i].replace(',', '').trim().toUpperCase();
            }
        }
        if (!(extensionValueArr.includes(getExtension(file.name).toUpperCase()))) {
            dropzone.removeFile(file);
            if (extraParam.isDropzoneUnder) {
                dropzoneMessage.style.display = 'block';
            }
            aliceAlert.alertWarning(i18n.msg('fileupload.msg.extensionNotAvailable'));
        } else if (file.size > extraParam.dropZoneMaxFileSize * 1024 * 1024) {
            dropzone.removeFile(file);
            aliceAlert.alert(i18n.msg('fileupload.msg.maxFileSize', extraParam.dropZoneMaxFileSize));
        } else if (extraParam.dropZoneMaxFiles !== null && (dropzone.files.length > extraParam.dropZoneMaxFiles)) {
            if (uploaderType === 'avatarUploader' && extraParam.dropZoneMaxFiles === 1) {
                if (dropzone.files.length > 1) {
                    dropzone.removeFile(dropzone.files[0]);
                }
                extraParam.fileName = createUid();
                document.getElementById('avatarUUID').value = extraParam.fileName;
            } else {
                dropzone.removeFile(file);
                aliceAlert.alert(i18n.msg('fileupload.msg.maxFileCount', extraParam.dropZoneMaxFiles));
            }
        }
    };

    /**
     * 파일업로드 드랍존 생성
     */
    const createFileUploader = function () {
        // 드랍존 영역 가져오기.
        const dropZoneFiles = document.getElementById(extraParam.dropZoneFilesId);
        const dropZoneUploadedFiles = document.getElementById(extraParam.dropZoneUploadedFilesId);

        createDragAndDropZone(dropZoneFiles);

        // 파일 업로드 기능 정의
        const dropzoneId = '#'+extraParam.dropZoneFilesId+' #' + dragAndDropZoneId;
        const myDropZone = new Dropzone(dropzoneId, {
            paramName: 'file', // file 매개변수명
            params: extraParam || null, // 추가 매개변수
            maxFilesize: extraParam.dropZoneMaxFileSize, // 첨부파일 용량 제한
            url: extraParam.dropZoneUrl,
            maxThumbnailFilesize: 10, // MB, 썸네일 생성 최소 기준값, 초과시 썸네일 생성 안함
            maxFiles: extraParam.dropZoneMaxFiles, // 첨부파일 개수 제한
            autoProcessQueue: true, //자동업로드, processQueue() 사용
            addRemoveLinks: false,
            acceptedFiles: extraParam.acceptedFiles,
            previewTemplate: createTemplate(), // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // 직접 파일을 추가 할 때까지 대기열에 남은 파일이 있는지 확인
            clickable: extraParam.clickable ? '.' + extraParam.clickable : extraParam.clickable, // 파일첨부 클릭 트리거 정의
            createImageThumbnails: false,
            dictDefaultMessage: extraParam.dictDefaultMessage,
            headers: extraParam.isHeaders ? {
                'X-CSRF-Token': document.querySelector('meta[name="_csrf"]').getAttribute('content')
            } : null,
            init: function () { // 드랍존 초기화시 사용할 이벤트 리스너 등록
                let _this = this;
                // 등록된 파일이 있으면 조회.
                aliceJs.fetchJson(extraParam.defaultUrl + '/filelist?ownId=' + ((extraParam.hasOwnProperty('ownId')) ? extraParam.ownId : '')
                    +'&fileDataId='+((extraParam.hasOwnProperty('fileDataIds')) ? extraParam.fileDataIds : ''), {
                    method: 'GET'
                }).then((files) => {
                    _this.isFileExist = (files.length > 0);
                    // 파일이 존재하지 않으면
                    if (!_this.isFileExist && extraParam.isView) {
                        const noFileStr = extraParam.isForm ? document.createElement('input') : document.createElement('span');
                        noFileStr.className = 'text-no-file text-ellipsis';
                        // form-designer 또는 신청서일 때
                        if (extraParam.isForm) {
                            noFileStr.placeholder = i18n.msg('file.msg.noAttachFile');
                            noFileStr.disabled = true;
                        } else {
                            noFileStr.textContent = i18n.msg('file.msg.noAttachFile');
                        }
                        dropZoneUploadedFiles.appendChild(noFileStr);
                    }

                    files.forEach(function (fileMap) {
                        let file = fileMap.fileLocDto;
                        const fileBytes = file.fileSize;
                        const logValueDigit = 1024;
                        const unit = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
                        const fileSizeLogValue = Math.floor(Math.log(fileBytes) / Math.log(logValueDigit));
                        let convertedFileSize;
                        if (fileSizeLogValue === Number.NEGATIVE_INFINITY) {
                            convertedFileSize = '0 ' + unit[0];
                        } else {
                            convertedFileSize = (fileBytes / Math.pow(logValueDigit, Math.floor(fileSizeLogValue))).toFixed(2) + ' ' + unit[fileSizeLogValue];
                        }

                        // 파일 목록 생성
                        const fileType = document.createElement('img');
                        fileType.className = 'dz-file-type';
                        fileType.src = setFileIcon(file.originName, extraParam.isView);

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

                        // 삭제
                        const remove = document.createElement('div');
                        remove.className = 'dz-remove';
                        const removeIcon = document.createElement('span');
                        removeIcon.className = 'z-icon i-delete';
                        remove.appendChild(removeIcon);

                        // 다운로드
                        const download = document.createElement('div');
                        download.className = 'dz-download';
                        const downloadIcon = document.createElement('span');
                        downloadIcon.className = 'z-icon i-download';
                        download.appendChild(downloadIcon);

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
                        fileDetails.append(download);
                        fileDetails.append(fileSeq);

                        const uploadedFileView = document.createElement('div');
                        uploadedFileView.className = 'dz-preview dz-file-preview';
                        uploadedFileView.appendChild(fileDetails);

                        if (extraParam.isView) { // view 일때
                            fileName.style.cursor = 'pointer';
                            // 파일 다운로드
                            fileName.addEventListener('click', fileDownloadHandler);

                            dropZoneUploadedFiles.className = 'dropzone dz-uploaded';
                            dropZoneUploadedFiles.appendChild(uploadedFileView);

                            // 아이콘 제거
                            download.remove();
                            remove.remove();

                        } else { // edit 일때
                            document.querySelector(dropzoneId).appendChild(uploadedFileView);

                            // 파일 다운로드
                            download.addEventListener('click', fileDownloadHandler);

                            // 파일삭제 : 첨부파일 목록에서 제외, 삭제 flag 추가
                            remove.addEventListener('click', function (e) {
                                const delFile = this.parentElement.querySelector('input[name=loadedFileSeq]');
                                delFile.setAttribute('name', delFileAttrName);

                                const delFilePreview = delFile.closest('.dz-preview');
                                delFilePreview.style.display = 'none';

                                // 파일이 하나도 없다면 아이콘을 보여준다.
                                let previewList = delFilePreview.parentNode.querySelectorAll('.dz-preview:not([style*="display:none"]):not([style*="display: none"])');
                                if (previewList.length === 0) {
                                    delFilePreview.parentNode.querySelector('.i-upload').style.display = 'block';
                                    _this.isFileExist = false;
                                }

                                if (typeof _this.options.params.userCallback === 'function') {
                                    _this.options.params.userCallback();
                                }
                            });
                        }
                    });
                });

                if (extraParam.editor) {
                    const dropzoneMessage = _this.element.querySelector('.dz-message');
                    // 아이콘 추가
                    const dropzoneIcon = document.createElement('span');
                    dropzoneIcon.className = 'z-icon i-upload';
                    if (_this.isFileExist) {
                        dropzoneIcon.style.display = 'none';
                    }
                    dropzoneMessage.insertBefore(dropzoneIcon, dropzoneMessage.firstChild);
                    // browse 버튼 추가
                    const addFileBtn = _this.element.querySelector('.' + addFileBtnWrapClassName);
                    dropzoneMessage.appendChild(addFileBtn);

                    this.on('addedfile', function (file) {
                        const dropzoneMessage = _this.element.querySelector('.dz-message');
                        if (extraParam.isDropzoneUnder) {
                            dropzoneMessage.style.display = 'none';
                        }
                        // 파일 추가시 아이콘 숨기기
                        if (!_this.isFileExist) {
                            dropzoneMessage.querySelector('.i-upload').style.display = 'none';
                            _this.isFileExist = true;
                        }
                        file.previewElement.querySelector('.dz-file-type').src = setFileIcon(file.name, extraParam.isView);
                        // 삭제 아이콘 추가
                        const removeIcon = document.createElement('span');
                        removeIcon.className = 'z-icon i-delete';
                        const removeButton = file.previewElement.querySelector('.dz-remove');
                        removeButton.style.gridColumn = '5';
                        removeButton.appendChild(removeIcon);

                        validation(this, file, 'fileUploader');
                        exportFile = file;
                    });

                    this.on('removedfile', function (file) {
                        let previewList = _this.element.querySelectorAll('.dz-preview:not([style*="display:none"]):not([style*="display: none"])');
                        if (_this.files.length === 0 && previewList.length === 0) {
                            const dropzoneMessage = _this.element.querySelector('.dz-message');
                            dropzoneMessage.querySelector('.i-upload').style.display = 'block';
                            _this.isFileExist = false;
                        }
                        if (typeof _this.options.params.userCallback === 'function') {
                            _this.options.params.userCallback();
                        }
                    });

                    this.on('success', function (file, response) {
                        const seq = document.createElement('input');
                        seq.setAttribute('type', 'hidden');
                        seq.setAttribute('name', fileAttrName);
                        if (response.file !== undefined) {
                            seq.value = response.file.fileSeq;
                            file.previewElement.appendChild(seq);
                        }
                        if (typeof _this.options.params.userCallback === 'function') {
                            _this.options.params.userCallback();
                        }
                    });

                    this.on('error', function (file, errorMsg, xhr) {
                        if (xhr !== undefined) {
                            const res = JSON.parse(xhr.response);
                            file.previewElement.querySelector('.dz-error-message').innerText = res.message;
                        }
                    });

                    this.on('complete', function (file) {
                        if (extraParam.isDropzoneUnder) {
                            const dropzoneMessage = _this.element.querySelector('.dz-message');
                            document.getElementById(dragAndDropZoneId).appendChild(dropzoneMessage);
                            dropzoneMessage.style.display = 'block';
                        }
                    });
                } else {
                    dropZoneFiles.remove();
                    dropZoneUploadedFiles.querySelectorAll('.dz-remove').forEach(element => {
                        element.style.setProperty('background-image', 'none');
                    });
                }
            },
            accept: function (file, done) { // done 함수 호출시 인수없이 호출해야 정상 업로드 진행
                done();
            }
        });
    };

    /**
     * 아바타 파일업로드를 위한 드랍존 생성.
     */
    const createAvatarUploader = function () {
        // 드랍존 영역 가져오기.
        const dropZoneFiles = document.getElementById(extraParam.dropZoneFilesId);

        createDragAndDropZone(dropZoneFiles);

        // 파일 업로드 기능 정의
        const dropzoneId = '#'+extraParam.dropZoneFilesId+' #' + dragAndDropZoneId;
        const myDropZone = new Dropzone(dropzoneId, {
            paramName: 'file', // file 매개변수명
            params: extraParam || null, // 추가 매개변수
            maxFilesize: extraParam.dropZoneMaxFileSize, // 첨부파일 용량 제한
            url: extraParam.dropZoneUrl,
            maxThumbnailFilesize: 10, // MB, 썸네일 생성 최소 기준값, 초과시 썸네일 생성 안함
            maxFiles: extraParam.dropZoneMaxFiles, // 첨부파일 개수 제한
            autoProcessQueue: true, //자동업로드, processQueue() 사용
            addRemoveLinks: false,
            acceptedFiles: extraParam.acceptedFiles,
            previewTemplate: createTemplate(), // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // 직접 파일을 추가 할 때까지 대기열에 남은 파일이 있는지 확인
            clickable: '.' + extraParam.clickable, // 파일첨부 클릭 트리거 정의
            createImageThumbnails: true,
            thumbnailWidth: extraParam.thumbnailWidth,
            thumbnailHeight: extraParam.thumbnailHeight,
            dictDefaultMessage: extraParam.dictDefaultMessage,
            headers: extraParam.isHeaders ? {
                'X-CSRF-Token': document.querySelector('meta[name="_csrf"]').getAttribute('content')
            } : null,
            init: function () { // 드랍존 초기화시 사용할 이벤트 리스너 등록
                let _this = this;
                const dropzoneMessage = _this.element.querySelector('.dz-message');
                // 아이콘 추가
                const dropzoneIcon = document.createElement('span');
                dropzoneIcon.className = 'z-icon i-avatar';
                dropzoneMessage.insertBefore(dropzoneIcon, dropzoneMessage.firstChild);
                // browse 버튼 추가
                const addFileBtn = _this.element.querySelector('.' + addFileBtnWrapClassName);
                dropzoneMessage.appendChild(addFileBtn);

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
                    extraParam.fileName = extraParam.avatar.id;
                    document.getElementById('avatarUUID').value = extraParam.fileName;
                    _this.files.push(mockFile);
                    _this.emit('addedfile', mockFile);
                    _this.createThumbnailFromUrl(mockFile,
                        _this.options.thumbnailWidth,
                        _this.options.thumbnailHeight,
                        _this.options.thumbnailMethod, true, function (thumbnail) {
                            _this.emit('thumbnail', mockFile, thumbnail);
                        });
                    _this.emit('complete', mockFile);
                }

                this.on('addedfile', function (file) {
                    extraParam.fileName = createUid();
                    document.getElementById('avatarUUID').value = extraParam.fileName;
                    validation(this, file, 'avatarUploader');
                    exportFile = file;
                });

                this.on('removedfile', function (file) {
                    extraParam.fileName = '';
                    document.getElementById('avatarUUID').value = extraParam.fileName;
                });

                this.on('success', function (file, response) {
                    const seq = document.createElement('input');
                    seq.setAttribute('type', 'hidden');
                    seq.setAttribute('name', fileAttrName);
                    seq.value = response.file.fileSeq;
                    file.previewElement.appendChild(seq);

                    let thumbs = document.querySelectorAll('.dz-image');
                    [].forEach.call(thumbs, function (thumb) {
                        thumb.style = 'width: 100%; height: 100%;';
                    });
                });

                this.on('thumbnail', function(file, dataUrl) {
                    let thumbs = document.querySelectorAll('.dz-image');
                    [].forEach.call(thumbs, function (thumb) {
                        let img = thumb.querySelector('img');
                        if (img) {
                            img.setAttribute('width', '100%');
                            img.setAttribute('height', '100%');
                        }
                    });
                });

                this.on('error', function (file, errorMsg, xhr) {
                    if (xhr !== undefined) {
                        const res = JSON.parse(xhr.response);
                        file.previewElement.querySelector('.dz-error-message').innerText = res.message;
                    }
                });
            },
            accept: function (file, done) { // done 함수 호출시 인수없이 호출해야 정상 업로드 진행
                done();
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
        },
        getFile: function() {
            return getFile();
        }
    };
}());
