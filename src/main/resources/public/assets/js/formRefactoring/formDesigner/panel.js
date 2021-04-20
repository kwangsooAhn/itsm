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
                        propertyObject.addUI(childPropertyObject);
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
        const label = new UILabel().setUIClass('property-label').setUITextAlign('left');
        label.UILabelText = new UISpan().setUIClass('property-label-text')
            .setUITextContent(i18n.msg(data.name));
        label.addUI(label.UILabelText);
        // 필수 여부
        if (data.validate.required) {
            label.UIRequiredText = new UISpan().setUIClass('required');
            label.addUI(label.UIRequiredText);
        }
        // 툴팁(도움말) 기능 추가
        if (data.help !== '') {
            label.UITooltip = new UIDiv().setUIClass('help-tooltip');
            label.UITooltip.addUI(new UISpan().setUIClass('icon').addUIClass('help-tooltip-icon'));
            label.UITooltip.UIContent = new UIDiv().setUIClass('tooltip-contents');
            label.UITooltip.UIContent.addUI(new UISpan().setUIInnerHTML(i18n.msg(data.help)));
            label.UITooltip.addUI(label.UITooltip.UIContent);
            label.addUI(label.UITooltip);
        }
        return label;
    }
    // 타입에 따른 세부 속성 객체 생성
    makePropertyByType(key, data) {
        let object = null;
        switch(data.type) {
        case 'group':
            object = new UIDiv().setUIClass('property-group');
            object.UIGroupLabel = new UILabel().setUIClass('property-group-label').setUITextAlign('left');
            object.UIGroupLabel.UILabelText = new UISpan().setUIClass('property-group-label-text')
                .setUITextContent(i18n.msg(data.name));
            object.UIGroupLabel.addUI(object.UIGroupLabel.UILabelText);
            object.addUI(object.UIGroupLabel);
            break;
        case 'clipboard':
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);
            // 클립보드
            object.UIClipboard = new UIClipboard();
            object.UIClipboard.UIInput.setUIValue(data.value);
            object.addUI(object.UIClipboard);
            break;
        case 'input':
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);
            // inputbox
            object.UIInput = new UIInput().setUIId(key).setUIValue(data.value)
                .setUIAttribute('data-validate-required', data.validate.required)
                .setUIAttribute('data-validate-type', data.validate.type)
                .setUIAttribute('data-validate-min', data.validate.min)
                .setUIAttribute('data-validate-max', data.validate.max)
                .setUIAttribute('data-validate-minLength', data.validate.minLength)
                .setUIAttribute('data-validate-maxLength', data.validate.maxLength)
                .onUIKeyUp(this.updateProperty.bind(this))
                .onUIChange(this.updateProperty.bind(this));
            // 단위 추가
            if (data.unit !== '') {
                object.UIInput.addUIClass('icon-unit-' + data.unit);
            }
            object.addUI(object.UIInput);
            break;
        case 'input-box': // 박스 모델용
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);

            object.UIBox = new UIDiv().setUIClass('input-box');
            object.addUI(object.UIBox);

            // inputbox : top, right, bottom, left 박스 모델
            const boxValueArray = data.value.split(' ');
            ['Top', 'Left', 'Bottom', 'Right'].forEach((item, index) => {
                object.UIBox['UIInput' + item] = new UIInput().setUIId(key + item)
                    .setUIValue(boxValueArray[index])
                    .setUIAttribute('data-validate-required', data.validate.required)
                    .setUIAttribute('data-validate-type', data.validate.type)
                    .setUIAttribute('data-validate-min', data.validate.min)
                    .setUIAttribute('data-validate-max', data.validate.max)
                    .setUIAttribute('data-validate-minLength', data.validate.minLength)
                    .setUIAttribute('data-validate-maxLength', data.validate.maxLength)
                    .onUIKeyUp(this.updateProperty.bind(this))
                    .onUIChange(this.updateProperty.bind(this));
                // 단위 추가
                if (data.unit !== '') {
                    object.UIBox['UIInput' + item].addUIClass('icon-unit-' + data.unit);
                }
                object.UIBox.addUI(object.UIBox['UIInput' + item]);
            });
            break;
        case 'textarea':
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);
            // textarea
            object.UITextArea = new UITextArea().setUIId(key).addUIClass('textarea-scroll-wrapper')
                .setUIValue(data.value)
                .setUIAttribute('data-validate-required', data.validate.required)
                .setUIAttribute('data-validate-minLength', data.validate.minLength)
                .setUIAttribute('data-validate-maxLength', data.validate.maxLength)
                .onUIChange(this.updateProperty.bind(this));
            object.addUI(object.UITextArea);
            break;
        case 'select':
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);

            // select box
            const option = data.option.reduce((result, option) => {
                result[option.value] = i18n.msg(option.name);
                return result;
            }, {});
            object.UISelect = new UISelect().setUIId(key).setUIOptions(option).setUIValue(data.value)
                .onUIChange(this.updateProperty.bind(this));
            object.addUI(object.UISelect);
            break;
        case 'switch':
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            object.UISwitch = new UISwitch(data.value).setUIId(key).setUITextContent(i18n.msg(data.name));
            object.UISwitch.UISpan.addUIClass('property-label');
            // 툴팁(도움말) 기능 추가
            if (data.help !== '') {
                object.UISwitch.UITooltip = new UIDiv().setUIClass('help-tooltip');
                object.UISwitch.UITooltip.addUI(new UISpan().setUIClass('icon').addUIClass('help-tooltip-icon'));
                object.UISwitch.UITooltip.UIContent = new UIDiv().setUIClass('tooltip-contents');
                object.UISwitch.UITooltip.UIContent.addUI(new UISpan().setUIInnerHTML(i18n.msg(data.help)));
                object.UISwitch.UITooltip.addUI( object.UISwitch.UITooltip.UIContent);
                object.UISwitch.addUI( object.UISwitch.UITooltip);
            }
            object.UISwitch.UICheckbox.onUIChange(this.updateProperty.bind(this));
            object.addUI(object.UISwitch);
            break;
        case 'slider':
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);

            // slider
            object.UISlider = new UISlider(data.value).setUIMin(0).setUIMax(FORM.COLUMN);
            object.UISlider.UIInput.setUIId(key).onUIChange(this.updateProperty.bind(this));
            object.addUI(object.UISlider);
            break;
        case 'rgb':
        case 'rgba':
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);

            // color picker
            const colorPickerOption = {
                isOpacity: (data.type === 'rgba'), // 불투명도 사용여부
                data: {
                    isSelected: true, // 기존 색상 선택 여부
                    selectedClass: 'selected', // 기존 값 색상에 css 적용 (테두리)
                    value: data.value // 기존 값
                }
            };
            object.UIColorPicker = new UIColor(colorPickerOption).setUIId(key);
            object.UIColorPicker.UIColor.UIInput.onUIChange(this.updateProperty.bind(this));
            object.addUI(object.UIColorPicker);
            break;
        case 'button-switch-icon': // 정렬
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);

            // 버튼 그룹
            object.UIButtonGroup = new UIDiv().setUIClass('btn-switch-group');
            data.option.forEach((item) => {
                const name = item.value.substr(0, 1).toUpperCase() +
                    item.value.substr(1, item.value.length);
                object.UIButtonGroup['UIButton' + name] = new UIButton().setUIId(key)
                    .setUIAttribute('data-value', item.value)
                    .addUIClass('btn-switch').onUIClick(this.updateButton.bind(this));
                object.UIButtonGroup['UIButton' + name].addUI(new UISpan().setUIClass('icon').addUIClass(item.name));

                if (data.value === item.value) {
                    object.UIButtonGroup['UIButton' + name].addUIClass('active');
                }
                object.UIButtonGroup.addUI(object.UIButtonGroup['UIButton' + name]);
            });
            object.addUI(object.UIButtonGroup);
            break;
        case 'button-toggle-icon': // bold, italic, underline
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);

            // 버튼 그룹
            object.UIButtonGroup = new UIDiv().setUIClass('btn-toggle-group');
            const toggleValueArray = data.value.split('|');
            data.option.forEach((item, index) => {
                const name = item.value.substr(0, 1).toUpperCase() +
                    item.value.substr(1, item.value.length);

                object.UIButtonGroup['UIButton' + name] = new UIButton().setUIId(key + name)
                    .setUIAttribute('data-value', (toggleValueArray[index] === 'Y'))
                    .addUIClass('btn-toggle').onUIClick(this.updateButton.bind(this));
                object.UIButtonGroup['UIButton' + name].addUI(new UISpan().setUIClass('icon').addUIClass(item.name));

                if (toggleValueArray[index] === 'Y') {
                    object.UIButtonGroup['UIButton' + name].addUIClass('active');
                }
                object.UIButtonGroup.addUI(object.UIButtonGroup['UIButton' + name]);
            });
            object.addUI(object.UIButtonGroup);
            break;
        case 'default-type': // inputBox - 기본값 타입
            object = new UIDiv().setUIClass('property')
                .setUIProperty('--data-column', data.columnWidth);
            // 라벨
            object.UILabel = this.makeUILabel(data);
            object.addUI(object.UILabel);
            // 그룹
            object.UIGroup = new UIDiv().setUIClass('default-type');
            const defaultTypeValueArray = data.value.split('|');
            // switch button
            object.UIGroup.UIButtonGroup = new UIDiv().setUIClass('btn-switch-group');
            data.option.forEach((item) => {
                const name = item.value.substr(0, 1).toUpperCase() +
                    item.value.substr(1, item.value.length);
                object.UIGroup.UIButtonGroup['UIButton' + name] = new UIButton().setUIId(key)
                    .setUIAttribute('data-type', item.value)
                    .addUIClass('btn-switch').onUIClick(this.updateDefaultType.bind(this));
                object.UIGroup.UIButtonGroup['UIButton' + name].addUI(new UISpan().setUIClass('text')
                    .setUITextContent(i18n.msg(item.name)));

                if (defaultTypeValueArray[0] === item.value) {
                    object.UIGroup.UIButtonGroup['UIButton' + name].addUIClass('active');
                }
                object.UIGroup.UIButtonGroup.addUI(object.UIGroup.UIButtonGroup['UIButton' + name]);
            });
            object.UIGroup.addUI(object.UIGroup.UIButtonGroup);

            // input
            object.UIGroup.UIInput = new UIInput().setUIId(key)
                .addUIClass((defaultTypeValueArray[0] === 'input') ? 'on' : 'off')
                .setUIValue((defaultTypeValueArray[0] === 'input') ? defaultTypeValueArray[1] : '')
                .setUIAttribute('data-validate-minLength', data.validate.minLength)
                .setUIAttribute('data-validate-maxLength', data.validate.maxLength)
                .onUIKeyUp(this.updateDefaultType.bind(this))
                .onUIChange(this.updateDefaultType.bind(this));
            object.UIGroup.addUI(object.UIGroup.UIInput);

            // select
            const selectOptionValue = (defaultTypeValueArray[0] === 'select') ? defaultTypeValueArray[1] : data.selectOption[0].value;
            const selectOption = data.selectOption.reduce((result, option) => {
                result[option.value] = i18n.msg(option.name);
                return result;
            }, {});
            object.UIGroup.UISelect = new UISelect().setUIId(key)
                .addUIClass((defaultTypeValueArray[0] === 'select') ? 'on' : 'off')
                .setUIOptions(selectOption).setUIValue(selectOptionValue)
                .onUIChange(this.updateDefaultType.bind(this));
            object.UIGroup.addUI(object.UIGroup.UISelect);
            
            object.addUI(object.UIGroup);
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