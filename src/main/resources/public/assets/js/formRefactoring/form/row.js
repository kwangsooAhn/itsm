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

import * as util from '../lib/util.js';
import * as mixin from '../lib/mixins.js';
import { UIDiv } from '../lib/ui.js';
import {CLASS_PREFIX, FORM, UNIT} from '../lib/constants.js';

export default class Row {
    constructor(data = {}) {
        this.type = 'row';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.margin = data.margin || '10 0 10 0'; // row 간 간격(위 오른쪽 아래 왼쪽)
        this.padding = data.padding || '10 10 10 10'; // row 내부 여백(위 오른쪽 아래 왼쪽)

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);
        // Tooltip Mixin import
        util.importMixin(this, mixin.toolTipMenuMixin);

        this.init();
    }
    // 초기화
    init() {
        // row 용 툴팁
        const rowTooltip = new UIRowTooltip()
            .setUICSSText(`margin:${this.margin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);
        // row
        rowTooltip.UIRow = new UIRow().setUIId(this.id)
            .setUICSSText(`padding:${this.padding.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);

        rowTooltip.addUI(rowTooltip.UIRow);
        // 툴팁
        rowTooltip.UITooltipMenu = this.makeTooltip();
        rowTooltip.addUI(rowTooltip.UITooltipMenu);

        this.UIElement = rowTooltip;
    }

    setMarginTop(top) {
        const margin = this.margin.split(' ');
        margin[0] = top;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginTop(top + UNIT.PX);
    }

    getMarginTop() {
        const margin = this.margin.split(' ');
        return margin[0];
    }

    setMarginRight(right) {
        const margin = this.margin.split(' ');
        margin[1] = right;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginRight(right + UNIT.PX);
    }

    getMarginRight() {
        const margin = this.margin.split(' ');
        return margin[1];
    }

    setMarginBottom(bottom) {
        const margin = this.margin.split(' ');
        margin[2] = bottom;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginBottom(bottom + UNIT.PX);
    }

    getMarginBottom() {
        const margin = this.margin.split(' ');
        return margin[2];
    }

    setMarginLeft(left) {
        const margin = this.margin.split(' ');
        margin[3] = left;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginLeft(left + UNIT.PX);
    }

    getMarginLeft() {
        const margin = this.margin.split(' ');
        return margin[3];
    }

    setPaddingTop(top) {
        const padding = this.padding.split(' ');
        padding[0] = top;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingTop(top + UNIT.PX);
    }

    getPaddingTop() {
        const padding = this.padding.split(' ');
        return padding[0];
    }

    setPaddingRight(right) {
        const padding = this.padding.split(' ');
        padding[1] = right;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingRight(right + UNIT.PX);
    }

    getPaddingRight() {
        const padding = this.padding.split(' ');
        return padding[1];
    }

    setPaddingBottom(bottom) {
        const padding = this.padding.split(' ');
        padding[2] = bottom;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingBottom(bottom + UNIT.PX);
    }

    getPaddingBottom() {
        const padding = this.padding.split(' ');
        return padding[2];
    }

    setPaddingLeft(left) {
        const padding = this.padding.split(' ');
        padding[3] = left;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setUIPaddingLeft(padding + UNIT.PX);
    }

    getPaddingLeft() {
        const padding = this.padding.split(' ');
        return padding[3];
    }

    // 세부 속성
    getProperty() {
        const PANEL_PROPERTIES = {
            'id': {
                'name': 'form.properties.id',
                'type': 'clipboard',
                'unit': '',
                'help': '',
                'columnWidth': '12',
                'validate': {
                    'required': false,
                    'type': '',
                    'max': '',
                    'min': '',
                    'maxLength': '',
                    'minLength': ''
                }
            },
            'margin': {
                'name': 'form.properties.margin',
                'type': 'input-box',
                'unit': 'px',
                'help': '',
                'columnWidth': '12',
                'validate': {
                    'required': false,
                    'type': 'number',
                    'max': '100',
                    'min': '0',
                    'maxLength': '',
                    'minLength': ''
                }
            },
            'padding': {
                'name': 'form.properties.padding',
                'type': 'input-box',
                'unit': 'px',
                'help': '',
                'columnWidth': '12',
                'validate': {
                    'required': false,
                    'type': 'number',
                    'max': '100',
                    'min': '0',
                    'maxLength': '',
                    'minLength': ''
                }
            }
        };
        return Object.entries(PANEL_PROPERTIES).reduce((property, [key, value]) => {
            property[key] = Object.assign(value, { 'value': this[key] });
            return property;
        }, {});
    }

    /**
     * 현재 객체를 대상이 되는 객체로 변경 (복사) 하여 반환한다
     * @param source 대상 객체
     * @param flag 객체의 키가 되는 id도 복제할지 여부 (true이면 id도 복제됨)
     */
    copy(source, flag) {
        this.type = source.type;
        this.displayOrder = source.displayOrder;
        this.margin = source.margin;
        this.padding = source.padding;
        this.parent = source.parent;
        if (flag) { this.id = source.id; }

        this.init();

        source.children.forEach((child, index) =>{
            this.add(child.clone(flag, { type: child.type }), index);
        });
        return this;
    }

    toJson() {
        const components = [];
        for (let i = 0; i < this.children.length; i ++) {
            const child = this.children[i];
            components.push(child.toJson());
        }
        return {
            id: this.id,
            displayOrder: this.displayOrder,
            margin: this.margin,
            padding: this.padding,
            components: components
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