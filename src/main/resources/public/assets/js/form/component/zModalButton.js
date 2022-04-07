/**
 * Modal Button Mixin
 *
 * @author jy.lim <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZInputBoxProperty from "../../formDesigner/property/type/zInputBoxProperty.js";
import ZOptionListProperty from "../../formDesigner/property/type/zOptionListProperty.js";
import ZSwitchProperty from "../../formDesigner/property/type/zSwitchProperty.js";
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import {FORM, UNIT} from '../../lib/zConstants.js';
import {UIButton, UIDiv, UISpan} from '../../lib/zUI.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        text: i18n.msg('common.btn.inquiry'),
        options: [FORM.DEFAULT_OPTION_ROW]
    },
    validation: {
        required: false, // 필수값 여부
    },
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const modalButtonMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        console.log(this.data);
        console.log(this.data.value);
        console.log(this._value);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv()
            .setUIClass('z-element')
            .addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIButton = new UIButton()
            .setUIClass('z-button')
            .addUIClass('secondary')
        element.UIButton.UIText = new UISpan()
            .setUIId('elementButtonText')
            .setUITextContent(this.elementButtonText)
            .onUIChange(this.updateValue.bind(this));
        element.UIButton.addUI(element.UIButton.UIText);
        element.addUI(element.UIButton);
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
    set element(element) {
        this._element = element;
    },
    get element() {
        return this._element;
    },
    set elementButtonText(value) {
        this._element.text = value;
        this.UIElement.UIComponent.UIElement.UIButton.UIText.setUITextContent(value);
    },
    get elementButtonText() {
        return this._element.text;
    },
    set elementOptions(options) {
        this._element.options = options;
    },
    get elementOptions() {
        return this._element.options;
    },
    set elementOrderBy(value) {
        this._element.orderBy = value;
    },
    get elementOrderBy() {
        return this._element.orderBy || 'true';
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    // 기본 값 변경
    set value(value) {
        this._value = value;
    },
    // 기본 값 조회
    get value() {
        return this._value;
    },
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();

        this.value = e.target.value;

        console.log(this.value);
    },
    // 세부 속성 조회
    getProperty() {
        // 엘리먼트 설정
        // element - 조회 대상
        const searchTargetProperty = new ZInputBoxProperty('elementSearchTarget', 'element.searchTarget', '')
            .setValidation(true, '', '', '', '', '');
        // 데이터 정렬 설정
        // element - 항목명(field) / 너비
        const fieldProperty = new ZOptionListProperty('fieldOptions', 'element.options', this.elementOptions, true)
            .setValidation(true, '', '', '', '', '');
        // element - 정렬 기준(order by field / asc desc)
        const orderByProperty = new ZSwitchProperty('fieldOrderBy', 'element.orderByAsc', this.elementOrderBy);

        // element - 모달 크기 (w/h)
        const modalWidthProperty = new ZInputBoxProperty('elementModalWidth', 'element.width', '')
            .setValidation(false, 'number', '0', '1720', '', '');
        modalWidthProperty.unit = UNIT.PX;
        const modalHeightProperty = new ZInputBoxProperty('elementModalHeight', 'element.height', '')
            .setValidation(false, 'number', '0', '800', '', '');
        modalHeightProperty.unit = UNIT.PX;
        return [

            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZInputBoxProperty('elementButtonText', 'element.buttonText', this.elementButtonText)
                    .setValidation(true, '', '', '', '', ''))
                .addProperty(searchTargetProperty),
            new ZGroupProperty('group.sort')
                .addProperty(fieldProperty)
                .addProperty(orderByProperty),
            new ZGroupProperty('group.option')
                .addProperty(modalWidthProperty)
                .addProperty(modalHeightProperty),
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
        return true;
    }
};
