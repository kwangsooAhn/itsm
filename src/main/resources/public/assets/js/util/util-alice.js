const aliceJs = {};

aliceJs.systemCalendarDatetimeFormat = 'YYYY-MM-DD HH:mm:ss';
aliceJs.systemCalendarDateFormat = 'YYYY-MM-DD';
aliceJs.systemCalendarTimeFormat = 'HH:mm:ss';

/**
 *  XMLHttpReqeust 응답시 에러 발생하는 경우 호출
 *
 * @param elementId 에러를 출력할 장소 element id
 * @param text response text
 */
aliceJs.xhrErrorResponse = function (elementId, text) {
    let elmNode = document.getElementById(elementId);

    if (elmNode == null) {
        elmNode = document.createElement('div');
        elmNode.setAttribute('id', 'printError');
        document.getElementsByTagName('body').item(0).appendChild(elmNode);
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
    elmNode.appendChild(table);
    console.log(data);
    aliceJs.alert('[' + data.status + ']' + data.error + '<br/>' + data.message)
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
                serialized.push(encodeURIComponent(field.name) + "=" + encodeURIComponent(field.options[n].value));
            }
        }

        // Convert field data to a query string
        else if ((field.type !== 'checkbox' && field.type !== 'radio') || field.checked) {
            serialized.push(encodeURIComponent(field.name) + "=" + encodeURIComponent(field.value));
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
        aliceJs.alert("Error creating the XMLHttpRequest object.");
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
    var divCheck = document.getElementById('divProgressBar');
    if (divCheck === null) {
        var divProgressBar = document.createElement('div');
        divProgressBar.id = 'divProgressBar';
        divProgressBar.style.position = 'fixed';
        divProgressBar.style.display = 'block';
        divProgressBar.style.width = '100%';
        divProgressBar.style.height = '100%';
        divProgressBar.style.top = '0';
        divProgressBar.style.left = '0';
        divProgressBar.style.right = '0';
        divProgressBar.style.bottom = '0';
        divProgressBar.style.backgroundColor = 'grey';
        divProgressBar.style.opacity = 0.5;    
        divProgressBar.style.pointerEvents = 'all';
        
        var imgProgressBar = document.createElement('img');
        imgProgressBar.src = '/assets/media/image/loading_w_dark.gif';
        imgProgressBar.style.position = 'absolute';
        imgProgressBar.style.left = '50%';
        imgProgressBar.style.top = '0';
        imgProgressBar.style.bottom = '0';
        imgProgressBar.style.margin = 'auto';
        if (divProgressBar && document.body) {
            divProgressBar.appendChild(imgProgressBar);
            document.body.appendChild(divProgressBar);
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

/*
 * 첨부파일 삭제
 */
function delFileCheck() {
    const f_length = document.getElementsByName('delFileSeq').length;
    if (f_length > 0) {
        document.getElementsByName('delFileSeq').forEach(function(elm) {
            const delOpt = {
                method: 'delete',
                url: '/filedel?seq=' + Number(elm.value)
            };
            aliceJs.sendXhr(delOpt);
        });
    }
    return true;
}

/**
 * open alert dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
aliceJs.alert = function(message, callbackFunc) {
    const myModal = new gModal({
        title: 'Alert',
        body: '<div style="text-align: center;">' + message + '</div>',
        buttons: [
            {
                content: 'OK',
                classes: 'gmodal-button-green',
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
 * open confirm dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
aliceJs.confirm = function(message, callbackFunc) {
    const myModal = new gModal({
        title: 'Confirm',
        body: '<div style="text-align: center;">' + message + '</div>',
        buttons: [
            {
                content: 'Cancel',
                classes: 'gmodal-button-red',
                bindKey: false, /* no key! */
                callback: function(modal) {
                    modal.hide();
                }
            },{
                content: 'OK',
                classes: 'gmodal-button-green',
                bindKey: false, /* no key! */
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
 * Deprecated (2020-05-20, hcjung) : 현재 사용중인 부분들 리펙토링 후 삭제 예정.
 * 현재 방식으로는 Client의 시간을 가져오는데 이건 사용하면 안됨.
 *
 * 현재 시간을 format 형식에 따라 반환.
 * @param {String} format format
 * @param {String} day 날짜 간격(3 = 현재 날짜의 3일 후, -3 = 현재 날짜의 3일전을 의미)
 * @param {String} time 시간 간격(3 = 현재 시간 기준 3시간 후, -3 = 현재 시간기준 3시간 전을 의미)
 * @return {String} format 변경된 시간
 */
//TODO: datepicker 라이브러리 사용과 동일한 형식으로 12시, 24시일 경우 출력되도록 수정 필요.
aliceJs.getTimeStamp = function(format, day, time) {
    const today = new Date();

    if (day !== undefined && day !== null && day !== '') {
        today.setDate(today.getDate() + Number(day));
    }
    if (time !== undefined && time !== null && time !== '') {
        today.setHours(today.getHours() + Number(time));
    }

    return format.replace(/YYYY/g, aliceJs.parseZero(today.getFullYear(), 4))
        .replace(/MM/g, aliceJs.parseZero(today.getMonth() + 1, 2))
        .replace(/DD/g, aliceJs.parseZero(today.getDate(), 2))
        .replace(/hh/g, aliceJs.parseZero(today.getHours(), 2))
        .replace(/mm/g, aliceJs.parseZero(today.getMinutes(), 2));
};

/**
 * Deprecated (2020-05-20, hcjung) : 현재 사용중인 부분들 리펙토링 후 삭제 예정.
 * 이렇게 Low 레벨의 컨트롤을 할 필요는 없을 듯.
 * moment 라이브러리 사용이 필요.
 *
 * 시분초에 length가 변경될 경우 0 붙이는 함수이다.
 * 예를 들어 1월은 01월 3시 일경우 03시등으로 변경하기 위해 사용한다.
 * @param {Number} num 날짜, 시간 값
 * @param {Number} digits 자릿수
 * @return {String} zero + num 변경된 날짜 시간 값
 */
aliceJs.parseZero = function(num, digits) {
    let zero = '';
    num = num.toString();
    if (num.length < digits) {
        for (let i = 0; i < (digits - num.length); i++) {
            zero += '0';
        }
    }
    return zero + num;
};

/**
 * Deprecated (2020-05-20, hcjung) : 현재 사용중인 부분들 리펙토링 후 삭제 예정.
 * moment 라이브러리 사용으로 불필요.
 *
 * 사용자가 원하는 포맷으로 현재 시간을 format 형식에 따라 반환.
 * @param {String} 이전 날짜 포맷              EX) YYYY-MM-DD YY hh:mm 12
 * @param {String} 변경하고자 하는 날짜 포맷   EX) YYYY-MM-DD hh:mm 12
 * @param {String} 날짜                       EX) 2020-03-31 PM 01:00 or 2020-03-31 13:00
 * @param {String} 언어                       EX) en, ko
 * @return {String} 변경된 날짜               EX) 2020-03-31 PM 01:00 or 2020-03-31 13:00
 */
aliceJs.changeDateFormat = function(beforeFormat, afterFormat, dateValue, userLang) {
    //반환 날짜
    let returnDate;
    if (beforeFormat != undefined && afterFormat != undefined && dateValue !== '') {
        if (beforeFormat === afterFormat) { return dateValue; }
        //이전 날짜 포맷 배열처리
        let beforeFormatArray = beforeFormat.split(' ');
        //변경 날짜 포맷 배열처리
        let afterFormatArray = afterFormat.split(' ');
        //입력 받은 날짜를 배열 처리
        let dateArray = dateValue.split(' ');
        let beforeDateArray; //날짜 처리
        let beforeHourArray; //시간 처리
        //현재 날짜
        let year, month, day, hour, min = '';

        if (beforeFormatArray[0].toUpperCase() === 'YYYY-MM-DD') {
            beforeDateArray = dateArray[0].split('-');
            year = beforeDateArray[0];
            month = beforeDateArray[1];
            day = beforeDateArray[2];
        } else if (beforeFormatArray[0].toUpperCase() === 'YYYY-DD-MM') {
            beforeDateArray = dateArray[0].split('-');
            year = beforeDateArray[0];
            month = beforeDateArray[2];
            day = beforeDateArray[1];
        } else if (beforeFormatArray[0].toUpperCase() === 'MM-DD-YYYY') {
            beforeDateArray = dateArray[0].split('-');
            year = beforeDateArray[2];
            month = beforeDateArray[0];
            day = beforeDateArray[1];
        } else if (beforeFormatArray[0].toUpperCase() === 'DD-MM-YYYY') {
            beforeDateArray = dateArray[0].split('-');
            year = beforeDateArray[2];
            month = beforeDateArray[1];
            day = beforeDateArray[0];
        }

        if (dateArray[2] !== undefined) {
            if (dateArray[1] === '오전' || dateArray[1] === '오후' || dateArray[1] === 'AM' || dateArray[1] === 'PM') {
                beforeHourArray = dateArray[2].split(':');
            } else if (dateArray[2] === '오전' || dateArray[2] === '오후' || dateArray[2] === 'AM' || dateArray[2] === 'PM') {
                beforeHourArray = dateArray[1].split(':');
            }
            hour = beforeHourArray[0];
            min = beforeHourArray[1];
        } else if (dateArray[1] !== undefined) {
            beforeHourArray = dateArray[1].split(':');
            hour = beforeHourArray[0];
            min = beforeHourArray[1];
        }

        if (afterFormatArray[0].toUpperCase() === 'YYYY-MM-DD') {
            returnDate = year + '-' + aliceJs.parseZero(month, 2) + '-' + aliceJs.parseZero(day, 2);
        } else if (afterFormatArray[0].toUpperCase() === 'YYYY-DD-MM') {
            returnDate = year + '-' + aliceJs.parseZero(day, 2) + '-' + aliceJs.parseZero(month, 2);
        } else if (afterFormatArray[0].toUpperCase() === 'MM-DD-YYYY') {
            returnDate = aliceJs.parseZero(month, 2) + '-' + aliceJs.parseZero(day, 2) + '-' + year;
        } else if (afterFormatArray[0].toUpperCase() === 'DD-MM-YYYY') {
            returnDate = aliceJs.parseZero(day, 2) + '-' + aliceJs.parseZero(month, 2) + '-' + year;
        }

        if (hour !== '' && hour !== undefined && min !== '' && min !== undefined) {
            if (beforeFormatArray[2] !== undefined && beforeFormatArray[2] === 'a' && afterFormatArray[2] !== undefined && afterFormatArray[2] === 'a') {
                if (userLang === 'en') {
                    if (dateArray[1] === '오전') {
                        dateArray[1] = 'AM';
                    } else if (dateArray[1] === '오후') {
                        dateArray[1] = 'PM';
                    }
                } else if (userLang === 'ko') {
                    if (dateArray[1] === 'AM') {
                        dateArray[1] = '오전';
                    } else if (dateArray[1] === 'PM') {
                        dateArray[1] = '오후';
                    }
                }
                returnDate = returnDate + ' ' + aliceJs.parseZero(hour, 2) + ':' + aliceJs.parseZero(min, 2) + ' ' + dateArray[1];
            } else if (beforeFormatArray[2] !== undefined && beforeFormatArray[2] === 'a') {
                if (dateArray[1] === 'PM' || dateArray[1] === '오후') {
                    hour = eval(parseInt(hour) + 12);
                    if (hour === 24) {
                        hour = 23;
                    }
                }
                returnDate = returnDate + ' ' + aliceJs.parseZero(hour, 2) + ':' + aliceJs.parseZero(min, 2);
            } else if (afterFormatArray[2] !== undefined && afterFormatArray[2] === 'a') {
                if (hour > 11) {
                    hour = eval(hour - 12);
                    if (userLang === 'en') {
                        returnDate = returnDate + ' ' + aliceJs.parseZero(hour, 2) + ':' + aliceJs.parseZero(min, 2) + ' PM';
                    } else if (userLang === 'ko') {
                        returnDate = returnDate + ' ' + aliceJs.parseZero(hour, 2) + ':' + aliceJs.parseZero(min, 2) + ' 오후';
                    }
                } else {
                    if (userLang === 'en') {
                        returnDate = returnDate + ' ' + aliceJs.parseZero(hour, 2) + ':' + aliceJs.parseZero(min, 2) + ' AM';
                    } else if (userLang === 'ko') {
                        returnDate = returnDate + ' ' + aliceJs.parseZero(hour, 2) + ':' + aliceJs.parseZero(min, 2) + ' 오전';
                    }
                }
            } else {
                returnDate = returnDate + ' ' + aliceJs.parseZero(hour, 2) + ':' + aliceJs.parseZero(min, 2);
            }
        }
    }
    return returnDate;
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
 * 시스템 기본 포맷으로 된 날짜와 시간 데이터를 사용자의 타임존과 포맷으로 변환.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 * @param {String} beforeDatetime 시스템 포맷으로 된 변경될 날짜
 * @param {String} userDatetimeFormat 사용자 날짜, 시간 포맷
 * @param {String} userTimezone 사용자 타임존
 * @return {String} resultDatetime 변환된 결과 날짜와 시간
 */
aliceJs.convertToUserDatetimeFormatWithTimezone = function(beforeDatetime, userDatetimeFormat, userTimezone) {
    if (aliceJs.isEmpty(beforeDatetime) || aliceJs.isEmpty(userDatetimeFormat) || aliceJs.isEmpty(userTimezone)
        || !moment(beforeDatetime, aliceJs.systemCalendarDatetimeFormat).isValid()) {
        return beforeDatetime;
    }

    // 콘솔에서 아래와 같이 테스트 가능.
    // moment.tz('2020-05-22 17:33', 0).tz('America/Anchorage').format('MM-DD-YYYY hh:mm A');
    let resultDatetime = moment.tz(beforeDatetime, aliceJs.systemCalendarDatetimeFormat, 0)
        .tz(userTimezone)
        .format(userDatetimeFormat);
    return resultDatetime;
}

/**
 * 시스템 기본 포맷으로 된 날짜와 시간 데이터를 사용자의 포맷으로 변환.
 *
 * @since 2020-05-28
 * @param {String} beforeDatetime 시스템 포맷으로 된 변경될 날짜
 * @param {String} userDatetimeFormat 사용자 날짜, 시간 포맷
 * @return {String} resultDatetime 변환된 결과 날짜와 시간
 */
aliceJs.convertToUserDatetimeFormat = function(beforeDatetime, userDatetimeFormat) {
    if (aliceJs.isEmpty(beforeDatetime) || aliceJs.isEmpty(userDatetimeFormat)
        || !moment(beforeDatetime, aliceJs.systemCalendarDatetimeFormat).isValid()) {
        return beforeDatetime;
    }

    let resultDatetime = moment(beforeDatetime, aliceJs.systemCalendarDatetimeFormat).format(userDatetimeFormat);
    return resultDatetime;
}

/**
 * 시스템 공통 포맷의 시간 데이터를 사용자 시간 포맷으로 변경. 타임존 개념은 없음.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 * @param {String} beforeTime 사용자 포맷으로 된 변경될 원본 시간
 * @param {String} userTimeFormat 사용자 시간 포맷
 * @return {String} resultDatetime 변환된 결과 시간
 */
aliceJs.convertToUserTimeFormat = function(beforeTime, userTimeFormat) {
    if (aliceJs.isEmpty(beforeTime) || aliceJs.isEmpty(userTimeFormat)
        || !moment(beforeTime, aliceJs.systemCalendarTimeFormat).isValid()) {
        return beforeTime;
    }

    // 콘솔에서 아래와 같이 테스트 가능.
    // moment('17:33', 'HH:mm:ss').format('hh:mm A');
    let resultTime = moment(beforeTime, aliceJs.systemCalendarTimeFormat)
        .format(userTimeFormat);
    return resultTime;
}

/**
 * 시스템 공통 포맷의 날짜 데이터를 사용자 날짜 포맷으로 변경. 타임존 개념은 없음.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 * @param {String} beforeDate 사용자 포맷으로 된 변경될 원본 날짜
 * @param {String} userDateFormat 사용자 날짜 포맷
 * @return {String} resultDatetime 변환된 결과 날짜
 */
aliceJs.convertToUserDateFormat = function(beforeDate, userDateFormat) {
    if (aliceJs.isEmpty(beforeDate) || aliceJs.isEmpty(userDateFormat)
        || !moment(beforeDate, aliceJs.systemCalendarDateFormat).isValid()) {
        return beforeDate;
    }

    // 콘솔에서 아래와 같이 테스트 가능.
    // moment('2020-05-25', 'YYYY-MM-DD').format('MM-DD-YYYY');
    let resultDate = moment(beforeDate, aliceJs.systemCalendarDateFormat)
        .format(userDateFormat);
    return resultDate;
}

/**
 * 사용자의 포맷과 타임존으로 된 날짜와 시간을 시스템 기본 포맷으로 변환.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 * @param {String} beforeDatetime 사용자의 포맷과 타임존이 적용된 변경될 날짜,시간
 * @param {String} userDatetimeFormat 사용자 날짜,시간 포맷
 * @param {String} userTimezone 사용자 타임존
 * @return {String} resultDatetime 변환된 결과 날짜,시간
 */
aliceJs.convertToSystemDatetimeFormatWithTimezone = function(beforeDatetime, userDatetimeFormat, userTimezone) {
    beforeDatetime = aliceJs.convertToSystemHourType(beforeDatetime);
    if (aliceJs.isEmpty(beforeDatetime) || aliceJs.isEmpty(userDatetimeFormat) || aliceJs.isEmpty(userTimezone) ||
            !moment.tz(beforeDatetime, userDatetimeFormat, userTimezone).isValid()) {
        return beforeDatetime;
    }

    // 콘솔에서 아래와 같이 테스트 가능.
    // moment.tz('05-22-2020 09:33 AM', 'MM-DD-YYYY hh:mm A', 'America/Anchorage').utc(0).format('YYYY-MM-DD HH:mm');
    let resultDatetime = moment.tz(beforeDatetime, userDatetimeFormat, userTimezone)
        .utc(0)
        .format(aliceJs.systemCalendarDatetimeFormat);

    return resultDatetime;
}

/**
 * 사용자의 포맷으로 된 날짜를 시스템 기본 포맷으로 변환.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 * @param {String} beforeDatetime 사용자의 포맷이 적용된 변경될 날짜
 * @param {String} userDatetimeFormat 사용자 날짜 포맷
 * @return {String} resultDatetime 변환된 결과 날짜
 */
aliceJs.convertToSystemDateFormat = function(beforeDate, userDateFormat) {
    if (aliceJs.isEmpty(beforeDate) || aliceJs.isEmpty(userDateFormat) ||
        !moment(beforeDate, userDateFormat).isValid()) {
        return beforeDate;
    }

    // 콘솔에서 아래와 같이 테스트 가능.
    // moment('05-22-2020', 'MM-DD-YYYY').format('YYYY-MM-DD');
    let resultDate = moment(beforeDate, userDateFormat)
        .format(aliceJs.systemCalendarDateFormat);

    return resultDate;
}

/**
 * 사용자의 포맷으로 된 시간을 시스템 기본 포맷으로 변환.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 * @param {String} beforeTime 사용자의 포맷이 적용된 변경될 시간
 * @param {String} userTimeFormat 사용자 시간 포맷
 * @return {String} resultDatetime 변환된 결과 시간
 */
aliceJs.convertToSystemTimeFormat = function(beforeTime, userTimeFormat) {
    beforeTime = aliceJs.convertToSystemHourType(beforeTime);
    if (aliceJs.isEmpty(beforeTime) || aliceJs.isEmpty(userTimeFormat) ||
        !moment(beforeTime, userTimeFormat).isValid()) {
        return beforeTime;
    }

    // 콘솔에서 아래와 같이 테스트 가능.
    // moment('09:33 AM', 'hh:mm A').format('HH:mm');
    let resultTime = moment(beforeTime, userTimeFormat)
        .format(aliceJs.systemCalendarTimeFormat);

    return resultTime;
}

/**
 * 사용자의 타임존을 기준으로 현재시간을 사용자 날짜,시간 포맷으로 반환.
 *
 * @author Jung Hee chan
 * @since 2020-05-20
 *
 * @param {String} userTimezone 사용자의 타임존 정보.
 * @param {String} [userFormat=aliceJs.systemCalendarDatetimeFormat] 사용자의 날짜, 시간 포맷.
 * @return {String} UTC+0 기준으로 변경된 시스템 기본 포맷의 현재 시간
 */
aliceJs.getCurrentDatetimeWithTimezoneAndFormat = function(userTimezone, userFormat) {
    let resultDatetime;
    let momentObject = moment().utc(0);
    if (!aliceJs.isEmpty(userTimezone)) {
        momentObject = moment.tz(userTimezone);
    }

    if (aliceJs.isEmpty(userFormat)) {
        resultDatetime = momentObject.format(aliceJs.systemCalendarDatetimeFormat);
    } else {
        resultDatetime = momentObject.format(userFormat);
    }
    return resultDatetime;
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
 * 한글로 '오전','오후'로 표기된 내용을 DB에 넣기 위해 'AM','PM'으로 치환.
 * 현재 다국어 처리와 관련해서
 * DB -> 화면으로 가는 경우에는 picker에서 초기화할때 처리하지만 아래 2가지는 해결해야함.
 * 1) 화면 -> DB로 가는 경우 : 여기서 변경 임시로 처리
 * 2) DB -> 화면으로 가지만 picker가 동작하지 않는 경우 : #8548 일감으로 별도 처리
 *
 * 위의 2가지를 포함하여 개선 여지가 있음.
 *
 * @author Jung Hee Chan
 * @since 2020-05-27
 */
aliceJs.convertToSystemHourType = function(value) {
    if (value.indexOf('오후') !== -1) {
        value = value.replace('오후', 'PM');
    } else if (value.indexOf('오전') !== -1) {
        value = value.replace('오전', 'AM');
    }
    return value;
};
