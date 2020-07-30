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
 */
const fileUploader = (function () {
    "use strict";

    let extraParam, dropZoneFilesId, dropZoneUploadedFilesId, fileAttrName, delFileAttrName;
    const setExtraParam = function (param) {
        delFileAttrName = 'delFileSeq'
        fileAttrName = 'fileSeq'
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
        dropZoneFilesId = extraParam.dropZoneFilesId;

        if (extraParam.dropZoneUploadedFilesId !== undefined) {
            dropZoneUploadedFilesId = extraParam.dropZoneUploadedFilesId;
        } else {
            dropZoneUploadedFilesId = 'dropZoneUploadedFiles';
        }

        if (extraParam.dropZoneUrl === undefined) {
            extraParam.dropZoneUrl = '/fileupload'
        }

        if (extraParam.dropZoneMaxFiles === undefined) {
            extraParam.dropZoneMaxFiles = null;
        }

        if (extraParam.clickable === undefined) {
            extraParam.clickable = '.add-file-button';
        }

        if (extraParam.acceptedFiles === undefined) {
            extraParam.acceptedFiles = null;
        }

        if (extraParam.type === undefined) {
            extraParam.type = 'dropzone';
        }

        // 파일 추가 버튼 정의 및 추가
        const dropZoneFiles = document.getElementById(''+ dropZoneFilesId +'');
        dropZoneFiles.className = 'fileEditorable';

        if (extraParam.clickable === '.add-file-button' && extraParam.type !== 'avatarFileUploader') {
            const addFileSpan = document.createElement('span');
            addFileSpan.className = 'add-file-button';
            const addFileBtn = document.createElement('button');
            addFileBtn.innerText = 'ADD';
            addFileBtn.setAttribute('type', 'button');
            addFileSpan.appendChild(addFileBtn);
            dropZoneFiles.appendChild(addFileSpan);
        }

        // 파일 드랍 영역 및 파일을 보여줄 장소 정의
        const fileDropZone = document.createElement('div');
        fileDropZone.id = 'dropZoneFileUpload';
        fileDropZone.className = extraParam.type;

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
        const deleteButtonData = document.createElement('button');
        deleteButtonData.className = 'dz-remove';
        deleteButtonData.dataset.dzRemove = '';
        deleteButtonData.innerText = 'DELETE';

        const detail = document.createElement('div');
        detail.className = 'dz-details';
        const filename = document.createElement('div');
        filename.className = 'dz-filename';
        filename.appendChild(filenameData);

        const size = document.createElement('div');
        size.className = 'dz-size';
        size.appendChild(sizeData);
        detail.appendChild(size).appendChild(filename);

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
        fileViewTemplate.appendChild(deleteButtonData);
        const fileView = document.createElement('div');
        fileView.appendChild(fileViewTemplate);

        // 파일 업로드 영역에 드랍 영역 정의
        document.getElementById(''+ dropZoneFilesId +'').appendChild(fileDropZone);

        // TO-DO
        // 2020.07.25 Jung Hee Chan
        // avatar 업로드는 기능 정리 필요. 일단 디자인 작업을 위해서 버튼 춢력은 주석 처리.
        if (extraParam.clickable == '.add-img-button') {
            const addFileSpan = document.createElement('span');
            addFileSpan.className = 'add-img-button';
            const addFileBtn = document.createElement('button');
            //addFileBtn.innerText = '아바타 추가';
            addFileBtn.setAttribute('type', 'button');
            addFileSpan.appendChild(addFileBtn);
            dropZoneFiles.appendChild(addFileSpan);
        }

        // 파일 업로드 기능 정의
        let dropzoneId = '#'+dropZoneFilesId+' #dropZoneFileUpload';
        const myDropZone = new Dropzone(''+ dropzoneId +'', {
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
            clickable: extraParam.clickable, // Define the element that should be used as click trigger to select files.
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
                            const originName = document.createElement('span');
                            originName.setAttribute('name', 'loadedFileNames');
                            originName.style.cursor = 'pointer';
                            originName.innerText = file.originName;
                            const fileSize = document.createElement('span');
                            fileSize.setAttribute('name', 'loadedFileSize');
                            fileSize.innerText = ' (' + convertedFileSize + ')';
                            const fileSeq = document.createElement('input');
                            fileSeq.setAttribute('type', 'hidden');
                            fileSeq.setAttribute('name', 'loadedFileSeq');
                            fileSeq.value = file.fileSeq;
                            const delBtn = document.createElement('button');
                            delBtn.setAttribute('type', 'button');
                            delBtn.className = 'file-delete fileEditorable';
                            delBtn.innerText = 'DELETE';
                            const fileTag = document.createElement('div');
                            fileTag.append(originName);
                            fileTag.append(fileSize);
                            fileTag.append(fileSeq);
                            fileTag.append(delBtn);
                            document.getElementById(''+dropZoneUploadedFilesId+'').appendChild(fileTag);
                            // 파일 다운로드
                            originName.addEventListener('click', function (e) {
                                const thisEvent = e.target;
                                const fileDownOpt = {
                                    method: 'get',
                                    url: '/filedownload?seq=' + Number(thisEvent.parentElement.querySelector('input[name=loadedFileSeq]').value),
                                    callbackFunc: function (xhr) {
                                        const a = document.createElement('a');
                                        const url = window.URL.createObjectURL(xhr.response);
                                        a.href = url;
                                        a.download = thisEvent.parentElement.querySelector('span[name=loadedFileNames').innerText;
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
                            delBtn.addEventListener('click', function (e) {
                                const delFile = this.parentElement.querySelector('input[name=loadedFileSeq]');
                                delFile.setAttribute('name', delFileAttrName);
                                delFile.parentElement.style.display = 'none';
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
                    // Hookup the start button
                    //file.previewElement.querySelector(".start").onclick = function() { _this.enqueueFile(file); };
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
                    var fileName = file.name;
                    var fileNameLength = file.name.length;
                    var lastDot = fileName.lastIndexOf('.');
                    var fileNameExtension = fileName.substring(lastDot+1, fileNameLength).toUpperCase();
                    var array = [];

                    for (var i = 0; i < fileNameExtensionList.length; i++)  {
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
                    //fileDropzone.removeFile(file);
                    //fileDropzone.removeAllFiles(file);
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
                if (file.name == "justinbieber.jpg") {
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
