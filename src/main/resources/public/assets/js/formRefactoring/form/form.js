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
import * as mixin from '../lib/mixins.js';
import { UIDiv } from '../lib/ui.js';
import { CLASS_PREFIX, UNIT, FORM } from '../lib/constants.js';
import ClipboardProperty from '../formDesigner/property/type/clipboardProperty.module.js';
import InputBoxProperty from '../formDesigner/property/type/inputBoxProperty.module.js';
import TextAreaProperty from '../formDesigner/property/type/textAreaProperty.module.js';
import DropdownProperty from '../formDesigner/property/type/dropdownProperty.module.js';
import GroupProperty from '../formDesigner/property/type/groupProperty.module.js';
import BoxModelProperty from '../formDesigner/property/type/boxModelProperty.module.js';

export default class Form {
    constructor(data = {}) {
        this.type = 'form';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.name = data.name || '';
        this.desc = data.desc || '';
        this.status = data.status || 'form.status.edit'; // 문서 상태 : 편집, 발생, 사용, 폐기
        this.category = data.category || 'process'; // process | cmdb

        this.display = {
            width: data.display.width || '905',
            margin: data.display.margin || '60 0 60 0',
            padding: data.display.padding || '15 15 15 15'
        };

        // Control Mixin import
        aliceJs.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        const formCssText = `width:${this.display.width + UNIT.PX};` +
            `margin:${this.display.margin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};` +
            `padding:${this.display.padding.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`;

        this.UIElement = new UIForm()
            .setUIId(this.id)
            .setUICSSText(formCssText);
    }

    setName(name) {
        this.name = name;
        if (typeof this.parent.setFormName === 'function') {
            this.parent.setFormName(name);
        }
    }

    getName() {
        return this.name;
    }

    setDesc(desc) {
        this.desc = desc;
    }

    getDesc() {
        return this.desc;
    }

    setStatus(status) {
        this.status = status;
    }

    getStatus() {
        return this.status;
    }

    setDisplayWidth(width) {
        this.display.width = width;
        this.UIElement.setUIWidth(this.display.width + UNIT.PX);
    }

    getDisplayWidth() {
        return this.display.width;
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
        this.UIElement.setUIPaddingTop(top + UNIT.PX);
    }

    getDisplayPaddingTop() {
        const padding = this.display.padding.split(' ');
        return padding[0];
    }

    setDisplayPaddingRight(right) {
        const padding = this.display.padding.split(' ');
        padding[1] = right;
        this.display.padding = padding.join(' ');
        this.UIElement.setUIPaddingRight(right + UNIT.PX);
    }

    getDisplayPaddingRight() {
        const padding = this.display.padding.split(' ');
        return padding[1];
    }

    setDisplayPaddingBottom(bottom) {
        const padding = this.display.padding.split(' ');
        padding[2] = bottom;
        this.display.padding = padding.join(' ');
        this.UIElement.setUIPaddingBottom(bottom + UNIT.PX);
    }

    getDisplayPaddingBottom() {
        const padding = this.display.padding.split(' ');
        return padding[2];
    }

    setDisplayPaddingLeft(left) {
        const padding = this.display.padding.split(' ');
        padding[3] = left;
        this.display.padding = padding.join(' ');
        this.UIElement.setUIPaddingLeft(padding + UNIT.PX);
    }

    getDisplayPaddingLeft() {
        const padding = this.display.padding.split(' ');
        return padding[3];
    }
    // 세부 속성
    getProperty() {
        // display 속성 - width
        const displayWidthProperty = new InputBoxProperty('display.width', this.display.width)
            .setValidation(true, 'number', '0', '8192', '', '');
        displayWidthProperty.unit = 'px';

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
            new InputBoxProperty('name', this.name).setValidation(true, '', '', '', '', '128'),
            new TextAreaProperty('desc', this.desc).setValidation(false, '', '', '', '', '512'),
            new DropdownProperty('status', this.status, [
                { 'name': 'form.status.edit', 'value': 'form.status.edit' },
                { 'name': 'form.status.publish', 'value': 'form.status.publish' },
                { 'name': 'form.status.use', 'value': 'form.status.use' },
                { 'name': 'form.status.destroy', 'value': 'form.status.destroy'}
            ]),
            new GroupProperty('group.display')
                .addProperty(displayWidthProperty)
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
        this.name = source.name;
        this.desc = source.desc;
        this.status = source.status;
        this.display = source.display;
        this.category = source.category;
        this.parent = source.parent;
        if (flag) { this.id = source.id; }

        this.init();

        source.children.forEach((child, index) =>{
            this.add(child.clone(flag), index);
        });
        return this;
    }
    // json 데이터 추출
    toJson() {
        const group = [];
        for (let i = 0; i < this.children.length; i ++) {
            const child = this.children[i];
            group.push(child.toJson());
        }
        return {
            id: this.id,
            name: this.name,
            desc: this.desc,
            status: this.status,
            display: this.display,
            category: this.category,
            group: group
        };
    }
}

export class UIForm extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + FORM.LAYOUT.FORM;
    }
}