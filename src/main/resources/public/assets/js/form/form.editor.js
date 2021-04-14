/**
 * @projectDescription Form Designer Editor Library
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.editor = global.editor || {})));
}(this, (function (exports) {
    'use strict';

    let ATTRIBUTE_TABLE_COLUMN = 'drTableColumns'; // 컴포넌트 세부 속성 - drTableColumns
    const history = {
        redo_list: [],
        undo_list: [],
        saveHistory: function(data, list, keep_redo) {
            if (data.length === 1 && workflowUtil.compareJson(data[0][0], data[0][1])) { // data check
                return;
            }
            keep_redo = keep_redo || false;
            if (!keep_redo) {
                this.redo_list = [];
            }
            (list || this.undo_list).push(data);

            isEdited = !workflowUtil.compareJson(editor.data, savedData);
            changeFormName();
        },
        undo: function() {
            if (this.undo_list.length) {
                let restoreData = this.undo_list.pop();
                restoreForm(restoreData, 'undo');
                this.saveHistory(restoreData, this.redo_list, true);
            }
        },
        redo: function() {
            if (this.redo_list.length) {
                let restoreData = this.redo_list.pop();
                restoreForm(restoreData, 'redo');
                this.saveHistory(restoreData, this.undo_list, true);
            }
        }
    };
    
    const shortcuts = [
        { 'keys': 'ctrl+s', 'command': 'editor.save(false);', 'force': true },                //폼 양식 저장
        { 'keys': 'ctrl+shift+s', 'command': 'editor.saveAs();', 'force': true },             //폼 양식 다른이름으로 저장
        { 'keys': 'ctrl+z', 'command': 'editor.undo();', 'force': false },                    //폼 편집 화면 작업 취소
        { 'keys': 'ctrl+shift+z', 'command': 'editor.redo();', 'force': false },              //폼 편집 화면 작업 재실행
        { 'keys': 'ctrl+e', 'command': 'editor.preview();', 'force': false },                 //폼 양식 미리보기
        { 'keys': 'insert', 'command': 'editor.copyComponent();', 'force': false },           //컴포넌트를 복사하여 바로 아래 추가
        { 'keys': 'ctrl+x,delete', 'command': 'editor.deleteComponent();', 'force': false },  //컴포넌트 삭제
        { 'keys': 'ctrl+pagedown', 'command': 'editor.addEditboxDown();', 'force': false },   //아래 컴포넌트 새로 만들기
        { 'keys': 'ctrl+home', 'command': 'editor.selectFirstComponent();', 'force': false }, //첫번째 컴포넌트 선택
        { 'keys': 'ctrl+end', 'command': 'editor.selectLastComponent();', 'force': false },   //마지막 컴포넌트 선택
        { 'keys': 'up', 'command': 'editor.selectUpComponent();', 'force': false },           //바로위 컴포넌트 선택
        { 'keys': 'down', 'command': 'editor.selectDownComponent();', 'force': false },       //바로위 컴포넌트 선택
        { 'keys': 'alt+e', 'command': 'editor.selectProperties();', 'force': false }          //세부 속성 편집: 제일 처음으로 이동
    ];

    let isEdited = false;
    window.addEventListener('beforeunload', function (event) {
        if (isEdited) {
            event.preventDefault();
            event.returnValue = '';
        }
    });
    let isView = true;            //view 인지여부
    let data = {};                 //저장용 데이터
    let savedData = {};

    let formPanel = null,
        propertiesPanel = null,
        previousComponentIds = [], //이전에 선택한 컴포넌트
        selectedComponentIds = [], //선택된 컴포넌트 ID
        customCodeList = null;        //커스텀 컴포넌트 세부속성에서 사용할 코드 데이터

    /**
     * text, textarea validate check.
     * 
     * @param eventName 이벤트명
     * @param element target element
     * @param validate validate 속성
     */
    function validateCheck(eventName, element, validate) {
        if (typeof validate === 'undefined' || validate === '') { return; }
        // TODO: 정규표현식 다국어 지원
        const numberRegex = /^[-+]?[0-9]*\.?[0-9]+$/;
        const labelRegex = /^([a-zA-Z0-9가-힣]{0,2})$|^([a-zA-Z0-9가-힣])([a-zA-Z0-9가-힣-_\.])*([a-zA-Z0-9가-힣])$/i; // 숫자와 문자(영문자, 한글)로 시작, 종료 되어야하고 숫자,문자, 쉼표(.), 언더바(_), 대시(-) 를 포함 할 수 있다.
        const validateFunc = {
            number: function(value) {
                return numberRegex.test(value);
            },
            min: function(value, arg) {
                if (numberRegex.test(arg)) {
                    return (value >= Number(arg));
                }
                return true;
            },
            max: function(value, arg) {
                if (numberRegex.test(arg)) {
                    return (value <= Number(arg));
                }
                return true;
            },
            minLength: function(value, arg) {
                if (numberRegex.test(arg)) {
                    return (value.length >= Number(arg));
                }
                return true;
            },
            maxLength: function(value, arg) {
                if (numberRegex.test(arg)) {
                    return (value.length <= Number(arg));
                }
                return true;
            },
            required: function(value) {
                return !aliceJs.isEmpty(value);
            },
            label: function(value) { // 라벨링 전용
                return labelRegex.test(value);
            }
        };
        // 이벤트 등록
        element.addEventListener(eventName, function(e) {
            const target = e.target;
            let targetMsg = target.parentNode.querySelector('.error-msg') || target.parentNode.parentNode.querySelector('.error-msg');
            if (target.parentNode.tagName === 'TD') { // table 일 경우
                const tb = aliceJs.clickInsideElement(e, 'property-field-table');
                targetMsg = tb.parentNode.querySelector('.error-msg');
            }
            if (target.classList.contains('error')) {
                target.classList.remove('error');
                targetMsg.classList.remove('on');
            }
            let result = true;
            let validateArray = validate.split('|');
            for (let i = 0; i < validateArray.length; i++) {
                let validateValueArray = validateArray[i].split('[');
                // 필수값을 어긴 경우
                if (validateValueArray[0] !== 'required' && target.value === '') { break; }

                let arg = (typeof validateValueArray[1] !== 'undefined') ? validateValueArray[1].replace(/\]\s*$/gi, '') : '';
                switch (validateValueArray[0]) {
                case 'number':
                    result = validateFunc.number(target.value);
                    break;
                case 'min':
                    result = validateFunc.number(target.value);
                    if (result) {
                        result = validateFunc.min(target.value, arg);
                    } else {
                        validateValueArray[0] = 'number';
                    }
                    break;
                case 'max':
                    result = validateFunc.number(target.value);
                    if (result) {
                        result = validateFunc.max(target.value, arg);
                    } else {
                        validateValueArray[0] = 'number';
                    }
                    break;
                case 'minLength':
                    result = validateFunc.minLength(target.value, arg);
                    break;
                case 'maxLength':
                    result = validateFunc.maxLength(target.value, arg);
                    break;
                case 'required':
                    result = validateFunc.required(target.value);
                    break;
                case 'label':
                    result = validateFunc.label(target.value);
                    break;
                case 'unique': // table 중복 체크
                    result = true;
                    if (target.parentNode.tagName === 'TD') { // table 일 경우
                        const tb = aliceJs.clickInsideElement(e, 'property-field-table');
                        const targetCellIdx = target.parentNode.cellIndex;
                        const targetRowIdx = target.parentNode.parentNode.rowIndex;
                        for (let i = 1; i < targetRowIdx; i++) {
                            const tbRow = tb.rows[i];
                            const inputCell = tbRow.cells[targetCellIdx].querySelector('input');
                            if (target.value.trim() === inputCell.value.trim()) {
                                result = false;
                                break;
                            }
                        }
                    }
                default:
                    break;
                }
                if (!result) {
                    // 유효성 검증 실패시 저장되지 않도록 기존 이벤트를 모두 중지함
                    e.stopImmediatePropagation();
                    // 에러 문구 표시
                    target.classList.add('error');
                    targetMsg.innerHTML = i18n.msg('form.msg.' + validateValueArray[0], arg);
                    targetMsg.classList.add('on');
                    break;
                }
            }
        });
    }

    /**
     * 중복 validate check
     */
    function uniqueCheck() {
        // TODO: 공통 유효성 체크로 변경 필요. (validation-alice.js)
        // custom tag로 data-validate를 사용하는 것은 현재 폼 디자이너만 사용하니 추후 공통으로 적용하는 방향이 좋을 것 같음.
        const tbs = propertiesPanel.querySelectorAll('table');
        for (let i = 0, tbLen = tbs.length; i < tbLen; i++) {
            const tb = tbs[i];
            const tbValidate = tb.getAttribute('date-validate');
            if (tbValidate && tbValidate === 'unique') {
                const errorMsg = tb.parentNode.querySelector('.error-msg');
                let keys = [];
                for (let j = 1, rowLen = tb.rows.length; j < rowLen; j++) {
                    const tbRow = tb.rows[j];
                    const inputCell = tbRow.cells[1].querySelector('input');
                    if (keys.indexOf(inputCell.value.trim()) > -1) {
                        inputCell.classList.add('error');
                        errorMsg.innerHTML = i18n.msg('form.msg.duplicate');
                        errorMsg.classList.add('on');
                        return false;
                    }
                    keys.push(inputCell.value.trim());
                }
            }
        }
    }

    /**
     * 폼 저장
     *
     * @param {String} flag 저장후  닫을지 여부
     */
    function saveForm(flag) {
        //view 모드이면 단축키로 저장되지 않는다.
        if (isView) { return false; }

        // 유효성이 통과되지 않으면 저장되지 않는다.
        if (!hasErrorClass)  {  return false; }

        data = JSON.parse(JSON.stringify(editor.data));

        // 2020-05-22 Jung Hee Chan
        // datetime 형태의 속성들은 저장을 위해 시스템 공통 포맷으로 변경한다. (YYYY-MM-DD HH:mm, UTC+0)
        data.components = aliceForm.reformatCalendarFormat('save', data.components);

        let lastCompIndex = component.getLastIndex();
        data.components = data.components.filter(function(comp) {
            return !(comp.display.order === lastCompIndex && comp.type === aliceForm.defaultType);
        });
        aliceJs.sendXhr({
            method: 'PUT',
            url: '/rest/form/' + data.formId + '/data',
            callbackFunc: function(xhr) {
                if (xhr.responseText) {
                    isEdited = false;
                    savedData = JSON.parse(JSON.stringify(editor.data));
                    changeFormName();
                    if (flag) {
                        aliceAlert.alertSuccess(i18n.msg('common.msg.save'), function () {
                            if (window.opener && !window.opener.closed) {
                                opener.location.reload();
                            }
                            window.close();
                        });
                    } else {
                        aliceAlert.alertSuccess(i18n.msg('common.msg.save'));
                    }
                } else {
                    aliceAlert.alertDanger(i18n.msg('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(data)
        });
    }

    /**
     * 다른 이름으로 저장.
     */
    function saveAsForm() {

        /**
         * 다른 이름으로 저장 content.
         *
         * @return {string} content html
         */
        const createDialogContent = function() {
            return `
                <div class="save-as-main flex-column">
                    <label class="field-label" for="form_name">${i18n.msg('form.label.name')}<span class="required"></span></label>
                    <input type="text" id="form_name">
                    <label class="field-label" for="form_description">${i18n.msg('form.label.description')}</label>
                    <textarea rows="3" class="textarea-scroll-wrapper" id="form_description"></textarea>
                </div>
                `;
        };

        /**
         * 필수체크.
         *
         * @return {boolean} 체크성공여부
         */
        const checkRequired = function() {
            let nameLabelElem = document.getElementById('form_name');
            if (nameLabelElem.value.trim() === '') {
                nameLabelElem.classList.add('error');
                aliceAlert.alertWarning(i18n.msg('common.msg.requiredEnter'), function() {
                    nameLabelElem.focus();
                });
                return false;
            }
            nameLabelElem.classList.remove('error');
            return true;
        };

        /**
         *  저장처리.
         */
        const saveAs = function() {
            data = JSON.parse(JSON.stringify(editor.data));

            // datetime 형태의 속성들은 저장을 위해 시스템 공통 포맷으로 변경한다. (YYYY-MM-DD HH:mm, UTC+0)
            data.components = aliceForm.reformatCalendarFormat('save', data.components);

            let lastCompIndex = component.getLastIndex();
            data.components = data.components.filter(function (comp) {
                return !(comp.display.order === lastCompIndex && comp.type === aliceForm.defaultType);
            });
            data.name = document.getElementById('form_name').value;
            data.desc = document.getElementById('form_description').value;
            aliceJs.sendXhr({
                method: 'POST',
                url: '/rest/forms' + '?saveType=saveas',
                callbackFunc: function (xhr) {
                    if (xhr.responseText !== '') {
                        aliceAlert.alertSuccess(i18n.msg('common.msg.save'), function () {
                            if (window.opener && !window.opener.closed) {
                                opener.location.reload();
                            }
                            window.name = 'form_' + xhr.responseText + '_edit';
                            location.href = '/form/' + xhr.responseText + '/edit';
                        });
                    } else {
                        aliceAlert.alertDanger(i18n.msg('common.label.fail'));
                    }
                },
                contentType: 'application/json; charset=utf-8',
                params: JSON.stringify(data)
            });
        };

        /**
         * 다른 이름으로 저장하기 모달 저장 CallBack.
         */
        const saveAsCallBack = function() {
            if (checkRequired()) {
                saveAs();
                return true;
            }
            return false;
        };

        /**
         * 다른 이름으로 저장하기 모달.
         */
        const saveAsModal = new modal({
            title: i18n.msg('common.btn.saveAs'),
            body: createDialogContent(),
            classes: 'save-as',
            buttons: [
                {
                    content: i18n.msg('common.btn.save'),
                    classes: 'default-line',
                    bindKey: false,
                    callback: function(modal) {
                        if (saveAsCallBack()) {
                            modal.hide();
                        }
                    }
                }, {
                    content: i18n.msg('common.btn.cancel'),
                    classes: 'default-line',
                    bindKey: false,
                    callback: function(modal) {
                        modal.hide();
                    }
                }
            ],
            close: {
                closable: false,
            },
            onCreate: function(modal) {
                OverlayScrollbars(document.getElementById('form_description'), {
                    className: 'scrollbar',
                    resize: 'none',
                    sizeAutoCapable: true,
                    textarea: {
                        dynHeight: false,
                        dynWidth: false,
                        inheritedAttrs: 'class'
                    }
                });
            }
        });
        saveAsModal.show();
    }

    /**
     * 폼을 다시 그리고, 데이터 수정를 수정 한다.
     *
     * @param restoreData 데이터
     * @param type 타입(undo, redo)
     */
    function restoreForm(restoreData, type) {
        const restore = function (originData, changeData) {
            if (!Object.keys(originData).length || !Object.keys(changeData).length) { // add or delete component
                if (!Object.keys(changeData).length) { // delete component
                    let element = document.getElementById(originData.componentId);
                    element.remove();
                    for (let i = 0, len = editor.data.components.length; i < len; i++) {
                        if (originData.componentId === editor.data.components[i].componentId) {
                            editor.data.components.splice(i, 1);
                            break;
                        }
                    }
                } else { // add component
                    let componentObj = component.draw(changeData.type, changeData);
                    const compOrder = Number(changeData.display.order) - 1;
                    let targetElement = formPanel.querySelectorAll('.component').item(compOrder);
                    targetElement.parentNode.insertBefore(componentObj.domElem, targetElement);
                    setComponentData(componentObj.property);
                }
                reorderComponent();
            } else { // modify
                if (typeof changeData.type === 'undefined') {  // form
                    editor.data = changeData;
                    if (originData.name !== changeData.name) { // modify name
                        changeFormName();
                    }
                } else { // component
                    let componentObj = component.draw(changeData.type, JSON.parse(JSON.stringify(changeData)));
                    let compProp = componentObj.property;
                    setComponentData(compProp);
                    let targetElement = document.getElementById(compProp.componentId);
                    if (originData.display.order !== changeData.display.order) {
                        targetElement.innerHTML = '';
                        targetElement.remove();
                        const compOrder = Number(changeData.display.order) - 1;
                        let nextElement = formPanel.querySelectorAll('.component').item(compOrder);
                        nextElement.parentNode.insertBefore(componentObj.domElem, nextElement);
                    } else {
                        targetElement.parentNode.insertBefore(componentObj.domElem, targetElement);
                        targetElement.innerHTML = '';
                        targetElement.remove();
                    }
                    reorderComponent();
                }
            }
        };
        let historyData = JSON.parse(JSON.stringify(restoreData));
        if (historyData.length > 1 && type === 'redo') {
            historyData.reverse();
        }
        historyData.forEach(function(data) {
            let originData = data[1],
                changeData = data[0];
            if (type === 'redo') {
                originData = data[0];
                changeData = data[1];
            }
            restore(JSON.parse(JSON.stringify(originData)), JSON.parse(JSON.stringify(changeData)));
        });
        editor.showFormProperties();
    }

    /**
     * 작업 취소
     */
    function undoForm() {
        history.undo();
    }

    /**
     * 작업 재실행
     */
    function redoForm() {
        history.redo();
    }

    /**
     * 미리보기
     */
    function previewForm() {
        const itemName = 'alice_forms-preview-' + editor.data.formId;
        sessionStorage.setItem(itemName, JSON.stringify({'form': editor.data}));
        let url = '/form/' + editor.data.formId + '/preview';
        const specs = 'left=0,top=0,menubar=no,toolbar=no,location=no,status=no,titlebar=no,scrollbars=yes,resizable=no';
        window.open(url, itemName, 'width=1200,height=900,' + specs);
    }

    /**
     * 컴포넌트 신규 추가
     * @param {String} type 컴포넌트 타입
     * @param {String} id 컴포넌트 Id
     */
    function addComponent(type, id) {
        if (type !== undefined) { //기존 editbox를 지운후, 해당 컴포넌트 추가
            let histories = [];
            let elem = document.getElementById(id);

            let replaceEditbox = editor.data.components.filter((comp) => comp.componentId === id);
            let replaceObj = component.draw(type);
            let compProp = replaceObj.property;
            compProp.componentId = id;
            setComponentData(compProp);

            replaceObj.domElem.id = id;
            elem.parentNode.insertBefore(replaceObj.domElem, elem);

            // accordion-start 컴포넌트이면 accordion-end도 함께 그려준다.
            let accordionEndCompId = '';
            if (type === 'accordion-start') {
                let accordionEndObj = component.draw('accordion-end');
                let accordionEndProp = accordionEndObj.property;
                accordionEndProp.display.startId = id;

                compProp.display.endId = accordionEndProp.componentId;
                setComponentData(accordionEndProp);

                elem.parentNode.insertBefore(accordionEndObj.domElem, elem);
                accordionEndCompId = accordionEndProp.componentId;
            }

            elem.innerHTML = '';
            elem.remove();

            reorderComponent();

            let addCompAttr = editor.data.components.filter((comp) => comp.componentId === id);
            histories.push({
                0: JSON.parse(JSON.stringify(replaceEditbox[0])),
                1: JSON.parse(JSON.stringify(addCompAttr[0]))
            });

            if (accordionEndCompId !== '') {
                let accordionEndAttr = editor.data.components.filter((comp) => comp.componentId === accordionEndCompId);
                histories.push({0: {}, 1: JSON.parse(JSON.stringify(accordionEndAttr[0]))});

                addEditboxDown(accordionEndCompId, function (attr) {
                    histories.push({0: {}, 1: JSON.parse(JSON.stringify(attr))});
                    history.saveHistory(histories);
                });
            } else {
                addEditboxDown(id, function (attr) {
                    histories.push({0: {}, 1: JSON.parse(JSON.stringify(attr))});
                    history.saveHistory(histories);
                });
            }
            
        } else { // Enter 키를 누를 경우 editbox를 추가한다.
            let editbox = component.draw(aliceForm.defaultType);
            setComponentData(editbox.property);
            editbox.domElem.querySelector('[contenteditable=true]').focus();
            selectedComponentIds.length = 0;
            selectedComponentIds.push(editbox.id);
            showComponentProperties();
            reorderComponent();
        }
    }

    /**
     * 컴포넌트 복사
     * @param {String} elemId 선택한 element Id
     */
    function copyComponent(elemId) {
        let copyElemId = elemId || selectedComponentIds[0];
        let elem = document.getElementById(copyElemId);
        if (elem === null) { return; }

        //복사
        for (let i = 0; i < editor.data.components.length; i++) {
            if (copyElemId === editor.data.components[i].componentId) {
                let copyData = JSON.parse(JSON.stringify(editor.data.components[i]));
                copyData.componentId = workflowUtil.generateUUID();
                let componentObj = component.draw(copyData.type, copyData);
                setComponentData(componentObj.property);
                elem.parentNode.insertBefore(componentObj.domElem, elem.nextSibling);
                //재정렬
                reorderComponent();
                if (copyData.type === aliceForm.defaultType) {
                    componentObj.domElem.querySelector('[contenteditable=true]').focus();
                }
                componentObj.domElem.click();
                let copyCompAttr = editor.data.components.filter(function(c) { return c.componentId === componentObj.id; });
                history.saveHistory([{0: {}, 1: JSON.parse(JSON.stringify(copyCompAttr[0]))}]);
                break;
            }
        }
    }

    /**
     * 컴포넌트 삭제
     */
    function deleteComponent() {
        if (selectedComponentIds.length === 0) { return false; }

        // editbox 컴포넌트 1개만 존재할 경우 삭제 로직을 타지 않는다.
        const firstElem = document.getElementById(selectedComponentIds[0]);
        if (document.querySelectorAll('.component').length === 1 && firstElem &&
            firstElem.getAttribute('data-type') === aliceForm.defaultType) { return false; }

        // 컴포넌트 삭제
        let histories = [];
        let delIdx = [];
        for (let i = 0, len = selectedComponentIds.length; i < len; i++) {
            const delComp = document.getElementById(selectedComponentIds[i]);
            delIdx.push(Number(delComp.getAttribute('data-index')));
            delComp.remove();
            const compIdx = getComponentIndex(selectedComponentIds[i]);
            histories.push({0: JSON.parse(JSON.stringify(editor.data.components[compIdx])), 1: {}});
            editor.data.components.splice(compIdx, 1);
        }
        previousComponentIds.length = 0;

        // 이력 재정렬
        if (histories.length > 1) {
            histories.sort(function (a, b) {
                return a[0].display.order < b[0].display.order ? -1 : a[0].display.order > b[0].display.order ? 1 : 0;
            });
        }

        // 삭제 후 바로 이전 컴포넌트에 focus가 이동한다.
        let focusElem = null;
        let focusIdx = Math.min.apply(null, delIdx);
        let components = document.querySelectorAll('.component');
        if (components.length === 0) { // 컴포넌트 없을 경우 editbox 컴포넌트 신규 추가한다.
            const editbox = component.draw(aliceForm.defaultType);
            histories.push({0: {}, 1: JSON.parse(JSON.stringify(editbox.property))});
            setComponentData(editbox.property);
            focusElem = editbox.domElem;
        } else {
            focusElem = components[focusIdx - 1];
            if (typeof focusElem === 'undefined') { //컴포넌트가 존재하지 않으면 마지막 컴포넌트를 선택한다.
                focusElem = formPanel.lastElementChild;
            }
        }
        if (focusElem.getAttribute('data-type') === aliceForm.defaultType) {
            focusElem.querySelector('[contenteditable=true]').focus();
        } else {
            focusElem.focus();
        }
        focusElem.click();
        //재정렬
        reorderComponent();
        // 이력저장
        history.saveHistory(histories);
    }

    /**
     * 선택된 컴포넌트의 순서를 담은 배열을 반환
     */
    function getSelectComponentIndex() {
        let rtn = [];
        if (selectedComponentIds.length > 1) {
            let i, len, compIdxs = [];
            for (i = 0, len = selectedComponentIds.length; i < len; i++) {
                const comp = document.getElementById(selectedComponentIds[i]);
                compIdxs.push(Number(comp.getAttribute('data-index')));
            }
            compIdxs.sort(function(a, b) { // 오름차순 재정렬
                return a - b;
            });
            rtn = compIdxs.slice();
            for (i = 1, len = compIdxs.length; i < len; i++) {
                if (compIdxs[i] - compIdxs[i - 1] > 1) { 
                    rtn.length = 0;
                    break;
                }
            }
        }
        return rtn;
    }

    /**
     * 첫번째 컴포넌트 선택
     */
    function selectFirstComponent() {
        const firstComponent = formPanel.firstElementChild;
        if (firstComponent.getAttribute('data-type') === aliceForm.defaultType) {
            firstComponent.querySelector('[contenteditable=true]').focus();
        }
        formPanel.scrollTop = 0;
        selectedComponentIds.length = 0;
        selectedComponentIds.push(firstComponent.id);
        showComponentProperties();
    }

    /**
     * 마지막 컴포넌트 선택 : 컴포넌트가 1개 밖에 없을 경우 첫번째 컴포넌트 선택됨
     */
    function selectLastComponent() {
        const lastComponent = formPanel.lastElementChild;
        if (lastComponent.getAttribute('data-type') === aliceForm.defaultType) {
            lastComponent.querySelector('[contenteditable=true]').focus();
        }
        formPanel.scrollTop = formPanel.scrollHeight;
        selectedComponentIds.length = 0;
        selectedComponentIds.push(lastComponent.id);
        showComponentProperties();
    }

    /**
     * 바로 위 컴포넌트 선택
     */
    function selectUpComponent() {
        if (document.getElementById('context-menu').classList.contains('on')) { return false; }
        if (selectedComponentIds.length !== 1) { return false; }

        let selectedElem = document.getElementById(selectedComponentIds[0]);
        if (selectedElem !== null) {
            let previousElem = selectedElem.previousElementSibling;
            if (previousElem === null && selectedElem.getAttribute('data-index') === '1') {
                formPanel.scrollTop = formPanel.scrollHeight;
                previousElem = formPanel.lastElementChild;
            }
            if (previousElem.getAttribute('data-type') === aliceForm.defaultType) {
                previousElem.querySelector('[contenteditable=true]').focus();
            }
            selectedComponentIds.length = 0;
            selectedComponentIds.push(previousElem.id);
            showComponentProperties();
        }
    }

    /**
     * 바로 아래 컴포넌트 선택
     */
    function selectDownComponent() {
        if (document.getElementById('context-menu').classList.contains('on')) { return false; }
        if (selectedComponentIds.length !== 1) { return false; }

        let selectedElem = document.getElementById(selectedComponentIds[0]);
        if (selectedElem !== null) {
            let nextElem = selectedElem.nextElementSibling;
            if (nextElem === null && Number(selectedElem.getAttribute('data-index')) === component.getLastIndex()) {
                formPanel.scrollTop = 0;
                nextElem = formPanel.firstElementChild;
            }
            if (nextElem.getAttribute('data-type') === aliceForm.defaultType) {
                nextElem.querySelector('[contenteditable=true]').focus();
            }
            selectedComponentIds.length = 0;
            selectedComponentIds.push(nextElem.id);
            showComponentProperties();
        }
    }

    /**
     * 세부 속성 편집: 제일 처음으로 이동
     */
    function selectProperties() {
        const selectElems = propertiesPanel.querySelectorAll('input[type=text]:not([readonly]), select');
        if (selectElems.length === 0) { return false; }

        selectElems[0].focus();
    }

    /**
     * elemId 선택한 element Id를 기준으로 아래에 editbox 추가 후 data의 display order 변경
     * @param {String} elemId 선택한 element Id
     * @param {Function} callbackFunc callback function
     */
    function addEditboxDown(elemId, callbackFunc) {
        let addElemId = elemId || selectedComponentIds[selectedComponentIds.length - 1];
        let elem = document.getElementById(addElemId);
        if (elem === null) { return; }

        let editbox = null;
        if (elem.nextSibling !== null) {
            editbox = component.draw(aliceForm.defaultType);
            setComponentData(editbox.property);
            elem.parentNode.insertBefore(editbox.domElem, elem.nextSibling);
        } else { //마지막에 추가된 경우
            editbox = component.draw(aliceForm.defaultType);
            setComponentData(editbox.property);
            elem.parentNode.appendChild(editbox.domElem);
        }
        // 컴포넌트 순서 재정렬
        reorderComponent();

        editbox.domElem.querySelector('[contenteditable=true]').focus();
        editbox.domElem.click();

        if (typeof callbackFunc === 'function') {
            callbackFunc(editbox.property);
        } else {
            let addEditboxCompAttr = editor.data.components.filter(function(comp) { return comp.componentId === editbox.id; });
            history.saveHistory([{0: {}, 1: JSON.parse(JSON.stringify(addEditboxCompAttr[0]))}]);
        }
    }

    /**
     * 컴포넌트 재정렬
     */
    function reorderComponent() {
        const components = document.querySelectorAll('.component');
        for (let i = 0, len = components.length; i < len; i++) {
            let elem = components[i];
            elem.setAttribute('data-index', String(i + 1));
            elem.setAttribute('tabIndex', String(i + 1));
            //데이터 display 순서 변경
            for (let j = 0, len = editor.data.components.length; j < len; j++) {
                let comp = editor.data.components[j];
                if (comp.componentId === elem.id) {
                    comp.display.order = i + 1;
                    break;
                }
            }
        }
        //컴포넌트 재정렬
        editor.data.components.sort(function (a, b) {
            return a.display.order < b.display.order ? -1 : a.display.order > b.display.order ? 1 : 0;
        });
        component.setLastIndex(components.length);
        aliceJs.initDesignedSelectTag();
    }

    /**
     * 컴포넌트 ID를 전달 받아서 일치하는 컴포넌트의 index 반환한다
     * @param {String} id 조회할 컴포넌트 id
     * @return {Number} component index 조회한 컴포넌트 index
     */
    function getComponentIndex(id) {
        for (let i = 0, len = editor.data.components.length; i < len; i++) {
            let comp = editor.data.components[i];
            if (comp.componentId === id) { return i; }
        }
        return -1;
    }

    /**
     * 컴포넌트 데이터 추가/수정
     * @param {Object} compData 컴포넌트 데이터
     */
    function setComponentData(compData) {
        let isExist = false;
        for (let i = 0, len = editor.data.components.length; i < len; i++) {
            let comp = editor.data.components[i];
            if (comp.componentId === compData.componentId) {//수정
                editor.data.components[i] = compData;
                isExist = true;
                break;
            }
        }
        if (!isExist) {//추가
            editor.data.components.push(compData);
        }
    }

    /**
     * 컴포넌트의 데이터를 전달받아서 우측 properties panel 출력용으로 컴포넌트 기본 속성을 정제하여 조회한다.
     *
     * 2020-06-03 Jung Hee Chan
     *   - 오타, 네이밍, 주석 수정 및 추가.
     *   - 날짜시간 속성에 대한 reformat은 reformatCalendarFormat과 함께 향후 정리가 필요.
     *   - 최초 기본 속성을 가져와서 사용하는 경우에도 사용자의 포맷으로 변경하기 위해서 변환 추가를 했으나
     *   - 변환을 1번만 하는게 아니라 새로 그릴때마다 해서 지금 값이 최초 기본 속성인지 알수가 없음.
     *   - form.core에서 initSync에서 파일 읽어올때 처리하도록 했음. 결론적으로 여기는 정리만...
     *
     * @param {Object} componentData 컴포넌트 데이터. 화면처럼 속성그룹, 속성명과 값이 포함.
     * @return {String} initializedProperties 정제한 컴포넌트 기본 속성 데이터
     */
    function initProperties(componentData) {
        // set default component properties
        let initializedProperties = aliceJs.mergeObject({}, aliceForm.componentProperties[componentData.type]);

        Object.keys(componentData).forEach(function(propertyGroupId) {
            if (initializedProperties.hasOwnProperty(propertyGroupId)) {
                if (aliceJs.isObject(componentData[propertyGroupId]))  { // object
                    Object.keys(componentData[propertyGroupId]).forEach(function(propertyId) {
                        Object.keys(initializedProperties[propertyGroupId]).forEach(function(initProperty) {
                            if (propertyId === initializedProperties[propertyGroupId][initProperty].id) {
                                initializedProperties[propertyGroupId][initProperty].value = componentData[propertyGroupId][propertyId];
                            }
                        });
                    });
                } else { // array
                    if (propertyGroupId === ATTRIBUTE_TABLE_COLUMN) { // dynamic row table 컴포넌트
                        const defaultArrayProperties = JSON.parse(JSON.stringify(initializedProperties[propertyGroupId]));

                        let tempArrayProperties;
                        initializedProperties[propertyGroupId] = [];
                        Object.keys(componentData[propertyGroupId]).forEach(function(idx) {
                            tempArrayProperties = defaultArrayProperties;
                            Object.keys(defaultArrayProperties).forEach(function(subIdx) {
                                tempArrayProperties[subIdx] = Object.assign({}, defaultArrayProperties[subIdx]);
                                tempArrayProperties[subIdx].value = componentData[propertyGroupId][idx][defaultArrayProperties[subIdx].id];
                            });
                            initializedProperties[propertyGroupId][idx] = tempArrayProperties;
                        });
                    }
                }
            }
        });
        return initializedProperties;
    }

    /**
     * 컴포넌트를 다시 그린다.
     *  @param {Object} data 변경된 속성
     */
    function redrawComponent(data) {
        const id = data.componentId;
        // 지우기
        let removeElement = document.getElementById(id);
        removeElement.innerHTML = '';
        removeElement.remove();

        // 신규 추가
        let element = component.draw(data.type, data);
        if (element) {
            // 데이터 재할당
            let compAttr = element.property;
            compAttr.componentId = id;
            setComponentData(compAttr);

            element.domElem.id = id;
            formPanel.insertBefore(element.domElem, formPanel.children[Number(data.display.order) - 1]);

            // 선택된 컴포넌트가 여러개면, 상단에 tooptip menu에 delete 항목만 표시
            if (selectedComponentIds.length > 1) {
                const menuItems = element.domElem.querySelectorAll('.menu-item');
                for (let j = 0, menuLen = menuItems.length; j < menuLen; j++) {
                    if (!menuItems[j].classList.contains('delete')) {
                        menuItems[j].classList.add('hidden');
                    }
                }
            }

            element.domElem.classList.add('selected');
            // 선택된 컴포넌트가 인접할 경우 border 없애기
            if (element.domElem.previousSibling !== null &&
                selectedComponentIds.indexOf(element.domElem.previousSibling.id) !== -1 &&
                !element.domElem.previousSibling.classList.contains('adjoin')) {
                element.domElem.previousSibling.classList.add('adjoin');
            }

            reorderComponent();
        }
    }

    /**
     * DR Table 컴포넌트의 Column 을 다시 그린다.
     *  @param {Number} index 열
     *  @param {Object} data 변경된 속성
     */
    function redrawDRTableColumns(index, data) {
        const displayType = data['dataAttribute']['displayType'];
        const columnData = data[ATTRIBUTE_TABLE_COLUMN][index];

        const selectedComponent = document.getElementById(data.componentId);
        if (selectedComponent === null) { return; }

        const drTable =  selectedComponent.querySelector('.dr-table');

        // 너비 변경
        drTable.rows[0].cells[index].style.width = '' + ((Number(columnData['width']) / 12) * 100) + '%';

        // 컬럼명 변경
        drTable.rows[0].cells[index].innerHTML = columnData['text'];

        // 컬럼 데이터 타입에 따른 다시 그리기
        for (let i = 1, rowLen = drTable.rows.length; i < rowLen; i ++) {
            const row = drTable.rows[i];
            row.cells[index].innerHTML = component.getDRTableColumnsTemplate(columnData.type, columnData, displayType);
        }
    }

    /**
     * 변경된 값을 컴포넌트 속성 정보에 반영하고, 컴포넌트를 다시 그린다.
     * @param {String} value 변경된 값
     * @param {String} group 변경된 그룹 key
     * @param {String} field 변경된 field key (option일 때만 field가 존재하지 않음)
     * @param {Number} [index] 변경된 index (그룹이 option 일 경우만 해당되므로 생략가능)
     * @param {String} subGroup 세부 속성이 존재할때, 변경된 세부 그룹 key
     */
    function changePropertiesValue(value, group, field, index, subGroup) {
        if (typeof group === 'undefined' || group === '') { return false; }

        let histories = [];
        for (let i = 0, len = selectedComponentIds.length; i < len; i++) {
            const compIdx = getComponentIndex(selectedComponentIds[i]);
            let componentData = editor.data.components[compIdx];
            let originComponentData = JSON.parse(JSON.stringify(componentData));

            if (typeof field === 'undefined') { //+, - 버튼 옵션 추가
                if (!Array.isArray(value)) {
                    componentData[group].push(value);
                } else {
                    componentData[group] = value;
                }
            } else {
                if (typeof index === 'undefined') {
                    componentData[group][field] = value;
                } else {
                    if (typeof subGroup === 'undefined') {
                        componentData[group][index][field] = value;
                    } else {
                        componentData[group][index][field][subGroup] = value;
                    }
                }
            }
            // dr table 컴포넌트의 column 을 새로 그려준다.
            if (group === ATTRIBUTE_TABLE_COLUMN && index !== undefined) {
                redrawDRTableColumns(index, componentData);
            } else {
                redrawComponent(componentData);
            }
            histories.push({0: originComponentData, 1: JSON.parse(JSON.stringify(componentData))});
        }
        history.saveHistory(histories);
    }

    /**
     * date, time, datetime default 포멧 변경시,
     * default 값을 none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등으로 저장한다.
     * @param {Object} e 이벤트 대상
     */
    function setDateFormat(e) {
        let el = e.target || e;
        let parentEl = el.parentNode.parentNode;
        let checkedRadio = parentEl.parentNode.querySelector('input[type=radio]:checked');
        if (checkedRadio !== null) { // radio 버튼 존재시
            if (parentEl.querySelector('input[type=radio]').id !== checkedRadio.id) { return false; }
            let checkedPropertiesArr = checkedRadio.name.split('-');
            let changeValue = checkedRadio.value;
            if (changeValue === 'none' || changeValue === 'now') {
                changePropertiesValue(changeValue, checkedPropertiesArr[0], checkedPropertiesArr[1]);
            } else {
                let inputCells = parentEl.querySelectorAll('input[type=text]');
                if (changeValue === 'datepicker' || changeValue === 'timepicker' || changeValue === 'datetimepicker') {
                    changeValue += ('|' + inputCells[0].value);
                } else {
                    for (let i = 0, len = inputCells.length; i < len; i++ ) {
                        changeValue += ('|' + inputCells[i].value);
                    }
                }
                changePropertiesValue(changeValue, checkedPropertiesArr[0], checkedPropertiesArr[1]);
            }
        } else {
            let changePropertiesArr = parentEl.id.split('-');
            changePropertiesValue(el.value, changePropertiesArr[0], changePropertiesArr[1]);
        }
    }

    /**
     * 커스텀 코드 컴포넌트 세부 속성 변경시 일치하는 세부 항목을 가져온다.
     * @param e 이벤트
     * @param data 컴포넌트 데이터
     */
    function changeCustomCodeHandler(e, data) {
        let customCodeDataSelect = (typeof data !== 'undefined') ? e : propertiesPanel.querySelector('input[id=code]').parentNode.parentNode.querySelector('select');
        let customCode = (typeof data !== 'undefined') ? data.customCode : e.target.value;
        customCodeDataSelect.innerHTML = '';
        if (typeof data === 'undefined' && typeof e.target !== 'undefined' && e.target.id === 'customCode') {
            const changePropertiesArr = e.target.parentNode.parentNode.id.split('-');
            changePropertiesValue(e.target.value, changePropertiesArr[0], changePropertiesArr[1]);
        }

        aliceJs.sendXhr({ //커스텀 코드 변경에 따른 커스텀 데이터 조회
            method: 'GET',
            url: '/rest/custom-codes/' + customCode,
            callbackFunc: function(xhr) {
                let customCodeData = JSON.parse(xhr.responseText);
                customCodeDataSelect.innerHTML = customCodeData.map(d => `<option value='${d.key}'>${d.value}</option>`).join('');
                // for designed select
                // 디자인된 select를 위해 wrap 위에서 querySelector 사용
                let targetRadio = customCodeDataSelect.parentNode.parentNode.querySelector('input[type=radio]');
                if (targetRadio.checked) {
                    if (typeof data !== 'undefined' && data.default !== '') {
                        const propValueArr = data.default.split('|');
                        customCodeDataSelect.value = propValueArr[1];
                    }
                    let val = 'code|' + customCodeDataSelect.value + '|';
                    if (customCodeDataSelect.selectedIndex !== -1) {
                        val += customCodeDataSelect.options[customCodeDataSelect.selectedIndex].text;
                    }
                    const targetName = targetRadio.name.split('-');
                    changePropertiesValue(val, targetName[0], targetName[1]);
                }
            },
            contentType: 'application/json; charset=utf-8',
            showProgressbar: false
        });
    }

    /**
     * 컴포넌트 세부 속성에  button click 이벤트 (Position, Align 등 버튼이 고정)
     * @param e 이벤트
     */
    function toggleOptionButtonClickHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-field');
        const parentElem = elem.parentNode; // property-field-position, property-field-align
        const groupElem = parentElem.parentNode;
        let changePropertiesArr = groupElem.id.split('-'); // property-field
        const isActive = elem.classList.contains('active');
        if (!isActive) {
            for (let i = 0, len = parentElem.childNodes.length ; i< len; i++) {
                let child = parentElem.childNodes[i];
                if (child.classList.contains('active')) {
                    child.classList.remove('active');
                }
            }
            elem.classList.add('active');
            if (groupElem.getAttribute('data-field-type')) {
                changePropertiesValue(elem.id, groupElem.getAttribute('data-field-type'), changePropertiesArr[0], groupElem.getAttribute('data-field-index'), changePropertiesArr[1]);
            } else {
                changePropertiesValue(elem.id, changePropertiesArr[0], changePropertiesArr[1]);
            }
        }
    }

    function toggleSessionButtonClickHandler(e) {
        const toggleBtnElem = e.target; // 선택된 toggle 버튼
        const parentElem = toggleBtnElem.parentNode; // property-field-toggle
        const parentGroupElem = parentElem.parentNode; // property-field
        let changePropertiesArr = parentGroupElem.id.split('-'); // property-field
        if (!toggleBtnElem.classList.contains('active')) {
            let defaultValue = '';
            for (let i = 0, len = parentElem.childNodes.length ; i< len; i++) {
                const child = parentElem.childNodes[i];
                const toggleTargetElem = parentGroupElem.querySelector('#' + child.name);
                if (child.name === toggleBtnElem.name) {
                    toggleBtnElem.classList.add('active');
                    toggleTargetElem.style.display = 'block';
                    if (toggleTargetElem.tagName === 'SELECT') {
                        toggleTargetElem.options[0].selected = true;
                        defaultValue = toggleTargetElem.options[0].value + '|' + toggleTargetElem.options[0].text;
                        // for designed select
                        if (toggleTargetElem.parentNode.classList.contains('select')) {
                            toggleTargetElem.parentElement.querySelector('.designed-select').innerText = toggleTargetElem.options[0].text;
                        }
                    } else {
                        defaultValue = toggleTargetElem.value;
                    }
                    // for designed select
                    // toggle 에 따라 designed select 를 화면에 표시 출력.
                    if (toggleTargetElem.parentNode.classList.contains('select')) {
                        toggleTargetElem.parentNode.style.display = 'block';
                    }
                } else {
                    if (child.classList.contains('active')) {
                        child.classList.remove('active');
                    }
                    toggleTargetElem.style.display = 'none';
                    // for designed select
                    // toggle 에 따라 designed select 를 화면에서 숨김.
                    if (toggleTargetElem.parentNode.classList.contains('select')) {
                        toggleTargetElem.parentNode.style.display = 'none';
                    }
                }
            }
            changePropertiesValue(toggleBtnElem.name + '|' + defaultValue, changePropertiesArr[0], changePropertiesArr[1]);
        }
    }

    /**
     * 컴포넌트 세부 속성에  button click 이벤트 (boid, italic, underline, align 등)
     * @param e 이벤트
     */
    function toggleButtonClickHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-field');
        const parentElem = elem.parentNode;
        const isActive = elem.classList.contains('active');
        if (parentElem.id === 'align') {
            if (!isActive) {
                for (let i = 0, len = parentElem.childNodes.length ; i< len; i++) {
                    let child = parentElem.childNodes[i];
                    if (child.classList.contains('active')) {
                        child.classList.remove('active');
                    }
                }
                elem.classList.add('active');
                changePropertiesValue(elem.id, parentElem.parentNode.parentNode.parentNode.id, parentElem.id);
            }
        } else {
            if (isActive) {
                elem.classList.remove('active');
                elem.setAttribute('data-value', 'N');
            } else {
                elem.classList.add('active');
                elem.setAttribute('data-value', 'Y');
            }
            changePropertiesValue(isActive ? 'N' : 'Y', parentElem.parentNode.parentNode.id, elem.id);
        }
    }

    /**
     * 컴포넌트 세부 속성에 옵션 추가(+) button click 이벤트
     * @param e 이벤트
     */
    function addOptionHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-option');
        const tb = elem.parentNode.parentNode.parentNode.querySelector('table');
        const row = document.createElement('tr');
        const rowCount = tb.rows.length;
        const firstRow = tb.rows[0];
        const lastRow = tb.rows[rowCount - 1];

        row.innerHTML = lastRow.innerHTML;
        let rowData = {};
        for (let i = 0; i < firstRow.cells.length; i++) {
            const cell = row.cells[i];
            const inputCell = cell.querySelector('input');
            if (inputCell.type === 'checkbox') { // seq
                inputCell.value = rowCount;
                inputCell.id = 'checkbox-' + rowCount;
                inputCell.parentNode.setAttribute('for', 'checkbox-' + rowCount);
            } else {
                inputCell.addEventListener('change', function(e) {
                    let changeCell = e.target;
                    let changeTd = changeCell.parentNode;
                    let changeRow = changeTd.parentNode;
                    changePropertiesValue(changeCell.value, tb.parentNode.id, changeTd.id, changeRow.childNodes[0].childNodes[0].childNodes[0].value - 1);
                }, false);
                inputCell.value = firstRow.cells[i].getAttribute('data-default');
            }
            rowData[cell.id] = inputCell.value;
        }
        tb.appendChild(row);
        changePropertiesValue(rowData, tb.parentNode.id);
    }

    /**
     *  컴포넌트 세부 속성에 옵션 삭제(-) button click 이벤트
     * @param e
     */
    function removeOptionHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-option');
        const tb = elem.parentNode.parentNode.parentNode.querySelector('table');
        const compIdx = getComponentIndex(selectedComponentIds[0]);
        let removeOptionData = JSON.parse(JSON.stringify(editor.data.components[compIdx][tb.parentNode.id]));
        let minusCnt = 0;
        let rowCount = tb.rows.length;
        for (let i = 1; i < rowCount; i++) {
            let row = tb.rows[i];
            let chkBox = row.cells[0].childNodes[0].childNodes[0];
            if (chkBox.checked && rowCount > 2) {
                tb.deleteRow(i);
                removeOptionData.splice(i - 1, 1);
                rowCount--;
                i--;
                minusCnt++;
            } else if (chkBox.value !== i) {
                chkBox.value = i;
                chkBox.id = 'checkbox-' + i;
                chkBox.parentNode.setAttribute('for', 'checkbox-' + i);
                removeOptionData[i - 1].seq = i;
            }
        }
        if (minusCnt > 0) {
            changePropertiesValue(removeOptionData, tb.parentNode.id);
        }
    }
    
    /**
     *  dynamic Row table 컴포넌트의 열 옵션 추가(+) button click 이벤트
     * @param e
     */
    function addDRTableColumnsHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-option');
        const groupElem = elem.parentNode.parentNode.parentNode;
        // column 추가
        const columnBlocks = groupElem.querySelectorAll('.property-field-block');
        const columnBlockLen = columnBlocks.length;
        const columnGroupTemplate =
            `<div class="property-field-block" id="column-${columnBlockLen}">` +
               `<div class="property-field-block-header">` +
                   `<label class="checkbox" for="chk-${columnBlockLen}" tabindex="0">` +
                       `<input type="checkbox" id="chk-${columnBlockLen}" value="${columnBlockLen}"/>` +
                       `<span></span>` +
                       `<span class="label">${i18n.msg('form.attribute.drTableColumns', (columnBlockLen + 1))}</span>` +
                   `</label>` +
               `</div>` +
           `</div>`;
        groupElem.insertAdjacentHTML('beforeend', columnGroupTemplate);

        // component 데이터 추가
        let drTableColumnDefaultData = component.getProperty('dynamic-row-table', {})['drTableColumns'][0];
        const compIdx = getComponentIndex(selectedComponentIds[0]);
        let drTableColumnData = JSON.parse(JSON.stringify(editor.data.components[compIdx][groupElem.id]));
        drTableColumnData.push(drTableColumnDefaultData);
        changePropertiesValue(drTableColumnData, groupElem.id);
    }

    /**
     *  dynamic Row table 컴포넌트의 열 옵션 삭제(-) button click 이벤트
     * @param e
     */
    function removeDRTableColumnsHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-option');
        const groupElem = elem.parentNode.parentNode.parentNode;

        // component 데이터
        const compIdx = getComponentIndex(selectedComponentIds[0]);
        let fieldData = JSON.parse(JSON.stringify(editor.data.components[compIdx][groupElem.id]));

        let fieldBlockLen = groupElem.children.length;
        const checkedBlockLen = groupElem.querySelectorAll('input[type="checkbox"]:checked').length;

        if (checkedBlockLen === (fieldBlockLen - 1)) {
            aliceAlert.alertWarning(i18n.msg('form.msg.failedAllColumnDelete'));
            return false;
        } else {
            // 삭제
            let deleteCount = 0;
            for (let i = 1; i < fieldBlockLen; i++) {
                const fieldBlock = groupElem.children[i];
                const chkElem = fieldBlock.querySelector('input[type=checkbox]');
                if (chkElem) {
                    if (chkElem.checked && fieldBlockLen > 2) {
                        groupElem.removeChild(fieldBlock);
                        fieldData.splice(i - 1, 1);
                        fieldBlockLen--;
                        i--;
                        deleteCount++;
                    } else if (chkElem.value !== i) {
                        fieldBlock.id = 'column-' + (i - 1);
                        chkElem.value = i - 1;
                        chkElem.id = 'chk-' + (i - 1);
                        chkElem.parentNode.htmlFor = 'chk-'  + (i - 1);
                        chkElem.parentNode.querySelector('.label').textContent = i18n.msg('form.attribute.drTableColumns', i);
                        // id 재할당
                        const fields = fieldBlock.querySelectorAll('.property-field');
                        for (let j = 0; j < fields.length; j++) {
                            let curIds = fields[j].id.split('-');
                            fields[j].id = curIds[0] + '-' + curIds[1] + '-' + (i - 1);
                        }
                    }
                }
            }
            // 새로 그려준다.
            if (deleteCount > 0) {
                changePropertiesValue(fieldData, groupElem.id);
            }
        }
    }

    /**
     * 컴포넌트 dataAttribute-label 세부 속성에 옵션 추가(+) button click 이벤트
     * @param e 이벤트
     */
    function addLabelOptionHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-option');
        const tb = elem.parentNode.querySelector('table');
        const row = document.createElement('tr');
        const rowCount = tb.rows.length;
        const firstRow = tb.rows[0];
        let rowData = {};

        let checkBoxTemplate = `<td>` +
                `<label class="checkbox" for="checkbox-label-${rowCount}" tabindex="0">` +
                    `<input type="checkbox" id="checkbox-label-${rowCount}" value="${rowCount}" />` +
                    `<span></span>` +
                `</label>` +
            `</td>`;

        for (let i = 0; i < firstRow.cells.length; i++) {
            const cell = firstRow.cells[i];
            if (i === 0) {
                row.insertAdjacentHTML('beforeend', checkBoxTemplate);
            } else {
                let rowTemplate = `<td>` +
                        `<input type="text" id="${cell.getAttribute('data-role')}" class="property-value" value="${aliceJs.filterXSS(cell.getAttribute('data-default'))}" ` +
                            `data-validate="${cell.getAttribute('data-validate')}"/>` +
                    `</td>`;
                row.insertAdjacentHTML('beforeend', rowTemplate);
            }
        }
        // 이벤트 등록
        const inputCells = row.querySelectorAll('input[type=text]');
        for (let i = 0; i < inputCells.length; i++) {
            inputCells[i].addEventListener('change', function(e) {
                const changeRow = e.target.parentNode.parentNode;
                const labelTb = changeRow.parentNode.parentNode; // row > tbody > table
                const changeCheckbox = changeRow.querySelector('input[type=checkbox]');
                const labelTbArr = tb.parentNode.id.split('-');
                const tableIdArr = tb.id.split('-');
                let changeData = {};
                changeData[changeRow.cells[1].childNodes[0].value] = changeRow.cells[2].childNodes[0].value;

                changePropertiesValue(changeData, labelTbArr[0], tableIdArr[1], labelTbArr[1], Number(changeCheckbox.value) - 1);
            }, false);
            // 유효성 추가
            validateCheck('keyup', inputCells[i], inputCells[i].getAttribute('data-validate'));
        }
        rowData[firstRow.cells[1].getAttribute('data-default')] = firstRow.cells[2].getAttribute('data-default');
        tb.getElementsByTagName('tbody')[0].appendChild(row);

        const changePropertiesArr = tb.parentNode.id.split('-');
        const tableIdArr = tb.id.split('-');
        changePropertiesValue(rowData, changePropertiesArr[0], tableIdArr[1], changePropertiesArr[1], rowCount - 1);
    }

    /**
     *  컴포넌트 dataAttribute-label 세부 속성에 옵션 삭제(-) button click 이벤트
     * @param e
     */
    function removeLabelOptionHandler(e) {
        const elem = aliceJs.clickInsideElement(e, 'btn-option');
        const tb = elem.parentNode.querySelector('table');
        const changePropertiesArr = tb.parentNode.id.split('-');
        const tableIdArr = tb.id.split('-');
        const compIdx = getComponentIndex(selectedComponentIds[0]);
        let removeOptionData = JSON.parse(JSON.stringify(editor.data.components[compIdx][changePropertiesArr[0]][changePropertiesArr[1]][tableIdArr[1]]));
        let minusCnt = 0;
        let rowCount = tb.rows.length;
        for (let i = 1; i < rowCount; i++) {
            let row = tb.rows[i];
            let chkBox = row.cells[0].childNodes[0].childNodes[0];
            if (chkBox.checked) {
                tb.deleteRow(i);
                removeOptionData.splice(i - 1, 1);
                rowCount--;
                i--;
                minusCnt++;
            } else if (chkBox.value !== i) {
                chkBox.value = i;
                chkBox.id = 'checkbox-label-' + i;
                chkBox.parentNode.setAttribute('for', 'checkbox-label-' + i);
            }
        }
        if (minusCnt > 0) {
            changePropertiesValue(removeOptionData, changePropertiesArr[0], tableIdArr[1], changePropertiesArr[1]);
        } else {
            aliceAlert.alertWarning(i18n.msg('common.msg.deleteTartget'));
        }
    }

    /**
     *  이벤트 추가
     * @param target 이벤트 등록 대상상
    */
    function addChangePropertiesEvent(target) {
        // keyup 이벤트 추가
        const inputElems = target.querySelectorAll('input[type=text]:not([readonly]):not(.input-image)');
        let i, len;
        for (i = 0, len = inputElems.length; i < len; i++) {
            if (inputElems[i].id === 'date' || inputElems[i].id === 'time' || inputElems[i].id === 'datetime-day' || inputElems[i].id === 'datetime-hour') {
                inputElems[i].addEventListener('keyup', setDateFormat, false);
            } else {
                inputElems[i].addEventListener('keyup', function (e) {
                    const elem = e.target;
                    let parentElem = elem.parentNode;
                    if (parentElem.classList.contains('picker-wrapper') || parentElem.classList.contains('wdp-hour-el-container')) { return false; } // date picker 제외
                    if (parentElem.tagName === 'TD') { // option
                        const row = parentElem.parentNode;
                        const tb = row.parentNode.parentNode;
                        const tableIdArr = tb.id.split('-');
                        const seqCell = row.cells[0].childNodes[0].childNodes[0];
                        if (tableIdArr[1] === 'option') {
                            changePropertiesValue(elem.value, tableIdArr[1], parentElem.id, Number(seqCell.value) - 1);
                        } else if (tableIdArr[1] === 'label') {
                            const changePropertiesArr = tb.parentNode.id.split('-');
                            let changeData = {};
                            if (elem.id === 'key') {
                                changeData[elem.value] = row.cells[2].childNodes[0].value;
                            } else {
                                changeData[row.cells[1].childNodes[0].value] = elem.value;
                            }
                            changePropertiesValue(changeData, changePropertiesArr[0], tableIdArr[1], changePropertiesArr[1], Number(seqCell.value) - 1);
                        }
                    } else {
                        if (parentElem.id === '') {
                            parentElem = parentElem.parentNode;
                        }
                        const changePropertiesArr = parentElem.id.split('-');
                        let changeValue = elem.value;
                        if (elem.classList.contains('session')) { changeValue = elem.id + '|' + elem.value; }
                        // changePropertiesArr 의 길이가 어떤 의미인지 알 수 없어서 무식하게 조건만 추가했음. (2021-02 hcjung)
                        //  - 이런 식이면 계속 if 문만 늘어나게 코딩할 수 밖에 없음. 최악의 방향으로... ㅠㅠ
                        //  - DOM id를 이용한 방법은 리펙토링에서 빼는 것이 좋을 것 같다.
                        if (changePropertiesArr.length > 2) {
                            if (changePropertiesArr[0] !== ATTRIBUTE_TABLE_COLUMN && parentElem.getAttribute('data-field-type') === ATTRIBUTE_TABLE_COLUMN) {
                                changePropertiesValue(changeValue, parentElem.getAttribute('data-field-type'), changePropertiesArr[0], parentElem.getAttribute('data-field-index'), changePropertiesArr[1]);
                            } else {
                                changePropertiesValue(changeValue, changePropertiesArr[0], changePropertiesArr[1], changePropertiesArr[2]);
                            }
                        } else {
                            if (parentElem.getAttribute('data-field-type')) {
                                changePropertiesValue(changeValue, parentElem.getAttribute('data-field-type'), changePropertiesArr[0], parentElem.getAttribute('data-field-index'), changePropertiesArr[1]);
                            } else {
                                changePropertiesValue(changeValue, changePropertiesArr[0], changePropertiesArr[1]);
                            }
                        }
                    }
                }, false);
            }
        }

        // input=range 이벤트 추가
        const rangeElems = target.querySelectorAll('input[type=range]');
        for (i = 0, len = rangeElems.length; i < len; i++) {
            const rangeElem = rangeElems[i];
            rangeElem.addEventListener('input', function(e) {
                let elem  = e.target;
                const parentElem = elem.parentNode.parentNode;
                const slider = parentElem.querySelector('input[type=text]');
                slider.value = elem.value;
                const changePropertiesArr = parentElem.id.split('-');
                if (changePropertiesArr.length > 2) {
                    changePropertiesValue(elem.value, changePropertiesArr[0], changePropertiesArr[1], changePropertiesArr[2]);
                } else {
                    changePropertiesValue(elem.value, changePropertiesArr[0], changePropertiesArr[1]);
                }
            }, false);
        }

        // change 이벤트 추가
        const changeElems =  target.querySelectorAll('input[type=checkbox], input[type=radio], select, input[class*="color"]');
        for (i = 0, len = changeElems.length; i < len; i++) {
            const changeElem = changeElems[i];
            switch (changeElem.type) {
            case 'checkbox':
                if (changeElem.classList.contains('property-value')) {
                    changeElem.addEventListener('change', function (e) {
                        const changePropertiesArr = e.target.parentNode.parentNode.id.split('-');
                        changePropertiesValue(e.target.checked, changePropertiesArr[0], changePropertiesArr[1]);
                    }, false);
                }
                break;
            case 'text':
                changeElem.addEventListener('change', function(e) {
                    let elem  = e.target;
                    if (elem.classList.contains('color')) { // color picker
                        const changePropertiesArr = elem.id.split('-');
                        let opacity = (typeof elem.dataset['opacity'] !== 'undefined' && elem.dataset['opacity'] !== '') ? Number(elem.dataset['opacity']) / 100 : 1;
                        if (!aliceJs.isHexCode(elem.value)) {
                            elem.value = aliceJs.rgbaToHex(elem.value); // opacity 값 갱신하기 위해 Hex로 변환
                        }
                        elem.value = aliceJs.hexToRgba(elem.value, opacity);
                        changePropertiesValue( elem.value, changePropertiesArr[0], changePropertiesArr[1]);
                    } else {
                        let parentElem =  elem.parentNode;
                        const changePropertiesArr = parentElem.id.split('-');
                        changePropertiesValue(elem.value, changePropertiesArr[0], changePropertiesArr[1]);
                    }
                }, false);
                break;
            case 'radio':
                if (changeElem.parentNode.parentNode.classList.contains('radio-datetime')) { // date picker
                    changeElem.addEventListener('change', setDateFormat, false);
                } else {
                    changeElem.addEventListener('change', function (e) {
                        const elem = e.target;
                        const parentElem = elem.parentNode.parentNode; // vertical-group
                        const changePropertiesArr = parentElem.parentNode.id.split('-');
                        let val = (elem.id !== 'none') ? elem.id + '|' + parentElem.querySelector('select').value : elem.id;
                        if (elem.checked && elem.id !== 'none') {
                            val += '|';
                            if (parentElem.querySelector('select').selectedIndex !== -1) {
                                val += parentElem.querySelector('select').options[parentElem.querySelector('select').selectedIndex].text;
                            }
                        }
                        changePropertiesValue(val, changePropertiesArr[0], changePropertiesArr[1]);
                    }, false);
                }
                break;
            default: //select
                if (changeElem.id === 'customCode') {
                    changeElem.addEventListener('change', changeCustomCodeHandler, false);
                } else {
                    changeElem.addEventListener('change', function (e) {
                        const elem = e.target;
                        const parentElem = elem.parentElement.classList.contains('select') ? elem.parentNode.parentNode : elem.parentNode;
                        if (elem.classList.contains('session')) {
                            const changePropertiesArr = parentElem.id.split('-');
                            changePropertiesValue(elem.id + '|' + elem.value + '|' + elem.options[elem.selectedIndex].text, changePropertiesArr[0], changePropertiesArr[1]);
                        } else {
                            const targetRadio = parentElem.querySelector('input[type=radio]');
                            if (targetRadio !== null) {
                                const changePropertiesArr = parentElem.parentNode.id.split('-');
                                if (!targetRadio.checked) { return; }
                                let val = (targetRadio.id !== 'none') ? targetRadio.id + '|' + elem.value : targetRadio.id;
                                if (targetRadio.checked && targetRadio.id !== 'none') {
                                    val += '|' + elem.options[elem.selectedIndex].text;
                                }
                                changePropertiesValue(val, changePropertiesArr[0], changePropertiesArr[1]);
                            } else {
                                const changePropertiesArr = parentElem.id.split('-');
                                // changePropertiesArr 의 길이가 어떤 의미인지 알 수 없어서 무식하게 조건만 추가했음. (2021-02 hcjung)
                                //  - 이런 식이면 계속 if 문만 늘어나게 코딩할 수 밖에 없음. 최악의 방향으로... ㅠㅠ
                                //  - DOM id를 이용한 방법은 리펙토링에서 빼는 것이 좋을 것 같다.
                                if (changePropertiesArr.length > 2) {
                                    if (changePropertiesArr[0] !== ATTRIBUTE_TABLE_COLUMN && parentElem.getAttribute('data-field-type') === ATTRIBUTE_TABLE_COLUMN) {
                                        changePropertiesValue(elem.value, parentElem.getAttribute('data-field-type'), changePropertiesArr[0], parentElem.getAttribute('data-field-index'), changePropertiesArr[1]);
                                    } else {
                                        changePropertiesValue(elem.value, changePropertiesArr[0], changePropertiesArr[1], changePropertiesArr[2]);
                                    }
                                } else {
                                    if (parentElem.getAttribute('data-field-type')) {
                                        changePropertiesValue(elem.value, parentElem.getAttribute('data-field-type'), changePropertiesArr[0], parentElem.getAttribute('data-field-index'), changePropertiesArr[1]);
                                    } else {
                                        changePropertiesValue(elem.value, changePropertiesArr[0], changePropertiesArr[1]);
                                    }
                                }
                            }
                        }

                    }, false);
                }
            }
        }
    }

    /**
     * 우측 properties panel 세부 속성 - 상세 속성 출력
     */
    function drawProperties(elem, key, group, property) {
        elem.setAttribute('id', group + '-' + property.id + (typeof property.level !== 'undefined' ? '-' + property.level : ''));
        // tooltip
        const tooltipTemplate = (typeof property.help === 'undefined') ? `` : `<div class="help-tooltip">
            <span class="help-tooltip-icon"></span>
            <div class="tooltip-contents">
                <span>${i18n.msg(property.help)}</span>
            </div>
        </div>`;
        let fieldTemplate = ``;
        switch (property.type) {
        case 'inputbox':
            fieldTemplate =
                    `<label class="property-field-name">${i18n.msg('form.attribute.' + property.id)}</label>${tooltipTemplate}` +
                    `<input type="text" class="property-value" value="${aliceJs.filterXSS(property.value)}" maxlength="100"/>`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        case 'checkbox-boolean': // 라벨 클릭시에 체크박스가 동작한다.
            fieldTemplate =
                    `<label class="property-field-name checkbox" for="checkbox-${key}-${property.id}" tabindex="0">` +
                        `<input type="checkbox" class="property-value" id="checkbox-${key}-${property.id}" name="${property.id}" ${property.value ? 'checked' : ''}>` +
                        `<span></span>` +
                        `<span class="label">${i18n.msg('form.attribute.' + property.id)}</span>` +
                    `</label>${tooltipTemplate}`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        case 'button-option-text':
        case 'button-option-icon':
            let optionType = property.type.split('-')[2];

            const fieldOptions = property.option.map(function (opt) {
                return `<button type="button" id="${opt.id}" class="btn-field${property.value === opt.id ? ' active' : ''}">${optionType === 'text' ? opt.name : ''}` +
                               `<span class="icon icon-${group}-${property.id}-${opt.id}"></span>` +
                           `</button>`;
            }).join('');

            fieldTemplate =
                    `<label class="property-field-name">${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>` +
                    `<div class="btn-group-toggle property-field-${optionType}">${fieldOptions}</div>`;
            elem.insertAdjacentHTML('beforeend', fieldTemplate);

            // 버튼 이벤트 핸들러 추가
            const buttonElemList = elem.querySelector('.property-field-' + optionType).children;
            for (let i = 0, len = buttonElemList.length; i < len; i++) {
                buttonElemList[i].addEventListener('click', toggleOptionButtonClickHandler, false);
            }
            break;
        case 'customcode':
            const fieldCustomCodeOptions = customCodeList.map(function (code) {
                return `<option value='${code.customCodeId}' ${property.value === code.customCodeId ? 'selected=\'true\'' : ''}>${aliceJs.filterXSS(code.customCodeName)}</option>`;
            }).join('');

            fieldTemplate =
                    `<label class='property-field-name'>${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>` +
                    `<select class='property-value' id='${property.id}'>${fieldCustomCodeOptions}</select>`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            // 첫번째 커스텀 코드를 저장
            if (property.value === '' && customCodeList.length > 0) {
                changePropertiesValue(customCodeList[0].customCodeId, group, property.id);
            }
            break;
        case 'labeling':
            // 테이블 Header 추가
            let tableHeaderOptions = `<th></th>`;
            tableHeaderOptions += property.option.map(function(opt) {
                return `<th data-role="${opt.id}" data-default="${opt.value}" data-validate="${opt.validate}">${i18n.msg('form.attribute.option.' + opt.id)}</th>`;
            }).join('');

            // 테이블 Row 추가
            let tableRowOptions = property.value.label.map(function(opt, index) { // {key: value}
                let checkBoxTemplate = `<td>` +
                            `<label class="checkbox" for="checkbox-label-${index + 1}" tabindex="0">` +
                                `<input type="checkbox" id="checkbox-label-${index + 1}" value="${index + 1}" />` +
                                `<span></span>` +
                            `</label>` +
                        `</td>`;
                let labelKey = Object.keys(opt)[0];
                return `<tr>` +
                            `${checkBoxTemplate}` +
                            `<td><input type="text" id="key" class="property-value" value="${aliceJs.filterXSS(labelKey)}" data-validate="${property.option[0].validate}"/></td>` +
                            `<td><input type="text" id="value" class="property-value" value="${aliceJs.filterXSS(opt[labelKey])}" data-validate="${property.option[1].validate}"/></td>` +
                        `</tr>`;
            }).join('');

            fieldTemplate =
                    `<label class="property-field-name">${i18n.msg('form.attribute.' + property.id)}</label>${tooltipTemplate}` +
                    `<button type="button" class="ghost-line btn-option float-right" id="label-option-plus"><span class="icon icon-plus"></span></button>` +
                    `<button type="button" class="ghost-line btn-option float-right mr-1" id="label-option-minus"><span class="icon icon-minus"></span></button>` +
                    `<table class="property-field-table" id="table-label" date-validate="unique">` +
                        `<colgroup>` +
                            `<col width="20%">` +
                            `<col width="40%">` +
                            `<col width="40%">` +
                        `</colgroup>` +
                        `<tbody>` +
                            `<tr>${tableHeaderOptions}</tr>` +
                            `${tableRowOptions}` +
                        `</tbody>` +
                    `</table>` +
                    `<label class="error-msg"></label>`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        case 'image':
            fieldTemplate =
                    `<label class="property-field-name">${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>` +
                    `<div class="property-field-image">` +
                        `<input type="text" class="input-image property-value" value="${aliceJs.filterXSS(property.value)}" id="path-${key}" maxlength="100"/>` +
                        `<button type="button" class="ghost-line" id="imageUploadPop"><span class="icon icon-search"></span></button>` +
                    `</div>`;
            elem.insertAdjacentHTML('beforeend', fieldTemplate);

            elem.querySelector('#imageUploadPop').addEventListener('click', function(e) {
                aliceJs.thumbnail({
                    title: i18n.msg('image.label.popupTitle'),
                    targetId: 'path-' + key,
                    type: 'image',
                    isThumbnailInfo: true,
                    isFilePrefix: true,
                    thumbnailDoubleClickUse: true,
                    selectedPath: elem.querySelector('#path-' + key).value
                });
            });
            break;
        case 'rgb':
        case 'rgba':
            fieldTemplate =
                    `<div class="color-picker">` +
                        `<label class="property-field-name">${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>` +
                        `<div class="color-input">` +
                            `<div class="selected-color-box">` +
                                `<span class="selected-color" style="background-color: ${property.value};"></span>` +
                            `</div>` +
                            `<input type="text" class="property-value color" id="${group}-${property.id}-value"  value="${property.value}" readonly>` +
                        `</div>` +
                        `<div id="${group + '-' + property.id}-colorPaletteLayer">` +
                            `<div id="${group + '-' + property.id}-colorPalette" class="color-palette"></div>` +
                            `<div id="${group + '-' + property.id}-colorPalette-opacity" class="color-palette-opacity"></div>` +
                        `</div>` +
                    `</div>`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);

            // color palette 초기화
            let option = {
                isOpacity: (property.type === 'rgba'), // 불투명도 사용여부
                data: {
                    isSelected: true, // 기존 색상 선택 여부
                    selectedClass: 'selected', // 기존 값 색상에 css 적용 (테두리)
                    value: property.value // 기존 값
                }
            };
            colorPalette.initColorPalette(elem.querySelector('#' + group + '-' + property.id + '-colorPaletteLayer'),
                elem.querySelector('.selected-color'),
                elem.querySelector('#' + group + '-' + property.id + '-value'),
                option);
            break;
        case 'radio-datetime':
            elem.classList.add('vertical');

            let optionDefaultArr;
            const defaultFormatArr = property.value !== '' ? property.value.split('|') : ''; // none, now, date|-3, time|2, datetime|7|0 등
            const fieldDatetimeOptions = property.option.map(function (opt) {
                optionDefaultArr = ['', '', ''];
                if (defaultFormatArr[0] === opt.id) {
                    optionDefaultArr = defaultFormatArr;
                }
                let labelName = i18n.msg('form.attribute.option.' + opt.id).split('{0}');
                let optionTemplate= ``;
                if (opt.id === 'date' || opt.id === 'datetime' || 'time') {
                    optionTemplate += `${opt.id === 'date' || opt.id === 'time' ? '<input type=\'text\' class=\'property-value\' data-validate=\'' + opt.validate + '\' id=\'' + opt.id + '\' value=\'' + optionDefaultArr[1] + '\' maxlength=\'4\'/><span>' + labelName[1] + '</span>' : ''}` +
                            `${opt.id === 'datetime' ? '<input type=\'text\' class=\'property-value\' data-validate=\'' + opt.validate + '\' id=\'' + opt.id + '-day\' value=\'' + optionDefaultArr[1] + '\' /><span id=\'' + opt.id + '-day\'>' + labelName[1] + '</span>' + '<input type=\'text\' class=\'property-value\' data-validate=\'' + opt.validate + '\' id=\'' + opt.id + '-hour\' value=\'' + optionDefaultArr[2] + '\' /><span id=\'' + opt.id + '-hour\'>' + labelName[2] + '</span>' : ''}`;

                }
                return `<div class='vertical-group radio-datetime'>` +
                        `<label class="radio" for="${opt.id}">` +
                            `<input type="radio" id="${opt.id}" name="${group}-${property.id}" value="${opt.id}" ${defaultFormatArr[0] === opt.id ? 'checked=\'true\'' : ''} /><span></span>` +
                            `${opt.id === 'now' || opt.id === 'none' ? '<span class="label">' + labelName[0] + '</span>' : ''}`+
                        `</label>` +
                        `${opt.id === 'date' || opt.id === 'datetime' || opt.id === 'time' ? '<div>' + optionTemplate + '</div>' : ''}`+
                        `${opt.id === 'datepicker' || opt.id === 'timepicker' || opt.id === 'datetimepicker' ? '<input type=\'text\' class=\'' + opt.id + '\' id=\'' + opt.id + '-' + key + '\' value=\'' + optionDefaultArr[1] + '\'/>' : ''}` +
                    `</div>`;
            }).join('');

            fieldTemplate = `<label class='property-field-name'>${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>${fieldDatetimeOptions}`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        case 'radio-custom':
            elem.classList.add('vertical');
            const fieldValueArr = property.value.split('|');
            const fieldRadioOptions = property.option.map(function (opt) {
                return `<div class="vertical-group radio-custom">` +
                    `<label class="radio" for="${opt.id}">` +
                        `<input type="radio" id="${opt.id}" name="${group}-${property.id}" value="${opt.id}" ${fieldValueArr[0] === opt.id ? 'checked=\'true\'' : ''} /><span></span>` +
                        `<span class="label" >${i18n.msg('form.attribute.option.' + opt.id)}</span>` +
                    `</label>` +
                    `${opt.id !== 'none' ? '<select>' + opt.items.map(function (item) {
                        return `<option value='${item.id}' ${item.id === fieldValueArr[1] ? 'selected=\'selected\'' : ''}>${item.name}</option>`;
                    }).join('') + '</select>' : ''}` +
                    `</div>`;
            }).join('');

            fieldTemplate = `<label class='property-field-name'>${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>${fieldRadioOptions}`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        case 'select':
            let fieldSelectOptions = property.option.map(function (opt) {
                return `<option value='${opt.id}' ${property.value === opt.id ? 'selected=\'true\'' : ''}>${opt.name}</option>`;
            }).join('');

            fieldTemplate =
                    `<label class='property-field-name'>${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>` +
                    `<select class='property-value'>${fieldSelectOptions}</select>`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        case 'slider':
            fieldTemplate =
                    `<label class="property-field-name">${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>` +
                    `<div class="property-field-range">` +
                        `<input type="range" class="property-value" id="${group + '-' + property.id}" min="1" max="12" value="${property.value}"/>` +
                        `<input type="text" id="${group + '-' + property.id}-value" value="${property.value}" readonly/>` +
                    `</div>`;
            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        case 'session':
            fieldTemplate = `<label class='property-field-name'>${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>`;
            const propValueArr = property.value.split('|');
            const fieldSessionOptions = property.option.map(function (opt) {
                return `<button type="button" name="${opt.id}" class="btn default-line ${propValueArr[0]  === opt.id ? 'active' : ''}">${opt.name}</button>`;
            }).join('');
            fieldTemplate += `<div class="property-field-toggle btn-group-toggle">${fieldSessionOptions}</div>`;

            // 직접 입력할 경우 input box
            fieldTemplate += `<input type='text' class='property-value ${property.type}' id='none' style='${propValueArr[0] === 'none' ? '' : 'display: none;'}' value='${propValueArr[0] === 'none' ? aliceJs.filterXSS(propValueArr[1]) : ''}' maxlength="100"/>`;
            // 자동 입력일 경우 select box
            const fieldSubOptions = property.option[1].items.map(function (opt) {
                return `<option value='${opt.id}' ${propValueArr[1] === opt.id ? 'selected=\'true\'' : ''}>${opt.name}</option>`;
            }).join('');
            fieldTemplate +=
                    `<select class='property-value ${property.type}' id='select' style='${propValueArr[0] === 'select' ? '' : 'display: none;'}'>${fieldSubOptions}</select>`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);

            // 버튼 이벤트 핸들러 추가
            const toggleButtons = elem.querySelector('.property-field-toggle').children;
            for (let i = 0, len = toggleButtons.length; i < len; i++) {
                toggleButtons[i].addEventListener('click', toggleSessionButtonClickHandler, false);
            }
            break;
        case 'datepicker':
        case 'timepicker':
        case 'datetimepicker':
            fieldTemplate =
                    `<label class='property-field-name'>${i18n.msg('form.attribute.' + property.id)}${tooltipTemplate}</label>` +
                    `<input type='text' class='${property.type} property-value' id='${property.id}-${key}' name='${group}-${property.id}' value='${property.value}'>`;

            elem.insertAdjacentHTML('beforeend', fieldTemplate);
            break;
        }
        // 단위 추가
        if (typeof property.unit !== 'undefined' && property.unit !== '') {
            elem.insertAdjacentHTML('beforeend', `<span class='property-field-unit'>${property.unit}</span>`);
        }
    }

    /**
     * 우측 properties panel 세부 속성 출력
     */
    function showComponentProperties() {
        if (previousComponentIds.toString() === selectedComponentIds.toString()) { return false; }
        hideComponentProperties();

        if (selectedComponentIds.length === 0) { return false; }
        previousComponentIds = selectedComponentIds.slice();

        // 하나만 선택되었고, 현재 선택된 컴포넌트가 editbox라면 form 속성을 출력한다.
        const selectedComponentElem = document.getElementById(selectedComponentIds[0]);
        if (selectedComponentElem === null) { return false; }
        if (selectedComponentIds.length === 1 && selectedComponentElem.getAttribute('data-type') === aliceForm.defaultType) {
            showFormProperties(selectedComponentIds[0]);
            return false;
        }
        let selectedComponentTypes = [];
        let isHideComponent = false;
        for (let i = 0, len = selectedComponentIds.length; i < len; i++) {
            let selectedElem = document.getElementById(selectedComponentIds[i]);
            if (selectedElem !== null) {
                selectedElem.classList.add('selected');
                const componentType = selectedElem.getAttribute('data-type');
                if (selectedComponentTypes.indexOf(componentType) === -1) {
                    selectedComponentTypes.push(componentType);
                }
                if (componentType === 'label' || componentType === 'image' || componentType === 'divider' || componentType === aliceForm.defaultType) {
                    isHideComponent = true;
                }
                if (len > 1) {
                    // 선택된 컴포넌트가 여러개면, 상단에 tooptip menu에 delete 항목만 표시
                    const menuItems = selectedElem.querySelectorAll('.menu-item');
                    for (let j = 0, menuLen = menuItems.length; j < menuLen; j++) {
                        if (!menuItems[j].classList.contains('delete')) {
                            menuItems[j].classList.add('hidden');
                            // 아코디언 컴포넌트일 경우 add 항목 활성화
                            if (len === 2 && componentType === 'accordion-start' && menuItems[j].classList.contains('add')) {
                                menuItems[j].classList.remove('hidden');
                            }
                        }
                    }

                    // 선택된 컴포넌트가 인접할 경우 border 없애기
                    if (selectedElem.previousSibling !== null &&
                        selectedComponentIds.indexOf(selectedElem.previousSibling.id) !== -1 &&
                        !selectedElem.previousSibling.classList.contains('adjoin')) {
                        selectedElem.previousSibling.classList.add('adjoin');
                    }
                }
            }
        }

        // 선택된 첫번째 컴포넌트의 속성을 출력한다.
        let compIdx = getComponentIndex(selectedComponentIds[0]);
        // 아코디언 컴포넌트일 경우 accordion 속성을 표시한다.
        let isAccordion = false;
        if (selectedComponentIds.length === 2 && selectedComponentTypes.indexOf('accordion-start') !== -1) {
            compIdx = getComponentIndex(selectedComponentIds[selectedComponentTypes.indexOf('accordion-start')]);
            isAccordion = true;
        }
        let componentData = editor.data.components[compIdx];
        let properties = initProperties(componentData);

        const componentTemplate = document.getElementById('component-template');
        const componentElem = componentTemplate.content.cloneNode(true);
        const componentTitleElem = document.getElementById('properties-name');

        // 1. 컴포넌트가 2개 이상이면 dataAttribute 속성은 보여주지 않는다.
        // 2. 컴포넌트가 2개 이상이면, label과 display의 column 속성만 보여진다.
        if (selectedComponentIds.length > 1 && !isAccordion) {
            componentTitleElem.innerHTML = i18n.msg('form.properties.common');
            // 3. 서로 다른 컴포넌트이고, Divider, Image, Label가 포함되어 있다면 아무 속성도 출력하지 않는다.
            if ((selectedComponentTypes.length > 1 && isHideComponent) || (selectedComponentTypes.length === 1 && selectedComponentTypes[0] === aliceForm.defaultType)) {
                const emptyPanel = componentElem.querySelector('.property-empty');
                if (!emptyPanel.classList.contains('on')) {
                    emptyPanel.classList.add('on');
                    propertiesPanel.appendChild(componentElem);
                }
                return false;
            }
            if (properties.hasOwnProperty('dataAttribute')) { delete properties.dataAttribute; }
            // 4. label, image, divider 는 display의 column 속성만 보여준다.
            if (properties.hasOwnProperty('display') && !isHideComponent) {
                properties.display = properties.display.filter(function (c) {
                    return c.id.includes('column');
                });
            }
            if (properties.hasOwnProperty('option')) { delete properties.option;}
            if (properties.hasOwnProperty('validate')) { delete properties.validate; }
        } else {
            // 5. 컴포넌트가 2개 이상이면 제목은 출력되지 않는다.
            componentTitleElem.innerHTML = i18n.msg('form.component.' + componentData.type);
        }
        // 세부 속성을 출력한다.
        Object.keys(properties).forEach(function(group) {
            let buttonGroupExist = false;
            let buttonGroupElem = null;
            let groupElem = componentElem.querySelector('#' + group);
            if (groupElem !== null) {
                // 표시하고자 하는 property group을 보여준다.
                if (!groupElem.classList.contains('on')) { groupElem.classList.add('on'); }

                // 옵션이 존재할 경우 이벤트 핸들러 등록
                if (group === 'option') {
                    groupElem.querySelector('#option-minus').addEventListener('click', removeOptionHandler, false);
                    groupElem.querySelector('#option-plus').addEventListener('click', addOptionHandler, false);
                }
                // 열 옵션이 존재할 경우 이벤트 핸들러 등록
                if (group === ATTRIBUTE_TABLE_COLUMN) {
                    groupElem.querySelector('#drTableColumns-delete').addEventListener('click', removeDRTableColumnsHandler, false);
                    groupElem.querySelector('#drTableColumns-add').addEventListener('click', addDRTableColumnsHandler, false);
                }
                // 세부 속성 추가
                if (Array.isArray(properties[group])) {
                    Object.keys(properties[group]).forEach(function(field, idx) {
                        const fieldProp = properties[group][field];
                        // dynamic row table 일 경우
                        if (group === ATTRIBUTE_TABLE_COLUMN) {
                            groupElem = componentElem.querySelector('#' + group);
                            const fieldGroupTemplate =
                                `<div class="property-field-block" id="column-${idx}">` +
                                   `<div class="property-field-block-header">` +
                                       `<label class="checkbox" for="chk-${idx}" tabindex="0">` +
                                           `<input type="checkbox" id="chk-${idx}" value="${idx}"/>` +
                                           `<span></span>` +
                                           `<span class="label">${i18n.msg('form.attribute.drTableColumns', (Number(idx) + 1))}</span>` +
                                       `</label>` +
                                   `</div>` +
                                /*                                   `<div class="property-field-block-content"></div>` +*/
                               `</div>`;
                            groupElem.insertAdjacentHTML('beforeend', fieldGroupTemplate);

                            groupElem = groupElem.querySelector('#column-' + idx + ' .property-field-block-content');
                        }

                        if (group !== ATTRIBUTE_TABLE_COLUMN) {
                            if (typeof fieldProp.id !== 'undefined' && fieldProp.type !== 'hidden') {
                                const fieldGroupElem = document.createElement('div');
                                fieldGroupElem.classList.add('property-field');

                                if (fieldProp.type === 'button-group') { // 버튼이 존재할 경우 한 줄에 표시하기 위해 div로 감싼다.
                                    if (!buttonGroupExist) {
                                        buttonGroupElem = document.createElement('div');
                                        buttonGroupElem.classList.add('btn-group', 'property-field-button');
                                        fieldGroupElem.appendChild(buttonGroupElem);
                                        groupElem.appendChild(fieldGroupElem);

                                        buttonGroupExist = true;
                                    }
                                    if (typeof fieldProp.option !== 'undefined') { //align
                                        const fieldButtonOptions = fieldProp.option.map(function (opt) {
                                            return `<button type='button' id='${opt.id}' class='btn-field${fieldProp.value === opt.id ? ' active' : ''}'>` +
                                                `<span class="icon icon-align-${opt.id}"></span> ` +
                                                `</button>`;
                                        }).join('');
                                        buttonGroupElem.insertAdjacentHTML('beforeend', `<div class="btn-group-toggle" id='${fieldProp.id}'>${fieldButtonOptions}</div>`);

                                        const buttonElemList = buttonGroupElem.querySelector('#' + fieldProp.id).children;
                                        for (let i = 0, len = buttonElemList.length; i < len; i++) {
                                            buttonElemList[i].addEventListener('click', toggleButtonClickHandler, false);
                                        }
                                    } else { //bold, italic, underline
                                        const buttonTemplate = `<button type='button' id='${fieldProp.id}' class='btn-field${fieldProp.value === 'Y' ? ' active' : ''}' data-value='${fieldProp.value}'>` +
                                            `<span class="icon icon-${fieldProp.id}"></span>` +
                                            `</button>`;
                                        buttonGroupElem.insertAdjacentHTML('beforeend', buttonTemplate);
                                        buttonGroupElem.querySelector('#' + fieldProp.id).addEventListener('click', toggleButtonClickHandler, false);
                                    }
                                } else {
                                    groupElem.appendChild(fieldGroupElem);
                                    // 세부 속성 추가
                                    drawProperties(fieldGroupElem, componentData.componentId, group, fieldProp);

                                    if ( fieldProp.type === 'radio-custom') {
                                        //custom-code 초기화
                                        const customCodeDataSelect = fieldGroupElem.querySelector('input[id=code]').parentNode.parentNode.querySelector('select');
                                        changeCustomCodeHandler(customCodeDataSelect, componentData[group]);
                                    }
                                }

                                // 유효성 검증 추가
                                if (typeof fieldProp.validate !== 'undefined' && fieldProp.validate !== '') {
                                    const fieldValueElems = fieldGroupElem.querySelectorAll('.property-value');
                                    for (let i = 0, len = fieldValueElems.length; i < len; i++) {
                                        fieldValueElems[i].parentNode.insertAdjacentHTML('beforeend', `<label class="error-msg"></label>`);
                                        validateCheck('keyup', fieldValueElems[i], fieldProp.validate);
                                    }
                                }
                                if (typeof fieldProp.option !== 'undefined') {
                                    const fieldValueElems = fieldGroupElem.querySelectorAll('.property-value');
                                    for (let i = 0, len = fieldValueElems.length; i < len; i++) {
                                        const fieldValueElem = fieldValueElems[i];
                                        if (fieldValueElem !== null && fieldValueElem.getAttribute('data-validate') !== null) {
                                            if (fieldValueElem.parentNode.tagName !== 'TD') { // table 구조가 아닐 경우
                                                fieldValueElem.parentNode.insertAdjacentHTML('afterend', `<label class="error-msg"></label>`);
                                            }
                                            validateCheck('keyup', fieldValueElem, fieldValueElem.getAttribute('data-validate'));
                                        }
                                    }
                                }
                            } else { // type === table
                                const tableElem = groupElem.querySelector('table');
                                if (tableElem !== null) {
                                    // 테이블 Header 추가
                                    const tableHeaderOptions = fieldProp.items.map(function(opt, index) {
                                        return `<th data-default="${opt.value}">${index === 0 ? '': i18n.msg('form.attribute.option.' + opt.id)}</th>`;
                                    }).join('');
                                    let fieldTableTemplate = `<tr>${tableHeaderOptions}</tr>`;

                                    // 테이블 Row 추가
                                    const tableRowOptions = componentData.option.map(function(opt) {
                                        return `<tr>${fieldProp.items.map(function(item, index) {
                                            return `<td id="${item.id}">` +
                                                `${index === 0 ?
                                                    `<label class="checkbox" for="checkbox-${opt[item.id]}" tabindex="0">` +
                                                    `<input type="checkbox" id="checkbox-${opt[item.id]}" value="${opt[item.id]}" />` +
                                                    `<span></span>` +
                                                    `</label>` :
                                                    `<input type="text" value="${aliceJs.filterXSS(opt[item.id])}" maxlength="100"/>`}` +
                                                `</td>`;
                                        }).join('')}</tr>`;
                                    }).join('');

                                    fieldTableTemplate += tableRowOptions;

                                    tableElem.insertAdjacentHTML('beforeend', fieldTableTemplate);
                                }
                            }
                        }
                    });
                }
            }
        });
        propertiesPanel.appendChild(componentElem);

        // 라벨링 이벤트 핸들러 등록
        if (propertiesPanel.querySelector('#label-option-minus') !== null) {
            propertiesPanel.querySelector('#label-option-minus').addEventListener('click', removeLabelOptionHandler, false);
            propertiesPanel.querySelector('#label-option-plus').addEventListener('click', addLabelOptionHandler, false);
        }

        const propertyGroupList = propertiesPanel.querySelectorAll('.property-group.on');
        const propertyLastGroup = propertyGroupList[propertyGroupList.length - 1];
        if (propertyLastGroup && !propertyLastGroup.classList.contains('last')) {
            propertyLastGroup.classList.add('last');
        }

        // date picker 초기화
        const datepickerElems = propertiesPanel.querySelectorAll('.datepicker');
        let i, len;
        for (i = 0, len = datepickerElems.length; i < len; i++) {
            dateTimePicker.initDatePicker(datepickerElems[i].id, setDateFormat);
        }
        const timepickerElems = propertiesPanel.querySelectorAll('.timepicker');
        for (i = 0, len = timepickerElems.length; i < len; i++) {
            dateTimePicker.initTimePicker(timepickerElems[i].id, setDateFormat);
        }
        const datetimepickerElems = propertiesPanel.querySelectorAll('.datetimepicker');
        for (i = 0, len = datetimepickerElems.length; i < len; i++) {
            dateTimePicker.initDateTimePicker(datetimepickerElems[i].id, setDateFormat);
        }
        // 이벤트 추가
        addChangePropertiesEvent(propertiesPanel);
        // focustout 이벤트 추가
        const imageInputElem = propertiesPanel.querySelector('.input-image');
        if (imageInputElem) {
            imageInputElem.addEventListener('focusout', function(e) {
                const elem = e.target;
                const parentElem = elem.parentNode.parentNode;
                const changePropertiesArr = parentElem.id.split('-');
                changePropertiesValue(elem.value, changePropertiesArr[0], changePropertiesArr[1]);
            }, false);
        }
        
        // for designed select
        // 속성창을 새로 그린 후 designed select 초기화
        aliceJs.initDesignedSelectTag();
    }

    /**
     * 우측 properties panel 삭제한다.
     */
    function hideComponentProperties() {
        propertiesPanel.innerHTML = '';
        if (previousComponentIds.length > 0) {
            for (let i = 0, len = previousComponentIds.length; i < len; i++) {
                let previousSelectedElem = document.getElementById(previousComponentIds[i]);
                if (len > 1) {
                    // 기존 상단 tooltip menu 에 hidden 항목 삭제
                    const menuItems = previousSelectedElem.querySelectorAll('.menu-item');
                    for (let j = 0, menuLen = menuItems.length; j < menuLen; j++) {
                        if (menuItems[j].classList.contains('hidden')) {
                            menuItems[j].classList.remove('hidden');
                        }
                    }
                    if (previousSelectedElem !== null &&
                        previousComponentIds.indexOf(previousSelectedElem.id) !== -1 &&
                        previousSelectedElem.classList.contains('adjoin')) {
                        previousSelectedElem.classList.remove('adjoin');
                    }
                }
                // 기존 선택된 컴포넌트 css 삭제
                if (previousSelectedElem !== null && previousSelectedElem.classList.contains('selected')) {
                    previousSelectedElem.classList.remove('selected');
                }
            }
            previousComponentIds.length = 0;
        }
        // DR Table 컴포넌트일 경우 field css 삭제
        let drTables = formPanel.querySelectorAll('.dr-table');
        if (drTables.length > 0) {
            for (let i = 0, len = drTables.length; i < len; i++) {
                const fields = drTables[i].querySelectorAll('th');
                for (let j = 0, fieldLen = fields.length; j < fieldLen; j++) {
                    if (fields[j].classList.contains('on')) {
                        fields[j].classList.remove('on');
                    }
                }
            }
        }
    }

    /**
     * DR Table Component 전용.
     * DR Table 헤더를 클릭하는 경우 속성창에 해당 컬럼의 상세 속성을 출력.
     * 상세 속성은
     *   1) 컬럼 타입, 너비, 테이블 컬럼 명등과 같은 공통 속성과
     *   2) 컬럼 타입에 따른 속성으로 나뉜다.
     *
     *  ** 현재는 input box 컬럼 타입만 제공.
     *
     * @param {String} componentId 컴포넌트 Id
     * @param {String} columnIndex DR Table 컬럼 인덱스
     * @param {String} columnType DR Table 컬럼 타입
     */
    function showDRTableColumnProperties(componentId, columnIndex, columnType) {
        hideComponentProperties();

        selectedComponentIds.length = 0;
        previousComponentIds.length = 0;
        selectedComponentIds.push(componentId);

        const selectedComponent = document.getElementById(componentId);
        if (selectedComponent === null) { return; }

        const drTableHeader = selectedComponent.querySelector('.dr-table').rows[0];
        // 선택된 컬럼에 테두리 표시. 다른 컬럼에서 테두리 삭제
        for (let i = 0, len = drTableHeader.cells.length; i < len; i++) {
            if (i === Number(columnIndex)) {
                drTableHeader.cells[i].classList.add('on');
            } else {
                drTableHeader.cells[i].classList.remove('on');
            }
        }

        // 컴포넌트 데이터 가져오기
        let componentData = editor.data.components[getComponentIndex(componentId)];
        let properties = initProperties(componentData);

        // 컴포넌트 템플릿을 이용해서 DOM 생성
        const componentTemplate = document.getElementById('component-template');
        const componentElem = componentTemplate.content.cloneNode(true);

        // 컴포넌트 이름 영역 출력.
        // 컬럼 선택 시 컴포넌트 이름 부분에는 어떤 컬럼을 선택했는지 표시한다.
        // 1) 컴포넌트 이름
        const componentTitleElem = document.getElementById('properties-name');
        componentTitleElem.innerHTML = '';
        const componentName = document.createElement('span');
        componentName.textContent = i18n.msg('form.component.' + componentData.type);
        componentTitleElem.appendChild(componentName);
        // 2) 구분자
        const separator = document.createElement('span');
        separator.className = 'ml-4 mr-4';
        separator.textContent = '>';
        componentTitleElem.appendChild(separator);
        // 3) 열 표시
        const typeName = document.createElement('span');
        typeName.textContent = i18n.msg('form.attribute.drTableColumns', (Number(columnIndex) + 1));
        componentTitleElem.appendChild(typeName);

        // 컴포넌트 이름 선택시, 컴포넌트 속성 창으로 다시 이동
        // "DR Table > 2 열" 과 같이 표시된 부분에서 "DR Table" 부분
        componentName.addEventListener('click', function(e) {
            e.stopPropagation();

            selectedComponentIds.length = 0;
            selectedComponentIds.push(componentId);
            showComponentProperties();
        });

        let columnData = componentData[ATTRIBUTE_TABLE_COLUMN][columnIndex]; // 현재 컬럼의 속성 데이터

        // 컬럼 공통 속성
        let columnCommonProperties = {};
        columnCommonProperties[ATTRIBUTE_TABLE_COLUMN] = Object.assign({}, properties[ATTRIBUTE_TABLE_COLUMN])[columnIndex]; // 현재 컬럼의 공통 속성

        Object.keys(columnData).forEach(function(propertyGroupId) {
            Object.keys(columnCommonProperties[ATTRIBUTE_TABLE_COLUMN]).forEach(function(initProperty) {
                if (propertyGroupId === columnCommonProperties[ATTRIBUTE_TABLE_COLUMN][initProperty].id) {
                    columnCommonProperties[ATTRIBUTE_TABLE_COLUMN][initProperty].value = columnData[propertyGroupId];
                }
            });
        });

        // 컬럼 타입별로 세부 속성 값 재할당
        let typeProperties = Object.assign({}, properties[columnType]);
        Object.keys(columnData).forEach(function(propertyGroupId) {
            if (typeProperties.hasOwnProperty(propertyGroupId)) {
                Object.keys(columnData[propertyGroupId]).forEach(function(propertyId) {
                    Object.keys(typeProperties[propertyGroupId]).forEach(function(initProperty) {
                        if (propertyId === typeProperties[propertyGroupId][initProperty].id) {
                            typeProperties[propertyGroupId][initProperty].value = columnData[propertyGroupId][propertyId];
                        }
                    });
                });
            }
        });

        typeProperties = Object.assign(columnCommonProperties, typeProperties);

        // 공통/타입별 세부 속성 출력
        Object.keys(typeProperties).forEach(function(group) {
            let buttonGroupExist = false;
            let buttonGroupElem = null;
            let groupElem = componentElem.querySelector('#' + group);
            if (groupElem !== null) {
                // 표시하고자 하는 property group을 보여준다.
                if (!groupElem.classList.contains('on')) { groupElem.classList.add('on'); }
                if (Array.isArray(typeProperties[group])) {
                    Object.keys(typeProperties[group]).forEach(function(field) {
                        const fieldProp = typeProperties[group][field];
                        const fieldGroupElem = document.createElement('div');
                        fieldGroupElem.className = 'property-field';
                        fieldGroupElem.setAttribute('data-field-index', columnIndex);
                        fieldGroupElem.setAttribute('data-field-type', ATTRIBUTE_TABLE_COLUMN);
                        fieldProp.level = columnIndex;
                        // 버튼이 존재할 경우 한 줄에 표시하기 위해 div로 감싼다.
                        if (fieldProp.type === 'button-group') {
                            if (!buttonGroupExist) {
                                buttonGroupElem = document.createElement('div');
                                buttonGroupElem.classList.add('btn-group', 'property-field-button');
                                fieldGroupElem.appendChild(buttonGroupElem);
                                groupElem.appendChild(fieldGroupElem);

                                buttonGroupExist = true;
                            }
                            if (typeof fieldProp.option !== 'undefined') { //align
                                const fieldButtonOptions = fieldProp.option.map(function (opt) {
                                    return `<button type='button' id='${opt.id}' class='btn-field${fieldProp.value === opt.id ? ' active' : ''}'>` +
                                               `<span class="icon icon-align-${opt.id}"></span> ` +
                                           `</button>`;
                                }).join('');
                                buttonGroupElem.insertAdjacentHTML('beforeend', `<div class="btn-group-toggle" id='${fieldProp.id}'>${fieldButtonOptions}</div>`);

                                const buttonElemList = buttonGroupElem.querySelector('#' + fieldProp.id).children;
                                for (let i = 0, len = buttonElemList.length; i < len; i++) {
                                    buttonElemList[i].addEventListener('click', toggleButtonClickHandler, false);
                                }
                            } else { //bold, italic, underline
                                const buttonTemplate = `<button type='button' id='${fieldProp.id}' class='btn-field${fieldProp.value === 'Y' ? ' active' : ''}' data-value='${fieldProp.value}'>` +
                                        `<span class="icon icon-${fieldProp.id}"></span>` +
                                    `</button>`;
                                buttonGroupElem.insertAdjacentHTML('beforeend', buttonTemplate);
                                buttonGroupElem.querySelector('#' + fieldProp.id).addEventListener('click', toggleButtonClickHandler, false);
                            }
                        } else {
                            groupElem.appendChild(fieldGroupElem);
                            // 세부 속성 추가
                            drawProperties(fieldGroupElem, componentData.componentId, group, fieldProp);
                        }

                        // 유효성 검증 추가
                        if (typeof fieldProp.validate !== 'undefined' && fieldProp.validate !== '') {
                            const fieldValueElems = fieldGroupElem.querySelectorAll('.property-value');
                            for (let i = 0, len = fieldValueElems.length; i < len; i++) {
                                fieldValueElems[i].parentNode.insertAdjacentHTML('beforeend', `<label class="error-msg"></label>`);
                                validateCheck('keyup', fieldValueElems[i], fieldProp.validate);
                            }
                        }
                        if (typeof fieldProp.option !== 'undefined') {
                            const fieldValueElems = fieldGroupElem.querySelectorAll('.property-value');
                            for (let i = 0, len = fieldValueElems.length; i < len; i++) {
                                const fieldValueElem = fieldValueElems[i];
                                if (fieldValueElem !== null && fieldValueElem.getAttribute('data-validate') !== null) {
                                    fieldValueElem.parentNode.insertAdjacentHTML('afterend', `<label class="error-msg"></label>`);
                                    validateCheck('keyup', fieldValueElem, fieldValueElem.getAttribute('data-validate'));
                                }
                            }
                        }
                    });
                }
            }
        });
        propertiesPanel.appendChild(componentElem);

        const propertyGroupList = propertiesPanel.querySelectorAll('.property-group.on');
        const propertyLastGroup = propertyGroupList[propertyGroupList.length - 1];
        if (propertyLastGroup && !propertyLastGroup.classList.contains('last')) {
            propertyLastGroup.classList.add('last');
        }

        // date picker 초기화
        const datepickerElems = propertiesPanel.querySelectorAll('.datepicker');
        let i, len;
        for (i = 0, len = datepickerElems.length; i < len; i++) {
            dateTimePicker.initDatePicker(datepickerElems[i].id, setDateFormat);
        }
        const timepickerElems = propertiesPanel.querySelectorAll('.timepicker');
        for (i = 0, len = timepickerElems.length; i < len; i++) {
            dateTimePicker.initTimePicker(timepickerElems[i].id, setDateFormat);
        }
        const datetimepickerElems = propertiesPanel.querySelectorAll('.datetimepicker');
        for (i = 0, len = datetimepickerElems.length; i < len; i++) {
            dateTimePicker.initDateTimePicker(datetimepickerElems[i].id, setDateFormat);
        }

        // 이벤트 추가
        addChangePropertiesEvent(propertiesPanel);

        // for designed select
        // 속성창을 새로 그린 후 designed select 초기화
        aliceJs.initDesignedSelectTag();

        // 컬럼 추가/삭제 버튼 숨기기
        const buttonDiv = propertiesPanel.querySelectorAll('.property-group-drTableColumns');
        for (i = 0, len = buttonDiv.length; i < len; i ++) {
            buttonDiv[i].style.display = 'none';
        }
    }
    /**
     * 우측 properties panel에 폼 세부 속성 출력한다.
     *
     * @param {String} elemId 선택한 element Id
     */
    function showFormProperties(elemId) {
        hideComponentProperties();
        if (typeof elemId !== 'undefined' && elemId !== '') {
            if (!document.getElementById(elemId).classList.contains('selected')) {
                document.getElementById(elemId).classList.add('selected'); //현재 선택된 컴포넌트 css 추가
                previousComponentIds.push(elemId);
            }
        } else {
            selectedComponentIds.length = 0;
            previousComponentIds.length = 0;
        }
        let formProperties = editor.data;
        // 제목 변경
        const formTitleElem = document.getElementById('properties-name');
        formTitleElem.innerHTML = i18n.msg('form.properties.title');
        //폼 속성 출력
        const formTemplate = document.getElementById('form-template');
        const formElem = formTemplate.content.cloneNode(true);
        const formNodes = formElem.querySelectorAll('.property-field');
        formNodes.forEach(function(node) {
            Object.keys(formProperties).some(function(prop) {
                if (prop === node.id) {
                    const propElem = node.querySelector('.property-value');
                    switch (node.id) {
                    case 'name':
                        propElem.setAttribute('value', formProperties[prop]);
                        // 필수값 체크
                        validateCheck('focusout', propElem, 'required');
                        propElem.addEventListener('keyup', function(e) {
                            const elem = e.target;
                            let formOriginAttr = JSON.parse(JSON.stringify(editor.data));
                            editor.data.name = elem.value;
                            history.saveHistory([{0: formOriginAttr, 1: JSON.parse(JSON.stringify(editor.data))}]);
                        });
                        break;
                    case 'formId':
                        propElem.setAttribute('value', formProperties[prop]);
                        break;
                    case 'desc':
                        propElem.value = formProperties[prop];
                        propElem.addEventListener('change', function(e) {
                            let formOriginAttr = JSON.parse(JSON.stringify(editor.data));
                            editor.data.desc = this.value;
                            history.saveHistory([{0: formOriginAttr, 1: JSON.parse(JSON.stringify(editor.data))}]);
                        }, false);
                        propElem.className = 'textarea-scroll-wrapper';
                        break;
                    case 'status':
                        for (let i = 0, len = propElem.options.length; i < len; i++) {
                            if (formProperties[prop] === propElem.options[i].value) {
                                propElem.options[i].setAttribute('selected', 'selected');
                                break;
                            }
                        }
                        propElem.addEventListener('change', function(e) {
                            let formOriginAttr = JSON.parse(JSON.stringify(editor.data));
                            editor.data.status = this.value;
                            history.saveHistory([{0: formOriginAttr, 1: JSON.parse(JSON.stringify(editor.data))}]);
                        }, false);
                        break;
                    }
                }
            });
        });
        propertiesPanel.appendChild(formElem);

        // textarea 에 스크롤 적용
        OverlayScrollbars(propertiesPanel.querySelectorAll('textarea'), {
            className: 'inner-scrollbar',
            resize: 'vertical',
            sizeAutoCapable: true,
            textarea: {
                dynHeight: false,
                dynWidth: false,
                inheritedAttrs: 'class'
            }
        });

        // for designed select
        // 폼 속성창을 그린 후 designed select 초기화.
        aliceJs.initDesignedSelectTag();
    }

    /**
     * 조회된 데이터 draw.
     */
    function drawForm() {
        //컴포넌트 재정렬
        if (editor.data.components.length > 0) {
            if (editor.data.components.length > 2) {
                editor.data.components.sort(function (a, b) {
                    return a.display.order < b.display.order ? -1 : a.display.order > b.display.order ? 1 : 0;  
                });
            }
            //데이터로 전달받은 컴포넌트 속성과 기본 속성을 merge한 후 컴포넌트 draw
            for (let i = 0, len = editor.data.components.length; i < len; i ++) {
                let componentProp = editor.data.components[i];
                let componentObj = component.draw(componentProp.type, componentProp);
                setComponentData(componentObj.property);
            }
        }

        //모든 컴포넌트를 그린 후 마지막에 editbox 추가
        let editboxComponentObj = component.draw(aliceForm.defaultType);
        setComponentData(editboxComponentObj.property);
        savedData = JSON.parse(JSON.stringify(editor.data));

        //첫번째 컴포넌트 선택
        const firstComponent = document.getElementById('form-panel').querySelectorAll('.component')[0];
        firstComponent.click();
        if (firstComponent.getAttribute('data-type') === aliceForm.defaultType) { //editbox 컴포넌트일 경우 input box 안에 포커싱
            firstComponent.querySelector('[contenteditable=true]').focus();
        }

        //폼 이름 출력
        changeFormName();

        //편집 여부 초기화
        isEdited = false;
    }

    /**
     * 폼 이름 변경.
     */
    function changeFormName() {
        document.getElementById('form-name').textContent = (isEdited ? '*' : '') + editor.data.name;
    }

    /**
     * init.
     *
     * @param {String} formId 폼 아이디
     * @param {String} flag view 모드 = false, edit 모드 = true
     */
    function init(formId, flag) {
        console.info('form editor initialization. [FORM ID: ' + formId + ']');
        formPanel = document.getElementById('form-panel');
        formPanel.setAttribute('data-readonly', true);
        formPanel.setAttribute('data-display', 'form');
        propertiesPanel = document.getElementById('properties-panel');

        if (flag === 'true') { isView = false; }

        //컴포넌트 메뉴 초기화
        component.init(formPanel);

        //컨텍스트 메뉴 초기화
        context.init();

        //단축키 초기화 및 등록
        shortcut.init();
        for (let i = 0; i < shortcuts.length; i++) {
            shortcut.add(shortcuts[i].keys, shortcuts[i].command, shortcuts[i].force);
        }

        //load custom-code list.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/custom-codes?viewType=editor',
            callbackFunc: function(xhr) {
                customCodeList = JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });

        // load form data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/form/' + formId + '/data',
            callbackFunc: function(xhr) {
                let responseObject = JSON.parse(xhr.responseText);
                responseObject.components = aliceForm.reformatCalendarFormat('read', responseObject.components);
                editor.data = responseObject;
                drawForm();
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    exports.init = init;
    exports.save = saveForm;
    exports.saveAs = saveAsForm;
    exports.undo = undoForm;
    exports.redo = redoForm;
    exports.preview = previewForm;
    exports.addComponent = addComponent;
    exports.copyComponent = copyComponent;
    exports.deleteComponent = deleteComponent;
    exports.selectFirstComponent = selectFirstComponent;
    exports.selectLastComponent = selectLastComponent;
    exports.selectUpComponent = selectUpComponent;
    exports.selectDownComponent = selectDownComponent;
    exports.reorderComponent = reorderComponent;
    exports.addEditboxDown = addEditboxDown;
    exports.getComponentIndex = getComponentIndex;
    exports.setComponentData = setComponentData;
    exports.showFormProperties = showFormProperties;
    exports.showComponentProperties = showComponentProperties;
    exports.hideComponentProperties = hideComponentProperties;
    exports.selectProperties = selectProperties;
    exports.getSelectComponentIndex = getSelectComponentIndex;
    exports.history = history;
    exports.selectedComponentIds = selectedComponentIds;
    exports.showDRTableColumnProperties = showDRTableColumnProperties;
    exports.uniqueCheck = uniqueCheck;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
