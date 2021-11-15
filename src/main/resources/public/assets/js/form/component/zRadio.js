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

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZOptionListProperty from '../../formDesigner/property/type/zOptionListProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchButtonProperty from '../../formDesigner/property/type/zSwitchButtonProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv, UILabel, UIRadioButton, UISpan } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

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
            .setUIClass('z-element')
            .addUIClass('align-left')
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIProperty('--data-column', this.elementColumnWidth);
        return this.makeRadioButton(element);
    },
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.parent?.parent?.displayType === FORM.DISPLAY_TYPE.READONLY) {
            for (let i = 0; i < this.element.options.length; i++) {
                this.UIElement.UIComponent.UIElement['UILabel' + i].addUIClass('readonly');
                this.UIElement.UIComponent.UIElement['UILabel' + i].UIRadio.addUIClass('readonly');
            }
        }
    },
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

        const firstRadioButton = this.UIElement.UIComponent.UIElement.UILabel0.UIRadio;
        if (firstRadioButton.hasUIClass(zValidation.getErrorClassName())) {
            firstRadioButton.removeUIClass(zValidation.getErrorClassName());
        }

        this.value = e.target.value;
    },
    makeRadioButton(object) {
        for (let i = 0; i < this.element.options.length; i++) {
            let checkedYn = (this.element.options[i].checked || false);
            if (this._value !== '' && this.element.options[i].value !== '') {
                checkedYn = this._value === this.element.options[i].value;
            }
            const radioId = 'radio'
                + this.id.substr(0, 1).toUpperCase()
                + this.id.substr(1, this.id.length)
                + (i + 1);
            object['UILabel' + i] = new UILabel()
                .setUIFor(radioId)
                .setUIClass(this.element.align)
                .addUIClass('z-radio')
                .setUIAttribute('tabindex', '-1');
            object['UILabel' + i].UIRadio = new UIRadioButton(checkedYn)
                .setUIId(radioId)
                .setUIAttribute('value', this.element.options[i].value)
                .setUIAttribute('name', this.id)
                .onUIClick(this.updateValue.bind(this));
            object['UILabel' + i].UISpan = new UISpan().setUITextContent(this.element.options[i].name);

            if (this.element.position === FORM.ELEMENT.POSITION.RIGHT) {
                object['UILabel' + i].addUI(object['UILabel' + i].UISpan);
                object['UILabel' + i].addUI(object['UILabel' + i].UIRadio);
                object['UILabel' + i].addUI(new UISpan());
                object.addUI(object['UILabel' + i]);
            } else {
                object['UILabel' + i].addUI(object['UILabel' + i].UIRadio);
                object['UILabel' + i].addUI(new UISpan());
                object['UILabel' + i].addUI(object['UILabel' + i].UISpan);
                object.addUI(object['UILabel' + i]);
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
                    {'name': 'i-display-position-left', 'value': 'left'},
                    {'name': 'i-display-position-right', 'value': 'right'},
                ]))
                .addProperty(new ZOptionListProperty('elementOptions', 'element.options', this.elementOptions, false)),
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
    },
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        return !this.isEmptyOptions(this.element.options);
    }
};
