/**
 * Group 내부를 이루는 구성 Class.
 *
 *  내부적으로 너비를 가지는 복수개의 엘리먼트들로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as mixin from '../lib/zMixins.js';
import { UIDiv } from '../lib/zUI.js';
import { CLASS_PREFIX, FORM, UNIT } from '../lib/zConstants.js';
import ZClipboardProperty from '../formDesigner/property/type/zClipboardProperty.js';
import ZGroupProperty from '../formDesigner/property/type/zGroupProperty.js';
import ZBoxModelProperty from '../formDesigner/property/type/zBoxModelProperty.js';

export default class ZRow {
    constructor(data = {}) {
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this._type = 'row';
        this._id =  data.id || ZWorkflowUtil.generateUUID();
        this._display = data.display || {
            displayOrder: 0,     // 표시 순서
            margin: '4 0 4 0', // row 간 간격(위 오른쪽 아래 왼쪽)
            padding: '0 0 0 0' // row 내부 여백(위 오른쪽 아래 왼쪽)
        };
        // Control Mixin import
        aliceJs.importMixin(this, mixin.controlMixin);
        // Tooltip Mixin import
        aliceJs.importMixin(this, mixin.toolTipMenuMixin);

        this.init();
    }
    // 초기화
    init() {
        // row 용 툴팁
        const rowTooltip = new UIRowTooltip()
            .setUICSSText(`margin:${this.displayMargin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);
        // row
        rowTooltip.UIRow = new UIRow().setUIId(this.id)
            .setUICSSText(`padding:${this.displayPadding.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);

        rowTooltip.addUI(rowTooltip.UIRow);
        // 툴팁
        rowTooltip.UITooltipMenu = this.makeTooltip();
        rowTooltip.addUI(rowTooltip.UITooltipMenu);

        this.UIElement = rowTooltip;
    }

    get type() {
        return this._type;
    }

    set type(type) {
        this._type = type;
    }

    get id() {
        return this._id;
    }

    set id(id) {
        this._id = id;
    }

    set display(display) {
        this._display = display;
    }

    get display() {
        return this._display;
    }

    set displayDisplayOrder(displayOrder) {
        this._display.displayOrder = displayOrder;
    }

    get displayDisplayOrder() {
        return this._display.displayOrder;
    }

    set displayMargin(margin) {
        this._display.margin = margin;
    }

    get displayMargin() {
        return this._display.margin;
    }

    set displayMarginTop(top) {
        const margin = this._display.margin.split(' ');
        margin[0] = top;
        this._display.margin = margin.join(' ');
        this.UIElement.setUIMarginTop(top + UNIT.PX);
    }

    get displayMarginTop() {
        const margin = this._display.margin.split(' ');
        return margin[0];
    }

    set displayMarginRight(right) {
        const margin = this._display.margin.split(' ');
        margin[1] = right;
        this._display.margin = margin.join(' ');
        this.UIElement.setUIMarginRight(right + UNIT.PX);
    }

    get displayMarginRight() {
        const margin = this._display.margin.split(' ');
        return margin[1];
    }

    set displayMarginBottom(bottom) {
        const margin = this._display.margin.split(' ');
        margin[2] = bottom;
        this._display.margin = margin.join(' ');
        this.UIElement.setUIMarginBottom(bottom + UNIT.PX);
    }

    get displayMarginBottom() {
        const margin = this._display.margin.split(' ');
        return margin[2];
    }

    set displayMarginLeft(left) {
        const margin = this._display.margin.split(' ');
        margin[3] = left;
        this._display.margin = margin.join(' ');
        this.UIElement.setUIMarginLeft(left + UNIT.PX);
    }

    get displayMarginLeft() {
        const margin = this._display.margin.split(' ');
        return margin[3];
    }

    set displayPadding(padding) {
        this._display.padding = padding;
    }

    get displayPadding() {
        return this._display.padding;
    }

    set displayPaddingTop(top) {
        const padding = this._display.padding.split(' ');
        padding[0] = top;
        this._display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingTop(top + UNIT.PX);
    }

    get displayPaddingTop() {
        const padding = this._display.padding.split(' ');
        return padding[0];
    }

    set displayPaddingRight(right) {
        const padding = this._display.padding.split(' ');
        padding[1] = right;
        this._display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingRight(right + UNIT.PX);
    }

    get displayPaddingRight() {
        const padding = this._display.padding.split(' ');
        return padding[1];
    }

    set displayPaddingBottom(bottom) {
        const padding = this._display.padding.split(' ');
        padding[2] = bottom;
        this._display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingBottom(bottom + UNIT.PX);
    }

    get displayPaddingBottom() {
        const padding = this._display.padding.split(' ');
        return padding[2];
    }

    set displayPaddingLeft(left) {
        const padding = this._display.padding.split(' ');
        padding[3] = left;
        this._display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingLeft(padding + UNIT.PX);
    }

    get displayPaddingLeft() {
        const padding = this._display.padding.split(' ');
        return padding[3];
    }

    // 세부 속성
    getProperty() {
        // display 속성 - margin
        const displayMarginProperty = new ZBoxModelProperty('display.margin', this.displayMargin)
            .setValidation(false, 'number', '0', '100', '', '');
        displayMarginProperty.unit = UNIT.PX;

        // display 속성 - padding
        const displayPaddingProperty = new ZBoxModelProperty('display.padding', this.displayPadding)
            .setValidation(false, 'number', '0', '100', '', '');
        displayPaddingProperty.unit = UNIT.PX;

        return [
            new ZClipboardProperty('id', this.id),
            new ZGroupProperty('group.display')
                .addProperty(displayMarginProperty)
                .addProperty(displayPaddingProperty)
        ];
    }

    /**
     * 현재 객체를 대상이 되는 객체로 변경 (복사) 하여 반환한다
     * @param source 대상 객체
     * @param flag 객체의 키가 되는 id도 복제할지 여부 (true이면 id도 복제됨)
     */
    copy(source, flag) {
        this.parent = source.parent;
        this._type = source.type;
        this._display = source.display;
        if (flag) { this._id = source.id; }

        this.init();

        source.children.forEach((child, index) =>{
            this.add(child.clone(flag, { type: child.type }), index);
        });
        return this;
    }
    // json 데이터 추출
    toJson() {
        const component = [];
        for (let i = 0; i < this.children.length; i ++) {
            const child = this.children[i];
            component.push(child.toJson());
        }
        return {
            id: this._id,
            display: this._display,
            component: component
        };
    }
}
export class UIRowTooltip extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + 'row-tooltip';
    }
}

export class UIRow extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + FORM.LAYOUT.ROW + ' flex-row';
    }
}