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

    const DATA_ATTRIBUTE_LABEL_LIST = 'labelList';
    const componentNameList = [ //컴포넌트 명
            {'type': 'editbox', 'name': 'Edit Box'},
            {'type': 'inputbox', 'name': 'Input Box'},
            {'type': 'textbox', 'name': 'Text Box'},
            {'type': 'dropdown', 'name': 'Dropdown'},
            {'type': 'radio', 'name': 'Radio Button'},
            {'type': 'checkbox', 'name': 'Checkbox'},
            {'type': 'label', 'name': 'Label'},
            {'type': 'image', 'name': 'Image'},
            {'type': 'divider', 'name': 'Divider'},
            {'type': 'date', 'name': 'Date'},
            {'type': 'time', 'name': 'Time'},
            {'type': 'datetime', 'name': 'Date Time'},
            {'type': 'fileupload', 'name': 'File Upload'},
            {'type': 'custom-code', 'name': 'Custom Code'},
            {'type': 'dynamic-row-table', 'name': 'Dynamic Row Table'},
            {'type': 'accordion', 'name': 'Accordion'}
    ];
    let renderOrder = 0;    // 컴포넌트 index = 출력 순서 생성시 사용
    let parent = null;
    let isReadOnly = false;  // 편집 가능한지 여부 : 폼 양식 화면, preview, 완료된 문서일 경우 true 이다.
    let formType = 'form';   // 폼양식, 신청서, 처리할 문서, 완료된 문서 등 프린트 문서 타입
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
        let defaultValue = (defaultValueArr[0] === 'none') ? defaultValueArr[1] : (defaultValueArr[1] === 'department' ? aliceForm.session['departmentName'] : aliceForm.session[defaultValueArr[1]]);
        if (formType === 'form' && defaultValueArr[0] !== 'none') { // 폼 양식 화면 세션 값 미출력
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                        `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                        `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                        `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                            `${aliceJs.filterXSS(property.label.text)}` +
                        `</label>` +
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
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
        const textboxElement = parent.lastChild;
        if (editorUseYn) {
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
                readOnly: isReadOnly            //폼 양식 편집 화면, 완료된 문서 등에서는 editor를 편집할 수 없다.
            };
            const editor = new Quill(editorContainer, editorOptions);
            editor.setContents(defaultValue);
        } else {
            // scrollbar 적용
            textboxElement.querySelector("textarea").className = 'textarea-scroll-wrapper';
            OverlayScrollbars(textboxElement.querySelector("textarea"), {
                className: 'inner-scrollbar',
                resize: 'vertical',
                sizeAutoCapable: true,
                textarea: {
                    dynHeight: false,
                    dynWidth: false,
                    inheritedAttrs: "class"
                }
            });
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
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
            return `<label class="field-radio radio" for='radio-${property.componentId}-${opt.seq}'>` +
                `${(property.display.position === 'right') ?
                    `<input type="radio" id="radio-${property.componentId}-${opt.seq}" name="radio-${property.componentId}" value="${aliceJs.filterXSS(opt.value)}"` +
                    `${(typeof property.value !== 'undefined' && opt.value === property.value) ? " checked='true'" : ""}` +
                    `${displayType === 'readonly' ? ' disabled' : ''}><span></span>` +
                    `<span class="label">${aliceJs.filterXSS(opt.name)}</span>` :
                    `<span class="label">${aliceJs.filterXSS(opt.name)}</span>` +
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div id="radio" class="field-content ${property.display.direction}" style="--data-column: ${property.display.column};"` +
                `${displayType === 'editableRequired' ? 'required' : ''} tabindex="0">` +
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
            return `<label class="field-checkbox checkbox"  for='checkbox-${property.componentId}-${opt.seq}'>` +
                `${(property.display.position === 'right') ?
                    `<input type="checkbox" id="checkbox-${property.componentId}-${opt.seq}" name="checkbox-${property.componentId}" value="${aliceJs.filterXSS(opt.value)}"` +
                    `${(checkboxValueArr.indexOf(opt.value) > -1) ? " checked='true'" : ""}` +
                    `${displayType === 'readonly' ? ' disabled' : ''}><span></span>` +
                    `<span class="label">${aliceJs.filterXSS(opt.name)}</span>` :
                    `<span class="label">${aliceJs.filterXSS(opt.name)}</span>` +
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
                    `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                        `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                        `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                        `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                        `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                            `${aliceJs.filterXSS(property.label.text)}` +
                        `</label>` +
                        `<span class="required"></span>` +
                    `</div>` +
                    `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                    `<div id="chkbox" class="field-content ${property.display.direction}" style="--data-column: ${property.display.column};"` +
                    `${displayType === 'editableRequired' ? 'required' : ''} tabindex="0">` +
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
                `<div class="field-label align-${property.display.align}" >` +
                    `<label style="color: ${property.display.color}; font-size: ${property.display.size}px;` +
                    `${property.display.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.display.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.display.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.display.text)}` +
                    `</label>` +
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
                    `<div class="img-placeholder"><span class="icon-no-img"></span><p>Select Your Image</p></div>` +
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
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
        if (!isReadOnly) {
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
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
        if (!isReadOnly) {
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
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
        if (!isReadOnly) {
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
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content file-uploader-edit" id="fileupload"${displayType === 'editableRequired' ? ' required' : ''} style="--data-column: ${property.display.column};">` +
                    `<div id="dropZoneFiles-${this.id}"></div>` +
                    `<div id="dropZoneUploadedFiles-${this.id}">` +
                        `<div class="dropzone">` +
                            `<div class="dz-default dz-message">` +
                                `<span class="icon-no-file"></span>` +
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
        if (formType !== 'form' && formType !== 'preview') {
            document.getElementById('dropZoneUploadedFiles-' + this.id).innerHTML = '';
            let fileOptions = {
                extra: {
                    formId: 'frm',
                    ownId: '',
                    dropZoneFilesId: 'dropZoneFiles-' + this.id,
                    dropZoneUploadedFilesId: 'dropZoneUploadedFiles-' + this.id,
                    editor: (displayType !== 'readonly' && !isReadOnly),
                    isView: (displayType === 'readonly' || isReadOnly)
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
        const defaultValueArr = property.display['default'].split('|'); // none| , session|department|부서
        let defaultValue = '';
        let defaultCustomData = ''; // 신청서 작성 및 처리할 문서
        if (typeof property.value !== 'undefined') {
            if (property.value !== '') {
                let customDataValue = property.value.split('|');
                defaultValue = (customDataValue.length > 1) ? customDataValue[1] : '';
                defaultCustomData = property.value;
            }
        } else { // 폼 및 신청서는 default값 출력한다.
            switch(defaultValueArr[0]) {
                case 'session':
                    if (defaultValueArr[1] === 'userName') {
                        defaultCustomData = aliceForm.session.userKey + '|' + aliceForm.session['userName'];
                        defaultValue = aliceForm.session['userName'];
                    } else if (defaultValueArr[1] === 'department') {
                        defaultCustomData = aliceForm.session.department + '|' + aliceForm.session['departmentName'];
                        defaultValue = aliceForm.session['departmentName'];
                    }
                    if (formType === 'form' || formType === 'preview') {
                        defaultValue = defaultValueArr[2];
                    }
                    break;
                case 'code':
                    defaultCustomData = defaultValueArr[1] + '|' + defaultValueArr[2];
                    defaultValue = defaultValueArr[2];
                    break;
                default: //none
                    defaultCustomData = defaultValueArr[0] + '|';
                    break;
            }
        }

        const displayType = property['dataAttribute']['displayType'];
        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                    `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                    `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                    `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content custom-code" style="--data-column: ${property.display.column};">` +
                    `<input class="custom-code-text" type="text" id="custom-code-${this.id}" custom-data="${defaultCustomData}" value="${aliceJs.filterXSS(defaultValue)}"` +
                    `${displayType === 'editableRequired' ? ' required' : ''} readonly />` +
                    `<button type="button" class="ghost-line flex-fill" id="codeBtn-${this.id}">${aliceJs.filterXSS(property.display.buttonText)}</button>` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // custom-code 초기화
        if (formType !== 'form' && formType !== 'preview') {
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
     * Daynamic Row(DR) Table Cell 값 변경 및 유효성 검증 추가
     * @param {Object} elem 테이블의 cell
     * @param {String} value 값
     */
    function setDRTableCellData(elem, value) {
        const childElem  = elem.children[0];
        // TODO: DR Table 컴포넌트 내부는 inputbox, select, checkbox, radio 등으로 이루어진다. (추후 구현 예정)
        switch(childElem.type) {
            case 'text':
                childElem.value = value;
                // 신청서 및 처리할 문서 유효성 검증 추가
                if (!isReadOnly && !parent.hasAttribute('data-readonly')) {
                    childElem.addEventListener('focusout', function(e) {
                        aliceDocument.checkValidate(e.target);
                    }, false);
                }
            break;
            default:
            break
        }
    }

    /**
     * Daynamic Row(DR) Table Row 추가
     * @param {Object} elem 테이블
     * @param {Object} data 배열 데이터
     */
    function addDRTableRow(elem, data) {
        const row = document.createElement('tr');
        row.innerHTML = elem.rows[1].innerHTML; // 첫번째 열을 그대로 복사
        elem.getElementsByTagName('tbody')[0].appendChild(row);

        // 값 초기화
        for (let cellIndex = 0, cellLen= row.cells.length; cellIndex < cellLen; cellIndex++) {
            const cell = row.cells[cellIndex];
            setDRTableCellData(cell, (typeof data !== 'undefined' ? data[cellIndex] : ''));

            if (cellIndex === (cellLen - 1)) {
                // row 삭제 버튼 추가
                let rowDeleteDiv = document.createElement('div');
                rowDeleteDiv.className = 'dr-table-row-delete';

                let rowDeleteBtn = document.createElement('button');
                rowDeleteBtn.type = 'button';
                rowDeleteBtn.className = 'btn-option btn-dr-table-row-delete';

                let spanRowDelete = document.createElement('span');
                spanRowDelete.className = 'icon-fail';
                rowDeleteBtn.addEventListener('click', function(e) {
                    e.stopPropagation();

                    const elem = aliceJs.clickInsideElement(e, 'btn-option');
                    const deleteRow = elem.parentNode.parentNode.parentNode;
                    deleteRow.parentNode.removeChild(deleteRow);
                });
                rowDeleteBtn.appendChild(spanRowDelete);
                rowDeleteDiv.appendChild(rowDeleteBtn);
                cell.appendChild(rowDeleteDiv);
            }
        }
    }

    /**
     * Daynamic Row(DR) Table 컴포넌트 타입에 따라 field 별 세부 html 반환
     * @param {String} type 타입
     * @param {Object} property 타입별 속성
     * @param {String} displayType 신청서 양식 타입
     * @return Template Literal 템플릿 리터럴
     */
    function getFieldTemplate(type, property, displayType) {
        // inputbox, select, radio, checkbox 등 추후 구현 예정
        switch(type) {
            case 'inputbox':
                return `<input type="text" class="align-${property.display.align}" ` +
                    `placeholder="${aliceJs.filterXSS(property.display.placeholder)}" value=""` +
                    `${displayType === 'editableRequired' ? ' required' : ''}` +
                    ` maxlength="${property.validate.lengthMax}" minlength="${property.validate.lengthMin}"` +
                    ` regexp='${property.validate.regexp}' regexp-msg='${aliceJs.filterXSS(property.validate.regexpMsg)}' />`;
                break;
            default:
            break;
        }
    }

    /**
     * Daynamic Row(DR) Table 컴포넌트 속성
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function DaynamicRowTable(property) {
        this.id = property.componentId;
        this.name = 'Dynamic Row Table';
        this.type = 'dynamic-row-table';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];

        // 테이블 Header 추가
        const tableHeaderOptions = property.field.map(function(opt, idx) {
            const thWidth = (Number(opt.column) / 12) * 100; // table이 100%를 12 등분하였을때 차지하는 너비의 퍼센트 값
            return `<th data-field-type="${opt.type}" data-field-index="${idx}" class="align-${property.header.align}" ` +
                        `style="width: ${thWidth}%; border-color: ${property.display.border}; ` +
                        `color: ${property.header.color}; font-size: ${property.header.size}px;` +
                        `${property.header.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                        `${property.header.italic === 'Y' ? ' font-style: italic;' : ''}` +
                        `${property.header.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                        `${aliceJs.filterXSS(opt.text)}` +
                    `</th>`;
        }).join('');

        // 테이블 Row 추가
        const tableRowOptions = property.field.map(function(opt, idx) {
            const tdWidth = (Number(opt.column) / 12) * 100; // table이 100%를 12 등분하였을때 차지하는 너비의 퍼센트 값
            return `<td style="width: ${tdWidth}%; border-color: ${property.display.border}">` +
                          `${getFieldTemplate(opt.type, opt, displayType)}` +
                    `</td>`;
        }).join('');

        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}">` +
            `<div class="move-handler"></div>` +
            `<div class="field-group">` +
                `<div class="field-label align-${property.label.align} ${property.label.position}" style="--data-column: ${property.label.column};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                        `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                        `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                        `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                            `${aliceJs.filterXSS(property.label.text)}` +
                        `</label>` +
                    `<span class="required"></span>` +
                `</div>` +
                `<div class="field-empty ${property.label.position}" style="--data-column: ${property.label.column};"></div>` +
                `<div class="field-content" style="--data-column: ${property.display.column};">` +
                    `<table class="dr-table" id="dr-table-${property.componentId}"` +
                    ` row-max="${property.display.rowMax}" row-min="${property.display.rowMin}">` +
                        `<thead>` +
                            `<tr>${tableHeaderOptions}</tr>` +
                        `</thead>` +
                        `<tbody>` +
                            `<tr>${tableRowOptions}</tr>` +
                        `</tbody>` +
                    `</table>` +
                `</div>` +
            `</div>` +
            `<button type="button" class="ghost-line btn-option btn-dr-table-row-add" id="btn-add-${property.componentId}" style="${isReadOnly ? 'opacity: 0;' : ''}">` +
                `<span class="icon icon-plus"></span>` +
            `</button>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);

        // 데이터 매핑 : "value": ["1행 1열 데이터", "1행 2열 데이터", "2행 1열 데이터", "2행 2열 데이터"] 형태로 전달됨
        const valueArr = (typeof property.value !== 'undefined' && property.value !== '') ? JSON.parse(property.value) : [];
        const drTable =  parent.querySelector('#dr-table-' + property.componentId);
        const drTableFirstRow = drTable.rows[1]; // 헤더 제외
        const drTableFirstCellLen = drTableFirstRow.cells.length;
        for (let i = 0, len = valueArr.length; i < len; i += drTableFirstCellLen) {
            let row = drTable.rows[i + 1];
            if (i === 0) {
                const firstRow = drTable.rows[1];
                for (let j = 0; j < firstRow.cells.length; j++) {
                    const cell = firstRow.cells[j];
                    setDRTableCellData(cell, valueArr.splice(0, 1));
                }
            } else {
                addDRTableRow(drTable, valueArr.splice(0, drTableFirstCellLen));
            }
        }
        // 폼 디자이너 편집 화면에서면 수정 가능
        if (isReadOnly) {
            // 헤더 속성에 이벤트 추가
            const drTableHeaderCells = drTable.rows[0].cells;
            for (let i = 0, len = drTableHeaderCells.length; i < len; i++) {
                drTableHeaderCells[i].addEventListener('click', function(e) {
                    // 상위로 이벤트가 전파되지 않도록 중단한다.
                    e.stopPropagation();

                    editor.showDRTableTypeProperties(property.componentId, e.target.getAttribute('data-field-index'), e.target.getAttribute('data-field-type'));
                }, false);
            }
        } else { // 신청서, 처리할 문서에서 버튼 동작
            const addBtn = parent.querySelector('#btn-add-' + property.componentId);
            addBtn.addEventListener('click', function (e) {
                e.stopPropagation();

                // row 추가
                const elem = aliceJs.clickInsideElement(e, 'btn-option');
                const tb = elem.parentNode.querySelector('.dr-table');
                addDRTableRow(drTable);
            });
        }
    }

    /**
     * Accordion 컴포넌트 (Start 영역)
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function AccordionStart(property) {
        this.id = property.componentId;
        this.name = 'Accordion Start';
        this.type = 'accordion-start';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];

        this.template =
        `<div id="${this.id}" class="component active" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}" data-endId="${property.display.endId}">` +
            `<div class="move-handler disabled"></div>` +
            `<div class="field-group">` +
                `<div class="field-content accordion align-${property.label.align}" style="--data-column: 12; ` +
                    `border-bottom: ${property.display.thickness}px solid ${property.display.color};">` +
                    `<label style="color: ${property.label.color}; font-size: ${property.label.size}px;` +
                        `${property.label.bold === 'Y' ? ' font-weight: bold;' : ''}` +
                        `${property.label.italic === 'Y' ? ' font-style: italic;' : ''}` +
                        `${property.label.underline === 'Y' ? ' text-decoration: underline;' : ''}">` +
                            `${aliceJs.filterXSS(property.label.text)}` +
                    `</label>` +
                    `<span class="icon icon-arrow-down on"></span>` +
                    `<span class="icon icon-arrow-up"></span>` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
        if (formType !== 'form') {
            const accordionStartComp = parent.querySelector('#' + property.componentId);
            accordionStartComp.addEventListener('click', function(e) {
                const elem =  aliceJs.clickInsideElement(e, aliceForm.COMPONENT);
                const arrowDown = elem.querySelector('.icon-arrow-down');
                const arrowUp = elem.querySelector('.icon-arrow-up');
                if (elem.classList.contains('active')) { // 접기
                    elem.classList.remove('active');
                    arrowDown.classList.remove('on');
                    arrowUp.classList.add('on');

                    let curComp = elem;
                    while (curComp = curComp.nextSibling) {
                        curComp.style.display = 'none';
                        if (curComp.nextSibling.id === elem.getAttribute('data-endId')) {
                            break;
                        }
                    }
                } else { // 펴기
                    elem.classList.add('active');
                    arrowDown.classList.add('on');
                    arrowUp.classList.remove('on');

                    let curComp = elem;
                    while (curComp = curComp.nextSibling) {
                        curComp.style.display = 'flex';
                        if (curComp.nextSibling.id === elem.getAttribute('data-endId')) {
                            break;
                        }
                    }
                }
            }, false);
        }
    }

        /**
     * Accordion 컴포넌트 (End) 영역)
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function AccordionEnd(property) {
        this.id = property.componentId;
        this.name = 'Accordion End';
        this.type = 'accordion-end';
        this.property = property;
        this.renderOrder = property.display.order;

        const displayType = property['dataAttribute']['displayType'];

        this.template =
        `<div id="${this.id}" class="component" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}" data-displayType="${displayType}" ` +
            `data-startId="${property.display.startId}" style="${formType === 'form' ? '' : 'display: none;'}">` +
            `<div class="move-handler disabled"></div>` +
            `<div class="field-group">` +
                `<div class="field-content" style="--data-column: 12;">` +
                    `<hr style="border-top: ${property.display.thickness}px solid ${property.display.color};">` +
                `</div>` +
            `</div>` +
        `</div>`;

        parent.insertAdjacentHTML('beforeend', this.template);
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
            case 'dynamic-row-table':
                componentObject =  new DaynamicRowTable(componentProperty);
                break;
            case 'accordion-start':
                componentObject =  new AccordionStart(componentProperty);
                break;
            case 'accordion-end':
                componentObject =  new AccordionEnd(componentProperty);
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
                if (group === 'option') { // 옵션 json 구조 변경
                    let options = [];
                    for (let i = 0, len = defaultProperty[group][0].items.length; i < len; i += 3) {
                        let option = {};
                        for (let j = i; j < i + len; j++) {
                            let child = defaultProperty[group][0].items[j];
                            option[child.id] = child.value;
                        }
                        options.push(option);
                    }
                    refineProperty[group] = options;
                } else if (group === 'field') { // dynamic row table
                    let field = {};
                    Object.keys(defaultProperty[group]).forEach(function(child) {
                        const fieldItem = defaultProperty[group][child];
                        field[fieldItem.id] = fieldItem.value;
                        if (fieldItem.id === 'type') {
                            Object.keys(defaultProperty[fieldItem.value]).forEach(function(subGroup) {
                                field[subGroup] = {};
                                const subGroupItem = defaultProperty[fieldItem.value][subGroup];
                                Object.keys(subGroupItem).forEach(function(subChild) {
                                    const attributeItem = subGroupItem[subChild];
                                    field[subGroup][attributeItem.id] = attributeItem.value;
                                });
                            });
                        }
                    });
                    refineProperty[group] = [field];
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
                        if (attributeItem.id === DATA_ATTRIBUTE_LABEL_LIST) { // 라벨링
                            let labelList = {
                                'label_target': aliceForm.COMPONENT,
                                'target_id': data.componentId,
                                'label': []
                            };
                            refineProperty[group][attributeItem.id] = labelList;
                        } else {
                            refineProperty[group][attributeItem.id] = attributeItemValue;
                        }
                    });
                }
            });

            if (type === 'dynamic-row-table') {
                delete refineProperty['inputbox'];
            }

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
        isReadOnly = target.hasAttribute('data-readonly');
        formType = target.getAttribute('data-display');
        if (isReadOnly) {
            tooltipTemplate = document.getElementById('tooltip-template');
        }
    }

    exports.init = init;
    exports.draw = draw;
    exports.getLastIndex = getLastIndex;
    exports.setLastIndex = setLastIndex;
    exports.getProperty = getPropertiesWithType;
    exports.getFieldTemplate = getFieldTemplate;
    exports.getName = getName;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
