(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceDocument = global.aliceDocument || {})));
}(this, (function (exports) {
    'use strict';

    let documentContainer = null;
    let buttonContainer = null;
    let commentContainer = null;
    const numIncludeRegular = /[0-9]/gi;
    const numRegular = /^[0-9]*$/;
    const emailRegular = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    const defaultAssigneeTypeForSave = 'assignee.type.assignee';

    let dataForPrint = ''; // 프린트 출력용 저장 데이터

    /**
     * get component target.
     *
     * @param componentElement component element.
     */
    function getComponentTarget(componentElement) {
        let componentTarget;
        switch (componentElement.getAttribute('data-type')) {
            case 'textarea':
                componentTarget = componentElement.querySelector('.editor-container');
                if (!componentTarget) { componentTarget = componentElement.querySelector('textarea'); }
                break;
            case 'select':
                componentTarget = componentElement.querySelector('select');
                break;
            case 'radio':
                componentTarget = componentElement.querySelector('div#radio');
                break;
            case 'checkbox':
                componentTarget = componentElement.querySelector('div#chkbox');
                break;
            case 'fileupload':
                componentTarget = componentElement.querySelector('#fileupload');
                break;
            default :
                componentTarget = componentElement.querySelector('input');
                break;
        }
        return componentTarget;
    }

    /**
     * Check Validate for save.
     */
    function checkValidateForSave() {
        const componentElements = document.querySelectorAll('.component');
        for (let i = 0; i < componentElements.length; i++) {
            let componentChild = getComponentTarget(componentElements[i]);
            if (componentChild === null) { continue; }
            checkValidate(componentChild);
        }
        const validateElement = document.querySelectorAll('.validate-error');
        if (validateElement.length !== 0) {
            aliceJs.alert(i18n.get('common.msg.checkDocument'), function () {
                if (validateElement[0].classList.contains('editor-container')) {
                    validateElement[0].firstChild.focus();
                } else if (validateElement[0].id === 'radio' || validateElement[0].id === 'chkbox') {
                    validateElement[0].querySelector('input').focus();
                } else {
                    validateElement[0].focus();
                }
            });
            return true;
        }
        return false;
    }

    /**
     * Check Validate.
     *
     * @param element element
     */
    function checkValidate(element) {
        if (element.classList.contains('validate-error')) {
            element.classList.remove('validate-error');
            element.removeAttribute('title');
        }

        for (let i = 0, len = element.attributes.length; i < len; i++) {
            let message = null;
            const attribute = element.attributes[i];
            const nodeValue = attribute.nodeValue;
            if (nodeValue !== '') {
                switch (attribute.nodeName) {
                    case 'min-length':
                    case 'max-length':
                        let length = 0;
                        if (element.classList.contains('editor-container')) { // editor
                            length = Quill.find(element).getLength() - 1;
                        } else {
                            length = element.value.length;
                        }
                        if (attribute.nodeName === 'min-length' && length < Number(nodeValue)) {
                            message = i18n.get('document.msg.lengthMin', nodeValue);
                        } else if (attribute.nodeName === 'max-length' && length > Number(nodeValue)) {
                            message = i18n.get('document.msg.lengthMax', nodeValue);
                        }
                        break;
                    case 'date-min':
                    case 'date-max':
                        const dateValue = moment(element.value, aliceForm.options.dateFormat);
                        const dateNodeValue = moment(nodeValue, aliceJs.systemCalendarDateFormat);
                        if (attribute.nodeName === 'date-min' && dateValue.isBefore(dateNodeValue)) {
                            message = i18n.get('document.msg.dateMin', dateNodeValue.format(aliceForm.options.dateFormat));
                        } else if (attribute.nodeName === 'date-max' && dateValue.isAfter(dateNodeValue)) {
                            message = i18n.get('document.msg.dateMax', dateNodeValue.format(aliceForm.options.dateFormat));
                        }
                        break;
                    case 'time-min':
                    case 'time-max':
                        const timeValue = moment(aliceJs.convertToSystemHourType(element.value), aliceForm.options.hourFormat);
                        const timeNodeValue = moment(nodeValue, aliceJs.systemCalendarTimeFormat);
                        if (attribute.nodeName === 'time-min' && timeValue.isBefore(timeNodeValue)) {
                            message = i18n.get('document.msg.timeMin', timeNodeValue.format(aliceForm.options.hourFormat));
                        } else if (attribute.nodeName === 'time-max' && timeValue.isAfter(timeNodeValue)) {
                            message = i18n.get('document.msg.timeMax', timeNodeValue.format(aliceForm.options.hourFormat));
                        }
                        break;
                    case 'datetime-min':
                    case 'datetime-max':
                        const datetimeValue = moment(aliceJs.convertToSystemHourType(element.value), aliceForm.options.datetimeFormat);
                        const datetimeNodeValue = moment(nodeValue, aliceJs.systemCalendarDatetimeFormat);
                        if (attribute.nodeName === 'datetime-min' && datetimeValue.isBefore(datetimeNodeValue)) {
                            message = i18n.get('document.msg.dateMin', datetimeNodeValue.format(aliceForm.options.datetimeFormat));
                        } else if (attribute.nodeName === 'datetime-max' && datetimeValue.isAfter(datetimeNodeValue)) {
                            message = i18n.get('document.msg.dateMax', datetimeNodeValue.format(aliceForm.options.datetimeFormat));
                        }
                        break;
                    case 'regexp':
                        if (checkRegexp(nodeValue, element.value)) {
                            message = element.getAttribute('regexp-msg');
                        }
                        break;
                }
            } else if (attribute.nodeName === 'required') {
                message = checkRequired(element);
            }

            if (message !== null) {
                element.classList.add('validate-error');
                element.title = message;
                return true;
            }
        }
        return false;
    }

    /**
     * Check Regexp.
     *
     * @param nodeValue node value
     * @param value element value
     */
    function checkRegexp(nodeValue, value) {
        let result = false;
        if (value.length !== 0) {
            switch (nodeValue) {
                case 'char':
                    result = (value.match(numIncludeRegular) !== null);
                    break;
                case 'num':
                    result = !numRegular.test(value);
                    break;
                case 'numchar':
                    // TODO: number + char
                    break;
                case 'phone':
                    // TODO: phone
                    break;
                case 'email':
                    result = !emailRegular.test(value);
                    break;
            }
        }
        return result;
    }

    /**
     * Check Required.
     *
     * @param element element
     */
    function checkRequired(element) {
        let message = null;
        switch (element.id) {
            case 'editor':
                let textEditor = Quill.find(element);
                if (textEditor.getLength() === 1) {
                    message = i18n.get('document.msg.requiredEnter');
                }
                break;
            case 'radio':
            case 'chkbox':
                if (!checkSelect(element)) {
                    message = i18n.get('document.msg.requiredSelect');
                }
                break;
            case 'fileupload':
                if (element.querySelectorAll('input[name=loadedFileSeq], input[name=fileSeq]').length === 0) {
                    message = i18n.get('document.msg.requiredFileupload');
                }
                break;
            case 'custom-code':
                if (element.value === '') {
                    message = i18n.get('document.msg.requiredSelect');
                }
                break;
            default :
                if (element.value === '') {
                    message = i18n.get('document.msg.requiredEnter');
                }
                break;
        }
        return message;
    }

    /**
     * button를 만든다.
     * 저장과 취소 버튼은 기본적으로 생성된다.
     * @param  buttonData : button 정보 값
     */
    function addButton(buttonData) {
        const buttonEle = document.createElement('div');
        buttonEle.style.margin = '10px';
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
    function checkSelect(element) {
        let elements = element.getElementsByTagName('input');
        for (let i = 0; i < elements.length; i++) {
            if (elements[i].checked) { return true; }
        }
        return false;
    }

    /**
     * id Component를 만든다. (document, token)
     *
     * @param v_kind 분류, data : id 값
     * @param v_data id
     */
    function addIdComponent(v_kind, v_data) {
        const comp = document.createElement('div');
        comp.id = v_kind;
        comp.setAttribute('data-id', v_data);
        documentContainer.appendChild(comp);
    }

    /**
     * ComponentData 조회.
     *
     * 2020-05-27 Jung Hee Chan : target 개념을 추가.
     *   - getComponentData는 문서를 저장하는 경우와 출력하는 경우에 호출.
     *   - 문서를 저장하는 경우는 날짜와 관련된 내용을 타임존, 포맷을 시스템 공통으로 변환.
     *   - 출력을 하는 경우는 있는 값을 그대로 사용.
     *
     * @param {String} [target] 문서 출력을 위한 경우 'print'값을 받음. 이외의 값인 경우에는 저장으로 판단.
     */
    function getComponentData(target) {
        let componentArrayList = [];
        const componentElements = documentContainer.getElementsByClassName('component');
        for (let eIndex = 0; eIndex < componentElements.length; eIndex++) {
            let componentDataType = componentElements[eIndex].getAttribute('data-type');
            if (componentDataType === 'text' || componentDataType === 'date' || componentDataType === 'time' || componentDataType === 'datetime' ||
                componentDataType === 'textarea' || componentDataType === 'select' || componentDataType === 'radio' || componentDataType === 'checkbox' ||
                componentDataType === 'fileupload' || componentDataType === 'custom-code') {
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
                        if (target === 'print') {
                            componentValue = componentChild.item(0).value;
                        } else {
                            componentValue = aliceJs.convertToSystemDateFormat(componentChild.item(0).value, aliceForm.options.dateFormat);
                        }
                        break;
                    case 'time':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        if (target === 'print') {
                            componentValue = componentChild.item(0).value;
                        } else {
                            componentValue = aliceJs.convertToSystemTimeFormat(componentChild.item(0).value, aliceForm.options.hourFormat);
                        }
                        break;
                    case 'datetime':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        if (target === 'print') {
                            componentValue = componentChild.item(0).value;
                        } else {
                            componentValue = aliceJs.convertToSystemDatetimeFormatWithTimezone(componentChild.item(0).value, aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                        }
                        break;
                    case 'textarea':
                        componentChild = componentElements[eIndex].querySelector('.editor-container');
                        if (componentChild) {
                            let textEditor = Quill.find(componentChild);
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
                        let componentValueArr = [];
                        for (let checkBoxIndex = 0; checkBoxIndex < componentChild.length; checkBoxIndex++) {
                            if (componentChild[checkBoxIndex].checked && componentValueArr.indexOf(componentChild[checkBoxIndex].value) === -1) {
                                componentValueArr.push(componentChild[checkBoxIndex].value);
                            }
                        }
                        componentValue = JSON.stringify(componentValueArr);
                        break;
                    case 'fileupload':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        for (let fileuploadIndex = 0; fileuploadIndex < componentChild.length; fileuploadIndex++) {
                            if (componentChild[fileuploadIndex].name !== 'delFileSeq') {
                                if (componentValue === '' && componentValue.indexOf(",") === -1) {
                                    componentValue = componentChild[fileuploadIndex].value;
                                } else {
                                    componentValue = componentValue + ',' + componentChild[fileuploadIndex].value;
                                }
                            }
                        }
                        break;
                    case 'custom-code':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        componentValue = componentChild.item(0).getAttribute('custom-data');
                        break;
                    default:
                        break;
                }
                componentChildObject.componentId = componentId;
                componentChildObject.value = componentValue;
                componentArrayList.push(componentChildObject);
            }
        }
        return componentArrayList;
    }

    /**
     * save document.
     */
    function save(v_kind) {
        // validation check
        if (v_kind !== 'save' && checkValidateForSave()) {
            return false;
        }
        let tokenObject = {};

        //documentId 값을 구한다.
        const documentElements = document.getElementById('documentId');
        if (documentElements !== null && documentElements !== undefined) {
            tokenObject.documentId = documentElements.getAttribute('data-id');
        } else {
            tokenObject.documentId = "";
        }

        //tokenObject init (RestTemplateTokenDto)
        const tokenElements = document.getElementById('tokenId');
        if (tokenElements !== null && tokenElements !== undefined) {
            tokenObject.tokenId = tokenElements.getAttribute('data-id');
        } else {
            tokenObject.tokenId = '';
        }

        let actionMsg = '';
        if (v_kind === 'save') {
            tokenObject.isComplete = false; //해당 값이 false라면 저장이다.
            tokenObject.assigneeId = aliceForm.options.sessionInfo.userKey;
            tokenObject.assigneeType = defaultAssigneeTypeForSave;
            actionMsg = i18n.get('common.msg.save');
        } else {
            tokenObject.isComplete = true; //해당 값이 true라면 처리이다.
            tokenObject.assigneeId = '';
            tokenObject.assigneeType = '';
            actionMsg = i18n.get('common.msg.process');
        }

        const componentArrayList = getComponentData();
        if (componentArrayList.length > 0) {
            tokenObject.componentData = componentArrayList;
        }

        tokenObject.action = v_kind;
        let method = '';
        let url = '';
        if (tokenObject.tokenId === '') {
            method = 'post';
            url = '/rest/tokens/data'
        } else {
            method = 'put';
            url = '/rest/tokens/' + tokenObject.tokenId + '/data'
        }

        // 2020-04-06 kbh
        // 프로세스 넘기려고 부득이하게 하드코딩함. merge 된 후 삭제 예정
        //tokenObject.documentId = 'beom'
        //tokenObject.elementId = 'a12c2f06debf788570a6b08a5ece73ac'
        const opt = {
            method: method,
            url: url,
            params: JSON.stringify(tokenObject),
            contentType: 'application/json',
            callbackFunc: function(xhr) {
                if (xhr.responseText === 'true') {
                    aliceJs.alert(actionMsg, function () {
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
        if (typeof data === 'string') {
            data = JSON.parse(data);
        }
        data.form.components = data.form.components.filter(function(comp) { return comp.type !== aliceForm.options.defaultType; }); //editbox 제외
        documentContainer = document.getElementById('document-container');
        documentContainer.setAttribute('data-isToken', (data.tokenId !== undefined) ? 'true' : 'false'); //신청서 = false , 처리할 문서 = true
        buttonContainer = document.getElementById('button-container');

        if (data.form.components.length > 0) {
            if (data.form.components.length > 2) {
                data.form.components.sort(function (a, b) {
                    return a.display.order - b.display.order;
                });
            }
            for (let i = 0, len = data.form.components.length; i < len; i++) {
                //데이터로 전달받은 컴포넌트 속성과 기본 속성을 merge한 후 컴포넌트 draw
                let componentAttr = data.form.components[i];
                let defaultComponentAttr = component.getData(componentAttr.type);
                let mergeComponentAttr = aliceJs.mergeObject(defaultComponentAttr, componentAttr);
                data.form.components[i] = mergeComponentAttr;

                component.draw(componentAttr.type, documentContainer, mergeComponentAttr);
            }
            //유효성 검증 추가
            if (!documentContainer.hasAttribute('data-readonly')) {
                const checkComponents = ['text', 'textarea', 'select', 'radio', 'checkbox'];
                const componentElements = document.querySelectorAll('.component');
                for (let i = 0; i < componentElements.length; i++) {
                    let componentChild = getComponentTarget(componentElements[i]);
                    if (componentChild === null ||
                        checkComponents.indexOf(componentElements[i].getAttribute('data-type')) === -1) { continue; }
                    componentChild.addEventListener('focusout', function() {
                        checkValidate(this);
                    }, false);
                }
            }
        }
        if (data.documentId !== undefined) {
            addIdComponent('documentId', data.documentId);
        }

        if (data.tokenId !== undefined) {
            addIdComponent('tokenId', data.tokenId);
        }
        if (data.actions !== undefined) {
            addButton(data.actions);
        }

        //Add Comment Box
        if (data.instanceId !== undefined) {
            addCommentBox(data.instanceId);
        }
    }

    /**
     * Comment Object.
     *
     * @param instanceId
     */
    function addCommentBox(instanceId) {
        commentContainer = document.getElementById('comment-container');
        if (commentContainer !== null) {
            let commentBoxContainer = document.createElement('div');
            let commentButtonContainer = document.createElement('div');
            commentBoxContainer.classList.add('field');
            commentBoxContainer.style.width = '90%';
            commentButtonContainer.classList.add('field');
            commentButtonContainer.style.width = '10%';

            let commentBoxTextarea = document.createElement('textarea');
            let commentButton = document.createElement('button');
            commentButton.type = 'button';
            commentButton.innerText = i18n.get('common.btn.register');
            commentButton.addEventListener('click', function () {
                aliceDocument.saveComment(instanceId, commentBoxTextarea.value);
            });

            commentBoxContainer.appendChild(commentBoxTextarea);
            commentButtonContainer.appendChild(commentButton);
            commentContainer.appendChild(commentBoxContainer);
            commentContainer.appendChild(commentButtonContainer);
        }
    }

    /**
     * Save Comment.
     *
     * @param instanceId
     * @param comment
     */
    function saveComment(instanceId, comment) {
        let data = {
            instanceId: instanceId,
            content: comment
        };
        const opt = {
            method: 'POST',
            url: '/rest/comments',
            params: JSON.stringify(data),
            contentType: 'application/json',
            callbackFunc: function(xhr) {
                if (xhr.responseText) {
                    location.reload();
                } else {
                    aliceJs.alert(i18n.get('common.msg.fail'));
                }
            }
        };
        aliceJs.sendXhr(opt);
    }

    /**
     * Delete Comment.
     *
     * @param commentId
     */
    function deleteComment(commentId) {
        const opt = {
            method: 'DELETE',
            url: '/rest/comments/' + commentId,
            callbackFunc: function(xhr) {
                if (xhr.responseText) {
                    aliceJs.alert(i18n.get('common.msg.delete.success'), function() {
                        location.reload();
                    });
                } else {
                    aliceJs.alert(i18n.get('common.msg.save.fail'));
                }
            }
        };
        aliceJs.sendXhr(opt);
    }

    /**
     * init document.
     *
     * @param documentId 문서 id
     */
    function init(documentId) {
        console.info('document editor initialization. [DOCUMENT ID: ' + documentId + ']');

        // document data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/documents/' + documentId + '/data',
            callbackFunc: function(xhr) {
                let responseObject = JSON.parse(xhr.responseText);
                responseObject.form.components = aliceForm.reformatCalendarFormat('read', responseObject.form.components);

                // dataForPrint 변수가 전역으로 무슨 목적이 있는 것 같아 그대로 살려둠.
                dataForPrint = responseObject;
                dataForPrint.documentId = documentId;
                drawDocument(dataForPrint);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * init Token.
     *
     * @param tokenId 문서 토큰 id
     */
    function initToken(tokenId) {
        console.info('document editor initialization. [Token ID: ' + tokenId + ']');

        // token data search.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/tokens/' + tokenId + '/data',
            callbackFunc: function(xhr) {
                let responseObject = JSON.parse(xhr.responseText);
                responseObject.form.components = aliceForm.reformatCalendarFormat('read', responseObject.form.components);

                // dataForPrint 변수가 전역으로 무슨 목적이 있는 것 같아 그대로 살려둠.
                dataForPrint = responseObject;
                dataForPrint.tokenId = tokenId;
                drawDocument(dataForPrint);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * Print document.
     *
     * @param url
     */
    function print(url) {
        let form = document.createElement('form');
        form.action = url + '/print';
        form.name = 'print';
        form.method = 'post';
        form.target = '_blank';
        let textarea = document.createElement('textarea');
        textarea.name = 'data';
        let componentArrayList = getComponentData('print');
        dataForPrint.form.components = dataForPrint.form.components.filter(function(comp) {
            componentArrayList.forEach(function(array) {
                if (comp.componentId === array.componentId) {
                    comp.value = (typeof comp.value === 'undefined') ? '' : array.value;
                }
            });
            if (comp.dataAttribute.displayType !== 'hidden') {
                comp.dataAttribute.displayType = 'readonly';
            }
            return comp;
        });
        textarea.value = JSON.stringify(dataForPrint);
        form.appendChild(textarea);
        form.style.display = 'none';
        document.body.appendChild(form);

        form.submit();
    }

    exports.init = init;
    exports.initToken = initToken;
    exports.save = save;
    exports.saveComment = saveComment;
    exports.deleteComment = deleteComment;
    exports.drawDocument = drawDocument;
    exports.checkValidate = checkValidate;
    exports.print = print;

    Object.defineProperty(exports, '__esModule', {value: true});
})));

/**
 * Drag, Drop 방지 관련 이벤트 리스너.
 *
 */
window.addEventListener('dragover', (e) => {
    e.stopPropagation();
    e.preventDefault();
    e.dataTransfer.effectAllowed = 'none';
    e.dataTransfer.dropEffect = 'none';
}, false);

window.addEventListener('drop', (e) => {
    e.stopPropagation();
    e.preventDefault();
    e.dataTransfer.effectAllowed = 'none';
    e.dataTransfer.dropEffect = 'none';
}, false);
