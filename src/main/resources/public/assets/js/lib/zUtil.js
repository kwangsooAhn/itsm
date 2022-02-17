const aliceJs = {};

aliceJs.systemCalendarDatetimeFormat = 'YYYY-MM-DD HH:mm:ss';
aliceJs.systemCalendarDateFormat = 'YYYY-MM-DD';
aliceJs.systemCalendarTimeFormat = 'HH:mm:ss';

const rgbaReg = /^rgba?\((\d+),(\d+),(\d+),?([^,\s)]+)?/i;
const hexReg = /^#([A-Fa-f0-9]{3}){1,2}$/;

aliceJs.imageExtensions = ['png', 'jpg', 'jpeg', 'gif'];
aliceJs.searchDataCount = 15;
aliceJs.fileOffsetCount = 17;
aliceJs.autoRefreshPeriod = 60000;
/**
 *  XMLHttpReqeust 응답시 에러 발생하는 경우 호출
 *
 * @param elementId 에러를 출력할 장소 element id
 * @param text response text
 *
 * 2020-07-20 Jung Hee Chan
 * - printError div를 만들어서 에러 내용을 상세히 찍는 부분은 그대로 살려둔다. 다만 화면에 출력하는 부분만 주석처리 함.
 * - 향후 단순 alert이 아닌 상세 리포팅 모달이 필요한 경우 아래 모듈을 발전시켜 사용할 예정이다.
 */
aliceJs.xhrErrorResponse = function (elementId, text) {
    let elmNode = document.getElementById(elementId);

    if (elmNode == null) {
        elmNode = document.createElement('div');
        elmNode.setAttribute('id', 'printError');
        if (document.getElementsByTagName('body').item(0) !== null) {
            document.getElementsByTagName('body').item(0).appendChild(elmNode);
        }
    }

    while (elmNode.hasChildNodes()) {
        elmNode.removeChild(elmNode.firstChild);
    }

    if (text === undefined) {
        return;
    }

    // text 정보들 - timestamp, status, error, message, path, exceptionType, knownError
    const data = JSON.parse(text);
    let messages = [
        {key: '코드', text: data.status},
        {key: '에러', text: data.error},
        {key: '메시지', text: data.message},
        {key: '호출 URL', text: data.path}
    ];
    if (data.hasOwnProperty('exceptionType')) {
        messages.push({key: '예외 종류', text: data.exceptionType});
    }
    if (data.hasOwnProperty('knownError')) {
        messages.push({key: '정의된 에러', text: data.knownError});
    }
    const table = document.createElement('table');
    messages.forEach(function (obj) {
        const tr = table.insertRow();
        const keyTd = tr.insertCell();
        const valueTd = tr.insertCell();
        keyTd.innerText = obj.key;
        valueTd.innerText = obj.text;
    });
    //elmNode.appendChild(table);
    console.info(data);
    zAlert.warning(data.message);
};

/*!
 * Serialize all form data into a query string
 * (c) 2018 Chris Ferdinandi, MIT License, https://gomakethings.com
 * @param  {Node}   form The form to serialize
 * @return {String}      The serialized form data
 */
aliceJs.serialize = function (form) {
    // Setup our serialized data
    const serialized = [];

    // Loop through each field in the form
    for (let i = 0; i < form.elements.length; i++) {

        const field = form.elements[i];

        // Don't serialize fields without a name, submits, buttons, file and reset inputs, and disabled fields
        if (!field.name || field.disabled || field.type === 'file' || field.type === 'reset' ||
            field.type === 'submit' || field.type === 'button') continue;

        // If a multi-select, get all selections
        if (field.type === 'select-multiple') {
            for (let n = 0; n < field.options.length; n++) {
                if (!field.options[n].selected) continue;
                serialized.push(encodeURIComponent(field.name) + '=' + encodeURIComponent(field.options[n].value));
            }
        }

        // Convert field data to a query string
        else if ((field.type !== 'checkbox' && field.type !== 'radio') || field.checked) {
            let fieldValue = encodeURIComponent(field.value);
            if (field.classList.contains('search-datetime') || field.classList.contains('datetime')) {
                fieldValue = encodeURIComponent(i18n.systemDateTime(field.value));
            } else if (field.classList.contains('search-date') || field.classList.contains('date')) {
                fieldValue = encodeURIComponent(i18n.systemDate(field.value));
            }
            serialized.push(encodeURIComponent(field.name) + '=' + fieldValue);
        }
    }

    return serialized.join('&');

};

/**
 * serialize array로 돌려준다.
 * @param form
 * @returns {[]}
 */
aliceJs.serializeArray = function (form) {
    // Setup our serialized data
    const serialized = [];

    // Loop through each field in the form
    for (let i = 0; i < form.elements.length; i++) {

        const field = form.elements[i];

        // Don't serialize fields without a name, submits, buttons, file and reset inputs, and disabled fields
        if (!field.name || field.disabled || field.type === 'file' || field.type === 'reset' ||
            field.type === 'submit' || field.type === 'button') continue;

        // If a multi-select, get all selections
        if (field.type === 'select-multiple') {
            for (let n = 0; n < field.options.length; n++) {
                if (!field.options[n].selected) continue;
                serialized.push({
                    name: field.name,
                    value: field.options[n].value
                });
            }
        }

        // Convert field data to a query string
        else if ((field.type !== 'checkbox' && field.type !== 'radio') || field.checked) {
            serialized.push({
                name: field.name,
                value: field.value
            });
        }
    }
    return serialized;
};

/**
 * serialize object 로 돌려준다
 * @param form
 * @returns {{}}
 */
aliceJs.serializeObject = function (form) {
    const result = {};
    aliceJs.serializeArray(form).forEach(function (element) {
        const node = result[element.name];
        if ('undefined' !== typeof node && node !== null) {
            if (Array.isArray(node)) {
                node.push(element.value);
            } else {
                result[element.name] = [node, element.value];
            }
        } else {
            result[element.name] = element.value;
        }
    });
    return result;
};

/**
 * 폼 데이터를 object 로 변환하여 리턴한다.
 * 폼 데이터중 첨부파일의 seq가 있는 경우 배열로 넘기기 위해 별도의 처리를 한다.
 * @param form element - document.getElementById('fromid')
 * @returns {{}}
 */
aliceJs.formDataToObject = function (form) {
    let formDataObject = {fileSeq:[], delFileSeq:[]};
    const formData = new FormData(form);
    formData.forEach(function(value, key) {

        if (key === 'fileSeq' || key === 'delFileSeq') {
            formDataObject[key].push(value);
        } else {
            formDataObject[key] = value;
        }
    });
    return formDataObject;
};

/**
 * 비동기 호출 및 응답시 사용한다.
 *
 *
 * @param option
 * method get or post
 * url get인 경우 쿼리도 같이 보내줘야한다. post인 경우는 @params 를 사용한다.
 * callbackFunc 리턴값 사용할 콜백 메서드
 * params post 인 경우 사용할 파라미터
 * async 비동기 true, 동기 false
 */
aliceJs.sendXhr = function (option) {
    let method = option.method;
    let url = option.url;
    let callbackFunc = option.callbackFunc;
    let params = option.params;
    let async = (option.async === undefined || option.async === null) ? true : option.async;
    let showProgressbar = (option.showProgressbar === undefined || option.showProgressbar === null) ? true : option.showProgressbar;

    let xhr;
    try {
        if (window.ActiveXObject) {
            xhr = new ActiveXObject('Microsoft.XMLHTTP');
        } else {
            xhr = new XMLHttpRequest();
        }

    } catch (e) {
        zAlert.danger('Error creating the XMLHttpRequest object.');
        return;
    }

    if (option.responseType) {
        xhr.responseType = option.responseType;
    }

    xhr.onreadystatechange = function () {
        if (this.readyState === 0) {
            //console.log('요청이 초기화되지 않음, 객체만 생성되고 아직 초기화되지 않은 상태(' + this.status + ')');
        } else if (this.readyState === 1) {
            //console.log('서버연결설정, OPEN 메서드가 호출되고 아직 send 메서드가 불리지 않은 상태(' + this.status + ')');
            if (showProgressbar) {
                showProgressBar();
            }
        } else if (this.readyState === 2) {
            //console.log('요청 접수, send메서드가 불렸지만 status와 헤더는 아직 도착하지 않음(' + this.status + ')');
        } else if (this.readyState === 3) {
            //console.log('처리 요청, 데이터의 일부를 받은 상태(' + this.status + ')');
        } else if (this.status === 403) {
            window.location.href = '/sessionInValid';
        } else if (this.readyState === 4 && this.status === 200) {
            //console.log('요청 완료및 응답 준비, 데이터를 전부 받음(' + this.status + ')');
            aliceJs.xhrErrorResponse('printError');
            if (typeof callbackFunc === 'function') {
                callbackFunc(this);
            } else {
                console.info('No callback function');
            }
            if (showProgressbar) {
                hiddenProgressBar();
            }
        } else {
            if (this.responseType === '') {
                try {
                    aliceJs.xhrErrorResponse('printError', this.responseText);
                } catch (e) {
                    document.getElementsByTagName('body').item(0).innerHTML = this.responseText;
                }
            } else {
                aliceJs.xhrErrorResponse('printError', this.responseText);
            }
            if (showProgressbar) {
                hiddenProgressBar();
            }
        }
    };

    // 네트워크 수준의 에러시 처리 내용
    xhr.onerror = function () {
        console.error('Maybe network error');
        aliceJs.xhrErrorResponse('printError', this.responseText);
    };

    xhr.open(method, url, async);

    // get 이외 csrf 적용
    if (method.toUpperCase() !== 'GET') {
        const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
        const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        xhr.setRequestHeader(header, token);
        if (option.contentType) {
            xhr.setRequestHeader('Content-type', option.contentType);
        } else {
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        }

    } else {
        params = null;
    }
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    xhr.send(params);
};

/*
 * ProgressBar 보여줌
 */
function showProgressBar() {
    //divProgressBar 적용이 되지 않을떄는 그냥 넘어가도록 조치
    let divCheck = document.getElementById('divProgressBar');
    if (divCheck === null) {
        let divProgressBar = document.createElement('div');
        divProgressBar.id = 'divProgressBar';
        divProgressBar.style.position = 'fixed';
        divProgressBar.style.display = 'block';
        divProgressBar.style.width = '100%';
        divProgressBar.style.height = '100%';
        divProgressBar.style.top = '0';
        divProgressBar.style.bottom = '0';
        divProgressBar.style.backgroundColor = 'grey';
        divProgressBar.style.opacity = 0.5;
        divProgressBar.style.pointerEvents = 'all';
        divProgressBar.style.zIndex = 999999;

        let imgProgressBar = document.createElement('img');
        imgProgressBar.src = '/assets/media/images/loading_w_dark.gif';
        imgProgressBar.style.position = 'absolute';
        if (document.querySelector('.container') !== null) {
            imgProgressBar.style.left = '42%';
        } else {
            imgProgressBar.style.left = '50%';
        }
        imgProgressBar.style.top = '0';
        imgProgressBar.style.bottom = '0';
        imgProgressBar.style.margin = 'auto';
        if (divProgressBar && document.body) {
            divProgressBar.appendChild(imgProgressBar);
            if (document.querySelector('.container') !== null) {
                document.querySelector('.container').appendChild(divProgressBar);
            } else {
                document.body.appendChild(divProgressBar);
            }
        }
    } else {
        return false;
    }
}

/*
 * ProgressBar 숨길
 */
function hiddenProgressBar() {
    //divProgressBar 적용이 되지 않을떄는 그냥 넘어가도록 조치
    var divCheck = document.getElementById('divProgressBar');
    if (divCheck === null) {
        return false;
    }
    divCheck.parentNode.removeChild(divCheck);
}

/**
 * 파라미터로 받은 날짜 데이터 기준으로 4가지 date 포맷을 받아서 yyyy-mm-dd HH:MM로 반환한다.
 * @param p_date 입력받는 날짜 2020-02-03 or 2020-02-03 14:30 or 2020-02-03 14:30 오전
 *  @param p_format 입력받는 날짜 형식
 */
function changeDateFormatYYYYMMDD(p_date, p_format) {
    var v_date = '';
    var arrayDate = [];
    var arrayResultDate = '';
    var arrayFormat = '';
    var index = 0;
    if (p_date === '' || p_date === null) {
        return;
    } else {
        v_date = p_date.replace(/ /gi, '');
        if (v_date.length === 10) {
            arrayDate[0] = v_date;
        } else if (v_date.length === 15) {
            arrayDate[0] = v_date.substring(0, 10);
            index = v_date.lastIndexOf(':');
            arrayDate[1] = v_date.substring(index-2, index+3);
        } else if (v_date.length === 17) {
            arrayDate[0] = v_date.substring(0, 10);
            index = v_date.lastIndexOf(':');
            arrayDate[1] = v_date.substring(index-2, index+3);
            if (p_date.indexOf('PM') !== -1 || p_date.indexOf('오후') !== -1) {
                if (p_date.indexOf('PM') !== -1) {
                    index = v_date.lastIndexOf('PM');
                } else if (p_date.indexOf('오후') !== -1) {
                    index = v_date.lastIndexOf('오후');
                }
                arrayDate[2] = v_date.substring(index-2, index+2);
            }
        } else {
            return;
        }
    }

    if (p_format === '' || p_format === null) {
        return;
    } else {
        //최대 포맷 형식이 YYYY-MM-DD HH:MM a 라고 들어올 것이라고 생각해서 배열처리한다.
        arrayFormat = p_format.split(' ');
    }

    //파라미터 날짜 형식에 따라서 다시  날짜만 YYY-MM-DD로 변환한다.
    if (arrayFormat[0].toUpperCase() === 'YYYY-MM-DD') {
        v_date = arrayDate[0];
    } else if (arrayFormat[0].toUpperCase() === 'YYYY-DD-MM') {
        arrayResultDate = arrayDate[0].split('-');
        v_date = arrayResultDate[0] +'-'+ arrayResultDate[2] +'-'+ arrayResultDate[1];
    } else if  (arrayFormat[0].toUpperCase() === 'DD-MM-YYYY') {
        arrayResultDate = arrayDate[0].split('-');
        v_date = arrayResultDate[2] +'-'+ arrayResultDate[1] +'-'+ arrayResultDate[0];
    } else if (arrayFormat[0].toUpperCase() === 'MM-DD-YYYY') {
        arrayResultDate = arrayDate[0].split('-');
        v_date = arrayResultDate[2] +'-'+ arrayResultDate[0] +'-'+ arrayResultDate[1];
    } else {
        return;
    }

    //받은 날짜가 시간도 있다면 시간을 추가 한다.
    if (arrayDate.length === 2 || arrayDate.length === 3) {
        v_date = v_date +' '+ arrayDate[1];
    }
    //console.log("v_date==="+v_date);
    //올바르게 변환한 yyyy-mm-dd와 시간을 객체로 변환한다.
    var result_date = new Date(v_date);
    var year = result_date.getFullYear();
    var month = (1+result_date.getMonth());
    month = month >= 10 ? month : '0' + month;
    var day = result_date.getDate();
    day = day >= 10 ? day : '0' + day;
    var hour = '';
    var min = '';
    if (arrayDate.length === 2 || arrayDate.length === 3) {
        hour = result_date.getHours();
        if (arrayDate.length === 3) {
            if (p_date.indexOf('PM') !== -1 || p_date.indexOf('오후') !== -1) {
                hour = eval(hour+12);
            }
            //java LocalDateTime은 hour를 24로 계산할 수 없다.
            if (hour === 24) {
                hour = 23;
            }
        }
        hour = hour >= 10 ? hour : '0'+hour;
        min = result_date.getMinutes();
        min = min >= 10 ? min : '0'+min;
    }
    if (arrayDate.length === 1) {
        v_date = year+'-'+month+'-'+day;
    } else if (arrayDate.length === 2 || arrayDate.length === 3) {
        v_date = year+'-'+month+'-'+day+' '+ hour+':'+min;
    }
    return v_date;
}

/**
 * 파라미터로 받은 날짜 데이터 기준으로 얼마의 시간이 지났는지 계산하여 반환한다. (ex. n분 전, n일 전...)
 * @param date 입력받는 날짜
 * @return string 사용자 언어로 변경된 메시지
 *
 * humanize-duration.min.js 사용
 * https://github.com/EvanHahn/HumanizeDuration.js/
 */
function dateFormatFromNow(date) {
    if (date) {
        const durationMilliseconds = luxon.Interval.fromDateTimes(new Date(i18n.userDateTime(date)), new Date())
            .toDuration()
            .valueOf();
        const humanizedString = humanizeDuration(durationMilliseconds, { largest : 1, floor : true, units : ['y', 'mo', 'd', 'h', 'm', 's'] }).split(' ');

        switch (true) {
            case humanizedString[1].includes('year') : return i18n.msg('date.label.yearsAgo', humanizedString[0]);
            case humanizedString[1].includes('month') : return i18n.msg('date.label.monthsAgo', humanizedString[0]);
            case humanizedString[1].includes('day') : return i18n.msg('date.label.daysAgo', humanizedString[0]);
            case humanizedString[1].includes('hour') : return i18n.msg('date.label.hoursAgo', humanizedString[0]);
            case humanizedString[1].includes('min') : return i18n.msg('date.label.minutesAgo', humanizedString[0]);
            case humanizedString[1].includes('sec') : return i18n.msg('date.label.aMomentAgo', humanizedString[0]);
            default : return '';
        }
    }
}

/**
 * 확장자에 따른 아이콘 조회
 * @param extension 확장자
 * @returns icon path 파일명
 */
aliceJs.getFileExtensionIcon = function(extension){
    return '/assets/media/icons/fileUploader/icon_document_' + extension + '.svg';
};

/**
 * 썸네일
 *
 * @param options 옵션
 *
 * title: 'Image',        // 모달 제목
 * type: 'image',         // 타입 : image, icon
 * isThumbnailInfo: true, // 하단 정보 출력 여부
 * isFilePrefix: true,    // 파일 선택시 파일명 앞에 'file:///' 추가 여부
 * thumbnailDoubleClickUse: false, // 더블클릭으로 이미지 선택기능 여부
 */
aliceJs.thumbnail = function(options) {
    /**
     * 썸네일 저장
     *
     * @param targetId 대상 input
     */
    const saveThumbnail = function(targetId) {
        // image 미선택 시 알림창 출력
        let selectedFile = document.querySelector('.z-thumbnail.selected');
        if (!selectedFile) {
            zAlert.warning(i18n.msg('file.msg.fileSelect'));
            return false;
        }
        const targetElem = document.getElementById(targetId);
        if (targetElem) {
            if (options.isFilePrefix) {
                targetElem.value = 'file:///' + selectedFile.dataset.name;
            } else {
                targetElem.value = selectedFile.dataset.name;
            }
            targetElem.dispatchEvent(new Event('focusout'));
        }
        aliceJs.inputButtonRemove();
        return true;
    };

    /**
     * 썸네일 선택.
     */
    const thumbnailSelect = function(e) {
        const elem = aliceJs.clickInsideElement(e, 'z-thumbnail');
        if (elem) {
            const parentElem = elem.parentNode;
            const isSelected = elem.classList.contains('selected');
            if (!isSelected) {
                for (let i = 0, len = parentElem.childNodes.length ; i< len; i++) {
                    let child = parentElem.childNodes[i];
                    if (child.classList.contains('selected')) {
                        child.classList.remove('selected');
                    }
                }
                elem.classList.add('selected');
            }
        }
    };

    const getThumbnail = function (type, file) {
        let thumbnailTemplate = '';
        switch (type) {
            case 'image':
                thumbnailTemplate = `<div class="z-thumbnail-image" ` +
                    `style="background-image:url('data:image/${file.extension};base64,${file.data}');">` +
                    `</div>`;
                break;
            case 'icon':
            case 'cmdb-icon':
                thumbnailTemplate = `<div class="z-thumbnail-icon" ` +
                    `style="background-image:url('data:image/${file.extension};base64,${file.data}');background-size:100%;">` +
                    `</div>`;
                break;
            case 'file':
                thumbnailTemplate = `<div class="z-thumbnail-file">` +
                    `<img src="${aliceJs.getFileExtensionIcon((file.extension).trim().toLowerCase())}"></div>` +
                    `</div>`;
                break;
            default:
                break;
        }
        return thumbnailTemplate;
    };

    /**
     * 썸네일 content.
     *
     * @param files 파일목록
     * @return content html
     */
    const createContent = function(files) {
        const container = document.createElement('div');
        container.className = 'z-thumbnail-main flex-row flex-wrap';

        if (files.data.length > 0) {
            for (let i = 0, len = files.data.length; i < len; i++) {
                let file = files.data[i];
                const fileExtension = (file.extension).trim().toLowerCase();
                const isImageFile = aliceJs.imageExtensions.includes(fileExtension);
                const thumbnail = document.createElement('div');
                thumbnail.className = 'z-thumbnail';
                thumbnail.setAttribute('data-name', file.name);

                if (typeof options.selectedPath !== 'undefined' &&  options.selectedPath.indexOf(file.name) > -1) {
                    thumbnail.classList.add('selected');
                }
                // 이벤트 등록
                thumbnail.addEventListener('click', thumbnailSelect, false);
                if (options.thumbnailDoubleClickUse) {
                    thumbnail.addEventListener('dblclick', function() {
                        document.querySelector('.thumbnail-save').click();
                    }, false);
                }

                container.appendChild(thumbnail);
                // 썸네일 조회
                const fileType = (options.type === 'file' && isImageFile) ? 'image' : options.type;
                thumbnail.insertAdjacentHTML('beforeend', getThumbnail(fileType, file));

                if (options.isThumbnailInfo) {
                    const thumbnailInfo = document.createElement('div');
                    thumbnailInfo.className = 'z-thumbnail-info';
                    thumbnail.appendChild(thumbnailInfo);

                    const thumbnailName = document.createElement('p');
                    thumbnailName.className = 'z-thumbnail-info-text';
                    thumbnailName.innerHTML = `<label class="text-ellipsis">${file.name}</label>`;
                    thumbnailInfo.appendChild(thumbnailName);

                    const thumbnailText = (isImageFile) ? `${file.width} X ${file.height} (${file.size})` : `${file.size}`;
                    const thumbnailSize = document.createElement('p');
                    thumbnailSize.className = 'z-thumbnail-info-text';
                    thumbnailSize.innerHTML = `<label class="text-ellipsis">${thumbnailText}</label>`;
                    thumbnailInfo.appendChild(thumbnailSize);

                    if (options.type !== 'file') {
                        const thumbnailBottom = document.createElement('div');
                        thumbnailBottom.className = 'z-thumbnail-bottom';
                        thumbnailBottom.innerHTML = `<label>${i18n.userDateTime(file.updateDt)}</label>`;
                        thumbnail.appendChild(thumbnailBottom);
                    }
                }
            }
        } else {
            // 썸네일이 존재하지 않을 경우 안내 문구 표시
            const thumbnailNodataTemplate = `
                <div class="z-thumbnail-nodata align-center">
                    <label>${i18n.msg('common.msg.noData')}</label>
                </div>
            `;
            container.insertAdjacentHTML('beforeend', thumbnailNodataTemplate);
        }
        return container;
    };

    // 이미지 파일 로드
    aliceJs.fetchJson('/rest/files?type=' + options.type, {
        method: 'GET'
    }).then((files) => {
        const modalOptions = {
            title: options.title,
            body: createContent(files),
            classes: 'z-thumbnail-' + options.type,
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'z-button primary thumbnail-save',
                bindKey: false,
                callback: function(modal) {
                    if (saveThumbnail(options.targetId)) {
                        modal.hide();
                    }
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: function(modal) {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: function (modal) {
                OverlayScrollbars(document.querySelector('.z-thumbnail-main').closest('.modal-content'), {className: 'scrollbar'});
            }
        };

        let thumbnailModal = new modal(modalOptions);
        thumbnailModal.show();
    });
};

/**
 * Object 객체이며 true, 아니면 false를 반환
 * @param item 대상
 * @returns {Boolean} boolean
 */
aliceJs.isObject = function(item) {
    return (item && typeof item === 'object' && !Array.isArray(item) && item !== null);
};

/**
 * Merge a `source` object to a `target` recursively
 * @param target target 객체
 * @param source source 객제
 */
aliceJs.mergeObject = function(target, source) {
    if (aliceJs.isObject(target) && aliceJs.isObject(source)) {
        Object.keys(source).forEach(function(key) {
            if (aliceJs.isObject(source[key])) {
                if (!target[key]) {
                    Object.assign(target, {[key]: {}});
                }
                aliceJs.mergeObject(target[key], source[key]);
            } else if (typeof source[key] === 'function') {
                const descriptor = Object.getOwnPropertyDescriptor(source, key);
                Object.defineProperty(target, key, descriptor);
            } else {
                Object.assign(target, JSON.parse(JSON.stringify({ [key]: source[key] })));
            }
        });
    }
    return target;
};

/**
 * 받은 값이 빈 값인지 체크.
 * Array, Object, null, undefined 도 빈 값 체크.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 *
 * @param {Object} value
 * @returns {boolean}
 */
aliceJs.isEmpty = function(value) {
    return value === '' || value == null || (typeof value === 'object' && !Object.keys(value).length);
};

/**
 * 특정 클래스 이름을 가진 요소 내부를 클릭했는지 확인
 * @param {Object} e 이벤트객체
 * @param {String} className 클래스명
 * @return {Object} 존재하면 객제 반환
 */
aliceJs.clickInsideElement = function (e, className) {
    let el = e.srcElement || e.target;
    while (el !== null) {
        if (el.classList && el.classList.contains(className)) return el;
        el = el.parentNode;
    }
    return null;
};

/**
 * RGBA 값을 Hex 값으로 변환.
 *
 * @param {string} value
 * @returns {string} rgba
 */
aliceJs.rgbaToHex = function(value) {
    let rgba = value.replace(/\s/g, '').match(rgbaReg);
    return rgba ?
        '#' +
        (rgba[1] | 1 << 8).toString(16).slice(1) +
        (rgba[2] | 1 << 8).toString(16).slice(1) +
        (rgba[3] | 1 << 8).toString(16).slice(1) : value;
};

/**
 * RGBA 의 alpha 값 조회.
 *
 * @param {string} value
 * @returns {string} alpha
 */
aliceJs.rgbaOpacity = function(value) {
    let rgba = value.replace(/\s/g, '').match(rgbaReg);
    let alpha = (rgba && rgba[4]);
    if (alpha === null || alpha === '') {
        alpha = 0.5;
    }
    return alpha;
};

/**
 * Hex 값을 RGBA 값으로 변환.
 *
 * @param {string} value
 * @param {number} opacity
 * @returns {string} hexValue
 */
aliceJs.hexToRgba = function(value, opacity) {
    let hexValue;
    if (value !== '' && typeof opacity !== 'undefined') {
        if (aliceJs.isHexCode(value)) {
            hexValue = value.substring(1).split('');
            if (hexValue.length === 3) {
                hexValue = [hexValue[0], hexValue[0], hexValue[1], hexValue[1], hexValue[2], hexValue[2]];
            }
            hexValue = '0x' + hexValue.join('');
            return 'rgba(' + [(hexValue >> 16) & 255, (hexValue >> 8) & 255, hexValue & 255].join(',') + ',' + opacity + ')';
        } else {
            throw new Error('Bad Hex');
        }
    }
    return value;
};

/**
 * Hex 값 체크.
 *
 * @param {string} value
 * @returns {boolean}
 */
aliceJs.isHexCode = function(value) {
    return hexReg.test(value);
};

/**
 * RGBA 값 체크.
 *
 * @param {string} value
 * @returns {boolean}
 */
aliceJs.isRgba = function(value) {
    return rgbaReg.test(value);
};

/**
 * Slide Toggle (메뉴 등).
 *
 * @param {HTMLElement} target 대상
 * @param {Number} duration 기간 (기본 500ms)
 */
aliceJs.slideToggle = (target, duration = 500) => {
    if (window.getComputedStyle(target).display === 'none') {
        return aliceJs.slideDown(target, duration);
    } else {
        return aliceJs.slideUp(target, duration);
    }
};

/**
 * Slide Up.
 *
 * @param {HTMLElement} target
 * @param {Number} duration
 */
aliceJs.slideUp = (target, duration = 500) => {
    target.style.transitionProperty = 'height, margin, padding';
    target.style.transitionDuration = duration + 'ms';
    target.style.boxSizing = 'border-box';
    target.style.height = target.offsetHeight + 'px';
    target.offsetHeight;
    target.style.overflow = 'hidden';
    target.style.height = '0';
    target.style.paddingTop = '0';
    target.style.paddingBottom = '0';
    target.style.marginTop = '0';
    target.style.marginBottom = '0';
    window.setTimeout( () => {
        target.style.display = 'none';
        target.style.removeProperty('height');
        target.style.removeProperty('padding-top');
        target.style.removeProperty('padding-bottom');
        target.style.removeProperty('margin-top');
        target.style.removeProperty('margin-bottom');
        target.style.removeProperty('overflow');
        target.style.removeProperty('transition-duration');
        target.style.removeProperty('transition-property');
    }, duration);
};

/**
 * Slide Down.
 *
 * @param {HTMLElement} target
 * @param {Number} duration
 */
aliceJs.slideDown = (target, duration = 500) => {
    target.style.removeProperty('display');
    let display = window.getComputedStyle(target).display;

    if (display === 'none') display = 'block';

    target.style.display = display;
    let height = target.offsetHeight;
    target.style.overflow = 'hidden';
    target.style.height = '0';
    target.style.paddingTop = '0';
    target.style.paddingBottom = '0';
    target.style.marginTop = '0';
    target.style.marginBottom = '0';
    target.offsetHeight;
    target.style.boxSizing = 'border-box';
    target.style.transitionProperty = 'height, margin, padding';
    target.style.transitionDuration = duration + 'ms';
    target.style.height = height + 'px';
    target.style.removeProperty('padding-top');
    target.style.removeProperty('padding-bottom');
    target.style.removeProperty('margin-top');
    target.style.removeProperty('margin-bottom');
    window.setTimeout( () => {
        target.style.removeProperty('height');
        target.style.removeProperty('overflow');
        target.style.removeProperty('transition-duration');
        target.style.removeProperty('transition-property');
    }, duration);
};

/**
 * Replace all SVG images with inline SVG
 *
 */
aliceJs.loadSvg = function() {
    const svgList = document.querySelectorAll('img.load-svg');
    for (let i = 0, len = svgList.length; i < len; i++) {
        const img = svgList[i];
        const imgId = img.id;
        const imgClass = img.className;
        const imgUrl = img.getAttribute('src');
        if (typeof imgUrl === 'undefined' || imgUrl === '') { continue; }

        aliceJs.fetchText(imgUrl, {
            method: 'GET',
            headers: {
                'Content-Type': 'image/svg+xml; charset=utf-8'
            }
        }).then((response) => {
            const parser = new DOMParser();
            const svgFile = parser.parseFromString(response, 'application/xml');
            let svg = svgFile.documentElement;
            if (typeof imgId !== 'undefined' && imgId !== '') {
                svg.setAttribute('id', imgId);
            }

            if (typeof imgClass !== 'undefined' && imgClass !== '') {
                svg.setAttribute('class', imgClass);
            }

            img.insertAdjacentHTML('beforebegin', svg.outerHTML);
            img.remove();
        });
    }
};

/**
 * 마지막 데이터 여부 판단.
 *
 * @param offset 현재 조회된 offset
 * @param objectId 전체수 저장 object-id (default: totalCount)
 * @return {boolean} 스크롤 처리 진행 여부
 */
aliceJs.isEnableScrollEvent = function(offset, objectId = 'totalCount') {
    let totalObject = document.getElementById(objectId);
    return offset < totalObject.value;
};

/**
 * 목록에 TotalCount 표시한다.
 *
 * @param totalCount 목록에 보여주고 싶은 건수
 * @param objectId 전체수 저장 object-id (default: spanTotalCount)
 */
aliceJs.showTotalCount = function(totalCount, objectId = 'spanTotalCount') {
    document.getElementById(objectId).textContent = i18n.msg('common.label.count', totalCount);
};

/**
 * 문자열 <,>, ', ", & 등 특수문자에 대하여 코드로 변환해준다.
 * 사용법은 아래와 같다.
 * aliceJs.filterXSS('<p><script>alert('test');</script></p>')
 *
 * @param str 문자열
 * @return {string} 변환된 문자열
 */
aliceJs.filterXSS = function (str) {
    return (typeof str === 'string' || str instanceof String)?
        str.replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/\"/g, '&quot;')
            .replace(/\'/g, '&apos;'):str;
};

/**
 * Move object
 * @param index1 old index
 * @param index2 new index
 */
aliceJs.moveObject = function (array, index1, index2) {
    while (index1 < 0) {
        index1 += array.length;
    }
    while (index2 < 0) {
        index2 += array.length;
    }
    if (index2 >= array.length) {
        let k = index2 - array.length + 1;
        while (k--) {
            array.push(undefined);
        }
    }
    array.splice(index2, 0, array.splice(index1, 1)[0]);
};

aliceJs.getNodeIndex = function (elem) {
    let index = 0;

    if (!elem || !elem.parentNode) {
        return -1;
    }

    // eslint-disable-next-line no-cond-assign
    while (elem = elem.previousElementSibling) {
        if (elem.nodeName.toUpperCase() !== 'TEMPLATE') {
            index++;
        }
    }
    return index;
};

aliceJs.swapNode = function (node1, node2) {
    let p1 = node1.parentNode,
        p2 = node2.parentNode,
        i1, i2;

    if (!p1 || !p2 || p1.isEqualNode(node2) || p2.isEqualNode(node1)) return;

    i1 = aliceJs.getNodeIndex(node1);
    i2 = aliceJs.getNodeIndex(node2);

    if (p1.isEqualNode(p2) && i1 < i2) {
        i2++;
    }
    p1.insertBefore(node2, p1.children[i1]);
    p2.insertBefore(node1, p2.children[i2]);
};

/**
 * 비동기 통신 후 Promise 형태로 반환되는 함수
 *
 * @param option 옵션
 * @return 비동기 통신 객체 = Promise 객체
 */
aliceJs.doFetch = async function(url, option) {
    // Progressbar 추가
    const showProgressbar = (option.showProgressbar === undefined || option.showProgressbar === null) ? false : option.showProgressbar;
    if (showProgressbar) {
        showProgressBar();
    }
    if (option.showProgressbar) {
        delete option.showProgressbar;
    }
    const checkFetch = function (response) {
        // Progressbar 삭제
        if (showProgressbar) {
            hiddenProgressBar();
        }
        // fetch에서 리턴된 Promise는 응답이 404나 500 오류여도 reject하지 않는다.
        // reject 되는 경우는 네트워크 장애 또는 요청이 완료되지 못한 경우에만 발생한다.
        if (!response.ok) {
            if (response.status === 403) {
                window.location.href = '/sessionInValid';
            } else {
                throw new Error('HTTP error, status = ' + response.status + ', url = ' + response.url);
            }
        }
        return response;
    };
    const fetchParam = Object.assign({}, option);
    const response = await fetch(url, fetchParam).then(checkFetch);
    return response;
};

/**
 * 비동기 통신 후 Promise 형태로 Json 데이터를 반환하는 함수
 * @param url url
 * @param option 옵션
 * @returns Promise 객체 반환값
 */
aliceJs.fetchJson = function(url, option) {
    return aliceJs.doFetch(url, option)
        .then(response => response.text())
        .then((data) => {
            const jsonData = data ? JSON.parse(data) : {};
            // 상태 코드가 200이 아닐 경우 경고 표시
            if (jsonData.hasOwnProperty('status') && jsonData.status !== 200 &&
                jsonData.hasOwnProperty('message')) {
                zAlert.danger(jsonData.message);
            }
            return jsonData;
        });
};

/**
 * 비동기 통신 후 Promise 형태로 Text 데이터를 반환하는 함수
 * @param url url
 * @param option 옵션
 * @returns Promise 객체 반환값
 */
aliceJs.fetchText = function(url, option) {
    return aliceJs.doFetch(url, option)
        .then(response => response.text());
};

/**
 * 비동기 통신 후 Promise 형태로 Blob 데이터를 반환하는 함수
 * @param url url
 * @param option 옵션
 * @returns Promise 객체 반환값
 */
aliceJs.fetchBlob = function (url, option) {
    return aliceJs.doFetch(url, option)
        .then(response => response.blob());
};

/**
 * 믹스인을 추가하는 함수
 *
 * @param target 믹스인을 추가할 대상 객체의 prototype
 * @param source 믹스인
 * @return target 믹스인이 추가된 대상
 */
aliceJs.importMixin = function (target, source) {
    for (const key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
            const descriptor = Object.getOwnPropertyDescriptor(source, key);
            Object.defineProperty(target, key, descriptor);
        }
    }
    return target;
};

/**
 * 정규식에 부합하지 않는 글자들을 지운다.
 * @param event
 * @param rexg
 * @param flag
 */
aliceJs.removeChar = function (event, rexg = integerReg, flag = 'g') {
    event = event || window.event;
    let regToString = '[' + rexg.toString()
        .replaceAll('/', '')
        .replaceAll('[', '')
        .replaceAll(']', '')
        .replaceAll('*$', '') + ']';

    const keyID = (event.which) ? event.which : event.keyCode;
    if (!(keyID === 8 || keyID === 9 || keyID === 46 || keyID === 37 || keyID === 39)) {
        event.target.value = event.target.value.replace(new RegExp(regToString, flag), '');
    }
};


aliceJs.convertDateFormat = function (format, type, date) {
    let reformatDate = date;
    if (format === 'systemFormat') { //  ISO8601 규격 포멧으로 서버에 데이터 전달할때 사용 ex> 2021-06-29T15:00:00.000Z
        switch (type) {
            case 'dateTime':
                reformatDate = i18n.systemDateTime(date);
                break;
            case 'date':
                reformatDate = i18n.systemDate(date);
                break;
            case 'time':
                reformatDate = i18n.systemTime(date);
                break;
        }
    } else if (format === 'userFormat') {  //화면에 뿌려질 양식으로 서버에서 받은 데이터를 화면에 뿌려줄때 사용됨ex> 2021-06-21-15:30
        switch (type) {
            case 'dateTime':
                reformatDate = i18n.userDateTime(date);
                break;
            case 'date':
                reformatDate = i18n.userDate(date);
                break;
            case 'time':
                reformatDate = i18n.userTime(date);
                break;
        }
    }
    return reformatDate;
};

/**
 * alert div내부의 X 버튼을 클릭했을 때, 해당 div를 제거한다.
 * @param req
 */
aliceJs.removeTarget = function (req) {
    let target = req.parentElement;
    target.remove();
};

/**
 * input 엘리먼트 내부의 X 버튼을 클릭했을 때, 같은 레벨의 input value를 clear한다.
 * @param req
 */
aliceJs.clearText = function (req) {
    let target = req.parentElement.getElementsByTagName('input');
    for (let i = 0; i < target.length; i++) {
        target[i].value = '';
    }
};

/**
 * input+button 에 input value 초기화 x 버튼 출력
 * @param target
 */
aliceJs.inputButtonRemove = function(target) {
    let xTarget = target || document.querySelector('.input-button-remove-btn');
    if (xTarget !== null) {
        let inputValue = xTarget.previousElementSibling.value;
        if (inputValue === null || inputValue === '') {
            xTarget.classList.remove('active');
        } else {
            xTarget.classList.add('active');
        }
        xTarget.addEventListener('click', function (e) {
            e.preventDefault();
            this.previousElementSibling.value = null;
            this.classList.remove('active');
        });
    }
};

/**
 * 특정 키를 눌렀을시 함수 실행
 * @param event
 * @param keyName
 * @param callBackFunc
 */
aliceJs.pressKeyForAction = function(event, keyName, callBackFunc) {
    if (event.key === keyName) {
        callBackFunc();
    }
};

/**
 * z-slider > range value에 따라 range fill 영역을 계산한다.
 * @param target
 */
aliceJs.drawSlider = function(target) {
    let thumbLocation =  parseInt((target.value - 1) * 100 / (target.max - 1)) + '%';
    target.style.cssText = '--range-location:' + thumbLocation;
};

/**
 * validation message 처리
 * @param target     : validation message box를 띄울 target element
 * @param message    : validation message 텍스트
 * @param type       : validation message의 타입 (alert / success)
 * @param isAbsolute : message box의 position: absolute 처리 여부
 */
aliceJs.drawValidateMsg = function(target, message, type, isAbsolute) {
    // reset attributes
    document.querySelectorAll('.z-input').forEach(elem => {
        elem.addEventListener('input', () => el.removeAttribute('data-' + type));
    });

    // set validate message element
    let validateMsg = '';
    if (target.parentElement.querySelectorAll('.z-validation').length > 0) {
        validateMsg = target.parentElement.querySelector('.z-validation');
        validateMsg.textContent = i18n.msg(message);
    } else {
        validateMsg = document.createElement('div');
        validateMsg.className = 'z-validation ' + type;
        validateMsg.textContent = i18n.msg(message);
    }

    // set clear button
    let clearSpan = document.createElement('span');
    clearSpan.className = 'z-icon i-remove ml-auto';
    clearSpan.onclick = function() {
        aliceJs.removeTarget(this);
    };
    validateMsg.appendChild(clearSpan);

    // for absolute option
    if (isAbsolute) {
        const location = target.getBoundingClientRect();
        validateMsg.style.position = 'absolute';
        validateMsg.style.top = location.top + location.height + 2 + 'px';
        validateMsg.style.left = location.left + 'px';
        validateMsg.style.width = location.width + 'px';
    }

    target.after(validateMsg);
    target.setAttribute('data-' + type, 'true');
};

/**
 * noticePopup 데이터를 통해 팝업창을 생성한다.
 * @param noticePopupData : 공지사항 데이터
 */
aliceJs.openNoticePopup = function (noticePopupData) {
    noticePopupData.forEach((data) => {
        const targetId = data.noticeNo;
        const targetUrl = '/notices/' + targetId + '/view-pop';
        if (aliceJs.getCookie(targetId) !== 'done') {
            window.open(targetUrl, targetId, 'width=' + data.popWidth + ',height=' + data.popHeight + ',top=' + 150 + ',left=' + 150);
        }
    });
};

/**
 * cookie 데이터가 존재하는지 검사한다.
 * @param name : 공지사항 게시글의 noticeNo 데이터
 */
aliceJs.getCookie = function(name) {
    const nameOfCookie = name + '=';
    let x = 0;
    while (x <= document.cookie.length) {
        const y = (x + nameOfCookie.length);
        if (document.cookie.substring(x, y) == nameOfCookie) {
            if ((endOfCookie = document.cookie.indexOf(';', y)) == -1)
                endOfCookie = document.cookie.length;
            return unescape(document.cookie.substring(y, endOfCookie));
        }
        x = document.cookie.indexOf(' ', x) + 1;
        if (x == 0)
            break;
    }
    return '';
};

/**
 * String 형태의 HTML 데이터를 Element 로 변환
 * @param htmlString
 * @return {Element}
 */
aliceJs.makeElementFromString = function(htmlString) {
    let div = document.createElement('div');
    div.innerHTML = htmlString;
    return div.firstElementChild;
};

/**
 * Excel 다운로드
 *
 * @param option 옵션
 */
aliceJs.fetchDownload = function(option) {
    let url = option.url;
    let fileName = (option.fileName === undefined || option.fileName === null) ? '' : option.fileName;
    aliceJs.doFetch(url, {
        method: 'GET',
        showProgressBar: true
    }).then(response => {
        if (fileName === '') {
            fileName = response.headers.get('Content-Disposition').split(';')[1].split('=')[1];
        }
        return response.blob();
    }).then(blob => {
        if (typeof blob === 'object') {
            const a = document.createElement('a');
            const url = window.URL.createObjectURL(blob);
            a.href = url;
            a.download = fileName;
            document.body.append(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        }
    }).catch(err => {
        zAlert.warning(err);
    });
};
