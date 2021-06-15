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

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import { UIDiv, UIInput } from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZDefaultValueSelectProperty from '../../formDesigner/property/type/zDefaultValueSelectProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';

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
        required: false, // 필수값 여부
        minLength: '0',
        maxLength: '100'
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const inputBoxMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInputbox = new UIInput().setUIPlaceholder(this.elementPlaceholder)
            .setUIRequired(this.validationRequired)
            // TODO: 처리할 문서 - 그룹의 displayType에 따라서 readonly 처리
            //.setUIReadOnly((this.parent.parent.displayType === FORM.DISPLAY_TYPE.READONLY))
            .setUIValue(this.value)
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('data-validation-type', this.validationValidationType)
            .setUIAttribute('data-validation-maxLength', this.validationMaxLength)
            .setUIAttribute('data-validation-minLength', this.validationMinLength)
            .onUIKeyUp(this.updateValue.bind(this))
            .onUIChange(this.updateValue.bind(this));
        element.addUI(element.UIInputbox);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {},
    set element(element) {
        this._element = element;
    },
    get element() {
        return this._element;
    },
    set elementPlaceholder(placeholder) {
        this._element.placeholder = placeholder;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIPlaceholder(placeholder);
    },
    get elementPlaceholder() {
        return this._element.placeholder;
    },
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementDefaultValueSelect(value) {
        this._element.defaultValueSelect = value;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIValue(this.value);
    },
    get elementDefaultValueSelect() {
        return this._element.defaultValueSelect;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationValidationType(type) {
        this._validation.validationType = type;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-type', type);
    },
    get validationValidationType() {
        return this._validation.validationType;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-required', boolean);
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set validationMinLength(min) {
        this._validation.minLength = min;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-minLength', min);
    },
    get validationMinLength() {
        return this._validation.minLength;
    },
    set validationMaxLength(max) {
        this._validation.maxLength = max;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-maxLength', max);
    },
    get validationMaxLength() {
        return this._validation.maxLength;
    },
    // 기본 값 변경
    set value(value) {
        this._value = value;
    },
    // 기본 값 조회
    get value() {
        if (this._value === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = this._element.defaultValueSelect.split('|');
            if (defaultValues[0] === 'input') {
                return defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                return SESSION[defaultValues[1]] || '';
            }
        } else {
            return this._value;
        }
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }
        // 유효성 검증
        // keyup 일 경우 type, min, max 체크
        if (e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            return false;
        }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            return false;
        }

        this.value = e.target.value;
    },

    getProperty() {
        // validation - validation Type
        const validationTypeProperty = new ZDropdownProperty('validation.validationType',
            this.validationValidationType, [
                {name: 'form.properties.none', value: 'none'},
                {name: 'form.properties.char', value: 'char'},
                {name: 'form.properties.number', value: 'number'},
                {name: 'form.properties.email', value: 'email'},
                {name: 'form.properties.phone', value: 'phone'}
            ]);

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZInputBoxProperty('element.placeholder', this.elementPlaceholder))
                .addProperty(new ZSliderProperty('element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZDefaultValueSelectProperty('element.defaultValueSelect', this.elementDefaultValueSelect)),
            new ZGroupProperty('group.validation')
                .addProperty(validationTypeProperty)
                .addProperty(new ZSwitchProperty('validation.required', this.validationRequired))
                .addProperty(new ZInputBoxProperty('validation.minLength', this.validationMinLength))
                .addProperty(new ZInputBoxProperty('validation.maxLength', this.validationMaxLength))
        ];
    },
    // json 데이터 추출 (서버에 전달되는 json 데이터)
    toJson() {
        return {
            id: this._id,
            type: this._type,
            display: this._display,
            isTopic: this._isTopic,
            mapId: this._mapId,
            tags: this._tags,
            value: this._value,
            label: this._label,
            element: this._element,
            validation: this._validation
        };
    }
};