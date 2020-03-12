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
              {'type': 'fileupload', 'name': 'File Upload', 'icon': ''}
          ];
    let formPanel = null,
        defaultData = {},          //컴포넌트 기본 세부 속성
        componentIdx = 0;          //컴포넌트 index = 출력 순서 생성시 사용

    const utils = {
        /**
         * 템플릿 리터럴 문자열을 전달받아서 컴포넌트 생성
         *
         * @param template 템플릿 리터럴
         * @return elem 생성된 컴포넌트 객체
         */
        createComponentByTemplate: function(template) {
            let elem = document.createElement('component');
            elem.classList.add('component');
            elem.innerHTML = template;
            return elem;
        },
        /**
         * 현재 시간을 2020-02-19 13:30 형식으로 추출 > date, time, datetime 컴포넌트 사용 용도
         */
        getTimeStamp: function(day, time) {
            const today = new Date();
            if (day !== undefined && day !== null && day !== '') {
                today.setDate(today.getDate() + Number(day));
            }
            if (time !== undefined && time !== null && time !== '') {
            	today.setHours(today.getHours() + Number(time));
            }
            let s = utils.parseZero(today.getFullYear(), 4) + '-' +
                    utils.parseZero(today.getMonth() + 1, 2) + '-' +
                    utils.parseZero(today.getDate(), 2) + ' ' +
                    utils.parseZero(today.getHours(), 2) + ':' +
                    utils.parseZero(today.getMinutes(), 2);
            return s;
        },
        /**
         * 시분초에 length가 변경될 경우 0 붙이는 함수
         */
        parseZero: function(n, digits) {
            let zero = '';
            n = n.toString();
            if (n.length < digits) {
            for (let i = 0; i < (digits - n.length); i++)
                zero += '0';
            }
            return zero + n;
        }
    };

    /**
     * Editbox. 컴포넌트를 추가하기 위해서..
     *
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
     * Text.
     *
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Text(attr) {
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
                    <input type='text' placeholder='${attr.display.placeholder}' readonly 
                    style='border-color: ${attr.display["outline-color"]}; border-width: ${attr.display["outline-width"]}px;' 
                    ${attr.validate.required === "Y" ? "required" : ""} 
                    max-length=${attr.validate["length-max"]} min-length=${attr.validate["length-min"]} />
                </div>
            </div>
        `);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Text Box.
     *
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Textarea(attr) {
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
                    <textarea placeholder='${attr.display.placeholder}' rows='${attr.display.rows}' readonly 
                    style='border-color: ${attr.display["outline-color"]}; border-width: ${attr.display["outline-width"]}px;' 
                    ${attr.validate.required === "Y" ? "required" : ""}></textarea>
                </div>
            </div>
        `);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Dropdown.
     *
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

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Radio Button.
     *
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

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Checkbox.
     *
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

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Label.
     *
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
     * Image.
     *
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
     * Divider.
     *
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Divider(attr) {
        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%;'>
                    <hr style='border: ${attr.display.type} ${attr.display.width}px ${attr.display.color};'>
                </div>
            </div>
        `);
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * Date.
     *
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function Datebox(attr) {
        //날짜 포멧 변경
        let defaultFormatArr = attr.display['default'].split('|');
        let defaultInputVal = '';
        if (defaultFormatArr[0] === 'now') { 
            defaultInputVal = utils.getTimeStamp();
            defaultInputVal = defaultInputVal.split(' ')[0];
        } else if (defaultFormatArr[0] === 'datepicker') { 
            defaultInputVal = defaultFormatArr[1];
        } else if (defaultFormatArr[0] === 'date') { 
            defaultInputVal = utils.getTimeStamp(defaultFormatArr[1]); 
            defaultInputVal = defaultInputVal.split(' ')[0];
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
                    <input type='text' id='date-${attr.id}' placeholder='${formEditor.userData.defaultDateFormat}' value='${defaultInputVal}' readonly/>
                </div>
            </div>
        `);
        
        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
        //TODO: 데이터 포멧 변환
        dateTimePicker.initDatePicker('date-' + attr.id, formEditor.userData.defaultDateFormat, formEditor.userData.defaultLang);
    }

    /**
     * Time.
     *
     * @param {Object} attr 컴포넌트 속성성
    * @constructor
     */
    function Timebox(attr) {
        //시간 포멧 변경
        let defaultFormatArr = attr.display['default'].split('|');
        let defaultInputVal = '';
        if (defaultFormatArr[0] === 'now') { 
            defaultInputVal = utils.getTimeStamp();
            defaultInputVal = defaultInputVal.split(' ')[1];
        } else if (defaultFormatArr[0] === 'timepicker') { 
            defaultInputVal = defaultFormatArr[1];
        } else if (defaultFormatArr[0] === 'time') { 
            defaultInputVal = utils.getTimeStamp('', defaultFormatArr[1]); 
            defaultInputVal = defaultInputVal.split(' ')[1];
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
                    <input type='text' id='time-${attr.id}' placeholder='hh:mm' value='${defaultInputVal}' readonly/>
                </div>
            </div>
        `);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
        //TODO: 데이터 포멧 변환
        dateTimePicker.initTimePicker('time-' + attr.id, formEditor.userData.defaultTimeFormat);
    }

    /**
     * Date Time.
     *
     * @param {Object} attr 컴포넌트 속성
     * @constructor
     */
    function DateTimebox(attr) {
        //날짜 시간 포멧 변경
        let defaultFormatArr = attr.display['default'].split('|');
        let defaultInputVal = '';
        if (defaultFormatArr[0] === 'now') { 
            defaultInputVal = utils.getTimeStamp();
        } else if (defaultFormatArr[0] === 'datetimepicker') { 
            defaultInputVal = defaultFormatArr[1];
        } else if (defaultFormatArr[0] === 'datetime') { 
            defaultInputVal = utils.getTimeStamp(defaultFormatArr[1], defaultFormatArr[2]);
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
                    <input type='text' id='datetime-${attr.id}' placeholder='${formEditor.userData.defaultDateFormat + " hh:mm"}' value='${defaultInputVal}' readonly />
                </div>
            </div>
        `);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
        //TODO: 데이터 포멧 변환
        dateTimePicker.initDateTimePicker('datetime-' + attr.id, formEditor.userData.defaultDateFormat, formEditor.userData.defaultTimeFormat, formEditor.userData.defaultLang); 
    }

    /**
     * Fileupload.
     *
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
                <div class='field dropbox' style='flex-basis: 100%;'>
                    <p> drag and drop files or click to select </p>
                </div>
            </div>
        `);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        formPanel.appendChild(comp);
        this.domElem = comp;
    }

    /**
     * 컴포넌트 draw.
     * 
     * @param {String} compType 컴포넌트 타입
     * @param {Object} compData 컴포넌트 데이터
     */
    function draw(compType, compData) {
        let compAttr = { display: {} },
            compId = '';
        if (compData !== undefined) { //데이터로 불러온 컴포넌트
            compId = compData.id;
            compAttr = compData;
        } else { //신규 생성된 컴포넌트는 default 속성을 넣어줌
            compId = workflowUtil.generateUUID();
            compAttr.id = compId;
            compAttr.type = compType;
            
            let defaultAttr = defaultData[compType];
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
        }
        compAttr.display.order = ++componentIdx;
        
        let componentConstructor;
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
         }
         return componentConstructor;
    }
    /**
     * 컴포넌트 기본 속성 조회
     * 
     * @param type 컴포넌트 타입
     */
    function getDefaultAttribute(type) {
        return JSON.stringify(defaultData[type]);
    }
    /**
     * 좌측 세부 속성창에 출력될 컴포넌트 제목 객체 조회
     * 
     * @param type 컴포넌트 타입
     */
    function getTitle(type) {
        let search = {};
        for (let i = 0, len = componentTitles.length; i < len; i++) {
            let prop = componentTitles[i];
            if (prop.type === type) {
                search = prop;
                break;
            }
        }
        return search;
    }
    /**
     * 마지막 추가된 컴포넌트 순서 조회
     */
    function getLastIndex() {
        return componentIdx;
    }
    /**
     * 마지막 추가된 컴포넌트 순서 초기화
     * 
     * @param idx 컴포넌트 순서
     */
    function setLastIndex(idx) {
        componentIdx = idx;
    }
     /**
     * 컴포넌트 초기화
     */
    function init() {
        formPanel = document.getElementById('panel-form');
        
        //컴포넌트 기본 속성 조회 : '/assets/js/form/componentAttribute.json'
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
    exports.getTitle = getTitle;
    exports.getLastIndex = getLastIndex;
    exports.setLastIndex = setLastIndex;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
