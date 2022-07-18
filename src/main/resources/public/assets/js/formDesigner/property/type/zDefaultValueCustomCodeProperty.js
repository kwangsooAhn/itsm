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
import { UIButton, UIDiv, UILabel, UIRadioButton, UISpan, UISelect, UIInput } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    options: [
        { name: 'form.properties.option.none', value: FORM.CUSTOM.NONE },
        { name: 'form.properties.default.session', value: FORM.CUSTOM.SESSION },
        { name: 'form.properties.default.code', value: FORM.CUSTOM.CODE }
    ],
    selectOptions: [
        { name: 'form.properties.userKey', value: 'userKey' },
        { name: 'form.properties.userId', value: 'userId' },
        { name: 'form.properties.userName', value: 'userName' },
        { name: 'form.properties.email', value: 'email' },
        { name: 'form.properties.jobPosition', value: 'position' },
        { name: 'form.properties.department', value: 'department' },
        { name: 'form.properties.officeNumber', value: 'officeNumber' }
    ]
};

export default class ZDefaultValueCustomCodeProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'defaultValueCustomCodeProperty', value, isAlwaysEditable);

        this.options = propertyExtends.options;
        this.selectOptions = propertyExtends.selectOptions;
    }

    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우) 또는 (문서가 '사용/발행' 이고 연결된 업무흐름이 없는 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);
        
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

        if (!this.isEditable) {
            this.UIElement.UISelect.addUIClass('readonly');
        }

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
                .setUIClass('radio')
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

            if (!this.isEditable) {
                radioGroup.UILabel.addUIClass('readonly');
                radioGroup.UILabel.UIRadio.addUIClass('readonly');
            }

            if (!zValidation.isEmpty(item.name)) {
                radioGroup.UILabel.addUI(new UISpan().setUIClass('label').setUIInnerHTML(i18n.msg(item.name)));
            }
            switch (item.value) {
                case FORM.CUSTOM.SESSION:
                    const matchCustomCode = this.getCustomCode(defaultCustomCodeValues[0]);
                    if (zValidation.isEmpty(matchCustomCode.sessionKey)) {
                        radioGroup.setUIDisplay('none');
                    }
                    break;
                case FORM.CUSTOM.CODE:
                    radioGroup.UIInputButton = new UIDiv()
                        .setUIClass('flex-row input-button');
                    radioGroup.addUI(radioGroup.UIInputButton);
                    // input
                    const customCodeId = (defaultCustomCodeValues[1] === item.value) ? defaultCustomCodeValues[2] : '';
                    const customCodeText = (defaultCustomCodeValues[1] === item.value) ? defaultCustomCodeValues[3] : '';
                    radioGroup.UIInputButton.UIInput = new UIInput()
                        .setUIReadOnly(true)
                        .setUIId('code')
                        .setUIAttribute('data-value', customCodeId)
                        .setUIValue(customCodeText)
                        .onUIChange(this.updateProperty.bind(this));
                    radioGroup.UIInputButton.addUI(radioGroup.UIInputButton.UIInput);
                    if (this.isEditable) {
                        // small icon button
                        radioGroup.UIInputButton.UIIconButton = new UIButton()
                            .setUIClass('button-icon-sm')
                            .setUIAttribute('tabindex', '-1')
                            .onUIClick(this.clearText.bind(this));
                        radioGroup.UIInputButton.UIIconButton.UIIcon = new UISpan()
                            .setUIClass('icon')
                            .addUIClass('i-remove');
                        radioGroup.UIInputButton.UIIconButton.addUI(radioGroup.UIInputButton.UIIconButton.UIIcon);
                        radioGroup.UIInputButton.addUI(radioGroup.UIInputButton.UIIconButton);
                        // button
                        radioGroup.UIInputButton.UIButton = new UIButton()
                            .setUIClass('button-icon')
                            .addUIClass('button-code')
                            .setUIAttribute('data-value', defaultCustomCodeValues[0])
                            .addUI(new UISpan().setUIClass('icon').addUIClass('i-search'))
                            .onUIClick(this.openCustomCodeData.bind(this));
                        radioGroup.UIInputButton.addUI(radioGroup.UIInputButton.UIButton);
                    }
                    break;
            }
            this.UIElement.UIGroup['UIRadioGroup' + idx] = radioGroup;
            this.UIElement.UIGroup.addUI(radioGroup);
        });

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    // 커스텀 코드 조회
    getCustomCode(id) {
        return FORM.CUSTOM_CODE.find(function(data) {
            return data.customCodeId === id;
        });
    }

    // 코드 선택 모달
    openCustomCodeData() {
        const customCodeId = this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIButton.getUIAttribute('data-value');
        const selectedValue = this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.getUIAttribute('data-value');
        tree.load({
            view: 'modal',
            title: i18n.msg('form.label.customCodeTarget'),
            dataUrl: '/rest/custom-codes/' + customCodeId,
            target: 'treeList',
            text: 'codeName',
            rootAvailable: false,
            leafIcon: '/assets/media/icons/tree/icon_tree_leaf.svg',
            selectedValue: selectedValue,
            callbackFunc: (response) => {
                this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIValue(response.textContent);
                this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIAttribute('data-value', response.id);
                this.updateProperty.call(this);
            }
        });
    }

    // 커스텀 코드 데이터 select box 생성
    async makeCustomCodeData(UISelect, customCodeId, customCodeValue) {
        let customCodeDataOption = [];
        const response = await aliceJs.fetchJson('/rest/custom-codes/' + customCodeId, {
            method: 'GET'
        });

        if (response.status === aliceJs.response.success && !zValidation.isEmpty(response.data)) {
            let customCodeData = response.data.data;
            for (let i = 0; i < customCodeData.length; i++) {
                if (zValidation.isEmpty(customCodeData[i].name)) {
                    customCodeData[i].name = customCodeData[i].value;
                }
                customCodeData[i].value = customCodeData[i].key;
                customCodeDataOption.push(customCodeData[i]);
            }

            const customDataOptionValue = zValidation.isEmpty(customCodeValue) ? customCodeData[0].key : customCodeValue;
            UISelect.setUIOptions(customCodeDataOption).setUIValue(customDataOptionValue);
        }
    }

    // 커스텀 코드 변경시 커스텀 코드 데이터 select box를 업데이트 한다.
    updateCustomCodeData(e) {
        const matchCustomCode = this.getCustomCode(e.target.value);
        this.UIElement.UIGroup['UIRadioGroup1']
            .setUIDisplay(zValidation.isEmpty(matchCustomCode.sessionKey) ? 'none' : 'inline-flex');

        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIButton.setUIAttribute('data-value', matchCustomCode.customCodeId);
        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIValue('');
        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIAttribute('data-value', '');

        this.updateProperty.call(this, e);
    }
    // 코드 삭제
    clearText(e) {
        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIValue('');
        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIAttribute('data-value', '');

        this.updateProperty.call(this, e);
    }

    // 속성 변경 시, 발생하는 이벤트 핸들러
    // customCode|none|없음  customCode|session|세션값  customCode|code|코드값|코드명
    updateProperty(e) {
        if (e && e.preventDefault) {
            e.preventDefault();
        }

        const curRadioElem = this.UIElement.UIGroup.domElement.querySelector('input[type=radio]:checked');
        const customCodeId = this.UIElement.UISelect.domElement.value;
        const radioType = curRadioElem.getAttribute('data-value');
        const matchCustomCode = this.getCustomCode(customCodeId);
        const codeInputElem = this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.domElement;
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
                    codeInputElem.getAttribute('data-value') + '|' +
                    codeInputElem.value);
                break;
        }
    }
}
