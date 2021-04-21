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
import * as util from '../lib/util.js';
import * as mixin from '../lib/mixins.js';
import { CLASS_PREFIX, FORM, UNIT } from '../lib/constants.js';
import { UICheckbox, UIDiv, UILabel, UISpan } from '../lib/ui.js';

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

export default class Group {
    constructor(data = {}) {
        this.type = 'group';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.isAccordionUsed = data.isAccordionUsed;
        this.margin = data.margin || '10 0 10 0'; // 그룹 간 간격(위 오른쪽 아래 왼쪽)
        this.label = util.mergeObject(DEFAULT_GROUP_LABEL_PROPERTY, data.label);

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        // 그룹용 툴팁
        const groupTooltip = new UIGroupTooltip()
            .setUICSSText(`margin:${this.margin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);
        // 그룹
        groupTooltip.UIGroup = new UIGroup(this.isAccordionUsed).setUIId(this.id);
        // 아코디언용 체크박스
        groupTooltip.UIGroup.UICheckbox.setUIId('chk-' + this.id)
            .setUIClass(CLASS_PREFIX + 'group-accordion-checkBox');
        // 라벨
        groupTooltip.UIGroup.UILabel.setUIFor('chk-' + this.id)
            .addUIClass((this.label.visibility ? 'on' : 'off')) // 라벨 사용여부: 라벨 숨김 또는 보임
            .setUICSSText(`text-align: ${this.label.align};`);
        // 라벨 텍스트
        const groupLabelCssText = `color:${this.label.fontColor};` +
            `font-size:${this.label.fontSize + UNIT.PX};` +
            `${this.label.bold ? 'font-weight:bold;' : ''}` +
            `${this.label.italic ? 'font-style:italic;' : ''}` +
            `${this.label.underline ? 'text-decoration:underline;' : ''}`;
        groupTooltip.UIGroup.UILabel.UILabelText.setUICSSText(groupLabelCssText)
            .setUITextContent(this.label.text);

        groupTooltip.addUI(groupTooltip.UIGroup);
        this.UIElement = groupTooltip;
    }

    setIsAccordionUsed(boolean) {
        this.isAccordionUsed = boolean;
        if (boolean) {
            this.UIElement.UIGroup.addUIClass('accordion');
        } else {
            this.UIElement.UIGroup.removeUIClass('accordion');
        }
    }

    getIsAccordionUsed() {
        return this.isAccordionUsed;
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

    setLabelVisibility(boolean) {
        this.label.visibility = boolean;
        if (boolean) {
            this.UIElement.UIGroup.UILabel.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIGroup.UILabel.removeUIClass('on').addUIClass('off');
        }
    }

    getLabelVisibility() {
        return this.label.visibility;
    }

    setLabelFontColor(color) {
        this.label.fontColor = color;
        this.UIElement.UIGroup.UILabel.UILabelText.setUIColor(color);
    }

    getLabelFontColor() {
        return this.label.fontColor;
    }

    setLabelFontSize(size) {
        this.label.fontSize = size;
        this.UIElement.UIGroup.UILabel.UILabelText.setUIFontSize(size);
    }

    getLabelFontSize() {
        return this.label.fontSize;
    }

    setLabelAlign(value) {
        this.label.align = value;
        this.UIElement.UIGroup.UILabel.setUITextAlign(value);
    }

    getLabelAlign() {
        return this.label.align;
    }

    setLabelFontOptionBold(boolean) {
        this.label.bold = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setUIFontWeight((boolean === 'true' ? 'bold' : ''));
    }

    getLabelFontOptionBold() {
        return this.label.bold;
    }

    setLabelFontOptionItalic(boolean) {
        this.label.italic = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setUIFontStyle((boolean === 'true' ? 'italic' : ''));
    }

    getLabelFontOptionItalic() {
        return this.label.italic;
    }

    setLabelFontOptionUnderline(boolean) {
        this.label.underline = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setUITextDecoration((boolean === 'true' ? 'underline' : ''));
    }

    getLabelFontOptionUnderline() {
        return this.label.underline;
    }

    setLabelText(text) {
        this.label.text = text;
        this.UIElement.UIGroup.UILabel.UILabelText.setUITextContent(text);
    }

    getLabelText() {
        return this.label.text;
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
            'isAccordionUsed': {
                'name': 'form.properties.isAccordionUsed',
                'type': 'switch',
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
            'label': {
                'name': 'form.properties.label',
                'type': 'group',
                'children': {
                    'visibility': {
                        'name': 'form.properties.visibility',
                        'type': 'switch',
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
                    'fontColor': {
                        'name': 'form.properties.fontColor',
                        'type': 'rgb',
                        'unit': '',
                        'help': '',
                        'columnWidth': '8',
                        'validate': {
                            'required': false,
                            'type': 'rgb',
                            'max': '',
                            'min': '',
                            'maxLength': '25',
                            'minLength': ''
                        }
                    },
                    'fontSize': {
                        'name': 'form.properties.fontSize',
                        'type': 'input',
                        'unit': 'px',
                        'help': '',
                        'columnWidth': '3',
                        'validate': {
                            'required': false,
                            'type': 'number',
                            'max': '100',
                            'min': '10',
                            'maxLength': '',
                            'minLength': ''
                        }
                    },
                    'align' : {
                        'name': 'form.properties.align',
                        'type': 'button-switch-icon',
                        'unit': '',
                        'help': '',
                        'columnWidth': '5',
                        'validate': {
                            'required': false,
                            'type': '',
                            'max': '',
                            'min': '',
                            'maxLength': '',
                            'minLength': ''
                        },
                        'option': [
                            { 'name': 'icon-align-left', 'value': 'left' },
                            { 'name': 'icon-align-center', 'value': 'center' },
                            { 'name': 'icon-align-right', 'value': 'right' }
                        ]
                    },
                    'fontOption' : {
                        'name': 'form.properties.option',
                        'type': 'button-toggle-icon',
                        'unit': '',
                        'help': '',
                        'columnWidth': '5',
                        'validate': {
                            'required': false,
                            'type': '',
                            'max': '',
                            'min': '',
                            'maxLength': '',
                            'minLength': ''
                        },
                        'option': [
                            { 'name': 'icon-bold', 'value': 'bold'},
                            { 'name': 'icon-italic', 'value': 'italic' },
                            { 'name': 'icon-underline', 'value': 'underline' }
                        ]
                    },
                    'text': {
                        'name': 'form.properties.text',
                        'type': 'input',
                        'unit': '',
                        'help': '',
                        'columnWidth': '12',
                        'validate': {
                            'required': false,
                            'type': '',
                            'max': '',
                            'min': '',
                            'maxLength': '128',
                            'minLength': ''
                        }
                    },
                }
            }
        };
        return Object.entries(PANEL_PROPERTIES).reduce((property, [key, value]) => {
            if (value.type === 'group') {
                const childProperties = Object.entries(value.children).reduce((child, [childKey, childValue]) => {
                    const tempChildValue = { 'value': this[key][childKey] };
                    if (childValue.type === 'button-toggle-icon') { // 토글 데이터
                        tempChildValue.value = childValue.option.map((item) =>
                            (this[key][item.value]) ? 'Y' : 'N').join('|');
                    }
                    child[childKey] = Object.assign(childValue, tempChildValue);
                    return child;
                }, {});
                property[key] = Object.assign(value, { 'children': childProperties });
            } else {
                property[key] = Object.assign(value, { 'value': this[key] });
            }
            return property;
        }, {});
    }
    // 복사 (자식 포함)
    copy(source) {
        this.type = source.type;
        this.id =  source.id;
        this.displayOrder = source.displayOrder;
        this.isAccordionUsed = source.isAccordionUsed;
        this.margin = source.margin;
        this.label = source.label;
        this.parent = source.parent;
        this.UIElement = source.UIElement;

        for (let i = 0; i < source.children.length; i ++) {
            const child = source.children[i];
            this.add(child.clone(), i);
        }
        return this;
    }

    toJSon() {
        const rows = [];
        for (let i = 0; i < this.children.length; i ++) {
            const child = this.children[i];
            rows.push(child.toJSon());
        }
        return {
            id: this.id,
            displayOrder: this.displayOrder,
            isAccordionUsed: this.isAccordionUsed,
            margin: this.margin,
            label: this.label,
            rows: rows
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