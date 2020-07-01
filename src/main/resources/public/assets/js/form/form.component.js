/**
* @projectDescription Form Designer Component Library
*
* @author woodajung
* @version 1.0
*/
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.component = global.component || {})));
}(this, (function (exports) {
    'use strict';

    const componentTitles = [ //컴포넌트 명
            {'type': 'editbox', 'name': 'Edit Box', 'icon': ''},
            {'type': 'text', 'name': 'Text', 'icon': ''},
            {'type': 'textarea', 'name': 'Text Box', 'icon': ''},
            {'type': 'select', 'name': 'Dropdown', 'icon': ''},
            {'type': 'radio', 'name': 'Radio Button', 'icon': ''},
            {'type': 'checkbox', 'name': 'Checkbox', 'icon': ''},
            {'type': 'label', 'name': 'Label', 'icon': ''},
            {'type': 'image', 'name': 'Image', 'icon': ''},
            {'type': 'divider', 'name': 'Divider', 'icon': ''},
            {'type': 'date', 'name': 'Date', 'icon': ''},
            {'type': 'time', 'name': 'Time', 'icon': ''},
            {'type': 'datetime', 'name': 'Date Time', 'icon': ''},
            {'type': 'fileupload', 'name': 'File Upload', 'icon': ''},
            {'type': 'custom-code', 'name': 'Custom Code', 'icon': ''}
        ],
        editboxPlaceholder= '+ Typing \'/\' for add component',
        columnWidth = 8.33;  //폼 양식을 12등분 하였을 때, 1개의 너비
        
    let componentIdx = 0;          //컴포넌트 index = 출력 순서 생성시 사용

    const utils = {
        /**
         * 템플릿 리터럴 문자열을 전달받아서 컴포넌트 생성한다.
         *
         * @param {String} template 템플릿 리터럴
         * @return {Object} elem 생성된 컴포넌트 객체
         */
        createComponentByTemplate: function(template) {
            let elem = document.createElement('component');
            elem.classList.add('component');
            elem.innerHTML = template;
            return elem;
        }
    };
    /**
     * 우측 세부 속성창(properties panel)에 출력될 컴포넌트 제목 객체 조회
     * @param {String} type 컴포넌트 타입
     * @return {Object} match title 일치하는 제목 객제
     */
    function getTitle(type) {
        return componentTitles.filter(function(title) { return title.type === type; })[0];
    }

    /**
     * Editbox 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @constructor
     */
    function Editbox(target) {
        let comp = utils.createComponentByTemplate(`
            <div class='add-icon'></div>
            <div class='move-icon'></div>
            <div class='group' contenteditable='true' placeholder="${editboxPlaceholder}"></div>
        `);
        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Text 컴포넌트
     *
     * @param {Object} attr 컴포넌트 속성
     * @param {Object} target 컴포넌트가 추가될 대상
     * @constructor
     */
    function Text(attr, target) {
        let textDefaultArr = attr.display['default'].split('|');
        let textDefaultValue = (textDefaultArr[0] === 'none') ? textDefaultArr[1] : aliceForm.session[textDefaultArr[1]];
        if (target.hasAttribute('data-readonly')) { //폼 양식
            if (textDefaultArr[0] !== 'none') { textDefaultValue = textDefaultArr[2]; } //폼 양식 편집 화면에서는 세션 값이 출력되지 않는다.
        } else { //신청서 및 처리할 문서
            if (target.getAttribute('data-isToken') === 'true') { //처리할 문서
                textDefaultValue = attr.value;
            }
        }
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'  style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}</div>
                    <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                    <input type='text' placeholder='${attr.display.placeholder}' value='${textDefaultValue}'
                    ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''} max-length='${attr.validate.lengthMax}' maxlength='${attr.validate.lengthMax}'
                    min-length='${attr.validate.lengthMin}' regexp='${attr.validate.regexp}' regexp-msg='${attr.validate.regexpMsg}'/>
                </div>
            </div>
        `);

        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Text Box 컴포넌트
     *
     * @param {Object} attr 컴포넌트 속성
     * @param {Object} target 컴포넌트가 추가될 대상
     * @constructor
     */
    function Textarea(attr, target) {
        const defaultRowHeight = 26;
        const textEditorUseYn = attr.display.editorUseYn;
        const textEditorHeight = attr.display.rows !== '' ? Number(attr.display.rows) * defaultRowHeight : defaultRowHeight;
        let textAreaDefaultValue = '';
        //처리할 문서는 실 데이터를 출력한다.
        if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true') {
            textAreaDefaultValue = (textEditorUseYn && attr.value !== '') ? JSON.parse(attr.value) : attr.value;
        }
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}</div>
                    <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                ${textEditorUseYn ?
                    `<div style='width: 100%;'>
                        <div id='editor' class='editor-container'
                        style='height: ${textEditorHeight}px;' ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''}
                        max-length='${attr.validate.lengthMax}' min-length='${attr.validate.lengthMin}'></div>
                    </div>` :
                    `<textarea placeholder='${attr.display.placeholder}' rows='${attr.display.rows}' 
                    ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''} maxlength='${attr.validate.lengthMax}'
                    max-length='${attr.validate.lengthMax}' min-length='${attr.validate.lengthMin}'>${textAreaDefaultValue}</textarea>`
                }
                </div>
            </div>
        `);

        target.appendChild(comp);
        this.domElem = comp;

        if (textEditorUseYn) { //텍스트 에디터
            let textEditorOptions = {
                modules: {
                    toolbar: [
                        [{'header': [1, 2, 3, 4, 5, 6, false]}],
                        ['bold', 'italic', 'underline'],
                        [{'color': []}, {'background': []}],
                        [{'align': []}, { 'list': 'bullet' }],
                        ['image']
                    ]
                },
                placeholder: attr.display.placeholder,
                theme: 'snow',
                readOnly: (target.hasAttribute('data-readonly'))            //폼 양식 편집 화면에서는 editor를 편집할 수 없다.
            };
            let textEditorContainer = comp.querySelector('.editor-container');
            let textEditor = new Quill(textEditorContainer, textEditorOptions);
            textEditor.setContents(textAreaDefaultValue);
        }
    }

    /**
     * Dropdown 컴포넌트
     *
     * @param {Object} attr 컴포넌트 속성
     * @param {Object} target 컴포넌트가 추가될 대상
     * @constructor
     */
    function Selectbox(attr, target) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                    </div>
                    <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                    <select ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''}></select>
                </div>
            </div>
        `);
        let selectElem = comp.querySelector('select');
        for (let i = 0, len = attr.option.length; i < len; i++) {
            let optElem = document.createElement('option');
            optElem.value = attr.option[i].value;
            optElem.text = attr.option[i].name;
            optElem.setAttribute('seq', attr.option[i].seq);
            //처리할 문서는 실 데이터를 출력한다.
            if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true' && optElem.value === attr.value) {
                optElem.selected = true;
            }
            selectElem.appendChild(optElem);
        }

        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Radio Button 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Radiobox(attr, target) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                    </div>
                    <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;' id='radio' ${attr.dataAttribute.displayType === 'editableRequired' ? "required" : ""}></div>
            </div>
        `);
        let fieldLastEle = comp.querySelector('#radio');
        fieldLastEle.classList.add(attr.display.direction);
        for (let i = 0, len = attr.option.length; i < len; i++) {
            let divEle = document.createElement('div');
            divEle.classList.add('field-radio');
            if (attr.display.direction === 'horizontal') { divEle.style.display = 'inline-block'; }
            
            let radioElem = document.createElement('input');
            radioElem.type = 'radio';
            radioElem.id = attr.option[i].value;
            radioElem.value = attr.option[i].value;
            radioElem.name = attr.option[i].name;
            radioElem.setAttribute('seq', attr.option[i].seq);

            //처리할 문서는 실 데이터를 출력한다.
            if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true' && attr.value !== '') {
                radioElem.checked = (radioElem.value === attr.value);
            } else {
                radioElem.checked = (i === 0);
            }

            radioElem.addEventListener('click', function() {
                let checkedRadioElem = comp.querySelectorAll('input[type=radio]:checked');
                for (let j = 0, checkLen = checkedRadioElem.length; j < checkLen; j++) {
                    checkedRadioElem[j].checked = false;
                }
                this.checked = true;
            });
            
            let lblElem = document.createElement('label');
            lblElem.setAttribute('for', attr.option[i].value);
            lblElem.textContent = attr.option[i].name;
            
            if (attr.display.position === 'left') {
                divEle.appendChild(lblElem);
                divEle.appendChild(radioElem);
            } else {
                divEle.appendChild(radioElem);
                divEle.appendChild(lblElem);
            }
            fieldLastEle.appendChild(divEle);
        }

        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Checkbox 컴포넌트
     *
     * @param {Object} attr 컴포넌트 속성
     * @param {Object} target 컴포넌트가 추가될 대상
     * @constructor
     */
    function Checkbox(attr, target) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                    </div>
                    <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;' id='chkbox' ${attr.dataAttribute.displayType === 'editableRequired' ? "required" : ""}></div>
            </div>
        `);
        let fieldLastEle = comp.querySelector('#chkbox');
        fieldLastEle.classList.add(attr.display.direction);
        for (let i = 0, len = attr.option.length; i < len; i++) {
            let divEle = document.createElement('div');
            divEle.classList.add('field-checkbox');
            if (attr.display.direction === 'horizontal') { divEle.style.display = 'inline-block'; }
            
            let checkElem = document.createElement('input');
            checkElem.type = 'checkbox';
            checkElem.id = attr.option[i].value;
            checkElem.setAttribute('seq', attr.option[i].seq);
            checkElem.value = attr.option[i].value;
            checkElem.name = attr.option[i].name;

            //처리할 문서는 실 데이터를 출력한다.
            if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true' && attr.value !== '') {
                const checkboxValues = JSON.parse(attr.value);
                for (let j = 0, checkLen = checkboxValues.length; j < checkLen; j++) {
                    if (checkElem.value === checkboxValues[j]) {
                        checkElem.checked = true;
                    }
                }
            }
            
            let lblElem = document.createElement('label');
            lblElem.setAttribute('for', attr.option[i].value);
            lblElem.textContent = attr.option[i].name;
            
            if (attr.display.position === 'left') {
                divEle.appendChild(lblElem);
                divEle.appendChild(checkElem);
            } else {
                divEle.appendChild(checkElem);
                divEle.appendChild(lblElem);
            }
            fieldLastEle.appendChild(divEle);
        }

        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Label 컴포넌트
     *
     * @param {Object} attr 컴포넌트 속성
     * @param {Object} target 컴포넌트가 추가될 대상
     * @constructor
     */
    function Label(attr, target) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%; text-align: ${attr.display.align};'>
                    <div class='label'style='color: ${attr.display.color}; font-size: ${attr.display.size}px;
                    ${attr.display.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.display.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.display.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.display.text}</div>
                </div>
            </div>
        `);
        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Image 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Imagebox(attr, target) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%; text-align: ${attr.display.align};'>
                    <img src='${attr.display.path}' alt='' width='${attr.display.width}' height='${attr.display.height}'>
                </div>
            </div>
        `);
        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Divider 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Divider(attr, target) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%;'>
                    <hr style='border-top: ${attr.display.type} ${attr.display.thickness}px ${attr.display.color}; border-bottom-width: 0px;'>
                </div>
            </div>
        `);
        target.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Date 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function DateBox(attr, target) {
        //날짜 포멧 변경
        let dateDefaultArr = attr.display['default'].split('|');
        let dateDefault = '';
        //처리할 문서는 사용자 포멧에 맞게 변환된 실 데이터를 출력한다. (form.core.js 의 reformatCalendarFormat()에서 처리한 데이터)
        if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true') {
            dateDefault = attr.value;
        } else { //문서양식, 신청서는 default값 출력한다.
            switch(dateDefaultArr[0]) {
                case 'now':
                    dateDefault = i18n.getDate();
                    break;
                case 'date':
                    let offset = {};
                    offset.days =  aliceJs.isEmpty(dateDefaultArr[1]) ? 0 : Number(dateDefaultArr[1]);
                    dateDefault = i18n.getDate(offset);
                    break;
                case 'datepicker':
                    if (!aliceJs.isEmpty(dateDefaultArr[1])) {
                        dateDefault = dateDefaultArr[1];
                    }
                    break;
                default: //none
            }
        }
        let comp = utils.createComponentByTemplate(`
                <div class='move-icon'></div>
                <div class='group'>
                    <div class='field' style='text-align: ${attr.label.align};'>
                        <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; 
                        ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        </div>
                        <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='date-${attr.componentId}' placeholder='${i18n.dateFormat}' value='${dateDefault}' ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''} date-max='${attr.validate.dateMax}' date-min='${attr.validate.dateMin}'/>
                    </div>
                </div>
            `);

        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            dateTimePicker.initDatePicker('date-' + attr.componentId, i18n.dateFormat, i18n.lang, function () {
                aliceDocument.checkValidate(document.getElementById('date-' + attr.componentId));
            });
        }
    }

    /**
     * Time 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성성
     * @constructor
     */
    function TimeBox(attr, target) {
        //시간 포멧 변경
        let timeDefaultArr = attr.display['default'].split('|');
        let timeDefault = '';
        //처리할 문서는 사용자 포멧에 맞게 변환된 실 데이터를 출력한다. (form.core.js 의 reformatCalendarFormat()에서 처리한 데이터)
        if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true') {
            timeDefault = attr.value;
        } else {
            switch(timeDefaultArr[0]) {
                case 'now':
                    timeDefault = i18n.getTime();
                    break;
                case 'time':
                    let offset = {};
                    offset.hours =  aliceJs.isEmpty(timeDefaultArr[1]) ? 0 : Number(timeDefaultArr[1]);
                    timeDefault = i18n.getTime(offset);
                    break;
                case 'timepicker':
                    if (!aliceJs.isEmpty(timeDefaultArr[1])) {
                        timeDefault = timeDefaultArr[1];
                    }
                    break;
                default: //none
            }
        }
        let comp = utils.createComponentByTemplate(`
                <div class='move-icon'></div>
                <div class='group'>
                    <div class='field' style='text-align: ${attr.label.align};'>
                        <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                        ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        </div>
                        <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='time-${attr.componentId}' placeholder='${i18n.timeFormat}' value='${timeDefault}' ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''} time-max='${attr.validate.timeMax}' time-min='${attr.validate.timeMin}'/>
                    </div>
                </div>
            `);

        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            dateTimePicker.initTimePicker('time-' + attr.componentId, i18n.timeFormat, i18n.lang, function () {
                aliceDocument.checkValidate(document.getElementById('time-' + attr.componentId));
            });
        }
    }

    /**
     * Date Time 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function DatetimeBox(attr, target) {
        //날짜 시간 포멧 변경
        let datetimeDefaultArr = attr.display['default'].split('|');
        let datetimeDefault = '';
        //처리할 문서는 사용자 포멧에 맞게 변환된 실 데이터를 출력한다. (form.core.js 의 reformatCalendarFormat()에서 처리한 데이터)
        if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true') {
                datetimeDefault = attr.value;
        } else {
            switch(datetimeDefaultArr[0]) {
                case 'now':
                    datetimeDefault = i18n.getDateTime();
                    break;
                case 'datetime':
                    let offset = {};
                    offset.days =  aliceJs.isEmpty(datetimeDefaultArr[1]) ? 0 : Number(datetimeDefaultArr[1]);
                    offset.hours = aliceJs.isEmpty(datetimeDefaultArr[2]) ? 0 : Number(datetimeDefaultArr[2]);
                    datetimeDefault = i18n.getDateTime(offset);
                    break;
                case 'datetimepicker':
                    if (!aliceJs.isEmpty(datetimeDefaultArr[1])) {
                        datetimeDefault = datetimeDefaultArr[1];
                    }
                    break;
                default: //none
            }
        }
        let comp = utils.createComponentByTemplate(`
                <div class='move-icon'></div>
                <div class='group'>
                    <div class='field' style='text-align: ${attr.label.align};'>
                        <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                        ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        </div>
                        <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='datetime-${attr.componentId}' placeholder='${i18n.dateTimeFormat}' value='${datetimeDefault}' ${attr.displayType === 'editableRequired' ? 'required' : ''} 
                        datetime-max='${attr.validate.datetimeMax}' datetime-min='${attr.validate.datetimeMin}'/>
                    </div>
                </div>
            `);
        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            dateTimePicker.initDateTimePicker('datetime-' + attr.componentId, i18n.dateFormat, i18n.timeFormat, i18n.lang, function () {
                aliceDocument.checkValidate(document.getElementById('datetime-' + attr.componentId));
            });
        }
    }

    /**
     * Fileupload 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Fileupload(attr, target) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                    </div>
                    <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;' id='fileupload' ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''}>
                    <div id='dropZoneFiles-${attr.componentId}'></div> 
                    <div id='dropZoneUploadedFiles-${attr.componentId}' class='dropbox'></div>
                </div>
            </div>
        `);

        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            document.getElementById('dropZoneUploadedFiles-' + attr.componentId).classList.remove('dropbox');
            let fileOptions = {
                extra: {
                    formId: 'frm',
                    ownId: '',
                    dropZoneFilesId: 'dropZoneFiles-' + attr.componentId,
                    dropZoneUploadedFilesId: 'dropZoneUploadedFiles-' + attr.componentId,
                    editor: (attr.dataAttribute.displayType !== 'readonly')
                }
            };
            if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true') {
                fileOptions.extra.fileDataIds = attr.value;
            }
            fileUploader.init(fileOptions);
        } else {
            let fileUploadElem = comp.querySelector('.dropbox');
            fileUploadElem.textContent = 'Drop files here to upload';
            let buttonElem = document.createElement('button');
            buttonElem.type = 'button';
            buttonElem.innerText = 'ADD';
            fileUploadElem.parentNode.insertBefore(buttonElem, fileUploadElem);
        }
    }
    /**
     * Custom-Code 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function CustomCode(attr, target) {
        let textDefaultArr = attr.display['default'].split('|');
        let textDefaultValue = '',  // 폼 디자이너, 미리보기용
            defaultCustomData = ''; // 신청서 작성 및 처리할 문서
        if (!target.hasAttribute('data-readonly')) { // 신청서 작성 및 처리할 문서
            //처리할 문서는 실 데이터를 출력한다.
            if (target.hasAttribute('data-isToken') && target.getAttribute('data-isToken') === 'true') {
                defaultCustomData = attr.value;
            } else {  // 신청서 작성
                if (textDefaultArr[0] !== 'none') {
                    defaultCustomData = textDefaultArr[1] + '|' + textDefaultArr[2];
                    if (textDefaultArr[0] === 'session') {
                        switch (textDefaultArr[1]) {
                            case 'userName':
                                defaultCustomData = aliceForm.session.userKey;
                                break;
                            case 'department':
                                defaultCustomData = aliceForm.session.department;
                                break;
                        }
                        defaultCustomData += '|' + aliceForm.session[textDefaultArr[1]];
                    }
                }
            }
        } else {
            if (textDefaultArr[0] !== 'none') {
                textDefaultValue = textDefaultArr[2];
            }
        }
        console.debug('textDefaultValue: %s, defaultCustomData: %s', textDefaultValue, defaultCustomData);
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}</div>
                    <span class='required' style='${attr.dataAttribute.displayType === "editableRequired" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='display: flex; flex-basis: 100%;'>
                    <input type='text' id='custom-code' ${attr.dataAttribute.displayType === 'editableRequired' ? 'required' : ''} readonly custom-data='${defaultCustomData}' value="${textDefaultValue}"/>
                    <input type='button' id='codeBtn' value='${attr.display["buttonText"]}'>
                </div>
            </div>
        `);

        target.appendChild(comp);
        this.domElem = comp;
        if (!target.hasAttribute('data-readonly')) {
            let customCodeTextElem = comp.querySelector('input[type="text"]');
            if (defaultCustomData !== '') {
                let customDataValue = defaultCustomData.split('|');
                customCodeTextElem.value = (customDataValue.length > 1) ? customDataValue[1] : '';
            }

            let searchBtn = comp.querySelector('#codeBtn');
            searchBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                let customCodeData = {
                    componentId: attr.componentId,
                    componentValue: customCodeTextElem.getAttribute('custom-data')
                };
                const itemName = 'alice_custom-codes-search-' + attr.componentId;
                sessionStorage.setItem(itemName, JSON.stringify(customCodeData));
                let url = '/custom-codes/' + attr.display.customCode + '/search';
                window.open(url, itemName, 'width=500, height=655');
            });
        }
    }

    /**
     * 컴포넌트를 생성하고 출력한다.
     * @param {String} compType 컴포넌트 타입
     * @param {Object} compTarget 컴포넌트를 추가할 대상
     * @param {Object} compData 컴포넌트 데이터
     * @return {Object} 생성된 컴포넌트 객체
     */
    function draw(compType, compTarget, compData) {
        let compAttr = { 'display': {} },
            compId = '';

        if (compData !== undefined) { //기존 저장된 컴포넌트 속성이 존재할 경우
            compId = compData.componentId;
            compAttr = compData;
        } else {                     //신규 생성된 컴포넌트일 경우
            compId = workflowUtil.generateUUID();
            compAttr = getData(compType);
            compAttr.componentId = compId;
            compAttr.type = compType;
        }
        compAttr.display.order = ++componentIdx;
        let componentConstructor = null;
        switch(compType) {
            case 'editbox':
                componentConstructor = new Editbox(compTarget);
                break;
            case 'text':
                componentConstructor =  new Text(compAttr, compTarget);
                break;
            case 'textarea':
                componentConstructor =  new Textarea(compAttr, compTarget);
                break;
            case 'select':
                componentConstructor =  new Selectbox(compAttr, compTarget);
                break;
            case 'radio':
                componentConstructor =  new Radiobox(compAttr, compTarget);
                break;
            case 'checkbox':
                componentConstructor =  new Checkbox(compAttr, compTarget);
                break;
            case 'label':
                componentConstructor =  new Label(compAttr, compTarget);
                break;
            case 'image':
                componentConstructor =  new Imagebox(compAttr, compTarget);
                break;
            case 'divider':
                componentConstructor =  new Divider(compAttr, compTarget);
                break;
            case 'date':
                componentConstructor =  new DateBox(compAttr, compTarget);
                break;
            case 'time':
                componentConstructor =  new TimeBox(compAttr, compTarget);
                break;
            case 'datetime':
                componentConstructor =  new DatetimeBox(compAttr, compTarget);
                break;
            case 'fileupload':
                componentConstructor =  new Fileupload(compAttr, compTarget);
                break;
            case 'custom-code':
                componentConstructor =  new CustomCode(compAttr, compTarget);
                break;
            default:
                break;
        }
        
        if (componentConstructor) {
            componentConstructor.id = compId;
            componentConstructor.type = compType;
            componentConstructor.attr = compAttr;
            componentConstructor.domElem.setAttribute('id', compId);
            componentConstructor.domElem.setAttribute('data-type', compType);
            componentConstructor.domElem.setAttribute('data-index', getLastIndex());
            componentConstructor.domElem.setAttribute('tabIndex', getLastIndex());

            if (typeof compAttr.dataAttribute !== 'undefined') {
                if (compAttr.dataAttribute.displayType === 'readonly') {
                    componentConstructor.domElem.setAttribute('data-readonly', true);
                } else if (compAttr.dataAttribute.displayType === 'hidden') {
                    componentConstructor.domElem.style.display = 'none';
                }
            }

            //공통 : 라벨 위치 조정
            if (typeof compAttr.label !== 'undefined') {
                let firstField = componentConstructor.domElem.querySelector('.group').firstElementChild;
                let lastField = componentConstructor.domElem.querySelector('.group').lastElementChild;
                if (compAttr.label.position === 'hidden') {
                    firstField.style.display = 'none';
                } else if (compAttr.label.position === 'left') {
                    firstField.style.flexBasis = (columnWidth * Number(compAttr.label.column)) + '%';
                } else { //top
                    firstField.style.flexBasis = (columnWidth * Number(compAttr.label.column)) + '%';
                    const secondField = document.createElement('div');
                    secondField.className = 'field';
                    secondField.style.flexBasis = (100 - (columnWidth * Number(compAttr.label.column))) + '%';
                    lastField.parentNode.insertBefore(secondField, lastField);
                }
                lastField.style.flexBasis = (columnWidth * Number(compAttr.display.column)) + '%';
            }
        }
        return componentConstructor;
    }
    /**
     * 컴포넌트 기본 속성을 읽어서 폼 저장을 위한 컴포넌트의 데이터로 정제하여 조회한다.
     * @param {String} type 컴포넌트 타입
     * @return {Object} refineAttr 컴포넌트 데이터
     */
    function getData(type) {
        let refineProp = { display: {} };
        let defaultProp = aliceJs.mergeObject({}, aliceForm.componentProperties[type]);
        Object.keys(defaultProp).forEach(function(group) {
            if (group === 'option') { //옵션 json 구조 변경
                let options = [];
                for (let i = 0, len = defaultProp[group][0].items.length; i < len; i+=3) {
                    let option = {};
                    for (let j = i; j < i + len; j++) {
                        let child = defaultProp[group][0].items[j];
                        option[child.id] = child.value;
                    }
                    options.push(option);
                }
                refineProp[group] = options;
            } else {
                refineProp[group] = {};
                Object.keys(defaultProp[group]).forEach(function(child) {
                    const attributeItem = defaultProp[group][child];
                    let attributeItemValue = attributeItem.value;
                    if (type === 'datetime' || type === 'date' || type === 'time') {
                        if (/datetimeM*/.test(attributeItem.id)) {
                            attributeItemValue = i18n.userDateTime(attributeItemValue);
                        } else if (/dateM*/.test(attributeItem.id)) {
                            attributeItemValue = i18n.userDate(attributeItemValue);
                        } else if (/timeM*/.test(attributeItem.id)) {
                            attributeItemValue = i18n.userTime(attributeItemValue);
                        } else {
                            attributeItemValue = attributeItem.value;
                        }
                    }
                    refineProp[group][attributeItem.id] = attributeItemValue;
                });
            }
        });
        return refineProp;
    }

    /**
     * 마지막 추가된 컴포넌트 Index 조회
     * @return {Number} 마지막 컴포넌트 index
     */
    function getLastIndex() {
        return componentIdx;
    }
    /**
     * 컴포넌트 Index 초기화
     * @param {Number} idx 초기화할 값
     */
    function setLastIndex(idx) {
        componentIdx = idx;
    }

    exports.draw = draw;
    exports.getTitle = getTitle;
    exports.getData = getData;
    exports.getLastIndex = getLastIndex;
    exports.setLastIndex = setLastIndex;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
