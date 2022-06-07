/**
 * CI(Configuration Item) Mixin
 *
 *
 * @author
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
import { CI, FORM, UNIT } from '../../lib/zConstants.js';
import { ZSession } from '../../lib/zSession.js';
import { UIButton, UICell, UIDiv, UIImg, UIInput, UIRow, UISpan, UITable } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        isEditable: false
    },
    validation: {
        required: false // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const ciMixin = {

    // 전달 받은 데이터와 기본 property meurge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '';
        // CI 데이터 초기화
        if (!zValidation.isEmpty(this._value)) {
            this._value = JSON.parse(this._value);
        }
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('z-element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);
        // 버튼 목록
        element.UIButtonGroup = this.makeCIButton();
        element.addUI(element.UIButtonGroup);
        // 테이블
        element.UITable = this.makeCITable();
        element.addUI(element.UITable);

        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            // 모든 버튼을 disabled 처리
            this.UIElement.UIComponent.UIElement.domElement.querySelectorAll('button').forEach((elem) => {
                elem.disabled = !elem.querySelector('span.i-search');
            });
            // 테이블의 상단 여백 제거
            this.UIElement.UIComponent.UIElement.UITable.removeUIClass('mt-2');
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off') : '';
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
    set elementIsEditable(boolean) {
        this._element.isEditable = boolean;

        this.UIElement.UIComponent.UIElement.clearUI();
        // 버튼 목록
        this.UIElement.UIComponent.UIElement.UIButtonGroup = this.makeCIButton();
        this.UIElement.UIComponent.UIElement.addUI(this.UIElement.UIComponent.UIElement.UIButtonGroup);
        // 테이블
        this.UIElement.UIComponent.UIElement.UITable = this.makeCITable();
        this.UIElement.UIComponent.UIElement.addUI(this.UIElement.UIComponent.UIElement.UITable);
    },
    get elementIsEditable() {
        return this._element.isEditable;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
        this.UIElement.UIComponent.UIElement.UITable.setUIAttribute('data-validation-required', boolean);
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
    makeCIButton() {
        const buttonGroup = new UIDiv().setUIClass('z-button-list');
        if (this.elementIsEditable) {
            // 등록
            const registerButton = new UIButton(i18n.msg('form.component.ci') + ' ' + i18n.msg('cmdb.ci.label.new'))
                .addUIClass('secondary')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.REGISTER)
                .onUIClick(this.openRegisterModal.bind(this));
            buttonGroup.addUI(registerButton);

            // 수정
            const updateButton = new UIButton(i18n.msg('cmdb.ci.label.update'))
                .addUIClass('secondary')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.MODIFY)
                .onUIClick(this.openSelectModal.bind(this));
            buttonGroup.addUI(updateButton);

            // 삭제
            const deleteButton = new UIButton(i18n.msg('cmdb.ci.label.delete'))
                .addUIClass('secondary')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.DELETE)
                .onUIClick(this.openSelectModal.bind(this));
            buttonGroup.addUI(deleteButton);
        } else {
            // 조회
            const selectButton = new UIButton(i18n.msg('cmdb.ci.label.select'))
                .addUIClass('secondary')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.READ)
                .onUIClick(this.openSelectModal.bind(this));
            buttonGroup.addUI(selectButton);
        }

        return buttonGroup;
    },
    makeCITable() {
        // 테이블
        const table = new UITable()
            .setUIClass('z-option-table')
            .addUIClass('z-ci-table')
            .addUIClass('mt-2')
            .setUIId('ciTable' + this.id)
            .setUIAttribute('tabindex', '-1')
            .setUIAttribute('data-validation-required', this.validationRequired);

        // 테이블 제목
        const row = new UIRow(table).setUIClass('z-option-table-header').addUIClass('z-ci-table-header');
        table.addUIRow(row);

        this.getCITableData().forEach((option) => {
            const tdWidth = (Number(option.columnWidth) / FORM.COLUMN) * 100;
            const tdClassName = (option.type === 'hidden' ? '' : 'on') + ' ' + option.class;
            const td = new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%;`)
                .setUITextContent((option.name !== '' ? i18n.msg(option.name) : ''));
            row.addUICell(td);
        });

        if (Array.isArray(this.value) && this.value.length > 0) {
            this.value.forEach((CIData) => {
                this.addCITableRow(table, CIData);
            });
        } else {
            this.setEmptyCITable(table);
        }
        return table;
    },
    getCITableData() {
        if (this.elementIsEditable) {
            // CI 컴포넌트 편집 가능여부가 true 일때 = 구분, CI 아이콘, CI Type, CI 이름, CI 설명, 편집 아이콘,  row 삭제 아이콘  7개
            return [
                {id: 'actionType', type: 'readonly', columnWidth: '1', name: 'form.label.actionType', class: 'align-left first-column'},
                {id: 'ciId', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'ciNo', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'ciIcon', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'ciIconData', type: 'image', columnWidth: '1', name: '', class: 'align-left'},
                {id: 'typeId', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'typeName', type: 'editable', columnWidth: '3', name: 'cmdb.ci.label.type', class: 'align-left'},
                {id: 'ciName', type: 'editable', columnWidth: '3', name: 'cmdb.ci.label.name', class: 'align-left'},
                {id: 'ciDesc', type: 'editable', columnWidth: '4', name: 'cmdb.ci.label.description', class: 'align-left'},
                {id: 'classId', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'editIcon', type: 'icon-edit', columnWidth: '1', name: '', class: 'align-center'},
                {id: 'deleteIcon', type: 'icon-delete', columnWidth: '1', name: '', class: 'align-center last-column'},
                {id: 'interlink', type: 'hidden', columnWidth: '0', name: '', class: ''}
            ];
        } else {
            // CI 컴포넌트 편집 가능여부가 false 일때 =  CI 아이콘, CI Type , CI 이름, 세부 정보 조회 아이콘, row 삭제 아이콘  5개
            return [
                {id: 'actionType', type: 'hidden', columnWidth: '0', name: 'form.label.actionType', class: ''},
                {id: 'ciId', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'ciNo', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'ciIcon', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'ciIconData', type: 'image', columnWidth: '1', name: '', class: 'align-left  first-column'},
                {id: 'typeId', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'typeName', type: 'editable', columnWidth: '3', name: 'cmdb.ci.label.type', class: 'align-left'},
                {id: 'ciName', type: 'editable', columnWidth: '4', name: 'cmdb.ci.label.name', class: 'align-left'},
                {id: 'ciDesc', type: 'editable', columnWidth: '4', name: 'cmdb.ci.label.description', class: 'align-left'},
                {id: 'classId', type: 'hidden', columnWidth: '0', name: '', class: ''},
                {id: 'searchIcon', type: 'icon-search', columnWidth: '1', name: '', class: 'align-center'},
                {id: 'deleteIcon', type: 'icon-delete', columnWidth: '1', name: '', class: 'align-center last-column'},
                {id: 'interlink', type: 'hidden', columnWidth: '0', name: '', class: ''}
            ];
        }
    },
    // CI 테이블 각 cell 반환
    getCITableDataToCell(row, option, data) {
        const tdWidth = (Number(option.columnWidth) / FORM.COLUMN) * 100;
        const tdClassName = (option.type === 'hidden' ? '' : 'on ') + option.class;

        switch (option.type) {
            case 'editable':
                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%;`)
                    .setUITextContent((typeof data[option.id] !== 'undefined') ? data[option.id] : '');
            case 'readonly':
                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%;`)
                    .setUITextContent(i18n.msg('cmdb.ci.actionType.' + data.actionType));
            case 'image':
                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%;`)
                    .addUI(new UIImg().setUISrc(data[option.id]).setUIWidth('20' + UNIT.PX).setUIHeight('20' + UNIT.PX));
            case 'icon-edit': // CI 등록 / 수정
                if (data.actionType === CI.ACTION_TYPE.DELETE) {
                    const viewButton = new UIButton()
                        .setUIClass('z-button-icon')
                        .addUIClass('extra')
                        .setUIAttribute('data-type', data.actionType)
                        .onUIClick(this.openViewModal.bind(this, data.ciId))
                        .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-search'));

                    return new UICell(row).setUIClass(tdClassName)
                        .setUICSSText(`width:${tdWidth}%;`)
                        .addUI(viewButton);
                } else {
                    const editButton = new UIButton()
                        .setUIClass('z-button-icon')
                        .addUIClass('extra')
                        .setUIAttribute('data-type', data.actionType)
                        .onUIClick(this.openUpdateModal.bind(this, row.getUIIndex(), data))
                        .addUI(new UISpan().setUIClass('z-icon').addUIClass(
                            (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) ? 'i-edit' : 'i-search')
                        );

                    return new UICell(row).setUIClass(tdClassName)
                        .setUICSSText(`width:${tdWidth}%;`)
                        .addUI(editButton);
                }
            case 'icon-search': // CI 상세 조회
                const searchButton = new UIButton()
                    .setUIClass('z-button-icon')
                    .addUIClass('extra')
                    .setUIAttribute('data-type', data.actionType)
                    .onUIClick(this.openViewModal.bind(this, data.ciId))
                    .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-search'));

                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%;`)
                    .addUI(searchButton);
            case 'icon-delete': // Row 삭제
                const deleteButton = new UIButton()
                    .setUIClass('z-button-icon')
                    .addUIClass('extra')
                    .setUIAttribute('data-type', data.actionType)
                    .onUIClick(this.removeCITableRow.bind(this, row.parent, -1, data))
                    .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-delete'));

                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%;`)
                    .addUI(deleteButton);
            default: // hidden
                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%;`)
                    .addUI(new UIInput(data[option.id]).setUIType('hidden'));
        }
    },
    // CI 테이블 row 추가
    addCITableRow(targetTable, data) {
        // 데이터가 없을 경우
        if (targetTable.rows.length === 2 && targetTable.rows[1].hasUIClass('no-data-found-list')) {
            this.removeCITableRow(targetTable, 1);
        }
        // row 추가
        const row = new UIRow(targetTable).setUIClass('z-ci-table-row');
        targetTable.addUIRow(row);
        // td 추가
        this.getCITableData().forEach((option) => {
            row.addUICell(this.getCITableDataToCell(row, option, data));
        });
    },
    // CI 테이블 row 변경
    updateCITableRow(targetTable, rowIndex, data) {
        // row 추가
        const row = new UIRow(targetTable).setUIClass('z-ci-table-row');
        targetTable.updateUIRow(rowIndex, row);
        // td 추가
        this.getCITableData().forEach((option) => {
            row.addUICell(this.getCITableDataToCell(row, option, data));
        });

    },
    // CI 테이블 row 삭제
    removeCITableRow(targetTable, rowIndex, ciData) {
        if (typeof this.value !== 'string' && rowIndex === -1) {
            const findIndex = this.value.findIndex(function (item) {
                return item.ciId === ciData.ciId;
            });
            rowIndex = findIndex + 1;
        }
        if (!zValidation.isEmpty(ciData)) {
            const alertMsg = (ciData.actionType === CI.ACTION_TYPE.REGISTER || ciData.actionType === CI.ACTION_TYPE.MODIFY) ?
                'cmdb.ci.msg.deleteEditableCI' : 'cmdb.ci.msg.deleteReadableCI';
            zAlert.confirm(i18n.msg(alertMsg), () => {
                if (ciData.actionType === CI.ACTION_TYPE.REGISTER || ciData.actionType === CI.ACTION_TYPE.MODIFY) {
                    // action 타입이 Register, Modify 일 경우, wf_component_ci_data 테이블에 데이터 삭제
                    aliceJs.fetchJson('/rest/cmdb/cis/data?ciId=' + ciData.ciId + '&componentId=' + this.id, {
                        method: 'DELETE'
                    }).then((response) => {
                        switch (response.status) {
                            case aliceJs.response.success:
                                targetTable.removeUIRow(targetTable.rows[rowIndex]);
                                const newValue = JSON.parse(JSON.stringify(this.value));
                                newValue.splice(rowIndex - 1, 1);
                                this.value = newValue;
                                // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
                                if (Array.isArray(this.value) && !this.value.length) {
                                    this.setEmptyCITable(targetTable);
                                }
                                break;
                            case aliceJs.response.error:
                                zAlert.danger(i18n.msg('cmdb.ci.msg.failedDeleteCI'));
                                break;
                            default :
                                break;
                        }
                    });
                } else {
                    targetTable.removeUIRow(targetTable.rows[rowIndex]);
                    const newValue = JSON.parse(JSON.stringify(this.value));
                    newValue.splice(rowIndex - 1, 1);
                    this.value = newValue;

                    // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
                    if (Array.isArray(this.value) && !this.value.length) {
                        this.setEmptyCITable(targetTable);
                    }
                }
            });
        } else {
            targetTable.removeUIRow(targetTable.rows[rowIndex]);
        }
    },
    // 데이터가 없을때
    setEmptyCITable(targetTable) {
        const row = new UIRow(targetTable).setUIClass('no-data-found-list');
        targetTable.addUIRow(row);

        const td = new UICell(row).setUIClass('on align-center first-column last-column')
            .setColspan(12)
            .setUITextContent(i18n.msg('common.msg.noData'));
        row.addUICell(td);
    },
    /**
     * 신규 CI 저장 데이터 조회
     * @param actionType 등록|수정 인지
     */
    getRegisterCIData(actionType) {
        const ciKeys = ['ciId', 'ciNo', 'ciIcon', 'ciIconData', 'typeId', 'typeName', 'ciName', 'ciDesc', 'classId', 'interlink'];
        const ciData = {ciStatus: CI.STATUS.USE, actionType: actionType};
        ciKeys.forEach((key) => {
            const domElement = document.getElementById(key);
            if (zValidation.isDefined(domElement)) {
                ciData[key] = domElement.value;
            }
        });
        return ciData;
    },
    // CI 저장 - 서버
    saveCIData(data, callbackFunc) {
        const instanceIdElem = document.getElementById('instanceId');
        const saveData = {
            ciId: data.ciId,
            componentId: this.id,
            values: {ciAttributes: [], ciTags: [], relatedCIData: []},
            instanceId: instanceIdElem.value
        };
        document.querySelectorAll('.attribute').forEach((el) => {
            const attributeType = el.getAttribute('data-attributeType');
            let ciAttribute = this.getCIDataForType(attributeType, el);
            if (Object.keys(ciAttribute).length !== 0) {
                saveData.values.ciAttributes.push(ciAttribute);
            }
        });
        document.querySelectorAll('.z-relation-data').forEach((el) => {
            let data = {};
            data.targetCIIconData = el.childNodes[0].childNodes[0].src;
            data.targetCIIcon = el.childNodes[0].childNodes[1].value;
            data.targetTypeName = el.childNodes[1].textContent;
            data.targetCIName = el.childNodes[2].childNodes[0].textContent;
            data.targetCIId = el.childNodes[2].childNodes[1].value;
            saveData.values.relatedCIData.push(data);
        });
        for (let i = 0; i < saveData.values.relatedCIData.length; i++) {
            for (let k = 0; k < i; k++) {
                if (saveData.values.relatedCIData[i].targetCIId == saveData.values.relatedCIData[k].targetCIId) {
                    zAlert.warning(i18n.msg('cmdb.ci.msg.duplicateRelatedCIData'));
                    return;
                }
            }
        }
        if (!zValidation.isEmpty(document.getElementById('ciTags').value)) {
            const ciTags = JSON.parse(document.getElementById('ciTags').value);
            console.log('저장된 내용을 불러옵니다.')
            console.log(ciTags)
            ciTags.forEach((tag) => {
                saveData.values.ciTags.push({'id': data.ciId, 'value': tag.value});
            });
        }
        aliceJs.fetchJson('/rest/cmdb/cis/' + data.ciId + '/data', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData)
        }).then((response) => {
            switch (response.status) {
                case aliceJs.response.success:
                    zAlert.success(i18n.msg('common.msg.save'), () => {
                        if (typeof callbackFunc === 'function') {
                            callbackFunc();
                        }
                    });
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default :
                    break;
            }
        });
    },
    // 타입에 따른 엘리먼트 데이터 조회
    getCIDataForType(type, el) {
        let rtn = {};
        switch (type) {
            case 'inputbox':
                const inputElem = el.querySelector('input');
                rtn.id = inputElem.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = inputElem.value;
                break;
            case 'dropdown':
                const selectElem = el.querySelector('select');
                rtn.id = selectElem.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = selectElem.value;
                break;
            case 'radio':
                const radioElem = el.querySelector('input[name="attribute-radio"]:checked');
                if (radioElem !== null) {
                    rtn.id = radioElem.getAttribute('data-attributeId');
                    rtn.type = type;
                    rtn.value = radioElem.value;
                }
                break;
            case 'checkbox':
                let checkValues = [];
                el.querySelectorAll('input[name="attribute-checkbox"]').forEach(function (chkElem, idx) {
                    if (idx === 0) {
                        rtn.id = chkElem.getAttribute('data-attributeId');
                        rtn.type = type;
                    }
                    if (chkElem.checked) {
                        checkValues.push(chkElem.value);
                    }
                });
                rtn.value = checkValues.join();
                break;
            case 'custom-code':
                const customElem = el.querySelector('input');
                rtn.id = customElem.parentNode.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = customElem.getAttribute('data-custom-data');
                break;
            case 'group-list':
                const groupElem = el.querySelector('.child-attribute-group');
                rtn.id = groupElem.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = '';
                rtn.childAttributes = [];
                groupElem.querySelectorAll('.child-attribute-row').forEach((rowElem, rowIdx) => {
                    rowElem.querySelectorAll('.child-attribute').forEach((childElem) => {
                        const childElemAttributeType = childElem.getAttribute('data-attributeType');
                        let childAttribute = this.getCIDataForType(childElemAttributeType, childElem);
                        childAttribute.seq = rowIdx;
                        rtn.childAttributes.push(childAttribute);
                    });
                });
                break;
            case 'date':
                const dateElem = el.querySelector('input');
                rtn.id = dateElem.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = i18n.systemDate(dateElem.value);
                break;
            case 'datetime':
                const dateTimeElem = el.querySelector('input');
                rtn.id = dateTimeElem.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = i18n.systemDateTime(dateTimeElem.value);
                break;
            case 'userSearch':
                const userSearchElem = el.querySelector('input');
                rtn.id = userSearchElem.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = `${userSearchElem.getAttribute('data-user-search')}|` +
                    `${userSearchElem.value}|${userSearchElem.getAttribute('data-user-id')}`;
                break;
            case 'organizationSearch':
                const organizationSearchElem = el.querySelector('input');
                rtn.id = organizationSearchElem.getAttribute('data-attributeId');
                rtn.type = type;
                rtn.value = `${organizationSearchElem.getAttribute('data-organization-search')}|` +
                    `${organizationSearchElem.value}`;
                break;
            default:
                break;
        }
        return rtn;
    },
    // 신규 CI 등록 모달
    openRegisterModal() {
        aliceJs.fetchText('/cmdb/cis/component/new', {
            method: 'GET'
        }).then((htmlData) => {
            const registerModal = new modal({
                title: i18n.msg('cmdb.ci.label.register'),
                body: htmlData,
                classes: 'cmdb-ci-register-modal',
                buttons: [{
                    content: i18n.msg('common.btn.register'),
                    classes: 'z-button primary',
                    bindKey: false,
                    callback: (modal) => {
                        // TODO: 유효성 검증 - CI 리팩토링 후 zValidation.js 모듈 사용하도록 처리
                        if (!isValidRequiredAll(modal) || !hasErrorClass()) {
                            return false;
                        }
                        // CI 저장 데이터 조회
                        const ciData = this.getRegisterCIData(CI.ACTION_TYPE.REGISTER);
                        // 테이블 row 추가
                        this.addCITableRow(this.UIElement.UIComponent.UIElement.UITable, ciData);
                        // value 추가
                        if (zValidation.isEmpty(this._value)) {
                            this.value = [];
                        }
                        const newValue = JSON.parse(JSON.stringify(this.value));
                        newValue.push(ciData);
                        this.value = newValue;

                        // 서버 저장
                        this.saveCIData(ciData, function () {
                            modal.hide();
                        });
                    }
                }, {
                    content: i18n.msg('common.btn.cancel'),
                    classes: 'z-button secondary',
                    bindKey: false,
                    callback: (modal) => {
                        zAlert.confirm(i18n.msg('cmdb.ci.msg.deleteInformation'), function () {
                            modal.hide();
                        });
                    }
                }],
                close: {
                    closable: false,
                },
                onCreate: (modal) => {
                    let param = {};
                    const newCIId = ZWorkflowUtil.generateUUID();
                    document.getElementById('ciId').value = newCIId;
                    // CI 타입 선택 모달 바인딩
                    const ciTypeId = document.getElementById('typeId').value;
                    document.getElementById('typeSelectBtn').addEventListener('click', this.openSelectTypeModal.bind(this, ciTypeId));
                    document.getElementById('editCIRelationBtn').addEventListener('click', this.openRelationSelectModal.bind(this, newCIId));
                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.cmdb-ci-content-edit'), {className: 'scrollbar'});
                    OverlayScrollbars(document.querySelectorAll('textarea'), {
                        className: 'scrollbar',
                        resize: 'vertical',
                        sizeAutoCapable: true,
                        textarea: {
                            dynHeight: false,
                            dynWidth: false,
                            inheritedAttrs: 'class'
                        }
                    });
                    // 태그 초기화
                    new zTag(document.getElementById('ciTags'), {
                        suggestion: true,
                        realtime: true,
                        tagType: 'ci',
                        targetId: newCIId
                    });
                }
            });
            registerModal.show();
        });
    },
    // 기존 CI 변경 모달
    openUpdateModal(rowIndex, data) {
        const instanceIdElem = document.getElementById('instanceId');
        const urlParam = 'ciId=' + data.ciId + '&componentId=' + this.id + '&instanceId=' + instanceIdElem.value +
            '&displayType=' + this.displayType + '&interlink=' + data.interlink;
        aliceJs.fetchText('/cmdb/cis/component/edit?' + urlParam, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then((htmlData) => {
            // 버튼 생성
            const modalButtons = [];
            let modalTitle = (data.actionType === CI.ACTION_TYPE.MODIFY) ? 'cmdb.ci.label.update' :
                'cmdb.ci.label.register';
            if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
                // 수정
                modalButtons.push({
                    content: i18n.msg('common.btn.modify'),
                    classes: 'z-button primary',
                    bindKey: false,
                    callback: (modal) => {
                        // TODO: 유효성 검증 - CI 리팩토링 후 zValidation.js 모듈 사용하도록 처리
                        if (!isValidRequiredAll(modal) || !hasErrorClass()) {
                            return false;
                        }
                        // CI 저장 데이터 조회
                        const ciData = this.getRegisterCIData(data.actionType);
                        // 테이블 row 변경
                        this.updateCITableRow(this.UIElement.UIComponent.UIElement.UITable, rowIndex, ciData);
                        // value 변경
                        const newValue = JSON.parse(JSON.stringify(this.value));
                        newValue[rowIndex - 1] = ciData;
                        this.value = newValue;

                        // 서버 저장
                        this.saveCIData(ciData, function () {
                            modal.hide();
                        });
                    }
                });
                // 취소
                modalButtons.push({
                    content: i18n.msg('common.btn.cancel'),
                    classes: 'z-button secondary',
                    bindKey: false,
                    callback: (modal) => {
                        zAlert.confirm(i18n.msg('cmdb.ci.msg.deleteInformation'), function () {
                            modal.hide();
                        });
                    }
                });
            } else {
                // 닫기
                modalButtons.push({
                    content: i18n.msg('common.btn.close'),
                    classes: 'z-button secondary',
                    bindKey: false,
                    callback: (modal) => modal.hide()
                });
                modalTitle = 'cmdb.ci.label.view';
            }
            const updateModal = new modal({
                title: i18n.msg(modalTitle),
                body: htmlData,
                classes: 'cmdb-ci-update-modal',
                buttons: modalButtons,
                close: { closable: false, },
                onCreate: async (modal) => {
                    let param = {};
                    // 수정된 데이터가 존재할 경우 수정 데이터로 변경
                    document.getElementById('ciAttributes').click();

                    // CI 타입 선택 모달 바인딩
                    const ciTypeId = document.getElementById('typeId').value;
                    document.getElementById('typeSelectBtn').addEventListener('click', this.openSelectTypeModal.bind(this, ciTypeId));

                    // 수정시, 타입 변경을 막음
                    if (data.actionType === CI.ACTION_TYPE.MODIFY) {
                        document.getElementById('typeSelectBtn').disabled = true;
                    }

                    aliceJs.fetchJson('/rest/cmdb/cis/' + data.ciId + '/relation?componentId=' + this.id +
                        '&instanceId=' + instanceIdElem.value, {
                        method: 'GET'
                    }).then((response) => {
                        if (response.status === aliceJs.response.success && !zValidation.isEmpty(response.data)) {
                            const ciRelation = response.data;
                            for (let i = 0; i < ciRelation.length; i++) {

                                this.addCIRelation(ciRelation[i], false);
                            }
                        }
                    });
                    if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
                        document.getElementById('editCIRelationBtn').addEventListener('click',
                            this.openRelationSelectModal.bind(this, data.ciId));
                        OverlayScrollbars(document.querySelector('.cmdb-ci-content-edit'), {className: 'scrollbar'});
                    } else {
                        OverlayScrollbars(document.querySelector('.cmdb-ci-content-view'), {className: 'scrollbar'});
                    }

                    OverlayScrollbars(document.querySelectorAll('textarea'), {
                        className: 'scrollbar',
                        resize: 'vertical',
                        sizeAutoCapable: true,
                        textarea: {
                            dynHeight: false,
                            dynWidth: false,
                            inheritedAttrs: 'class'
                        }
                    });

                }
            });
            updateModal.show();

            // 태그 초기화
            new zTag(document.getElementById('ciTags'), {
                suggestion: true,
                realtime: true,
                tagType: 'ci',
                targetId: data.ciId
            });
        });

    },
    // 기존 CI 조회 모달 Template 조회
    getSelectModalContent(flag, ciId) {
        return `<form id="searchFrm">` +
            `<input type="text" class="z-input i-search col-5 mr-2" name="searchValue" id="searchValue" maxlength="100" placeholder="${i18n.msg('cmdb.ci.label.searchPlaceholder')}"/>` +
            `<input type="text" class="z-input i-search col-3 mr-2" name="tagSearch" id="tagSearch" maxlength="100" placeholder="${i18n.msg('cmdb.ci.label.tagPlaceholder')}"/>` +
            `<input type="hidden" name="flag" id="flag" value="${flag}"/>` +
            `<input type="hidden" name="relationSearch" id="relationSearch" value="${(ciId !== null) ? ciId :''}"/>` +
            `<span id="ciListTotalCount" class="z-search-count"></span>` +
            `</form>` +
            `<div class="table-set" id="ciList"></div>`;
    },
    // 기존 CI 조회 모달
    openSelectModal(e) {
        const selectModal = new modal({
            title: i18n.msg('cmdb.ci.label.select'),
            body: this.getSelectModalContent('component'),
            classes: 'cmdb-ci-list-modal',
            buttons: [{
                content: i18n.msg('common.btn.check'),
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    // 체크된 CI 출력
                    let isChecked = false;
                    document.querySelectorAll('.cmdb-ci-list-modal input[type=checkbox]:not([disabled])').forEach((chkElem) => {
                        if (chkElem.checked) {
                            isChecked = true;
                            const actionType = e.target.getAttribute('data-actionType');
                            const ci = {
                                actionType: actionType,
                                ciStatus: (actionType === CI.ACTION_TYPE.DELETE) ? CI.STATUS.DELETE : CI.STATUS.USE
                            };
                            const ciTbCells = document.getElementById('ciRow' + chkElem.value).children;
                            Array.from(ciTbCells).forEach(function (cell) {
                                if (typeof cell.id !== 'undefined' && cell.id.trim() !== '') {
                                    ci[cell.id] = (cell.id === 'ciId') ? chkElem.value : cell.textContent;
                                }
                            });
                            this.addCITableRow(this.UIElement.UIComponent.UIElement.UITable, ci);
                            if (zValidation.isEmpty(this._value)) {
                                this.value = [];
                            }
                            this.value.push(ci);
                        }
                    });
                    if (isChecked) {
                        modal.hide();
                    } else {
                        zAlert.warning(i18n.msg('cmdb.ci.msg.selectCI'));
                    }
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: (modal) => {
                let this_ = this; //zTag에서 callback에서 this는 zCI가 아니고 zTag이기에 this를 컴포지션함.

                this.selectModalSearchCI();

                new zTag(document.getElementById('tagSearch'), {
                    suggestion: true,
                    realtime: true,
                    tagType: 'ci',
                    targetId: '',
                    callback: function () {
                        this_.selectModalSearchCI();
                    }
                });

                document.getElementById('searchValue').addEventListener('keyup', aliceJs.debounce ((e) => {
                    e.preventDefault();
                    this.selectModalSearchCI();
                }), false);
            }
        });
        selectModal.show();
    },
    // 기존 CI 조회 모달 검색
    selectModalSearchCI() {
        const urlParam = aliceJs.serialize(document.getElementById('searchFrm')) + '&isSearchType=true';
        aliceJs.fetchText('/cmdb/cis/component/list?' + urlParam, {
            method: 'GET'
        }).then((htmlData) => {
            document.getElementById('ciList').innerHTML = htmlData;
            // 카운트 변경
            aliceJs.showTotalCount(document.querySelectorAll('.ci-list').length, 'ciListTotalCount');
            // 태그 초기화
            let ciListTags = document.querySelectorAll('.cmdb-ci-list-modal input[name=ciListTags]');
            let ciIdList = (document.querySelectorAll('.cmdb-ci-list-modal input[type=checkbox]'));
            for (let i = 0; i < ciListTags.length; i++) {
                new zTag(ciListTags[i], {
                    suggestion: true,
                    realtime: true,
                    tagType: 'ci',
                    targetId: ciIdList[i].value
                });
            }
            // 스크롤바
            OverlayScrollbars(document.querySelector('.modal-content'), {className: 'scrollbar'});
            OverlayScrollbars(document.querySelector('#ciList .z-table-body'), {className: 'scrollbar'});

            // 이미 선택된 CI 들은 선택 불가능
            if (Array.isArray(this.value) && this.value.length > 0) {
                const ciChkElems = document.querySelectorAll('input[type=checkbox]');
                ciChkElems.forEach((chkElem) => {
                    this.value.forEach((CIData) => {
                        if (chkElem.value === CIData.ciId) {
                            chkElem.checked = true;
                            chkElem.disabled = true;
                        }
                    });
                });
            }
        });

    },
    // 연관 CI 선택 모달
    openRelationSelectModal(ciId) {
        const selectModal = new modal({
            title: i18n.msg('cmdb.ci.label.select'),
            body: this.getSelectModalContent('relation', ciId),
            classes: 'cmdb-ci-list-modal',
            buttons: [{
                content: i18n.msg('common.btn.check'),
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    // 기존 연관관계 row 초기화
                    const orgBody = document.getElementById('ciRelation');
                    while (orgBody.hasChildNodes()) {
                        orgBody.removeChild(orgBody.firstChild);
                    }
                    // 체크된 CI 출력
                    let isChecked = false;
                    document.querySelectorAll('.cmdb-ci-list-modal input[type=checkbox]:not([disabled])').forEach((chkElem) => {
                        if (chkElem.checked) {
                            isChecked = true;
                            const ci = {};
                            const ciTbCells = document.getElementById('ciRow' + chkElem.value).children;
                            Array.from(ciTbCells).forEach(function (cell) {
                                if (typeof cell.id !== 'undefined' && cell.id.trim() !== '') {
                                    ci[cell.id] = (cell.id === 'ciId') ? chkElem.value : cell.textContent;
                                }
                            });
                            this.addCIRelation(ci, isChecked);
                        }
                    });
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: (modal) => {
                let this_ = this; //zTag에서 callback에서 this는 zCI가 아니고 zTag이기에 this를 컴포지션함.

                this.relationSelectModalSearchCI();

                new zTag(document.getElementById('tagSearch'), {
                    suggestion: true,
                    realtime: true,
                    tagType: 'ci',
                    targetId: '',
                    callback: function () {
                        this_.relationSelectModalSearchCI();
                    }
                });
                document.getElementById('searchValue').addEventListener('keyup', aliceJs.debounce ((e) => {
                    e.preventDefault();
                    this.relationSelectModalSearchCI();
                }), false);
            }
        });
        selectModal.show();
    },
    // 연관 CI 조회 모달 검색
    relationSelectModalSearchCI() {
        const urlParam = aliceJs.serialize(document.getElementById('searchFrm'));
        aliceJs.fetchText('/cmdb/cis/component/list?' + urlParam, {
            method: 'GET'
        }).then((htmlData) => {
            document.getElementById('ciList').innerHTML = htmlData;
            // 카운트 변경
            aliceJs.showTotalCount(document.querySelectorAll('.ci-list').length, 'ciListTotalCount');
            // 태그 초기화
            let ciListTags = document.querySelectorAll('.cmdb-ci-list-modal input[name=ciListTags]');
            let ciIdList = (document.querySelectorAll('.cmdb-ci-list-modal input[type=checkbox]'));
            for (let i = 0; i < ciListTags.length; i++) {
                new zTag(ciListTags[i], {
                    suggestion: true,
                    realtime: true,
                    tagType: 'ci',
                    targetId: ciIdList[i].value
                });
            }
            // 스크롤바
            OverlayScrollbars(document.querySelector('.modal-content'), {className: 'scrollbar'});
            OverlayScrollbars(document.querySelector('#ciList .z-table-body'), {className: 'scrollbar'});

            // 연관 CI 정보 가져오기
            // ciRelation 목록이 있는 경우, 현재 목록에 있는 ciId를 전달한다.
            if (document.getElementById('ciRelation').hasChildNodes()) {
                document.querySelectorAll('.target-ci-id').forEach( (ciId) => {
                    const relCIChkElems = document.querySelectorAll('input[type=checkbox]');
                    relCIChkElems.forEach((chkElem) => {
                        if (chkElem.value === ciId.value) {
                            chkElem.checked = true;
                        }
                    });
                });
            }
        });

    },
    // 기존 CI 상세 조회 모달
    openViewModal(ciId) {
        aliceJs.fetchText('/cmdb/cis/component/view?ciId=' + ciId, {
            method: 'GET'
        }).then((htmlData) => {
            const viewModal = new modal({
                title: i18n.msg('cmdb.ci.label.view'),
                body: htmlData,
                classes: 'cmdb-ci-view-modal',
                buttons: [{
                    content: i18n.msg('common.btn.close'),
                    classes: 'z-button secondary',
                    bindKey: false,
                    callback: (modal) => modal.hide()
                }],
                close: {closable: false},
                onCreate: (modal) => {
                    // 세부 데이터가 존재할 경우
                    document.getElementById('ciAttributes').click();

                    // 스크롤바 추가
                    OverlayScrollbars(document.querySelector('.cmdb-ci-content-view'), {className: 'scrollbar'});
                    OverlayScrollbars(document.querySelectorAll('textarea'), {
                        className: 'scrollbar',
                        resize: 'vertical',
                        sizeAutoCapable: true,
                        textarea: {
                            dynHeight: false,
                            dynWidth: false,
                            inheritedAttrs: 'class'
                        }
                    });
                    // 태그 초기화
                    new zTag(document.getElementById('ciTags'), {
                        suggestion: true,
                        realtime: true,
                        tagType: 'ci',
                        targetId: ciId
                    });
                }
            });
            viewModal.show();
        });
    },
    // CI 타입 선택 모달 호출
    openSelectTypeModal(typeId) {
        tree.load({
            view: 'modal',
            dataUrl: '/rest/cmdb/types',
            title: i18n.msg('cmdb.ci.label.type'),
            source: 'ciType',
            target: 'modalTreeList',
            text: 'typeName',
            selectedValue: typeId,
            rootAvailable: false,
            callbackFunc: function (response) {
                if (response.id !== 'root') {
                    // 아이콘과 클래스가 없을 경우, 타입 변경시 기본 값을 추가해준다.
                    aliceJs.fetchJson('/rest/cmdb/types/' + response.id, {
                        method: 'GET'
                    }).then((response) => {
                        switch (response.status) {
                            case aliceJs.response.success:
                                const typeData = response.data;
                                document.getElementById('classId').value = typeData.classId;
                                document.getElementById('className').value = typeData.className;
                                document.getElementById('ciIcon').value = typeData.typeIcon;
                                document.getElementById('ciIconData').value = typeData.typeIconData;
                                // Class 상세 속성 표시
                                aliceJs.fetchJson('/rest/cmdb/classes/' + typeData.classId + '/attributes', {
                                    method: 'GET'
                                }).then((response) => {
                                    if (response.status === aliceJs.response.success) {
                                        const ciAttributeDOMElement = document.getElementById('ciAttributes');
                                        zCmdbAttribute.drawDetails(ciAttributeDOMElement, response.data, ZSession.getAll(), 'edit');
                                    }
                                });
                                break;
                            case aliceJs.response.error:
                                zAlert.warning(i18n.msg('cmdb.type.msg.selectAvailableType'));
                                break;
                            default :
                                break;
                        }
                    });
                    document.getElementById('typeName').value = response.dataset.name;
                    document.getElementById('typeId').value = response.id;
                } else {
                    zAlert.warning(i18n.msg('cmdb.type.msg.selectAvailableType'));
                }
            }
        });
    },
    // 세부 속성 데이터 조회
    getProperty() {
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZSwitchProperty('elementIsEditable', 'element.isEditable', this.elementIsEditable)),
            new ZGroupProperty('group.validation')
                .addProperty(new ZSwitchProperty('validationRequired', 'validation.required', this.validationRequired))
        ];
    },
    // 연관 관계 입력 row 추가
    addCIRelation(ciRelations, isChecked) {
        const trRow = document.createElement('tr');
        trRow.className = 'z-table-row z-relation-data';

        const targetIcon = document.createElement('td');
        targetIcon.className = 'col-1 align-center';
        const targetIconImg = document.createElement('img');
        targetIconImg.src = isChecked ? ciRelations.ciIconData : ciRelations.targetCIIconData;
        const targetIconName = document.createElement('input');
        targetIconName.type = 'hidden';
        targetIconName.value = isChecked ? ciRelations.ciIcon : ciRelations.targetCIIcon;
        targetIcon.appendChild(targetIconImg);
        targetIcon.appendChild(targetIconName);
        trRow.appendChild(targetIcon);

        const targetTypeName = document.createElement('td');
        targetTypeName.className = 'col-4 align-left';
        targetTypeName.textContent = isChecked ? ciRelations.typeName : ciRelations.targetTypeName;
        trRow.appendChild(targetTypeName);

        const targetCIData = document.createElement('td');
        targetCIData.className = 'col-4 align-left';
        const targetCIName = document.createElement('span');
        targetCIName.textContent = isChecked ? ciRelations.ciName : ciRelations.targetCIName;
        targetCIData.appendChild(targetCIName);
        const targetCIId = document.createElement('input');
        targetCIId.className = 'target-ci-id';
        targetCIId.type = 'hidden';
        targetCIId.value =  isChecked ? ciRelations.ciId : ciRelations.targetCIId;
        targetCIData.appendChild(targetCIId);
        trRow.appendChild(targetCIData);

        const deleteRel = document.createElement('td');
        deleteRel.className = 'col-1 align-center';
        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'z-button-icon extra';
        const deleteIcon = document.createElement('span');
        deleteIcon.className = 'z-icon i-delete';
        deleteBtn.addEventListener('click', function () {
            deleteBtn.parentElement.parentElement.remove();
        });
        deleteBtn.appendChild(deleteIcon);
        deleteRel.appendChild(deleteBtn);
        trRow.appendChild(deleteRel);

        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            deleteBtn.disabled = true;
        }
        document.getElementById('ciRelation').appendChild(trRow);
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
            value: (Array.isArray(this._value) && this._value.length > 0) ? JSON.stringify(this._value) : this._value,
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
