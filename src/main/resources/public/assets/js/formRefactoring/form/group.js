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
import { CLASS_PREFIX, FORM } from '../lib/constants.js';
import { UICheckbox, UIDiv, UILabel, UISpan } from '../lib/ui.js';

export default class Group {
    constructor(data = {}) {
        this.type = 'group';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.isAccordionUsed = data.isAccordionUsed;
        this.margin = data.margin || '10 0 10 0'; // 그룹 간 간격(위 오른쪽 아래 왼쪽)

        const GROUP_LABEL = {
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
        this.label = util.mergeObject(GROUP_LABEL, data.label);

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        // 그룹용 툴팁
        const groupTooltip = new UIGroupTooltip()
            .setMargin(this.margin.split(' ').join('px ') + 'px');
        // 그룹
        groupTooltip.UIGroup = new UIGroup(this.isAccordionUsed).setId(this.id);
        // 아코디언용 체크박스
        groupTooltip.UIGroup.UICheckbox.setId('chk-' + this.id)
            .setClass(CLASS_PREFIX + 'group-accordion-checkBox');
        // 라벨
        groupTooltip.UIGroup.UILabel.setFor('chk-' + this.id)
            .addClass((this.label.visibility ? 'on' : 'off'))
            .setTextAlign(this.label.align); // 라벨 사용여부: 라벨 숨김 또는 보임
        // 라벨 텍스트
        groupTooltip.UIGroup.UILabel.UILabelText.setFontSize(this.label.fontSize + 'px')
            .setFontWeight((this.label.bold ? 'bold' : ''))
            .setFontStyle((this.label.italic ? 'italic' : ''))
            .setTextDecoration((this.label.underline ? 'underline' : ''))
            .setColor(this.label.fontColor)
            .setTextContent(this.label.text);

        groupTooltip.add(groupTooltip.UIGroup);
        this.UIElement = groupTooltip;
    }

    setIsAccordionUsed(boolean) {
        this.isAccordionUsed = boolean;
        if (boolean) {
            this.UIElement.UIGroup.addClass('accordion');
        } else {
            this.UIElement.UIGroup.removeClass('accordion');
        }
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

    setLabelVisibility(boolean) {
        this.label.visibility = boolean;
        if (boolean) {
            this.UIElement.UIGroup.UILabel.removeClass('off').addClass('on');
        } else {
            this.UIElement.UIGroup.UILabel.removeClass('on').addClass('off');
        }
    }

    setLabelFontColor(color) {
        this.label.fontColor = color;
        this.UIElement.UIGroup.UILabel.UILabelText.setColor(color);
    }

    setLabelFontSize(size) {
        this.label.fontSize = size;
        this.UIElement.UIGroup.UILabel.UILabelText.setFontSize(size);
    }

    setLabelAlign(value) {
        this.label.align = value;
        this.UIElement.UIGroup.UILabel.setTextAlign(value);
    }

    setLabelFontOptionBold(boolean) {
        this.label.bold = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setFontWeight((boolean === 'true' ? 'bold' : ''));
    }

    setLabelFontOptionItalic(boolean) {
        this.label.italic = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setFontStyle((boolean === 'true' ? 'italic' : ''));
    }

    setLabelFontOptionUnderline(boolean) {
        this.label.underline = boolean;
        this.UIElement.UIGroup.UILabel.UILabelText
            .setTextDecoration((boolean === 'true' ? 'underline' : ''));
    }

    setLabelText(text) {
        this.label.text = text;
        this.UIElement.UIGroup.UILabel.UILabelText.setTextContent(text);
    }

    // 세부 속성
    getProperty() {
        // 기존 데이터 속성과 패널에 표시되는 기본 속성을 merge 한 후, 조회한다.
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
        return Object.entries(PROPERTIES).reduce((property, [key, value]) => {
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
            this.addClass('accordion');
        }

        this.UICheckbox = new UICheckbox(true);
        this.add(this.UICheckbox);

        this.UILabel = new UILabel().setClass(CLASS_PREFIX + 'group-label');
        this.UILabel.UILabelText = new UISpan().setClass(CLASS_PREFIX + 'group-label-text');
        this.UILabel.UIIcon = new UISpan().setClass(CLASS_PREFIX + 'group-label-icon arrow-left');
        this.UILabel.add(this.UILabel.UILabelText).add(this.UILabel.UIIcon);
        this.add(this.UILabel);
    }
}