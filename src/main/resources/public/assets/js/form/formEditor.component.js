(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.component = global.component || {})));
}(this, (function (exports) {
    'use strict';
    
    const defaultColWidth = 8.33,  //폼 패널을 12등분하였을때, 1개의 너비
          defaultPlaceholder= '+ Typing for add component',
          compTitleProperties = [  //세부속성에서 사용할 제목
              {'type': 'text', 'name': 'Text', 'icon': ''},
              {'type': 'textarea', 'name': 'Text Box', 'icon': ''},
              {'type': 'select', 'name': 'Dropdown', 'icon': ''},
              {'type': 'radio', 'name': 'Radio Button', 'icon': ''},
              {'type': 'checkbox', 'name': 'Checkbox', 'icon': ''},
              {'type': 'label', 'name': 'Label', 'icon': ''},
              {'type': 'image', 'name': 'Image', 'icon': ''},
              {'type': 'line', 'name': 'Line', 'icon': ''},
              {'type': 'date', 'name': 'Date', 'icon': ''},
              {'type': 'time', 'name': 'Time', 'icon': ''},
              {'type': 'datetime', 'name': 'Date Time', 'icon': ''},
              {'type': 'fileupload', 'name': 'Fileupload', 'icon': ''}
          ];

    let formPanel = null,
        propertiesPanel = null,
        lastComponentId = 0,       //마지막으로 추가된 컴포넌트 ID
        componentIdx = 0,          //컴포넌트 index = 출력 순서 생성시 사용
        selectedComponentId = '',  //선택된 컴포넌트 ID 
        defaultAttributes = {};
    
    /**
     * 현재 시간을 2020-02-19 13:30 형식으로 추출 > date, time, datetime 컴포넌트 사용 용도
     */
    function getTimeStamp() {
        let today = new Date();
        let s = parseZero(today.getFullYear(), 4) + '-' +
                parseZero(today.getMonth() + 1, 2) + '-' +
                parseZero(today.getDate(), 2) + ' ' +
                parseZero(today.getHours(), 2) + ':' +
                parseZero(today.getMinutes(), 2);
        return s;
    }
    /**
     * 시분초에 length가 변경될 경우 0 붙이는 함수
     */
    function parseZero(n, digits) {
        let zero = '';
        n = n.toString();
        if (n.length < digits) {
          for (let i = 0; i < (digits - n.length); i++)
            zero += '0';
        }
        return zero + n;
    }
    /**
     * 템플릿 리터럴 문자열을 전달받아서 element 생성
     *
     * @method createElement
     * @param template 템플릿 리터럴
     * @return elem 생성된 element
     */
    function getElementByTemplate(template) {
        let elem = document.createElement('div');
        elem.classList.add('group');
        elem.innerHTML = template;
        return elem;
    }
    
    /**
     * 컴포넌트 추가
     *
     * @param options 옵션 {component id, component 세부 속성, component 타입(*)}
     */
    function addComponent(options) {
        options = options || {};
        
        let elem = null;
        if (options.componentId !== undefined) {
            elem = document.getElementById(options.componentId);
        }
        let obj = { type: options.type, display: {} };
        if (elem) { //editbox 삭제 후 컴포넌트 추가
            elem.removeChild(elem.childNodes[1]);
            obj.id = options.componentId;
            let detailAttr = component.defaultAttributes[options.type];
            Object.keys(detailAttr).forEach(function(pAttr) {
                if (pAttr === 'option') {
                    let optionArray = [];
                    for (let i = 0, len = detailAttr[pAttr][0].items.length; i < len; i+=3) {
                        let optionAttr = {};
                        for (let j = i; j < i + 3; j++) {
                            let attr = detailAttr[pAttr][0].items[j];
                            optionAttr[attr.id] = attr.value;
                        }
                        optionArray.push(optionAttr);
                    }
                    obj[pAttr] = optionArray;
                } else {
                    obj[pAttr] = {};
                    Object.keys(detailAttr[pAttr]).forEach(function(attr) {
                        obj[pAttr][detailAttr[pAttr][attr].id] = detailAttr[pAttr][attr].value;
                    });
                }
            });
            obj.display.order = Number(elem.getAttribute('data-index'));
            formEditor.setComponentData(obj);
        } else {
            elem = document.createElement('div');
            elem.classList.add('component');
            let compId = (options.componentId !== undefined) ? options.componentId  : workflowUtil.generateUUID();
            lastComponentId = compId;
            elem.setAttribute('id', compId);
            elem.setAttribute('data-index', (++componentIdx));
            if (options.componentId === undefined) {
                obj.id = compId;
                obj.display.order = componentIdx;
                formEditor.setComponentData(obj);
            }
            if (options.attrs !== undefined) { obj = options.attrs; }
            let img = document.createElement('img'); 
            img.classList.add('move-icon');
            elem.appendChild(img);
            formPanel.appendChild(elem);
        }
        elem.setAttribute('data-type', options.type);
        if (obj.option !== undefined && obj.option.length > 1) {
            obj.option.sort(function (a, b) { //현재 객체 배열을 정렬
                return a.seq < b.seq ? -1 : a.seq > b.seq ? 1 : 0;  
            });
        }
        let comp = null;
        switch (options.type) {
            case 'text':
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' placeholder='${obj.display.placeholder}' readonly 
                        style='border-color: ${obj.display["outline-color"]}; border-width: ${obj.display["outline-width"]}px;' 
                        ${obj.validate.required === "Y" ? "required" : ""} 
                        max-length=${obj.validate["length-max"]} min-length=${obj.validate["length-min"]} />
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'textarea':
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <textarea placeholder='${obj.display.placeholder}' rows='${obj.display.rows}' readonly 
                        style='border-color: ${obj.display["outline-color"]}; border-width: ${obj.display["outline-width"]}px;' 
                        ${obj.validate.required === "Y" ? "required" : ""}></textarea>
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'select':
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <select></select></div>`);
                let selectbox = comp.querySelector('select');
                for (let i = 0, len = obj.option.length; i < len; i++) {
                    let option = document.createElement('option');
                    option.value = obj.option[i].value + '-' + obj.option[i].seq;
                    option.text = obj.option[i].name;
                    selectbox.appendChild(option);
                }
                elem.appendChild(comp);
                break;
            case 'radio':
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;' id='radio'></div>`);
                let radio = comp.querySelector('#radio');
                radio.classList.add(obj.display.direction);
                for (let i = 0, len = obj.option.length; i < len; i++) {
                    let option = document.createElement('div');
                    option.classList.add('field-radio');
                    if (obj.display.direction === 'horizontal') { option.style.display = 'inline-block'; }
                    radio.appendChild(option);
                    
                    let optionRadio = document.createElement('input');
                    optionRadio.setAttribute('type', 'radio');
                    optionRadio.setAttribute('id', obj.option[i].value + '-' + obj.option[i].seq);
                    optionRadio.value = obj.option[i].value;
                    optionRadio.name = obj.option[i].name;
                    
                    if (i === 0) { optionRadio.setAttribute('checked', 'checked'); }
                    
                    let optionLabel = document.createElement('label');
                    optionLabel.setAttribute('for', obj.option[i].value + '-' + obj.option[i].seq);
                    optionLabel.textContent = obj.option[i].name;
                    
                    if (obj.display.position === 'left') {
                        option.appendChild(optionLabel);
                        option.appendChild(optionRadio);
                    } else {
                        option.appendChild(optionRadio);
                        option.appendChild(optionLabel);
                    }
                }
                elem.appendChild(comp);
                break;
            case 'checkbox':
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;' id='chkbox'></div>`);
                let chkbox = comp.querySelector('#chkbox');
                chkbox.classList.add(obj.display.direction);
                for (let i = 0, len = obj.option.length; i < len; i++) {
                    let option = document.createElement('div');
                    option.classList.add('field-checkbox');
                    if (obj.display.direction === 'horizontal') { option.style.display = 'inline-block'; }
                    chkbox.appendChild(option);
                    
                    let optionChk = document.createElement('input');
                    optionChk.setAttribute('type', 'checkbox');
                    optionChk.setAttribute('id', obj.option[i].value + '-' + obj.option[i].seq);
                    optionChk.value = obj.option[i].value;
                    optionChk.name = obj.option[i].name;
                    
                    if (i === 0) { optionChk.setAttribute('checked', 'checked'); }
                    
                    let optionLabel = document.createElement('label');
                    optionLabel.setAttribute('for', obj.option[i].value + '-' + obj.option[i].seq);
                    optionLabel.textContent = obj.option[i].name;
                    
                    if (obj.display.position === 'left') {
                        option.appendChild(optionLabel);
                        option.appendChild(optionChk);
                    } else {
                        option.appendChild(optionChk);
                        option.appendChild(optionLabel);
                    }
                }
                elem.appendChild(comp);
                break;
            case 'label':
                comp = getElementByTemplate(`
                    <div class='field' style='flex-basis: 100%;'>
                        <div class='label'style='color: ${obj.display.color}; font-size: ${obj.display.size}px; text-align: ${obj.display.align}; 
                        ${obj.display.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.display.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.display.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.display.text}</div>
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'image':
                comp = getElementByTemplate(`
                    <div class='field' style='flex-basis: 100%;'>
                        <img src='${obj.display.path}' alt='' width='${obj.display.width}' height='${obj.display.height}'>
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'line':
                comp = getElementByTemplate(`
                    <div class='field' style='flex-basis: 100%;'>
                        <hr style='border: '${obj.display.type} ${obj.display.width}px  ${obj.display.color};'>
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'date':
                let defaultDate = obj.display['default'];
                if (defaultDate === 'today') { 
                    defaultDate = getTimeStamp(); 
                    defaultDate = defaultDate.split(' ')[0];
                }
                defaultDate = changeDateFormatYYYYMMDD(defaultDate, obj.display.format);
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='date-${obj.id}' placeholder='${obj.display.format}' value='${defaultDate}'/>
                    </div>`);
                elem.appendChild(comp);
                dateTimePicker.initDatePicker('date-' + obj.id, obj.display.format);
                break;
            case 'time': 
                let defaultTime = obj.display['default'];
                if (defaultTime === 'now') { 
                    defaultTime = getTimeStamp(); 
                    defaultTime = changeDateFormatYYYYMMDD(defaultTime, 'yyyy-MM-dd ' + obj.display.format);
                    defaultTime = defaultTime.split(' ')[1];
                }
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='time-${obj.id}' placeholder='${obj.display.format}' value='${defaultTime}'/>
                    </div>`);
                elem.appendChild(comp);
                dateTimePicker.initTimePicker('time-' + obj.id, obj.display.format);
                break;
            case 'datetime':
                let defaultDateTime = obj.display['default'];
                if (defaultDateTime === 'now') { 
                    defaultDateTime = getTimeStamp();
                }
                defaultDateTime = changeDateFormatYYYYMMDD(defaultDateTime, obj.display.format);
                comp = getElementByTemplate(`
                    <div class='field'>
                        <div class='label' style='color: ${obj.label.color}; font-size: ${obj.label.size}px; text-align: ${obj.label.align}; 
                        ${obj.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${obj.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${obj.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${obj.label.text}
                            <span class='required' style='${obj.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='datetime-${obj.id}' placeholder='${obj.display.format}' value='${defaultDateTime}' />
                    </div>`);
                elem.appendChild(comp);
                dateTimePicker.initDateTimePicker('datetime-' + obj.id, obj.display.format);
                break;
            case 'fileupload':
                comp = getElementByTemplate(`
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='file' name='files[]' multiple />
                    </div>`);
                elem.appendChild(comp);
                break;
            case 'editbox':
                comp = document.createElement('div');
                comp.classList.add('group');
                comp.setAttribute('contenteditable', 'true');
                comp.setAttribute('placeholder', defaultPlaceholder);
                elem.appendChild(comp);
                break;
            default:
                console.info('Component does not exist.');
        }
        if (obj.label !== undefined) {
            if (obj.label.position === 'hidden') {
                comp.firstElementChild.style.display = 'none';
            } else if (obj.label.position === 'left') {
                comp.firstElementChild.style.flexBasis = (defaultColWidth * Number(obj.label.column)) + '%';
                comp.lastElementChild.style.flexBasis = (defaultColWidth * Number(obj.display.column)) + '%';
            }
        }
        comp.setAttribute('tabIndex', elem.getAttribute('data-index'));
        if (options.isFocus) {
            comp.focus();
        }
        return elem;
    }
    /**
     * 우측 properties panel 세부 속성 출력
     *
     * @param id 조회할 컴포넌트 ID
     */
    function showPropertiesPanel(id) {
        if (selectedComponentId === id) { return; }
        
        selectedComponentId = id;
        let compAttr = formEditor.getComponentData(id);
        if (compAttr === null) { return; }
        let detailAttr = component.defaultAttributes[compAttr.type];
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
        //세부 속성 출력
        setComponentTitle(compAttr.type);
        Object.keys(detailAttr).forEach(function(group) {
            let groupDiv = document.createElement('div');
            groupDiv.setAttribute('id', group);
            groupDiv.classList.add('property-group');
            groupDiv.textContent = group;
            propertiesPanel.appendChild(groupDiv);
            
            let buttonExist = false,
                fieldButtonDiv = null,
                groupTb = null;
            if (group === 'option') {
                let plusButton = document.createElement('button');
                plusButton.classList.add('plus');
                plusButton.addEventListener('click', function() { //옵션 추가
                    let tb = this.parentNode.querySelector('table');
                    let row = document.createElement('tr');
                    row.innerHTML = tb.lastElementChild.innerHTML;
                    tb.appendChild(row);
                });
                groupDiv.appendChild(plusButton);
                let minusButton = document.createElement('button');
                minusButton.classList.add('minus');
                minusButton.addEventListener('click', function() { //옵션 삭제
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
                        colorPaletteDiv = null;
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
                            //TODO colorpicker 추가 예정
                            propertyName
                            propertyName = document.createElement('span');
                            propertyName.classList.add('property-field-name');
                            propertyName.textContent = fieldArr.name;
                            fieldGroupDiv.appendChild(propertyName);

                            propertyValue = document.createElement('input');
                            propertyValue.classList.add('property-field-value', 'underline');
                            propertyValue.setAttribute('id', group + '-' + fieldArr.id + '-value');
                            propertyValue.setAttribute('type', 'text');
                            propertyValue.setAttribute('value', fieldArr.value);
                            fieldGroupDiv.appendChild(propertyValue);
                            colorPaletteDiv = document.createElement('span');
                            colorPaletteDiv.setAttribute('id', group + '-' + fieldArr.id + '-colorPalette')
                            fieldGroupDiv.appendChild(colorPaletteDiv);
                            colorPalette.initPalette(colorPaletteDiv.getAttribute('id'))
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
    function hidePropertiesPanel() {
        propertiesPanel.innerHTML = '';
        selectedComponentId = '';
    }
    /**
     * properties panel에 컴포넌트 제목 출력
     * 
     * @param type 컴포넌트 타입
     */
    function setComponentTitle(type) {
        let search = {};
        for (let i = 0, len = compTitleProperties.length; i < len; i++) {
            let prop = compTitleProperties[i];
            if (prop.type === type) {
                search = prop;
                break;
            }
        }
        let compTitle = document.createElement('div');
        compTitle.classList.add('property-title');
        compTitle.textContent = search.name;
        //TODO: 제목 icon 추가
        propertiesPanel.appendChild(compTitle);
    }
    /**
     * 선택된 컴포넌트 ID 조회
     */
    function getSelectedComponentId() {
        return selectedComponentId;
    }
    /**
     * 선택된 컴포넌트 ID 초기화
     *
     * @param id 컴포넌트 ID
     */
    function setSelectedComponentId(id) {
        selectedComponentId = id;
    }
    /**
     * 마지막 추가된 컴포넌트 ID 조회
     */
    function getLastComponentId() {
        return lastComponentId;
    }
    /**
     * 컴포넌트 기본 속성 조회 : '/assets/js/form/componentAttribute.json'
     */
    function loadAttribute(data) {
        component.defaultAttributes = JSON.parse(data);
    }
    /**
     * 컴포넌트 초기화
     */
    function init() {
        formPanel = document.getElementById('panel-form');
        propertiesPanel = document.getElementById('panel-property');
        
        //load component default data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentAttribute.json',
            callbackFunc: function(xhr) {
                loadAttribute(xhr.responseText)
            },
            contentType: 'application/json; charset=utf-8'
        });
    }
    
    exports.init = init;
    exports.add = addComponent;
    exports.setSelectedComponentId = setSelectedComponentId;
    exports.getSelectedComponentId = getSelectedComponentId;
    exports.getLastComponentId = getLastComponentId;
    exports.showPropertiesPanel = showPropertiesPanel;
    exports.hidePropertiesPanel = hidePropertiesPanel;
    
    Object.defineProperty(exports, '__esModule', { value: true });
})));
