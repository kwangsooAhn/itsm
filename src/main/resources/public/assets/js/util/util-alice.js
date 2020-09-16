const aliceJs = {};

aliceJs.systemCalendarDatetimeFormat = 'YYYY-MM-DD HH:mm:ss';
aliceJs.systemCalendarDateFormat = 'YYYY-MM-DD';
aliceJs.systemCalendarTimeFormat = 'HH:mm:ss';

const rgbaReg = /^rgba?\((\d+),(\d+),(\d+),?([^,\s)]+)?/i;
const hexReg = /^#([A-Fa-f0-9]{3}){1,2}$/;

aliceJs.searchDataCount = 15;

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
    aliceJs.alertWarning(data.message);
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
        if (!field.name || field.disabled || field.type === 'file' || field.type === 'reset' || field.type === 'submit' || field.type === 'button') continue;

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
            if (field.classList.contains('search-datetime')) {
                fieldValue = i18n.systemDateTime(encodeURIComponent(field.value));
            } else if (field.classList.contains('search-date')) {
                fieldValue = i18n.systemDate(encodeURIComponent(field.value));
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
        if (!field.name || field.disabled || field.type === 'file' || field.type === 'reset' || field.type === 'submit' || field.type === 'button') continue;

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
    return result
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
    return formDataObject
}

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
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            xhr = new XMLHttpRequest();
        }

    } catch (e) {
        aliceJs.alertDanger("Error creating the XMLHttpRequest object.");
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
    if (method.toUpperCase() !== "GET") {
        const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");
        const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
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
        divProgressBar.style.zIndex = 999;

        let imgProgressBar = document.createElement('img');
        imgProgressBar.src = '/assets/media/image/loading_w_dark.gif';
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
        v_date = p_date.replace(/ /gi, "");
        if (v_date.length === 10) {
            arrayDate[0] = v_date;
        } else if (v_date.length === 15) {
            arrayDate[0] = v_date.substring(0,10);
            index = v_date.lastIndexOf(':');
            arrayDate[1] = v_date.substring(index-2,index+3);
        } else if (v_date.length === 17) {
            arrayDate[0] = v_date.substring(0,10);
            index = v_date.lastIndexOf(':');
            arrayDate[1] = v_date.substring(index-2,index+3);
            if (p_date.indexOf('PM') !== -1 || p_date.indexOf('오후') !== -1) {
                if (p_date.indexOf('PM') !== -1) {
                    index = v_date.lastIndexOf('PM');
                } else if (p_date.indexOf('오후') !== -1) {
                    index = v_date.lastIndexOf('오후');
                }
                arrayDate[2] = v_date.substring(index-2,index+2);
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
                hour = eval(hour+12)
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
 */
function dateFormatFromNow(date) {
    let v_date = '';
    let p_date = new Date(i18n.userDateTime(date));
    let now = new Date();
    let diff, day, hour, min, sec;

    if (date === '' || date === null) {
        return;
    } else {
        if (now.getFullYear() > p_date.getFullYear()) {
            diff = now.getFullYear() - p_date.getFullYear();
            v_date = diff + '년 전';
        } else if (now.getMonth() > p_date.getMonth()) {
            diff = now.getMonth() - p_date.getMonth();
            v_date = diff + '달 전';
        } else if (now.getDate() > p_date.getDate()) {
            diff = now.getDate() - p_date.getDate();
            v_date = diff + '일 전';
        } else if (now.getDate() === p_date.getDate()) {
            let nowTime = now.getTime();
            let writeTime = p_date.getTime();
            if (nowTime > writeTime) {
                sec = parseInt(nowTime - writeTime) / 1000;
                day = parseInt(sec / 60 / 60 / 24);
                sec = (sec - (day * 60 * 60 * 24));
                hour = parseInt(sec / 60 / 60);
                sec = (sec - (hour * 60 * 60));
                min = parseInt(sec / 60);
                sec = parseInt(sec - (min * 60));
                if (hour > 0) {
                    v_date = hour + '시간 전';
                } else if (min > 0) {
                    v_date = min + '분 전';
                } else if (sec > 0) {
                    v_date = '방금 전';
                }
            }
        }
        return v_date;
    }
}

/**
 * open alert dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
aliceJs.alert = function(message, callbackFunc) {
    const myModal = new gModal({
        message: message,
        type: 'gmodal-icon-info',
        buttons: [
            {
                content: '닫기',
                bindKey: 13, /* Enter */
                callback: function(modal) {
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                    modal.hide();
                }
            }
        ],
        close: {
            closable: false,
        }
    });
    myModal.show();
};

/**
 * open alert dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
aliceJs.alertSuccess = function(message, callbackFunc) {
    const myModal = new gModal({
        message: message,
        type: 'gmodal-icon-success',
        buttons: [
            {
                content: '닫기',
                bindKey: 13, /* Enter */
                callback: function(modal) {
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                    modal.hide();
                }
            }
        ],
        close: {
            closable: false,
        }
    });
    myModal.show();
};

/**
 * open alert dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
aliceJs.alertWarning = function(message, callbackFunc) {
    const myModal = new gModal({
        message: message,
        type: 'gmodal-icon-warning',
        buttons: [
            {
                content: '닫기',
                bindKey: 13, /* Enter */
                callback: function(modal) {
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                    modal.hide();
                }
            }
        ],
        close: {
            closable: false,
        }
    });
    myModal.show();
};

/**
 * open error dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
aliceJs.alertDanger = function(message, callbackFunc) {
    const myModal = new gModal({
        message: message,
        type: 'gmodal-icon-danger',
        buttons: [
            {
                content: '닫기',
                bindKey: 13, /* Enter */
                callback: function(modal) {
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                    modal.hide();
                }
            }
        ],
        close: {
            closable: false,
        }
    });
    myModal.show();
};

/**
 * open confirm with icon dialog.
 *
 * @param message message
 * @param okCallbackFunc ok 시 callback function
 * @param cancelCallbackFunc cancel 시 callback function
 */
aliceJs.confirmIcon = function(message, okCallbackFunc, cancelCallbackFunc, param1, param2, param3, param4) {
    const myModal = new gModal({
        message: message,
        type: 'gmodal-icon-confirm',
        buttons: [
            {
                content: i18n.msg('common.btn.cancel'),
                bindKey: false, /* no key! */
                callback: function(modal) {
                    if (typeof cancelCallbackFunc === 'function') {
                        cancelCallbackFunc();
                    }
                    modal.hide();
                }
            },{
                content: i18n.msg('common.btn.check'),
                classes: 'gmodal-button-blue',
                bindKey: false, /* no key! */
                callback: function(modal) {
                    if (typeof okCallbackFunc === 'function') {
                        okCallbackFunc(param1, param2, param3, param4);
                    }
                    modal.hide();
                }
            }
        ],
        close: {
            closable: false,
        }
    });
    myModal.show();
};

/**
 * open confirm dialog.
 *
 * @param message message
 * @param okCallbackFunc ok 시 callback function
 * @param cancelCallbackFunc cancel 시 callback function
 */
aliceJs.confirm = function(message, okCallbackFunc, cancelCallbackFunc) {
    const myModal = new gModal({
        message: message,
        type: 'gmodal-no-icon',
        buttons: [
            {
                content: i18n.msg('common.btn.cancel'),
                bindKey: false, /* no key! */
                callback: function(modal) {
                    if (typeof cancelCallbackFunc === 'function') {
                        cancelCallbackFunc();
                    }
                    modal.hide();
                }
            },{
                content: i18n.msg('common.btn.check'),
                classes: 'gmodal-button-blue',
                bindKey: false, /* no key! */
                callback: function(modal) {
                    if (typeof okCallbackFunc === 'function') {
                        okCallbackFunc();
                    }
                    modal.hide();
                }
            }
        ],
        close: {
            closable: false,
        }
    });
    myModal.show();
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
                if (!target[key]) { Object.assign(target, { [key]: {} }); }
                aliceJs.mergeObject(target[key], source[key]);
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
    return value === "" || value == null || (typeof value == "object" && !Object.keys(value).length);
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
}

/**
 * RGBA 값을 Hex 값으로 변환.
 *
 * @param {string} value
 * @returns {string} rgba
 */
aliceJs.rgbaToHex = function(value) {
    let rgba = value.replace(/\s/g, '').match(rgbaReg)
    return rgba ?
        '#' +
        (rgba[1] | 1 << 8).toString(16).slice(1) +
        (rgba[2] | 1 << 8).toString(16).slice(1) +
        (rgba[3] | 1 << 8).toString(16).slice(1) : value;
}

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
    return alpha
}

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
}

/**
 * Hex 값 체크.
 *
 * @param {string} value
 * @returns {boolean}
 */
aliceJs.isHexCode = function(value) {
    return hexReg.test(value);
}

/**
 * RGBA 값 체크.
 *
 * @param {string} value
 * @returns {boolean}
 */
aliceJs.isRgba = function(value) {
    return rgbaReg.test(value);
}

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
}

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
}

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
    target.style.transitionProperty = "height, margin, padding";
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
}

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

        aliceJs.sendXhr({
            method: 'GET',
            url: imgUrl,
            contentType: 'image/svg+xml; charset=utf-8',
            callbackFunc: function(xhr) {
                let svgFile = xhr.responseXML;
                let svg = svgFile.documentElement;

                if (typeof imgId !== 'undefined' && imgId !== '') {
                    svg.setAttribute('id', imgId);
                }

                if (typeof imgClass !== 'undefined' && imgClass !== '') {
                    svg.setAttribute('class', imgClass);
                }

                img.insertAdjacentHTML('beforebegin', svg.outerHTML);
                img.remove();
            }
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
aliceJs.isEnableScrollEvent = function(offset, objectId = "totalCount") {
    let totalObject = document.getElementById(objectId);
    return offset < totalObject.value;
}
