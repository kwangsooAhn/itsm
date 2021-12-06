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
import {FORM} from '../../../lib/zConstants.js';
import {UIDiv, UILabel, UIRadioButton, UISpan, UISelect} from '../../../lib/zUI.js';
import {zValidation} from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    options: [
        {name: 'form.properties.option.none', value: FORM.CUSTOM.NONE},
        {name: 'form.properties.default.session', value: FORM.CUSTOM.SESSION},
        {name: 'form.properties.default.code', value: FORM.CUSTOM.CODE}
    ],
    selectOptions: [
        {name: 'form.properties.userKey', value: 'userKey'},
        {name: 'form.properties.userId', value: 'userId'},
        {name: 'form.properties.userName', value: 'userName'},
        {name: 'form.properties.email', value: 'email'},
        {name: 'form.properties.jobPosition', value: 'position'},
        {name: 'form.properties.department', value: 'department'},
        {name: 'form.properties.officeNumber', value: 'officeNumber'}
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
            .onUIChange(this.updateCustomCodeData.bind(this));
        this.UIElement.addUI(this.UIElement.UISelect);

        // 기본 값
        // 기본 값 - 라벨
        this.UIElement.UILabel = this.makeLabelProperty('form.properties.element.defaultValue',
            'form.help.custom-code-default');
        this.UIElement.UILabel.addUIClass('mt-3');
        this.UIElement.addUI(this.UIElement.UILabel);
        // 기본 값 - 그룹
        this.UIElement.UIGroup = new UIDiv().setUIClass('default-type-radio');
        this.UIElement.addUI(this.UIElement.UIGroup);

        this.options.forEach((item, idx) => {
            const radioGroup = new UIDiv().setUIClass('radio-property-group').addUIClass('vertical');
            const radioId = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            // 라벨
            radioGroup.UILabel = new UILabel()
                .setUIClass('z-radio')
                .addUIClass('mb-1')
                .setUIFor('radioProperty' + radioId);
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
                radioGroup.UILabel.addUI(new UISpan().setUIClass('z-label').setUIInnerHTML(i18n.msg(item.name)));
            }

            switch (item.value) {
                case FORM.CUSTOM.SESSION:
                    const matchCustomCode = this.getCustomCode(defaultCustomCodeValues[0]);
                    if (zValidation.isEmpty(matchCustomCode.sessionKey)) {
                        radioGroup.setUIDisplay('none');
                    }
                    break;
                case FORM.CUSTOM.CODE:
                    radioGroup.UISelect = new UISelect()
                        .setUIId('code')
                        .setUIAttribute('data-value', item.value)
                        .onUIChange(this.updateProperty.bind(this));
                    const customCodeValue = (defaultCustomCodeValues[1] === item.value) ? defaultCustomCodeValues[2] : '';
                    this.makeCustomCodeData(radioGroup.UISelect, defaultCustomCodeValues[0], customCodeValue).then(function () {
                        radioGroup.addUI(radioGroup.UISelect);
                        aliceJs.initDesignedSelectTag(radioGroup.domElement);
                    });
                    break;
            }
            this.UIElement.UIGroup['UIRadioGroup' + idx] = radioGroup;
            this.UIElement.UIGroup.addUI(radioGroup);
        });

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
    }

    // 커스텀 코드 조회
    getCustomCode(id) {
        return FORM.CUSTOM_CODE.find(function (data) {
            return data.customCodeId === id;
        });
    }

    // 커스텀 코드 데이터 select box 생성
    async makeCustomCodeData(UISelect, customCodeId, customCodeValue) {
        let customCodeDataOption = [];
        let customCodeData = await aliceJs.fetchJson('/rest/custom-codes/' + customCodeId, {
            method: 'GET'
        });
        for (let i = 0; i < customCodeData.length; i++) {
            if (zValidation.isEmpty(customCodeData[i].name)) {
                customCodeData[i].name = customCodeData[i].value;
            }
            customCodeData[i].value = customCodeData[i].key;
            customCodeDataOption.push(customCodeData[i]);
        }
        if (!zValidation.isEmpty(customCodeData)) {
            const customDataOptionValue = customCodeData[0].key;
            UISelect.setUIOptions(customCodeDataOption).setUIValue(customDataOptionValue);
        }
    }

    // 커스텀 코드 변경시 커스텀 코드 데이터 select box를 업데이트 한다.
    async updateCustomCodeData(e) {
        const matchCustomCode = this.getCustomCode(e.target.value);
        if (zValidation.isEmpty(matchCustomCode.sessionKey)) {
            this.UIElement.UIGroup['UIRadioGroup1'].setUIDisplay('none');

        } else {
            this.UIElement.UIGroup['UIRadioGroup1'].setUIDisplay('inline-flex');
        }

        await this.makeCustomCodeData(this.UIElement.UIGroup['UIRadioGroup2'].UISelect, e.target.value, '');

        this.updateProperty.call(this, e);
    }

    // 속성 변경 시, 발생하는 이벤트 핸들러
    // customCode|none|없음  customCode|session|세션값  customCode|code|코드값|코드명
    updateProperty(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }

        const curRadioElem = this.UIElement.UIGroup.domElement.querySelector('input[type=radio]:checked');
        const customCodeId = this.UIElement.UISelect.domElement.value;
        const radioType = curRadioElem.getAttribute('data-value');
        const matchCustomCode = this.getCustomCode(customCodeId);
        const codeSelectBox = this.UIElement.UIGroup['UIRadioGroup2'].UISelect.domElement;
        switch (radioType) {
            case FORM.CUSTOM.NONE:
                this.panel.update.call(this.panel, this.key, customCodeId + '|' + radioType + '|');
                break;
            case FORM.CUSTOM.SESSION:

                // 세션 값이 없으면 > 기본값 없음으로 선택하기
                if (zValidation.isEmpty(matchCustomCode.sessionKey)) {
                    this.UIElement.UIGroup['UIRadioGroup0'].UILabel.UIRadio.domElement.checked = true;
                    this.panel.update.call(this.panel, this.key, customCodeId + '|' + FORM.CUSTOM.NONE + '|');
                } else {
                    this.panel.update.call(this.panel, this.key, customCodeId + '|' +
                        radioType + '|' + matchCustomCode.sessionKey);
                }
                break;
            case FORM.CUSTOM.CODE:
                this.panel.update.call(this.panel, this.key, customCodeId + '|' + radioType + '|' +
                    codeSelectBox.options[codeSelectBox.selectedIndex].value + '|' +
                    codeSelectBox.options[codeSelectBox.selectedIndex].text);
                break;
        }
    }
}
