/**
 * Custom Code Mixin
 *
 *
 * @author Kim Sung Min <ksm00@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import {UIButton, UIDiv, UIInput, UIInputButton} from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZDefaultValueCustomCodeProperty from '../../formDesigner/property/type/ZDefaultValueCustomCodeProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        defaultDropDownValue: '',
        defaultValueRadio: 'none', // none|없음  session|세션값  code|코드값
    },
    validation: {
        required: false // 필수값 여부
    }
};

Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const customCodeMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this._value || '${default}';
        this._buttonText = 'button';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);
        return this.makeCustomCode(element);
    },
    makeCustomCode(object) {
        object.UIInputButton = new UIInputButton()
            .setUIRequired(this.validationRequired)
            .setUIId('customcode' + this.id)
            .setUIAttribute('data-validation-required', this.validationRequired);
        object.UIInput = new UIInput(this.value).setUIReadOnly(true).setUIClass('input');
        object.UIButton = new UIButton(this.buttonText).setUIClass('button default-line');
        object.addUI(object.UIInputButton.addUI(object.UIInput).addUI(object.UIButton));
        return object;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {},
    // set, get
    set value(value) {
        this._value = value;
    },
    get value() {
        if (this._value === '${default}') {
            return this.makeDefaultValue(this.elementDefaultValueRadio); // 기본값 반환
        } else { // 저장된 값 반환
            return this._value;
        }
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementDefaultValueSelect(value) {
        this._element.defaultValueSelect = value;
        this.UIElement.UIComponent.UIInputButton.setUIValue(this.value);
    },
    get elementDefaultValueSelect() {
        return this._element.defaultValueSelect;
    },
    set status(status) {
        this._status = status;
    },
    get status() {
        return this._status;
    },
    set elementDefaultValueRadio(value) {
        // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
        this._element.defaultValueRadio = value;
        //this.UIElement.UIComponent.UIInputButton.setUIValue(this.makeDefaultValue(value));
    },
    get elementDefaultValueRadio() {
        return this._element.defaultValueRadio;
    },
    set defaultDropDownValue(value) {
        this._element.defaultDropDownValue = value;
        //this.UIElement.UIComponent.UIInputButton.UIButton.onUIClick(this.updateProperty.bind(this));
    },
    get defaultDropDownValue() {
        if (this._value === '${default}') {
            return FORM.CUSTOM_CODE[0].customCodeId;
        } else {
            return this._element.defaultDropDownValue;
        }
    },
    get buttonText() {
        return this._buttonText;
    },
    set buttonText(text) {
        this._buttonText = text;
        this.UIElement.UIComponent.UIElement.UIButton.setUITextContent(text);
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }

        this.value = e.target.value;
    },
    // 기본값 조회
    makeDefaultValue(value) {
        // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
        const defaultValueArray = value.split('|');
        switch (defaultValueArray[0]) {
        case FORM.CUSTOM.NONE:
            return '';
        case FORM.CUSTOM.SESSION:
            return 'username';
        case FORM.CUSTOM.CODE:
            return 'code|1';
        }
    },
    getProperty() {
        let customCode = [];
        let customCodeChild = [];
        FORM.CUSTOM_CODE.forEach(function(data){
            let dropDownOption = new Object();
            dropDownOption.name = data.customCodeName;
            dropDownOption.value = data.customCodeId;
            customCode.push(dropDownOption);
        });
        FORM.CUSTOM_CODE_CHILD.forEach(function(data){
            let dropDownOption = new Object();
            dropDownOption.name = data.value;
            dropDownOption.value = data.key;
            customCodeChild.push(dropDownOption);
        });
        const customCodeProperty = new ZDefaultValueCustomCodeProperty('elementCustomCode', 'element.defaultValueRadio',
            this.defaultDropDownValue, this.elementDefaultValueRadio, customCode,
            [
                { name: 'form.properties.option.none', value: FORM.CUSTOM.NONE },
                { name: 'form.properties.default.session', value: FORM.CUSTOM.SESSION },
                { name: 'form.properties.default.code', value: FORM.CUSTOM.CODE }
            ], customCodeChild);
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(customCodeProperty)
                .addProperty(new ZInputBoxProperty('buttonText', 'buttonText', this.buttonText)
                    .setValidation(false, '', '', '', '', '128')),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
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
            element: this._element
        };
    }
};