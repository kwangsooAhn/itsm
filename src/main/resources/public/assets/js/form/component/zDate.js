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

import { CLASS_PREFIX } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import {UIDiv, UIInput} from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZDefaultValueRadioProperty from '../../formDesigner/property/type/zDefaultValueDateProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        placeholder: 'yyyy-MM-dd',
        columnWidth: '10',
        defaultValueRadio: 'none',

        //none: false,
        //current: true,
        //date: false,
        //datepicker: false,
        //dateInput: '',
        //datepickerInput: '',
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
            .setUIAttribute('data-validation-maxdate', this.validationMaxDate)
            .setUIAttribute('data-validation-mindate', this.validationMinDate);
        //.onUIKeyUp(this.updateValue.bind(this))
        //.onUIChange(this.updateValue.bind(this));
        element.addUI(element.UIDate);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        zDateTimePicker.initDatePicker(this.UIElement.UIComponent.UIElement.UIDate.domElement, this.updateValue.bind(this));
    },
    // set, get
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
    set elementDefaultValueRadio(value) {
        this._element.defaultValueRadio = value;
    },
    get elementDefaultValueRadio() {
        return this._element.defaultValueRadio;
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
    /*set elementNone(boolean) {
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
    set elementDate(boolean) {
        if (boolean === true) {
            let offset = {};
            offset.days = this._element.dateInput;
            this.UIElement.UIComponent.UIElement.UIDate.setUIValue(i18n.getDate(offset));
            this.UIElement.UIComponent.UIElement.UIDate.setUITextContent(i18n.getDate(offset));
            this._element.date = true;
        } else {
            this._element.date = false;
        }
    },
    get elementDate() {
        return this._element.date;
    },
    set elementDateInput(value) {
        let offset = {};
        offset.days = value;
        if (this._element.date === true) {
            this.UIElement.UIComponent.UIElement.UIDate.setUIValue(i18n.getDate(offset));
            this.UIElement.UIComponent.UIElement.UIDate.setUITextContent(i18n.getDate(offset));
        }
        this._element.dateInput = value;
    },
    get elementDateInput() {
        return this._element.dateInput;
    },
    set elementDatepicker(boolean) {
        if (boolean === true) {
            this._element.datepicker = true;
            this.UIElement.UIComponent.UIElement.UIDate.setUIValue(this._element.datepickerInput);
            this.UIElement.UIComponent.UIElement.UIDate.setUITextContent(this._element.datepickerInput);
        } else {
            this._element.datepicker = false;
        }
    },
    get elementDatepicker() {
        return this._element.datepicker;
    },
    set elementDatepickerInput(value) {
        if (this._element.datepicker === true) {
            this.UIElement.UIComponent.UIElement.UIDate.setUIValue(value);
            this.UIElement.UIComponent.UIElement.UIDate.setUITextContent(value);
        }
        this._element.datepickerInput = value;
    },
    get elementDatepickerInput() {
        return this._element.datepickerInput;
    },*/
    set value(value) {
        this._value = value;
    },
    get value() {
        if (this._value === '${default}') {
            return ''; // 기본값 반환
        } else { // 저장된 값 반환
            return this._value;
        }
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        console.log(e);
        /* e.stopPropagation();
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

        this.value = e.target.value;*/
    },
    getProperty() {
        //const elementDateProperty = new ZDefaultValueDateProperty('element.date', this.elementDate);
        //elementDateProperty.bindValue = this.elementDateInput;
        //const elementDatePickerProperty = new ZDefaultValueDateProperty('element.datepicker', this.elementDatepicker);
        //elementDatePickerProperty.bindValue = this.elementDatepickerInput;

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZDefaultValueRadioProperty('element.defaultValueRadio', this.defaultValueRadio)),
            //.addProperty(new ZDefaultValueDateProperty('element.none', this.elementNone))
            //.addProperty(new ZDefaultValueDateProperty('element.current', this.elementCurrent))
            //.addProperty(elementDateProperty)
            //.addProperty(elementDatePickerProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validation.required', this.validationRequired))
                //.addProperty(new ZDefaultValueRadioProperty('validation.minDate', this.validationMinDate))
                //.addProperty(new ZDefaultValueRadioProperty('validation.maxDate', this.validationMaxDate))
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