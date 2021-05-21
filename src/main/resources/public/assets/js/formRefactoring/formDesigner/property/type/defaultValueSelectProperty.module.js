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
import Property from '../property.module.js';
import {UIButton, UIDiv, UIInput, UISelect, UISpan} from '../../../lib/ui.js';

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

export default class DefaultValueSelectProperty extends Property {
    constructor(name, value) {
        super(name, 'defaultValueSelectProperty', value);

        this.options = propertyExtends.options;
        this.selectOptions = propertyExtends.selectOptions;
    }

    makeProperty(panel) {
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
                .addUIClass('btn-switch').onUIClick(panel.updateDefaultType.bind(panel));
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
            .onUIKeyUp(panel.updateDefaultType.bind(panel))
            .onUIChange(panel.updateDefaultType.bind(panel));
        this.UIElement.UIGroup.addUI(this.UIElement.UIGroup.UIInput);

        // select
        const selectOptionValue = (defaultTypeValueArray[0] === 'select') ? defaultTypeValueArray[1] : this.selectOptions[0].value;
        const selectOption = this.selectOptions.reduce((result, option) => {
            result[option.value] = i18n.msg(option.name);
            return result;
        }, {});
        this.UIElement.UIGroup.UISelect = new UISelect().setUIId(this.getKeyId())
            .addUIClass((defaultTypeValueArray[0] === 'select') ? 'on' : 'off')
            .setUIOptions(selectOption).setUIValue(selectOptionValue)
            .onUIChange(panel.updateDefaultType.bind(panel));
        this.UIElement.UIGroup.addUI(this.UIElement.UIGroup.UISelect);

        this.UIElement.addUI(this.UIElement.UIGroup);

        return this.UIElement;
    }
}