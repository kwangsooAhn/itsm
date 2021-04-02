(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceDocument = global.aliceDocument || {})));
}(this, (function (exports) {
    'use strict';

    let documentPanel = null;
    let buttonPanel = null;
    let commentContainer = null;
    let documentModal = null;
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
        case 'ci':
            componentTarget = componentElement.querySelector('.ci-table');
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
        const validateElement = document.querySelectorAll('.error');
        if (validateElement.length !== 0) {
            aliceJs.alertWarning(i18n.msg('document.msg.checkDocument'), function () {
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
        if (element.classList.contains('error')) {
            element.classList.remove('error');
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
                        message = i18n.msg('document.msg.lengthMin', nodeValue);
                    } else if (attribute.nodeName === 'maxlength' && length > Number(nodeValue)) {
                        message = i18n.msg('document.msg.lengthMax', nodeValue);
                    }
                    break;
                case 'date-min':
                case 'date-max':
                    if (attribute.nodeName === 'date-min' && i18n.compareSystemDate(element.value, nodeValue)) {
                        message = i18n.msg('common.msg.selectAfterDate', nodeValue);
                    } else if (attribute.nodeName === 'date-max' && i18n.compareSystemDate(nodeValue, element.value)) {
                        message = i18n.msg('common.msg.selectBeforeDate', nodeValue);
                    }
                    break;
                case 'time-min':
                case 'time-max':
                    if (attribute.nodeName === 'time-min' && i18n.compareSystemTime(element.value, nodeValue)) {
                        message = i18n.msg('common.msg.selectAfterTime', nodeValue);
                    } else if (attribute.nodeName === 'time-max' && i18n.compareSystemTime(nodeValue, element.value)) {
                        message = i18n.msg('common.msg.selectBeforeTime', nodeValue);
                    }
                    break;
                case 'datetime-min':
                case 'datetime-max':
                    if (attribute.nodeName === 'datetime-min' && i18n.compareSystemDateTime(element.value, nodeValue)) {
                        message = i18n.msg('common.msg.selectAfterDateTime', nodeValue);
                    } else if (attribute.nodeName === 'datetime-max' && i18n.compareSystemDateTime(nodeValue, element.value)) {
                        message = i18n.msg('common.msg.selectBeforeDateTime', nodeValue);
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
                element.classList.add('error');
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
        let dataType = element.id.substr(0, 2) === 'ci' ? `ci` : element.id;

        switch (dataType) {
        case 'editor':
            let textEditor = Quill.find(element);
            if (textEditor.getLength() === 1) {
                message = i18n.msg('common.msg.requiredEnter');
            }
            break;
        case 'radio':
        case 'chkbox':
            if (!checkSelect(element)) {
                message = i18n.msg('common.msg.requiredSelect');
            }
            break;
        case 'fileupload':
            if (element.querySelectorAll('input[name=loadedFileSeq], input[name=fileSeq]').length === 0) {
                message = i18n.msg('document.msg.requiredFileupload');
            }
            break;
        case 'ci':
            if (element.tBodies[0].rows.length === 1 && element.tBodies[0].firstChild.classList.contains('no-data-found-list')) {
                message = i18n.msg('common.msg.requiredEnter');
            }
            break;
        default :
            if (element.value === '') {
                if (element.classList.contains('custom-code-text')) { // custom-code
                    message = i18n.msg('common.msg.requiredSelect');
                } else {
                    message = i18n.msg('common.msg.requiredEnter');
                }
            }
            break;
        }
        return message;
    }

    /**
     * button를 만든다.
     * 저장과 취소 버튼은 기본적으로 생성된다.
     * '프로세스맵', ['접수' , '반려', '처리'], '저장', '닫기', '인쇄' 순으로 표기한다.
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
                    if (documentPanel.hasAttribute('data-readonly') && element.value !== 'close') {
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
                    if (buttonPanel !== null) {
                        buttonPanel.appendChild(buttonProcessEle);
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
        documentPanel.appendChild(comp);
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
        const componentElements = documentPanel.querySelectorAll('.component');
        for (let eIndex = 0; eIndex < componentElements.length; eIndex++) {
            let componentDataType = componentElements[eIndex].getAttribute('data-type');

            if (componentDataType === 'inputbox' || componentDataType === 'date' || componentDataType === 'time' || componentDataType === 'datetime' ||
                componentDataType === 'textbox' || componentDataType === 'dropdown' || componentDataType === 'radio' || componentDataType === 'checkbox' ||
                componentDataType === 'fileupload' || componentDataType === 'custom-code' || componentDataType === 'dynamic-row-table' || componentDataType === 'ci') {
                let componentId = componentElements[eIndex].getAttribute('id');
                let componentValue = '';
                let componentValueArr = [];
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
                    componentValueArr = [];
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
                case 'dynamic-row-table':
                    // "value": ["1행 1열 데이터", "1행 2열 데이터", "2행 1열 데이터", "2행 2열 데이터"] 형태로 데이터 전달
                    componentChild = componentElements[eIndex].getElementsByTagName('table')[0];
                    componentValueArr = [];
                    for (let rowIndex = 1, rowLen = componentChild.rows.length; rowIndex < rowLen; rowIndex++) {
                        const row = componentChild.rows[rowIndex];
                        for (let cellIndex = 0, cellLen = row.cells.length; cellIndex < cellLen; cellIndex++) {
                            const cell = row.cells[cellIndex];
                            const childElem  = cell.children[0];
                            let childValue = '';
                            // DR Table 컴포넌트 내부는 inputbox, select, checkbox, radio 등으로 이루어진다. (추후 구현 예정)
                            switch(childElem.type) {
                            case 'text':
                                childValue = childElem.value;
                                break;
                            default:
                                break;
                            }
                            componentValueArr.push(childValue);
                        }
                    }

                    componentValue = JSON.stringify(componentValueArr);
                    break;
                case 'ci':
                    const componentData = aliceDocument.data.form.components[eIndex];
                    componentValueArr = [];
                    // 삭제, 조회일 경우에는 actionType과 ciId만 저장한다.
                    const allowedKeys = ['actionType', 'ciId', 'ciStatus'];
                    const filterActionType = ['delete', 'read'];
                    componentData.value.forEach(function(v) {
                        if (filterActionType.includes(v.actionType)) {
                            const filterValue = Object.keys(v)
                                .filter( function (key) { return allowedKeys.includes(key); })
                                .reduce(function (obj, key) {
                                    obj[key] = v[key];
                                    return obj;
                                }, {});
                            componentValueArr.push(filterValue);
                        } else {
                            componentValueArr.push(v);
                        }
                    });
                    componentValue = JSON.stringify(componentValueArr);
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
        let exceptionList = ['save', 'cancel', 'terminate'];
        if ((exceptionList.indexOf(v_kind) === -1) && checkValidateForSave()) {
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

        //instanceId 값 추가
        const instanceElements = document.getElementById('instanceId');
        if (instanceElements !== null && instanceElements !== undefined) {
            tokenObject.instanceId = instanceElements.getAttribute('data-id');
        } else {
            tokenObject.instanceId = '';
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
            actionMsg = i18n.msg('common.msg.save');
        } else {
            tokenObject.isComplete = true; //해당 값이 true라면 처리이다.
            tokenObject.assigneeId = '';
            tokenObject.assigneeType = '';
            actionMsg = i18n.msg('document.msg.process');
        }

        const componentArrayList = getComponentData(); // 모든 컴포넌트 데이터 가져오기
        if (componentArrayList.length > 0) {
            tokenObject.componentData = componentArrayList;
        }

        tokenObject.action = v_kind;
        let method = '';
        let url = '';
        if (tokenObject.tokenId === '') {
            method = 'post';
            url = '/rest/tokens/data';
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
                    aliceJs.alertDanger(i18n.msg('common.msg.fail'));
                }
            }
        };
        aliceJs.sendXhr(opt);
    }

    /**
     * 컴포넌트 ID를 전달 받아서 린트용 데이터(폼 출력용)에서 일치하는 컴포넌트의 index 반환한다
     * @param {String} id 조회할 컴포넌트 id
     * @return {Number} component index 조회한 컴포넌트 index
     */
    function getComponentIndex(id) {
        for (let i = 0, len = aliceDocument.data.form.components.length; i < len; i++) {
            let comp = dataForPrint.form.components[i];
            if (comp.componentId === id) { return i; }
        }
        return -1;
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
        documentPanel = document.getElementById('document-panel');
        component.init(documentPanel);
        buttonPanel = document.getElementById('button-panel');
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
            // 유효성 검증 추가
            if (documentPanel.getAttribute('data-display') !== 'complete') {
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

        if (data.instanceId !== undefined) {
            addIdComponent('instanceId', data.instanceId);
        }

        if (data.token !== undefined) {
            addIdComponent('tokenId', data.token.tokenId);
            createTokenInfoTab();
        }
        if (data.actions !== undefined) {
            addButton(data.actions);
        }
        aliceJs.initDesignedSelectTag();
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
            commentBoxTextarea.setAttribute('placeholder', i18n.msg('comment.msg.enterComments'));
            commentBoxTextarea.className = 'textarea-scroll-wrapper';
            let commentButton = document.createElement('button');
            commentButton.type = 'button';
            commentButton.innerText = i18n.msg('common.btn.register');
            commentButton.classList.add('default-line');
            commentButton.addEventListener('click', function () {
                aliceDocument.saveComment(instanceId, commentBoxTextarea.value);
            });

            commentContainer.appendChild(commentBoxTextarea);
            commentContainer.appendChild(commentButton);

            OverlayScrollbars(commentContainer.querySelector('textarea'), {
                className: 'inner-scrollbar',
                resize: 'vertical',
                sizeAutoCapable: true,
                textarea: {
                    dynHeight: false,
                    dynWidth: false,
                    inheritedAttrs: 'class'
                }
            });
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
                    createTokenInfoTab();
                } else {
                    aliceJs.alertDanger(i18n.msg('common.msg.fail'));
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
        aliceJs.confirmIcon(i18n.msg('common.msg.confirmDelete'), function() {
            const opt = {
                method: 'DELETE',
                url: '/rest/comments/' + commentId,
                callbackFunc: function (xhr) {
                    if (xhr.responseText) {
                        aliceJs.alertSuccess(i18n.msg('common.msg.delete'), function () {
                            createTokenInfoTab();
                        });
                    } else {
                        aliceJs.alertDanger(i18n.msg('common.msg.fail'));
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
        aliceJs.confirmIcon(i18n.msg('common.msg.confirmDelete'), function() {
            const opt = {
                method: 'DELETE',
                url: '/rest/folders/' + dataForDeletion.folderId,
                contentType: 'application/json',
                params: JSON.stringify(dataForDeletion),
                callbackFunc: function(xhr) {
                    if (xhr.responseText) {
                        aliceJs.alertSuccess(i18n.msg('common.msg.delete'), function() {
                            createTokenInfoTab();
                        });
                    } else {
                        aliceJs.alertDanger(i18n.msg('common.msg.fail'));
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
    function openDocumentModal(data, id) {
        const getDocumentModalContent = function() {
            const body = document.createElement('div');
            body.className = 'document-main flex-column align-items-center'; // contents

            // 메인 상단 영역
            const mainHeader = document.createElement('div');
            mainHeader.className = 'document-main-header flex-row justify-content-end align-items-center';
            body.appendChild(mainHeader);

            // 상단 button 추가
            const buttonPanel = document.createElement('div');
            buttonPanel.className = 'btn-list';
            mainHeader.appendChild(buttonPanel);

            // 동적 버튼
            const buttonGroup = document.createElement('div');
            buttonGroup.id = 'button-panel';
            buttonPanel.appendChild(buttonGroup);

            // 인쇄 버튼
            const printButton = document.createElement('button');
            printButton.type = 'button';
            printButton.className = 'default-line';
            printButton.innerText = i18n.msg('common.btn.print');
            printButton.addEventListener('click', print.bind(null, '/documents/' + id), false);
            buttonPanel.appendChild(printButton);

            const documentPanel = document.createElement('div');
            documentPanel.className = 'drawing-board';
            documentPanel.id = 'document-panel';
            documentPanel.setAttribute('data-display', 'document');
            body.appendChild(documentPanel);
            return body;
        };

        documentModal = new modal({
            title: '',
            body: getDocumentModalContent(),
            classes: 'document-modal-dialog document-container',
            buttons:[],
            close: {
                closable: false,
            },
            onCreate: function (modal) {
                // 문서 draw
                drawDocument(data);
            }
        });
        documentModal.show();
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
                responseObject.documentId = documentId;
                aliceDocument.data = responseObject;
                openDocumentModal(aliceDocument.data, documentId);
                // dataForPrint 변수가 전역으로 무슨 목적이 있는 것 같아 그대로 살려둠.
                dataForPrint = responseObject;

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
                aliceDocument.data = responseObject;
                drawDocument(aliceDocument.data);
                // dataForPrint 변수가 전역으로 무슨 목적이 있는 것 같아 그대로 살려둠.
                dataForPrint = responseObject;
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

    /**
     * 태그 추가.
     *
     * @param tag 태그 정보
     */
    function onAddTag(tag) {
        const jsonData = {
            tagContent: tag.detail.data.value,
            instanceId: document.getElementById('instanceId').getAttribute('data-id')
        };
        aliceJs.sendXhr({
            method: 'POST',
            url: '/rest/tags',
            params: JSON.stringify(jsonData),
            contentType: 'application/json',
            showProgressbar: true,
            callbackFunc: function (response) {
                createTokenInfoTab();
            }
        });
    }

    /**
     * 태그 삭제.
     *
     * @param tag 태그 정보
     */
    function onRemoveTag(tag) {
        aliceJs.sendXhr({
            method: 'DELETE',
            url: '/rest/tags/' + tag.detail.data.id,
            showProgressbar: true,
            callbackFunc: function (response) {
                createTokenInfoTab();
            }
        });
    }

    /**
     * 문서의 오른쪽 탭 정보를 조회한다.
     * @param data 탭 정보를 생성함에 있어 사용할 parameters.
     */
    function createTokenInfoTab() {
        let instanceId = document.getElementById('instanceId').getAttribute('data-id');
        let tokenId = document.getElementById('tokenId').getAttribute('data-id');
        // 프린트 페이지일 경우,
        if (document.querySelector('.token-properties') === null) { return false; }

        // 탭 정보들 조회하여 셋팅.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/tokens/' + tokenId + '/edit-tab',
            async: false,
            showProgressbar: true,
            callbackFunc: function (response) {
                if (document.querySelector('.token-properties').children) {
                    document.querySelector('.token-properties').remove();
                    let tokenInfo = document.createElement('div');
                    tokenInfo.className = 'token-properties';
                    document.querySelector('.document-container').append(tokenInfo);
                }
                document.querySelector('.token-properties').innerHTML = response.responseText;

                // 탭 정보에 이벤트를 등록
                document.querySelectorAll('.token-info-tab > h4').forEach((ele) => {
                    ele.addEventListener('click', (e) => {
                        // 탭 동작
                        Array.prototype.filter.call(e.target.parentNode.children, function (child) {
                            return child !== e.target;
                        }).forEach((sblingEle) => {
                            sblingEle.classList.remove('active');
                        });
                        e.target.classList.add('active');
                        // 컨텐츠 내용 동작
                        let targetElement = document.querySelector('.' + e.target.dataset.targetContents);
                        if (targetElement != null) {
                            Array.prototype.filter.call(targetElement.parentNode.children, function (child) {
                                return child !== targetElement;
                            }).forEach((sblingEle) => {
                                sblingEle.style.display = 'none';
                            });
                            targetElement.style.display = 'block';
                        }
                        // 선택된 탭을 저장 > 새로고침시 초기화를 막기 위함
                        sessionStorage.setItem('token-info-tab', e.target.dataset.targetContents);
                    });
                });

                // userDate 변환
                document.querySelectorAll('.dateFormatFromNow').forEach((element) => {
                    element.textContent = dateFormatFromNow(element.textContent);
                });
                document.querySelectorAll('.user-date-time').forEach((element) => {
                    element.textContent = i18n.userDateTime(element.textContent);
                });

                addCommentBox(instanceId);

                new Tagify(document.querySelector('input[name=tags]'), {
                    pattern: /^.{0,100}$/,
                    editTags: false,
                    callbacks: {
                        'add': onAddTag,
                        'remove': onRemoveTag
                    },
                    placeholder: i18n.msg('token.msg.tag')
                });

                const selectedTab = sessionStorage.getItem('token-info-tab') ? sessionStorage.getItem('token-info-tab') : 'token-history';
                document.querySelector('h4[data-target-contents="' + selectedTab + '"]').click();
                OverlayScrollbars(document.querySelectorAll('.token-info-contents'), {className: 'scrollbar'});
            }
        });
    }

    exports.init = init;
    exports.initToken = initToken;
    exports.save = save;
    exports.saveComment = saveComment;
    exports.deleteComment = deleteComment;
    exports.drawDocument = drawDocument;
    exports.checkValidate = checkValidate;
    exports.deleteRelatedDoc = deleteRelatedDoc;
    exports.createTokenInfoTab = createTokenInfoTab;
    exports.print = print;
    exports.getComponentIndex = getComponentIndex;

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
