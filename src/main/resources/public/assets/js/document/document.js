(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceDocument = global.aliceDocument || {})));
}(this, (function (exports) {
    'use strict';

    let documentContainer = null;
    let buttonContainer = null;
    let commentContainer = null;
    let documentModal = null;
    let isDocument = true; // 신청서 vs 처리할 문서
    const numIncludeRegular = /[0-9]/gi;
    const numRegular = /^[0-9]*$/;
    const phoneRegular = /^([+]?[0-9])([-]?[0-9])*$/;
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
            case 'textbox':
                componentTarget = componentElement.querySelector('.editor-container');
                if (!componentTarget) { componentTarget = componentElement.querySelector('textarea'); }
                break;
            case 'dropdown':
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
            aliceJs.alertWarning(i18n.get('document.msg.checkDocument'), function () {
                if (validateElement[0].classList.contains('editor-container')) {
                    Quill.find(validateElement[0]).focus();
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
                    case 'minlength':
                    case 'maxlength':
                        let length = 0;
                        if (element.classList.contains('editor-container')) { // editor
                            length = Quill.find(element).getLength() - 1;
                        } else {
                            length = element.value.length;
                        }
                        if (attribute.nodeName === 'minlength' && length < Number(nodeValue)) {
                            message = i18n.get('document.msg.lengthMin', nodeValue);
                        } else if (attribute.nodeName === 'maxlength' && length > Number(nodeValue)) {
                            message = i18n.get('document.msg.lengthMax', nodeValue);
                        }
                        break;
                    case 'date-min':
                    case 'date-max':
                        if (attribute.nodeName === 'date-min' && i18n.compareSystemDate(element.value, nodeValue)) {
                            message = i18n.get('common.msg.selectAfterDate', nodeValue);
                        } else if (attribute.nodeName === 'date-max' && i18n.compareSystemDate(nodeValue, element.value)) {
                            message = i18n.get('common.msg.selectBeforeDate', nodeValue);
                        }
                        break;
                    case 'time-min':
                    case 'time-max':
                        if (attribute.nodeName === 'time-min' && i18n.compareSystemTime(element.value, nodeValue)) {
                            message = i18n.get('common.msg.selectAfterTime', nodeValue);
                        } else if (attribute.nodeName === 'time-max' && i18n.compareSystemTime(nodeValue, element.value)) {
                            message = i18n.get('common.msg.selectBeforeTime', nodeValue);
                        }
                        break;
                    case 'datetime-min':
                    case 'datetime-max':
                        if (attribute.nodeName === 'datetime-min' && i18n.compareSystemDateTime(element.value, nodeValue)) {
                            message = i18n.get('common.msg.selectAfterDateTime', nodeValue);
                        } else if (attribute.nodeName === 'datetime-max' && i18n.compareSystemDateTime(nodeValue, element.value)) {
                            message = i18n.get('common.msg.selectBeforeDateTime', nodeValue);
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
                    result = !phoneRegular.test(value);
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
                    message = i18n.get('common.msg.requiredEnter');
                }
                break;
            case 'radio':
            case 'chkbox':
                if (!checkSelect(element)) {
                    message = i18n.get('common.msg.requiredSelect');
                }
                break;
            case 'fileupload':
                if (element.querySelectorAll('input[name=loadedFileSeq], input[name=fileSeq]').length === 0) {
                    message = i18n.get('document.msg.requiredFileupload');
                }
                break;
            default :
                if (element.value === '') {
                    if (element.classList.contains('custom-code-text')) { // custom-code
                        message = i18n.get('common.msg.requiredSelect');
                    } else {
                        message = i18n.get('common.msg.requiredEnter');
                    }
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
        if (buttonData !== undefined && buttonData !== '') {
            buttonData.forEach(function(element) {
                if (element.name !== '') {
                    let buttonProcessEle = document.createElement('button');
                    buttonProcessEle.type = 'button';
                    buttonProcessEle.className = 'default-fill';
                    if (element.customYn === true) {
                        buttonProcessEle.innerText = element.name;
                    } else {
                        buttonProcessEle.innerText = i18n.msg(element.name);
                    }
                    // 문서가 view 일 경우, 닫기 버튼을 제외하곤 disabled 처리한다.
                    if (documentContainer.hasAttribute('data-readonly') && element.value !== 'close') {
                        buttonProcessEle.disabled = true;
                    }
                    buttonProcessEle.addEventListener('click', function () {
                       if (element.value === 'close') {
                           if (opener !== null && opener !== undefined) { // TODO: 문서함 디자인시  window.close(); 삭제 필요.
                               window.close();
                           } else {
                               documentModal.hide();
                           }
                       } else {
                           aliceDocument.save(element.value);
                       }
                    });
                    if (buttonContainer !== null) {
                        buttonContainer.appendChild(buttonProcessEle);
                    }
                }
            });
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
        const componentElements = documentContainer.querySelectorAll('.component');
        for (let eIndex = 0; eIndex < componentElements.length; eIndex++) {
            let componentDataType = componentElements[eIndex].getAttribute('data-type');

            // 날짜 관련 컴포넌트들이 숨김일 경우 저장하지 않는다.
            const componentDisplayType = componentElements[eIndex].getAttribute('data-displayType');
            if ((componentDataType === 'date' || componentDataType === 'time' || componentDataType === 'datetime') && componentDisplayType === 'hidden') { continue; }

            if (componentDataType === 'inputbox' || componentDataType === 'date' || componentDataType === 'time' || componentDataType === 'datetime' ||
                componentDataType === 'textbox' || componentDataType === 'dropdown' || componentDataType === 'radio' || componentDataType === 'checkbox' ||
                componentDataType === 'fileupload' || componentDataType === 'custom-code') {
                let componentId = componentElements[eIndex].getAttribute('id');
                let componentValue = '';
                let componentChildObject = {};
                let componentChild = '';

                switch (componentDataType) {
                    case 'inputbox':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        componentValue = componentChild.item(0).value;
                        break;
                    case 'date':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        if (target === 'print') {
                            componentValue = componentChild.item(0).value;
                        } else {
                            componentValue = i18n.systemDate(componentChild.item(0).value);
                        }
                        break;
                    case 'time':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        if (target === 'print') {
                            componentValue = componentChild.item(0).value;
                        } else {
                            componentValue = i18n.systemTime(componentChild.item(0).value);
                        }
                        break;
                    case 'datetime':
                        componentChild = componentElements[eIndex].getElementsByTagName('input');
                        if (target === 'print') {
                            componentValue = componentChild.item(0).value;
                        } else {
                            componentValue = i18n.systemDateTime(componentChild.item(0).value);
                        }
                        break;
                    case 'textbox':
                        componentChild = componentElements[eIndex].querySelector('.editor-container');
                        if (componentChild) {
                            let textEditor = Quill.find(componentChild);
                            componentValue = JSON.stringify(textEditor.getContents());
                        } else {
                            componentChild = componentElements[eIndex].getElementsByTagName('textarea');
                            componentValue = componentChild.item(0).value;
                        }
                        break;
                    case 'dropdown':
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
                                if (componentValue === '') {
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
            tokenObject.documentId = '';
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
            tokenObject.assigneeId = aliceForm.session.userKey;
            tokenObject.assigneeType = defaultAssigneeTypeForSave;
            actionMsg = i18n.get('common.msg.save');
        } else {
            tokenObject.isComplete = true; //해당 값이 true라면 처리이다.
            tokenObject.assigneeId = '';
            tokenObject.assigneeType = '';
            actionMsg = i18n.get('document.msg.process');
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
            url = '/rest/tokens/' + tokenObject.tokenId + '/data';
        }
        const opt = {
            method: method,
            url: url,
            params: JSON.stringify(tokenObject),
            contentType: 'application/json',
            callbackFunc: function(xhr) {
                if (xhr.responseText === 'true') {
                    aliceJs.alertSuccess(actionMsg, function () {
                        if (opener !== null && opener !== undefined) { // TODO: 문서함 디자인시  window.close(); 삭제 필요.
                             opener.location.reload();
                             window.close();
                        } else {
                            documentModal.hide();
                        }
                    });
                } else {
                    aliceJs.alertDanger(i18n.get('common.msg.fail'));
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

        data.form.components = data.form.components.filter(function(comp) { return comp.type !== aliceForm.defaultType; }); //editbox 제외
        documentContainer = document.getElementById('document-panel');
        component.init(documentContainer);

        buttonContainer = document.getElementById('button-container');
        if (data.form.components.length > 0) {
            if (data.form.components.length > 2) {
                data.form.components.sort(function (a, b) {
                    return a.display.order - b.display.order;
                });
            }
            for (let i = 0, len = data.form.components.length; i < len; i++) {
                //데이터로 전달받은 컴포넌트 속성과 기본 속성을 merge한 후 컴포넌트 draw
                let componentProp = data.form.components[i];
                let componentObj = component.draw(componentProp.type, componentProp);
                data.form.components[i] = componentObj.property;
            }
            //유효성 검증 추가
            if (!documentContainer.hasAttribute('data-readonly')) {
                const checkComponents = ['inputbox', 'textbox', 'dropdown', 'radio', 'checkbox'];
                const componentElements = document.querySelectorAll('.component');
                for (let i = 0; i < componentElements.length; i++) {
                    let componentChild = getComponentTarget(componentElements[i]);
                    if (componentChild === null ||
                        checkComponents.indexOf(componentElements[i].getAttribute('data-type')) === -1) { continue; }
                    if (componentChild.classList.contains('editor-container')) { // editor
                        const quill = Quill.find(componentChild);
                        let isCheck = false;
                        quill.on('text-change', function(delta) {
                            delta.ops.forEach(function (obj) {
                                if (typeof obj.insert !== 'undefined' || typeof obj.delete !== 'undefined') {
                                    isCheck = true;
                                }
                            });
                        });
                        quill.on('selection-change', function(range, oldRange, source) {
                            if (range) {
                                isCheck = (source === 'user');
                            } else if (range === null && isCheck) {
                                checkValidate(componentChild);
                            }
                        });
                    } else {
                        componentChild.addEventListener('focusout', function() {
                            checkValidate(this);
                        }, false);
                    }
                }
            }
        }
        if (data.documentId !== undefined) {
            addIdComponent('documentId', data.documentId);
        }

        if (data.token !== undefined) {
            addIdComponent('tokenId', data.token.tokenId);
            isDocument = false;
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

            let commentBoxTextarea = document.createElement('textarea');
            commentBoxTextarea.setAttribute('placeholder',i18n.get('comment.msg.enterComments'));
            let commentButton = document.createElement('button');
            commentButton.type = 'button';
            commentButton.innerText = i18n.get('common.btn.register');
            commentButton.classList.add('default-line');
            commentButton.addEventListener('click', function () {
                aliceDocument.saveComment(instanceId, commentBoxTextarea.value);
            });

            commentContainer.appendChild(commentBoxTextarea);
            commentContainer.appendChild(commentButton);
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
                    aliceJs.alertDanger(i18n.get('common.msg.fail'));
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
        aliceJs.confirmIcon(i18n.get('common.msg.confirmDelete'), function() {
            const opt = {
                method: 'DELETE',
                url: '/rest/comments/' + commentId,
                callbackFunc: function (xhr) {
                    if (xhr.responseText) {
                        aliceJs.alertSuccess(i18n.get('common.msg.delete'), function () {
                            location.reload();
                        });
                    } else {
                        aliceJs.alertDanger(i18n.get('common.msg.fail'));
                    }
                }
            };
            aliceJs.sendXhr(opt);
        });
    }

    /**
     * 관련 문서 삭제
     * @param dataForDeletion
     */
    function deleteRelatedDoc(dataForDeletion) {
        aliceJs.confirmIcon(i18n.get('common.msg.confirmDelete'), function() {
            const opt = {
                method: 'DELETE',
                url: '/rest/folders/' + dataForDeletion.folderId,
                contentType: 'application/json',
                params: JSON.stringify(dataForDeletion),
                callbackFunc: function(xhr) {
                    if (xhr.responseText) {
                        aliceJs.alertSuccess(i18n.get('common.msg.delete'), function() {
                            location.reload();
                        });
                    } else {
                        aliceJs.alertDanger(i18n.get('common.msg.fail'));
                    }
                }
            };
            aliceJs.sendXhr(opt);
        });
    }

    /**
     * 신청서, 처리할 문서 모달 Draw.
     *
     * @param data 문서 데이터
     * @param id 문서 id
     */
    function modal(data, id) {
        this.id = id;
        this.data = data;

        this.show = function() { // 모달 표시
            if (typeof this.wrapper !== 'undefined') {
                this.wrapper.classList.add('document-modal-active');
                document.body.classList.add('document-modal-active');
            }
        };
        this.hide = function() { // 모달 숨김
            if (typeof this.wrapper !== 'undefined') {
                this.wrapper.classList.remove('document-modal-active');
                document.body.classList.remove('document-modal-active');
                this.destroy();
            }
        };
        this.create = function() {
            if (typeof this.wrapper !== 'undefined') { return; }
            let backdrop, dialog;

            this.wrapper = document.createElement('div');
            this.wrapper.classList.add('document-modal-wrapper');
            this.wrapper.id = 'document-modal-wrapper-' + this.id;

            backdrop = document.createElement('div');
            backdrop.className = 'document-modal-backdrop';

            dialog = document.createElement('div');
            dialog.classList.add('document-modal-dialog', 'container-document');

            const body = document.createElement('div');
            body.className = 'contents';

            // 상단 button 추가
            const buttonPanel = document.createElement('div');
            buttonPanel.className = 'button-board';
            body.appendChild(buttonPanel);

            // 인쇄 버튼
            const printButton = document.createElement('button');
            printButton.type = 'button';
            printButton.className = 'default-line';
            printButton.innerText = i18n.get('common.btn.print');
            printButton.addEventListener('click', print.bind(null, '/documents/' + this.id), false);
            buttonPanel.appendChild(printButton);

            // 동적 버튼
            const buttonGroup = document.createElement('div');
            buttonGroup.className = 'button-list';
            buttonGroup.id = 'button-container';
            buttonPanel.appendChild(buttonGroup);

            const documentPanel = document.createElement('div');
            documentPanel.className = 'drawing-board';
            documentPanel.id = 'document-panel';
            body.appendChild(documentPanel);

            dialog.appendChild(body);
            this.wrapper.appendChild(backdrop);
            this.wrapper.appendChild(dialog);
            document.body.appendChild(this.wrapper);
            // 문서 draw
            drawDocument(this.data);
        }
        this.destroy = function() { // 모달 제거
            if (typeof this.wrapper !== 'undefined') {
                document.body.removeChild(this.wrapper);
                this.wrapper = undefined;
            }
        };
        this.create();
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
                documentModal = new modal(dataForPrint, documentId);
                documentModal.show();
                //drawDocument(dataForPrint);
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
        sessionStorage.setItem('alice_print', JSON.stringify(dataForPrint));
        window.open(url + '/print', '_blank');
    }

    exports.init = init;
    exports.initToken = initToken;
    exports.save = save;
    exports.saveComment = saveComment;
    exports.deleteComment = deleteComment;
    exports.drawDocument = drawDocument;
    exports.checkValidate = checkValidate;
    exports.deleteRelatedDoc = deleteRelatedDoc;
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
