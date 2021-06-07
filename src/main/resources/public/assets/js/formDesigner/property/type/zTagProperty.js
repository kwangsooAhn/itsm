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
import ZProperty from '../zProperty.js';
import { UIDiv, UIInput } from '../../../lib/ui.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZTagProperty extends ZProperty {
    constructor(name, value) {
        super(name, 'tagProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // inputbox
        this.UIElement.UIInput = new UIInput().setUIId(this.getKeyId()).setUIValue(this.value);
        this.UIElement.addUI(this.UIElement.UIInput);

        // tag 는 실제 그려진 UI를 이용해서 tagify 적용이 필요함.
        // 여기서 append 를 하고 리턴을 null.
        panel.domElement.appendChild(this.UIElement.domElement);
        if (this.panel.editor.selectedObject.id) {
            new zTag(document.querySelector('input[id=tags]'), {
                suggestion: true,
                realtime: false,
                tagType: 'component',
                targetId: this.panel.editor.selectedObject.id
            }, {
                callbacks: {
                    'add': (e) => {
                        const inputElem = e.detail.tagify.DOM.originalInput;
                        this.panel.update.call(this.panel, inputElem.id, inputElem.value);
                    },
                    'remove': (e) => {
                        const inputElem = e.detail.tagify.DOM.originalInput;
                        this.panel.update.call(this.panel, inputElem.id, inputElem.value);
                    }
                }
            });
        }
        return null;
    }
}