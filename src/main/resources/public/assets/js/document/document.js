(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceDocument = global.aliceDocument || {})));
}(this, (function (exports) {
    'use strict';

    let documentContainer = null;
    const defaultColWidth = 8.33; //폼 패널을 12등분하였을때, 1개의 너비
    let userData = {              //사용자 세션 정보
        defaultLang: 'en',
        defaultDateFormat: 'YYYY-MM-DD',
        defaultTimeFormat: 'hh:mm',
        defaultTime: '24'
    };

    const numIncludeRegular = /[0-9]/gi;
    const numRegular = /^[0-9]*$/;
    const emailRegular = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    const defaultAssigneeTypeForSave = 'assignee';

    /**
     * alert message.
     *
     * @param element element
     * @param msg 메시지
     */
    function alertMsg(element, msg) {
        aliceJs.alert(msg, function() {
            if (element) {
                element.focus();
            }
        });
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

            if (typeof validateData !== 'undefined' && validateData.required === 'Y') {
                const requiredEle = document.createElement('span');
                requiredEle.className = 'required';
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
                if (displayData['default']) {
                    let defaultTextValueArr = displayData['default'].split('|');
                    let defaultTextValue = '';
                    if (defaultTextValueArr[0] === 'none') {
                        defaultTextValue = defaultTextValueArr[1];
                    } else {
                        defaultTextValue = userData[defaultTextValueArr[1]];
                    }
                    textEle.value = defaultTextValue;
                }
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
                const textEditorUseYn = displayData['editor-useYn'] === 'Y' ? true : false;
                if (textEditorUseYn) {
                    const defaultRowHeight = 26;
                    const textEditorGroupEle = document.createElement('div');
                    textEditorGroupEle.style.width = '100%';
                    fieldLastEle.appendChild(textEditorGroupEle);

                    const textEditorEle = document.createElement('div');
                    textEditorEle.className = 'editor-container';
                    let textEditorEleHeight = displayData.rows !== '' ? Number(displayData.rows) * defaultRowHeight : defaultRowHeight;
                    textEditorEle.style.height = textEditorEleHeight + 'px';
                    textEditorEle.style.borderWidth = displayData['outline-width'] + 'px';
                    textEditorEle.style.borderColor = displayData['outline-color'];
                    textEditorEle.setAttribute('data-required', (validateData.required === 'Y'));
                    textEditorGroupEle.appendChild(textEditorEle);

                    let textEditorOptions = {
                        modules: {
                            toolbar: [
                                [{'header': [1, 2, 3, 4, 5, 6, false]}],
                                ['bold', 'italic', 'underline'],
                                [{'color': []}, {'background': []}],
                                [{'align': []}, {'list': 'bullet'}, 'image']
                            ]
                        },
                        placeholder: displayData.placeholder,
                        theme: 'snow'
                    };
                    let textEditor = new Quill(textEditorEle, textEditorOptions);
                    //유효성 검사
                    textEditor.on('selection-change', function (range, oldRange, source) {
                        if (range === null && oldRange !== null) {
                            if (validateData['length-min'] !== '' && textEditor.getLength() < Number(validateData['length-min'])) {
                                alertMsg(textEditor, i18n.get('document.msg.lengthMin', validateData['length-min']));
                            }
                            if (validateData['length-max'] !== '' && textEditor.getLength() > Number(validateData['length-max'])) {
                                textEditor.deleteText(Number(validateData['length-max']) - 1, textEditor.getLength());
                                alertMsg(textEditor, i18n.get('document.msg.lengthMax', validateData['length-max']));
                            }
                        }
                    });
                    let textEditorToolbar = textEditorGroupEle.querySelector('.ql-toolbar');
                    if (textEditor !== null && textEditorToolbar) {
                        textEditorToolbar.style.borderWidth = displayData['outline-width'] + 'px';
                        textEditorToolbar.style.borderColor = displayData['outline-color'];
                    }
                } else {
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
                }
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
            case 'divider':
                const lineEle = document.createElement('hr');
                lineEle.style.borderWidth = displayData.thickness + 'px';
                lineEle.style.borderStyle = displayData.type;
                lineEle.style.borderColor = displayData.color;
                fieldLastEle.appendChild(lineEle);
                break;
            case 'date':
                let dateDefaultArr = displayData.default.split('|');
                let dateDefault = '';
                if (dateDefaultArr[0] === 'now') {
                    dateDefault = aliceJs.getTimeStamp(userData.defaultDateFormat);
                    dateDefault = dateDefault.split(' ')[0];
                } else if (dateDefaultArr[0] === 'datepicker') {
                    dateDefault = dateDefaultArr[1];
                } else if (dateDefaultArr[0] === 'date') {
                    dateDefault = aliceJs.getTimeStamp(userData.defaultDateFormat, dateDefaultArr[1]);
                    dateDefault = dateDefault.split(' ')[0];
                }
                const dateEle = document.createElement('input');
                dateEle.id = 'date-' + compData.id;
                dateEle.type = 'text';
                dateEle.placeholder = userData.defaultDateFormat;
                dateEle.value = dateDefault;
                dateEle.required = (validateData.required === 'Y');
                dateEle.readOnly = true;
                dateEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(dateEle);
                dateTimePicker.initDatePicker('date-' + compData.id, userData.defaultDateFormat, userData.defaultLang);
                break;
            case 'time':
                let timeDefaultArr = displayData.default.split('|');
                let timeDefault = '';
                if (timeDefaultArr[0] === 'now') {
                    timeDefault = aliceJs.getTimeStamp(userData.defaultTimeFormat);
                } else if (timeDefaultArr[0] === 'timepicker') {
                    timeDefault = timeDefaultArr[1];
                } else if (timeDefaultArr[0] === 'time') {
                    timeDefault = aliceJs.getTimeStamp(userData.defaultTimeFormat, '', timeDefaultArr[1]);
                }
                const timeEle = document.createElement('input');
                timeEle.id = 'time-' + compData.id;
                timeEle.type = 'text';
                timeEle.placeholder = userData.defaultTimeFormat;
                timeEle.value = timeDefault;
                timeEle.required = (validateData.required === 'Y');
                timeEle.readOnly = true;
                fieldLastEle.appendChild(timeEle);
                dateTimePicker.initTimePicker('time-' + compData.id, userData.defaultTime);
                break;
            case 'datetime':
                let datetimeDefaultArr = displayData.default.split('|');
                let datetimeDefault = '';
                if (datetimeDefaultArr[0] === 'now') {
                    datetimeDefault = aliceJs.getTimeStamp(userData.defaultDateFormat + ' ' + userData.defaultTimeFormat);
                } else if (datetimeDefaultArr[0] === 'datetimepicker') {
                    datetimeDefault = datetimeDefault[1];
                } else if (datetimeDefaultArr[0] === 'datetime') {
                    datetimeDefault = aliceJs.getTimeStamp(userData.defaultDateFormat + ' ' + userData.defaultTimeFormat, datetimeDefaultArr[1], datetimeDefaultArr[2]);
                }

                const datetimeEle = document.createElement('input');
                datetimeEle.id = 'datetime-' + compData.id;
                datetimeEle.type = 'text';
                datetimeEle.placeholder = userData.defaultDateFormat + ' ' + userData.defaultTimeFormat;
                datetimeEle.value = datetimeDefault;
                datetimeEle.required = (validateData.required === 'Y');
                datetimeEle.readOnly = true;
                datetimeEle.addEventListener('focusout', function() {
                    validateCheck(this, validateData);
                });
                fieldLastEle.appendChild(datetimeEle);
                dateTimePicker.initDateTimePicker('datetime-' + compData.id, userData.defaultDateFormat, userData.defaultTime, userData.defaultLang);
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
            if (data.components.length > 2) {
                data.components.sort(function (a, b) {
                    return a.display.order - b.display.order;
                });
            }
            for (let i = 0; i < data.components.length; i++) {
                addComponent(data.components[i]);
            }
        }

        if (data.documentId !== undefined) {
            addIdComponent('documentId', data.documentId);
        }

        if (data.tokenId !== undefined) {
            addIdComponent('tokenId', data.tokenId);
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
        //textarea Editor 필수 체크
        const textEditorElems = document.querySelectorAll('.editor-container');
        for (let i = 0; i < textEditorElems.length; i++) {
            let textEditorElem = textEditorElems[i];
            if (textEditorElem.getAttribute('data-required') === 'true') {
                let textEditor = new Quill(textEditorElem);
                if (textEditor.getLength() === 1) {
                    alertMsg(textEditor, i18n.get('document.msg.requiredEnter'));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * id Component를 만든다. (document, token)
     *
     * @param v_kind 분류, data : id 값
     */
    function addIdComponent(v_kind, v_data) {
        const comp = document.createElement('div');
        comp.id = v_kind;
        comp.setAttribute('data-id', v_data);
        documentContainer.appendChild(comp);
    }

    /**
     * save document.
     */
    function save(v_kind) {
        // validation check
        if (v_kind == 'process') {
            if (requiredCheck()) {
                return false;
            }
        }

        let documentObject = {};
        let tokenObject = {};
        let componentArrayList = [];
        let actionArrayList = [];

        //documentId 값을 구한다.
        const documentElements = document.getElementById('documentId');
        if (documentElements !== null && documentElements !== undefined) {
            //documentObject.documentId = documentElements.getAttribute('data-id');
            tokenObject.documentId = documentElements.getAttribute('data-id');
        } else {
            //documentObject.documentId = '';
            tokenObject.documentId = "";
        }
        //tokenObject.documentName = "";

        //ComponentsInfo
        const componentElements = documentContainer.getElementsByClassName('component');
        for (let eIndex = 0; eIndex < componentElements.length; eIndex++) {
            let componentDataType = componentElements[eIndex].getAttribute('data-type');

            if (componentDataType === 'text' || componentDataType === 'date' || componentDataType === 'time' || componentDataType === 'datetime' ||
                componentDataType === 'textarea' || componentDataType === 'select' || componentDataType === 'radio' || componentDataType === 'checkbox') {
                let componentId = componentElements[eIndex].getAttribute('id');
                let componentValue = '';
                let componentChildObject = {};
                let componentChild = '';

                switch (componentDataType) {
                    case 'text':
                    case 'date':
                    case 'time':
                    case 'datetime':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        componentValue = componentChild.item(0).value;
                        break;
                    case 'textarea':
                        componentChild = componentElements[eIndex].querySelector('.editor-container');
                        if (componentChild) {
                            let textEditor = new Quill(componentChild); //Quill.find(componentChild) === textEditor : true
                            componentValue = JSON.stringify(textEditor.getContents());
                        } else {
                            componentChild = componentElements[eIndex].getElementsByTagName('textarea');
                            componentValue = componentChild.item(0).value;
                        }
                        break;
                    case 'select':
                        componentChild = componentElements[eIndex].getElementsByTagName('select');
                        componentValue = componentChild.item(0).options[componentChild.item(0).selectedIndex].value;
                        break;
                    case 'radio':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        for (let radioIndex = 0; radioIndex < componentChild.length; radioIndex++) {
                            if (componentChild[radioIndex].checked) {
                                componentValue = componentChild[radioIndex].value;
                            }
                        }
                        break;
                    case 'checkbox':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        for (let checkBoxIndex = 0; checkBoxIndex < componentChild.length; checkBoxIndex++) {
                            if (componentChild[checkBoxIndex].checked) {
                                if (checkBoxIndex === 0) {
                                    componentValue = componentChild[checkBoxIndex].value;
                                } else {
                                    componentValue = componentValue + ',' + componentChild[checkBoxIndex].value;
                                }
                            }
                        }
                        break;
                }

                componentChildObject.componentId = componentId;
                componentChildObject.value = componentValue;
                componentArrayList.push(componentChildObject);
            }
        }

        //tokenObject init (RestTemplateTokenDto)
        const tokenElements = document.getElementById('tokenId');
        if (tokenElements !== null && tokenElements !== undefined) {
            tokenObject.tokenId = tokenElements.getAttribute('data-id');
        } else {
            tokenObject.tokenId = '';
        }
        if (v_kind === 'save') {
            tokenObject.isComplete = false; //해당 값이 false라면 저장이다.
            tokenObject.assigneeId = userData.userKey;
            tokenObject.assigneeType = defaultAssigneeTypeForSave;
        } else if (v_kind === 'process') {
            tokenObject.isComplete = true; //해당 값이 true라면 처리이다.
        }

        tokenObject.elementId = '';
        if (componentArrayList.length > 0) {
            tokenObject.data = componentArrayList;
        } else {
            tokenObject.data = '';
        }

        tokenObject.actions = actionArrayList;

        let method = '';
        if (tokenObject.tokenId === '') {
            method = 'post';
        } else {
            method = 'put';

            /*const object = {
                //documentDto : documentObject,
                tokenDto: tokenObject
            };*/

            //console.log(tokenObject);
            //return false;

            const opt = {
                method: method,
                url: '/rest/tokens/data',
                params: JSON.stringify(tokenObject),
                contentType: 'application/json',
                callbackFunc: function() {
                    aliceJs.alert(i18n.get('common.msg.save'), function() {
                        window.close();
                    });
                }
            };
            aliceJs.sendXhr(opt);
        }

        /*const object = {
            //documentDto : documentObject,
            tokenDto: tokenObject
        };*/

        //console.log(tokenObject);
        //return false;

        const opt = {
            method: method,
            url: '/rest/tokens/data',
            params: JSON.stringify(tokenObject),
            contentType: 'application/json',
            callbackFunc: function() {
                alert(i18n.get('common.msg.save'));
                window.close();
            }
        };
        aliceJs.sendXhr(opt);
    }

    /**
     * init document.
     *
     * @param documentId 문서 id
     * @param {String} authInfo 사용자 세션 정보
     */
    function init(documentId, authInfo) {
        console.info('document editor initialization. [DOCUMENT ID: ' + documentId + ']');
        documentContainer = document.getElementById('document-container');

        let authData = JSON.parse(authInfo);
        //신청서화면에서 사용할 사용자 세션 정보
        if (authData) {
            Object.assign(userData, authData);

            userData.defaultLang  = authData.lang;
            userData.userKey = authData.userKey;
            let format = authData.timeFormat;
            let formatArray = format.split(' ');

            userData.defaultDateFormat =  formatArray[0].toUpperCase();
            if (formatArray.length === 3) { userData.defaultTime = '12'; }
        }

        // document data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/documents/data/' + documentId,
            callbackFunc: function(xhr) {
                let jsonData = JSON.parse(xhr.responseText);
                jsonData.documentId = documentId;
                //진행중 저장을 위해서 테스트 데이터
                //jsonData.tokenId = '40288ab770a01cd30170a01d69e40003';
                drawDocument(jsonData);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * Init Container.
     *
     * @param elementId
     * @param {String} authInfo 사용자 세션 정보
     */
    function initContainer(elementId, authInfo) {
        documentContainer = document.getElementById(elementId);

        let authData = JSON.parse(authInfo);
        //미리 보기시 사용할 사용자 세션 정보
        if (authData) {
            Object.assign(userData, authData);

            userData.defaultLang  = authData.lang;
            userData.userKey = authData.userKey;
            let format = authData.timeFormat;
            let formatArray = format.split(' ');

            userData.defaultDateFormat =  formatArray[0].toUpperCase();
            if (formatArray.length === 3) { userData.defaultTime = '12'; }
        }
    }

    exports.init = init;
    exports.save = save;
    exports.initContainer = initContainer;
    exports.drawDocument = drawDocument;

    Object.defineProperty(exports, '__esModule', {value: true});
})));
