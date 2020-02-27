(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.component = global.component || {})));
}(this, (function (exports) {
    'use strict';

    const defaultColWidth = 8.33,  //폼 패널을 12등분하였을때, 1개의 너비
          editboxPlaceholder= '+ Typing for add component',
          componentTitles = [  //세부속성에서 사용할 제목
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
        componentIdx = 0,          //컴포넌트 index = 출력 순서 생성시 사용
        data = {};
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
        getTimeStamp: function() {
            let today = new Date();
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

    function Editbox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group' contenteditable='true' placeholder='${editboxPlaceholder}'></div>
        `);
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        this.domElem = comp;
    }

    function Text(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

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
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }

        this.domElem = comp;
    }

    function Textarea(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

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
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }

        this.domElem = comp;
    }

    function Selectbox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

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
            selectbox.appendChild(option);
        }
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }

        this.domElem = comp;
    }

    function Radiobox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

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
            optionRadio.value = attr.option[i].value;
            optionRadio.name = attr.option[i].name;
            
            if (i === 0) { optionRadio.setAttribute('checked', 'checked'); }
            
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
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }

        this.domElem = comp;
    }

    function Checkbox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

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
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }

        this.domElem = comp;
    }

    function Label(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

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
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        this.domElem = comp;
    }

    function Imagebox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

        let comp = utils.createComponentByTemplate(`
            <img class='move-icon' src=''>
            <div class='group'>
                <div class='field' style='flex-basis: 100%;'>
                    <img src='${attr.display.path}' alt='' width='${attr.display.width}' height='${attr.display.height}'>
                </div>
            </div>
        `);
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        this.domElem = comp;
    }

    function Line(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%;'>
                    <hr style='border: '${attr.display.type} ${attr.display.width}px  ${attr.display.color};'>
                </div>
            </div>
        `);
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        this.domElem = comp;
    }

    function Datebox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

        let defaultDate = attr.display['default'];
        if (defaultDate === 'today') { 
            defaultDate = utils.getTimeStamp(); 
            defaultDate = defaultDate.split(' ')[0];
        }
        defaultDate = changeDateFormatYYYYMMDD(defaultDate, attr.display.format);

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
                    <input type='text' id='date-${attr.id}' placeholder='${attr.display.format}' value='${defaultDate}'/>
                </div>
            </div>
        `);
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }

        this.domElem = comp;
        dateTimePicker.initDatePicker('date-' + attr.id, attr.display.format);
    }

    function Timebox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

        let defaultTime = attr.display['default'];
        if (defaultTime === 'now') { 
            defaultTime = utils.getTimeStamp(); 
            defaultTime = changeDateFormatYYYYMMDD(defaultTime, 'yyyy-MM-dd ' + attr.display.format);
            defaultTime = defaultTime.split(' ')[1];
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
                    <input type='text' id='time-${attr.id}' placeholder='${attr.display.format}' value='${defaultTime}'/>
                </div>
            </div>
        `);
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }
        
        this.domElem = comp;

        dateTimePicker.initTimePicker('time-' + attr.id, attr.display.format);
    }

    function DateTimebox(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

        let defaultDateTime = attr.display['default'];
        if (defaultDateTime === 'now') { 
            defaultDateTime = utils.getTimeStamp();
        }
        defaultDateTime = changeDateFormatYYYYMMDD(defaultDateTime, attr.display.format);

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
                    <input type='text' id='datetime-${attr.id}' placeholder='${attr.display.format}' value='${defaultDateTime}' />
                </div>
            </div>
        `);
        
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        if (attr.label.position === 'hidden') {
            comp.querySelector('.group').firstElementChild.style.display = 'none';
        } else if (attr.label.position === 'left') {
            comp.querySelector('.group').firstElementChild.style.flexBasis = (defaultColWidth * Number(attr.label.column)) + '%';
            comp.querySelector('.group').lastElementChild.style.flexBasis = (defaultColWidth * Number(attr.display.column)) + '%';
        }

        this.domElem = comp;
        dateTimePicker.initDateTimePicker('datetime-' + attr.id, attr.display.format);
    }

    function Fileupload(id, type, attr) {
        this.attr = attr;
        this.id = id;
        this.type = type;

        let comp = utils.createComponentByTemplate(`
            <div class='move-icon'></div>
            <div class='group'>
                <div class='field' style='flex-basis: 100%;'>
                    <input type='file' name='files[]' multiple />
                </div>
            </div>
        `);
        comp.setAttribute('id', id);
        comp.setAttribute('data-index', attr.display.order);
        comp.setAttribute('tabIndex', attr.display.order);
        formPanel.appendChild(comp);

        this.domElem = comp;
    }

    /**
     * 컴포넌트 draw
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
            
            let defaultAttr = component.data[compType];
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
        
        switch(compType) {
            case 'editbox':
              return new Editbox(compId, compType, compAttr);
            case 'text':
              return new Text(compId, compType, compAttr);
            case 'textarea':
                return new Textarea(compId, compType, compAttr);
            case 'select':
                return new Selectbox(compId, compType, compAttr);
            case 'radio':
                return new Radiobox(compId, compType, compAttr);
            case 'checkbox':
                return new Checkbox(compId, compType, compAttr);
            case 'label':
                return new Label(compId, compType, compAttr);
            case 'image':
                return new Imagebox(compId, compType, compAttr);
            case 'line':
                return new Line(compId, compType, compAttr);
            case 'date':
                return new Datebox(compId, compType, compAttr);
            case 'time':
                return new Timebox(compId, compType, compAttr);
            case 'datetime':
                return new DateTimebox(compId, compType, compAttr);
            case 'fileupload':
                return new Fileupload(compId, compType, compAttr);
         }
    }
    /**
     * 컴포넌트 기본 속성 조회
     * 
     * @param type 컴포넌트 타입
     */
    function getDefaultAttribute(type) {
        return component.data[type];
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
     * 컴포넌트 기본 속성 조회 : '/assets/js/form/componentAttribute.json'
     */
    function loadDefaultAttribute(data) {
        component.data = JSON.parse(data);
    }

     /**
     * 컴포넌트 초기화
     */
    function init() {
        formPanel = document.getElementById('panel-form');
        
        //load component default data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentAttribute.json',
            callbackFunc: function(xhr) {
                loadDefaultAttribute(xhr.responseText)
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
