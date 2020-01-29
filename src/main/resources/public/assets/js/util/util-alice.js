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

    alert('Error !!')
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

    let xhr;
    try {
        if (window.ActiveXObject) {
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            xhr = new XMLHttpRequest();
        }

    } catch (e) {
        alert("Error creating the XMLHttpRequest object.");
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
            showProgressBar();
        } else if (this.readyState === 2) {
            //console.log('요청 접수, send메서드가 불렸지만 status와 헤더는 아직 도착하지 않음(' + this.status + ')');
        } else if (this.readyState === 3) {
            //console.log('처리 요청, 데이터의 일부를 받은 상태(' + this.status + ')');
        } else if (this.readyState === 4 && this.status === 200) {
            //console.log('요청 완료및 응답 준비, 데이터를 전부 받음(' + this.status + ')');
            aliceJs.xhrErrorResponse('printError');
            if (typeof callbackFunc === 'function') {
                callbackFunc(this);
            } else {
                console.info('No callback function');
            }
            hiddenProgressBar();
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
            hiddenProgressBar();
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
    xhr.send(params);
};

function createXmlHttpRequestObject(method, url, async) {
    showProgressBar();
    // will store the reference to the XMLHttpRequest object
    var xmlHttp;
    var token;
    var metas = document.getElementsByTagName('meta');

    if (typeof async === "undefined") {
        async = true;
    }

    // if running Internet Explorer
    if (window.ActiveXObject) {
        try {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e) {
            xmlHttp = false;
        }
    }
    // if running Mozilla or other browsers
    else {
        try {
            xmlHttp = new XMLHttpRequest();
        } catch (e) {
            xmlHttp = false;
        }
    }
    // return the created object or display an error message
    if (!xmlHttp) {
        alert("Error creating the XMLHttpRequest object.");
    } else {
        if (method.toUpperCase() != "GET") {
            for (var i = 0; i < metas.length; i++) {
                if (metas[i].getAttribute('name') === '_csrf') {
                    token = metas[i].getAttribute('content');
                }
            }
            xmlHttp.open(method, url, async);
            xmlHttp.setRequestHeader('X-CSRF-TOKEN', token);
        } else {
            xmlHttp.open(method, url, async);
        }
        return xmlHttp;
    }
    hiddenProgressBar();
}

/*
 * ProgressBar 보여줌
 */
function showProgressBar() {
    //divProgressBar 적용이 되지 않을떄는 그냥 넘어가도록 조치
    var divCheck = document.getElementById('divProgressBar');
    if (divCheck === null) {
        var divProgressBar = document.createElement('div');
        divProgressBar.id = 'divProgressBar';
        divProgressBar.style.zIndex = 1000;
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
        divProgressBar.appendChild(imgProgressBar);
        document.body.appendChild(divProgressBar);
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
 * 메시지를 받아서 다국어를 반환한다.
 * @param messageId 다국어 yaml id를 받는다.
 */
function i18n(messageId) {
    var result = '';
    var strUrl = '/i18n/'+ messageId;
    const opt = {
             method: 'GET',
             url: strUrl,
             async: false,
             callbackFunc: function(response) {
                 result = response.responseText;
             }
    };
    aliceJs.sendXhr(opt);
    return result;
}
