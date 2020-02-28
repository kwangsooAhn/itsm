(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceDocument = global.aliceDocument || {})));
}(this, (function (exports) {
    'use strict';

    let documentContainer = null;
    const defaultColWidth = 8.33; //폼 패널을 12등분하였을때, 1개의 너비

    const numIncludeRegular = /[0-9]/gi;
    const numRegular = /^[0-9]*$/;
    const emailRegular = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

    /**
     * 현재 시간 format 형식에 따라 반환.
     *
     * @param format format
     */
    //TODO: 임시 메소드. 추후 수정 필요.
    function getTimeStamp(format) {
        const today = new Date();
        return format.replace(/yyyy/gi, parseZero(today.getFullYear(), 4))
                     .replace(/MM/gi, parseZero(today.getMonth() + 1, 2))
                     .replace(/dd/gi, parseZero(today.getDate(), 2))
                     .replace(/HH/gi, parseZero(today.getHours(), 2))
                     .replace(/mm/gi, parseZero(today.getMinutes(), 2));
    }

    function parseZero(n, digits) {
        let zero = '';
        n = n.toString();
        if (n.length < digits) {
            for (let i = 0; i < (digits - n.length); i++)
                zero += '0';
        }
        return zero + n;
    }

    /**
     * alert message.
     *
     * @param element element
     * @param msg 메시지
     */
    function alertMsg(element, msg) {
        alert(msg);
        setTimeout(function() { element.focus(); }, 10);
    }

     /**
     * Validation Check.
     *
     * @param element element
     * @param validateData validate 데이터
     */
    function validateCheck(element, validateData) {
        const chkVal = element.value.trim();
        if (chkVal.length !== 0) {
            for (let key in validateData) {
                const value = validateData[key];
                if (value !== '') {
                    if (key === 'regexp') {
                        switch (value) {
                            case 'char':
                                if (numIncludeRegular.test(chkVal)) {
                                    alertMsg(element, validateData['regexp-msg']);
                                    return true;
                                }
                                break;
                            case 'num':
                                if (!numRegular.test(chkVal)) {
                                    alertMsg(element, validateData['regexp-msg']);
                                    return true;
                                }
                                break;
                            case 'email':
                                if (!emailRegular.test(chkVal)) {
                                    alertMsg(element, validateData['regexp-msg']);
                                    return true;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    if (key === 'length-min' && value > chkVal.length) {
                        alertMsg(element, i18n.get('document.msg.lengthMin', value));
                        return true;
                    }
                    if (key === 'length-max' && value < chkVal.length) {
                        alertMsg(element, i18n.get('document.msg.lengthMax', value));
                        return true;
                    }
                    if (key === 'date-min' && value > chkVal) {
                        alertMsg(element, i18n.get('document.msg.dateMin', value));
                        return true;
                    }
                    if (key === 'date-max' && value < chkVal) {
                        alertMsg(element, i18n.get('document.msg.dateMax', value));
                        return true;
                    }
                }
            }
        }
    }

    /**
     * add Component.
     *
     * @param compData 컴포넌트 데이터
     */
    function addComponent(compData) {
        const comp = document.createElement('div');
        comp.id = compData.id;
        comp.className = 'component';
        comp.setAttribute('data-type', compData.type);

        const fieldFirstEle = document.createElement('div');
        const fieldLastEle = document.createElement('div');
        fieldFirstEle.className = 'field';
        fieldLastEle.className = 'field';

        const lblData = compData.label;
        const displayData = compData.display;
        const validateData = compData.validate;

        if (typeof lblData !== 'undefined' && lblData.position !== 'hidden') {
            const lblEle = document.createElement('div');
            lblEle.className = 'label';
            lblEle.style.fontSize = lblData.size + 'px';
            lblEle.style.color = lblData.color;
            lblEle.style.fontWeight = (lblData.bold === 'Y') ? 'bold' : '';
            lblEle.style.fontStyle = (lblData.italic === 'Y') ? 'italic' : '';
            lblEle.style.textDecoration = (lblData.underline === 'Y') ? 'underline' : '';
            lblEle.innerHTML = lblData.text;
            fieldFirstEle.style.textAlign = lblData.align;
            fieldFirstEle.appendChild(lblEle);

            if (validateData.required === 'Y') {
                const requiredEle = document.createElement('span');
                requiredEle.className  = 'required';
                requiredEle.innerText = '*';
                fieldFirstEle.append(requiredEle);
            }
            if (lblData.position === 'left') {
                comp.style.display = 'flex';
                fieldFirstEle.style.flexBasis = (defaultColWidth * Number(lblData.column)) + '%';
                fieldLastEle.style.flexBasis = (defaultColWidth * Number(displayData.column)) + '%';
            }
            comp.appendChild(fieldFirstEle);
        }
        comp.appendChild(fieldLastEle);
        documentContainer.appendChild(comp);

        switch (compData.type) {
            case 'text':
                const textEle = document.createElement('input');
                textEle.type = 'text';
                textEle.placeholder = displayData.placeholder;
                textEle.style.outlineWidth = displayData['outline-width'] + 'px';
                textEle.style.outlineColor = displayData['outline-color'];
                textEle.minLength = validateData['length-min'];
                textEle.maxLength = validateData['length-max'];
                textEle.required = (validateData.required === 'Y');
                textEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(textEle);
                break;
            case 'textarea':
                const textareaEle = document.createElement('textarea');
                textareaEle.placeholder = displayData.placeholder;
                textareaEle.style.outlineWidth = displayData['outline-width'] + 'px';
                textareaEle.style.outlineColor = displayData['outline-color'];
                textareaEle.minLength = validateData['length-min'];
                textareaEle.maxLength = validateData['length-max'];
                textareaEle.required = (validateData.required === 'Y');
                textareaEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(textareaEle);
                break;
            case 'select':
                const selectEle = document.createElement('select');
                selectEle.required = (validateData.required === 'Y');
                const optData = compData.option;
                optData.sort(function(a, b) {
                    return a.seq - b.seq;
                });
                for (let i = 0; i < optData.length; i++) {
                    const optEle = document.createElement('option');
                    optEle.text = optData[i].name;
                    optEle.value = optData[i].value;
                    selectEle.appendChild(optEle);
                }
                fieldLastEle.appendChild(selectEle);
                break;
            case 'radio':
                const radioOptData = compData.option;
                radioOptData.sort(function(a, b) {
                    return a.seq - b.seq;
                });
                for (let i = 0; i < radioOptData.length; i++) {
                    const divEle = document.createElement('div');
                    if (displayData.direction === 'horizontal') { divEle.style.display = 'inline-block'}
                    const radioEle = document.createElement('input');
                    radioEle.type = 'radio';
                    radioEle.name = 'radio-' + compData.id;
                    radioEle.id = radioOptData[i].value;
                    radioEle.value = radioOptData[i].value;
                    radioEle.checked = (i === 0);
                    radioEle.required = (i === 0 && validateData.required === 'Y');

                    const lblEle = document.createElement('label');
                    lblEle.innerText = radioOptData[i].name;
                    lblEle.setAttribute('for', radioOptData[i].value);
                    if (displayData.position === 'left') {
                        divEle.appendChild(radioEle);
                        divEle.appendChild(lblEle);
                    } else {
                        divEle.appendChild(lblEle);
                        divEle.appendChild(radioEle);
                    }
                    fieldLastEle.appendChild(divEle);
                }
                break;
            case 'checkbox':
                const checkOptData = compData.option;
                checkOptData.sort(function(a, b) {
                    return a.seq - b.seq;
                });
                for (let i = 0; i < checkOptData.length; i++) {
                    const divEle = document.createElement('div');
                    if (displayData.direction === 'horizontal') { divEle.style.display = 'inline-block'}
                    const checkEle = document.createElement('input');
                    checkEle.type = 'checkbox';
                    checkEle.name = 'check-' + compData.id;
                    checkEle.id = checkOptData[i].value;
                    checkEle.value = checkOptData[i].value;
                    checkEle.required = (i === 0 && validateData.required === 'Y');

                    const lblEle = document.createElement('label');
                    lblEle.innerText = checkOptData[i].name;
                    lblEle.setAttribute('for', checkOptData[i].value);
                    if (displayData.position === 'left') {
                        divEle.appendChild(checkEle);
                        divEle.appendChild(lblEle);
                    } else {
                        divEle.appendChild(lblEle);
                        divEle.appendChild(checkEle);
                    }
                    fieldLastEle.appendChild(divEle);
                }
                break;
            case 'label':
                const lblEle = document.createElement('div');
                lblEle.style.fontSize = displayData.size + 'px';
                lblEle.style.color = displayData.color;
                lblEle.style.fontWeight = (displayData.bold === 'Y') ? 'bold' : '';
                lblEle.style.fontStyle = (displayData.italic === 'Y') ? 'italic' : '';
                lblEle.style.textDecoration = (displayData.underline === 'Y') ? 'underline' : '';
                lblEle.style.textAlign = displayData.align;
                lblEle.innerHTML = displayData.text;
                fieldLastEle.appendChild(lblEle);
                break;
            case 'image':
                const imgEle = document.createElement('img');
                imgEle.src = displayData.path;
                imgEle.width = displayData.width;
                imgEle.height = displayData.height;
                fieldLastEle.style.textAlign = displayData.align;
                fieldLastEle.appendChild(imgEle);
                break;
            case 'line':
                const lineEle = document.createElement('hr');
                lineEle.style.borderWidth = displayData.width + 'px';
                lineEle.style.borderStyle = displayData.type;
                lineEle.style.borderColor = displayData.color;
                fieldLastEle.appendChild(lineEle);
                break;
            case 'date':
                let dateDefault = displayData.default;
                if (dateDefault === 'today') {
                    dateDefault = getTimeStamp(displayData.format);
                    dateDefault = dateDefault.split(' ')[0];
                }
                const dateEle = document.createElement('input');
                dateEle.id = 'date-' + compData.id;
                dateEle.type = 'text';
                dateEle.placeholder = displayData.format;
                dateEle.value = dateDefault;
                dateEle.required = (validateData.required === 'Y');
                dateEle.readOnly = true;
                dateEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(dateEle);
                dateTimePicker.initDatePicker('date-' + compData.id, displayData.format.toUpperCase());
                break;
            case 'time':
                let timeDefault = displayData.default;
                if (timeDefault === 'now') {
                    timeDefault = getTimeStamp(displayData.format);
                    timeDefault = timeDefault.split(' ')[1];
                }
                const timeEle = document.createElement('input');
                timeEle.id = 'time-' + compData.id;
                timeEle.type = 'text';
                timeEle.placeholder = displayData.format;
                timeEle.value = timeDefault;
                timeEle.required = (validateData.required === 'Y');
                timeEle.readOnly = true;
                fieldLastEle.appendChild(timeEle);
                dateTimePicker.initTimePicker('time-' + compData.id);
                break;
            case 'datetime':
                let datetimeDefault = displayData.default;
                if (datetimeDefault === 'now') {
                    datetimeDefault = getTimeStamp(displayData.format);
                }
                const datetimeEle = document.createElement('input');
                datetimeEle.id = 'datetime-' + compData.id;
                datetimeEle.type = 'text';
                datetimeEle.placeholder = displayData.format;
                datetimeEle.value = datetimeDefault;
                datetimeEle.required = (validateData.required === 'Y');
                datetimeEle.readOnly = true;
                datetimeEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(datetimeEle);
                dateTimePicker.initDateTimePicker('datetime-' + compData.id, displayData.format.toUpperCase());
                break;
            case 'fileupload':
                const fileEle = document.createElement('input');
                fileEle.type = 'file';
                fileEle.multiple = true;
                fieldLastEle.appendChild(fileEle);
                break;
            default :
                break;
        }
    }

    /**
     * draw document.
     *
     * @param data 문서 데이터.
     */
    function drawDocument(data) {
        if (data.components.length > 0) {
            data.components.sort(function(a, b) {
                return a.display.order - b.display.order;
            });
            for (let i = 0; i < data.components.length; i++) {
                addComponent(data.components[i]);
            }
        }
    }

    /**
     * radio, checkbox 선택 확인.
     *
     * @param element element
     */
    function selectCheck(element) {
        let elements = document.getElementsByName(element.name);
        for (let j = 0; j < elements.length; j++) {
            if (elements[j].checked) { return true; }
        }
        return false;
    }

    /**
     * required Check.
     *
     */
    function requiredCheck() {
        const requiredObjs = document.querySelectorAll(':required');
        for (let i = 0; i < requiredObjs.length; i++) {
            let requiredObj = requiredObjs[i];
            if (requiredObj.type === 'radio' || requiredObj.type === 'checkbox') {
                if (!selectCheck(requiredObj)) {
                    alertMsg(requiredObj, i18n.get('document.msg.requiredSelect'));
                    return true;
                }
            } else if (requiredObj.value === '') {
                alertMsg(requiredObj, i18n.get('document.msg.requiredEnter'));
                return true;
            }
        }
        return false;
    }

    /**
     * save document.
     */
    function save() {
        if (!requiredCheck()) {
            //TODO: 구현 필요.
            alert(i18n.get('common.msg.save'));
        }
    }

    /**
     * init document.
     *
     * @param documentId 문서 id
     */
    function init(documentId) {
        console.info('document editor initialization. [DOCUMENT ID: ' + documentId + ']');
        documentContainer = document.getElementById('document-container');

        // document data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/documents/data/' + documentId,
            callbackFunc: function(xhr) {
                drawDocument(JSON.parse(xhr.responseText))
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    exports.init = init;
    exports.save = save;

    Object.defineProperty(exports, '__esModule', {value: true});
})));