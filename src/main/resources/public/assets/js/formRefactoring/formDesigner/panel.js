/**
 * 세부 속성 패널 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import {
    UIClipboard, UITextArea, UIInput, UIDiv, UILabel, UISpan, UISelect,
    UISwitch, UIColor, UIButton, UISlider
} from '../lib/ui.js';
import { FORM } from '../lib/constants.js';

export default class Panel {
    constructor(editor) {
        this.domElement = document.getElementById('propertyPanel'); // 속성 패널
        this.editor = editor;
    }
    // 세부 속성 출력
    on() {
        if (!this.editor.selectedObject) { return false; }
        // 세부 속성 이름 출력
        this.setPropertyName(this.editor.selectedObject.type);

        // 세부 속성 표시
        const property = this.editor.selectedObject.getProperty();

        Object.entries(property).map(([key, value]) => {
            const propertyObject = this.makePropertyByType(key, value);
            // 라벨, 엘리먼트, 유효성 등 그룹에 포함될 경우
            if (value.type === 'group') {
                Object.entries(value.children).map(([childKey, childValue]) => {
                    const childKeyId = key + childKey.substr(0, 1).toUpperCase() +
                        childKey.substr(1, childKey.length);
                    const childPropertyObject = this.makePropertyByType(childKeyId, childValue);
                    if (childPropertyObject !== null) {
                        propertyObject.add(childPropertyObject);
                    }
                });
            }
            if (propertyObject !== null) {
                this.domElement.appendChild(propertyObject.domElement);
            }
        });

        return this;
    }
    // 세부 속성 삭제
    off() {
        this.domElement.innerHTML = '';
        this.setPropertyName('');
        return this;
    }
    makeUILabel(data) {
        const label = new UILabel().setClass('property-label').setTextAlign('left');
        label.UILabelText = new UISpan().setClass('property-label-text')
            .setTextContent(i18n.msg(data.name));
        label.add(label.UILabelText);
        // 필수 여부
        if (data.validate.required) {
            label.UIRequiredText = new UISpan().setClass('required');
            label.add(label.UIRequiredText);
        }
        // 툴팁(도움말) 기능 추가
        if (data.help !== '') {
            label.UITooltip = new UIDiv().setClass('help-tooltip');
            label.UITooltip.add(new UISpan().setClass('icon').addClass('help-tooltip-icon'));
            label.UITooltip.UIContent = new UIDiv().setClass('tooltip-contents');
            label.UITooltip.UIContent.add(new UISpan().setInnerHTML(i18n.msg(data.help)));
            label.UITooltip.add(label.UITooltip.UIContent);
            label.add(label.UITooltip);
        }
        return label;
    }
    // 타입에 따른 세부 속성 객체 생성
    makePropertyByType(key, data) {
        let object = null;
        switch(data.type) {
        case 'group':
            object = new UIDiv().setClass('property-group');
            object.UIGroupLabel = new UILabel().setClass('property-group-label').setTextAlign('left');
            object.UIGroupLabel.UILabelText = new UISpan().setClass('property-group-label-text')
                .setTextContent(i18n.msg(data.name));
            object.UIGroupLabel.add(object.UIGroupLabel.UILabelText);
            object.add(object.UIGroupLabel);
            break;
        case 'clipboard':
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);
            // 클립보드
            object.UIClipboard = new UIClipboard();
            object.UIClipboard.UIInput.setValue(data.value);
            object.add(object.UIClipboard);
            break;
        case 'input':
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);
            // inputbox
            object.UIInput = new UIInput().setId(key).setValue(data.value)
                .setAttribute('data-validate-required', data.validate.required)
                .setAttribute('data-validate-type', data.validate.type)
                .setAttribute('data-validate-min', data.validate.min)
                .setAttribute('data-validate-max', data.validate.max)
                .setAttribute('data-validate-minLength', data.validate.minLength)
                .setAttribute('data-validate-maxLength', data.validate.maxLength)
                .onKeyUp(this.updateProperty.bind(this))
                .onChange(this.updateProperty.bind(this));
            // 단위 추가
            if (data.unit !== '') {
                object.UIInput.addClass('icon-unit-' + data.unit);
            }
            object.add(object.UIInput);
            break;
        case 'input-box': // 박스 모델용
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);

            object.UIBox = new UIDiv().setClass('input-box');
            object.add(object.UIBox);

            // inputbox : top, right, bottom, left 박스 모델
            const boxValueArray = data.value.split(' ');
            ['Top', 'Left', 'Bottom', 'Right'].forEach((item, index) => {
                object.UIBox['UIInput' + item] = new UIInput().setId(key + item)
                    .setValue(boxValueArray[index])
                    .setAttribute('data-validate-required', data.validate.required)
                    .setAttribute('data-validate-type', data.validate.type)
                    .setAttribute('data-validate-min', data.validate.min)
                    .setAttribute('data-validate-max', data.validate.max)
                    .setAttribute('data-validate-minLength', data.validate.minLength)
                    .setAttribute('data-validate-maxLength', data.validate.maxLength)
                    .onKeyUp(this.updateProperty.bind(this))
                    .onChange(this.updateProperty.bind(this));
                // 단위 추가
                if (data.unit !== '') {
                    object.UIBox['UIInput' + item].addClass('icon-unit-' + data.unit);
                }
                object.UIBox.add(object.UIBox['UIInput' + item]);
            });
            break;
        case 'textarea':
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);
            // textarea
            object.UITextArea = new UITextArea().setId(key).addClass('textarea-scroll-wrapper')
                .setValue(data.value)
                .setAttribute('data-validate-required', data.validate.required)
                .setAttribute('data-validate-minLength', data.validate.minLength)
                .setAttribute('data-validate-maxLength', data.validate.maxLength)
                .onChange(this.updateProperty.bind(this));
            object.add(object.UITextArea);
            break;
        case 'select':
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);

            // select box
            const option = data.option.reduce((result, option) => {
                result[option.value] = i18n.msg(option.name);
                return result;
            }, {});
            object.UISelect = new UISelect().setId(key).setOptions(option).setValue(data.value)
                .onChange(this.updateProperty.bind(this));
            object.add(object.UISelect);
            break;
        case 'switch':
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            object.UISwitch = new UISwitch(data.value).setId(key).setTextContent(i18n.msg(data.name));
            object.UISwitch.UISpan.addClass('property-label');
            // 툴팁(도움말) 기능 추가
            if (data.help !== '') {
                object.UISwitch.UITooltip = new UIDiv().setClass('help-tooltip');
                object.UISwitch.UITooltip.add(new UISpan().setClass('icon').addClass('help-tooltip-icon'));
                object.UISwitch.UITooltip.UIContent = new UIDiv().setClass('tooltip-contents');
                object.UISwitch.UITooltip.UIContent.add(new UISpan().setInnerHTML(i18n.msg(data.help)));
                object.UISwitch.UITooltip.add( object.UISwitch.UITooltip.UIContent);
                object.UISwitch.add( object.UISwitch.UITooltip);
            }
            object.UISwitch.UICheckbox.onChange(this.updateProperty.bind(this));
            object.add(object.UISwitch);
            break;
        case 'slider':
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);

            // slider
            object.UISlider = new UISlider(data.value).setMin(0).setMax(FORM.COLUMN);
            object.UISlider.UIInput.setId(key).onChange(this.updateProperty.bind(this));
            object.add(object.UISlider);
            break;
        case 'rgb':
        case 'rgba':
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);

            // color picker
            const colorPickerOption = {
                isOpacity: (data.type === 'rgba'), // 불투명도 사용여부
                data: {
                    isSelected: true, // 기존 색상 선택 여부
                    selectedClass: 'selected', // 기존 값 색상에 css 적용 (테두리)
                    value: data.value // 기존 값
                }
            };
            object.UIColorPicker = new UIColor(colorPickerOption).setId(key);
            object.UIColorPicker.UIColor.UIInput.onChange(this.updateProperty.bind(this));
            object.add(object.UIColorPicker);
            break;
        case 'button-switch-icon': // 정렬
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);

            // 버튼 그룹
            object.UIButtonGroup = new UIDiv().setClass('btn-switch-group');
            data.option.forEach((item) => {
                const name = item.value.substr(0, 1).toUpperCase() +
                    item.value.substr(1, item.value.length);
                object.UIButtonGroup['UIButton' + name] = new UIButton().setId(key)
                    .setAttribute('data-value', item.value)
                    .addClass('btn-switch').onClick(this.updateButton.bind(this));
                object.UIButtonGroup['UIButton' + name].add(new UISpan().setClass('icon').addClass(item.name));

                if (data.value === item.value) {
                    object.UIButtonGroup['UIButton' + name].addClass('active');
                }
                object.UIButtonGroup.add(object.UIButtonGroup['UIButton' + name]);
            });
            object.add(object.UIButtonGroup);
            break;
        case 'button-toggle-icon': // bold, italic, underline
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);

            // 버튼 그룹
            object.UIButtonGroup = new UIDiv().setClass('btn-toggle-group');
            const toggleValueArray = data.value.split('|');
            data.option.forEach((item, index) => {
                const name = item.value.substr(0, 1).toUpperCase() +
                    item.value.substr(1, item.value.length);

                object.UIButtonGroup['UIButton' + name] = new UIButton().setId(key + name)
                    .setAttribute('data-value', (toggleValueArray[index] === 'Y'))
                    .addClass('btn-toggle').onClick(this.updateButton.bind(this));
                object.UIButtonGroup['UIButton' + name].add(new UISpan().setClass('icon').addClass(item.name));

                if (toggleValueArray[index] === 'Y') {
                    object.UIButtonGroup['UIButton' + name].addClass('active');
                }
                object.UIButtonGroup.add(object.UIButtonGroup['UIButton' + name]);
            });
            object.add(object.UIButtonGroup);
            break;
        case 'default-type': // inputBox - 기본값 타입
            object = new UIDiv().setClass('property')
                .setProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.add(object.UILabel);
            // 그룹
            object.UIGroup = new UIDiv().setClass('default-type');
            const defaultTypeValueArray = data.value.split('|');
            // switch button
            object.UIGroup.UIButtonGroup = new UIDiv().setClass('btn-switch-group');
            data.option.forEach((item) => {
                const name = item.value.substr(0, 1).toUpperCase() +
                    item.value.substr(1, item.value.length);
                object.UIGroup.UIButtonGroup['UIButton' + name] = new UIButton().setId(key)
                    .setAttribute('data-type', item.value)
                    .addClass('btn-switch').onClick(this.updateDefaultType.bind(this));
                object.UIGroup.UIButtonGroup['UIButton' + name].add(new UISpan().setClass('text')
                    .setTextContent(i18n.msg(item.name)));

                if (defaultTypeValueArray[0] === item.value) {
                    object.UIGroup.UIButtonGroup['UIButton' + name].addClass('active');
                }
                object.UIGroup.UIButtonGroup.add(object.UIGroup.UIButtonGroup['UIButton' + name]);
            });
            object.UIGroup.add(object.UIGroup.UIButtonGroup);

            // input
            object.UIGroup.UIInput = new UIInput().setId(key)
                .addClass((defaultTypeValueArray[0] === 'input') ? 'on' : 'off')
                .setValue((defaultTypeValueArray[0] === 'input') ? defaultTypeValueArray[1] : '')
                .setAttribute('data-validate-minLength', data.validate.minLength)
                .setAttribute('data-validate-maxLength', data.validate.maxLength)
                .onKeyUp(this.updateDefaultType.bind(this))
                .onChange(this.updateDefaultType.bind(this));
            object.UIGroup.add(object.UIGroup.UIInput);

            // select
            const selectOptionValue = (defaultTypeValueArray[0] === 'select') ? defaultTypeValueArray[1] : data.selectOption[0].value;
            const selectOption = data.selectOption.reduce((result, option) => {
                result[option.value] = i18n.msg(option.name);
                return result;
            }, {});
            object.UIGroup.UISelect = new UISelect().setId(key)
                .addClass((defaultTypeValueArray[0] === 'select') ? 'on' : 'off')
                .setOptions(selectOption).setValue(selectOptionValue)
                .onChange(this.updateDefaultType.bind(this));
            object.UIGroup.add(object.UIGroup.UISelect);
            
            object.add(object.UIGroup);
            break;
        default:
            break;
        }
        return object;
    }
    // 세부 속성 이름 출력
    setPropertyName(name) {
        // TODO: Breadcrumb 기능 추가
        document.getElementById('propertyName').textContent = name;
    }
    // default Type 업데이트
    updateDefaultType(e) {
        let passValidate = true;
        let defaultTypeGroup = e.target.parentNode;
        let changeValue = '';
        if (e.type === 'keyup') { // input
            changeValue = 'input|' + e.target.value;
        } else if (e.type === 'change') { // selectbox, input
            // change 일 경우 minLength, maxLength 체크
            passValidate = this.changeValidateCheck(e.target);
            changeValue = ((e.target.type === 'input') ? 'input|' : 'select|') + e.target.value;
        } else { // click - button
            if (e.target.classList.contains('active')) { return false; }

            const buttonGroup =  e.target.parentNode;
            for (let i = 0, len = buttonGroup.childNodes.length ; i< len; i++) {
                let child = buttonGroup.childNodes[i];
                if (child.classList.contains('active')) {
                    child.classList.remove('active');
                }
            }
            e.target.classList.add('active');

            defaultTypeGroup = defaultTypeGroup.parentNode;
            const input = defaultTypeGroup.querySelector('.input');
            const select = defaultTypeGroup.querySelector('.select');
            if (e.target.getAttribute('data-type') === 'input') { // input 활성화
                input.classList.remove('off');
                select.classList.add('off');
                changeValue = 'input|';
            } else { // select 활성화
                select.classList.remove('off');
                input.classList.add('off');
                select.selectedIndex = 0;
                changeValue = 'select|' + select.options[0].value;
            }
        }
        if (passValidate) {
            const method = 'set' + e.target.id.substr(0, 1).toUpperCase() +
            e.target.id.substr(1, e.target.id.length);
            this.editor.selectedObject[method].call(this.editor.selectedObject, changeValue);
            // TODO: 이력 저장
            // this.editor.history.save();
        } else {
            // TODO: 에러 메시지 표시 및 포커스
        }
    }
    // 버튼 업데이트
    updateButton(e) {
        if (e.target.classList.contains('btn-switch')) { // 정렬
            if (e.target.classList.contains('active')) { return false; }

            const buttonGroup =  e.target.parentNode;
            for (let i = 0, len = buttonGroup.childNodes.length ; i< len; i++) {
                let child = buttonGroup.childNodes[i];
                if (child.classList.contains('active')) {
                    child.classList.remove('active');
                }
            }
            e.target.classList.add('active');
        } else { // bold, italic 등 toggle button
            if (e.target.classList.contains('active')) {
                e.target.classList.remove('active');
                e.target.setAttribute('data-value', false);
            } else {
                e.target.classList.add('active');
                e.target.setAttribute('data-value', true);
            }
        }
        this.updateProperty.call(this, e);
    }
    // 세부 속성 업데이트
    updateProperty(e) {
        //TODO: 유효성 검증
        let passValidate = true;
        if (e.type === 'keyup') {// keyup 일 경우 type, min, max 체크
            passValidate = this.keyUpValidateCheck(e.target);
        } else if (e.type === 'change') {// change 일 경우 필수값, minLength, maxLength 체크
            passValidate = this.changeValidateCheck(e.target);
        }

        if (passValidate) {
            let changeValue = e.target.value;
            if (e.target.type === 'checkbox') {
                changeValue = e.target.checked;
            } else if (e.target.type === 'button') {
                changeValue = e.target.getAttribute('data-value');
            }
            const method = 'set' + e.target.id.substr(0, 1).toUpperCase() +
                e.target.id.substr(1, e.target.id.length);
            this.editor.selectedObject[method].call(this.editor.selectedObject, changeValue);
            // TODO: 이력 저장
            // this.editor.history.save();
        } else {
            // TODO: 에러 메시지 표시 및 포커스
        }
    }
    // keyup 유효성 검증
    keyUpValidateCheck(element) {
        // type(number, char, email 등), min, max 체크
        return true;
    }
    // change 유효성 검증
    changeValidateCheck(element) {
        // 필수값, minLength, maxLength 체크
        return true;
    }
}