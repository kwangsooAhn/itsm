/**
 * @projectDescription Form Designer Editor Library
 *
 * @author woodajung
 * @version 1.0
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.editor = global.editor || {})));
}(this, (function (exports) {
    'use strict';

    const defaultComponent = 'editbox';
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
        { 'keys': 'ctrl+s', 'command': 'editor.save(false);' },               //폼 양식 저장
        { 'keys': 'ctrl+shift+s', 'command': 'editor.saveAs();' },            //폼 양식 다른이름으로 저장
        { 'keys': 'ctrl+z', 'command': 'editor.undo();' },                    //폼 편집 화면 작업 취소'
        { 'keys': 'ctrl+shift+z', 'command': 'editor.redo();' },              //폼 편집 화면 작업 재실행
        { 'keys': 'ctrl+i', 'command': 'editor.importForm();' },              //폼 양식 가져오기
        { 'keys': 'ctrl+b', 'command': 'editor.exportForm();' },              //폼 양식 내보내기
        { 'keys': 'ctrl+e', 'command': 'editor.preview();' },                 //폼 양식 미리보기
        { 'keys': 'ctrl+q', 'command': 'editor.save(true);' },                //폼 양식 저장하고 나가기
        { 'keys': 'insert', 'command': 'editor.copyComponent();' },           //컴포넌트를 복사하여 바로 아래 추가
        { 'keys': 'ctrl+x,delete', 'command': 'editor.deleteComponent();' },  //컴포넌트 삭제
        { 'keys': 'ctrl+pageup', 'command': 'editor.addEditboxUp();' },       //위에 컴포넌트 새로 만들기
        { 'keys': 'ctrl+pagedown', 'command': 'editor.addEditboxDown();' },   //아래 컴포넌트 새로 만들기
        { 'keys': 'ctrl+home', 'command': 'editor.selectFirstComponent();' }, //첫번째 컴포넌트 선택
        { 'keys': 'ctrl+end', 'command': 'editor.selectLastComponent();' },   //마지막 컴포넌트 선택
        { 'keys': 'up', 'command': 'editor.selectUpComponent();' },           //바로위 컴포넌트 선택
        { 'keys': 'down', 'command': 'editor.selectDownComponent();' },       //바로위 컴포넌트 선택
        { 'keys': 'alt+e', 'command': 'editor.selectProperties();' }          //세부 속성 편집: 제일 처음으로 이동
    ];

    let isEdited = false;
    window.addEventListener('beforeunload', function (event) {
        if (isEdited) {
            event.returnValue = '';
        }
    });
    let isView = true;            //view 인지여부
    let data = {};                 //저장용 데이터
    let savedData = {};

    let formPanel = null,
        propertiesPanel = null,
        selectedComponentId = '', //선택된 컴포넌트 ID
        formProperties = {},      //좌측 properties panel에 출력되는 폼 정보
        customCodeList = null;        //커스텀 컴포넌트 세부속성에서 사용할 코드 데이터

    /**
     * text, textarea validate check.
     *
     * @param element target element
     * @param validate validate attr
     */
    function validateCheck(element, validate) {
        if (typeof validate === 'undefined' || validate === '') { return; }
        const numberRegex = /^[-+]?[0-9]*\.?[0-9]+$/;
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
            }
        };

        element.addEventListener('focusout', function(e) {
            if (element.classList.contains('validate-error')) {
                element.classList.remove('validate-error');
            }
            if (element.value === '') { return; }
            let result = true;
            let validateArray = validate.split('|');
            for (let i = 0; i < validateArray.length; i++) {
                let validateValueArray = validateArray[i].split('[');
                let arg = (typeof validateValueArray[1] !== 'undefined') ? validateValueArray[1].replace(/\]\s*$/gi, '') : '';
                switch (validateValueArray[0]) {
                    case 'number':
                        result = validateFunc.number(element.value);
                        break;
                    case 'min':
                        result = validateFunc.number(element.value);
                        if (result) {
                            result = validateFunc.min(element.value, arg);
                        } else {
                            validateValueArray[0] = 'number';
                        }
                        break;
                    case 'max':
                        result = validateFunc.number(element.value);
                        if (result) {
                            result = validateFunc.max(element.value, arg);
                        } else {
                            validateValueArray[0] = 'number';
                        }
                        break;
                    case 'minLength':
                        result = validateFunc.minLength(element.value, arg);
                        break;
                    case 'maxLength':
                        result = validateFunc.maxLength(element.value, arg);
                        break;
                }
                if (!result) {
                    e.stopImmediatePropagation();
                    element.classList.add('validate-error');
                    aliceJs.alert(i18n.get('form.msg.alert.' + validateValueArray[0], arg), function() {
                        element.value = '';
                        element.focus();
                    });
                    break;
                }
            }
        });
    }

    /**
     * 폼 저장
     *
     * @param {String} flag 저장후  닫을지 여부
     */
    function saveForm(flag) {
        //view 모드이면 단축키로 저장되지 않는다.
        if (isView) { return false; }
        data = JSON.parse(JSON.stringify(editor.data));

        // 2020-05-22 Jung Hee Chan
        // datetime 형태의 속성들은 저장을 위해 시스템 공통 포맷으로 변경한다. (YYYY-MM-DD HH:mm, UTC+0)
        data.components = reformatCalendarFormat('save', data.components);

        let lastCompIndex = component.getLastIndex();
        data.components = data.components.filter(function(comp) {
            return !(comp.display.order === lastCompIndex && comp.type === defaultComponent);
        });
        aliceJs.sendXhr({
            method: 'PUT',
            url: '/rest/forms/' + data.form.id + '/data',
            callbackFunc: function(xhr) {
                if (xhr.responseText) {
                    isEdited = false;
                    savedData = JSON.parse(JSON.stringify(editor.data));
                    changeFormName();
                    if (flag) {
                        aliceJs.alert(i18n.get('common.msg.save'), function () {
                            opener.location.reload();
                            window.close();
                        });
                    } else {
                        aliceJs.alert(i18n.get('common.msg.save'));
                    }
                } else {
                    aliceJs.alert(i18n.get('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(data)
        });
    }

    /**
     * 다른 이름으로 저장 content.
     *
     * @return {string} content html
     */
    function createDialogContent() {
        return `
                <div>
                    <div>
                        <label class="gmodal-input-label" for="form_name">${i18n.get('form.label.name')}<span class="required">*</span></label>
                        <input class="gmodal-input" id="form_name">
                    </div>
                    <div>
                        <label class="gmodal-input-label" for="process_description">${i18n.get('form.label.description')}</label>
                        <textarea class="gmodal-input" rows="3" id="form_description"></textarea>
                    </div>
                    <div class="gmodal-required">${i18n.get('common.msg.requiredEnter')}</div>
                </div>
                `
    }

    /**
     * 다른 이름으로 저장.
     */
    function saveAsForm() {

        /**
         * 필수체크.
         *
         * @return {boolean} 체크성공여부
         */
        const checkRequired = function() {
            let nameLabelElem = document.getElementById('form_name');
            if (nameLabelElem.value.trim() === '') {
                nameLabelElem.style.backgroundColor = '#ff000040';
                document.querySelector('.gmodal-required').style.display = 'block';
                return false;
            }
            nameLabelElem.style.backgroundColor = '';
            document.querySelector('.gmodal-required').style.display = 'none';
            return true;
        };

        /**
         *  저장처리.
         */
        const saveAs = function() {
            data = JSON.parse(JSON.stringify(editor.data));
            let lastCompIndex = component.getLastIndex();
            data.components = data.components.filter(function (comp) {
                return !(comp.display.order === lastCompIndex && comp.type === defaultComponent);
            });
            data.form.name = document.getElementById('form_name').value;
            data.form.desc = document.getElementById('form_description').value;
            aliceJs.sendXhr({
                method: 'POST',
                url: '/rest/forms' + '?saveType=saveas',
                callbackFunc: function (xhr) {
                    if (xhr.responseText !== '') {
                        aliceJs.alert(i18n.get('common.msg.save'), function () {
                            opener.location.reload();
                            window.name = 'form_' + xhr.responseText + '_edit';
                            location.href = '/forms/' + xhr.responseText + '/edit';
                        });
                    } else {
                        aliceJs.alert(i18n.get('common.label.fail'));
                    }
                },
                contentType: 'application/json; charset=utf-8',
                params: JSON.stringify(data)
            });
        };

        const saveAsModal = new gModal({
            title: i18n.get('common.btn.saveAs'),
            body: createDialogContent(),
            buttons: [
                {
                    content: i18n.get('common.btn.cancel'),
                    classes: 'gmodal-button-red',
                    bindKey: false, /* no key! */
                    callback: function(modal) {
                        modal.hide();
                    }
                }, {
                    content: i18n.get('common.btn.save'),
                    classes: 'gmodal-button-green',
                    bindKey: false, /* no key! */
                    callback: function(modal) {
                        if (checkRequired()) {
                            saveAs();
                            modal.hide();
                        }
                    }
                }
            ],
            close: {
                closable: false,
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
                    let element = document.getElementById(originData.id);
                    element.remove();
                    for (let i = 0, len = editor.data.components.length; i < len; i++) {
                        if (originData.id === editor.data.components[i].id) {
                            editor.data.components.splice(i, 1);
                            break;
                        }
                    }
                } else { // add component
                    let defaultComponentAttr = component.getData(changeData.type);
                    let mergeComponentAttr = aliceJs.mergeObject(defaultComponentAttr, JSON.parse(JSON.stringify(changeData)));
                    let element = component.draw(changeData.type, formPanel, mergeComponentAttr);
                    const compOrder = Number(changeData.display.order) - 1;
                    let targetElement = formPanel.querySelectorAll('.component').item(compOrder);
                    targetElement.parentNode.insertBefore(element.domElem, targetElement);
                    setComponentData(element.attr);
                }
                reorderComponent();
            } else { // modify
                if (changeData.id === editor.data.form.id) { // form
                    editor.data.form = changeData;
                    if (originData.name !== changeData.name) { // modify name
                        changeFormName();
                    }
                } else { // component
                    let element = component.draw(changeData.type, formPanel, JSON.parse(JSON.stringify(changeData)));
                    let compAttr = element.attr;
                    setComponentData(compAttr);
                    let targetElement = document.getElementById(compAttr.id);
                    if (originData.display.order !== changeData.display.order) {
                        targetElement.innerHTML = '';
                        targetElement.remove();
                        reorderComponent();
                        const compOrder = Number(changeData.display.order) - 1;
                        let nextElement = formPanel.querySelectorAll('.component').item(compOrder);
                        nextElement.parentNode.insertBefore(element.domElem, nextElement);
                    } else {
                        targetElement.parentNode.insertBefore(element.domElem, targetElement);
                        targetElement.innerHTML = '';
                        targetElement.remove();
                    }
                    reorderComponent();
                }
            }
        };
        restoreData.forEach(function(data) {
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
        let url = '/forms/' + editor.data.form.id + '/preview';
        const specs = 'left=0,top=0,menubar=no,toolbar=no,location=no,status=no,titlebar=no,scrollbars=yes,resizable=no';
        window.open(url, 'result', 'width=800,height=805,' + specs);

        let form = document.createElement('form');
        form.action = url;
        form.method = 'POST';
        form.target = 'result';
        let input = document.createElement('textarea');
        input.name = 'data';
        input.value = JSON.stringify(editor.data);
        form.appendChild(input);
        form.style.display = 'none';
        document.body.appendChild(form);
        form.submit();
    }

    /**
     * export
     */
    function exportForm() {
        //TODO: export
    }

    /**
     * import
     */
    function importForm() {
        //TODO: import
    }

    /**
     * 컴포넌트 신규 추가
     * @param {String} type 컴포넌트 타입
     * @param {String} componentId 컴포넌트 Id
     */
    function addComponent(type, componentId) {
        if (type !== undefined) { //기존 editbox를 지운후, 해당 컴포넌트 추가
            let histories = [];
            let elem = document.getElementById(componentId);
            let replaceEditbox = editor.data.components.filter(function(comp) { return comp.id === componentId; });
            let replaceComp = component.draw(type, formPanel);
            let compAttr = replaceComp.attr;
            compAttr.id = componentId;
            setComponentData(compAttr);

            replaceComp.domElem.id = componentId;
            elem.parentNode.insertBefore(replaceComp.domElem, elem);
            elem.innerHTML = '';
            elem.remove();

            reorderComponent();

            let addCompAttr = editor.data.components.filter(function(comp) { return comp.id === componentId; });
            histories.push({0: JSON.parse(JSON.stringify(replaceEditbox[0])), 1: JSON.parse(JSON.stringify(addCompAttr[0]))});

            addEditboxDown(componentId, function(attr) {
                histories.push({0: {}, 1: JSON.parse(JSON.stringify(attr))});
                history.saveHistory(histories);
            });
        } else {
            let editbox = component.draw(defaultComponent, formPanel);
            setComponentData(editbox.attr);
            editbox.domElem.querySelector('[contenteditable=true]').focus();
            showComponentProperties(editbox.id);

            reorderComponent();
        }
    }

    /**
     * 컴포넌트 복사
     * @param {String} elemId 선택한 element Id
     */
    function copyComponent(elemId) {
        let copyElemId = elemId || selectedComponentId;
        let elem = document.getElementById(copyElemId);
        if (elem === null) { return; }

        //복사
        let elemIdx = Number(elem.getAttribute('data-index')) + 1;
        for (let i = 0; i < editor.data.components.length; i++) {
            if (copyElemId === editor.data.components[i].id) {
                let copyData = JSON.parse(JSON.stringify(editor.data.components[i]));
                copyData.id = workflowUtil.generateUUID();
                let comp = component.draw(copyData.type, formPanel, copyData);
                setComponentData(comp.attr);
                elem.parentNode.insertBefore(comp.domElem, elem.nextSibling);
                //재정렬
                reorderComponent();
                if (copyData.type === 'editbox') {
                    comp.domElem.querySelector('[contenteditable=true]').focus();
                }
                showComponentProperties(comp.id);

                let copyCompAttr = editor.data.components.filter(function(c) { return c.id === comp.id; });
                history.saveHistory([{0: {}, 1: JSON.parse(JSON.stringify(copyCompAttr[0]))}]);
                break;
            }
        }
    }

    /**
     * 컴포넌트 삭제
     * @param {String} elemId 선택한 element Id
     */
    function deleteComponent(elemId) {
        let delElemId = elemId || selectedComponentId;
        let elem = document.getElementById(delElemId);
        if (elem === null) { return; }

        //삭제 후 다음 컴포넌트에 focus가 가며, 이전 컴포넌트로 포커스가 이동한다. 첫번째 컴포넌트일 경우 바로 아래 컴포넌트로 focus가 간다.
        let previousSelectedElem = elem.previousElementSibling;
        if (previousSelectedElem === null && elem.getAttribute('data-index') === '1') {
            previousSelectedElem = elem.nextElementSibling;
        }

        //editbox 컴포넌트 1개만 존재할 경우 삭제 로직을 타지 않는다.
        if (document.querySelectorAll('.component').length === 1 &&
            elem.getAttribute('data-type') === defaultComponent) { return false; }


        let histories = [];
        //컴포넌트 삭제
        elem.remove();
        selectedComponentId = '';
        for (let i = 0; i < editor.data.components.length; i++) {
            if (delElemId === editor.data.components[i].id) {
                histories.push({0: JSON.parse(JSON.stringify(editor.data.components[i])), 1: {}});
                editor.data.components.splice(i, 1);
                break;
            }
        }
        //컴포넌트 없을 경우 editbox 컴포넌트 신규 추가.
        if (document.querySelectorAll('.component').length === 0) {
            let editbox = component.draw(defaultComponent, formPanel);
            histories.push({0: {}, 1: JSON.parse(JSON.stringify(editbox.attr))});
            setComponentData(editbox.attr);
            editbox.domElem.querySelector('[contenteditable=true]').focus();
            showComponentProperties(editbox.id);
        } else {
            if (previousSelectedElem.getAttribute('data-type') === defaultComponent) {
                previousSelectedElem.querySelector('[contenteditable=true]').focus();
            }
            showComponentProperties(previousSelectedElem.id);
        }
        //재정렬
        reorderComponent();
        // 이력저장
        history.saveHistory(histories);
    }
    /**
     * 첫번째 컴포넌트 선택
     */
    function selectFirstComponent() {
        let firstComponent = formPanel.firstElementChild;
        if (firstComponent.getAttribute('data-type') === defaultComponent) {
            firstComponent.querySelector('[contenteditable=true]').focus();
        }
        formPanel.scrollTop = 0;
        showComponentProperties(firstComponent.id);
    }

    /**
     * 마지막 컴포넌트 선택 : 컴포넌트가 1개 밖에 없을 경우 첫번째 컴포넌트 선택됨
     */
    function selectLastComponent() {
        let lastComponent = formPanel.lastElementChild;
        if (lastComponent.getAttribute('data-type') === defaultComponent) {
            lastComponent.querySelector('[contenteditable=true]').focus();
        }
        formPanel.scrollTop = formPanel.scrollHeight;
        showComponentProperties(lastComponent.id);
    }

    /**
     * 바로 위 컴포넌트 선택
     */
    function selectUpComponent() {
        if (document.getElementById('context-menu').classList.contains('on')) { return false; }
        if (selectedComponentId === '') { return false; }

        let selectedElem = document.getElementById(selectedComponentId);
        if (selectedElem !== null) {
            let previousElem = selectedElem.previousElementSibling;
            if (previousElem === null && selectedElem.getAttribute('data-index') === '1') {
                formPanel.scrollTop = formPanel.scrollHeight;
                previousElem = formPanel.lastElementChild;
            }
            if (previousElem.getAttribute('data-type') === defaultComponent) {
                previousElem.querySelector('[contenteditable=true]').focus();
            }
            showComponentProperties(previousElem.id);
        }
    }

    /**
     * 바로 아래 컴포넌트 선택
     */
    function selectDownComponent() {
        if (document.getElementById('context-menu').classList.contains('on')) { return false; }
        if (selectedComponentId === '') { return false; }

        let selectedElem = document.getElementById(selectedComponentId);
        if (selectedElem !== null) {
            let nextElem = selectedElem.nextElementSibling;
            if (nextElem === null && Number(selectedElem.getAttribute('data-index')) === component.getLastIndex()) {
                formPanel.scrollTop = 0;
                nextElem = formPanel.firstElementChild;
            }
            if (nextElem.getAttribute('data-type') === defaultComponent) {
                nextElem.querySelector('[contenteditable=true]').focus();
            }
            showComponentProperties(nextElem.id);
        }
    }

    /**
     * 세부 속성 편집: 제일 처음으로 이동
     */
    function selectProperties() {
        if (propertiesPanel.getElementsByTagName('input')[0] === null) { return false; }

        propertiesPanel.getElementsByTagName('input')[0].focus();
    }

    /**
     * elemId 선택한 element Id를 기준으로 위에 editbox 추가 후 data의 display order 변경
     * @param {String} elemId 선택한 element Id
     */
    function addEditboxUp(elemId) {
        let addElemId = elemId || selectedComponentId;
        let elem = document.getElementById(addElemId);
        if (elem === null) { return; }

        let editbox = component.draw(defaultComponent, formPanel);
        setComponentData(editbox.attr);
        elem.parentNode.insertBefore(editbox.domElem, elem);

        // 컴포넌트 순서 재정렬
        reorderComponent();

        editbox.domElem.querySelector('[contenteditable=true]').focus();
        showComponentProperties(editbox.id);

        let addEditboxCompAttr = editor.data.components.filter(function(comp) { return comp.id === editbox.id; });
        history.saveHistory([{0: {}, 1: JSON.parse(JSON.stringify(addEditboxCompAttr[0]))}]);
    }

    /**
     * elemId 선택한 element Id를 기준으로 아래에 editbox 추가 후 data의 display order 변경
     * @param {String} elemId 선택한 element Id
     * @param {Function} callbackFunc callback function
     */
    function addEditboxDown(elemId, callbackFunc) {
        let addElemId = elemId || selectedComponentId;
        let elem = document.getElementById(addElemId);
        if (elem === null) { return; }

        let editbox = null;
        if (elem.nextSibling !== null) {
            editbox = component.draw(defaultComponent, formPanel);
            setComponentData(editbox.attr);
            elem.parentNode.insertBefore(editbox.domElem, elem.nextSibling);
        } else { //마지막에 추가된 경우
            editbox = component.draw(defaultComponent, formPanel);
            setComponentData(editbox.attr);
            elem.parentNode.appendChild(editbox.domElem);
        }
        // 컴포넌트 순서 재정렬
        reorderComponent();

        editbox.domElem.querySelector('[contenteditable=true]').focus();
        showComponentProperties(editbox.id);

        if (typeof callbackFunc === 'function') {
            callbackFunc(editbox.attr);
        } else {
            let addEditboxCompAttr = editor.data.components.filter(function(comp) { return comp.id === editbox.id; });
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
                if (comp.id === elem.id) {
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
    }

    /**
     * 컴포넌트 ID를 전달 받아서 일치하는 컴포넌트의 index 반환한다
     * @param {String} id 조회할 컴포넌트 id
     * @return {Number} component index 조회한 컴포넌트 index
     */
    function getComponentIndex(id) {
        for (let i = 0, len = editor.data.components.length; i < len; i++) {
            let comp = editor.data.components[i];
            if (comp.id === id) { return i; }
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
            if (comp.id === compData.id) {//수정
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
        let initializedProperties = aliceJs.mergeObject({}, aliceForm.options.componentAttribute[componentData.type]);

        Object.keys(componentData).forEach(function(propertyGroupId) {
            if (aliceJs.isObject(componentData[propertyGroupId]) && initializedProperties.hasOwnProperty(propertyGroupId))  {
                Object.keys(componentData[propertyGroupId]).forEach(function(propertyId) {
                    Object.keys(initializedProperties[propertyGroupId]).forEach(function(initProperty) {
                        if (propertyId === initializedProperties[propertyGroupId][initProperty].id) {
                            initializedProperties[propertyGroupId][initProperty].value = componentData[propertyGroupId][propertyId];
                        }
                    });
                });
            }
        });
        return initializedProperties;
    }

    /**
     * 우측 properties panel 세부 속성 출력
     * @param {String} id 조회할 컴포넌트 ID
     */
    function showComponentProperties(id) {
        if (selectedComponentId === id) { return false; }
        propertiesPanel.innerHTML = '';

        if (selectedComponentId !== '') { //기존 선택된 컴포넌트 css 삭제
            if (document.getElementById(selectedComponentId).classList.contains('selected')) {
                document.getElementById(selectedComponentId).classList.remove('selected');
            }
        }

        selectedComponentId = id;
        let selectedComponentElem = document.getElementById(id);
        if (selectedComponentElem === null) { return false; }

        //현재 선택된 컴포넌트가 editbox라면 form 속성을 출력한다.
        if (selectedComponentElem.getAttribute('data-type') === defaultComponent) {
            showFormProperties(selectedComponentId);
            return false;
        }

        selectedComponentElem.classList.add('selected'); //현재 선택된 컴포넌트 css 추가

        let compIdx = getComponentIndex(id);
        if (compIdx === -1) { return false; }

        let compAttr = editor.data.components[compIdx];
        /**
         * 컴포넌트를 다시 그린다.
         */
        const redrawComponent = function() {
            let element = component.draw(compAttr.type, formPanel, compAttr);
            if (element) {
                let compAttr = element.attr;
                compAttr.id = id;
                setComponentData(compAttr);

                let targetElement = document.getElementById(id);
                element.domElem.id = id;

                targetElement.parentNode.insertBefore(element.domElem, targetElement);
                targetElement.innerHTML = '';
                targetElement.remove();

                element.domElem.classList.add('selected');

                reorderComponent();
            }
        };

        /**
         * 변경된 값을 컴포넌트 속성 정보에 반영하고, 컴포넌트를 다시 그린다.
         * @param {String} value 변경된 값
         * @param {String} group 변경된 그룹 key
         * @param {String} field 변경된 field key
         * @param {Number} [index] 변경된 index (그룹이 option 일 경우만 해당되므로 생략가능)
         */
        const changePropertiesValue = function(value, group, field, index) {
            let originCompAttr = JSON.parse(JSON.stringify(compAttr));
            if (typeof index === 'undefined') {
                compAttr[group][field] = value;
            } else {
                compAttr[group][index][field] = value;
            }
            history.saveHistory([{0: originCompAttr, 1: JSON.parse(JSON.stringify(compAttr))}]);
            redrawComponent();
        };

        /**
         * date, time, datetime default 포멧 변경시,
         * default 값을 none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등으로 저장한다.
         * @param {Object} e 이벤트 대상
         */
        const setDateFormat = function(e) {
            let el = e.target || e;
            let parentEl = e.target ? el.parentNode : el.parentNode.parentNode;
            if (parentEl.classList.contains('property-field')) {
                let changePropertiesArr = el.name.split('.');
                changePropertiesValue(el.value, changePropertiesArr[0], changePropertiesArr[1]);
            } else {
                let checkedRadio = parentEl.parentNode.querySelector('input[type=radio]:checked');
                if (checkedRadio === null || parentEl.firstElementChild.id !== checkedRadio.id) { return false; }

                let checkedPropertiesArr = checkedRadio.name.split('.');
                let changeValue = checkedRadio.value;
                if (changeValue === 'none' || changeValue === 'now') {
                    changePropertiesValue(changeValue, checkedPropertiesArr[0], checkedPropertiesArr[1]);
                } else {
                    let inputCells = parentEl.querySelectorAll('input[type="text"]');
                    if (changeValue === 'datepicker' || changeValue === 'timepicker' || changeValue === 'datetimepicker') {
                        changeValue += ('|' + inputCells[0].value);
                    } else {
                        for (let i = 0, len = inputCells.length; i < len; i++ ) {
                            changeValue += ('|' + inputCells[i].value);
                        }
                    }
                    changePropertiesValue(changeValue, checkedPropertiesArr[0], checkedPropertiesArr[1]);
                }
            }
        };
        let detailAttr = initProperties(compAttr);
        //제목 출력
        let compTitleAttr = component.getTitle(compAttr.type);
        let compTitleElem = document.createElement('div');
        compTitleElem.classList.add('property-title');
        compTitleElem.textContent = compTitleAttr.name;
        //TODO: 제목 icon 추가
        propertiesPanel.appendChild(compTitleElem);

        //TODO: 세부 속성 출력 코드 리팩토링
        Object.keys(detailAttr).forEach(function(group) {
            let groupDiv = document.createElement('div');
            groupDiv.setAttribute('id', group);
            groupDiv.classList.add('property-group');
            groupDiv.textContent = group;
            propertiesPanel.appendChild(groupDiv);

            let buttonExist = false,
                fieldButtonDiv = null,
                groupTb = null;
            if (group === 'option') { //세부 속성에 옵션이 존재할 경우 table 구조로 출력
                let plusButton = document.createElement('button');
                plusButton.classList.add('plus');
                plusButton.addEventListener('click', function() { //옵션 추가 버튼
                    let tb = this.parentNode.querySelector('table');
                    let row = document.createElement('tr');
                    let cell = document.createElement('td');
                    const rowCount = tb.rows.length;
                    cell.innerHTML = tb.lastElementChild.children[0].innerHTML;
                    row.appendChild(cell);

                    const rowData = {};
                    detailAttr.option[0].items.forEach(function(option, i) {
                        cell = document.createElement('td');
                        cell.id = option.id;
                        cell.innerHTML = tb.lastElementChild.children[i + 1].innerHTML;
                        let inputCell = cell.querySelector('input');
                        inputCell.addEventListener('change', function() {
                            changePropertiesValue(this.value, group, option.id, rowCount - 1);
                        }, false);
                        if (option.id === 'seq') {
                            inputCell.value = rowCount;
                            inputCell.setAttribute('readonly', 'true');
                            rowData[option.id] = rowCount;
                        } else {
                            inputCell.value = option.value;
                            rowData[option.id] = option.value;
                        }
                        row.appendChild(cell);
                    });
                    compAttr[group].push(rowData);
                    tb.appendChild(row);
                    redrawComponent();
                });
                groupDiv.appendChild(plusButton);

                let minusButton = document.createElement('button');
                minusButton.classList.add('minus');
                minusButton.addEventListener('click', function() { //옵션 삭제 버튼
                    let minusCnt = 0;
                    let tb = this.parentNode.querySelector('table');
                    let rowCount = tb.rows.length;
                    for (let i = 1; i < rowCount; i++) {
                        let row = tb.rows[i];
                        let chkbox = row.cells[0].childNodes[0];
                        let seqCell = row.cells[1].childNodes[0];
                        if (chkbox.checked && rowCount > 2) {
                            tb.deleteRow(i);
                            compAttr[group].splice(i - 1, 1);
                            rowCount--;
                            i--;
                            minusCnt++;
                        } else if (seqCell.value !== i) {
                            seqCell.value = i;
                            compAttr[group][i - 1].seq = i;
                        }
                    }
                    if (minusCnt > 0) {
                        redrawComponent();
                    }
                });
                groupDiv.appendChild(minusButton);

                groupTb = document.createElement('table');
                groupDiv.appendChild(groupTb);
            }
            if (detailAttr[group] !== null && typeof(detailAttr[group]) === 'object')  { //세부 속성
                Object.keys(detailAttr[group]).forEach(function(field) {
                    let fieldArr = detailAttr[group][field];
                    let fieldGroupDiv = null,
                        propertyName = null,
                        propertyValue = null;
                    if (fieldArr.type === 'button') {
                        if (!buttonExist) {
                            fieldGroupDiv = document.createElement('div');
                            fieldGroupDiv.classList.add('property-field');
                            fieldGroupDiv.setAttribute('id', fieldArr.id);
                            groupDiv.appendChild(fieldGroupDiv);

                            fieldGroupDiv.removeAttribute('id');
                            fieldButtonDiv = document.createElement('div');
                            fieldButtonDiv.classList.add('property-field-button');
                            fieldGroupDiv.appendChild(fieldButtonDiv);
                            buttonExist = true;
                        }
                    } else if (fieldArr.id !== undefined) {
                        fieldGroupDiv = document.createElement('div');
                        fieldGroupDiv.classList.add('property-field');
                        fieldGroupDiv.setAttribute('id', fieldArr.id);
                        groupDiv.appendChild(fieldGroupDiv);
                    }
                    if (fieldArr.type !== 'button' && fieldArr.type !== 'table') { //속성명 출력
                        propertyName = document.createElement('span');
                        propertyName.classList.add('property-field-name');
                        propertyName.textContent = fieldArr.name;
                        fieldGroupDiv.appendChild(propertyName);

                        //도움말 추가
                        if (typeof fieldArr.help !== 'undefined') {
                            const helpTooltip = document.createElement('div');
                            helpTooltip.classList.add('help-tooltip');
                            const helpTootltipContent = document.createElement('p');
                            helpTootltipContent.innerHTML = i18n.get(fieldArr.help);
                            helpTooltip.appendChild(helpTootltipContent);
                            propertyName.appendChild(helpTooltip);
                        }
                    }
                    switch (fieldArr.type) {
                        case 'inputbox':
                        case 'inputbox-underline':
                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.setAttribute('value', fieldArr.value);
                            validateCheck(propertyValue, fieldArr.validate);
                            propertyValue.addEventListener('focusout', function() {
                                changePropertiesValue(this.value, group, fieldArr.id);
                            }, false);
                            fieldGroupDiv.appendChild(propertyValue);

                            if (fieldArr.type === 'inputbox-underline') { propertyValue.classList.add('underline'); }

                            if (fieldArr.unit !== '') {
                                let propertyUnit = document.createElement('span');
                                propertyUnit.classList.add('property-field-unit');
                                propertyUnit.textContent = fieldArr.unit;
                                fieldGroupDiv.appendChild(propertyUnit);
                            }
                            break;
                        case 'select':
                            propertyValue = document.createElement('select');
                            propertyValue.classList.add('property-field-value');
                            for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                let propertyOption = document.createElement('option');
                                propertyOption.value = fieldArr.option[i].id;
                                propertyOption.text = fieldArr.option[i].name;
                                if (fieldArr.value === fieldArr.option[i].id) {
                                    propertyOption.setAttribute('selected', 'selected');
                                }
                                propertyValue.appendChild(propertyOption);
                            }
                            propertyValue.addEventListener('change', function() {
                                changePropertiesValue(this.value, group, fieldArr.id);
                            }, false);
                            fieldGroupDiv.appendChild(propertyValue);
                            break;
                        case 'session':
                            propertyValue = document.createElement('select');
                            propertyValue.classList.add('property-field-value');
                            let propertyValueArr = fieldArr.value.split('|');
                            /**
                             * 사용자 입력을 받는 inputbox 또는  selectbox를 생성하고 이벤트를 등록한다.
                             *  none|직접입력값, select|userid|아이디, select|username|이름 등
                             * @param {String} type none, select 2가지 타입
                             * @param {String} defaultValue 기본 값
                             */
                            const setSubList = function(type, defaultValue) {
                                let subListElem = null;
                                if (type === 'none') {
                                    subListElem = document.createElement('input');
                                    subListElem.setAttribute('type', 'text');
                                    subListElem.setAttribute('value', defaultValue);
                                } else {
                                    subListElem = document.createElement('select');
                                    if (defaultValue === '') { defaultValue = fieldArr.option[1].items[0].id + '|' + fieldArr.option[1].items[0].name; }
                                    for (let i = 0, len = fieldArr.option[1].items.length; i < len; i++) {
                                        let selectItem = fieldArr.option[1].items[i];
                                        let subListOption = document.createElement('option');
                                        subListOption.value = selectItem.id;
                                        subListOption.text = selectItem.name;
                                        if (defaultValue === selectItem.id) {
                                            subListOption.setAttribute('selected', 'selected');
                                            defaultValue += ('|' + selectItem.name);
                                        }
                                        subListElem.appendChild(subListOption);
                                    }
                                }
                                subListElem.setAttribute('id', compAttr.id + '-' + group + '-' + fieldArr.id + '-session');
                                subListElem.classList.add('default-session');
                                subListElem.addEventListener('change', function() {
                                    if (type === 'none') {
                                        changePropertiesValue(type + '|' + this.value, group, fieldArr.id);
                                    } else {
                                        changePropertiesValue(type + '|' + this.value + '|' + this.options[this.selectedIndex].text, group, fieldArr.id);
                                    }
                                }, false);
                                fieldGroupDiv.appendChild(subListElem);

                                changePropertiesValue(type + '|' + defaultValue, group, fieldArr.id);
                            };
                            for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                let propertyOption = document.createElement('option');
                                propertyOption.value = fieldArr.option[i].id;
                                propertyOption.text = fieldArr.option[i].name;
                                if (propertyValueArr[0] === fieldArr.option[i].id) {
                                    propertyOption.setAttribute('selected', 'selected');
                                }
                                propertyValue.appendChild(propertyOption);
                            }
                            propertyValue.addEventListener('change', function() {
                                let delElem = document.getElementById( compAttr.id + '-' + group + '-' + fieldArr.id + '-session');
                                if (delElem) {
                                    delElem.remove();
                                }
                                setSubList(this.value, '');
                            }, false);

                            fieldGroupDiv.appendChild(propertyValue);
                            setSubList(propertyValueArr[0], propertyValueArr[1]);
                            break;
                        case 'slider':
                            propertyValue = document.createElement('input');
                            propertyValue.setAttribute('id', group + '-' + fieldArr.id);
                            propertyValue.setAttribute('type', 'range');
                            propertyValue.setAttribute('min', 1);
                            propertyValue.setAttribute('max', 12);
                            propertyValue.setAttribute('value', fieldArr.value);
                            fieldGroupDiv.appendChild(propertyValue);
                            propertyValue.addEventListener('change', function() {
                                let slider = document.getElementById(this.id + '-value');
                                slider.value = this.value;
                                changePropertiesValue(this.value, group, fieldArr.id);
                            });

                            let slideValue = document.createElement('input');
                            slideValue.classList.add('property-field-value', 'underline');
                            slideValue.setAttribute('id', group + '-' + fieldArr.id + '-value');
                            slideValue.setAttribute('type', 'text');
                            slideValue.setAttribute('value', fieldArr.value);
                            slideValue.setAttribute('readOnly', 'true');
                            fieldGroupDiv.appendChild(slideValue);
                            break;
                        case 'rgb':
                            let selectedColorBox = document.createElement('span');
                            selectedColorBox.classList.add('selected-color');
                            selectedColorBox.style.backgroundColor = fieldArr.value;
                            fieldGroupDiv.appendChild(selectedColorBox);

                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value', 'underline', 'color');
                            propertyValue.setAttribute('id', group + '-' + fieldArr.id + '-value');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.setAttribute('value', fieldArr.value);
                            propertyValue.setAttribute('readonly', 'true');
                            propertyValue.addEventListener('change', function() {
                                changePropertiesValue(this.value, group, fieldArr.id);
                            }, false);
                            fieldGroupDiv.appendChild(propertyValue);
                            let colorPaletteDiv = document.createElement('div');
                            colorPaletteDiv.setAttribute('id', group + '-' + fieldArr.id + '-colorPalette');
                            colorPaletteDiv.classList.add('color-palette');
                            groupDiv.appendChild(colorPaletteDiv);
                            colorPalette.initColorPalette(selectedColorBox, propertyValue, colorPaletteDiv);
                            break;
                        case 'radio':
                            for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                let propertyOption = document.createElement('input');
                                propertyOption.setAttribute('type', 'radio');
                                propertyOption.setAttribute('id', fieldArr.name + '-' + fieldArr.option[i].id);
                                propertyOption.value = fieldArr.option[i].id;
                                propertyOption.name = fieldArr.name;
                                if (fieldArr.value === fieldArr.option[i].id) {
                                    propertyOption.setAttribute('checked', 'checked');
                                }
                                propertyOption.addEventListener('change', function() {
                                    changePropertiesValue(this.value, group, fieldArr.id);
                                }, false);
                                fieldGroupDiv.appendChild(propertyOption);
                                let propertyLabel = document.createElement('label');
                                propertyLabel.setAttribute('for', fieldArr.name + '-' + fieldArr.option[i].id);
                                propertyLabel.textContent = fieldArr.option[i].name;
                                fieldGroupDiv.appendChild(propertyLabel);
                            }
                            break;
                        case 'radio-datetime':
                            fieldGroupDiv.classList.add('vertical');
                            let optionDefaultArr;
                            let defaultFormatArr = fieldArr.value !== '' ? fieldArr.value.split('|') : ''; //none, now, date|-3, time|2, datetime|7|0 등 
                            let propertyTemplate = ``;
                            for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                let option = fieldArr.option[i];
                                optionDefaultArr = ['', '', ''];
                                if (defaultFormatArr[0] === option.id) {
                                    optionDefaultArr = defaultFormatArr;
                                }
                                let labelName = option.name.split('{0}');

                                propertyTemplate += `
                                    <div class='vertical-group'>
                                    <input type='radio' id='${option.id}' name='${group}.${fieldArr.id}' value='${option.id}'
                                    ${defaultFormatArr[0] === option.id ? "checked='true'" : ""} />
                                    
                                    ${option.id === 'date' || option.id === 'time' ? "<input type='text' id='" + option.id +"' value='" + optionDefaultArr[1] + "'/><label for='" + option.id + "'>" + labelName[1] + "</label>" : ""}
                                    
                                    ${option.id === 'datetime'?
                                    "<input type='text' id='" + option.id +"-0' value='" + optionDefaultArr[1] + "' /><label for='" + option.id + "-0'>" + labelName[1] + "</label>" +
                                    "<input type='text' id='" + option.id +"-1' value='" + optionDefaultArr[2] + "' /><label for='" + option.id + "-1'>" + labelName[2] + "</label>" : ""}
                                    
                                    ${option.id === 'datepicker' || option.id === 'timepicker' || option.id === 'datetimepicker' ? "<input type='text' id='" + option.id + "-" + compAttr.id + "' value='" + optionDefaultArr[1] + "' style='width: 13.2rem;'/>" : ""}

                                    ${option.id === 'now' || option.id === 'none' ? "<label for='" + option.id + "'>" + labelName[0] + "</label>" : ""}
                                    </div>
                                `;
                            }
                            fieldGroupDiv.innerHTML += propertyTemplate;

                            //이벤트 등록
                            let changeOptions = fieldGroupDiv.querySelectorAll('input[type="radio"], input[type="text"]');
                            for (let i = 0, len = changeOptions.length; i < len; i++ ) {
                                if (changeOptions[i].type === 'text') {
                                    for (let j = 0; j < fieldArr.option.length; j++) {
                                        if (changeOptions[i].id.split('-')[0] === fieldArr.option[j].id) {
                                            validateCheck(changeOptions[i], fieldArr.option[j].validate);
                                        }
                                    }
                                    changeOptions[i].addEventListener('focusout', setDateFormat, false);
                                } else {
                                    changeOptions[i].addEventListener('change', setDateFormat, false);
                                }
                            }

                            if (compAttr.type === 'date') {
                                dateTimePicker.initDatePicker('datepicker-' + compAttr.id, aliceForm.options.dateFormat, aliceForm.options.lang, setDateFormat);
                            } else if (compAttr.type === 'time') {
                                dateTimePicker.initTimePicker('timepicker-' + compAttr.id, aliceForm.options.hourFormat, aliceForm.options.lang, setDateFormat);
                            } else if (compAttr.type === 'datetime') {
                                dateTimePicker.initDateTimePicker('datetimepicker-' + compAttr.id, aliceForm.options.dateFormat, aliceForm.options.hourFormat, aliceForm.options.lang, setDateFormat);
                            }
                            break;
                        case 'radio-custom':
                            fieldGroupDiv.classList.add('vertical');
                            let fieldValueArr = fieldArr.value.split('|');
                            let customDefaultTemplate = ``;
                            for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                let option = fieldArr.option[i];
                                customDefaultTemplate += `
                                    <div class='vertical-group'>
                                    <input type='radio' id='${option.id}' name='${group}.${fieldArr.id}' value='${option.id}'
                                    ${option.id === fieldValueArr[0] ? "checked='true'" : ""} />
                                    <label for='${option.id}'>${option.name}</label>
                                    ${option.id !== 'none' ? "<br/><select>" + option.items.map(item => `<option value='${item.id}' ${item.id === fieldValueArr[1] ? "selected='selected'" : ""}>${item.name}</option>`).join('') + "</select>": ""}
                                    </div>
                                `;
                            }
                            fieldGroupDiv.innerHTML += customDefaultTemplate;

                            let customCodeSelect = propertiesPanel.querySelector('#custom-code > select');
                            let changeCustomCode = function(val) {
                                let customCodeDataSelect = fieldGroupDiv.querySelector('input[id=code]').parentNode.querySelector('select');
                                customCodeDataSelect.innerHTML = '';
                                // load custom code data.
                                aliceJs.sendXhr({
                                    method: 'GET',
                                    url: '/rest/custom-codes/' + customCodeSelect.value,
                                    callbackFunc: function(xhr) {
                                        let customCodeData = JSON.parse(xhr.responseText);
                                        customCodeDataSelect.innerHTML = customCodeData.map(d => `<option value='${d.key}'>${d.value}</option>`).join('');
                                        if (val) {
                                            customCodeDataSelect.value = val;
                                        }
                                        let targetRadio = customCodeDataSelect.parentNode.querySelector('input[type=radio]');
                                        if (targetRadio.checked) {
                                            let val = 'code|' + customCodeDataSelect.value + '|';
                                            if (customCodeDataSelect.selectedIndex !== -1) {
                                                val += customCodeDataSelect.options[customCodeDataSelect.selectedIndex].text;
                                            }
                                            changePropertiesValue(val, group, fieldArr.id);
                                        }
                                    },
                                    contentType: 'application/json; charset=utf-8',
                                    showProgressbar: false
                                });
                            };
                            customCodeSelect.addEventListener('change', function() { changeCustomCode(); });
                            changeCustomCode(fieldValueArr[0] === 'code' ? fieldValueArr[1] : null);

                            fieldGroupDiv.querySelectorAll('input[type=radio], select').forEach(function(elem) {
                                elem.addEventListener('change', function(e) {
                                    let targetId = this.id;
                                    let targetRadio = this.parentNode.querySelector('input[type=radio]');
                                    if (elem.tagName.toUpperCase() === 'SELECT') {
                                        if (!targetRadio.checked) { return; }
                                        targetId = targetRadio.id;
                                    }
                                    let val = targetId !== 'none' ? targetId + '|' + this.parentNode.querySelector('select').value : targetId;
                                    if (targetRadio.checked && targetRadio.id !== 'none') {
                                        val += '|';
                                        if (this.parentNode.querySelector('select').selectedIndex !== -1) {
                                            val += this.parentNode.querySelector('select').options[this.parentNode.querySelector('select').selectedIndex].text;
                                        }
                                    }
                                    changePropertiesValue(val, group, fieldArr.id);
                                });
                            });

                            break;
                        case 'button':
                            if (fieldButtonDiv === null) { break; }

                            if (fieldArr.option !== undefined) {
                                for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                    propertyValue = document.createElement('button');
                                    propertyValue.classList.add(fieldArr.id);
                                    propertyValue.setAttribute('id', fieldArr.option[i].id);
                                    if (fieldArr.value === fieldArr.option[i].id) {
                                        propertyValue.classList.add('active');
                                    }
                                    fieldButtonDiv.appendChild(propertyValue);
                                    propertyValue.addEventListener('click', function() {
                                        if (!this.classList.contains('active')) {
                                            let className = this.classList[0];
                                            for (let i = 0, len = this.parentNode.childNodes.length ; i< len; i++) {
                                                let child = this.parentNode.childNodes[i];
                                                if (child.classList.contains(className)) {
                                                    child.classList.remove('active');
                                                }
                                            }
                                            this.classList.add('active');
                                            changePropertiesValue(this.id, group, fieldArr.id);
                                        }
                                    });
                                }
                            } else { //boolean
                                propertyValue = document.createElement('button');
                                propertyValue.classList.add(fieldArr.id);
                                propertyValue.setAttribute('id', fieldArr.id);
                                propertyValue.setAttribute('data-value', fieldArr.value);
                                if (fieldArr.value === 'Y') {
                                    propertyValue.classList.add('active');
                                }
                                fieldButtonDiv.appendChild(propertyValue);
                                propertyValue.addEventListener('click', function() {
                                    let isActive = this.classList.contains('active');
                                    if (isActive) {
                                        this.classList.remove('active');
                                        this.setAttribute('data-value', 'N');
                                    } else {
                                        this.classList.add('active');
                                        this.setAttribute('data-value', 'Y');
                                    }
                                    changePropertiesValue(isActive ? 'N' : 'Y', group, fieldArr.id);
                                });
                            }
                            break;
                        case 'table':
                            let headerRow = document.createElement('tr');
                            groupTb.appendChild(headerRow);

                            let headerCell = document.createElement('th');
                            headerRow.appendChild(headerCell);
                            for (let i = 0, len = fieldArr.items.length; i < len; i++) {
                                headerCell = document.createElement('th');
                                headerCell.innerHTML = fieldArr.items[i].name;
                                headerRow.appendChild(headerCell);
                            }

                            for (let i = 0, len = compAttr.option.length; i < len; i++) {
                                let row = document.createElement('tr');
                                groupTb.appendChild(row);

                                let cell = document.createElement('td');
                                let chkbox = document.createElement('input');
                                chkbox.setAttribute('type', 'checkbox');
                                cell.appendChild(chkbox);
                                row.appendChild(cell);

                                for (let j = 0, itemLen = fieldArr.items.length; j < itemLen; j++) {
                                    cell = document.createElement('td');
                                    cell.setAttribute('id', fieldArr.items[j].id);
                                    let inputCell = document.createElement('input');
                                    inputCell.setAttribute('type', 'text');
                                    inputCell.setAttribute('value', compAttr.option[i][fieldArr.items[j].id]);
                                    if (fieldArr.items[j].id === 'seq') {
                                        inputCell.setAttribute('readonly', 'true');
                                    }
                                    inputCell.addEventListener('change', function() {
                                        let seqCell = this.parentNode.parentNode.cells[1].childNodes[0];
                                        changePropertiesValue(this.value, group, fieldArr.items[j].id, Number(seqCell.value) - 1);
                                    }, false);
                                    cell.appendChild(inputCell);
                                    row.appendChild(cell);
                                }
                            }
                            break;
                        case 'datepicker':
                        case 'timepicker':
                        case 'datetimepicker':
                            propertyValue = document.createElement('input');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.classList.add('property-field-value');
                            propertyValue.setAttribute('id', fieldArr.id + '-' + compAttr.id);
                            propertyValue.setAttribute('name', group + '.' + fieldArr.id);
                            let dateTimePickerValue = '';
                            if (fieldArr.value != '') {
                                dateTimePickerValue = fieldArr.value;
                            }
                            propertyValue.setAttribute('value', dateTimePickerValue);
                            fieldGroupDiv.appendChild(propertyValue);
                            if (fieldArr.type === 'datepicker') {
                                dateTimePicker.initDatePicker(fieldArr.id + '-' + compAttr.id, aliceForm.options.dateFormat, aliceForm.options.lang, setDateFormat);
                            } else if (fieldArr.type === 'timepicker') {
                                dateTimePicker.initTimePicker(fieldArr.id + '-' + compAttr.id, aliceForm.options.hourFormat, aliceForm.options.lang, setDateFormat);
                            } else if (fieldArr.type === 'datetimepicker') {
                                dateTimePicker.initDateTimePicker(fieldArr.id + '-' + compAttr.id, aliceForm.options.dateFormat, aliceForm.options.hourFormat, aliceForm.options.lang, setDateFormat);
                            }
                            break;
                        case 'customcode':
                            propertyValue = document.createElement('select');
                            propertyValue.classList.add('property-field-value');
                            for (let i = 0, len = customCodeList.length; i < len; i++) {
                                let customCode = customCodeList[i];
                                let propertyOption = document.createElement('option');
                                propertyOption.value = customCode.customCodeId;
                                propertyOption.text = customCode.customCodeName;
                                if (fieldArr.value === customCode.customCodeId) {
                                    propertyOption.setAttribute('selected', 'selected');
                                }
                                propertyValue.appendChild(propertyOption);
                            }
                            //첫번째 커스텀 코드를 저장
                            if (fieldArr.value === '' && customCodeList.length > 0) {
                                changePropertiesValue(customCodeList[0].customCodeId, group, fieldArr.id);
                            }

                            propertyValue.addEventListener('change', function() {
                                changePropertiesValue(this.value, group, fieldArr.id);
                            }, false);
                            fieldGroupDiv.appendChild(propertyValue);
                            break;
                        case 'checkbox-boolean':
                            propertyValue = document.createElement('input');
                            propertyValue.setAttribute('type', 'checkbox');
                            propertyValue.classList.add('property-field-value');
                            propertyValue.name = fieldArr.id;
                            propertyValue.checked = fieldArr.value;
                            propertyValue.addEventListener('change', function(e) {
                                changePropertiesValue(e.target.checked, group, fieldArr.id);
                            }, false);
                            fieldGroupDiv.appendChild(propertyValue);
                            break;
                        case 'image':
                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.setAttribute('value', fieldArr.value);
                            validateCheck(propertyValue, fieldArr.validate);
                            propertyValue.addEventListener('change', function() {
                                changePropertiesValue(this.value, group, fieldArr.id);
                            }, false);
                            fieldGroupDiv.appendChild(propertyValue);

                            let propertyBtn = document.createElement('button');
                            propertyBtn.type = 'button';
                            propertyBtn.innerText = 'select';
                            propertyBtn.addEventListener('click', function(e) {
                                window.open('/forms/imageUpload/' + id + '/view', 'imageUploadPop', 'width=1200, height=700');
                            }, false);
                            fieldGroupDiv.appendChild(propertyBtn);
                            break;
                    }
                });
            }
        });
    }

    /**
     * 우측 properties panel 삭제한다.
     */
    function hideComponentProperties() {
        if (selectedComponentId !== '') {
            propertiesPanel.innerHTML = '';
            //기존 선택된 컴포넌트 css 삭제
            document.getElementById(selectedComponentId).classList.remove('selected');
            selectedComponentId = '';
        }
    }
    /**
     * 우측 properties panel에 폼 세부 속성 출력한다.
     *
     * @param {String} elemId 선택한 element Id
     */
    function showFormProperties(elemId) {
        propertiesPanel.innerHTML = '';

        //기존 선택된 컴포넌트 css 삭제
        document.querySelectorAll('.component').forEach(function(comp) {
            comp.classList.remove('selected');
        });

        if (typeof elemId !== 'undefined' && elemId !== '') {
            if (!document.getElementById(elemId).classList.contains('selected')) {
                document.getElementById(elemId).classList.add('selected'); //현재 선택된 컴포넌트 css 추가
            }
        } else {
            selectedComponentId = '';
        }
        let formAttr = editor.data.form;
        let detailAttr = formProperties.form;

        //data + 기본 속성 = 세부 속성 재할당 
        Object.keys(formAttr).forEach(function(form) {
            Object.keys(detailAttr).forEach(function(idx) {
                if (form === detailAttr[idx].id) {
                    detailAttr[idx].value = formAttr[form];
                }
            });
        });

        let formOriginAttr = JSON.parse(JSON.stringify(formAttr));

        //폼 속성 출력
        let groupDiv = document.createElement('div');
        groupDiv.setAttribute('id', 'form');
        groupDiv.classList.add('property-group');
        groupDiv.textContent = 'form';
        propertiesPanel.appendChild(groupDiv);

        Object.keys(detailAttr).forEach(function(idx) {
            let fieldArr = detailAttr[idx];
            let fieldGroupDiv = document.createElement('div');
            fieldGroupDiv.classList.add('property-field');
            fieldGroupDiv.setAttribute('id', fieldArr.id);
            groupDiv.appendChild(fieldGroupDiv);

            let propertyName = document.createElement('span');
            propertyName.classList.add('property-field-name');
            propertyName.textContent = fieldArr.name;
            fieldGroupDiv.appendChild(propertyName);

            let propertyValue = null;
            if (fieldArr.type === 'textarea') {
                propertyValue = document.createElement('textarea');
                propertyValue.value = fieldArr.value;
            } else if (fieldArr.type === 'select') { //상태값 출력
                propertyValue = document.createElement('select');
                for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                    let propertyOption = document.createElement('option');
                    propertyOption.value = fieldArr.option[i].id;
                    propertyOption.text = fieldArr.option[i].name;
                    if (fieldArr.value === fieldArr.option[i].id) {
                        propertyOption.setAttribute('selected', 'selected');
                    }
                    propertyValue.appendChild(propertyOption);
                }
            } else {
                propertyValue = document.createElement('input');
                propertyValue.setAttribute('type', 'text');
                propertyValue.setAttribute('value', fieldArr.value);
            }
            propertyValue.classList.add('property-field-value');
            if (fieldArr.id === 'name') {
                validateCheck(propertyValue, fieldArr.validate);
                propertyValue.addEventListener('keyup', function(e) {
                    editor.data.form.name = this.value;
                    history.saveHistory([{0: formOriginAttr, 1: JSON.parse(JSON.stringify(editor.data.form))}]);
                });
            } else {
                validateCheck(propertyValue, fieldArr.validate);
                propertyValue.addEventListener('change', function(e) {
                    editor.data.form[fieldArr.id] = this.value;
                    history.saveHistory([{0: formOriginAttr, 1: JSON.parse(JSON.stringify(editor.data.form))}]);
                }, false);
            }
            fieldGroupDiv.appendChild(propertyValue);

            if (fieldArr.type === 'inputbox-readonly') {
                propertyValue.classList.add('noline');
                propertyValue.setAttribute('readonly', 'true');
            }
        });
    }

    /**
     * 조회된 데이터 draw.
     *
     * @param {Object} data 조회한 폼 및 컴포넌트 정보
     */
    function drawForm(data) {
        editor.data = JSON.parse(data);
        if (editor.data.components.length > 0) {
            if (editor.data.components.length > 2) {
                editor.data.components.sort(function (a, b) { //컴포넌트 재정렬
                    return a.display.order < b.display.order ? -1 : a.display.order > b.display.order ? 1 : 0;
                });
            }
            //데이터로 전달받은 컴포넌트 속성과 기본 속성을 merge한 후 컴포넌트 draw
            for (let i = 0, len = editor.data.components.length; i < len; i ++) {
                let componentAttr = editor.data.components[i];
                let defaultComponentAttr = component.getData(componentAttr.type);
                let mergeComponentAttr = aliceJs.mergeObject(defaultComponentAttr, componentAttr);
                setComponentData(mergeComponentAttr);

                component.draw(componentAttr.type, formPanel, mergeComponentAttr);
            }
        }
        //모든 컴포넌트를 그린 후 마지막에 editbox 추가
        let editboxComponent = component.draw(defaultComponent, formPanel);
        setComponentData(editboxComponent.attr);
        savedData = JSON.parse(JSON.stringify(editor.data));

        //폼 상세 정보 출력
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/formAttribute.json',
            callbackFunc: function(xhr) {
                formProperties = JSON.parse(xhr.responseText);
                //첫번째 컴포넌트 선택
                const firstComponent = document.getElementById('panel-form').querySelectorAll('.component')[0];
                if (firstComponent.getAttribute('data-type') === defaultComponent) {
                    firstComponent.querySelector('[contenteditable=true]').focus();
                }
                showComponentProperties(firstComponent.id);
            },
            contentType: 'application/json; charset=utf-8'
        });

        isEdited = false;
        //폼 이름 출력
        changeFormName();
    }

    /**
     * 폼 이름 변경.
     */
    function changeFormName() {
        document.querySelector('.form-name').textContent = (isEdited ? '*' : '') + editor.data.form.name;
    }

    /**
     * init.
     *
     * @param {String} formId 폼 아이디
     * @param {String} flag view 모드 = false, edit 모드 = true
     */
    function init(formId, flag) {
        console.info('form editor initialization. [FORM ID: ' + formId + ']');
        formPanel = document.getElementById('panel-form');
        formPanel.setAttribute('data-readonly', true);

        if (flag === 'true') { isView = false; }

        propertiesPanel = document.getElementById('panel-properties');
        //컨텍스트 메뉴 초기화
        context.init();

        //단축키 초기화 및 등록
        shortcut.init();
        for (let i = 0; i < shortcuts.length; i++) {
            shortcut.add(shortcuts[i].keys, shortcuts[i].command);
        }

        //load custom-code list.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/custom-codes',
            callbackFunc: function(xhr) {
                customCodeList = JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });

        // load form data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/forms/' + formId + '/data',
            callbackFunc: function(xhr) {
                let responseObject = JSON.parse(xhr.responseText);
                responseObject.components = reformatCalendarFormat('read', responseObject.components);
                drawForm(JSON.stringify(responseObject));
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * 날짜와 관련있는 컴포넌트들에 대해서 사용자의 타임존과 출력 포맷에 따라 변환.
     * 폼 디자이너 모듈 Refactoring 시까지 임시로 사용할 가능성이 있음.
     *
     * @author Jung Hee Chan
     * @since 2020-05-22
     * @param {String} action save, read 중에서 1개. save인 경우는 시스템 공통 포맷으로, read인 경우 사용자 포맷으로 변환.
     * @param {Object} components 변환 대상이 되는 컴포넌트 목록.
     * @return {Object} resultComponents 변경된 결과
     */
    function reformatCalendarFormat(action, components) {
        if (action !== 'save' && action !== 'read') {
            return components;
        }

        components.forEach(function(component, idx) {
            if (component.type === 'datetime' || component.type === 'date' || component.type === 'time') {
                // 1. 기본값 타입 중에서 직접 Calendar로 입력한 값인 경우는 변환
                if (!(component.display.default.indexOf('picker') < 0)) {
                    let displayDefaultValueArray = component.display.default.split('|'); // 속성 값을 파싱한 배열
                    if (action === 'save') {
                        switch(component.type) {
                            case 'datetime':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToSystemDatetimeFormatWithTimezone(displayDefaultValueArray[1],
                                        aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                break;
                            case 'date':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToSystemDateFormat(displayDefaultValueArray[1],
                                        aliceForm.options.dateFormat);
                                break;
                            case 'time':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToSystemTimeFormat(displayDefaultValueArray[1],
                                        aliceForm.options.hourFormat);
                                break;
                        }
                    } else if (action === 'read') {
                        switch(component.type) {
                            case 'datetime':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToUserDatetimeFormatWithTimezone(displayDefaultValueArray[1],
                                        aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                break;
                            case 'date':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToUserDateFormat(displayDefaultValueArray[1],
                                        aliceForm.options.dateFormat);
                                break;
                            case 'time':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToUserTimeFormat(displayDefaultValueArray[1],
                                        aliceForm.options.hourFormat);
                                break;
                        }

                    }
                    components[idx].display.default = displayDefaultValueArray.join('|');
                }

                // 2. validate 용 date-min, date-max 변환
                let validateItems = component.validate;
                Object.keys(validateItems).forEach(function(validateItem) {
                    if (!(validateItem.indexOf('date-') < 0)) {
                        let validateItemValueArray = validateItems[validateItem].split('|'); // 속성 값을 파싱한 배열
                        if (action === 'save') {
                            switch(component.type) {
                                case 'datetime':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToSystemDatetimeFormatWithTimezone(validateItemValueArray[0],
                                            aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                    break;
                                case 'date':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToSystemDateFormat(validateItemValueArray[0],
                                            aliceForm.options.dateFormat);
                                    break;
                                case 'time':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToSystemDateFormat(validateItemValueArray[0],
                                            aliceForm.options.hourFormat);
                                    break;
                            }
                        } else if (action === 'read') {
                            switch(component.type) {
                                case 'datetime':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToUserDatetimeFormatWithTimezone(validateItemValueArray[0],
                                            aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                    break;
                                case 'date':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToUserDateFormat(validateItemValueArray[0],
                                            aliceForm.options.dateFormat);
                                    break;
                                case 'time':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToUserTimeFormat(validateItemValueArray[0],
                                            aliceForm.options.hourFormat);
                                    break;
                            }
                        }
                        components[idx].validate[validateItem] = validateItemValueArray.join('|')
                    }
                })
            }
        })
        return components;
    }

    exports.init = init;
    exports.save = saveForm;
    exports.saveAs = saveAsForm;
    exports.undo = undoForm;
    exports.redo = redoForm;
    exports.preview = previewForm;
    exports.exportForm = exportForm;
    exports.importForm = exportForm;
    exports.addComponent = addComponent;
    exports.copyComponent = copyComponent;
    exports.deleteComponent = deleteComponent;
    exports.selectFirstComponent = selectFirstComponent;
    exports.selectLastComponent = selectLastComponent;
    exports.selectUpComponent = selectUpComponent;
    exports.selectDownComponent = selectDownComponent;
    exports.reorderComponent = reorderComponent;
    exports.addEditboxUp = addEditboxUp;
    exports.addEditboxDown = addEditboxDown;
    exports.getComponentIndex = getComponentIndex;
    exports.setComponentData = setComponentData;
    exports.showFormProperties = showFormProperties;
    exports.showComponentProperties = showComponentProperties;
    exports.hideComponentProperties = hideComponentProperties;
    exports.selectProperties = selectProperties;
    exports.history = history;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
