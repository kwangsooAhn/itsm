/**
* @projectDescription Form Desigener Library
*
* @author woodajung
* @version 1.0
* @sdoc js/form/formEditor.menu.js
* @sdoc js/form/component.js
*/
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.formEditor = global.formEditor || {})));
}(this, (function (exports) {
    'use strict';
    
    const defaultComponent = 'editbox';
    let data = {};
    
    /**
     * 컴포넌트 신규 추가
     *
     * @param type 컴포넌트 타입
     * @param componentId 컴포넌트 Id
     * @param attrs 컴포넌트 세부 속성
     */
    function addComponent(type, componentId, attrs) {
        attrs = attrs || {};
        if (Object.keys(attrs).length === 0 && JSON.stringify(attrs) === JSON.stringify({})) {
            attrs.isNew = true; //신규 추가
        } else {
            attrs.isNew = false; //수정
        }
        let comp = component.add({type: type, attrs: attrs, componentId: componentId, isFocus: true});
        if (comp !== null && type !== defaultComponent) {
            addEditbox(comp.id, 'down');
        }
    }
    
    /**
     * 컴포넌트 복사
     */
    function copyComponent() {
        //TODO: 컴포넌트 복사 후 재정렬
    }
    /**
     * 컴포넌트 삭제
     */
    function removeComponent() {
        //TODO: 컴포넌트 삭제 후 재정렬
    }

    /**
     * elemId 선택한 element Id를 기준으로 위, 아래 editbox 추가 후 data의 display order 변경
     *
     * @param elemId 선택한 element Id
     * @param direction 방향 (up, down)
     */
    function addEditbox(elemId, direction) {
        let elem = document.getElementById(elemId);
        if (elem === null) { return; }
        let editbox = component.add({type: defaultComponent, isFocus: false});
        let elemIdx = Number(elem.getAttribute('data-index'));
        let tempCompData = formEditor.data.components.slice();
        
        if (direction === 'up') {
            let compElems = document.querySelectorAll('.component');
            let editboxHTML = editbox.innerHTML;
            for (let i = compElems.length - 1; i >= elemIdx - 1; i--) {
                let cur = compElems[i];
                let prev = compElems[i - 1];
                if (i > (elemIdx - 1) && cur.getAttribute('data-type') !== prev.getAttribute('data-type')) {
                    cur.innerHTML = prev.innerHTML;
                    cur.setAttribute('data-type', prev.getAttribute('data-type'));
                    formEditor.data.components[i] = tempCompData[i - 1];
                    formEditor.data.components[i].id = cur.id;
                    formEditor.data.components[i].display.order = (i + 1);
                }
                if (i == (elemIdx - 1)) {
                    cur.innerHTML = editboxHTML;
                    cur.setAttribute('data-type', defaultComponent);
                    formEditor.data.components[i] = { id: cur.id, type: defaultComponent, display: { order: (i + 1) } };
                    
                    cur.querySelector('.group').focus();
                }

            }
        } else {
            if ((elemIdx + 1) !== Number(editbox.getAttribute('data-index'))) { //마지막에 추가시 컴포넌트 순서 재정렬할 필요 없음
                let compElems = document.querySelectorAll('.component');
                let editboxHTML = editbox.innerHTML;
                for (let i = compElems.length - 1; i >= elemIdx; i--) {
                    let cur = compElems[i];
                    let prev = compElems[i - 1];
                    if (cur.getAttribute('data-type') !== prev.getAttribute('data-type') && i > elemIdx) {
                        cur.innerHTML = prev.innerHTML;
                        cur.setAttribute('data-type', prev.getAttribute('data-type'));
                        formEditor.data.components[i] = tempCompData[i - 1];
                        formEditor.data.components[i].id = cur.id;
                        formEditor.data.components[i].display.order = (i + 1);
                    }
                    if (i === elemIdx) {
                        cur.innerHTML = editboxHTML;
                        cur.setAttribute('data-type', defaultComponent);
                        formEditor.data.components[i] = { id: cur.id, type: defaultComponent, display: { order: (i + 1) } };
                        
                        cur.querySelector('.group').focus();
                    }
                }
            } else {
                editbox.querySelector('.group').focus();
            }
        }
    }
    /**
     * 폼 저장
     */
    function saveForm() {

        //dummy data
        var formInfo = {
            id: '40288ab27051cb31017051cfcd9c0002',
            name: 'test',
            desc: 'zzzzzz'
        };
        var collections = [
            {
                id: '4a417b48be2e4ebe82bf8f80a63622a4',
                type: 'text',
                label: {
                    position: 'left',
                    column: 2,
                    size: 12,
                    color: '#000000',
                    bold: 'Y',
                    italic: 'N',
                    underline: 'Y',
                    align: 'left',
                    text: '텍스트'
                },
                display: {
                    placeholder: '텍스트를 입력하세요.',
                    column: 10,
                    'outline-width': 1,
                    'outline-color': '#000000',
                    order: 2
                },
                validate: {
                    required: 'Y',
                    regexp: 'none',
                    'regexp-msg': '',
                    'length-min': 0,
                    'length-max': 10
                }
            },
            {
                id: '4a417b48be2e4ebe82bf8f80a63622a4',
                type: 'select',
                label: {
                    position: 'top',
                    column: 2,
                    size: 14,
                    color: '#150dff',
                    bold: 'N',
                    italic: 'Y',
                    underline: 'N',
                    align: 'left',
                    text: 'Select Box'
                },
                display: {
                    column: 10,
                    order: 1
                },
                option: [
                    { seq: 1, name: 'ITSM팀', value: 'itsm' },
                    { seq: 2, name: '인프라웹팀', value: 'infraweb' }
                ],
                validate: {
                    required: 'N'
                }
            }
        ];
        var data = {
            form: formInfo,
            collections: collections
        };

        console.log(JSON.stringify(data));

        aliceJs.sendXhr({
            method: 'PUT',
            url: '/rest/forms/data',
            callbackFunc: function(xhr) {
                if (xhr.responseText) {
                    alert(i18n.get('common.msg.save'));
                } else {
                    alert(i18n.get('common.label.fail'));
                }
            },
            contentType: 'application/json; charset=utf-8',
            params: JSON.stringify(data)
        });
    }

    /**
     * 작업 취소
     */
    function undoForm() {
        //TODO: 작업 취소
    }

    /**
     * 작업 재실행
     */
    function redoForm() {
        //TODO: 작업 재실행
    }
    
    /**
     * 미리보기
     */
    function previewForm() {
        //TODO: 미리보기
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
     * 컴포넌트 ID를 전달 받아서 상세 데이터 조회
     *
     * @param id 컴포넌트 id
     * @return {Object} component 상세 데이터
     */
    function getComponentData(id) {
        for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
            let comp = formEditor.data.components[i];
            if (comp.id === id) { return comp; }
        }
        return null;
    }

    /**
     * 컴포넌트 데이터 추가/수정
     *
     * @param compData 컴포넌트 데이터
     */
    function setComponentData(compData) {
        let isExist = false;
        for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
            let comp = formEditor.data.components[i];
            if (comp.id === compData.id) {//수정
                formEditor.data.components[i] = compData;
                isExist = true;
                break;
            }
        }
        if (!isExist) {//추가
            formEditor.data.components.push(compData);
        }
    }
    
    /**
     * 조회된 데이터로 form designer draw 
     * 
     * @param data 문서 정보
     */
    function drawForm(data) {
        console.debug(JSON.parse(data));
        //TODO: 컴포넌트 재정렬
        
        formEditor.data = JSON.parse(data);
        if (formEditor.data.components.length > 0 ) {
            for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
                let comp = formEditor.data.components[i];
                addComponent(comp.type, comp.id, comp);
            }
        } else {
            addComponent(defaultComponent);
        }

        document.querySelector('.form-name').textContent = formEditor.data.form.name;
    }
    
    /**
     * form designer 초기화
     *
     * @param formId 폼 아이디
     */
    function init(formId) {
        console.info('form editor initialization. [FORM ID: ' + formId + ']');

        workflowUtil.polyfill();
        component.init();
        context.init();

        // load form data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/forms/data/' + formId,
            callbackFunc: function(xhr) {
                drawForm(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }
    
    exports.init = init;
    exports.save = saveForm;
    exports.undo = undoForm;
    exports.redo = redoForm;
    exports.preview = previewForm;
    exports.exportform = exportForm;
    exports.importform = exportForm;
    exports.addComponent = addComponent;
    exports.copyComponent = copyComponent;
    exports.removeComponent = removeComponent;
    exports.addEditbox = addEditbox;
    exports.getComponentData = getComponentData;
    exports.setComponentData = setComponentData;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
