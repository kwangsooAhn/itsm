export const COMMON_PROPERTIES = {
    'id': {
        'name': 'form.properties.id',
        'type': 'clipboard',
        'unit': '',
        'help': '',
        'columnWidth': '12',
        'validate': {
            'required': false,
            'type': '',            'max': '',
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
                    {'name': 'icon-position-left', 'value': 'left'},
                    {'name': 'icon-position-top', 'value': 'top'},
                    {'name': 'icon-position-hidden', 'value': 'hidden'}
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
            'align': {
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
                    {'name': 'icon-align-left', 'value': 'left'},
                    {'name': 'icon-align-center', 'value': 'center'},
                    {'name': 'icon-align-right', 'value': 'right'}
                ]
            },
            'fontOption': {
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
                    {'name': 'icon-bold', 'value': 'bold'},
                    {'name': 'icon-italic', 'value': 'italic'},
                    {'name': 'icon-underline', 'value': 'underline'}
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
    }
}