(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.formEditor = global.formEditor || {})));
}(this, (function (exports) {
    'use strict';

    let data = {};
    
    function init(form) {
        console.info('form editor initialization. [FORM ID: ' + form.formId + ']');

        // add save button event handler.
        document.querySelector('#btnSave').addEventListener('click', saveForm);

        // load form data.
        const xhr = createXmlHttpRequestObject('GET', '/rest/forms/data/' + form.formId);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    data = JSON.parse(xhr.responseText);
                    setComponents();
                } else if (xhr.status === 400) {
                    alert('There was an error 400');
                } else {
                    console.log(xhr);
                    alert('something else other than 200 was returned. ' + xhr.status);
                }
            }
        };
        xhr.send();
    }

    /**
     * 조회된 문서양식 데이터로 component 를 추가한다.
     */
    function setComponents() {
        //TODO: add component.
        console.debug(data);
    }

    /**
     * 문서양식 저장.
     */
    function saveForm() {
        const xhr = createXmlHttpRequestObject('POST', '/rest/forms/data');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    if (xhr.responseText === '1') { //TODO: return 값은 engine 쪽 개발자와 추후 협의 필요!! 현재는 임시로..
                        alert('저장되었습니다.');
                    } else {
                        alert('저장실패');
                    }
                } else if (xhr.status === 400) {
                    alert('There was an error 400');
                } else {
                    console.log(xhr);
                    alert('something else other than 200 was returned. ' + xhr.status);
                }
            }
        };
        xhr.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
        xhr.send(JSON.stringify(data));
    }
    
    exports.init = init;
    Object.defineProperty(exports, '__esModule', { value: true });
})));