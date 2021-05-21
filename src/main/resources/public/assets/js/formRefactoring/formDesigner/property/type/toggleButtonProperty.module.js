/**
 * Toggle Button Property Class
 *
 * 속성을 선택하는 아이콘들을 모임이며 중복선택이 가능한 속성 타입을 위한 클래스이다.
 * 예를 들어 폰트 옵션의 경우 Bold, Italic 등의 중복 선택이 가능하다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from '../property.module.js';
import {UIButton, UIDiv, UISpan} from '../../../lib/ui.js';

const propertyExtends = {
    /* 슬라이드 속성 타입은 추가적인 설정이 없다. */
};

export default class ToggleButtonProperty extends Property {
    constructor(name, value, options) {
        super(name, 'toggleButtonProperty', value);

        this.options = options;
    }

    makeProperty(panel) {
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // 버튼 그룹
        this.UIElement.UIButtonGroup = new UIDiv().setUIClass('btn-toggle-group');
        const toggleValueArray = this.value.split('|');
        this.options.forEach((item, index) => {
            const name = item.value.substr(0, 1).toUpperCase() +
                item.value.substr(1, item.value.length);

            this.UIElement.UIButtonGroup['UIButton' + name] = new UIButton()
                .setUIId(this.getKeyId() + name)
                .setUIAttribute('data-value', (toggleValueArray[index] === 'Y'))
                .addUIClass('btn-toggle').onUIClick(panel.updateButton.bind(panel));
            this.UIElement.UIButtonGroup['UIButton' + name]
                .addUI(new UISpan().setUIClass('icon').addUIClass(item.name));

            if (toggleValueArray[index] === 'Y') {
                this.UIElement.UIButtonGroup['UIButton' + name].addUIClass('active');
            }
            this.UIElement.UIButtonGroup.addUI(this.UIElement.UIButtonGroup['UIButton' + name]);
        });
        this.UIElement.addUI(this.UIElement.UIButtonGroup);

        return this.UIElement;
    }
}