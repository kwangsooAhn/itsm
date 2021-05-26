/**
 * Switch Button Property Class
 *
 * 속성을 선택하는 아이콘들 중에서 1개만 선택할 수 있는 속성 타입을 위한 클래스이다.
 * 예를 들어 정렬 속성 같은 경우, 좌측, 중앙, 우측 정렬중 1개만 선택이 가능하다.
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

export default class SwitchButtonProperty extends Property {
    constructor(name, value, options) {
        super(name, 'switchButtonProperty', value);

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
        this.UIElement.UIButtonGroup = new UIDiv().setUIClass('btn-switch-group');
        this.options.forEach((item) => {
            const name = item.value.substr(0, 1).toUpperCase() +
                item.value.substr(1, item.value.length);
            this.UIElement.UIButtonGroup['UIButton' + name] = new UIButton()
                .setUIId(this.getKeyId())
                .setUIAttribute('data-value', item.value)
                .addUIClass('btn-switch')
                .onUIClick(this.updateProperty.bind(this));
            this.UIElement.UIButtonGroup['UIButton' + name].addUI(new UISpan().setUIClass('icon').addUIClass(item.name));

            if (this.value === item.value) {
                this.UIElement.UIButtonGroup['UIButton' + name].addUIClass('active');
            }
            this.UIElement.UIButtonGroup.addUI(this.UIElement.UIButtonGroup['UIButton' + name]);
        });
        this.UIElement.addUI(this.UIElement.UIButtonGroup);

        return this.UIElement;
    }
    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();
        // 정렬
        if (e.target.classList.contains('active')) {return false;}

        const buttonGroup = e.target.parentNode;
        for (let i = 0, len = buttonGroup.childNodes.length; i < len; i++) {
            let child = buttonGroup.childNodes[i];
            if (child.classList.contains('active')) {
                child.classList.remove('active');
            }
        }
        e.target.classList.add('active');

        this.panel.update.call(this.panel, e.target.id, e.target.getAttribute('data-value'));
    }
}