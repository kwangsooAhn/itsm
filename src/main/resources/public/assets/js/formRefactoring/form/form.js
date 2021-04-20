/**
 * Form Class.
 *
 * 폼은 1개, 혹은 그 이상의 Group으로 구성된다.
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
import { CLASS_PREFIX, UNIT, FORM } from '../lib/constants.js';

export default class Form {
    constructor(data = {}) {
        this.type = 'form';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.name = data.name || '';
        this.desc = data.desc || '';
        this.status = data.status || 'form.status.edit'; // 문서 상태 : 편집, 발생, 사용, 폐기
        this.width = data.width || '905';
        this.margin = data.margin || '60 0 60 0';
        this.padding = data.padding || '15 15 15 15';
        this.category = data.category || 'process'; // process | cmdb

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        const formCssText = `width:${this.width + UNIT.PX};` +
            `margin:${this.margin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};` +
            `padding:${this.padding.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`;

        this.UIElement = new UIForm()
            .setUIId(this.id)
            .setUICSSText(formCssText);
    }

    setName(name) {
        this.name = name;
    }

    setDesc(desc) {
        this.desc = desc;
    }

    setStatus(status) {
        this.status = status;
    }

    setWidth(width) {
        this.width = width;
        this.UIElement.setUIWidth(this.width + UNIT.PX);
    }

    setMarginTop(top) {
        const margin = this.margin.split(' ');
        margin[0] = top;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginTop(top + UNIT.PX);
    }

    setMarginRight(right) {
        const margin = this.margin.split(' ');
        margin[1] = right;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginRight(right + UNIT.PX);
    }

    setMarginBottom(bottom) {
        const margin = this.margin.split(' ');
        margin[2] = bottom;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginBottom(bottom + UNIT.PX);
    }

    setMarginLeft(left) {
        const margin = this.margin.split(' ');
        margin[3] = left;
        this.margin = margin.join(' ');
        this.UIElement.setUIMarginLeft(left + UNIT.PX);
    }

    setPaddingTop(top) {
        const padding = this.padding.split(' ');
        padding[0] = top;
        this.padding = padding.join(' ');
        this.UIElement.setUIPaddingTop(top + UNIT.PX);
    }

    setPaddingRight(right) {
        const padding = this.padding.split(' ');
        padding[1] = right;
        this.padding = padding.join(' ');
        this.UIElement.setUIPaddingRight(right + UNIT.PX);
    }

    setPaddingBottom(bottom) {
        const padding = this.padding.split(' ');
        padding[2] = bottom;
        this.padding = padding.join(' ');
        this.UIElement.setUIPaddingBottom(bottom + UNIT.PX);
    }

    setPaddingLeft(left) {
        const padding = this.padding.split(' ');
        padding[3] = left;
        this.padding = padding.join(' ');
        this.UIElement.setUIPaddingLeft(padding + UNIT.PX);
    }
    // 세부 속성
    getProperty() {
        // 기존 데이터 속성과 패널에 표시되는 기본 속성을 merge 한 후, 조회한다.
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
            'name': {
                'name': 'form.properties.name',
                'type': 'input',
                'unit': '',
                'help': '',
                'columnWidth': '12',
                'validate': {
                    'required': true,
                    'type': '',
                    'max': '',
                    'min': '',
                    'maxLength': '128',
                    'minLength': ''
                }
            },
            'desc': {
                'name': 'form.properties.desc',
                'type': 'textarea',
                'unit': '',
                'help': '',
                'columnWidth': '12',
                'validate': {
                    'required': false,
                    'type': '',
                    'max': '',
                    'min': '',
                    'maxLength': '512',
                    'minLength': ''
                }
            },
            'status': {
                'name': 'form.properties.status',
                'type': 'select',
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
                },
                'option': [
                    { 'name': 'form.status.edit', 'value': 'form.status.edit' },
                    { 'name': 'form.status.publish', 'value': 'form.status.publish' },
                    { 'name': 'form.status.use', 'value': 'form.status.use' },
                    { 'name': 'form.status.destroy', 'value': 'form.status.destroy'}
                ]
            },
            'width': {
                'name': 'form.properties.width',
                'type': 'input',
                'unit': 'px',
                'help': '',
                'columnWidth': '12',
                'validate': {
                    'required': true,
                    'type': 'number',
                    'max': '8192',
                    'min': '0',
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

    // 복사 (자식 포함)
    copy(source) {
        this.type = source.type;
        this.id =  source.id;
        this.displayOrder = source.displayOrder;
        this.name = source.name;
        this.desc = source.desc;
        this.status = source.status;
        this.width = source.width;
        this.margin = source.margin;
        this.padding = source.padding;
        this.category = source.category;
        this.parent = source.parent;
        this.UIElement = source.UIElement;

        for (let i = 0; i < source.children.length; i ++) {
            const child = source.children[i];
            this.add(child.clone(), i);
        }
        return this;
    }
    toJSon() {
        return {
            id: this.id,
            name: this.name,
            desc: this.desc,
            status: this.status,
            width: this.width,
            margin: this.margin,
            padding: this.padding,
            category: this.category
        };
    }
}

export class UIForm extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + FORM.LAYOUT.FORM;
    }
}