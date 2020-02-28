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
    
    let propertiesPanel = null,
        selectedComponentId = '', //선택된 컴포넌트 ID 
        data = {};
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
                    alert('저장되었습니다.');
                } else {
                    alert('저장실패');
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
     * 컴포넌트 신규 추가
     *
     * @param type 컴포넌트 타입
     * @param componentId 컴포넌트 Id
     */
    function addComponent(type, componentId) {
        if (type !== undefined) { //기존 editbox를 지운후, 해당 컴포넌트 추가
            let elem = document.getElementById(componentId);
            let removeComp = component.draw(type);
            let compAttr = removeComp.attr;
            compAttr.id = componentId;
            compAttr.display.order = Number(elem.getAttribute('data-index'));
            setComponentData(compAttr);
            elem.innerHTML = removeComp.domElem.innerHTML;
            removeComp.domElem.remove();
            
            let compIdx = component.getLastIndex();
            component.setLastIndex(compIdx - 1);
            addEditboxDown(componentId);
        } else {
            let editbox = component.draw(defaultComponent);
            setComponentData(editbox.attr);
            editbox.domElem.querySelector('[contenteditable=true]').focus();
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
     * elemId 선택한 element Id를 기준으로 위에 editbox 추가 후 data의 display order 변경
     *
     * @param elemId 선택한 element Id
     */
    function addEditboxUp(elemId) {
        let elem = document.getElementById(elemId);
        if (elem === null) { return; }
        
        let elemIdx = Number(elem.getAttribute('data-index'));
        let editbox = component.draw(defaultComponent);
        setComponentData(editbox.attr);
        elem.parentNode.insertBefore(editbox.domElem, elem);
        editbox.domElem.setAttribute('data-index', elemIdx);
        editbox.domElem.setAttribute('tabIndex', elemIdx);
        
        //신규 추가된 editbox 컴포넌트 아래에 존재하는 컴포넌트들 순서 재정렬
        let lastCompIndex = component.getLastIndex();
        formEditor.data.components[lastCompIndex - 1].display.order = elemIdx;
        for (let i = elem.parentNode.children.length - 1; i >= elemIdx; i--) {
            let childNode = elem.parentNode.children[i];
            childNode.setAttribute('data-index', lastCompIndex);
            childNode.setAttribute('tabIndex', lastCompIndex);
            
            //데이터 display 순서 변경
            for (let j = 0, len = formEditor.data.components.length; j < len; j++) {
                let comp = formEditor.data.components[j];
                if (comp.id === childNode.id) { 
                    comp.display.order = lastCompIndex;
                    break;
                }
            }
            lastCompIndex--;
        }
    }

    /**
     * elemId 선택한 element Id를 기준으로 아래에 editbox 추가 후 data의 display order 변경
     *
     * @param elemId 선택한 element Id
     */
    function addEditboxDown(elemId) {
        let elem = document.getElementById(elemId);
        if (elem === null) { return; }
        
        let elemIdx = Number(elem.getAttribute('data-index'));
        let editbox = null;
        if (elem.nextSibling !== null) {
            editbox = component.draw(defaultComponent);
            setComponentData(editbox.attr);
            elem.parentNode.insertBefore(editbox.domElem, elem.nextSibling);
            editbox.domElem.setAttribute('data-index', elemIdx + 1);
            editbox.domElem.setAttribute('tabIndex', elemIdx + 1);
            
            //신규 추가된 editbox 컴포넌트 아래에 존재하는 컴포넌트들 순서 재정렬
            let lastCompIndex = component.getLastIndex();
            formEditor.data.components[lastCompIndex - 1].display.order = elemIdx + 1;
            
            for (let i = elem.parentNode.children.length - 1; i > elemIdx; i--) {
                let childNode = elem.parentNode.children[i];
                childNode.setAttribute('data-index', lastCompIndex);
                childNode.setAttribute('tabIndex', lastCompIndex);
                
                //데이터 display 순서 변경
                for (let j = 0, len = formEditor.data.components.length; j < len; j++) {
                    let comp = formEditor.data.components[j];
                    if (comp.id === childNode.id) { 
                        comp.display.order = lastCompIndex;
                        break;
                    }
                }
                lastCompIndex--
            }
        } else { //마지막에 추가된 경우 
            editbox = component.draw(defaultComponent);
            setComponentData(editbox.attr);
            elem.parentNode.appendChild(editbox.domElem);
        }
        
        if(editbox !== null) {
            editbox.domElem.querySelector('[contenteditable=true]').focus();
        }
    }
    
    /**
     * 컴포넌트 ID를 전달 받아서 일치하는 컴포넌트의 index 반환
     *
     * @param id 컴포넌트 id
     * @return {Integer} component index
     */
    function getComponentIndex(id) {
        for (let i = 0, len = formEditor.data.components.length; i < len; i++) {
            let comp = formEditor.data.components[i];
            if (comp.id === id) { return i; }
        }
        return -1;
    }

    /**
     * 컴포넌트 데이터 추가/수정
     *
     * @param compData 컴포넌트 데이터
     */
    function setComponentData(compData) {
        let isExist = false;
        for (let i = 0, len = formEditor.data.components.length; i < len; i++) {
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
     * 우측 properties panel 세부 속성 출력
     *
     * @param id 조회할 컴포넌트 ID
     */
    function showProperties(id) {
        if (selectedComponentId === id) { return; }
        
        selectedComponentId = id;
        
        let compIdx = getComponentIndex(id);
        if (compIdx === -1) { return; }
        
        let compAttr = formEditor.data.components[compIdx];
        let detailAttr = component.getDefaultAttribute(compAttr.type);

        //세부 속성 재할당 data로 전달된 속성 + 기본속성
        Object.keys(compAttr).forEach(function(comp) {
            if (compAttr[comp] !== null && typeof(compAttr[comp]) === 'object')  {
                if (detailAttr.hasOwnProperty(comp)) {
                    Object.keys(compAttr[comp]).forEach(function(attr) {
                        Object.keys(detailAttr[comp]).forEach(function(d) {
                            if (attr === detailAttr[comp][d].id) {
                                detailAttr[comp][d].value = compAttr[comp][attr];
                            }
                        });
                    });
                }
            }
        });

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
                    row.innerHTML = tb.lastElementChild.innerHTML;
                    tb.appendChild(row);
                });
                groupDiv.appendChild(plusButton);

                let minusButton = document.createElement('button');
                minusButton.classList.add('minus');
                minusButton.addEventListener('click', function() { //옵션 삭제 버튼
                    let tb = this.parentNode.querySelector('table');
                    let rowCount = tb.rows.length;
                    for (let i = 1; i < rowCount; i++) {
                        let row = tb.rows[i];
                        let chkbox = row.cells[0].childNodes[0];
                        if (chkbox.checked && rowCount > 2) {
                            tb.deleteRow(i);
                            rowCount--;
                            i--;
                        }
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
                        propertyValue = null,
                        colorPaletteDiv = null
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
                    switch (fieldArr.type) {
                        case 'inputbox':
                        case 'inputbox-underline':
                            propertyName = document.createElement('span');
                            propertyName.classList.add('property-field-name');
                            propertyName.textContent = fieldArr.name;
                            fieldGroupDiv.appendChild(propertyName);
                            
                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.setAttribute('value', fieldArr.value);
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
                            propertyName = document.createElement('span');
                            propertyName.classList.add('property-field-name');
                            propertyName.textContent = fieldArr.name;
                            fieldGroupDiv.appendChild(propertyName);
                            
                            propertyValue = document.createElement('select');
                            propertyValue.classList.add('property-field-value');
                            for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                let propertyOption = document.createElement('option');
                                propertyOption.value = fieldArr.option[i].id;
                                propertyOption.text = fieldArr.option[i].name;
                                propertyValue.appendChild(propertyOption);
                            }
                            fieldGroupDiv.appendChild(propertyValue);
                            break;
                        case 'slider':
                            propertyName = document.createElement('span');
                            propertyName.classList.add('property-field-name');
                            propertyName.textContent = fieldArr.name;
                            fieldGroupDiv.appendChild(propertyName);
                            
                            propertyValue = document.createElement('input');
                            propertyValue.setAttribute('id', group + '-' + fieldArr.id);
                            propertyValue.setAttribute('type', 'range');
                            propertyValue.setAttribute('min', 0);
                            propertyValue.setAttribute('max', 12);
                            propertyValue.setAttribute('value', fieldArr.value);
                            fieldGroupDiv.appendChild(propertyValue);
                            propertyValue.addEventListener('change', function() {
                                let slider = document.getElementById(this.id + '-value');
                                slider.value = this.value;
                            });
                            
                            let slideValue = document.createElement('input');
                            slideValue.classList.add('property-field-value', 'underline');
                            slideValue.setAttribute('id', group + '-' + fieldArr.id + '-value');
                            slideValue.setAttribute('type', 'text');
                            slideValue.setAttribute('value', fieldArr.value);
                            fieldGroupDiv.appendChild(slideValue);
                            break;
                        case 'rgb':
                            propertyName = document.createElement('span');
                            propertyName.classList.add('property-field-name');
                            propertyName.textContent = fieldArr.name;
                            fieldGroupDiv.appendChild(propertyName);
                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value', 'underline');
                            propertyValue.setAttribute('id', group + '-' + fieldArr.id + '-value');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.setAttribute('value', fieldArr.value);
                            propertyValue.setAttribute('readonly', true);
                            fieldGroupDiv.appendChild(propertyValue);
                            colorPaletteDiv = document.createElement('div');
                            colorPaletteDiv.setAttribute('id', group + '-' + fieldArr.id + '-colorPalette');
                            colorPaletteDiv.classList.add('color-palette');
                            groupDiv.appendChild(colorPaletteDiv);
                            colorPalette.initColorPalette(propertyValue, colorPaletteDiv)
                            break;
                        case 'radio':
                            propertyName = document.createElement('span');
                            propertyName.classList.add('property-field-name');
                            propertyName.textContent = fieldArr.name;
                            fieldGroupDiv.appendChild(propertyName);
                            
                            for (let i = 0, len = fieldArr.option.length; i < len; i++) {
                                let propertyOption = document.createElement('input');
                                propertyOption.setAttribute('type', 'radio');
                                propertyOption.setAttribute('id', fieldArr.name + '-' + fieldArr.option[i].id);
                                propertyOption.value = fieldArr.option[i].id;
                                propertyOption.name = fieldArr.name;
                                if (fieldArr.value === fieldArr.option[i].id) { 
                                    propertyOption.setAttribute('checked', 'checked');
                                }
                                fieldGroupDiv.appendChild(propertyOption);
                                let propertyLabel = document.createElement('label');
                                propertyLabel.setAttribute('for', fieldArr.name + '-' + fieldArr.option[i].id);
                                propertyLabel.textContent = fieldArr.option[i].name;
                                fieldGroupDiv.appendChild(propertyLabel);
                            }
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
                                    if (this.classList.contains('active')) {
                                        this.classList.remove('active');
                                        this.setAttribute('data-value', 'N');
                                    } else {
                                        this.classList.add('active');
                                        this.setAttribute('data-value', 'Y');
                                    }
                                });
                            }
                            break;
                        case 'table':
                            for (let i = 0, len = fieldArr.items.length; i < len; i+=3) {
                                if (i === 0) {
                                    let headerRow = document.createElement('tr');
                                    groupTb.appendChild(headerRow);
                                    
                                    let headerCell = document.createElement('th');
                                    headerRow.appendChild(headerCell);
                                    for (let j = i; j < i + 3; j++) {
                                        headerCell = document.createElement('th');
                                        headerCell.innerHTML = fieldArr.items[j].name;
                                        headerRow.appendChild(headerCell);
                                    }
                                }
                                let row = document.createElement('tr');
                                groupTb.appendChild(row);
                                
                                let cell = document.createElement('td');
                                let chkbox = document.createElement('input');
                                chkbox.setAttribute('type', 'checkbox');
                                cell.appendChild(chkbox);
                                row.appendChild(cell);
                                
                                for (let j = i; j < i + 3; j++) {
                                    cell = document.createElement('td');
                                    cell.setAttribute('id', fieldArr.items[j].id);
                                    let inputCell = document.createElement('input');
                                    inputCell.setAttribute('type', 'text');     
                                    inputCell.setAttribute('value', fieldArr.items[j].value);
                                    cell.appendChild(inputCell);
                                    row.appendChild(cell);
                                }
                            }
                            break;
                    }
                });
            }
        });
    }
    
    /**
     * 우측 properties panel 삭제
     */
    function hideProperties() {
        propertiesPanel.innerHTML = '';
        selectedComponentId = '';
        
    }
    /**
     * 조회된 데이터로 form designer draw 
     * 
     * @param data 문서 정보
     */
    function drawForm(data) {
        console.debug(JSON.parse(data));
        formEditor.data = JSON.parse(data);
        
        //TODO. 폼 상세 정보 출력
        
        if (formEditor.data.components.length > 0 ) {
            formEditor.data.components.sort(function (a, b) { //컴포넌트 재정렬
                return a.display.order < b.display.order ? -1 : a.display.order > b.display.order ? 1 : 0;  
            });
            //데이터로 전달된 컴포넌트 draw
            for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
                let compData = formEditor.data.components[i];
                component.draw(compData.type, compData);
            }
        }
        //모든 컴포넌트를 그린 후 마지막에 editbox 추가
        let editbox = component.draw(defaultComponent);
        setComponentData(editbox.attr);
        editbox.domElem.querySelector('[contenteditable=true]').focus();

        document.querySelector('.form-name').textContent = formEditor.data.form.name;
    }
    
    /**
     * form designer 초기화
     *
     * @param formId 폼 아이디
     */
    function init(formId) {
        console.info('form editor initialization. [FORM ID: ' + formId + ']');
        propertiesPanel = document.getElementById('panel-properties');
        
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
    exports.addEditboxUp = addEditboxUp;
    exports.addEditboxDown = addEditboxDown;
    exports.getComponentIndex = getComponentIndex;
    exports.setComponentData = setComponentData;
    exports.showProperties = showProperties;
    exports.hideProperties = hideProperties;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
