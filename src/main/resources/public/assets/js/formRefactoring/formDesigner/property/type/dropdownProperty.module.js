/**
 * dropdown Property Class
 *
 * SELECT 형태의 속성항목 타입을 위한 클래스이다.
 * 실제 속성 값으로 선택할 수 있는 데이터를 options 필드에 추가한다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from '../property.module.js';
import { UIDiv, UISelect } from '../../../lib/ui.js';

const propertyExtends = {
    options : []
};

export default class DropdownProperty extends Property {
    constructor(name, value, options) {
        super(name, 'dropdownProperty', value);

        this.options = options;
    }

    makeProperty(panel) {
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // select box
        const mergeOptions = this.options.reduce((result, option) => {
            result[option.value] = i18n.msg(option.name);
            return result;
        }, {});
        this.UIElement.UISelect = new UISelect()
            .setUIId(this.getKeyId())
            .setUIOptions(mergeOptions)
            .setUIValue(this.value)
            .onUIChange(panel.updateProperty.bind(panel));
        this.UIElement.addUI(this.UIElement.UISelect);

        return this.UIElement;
    }
}