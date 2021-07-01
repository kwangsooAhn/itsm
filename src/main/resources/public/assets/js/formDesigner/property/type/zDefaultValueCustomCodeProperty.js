/**
 * Default Value Radio Property Class
 *
 * 커스텀 코드 컴포넌트의 기본 값을 어떤 방식으로 제공할지 선택하는 속성항목이다.
 *
 * @author Kim Sung Min <ksm00@brainz.co.kr>
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
    /* 추가적인 설정이 없다. */
};

export default class ZDefaultValueCustomCodeProperty extends ZProperty {
    constructor(key, name, dropDownValue, customCodeValue, dropDownOptions, customCodeOptions, customCodeOptionValue) {
        super(key, name, 'defaultValueCustomCodeProperty', dropDownValue);

        this.dropDownOptions = dropDownOptions;
        this.customCodeOptions = customCodeOptions;
        this.customCodeOptionValue = customCodeOptionValue;
        this.customCodeValue = customCodeValue;
    }

    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        //커스텀 코드
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty('form.properties.customCode');
        this.UIElement.addUI(this.UIElement.UILabel);

        // select box
        this.UIElement.UISelect = new UISelect()
            .setUIId('customCode')
            .setUIAttribute('name', this.key)
            .setUIOptions(JSON.parse(JSON.stringify(this.dropDownOptions)))
            .setUIValue(this.value)
            .setUIAttribute('data-value', 'customCode')
            .onUIChange(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UISelect);

        // 기본 값
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // 그룹
        this.UIElement.UIGroup = new UIDiv().setUIClass('default-type-radio');
        this.UIElement.addUI(this.UIElement.UIGroup);

        const defaultValueArray = this.value.split('|'); // none, now, date|-3, time|2, datetime|7|0 등

        this.customCodeOptions.forEach((item) => {
            const radioGroup = new UIDiv().setUIClass('radio-property-group').addUIClass('vertical');
            const radioId = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length);
            // 라벨
            radioGroup.UILabel = new UILabel().setUIClass('radio').setUIFor('radioProperty' + radioId);
            radioGroup.addUI(radioGroup.UILabel);
            // 라디오 버튼
            radioGroup.UILabel.UIRadio = new UIRadioButton(defaultValueArray[0] === item.value)
                .setUIId('radioProperty' + radioId)
                .setUIAttribute('name', this.key)
                .setUIClass('hidden-radio')
                .setUIAttribute('data-value', item.value)
                .onUIChange(this.updateProperty.bind(this));
            radioGroup.UILabel.addUI(radioGroup.UILabel.UIRadio);
            radioGroup.UILabel.addUI(new UISpan());
            if (item.name !== '') {
                radioGroup.UILabel.addUI(new UISpan().setUIClass('label').setUIInnerHTML(i18n.msg(item.name)));
            }

            switch (item.value) {
            case FORM.CUSTOM.SESSION:
                radioGroup.UISelect = new UISelect().setUIId('session')
                    .setUIOptions([
                        {name: i18n.msg('form.properties.name'), value: 'name'},
                        {name: i18n.msg('form.properties.department'), value: 'department'}
                    ]);
                radioGroup.addUI(radioGroup.UISelect);
                break;
            case FORM.CUSTOM.CODE:
                radioGroup.UISelect = new UISelect().setUIId('code').setUIOptions(this.customCodeOptionValue);
                radioGroup.addUI(radioGroup.UISelect);
                break;
            }

            this.UIElement.UIGroup.addUI(radioGroup);
        });

        return this.UIElement;
    }

    // 속성 변경 시, 발생하는 이벤트 핸들러
    // 값을 none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등으로 저장한다.
    updateProperty(e) {
        if (e.preventDefault) {
            e.preventDefault();
        }
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }

        const elem = e.target || e;
        const elemName = elem.getAttribute('data-value');

        switch (elemName) {
        case 'customCode':
            this.panel.update.call(this.panel, this.key, 'customCode|'+ e.target.value);
            break;
        case 'none':
            this.panel.update.call(this.panel, this.key, 'none||');
            break;
        case 'session':
            this.panel.update.call(this.panel, this.key, 'session|'+ e.target.value);
            break;
        case 'code':
            this.panel.update.call(this.panel, this.key, 'code|'+ e.target.value);
            break;
        }
    }
}
