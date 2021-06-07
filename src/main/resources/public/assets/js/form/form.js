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
import ClipboardProperty from '../formDesigner/property/type/clipboardProperty.js';
import InputBoxProperty from '../formDesigner/property/type/inputBoxProperty.js';
import TextAreaProperty from '../formDesigner/property/type/textAreaProperty.js';
import DropdownProperty from '../formDesigner/property/type/dropdownProperty.js';
import GroupProperty from '../formDesigner/property/type/groupProperty.js';
import BoxModelProperty from '../formDesigner/property/type/boxModelProperty.js';

export default class Form {
    constructor(data = {}) {
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this._type = 'form';
        this._id =  data.id || workflowUtil.generateUUID();
        this._name = data.name || '';
        this._desc = data.desc || '';
        this._status = data.status || 'form.status.edit'; // 문서 상태 : 편집, 발생, 사용, 폐기
        this._category = data.category || 'process'; // process | cmdb
        this._display = {
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
        const formCssText = `width:${this.displayWidth + UNIT.PX};` +
            `margin:${this.displayMargin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};` +
            `padding:${this.displayPadding.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`;

        this.UIElement = new UIForm()
            .setUIId(this.id)
            .setUICSSText(formCssText);
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

    set name(name) {
        this._name = name;
        // 폼 디자이너 상단 이름도 동일하게 변경
        if (typeof this.parent.setFormName === 'function') {
            this.parent.setFormName(name);
        }
    }

    get name() {
        return this._name;
    }

    set desc(desc) {
        this._desc = desc;
    }

    get desc() {
        return this._desc;
    }

    set status(status) {
        this._status = status;
    }

    get status() {
        return this._status;
    }

    set category(category) {
        this._category = category;
    }
    
    get category() {
        return this._category;
    }

    set display(display) {
        this._display = display;
    }

    get display() {
        return this._display;
    }

    set displayWidth(width) {
        this._display.width = width;
        this.UIElement.setUIWidth(this._display.width + UNIT.PX);
    }

    get displayWidth() {
        return this._display.width;
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
        this.UIElement.setUIPaddingTop(top + UNIT.PX);
    }

    get displayPaddingTop() {
        const padding = this._display.padding.split(' ');
        return padding[0];
    }

    set displayPaddingRight(right) {
        const padding = this._display.padding.split(' ');
        padding[1] = right;
        this._display.padding = padding.join(' ');
        this.UIElement.setUIPaddingRight(right + UNIT.PX);
    }

    get displayPaddingRight() {
        const padding = this._display.padding.split(' ');
        return padding[1];
    }

    set displayPaddingBottom(bottom) {
        const padding = this._display.padding.split(' ');
        padding[2] = bottom;
        this._display.padding = padding.join(' ');
        this.UIElement.setUIPaddingBottom(bottom + UNIT.PX);
    }

    get displayPaddingBottom() {
        const padding = this._display.padding.split(' ');
        return padding[2];
    }

    set displayPaddingLeft(left) {
        const padding = this._display.padding.split(' ');
        padding[3] = left;
        this._display.padding = padding.join(' ');
        this.UIElement.setUIPaddingLeft(padding + UNIT.PX);
    }

    get displayPaddingLeft() {
        const padding = this._display.padding.split(' ');
        return padding[3];
    }
    // 세부 속성
    getProperty() {
        // display 속성 - width
        const displayWidthProperty = new InputBoxProperty('display.width', this.displayWidth)
            .setValidation(true, 'number', '0', '8192', '', '');
        displayWidthProperty.unit = UNIT.PX;

        // display 속성 - margin
        const displayMarginProperty = new BoxModelProperty('display.margin', this.displayMargin)
            .setValidation(false, 'number', '0', '100', '', '');
        displayMarginProperty.unit = UNIT.PX;

        // display 속성 - padding
        const displayPaddingProperty = new BoxModelProperty('display.padding', this.displayPadding)
            .setValidation(false, 'number', '0', '100', '', '');
        displayPaddingProperty.unit = UNIT.PX;

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
            new GroupProperty('group._display')
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
        this.parent = source.parent;
        this._type = source.type;
        this._name = source.name;
        this._desc = source.desc;
        this._status = source.status;
        this._category = source.category;
        this._display = source._display;
        if (flag) { this._id = source.id; }

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
            id: this._id,
            name: this._name,
            desc: this._desc,
            status: this._status,
            category: this._category,
            display: this._display,
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