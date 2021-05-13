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

export default class Group {
    constructor(data = {}) {
        this.type = 'group';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.label = Object.assign({}, DEFAULT_GROUP_LABEL_PROPERTY, data.label);
        this.display = data.display || {
            displayOrder: 0,     // 표시 순서
            isAccordionUsed: true,
            margin: '10 0 10 0' // 그룹 간 간격(위 오른쪽 아래 왼쪽)
        }
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
            .setUICSSText(`margin:${this.display.margin.split(' ').join(UNIT.PX + ' ') + UNIT.PX};`);
        // 그룹
        groupTooltip.UIGroup = new UIGroup(this.display.isAccordionUsed).setUIId(this.id);
        // 아코디언용 체크박스
        const accordionId = workflowUtil.generateUUID(); // 미리보기에서도 동작하도록 key를 동적으로 만듬
        groupTooltip.UIGroup.UICheckbox.setUIId('chk-' + accordionId)
            .setUIClass(CLASS_PREFIX + 'group-accordion-checkBox');
        // 라벨
        groupTooltip.UIGroup.UILabel.setUIFor('chk-' + accordionId)
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
        // 툴팁
        groupTooltip.UITooltipMenu = this.makeTooltip();
        groupTooltip.addUI(groupTooltip.UITooltipMenu);

        this.UIElement = groupTooltip;
    }

    setDisplayIsAccordionUsed(boolean) {
        this.display.isAccordionUsed = boolean;
        if (boolean) {
            this.UIElement.UIGroup.addUIClass('accordion');
        } else {
            this.UIElement.UIGroup.removeUIClass('accordion');
        }
    }

    getDisplayIsAccordionUsed() {
        return this.display.isAccordionUsed;
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
            'display': {
                name: 'form.properties.display',
                type: 'group',
                children: {
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
                    }
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

    /**
     * 현재 객체를 대상이 되는 객체로 변경 (복사) 하여 반환한다
     * @param source 대상 객체
     * @param flag 객체의 키가 되는 id도 복제할지 여부 (true이면 id도 복제됨)
     */
    copy(source, flag) {
        this.type = source.type;
        this.display.displayOrder = source.display.displayOrder;
        this.display.isAccordionUsed = source.display.isAccordionUsed;
        this.display.margin = source.display.margin;
        this.label = source.label;
        this.parent = source.parent;
        if (flag) { this.id = source.id; }

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
            id: this.id,
            display: this.display,
            label: this.label,
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