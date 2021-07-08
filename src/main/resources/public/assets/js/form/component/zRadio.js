/**
 * Radio Mixin
 *
 *
 * @author Ahn kwangsoo <ks.ahn@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import { FORM, CLASS_PREFIX } from '../../lib/zConstants.js';
import { UIDiv, UILabel, UIRadioButton, UISpan } from '../../lib/zUI.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZSwitchButtonProperty from '../../formDesigner/property/type/zSwitchButtonProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';
import ZOptionListProperty from '../../formDesigner/property/type/zOptionListProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        position: FORM.ELEMENT.POSITION.LEFT,
        columnWidth: 10,
        align: FORM.ELEMENT.ALIGN.HORIZONTAL,
        options: [FORM.DEFAULT_OPTION_ROW]
    },
    validation: {
        required: false, // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const radioMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv()
            .setUIClass(CLASS_PREFIX + 'element')
            .addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);
        return this.makeRadioButton(element);
    },
    afterEvent() {},
    set element(element) {
        this._element = element;
    },
    get element() {
        return this._element;
    },
    // set, get
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementAlign(align) {
        this._element.align = align;
        this.UIElement.UIComponent.UIElement.clearUI();
        this.makeRadioButton(this.UIElement.UIComponent.UIElement);
    },
    get elementAlign() {
        return this._element.align;
    },
    set elementPosition(position) {
        this._element.position = position;
        this.UIElement.UIComponent.UIElement.clearUI();
        this.makeRadioButton(this.UIElement.UIComponent.UIElement);
    },
    get elementPosition() {
        return this._element.position;
    },
    set elementOptions(options) {
        this._element.options = options;
        this.UIElement.UIComponent.UIElement.clearUI();
        this.makeRadioButton(this.UIElement.UIComponent.UIElement);
    },
    get elementOptions() {
        return this._element.options;
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
    updateValue(e) {
        e.stopPropagation();

        this.value = e.target.value;
    },
    makeRadioButton(object) {
        for (let i = 0; i < this.element.options.length; i++) {
            let checkedYn = false;
            if(this._value !== '' && this.element.options[i].value !== '') {
                checkedYn = this._value === this.element.options[i].value;
            }
            const radioId = 'radio'
                + this.id.substr(0, 1).toUpperCase()
                + this.id.substr(1, this.id.length)
                + (i + 1);
            object.UILabel = new UILabel()
                .setUIFor(radioId)
                .setUIClass(this.element.align)
                .addUIClass('radio');
            object.UILabel.UIRadio = new UIRadioButton(checkedYn)
                .setUIId(radioId)
                .setUIAttribute('value', this.element.options[i].value)
                .setUIAttribute('name', this.id)
                .onUIClick(this.updateValue.bind(this));
            object.UILabel.UISpan = new UISpan().setUITextContent(this.element.options[i].name);

            if (this.element.position === FORM.ELEMENT.POSITION.RIGHT) {
                object.UILabel.addUI(object.UILabel.UISpan);
                object.UILabel.addUI(object.UILabel.UIRadio);
                object.UILabel.addUI(new UISpan());
                object.addUI(object.UILabel);
            } else {
                object.UILabel.addUI(object.UILabel.UIRadio);
                object.UILabel.addUI(new UISpan());
                object.UILabel.addUI(object.UILabel.UISpan);
                object.addUI(object.UILabel);
            }
        }
        return object;
    },
    // 세부 속성 조회
    getProperty() {
        return [...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZDropdownProperty('elementAlign', 'element.align', this._element.align, [
                    {name: i18n.msg('form.properties.align.horizontal'), value: 'horizontal'},
                    {name: i18n.msg('form.properties.align.vertical'), value: 'vertical'}
                ]))
                .addProperty(new ZSwitchButtonProperty('elementPosition', 'element.position', this._element.position, [
                    {'name': 'icon-display-position-left', 'value': 'left'},
                    {'name': 'icon-display-position-right', 'value': 'right'},
                ]))
                .addProperty(new ZOptionListProperty('elementOptions', 'element.options', this.elementOptions)),
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
