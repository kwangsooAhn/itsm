/**
 * Label Mixin
 *
 *
 * @author phc <phc@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { UNIT } from '../../lib/zConstants.js';
import { UIDiv } from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchButtonProperty from '../../formDesigner/property/type/zSwitchButtonProperty.js';
import ZToggleButtonProperty from '../../formDesigner/property/type/zToggleButtonProperty.js';
import ZColorPickerProperty from '../../formDesigner/property/type/zColorPickerProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '0',
        labelWidth: '10',
        text: '',
        fontSize: '12',
        align: 'left',
        fontOption: '',
        fontOptionBold: 'N',
        fontOptionItalic: 'N',
        fontOptionUnderline: 'N',
        fontColor: '#8B9094',
    },
    validation: {
        required: false, // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const labelMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('z-element')
            .setUIProperty('--data-column', this.elementColumnWidth);

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
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
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
    // 세부 속성 조회
    getProperty() {
        // label - text
        const labelTextProperty = new ZInputBoxProperty('labelText', 'label.text', this.labelText);
        labelTextProperty.columnWidth = '9';

        // label - fontSize
        const labelFontSizeProperty = new ZInputBoxProperty('labelFontSize', 'label.fontSize', this.labelFontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        labelFontSizeProperty.unit = UNIT.PX;
        labelFontSizeProperty.columnWidth = '3';

        // label - align
        const labelAlignProperty = new ZSwitchButtonProperty('labelAlign', 'label.align', this.labelAlign, [
            { 'name': 'i-align-left', 'value': 'left' },
            { 'name': 'i-align-center', 'value': 'center' },
            { 'name': 'i-align-right', 'value': 'right' }
        ]);
        labelAlignProperty.columnWidth = '6';

        // label - fontOption
        const labelFontOption = [
            { 'name': 'i-bold', 'value': 'bold'},
            { 'name': 'i-italic', 'value': 'italic' },
            { 'name': 'i-underline', 'value': 'underline' }
        ];
        const labelFontValue = labelFontOption.map((item) => {
            const method = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            return this['labelFontOption' + method] ? 'Y' : 'N';
        }).join('|');
        const labelFontOptionProperty = new ZToggleButtonProperty('labelFontOption', 'label.fontOption', labelFontValue, labelFontOption);
        labelFontOptionProperty.columnWidth = '6';

        // label - fontColor
        const labelFontColorProperty = new ZColorPickerProperty('labelFontColor', 'label.fontColor', this.labelFontColor)
            .setValidation(false, 'rgb', '', '', '', '25');
        labelFontColorProperty.columnWidth = '12';

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            new ZGroupProperty('group.element')
                .addProperty(labelTextProperty)
                .addProperty(labelFontSizeProperty)
                .addProperty(labelAlignProperty)
                .addProperty(labelFontOptionProperty)
                .addProperty(labelFontColorProperty)
        ];
    },
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
