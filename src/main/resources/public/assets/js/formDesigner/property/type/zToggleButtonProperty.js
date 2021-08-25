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
import ZProperty from '../zProperty.js';
import { UIButton, UIDiv, UISpan } from '../../../lib/zUI.js';

const propertyExtends = {
    /* 슬라이드 속성 타입은 추가적인 설정이 없다. */
};

export default class ZToggleButtonProperty extends ZProperty {
    constructor(key, name, value, options) {
        super(key, name, 'toggleButtonProperty', value);

        this.options = options;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // 버튼 그룹
        this.UIElement.UIButtonGroup = new UIDiv().setUIClass('z-button-toggle-group');
        const toggleValueArray = this.value.split('|');
        this.options.forEach((item, index) => {
            const name = item.value.substr(0, 1).toUpperCase() +
                item.value.substr(1, item.value.length);

            this.UIElement.UIButtonGroup['UIButton' + name] = new UIButton()
                .setUIId(this.key + name)
                .setUIAttribute('data-value', (toggleValueArray[index] === 'Y'))
                .setUIClass('z-button-toggle')
                .onUIClick(this.updateProperty.bind(this))
                .addUI(new UISpan().setUIClass('z-icon').addUIClass(item.name));

            if (toggleValueArray[index] === 'Y') {
                this.UIElement.UIButtonGroup['UIButton' + name].addUIClass('selected');
            }
            this.UIElement.UIButtonGroup.addUI(this.UIElement.UIButtonGroup['UIButton' + name]);
        });
        this.UIElement.addUI(this.UIElement.UIButtonGroup);

        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();
        // bold, italic 등 toggle button
        if (e.target.classList.contains('selected')) {
            e.target.classList.remove('selected');
            e.target.setAttribute('data-value', false);
        } else {
            e.target.classList.add('selected');
            e.target.setAttribute('data-value', true);
        }
        this.panel.update.call(this.panel, e.target.id, e.target.getAttribute('data-value') === 'true');
    }
}