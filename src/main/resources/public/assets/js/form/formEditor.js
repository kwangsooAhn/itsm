/**
* @projectDescription Form Designer Library
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
        data = {}, //저장용 데이터
        formProperties = {}, //좌측 properties panel에 출력되는 폼 정보
        userData = { //사용자 정보
            defaultLang: 'en',
            defaultDateFormat: 'YYYY-MM-DD',
            defaultTimeFormat: '24'
        };
    /**
     * 폼 저장
     */
    function saveForm() {
        data = JSON.parse(JSON.stringify(formEditor.data));
        let lastCompIndex = component.getLastIndex();
        data.components = data.components.filter(function(comp) { 
            return !(comp.display.order === lastCompIndex && comp.type === defaultComponent); 
        });
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
        let url = '/forms/' + formEditor.data.form.id + '/preview';
        const specs = 'left=0,top=0,menubar=no,toolbar=no,location=no,status=no,titlebar=no,scrollbars=yes,resizable=no';
        window.open(url, 'result', 'width=1500,height=920,' + specs);

        let form = document.createElement('form');
        form.action = url;
        form.method = 'POST';
        form.target = 'result';
        let input = document.createElement('textarea');
        input.name = 'data';
        input.value = JSON.stringify(formEditor.data);
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
     *
     * @param type 컴포넌트 타입
     * @param componentId 컴포넌트 Id
     */
    function addComponent(type, componentId) {
        if (type !== undefined) { //기존 editbox를 지운후, 해당 컴포넌트 추가
            let elem = document.getElementById(componentId);
            let replaceComp = component.draw(type);
            let compAttr = replaceComp.attr;
            compAttr.id = componentId;
            compAttr.display.order = Number(elem.getAttribute('data-index'));
            setComponentData(compAttr);
            
            replaceComp.domElem.id = componentId;
            replaceComp.domElem.setAttribute('data-index', compAttr.display.order);
            replaceComp.domElem.setAttribute('tabIndex', compAttr.display.order);
            elem.parentNode.insertBefore(replaceComp.domElem, elem);
            elem.remove();
            
            let compIdx = component.getLastIndex();
            component.setLastIndex(compIdx - 1);
            addEditboxDown(componentId);
        } else {
            let editbox = component.draw(defaultComponent);
            setComponentData(editbox.attr);
            editbox.domElem.querySelector('[contenteditable=true]').focus();
            showComponentProperties(editbox.id);
        }
    }

    /**
     * 컴포넌트 복사
     *
     * @param elemId 선택한 element Id
     */
    function copyComponent(elemId) {
        let elem = document.getElementById(elemId);
        if (elem === null) { return; }

        //복사
        let elemIdx = Number(elem.getAttribute('data-index')) + 1;
        for (let i = 0; i < formEditor.data.components.length; i++) {
            if (elemId === formEditor.data.components[i].id) {
                let copyData = JSON.parse(JSON.stringify(formEditor.data.components[i]));
                copyData.id = workflowUtil.generateUUID();
                let comp = component.draw(copyData.type, copyData);
                setComponentData(comp.attr);
                elem.parentNode.insertBefore(comp.domElem, elem.nextSibling);
                comp.domElem.setAttribute('data-index', elemIdx);
                comp.domElem.setAttribute('tabIndex', elemIdx);
                if (copyData.type === 'editbox') {
                    comp.domElem.querySelector('[contenteditable=true]').focus();
                }
                showComponentProperties(comp.id);
                break;
            }
        }
        //재정렬
        let lastCompIdx = component.getLastIndex();
        formEditor.data.components[lastCompIdx - 1].display.order = elemIdx;
        reorderComponent(elem, elemIdx, lastCompIdx);
    }

    /**
     * 컴포넌트 삭제
     *
     * @param elemId 선택한 element Id
     */
    function deleteComponent(elemId) {
        let elem = document.getElementById(elemId);
        if (elem === null) { return; }

        //재정렬
        let elemIdx = Number(elem.getAttribute('data-index'));
        let lastCompIdx = component.getLastIndex() - 1;
        component.setLastIndex(lastCompIdx);
        reorderComponent(elem, elemIdx, lastCompIdx);
        //삭제
        elem.remove();
        for (let i = 0; i < formEditor.data.components.length; i++) {
            if (elemId === formEditor.data.components[i].id) {
                formEditor.data.components.splice(i, 1);
                break;
            }
        }
        //컴포넌트 없을 경우 editbox 컴포넌트 신규 추가.
        if (document.querySelectorAll('.component').length === 0) {
            let editbox = component.draw(defaultComponent);
            setComponentData(editbox.attr);
            editbox.domElem.querySelector('[contenteditable=true]').focus();
            showComponentProperties(editbox.id);
        }
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
        let lastCompIdx = component.getLastIndex();
        formEditor.data.components[lastCompIdx - 1].display.order = elemIdx;
        reorderComponent(elem, elemIdx, lastCompIdx);

        if(editbox !== null) {
            editbox.domElem.querySelector('[contenteditable=true]').focus();
            showComponentProperties(editbox.id);
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

        let elemIdx = Number(elem.getAttribute('data-index')) + 1;
        let editbox = null;
        if (elem.nextSibling !== null) {
            editbox = component.draw(defaultComponent);
            setComponentData(editbox.attr);
            elem.parentNode.insertBefore(editbox.domElem, elem.nextSibling);
            editbox.domElem.setAttribute('data-index', elemIdx);
            editbox.domElem.setAttribute('tabIndex', elemIdx);

            //신규 추가된 editbox 컴포넌트 아래에 존재하는 컴포넌트들 순서 재정렬
            let lastCompIdx = component.getLastIndex();
            formEditor.data.components[lastCompIdx - 1].display.order = elemIdx;
            reorderComponent(elem, elemIdx, lastCompIdx);
        } else { //마지막에 추가된 경우
            editbox = component.draw(defaultComponent);
            setComponentData(editbox.attr);
            elem.parentNode.appendChild(editbox.domElem);
        }

        if(editbox !== null) {
            editbox.domElem.querySelector('[contenteditable=true]').focus();
            showComponentProperties(editbox.id);
        }
    }

    /**
     * 컴포넌트 재정렬
     *
     * @param elem 선택한 element
     * @param elemIdx 선택한 element data index
     * @param lastCompIdx 컴포넌트 last index
     */
    function reorderComponent(elem, elemIdx, lastCompIdx) {
        for (let i = elem.parentNode.children.length - 1; i >= elemIdx; i--) {
            let childNode = elem.parentNode.children[i];
            childNode.setAttribute('data-index', lastCompIdx);
            childNode.setAttribute('tabIndex', lastCompIdx);
            //데이터 display 순서 변경
            for (let j = 0, len = formEditor.data.components.length; j < len; j++) {
                let comp = formEditor.data.components[j];
                if (comp.id === childNode.id) {
                    comp.display.order = lastCompIdx;
                    break;
                }
            }
            lastCompIdx--;
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
    function showComponentProperties(id) {
        if (selectedComponentId === id) { return false; }
        propertiesPanel.innerHTML = '';
        //기존 선택된 컴포넌트 css 삭제
        if (selectedComponentId !== '') {
            document.getElementById(selectedComponentId).classList.remove('selected');
        }
        
        selectedComponentId = id;
        //현재 선택된 컴포넌트 css 추가
        document.getElementById(id).classList.add('selected');
        
        let compIdx = getComponentIndex(id);
        if (compIdx === -1) { return false; }
        
        let compAttr = formEditor.data.components[compIdx];
        let detailAttr = JSON.parse(component.getDefaultAttribute(compAttr.type));
        /**
         * 컴포넌트를 다시 그린다.
         */
        const redrawComponent = function() {
            const originDisplayOrder = compAttr.display.order;
            let element = component.draw(compAttr.type, compAttr);
            if (element) {
                let compAttr = element.attr;
                compAttr.id = id;
                compAttr.display.order = originDisplayOrder;
                setComponentData(compAttr);
                
                let targetElement = document.getElementById(id);
                element.domElem.id = id;
                element.domElem.setAttribute('data-index', originDisplayOrder);
                element.domElem.setAttribute('tabIndex', originDisplayOrder);
                
                targetElement.parentNode.insertBefore(element.domElem, targetElement);
                targetElement.remove();
                
                let compIdx = component.getLastIndex();
                component.setLastIndex(compIdx - 1);
                
                element.domElem.classList.add('selected');
            }
        }

        /**
         * 변경된 값을 컴포넌트 속성 정보에 반영하고, 컴포넌트를 다시 그린다.
         *
         * @param {String} value 변경된 값
         * @param {String} group 변경된 그룹 key
         * @param {String} field 변경된 field key
         * @param {Number} index 변경된 index (그룹이 option 일 경우만 해당되므로 생략가능)
         */
        const changePropertiesValue = function(value, group, field, index) {
            if (typeof index === 'undefined') {
                compAttr[group][field] = value;
            } else {
                compAttr[group][index][field] = value;
            }
            redrawComponent();
        };
        /**
         * date, time, datetime default 포멧 변경시,
         * default 값을 none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등으로 저장한다.
         */
        const changeDateFormat = function(e) {
            let el = e.target || e;
            let parentEl = e.target ? el.parentNode : el.parentNode.parentNode;
            if (parentEl.classList.contains('property-field')) {
                let changePropertiesArr = el.name.split('.');
                changePropertiesValue(el.value, changePropertiesArr[0], changePropertiesArr[1]);
            } else {
                let checkedRadio = parentEl.parentNode.querySelector('input[type=radio]:checked');
                if (parentEl.firstElementChild.id !== checkedRadio.id) { return false; }
                
                let checkedPropertiesArr = checkedRadio.name.split('.');
                let changeValue = checkedRadio.value;
                
                if (checkedRadio.value === 'none' || checkedRadio.value === 'now') {
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

        //세부 속성 재할당 data로 전달된 속성 + 기본속성
        Object.keys(compAttr).forEach(function(comp) {
            if (compAttr[comp] !== null && typeof(compAttr[comp]) === 'object' && detailAttr.hasOwnProperty(comp))  {
                Object.keys(compAttr[comp]).forEach(function(attr) {
                    Object.keys(detailAttr[comp]).forEach(function(d) {
                        if (attr === detailAttr[comp][d].id) {
                            detailAttr[comp][d].value = compAttr[comp][attr];
                        }
                    });
                });
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
                    }
                    switch (fieldArr.type) {
                        case 'inputbox':
                        case 'inputbox-underline':
                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.setAttribute('value', fieldArr.value);
                            propertyValue.addEventListener('change', function() {
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
                        case 'slider':
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
                                changePropertiesValue(this.value, group, fieldArr.id);
                            });
                            
                            let slideValue = document.createElement('input');
                            slideValue.classList.add('property-field-value', 'underline');
                            slideValue.setAttribute('id', group + '-' + fieldArr.id + '-value');
                            slideValue.setAttribute('type', 'text');
                            slideValue.setAttribute('value', fieldArr.value);
                            fieldGroupDiv.appendChild(slideValue);
                            break;
                        case 'rgb':
                            let selectedColorBox = document.createElement('span');
                            selectedColorBox.classList.add('selected-color');
                            selectedColorBox.style.backgroundColor = fieldArr.value;
                            fieldGroupDiv.appendChild(selectedColorBox);
                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value', 'underline');
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
                            fieldGroupDiv.innerHTML = propertyTemplate;
                            
                            //이벤트 등록
                            let changeOptions = fieldGroupDiv.querySelectorAll('input[type="radio"], input[type="text"]');
                            for (let i = 0, len = changeOptions.length; i < len; i++ ) {
                                changeOptions[i].addEventListener('change', changeDateFormat, false);
                            }
                            
                            if (compAttr.type === 'date') {
                                dateTimePicker.initDatePicker('datepicker-' + compAttr.id, userData.defaultDateFormat, userData.defaultLang, changeDateFormat);
                            } else if (compAttr.type === 'time') {
                                dateTimePicker.initTimePicker('timepicker-' + compAttr.id, userData.defaultTimeFormat, changeDateFormat);
                            } else if (compAttr.type === 'datetime') {
                                dateTimePicker.initDateTimePicker('datetimepicker-' + compAttr.id, userData.defaultDateFormat, userData.defaultTimeFormat, userData.defaultLang, changeDateFormat);
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
                            propertyValue.setAttribute('value', fieldArr.value);
                            fieldGroupDiv.appendChild(propertyValue);
                            
                            if (fieldArr.type === 'datepicker') {
                                dateTimePicker.initDatePicker(fieldArr.id + '-' + compAttr.id, userData.defaultDateFormat, userData.defaultLang, changeDateFormat);
                            } else if (fieldArr.type === 'timepicker') {
                                dateTimePicker.initTimePicker(fieldArr.id + '-' + compAttr.id, userData.defaultTimeFormat, changeDateFormat);
                            } else if (fieldArr.type === 'datetimepicker') {
                                dateTimePicker.initDateTimePicker(fieldArr.id + '-' + compAttr.id, userData.defaultDateFormat, userData.defaultTimeFormat, userData.defaultLang, changeDateFormat);
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
    function hideComponentProperties() {
        if (selectedComponentId !== '') {
            propertiesPanel.innerHTML = '';
            //기존 선택된 컴포넌트 css 삭제
            document.getElementById(selectedComponentId).classList.remove('selected');
            selectedComponentId = '';
        }
    }
    /**
     * 우측 properties panel에 폼 세부 속성 출력
     */
    function showFormProperties() {
        if (selectedComponentId === '') { return false; }
        propertiesPanel.innerHTML = '';
        //기존 선택된 컴포넌트 css 삭제
        document.getElementById(selectedComponentId).classList.remove('selected');
        selectedComponentId = '';
        
        let formAttr = formEditor.data.form;
        let detailAttr = formProperties.form;
        
        //data + 기본 속성 = 세부 속성 재할당 
        Object.keys(formAttr).forEach(function(form) {
            Object.keys(detailAttr).forEach(function(idx) {
                if (form === detailAttr[idx].id) {
                    detailAttr[idx].value = formAttr[form];
                }
            });
        });
        
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
                propertyValue.addEventListener('keyup', function(e) {
                    formEditor.data.form.name = this.value;
                    document.querySelector('.form-name').textContent = this.value;
                });
            } else {
                propertyValue.addEventListener('change', function(e) {
                    formEditor.data.form[fieldArr.id] = this.value;
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
     * 데이터로 전달받은 속성과 기본 속성 merge
     *
     * @param compData 컴포넌트 데이터
     */
    function mergeComponentData(compData) {
        let compAttr = {};
        let defaultAttr = JSON.parse(component.getDefaultAttribute(compData.type));
        Object.keys(defaultAttr).forEach(function(group) {
            if (group === 'option') { //옵션 json 구조 변경
                let options = [];
                for (let i = 0, len = defaultAttr[group][0].items.length; i < len; i+=3) {
                   let option = {};
                   for (let j = i; j < i + 3; j++) {
                       let child = defaultAttr[group][0].items[j];
                       option[child.id] = child.value;
                   }
                   options.push(option);
                }
                compAttr[group] = options;
            } else {
                compAttr[group] = {};
                Object.keys(defaultAttr[group]).forEach(function(child) {
                    compAttr[group][defaultAttr[group][child].id] = defaultAttr[group][child].value;
                });
            }
        });
        compAttr.id = compData.id;
        compAttr.type = compData.type;
        
        Object.keys(compData).forEach(function(comp) {
            if (compData[comp] !== null && typeof(compData[comp]) === 'object' && compAttr.hasOwnProperty(comp))  {
                Object.keys(compData[comp]).forEach(function(attr) {
                    Object.keys(compAttr[comp]).forEach(function(d) {
                        if (attr === d) {
                            compAttr[comp][d] = compData[comp][attr];
                        }
                    });
                });
            }
        });
        compAttr.display.order = compData.display.order;
        setComponentData(compAttr);
        return compAttr;
    }
    
     /**
     * 조회된 데이터로 form designer draw 
     * 
     * @param data 문서 정보
     */
    function drawForm(data) {
        console.debug(JSON.parse(data));
        formEditor.data = JSON.parse(data);
        
        if (formEditor.data.components.length > 0 ) {
            formEditor.data.components.sort(function (a, b) { //컴포넌트 재정렬
                return a.display.order < b.display.order ? -1 : a.display.order > b.display.order ? 1 : 0;  
            });
            //데이터로 전달된 컴포넌트 draw
            for (let i = 0, len = formEditor.data.components.length; i < len; i ++) {
                let compData = formEditor.data.components[i];
                //기본속성과 merge 작업
                let mergeData = mergeComponentData(compData);
                component.draw(mergeData.type, mergeData);
            }
        }
        //모든 컴포넌트를 그린 후 마지막에 editbox 추가
        let editbox = component.draw(defaultComponent);
        setComponentData(editbox.attr);
        editbox.domElem.querySelector('[contenteditable=true]').focus();
        
        //TODO. 폼 상세 정보 출력
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/formAttribute.json',
            callbackFunc: function(xhr) {
                formProperties = JSON.parse(xhr.responseText);
                showComponentProperties(editbox.id);
            },
            contentType: 'application/json; charset=utf-8'
        });

        document.querySelector('.form-name').textContent = formEditor.data.form.name;
    }
    
    /**
     * form designer 초기화
     *
     * @param formId 폼 아이디
     * @param authInfo 사용자 세션 정보
     */
    function init(formId, authInfo) {
        console.info('form editor initialization. [FORM ID: ' + formId + ']');
        propertiesPanel = document.getElementById('panel-properties');
        let authData = JSON.parse(authInfo);
        
        //편집화면에서 사용할 dateformat 설정
        if (authData) {
            userData.defaultLang  = authData.lang;
            let format = authData.timeFormat;
            let formatArray = format.split(' ');
            
            userData.defaultDateFormat =  formatArray[0].toUpperCase();
            if (formatArray.length === 3) { userData.defaultTimeFormat = '12'; }
        }
        
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
    exports.deleteComponent = deleteComponent;
    exports.addEditboxUp = addEditboxUp;
    exports.addEditboxDown = addEditboxDown;
    exports.getComponentIndex = getComponentIndex;
    exports.setComponentData = setComponentData;
    exports.showFormProperties = showFormProperties;
    exports.showComponentProperties = showComponentProperties;
    exports.hideComponentProperties = hideComponentProperties;
    exports.reorderComponent = reorderComponent;
    exports.userData = userData;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
