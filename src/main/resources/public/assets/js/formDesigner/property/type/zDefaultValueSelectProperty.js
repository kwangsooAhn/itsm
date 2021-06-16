/**
 * Default Value Select Property Class
 *
 * 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 * 현재는 inputBox 에서만 사용되고 있으며 옵션들도 inputBox 용으로 맞추어져 있다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { UIButton, UIDiv, UIInput, UISelect, UISpan } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    options : [
        {name: 'form.properties.direct', value: 'input'},
        {name: 'form.properties.auto', value: 'select'}
    ],
    selectOptions : [
        {name: 'form.properties.userKey', value: 'userKey'},
        {name: 'form.properties.userId', value: 'userId'},
        {name: 'form.properties.userName', value: 'userName'},
        {name: 'form.properties.email', value: 'email'},
        {name: 'form.properties.jobPosition', value: 'position'},
        {name: 'form.properties.department', value: 'department'},
        {name: 'form.properties.officeNumber', value: 'officeNumber'},
        {name: 'form.properties.officeNumber', value: 'officeNumber'}
    ]
};

export default class ZDefaultValueSelectProperty extends ZProperty {
    constructor(name, value) {
        super(name, 'defaultValueSelectProperty', value);

        this.options = propertyExtends.options;
        this.selectOptions = propertyExtends.selectOptions;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // 그룹
        this.UIElement.UIGroup = new UIDiv().setUIClass('default-type');
        const defaultTypeValueArray = this.value.split('|');
        // switch button
        this.UIElement.UIGroup.UIButtonGroup = new UIDiv().setUIClass('btn-switch-group');
        this.options.forEach((item) => {
            const name = item.value.substr(0, 1).toUpperCase() +
                item.value.substr(1, item.value.length);
            this.UIElement.UIGroup.UIButtonGroup['UIButton' + name] = new UIButton().setUIId(this.getKeyId())
                .setUIAttribute('data-type', item.value)
                .addUIClass('btn-switch').onUIClick(this.updateProperty.bind(this));
            this.UIElement.UIGroup.UIButtonGroup['UIButton' + name].addUI(new UISpan().setUIClass('text')
                .setUITextContent(i18n.msg(item.name)));

            if (defaultTypeValueArray[0] === item.value) {
                this.UIElement.UIGroup.UIButtonGroup['UIButton' + name].addUIClass('active');
            }
            this.UIElement.UIGroup.UIButtonGroup.addUI(this.UIElement.UIGroup.UIButtonGroup['UIButton' + name]);
        });
        this.UIElement.UIGroup.addUI(this.UIElement.UIGroup.UIButtonGroup);

        // input
        this.UIElement.UIGroup.UIInput = new UIInput().setUIId(this.getKeyId())
            .addUIClass((defaultTypeValueArray[0] === 'input') ? 'on' : 'off')
            .setUIValue((defaultTypeValueArray[0] === 'input') ? defaultTypeValueArray[1] : '')
            .setUIAttribute('data-validation-minLength', this.validation.minLength)
            .setUIAttribute('data-validation-maxLength', this.validation.maxLength)
            .onUIKeyUp(this.updateProperty.bind(this))
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.UIGroup.addUI(this.UIElement.UIGroup.UIInput);

        // select
        const selectOptionValue = (defaultTypeValueArray[0] === 'select') ? defaultTypeValueArray[1] : this.selectOptions[0].value;
        const selectOption = this.selectOptions.reduce((result, option) => {
            option.name = i18n.msg(option.name);
            result.push(option)
            return result;
        }, []);
        this.UIElement.UIGroup.UISelect = new UISelect().setUIId(this.getKeyId())
            .addUIClass((defaultTypeValueArray[0] === 'select') ? 'on' : 'off')
            .setUIOptions(selectOption).setUIValue(selectOptionValue)
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.UIGroup.addUI(this.UIElement.UIGroup.UISelect);

        this.UIElement.addUI(this.UIElement.UIGroup);

        return this.UIElement;
    }
    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) { return false; }
        if (e.type === 'click' && e.target.classList.contains('active')) { return false; }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            this.panel.validationStatus = false; // 유효성 검증 실패
            return false;
        }
        this.panel.update.call(this.panel, e.target.id, this.getPropertyValue(e.type, e.target));
    }

    /**
     * 속성 엘리먼트의 값 조회
     * @param evtType 이벤트 타입
     * @param e 이벤트객체
     */
    getPropertyValue(evtType, element) {
        switch (evtType) {
        case 'keyup': // input
            return 'input|' + element.value;
        case 'change': // select box, input
            return ((element.type === 'input') ? 'input|' : 'select|') + element.value;
        case 'click': // button
            const buttonGroup = element.parentNode;
            // 초기화
            for (let i = 0, len = buttonGroup.childNodes.length ; i< len; i++) {
                let child = buttonGroup.childNodes[i];
                if (child.classList.contains('active')) {
                    child.classList.remove('active');
                }
            }
            element.classList.add('active');

            const defaultTypeGroup = buttonGroup.parentNode;
            const input = defaultTypeGroup.querySelector('.input');
            const select = defaultTypeGroup.querySelector('.select');
            if (element.getAttribute('data-type') === 'input') { // input 활성화
                input.classList.remove('off');
                select.classList.add('off');
                return 'input|';
            } else { // select 활성화
                select.classList.remove('off');
                input.classList.add('off');
                select.selectedIndex = 0;
                return 'select|' + select.options[0].value;
            }
        default:
            return '';
        }
    }
}