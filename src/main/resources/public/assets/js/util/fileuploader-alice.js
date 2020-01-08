/**
 * 파일 업로드
 *
 * fileUploader.createDropZone(param) 호출로 사용한다.
 * param 타입은 object 다.
 *
 * @param param.extra 추가로 사용할 매개변수
 *        - formId 임시로 업로드한 파일명을 실제로 사용하기 위해 request 에 포함하여 전달할 form id (form tag)
 *        - task 현재 문서에 보여주기 위한 파일을 가져올 sql key
 */
var fileUploader = (function($) {
    "use strict";
    // test

    var extraParam;
    var setExtraParam = function (param) {
        extraParam = param;
    }

    var createDropZone = function() {
        // 파일 추가 버튼 정의 및 추가
        $('#dropZoneFiles').before($('<button>').append($('<span>').text('Add File')).addClass('add-file-button'));

        // 파일 드랍 영역 및 파일을 보여줄 장소 정의
        var fileDropZone = $('<div>').attr('id', 'dropZoneFileUpload').addClass('dropzone');

        // 파일 템플릿 생성
        var thumbnailData = $('<img>').attr('data-dz-thumbnail', '');
        var filenameData = $('<span>').attr('data-dz-name', '');
        var sizeData = $('<span>').attr('data-dz-size', '');
        var errorMsgData = $('<span>').attr('data-dz-errormessage', '');
        var progressData = $('<span>').attr('data-dz-uploadprogress', '').addClass('dz-upload');
        var deleteButtonData = $('<button>').addClass('dz-remove').attr('data-dz-remove', '').text('Delete');

        var detail = $('<div>').addClass('dz-details');
        var filename = $('<div>').addClass('dz-filename').append(filenameData);
        var size = $('<div>').addClass('dz-size').append(sizeData);
        detail.append(size).append(filename);

        var thumbnail = $('<div>').addClass('dz-image').append(thumbnailData);
        var progress = $('<div>').addClass('dz-progress').append(progressData);
        var errorMsg = $('<div>').addClass('dz-error-message').append(errorMsgData);
        var successMark = $('<div>').addClass('dz-success-mark').append($('<span>').text('success'));
        var errorMark = $('<div>').addClass('dz-error-mark').append($('<span>').text('error'));

        var fileViewTemplate = $('<div>').attr('name', 'fileTemplate').addClass('dz-preview dz-file-preview')
            .append(thumbnail)
            .append(detail)
            .append(progress)
            .append(errorMsg)
            .append(successMark)
            .append(errorMark)
            .append(deleteButtonData);

        // 파일 업로드 영역에 드랍 영역 정의
        $('#dropZoneFiles').append(fileDropZone);

        // 파일 업로드 기능 정의
        var myDropZone = new Dropzone('#dropZoneFiles #dropZoneFileUpload', {
            paramName: "file", // file 매개변수명
            params: extraParam || null, // 추가 매개변수
            maxFilesize: 3, // MB
            url: '/fileupload',
            maxThumbnailFilesize: 10, // MB, 썸네일 생성 최소 기준값, 초과시 썸네일 생성 안함
            maxFiles: null, // 처리할 최대 파일수
            autoProcessQueue: true, //자동업로드, processQueue() 사용
            addRemoveLinks: false,
            //acceptedFiles: "image/*",
            previewTemplate: $('<div>').append(fileViewTemplate).html(), // 기본 출력 템플릿 변경시 사용, API 참조 할 것.
            autoQueue: true, // Make sure the files aren't queued until manually added
            clickable: ".add-file-button", // Define the element that should be used as click trigger to select files.
            init: function() { // 드랍존 초기화시 사용할 이벤트 리스너 등록
                var _this = this;

                // 등록된 파일이 있으면 조회.
                $.ajax({
                    url: '/filelist',
                    type: 'get',
                    dataType: 'json',
                    data: {task: extraParam.task},
                    success: function(response) {
                        $.each(response.files, function(i, file) {

                            // 파일 목록 생성
                            var originName = $('<span>').attr('name', 'loadedFileNames').text(file.originName)
                            var fileSize = $('<span>').attr('name', 'loadedFileSize').text(' ('+file.size+')')
                            var fileSeq = $('<input>').attr({'type':'hidden', 'name': 'loadedFileSeq'}).text(file.fileSeq)
                            var delBtn = $('<button>').attr('type', 'button').addClass('file-delete').text('Delete');
                            var fileTag = $('<div>').css('cursor', 'pointer').append(originName).append(fileSize).append(fileSeq).append(delBtn)
                            $('#dropZoneUploadedFiles').append(fileTag)

                            // 파일 다운로드
                            $(originName).on('click', function() {
                                var _this = $(this)
                                $.ajax({
                                    url: '/filedownload',
                                    type: 'get',
                                    data: {seq: Number(_this.parent().find('input[name=loadedFileSeq]').text()), test: _this.parent().find('span[name=loadedFileNames]').text()},
                                    xhrFields: {
                                        responseType: 'blob'
                                    },
                                    success: function (data) {
                                        var a = document.createElement('a');
                                        var url = window.URL.createObjectURL(data);
                                        a.href = url;
                                        a.download = _this.parent().find('span[name=loadedFileNames').text();
                                        document.body.append(a);
                                        a.click();
                                        a.remove();
                                        window.URL.revokeObjectURL(url);
                                    },
                                    error: function(res, error, xhr) {
                                        alert('다운로드중 에러.')
                                        console.log(res)
                                    }
                                });

                            });

                            // 파일삭제
                            $(delBtn).on('click', function() {
                                var _delBtn = $(this);
                                $.ajax({
                                    url: '/filedel',
                                    type: 'delete',
                                    dataType: 'json',
                                    data: {seq: Number(_delBtn.parent().find('input[name=loadedFileSeq]').text()), testSeq: _delBtn.parent().find('span[name=loadedFileNames]').text()},
                                    success: function(res) {
                                        alert('삭제완료')
                                        _delBtn.parent().remove()
                                    },
                                    error: function(res, error, xhr) {
                                        alert('에러남');
                                        console.log(res);
                                        console.log(error);
                                        console.log(xhr);
                                    }

                                })

                            });


                        });
                    },
                    error: function(res, msg, xhr) {
                        console.log('error');

                        console.log(res.responseText);

                        console.log(xhr);
                    }
                });

                //파일접근시 사용.
                //all accepted files: .getAcceptedFiles()
                //all rejected files: .getRejectedFiles()
                //all queued files: .getQueuedFiles()
                //all uploading files: .getUploadingFiles()


                this.on("addedfile", function(file) {
                    console.log('addfile..');
                    // Hookup the start button
                    //file.previewElement.querySelector(".start").onclick = function() { _this.enqueueFile(file); };
                });

                this.on("removedfile", function(file) {

                });

                // Update the total progress bar
                this.on("totaluploadprogress", function(progress) {
                    //document.querySelector("#total-progress .progress-bar").style.width = progress + "%";
                    console.log('totaluploadprogress..');
                });

                this.on("sending", function(file) {
                    // Show the total progress bar when upload starts
                    //document.querySelector("#total-progress").style.opacity = "1";
                    // And disable the start button
                    //file.previewElement.querySelector(".start").setAttribute("disabled", "disabled");
                    console.log('sending..');
                });

                // Hide the total progress bar when nothing's uploading anymore
                this.on("queuecomplete", function(progress) {
                    //document.querySelector("#total-progress").style.opacity = "0";
                    console.log('queuecomplete..');
                });

                this.on("success", function(file, response) {
                    var seq = $('<input>').attr({'type':'hidden', 'name':'fileSeq'}).val(response.file.fileSeq);
                    console.log(seq)
                    $('#' + extraParam.formId).append(seq);
                    //$(file.previewElement).find('.dz-success-mark').show();
                    //$(file.previewElement).find('.dz-error-mark').hide();
                });

                this.on("complete", function(file) {
                    //fileDropzone.removeFile(file);
                    //fileDropzone.removeAllFiles(file);
                    console.log('complete..');
                });

                this.on("canceled", function() {
                    console.log('canceled..');

                });

                this.on("error", function(file, errorMsg, xhr) {
                    console.error('error')
                    //$(file.previewElement).find('.dz-success-mark').hide();
                    //$(file.previewElement).find('.dz-error-mark').show();
                    var res = JSON.parse(xhr.response)

                    //aliceJs.xhrErrorResponse()

                    $(file.previewElement).find('.dz-error-message').text(res.msg).addClass("dz-error");
                });

            },
            accept: function(file, done) { // done 함수 호출시 인수없이 호출해야 정상 업로드 진행
                console.log('accept');
                if (file.name == "justinbieber.jpg") {
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
            createDropZone();
        }
    }

}(jQuery));