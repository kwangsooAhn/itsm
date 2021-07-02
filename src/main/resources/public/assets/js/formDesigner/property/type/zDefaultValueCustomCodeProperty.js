/**
 * Default Value Radio Property Class
 *
 * 커스텀 코드 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { FORM } from '../../../lib/zConstants.js';
import { UIDiv, UILabel, UIRadioButton, UISpan, UISelect } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    options: [
        { name: 'form.properties.option.none', value: FORM.CUSTOM.NONE },
        { name: 'form.properties.default.session', value: FORM.CUSTOM.SESSION },
        { name: 'form.properties.default.code', value: FORM.CUSTOM.CODE }
    ],
    selectOptions : [
        { name: 'form.properties.userKey', value: 'userKey' },
        { name: 'form.properties.userId', value: 'userId' },
        { name: 'form.properties.userName', value: 'userName' },
        { name: 'form.properties.email', value: 'email' },
        { name: 'form.properties.jobPosition', value: 'position' },
        { name: 'form.properties.department', value: 'department' },
        { name: 'form.properties.officeNumber', value: 'officeNumber' },
        { name: 'form.properties.officeNumber', value: 'officeNumber' }
    ]
};

export default class ZDefaultValueCustomCodeProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'defaultValueCustomCodeProperty', value);

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

        // customCode|none|없음  customCode|session|세션값  customCode|code|코드값|코드명
        const defaultCustomCodeValues = this.value.split('|');
        // custom code
        const customCodeOption = FORM.CUSTOM_CODE.reduce((result, option) => {
            option.name = option.customCodeName;
            option.value = option.customCodeId;
            result.push(option);
            return result;
        }, []);

        this.UIElement.UISelect = new UISelect()
            .setUIId('customCode')
            .setUIAttribute('name', this.key)
            .setUIOptions(customCodeOption)
            .setUIValue(defaultCustomCodeValues[0])
            .setUIAttribute('data-value', 'customCode')
            .onUIChange(this.updateCustomCodeDate.bind(this));
        this.UIElement.addUI(this.UIElement.UISelect);

        // 기본 값
        // 기본 값 - 라벨
        this.UIElement.UILabel = this.makeLabelProperty('form.properties.element.defaultValue');
        this.UIElement.UILabel.addUIClass('mt-3');
        this.UIElement.addUI(this.UIElement.UILabel);
        // 기본 값 - 그룹
        this.UIElement.UIGroup = new UIDiv().setUIClass('default-type-radio');
        this.UIElement.addUI(this.UIElement.UIGroup);

        this.options.forEach((item) => {
            const radioGroup = new UIDiv().setUIClass('radio-property-group').addUIClass('vertical');
            const radioId = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            // 라벨
            radioGroup.UILabel = new UILabel().setUIClass('radio').setUIFor('radioProperty' + radioId);
            radioGroup.addUI(radioGroup.UILabel);
            // 라디오 버튼
            radioGroup.UILabel.UIRadio = new UIRadioButton(defaultCustomCodeValues[1] === item.value)
                .setUIId('radioProperty' + radioId)
                .setUIAttribute('name', this.key)
                .setUIClass('hidden-radio')
                .setUIAttribute('data-value', item.value)
                .onUIChange(this.updateProperty.bind(this));
            radioGroup.UILabel.addUI(radioGroup.UILabel.UIRadio);
            radioGroup.UILabel.addUI(new UISpan());

            if (!zValidation.isEmpty(item.name)) {
                radioGroup.UILabel.addUI(new UISpan().setUIClass('label').setUIInnerHTML(i18n.msg(item.name)));
            }

            switch (item.value) {
            case FORM.CUSTOM.SESSION:
                const sessionSelectOption = this.selectOptions.reduce((result, option) => {
                    option.name = i18n.msg(option.name);
                    result.push(option);
                    return result;
                }, []);
                const sessionSelectOptionValue = (defaultCustomCodeValues[1] === item.value) ? defaultCustomCodeValues[2] : this.selectOptions[0].value;
                radioGroup.UISelect = new UISelect()
                    .addUIClass('mt-1')
                    .setUIId('session')
                    .setUIOptions(sessionSelectOption)
                    .setUIValue(sessionSelectOptionValue)
                    .setUIAttribute('data-value', item.value)
                    .onUIChange(this.updateProperty.bind(this));
                radioGroup.addUI(radioGroup.UISelect);
                break;
            case FORM.CUSTOM.CODE:
                radioGroup.UISelect = new UISelect()
                    .addUIClass('mt-1')
                    .setUIId('code')
                    .setUIAttribute('data-value', item.value)
                    .onUIChange(this.updateProperty.bind(this));
                radioGroup.addUI(radioGroup.UISelect);
                this.UIElement.UIGroup.UIDiv = radioGroup;

                const customCodeValue = (defaultCustomCodeValues[1] === item.value) ? defaultCustomCodeValues[2] : '';
                this.makeCustomCodeDate(radioGroup.UISelect, defaultCustomCodeValues[0], customCodeValue);
                break;
            }
            this.UIElement.UIGroup.addUI(radioGroup);
        });

        return this.UIElement;
    }
    // 커스텀 코드 데이터 select box 생성
    makeCustomCodeDate(UISelect, customCodeId, customCodeValue) {
        aliceJs.fetchJson('/rest/custom-codes/' + customCodeId, {
            method: 'GET'
        }).then((customCodeData) => {
            if (!zValidation.isEmpty(customCodeData)) {
                const customCodeDataOption = customCodeData.reduce((result, data) => {
                    data.name = data.value;
                    data.value = data.key;
                    result.push(data);
                    return result;
                }, []);
                const customDataOptionValue = zValidation.isEmpty(customCodeValue) ? customCodeData[0].key : customCodeValue;
                UISelect.setUIOptions(customCodeDataOption).setUIValue(customDataOptionValue);
            }
        });
    }
    // 커스텀 코드 변경시 커스텀 코드 데이터 select box를 업데이트 한다.
    updateCustomCodeDate(e) {
        this.makeCustomCodeDate(this.UIElement.UIGroup.UIDiv.UISelect, e.target.value, '');

        this.updateProperty.call(this, e);
    }

    // 속성 변경 시, 발생하는 이벤트 핸들러
    // customCode|none|없음  customCode|session|세션값  customCode|code|코드값|코드명
    updateProperty(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        const elem = e.target || e;
        const parentElem = elem.type === 'radio' ? elem.parentNode.parentNode : elem.parentNode;
        const checkedRadio = parentElem.querySelector('input[type=radio]:checked');
        const selectElem = checkedRadio.parentNode.parentNode.querySelector('.select');
        // radio 변경시
        const customCodeId = this.UIElement.UISelect.domElement.value;
        const radioType = checkedRadio.getAttribute('data-value');

        switch (radioType) {
        case FORM.CUSTOM.NONE:
            this.panel.update.call(this.panel, this.key, customCodeId + '|' + radioType  + '|');
            break;
        case FORM.CUSTOM.SESSION:
            this.panel.update.call(this.panel, this.key, customCodeId + '|' + radioType  + '|' + selectElem.value);
            break;
        case FORM.CUSTOM.CODE:
            const selectText = selectElem.options[selectElem.selectedIndex].text;
            this.panel.update.call(this.panel, this.key, customCodeId + '|' + radioType  + '|' + selectElem.value + '|' + selectText);
            break;
        }
    }
}
