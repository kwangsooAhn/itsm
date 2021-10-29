/**
 * @projectDescription 파일 업로드
 *
 * - dropzone.js 를 기본으로 커스텀하였기 때문에 반드시 함께 import 한다.
 * - zI18n.js 의 사용자 설정에 따른 타임존, 날짜시간 포맷, 언어 등을 사용하므로 반드시 함께 import 한다.
 *
 * @author Woo Da jung
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.zFileUploader = global.zFileUploader || {})));
}(this, (function (exports) {
    'use strict';

    const fileAttrName = 'fileSeq'; // 서버로 전달하여 업로드 할 fileSeq input hidden 의 속성 이름
    const delFileAttrName = 'delFileSeq'; // 서버로 전달하여 삭제할 fileSeq input hidden 의 속성 이름
    const dragAndDropZoneId = 'dropZoneFileUpload'; // 드래그앤드랍으로 업로드가 가능한 구역
    const addFileBtnWrapClassName = 'add-file-button-wrap'; // 업로드 버튼 클릭 구역 wrapper
    const unit = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
    const logValueDigit = 1024;

    function generateUUID() {
        function s4() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        }
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
    }

    /**
     * 확장자에 따른 파일 이름 조회
     */
    function getExtension(fileName) {
        let dot = fileName.lastIndexOf('.');
        return fileName.substring(dot + 1, fileName.length).toLowerCase();
    }

    /**
     * 확장자에 따른 파일 아이콘 조회
     */
    function getFileIcon(fileName) {
        return '/assets/media/icons/fileUploader/icon_document_' + getExtension(fileName) + '.svg';
    }

    /**
     * 매개변수 초기화
     */
    function setExtraParam(extraParam) {
        if (typeof extraParam.dropZoneMaxFileSize === 'undefined') {
            extraParam.dropZoneMaxFileSize = 100;
        }

        if (typeof extraParam.dropZoneFilesId === 'undefined') {
            extraParam.dropZoneFilesId = 'dropZoneFiles';
        }

        if (typeof extraParam.dropZoneUploadedFilesId === 'undefined') {
            extraParam.dropZoneUploadedFilesId = 'dropZoneUploadedFiles';
        }

        if (typeof extraParam.dropZoneUrl === 'undefined') {
            extraParam.dropZoneUrl = '/fileupload';
        }

        if (typeof extraParam.dropZoneMaxFiles === 'undefined') {
            extraParam.dropZoneMaxFiles = null;
        }

        if (typeof extraParam.clickable === 'undefined') {
            extraParam.clickable = 'add-file-button';
        }

        if (typeof extraParam.acceptedFiles === 'undefined') {
            extraParam.acceptedFiles = null;
        }

        extraParam.type = 'dropzone ' + (extraParam.type || '');

        // edit 모드일때
        if (typeof extraParam.editor === 'undefined') {
            extraParam.editor = true;
        }

        if (typeof extraParam.enableImageThumbnails === 'undefined') {
            extraParam.enableImageThumbnails = false;
        }
        // 아바타이면 이미지 썸네일 속성은 무조건 true 이다.
        if (extraParam.type === 'avatar') {
            extraParam.enableImageThumbnails = true;
        }

        if (typeof extraParam.dictDefaultMessage === 'undefined') {
            extraParam.dictDefaultMessage = i18n.msg('file.msg.upload');
        }

        if (typeof extraParam.clickableLineMessage === 'undefined') {
            extraParam.clickableLineMessage = ' ' + i18n.msg('file.label.or') + ' ';
        }

        if (typeof extraParam.clickableMessage === 'undefined') {
            extraParam.clickableMessage = i18n.msg('file.msg.browser');
        }
        // view 모드일때
        if (typeof extraParam.isView === 'undefined') {
            extraParam.isView = false;
        }
        // 폼 디자이너, 신청서인지 여부
        if (typeof extraParam.isForm === 'undefined') {
            extraParam.isForm = false;
        }

        if (typeof extraParam.defaultUrl === 'undefined') {
            extraParam.defaultUrl = '';
        }
        // 헤더 추가 여부
        if (typeof extraParam.isHeaders === 'undefined') {
            extraParam.isHeaders = true;
        }

        // dropzone 영역이 아래에 나오게 하고싶은 경우
        if (typeof extraParam.isDropzoneUnder === 'undefined') {
            extraParam.isDropzoneUnder = false;
        }
    }

    /**
     * 업로드 영역의 dropzone template 을 생성하여 리턴한다.
     * @returns {string}
     */
    function getPreviewTemplate() {
        return `<div id="fileTemplate" class="dz-preview dz-file-preview">` +
                `<div class="dz-image"><img data-dz-thumbnail="" alt=""/></div>` +
                `<div class="dz-details">` +
                    `<img class="dz-file-type" alt=""/>` +
                    `<div class="dz-filename"><span data-dz-name=""></span></div>` +
                    `<div class="dz-size"><span data-dz-size=""></span></div>` +
                    `<div class="dz-remove" data-dz-remove=""></div>` +
                `</div>` +
                `<div class="dz-progress">` +
                    `<span class="dz-upload" data-dz-uploadprogress=""></span>` +
                    `<span class="dz-upload-text float-left" data-dz-uploadprogress="">Uploading</span>` +
                `</div>` +
                `<div class="dz-error-message"><span data-dz-errormessage=""></span></div>` +
                `<div class="dz-success-mark"><span>SUCCESS</span></div>` +
                `<div class="dz-error-mark"><span>FAILED</span></div>` +
            `</div>`;
    }

    /**
     * 업로드된 파일 영역의 dropzone template 을 생성하여 리턴한다.
     * @returns {string}
     */
    function getPreviewUploadedTemplate(file, options) {
        const fileBytes = file.fileSize;
        const fileSizeLogValue = Math.floor(Math.log(fileBytes) / Math.log(logValueDigit));
        let convertedFileSize;
        if (fileSizeLogValue === Number.NEGATIVE_INFINITY) {
            convertedFileSize = '0 ' + unit[0];
        } else {
            convertedFileSize = (fileBytes / Math.pow(logValueDigit, Math.floor(fileSizeLogValue))).toFixed(2) +
                ' ' + unit[fileSizeLogValue];
        }
        return `` +
        `<div class="dz-preview dz-file-preview">` +
            `<div class="dz-details">` +
                `<img class="dz-file-type" src="${getFileIcon(file.originName, options.isView)}" alt=""/>` +
                `<div class="dz-filename" id="loadedFileNames" style="${options.isView ? 'cursor: pointer': ''}">` +
                    `<span>${file.originName}</span>` +
                `</div>` +
                `<div class="dz-size" id="loadedFileSize"><span>${convertedFileSize}</span></div>` +
                `${options.isView ? `` : 
                    `<div class="dz-remove"><span class="z-icon i-delete"></span></div>`}` +
                    `<div class="dz-download"><span class="z-icon i-download"></span></div>` +
                `<input type="hidden" name="loadedFileSeq" value="${file.fileSeq}" />` +
            `</div>` +
        `</div>`;
    }

    /**
     * 드랍존 생성
     */
    function createDragAndDropZone(targetElement, options) {
        const dropZoneTemplate =
            `<div id="${dragAndDropZoneId}" class="${options.type}">` +
                `<div class="${addFileBtnWrapClassName}">` +
                    `<span>${options.dictDefaultMessage}</span>` +
                    // 타입이 아바타일 경우 개행 추가
                    `${options.type.indexOf('avatar') === -1 ? `` : `<br>`}` +
                    // todo: #11252 현재 폼 디자이너 및 신청서는 clickable 옵션 사용이 제한되므로,
                    //              디자인 차원에서 관련 메시지 및 버튼을 제거합니다.
                    `${options.isForm ? `` :
                        `<span>${options.clickableLineMessage}</span>` +
                    `<span class="underline ${typeof options.clickable !== 'boolean' ? options.clickable : ''}">` +
                        `${options.clickableMessage}` +
                    `</span>`}` +
                `</div>` +
            `</div>`;
        targetElement.insertAdjacentHTML('beforeend', dropZoneTemplate);
    }

    /**
     * 업로드된 파일 다운로드 이벤트 핸들러
     */
    function fileDownloadHandler(e) {
        const target = (e.target.parentElement.classList.contains('dz-details')) ?  e.target : e.target.parentElement;
        const fileSeq = Number(target.parentElement.querySelector('input[name=loadedFileSeq]').value);
        aliceJs.fetchBlob(this.options.params.defaultUrl + '/filedownload?seq=' + fileSeq, {
            method: 'GET',
            showProgressbar: true,
        }).then(blob => {
            if (typeof blob === 'object') {
                const a = document.createElement('a');
                const url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = target.parentElement.querySelector('#loadedFileNames span').textContent;
                document.body.append(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            } else {
                zAlert.warning(i18n.msg('file.msg.noAttachFile'));
            }
        }).catch(err => {
            zAlert.warning(err);
        });
    }

    /**
     * 업로드된 파일 삭제 이벤트 핸들러
     */
    function fileRemoveHandler(e) {
        const target = (e.target.parentElement.classList.contains('dz-details')) ?  e.target : e.target.parentElement;
        const delFile = target.parentElement.querySelector('input[name=loadedFileSeq]');
        delFile.name = delFileAttrName;

        const delFilePreview = delFile.closest('.dz-preview');
        delFilePreview.style.display = 'none';

        // 파일이 하나도 없다면 아이콘을 보여준다.
        const previewList = delFilePreview.parentNode.querySelectorAll(
            '.dz-preview:not([style*="display:none"]):not([style*="display: none"])');
        if (previewList.length === 0) {
            delFilePreview.parentNode.querySelector('.i-document-txt').style.display = 'block';
            this.isFileExist = false;
        }

        if (typeof this.options.params.userCallback === 'function') {
            this.options.params.userCallback();
        }
    }

    async function getAcceptedFileExtentions(options) {
        //파일 확장자 목록 관련 출력
        let extensionValueArr = [];
        // 수용 파일 확장자가 없다면 기본 파일 확장자 제한(DB)에서 확인 한다.
        if (options.acceptedFiles === null) {
            const url = (options.defaultUrl === '' ? '/rest' : options.defaultUrl) + '/filenameextensions';
            const fileNameExtensionList = await aliceJs.fetchJson(url, {
                method: 'GET',
            });
            for (let i = 0; i < fileNameExtensionList.length; i++) {
                extensionValueArr[i] = fileNameExtensionList[i].fileNameExtension;
            }
        } else {
            const acceptedFiles = options.acceptedFiles.split('.');
            for (let i = 0; i < acceptedFiles.length; i++) {
                extensionValueArr[i] = acceptedFiles[i].replace(',', '').trim().toUpperCase();
            }
        }
        return extensionValueArr;
    }

    /**
     * 파일업로드 validation (파일확장자, 최대 파일 수, 최대 파일 사이즈)
     * dropzone : 현재 dropzone 객체
     * file : 첨부한 파일
     * options : 옵션
     */
    function fileUploadValidationCheck(dropzone, file, options) {
        //파일 확장자 목록 관련 출력
        getAcceptedFileExtentions(options).then((extensionValueArr) => {
            if (!extensionValueArr.includes(getExtension(file.name).toUpperCase())) {
                dropzone.removeFile(file);
                if (options.isDropzoneUnder) {
                    dropzone.element.querySelector('.dz-message').style.display = 'block';
                }
                zAlert.warning(i18n.msg('fileupload.msg.extensionNotAvailable'));
            } else if (file.size > options.dropZoneMaxFileSize * 1024 * 1024) {
                dropzone.removeFile(file);
                zAlert.info(i18n.msg('fileupload.msg.maxFileSize', options.dropZoneMaxFileSize));
            } else if (options.dropZoneMaxFiles !== null && dropzone.files.length > options.dropZoneMaxFiles) {
                if (options.type === 'avatar' && options.dropZoneMaxFiles === 1) {
                    if (dropzone.files.length > 1) {
                        dropzone.removeFile(dropzone.files[0]);
                    }
                    options.fileName = generateUUID();
                    document.getElementById('avatarUUID').value = options.fileName;
                } else {
                    dropzone.removeFile(file);
                    zAlert.info(i18n.msg('fileupload.msg.maxFileCount', options.dropZoneMaxFiles));
                }
            }
        });
    }

    /**
     * 파일업로드 초기화
     */
    function initFileUploader(options) {
        // 드랍존 영역 가져오기.
        const dropZoneFiles = document.getElementById(options.dropZoneFilesId);
        const dropZoneUploadedFiles = document.getElementById(options.dropZoneUploadedFilesId);

        createDragAndDropZone(dropZoneFiles, options);
        // 파일 업로드 기능 정의
        const dropzoneId = '#' + options.dropZoneFilesId + ' #' + dragAndDropZoneId;
        new Dropzone(dropzoneId, {
            paramName: 'file', // file 매개변수명
            params: options || null, // 추가 매개변수
            maxFilesize: options.dropZoneMaxFileSize, // 첨부파일 용량 제한
            url: options.dropZoneUrl,
            maxThumbnailFilesize: 10, // MB, 썸네일 생성 최소 기준값, 초과시 썸네일 생성 안함
            maxFiles: options.dropZoneMaxFiles, // 첨부파일 개수 제한
            autoProcessQueue: true, //자동업로드, processQueue() 사용
            addRemoveLinks: false,
            acceptedFiles: options.acceptedFiles,
            previewTemplate: getPreviewTemplate(), // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // 직접 파일을 추가 할 때까지 대기열에 남은 파일이 있는지 확인
            clickable: options.clickable ? '.' + options.clickable : options.clickable, // 파일첨부 클릭 트리거 정의
            createImageThumbnails: false,
            dictDefaultMessage: options.dictDefaultMessage,
            headers: options.isHeaders
                ? {
                    'X-CSRF-Token': document.querySelector('meta[name="_csrf"]').getAttribute('content'),
                }
                : null,
            init: function () {
                // 드랍존 초기화시 사용할 이벤트 리스너 등록
                let _this = this;
                // 등록된 파일이 있으면 조회.
                aliceJs.fetchJson(
                    options.defaultUrl +
                    '/filelist?ownId=' +
                    (typeof options.ownId !== 'undefined' ? options.ownId : '') +
                    '&fileDataId=' +
                    (typeof options.fileDataIds !== 'undefined' ? options.fileDataIds : ''),
                    {
                        method: 'GET',
                    }
                ).then(files => {
                    _this.isFileExist = files.length > 0;
                    // 파일이 존재하지 않으면
                    if (!_this.isFileExist && options.isView) {
                        const noFileStr = document.createElement('span');
                        noFileStr.className = 'text-no-file text-ellipsis';
                        noFileStr.textContent = i18n.msg('file.msg.noAttachFile');
                        dropZoneUploadedFiles.appendChild(noFileStr);
                    }

                    files.forEach(function (fileMap, idx) {
                        const fileTemplate = getPreviewUploadedTemplate(fileMap.fileLocDto, options);

                        if (options.isView) {
                            dropZoneUploadedFiles.className = 'dropzone dz-uploaded';
                            dropZoneUploadedFiles.insertAdjacentHTML('beforeend', fileTemplate);

                            // 파일 다운로드 이벤트
                            dropZoneUploadedFiles.children[idx].querySelector('.dz-filename')
                                .addEventListener('click', fileDownloadHandler.bind(_this));
                        } else {
                            // edit 일때
                            const dropzoneElement = document.querySelector(dropzoneId);
                            dropzoneElement.insertAdjacentHTML('beforeend', fileTemplate);
                            // 파일 다운로드
                            dropzoneElement.children[idx + 1].querySelector('.dz-download')
                                .addEventListener('click', fileDownloadHandler.bind(_this));

                            // 파일삭제 : 첨부파일 목록에서 제외, 삭제 flag 추가
                            dropzoneElement.children[idx + 1].querySelector('.dz-remove')
                                .addEventListener('click', fileRemoveHandler.bind(_this));
                        }
                    });
                });

                if (options.editor) {
                    const dropzoneMessage = _this.element.querySelector('.dz-message');
                    // 아이콘 추가
                    const dropzoneIcon = document.createElement('span');
                    dropzoneIcon.className = 'z-icon i-upload';
                    dropzoneMessage.insertBefore(dropzoneIcon, dropzoneMessage.firstChild);
                    // browse 버튼 추가
                    const addFileBtn = _this.element.querySelector('.' + addFileBtnWrapClassName);
                    dropzoneMessage.appendChild(addFileBtn);

                    _this.on('addedfile', function (file) {
                        const dropzoneMessage = _this.element.querySelector('.dz-message');
                        if (options.isDropzoneUnder) {
                            dropzoneMessage.style.display = 'none';
                        }

                        file.previewElement.querySelector('.dz-file-type').src = getFileIcon(file.name, options.isView);
                        // 삭제 아이콘 추가
                        const removeIcon = document.createElement('span');
                        removeIcon.className = 'z-icon i-delete';
                        const removeButton = file.previewElement.querySelector('.dz-remove');
                        removeButton.className = 'dz-remove added';
                        removeButton.appendChild(removeIcon);

                        fileUploadValidationCheck(_this, file, options);
                    });

                    _this.on('removedfile', function () {
                        const previewList = _this.element.querySelectorAll(
                            '.dz-preview:not([style*="display:none"]):not([style*="display: none"])');
                        if (_this.files.length === 0 && previewList.length === 0) {
                            _this.isFileExist = false;
                        }
                        if (typeof _this.options.params.userCallback === 'function') {
                            _this.options.params.userCallback();
                        }
                    });

                    _this.on('success', function (file, response) {
                        const seq = document.createElement('input');
                        seq.setAttribute('type', 'hidden');
                        seq.setAttribute('name', fileAttrName);
                        if (response.file !== undefined) {
                            seq.value = response.file.fileSeq;
                            file.previewElement.appendChild(seq);
                        }
                        if (typeof _this.options.params.userCallback === 'function') {
                            _this.options.params.userCallback(file);
                        }
                    });

                    _this.on('error', function (file, errorMsg, xhr) {
                        if (xhr !== undefined) {
                            const res = JSON.parse(xhr.response);
                            file.previewElement.querySelector('.dz-error-message').innerText = res.message;
                        }
                    });

                    _this.on('complete', function () {
                        if (options.isDropzoneUnder) {
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
            accept: function (file, done) {
                // done 함수 호출시 인수없이 호출해야 정상 업로드 진행
                done();
            },
        });
    }

    /**
     * 아바타 파일업로드 초기화
     */
    function initAvatarUploader(options) {
        // 드랍존 영역 가져오기.
        const dropZoneFiles = document.getElementById(options.dropZoneFilesId);

        createDragAndDropZone(dropZoneFiles, options);

        // 파일 업로드 기능 정의
        const dropzoneId = '#' + options.dropZoneFilesId + ' #' + dragAndDropZoneId;
        new Dropzone(dropzoneId, {
            paramName: 'file', // file 매개변수명
            params: options || null, // 추가 매개변수
            maxFilesize: options.dropZoneMaxFileSize, // 첨부파일 용량 제한
            url: options.dropZoneUrl,
            maxThumbnailFilesize: 10, // MB, 썸네일 생성 최소 기준값, 초과시 썸네일 생성 안함
            maxFiles: options.dropZoneMaxFiles, // 첨부파일 개수 제한
            autoProcessQueue: true, //자동업로드, processQueue() 사용
            addRemoveLinks: false,
            acceptedFiles: options.acceptedFiles,
            previewTemplate: getPreviewTemplate(), // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // 직접 파일을 추가 할 때까지 대기열에 남은 파일이 있는지 확인
            clickable: '.' + options.clickable, // 파일첨부 클릭 트리거 정의
            createImageThumbnails: true,
            thumbnailWidth: options.thumbnailWidth,
            thumbnailHeight: options.thumbnailHeight,
            dictDefaultMessage: options.dictDefaultMessage,
            headers: options.isHeaders
                ? {
                    'X-CSRF-Token': document.querySelector('meta[name="_csrf"]').getAttribute('content'),
                }
                : null,
            init: function () {
                // 드랍존 초기화시 사용할 이벤트 리스너 등록
                let _this = this;
                const dropzoneMessage = _this.element.querySelector('.dz-message');
                // 아이콘 추가
                const dropzoneIcon = document.createElement('span');
                dropzoneIcon.className = 'z-icon i-avatar';
                dropzoneMessage.insertBefore(dropzoneIcon, dropzoneMessage.firstChild);
                // browse 버튼 추가
                const addFileBtn = _this.element.querySelector('.' + addFileBtnWrapClassName);
                dropzoneMessage.appendChild(addFileBtn);

                if (options.avatar.id !== null && options.avatar.size > 0) {
                    const mockFile = {
                        id: options.avatar.id,
                        name: options.avatar.value,
                        size: options.avatar.size,
                        dataURL: options.avatar.path,
                        status: Dropzone.ADDED,
                        accepted: true,
                        isNew: false,
                    };
                    options.fileName = options.avatar.id;
                    document.getElementById('avatarUUID').value = options.fileName;
                    _this.files.push(mockFile);
                    _this.emit('addedfile', mockFile);
                    _this.createThumbnailFromUrl(
                        mockFile,
                        _this.options.thumbnailWidth,
                        _this.options.thumbnailHeight,
                        _this.options.thumbnailMethod,
                        true,
                        function (thumbnail) {
                            _this.emit('thumbnail', mockFile, thumbnail);
                        }
                    );
                    _this.emit('complete', mockFile);
                }

                _this.on('addedfile', function (file) {
                    options.fileName = generateUUID();
                    document.getElementById('avatarUUID').value = options.fileName;
                    fileUploadValidationCheck(_this, file, options);
                });

                _this.on('removedfile', function () {
                    options.fileName = '';
                    document.getElementById('avatarUUID').value = options.fileName;
                });

                _this.on('success', function (file, response) {
                    const seq = document.createElement('input');
                    seq.setAttribute('type', 'hidden');
                    seq.setAttribute('name', fileAttrName);
                    seq.value = response.file.fileSeq;
                    file.previewElement.appendChild(seq);

                    const thumbs = document.querySelectorAll('.dz-image');
                    [].forEach.call(thumbs, function (thumb) {
                        thumb.style = 'width: 100%; height: 100%;';
                    });
                });

                _this.on('thumbnail', function () {
                    let thumbs = document.querySelectorAll('.dz-image');
                    [].forEach.call(thumbs, function (thumb) {
                        let img = thumb.querySelector('img');
                        if (img) {
                            img.setAttribute('width', '100%');
                            img.setAttribute('height', '100%');
                        }
                    });
                });

                _this.on('error', function (file, errorMsg, xhr) {
                    if (xhr !== undefined) {
                        const res = JSON.parse(xhr.response);
                        file.previewElement.querySelector('.dz-error-message').innerText = res.message;
                    }
                });
            },
            accept: function (file, done) {
                // done 함수 호출시 인수없이 호출해야 정상 업로드 진행
                done();
            },
        });
    }

    /**
     * 초기화
     *
     * @param param 매개변수
     */
    function init(param) {
        setExtraParam(param);
        initFileUploader(param);
    }

    /**
     * 아바타 초기화
     *
     * @param param 매개변수
     */
    function avatar(param) {
        setExtraParam(param);
        initAvatarUploader(param);
    }

    exports.init = init;
    exports.avatar = avatar;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
