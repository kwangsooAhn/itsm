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
import * as mixin from '../lib/mixins.js';
import { UIDiv } from '../lib/ui.js';
import {CLASS_PREFIX, FORM, UNIT} from '../lib/constants.js';
import ClipboardProperty from '../formDesigner/property/type/clipboardProperty.module.js';
import GroupProperty from '../formDesigner/property/type/groupProperty.module.js';
import BoxModelProperty from '../formDesigner/property/type/boxModelProperty.module.js';

export default class Row {
    constructor(data = {}) {
        this.type = 'row';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.display = data.display || {
            displayOrder: 0,     // 표시 순서
            margin: '10 0 10 0', // row 간 간격(위 오른쪽 아래 왼쪽)
            padding: '10 10 10 10' // row 내부 여백(위 오른쪽 아래 왼쪽)
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
            .setUICSSText(`margin:${this.display.margin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);
        // row
        rowTooltip.UIRow = new UIRow().setUIId(this.id)
            .setUICSSText(`padding:${this.display.padding.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);

        rowTooltip.addUI(rowTooltip.UIRow);
        // 툴팁
        rowTooltip.UITooltipMenu = this.makeTooltip();
        rowTooltip.addUI(rowTooltip.UITooltipMenu);

        this.UIElement = rowTooltip;
    }

    setDisplayMarginTop(top) {
        const margin = this.display.margin.split(' ');
        margin[0] = top;
        this.display.margin = margin.join(' ');
        this.UIElement.setUIMarginTop(top + UNIT.PX);
    }

    getDisplayMarginTop() {
        const margin = this.display.margin.split(' ');
        return margin[0];
    }

    setDisplayMarginRight(right) {
        const margin = this.display.margin.split(' ');
        margin[1] = right;
        this.display.margin = margin.join(' ');
        this.UIElement.setUIMarginRight(right + UNIT.PX);
    }

    getDisplayMarginRight() {
        const margin = this.display.margin.split(' ');
        return margin[1];
    }

    setDisplayMarginBottom(bottom) {
        const margin = this.display.margin.split(' ');
        margin[2] = bottom;
        this.display.margin = margin.join(' ');
        this.UIElement.setUIMarginBottom(bottom + UNIT.PX);
    }

    getDisplayMarginBottom() {
        const margin = this.display.margin.split(' ');
        return margin[2];
    }

    setDisplayMarginLeft(left) {
        const margin = this.display.margin.split(' ');
        margin[3] = left;
        this.display.margin = margin.join(' ');
        this.UIElement.setUIMarginLeft(left + UNIT.PX);
    }

    getDisplayMarginLeft() {
        const margin = this.display.margin.split(' ');
        return margin[3];
    }

    setDisplayPaddingTop(top) {
        const padding = this.display.padding.split(' ');
        padding[0] = top;
        this.display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingTop(top + UNIT.PX);
    }

    getDisplayPaddingTop() {
        const padding = this.display.padding.split(' ');
        return padding[0];
    }

    setDisplayPaddingRight(right) {
        const padding = this.display.padding.split(' ');
        padding[1] = right;
        this.display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingRight(right + UNIT.PX);
    }

    getDisplayPaddingRight() {
        const padding = this.display.padding.split(' ');
        return padding[1];
    }

    setDisplayPaddingBottom(bottom) {
        const padding = this.display.padding.split(' ');
        padding[2] = bottom;
        this.display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingBottom(bottom + UNIT.PX);
    }

    getDisplayPaddingBottom() {
        const padding = this.display.padding.split(' ');
        return padding[2];
    }

    setDisplayPaddingLeft(left) {
        const padding = this.display.padding.split(' ');
        padding[3] = left;
        this.display.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingLeft(padding + UNIT.PX);
    }

    getDisplayPaddingLeft() {
        const padding = this.display.padding.split(' ');
        return padding[3];
    }

    // 세부 속성
    getProperty() {
        // display 속성 - margin
        const displayMarginProperty = new BoxModelProperty('display.margin', this.display.margin)
            .setValidation(false, 'number', '0', '100', '', '');
        displayMarginProperty.unit = 'px';

        // display 속성 - padding
        const displayPaddingProperty = new BoxModelProperty('display.padding', this.display.padding)
            .setValidation(false, 'number', '0', '100', '', '');
        displayPaddingProperty.unit = 'px';

        return [
            new ClipboardProperty('id', this.id),
            new GroupProperty('group.display')
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
        this.type = source.type;
        this.display = source.display;
        this.parent = source.parent;
        if (flag) { this.id = source.id; }

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
            id: this.id,
            display: this.display,
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