/**
 * 컴포넌트 내부를 이루는 구성 Class.
 *
 *  Group은 1개 이상의 Row로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as mixin from '../lib/mixins.js';
import { CLASS_PREFIX, FORM, UNIT } from '../lib/constants.js';
import { UICheckbox, UIDiv, UILabel, UISpan } from '../lib/ui.js';
import ZClipboardProperty from '../formDesigner/property/type/zClipboardProperty.js';
import ZInputBoxProperty from '../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../formDesigner/property/type/zGroupProperty.js';
import ZSwitchProperty from '../formDesigner/property/type/zSwitchProperty.js';
import ZBoxModelProperty from '../formDesigner/property/type/zBoxModelProperty.js';
import ZSwitchButtonProperty from '../formDesigner/property/type/zSwitchButtonProperty.js';
import ZToggleButtonProperty from '../formDesigner/property/type/zToggleButtonProperty.js';
import ZColorPickerProperty from '../formDesigner/property/type/zColorPickerProperty.js';

const DEFAULT_GROUP_LABEL_PROPERTY = {
    // 그룹의 라벨은 아코디언 위에 표시되기 때문에 항상 top 위치이며 보여주거나 숨기는 기능을 설정 한다.
    // 아코디언이 표시되지 않을 경우 라벨 표시 여부에 따라 라벨 영역이 사라지거나 나타난다.
    visibility: true, // 라벨 사용여부
    fontColor: 'rgb(63, 75, 86)',
    fontSize: '16',
    bold: false,
    italic: false,
    underline: false,
    align: 'left',
    text: 'GROUP LABEL'
};
Object.freeze(DEFAULT_GROUP_LABEL_PROPERTY);

export default class ZGroup {
    constructor(data = {}) {
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this._type = 'group';
        this._id =  data.id || workflowUtil.generateUUID();
        this._name = data.name || ''; // 신청서 양식에서 사용되는 이름
        this._displayType = data.displayType || 'document.displayType.editable';
        this._label = Object.assign({}, DEFAULT_GROUP_LABEL_PROPERTY, data.label);
        this._display = data.display || {
            displayOrder: 0,     // 표시 순서
            isAccordionUsed: true,
            margin: '10 0 10 0' // 그룹 간 간격(위 오른쪽 아래 왼쪽)
        };
        // Control Mixin import
        aliceJs.importMixin(this, mixin.controlMixin);
        // Tooltip Mixin import
        aliceJs.importMixin(this, mixin.toolTipMenuMixin);

        this.init();
    }
    // 초기화
    init() {
        // 그룹용 툴팁
        const groupTooltip = new UIGroupTooltip()
            .setUICSSText(`margin:${this.displayMargin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`)
            .setUIAttribute('data-displayType', this.displayType);
        // 그룹
        groupTooltip.UIGroup = new UIGroup(this.displayIsAccordionUsed).setUIId(this.id);
        // 아코디언용 체크박스
        const accordionId = workflowUtil.generateUUID(); // 미리보기에서도 동작하도록 key를 동적으로 만듬
        groupTooltip.UIGroup.UICheckbox.setUIId('chk-' + accordionId)
            .setUIClass(CLASS_PREFIX + 'group-accordion-checkBox');
        // 라벨
        groupTooltip.UIGroup.UILabel.setUIFor('chk-' + accordionId)
            .addUIClass((this.labelVisibility ? 'on' : 'off')) // 라벨 사용여부: 라벨 숨김 또는 보임
            .setUICSSText(`text-align: ${this.labelAlign};`);
        // 라벨 텍스트
        const groupLabelCssText = `color:${this.labelFontColor};` +
            `font-size:${this.labelFontSize + UNIT.PX};` +
            `${this.labelFontOptionBold ? 'font-weight:bold;' : ''}` +
            `${this.labelFontOptionItalic ? 'font-style:italic;' : ''}` +
            `${this.labelFontOptionUnderline ? 'text-decoration:underline;' : ''}`;
        groupTooltip.UIGroup.UILabel.UILabelText.setUICSSText(groupLabelCssText)
            .setUITextContent(this.labelText);

        groupTooltip.addUI(groupTooltip.UIGroup);
        // 툴팁
        groupTooltip.UITooltipMenu = this.makeTooltip();
        groupTooltip.addUI(groupTooltip.UITooltipMenu);

        this.UIElement = groupTooltip;
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

    get name() {
        return this._name;
    }

    set name(name) {
        this._name = name;
    }

    get displayType() {
        return this._displayType;
    }

    set displayType(displayType) {
        this._displayType = displayType;
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

    set displayIsAccordionUsed(boolean) {
        this._display.isAccordionUsed = boolean;
        if (boolean) {
            this.UIElement.UIGroup.addUIClass('accordion');
        } else {
            this.UIElement.UIGroup.removeUIClass('accordion');
        }
    }

    get displayIsAccordionUsed() {
        return this._display.isAccordionUsed;
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

    set label(label) {
        this._label = label;
    }

    get label() {
        return this._label;
    }

    set labelVisibility(boolean) {
        this._label.visibility = boolean;
        if (boolean) {
            this.UIElement.UIGroup.UILabel.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIGroup.UILabel.removeUIClass('on').addUIClass('off');
        }
    }

    get labelVisibility() {
        return this._label.visibility;
    }

    set labelFontColor(color) {
        this._label.fontColor = color;
        this.UIElement.UIGroup.UILabel.UILabelText.setUIColor(color);
    }

    get labelFontColor() {
        return this._label.fontColor;
    }

    set labelFontSize(size) {
        this._label.fontSize = size;
        this.UIElement.UIGroup.UILabel.UILabelText.setUIFontSize(size);
    }

    get labelFontSize() {
        return this._label.fontSize;
    }

    set labelAlign(value) {
        this._label.align = value;
        this.UIElement.UIGroup.UILabel.setUITextAlign(value);
    }

    get labelAlign() {
        return this._label.align;
    }

    set labelFontOptionBold(boolean) {
        this._label.bold = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setUIFontWeight((boolean === 'true' ? 'bold' : ''));
    }

    get labelFontOptionBold() {
        return this._label.bold;
    }

    set labelFontOptionItalic(boolean) {
        this._label.italic = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setUIFontStyle((boolean === 'true' ? 'italic' : ''));
    }

    get labelFontOptionItalic() {
        return this._label.italic;
    }

    set labelFontOptionUnderline(boolean) {
        this._label.underline = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setUITextDecoration((boolean === 'true' ? 'underline' : ''));
    }

    get labelFontOptionUnderline() {
        return this._label.underline;
    }

    set labelText(text) {
        this._label.text = text;
        this.UIElement.UIGroup.UILabel.UILabelText.setUITextContent(text);
    }

    get labelText() {
        return this._label.text;
    }

    // 세부 속성
    getProperty() {
        // display 속성 - margin
        const displayMarginProperty = new ZBoxModelProperty('display.margin', this.displayMargin)
            .setValidation(false, 'number', '0', '100', '', '');
        displayMarginProperty.unit = 'px';

        // labe - text
        const labelTextProperty = new ZInputBoxProperty('label.text', this.labelText);
        labelTextProperty.columnWidth = '8';

        // label - fontSize
        const labelFontSizeProperty = new ZInputBoxProperty('label.fontSize', this.labelFontSize)
            .setValidation(false, 'number', '10', '100', '', '');
        labelFontSizeProperty.unit = 'px';
        labelFontSizeProperty.columnWidth = '3';

        // label - align
        const labelAlignProperty = new ZSwitchButtonProperty('label.align', this.labelAlign, [
            { 'name': 'icon-align-left', 'value': 'left' },
            { 'name': 'icon-align-center', 'value': 'center' },
            { 'name': 'icon-align-right', 'value': 'right' }
        ]);
        labelAlignProperty.columnWidth = '5';

        // label - fontOption
        const labelFontOption = [
            { 'name': 'icon-bold', 'value': 'bold'},
            { 'name': 'icon-italic', 'value': 'italic' },
            { 'name': 'icon-underline', 'value': 'underline' }
        ];
        const labelFontValue = labelFontOption.map((item) => {
            const method = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            return this['labelFontOption' + method] ? 'Y' : 'N';
        }).join('|');
        const labelFontOptionProperty = new ZToggleButtonProperty('label.fontOption', labelFontValue, labelFontOption);
        labelFontOptionProperty.columnWidth = '5';

        // label - fontColor
        const labelFontColorProperty = new ZColorPickerProperty('label.fontColor', this.labelFontColor, false)
            .setValidation(false, 'rgb', '', '', '', '25');
        labelFontColorProperty.columnWidth = '12';

        return [
            new ZClipboardProperty('id', this.id),
            new ZInputBoxProperty('name', this.name),
            new ZGroupProperty('group.display')
                .addProperty(new ZSwitchProperty('display.isAccordionUsed', this.displayIsAccordionUsed))
                .addProperty(displayMarginProperty),
            new ZGroupProperty('group.label')
                .addProperty(new ZSwitchProperty('label.visibility', this.labelVisibility))
                .addProperty(labelTextProperty)
                .addProperty(labelFontSizeProperty)
                .addProperty(labelAlignProperty)
                .addProperty(labelFontOptionProperty)
                .addProperty(labelFontColorProperty)
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
        this._displayType = source.displayType;
        this._display.displayOrder = source.displayDisplayOrder;
        this._display.isAccordionUsed = source.displayIsAccordionUsed;
        this._display.margin = source.displayMargin;
        this._label = source.label;
        if (flag) { this._id = source.id; }

        this.init();

        source.children.forEach((child, index) => {
            this.add(child.clone(flag), index);
        });
        return this;
    }
    // json 데이터 추출
    toJson() {
        const row = [];
        for (let i = 0; i < this.children.length; i ++) {
            const child = this.children[i];
            row.push(child.toJson());
        }
        return {
            id: this._id,
            name: this._name,
            display: this._display,
            label: this._label,
            row: row
        };
    }
}

export class UIGroupTooltip extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + 'group-tooltip';
    }
}

export class UIGroup extends UIDiv {
    constructor(boolean) {
        super();
        this.domElement.className = CLASS_PREFIX + FORM.LAYOUT.GROUP;

        if (boolean) {
            this.addUIClass('accordion');
        }

        this.UICheckbox = new UICheckbox(true);
        this.addUI(this.UICheckbox);

        this.UILabel = new UILabel().setUIClass(CLASS_PREFIX + 'group-label');
        this.UILabel.UILabelText = new UISpan().setUIClass(CLASS_PREFIX + 'group-label-text');
        this.UILabel.UIIcon = new UISpan().setUIClass(CLASS_PREFIX + 'group-label-icon arrow-left');
        this.UILabel.addUI(this.UILabel.UILabelText).addUI(this.UILabel.UIIcon);
        this.addUI(this.UILabel);
    }
}