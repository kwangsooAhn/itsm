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
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv } from '../../lib/zUI.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        align: 'left',
    },
    validation: {
        required: false // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const fileUploadMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('element')
            .setUIProperty('--data-column', this.elementColumnWidth);

        element.UIFileUpload = new UIDiv().setUIClass('fileupload')
            .addUIClass('flex-column')
            .addUIClass('file-uploader--edit')
            .setUIId('fileupload' + this.id)
            .setUIAttribute('data-validation-required', this.validationRequired);

        element.UIFileUpload.dropZoneFiles = new UIDiv().setUIId('dropZoneFiles-' + this.id);
        element.UIFileUpload.addUI(element.UIFileUpload.dropZoneFiles);

        element.UIFileUpload.dropZoneUploadedFiles = new UIDiv().setUIId('dropZoneUploadedFiles-' + this.id);
        element.UIFileUpload.addUI(element.UIFileUpload.dropZoneUploadedFiles);

        element.addUI(element.UIFileUpload);
        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        let fileOptions = {
            formId: 'frm',
            ownId: '',
            dropZoneFilesId: 'dropZoneFiles-' + this.id,
            dropZoneUploadedFilesId: 'dropZoneUploadedFiles-' + this.id,
            clickable: false,
            editor: true,
            isView: false,
            isForm: true,
            fileDataIds: this.value,
            userCallback: this.updateValue.bind(this) // 파일업로드, 파일삭제시 호출되는 callback 함수
        };
        // 신청서 양식 편집 화면에 따른 처리
        // 읽기 전용일 경우
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.clearUI();
            const UIViewFileUpload = new UIDiv().setUIClass('file-uploader--view')
                .setUIId('fileupload' + this.id);
            // 파일 목록
            UIViewFileUpload.UIFileList = new UIDiv().setUIClass('file-uploader--view__list');
            UIViewFileUpload.addUI(UIViewFileUpload.UIFileList);
            // dropzone
            UIViewFileUpload.UIFileList.dropZoneFiles = new UIDiv().setUIId('dropZoneFiles-' + this.id);
            UIViewFileUpload.UIFileList.addUI(UIViewFileUpload.UIFileList.dropZoneFiles);

            UIViewFileUpload.UIFileList.dropZoneUploadedFiles = new UIDiv().setUIId('dropZoneUploadedFiles-' + this.id);
            UIViewFileUpload.UIFileList.addUI(UIViewFileUpload.UIFileList.dropZoneUploadedFiles);

            this.UIElement.UIComponent.UIElement.UIFileUpload = UIViewFileUpload;
            this.UIElement.UIComponent.UIElement.addUI(UIViewFileUpload);

            fileOptions.editor = false;
            fileOptions.isView = true;
        }
        // 미리보기 시 dropzone 중복을 방지하기 위해 id 재구성
        let validateElem = document.querySelectorAll('#dropZoneFiles-' + this.id);
        if (validateElem.length > 1) {
            let docDropZoneId = 'dropZoneFiles-' + this.id + '-document';
            validateElem.item(1).setAttribute('id', docDropZoneId);
            fileOptions.dropZoneFilesId = docDropZoneId;
        }
        zFileUploader.init(fileOptions);
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
    updateValue() {
        const tempValue = [];
        const inputElements = this.UIElement.UIComponent.UIElement.UIFileUpload.domElement.getElementsByTagName('input');
        for (let i = 0; i < inputElements.length; i++) {
            if (inputElements[i].name !== 'delFileSeq') {
                tempValue.push(inputElements[i].value);
            }
        }
        this.value = tempValue.join();
    },
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth)),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
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
