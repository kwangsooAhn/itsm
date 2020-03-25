const aliceJs = {};

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

    const data = JSON.parse(text);
    let messages = [
        {key: '제목', text: data.error + ' (' + data.status + ')'},
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

    aliceJs.alert('Error !!')
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
        if (divProgressBar) {
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
    popup.alert({
            content: message,
            keyboard: true,
            btn_align: 'right',
            default_btns: {
                ok: 'OK'
            }
        },
        function() {
            if (typeof callbackFunc === 'function') {
                callbackFunc();
            }
        }
    );
};

/**
 * open confirm dialog.
 *
 * @param message message
 * @param callbackFunc callback function
 */
aliceJs.confirm = function(message, callbackFunc) {
    popup.confirm({
            content : message,
            keyboard: true,
            btn_align: 'right',
            default_btns: {
                ok: 'OK',
                cancel: 'CANCEL'
            }
        },
        function(param) {
            if (param.proceed && typeof callbackFunc === 'function') {
                callbackFunc();
            }
        }
    );
};

/**
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
    return format.replace(/YYYY/gi, aliceJs.parseZero(today.getFullYear(), 4))
        .replace(/MM/gi, aliceJs.parseZero(today.getMonth() + 1, 2))
        .replace(/DD/gi, aliceJs.parseZero(today.getDate(), 2))
        .replace(/hh/gi, aliceJs.parseZero(today.getHours(), 2))
        .replace(/mm/gi, aliceJs.parseZero(today.getMinutes(), 2));
};

/**
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
 * 사용자가 원하는 포맷으로 현재 시간을 format 형식에 따라 반환.
 * @param {String} 이전 날짜 포맷
 * @param {String} 변경하고자 하는 날짜 포맷
 * @param {String} 날짜
 * @return {String} format 변경된 시간
 */
aliceJs.changeDateFormat = function(beforeFormat, afterFormat, dateValue, userLang) {

    //이전 날짜 포맷 배열처리
    let beforeFormatArray = beforeFormat.split(' ');
    //변경 날짜 포맷 배열처리
    let afterFormatArray = afterFormat.split(' ');
    //입력 받은 날짜를 배열 처리
    let dateArray = dateValue.split(' ');
    let beforeDateArray; //날짜 처리
    let beforeHourArray; //날짜 처리
    //현재 날짜
    let year, month, day, hour, min = '';
    //반환 날짜
    let returnDate;

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

    //배열이 2개까지 있다면 12시간을 가지고 있다고 생각한다.
    if (dateArray[2] !== undefined && dateArray[2] !== null && dateArray[2] !== '') {
        if (dateArray[1] ==='오전' || dateArray[1] ==='오후' || dateArray[1] ==='AM' || dateArray[1] ==='PM') {
            beforeHourArray = dateArray[2].split(':');
        } else if (dateArray[2] ==='오전' || dateArray[2] ==='오후' || dateArray[2] ==='AM' || dateArray[2] ==='PM') {
            beforeHourArray = dateArray[1].split(':');
        }
        hour = beforeHourArray[0];
        min = beforeHourArray[1];
    } else if (dateArray[1] !== undefined && dateArray[1] !== null && dateArray[1] !== '') {
        beforeHourArray = dateArray[1].split(':');
        hour = beforeHourArray[0];
        min = beforeHourArray[1];
    }

    if (afterFormatArray[0].toUpperCase() === 'YYYY-MM-DD') {
        returnDate = year+'-'+aliceJs.parseZero(month,2)+'-'+aliceJs.parseZero(day,2);
    } else if(afterFormatArray[0].toUpperCase() === 'YYYY-DD-MM') {
        returnDate = year+'-'+aliceJs.parseZero(day,2)+'-'+aliceJs.parseZero(month,2);
    } else if(afterFormatArray[0].toUpperCase() === 'MM-DD-YYYY') {
        returnDate = aliceJs.parseZero(month,2)+'-'+aliceJs.parseZero(day,2)+'-'+year;
    } else if(afterFormatArray[0].toUpperCase() === 'DD-MM-YYYY') {
        returnDate = aliceJs.parseZero(day,2)+'-'+aliceJs.parseZero(month,2)+'-'+year;
    }

    if (hour !='' && min !='') {
        if (beforeFormatArray[2] != undefined && afterFormatArray[2] != undefined) {
            //이전, 이후 모두 12시간
            if (beforeFormatArray[2] === '12' && afterFormatArray[2] === '12') {
                returnDate = returnDate +' '+ aliceJs.parseZero(hour,2) +':'+  aliceJs.parseZero(min,2)+' '+ dateArray[1];
            } else if (beforeFormatArray[2] === '12' && afterFormatArray[2] === '24') {
                if (dateArray[1] === 'PM' || dateArray[1] === '오후') {
                    hour = eval(parseInt(hour) + 12);
                    if (hour === 24) {
                        hour = 23;
                    }
                }
                returnDate = returnDate +' '+ aliceJs.parseZero(hour,2) +':'+  aliceJs.parseZero(min,2);
            } else if (beforeFormatArray[2] === '24' && afterFormatArray[2] === '12') {
                if (hour > 11) {
                    hour = eval(hour - 12);
                    if (userLang === 'en') {
                        returnDate = returnDate +' '+ aliceJs.parseZero(hour,2) +':'+  aliceJs.parseZero(min,2)+' PM';
                    } else if(userLang === 'ko') {
                        returnDate = returnDate +' '+ aliceJs.parseZero(hour,2) +':'+  aliceJs.parseZero(min,2)+' 오후';
                    }
                } else {
                    if (userLang === 'en') {
                        returnDate = returnDate +' '+ aliceJs.parseZero(hour,2) +':'+  aliceJs.parseZero(min,2)+' AM';
                    } else if(userLang === 'ko') {
                        returnDate = returnDate +' '+ aliceJs.parseZero(hour,2) +':'+  aliceJs.parseZero(min,2)+' 오전';
                    }
                }
            } else if (beforeFormatArray[2] === '24' && afterFormatArray[2] === '24') {
                returnDate = returnDate +' '+ aliceJs.parseZero(hour,2) +':'+  aliceJs.parseZero(min,2);
            }
        }
    }

    return returnDate;
};