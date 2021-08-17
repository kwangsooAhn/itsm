/**
 * Divider Mixin
 *
 *
 * @author jy.lim <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { FORM, CLASS_PREFIX, UNIT } from '../../lib/zConstants.js';
import { UIDiv, UIHorizontalRule } from '../../lib/zUI.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';
import ZColorPickerProperty from '../../formDesigner/property/type/zColorPickerProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        thickness: '1',
        color: '#3f4b56',
        type: 'solid', // solid | dotted | dashed
    },
    validation: {
        required: false, // 필수값 여부
    },
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const dividerMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIHorizontalRule = new UIHorizontalRule().setUIClass(CLASS_PREFIX + 'divider').setUIId('divider' + this.id);
        element.addUI(element.UIHorizontalRule);
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
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementThickness(value) {
        this._element.thickness = value;
        this.UIElement.UIComponent.UIElement.UIHorizontalRule.setUIThickness(value + UNIT.PX);
    },
    get elementThickness() {
        return this._element.thickness;
    },
    set elementColor(color) {
        this._element.color = color;
        this.UIElement.UIComponent.UIElement.UIHorizontalRule.setUIStyle(color, this._element.type);
    },
    get elementColor() {
        return this._element.color;
    },
    set elementType(type) {
        this._element.type = type;
        this.UIElement.UIComponent.UIElement.UIHorizontalRule.setUIStyle(this._element.color, type);
    },
    get elementType() {
        return this._element.type;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
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
    // 기본 값 변경
    set value(value) {
        this._value = value;
    },
    // 기본 값 조회
    get value() {
        return this._value;
    },
    // 세부 속성 조회
    getProperty() {
        // element - thickness
        const thicknessProperty = new ZInputBoxProperty('elementThickness', 'element.thickness', this.elementThickness)
            .setValidation(false, 'number', '0', '20', '', '');
        thicknessProperty.unit = UNIT.PX;

        // element - color
        const colorProperty = new ZColorPickerProperty('elementColor', 'element.color', this.elementColor)
            .setValidation(false, 'hex', '', '', '', '25');

        // element - Type
        const typeProperty = new ZDropdownProperty('elementType', 'element.type',
            this.elementType, [
                { name: i18n.msg('form.properties.lineType.line'), value: 'solid' },
                { name: i18n.msg('form.properties.lineType.dot'), value: 'dotted' },
                { name: i18n.msg('form.properties.lineType.dash'), value: 'dashed' },
            ]);

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(thicknessProperty)
                .addProperty(colorProperty)
                .addProperty(typeProperty)
        ];
    },
    // json 데이터 추출 (서버에 전달되는 json 데이터)
    toJson() {
        return {
            id: this._id,
            type: this._type,
            display: this._display,
            displayType: this._displayType,
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