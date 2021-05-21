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
import InputBoxProperty from '../../formDesigner/property/type/inputBoxProperty.module.js';
import GroupProperty from '../../formDesigner/property/type/groupProperty.module.js';
import SliderProperty from '../../formDesigner/property/type/sliderProperty.module.js';
import CommonProperty from '../../formDesigner/property/type/commonProperty.module.js';
import DefaultValueSelectProperty from '../../formDesigner/property/type/defaultValueSelectProperty.module.js';
import DropdownProperty from '../../formDesigner/property/type/dropdownProperty.module.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        placeholder: '',
        columnWidth: '10',
        defaultValueSelect: 'input|', // input|사용자입력 / select|세션값
    },
    validation: {
        validationType: 'none', // none | char | num | numchar | email | phone
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
        this.validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.validation);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.element.columnWidth);

        element.UIInputbox = new UIInput().setUIPlaceholder(this.element.placeholder)
            .setUIRequired((this.displayType === FORM.DISPLAY_TYPE.REQUIRED))
            .setUIReadOnly((this.displayType === FORM.DISPLAY_TYPE.READONLY))
            .setUIValue(this.getValue())
            .setUIAttribute('data-validation-required', (this.displayType === FORM.DISPLAY_TYPE.REQUIRED))
            .setUIAttribute('data-validation-type', this.validation.validationType)
            .setUIAttribute('data-validation-maxLength', this.validation.maxLength)
            .setUIAttribute('data-validation-minLength', this.validation.minLength)
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
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.label.position));
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
    setValidationValidationType(type) {
        this.validation.validationType = type;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-type', type);
    },
    getValidationValidationType() {
        return this.validation.validationType;
    },
    setValidationMinLength(min) {
        this.validation.minLength = min;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-minLength', min);
    },
    getValidationMinLength() {
        return this.validation.minLength;
    },
    setValidationMaxLength(max) {
        this.validation.maxLength = max;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-maxLength', max);
    },
    getValidationMaxLength() {
        return this.validation.maxLength;
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
            const defaultValues = this.element.defaultValueSelect.split('|');
            if (defaultValues[0] === 'input') {
                return defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                return SESSION[defaultValues[1]] || '';
            }
        } else {
            return this.value;
        }
    },
    getProperty() {
        // validation - validation Type
        const validationTypeProperty = new DropdownProperty('validation.validationType', this.validation.validationType, [
            {name: 'form.properties.none', value: 'none'},
            {name: 'form.properties.char', value: 'char'},
            {name: 'form.properties.number', value: 'number'},
            {name: 'form.properties.email', value: 'email'},
            {name: 'form.properties.phone', value: 'phone'}
        ]);

        return [
            ...new CommonProperty(this).getCommonProperty(),
            new GroupProperty('group.element')
                .addProperty(new InputBoxProperty('element.placeholder', this.element.placeholder))
                .addProperty(new SliderProperty('element.columnWidth', this.element.columnWidth))
                .addProperty(new DefaultValueSelectProperty('element.defaultValueSelect', this.element.defaultValueSelect)),
            new GroupProperty('group.validation')
                .addProperty(validationTypeProperty)
                .addProperty(new InputBoxProperty('validation.minLength', this.validation.minLength))
                .addProperty(new InputBoxProperty('validation.maxLength', this.validation.maxLength))
        ];
    },
    /**
     * keyup 유효성 검증 이벤트 핸들러
     * @param e 이벤트객체
     */
    keyUpValidateCheck(element) {
        // type(number, char, email 등), min, max 체크
        if (element.getAttribute('data-validation-type') &&
            element.getAttribute('data-validation-type') !== '') {
            return validation.emit(element.getAttribute('data-validation-type'), element);
        }
        return true;
    },
    /**
     * change 유효성 검증 이벤트 핸들러
     * @param e 이벤트객체
     */
    changeValidateCheck(element) {
        // 필수값, minLength, maxLength 체크
        if (element.getAttribute('data-validation-required') &&
            element.getAttribute('data-validation-required') !== 'false') {
            return validation.emit('required', element);
        }
        if (element.getAttribute('data-validation-minLength') &&
            element.getAttribute('data-validation-minLength') !== '') {
            return validation.emit('minLength', element, element.getAttribute('data-validation-minLength'));
        }
        if (element.getAttribute('data-validation-maxLength') &&
            element.getAttribute('data-validation-maxLength') !== '') {
            return validation.emit('maxLength', element, element.getAttribute('data-validation-maxLength'));
        }
        return true;
    }
};