/**
 * Group Property Class
 *
 * 그룹 형태로 하위 속성을 묶어주는 클래스이다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { UIDiv, UILabel, UISpan } from '../../../lib/zUI.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZGroupProperty {
    constructor(name) {
        this._name = 'form.properties.' + name;
        this._type = 'groupProperty';
        this.children = [];
    }

    get name() {
        return this._name;
    }

    set name(name) {
        this._name = 'form.properties.' + name;
    }

    get type() {
        return this._type;
    }

    set type(type) {
        this._type = type;
    }

    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property-group');
        // 라벨
        this.UIElement.UILabel = new UILabel().setUIClass('property-group-label')
            .setUITextAlign('left');
        // 라벨 문구
        this.UIElement.UILabel.UILabelText = new UISpan().setUIClass('property-group-label-text')
            .setUITextContent(i18n.msg(this.name));
        this.UIElement.UILabel.addUI(this.UIElement.UILabel.UILabelText);
        this.UIElement.addUI(this.UIElement.UILabel);

        return this.UIElement;
    }

    addProperty(object) {
        if (!object) { return false; }

        this.children.push(object);
        return this;
    }
}