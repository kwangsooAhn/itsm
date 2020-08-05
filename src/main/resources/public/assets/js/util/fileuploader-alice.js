/**
 * 파일 업로드
 *
 * fileUploader.createDropZone(param) 호출로 사용한다.
 * param 타입은 object 다.
 *
 * @param param.extra 추가로 사용할 매개변수
 *        - formId 임시로 업로드한 파일명을 실제로 사용하기 위해 request 에 포함하여 전달할 form id (form tag)
 *        - task 현재 문서에 보여주기 위한 파일을 가져올 sql key
 *        - fileAttrName 서버로 전달하여 업로드 할 fileSeq input hidden 의 속성 이름
 *        - delFileAttrName 서버로 전달하여 삭제할 fileSeq input hidden 의 속성 이름
 *        - clickable 파일 추가 버튼을 사용할 element 영역
 */
const fileUploader = (function () {
    "use strict";

    let extraParam, dropZoneFilesId, dropZoneUploadedFilesId, fileAttrName, delFileAttrName, dragAndDropZoneId, addFileBtnWrapClassName;

    const setExtraParam = function (param) {
        delFileAttrName = 'delFileSeq';
        fileAttrName = 'fileSeq';
        dragAndDropZoneId = 'dropZoneFileUpload';
        addFileBtnWrapClassName = 'add-file-button-wrap';

        extraParam = param;
    };

    const createDropZone = function () {
        /*<![CDATA[*/
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

        if (extraParam.type === undefined) {
            extraParam.type = 'dropzone';
        }

        dropZoneFilesId = extraParam.dropZoneFilesId;
        dropZoneUploadedFilesId = extraParam.dropZoneUploadedFilesId;

        // 드랍존 영역 가져오기.
        const dropZoneFiles = document.getElementById(dropZoneFilesId);
        dropZoneFiles.className = 'fileEditorable';

        // 파일을 드래그앤드랍하여 업로드하는 영역을 정의
        const dragAndDropZone = document.createElement('div');
        dragAndDropZone.id = dragAndDropZoneId;
        dragAndDropZone.className = extraParam.type;
        document.getElementById(dropZoneFilesId).appendChild(dragAndDropZone);

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
        // fileViewTemplate.appendChild(thumbnail);
        fileViewTemplate.appendChild(detail);
        fileViewTemplate.appendChild(progress);
        fileViewTemplate.appendChild(errorMsg);
        fileViewTemplate.appendChild(successMark);
        fileViewTemplate.appendChild(errorMark);
        const fileView = document.createElement('div');
        fileView.appendChild(fileViewTemplate);

        // 파일을 업로드하기 위한 별도의 버튼 기능을 정의하고 추가 (드래그앤드랍 외에 버튼으로 파일 추가)
        if (extraParam.clickable === 'add-file-button' && extraParam.type !== 'avatarFileUploader') {
            const justText = document.createElement('span');
            justText.textContent = 'or ';

            const addFileBtn = document.createElement('span');
            addFileBtn.className = extraParam.clickable;
            addFileBtn.textContent = 'browse';

            const addFileBtnWrap = document.createElement('div');
            addFileBtnWrap.className = addFileBtnWrapClassName
            addFileBtnWrap.appendChild(justText);
            addFileBtnWrap.appendChild(addFileBtn);
            dragAndDropZone.appendChild(addFileBtnWrap);
        }

        // TO-DO (아바타용인가??) 파일을 드래그앤드랍 말고 업로드하기 위한 별도의 버튼 기능을 정의하고 추가
        // 2020.07.25 Jung Hee Chan
        // avatar 업로드는 기능 정리 필요. 일단 디자인 작업을 위해서 버튼 춢력은 주석 처리.
        if (extraParam.clickable === 'add-img-button') {
            const addFileSpan = document.createElement('span');
            addFileSpan.className = 'add-img-button';
            const addFileBtn = document.createElement('button');
            //addFileBtn.innerText = '아바타 추가';
            addFileBtn.setAttribute('type', 'button');
            addFileSpan.appendChild(addFileBtn);
            dropZoneFiles.appendChild(addFileSpan);
        }

        // 파일 업로드 기능 정의
        let dropzoneId = '#'+dropZoneFilesId+' #' + dragAndDropZoneId;
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
            previewTemplate: fileView.innerHTML, // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // Make sure the files aren't queued until manually added
            clickable: '.' + extraParam.clickable, // Define the element that should be used as click trigger to select files.
            createImageThumbnails: false,
            allowNewFiles: false,
            headers: {
                'X-CSRF-Token': document.querySelector('meta[name="_csrf"]').getAttribute("content")
            },
            init: function () { // 드랍존 초기화시 사용할 이벤트 리스너 등록
                // 등록된 파일이 있으면 조회.
                const opt = {
                    method: 'get',
                    url: '/filelist?ownId=' + ((extraParam.hasOwnProperty('ownId')) ? extraParam.ownId : '')
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
                            if (fileSizeLogValue == "-Infinity") {
                                convertedFileSize = "0 " + unit[0];
                            } else {
                                convertedFileSize = (fileBytes / Math.pow(logValueDigit, Math.floor(fileSizeLogValue))).toFixed(2) + " " + unit[fileSizeLogValue];
                            }

                            // 파일 목록 생성
                            const fileType = document.createElement('div');
                            fileType.className = 'dz-file-type';

                            // TODO 파일 종류에 따라서 변경해서 셋팅한다.
                            fileType.style.backgroundImage = '/assets/theme/default/icons/icon_notice_document.svg';

                            const fileName = document.createElement('div');
                            fileName.className = 'dz-filename';
                            fileName.setAttribute('name', 'loadedFileNames');
                            fileName.style.cursor = 'pointer';
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
                            remove.style.backgroundImage = '/assets/theme/default/icons/icon_notice_delete.svg';
                            remove.style.backgroundSize = '1.125rem';
                            remove.style.backgroundRepeat = 'no-repeat';
                            remove.style.backgroundPositionX = 'center';
                            remove.style.backgroundPositionY = 'center';
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
                            const fileView = document.createElement('div');
                            fileView.className = 'dz-preview dz-file-preview';
                            fileView.appendChild(fileDetails);

                            document.getElementById(dropZoneUploadedFilesId).className = 'dropzone';
                            document.getElementById(dropZoneUploadedFilesId).appendChild(fileView);

                            // 파일 다운로드
                            fileName.addEventListener('click', function (e) {
                                const $this = this
                                const fileDownOpt = {
                                    method: 'get',
                                    url: '/filedownload?seq=' + Number($this.parentElement.querySelector('input[name=loadedFileSeq]').value),
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
                            });

                            // 파일삭제 : 첨부파일 목록에서 제외, 삭제 flag 추가
                            remove.addEventListener('click', function (e) {
                                const delFile = this.parentElement.querySelector('input[name=loadedFileSeq]');
                                delFile.setAttribute('name', delFileAttrName);
                                delFile.closest('.dz-preview').style.display = 'none';
                            });
                        });

                        if (extraParam.hasOwnProperty('editor') && !extraParam.editor) {
                            for (let editor of document.getElementsByClassName('fileEditorable')) {
                                editor.style.display = 'none'
                            }
                        }
                    },
                    params: '',
                    async: false
                };

                if (extraParam.dropZoneUrl === '/fileupload') {
                    aliceJs.sendXhr(opt);
                }

                const addFileBtn = document.querySelector('.' + addFileBtnWrapClassName)
                document.querySelector('.dz-message').appendChild(addFileBtn)

                //파일 확장자 목록 관련 출력
                var fileNameExtensionList;
                const opt2 = {
                    method: 'GET',
                    url: '/rest/fileNameExtensionList',
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
                    document.querySelector('.dz-message').style.display = 'none';
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
                    const fileName = file.name;
                    const fileNameLength = file.name.length;
                    const lastDot = fileName.lastIndexOf('.');
                    const fileNameExtension = fileName.substring(lastDot+1, fileNameLength).toUpperCase();
                    const array = [];

                    for (let i = 0; i < fileNameExtensionList.length; i++)  {
                        array[i] = fileNameExtensionList[i].fileNameExtension;
                    }

                    if (!(array.includes(fileNameExtension))) {
                        this.removeFile(file);
                        aliceJs.alert(i18n.get('fileupload.msg.extensionNotAvailable'))
                    }

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
                    const dropzoneMessage = document.querySelector('.dz-message')
                    document.querySelector('#dropZoneFileUpload').appendChild(dropzoneMessage);
                    dropzoneMessage.style.display = 'block';
                });

                // Hide the total progress bar when nothing's uploading anymore
                this.on("queuecomplete", function (progress) {
                    //document.querySelector("#total-progress").style.opacity = "0";
                });

                this.on("canceled", function () {
                });

                this.on("maxfilesexceeded", function (file, maxFiles) {
                    this.removeFile(file);
                    aliceJs.alert(i18n.get('fileupload.msg.maxFileCount', maxFiles));
                });

                this.on("maxfilesizeexceeded", function (file, maxFileSize) {
                    this.removeFile(file);
                    aliceJs.alert(i18n.get('fileupload.msg.maxFileSize', maxFileSize));
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
        /*]]>*/
    };

    return {
        init: function (param) {
            setExtraParam(param.extra);
            createDropZone();
        }
    }
}());
