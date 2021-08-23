/**
 * Date Mixin
 *
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import {CLASS_PREFIX, FORM} from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import {UIDiv, UIInput} from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZDefaultValueRadioProperty from '../../formDesigner/property/type/zDefaultValueRadioProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZDateTimePickerProperty from '../../formDesigner/property/type/zDateTimePickerProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        defaultValueRadio: 'none'
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
        this._value = this.data.value || '${default}';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIDate = new UIInput().setUIPlaceholder(i18n.dateFormat)
            .setUIClass(CLASS_PREFIX + 'input i-date-picker')
            .setUIId('date' + this.id)
            .setUIRequired(this.validationRequired)
            .setUIValue(this.getDefaultValue())
            .setUIAttribute('autocomplete', 'off')
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('data-validation-max-date', this.validationMaxDate)
            .setUIAttribute('data-validation-min-date', this.validationMinDate);
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
        // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
        this._element.defaultValueRadio = value;
        this.UIElement.UIComponent.UIElement.UIDate.setUIValue(
            aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this.getDefaultValue()));
    },
    get elementDefaultValueRadio() {
        return this._element.defaultValueRadio;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
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
        this._validation.minDate = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, min);
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-min-date'
            , aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, min));
    },
    get validationMinDate() {
        return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this._validation.minDate);
    },
    set validationMaxDate(max) {
        this._validation.maxDate = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, max);
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-max-date'
            , aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, max));
    },
    get validationMaxDate() {
        return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this._validation.maxDate);
    },
    set value(value) {
        this._value = value;
    },
    get value() {
        return this._value;
    },
    // 기본값 조회
    getDefaultValue() {
        if (this._value === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = this.elementDefaultValueRadio.split('|');
            let date = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    date = i18n.getDate();
                    break;
                case FORM.DATE_TYPE.DAYS:
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    date = i18n.getDate(offset);
                    break;
                case FORM.DATE_TYPE.DATE_PICKER:
                    date = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return date;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this.value);
        }
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        // 유효성 검증
        let isValidationPass = true;
        if (!zValidation.isEmpty(this.validationMinDate)) {
            isValidationPass = i18n.compareSystemDate(this.validationMinDate, e.value);
            zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectAfterDate', this.validationMinDate));
        }
        if (isValidationPass && !zValidation.isEmpty(this.validationMaxDate)) {
            isValidationPass = i18n.compareSystemDate(e.value, this.validationMaxDate);
            zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectBeforeDate', this.validationMaxDate));
        }
        this.value = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, e.value);
    },
    // 세부 속성 조회
    getProperty() {
        const defaultValueRadioProperty = new ZDefaultValueRadioProperty('elementDefaultValueRadio', 'element.defaultValueRadio',
            this.elementDefaultValueRadio,
            [
                { name: 'form.properties.option.none', value: FORM.DATE_TYPE.NONE },
                { name: 'form.properties.option.now', value: FORM.DATE_TYPE.NOW },
                { name: '', value: FORM.DATE_TYPE.DAYS },
                { name: '', value: FORM.DATE_TYPE.DATE_PICKER }
            ]);
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(defaultValueRadioProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
                .addProperty(new ZDateTimePickerProperty('validationMinDate', 'validation.minDate', this.validationMinDate, FORM.DATE_TYPE.DATE_PICKER))
                .addProperty(new ZDateTimePickerProperty('validationMaxDate', 'validation.maxDate', this.validationMaxDate, FORM.DATE_TYPE.DATE_PICKER))
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
