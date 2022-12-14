/**
 * Image Mixin
 *
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZFileProperty from '../../formDesigner/property/type/zFileProperty.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchButtonProperty from '../../formDesigner/property/type/zSwitchButtonProperty.js';
import { UNIT } from '../../lib/zConstants.js';
import { UIDiv, UIImg, UISpan, UIText } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        path: '', // 이미지 경로
        width: '', // 이미지 너비
        height: '', // 이미지 높이
        align: 'left'
    },
    validation: {
        required: false // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const imageMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
        
        // 파일 조회 타입
        this.fileType = 'image';
        this.fileSeparator = '/';
        this.basePath = '';

    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('element')
            .setUIProperty('--data-column', this.elementColumnWidth)
            .setUITextAlign(this.elementAlign);

        element.UIImg = new UIImg().setUIClass('imagebox').setUIId('imagebox' + this.id)
            .setUIAttribute('data-path', aliceJs.filterXSS(this.elementPath))
            .setUIWidth(this.elementWidth + UNIT.PX)
            .setUIHeight(this.elementHeight + UNIT.PX);
        element.addUI(element.UIImg);

        // placeholder
        element.UIDiv = new UIDiv().setUIClass('imagebox-placeholder')
            .addUI(new UISpan().setUIClass('ic-no-image'))
            .addUI(new UIText().addUIClass('mt-2').setUIInnerHTML(i18n.msg('resource.label.placeholder')));
        element.addUI(element.UIDiv);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 기본경로 조회
        aliceJs.fetchText('/rest/resources/basePath?type=' + this.fileType, {
            method: 'GET',
        }).then((response) => {
            this.basePath = response;
            // path 추가
            this.setImageSrc(this.basePath + this.fileSeparator + this.elementPath);
        });
    },
    // 이미지를 조회하여 경로 표시
    setImageSrc(path) {
        if (!zValidation.isEmpty(path)) {
            const urlParam = 'type=' + this.fileType + '&path=' + encodeURIComponent(path);
            aliceJs.fetchJson('/rest/resources/file?' + urlParam, {
                method: 'GET'
            }).then((response) => {
                if (response.status === aliceJs.response.success && !zValidation.isEmpty(response.data)) {
                    this.UIElement.UIComponent.UIElement.UIImg.setUISrc(
                        'data:image/' + response.data.extension + ';base64,' + response.data.data);
                }
            });
        } else {
            this.UIElement.UIComponent.UIElement.UIImg.setUISrc('');
        }
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

        this.setImageSrc(this.basePath + this.fileSeparator + path);
    },
    get elementPath() {
        return this._element.path;
    },
    set elementWidth(width) {
        this._element.width = width;
        this.UIElement.UIComponent.UIElement.UIImg.setUIWidth(width + UNIT.PX);
    },
    get elementWidth() {
        return this._element.width;
    },
    set elementHeight(height) {
        this._element.height = height;
        this.UIElement.UIComponent.UIElement.UIImg.setUIHeight(height + UNIT.PX);
    },
    get elementHeight() {
        return this._element.height;
    },
    set elementAlign(align) {
        this._element.align = align;
        this.UIElement.UIComponent.UIElement.setUITextAlign(align);
    },
    get elementAlign() {
        return this._element.align;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UIInputbox.setUIAttribute('data-validation-required', boolean);
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
    // 세부 속성 조회
    getProperty() {
        const elementWidthProperty = new ZInputBoxProperty('elementWidth', 'element.width', this.elementWidth)
            .setValidation(false, 'number', '0', '', '', '');
        elementWidthProperty.unit = UNIT.PX;

        const elementHeightProperty = new ZInputBoxProperty('elementHeight', 'element.height', this.elementHeight)
            .setValidation(false, 'number', '0', '', '', '');
        elementHeightProperty.unit = UNIT.PX;

        // label - align
        const elementAlignProperty = new ZSwitchButtonProperty('elementAlign', 'element.align', this.elementAlign, [
            { 'name': 'ic-align-left', 'value': 'left' },
            { 'name': 'ic-align-center', 'value': 'center' },
            { 'name': 'ic-align-right', 'value': 'right' }
        ]);
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZFileProperty('elementPath', 'element.path', this.elementPath, this.fileType))
                .addProperty(elementWidthProperty)
                .addProperty(elementHeightProperty)
                .addProperty(elementAlignProperty)
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
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        return true;
    }
};
