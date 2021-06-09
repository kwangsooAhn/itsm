/**
 * Date Mixin
 *
 *
 * @author
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import { UIDiv, UIInput, UIText } from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZDefaultValueSelectProperty from '../../formDesigner/property/type/zDefaultValueSelectProperty.js';
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
        required: false,
        minDate: '',
        maxDate: '',
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dateMixin = {

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
        element.UIDate = new UIInput().setUIPlaceholder(this.elementPlaceholder)
            .setUIRequired(this.validationRequired)
            .setUIValue(this.value)
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('data-validation-maxDate', this.validationMaxDate)
            .setUIAttribute('data-validation-minDate', this.validationMinDate)
            .onUIKeyUp(this.updateValue.bind(this))
            .onUIChange(this.updateValue.bind(this));
        element.addUI(element.UIDate);
        return element;
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
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIValue(this.value);
    },
    get elementDefaultValueSelect() {
        return this._element.defaultValueSelect;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-required', boolean);
        if (boolean) {
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set validationMinDate(min) {
        this._validation.minDate = min;
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-minDate', min);
    },
    get validationMinDate() {
        return this._validation.minDate;
    },
    set validationMaxDate(max) {
        this._validation.maxDate = max;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-maxDate', max);
    },
    get validationMaxDate() {
        return this._validation.maxDate;
    },
    // set, get
    set value(value) {
        this._value = value;
    },
    get value() {
        // this._value === '${default}' 일 경우, 신청서에서 변경되지 않은 값을 의미하므로 기본값을 표시한다.
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
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZInputBoxProperty('element.placeholder', this.elementPlaceholder))
                .addProperty(new ZSliderProperty('element.columnWidth', this.elementColumnWidth)),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validation.required', this.validationRequired))
                .addProperty(new ZInputBoxProperty('validation.minDate', this.validationMinDate))
                .addProperty(new ZInputBoxProperty('validation.maxDate', this.validationMaxDate))
        ];
    },
    // json 데이터 추출 (서버에 전달되는 json 데이터)
    toJson() {
        console.log("toJson");
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