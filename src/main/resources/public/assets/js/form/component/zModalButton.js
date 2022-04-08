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

import { FORM, UNIT } from '../../lib/zConstants.js';
import { UIButton, UIDiv, UISpan } from '../../lib/zUI.js';
import ZColumnProperty, { propertyExtends } from "../../formDesigner/property/type/zColumnProperty.js";
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZInputBoxProperty from "../../formDesigner/property/type/zInputBoxProperty.js";
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZOptionListProperty from "../../formDesigner/property/type/zOptionListProperty.js";
import ZSwitchProperty from "../../formDesigner/property/type/zSwitchProperty.js";

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        columns: [{
            ...propertyExtends.fieldCommon
        }],
        text: i18n.msg('common.btn.inquiry'),
        size: {
            w: 960,
            h: 800
        },
        sort: {
            field: '',
            order: 'asc'
        },
        searchTarget: '',
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
        console.log(this.data);
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv()
            .setUIClass('z-element')
            .addUIClass('align-right')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIButton = new UIButton()
            .setUIClass('z-button')
            .addUIClass('z-modal-button')
            .addUIClass('secondary')
        element.UIButton.UIText = new UISpan()
            .setUIId('elementButtonText')
            .addUIClass('z-modal-button-text')
            .addUIClass('text-ellipsis')
            .setUITextContent(this.elementButtonText)
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
    set elementModalWidth(value) {
        this._element.size.w = value;
    },
    get elementModalWidth() {
        return this._element.size.w;
    },
    set elementModalHeight(value) {
        this._element.size.h = value;
    },
    get elementModalHeight() {
        return this._element.size.h;
    },
    set elementSearchTarget(value) {
        this._element.searchTarget = value;
    },
    get elementSearchTarget() {
        return this._element.searchTarget;
    },
    set elementOptions(options) {
        this._element.options = options;
    },
    get elementOptions() {
        return this._element.options;
    },
    set elementColumns(columns) {
        console.log(columns);
        this._element.columns = columns;
        // this.UIElement.UIComponent.UIElement.UITable.clearUIRow().clearUI();
        // this.makeTable(this.UIElement.UIComponent.UIElement.UITable);
    },
    get elementColumns() {
        return this._element.columns;
    },
    set fieldOrderTarget(value) {
        console.log(value);
        this._element.sort.field = value;
    },
    get fieldOrderTarget() {
        return this._element.sort.field;
    },
    set fieldOrderBy(value) {
        console.log(value);
        this._element.sort.order = value ? 'asc' : 'desc';
    },
    get fieldOrderBy() {
        console.log(this._element.sort.order === 'asc');
        return (this._element.sort.order === 'asc');
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
    // 세부 속성 조회
    getProperty() {
        // element - 모달 크기
        const modalWidthProperty = new ZInputBoxProperty('elementModalWidth', 'element.modalWidth', this.elementModalWidth)
            .setValidation(false, 'number', '0', '1720', '', '');
        modalWidthProperty.columnWidth = '6';
        modalWidthProperty.unit = UNIT.PX;
        const modalHeightProperty = new ZInputBoxProperty('elementModalHeight', 'element.modalHeight', this.elementModalHeight)
            .setValidation(false, 'number', '0', '800', '', '');
        modalHeightProperty.columnWidth = '6';
        modalHeightProperty.unit = UNIT.PX;

        // 데이터 정렬 설정
        // 필드 정렬 기준
        const fieldOrderTarget = new ZInputBoxProperty('elementModalHeight', 'element.searchTargetCriteria', this.fieldOrderTarget)
            .setValidation(true, '', '', '', '', '');
        // element - 항목명(field) / 너비
        const fieldProperty = new ZOptionListProperty('fieldOptions', 'element.options', this.elementOptions, true)
            .setValidation(true, '', '', '', '', '');

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZInputBoxProperty('elementButtonText', 'element.buttonText', this.elementButtonText)
                    .setValidation(true, '', '', '', '', ''))
                .addProperty(modalWidthProperty)
                .addProperty(modalHeightProperty),
            new ZGroupProperty('group.display')
                .addProperty(new ZInputBoxProperty('elementSearchTarget', 'element.searchTarget', this.elementSearchTarget)
                    .setValidation(true, '', '', '', '', ''))
                .addProperty(new ZColumnProperty('elementColumns', '', FORM.COLUMN_PROPERTY.FIELD, this.elementColumns))
                .addProperty(new ZInputBoxProperty('elementModalHeight', 'element.searchTargetCriteria', this.fieldOrderTarget))
                .addProperty(new ZSwitchProperty('fieldOrderBy', 'element.orderByAsc', this.fieldOrderBy))
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
