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

import {FORM, CLASS_PREFIX, CI, UNIT} from '../../lib/zConstants.js';
import { zValidation } from '../../lib/zValidation.js';
import {UIButton, UIDiv, UITable, UIRow, UICell, UIImg, UISpan, UIInput} from '../../lib/zUI.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        isEditable: true,
        borderColor: 'rgba(235, 235, 235, 1)'
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

        // CI 데이터 초기화
        /*this._value ={
            actionType: 'delete',
            ciIcon: 'image_server.png',
            ciIconData: 'data:image/png;base64,',
            ciId: '5',
            ciName: 'update test CI',
            ciNo: '5',
            ciStatus: 'use',
            classId: 'df562114ab87c066adeaea79b2e4a8a2',
            typeId: '587b4557275bcce81664db9e12485ae2',
            typeName: '서버'
        };*/
        if (this._value !== '${default}') {
            this.value = JSON.parse(this.data.value);
        }
    },
    // component 엘리먼트 생성
    makeElement() {
        // label 상단 처리
        this.labelPosition = FORM.LABEL.POSITION.TOP;

        const element = new UIDiv().setUIClass(CLASS_PREFIX + 'element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        // 버튼 목록
        element.UIButtonGroup = this.makeCIButton();
        element.addUI(element.UIButtonGroup);
        // 테이블
        element.UITable = this.makeCITable();
        element.addUI(element.UITable);

        return element;
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
    },
    get elementIsEditable() {
        return this._element.isEditable;
    },
    set elementBorderColor(borderColor) {
        this._element.borderColor = borderColor;
    },
    get elementBorderColor() {
        return this._element.borderColor;
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
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UIElement.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off');
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
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        }
        // 유효성 검증
        // keyup 일 경우 type, min, max 체크
        if (e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            return false;
        }
        // change 일 경우 minLength, maxLength 체크
        if (e.type === 'change' && !zValidation.changeValidationCheck(e.target)) {
            return false;
        }

        this.value = e.target.value;
    },
    makeCIButton() {
        const buttonGroup = new UIDiv().setUIClass('btn-list');
        if (this.elementIsEditable) {
            // 등록
            const registerButton = new UIButton(i18n.msg('cmdb.ci.label.new') + ' ' + i18n.msg('cmdb.ci.label.register'))
                .addUIClass('default-line')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.REGISTER)
                .onUIClick(this.openRegisterModal.bind(this));
            buttonGroup.addUI(registerButton);

            // 수정
            const updateButton = new UIButton(i18n.msg('cmdb.ci.label.existing') + ' ' + i18n.msg('cmdb.ci.label.update'))
                .addUIClass('default-line')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.MODIFY)
                .onUIClick(this.openSelectModal.bind(this));
            buttonGroup.addUI(updateButton);

            // 삭제
            const deleteButton = new UIButton(i18n.msg('cmdb.ci.label.existing') + ' ' + i18n.msg('cmdb.ci.label.delete'))
                .addUIClass('default-line')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.DELETE)
                .onUIClick(this.openSelectModal.bind(this));
            buttonGroup.addUI(deleteButton);
        } else {
            // 조회
            const selectButton = new UIButton(i18n.msg('cmdb.ci.label.select'))
                .addUIClass('default-line')
                .setUIAttribute('data-actionType', CI.ACTION_TYPE.READ)
                .onUIClick(this.openSelectModal.bind(this));
            buttonGroup.addUI(selectButton);
        }

        return buttonGroup;
    },
    makeCITable() {
        // 테이블
        const table = new UITable()
            .setUIClass(CLASS_PREFIX + 'ci-table')
            .setUIId('ciTable' + this.id);

        // 테이블 제목
        const row = new UIRow(table).setUIClass(CLASS_PREFIX + 'ci-table-header');
        table.addUIRow(row);

        this.getCITableData().forEach((option) => {
            const tdWidth = (Number(option.columnWidth) / FORM.COLUMN) * 100;
            const tdClassName = (option.type === 'hidden' ? '' : 'on') + ' ' + option.class;
            const td = new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                .setUITextContent((option.name !== '' ? i18n.msg(option.name) : ''));
            row.addUICell(td);
        });

        if (Array.isArray(this.value)) {
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
                { id: 'actionType', type: 'readonly',    columnWidth: '1', name: 'form.label.actionType', class: 'align-left first-column' },
                { id: 'ciId',       type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'ciNo',       type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'ciIcon',     type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'ciIconData', type: 'image',       columnWidth: '1', name: '', class:'align-left' },
                { id: 'typeId',     type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'typeName',   type: 'editable',    columnWidth: '3', name: 'cmdb.ci.label.type', class: 'align-left' },
                { id: 'ciName',     type: 'editable',    columnWidth: '3', name: 'cmdb.ci.label.name', class: 'align-left' },
                { id: 'ciDesc',     type: 'editable',    columnWidth: '4', name: 'cmdb.ci.label.description', class: 'align-left' },
                { id: 'classId',    type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'editIcon',   type: 'icon-edit',   columnWidth: '1', name: '', class: 'align-center' },
                { id: 'deleteIcon', type: 'icon-delete', columnWidth: '1', name: '', class: 'align-center last-column' }
            ];
        } else {
            // CI 컴포넌트 편집 가능여부가 false 일때 =  CI 아이콘, CI Type , CI 이름, 세부 정보 조회 아이콘, row 삭제 아이콘  5개
            return [
                { id: 'actionType', type: 'hidden',      columnWidth: '0', name: 'form.label.actionType', class: '' },
                { id: 'ciId',       type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'ciNo',       type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'ciIcon',     type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'ciIconData', type: 'image',       columnWidth: '1', name: '', class: 'align-left  first-column' },
                { id: 'typeId',     type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'typeName',   type: 'editable',    columnWidth: '3', name: 'cmdb.ci.label.type', class: 'align-left' },
                { id: 'ciName',     type: 'editable',    columnWidth: '4', name: 'cmdb.ci.label.name', class: 'align-left' },
                { id: 'ciDesc',     type: 'editable',    columnWidth: '4', name: 'cmdb.ci.label.description', class: 'align-left' },
                { id: 'classId',    type: 'hidden',      columnWidth: '0', name: '', class: '' },
                { id: 'searchIcon', type: 'icon-search', columnWidth: '1', name: '', class: 'align-center' },
                { id: 'deleteIcon', type: 'icon-delete', columnWidth: '1', name: '', class: 'align-center last-column' }
            ];
        }
    },
    getCITableDataToCell(row, option, data) {
        const tdWidth = (Number(option.columnWidth) / FORM.COLUMN) * 100;
        const tdClassName = (option.type === 'hidden' ? '' : 'on') + ' ' + option.class;

        switch (option.type) {
        case 'editable':
            return new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                .setUITextContent((typeof data[option.id] !== 'undefined') ? data[option.id] : '');
        case 'readonly':
            return new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                .setUITextContent(i18n.msg('cmdb.ci.actionType.' + data.actionType));
        case 'image':
            return new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                .addUI(new UIImg().setUISrc(data[option.id]).setUIWidth('20' + UNIT.PX).setUIHeight('20' + UNIT.PX));
        case 'icon-edit': // CI 등록 / 수정
            if (data.actionType === CI.ACTION_TYPE.DELETE) {
                const viewButton = new UIButton()
                    .setUIAttribute('data-type', data.actionType)
                    .onUIClick(this.openViewModal.bind(this))
                    .addUI(new UISpan().setUIClass('icon').addUIClass('icon-search'));

                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                    .addUI(viewButton);
            } else {
                const editButton = new UIButton()
                    .setUIAttribute('data-type', data.actionType)
                    .onUIClick(this.openUpdateModal.bind(this))
                    .addUI(new UISpan().setUIClass('icon').addUIClass('icon-edit'));

                return new UICell(row).setUIClass(tdClassName)
                    .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                    .addUI(editButton);
            }
        case 'icon-search': // CI 상세 조회
            const searchButton = new UIButton()
                .setUIAttribute('data-type', data.actionType)
                .onUIClick(this.openViewModal.bind(this))
                .addUI(new UISpan().setUIClass('icon').addUIClass('icon-search'));

            return new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                .addUI(searchButton);
        case 'icon-delete': // Row 삭제
            const deleteButton = new UIButton()
                .setUIAttribute('data-type', data.actionType)
                .onUIClick(this.removeCITableRow.bind(this, row.parent, row.getUIIndex()))
                .addUI(new UISpan().setUIClass('icon').addUIClass('icon-delete'));

            return new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                .addUI(deleteButton);
        default: // hidden
            return new UICell(row).setUIClass(tdClassName)
                .setUICSSText(`width:${tdWidth}%; border-color: ${this.elementBorderColor};`)
                .addUI(new UIInput(data[option.id]).setUIType('hidden'));
        }
    },
    addCITableRow(targetTable, data) {
        // 데이터가 없을 경우
        if (targetTable.rows.length === 2 && targetTable.rows[1].hasUIClass('no-data-found-list')) {
            this.removeCITableRow(targetTable, 1);
        }
        // row 추가
        const row = new UIRow(targetTable).setUIClass(CLASS_PREFIX + 'ci-table-row');
        targetTable.addUIRow(row);
        // td 추가
        this.getCITableData().forEach((option) => {
            row.addUICell(this.getCITableDataToCell(row, option, data));
        });
    },
    removeCITableRow(targetTable, index) {
        targetTable.removeUIRow(targetTable.rows[index]);
    },
    // 데이터가 없을때
    setEmptyCITable(targetTable) {
        const row = new UIRow(targetTable).setUIClass('no-data-found-list');
        targetTable.addUIRow(row);

        const td = new UICell(row).setUIClass('on align-center first-column last-column')
            .setColspan(12)
            .setUICSSText(`border-color: ${this.elementBorderColor};`)
            .setUITextContent(i18n.msg('common.msg.noData'));
        row.addUICell(td);
    },
    openViewModal(e) {

    },
    openRegisterModal(e) {

    },
    openUpdateModal(e) {

    },
    openSelectModal(e) {

    },
    getProperty() {
        return [];
    }
};