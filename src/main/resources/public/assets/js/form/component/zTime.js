/**
 * Time Mixin
 *
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZDateTimePickerProperty from '../../formDesigner/property/type/zDateTimePickerProperty.js';
import ZDefaultValueRadioProperty from '../../formDesigner/property/type/zDefaultValueRadioProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv, UIInput } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';


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
        minTime: '',
        maxTime: '',
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const timeMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = (this.data.value === undefined) ? '${default}' : this.data.value;
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIDate = new UIInput().setUIPlaceholder(i18n.timeFormat)
            .setUIClass('ic-time-picker text-ellipsis')
            .setUIId('date' + this.id)
            .setUIRequired(this.validationRequired)
            .setUIValue(this.getDefaultValue())
            .setUIAttribute('autocomplete', 'none')
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('data-validation-maxtime', this.validationMaxTime)
            .setUIAttribute('data-validation-mintime', this.validationMinTime);
        element.addUI(element.UIDate);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 문서의 상태가 사용이 아닌 경우 = 신청서 진행 중이고
        // 신청서 양식 편집 화면에서 처리한 group 컴포넌트가 숨김이 아니며
        // 기본값이 '${default}' 이면 실제 값을 저장한다.
        if (!zValidation.isEmpty(this.parent) && !zValidation.isEmpty(this.parent.parent) &&
            !zValidation.isEmpty(this.parent.parent.parent) && this.parent.parent.parent.status !== FORM.STATUS.EDIT &&
            this.displayType === FORM.DISPLAY_TYPE.EDITABLE && this.value === '${default}') {
            this.value = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type,
                this.UIElement.UIComponent.UIElement.UIDate.getUIValue());
        }

        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIDate.setUIReadOnly(true);
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none') : '';
        } else {
            // datePicker 초기화
            zDateTimePicker.initTimePicker(this.UIElement.UIComponent.UIElement.UIDate.domElement,
                this.updateValue.bind(this));
        }
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
        this.UIElement.UIComponent.UIElement.UIDate.setUIValue(this.getDefaultValue(value));
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
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('none').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set validationMinTime(min) {
        this._validation.minTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, min);
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-mintime'
            , aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, min));
    },
    get validationMinTime() {
        return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this._validation.minTime);
    },
    set validationMaxTime(max) {
        this._validation.maxTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, max);
        this.UIElement.UIComponent.UIElement.UIDate.setUIAttribute('data-validation-maxtime'
            , aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, max));
    },
    get validationMaxTime() {
        return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this._validation.maxTime);
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
            let time = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    time = i18n.getTime();
                    break;
                case FORM.DATE_TYPE.HOURS:
                    const offset = {
                        hours: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    time = i18n.getTime(offset);
                    break;
                case FORM.DATE_TYPE.TIME_PICKER:
                    time = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return time;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, this.type, this.value);
        }
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        // 유효성 검증
        let isValidationPass = true;
        if (!zValidation.isEmpty(this.validationMinTime)) {
            isValidationPass = i18n.compareSystemTime(this.validationMinTime, e.value);
            zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectAfterTime', this.validationMinTime));
        }
        if (isValidationPass && !zValidation.isEmpty(this.validationMaxTime)) {
            isValidationPass = i18n.compareSystemTime(e.value, this.validationMaxTime);
            zValidation.setDOMElementError(isValidationPass, e, i18n.msg('common.msg.selectBeforeTime', this.validationMaxTime));
        }
        this.UIElement.UIComponent.UIElement.UIDate.removeUIClass('error');
        this.value = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, this.type, e.value);
    },
    getProperty() {
        const defaultValueRadioProperty = new ZDefaultValueRadioProperty('elementDefaultValueRadio', 'element.defaultValueRadio',
            this.elementDefaultValueRadio,
            [
                { name: 'form.properties.option.none', value: FORM.DATE_TYPE.NONE },
                { name: 'form.properties.option.now', value: FORM.DATE_TYPE.NOW },
                { name: '', value: FORM.DATE_TYPE.HOURS },
                { name: '', value: FORM.DATE_TYPE.TIME_PICKER }
            ]);
        defaultValueRadioProperty.help = 'form.help.time-default';
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(defaultValueRadioProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
                .addProperty(new ZDateTimePickerProperty('validationMinTime', 'validation.minTime', this.validationMinTime, FORM.DATE_TYPE.TIME_PICKER))
                .addProperty(new ZDateTimePickerProperty('validationMaxTime', 'validation.maxTime', this.validationMaxTime, FORM.DATE_TYPE.TIME_PICKER))
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
    },
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        return true;
    }
};
