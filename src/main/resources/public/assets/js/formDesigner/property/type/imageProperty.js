/**
 * Image Property Class
 *
 * 이미지를 선택하는 속성항목을 위한 클래스이다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import Property from '../property.js';
import { UIButton, UIDiv, UIInput, UISpan } from '../../../lib/ui.js';

const propertyExtends = {
    /* 추가적인 설정이 없다. */
};

export default class ImageProperty extends Property {
    constructor(name, value) {
        super(name, 'imageProperty', value);
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // input box
        this.UIElement.UIInput = new UIInput()
            .setUIId(this.getKeyId()).setUIValue(this.value)
            .setUIReadOnly(true)
            .onUIFocusout(this.updateProperty.bind(this));
        this.UIElement.addUI(this.UIElement.UIInput);

        // button
        this.UIElement.UIButton = new UIButton().setUIClass('ghost-line').setUIId('imageUploadPopUp')
            .onUIClick((e) => {
                console.log(e);
                console.log(this);
                aliceJs.thumbnail({
                    title: i18n.msg('image.label.popupTitle'),
                    targetId: this.getKeyId(),
                    type: 'image',
                    isThumbnailInfo: true,
                    isFilePrefix: true,
                    thumbnailDoubleClickUse: true,
                    selectedPath: this.value
                });
            });
        this.UIElement.UIButton.addUI(new UISpan().setUIClass('icon').addUIClass('icon-search'));
        this.UIElement.addUI(this.UIElement.UIButton);

        return this.UIElement;
    }
    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        console.log(this);
        console.log(e.target.id);
        console.log(e.target.value);

        //this.panel.update.call(this.panel, e.target.id, e.target.value);
    }
}