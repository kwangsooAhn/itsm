/**
 * Inputbox Mixin
 *
 * inputbox 컴포넌트를 위한 mixin 모음.
 * inputbox 는 HTML 에서 text 타입의 input 요소를 뜻한다.
 * 유효성 체크를 통해서 숫자, 문자, 전화번호, 이메일 등 제어가 가능하다.
 *
 * @author woodajung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import {SESSION, FORM, CLASS_PREFIX} from '../../lib/constants.js';
import {UIDiv, UIInput} from '../../lib/ui.js';
import {COMMON_PROPERTIES} from "../propertyType/commonPropertyPanel.js";
import InputTypeProperty from "../propertyType/InputTypeProperty.js";
import SliderTypeProperty from "../propertyType/SliderTypeProperty.js";
import DefaultValueSelectTypeProperty from "../propertyType/DefaultValueSelectTypeProperty.js";
import SelectTypeProperty from "../propertyType/SelectTypeProperty.js";

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        placeholder: '',
        columnWidth: '10',
        defaultValueSelect: 'input|', // input|사용자입력 / select|세션값
    },
    validate: {
        validateType: 'none', // none | char | num | numchar | email | phone
        minLength: '0',
        maxLength: '100'
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const inputBoxMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this.element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.element);
        this.validate = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validate, this.validate);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.element.columnWidth);

        element.UIInputbox = new UIInput().setUIPlaceholder(this.element.placeholder)
            .setUIRequired((this.displayType === FORM.DISPLAY_TYPE.REQUIRED))
            .setUIReadOnly((this.displayType === FORM.DISPLAY_TYPE.READONLY))
            .setUIValue(this.getValue())
            .setUIAttribute('data-validate-required', (this.displayType === FORM.DISPLAY_TYPE.REQUIRED))
            .setUIAttribute('data-validate-type', this.validate.validateType)
            .setUIAttribute('data-validate-maxLength', this.validate.lengthMax)
            .setUIAttribute('data-validate-minLength', this.validate.lengthMin)
            .onUIKeyUp(this.updateValue.bind(this))
            .onUIChange(this.updateValue.bind(this));
        element.addUI(element.UIInputbox);
        return element;
    },
    setElementPlaceholder(placeholder) {
        this.element.placeholder = placeholder;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIPlaceholder(placeholder);
    },
    getElementPlaceholder() {
        return this.element.placeholder;
    },
    setElementColumnWidth(width) {
        this.element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
    },
    getElementColumnWidth() {
        return this.element.columnWidth;
    },
    setElementDefaultValueSelect(value) {
        this.element.defaultValueSelect = value;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIValue(this.getValue());
    },
    getElementDefaultValueSelect() {
        return this.element.defaultValueSelect;
    },
    setValidateValidateType(type) {
        this.validate.validateType = type;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validate-type', type);
    },
    getValidateValidateType() {
        return this.validate.validateType;
    },
    setValidateLengthMin(min) {
        this.validate.lengthMin = min;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validate-minLength', min);
    },
    getValidateLengthMin() {
        return this.validate.lengthMin;
    },
    setValidateLengthMax(max) {
        this.validate.lengthMax = max;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validate-maxLength', max);
    },
    getValidateLengthMax() {
        return this.validate.lengthMax;
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter 입력시
        if (e.key === 'Enter' || e.keyCode === 13) {
            return false;
        }
        // 유효성 검증
        let passValidate = true;
        if (e.type === 'keyup') { // keyup 일 경우 type, min, max 체크
            passValidate = this.keyUpValidateCheck(e.target);
        } else if (e.type === 'change') { // change 일 경우 필수값, minLength, maxLength 체크
            passValidate = this.changeValidateCheck(e.target);
        }
        if (passValidate) {
            this.setValue(e.target.value);
        }
    },
    // 기본 값 변경
    setValue(value) {
        this.value = value;
    },
    // 기본 값 조회
    getValue() {
        if (this.value === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = this.element.defaultType.split('|');
            if (defaultValues[0] === 'input') {
                return defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                return SESSION[defaultValues[1]] || '';
            }
        } else {
            return this.value;
        }
    },
    // 세부 속성
    getProperty() {
        let PANEL_PROPERTIES = COMMON_PROPERTIES;
        PANEL_PROPERTIES.element = {
            name: 'form.properties.element',
            type: 'group',
            children: {}
        }

        PANEL_PROPERTIES.element.children.placeholder = new InputTypeProperty('placeholder').getPropertyTypeConfig();
        PANEL_PROPERTIES.element.children.columnWidth = new SliderTypeProperty('columnWidth').getPropertyTypeConfig();
        PANEL_PROPERTIES.element.children.defaultValueSelect = new DefaultValueSelectTypeProperty('defaultValueSelect').getPropertyTypeConfig();

            PANEL_PROPERTIES.validate = {
            name: 'form.properties.validate',
            type: 'group',
            children: {}
        }

        PANEL_PROPERTIES.validate.children.validateType = new SelectTypeProperty(
            'validateType',
            [
                {name: 'form.properties.none', value: 'none'},
                {name: 'form.properties.char', value: 'char'},
                {name: 'form.properties.number', value: 'number'},
                {name: 'form.properties.email', value: 'email'},
                {name: 'form.properties.phone', value: 'phone'}
            ]
        );
        PANEL_PROPERTIES.validate.children.minLength = new InputTypeProperty('minLength').getPropertyTypeConfig();
        PANEL_PROPERTIES.validate.children.maxLength = new InputTypeProperty('maxLength').getPropertyTypeConfig();

        return Object.entries(PANEL_PROPERTIES).reduce((property, [key, value]) => {
            if (value.type === 'group') {
                const childProperties = Object.entries(value.children).reduce((child, [childKey, childValue]) => {
                    const tempChildValue = {'value': this[key][childKey]};
                    if (childValue.type === 'button-toggle-icon') { // 토글 데이터
                        tempChildValue.value = childValue.option.map((item) =>
                            (this[key][item.value]) ? 'Y' : 'N').join('|');
                    }
                    child[childKey] = Object.assign(childValue, tempChildValue);
                    return child;
                }, {});
                property[key] = Object.assign(value, {'children': childProperties});
            } else {
                property[key] = Object.assign(value, {'value': this[key]});
            }
            return property;
        }, {});
    }
};