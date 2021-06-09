/**
 * Divider Mixin
 *
 *
 * @author
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { FORM, CLASS_PREFIX } from '../../lib/constants.js';
import { zValidation } from '../../lib/validation.js';
import { UIDiv, UIHorizontalRule } from '../../lib/ui.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';
import ZColorPickerProperty from '../../formDesigner/property/type/zColorPickerProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        lineThickness: '1',
        lineColor: '#3f4b56',
        lineType: 'solid', // solid | dotted | dashed
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
    },
    // component 엘리먼트 생성
    makeElement() {
        // label 숨김 처리
        this.labelPosition = FORM.LABEL.POSITION.HIDDEN;
        
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', 12);

        element.UIHorizontalRule = new UIHorizontalRule()
            .setUIAttribute('element-line-thickness', this.elementLineThickness)
            .setUIAttribute('element-line-color', this.elementLineColor)
            .setUIAttribute('element-line-type', this.elementLineType);

        element.addUI(element.UIHorizontalRule);
        return element;
    },
    set elementLineThickness(value) {
        this._element.lineThickness = value;
        // this.UIElement.UIComponent.UIElement.UIHorizontalRule.setUIThickness(value);
    },
    get elementLineThickness() {
        return this._element.lineThickness;
    },
    set elementLineColor(color) {
        this._element.lineColor = color;
        // this.UIElement.UIComponent.UIElement.UIHorizontalRule.setUIColor(color);
    },
    get elementLineColor() {
        return this._element.lineColor;
    },
    set elementLineType(type) {
        this._element.lineType = type;
        this.UIElement.UIComponent.UIElement.UIHorizontalRule.setUIAttribute('element-line-type', type);
    },
    get elementLineType() {
        return this._element.lineType;
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
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
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
        // this._value === '${default}' 일 경우, 신청서에서 변경되지 않은 값을 의미하므로 기본값을 표시한다.
        // 사용자 변경시 해당 값이 할당된다.
        return this._value;
    },
    // 값 변경시 이벤트 핸들러
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
        // element - thickness
        const lineThicknessProperty = new ZInputBoxProperty('element.lineThickness', this.elementLineThickness)
            .setValidation(false, 'number', '', '', '1', '100');
        lineThicknessProperty.unit = 'px';
        lineThicknessProperty.columnWidth = '12';

        // element - color
        const lineColorProperty = new ZColorPickerProperty('element.lineColor', this.elementLineColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        lineColorProperty.columnWidth = '12';

        // element - Type
        const lineTypeProperty = new ZDropdownProperty('element.lineType',
            this.elementLineType, [
                { name: 'form.properties.lineType.line', value: 'solid' },
                { name: 'form.properties.lineType.dot', value: 'dotted' },
                { name: 'form.properties.lineType.dash', value: 'dashed' },
            ]);
        lineTypeProperty.columnWidth = '12';

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            new ZGroupProperty('group.element')
                .addProperty(lineThicknessProperty)
                .addProperty(lineColorProperty)
                .addProperty(lineTypeProperty)
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