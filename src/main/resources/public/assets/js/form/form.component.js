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
        columnWidth = 12;   // 폼 양식을 몇 등분할지 값
    let renderOrder = 0;    // 컴포넌트 index = 출력 순서 생성시 사용
    let parent = null;
    let children = [];
    let isForm = false;     //폼 양식 화면인지 여부

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
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Editbox(property) {
        this.id = property.componentId;
        this.name = 'Edit Box';
        this.type = 'editbox';
        this.property = property;
        this.renderOrder = property.display.order;
        this.readOnly = false;
        this.hide = false;

        this.template =
        `<component class="component" id="${this.id}" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}">` +
            `<div class="add-icon"></div>` +
            `<div class="move-icon"></div>` +
            `<div class="group" contenteditable="true" placeholder="Typing '/' for add component"></div>` +
        `</component>`;
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
        this.readOnly = (property['dataAttribute']['displayType'] === 'readonly');
        this.hide = (property['dataAttribute']['displayType'] === 'hidden');

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
        
        this.template =
        `<component class="component" id="${this.id}" data-type="${this.type}" data-index="${this.renderOrder}" tabindex="${this.renderOrder}">` +
            `<div class="move-icon"></div>` +
            `<div class="group">` +
                `<div class="field" style="text-align: ${attr.label.align};">` +
                    `<div class="label" style="color: ${attr.label.color}; font-size: ${attr.label.size}px;` +
                        ` ${attr.label.bold === 'Y' ? 'font-weight: bold;' : ''}` +
                        ` ${attr.label.italic === 'Y' ? 'font-style: italic;' : ''}` +
                        ` ${attr.label.underline === 'Y' ? 'text-decoration: underline;' : ''}">${attr.label.text}</div>` +
                    `<span class="required" style="${attr.dataAttribute.displayType === 'editableRequired' ? '' : 'display: none;'}">*</span>` +
                `</div>` +
                `<div class="field"  style="flex-basis: 100%;">` +
                    
                `</div>` +
            `</div>` +
        `</component>`;

    }

    /**
     * Text Box 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function TextBox(property) {
        this.name = 'Text Box';
        this.type = 'textbox';

    }

    /**
     * Dropdown 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Dropdown(property) {
        this.name = 'Dropdown';
        this.type = 'select';

    }

    /**
     * Radio Button 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Radiobox(property) {
        this.name = 'Radio Button';
        this.type = 'radio';

    }

    /**
     * Checkbox 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Checkbox(property) {
        this.name = 'Checkbox';
        this.type = 'checkbox';
    }

    /**
     * Label 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Label(property) {
        this.name = 'Label';
        this.type = 'label';
    }

    /**
     * Image 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Imagebox(property) {
        this.name = 'Image';
        this.type = 'image';
    }

    /**
     * Divider 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Divider(property) {
        this.name = 'Divider';
        this.type = 'divider';
    }

    /**
     * Date 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function DateBox(property) {
        this.name = 'Date';
        this.type = 'date';
    }

    /**
     * Time 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성성
     * @constructor
     */
    function TimeBox(property) {
        this.name = 'Time';
        this.type = 'time';
    }

    /**
     * Date Time 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function DatetimeBox(property) {
        this.name = 'Date Time';
        this.type = 'datetime';
    }

    /**
     * Fileupload 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function Fileupload(property) {
        this.name = 'File Upload';
        this.type = 'fileupload';
    }
    /**
     * Custom-Code 컴포넌트
     *
     * @param {Object} property 컴포넌트 속성
     * @constructor
     */
    function CustomCode(property) {
        this.name = 'Custom Code';
        this.type = 'custom-code';

    }

    /**
     * 컴포넌트를 생성하고 출력한다.
     * @param {String} type 컴포넌트 타입
     * @param {Object} data 컴포넌트 데이터
     * @return {Object} 생성된 컴포넌트 객체
     */
    function draw(type, data) {
        if (typeof parent === 'undefined') { return false; }

        let componentProperty = {'display': { 'order': ++renderOrder }};
        if (typeof data !== 'undefined') { //기존 저장된 컴포넌트 속성이 존재할 경우
            componentProperty = data;
        } else {                     //신규 생성된 컴포넌트일 경우
            componentProperty.componentId = workflowUtil.generateUUID();
            componentProperty.type = type;
            componentProperty = getData(type, componentProperty);
        }
        console.log(componentProperty);
        let componentObject = null;
        switch(type) {
            case 'editbox':
                componentObject = new Editbox(componentProperty);
                break;
            case 'text':
                componentObject =  new InputBox(componentProperty);
                break;
            case 'textarea':
                componentObject =  new TextBox(componentProperty);
                break;
            case 'select':
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
        children.push(componentObject);
        console.log(componentObject);
        parent.insertAdjacentHTML('beforeend', componentObject.template);

        let componentElement = parent.querySelector('#' + componentObject.id);
        if (componentObject && componentElement) {
            componentObject.domElem = componentElement;

            // 신청서 display Type에 따른 처리
            if (componentObject.hide) {
                componentElement.style.display = 'none';
            } else {
                if (componentObject.readOnly) {
                    componentElement.setAttribute('data-readonly', true);
                }
            }

            // 공통 : 라벨 위치 조정 >> TODO : CSS 로 처리
            /*if (typeof componentProperty.label !== 'undefined') {
                let firstField = componentElement.querySelector('.group').firstElementChild;
                let lastField = componentElement.querySelector('.group').lastElementChild;
                if (componentProperty.label.position === 'hidden') {
                    firstField.style.display = 'none';
                } else if (componentProperty.label.position === 'left') {
                    firstField.style.flexBasis = ((100 * Number(componentProperty.label.column)) / columnWidth) + '%';
                } else { //top
                    firstField.style.flexBasis = ((100 * Number(componentProperty.label.column)) / columnWidth) + '%';
                    const secondField = document.createElement('div');
                    secondField.className = 'field';
                    secondField.style.flexBasis = (100 - ((100 * Number(componentProperty.label.column)) / columnWidth)) + '%';
                    lastField.parentNode.insertBefore(secondField, lastField);
                }
                lastField.style.flexBasis = ((100 * Number(componentProperty.display.column)) / columnWidth) + '%';
            }*/
        }
        return componentObject;
    }
    /**
     * 컴포넌트 기본 속성을 읽어서 폼 저장을 위한 컴포넌트의 데이터로 정제하여 조회한다.
     * @param {String} type 컴포넌트 타입
     * @param {Object} data 컴포넌트 데이터
     * @return {Object} refineProperty 정제된 컴포넌트 데이터
     */
    function getData(type, data) {
        let refineProperty = { display: {} };
        if (typeof refineProperty !== 'undefined') {
            refineProperty = data;
        }
        let defaultProperty = aliceJs.mergeObject({}, aliceForm.componentProperties[type]);
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
     * 라이브러리를 초기화한다.
     * @param target 컴포넌트를 추가할 대상 (폼 또는 문서)
     */
    function init(target) {
        parent = target;
        isForm = target.hasAttribute('data-readonly');
    }

    exports.init = init;

    exports.draw = draw;
    exports.getTitle = getTitle;
    exports.getData = getData;
    exports.getLastIndex = getLastIndex;
    exports.setLastIndex = setLastIndex;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
