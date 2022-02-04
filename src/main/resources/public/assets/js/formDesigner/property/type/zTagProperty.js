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
import { UIDiv, UIInput } from '../../../lib/zUI.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ZTagProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'tagProperty', value, isAlwaysEditable);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);
        // inputbox
        // 태그 속성의 특이한 점은 실제로는 input box 를 사용하지만 값은 json 형태의 데이터가 Array 형태로 관리된다는 점이다.
        // 아래와 같이 스트링으로 변환하여 input box 의 값으로 넣어줘야 tagify 에서 파싱해서 쓸 수 있다.
        let valueString = '[' +
            this.value.map(function (tag) {
                return JSON.stringify(tag);
            }).toString() + ']';

        this.UIElement.UIInput = new UIInput()//.removeUIClass('z-input').addUIClass('input')
            .setUIId(this.key).setUIValue(valueString)
            .setUIReadOnly(!this.isEditable);
        this.UIElement.addUI(this.UIElement.UIInput);

        // tag 는 실제 그려진 UI를 이용해서 tagify 적용이 필요함.
        // 여기서 append 를 하고 리턴을 null.
        panel.domElement.appendChild(this.UIElement.domElement);
        if (this.panel.editor.selectedObject.id) {
            new zTag(document.querySelector('input[id=tags]'), {
                suggestion: this.isEditable,
                realtime: false,
                tagType: 'component',
                targetId: this.panel.editor.selectedObject.id
            }, {
                callbacks: {
                    'add': (e) => {
                        const inputElem = e.detail.tagify.DOM.originalInput;
                        this.panel.update.call(this.panel, inputElem.id, e.detail.tagify.value);
                    },
                    'remove': (e) => {
                        const inputElem = e.detail.tagify.DOM.originalInput;
                        this.panel.update.call(this.panel, inputElem.id, e.detail.tagify.value);
                    }
                }
            });
        }
        return null;
    }
}