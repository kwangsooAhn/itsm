/**
 * Custom Code Mixin
 *
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import { UIButton, UIDiv, UIInput, UISpan } from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZDefaultValueCustomCodeProperty from '../../formDesigner/property/type/zDefaultValueCustomCodeProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        defaultValueCustomCode: ''
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

        // customCode|none|없음  customCode|session|세션값  customCode|code|코드값|코드명
        if (zValidation.isEmpty(this._element.defaultValueCustomCode)) {
            this._element.defaultValueCustomCode = FORM.CUSTOM_CODE[0].customCodeId + '|' + FORM.CUSTOM.NONE + '|';
        }
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInputButton = new UIDiv()
            .setUIClass(CLASS_PREFIX + 'input-button')
            .addUIClass('flex-row')
            .setUIId('customcode' + this.id)
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.UIInput = new UIInput(this.getDefaultValue())
            .setUIReadOnly(true)
            .setUIAttribute('data-custom-data', this.elementDefaultValueCustomCode)
            .onUIChange(this.updateValue.bind(this));
        element.UIButtonClear = new UIButton()
            .setUIClass(CLASS_PREFIX + 'button-clear')
            .setUIAttribute('tabIndex', '-1')
            .onUIClick(aliceJs.clearText);
        element.UIButton = new UIButton()
            .setUIClass(CLASS_PREFIX + 'button-search')
            .addUIClass(CLASS_PREFIX + 'button-icon')
            .addUIClass('form')
            .onUIClick(this.openCustomCodePopup.bind(this))
            .addUI(new UISpan().setUIClass(CLASS_PREFIX + 'icon').addUIClass('i-search'));

        element.addUI(element.UIInputButton.addUI(element.UIInput).addUI(element.UIButtonClear).addUI(element.UIButton));

        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {},
    // set, get
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementDefaultValueCustomCode(value) {
        this._element.defaultValueCustomCode = value;
        this.UIElement.UIComponent.UIElement.UIInput.setUIValue(this.getDefaultValue()).setUIAttribute('data-custom-data', value);
    },
    get elementDefaultValueCustomCode() {
        return this._element.defaultValueCustomCode;
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
    set value(value) {
        this._value = value;
    },
    get value() {
        return this._value;
    },
    // 기본값 조회
    getDefaultValue() {
        if (this._value === '${default}') {
            const defaultValues = this.elementDefaultValueCustomCode.split('|');
            switch (defaultValues[1]) {
                case FORM.CUSTOM.NONE:
                    return '';
                case FORM.CUSTOM.SESSION:
                    return SESSION[defaultValues[2]] || '';
                case FORM.CUSTOM.CODE:
                    return defaultValues[3];
            }
        } else {
            return this.value;
        }
    },
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();

        this.value = e.target.value;
    },
    // 세부 속성 조회
    getProperty() {
        const customCodeProperty = new ZDefaultValueCustomCodeProperty('elementDefaultValueCustomCode', 'element.DefaultValueCustomCode',
            this.elementDefaultValueCustomCode);
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(customCodeProperty),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
        ];
    },
    // TODO: #10252 커스텀 코드 모달로 변경시 일감 처리 예정
    openCustomCodePopup(e) {
        e.stopPropagation();
        const defaultValues = this.elementDefaultValueCustomCode.split('|');
        let customCodeData = {
            componentId: this.id,
            componentValue: this.elementDefaultValueCustomCode
        };
        const storageName = 'alice_custom-codes-search-' + this.id;
        sessionStorage.setItem(storageName, JSON.stringify(customCodeData));
        let url = '/custom-codes/' + defaultValues[0] + '/search';
        window.open(url, storageName, 'width=640, height=866');
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