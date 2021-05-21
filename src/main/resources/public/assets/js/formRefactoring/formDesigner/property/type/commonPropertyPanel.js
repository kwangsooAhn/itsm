// TODO: #10641 폼 리팩토링 - 공통속성 정리 일감 처리 후 삭제 예정
export const COMMON_PROPERTIES = {
    'id': {
        'name': 'form.properties.id',
        'type': 'clipboardProperty',
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
        'type': 'inputBoxProperty',
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
        'type': 'switchProperty',
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
    'tags': {
        'name': 'form.properties.tags',
        'type': 'tagProperty',
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
            'columnWidth': {
                'name': 'form.properties.columnWidth',
                'type': 'sliderProperty',
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
        'type': 'groupProperty',
        'children': {
            'position': {
                'name': 'form.properties.visibility',
                'type': 'switchButtonProperty',
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
                'type': 'colorPickerProperty',
                'unit': '',
                'help': '',
                'columnWidth': '8',
                'validate': {
                    'required': false,
                    'type': 'colorPickerProperty',
                    'max': '',
                    'min': '',
                    'maxLength': '25',
                    'minLength': ''
                }
            },
            'fontSize': {
                'name': 'form.properties.fontSize',
                'type': 'inputBoxProperty',
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
                'type': 'switchButtonProperty',
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
                'type': 'toggleButtonProperty',
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
                'type': 'inputBoxProperty',
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
};