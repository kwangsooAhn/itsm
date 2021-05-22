// TODO: #10641 폼 리팩토링 - 공통속성 정리 일감 처리 후 삭제 예정
export const COMMON_PROPERTIES = {
    'id': {
        'name': 'form.properties.dataAttribute',
        'type': 'group',
        'children': {
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
            'tags': {
                'name': 'form.properties.tags',
                'type': 'tag',
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
            }
        }
    },
    'label': {
        'name': 'form.properties.label',
        'type': 'group',
        'children': {
            'text': {
                'name': 'form.properties.text',
                'type': 'input',
                'unit': '',
                'help': '',
                'columnWidth': '8.5',
                'validate': {
                    'required': false,
                    'type': '',
                    'max': '',
                    'min': '',
                    'maxLength': '128',
                    'minLength': ''
                }
            },
            'fontSize': {
                'name': 'form.properties.fontSize',
                'type': 'input',
                'unit': 'px',
                'help': '',
                'columnWidth': '3 ',
                'validate': {
                    'required': false,
                    'type': 'number',
                    'max': '100',
                    'min': '10',
                    'maxLength': '',
                    'minLength': ''
                }
            },
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
            'fontColor': {
                'name': 'form.properties.fontColor',
                'type': 'rgb',
                'unit': '',
                'help': '',
                'columnWidth': '12',
                'validate': {
                    'required': false,
                    'type': 'rgb',
                    'max': '',
                    'min': '',
                    'maxLength': '25',
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
        }
    },
    'display': {
        name: 'form.properties.display',
        type: 'group',
        children: {
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
            }
        }
    }
}