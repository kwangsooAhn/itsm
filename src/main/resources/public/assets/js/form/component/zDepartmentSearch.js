/**
 * Department Search Mixin
 *
 *
 * @author Mo Hyung Nan <hn.mo@brainz.co.kr>
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
import { UIDiv, UIInput } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10'
    },
    validation: {
        required: false // 필수값 여부
    }
};

Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const departmentSearchMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '${default}';
    },
    // component 엘리먼트 생성
    makeElement() {
        const defaultValues = this.value.split('|');
        const element = new UIDiv().setUIClass('z-element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInput = new UIInput()
            .setUIClass('z-input i-user-search text-ellipsis')
            .setUIId('departmentSearch' + this.id)
            .setUIValue((this.value === '${default}') ? '' : defaultValues[1])
            .setUIRequired(this.validationRequired)
            .setUIAttribute('data-department-search', (this.value === '${default}') ? '' : defaultValues[0])
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('oncontextmenu', 'return false;')
            .setUIAttribute('onkeypress', 'return false;')
            .setUIAttribute('onkeydown', 'return false;')
            .onUIClick(this.openDepartmentSearchModal.bind(this))
            .onUIChange(this.updateValue.bind(this));

        element.addUI(element.UIInput);

        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIInput.setUIReadOnly(true);
            this.UIElement.UIComponent.UIElement.UIInput.setUICSSText('pointer-events:none');
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off') : '';
        }
    },
    // set, get
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(this.labelPosition));
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
        if (boolean) {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
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
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();

        // 사용자 검색 결과로 들어간 내용이 있는지 this.value와 'data-department-search' 확인하고 값을 저장한다.
        const departmentSearchData = e.target.getAttribute('data-department-search');

        // 값이 입력되었을 경우 error 없애기
        if (zValidation.isRequired(departmentSearchData)) {
            zValidation.removeDOMElementError(e.target);
        }

        this.value = `${departmentSearchData}|${e.target.value}`;
    },

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(e) {
        if (e && e.preventDefault) {
            e.preventDefault();
        }
        // 기존 값 초기화
        this.defaultValue = '';
        const curCriteria = this.UIElement.UISelect.domElement.value;
        let curTarget = [];
        switch (curCriteria) {
            case 'organization':
                curTarget.push({
                    value: this.UIElement.UIGroup.UIInputButton?.UIInput.getUIValue(),
                    id: this.UIElement.UIGroup.UIInputButton?.UIInput.getUIAttribute('data-value') || '',
                });
                break;
            case 'custom':
                this.UIElement.UIGroup.userTable?.rows.forEach((row) => {
                    const dataCell = row.cells[0].domElement;
                    if (row.domElement.rowIndex > 0 && dataCell.hasAttribute('data-value')) {
                        curTarget.push({
                            value: dataCell.textContent,
                            id: dataCell.getAttribute('data-value'),
                        });
                    }
                });
                break;
        }

        const dataObj = {};
        dataObj['targetCriteria'] = curCriteria;
        dataObj['searchKey'] = curTarget;

        this.panel.update.call(this.panel, this.key, JSON.stringify(dataObj));
    },

    // 사용자 선택 모달
    openDepartmentSearchModal(e) {
        e.stopPropagation();

        const selectedValue = this.UIElement.UIGroup.UIInputButton.UIInput.getUIAttribute('data-department-search');
        tree.load({
            view: 'modal',
            title: i18n.msg('department.label.deptList'),
            dataUrl: '/rest/organizations',
            target: 'treeList',
            source: 'organization',
            text: 'organizationName',
            nodeNameLabel: i18n.msg('common.msg.dataSelect', i18n.msg('department.label.deptName')),
            defaultIcon: '/assets/media/icons/tree/icon_tree_groups.svg',
            leafIcon: '/assets/media/icons/tree/icon_tree_group.svg',
            selectedValue: selectedValue,
            callbackFunc: (response) => {
                this.UIElement.UIGroup.UIInputButton.UIInput.setUIValue(response.textContent);
                this.UIElement.UIGroup.UIInputButton.UIInput.setUIAttribute('data-department-search', response.id);

                this.updateProperty.call(this, e);
            }
        });
    },

    // 세부 속성 조회
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
