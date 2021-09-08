/**
 * Date Time Mixin
 *
 *
 * @author 안광수 ks.ahn@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { FORM } from '../../lib/zConstants.js';
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
        minDateTime: '',
        maxDateTime: '',
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dataTimeMixin = {
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
        element.UIDate = new UIInput().setUIPlaceholder(i18n.dateTimeFormat)
            .setUIClass('z-input i-datetime-picker text-ellipsis')
            .setUIId('dateTime' + this.id)
            .setUIRequired(this.validationRequired)
            .setUIValue(this.getDefaultValue())
            .setUIAttribute('autocomplete', 'off')
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('data-validation-maxDateTime', this.validationMaxDateTime)
            .setUIAttribute('data-validation-minDateTime', this.validationMinDateTime);
        element.addUI(element.UIDate);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.parent?.parent?.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIDate.setUIReadOnly(true);
        }

        // 문서의 상태가 사용이 아닌 경우 = 신청서 진행 중이고
        // 신청서 양식 편집 화면에서 처리한 group 컴포넌트가 숨김이 아니며
        // 기본값이 '${default}' 이면 실제 값을 저장한다.
        if (this.parent?.parent?.parent?.status !== FORM.STATUS.EDIT &&
            this.parent?.parent?.displayType !== FORM.DISPLAY_TYPE.HIDDEN &&
            this.value === '${default}') {
            this.value = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type,
                this.UIElement.UIComponent.UIElement.UIDate.getUIValue());
        }

        // datePicker 초기화
        zDateTimePicker.initDateTimePicker(this.UIElement.UIComponent.UIElement.UIDate.domElement, this.updateValue.bind(this));
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
        this.UIElement.UIComponent.UIElement.UIDate.setUIValue(this.getDefaultValue());
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
    set validationMinDateTime(min) {
        this._validation.minDateTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, min);
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-minDateTime'
            , aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, min));
    },
    get validationMinDateTime() {
        return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this._validation.minDateTime);
    },
    set validationMaxDateTime(max) {
        this._validation.maxDateTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, max);
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-maxDateTime'
            , aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, max));
    },
    get validationMaxDateTime() {
        return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this._validation.maxDateTime);
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
            let dateTime = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    dateTime = i18n.getDateTime();
                    break;
                case FORM.DATE_TYPE.DATETIME:
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1]),
                        hours: zValidation.isEmpty(defaultValueArray[2]) || isNaN(Number(defaultValueArray[2])) ?
                            0 : Number(defaultValueArray[2])
                    };
                    dateTime = i18n.getDateTime(offset);
                    break;
                case FORM.DATE_TYPE.DATETIME_PICKER:
                    dateTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return dateTime;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this.value);
        }
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        // 유효성 검증
        let isValidationPass = true;
        if (!zValidation.isEmpty(this.validationMinDateTime)) {
            isValidationPass = i18n.compareSystemDateTime(this.validationMinDateTime, e.value);
            zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectAfterDateTime', this.validationMinDateTime));
        }
        if (isValidationPass && !zValidation.isEmpty(this.validationMaxDateTime)) {
            isValidationPass = i18n.compareSystemDateTime(e.value, this.validationMaxDateTime);
            zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectAfterDateTime', this.validationMaxDateTime));
        }
        this.value = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, e.value);
    },
    getProperty() {
        const defaultValueRadioProperty = new ZDefaultValueRadioProperty('elementDefaultValueRadio', 'element.defaultValueRadio',
            this.elementDefaultValueRadio,
            [
                {name: 'form.properties.option.none', value: FORM.DATE_TYPE.NONE},
                {name: 'form.properties.option.now', value: FORM.DATE_TYPE.NOW},
                {name: '', value: FORM.DATE_TYPE.DATETIME},
                {name: '', value: FORM.DATE_TYPE.DATETIME_PICKER}
            ]);
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(defaultValueRadioProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
                .addProperty(new ZDateTimePickerProperty('validationMinDateTime', 'validation.maxDateTime', this.validationMinDateTime, FORM.DATE_TYPE.DATETIME_PICKER))
                .addProperty(new ZDateTimePickerProperty('validationMaxDateTime', 'validation.maxDateTime', this.validationMaxDateTime, FORM.DATE_TYPE.DATETIME_PICKER))
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

