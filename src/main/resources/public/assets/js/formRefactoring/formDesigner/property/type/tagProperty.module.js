/**
 * Tag Property Class
 *
 * 태그를 추가하는 속성항목을 위한 클래스이다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from '../property.module.js';
import { UIDiv } from '../../../lib/ui.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class TagProperty extends Property{
    constructor(name, value) {
        super(name, 'tagProperty', value);
    }

    makeProperty(panel) {
        // TODO: 태그 디자인 추가
        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        return this.UIElement;
    }
}