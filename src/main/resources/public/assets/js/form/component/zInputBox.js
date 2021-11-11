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

import { ZSession } from '../../lib/zSession.js';
import { FORM } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import { UIDiv, UIInput } from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZDefaultValueSelectProperty from '../../formDesigner/property/type/zDefaultValueSelectProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';

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
        this._value = this.data.value || '${default}';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('z-element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInputbox = new UIInput().addUIClass('text-ellipsis')
            .setUIPlaceholder(this.elementPlaceholder)
            .setUIRequired(this.validationRequired)
            .setUIValue(this.getDefaultValue())
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('data-validation-type', this.validationValidationType)
            .setUIAttribute('data-validation-max-length', this.validationMaxLength)
            .setUIAttribute('data-validation-min-length', this.validationMinLength)
            .onUIKeyUp(this.updateValue.bind(this))
            .onUIChange(this.updateValue.bind(this));
        element.addUI(element.UIInputbox);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.parent?.parent?.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIInputbox.setUIReadOnly(true);
        }
        // 문서의 상태가 사용이 아닌 경우 = 신청서 진행 중이고
        // 신청서 양식 편집 화면에서 처리한 group 컴포넌트가 숨김이 아니며
        // 기본값이 '${default}' 이면 실제 값을 저장한다.
        if (this.parent?.parent?.parent?.status !== FORM.STATUS.EDIT &&
            this.parent?.parent?.displayType !== FORM.DISPLAY_TYPE.HIDDEN &&
            this.value === '${default}') {
            this.value = this.UIElement.UIComponent.UIElement.UIInputbox.getUIValue();
        }
    },
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
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIValue(this.getDefaultValue());
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
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-min-length', min);
    },
    get validationMinLength() {
        return this._validation.minLength;
    },
    set validationMaxLength(max) {
        this._validation.maxLength = max;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-max-length', max);
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
        return this._value;
    },
    // input에 표시되는 기본 값 조회
    getDefaultValue() {
        if (this.value === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = this.elementDefaultValueSelect.split('|');
            if (defaultValues[0] === 'input') {
                return defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                return ZSession.get(defaultValues[1]) || '';
            }
        } else {
            return this.value;
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
    // 세부 속성 조회
    getProperty() {
        // validation - validation Type
        const validationTypeProperty = new ZDropdownProperty('validationValidationType', 'validation.validationType',
            this.validationValidationType, [
                { name: i18n.msg('form.properties.none'), value: 'none' },
                { name: i18n.msg('form.properties.char'), value: 'char' },
                { name: i18n.msg('form.properties.number'), value: 'number' },
                { name: i18n.msg('form.properties.email'), value: 'email' },
                { name: i18n.msg('form.properties.phone'), value: 'phone' }
            ]);

        // 기본값
        const defaultValueProperty = new ZDefaultValueSelectProperty('elementDefaultValueSelect',
            'element.defaultValueSelect', this.elementDefaultValueSelect);
        defaultValueProperty.help = 'form.help.text-default';

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZInputBoxProperty('elementPlaceholder', 'element.placeholder', this.elementPlaceholder))
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(defaultValueProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
                .addProperty(validationTypeProperty)
                .addProperty(new ZInputBoxProperty('validationMinLength', 'validation.minLength', this.validationMinLength))
                .addProperty(new ZInputBoxProperty('validationMaxLength', 'validation.maxLength', this.validationMaxLength))
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