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
import {CLASS_PREFIX, FORM} from '../lib/constants.js';

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

        this.init();
    }
    // 초기화
    init() {
        // row 용 툴팁
        const rowTooltip = new UIRowTooltip().setMargin(this.margin);
        // row
        rowTooltip.UIRow = new UIRow()
            .setId(this.id)
            .setPadding(this.padding);
        rowTooltip.add(rowTooltip.UIRow);
        this.UIElement = rowTooltip;
    }

    setMarginTop(top) {
        const margin = this.margin.split(' ');
        margin[0] = top;
        this.margin = margin.join(' ');
        this.UIElement.setMarginTop(top + 'px');
    }

    setMarginRight(right) {
        const margin = this.margin.split(' ');
        margin[1] = right;
        this.margin = margin.join(' ');
        this.UIElement.setMarginRight(right + 'px');
    }

    setMarginBottom(bottom) {
        const margin = this.margin.split(' ');
        margin[2] = bottom;
        this.margin = margin.join(' ');
        this.UIElement.setMarginBottom(bottom + 'px');
    }

    setMarginLeft(left) {
        const margin = this.margin.split(' ');
        margin[3] = left;
        this.margin = margin.join(' ');
        this.UIElement.setMarginLeft(left + 'px');
    }

    setPaddingTop(top) {
        const padding = this.padding.split(' ');
        padding[0] = top;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setPaddingTop(top + 'px');
    }

    setPaddingRight(right) {
        const padding = this.padding.split(' ');
        padding[1] = right;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setPaddingRight(right + 'px');
    }

    setPaddingBottom(bottom) {
        const padding = this.padding.split(' ');
        padding[2] = bottom;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setPaddingBottom(bottom + 'px');
    }

    setPaddingLeft(left) {
        const padding = this.padding.split(' ');
        padding[3] = left;
        this.padding = padding.join(' ');
        this.UIElement.UIRow.setPaddingLeft(padding + 'px');
    }

    // 세부 속성
    getProperty() {
        const PROPERTIES = {
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
        return Object.entries(PROPERTIES).reduce((property, [key, value]) => {
            property[key] = Object.assign(value, { 'value': this[key] });
            return property;
        }, {});
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