/**
 * Inputbox Class
 *
 * 사용자 입력을 받는 input box.
 * 정규식을 이용하여 숫자, 문자, 전화번호, 이메일 등 제어가 가능하다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import { SESSION, FORM, CLASS_PREFIX } from '../../lib/constants.js';
import * as util from '../../lib/util.js';
import { UIDiv, UIInput } from '../../lib/ui.js';

const DEFAULT_ELEMENT_PROPERTY = {
    placeholder: '',
    columnWidth: '10',
    defaultType: 'input|', // input|사용자입력 / select|세션값
};
const DEFAULT_VALIDATE_PROPERTY = {
    validateType: 'none', // none | char | num | numchar | email | phone
    lengthMin: '0',
    lengthMax: '100'
};

export const inputBoxMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this.element = util.mergeObject(DEFAULT_ELEMENT_PROPERTY, this.element);
        this.validate = util.mergeObject(DEFAULT_VALIDATE_PROPERTY, this.validate);
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.element.columnWidth);
        // TODO: 유효성 검증 추가
        element.UIInputbox = new UIInput().setUIPlaceholder(this.element.placeholder)
            .setUIRequired((this.displayType === FORM.DISPLAY_TYPE.REQUIRED))
            .setUIValue(this.getValue())
            .setUIAttribute('data-validate-type', this.validate.validateType)
            .setUIAttribute('data-validate-maxLength', this.validate.lengthMax)
            .setUIAttribute('data-validate-minLength', this.validate.lengthMin);
        element.addUI(element.UIInputbox);
        return element;
    },
    setElementPlaceholder(placeholder) {
        this.element.placeholder = placeholder;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIPlaceholder(placeholder);
    },
    getElementPlaceholder() {
        return this.element.placeholder;
    },
    setElementColumnWidth(width) {
        this.element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
    },
    getElementColumnWidth() {
        return this.element.columnWidth;
    },
    setElementDefaultType(value) {
        this.element.defaultType = value;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIValue(this.getValue());
    },
    getElementDefaultType() {
        return this.element.defaultType;
    },
    setValidateValidateType(type) {
        this.validate.validateType = type;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validate-type', type);
    },
    getValidateValidateType() {
        return this.validate.validateType;
    },
    setValidateLengthMin(min) {
        this.validate.lengthMin = min;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validate-minLength', min);
    },
    getValidateLengthMin() {
        return this.validate.lengthMin;
    },
    setValidateLengthMax(max) {
        this.validate.lengthMax = max;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validate-maxLength', max);
    },
    getValidateLengthMax() {
        return this.validate.lengthMax;
    },
    // 기본 값 조회
    getValue() {
        if (this.value === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = this.element.defaultType.split('|');
            if (defaultValues[0] === 'input') {
                return defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                return SESSION[defaultValues[1]] || '';
            }
        } else {
            return this.value;
        }
    },
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
            'mapId': {
                'name': 'form.properties.mapId',
                'type': 'input',
                'unit': '',
                'help': 'form.help.mapping-id',
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
            'isTopic': {
                'name': 'form.properties.isTopic',
                'type': 'switch',
                'unit': '',
                'help': 'form.help.is-topic',
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
            'tags': { // TODO: 태그 기능은 추구 구현 예정
                'name': 'form.properties.tag',
                'type': 'table',
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
            'columnWidth': {
                'name': 'form.properties.columnWidth',
                'type': 'slider',
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
            'label': {
                'name': 'form.properties.label',
                'type': 'group',
                'children': {
                    'position': {
                        'name': 'form.properties.visibility',
                        'type': 'button-switch-icon',
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
                            { 'name': 'icon-position-left', 'value': 'left' },
                            { 'name': 'icon-position-top', 'value': 'top' },
                            { 'name': 'icon-position-hidden', 'value': 'hidden' }
                        ]
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
                    }
                }
            },
            'element': {
                'name': 'form.properties.element',
                'type': 'group',
                'children': {
                    'placeholder': {
                        'name': 'form.properties.placeholder',
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
                    'columnWidth': {
                        'name': 'form.properties.columnWidth',
                        'type': 'slider',
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
                    'defaultType': {
                        'name': 'form.properties.defaultType',
                        'type': 'default-type',
                        'unit': '',
                        'help': 'form.help.date-default',
                        'columnWidth': '12',
                        'validate': {
                            'required': false,
                            'type': '',
                            'max': '',
                            'min': '',
                            'maxLength': '',
                            'minLength': '128'
                        },
                        'option': [
                            { 'name': 'form.properties.direct', 'value': 'input'},
                            { 'name': 'form.properties.auto', 'value': 'select'}
                        ],
                        'selectOption': [
                            { 'name': 'form.properties.userKey', 'value': 'userKey' },
                            { 'name': 'form.properties.userId', 'value': 'userId' },
                            { 'name': 'form.properties.userName', 'value': 'userName' },
                            { 'name': 'form.properties.email', 'value': 'email' },
                            { 'name': 'form.properties.jobPosition', 'value': 'position'},
                            { 'name': 'form.properties.department', 'value': 'department' },
                            { 'name': 'form.properties.officeNumber', 'value': 'officeNumber' },
                            { 'name': 'form.properties.officeNumber', 'value': 'officeNumber' }
                        ]
                    }
                }
            },
            'validate': {
                'name': 'form.properties.validate',
                'type': 'group',
                'children': {
                    'validateType': {
                        'name': 'form.properties.validateType',
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
                            { 'name': 'form.properties.none', 'value': 'none' },
                            { 'name': 'form.properties.char', 'value': 'char' },
                            { 'name': 'form.properties.number', 'value': 'number' },
                            { 'name': 'form.properties.email', 'value': 'email'},
                            { 'name': 'form.properties.phone', 'value': 'phone'}
                        ]
                    },
                    'lengthMin': {
                        'name': 'form.properties.lengthMin',
                        'type': 'input',
                        'unit': '',
                        'help': '',
                        'columnWidth': '12',
                        'validate': {
                            'required': false,
                            'type': 'number',
                            'max': '128',
                            'min': '',
                            'maxLength': '',
                            'minLength': ''
                        }
                    },
                    'lengthMax': {
                        'name': 'form.properties.lengthMax',
                        'type': 'input',
                        'unit': '',
                        'help': '',
                        'columnWidth': '12',
                        'validate': {
                            'required': false,
                            'type': 'number',
                            'max': '128',
                            'min': '',
                            'maxLength': '',
                            'minLength': ''
                        }
                    }
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
};