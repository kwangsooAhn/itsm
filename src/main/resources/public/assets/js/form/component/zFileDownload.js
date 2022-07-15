/**
 * File Upload Mixin
 *
 *
 * @author jy.lim <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZFileProperty from '../../formDesigner/property/type/zFileProperty.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import { UIDiv, UISpan } from '../../lib/zUI.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        path: '', // 이미지 경로
        text: 'DOWNLOAD', // 표시 텍스트
    },
    validation: {
        required: false // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const fileDownloadMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('z-element')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIFileDownload = new UIDiv().setUIClass('z-fileDownload').addUIClass('flex-row');
        element.UIFileDownload.addUI(new UISpan().setUIClass('z-icon').addUIClass('i-download').addUIClass('mr-3'));

        element.UIFileDownload.UIText = new UISpan().setUIClass('z-file-text').setUIInnerHTML(this.elementText);
        element.UIFileDownload.addUI(element.UIFileDownload.UIText);

        element.addUI(element.UIFileDownload);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        this.UIElement.UIComponent.UIElement.UIFileDownload.onUIClick(this.downLoadFile.bind(this));
    },
    // set, get
    set element(element) {
        this._element = element;
    },
    get element() {
        return this._element;
    },
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column',
            this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    set elementPath(path) {
        this._element.path = path;
    },
    get elementPath() {
        return this._element.path;
    },
    set elementText(text) {
        this._element.text = text;
        this.UIElement.UIComponent.UIElement.UIFileDownload.UIText.setUIInnerHTML(text);
    },
    get elementText() {
        return this._element.text;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UIFileUpload.setUIAttribute('data-validation-required', boolean);
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('none').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none');
        }
    },
    get validationRequired() {
        return this._validation.required;
    },
    set value(value) {
        this._value = value;
    },
    get value() {
        return this._value;
    },
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZFileProperty('elementPath', 'element.path', this.elementPath, 'file'))
                .addProperty(new ZInputBoxProperty('elementText', 'element.text', this.elementText))
        ];
    },
    // json 데이터 추출 (서버에 전달되는 json 데이터)
    toJson() {
        return {
            id: this._id,
            type: this._type,
            display: this._display,
            isTopic: this._isTopic,
            mapId: this._mapId,
            tags: this._tags,
            value: this._value,
            label: this._label,
            element: this._element,
            validation: this._validation
        };
    },
    downLoadFile() {
        if (this.elementPath.startsWith('file:///')) {
            const fileName = this.elementPath.split('file:///')[1];
            aliceJs.fetchBlob('/rest/files/download?fileName=' + encodeURIComponent(fileName), {
                method: 'GET',
                showProgressbar: true
            }).then(blob => {
                if (typeof blob === 'object') {
                    const a = document.createElement('a');
                    const url = window.URL.createObjectURL(blob);
                    a.href = url;
                    a.download = fileName;
                    document.body.append(a);
                    a.click();
                    a.remove();
                    window.URL.revokeObjectURL(url);
                } else {
                    zAlert.warning(i18n.msg('file.msg.noAttachFile'));
                }
            }).catch(err => {
                zAlert.warning(err);
            });
        }
    },
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        return true;
    }
};
