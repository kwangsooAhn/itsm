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

import {CLASS_PREFIX, SESSION} from '../../lib/zConstants.js';
import {zValidation} from '../../lib/zValidation.js';
import {UIDiv, UIElement, UIInput} from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZDefaultValueDateProperty from '../../formDesigner/property/type/zDefaultValueDateProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        placeholder: 'yyyy-MM-dd',
        columnWidth: '10',
        none: false,
        current: true,
        date: '',
        dateInput: '',
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
            .setUIAttribute('data-validation-maxLength', this.validationMaxDate)
            .setUIAttribute('data-validation-minLength', this.validationMinDate)
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
        this.UIElement.UIComponent.UIElement.UIDate.setUIPlaceholder(placeholder);
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
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-required', boolean);
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set validationMinDate(min) {
        this._validation.minDate = min;
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-minLength', min);
    },
    get validationMinDate() {
        return this._validation.minDate;
    },
    set validationMaxDate(max) {
        this._validation.maxDate = max;
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-maxLength', max);
    },
    get validationMaxDate() {
        return this._validation.maxDate;
    },
    // set, get
    set value(value) {
        this._value = value;
    },
    set elementNone(boolean) {
        if (boolean === true) {
            this._element.none = true;
            this.UIElement.UIComponent.UIElement.UIDate.setUIValue('');
            this.UIElement.UIComponent.UIElement.UIDate.setUITextContent('');
        } else {
            this._element.none = false;
        }
    },
    get elementNone() {
        return this._element.none;
    },
    set elementCurrent(boolean) {
        if (boolean === true) {
            this._element.current = true;
            this.UIElement.UIComponent.UIElement.UIDate.setUIValue(i18n.getDate());
            this.UIElement.UIComponent.UIElement.UIDate.setUITextContent(i18n.getDate());
        } else {
            this._element.current = false;
        }
    },
    get elementCurrent() {
        return this._element.current;
    },
    set elementDate(value) {
        if (value = 'on') {
            this._element.date = value;
            this.UIElement.UIComponent.UIElement.UIDate.setUIValue(this.elementDate);
            this.UIElement.UIComponent.UIElement.UIDate.setUITextContent(this.elementDate);
        }
    },
    get elementDate() {
        return this._element.date;
    },
    set elementDatepicker(value) {
        this._element.datepicker = value;
        this.UIElement.UIComponent.UIElement.UIDate.setUIValue(this.elementDatepicker);
        this.UIElement.UIComponent.UIElement.UIDate.setUITextContent(this.elementDatepicker);
    },
    get elementDatepicker() {
        return this._element.datepicker;
    },
    get value() {
        if (this._value === '${default}') {
            return '';
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
                .addProperty(new ZSliderProperty('element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZDefaultValueDateProperty('element.none', this.elementNone))
                .addProperty(new ZDefaultValueDateProperty('element.current', this.elementCurrent))
                .addProperty(new ZDefaultValueDateProperty('element.date', this.elementDate))
                .addProperty(new ZDefaultValueDateProperty('element.datepicker', this.elementDatepicker)),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validation.required', this.validationRequired))
                .addProperty(new ZDefaultValueDateProperty('validation.minDate', this.validationMinDate))
                .addProperty(new ZDefaultValueDateProperty('validation.maxDate', this.validationMaxDate))
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