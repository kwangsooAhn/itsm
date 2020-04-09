(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceDocument = global.aliceDocument || {})));
}(this, (function (exports) {
    'use strict';

    let documentContainer = null;
    let buttonContainer = null;
    const numIncludeRegular = /[0-9]/gi;
    const numRegular = /^[0-9]*$/;
    const emailRegular = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    const defaultAssigneeTypeForSave = 'assignee.type.assignee';

    /**
     * alert message.
     *
     * @param element element
     * @param msg 메시지
     */
    function alertMsg(element, msg) {
        aliceJs.alert(msg, function() {
            element.focus();
        });
    }

    /**
     * Validation Check.
     *
     * @param elem element
     */
    function validateCheck(elem) {
        const chkVal = elem.value.trim();
        if (chkVal.length !== 0) {
            for (let i = 0, len = elem.attributes.length; i < len; i++) {
                let attr = elem.attributes[i];
                if (attr.nodeValue !== '') {
                    if (attr.nodeName === 'regexp') {
                        switch (attr.nodeValue) {
                            case 'char':
                                if (numIncludeRegular.test(chkVal)) {
                                    alertMsg(elem, elem.getAttribute('regexp-msg'));
                                    return true;
                                }
                                break;
                            case 'num':
                                if (!numRegular.test(chkVal)) {
                                    alertMsg(elem, elem.getAttribute('regexp-msg'));
                                    return true;
                                }
                                break;
                            case 'email':
                                if (!emailRegular.test(chkVal)) {
                                    alertMsg(elem, elem.getAttribute('regexp-msg'));
                                    return true;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    if (attr.nodeName === 'length-min' && attr.nodeValue > chkVal.length) {
                        alertMsg(elem, i18n.get('document.msg.lengthMin', attr.nodeValue));
                        return true;
                    }
                    if (attr.nodeName === 'length-max' && attr.nodeValue < chkVal.length) {
                        alertMsg(elem, i18n.get('document.msg.lengthMax', attr.nodeValue));
                        return true;
                    }
                    if (attr.nodeName === 'date-min') {
                        let dateMinValueArray = attr.nodeValue.split("|");
                        let dateMinValuePlaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
                        let dateMinValue = aliceJs.changeDateFormat(dateMinValueArray[1], dateMinValuePlaceholder, dateMinValueArray[0], aliceForm.options.lang);
                        if (dateMinValue > chkVal) {
                            alertMsg(elem, i18n.get('document.msg.dateMin').replace('{0}', dateMinValue));
                            return true;
                        }
                    }
                    if (attr.nodeName === 'date-max') {
                        let dateMaxValueArray = attr.nodeValue.split("|");
                        let dateMaxValuePlaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
                        let dateMaxValue = aliceJs.changeDateFormat(dateMaxValueArray[1], dateMaxValuePlaceholder, dateMaxValueArray[0], aliceForm.options.lang);
                        if (dateMaxValue < chkVal) {
                            alertMsg(elem, i18n.get('document.msg.dateMax').replace('{0}', dateMaxValue));
                            return true;
                        }
                    }
                }
            }
        }
    }

    /**
     * button를 만든다.
     * 저장과 취소 버튼은 기본적으로 생성된다.
     * @param  buttonData : button 정보 값
     */
    function addButton(buttonData) {
        const buttonEle = document.createElement('div');
        buttonEle.style.marginTop = '10px';
        buttonEle.style.textAlign = 'center';
        if (buttonData !== undefined && buttonData !== '') {
            buttonData.forEach(function(element) {
                if (element.name !== '') {
                    let buttonProcessEle = document.createElement('button');
                    buttonProcessEle.type = 'button';
                    buttonProcessEle.innerText = element.name;
                    buttonProcessEle.addEventListener('click', function () {
                        aliceDocument.save(element.value);
                    });
                    buttonEle.appendChild(buttonProcessEle);
                }
            });
        } else {
            //20200331 kimsungmin 다음 스프린트에서는 해당 버튼은 삭제가 되어야 한다.
            //token Id 가 없고 버튼에 대한 정보 없다는 것은 처음 문서 생성 이라고 판단한다.
            if (document.getElementById('tokenId') === null) {
                const buttonSaveEle = document.createElement('button');
                buttonSaveEle.type = 'button';
                buttonSaveEle.innerText = i18n.get('common.btn.save');
                buttonSaveEle.addEventListener('click', function () {
                    aliceDocument.save('save');
                });
                buttonEle.appendChild(buttonSaveEle);
            }
        }

        //20200331 kimsungmin 다음 스프린트에서는 해당 버튼은 삭제가 되어야 한다.
        const buttonCancelEle = document.createElement('button');
        buttonCancelEle.type = 'button';
        buttonCancelEle.innerText = i18n.get('common.btn.cancel');
        buttonCancelEle.addEventListener('click', function() {
            window.close();
        });
        buttonEle.appendChild(buttonCancelEle);
        if (buttonContainer !== null) {
            buttonContainer.appendChild(buttonEle);
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
            if (textEditorElem.getAttribute('required') === 'true') {
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
        if (v_kind !== 'save') {
            if (requiredCheck()) {
                return false;
            }
        }

        let tokenObject = {};
        let componentArrayList = [];
        let fileDataIds = '';

        //documentId 값을 구한다.
        const documentElements = document.getElementById('documentId');
        if (documentElements !== null && documentElements !== undefined) {
            tokenObject.documentId = documentElements.getAttribute('data-id');
        } else {
            tokenObject.documentId = "";
        }

        //ComponentsInfo
        const componentElements = documentContainer.getElementsByClassName('component');
        for (let eIndex = 0; eIndex < componentElements.length; eIndex++) {
            let componentDataType = componentElements[eIndex].getAttribute('data-type');
            if (componentDataType === 'text' || componentDataType === 'date' || componentDataType === 'time' || componentDataType === 'datetime' ||
                componentDataType === 'textarea' || componentDataType === 'select' || componentDataType === 'radio' || componentDataType === 'checkbox' ||
                componentDataType === 'fileupload') {
                let componentId = componentElements[eIndex].getAttribute('id');
                let componentValue = '';
                let componentChildObject = {};
                let componentChild = '';

                switch (componentDataType) {
                    case 'text':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        componentValue = componentChild.item(0).value;
                        break;
                    case 'date':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        let dateFormat = componentChild.item(0).placeholder;
                        componentValue = componentChild.item(0).value+'|'+dateFormat;
                        break;
                    case 'time':
                        let timeFormat = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        componentValue = componentChild.item(0).value+'|'+timeFormat;
                        break;
                    case 'datetime':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        let datetimeFormat = componentChild.item(0).placeholder;
                        componentValue = componentChild.item(0).value+'|'+datetimeFormat;
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
                                if (componentValue === '' && componentValue.indexOf(",") === -1) {
                                    componentValue = componentChild[checkBoxIndex].value;
                                } else {
                                    componentValue = componentValue + ',' + componentChild[checkBoxIndex].value;
                                }
                            }
                        }
                        break;
                    case 'fileupload' :
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        for (let fileuploadIndex = 0; fileuploadIndex < componentChild.length; fileuploadIndex++) {
                            if (componentChild[fileuploadIndex].name !== 'delFileSeq') {
                                if (componentValue === '' && componentValue.indexOf(",") === -1) {
                                    componentValue = componentChild[fileuploadIndex].value;
                                } else {
                                    componentValue = componentValue + ',' + componentChild[fileuploadIndex].value;
                                }
                            }
                            //신규 추가된 첨부파일만 임시폴더에서 일반폴더로 옮기기 위해서
                            if (componentChild[fileuploadIndex].name === 'fileSeq') {
                                if (fileDataIds === '' && fileDataIds.indexOf(",") === -1) {
                                    fileDataIds = componentChild[fileuploadIndex].value;
                                } else {
                                    fileDataIds = fileDataIds + ',' + componentChild[fileuploadIndex].value;
                                }
                            }
                        }
                        break;
                    case 'custom-code' :
                        //TODO 사용자 정의 코드 구현
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
            tokenObject.assigneeId = aliceForm.options.sessionInfo.userKey;
            tokenObject.assigneeType = defaultAssigneeTypeForSave;
        } else {
            tokenObject.isComplete = true; //해당 값이 true라면 처리이다.
            tokenObject.assigneeId = '';
            tokenObject.assigneeType = '';
        }

        if (componentArrayList.length > 0) {
            tokenObject.data = componentArrayList;
        } else {
            tokenObject.data = '';
        }

        tokenObject.action = v_kind;

        let method = '';
        if (tokenObject.tokenId === '') {
            method = 'post';
        } else {
            method = 'put';
        }

        if (fileDataIds !== '') {
            tokenObject.fileDataIds = fileDataIds;
        }

        const opt = {
            method: method,
            url: '/rest/tokens/data',
            params: JSON.stringify(tokenObject),
            contentType: 'application/json',
            callbackFunc: function(xhr) {
                if (xhr.responseText === 'true') {
                    aliceJs.alert(i18n.get('common.msg.save'), function () {
                        opener.location.reload();
                        window.close();
                    });
                } else {
                    aliceJs.alert(i18n.get('common.msg.fail'));
                }
            }
        };
        aliceJs.sendXhr(opt);
    }

    /**
     * 조회된 데이터 draw.
     *
     * @param data 문서 데이터.
     */
    function drawDocument(data) {
        let components = (data.token === undefined) ? data.components : data.token.components;
        if (components.length > 0) {
            if (components.length > 2) {
                components.sort(function (a, b) {
                    if (a.attributes === undefined) {
                        return a.display.order - b.display.order;
                    } else {
                        return a.attributes.display.order - b.attributes.display.order;
                    }
                });
            }
            for (let i = 0, len = components.length; i < len; i++) {
                //데이터로 전달받은 컴포넌트 속성과 기본 속성을 merge한 후 컴포넌트 draw
                let componentAttr = components[i];
                let compType = (componentAttr.attributes === undefined) ? componentAttr.type : componentAttr.attributes.type;
                let defaultComponentAttr = component.getData(compType);
                let mergeComponentAttr = null;
                if (componentAttr.attributes === undefined) { //신청서
                    mergeComponentAttr = aliceJs.mergeObject(defaultComponentAttr, componentAttr);
                    componentAttr = mergeComponentAttr;
                } else { //처리할 문서
                    mergeComponentAttr = aliceJs.mergeObject(defaultComponentAttr, componentAttr.attributes);
                    componentAttr.attributes = mergeComponentAttr;
                }
                component.draw(compType, documentContainer, componentAttr);
            }
            //유효성 검증 추가
            let validateElems = document.querySelectorAll('input[type="text"], textarea, .editor-container');
            for (let i = 0, len = validateElems.length; i < len; i++) {
                let elem = validateElems[i];
                if (elem.classList.contains('editor-container')) { //텍스트 에디터 유효성 검증 추가
                    let textEditor = new Quill(elem);
                    textEditor.on('selection-change', function (range, oldRange, source) {
                        if (range === null && oldRange !== null) {
                            if (elem.getAttribute('length-min') !== '' && textEditor.getLength() < Number(elem.getAttribute('length-min'))) {
                                alertMsg(textEditor, i18n.get('document.msg.lengthMin', elem.getAttribute('length-min')));
                            }
                            if (elem.getAttribute('length-max') !== '' && textEditor.getLength() > Number(elem.getAttribute('length-max'))) {
                                textEditor.deleteText(Number(elem.getAttribute('length-max')) - 1, textEditor.getLength());
                                alertMsg(textEditor, i18n.get('document.msg.lengthMax', elem.getAttribute('length-max')));
                            }
                        }
                    });
                } else {
                    elem.addEventListener('focusout', function () {
                        validateCheck(this);
                    });
                }
            }
        }
        if (data.documentId !== undefined) {
            addIdComponent('documentId', data.documentId);
        }

        if (data.tokenId !== undefined) {
            addIdComponent('tokenId', data.tokenId);
        }

        if (data.components != undefined) {
            addButton(data.action);
        } else if (data.token.components != undefined) {
            addButton(data.token.action);
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
        buttonContainer = document.getElementById('button-container');

        // document data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/documents/data/' + documentId,
            callbackFunc: function(xhr) {
                let jsonData = JSON.parse(xhr.responseText);
                jsonData.documentId = documentId;
                drawDocument(jsonData);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * init Token.
     *
     * @param tokenId 문서 id
     */
    function initToken(tokenId) {
        console.info('document editor initialization. [Token ID: ' + tokenId + ']');
        documentContainer = document.getElementById('document-container');
        buttonContainer = document.getElementById('button-container');

        // token data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/tokens/data/' + tokenId,
            callbackFunc: function(xhr) {
                let jsonData = JSON.parse(xhr.responseText);
                jsonData.tokenId = tokenId;
                drawDocument(jsonData);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * Init Container.
     *
     * @param elementId
     */
    function initContainer(elementId) {
        documentContainer = document.getElementById(elementId);
    }

    exports.init = init;
    exports.initToken = initToken;
    exports.save = save;
    exports.initContainer = initContainer;
    exports.drawDocument = drawDocument;

    Object.defineProperty(exports, '__esModule', {value: true});
})));
