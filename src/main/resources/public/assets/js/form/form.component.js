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
        editboxPlaceholder= '+ Typing \'/\' for add component';
        
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
        let textDefaultValue = (textDefaultArr[0] === 'none') ? textDefaultArr[1] : aliceForm.options.sessionInfo[textDefaultArr[1]];
        //폼 양식 편집 화면에서는 세션 값이 출력되지 않는다.
        if (target.hasAttribute('data-readonly') && textDefaultArr[0] !== 'none') {
            textDefaultValue = textDefaultArr[2];
        }
        //처리할 문서는 실 데이터를 출력한다.
        if (attr.values !== undefined && attr.values.length > 0) {
            textDefaultValue = attr.values[0].value;
        }
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'  style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}</div>
                    <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                    <input type='text' placeholder='${attr.display.placeholder}' value='${textDefaultValue}'
                    ${attr.displayType === 'editable_required' ? 'required' : ''} max-length='${attr.validate["length-max"]}'
                    min-length='${attr.validate["length-min"]}' regexp='${attr.validate.regexp}' regexp-msg='${attr.validate["regexp-msg"]}'/>
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
        const textEditorUseYn = attr.display['editor-useYn'] === 'Y';
        const textEditorHeight = attr.display.rows !== '' ? Number(attr.display.rows) * defaultRowHeight : defaultRowHeight;
        let textAreaDefaultValue = '';
        //처리할 문서는 실 데이터를 출력한다.
        if (attr.values !== undefined && attr.values.length > 0) {
            textAreaDefaultValue = textEditorUseYn ? JSON.parse(attr.values[0].value) : attr.values[0].value;
        }
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}</div>
                    <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                ${textEditorUseYn ?
                    `<div style='width: 100%;'>
                        <div id='editor' class='editor-container'
                        style='height: ${textEditorHeight}px;' ${attr.displayType === 'editable_required' ? 'required' : ''}
                        max-length='${attr.validate["length-max"]}' min-length='${attr.validate["length-min"]}'></div>
                    </div>` :
                    `<textarea placeholder='${attr.display.placeholder}' rows='${attr.display.rows}' 
                    ${attr.displayType === 'editable_required' ? 'required' : ''}
                    max-length='${attr.validate["length-max"]}' min-length='${attr.validate["length-min"]}'>${textAreaDefaultValue}</textarea>`
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
                    <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                    <select ${attr.displayType === 'editable_required' ? 'required' : ''}></select>
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
            if (attr.values !== undefined && attr.values.length > 0 && optElem.value === attr.values[0].value) {
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
                    <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;' id='radio' ${attr.displayType === 'editable_required' ? "required" : ""}></div>
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
            if (attr.values !== undefined && attr.values.length > 0) {
                radioElem.checked = (radioElem.value === attr.values[0].value);
            } else {
                radioElem.checked = (i === 0);
            }

            radioElem.addEventListener('click', function() {
                let checkedRadioElem = comp.querySelectorAll('input[type=radio]:checked');
                for (let j = 0, cheklen = checkedRadioElem.length; j < cheklen; j++) {
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
                    <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;' id='chkbox' ${attr.displayType === 'editable_required' ? "required" : ""}></div>
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
            if (attr.values !== undefined && attr.values.length > 0) {
                const checkboxValues = attr.values[0].value.split(',');
                for (let j = 0, cheklen = checkboxValues.length; j < cheklen; j++) {
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
    function Datebox(attr, target) {
        //날짜 포멧 변경
        let dateDefaultArr = attr.display['default'].split('|');
        let dateDefault = '';
        let datePlaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
        //처리할 문서는 실 데이터를 출력한다.
        if (attr.values !== undefined && attr.values.length > 0) {
            let dateValue = attr.values[0].value.split('|');
            if (dateValue[0] !== '') {
                dateDefault = aliceJs.changeDateFormat(dateValue[1], datePlaceholder, dateValue[0], aliceForm.options.lang);
            }
        } else {
            if (dateDefaultArr[0] === 'now') {
                dateDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat);
                dateDefault = dateDefault.split(' ')[0];
            } else if (dateDefaultArr[0] === 'datepicker') {
                if (dateDefaultArr[1] !== '') {
                    dateDefault = aliceJs.changeDateFormat(dateDefaultArr[2], datePlaceholder, dateDefaultArr[1], aliceForm.options.lang);
                }
            } else if (dateDefaultArr[0] === 'date') {
                dateDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat, dateDefaultArr[1]);
                dateDefault = dateDefault.split(' ')[0];
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
                        <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='date-${attr.id}' placeholder='${aliceForm.options.dateFormat}' value='${dateDefault}' ${attr.displayType === 'editable_required' ? 'required' : ''} date-max='${attr.validate["date-max"]}' date-min='${attr.validate["date-min"]}'/>
                    </div>
                </div>
            `);

        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            dateTimePicker.initDatePicker('date-' + attr.id, aliceForm.options.dateFormat, aliceForm.options.lang);
        }
    }

    /**
     * Time 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성성
     * @constructor
     */
    function Timebox(attr, target) {
        //시간 포멧 변경
        let timeDefaultArr = attr.display['default'].split('|');
        let timeFormat = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
        let beforeFormt = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + '24';
        let timeDefault = '';
        //처리할 문서는 실 데이터를 출력한다.
        if (attr.values !== undefined && attr.values.length > 0) {
            //저장한 날짜와 포맷
            let timeValue = attr.values[0].value.split('|');
            //저장한 가상 날짜 및 시간
            let dummyDateTime = aliceJs.getTimeStamp(timeValue[1]);
            //저장한 가상 날짜
            let dummyDate = dummyDateTime.split(' ');
            let timeValueArr = timeValue[0].split(' ');
            if (timeValueArr.length === 2) {
                timeDefault = aliceJs.changeDateFormat(timeValue[1], beforeFormt, dummyDate[0] +' '+ timeValue[0], aliceForm.options.lang);
            } else {
                timeDefault = aliceJs.changeDateFormat(timeValue[1], timeFormat, dummyDate[0] +' '+ timeValue[0], aliceForm.options.lang);
            }
            let time = timeDefault.split(' ');
            timeDefault = (time.length > 2) ? (time[1] +' '+ time[2]) : timeDefault = time[1];
        } else {
            if (timeDefaultArr[0] === 'now') {
                timeDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat);
                timeDefault = aliceJs.changeDateFormat(beforeFormt, timeFormat, timeDefault, aliceForm.options.lang);
                let timeNow = timeDefault.split(' ');
                timeDefault = (timeNow.length > 2) ? (timeNow[1] + ' ' + timeNow[2]) : timeNow[1];
            } else if (timeDefaultArr[0] === 'timepicker') {
                timeDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat) + ' ' + timeDefaultArr[1];
                timeDefault = aliceJs.changeDateFormat(timeDefaultArr[2], timeFormat, timeDefault, aliceForm.options.lang);
                let timePicker = timeDefault.split(' ');
                if (timePicker.length === 3) {
                    timeDefault = timePicker[1] + ' '+ timePicker[2];
                } else if (timePicker.length === 2) {
                    timeDefault = timePicker[1];
                } else if (timePicker.length === 1) {
                    timeDefault = aliceJs.getTimeStamp(aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat);
                    timeDefault = aliceJs.changeDateFormat(timeFormat, timeFormat, timeDefault, aliceForm.options.lang);
                    timePicker = timeDefault.split(' ');
                    timeDefault = timePicker[1];
                }
            } else if (timeDefaultArr[0] === 'time') {
                timeDefault = aliceJs.getTimeStamp(beforeFormt, '', timeDefaultArr[1]);
                let timeDate = timeDefault.split(' ');
                timeDefault = aliceJs.changeDateFormat(beforeFormt, timeFormat, timeDate[0] + ' ' + timeDate[1], aliceForm.options.lang);
                let time = timeDefault.split(' ');
                timeDefault = (time.length > 2) ? (time[1] + ' ' + time[2]) : time[1];
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
                        <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='time-${attr.id}' placeholder='${aliceForm.options.timeFormat}' value='${timeDefault}' ${attr.displayType === 'editable_required' ? 'required' : ''} time-max='${attr.validate["time-max"]}' time-min='${attr.validate["time-min"]}'/>
                    </div>
                </div>
            `);

        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            dateTimePicker.initTimePicker('time-' + attr.id, aliceForm.options.hourType, aliceForm.options.lang);
        }
    }

    /**
     * Date Time 컴포넌트
     *
     * @param {Object} target 컴포넌트가 추가될 대상
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function DateTimebox(attr, target) {
        //날짜 시간 포멧 변경
        let datetimeDefaultArr = attr.display['default'].split('|');
        let datetimeDefault = '';
        let beforeDatetimeDefault = '';
        let datetimePlaceholder = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat + ' ' + aliceForm.options.hourType;
        let timeFormat = aliceForm.options.dateFormat + ' ' + aliceForm.options.timeFormat;

        if (attr.values != undefined && attr.values.length > 0 ) {
            let dateValue = attr.values[0].value.split('|');
            if (dateValue[0] !== '') {
                datetimeDefault = aliceJs.changeDateFormat(dateValue[1], datetimePlaceholder, dateValue[0], aliceForm.options.lang);
            }
        } else {
            if (datetimeDefaultArr[0] === 'now') {
                datetimeDefault = aliceJs.getTimeStamp(timeFormat);
                datetimeDefault = aliceJs.changeDateFormat(timeFormat + ' 24', datetimePlaceholder, datetimeDefault, aliceForm.options.lang);
            } else if (datetimeDefaultArr[0] === 'datetimepicker') {
                if (datetimeDefaultArr[1] !== '') {
                    beforeDatetimeDefault = datetimeDefaultArr[1];
                    datetimeDefault = aliceJs.changeDateFormat(datetimeDefaultArr[2], datetimePlaceholder, beforeDatetimeDefault, aliceForm.options.lang);
                }
            } else if (datetimeDefaultArr[0] === 'datetime') {
                beforeDatetimeDefault = aliceJs.getTimeStamp(timeFormat, datetimeDefaultArr[1], datetimeDefaultArr[2]);
                datetimeDefault = aliceJs.changeDateFormat(timeFormat + ' 24', datetimePlaceholder, beforeDatetimeDefault, aliceForm.options.lang);
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
                        <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='datetime-${attr.id}' placeholder='${datetimePlaceholder}' value='${datetimeDefault}' ${attr.displayType === 'editable_required' ? 'required' : ''} date-max='${attr.validate["date-max"]}' date-min='${attr.validate["date-min"]}'/>
                    </div>
                </div>
            `);
        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            dateTimePicker.initDateTimePicker('datetime-' + attr.id, aliceForm.options.dateFormat, aliceForm.options.hourType, aliceForm.options.lang);
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
                    <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='flex-basis: 100%;' id='fileupload' ${attr.displayType === 'editable_required' ? 'required' : ''}>
                    <div id='dropZoneFiles-${attr.id}'></div> 
                    <div id='dropZoneUploadedFiles-${attr.id}' class='dropbox'></div>
                </div>
            </div>
        `);

        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            document.getElementById('dropZoneUploadedFiles-' + attr.id).classList.remove('dropbox');
            let fileOptions = {
                extra: {
                    formId: 'frm',
                    ownId: '',
                    dropZoneFilesId: 'dropZoneFiles-' + attr.id,
                    dropZoneUploadedFilesId: 'dropZoneUploadedFiles-' + attr.id,
                    editor: (attr.displayType !== 'readonly')
                }
            };
            if (attr.values !== undefined && attr.values.length > 0) {
                fileOptions.extra.fileDataIds = attr.values[0].value;
            }
            fileUploader.init(fileOptions);
        } else {
            let fileUploadElem = comp.querySelector('.dropbox');
            fileUploadElem.textContent = 'Drop files here to upload';
            let buttonElem = document.createElement('button');
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
        let defaultCustomData = (attr.values !== undefined && attr.values.length > 0) ? attr.values[0].value : '';
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='text-align: ${attr.label.align};'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px;
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}</div>
                    <span class='required' style='${attr.displayType === "editable_required" ? "" : "display: none;"}'>*</span>
                </div>
                <div class='field' style='display: flex; flex-basis: 100%;'>
                    <input type='text' ${attr.displayType === 'editable_required' ? 'required' : ''} readonly custom-data='${defaultCustomData}'/>
                    <input type='button' id='codeBtn' value='${attr.display["button-text"]}'>
                </div>
            </div>
        `);
        
        target.appendChild(comp);
        this.domElem = comp;

        if (!target.hasAttribute('data-readonly')) {
            let customCodeTextElem = comp.querySelector('input[type="text"]');
            if (defaultCustomData !== '') {
                let customCodeValues = defaultCustomData.split(',');
                let inputValue = '';
                for (let i = 0, len = customCodeValues.length; i < len; i++) {
                    let customDataValue = customCodeValues[i].split('|');
                    if (customDataValue.length > 1) {
                        if (inputValue === '' && inputValue.indexOf("/") === -1) {
                            inputValue = customDataValue[1];
                        } else {
                            inputValue += '/' + customDataValue[1];
                        }
                    }
                }
                customCodeTextElem.textContent = inputValue;
            }

            let searchBtn = comp.querySelector('#codeBtn');
            searchBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                let url = '/documents/custom-code/' + attr.display['custom-code'] + '/data';
                window.open(url, 'rtn', 'width=500, height=600');
                let customCodeData = {
                    componentId: attr.id,
                    componentValues: customCodeTextElem.getAttribute('custom-data')
                };

                let form = document.createElement('form');
                form.action = url;
                form.method = 'POST';
                form.target = 'rtn';
                let inputElem = document.createElement('input');
                inputElem.name = 'customCodeData';
                inputElem.value = JSON.stringify(customCodeData);
                form.appendChild(inputElem);
                form.style.display = 'none';

                document.body.appendChild(form);
                form.submit();
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
        let compAttr = { display: {} },
            compId = '';
        
        if (compData !== undefined) { //기존 저장된 컴포넌트 속성이 존재할 경우
            if (compData.attributes === undefined) { //폼 편집, 신청서
                compId = compData.id;
                compAttr = compData;
            } else {                                 //처리할 문서
                compId = compData.componentId;
                compAttr = compData.attributes;
                compAttr.values = compData.values;   //처리할 문서 실제 데이터
                compAttr.id = compId;
                compAttr.displayType = compData.displayType;
            }
        } else {                     //신규 생성된 컴포넌트일 경우
            compId = workflowUtil.generateUUID();
            compAttr = getData(compType);
            compAttr.id = compId;
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
                componentConstructor =  new Datebox(compAttr, compTarget);
                break;
            case 'time':
                componentConstructor =  new Timebox(compAttr, compTarget);
                break;
            case 'datetime':
                componentConstructor =  new DateTimebox(compAttr, compTarget);
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
            if (compAttr.displayType === 'readonly') {
                componentConstructor.domElem.setAttribute('data-readonly', true);
            }

            //공통 : 라벨 위치 조정
            if (typeof compAttr.label !== 'undefined') {
                let firstField = componentConstructor.domElem.querySelector('.group').firstElementChild;
                let lastField = componentConstructor.domElem.querySelector('.group').lastElementChild;
                if (compAttr.label.position === 'hidden') {
                    firstField.style.display = 'none';
                } else if (compAttr.label.position === 'left') {
                    firstField.style.flexBasis = (aliceForm.options.columnWidth * Number(compAttr.label.column)) + '%';
                } else { //top
                    firstField.style.flexBasis = (aliceForm.options.columnWidth * Number(compAttr.label.column)) + '%';
                    const secondField = document.createElement('div');
                    secondField.className = 'field';
                    secondField.style.flexBasis = (100 - (aliceForm.options.columnWidth * Number(compAttr.label.column))) + '%';
                    lastField.parentNode.insertBefore(secondField, lastField);
                }
                lastField.style.flexBasis = (aliceForm.options.columnWidth * Number(compAttr.display.column)) + '%';
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
        let refineAttr = { display: {} };
        let defaultAttr = JSON.parse(JSON.stringify(aliceForm.options.componentAttribute[type]));
        //let defaultAttr = Object.assign({}, aliceForm.options.componentAttribute[type]);
        Object.keys(defaultAttr).forEach(function(group) {
            if (group === 'option') { //옵션 json 구조 변경
                let options = [];
                for (let i = 0, len = defaultAttr[group][0].items.length; i < len; i+=3) {
                    let option = {};
                    for (let j = i; j < i + len; j++) {
                        let child = defaultAttr[group][0].items[j];
                        option[child.id] = child.value;
                    }
                    options.push(option);
                }
                refineAttr[group] = options;
            } else {
                refineAttr[group] = {};
                Object.keys(defaultAttr[group]).forEach(function(child) {
                    refineAttr[group][defaultAttr[group][child].id] = defaultAttr[group][child].value;
                });
            }
        });
        return refineAttr;
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
