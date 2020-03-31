/**
* @projectDescription Form Designer Component Add Library
*
* @author woodajung
* @version 1.0
* @sdoc js/form/formEditor.js
*/
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.component = global.component || {})));
}(this, (function (exports) {
    'use strict';

    const defaultColWidth = 8.33,  //폼 패널을 12등분하였을때, 1개의 너비
          editboxPlaceholder= '+ Typing \'/\' for add component',
          componentTitles = [  //세부속성에서 사용할 제목
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
          ];
    let formPanel = null,
        defaultData = {},          //컴포넌트 기본 세부 속성 데이터
        componentIdx = 0;          //컴포넌트 index = 출력 순서 생성시 사용

    const utils = {
        /**
         * 템플릿 리터럴 문자열을 전달받아서 컴포넌트 생성한다.
         * @param {String} template 템플릿 리터럴
         * @return {Object} elem 생성된 컴포넌트 객체
         */
        createComponentByTemplate: function(template) {
            let elem = document.createElement('component');
            elem.classList.add('component');
            elem.innerHTML = template;
            return elem;
        },
        /**
         * 현재 시간을 2020-02-19 13:30 형식으로 추출한다. 
         * date, time, datetime 컴포넌트의 default값으로 사용하기 위한 용도이다.
         * @param {String} day 날짜 간격(3 = 현재 날짜의 3일 후, -3 = 현재 날짜의 3일전을 의미)
         * @param {String} time 시간 간격(3 = 현재 시간 기준 3시간 후, -3 = 현재 시간기준 3시간 전을 의미)
         * @return {String} datetime 변경된 시간
         */
        getTimeStamp: function(day, time) {
            const today = new Date();
            
            if (day !== undefined && day !== null && day !== '') {
                today.setDate(today.getDate() + Number(day));
            }
            if (time !== undefined && time !== null && time !== '') {
                today.setHours(today.getHours() + Number(time));
            }
            let datetime = utils.parseZero(today.getFullYear(), 4) + '-' +
                    utils.parseZero(today.getMonth() + 1, 2) + '-' +
                    utils.parseZero(today.getDate(), 2) + ' ' +
                    utils.parseZero(today.getHours(), 2) + ':' +
                    utils.parseZero(today.getMinutes(), 2);
            return datetime;
        },
        /**
         * 시분초에 length가 변경될 경우 0 붙이는 함수이다.
         * 예를 들어 1월은 01월 3시 일경우 03시등으로 변경하기 위해 사용한다.
         * @param {Number} num 날짜, 시간 값
         * @param {Number} digits 자릿수
         * @return {Number} zero + num 변경된 날짜 시간 값
         */
        parseZero: function(num, digits) {
            let zero = '';
            num = num.toString();
            if (num.length < digits) {
                for (let i = 0; i < (digits - num.length); i++) { 
                    zero += '0'; 
                }
            }
            return zero + num;
        }
    };

    /**
     * Editbox 컴포넌트
     * @constructor
     */
    function Editbox() {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group' contenteditable='true' placeholder="${editboxPlaceholder}"></div>
        `);
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Text 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Text(attr) {
        let textDefaultArr = attr.display['default'].split('|');
        let textDefaultName = '';
        if (textDefaultArr[0] === 'none') {
            textDefaultName = textDefaultArr[1];
        } else {
            textDefaultName = textDefaultArr[2];
        }
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                    </div>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                    <input type='text' placeholder='${attr.display.placeholder}' value='${textDefaultName}' readonly 
                    style='border-color: ${attr.display["outline-color"]}; border-width: ${attr.display["outline-width"]}px;' 
                    ${attr.validate.required === "Y" ? "required" : ""} 
                    max-length=${attr.validate["length-max"]} min-length=${attr.validate["length-min"]} />
                </div>
            </div>
        `);

        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Text Box 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Textarea(attr) {
        const defaultRowHeight = 26;
        const textEditorUseYn = attr.display['editor-useYn'] === 'Y' ? true : false;
        const textEditorHeight = attr.display.rows !== '' ? Number(attr.display.rows) * defaultRowHeight : defaultRowHeight;
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                    </div>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                ${textEditorUseYn ?
            `<div style='width: 100%;'>
                        <div class='editor-container' 
                        style='height: ${textEditorHeight}px; border-color: ${attr.display["outline-color"]}; border-width: ${attr.display["outline-width"]}px;'></div>
                    </div>` :
            `<textarea placeholder='${attr.display.placeholder}' rows='${attr.display.rows}' readonly 
                    style='border-color: ${attr.display["outline-color"]}; border-width: ${attr.display["outline-width"]}px;' 
                    ${attr.validate.required === "Y" ? "required" : ""}></textarea>`
        }
                </div>
            </div>
        `);

        formPanel.appendChild(comp);
        this.domElem = comp;
        if (textEditorUseYn) {
            let textEditorOptions = {
                modules: {
                    toolbar: [
                        [{'header': [1, 2, 3, 4, 5, 6, false]}],
                        ['bold', 'italic', 'underline'],
                        [{'color': []}, {'background': []}],
                        [{'align': []}, { 'list': 'bullet' }, 'image']
                    ]
                },
                placeholder: attr.display.placeholder,
                readOnly: true,
                theme: 'snow'
            };
            let textEditorContainer = comp.querySelector('.editor-container');
            let textEditor = new Quill(textEditorContainer, textEditorOptions);

            let textEditorToolbar = textEditor.getModule('toolbar');
            if (textEditorToolbar) {
                textEditorToolbar.container.style.borderWidth = attr.display['outline-width'] + 'px';
                textEditorToolbar.container.style.borderColor = attr.display['outline-color'];
            }
        }
    }

    /**
     * Dropdown 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Selectbox(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                    </div>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                    <select></select>
                </div>
            </div>
        `);
        let selectbox = comp.querySelector('select');
        for (let i = 0, len = attr.option.length; i < len; i++) {
            let option = document.createElement('option');
            option.value = attr.option[i].value + '-' + attr.option[i].seq;
            option.text = attr.option[i].name;
            option.setAttribute('seq', attr.option[i].seq);
            selectbox.appendChild(option);
        }

        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Radio Button 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Radiobox(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                    </div>
                </div>
                <div class='field' style='flex-basis: 100%;' id='radio'></div>
            </div>
        `);
        let radio = comp.querySelector('#radio');
        radio.classList.add(attr.display.direction);
        for (let i = 0, len = attr.option.length; i < len; i++) {
            let option = document.createElement('div');
            option.classList.add('field-radio');
            if (attr.display.direction === 'horizontal') { option.style.display = 'inline-block'; }
            radio.appendChild(option);
            
            let optionRadio = document.createElement('input');
            optionRadio.setAttribute('type', 'radio');
            optionRadio.setAttribute('id', attr.option[i].value + '-' + attr.option[i].seq);
            optionRadio.setAttribute('seq', attr.option[i].seq);
            optionRadio.value = attr.option[i].value;
            optionRadio.name = attr.option[i].name;
            
            if (i === 0) { optionRadio.checked = true; }
            
            optionRadio.addEventListener('click', function() {
                let checkedRadio = comp.querySelectorAll('input[type=radio]:checked');
                for (let i = 0; i < checkedRadio.length; i++) {
                    checkedRadio[i].checked = false;
                }
                this.checked = true;
            });
            
            let optionLabel = document.createElement('label');
            optionLabel.setAttribute('for', attr.option[i].value + '-' + attr.option[i].seq);
            optionLabel.textContent = attr.option[i].name;
            
            if (attr.display.position === 'left') {
                option.appendChild(optionLabel);
                option.appendChild(optionRadio);
            } else {
                option.appendChild(optionRadio);
                option.appendChild(optionLabel);
            }
        }

        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Checkbox 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Checkbox(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                    </div>
                </div>
                <div class='field' style='flex-basis: 100%;' id='chkbox'></div>
            </div>
        `);
        let chkbox = comp.querySelector('#chkbox');
        chkbox.classList.add(attr.display.direction);
        for (let i = 0, len = attr.option.length; i < len; i++) {
            let option = document.createElement('div');
            option.classList.add('field-checkbox');
            if (attr.display.direction === 'horizontal') { option.style.display = 'inline-block'; }
            chkbox.appendChild(option);
            
            let optionChk = document.createElement('input');
            optionChk.setAttribute('type', 'checkbox');
            optionChk.setAttribute('id', attr.option[i].value + '-' + attr.option[i].seq);
            optionChk.setAttribute('seq', attr.option[i].seq);
            optionChk.value = attr.option[i].value;
            optionChk.name = attr.option[i].name;
            
            if (i === 0) { optionChk.setAttribute('checked', 'checked'); }
            
            let optionLabel = document.createElement('label');
            optionLabel.setAttribute('for', attr.option[i].value + '-' + attr.option[i].seq);
            optionLabel.textContent = attr.option[i].name;
            
            if (attr.display.position === 'left') {
                option.appendChild(optionLabel);
                option.appendChild(optionChk);
            } else {
                option.appendChild(optionChk);
                option.appendChild(optionLabel);
            }
        }

        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Label 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Label(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%;'>
                    <div class='label'style='color: ${attr.display.color}; font-size: ${attr.display.size}px; text-align: ${attr.display.align}; 
                    ${attr.display.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.display.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.display.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.display.text}</div>
                </div>
            </div>
        `);
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Image 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Imagebox(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%; text-align: ${attr.display.align};'>
                    <img src='${attr.display.path}' alt='' width='${attr.display.width}' height='${attr.display.height}'>
                </div>
            </div>
        `);
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Divider 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Divider(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%;'>
                    <hr style='border: ${attr.display.type} ${attr.display.thickness}px ${attr.display.color};'>
                </div>
            </div>
        `);
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Date 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Datebox(attr) {
        //날짜 포멧 변경
        let dateDefaultArr = attr.display['default'].split('|');
        let dateDefault = '';
        let dateFormat = formEditor.userData.defaultDateFormat;
        if (dateDefaultArr[0] === 'now') {
            dateDefault = aliceJs.getTimeStamp(dateFormat);
            dateDefault = dateDefault.split(' ')[0];
        } else if (dateDefaultArr[0] === 'datepicker') {
            if (dateDefaultArr[1] === '') {
                dateDefaultArr[1] = aliceJs.getTimeStamp(dateFormat);
            }
            dateDefault = aliceJs.changeDateFormat(dateDefaultArr[2], dateFormat, dateDefaultArr[1], formEditor.userData.lang);
        } else if (dateDefaultArr[0] === 'date') {
            if (dateDefaultArr[1] === '') {
                dateDefaultArr[1] = 0;
                dateDefault = aliceJs.getTimeStamp(dateFormat, '');
            } else {
                dateDefault = aliceJs.getTimeStamp(dateFormat, dateDefaultArr[1]);
            }
        }
        let comp = utils.createComponentByTemplate(`
                <div class='move-icon'></div>
                <div class='group'>
                    <div class='field'>
                        <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                        ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                            <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='date-${attr.id}' placeholder='${formEditor.userData.defaultDateFormat}' value='${dateDefault}' readonly/>
                    </div>
                </div>
            `);

        formPanel.appendChild(comp);
        this.domElem = comp;
        //TODO: 데이터 포멧 변환
        dateTimePicker.initDatePicker('date-' + attr.id, dateFormat, formEditor.userData.defaultLang);
    }

    /**
     * Time 컴포넌트
     * @param {Object} attr 컴포넌트 속성성
     * @constructor
     */
    function Timebox(attr) {
        //시간 포멧 변경
        let timeDefaultArr = attr.display['default'].split('|');
        let timeDefault = '';
        let timeFormat = formEditor.userData.defaultDateFormat +' ' +formEditor.userData.defaultTimeFormat +' ' +formEditor.userData.defaultTime;
        let beforeFormt = formEditor.userData.defaultDateFormat +' ' +formEditor.userData.defaultTimeFormat +' ' + '24';
        if (timeDefaultArr[0] === 'now') {
            timeDefault = aliceJs.getTimeStamp(formEditor.userData.defaultDateFormat +' ' +formEditor.userData.defaultTimeFormat);
            timeDefault = aliceJs.changeDateFormat(beforeFormt, timeFormat, timeDefault, formEditor.userData.lang);
            let timeNow = timeDefault.split(' ');
            if (timeNow.length > 2) {
                timeDefault = timeNow[1] +' '+timeNow[2];
            } else {
                timeDefault = timeNow[1];
            }
        } else if (timeDefaultArr[0] === 'time') {
            timeDefault = aliceJs.getTimeStamp(beforeFormt, '', timeDefaultArr[1]);
            let timeDate = timeDefault.split(' ');
            timeDefault = aliceJs.changeDateFormat(beforeFormt, timeFormat, timeDate[0] +' '+ timeDate[1], formEditor.userData.lang);
            let time = timeDefault.split(' ');
            if (time.length > 2) {
                timeDefault = time[1] +' '+time[2];
            } else {
                timeDefault = time[1];
            }
        } else if (timeDefaultArr[0] === 'timepicker') {
            timeDefault = aliceJs.getTimeStamp(formEditor.userData.defaultDateFormat)+ ' ' + timeDefaultArr[1];
            timeDefault = aliceJs.changeDateFormat(timeFormat, timeFormat, timeDefault, formEditor.userData.lang);
            let timepicker = timeDefault.split(' ');
            if (timepicker.length === 3) {
                timeDefault = timepicker[1] +' '+timepicker[2];
            } else if (timepicker.length === 2) {
                timeDefault = timepicker[1];
            } else if (timepicker.length === 1) {
                timeDefault = aliceJs.getTimeStamp(formEditor.userData.defaultDateFormat + ' ' + formEditor.userData.defaultTimeFormat);
                timeDefault = aliceJs.changeDateFormat(timeFormat, timeFormat, timeDefault, formEditor.userData.lang);
                timepicker = timeDefault.split(' ');
                timeDefault = timepicker[1];
            }
        }
        let targetElement = document.getElementById('time-' + attr.id);
        if (targetElement != undefined) {
            targetElement.remove();
        }
        let comp = '';
        if (formEditor.userData.defaultTime == '12') {
            comp = utils.createComponentByTemplate(`
                <div class='move-icon'></div>
                <div class='group'>
                    <div class='field'>
                        <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                        ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                            <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='time-${attr.id}' placeholder='${formEditor.userData.defaultTimeFormat} a' value='${timeDefault}' readonly/>
                    </div>
                </div>
            `);
        } else {
            comp = utils.createComponentByTemplate(`
                <div class='move-icon'></div>
                <div class='group'>
                    <div class='field'>
                        <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                        ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                            <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='time-${attr.id}' placeholder='${formEditor.userData.defaultTimeFormat}' value='${timeDefault}' readonly/>
                    </div>
                </div>
            `);
        }

        formPanel.appendChild(comp);
        this.domElem = comp;
        //TODO: 데이터 포멧 변환
        dateTimePicker.initTimePicker('time-' + attr.id, formEditor.userData.defaultTime, formEditor.userData.defaultLang);
    }

    /**
     * Date Time 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function DateTimebox(attr) {
        //날짜 시간 포멧 변경
        let datetimeDefaultArr = attr.display['default'].split('|');
        let datetimeDefault = '';
        let beforeDatetimeDefault = '';
        let timeFormat = formEditor.userData.defaultDateFormat + ' ' + formEditor.userData.defaultTimeFormat;
        let nowTimeFormat = formEditor.userData.defaultDateFormat + ' ' + formEditor.userData.defaultTimeFormat + ' '+  formEditor.userData.defaultTime;

        if (datetimeDefaultArr[0] === 'now') {
            datetimeDefault = aliceJs.getTimeStamp(timeFormat);
        } else if (datetimeDefaultArr[0] === 'datetimepicker') {
            if (datetimeDefaultArr[1] !== '') {
                beforeDatetimeDefault = datetimeDefaultArr[1];
            } else {
                beforeDatetimeDefault = aliceJs.getTimeStamp(timeFormat);
            }
            datetimeDefault = aliceJs.changeDateFormat(datetimeDefaultArr[2], nowTimeFormat, beforeDatetimeDefault, formEditor.userData.lang);
        } else if (datetimeDefaultArr[0] === 'datetime') {
            beforeDatetimeDefault = aliceJs.getTimeStamp(timeFormat, datetimeDefaultArr[1], datetimeDefaultArr[2]);
            datetimeDefault = aliceJs.changeDateFormat(datetimeDefaultArr[3], nowTimeFormat, beforeDatetimeDefault, formEditor.userData.lang);
        }

        let targetElement = document.getElementById('datetime-' + attr.id);
        if (targetElement != undefined) {
            targetElement.remove();
        }
        let comp = '';
            comp = utils.createComponentByTemplate(`
                <div class='move-icon'></div>
                <div class='group'>
                    <div class='field'>
                        <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                        ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                        ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                        ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                            <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                        </div>
                    </div>
                    <div class='field' style='flex-basis: 100%;'>
                        <input type='text' id='datetime-${attr.id}' placeholder='${formEditor.userData.defaultDateFormat} a' value='${datetimeDefault}' readonly />
                    </div>
                </div>
            `);
        formPanel.appendChild(comp);
        this.domElem = comp;
        //TODO: 데이터 포멧 변환
        dateTimePicker.initDateTimePicker('datetime-' + attr.id, formEditor.userData.defaultDateFormat, formEditor.userData.defaultTime, formEditor.userData.defaultLang);
    }

    /**
     * Fileupload 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Fileupload(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                    </div>
                </div>
                <div class='field' style='flex-basis: 100%;'>
                    <input type='file' readonly />
                </div>
            </div>
        `);

        formPanel.appendChild(comp);
        this.domElem = comp;
    }
    /**
     * Custom-Code 컴포넌트
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function CustomCode(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field'>
                    <div class='label' style='color: ${attr.label.color}; font-size: ${attr.label.size}px; text-align: ${attr.label.align}; 
                    ${attr.label.bold === "Y" ? "font-weight: bold;" : ""} 
                    ${attr.label.italic === "Y" ? "font-style: italic;" : ""} 
                    ${attr.label.underline === "Y" ? "text-decoration: underline;" : ""}'>${attr.label.text}
                        <span class='required' style='${attr.validate.required === "Y" ? "" : "display: none;"}'>*</span>
                    </div>
                </div>
                <div class='field' style='display: flex; flex-basis: 100%;'>
                    <input type='text' readonly ${attr.validate.required === "Y" ? "required" : ""} />
                    <button type='button' readonly>${attr.display["button-text"]}</button>
                </div>
            </div>
        `);
        
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * 컴포넌트를 생성하고 출력한다.
     * @param {String} compType 컴포넌트 타입
     * @param {Object} compData 컴포넌트 데이터
     * @return {Object} 생성된 컴포넌트 객체
     */
    function draw(compType, compData) {
        let compAttr = { display: {} },
            compId = '';
        
        if (compData !== undefined) { //기존 저장된 컴포넌트 속성이 존재할 경우
            compId = compData.id;
            compAttr = compData;
        } else {                      //신규 생성된 컴포넌트일 경우
            compId = workflowUtil.generateUUID();
            compAttr = getData(compType);
            compAttr.id = compId;
            compAttr.type = compType;
        }
        compAttr.display.order = ++componentIdx;
        let componentConstructor = null;
        switch(compType) {
            case 'editbox':
                componentConstructor = new Editbox();
                break;
            case 'text':
                componentConstructor =  new Text(compAttr);
                break;
            case 'textarea':
                componentConstructor =  new Textarea(compAttr);
                break;
            case 'select':
                componentConstructor =  new Selectbox(compAttr);
                break;
            case 'radio':
                componentConstructor =  new Radiobox(compAttr);
                break;
            case 'checkbox':
                componentConstructor =  new Checkbox(compAttr);
                break;
            case 'label':
                componentConstructor =  new Label(compAttr);
                break;
            case 'image':
                componentConstructor =  new Imagebox(compAttr);
                break;
            case 'divider':
                componentConstructor =  new Divider(compAttr);
                break;
            case 'date':
                componentConstructor =  new Datebox(compAttr);
                break;
            case 'time':
                componentConstructor =  new Timebox(compAttr);
                break;
            case 'datetime':
                componentConstructor =  new DateTimebox(compAttr);
                break;
            case 'fileupload':
                componentConstructor =  new Fileupload(compAttr);
                break;
            case 'custom-code':
                componentConstructor =  new CustomCode(compAttr);
                break;
            default:
                break;
         }
        
         if (componentConstructor) {
             componentConstructor.id = compId;
             componentConstructor.type = compType;
             componentConstructor.attr = compAttr;
             componentConstructor.domElem.setAttribute('id', compId);
             componentConstructor.domElem.setAttribute('data-index', getLastIndex());
             componentConstructor.domElem.setAttribute('tabIndex', getLastIndex());

             //공통 : 라벨 위치 조정
             if (typeof compAttr.label !== 'undefined') {
                 if (compAttr.label.position === 'hidden') {
                     componentConstructor.domElem.querySelector('.group').firstElementChild.style.display = 'none';
                 } else if (compAttr.label.position === 'left') {
                     componentConstructor.domElem.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(compAttr.label.column)) + '%';
                     componentConstructor.domElem.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(compAttr.display.column)) + '%';
                 }
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
        let defaultAttr = Object.assign({}, defaultData[type]);
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
     * 컴포넌트의 데이터를 전달받아서 우측 properties panel 출력용으로 컴포넌트 기본 속성을 정제하여 조회한다.
     * @param {Object} compDate 컴포넌트 데이터
     * @return {String} detailAttr 정제한 컴포넌트 기본 속성 데이터
     */
    function getDefaultAttribute(compDate) {
        let detailAttr = Object.assign({}, defaultData[compDate.type]);
        Object.keys(compDate).forEach(function(comp) {
            if (compDate[comp] !== null && typeof(compDate[comp]) === 'object' && detailAttr.hasOwnProperty(comp))  {
                Object.keys(compDate[comp]).forEach(function(attr) {
                    Object.keys(detailAttr[comp]).forEach(function(d) {
                        if (attr === detailAttr[comp][d].id) {
                            detailAttr[comp][d].value = compDate[comp][attr];
                        }
                    });
                });
            }
        });
        
        return JSON.stringify(detailAttr);
    }
    /**
     * 우측 세부 속성창(properties panel)에 출력될 컴포넌트 제목 객체 조회
     * @param {String} type 컴포넌트 타입
     * @return {Object} match title 일치하는 제목 객제
     */
    function getTitle(type) {
        return componentTitles.filter(function(title) { return title.type === type; })[0];
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
     /**
     * 라이브러리를 초기화하고 라이브러리 사용에 필요한 데이터를 불러온다.
     */
    function init() {
        formPanel = document.getElementById('panel-form');
        
        //컴포넌트 기본 속성인 '/assets/js/form/componentAttribute.json' 데이터를 조회하여 저장한다.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentAttribute.json',
            callbackFunc: function(xhr) {
                defaultData = JSON.parse(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }
    
    exports.init = init;
    exports.draw = draw;
    exports.getDefaultAttribute = getDefaultAttribute;
    exports.getData = getData;
    exports.getTitle = getTitle;
    exports.getLastIndex = getLastIndex;
    exports.setLastIndex = setLastIndex;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
