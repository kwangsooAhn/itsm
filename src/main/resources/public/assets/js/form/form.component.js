/**
 * @projectDescription Form Designer Component Library
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.component = global.component || {})));
}(this, (function (exports) {
    'use strict';

    const componentNameList = [ //컴포넌트 명
            {'type': 'editbox', 'name': 'Edit Box', 'icon': ''},
            {'type': 'inputbox', 'name': 'Input Box', 'icon': ''},
            {'type': 'textbox', 'name': 'Text Box', 'icon': ''},
            {'type': 'dropdown', 'name': 'Dropdown', 'icon': ''},
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
    let renderOrder = 0;    // 컴포넌트 index = 출력 순서 생성시 사용
    let parent = null;
    let isForm = false;     //폼 양식 화면인지 여부
    let tooltipTemplate = null; // 폼 양식 툴팁 메뉴

    /**
     * Editbox 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Editbox(property) {
        this.id = property.componentId;
        this.name = 'Edit Box';
        this.type = 'editbox';
        this.property = property;
        this.renderOrder = property.display.order;
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="editable">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group" contenteditable="true" placeholder="${i18n.msg("form.help.component-placeholder")}"></div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Input Box 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function InputBox(property) {
        this.id = property.componentId;
        this.name = 'Input Box';
        this.type = 'inputbox';
        this.property = property;
        this.renderOrder = property.display.order;

        // 기본값 설정
        const defaultValueArr = property.display['default'].split('|'); // select|userId|아이디
        let defaultValue = (defaultValueArr[0] === 'none') ? defaultValueArr[1] : aliceForm.session[defaultValueArr[1]];
        if (isForm && defaultValueArr[0] !== 'none') { // 폼 양식 화면 세션 값 미출력
            defaultValue = defaultValueArr[2];
        } else {
            if (typeof property.value !== 'undefined') { // 처리할 문서일 경우
                defaultValue = property.value;
            }
        }
        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                        `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                        `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                        `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                            `${aliceJs.filterXSS(property.label.text)}` +
                        `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" style="--data-column: ${property.display.column};">` +
                    `<input type="text" placeholder="${aliceJs.filterXSS(property.display.placeholder)}" value="${aliceJs.filterXSS(defaultValue)}"` +
                    `${displayType === 'editableRequired' ? ' required' : ''}` +
                    ` maxlength="${property.validate.lengthMax}" minlength="${property.validate.lengthMin}"` +
                    ` regexp='${property.validate.regexp}' regexp-msg='${aliceJs.filterXSS(property.validate.regexpMsg)}' />` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Text Box 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function TextBox(property) {
        this.id = property.componentId;
        this.name = 'Text Box';
        this.type = 'textbox';
        this.property = property;
        this.renderOrder = property.display.order;

        const editorUseYn = property.display.editorUseYn;
        let defaultValue = '';
        if (typeof property.value !== 'undefined') {
            defaultValue = (editorUseYn && property.value !== '') ? JSON.parse(property.value) : property.value;
        }
        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" style="--data-column: ${property.display.column};">` +
                `${editorUseYn ? 
                    `<div>` +
                        `<div id="editor" class="editor-container" style="--data-row: ${property.display.rows};"` +
                        `${displayType === 'editableRequired' ? ' required' : ''}` +
                        ` maxlength="${property.validate.lengthMax}" minlength="${property.validate.lengthMin}">` +
                        `</div>` +
                    `</div>` : 
                    `<textarea placeholder="${aliceJs.filterXSS(property.display.placeholder)}" rows="${property.display.rows}"` +
                    `style="--data-row: ${property.display.rows};" ${displayType === 'editableRequired' ? ' required' : ''}` +
                    ` maxlength="${property.validate.lengthMax}" minlength="${property.validate.lengthMin}">` +
                        `${aliceJs.filterXSS(defaultValue)}` +
                    `</textarea>`}` +
                `</div>` +
            `</div>` +
         `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // quill editor 초기화
        if (editorUseYn) {
            const textboxElement = parent.lastChild;
            const editorContainer = textboxElement.querySelector('.editor-container');
            let editorOptions = {
                modules: {
                    toolbar: [
                        [{'header': [1, 2, 3, 4, false]}],
                        ['bold', 'italic', 'underline'],
                        [{'color': []}, {'background': []}],
                        [{'align': []}, { 'list': 'bullet' }],
                        ['image']
                    ]
                },
                placeholder: property.display.placeholder,
                theme: 'snow',
                readOnly: isForm            //폼 양식 편집 화면에서는 editor를 편집할 수 없다.
            };
            const editor = new Quill(editorContainer, editorOptions);
            editor.setContents(defaultValue);
        }

    }

    /**
     * Dropdown 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Dropdown(property) {
        this.id = property.componentId;
        this.name = 'Dropdown';
        this.type = 'dropdown';
        this.property = property;
        this.renderOrder = property.display.order;

        const optionsTemplate = property.option.map(function (opt) {
            const isMatch = (typeof property.value !== 'undefined' && opt.value === property.value);
            return `<option value="${aliceJs.filterXSS(opt.value)}" data-seq="${opt.seq}" ${isMatch ? "selected='true'" : ""}>${aliceJs.filterXSS(opt.name)}</option>`;
        }).join('');

        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" style="--data-column: ${property.display.column};">` +
                    `<select ${displayType === 'editableRequired' ? 'required' : ''}>` +
                        `${optionsTemplate}` +
                    `</select>` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Radio Button 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Radiobox(property) {
        this.id = property.componentId;
        this.name = 'Radio Button';
        this.type = 'radio';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];
        const optionsTemplate = property.option.map(function (opt) {
            return `<label class="field-radio radio-group" for='radio-${property.componentId}-${opt.seq}'>` +
                `${(property.display.position === 'right') ?
                    `<input type="radio" id="radio-${property.componentId}-${opt.seq}" name="radio-${property.componentId}" value="${aliceJs.filterXSS(opt.value)}"` +
                    `${(typeof property.value !== 'undefined' && opt.value === property.value) ? " checked='true'" : ""}` +
                    `${displayType === 'readonly' ? ' disabled' : ''}><span></span>` +
                    `<span>${aliceJs.filterXSS(opt.name)}</span>` :
                    `<span>${aliceJs.filterXSS(opt.name)}</span>` +
                    `<input type="radio" id="radio-${property.componentId}-${opt.seq}" name="radio-${property.componentId}" value="${aliceJs.filterXSS(opt.value)}"` +
                    `${(typeof property.value !== 'undefined' && opt.value === property.value) ? " checked='true'" : ""}` +
                    `${displayType === 'readonly' ? ' disabled' : ''}><span></span>`
                 }` +
                `</label>`;
        }).join('');

        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div id="radio" class="field-content ${property.display.direction}" style="--data-column: ${property.display.column};"` +
                `${displayType === 'editableRequired' ? 'required' : ''}>` +
                    `${optionsTemplate}` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Checkbox 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Checkbox(property) {
        this.id = property.componentId;
        this.name = 'Checkbox';
        this.type = 'checkbox';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];
        const checkboxValueArr = (typeof property.value !== 'undefined' && property.value !== '') ? JSON.parse(property.value) : [];
        const optionsTemplate = property.option.map(function (opt) {
            return `<label class="field-checkbox checkbox-group"  for='checkbox-${property.componentId}-${opt.seq}'>` +
                `${(property.display.position === 'right') ?
                    `<input type="checkbox" id="checkbox-${property.componentId}-${opt.seq}" name="checkbox-${property.componentId}" value="${aliceJs.filterXSS(opt.value)}"` +
                    `${(checkboxValueArr.indexOf(opt.value) > -1) ? " checked='true'" : ""}` +
                    `${displayType === 'readonly' ? ' disabled' : ''}><span></span>` +
                    `<span>${aliceJs.filterXSS(opt.name)}</span>` :
                    `<span>${aliceJs.filterXSS(opt.name)}</span>` +
                    `<input type="checkbox" id="checkbox-${property.componentId}-${opt.seq}" name="checkbox-${property.componentId}" value="${aliceJs.filterXSS(opt.value)}"` +
                    `${(checkboxValueArr.indexOf(opt.value) > -1) ? " checked='true'" : ""}` +
                    `${displayType === 'readonly' ? ' disabled' : ''}><span></span>`
                }` +
                `</label>`;
        }).join('');

        this.template =
            `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
                `<div class="move-handler"></div>` +
                `<div class="field-group">` +
                    `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                        `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                        `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                        `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                        `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                            `${aliceJs.filterXSS(property.label.text)}` +
                        `</div>` +
                        `<span class="required"></span>` +
                    `</div>` +
                    `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                    `<div id="chkbox" class="field-content ${property.display.direction}" style="--data-column: ${property.display.column};"` +
                    `${displayType === 'editableRequired' ? 'required' : ''}>` +
                        `${optionsTemplate}` +
                    `</div>` +
                `</div>` +
            `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Label 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Label(property) {
        this.id = property.componentId;
        this.name = 'Label';
        this.type = 'label';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.display.align}" >` +
                    `<div class="label" style="color: ${property.display.color}; font-size: ${property.display.size}px;` +
                    `${property.display.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.display.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.display.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.display.text)}` +
                    `</div>` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Image 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Imagebox(property) {
        this.id = property.componentId;
        this.name = 'Image';
        this.type = 'image';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];
        const imageSrc = property.display.path;
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
                `<div class="field-group">` +
                `<div class="field-content ${property.display.align}">` +
                    `<img class="field-img" id="imagebox-${this.id}" src="" alt="" data-path="${aliceJs.filterXSS(imageSrc)}" width="${property.display.width}" height="${property.display.height}">` +
                    `<div class="img-placeholder"><img/><p>Select Your Image</p></div>` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // 이미지 팝업
        if (imageSrc.startsWith('file:///')) {
            aliceJs.sendXhr({
                method: 'get',
                url: '/rest/images/' + imageSrc.split('file:///')[1],
                contentType: 'application/json; charset=utf-8',
                callbackFunc: xhr => {
                    const responseText = xhr.responseText;
                    if (responseText !== '') {
                        const image = JSON.parse(responseText);
                        parent.querySelector('#imagebox-' + property.componentId).src = 'data:image/' + image.extension + ';base64,' + image.data;
                    }
                }
            });
        } else {
            parent.querySelector('#imagebox-' + property.componentId).src = imageSrc;
        }
    }

    /**
     * Divider 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Divider(property) {
        this.id = property.componentId;
        this.name = 'Divider';
        this.type = 'divider';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-content">` +
                    `<hr style="border-top: ${property.display.type} ${property.display.thickness}px ${property.display.color};">` +
                `</div>` +
            `</div>` +
        `</div>`;
        parent.insertAdjacentHTML('beforeend', this.template);
    }

    /**
     * Date 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function DateBox(property) {
        this.id = property.componentId;
        this.name = 'Date';
        this.type = 'date';
        this.property = property;
        this.renderOrder = property.display.order;

        // 기본값 설정
        const defaultValueArr = property.display['default'].split('|');
        let defaultValue = '';
        //처리할 문서는 사용자 포멧에 맞게 변환된 실 데이터를 출력한다. (form.core.js 의 reformatCalendarFormat()에서 처리한 데이터)
        if (typeof property.value !== 'undefined') {
            defaultValue = property.value;
        } else { // 신청서는 default값 출력한다.
            switch(defaultValueArr[0]) {
                case 'now':
                    defaultValue = i18n.getDate();
                    break;
                case 'date':
                    let offset = {};
                    offset.days =  aliceJs.isEmpty(defaultValueArr[1]) ? 0 : Number(defaultValueArr[1]);
                    defaultValue = i18n.getDate(offset);
                    break;
                case 'datepicker':
                    if (!aliceJs.isEmpty(defaultValueArr[1])) {
                        defaultValue = defaultValueArr[1];
                    }
                    break;
                default: //none
            }
        }
        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" style="--data-column: ${property.display.column};">` +
                    `<input type="text" class="datepicker" id="date-${this.id}" placeholder="${i18n.dateFormat}" value="${defaultValue}"` +
                    `${displayType === 'editableRequired' ? ' required' : ''}` +
                    ` date-max="${property.validate.dateMax}" date-min="${property.validate.dateMin}" />` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // data picker 초기화
        if (!isForm) {
            dateTimePicker.initDatePicker('date-' + property.componentId, function () {
                aliceDocument.checkValidate(document.getElementById('date-' + property.componentId));
            });
        }
    }

    /**
     * Time 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성성
     * @constructor
     */
    function TimeBox(property) {
        this.id = property.componentId;
        this.name = 'Time';
        this.type = 'time';
        this.property = property;
        this.renderOrder = property.display.order;

        // 기본값 설정
        const defaultValueArr = property.display['default'].split('|');
        let defaultValue = '';
        //처리할 문서는 사용자 포멧에 맞게 변환된 실 데이터를 출력한다. (form.core.js 의 reformatCalendarFormat()에서 처리한 데이터)
        if (typeof property.value !== 'undefined') {
            defaultValue = property.value;
        } else { // 신청서는 default값 출력한다.
            switch(defaultValueArr[0]) {
                case 'now':
                    defaultValue = i18n.getTime();
                    break;
                case 'time':
                    let offset = {};
                    offset.hours =  aliceJs.isEmpty(defaultValueArr[1]) ? 0 : Number(defaultValueArr[1]);
                    defaultValue = i18n.getTime(offset);
                    break;
                case 'timepicker':
                    if (!aliceJs.isEmpty(defaultValueArr[1])) {
                        defaultValue = defaultValueArr[1];
                    }
                    break;
                default: //none
            }
        }
        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" style="--data-column: ${property.display.column};">` +
                    `<input type="text" class="timepicker" id="time-${this.id}" placeholder="${i18n.timeFormat}" value="${defaultValue}"` +
                    `${displayType === 'editableRequired' ? ' required' : ''}` +
                    ` time-max="${property.validate.timeMax}" time-min="${property.validate.timeMin}" />` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // time picker 초기화
        if (!isForm) {
            dateTimePicker.initTimePicker('time-' + property.componentId, function () {
                aliceDocument.checkValidate(document.getElementById('time-' + property.componentId));
            });
        }
    }

    /**
     * Date Time 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function DatetimeBox(property) {
        this.id = property.componentId;
        this.name = 'Date Time';
        this.type = 'datetime';
        this.property = property;
        this.renderOrder = property.display.order;

        // 기본값 설정
        const defaultValueArr = property.display['default'].split('|');
        let defaultValue = '';
        // 처리할 문서는 사용자 포멧에 맞게 변환된 실 데이터를 출력한다. (form.core.js 의 reformatCalendarFormat()에서 처리한 데이터)
        if (typeof property.value !== 'undefined') {
            defaultValue = property.value;
        } else { // 신청서는 default값 출력한다.
            switch(defaultValueArr[0]) {
                case 'now':
                    defaultValue = i18n.getDateTime();
                    break;
                case 'datetime':
                    let offset = {};
                    offset.days =  aliceJs.isEmpty(defaultValueArr[1]) ? 0 : Number(defaultValueArr[1]);
                    offset.hours = aliceJs.isEmpty(defaultValueArr[2]) ? 0 : Number(defaultValueArr[2]);
                    defaultValue = i18n.getDateTime(offset);
                    break;
                case 'datetimepicker':
                    if (!aliceJs.isEmpty(defaultValueArr[1])) {
                        defaultValue = defaultValueArr[1];
                    }
                    break;
                default: //none
            }
        }
        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" style="--data-column: ${property.display.column};">` +
                    `<input type="text" class="datetimepicker" id="datetime-${this.id}" placeholder="${i18n.dateTimeFormat}" value="${defaultValue}"` +
                    `${displayType === 'editableRequired' ? ' required' : ''}` +
                    ` datetime-max="${property.validate.datetimeMax}" datetime-min="${property.validate.datetimeMin}" />` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // datetime picker 초기화
        if (!isForm) {
            dateTimePicker.initDateTimePicker('datetime-' + property.componentId, function () {
                aliceDocument.checkValidate(document.getElementById('datetime-' + property.componentId));
            });
        }
    }

    /**
     * Fileupload 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Fileupload(property) {
        this.id = property.componentId;
        this.name = 'File Upload';
        this.type = 'fileupload';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" id="fileupload"${displayType === 'editableRequired' ? ' required' : ''} style="--data-column: ${property.display.column};">` +
                    `<div id="dropZoneFiles-${this.id}"></div>` +
                    `<div id="dropZoneUploadedFiles-${this.id}">` +
                        `<div class="dropzone">` +
                            `<div class="dz-default dz-message">` +
                                `<span>${i18n.msg('file.msg.upload')}</span>` +
                                ` <div class="add-file-button-wrap"><span>${i18n.msg('file.label.or')} </span><span class="add-file-button dz-clickable">${i18n.msg('file.msg.browser')}</span></div>` +
                            `</div>` +
                        `</div>` +
                    `</div>` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // 드랍존 초기화
        if (!isForm) {
            document.getElementById('dropZoneUploadedFiles-' + this.id).innerHTML = '';
            let fileOptions = {
                extra: {
                    formId: 'frm',
                    ownId: '',
                    dropZoneFilesId: 'dropZoneFiles-' + this.id,
                    dropZoneUploadedFilesId: 'dropZoneUploadedFiles-' + this.id,
                    editor: (displayType !== 'readonly')
                }
            };
            if (typeof property.value !== 'undefined') {
                fileOptions.extra.fileDataIds = property.value;
            }
            fileUploader.init(fileOptions);
        }
    }
    /**
     * Custom-Code 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function CustomCode(property) {
        this.id = property.componentId;
        this.name = 'Custom Code';
        this.type = 'custom-code';
        this.property = property;
        this.renderOrder = property.display.order;

        // 기본값 설정
        const defaultValueArr = property.display['default'].split('|');
        let defaultValue = (isForm && defaultValueArr[0] !== 'none') ? defaultValueArr[2] : ''; // 폼 디자이너, 미리보기용
        let defaultCustomData = ''; // 신청서 작성 및 처리할 문서
        if (typeof property.value !== 'undefined') {
            defaultCustomData = property.value;
            if (!isForm) {
                let customDataValue = defaultCustomData.split('|');
                defaultValue = (customDataValue.length > 1) ? customDataValue[1] : '';
            }
        } else {
            if (defaultValueArr[0] !== 'none') {
                defaultCustomData = defaultValueArr[1] + '|' + defaultValueArr[2];
                if (!isForm) { // 임시
                    defaultValue = defaultValueArr[2];
                }
                if (defaultValueArr[0] === 'session') {
                    switch (defaultValueArr[1]) {
                        case 'userName':
                            defaultCustomData = aliceForm.session.userKey;
                            break;
                        case 'department':
                            defaultCustomData = aliceForm.session.department;
                            break;
                    }
                    defaultCustomData += '|' + aliceForm.session[defaultValueArr[1]];
                    if (!isForm) { // 임시
                        defaultValue = aliceForm.session[defaultValueArr[1]];
                    }
                }
            }
        }

        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label ${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<div class="label" style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</div>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content custom-code" style="--data-column: ${property.display.column};">` +
                    `<input class="custom-code-text" type="text" id="custom-code-${this.id}" custom-data="${defaultCustomData}" value="${aliceJs.filterXSS(defaultValue)}"` +
                    `${displayType === 'editableRequired' ? ' required' : ''} readonly />` +
                    `<input type="button" id="codeBtn-${this.id}" value="${aliceJs.filterXSS(property.display.buttonText)}">` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // custom-code 초기화
        if (!isForm) {
            const searchBtn = parent.querySelector('#codeBtn-' + property.componentId);
            searchBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                const customCodeTextElem = parent.querySelector('#custom-code-' + property.componentId);
                if (defaultCustomData !== '') {
                    let customDataValue = defaultCustomData.split('|');
                    customCodeTextElem.value = (customDataValue.length > 1) ? customDataValue[1] : '';
                }
                let customCodeData = {
                    componentId: property.componentId,
                    componentValue: customCodeTextElem.getAttribute('custom-data')
                };
                const itemName = 'alice_custom-codes-search-' + property.componentId;
                sessionStorage.setItem(itemName, JSON.stringify(customCodeData));
                let url = '/custom-codes/' + property.display.customCode + '/search';
                window.open(url, itemName, 'width=500, height=655');
            });
        }
    }

    /**
     * 컴포넌트를 생성하고 출력한다.
     * @param {String} type 컴포넌트 타입
     * @param {Object} data 컴포넌트 데이터
     * @return {Object} 생성된 컴포넌트 객체
     */
    function draw(type, data) {
        if (typeof parent === 'undefined') { return false; }

        let componentProperty = {'display': {}};
        if (typeof data !== 'undefined' && data !== null) { //기존 저장된 컴포넌트 속성이 존재할 경우
            componentProperty = data;
        } else {                     //신규 생성된 컴포넌트일 경우
            componentProperty.componentId = workflowUtil.generateUUID();
            componentProperty.type = type;

        }
        componentProperty = getPropertiesWithType(type, componentProperty);
        componentProperty.display.order = ++renderOrder;

        let componentObject = null;
        switch(type) {
            case 'editbox':
                componentObject = new Editbox(componentProperty);
                break;
            case 'inputbox':
                componentObject =  new InputBox(componentProperty);
                break;
            case 'textbox':
                componentObject =  new TextBox(componentProperty);
                break;
            case 'dropdown':
                componentObject =  new Dropdown(componentProperty);
                break;
            case 'radio':
                componentObject =  new Radiobox(componentProperty);
                break;
            case 'checkbox':
                componentObject =  new Checkbox(componentProperty);
                break;
            case 'label':
                componentObject =  new Label(componentProperty);
                break;
            case 'image':
                componentObject =  new Imagebox(componentProperty);
                break;
            case 'divider':
                componentObject =  new Divider(componentProperty);
                break;
            case 'date':
                componentObject =  new DateBox(componentProperty);
                break;
            case 'time':
                componentObject =  new TimeBox(componentProperty);
                break;
            case 'datetime':
                componentObject =  new DatetimeBox(componentProperty);
                break;
            case 'fileupload':
                componentObject =  new Fileupload(componentProperty);
                break;
            case 'custom-code':
                componentObject =  new CustomCode(componentProperty);
                break;
            default:
                break;
        }

        let componentElement = parent.lastChild;
        if (componentObject && componentElement) {
            if (tooltipTemplate) {
                const tooltipElem = tooltipTemplate.content.cloneNode(true);
                componentElement.appendChild(tooltipElem);
            }
            componentObject.domElem = componentElement;
        }
        return componentObject;
    }

    /**
     * 우측 세부 속성창(properties panel)에 출력될 컴포넌트 제목 객체 조회
     * @param {String} type 컴포넌트 타입
     * @return {Object} match title 일치하는 제목 객제
     */
    function getName(type) {
        return componentNameList.find(c => { return c.type === type; });
    }

    /**
     * 컴포넌트 기본 속성을 읽어서 폼 저장을 위한 컴포넌트의 데이터로 정제하여 조회한다.
     * @param {String} type 컴포넌트 타입
     * @param {Object} data 컴포넌트 데이터
     * @return {Object} refineProperty 정제된 컴포넌트 데이터
     */
    function getPropertiesWithType(type, data) {
        let refineProperty = { 'display': {} };
        if (typeof aliceForm.componentProperties[type] !== 'undefined') {
            let defaultProperty = JSON.parse(JSON.stringify(aliceForm.componentProperties[type]));
            Object.keys(defaultProperty).forEach(function(group) {
                if (group === 'option') { //옵션 json 구조 변경
                    let options = [];
                    for (let i = 0, len = defaultProperty[group][0].items.length; i < len; i+=3) {
                        let option = {};
                        for (let j = i; j < i + len; j++) {
                            let child = defaultProperty[group][0].items[j];
                            option[child.id] = child.value;
                        }
                        options.push(option);
                    }
                    refineProperty[group] = options;
                } else {
                    refineProperty[group] = {};
                    Object.keys(defaultProperty[group]).forEach(function(child) {
                        const attributeItem = defaultProperty[group][child];
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
                        refineProperty[group][attributeItem.id] = attributeItemValue;
                    });
                }
            });

            if (typeof data !== 'undefined') {
                refineProperty = aliceJs.mergeObject(refineProperty, data) ;
            }
        }
        return refineProperty;
    }

    /**
     * 마지막 추가된 컴포넌트 Index 조회
     * @return {Number} 마지막 컴포넌트 index
     */
    function getLastIndex() {
        return renderOrder;
    }
    /**
     * 컴포넌트 Index 초기화
     * @param {Number} idx 초기화할 값
     */
    function setLastIndex(idx) {
        renderOrder = idx;
    }

    /**
     * 컴포넌트를 추가할 대상을 지정한다.
     * @param target 컴포넌트를 추가할 대상 (폼 또는 문서)
     */
    function init(target) {
        parent = target;
        isForm = target.hasAttribute('data-readonly');
        if (isForm) {
            tooltipTemplate = document.getElementById('tooltip-template');
        }
    }

    exports.init = init;
    exports.draw = draw;
    exports.getLastIndex = getLastIndex;
    exports.setLastIndex = setLastIndex;
    exports.getName = getName;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
