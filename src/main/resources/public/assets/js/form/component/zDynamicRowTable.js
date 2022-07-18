/**
 * Dynamic Row Table Mixin
 *
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZColumnProperty, { propertyExtends } from '../../formDesigner/property/type/zColumnProperty.js';
import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZInputBoxProperty from '../../formDesigner/property/type/zInputBoxProperty.js';
import ZDropdownProperty from '../../formDesigner/property/type/zDropdownProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM, UNIT } from '../../lib/zConstants.js';
import { ZSession } from '../../lib/zSession.js';
import { UIButton, UICell, UIDiv, UIInput, UIRow, UISelect, UISpan, UITable } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '12',
        columns: [{
            ...propertyExtends.columnCommon,
            ...propertyExtends.input
        }]
    },
    plugin: {
        useYn: false,
        buttonText: 'TEXT',
        required: false, // 필수값 여부
        scriptType: ''
    },
    validation: {
        required: false // 필수값 여부
    }
};
Object.freeze(DEFAULT_COMPONENT_PROPERTY);
/**
 * 불필요한 key Event를 막기 위한 ascii key code
 */
const IGNORE_EVENT_KEYCODE = [8, 16, 17, 18, 19, 20, 27, 33, 34, 35, 36, 37, 38, 39, 40, 45, 46, 91, 92, 93,
    112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 144, 145];

export const dynamicRowTableMixin = {

    // 전달 받은 데이터와 기본 property merge
    initProperty: function() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._plugin = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.plugin, this.data.plugin);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        // 데이터 : "value" : [['제목', '제목', '제목'], ['1행 1열', '1행 2열', '1행 3열'], ['2행 1열', '2행 2열', '2행 2열']]
        this._value = this.data.value || '';
        // 데이터 초기화
        if (!zValidation.isEmpty(this._value)) {
            this._value = JSON.parse(this._value);
        }
        // 기본값 목록 초기화
        this._defaultValues = [];
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);

        // 버튼 목록
        element.UIDiv = new UIDiv().setUIClass('button-list');
        element.addUI(element.UIDiv);

        // 추가 버튼
        element.UIDiv.addUIButton = new UIButton(i18n.msg('form.label.addRow')).addUIClass('secondary');
        element.UIDiv.addUI(element.UIDiv.addUIButton);

        // 플러그인 검증 버튼 생성
        element.UIDiv.plugInUIButton = new UIButton()
            .addUIClass('plugIn-check')
            .addUIClass('primary')
            .addUIClass('mr-2')
            .addUIClass(this.pluginUseYn === 'none')
            .setUIAttribute('data-validation-required', this.pluginRequired)
            .setUIAttribute('data-validation-type', this.pluginScriptType)
            .onUIClick(this.pluginCheck.bind(this));
        element.UIDiv.plugInUIButton.UIText = new UISpan().setUITextContent(this.pluginButtonText);
        element.UIDiv.plugInUIButton.addUI(element.UIDiv.plugInUIButton.UIText);
        element.UIDiv.addUI(element.UIDiv.plugInUIButton);

        // 테이블
        element.UITable = new UITable()
            .setUIClass('option-table')
            .addUIClass('dr-table')
            .addUIClass('mt-2')
            .setUIId('drTable' + this.id)
            .setUIAttribute('tabindex', '-1')
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.addUI(element.UITable);

        // 기본값 목록 세팅
        this.setDefaultValues(this.elementColumns);

        this.makeTable(element.UITable);

        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        const drTable = this.UIElement.UIComponent.UIElement.UITable;
        const addRowButton = this.UIElement.UIComponent.UIElement.UIDiv.addUIButton;
        const plugInButton = this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton;
        // 행 추가 버튼 > 클릭 이벤트 부여
        addRowButton.onUIClick(this.addTableRow.bind(this, drTable, {}));
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            // 모든 버튼을 disabled 처리
            addRowButton.setUIDisabled(true);
            plugInButton.setUIDisabled(true);
            // 테이블의 상단 여백 제거
            drTable.removeUIClass('mt-2');
            // 테이블의 모든 cell을 readonly 처리하고 버튼은 disabled 처리한다.
            for (const row of drTable.domElement.rows) {
                for (const cell of row.cells) {
                    const elem = cell.querySelector('input, select, button');
                    if (zValidation.isDefined(elem)) {
                        // 버튼일 경우 readonly 대신 disabled 처리 필요
                        if (elem.tagName === 'BUTTON') {
                            elem.disabled = true;
                        } else if (elem.tagName === 'SELECT') {
                            elem.classList.add('readonly');
                        } else {
                            elem.readOnly = true;
                        }
                    }
                }
            }
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none') : '';
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
    set elementColumns(columns) {
        this._element.columns = this.changeDateTimeFormat(columns, FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT);
        this.UIElement.UIComponent.UIElement.UITable.clearUIRow().clearUI();
        this.makeTable(this.UIElement.UIComponent.UIElement.UITable);
    },
    get elementColumns() {
        return this.changeDateTimeFormat(this._element.columns, FORM.DATE_TYPE.FORMAT.USERFORMAT);
    },
    set plugin(plugin) {
        this._plugin = plugin;
    },
    get plugin() {
        return this._plugin;
    },
    set pluginUseYn(boolean) {
        this._plugin.useYn = boolean;
        if (boolean) {
            this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.removeUIClass('none').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.removeUIClass('on').addUIClass('none');
        }
    },
    get pluginUseYn() {
        return this._plugin.useYn;
    },
    set pluginButtonText(text) {
        this._plugin.buttonText = text;
        this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.UIText.setUITextContent(text);
    },
    get pluginButtonText() {
        return this._plugin.buttonText;
    },
    set pluginRequired(boolean) {
        this._plugin.required = boolean;
        this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.setUIAttribute('data-validation-required', boolean);
    },
    get pluginRequired() {
        return this._plugin.required;
    },
    set pluginScriptType(type) {
        this._plugin.scriptType = type;
        this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.setUIAttribute('data-validation-type', type);
    },
    get pluginScriptType() {
        return this._plugin.scriptType;
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
    makeTable(table) {
        // 테이블 제목
        const row = new UIRow(table).setUIClass('option-table-header').addUIClass('dr-table-header');
        table.addUIRow(row);

        this.elementColumns.forEach((column) => {
            const tdWidth = (Number(column.columnWidth) / FORM.COLUMN) * 100;
            const tdCssText = `width:${tdWidth}%;` +
                `color:${column.columnHead.fontColor};` +
                `font-size:${column.columnHead.fontSize + UNIT.PX};` +
                `${column.columnHead.bold ? 'font-weight:bold;' : ''}` +
                `${column.columnHead.italic ? 'font-style:italic;' : ''}` +
                `${column.columnHead.underline ? 'text-decoration:underline;' : ''}`;
            const td = new UICell(row)
                .addUIClass('align-' + column.columnHead.align)
                .setUICSSText(tdCssText)
                .addUI(new UISpan().addUIClass('dr-table-header-cell').setUIInnerHTML(column.columnName));
            row.addUICell(td);
        });
        // row 삭제 버튼 영역
        const td = new UICell(row)
            .addUIClass('align-center')
            .setUICSSText('width:35' + UNIT.PX);
        row.addUICell(td);

        if (Array.isArray(this.value) && this.value.length > 1) {
            this.value.forEach((rowData, index) => {
                if (index > 0) {
                    this.addTableRow(table, rowData);
                }
            });
        } else {
            this.setEmptyTable(table);
        }
    },
    // 데이터가 없을때
    setEmptyTable(targetTable) {
        const row = new UIRow(targetTable).setUIClass('no-data-found-list');
        targetTable.addUIRow(row);

        const td = new UICell(row).setUIClass('table-cell align-center first-column last-column')
            .setColspan(this.elementColumns.length + 1)
            .setUITextContent(i18n.msg('common.msg.noData'));
        row.addUICell(td);
    },
    // 테이블 row 추가
    addTableRow(targetTable, data) {
        // 데이터가 없을 경우 삭제
        if (targetTable.rows.length === 2 && targetTable.rows[1].hasUIClass('no-data-found-list')) {
            targetTable.removeUIRow(targetTable.rows[1]);
        }
        // value가 문자일 경우 배열로 변환
        if (zValidation.isEmpty(this._value)) {
            this.value = [];
            const columnHeadData = [];
            this.elementColumns.forEach((column) => {
                columnHeadData.push(column.columnName);
            });
            this.value.push(columnHeadData);
        }
        // row 추가
        const row = new UIRow(targetTable).setUIClass('dr-table-row');
        // td 추가
        const columnData = [];
        this.elementColumns.forEach((column, index) => {
            if (this.displayType !== FORM.DISPLAY_TYPE.HIDDEN) {
                if (zValidation.isEmpty(data[index])) {
                    let defaultValue = this._defaultValues[index];
                    columnData.push(defaultValue);
                } else {
                    columnData.push(data[index]);
                }
            }
            const tdWidth = (Number(column.columnWidth) / FORM.COLUMN) * 100;
            const tdCssText = `width:${tdWidth}%;` +
                `color:${column.columnContent.fontColor};` +
                `font-size:${column.columnContent.fontSize + UNIT.PX};` +
                `${column.columnContent.bold ? 'font-weight:bold;' : ''}` +
                `${column.columnContent.italic ? 'font-style:italic;' : ''}` +
                `${column.columnContent.underline ? 'text-decoration:underline;' : ''}`;

            const td = new UICell(row)
                .addUIClass('align-' + column.columnContent.align)
                .setUICSSText(tdCssText)
                .addUI(this.getElementByColumnType(column, columnData[index], index));
            row.addUICell(td);
        });
        // 데이터 추가
        if (zValidation.isEmpty(data)) {
            this.value[targetTable.rows.length] = columnData;
        }
        // 삭제 버튼
        const removeButton = new UIButton()
            .setUIClass('button-icon-sm')
            .addUIClass('mt-1')
            .addUIClass('mb-1')
            .onUIClick(this.removeTableRow.bind(this, targetTable, row))
            .addUI(new UISpan().setUIClass('icon').addUIClass('i-remove'));
        const td = new UICell(row)
            .addUIClass('align-center')
            .setUICSSText('width:35' + UNIT.PX)
            .addUI(removeButton);
        row.addUICell(td);
        targetTable.addUIRow(row);
        aliceJs.initDesignedSelectTag(targetTable.domElement);
    },
    // column Type 에 따른 cell 반환
    getElementByColumnType(column, cellValue, index) {
        switch (column.columnType) {
            case 'input':
                return this.getInputBoxForColumn(column, cellValue);
            case 'dropdown':
                return this.getDropDownForColumn(column, cellValue);
            case 'date':
                return this.getDateForColumn(column, cellValue, index);
            case 'time':
                return this.getTimeForColumn(column, cellValue, index);
            case 'dateTime':
                return this.getDateTimeForColumn(column, cellValue, index);
            case 'label':
                return this.getLabelForColumn(column, cellValue);
            case 'userSearch':
                return this.getUserSearchForColumn(column, cellValue, index);
            case 'organizationSearch':
                return this.getOrganizationSearchForColumn(column, cellValue, index);
            default:
                return new UISpan().setUIInnerHTML(cellValue);
        }
    },
    //column Type - input
    getInputBoxForColumn(column, cellValue) {
        let defaultValue = cellValue;
        if (cellValue === '${default}') {
            // 직접입력일 경우 : none|입력값
            const defaultValues = column.columnElement.defaultValueSelect.split('|');
            if (defaultValues[0] === 'input') {
                defaultValue = defaultValues[1];
            } else {  // 자동일경우 : select|userKey
                defaultValue = ZSession.get(defaultValues[1]) || '';
            }
        }
        return new UIInput().addUIClass('text-ellipsis')
            .setUIPlaceholder(column.columnElement.placeholder)
            .setUIValue(defaultValue)
            .setUIRequired(column.columnValidation.required)
            .setUIAttribute('data-validation-required', column.columnValidation.required)
            .setUIAttribute('data-validation-type', column.columnValidation.validationType)
            .setUIAttribute('data-validation-max-length', column.columnValidation.maxLength)
            .setUIAttribute('data-validation-min-length', column.columnValidation.minLength)
            .onUIKeyUp(this.updateValue.bind(this))
            .onUIChange(this.updateValue.bind(this));
    },
    // column Type - dropdown
    getDropDownForColumn(column, cellValue) {
        const selectbox = new UISelect()
            .setUIOptions(column.columnElement.options)
            .onUIChange(this.updateValue.bind(this));
        if (cellValue !== '${default}') {
            selectbox.setUIValue(cellValue);
        }
        return selectbox;
    },
    getDateForColumn(column, cellValue, index) {
        let dateWrapper = new UIDiv().setUIClass('picker-wrapper-dummy');
        let dateColumn = new UIInput().setUIPlaceholder(i18n.dateFormat)
            .setUIClass('input i-date-picker text-ellipsis')
            .setUIId('date' + index +  ZWorkflowUtil.generateUUID())
            .setUIAttribute('name', 'date' + index)
            .setUIValue(this.getDefaultValueForDate(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.DATE_PICKER)
            .setUIAttribute('data-validation-min-date', this._element.columns[index].columnValidation.minDate)
            .setUIAttribute('data-validation-max-date', this._element.columns[index].columnValidation.maxDate);
        dateWrapper.addUI(dateColumn);

        if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
            zDateTimePicker.initDatePicker(dateColumn.domElement, this.updateDateTimeValue.bind(this));
        }
        return dateWrapper;
    },
    getTimeForColumn(column, cellValue, index) {
        let timeWrapper = new UIDiv().setUIClass('element');

        let time = new UIInput().setUIPlaceholder(i18n.timeFormat)
            .setUIClass('input i-time-picker text-ellipsis')
            .setUIId('time' + index +  ZWorkflowUtil.generateUUID())
            .setUIAttribute('name', 'time' + index)
            .setUIValue(this.getDefaultValueForTime(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.TIME_PICKER)
            .setUIAttribute('data-validation-min-time', this._element.columns[index].columnValidation.minTime)
            .setUIAttribute('data-validation-max-time', this._element.columns[index].columnValidation.maxTime);
        timeWrapper.addUI(time);

        if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
            zDateTimePicker.initTimePicker(time.domElement, this.updateDateTimeValue.bind(this));
        }
        return timeWrapper;
    },
    getDateTimeForColumn(column, cellValue, index) {
        let dateTimeWrapper = new UIDiv().setUIClass('element');

        let dateTime = new UIInput().setUIPlaceholder(i18n.dateTimeFormat)
            .setUIClass('input i-datetime-picker text-ellipsis')
            .setUIId('datetime' + index +  ZWorkflowUtil.generateUUID())
            .setUIAttribute('name', 'datetime' + index)
            .setUIValue(this.getDefaultValueForDateTime(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.DATETIME_PICKER)
            .setUIAttribute('data-validation-min-datetime', this._element.columns[index].columnValidation.minDateTime)
            .setUIAttribute('data-validation-max-datetime', this._element.columns[index].columnValidation.maxDateTime);
        dateTimeWrapper.addUI(dateTime);

        if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
            zDateTimePicker.initDateTimePicker(dateTime.domElement, this.updateDateTimeValue.bind(this));
        }
        return dateTimeWrapper;
    },
    // column Type - label
    getLabelForColumn(column, cellValue) {
        const label = new UISpan().setUIClass('text-ellipsis')
            .setUITextContent(zValidation.isEmpty(cellValue) ? column.columnElement.text : cellValue);

        if (column.columnContent.underline) {
            label.setUITextDecoration('underline');
        }
        return label;
    },
    // column Type - userSearch
    getUserSearchForColumn(column, cellValue, index) {
        const defaultValues = cellValue.split('|');
        const element = new UIInput().setUIClass('input i-user-search text-ellipsis')
            .setUIId('userSearch' + index +  ZWorkflowUtil.generateUUID())
            .setUIValue((defaultValues.length > 1) ? defaultValues[1] : '')
            .setUIRequired(column.columnValidation.required)
            .setUIAttribute('data-validation-required', column.columnValidation.required)
            .setUIAttribute('data-modal-title', column.columnName)
            .setUIAttribute('data-realtime-selected-user', cellValue)
            .setUIAttribute('data-user-id', (defaultValues.length > 1) ? defaultValues[2] : '')
            .setUIAttribute('data-user-search', (defaultValues.length > 1) ? defaultValues[0] : '')
            .setUIAttribute('data-user-search-target', column.columnElement.userSearchTarget)
            .setUIAttribute('oncontextmenu', 'return false;')
            .setUIAttribute('onkeypress', 'return false;')
            .setUIAttribute('onkeydown', 'return false;')
            .onUIChange(this.updateValue.bind(this));

        if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
            element.onUIClick(this.openUserSearchModal.bind(this));
        }
        
        return element;
    },
    // column Type - OrganizationSearch
    getOrganizationSearchForColumn(column, cellValue, index) {
        const defaultValues = cellValue.split('|');
        const element = new UIInput().setUIClass('input i-organization-search text-ellipsis')
            .setUIId('organizationSearch' + index +  ZWorkflowUtil.generateUUID())
            .setUIValue((defaultValues.length > 1) ? defaultValues[1] : '')
            .setUIRequired(column.columnValidation.required)
            .setUIAttribute('data-validation-required', column.columnValidation.required)
            .setUIAttribute('data-modal-title', column.columnName)
            .setUIAttribute('data-organization-search', (defaultValues.length > 1) ? defaultValues[0] : '')
            .setUIAttribute('oncontextmenu', 'return false;')
            .setUIAttribute('onkeypress', 'return false;')
            .setUIAttribute('onkeydown', 'return false;')
            .onUIChange(this.updateValue.bind(this));

        if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
            element.onUIClick(this.openOrganizationSearchModal.bind(this));
        }

        return element;
    },
    // 컴포넌트별 기본값 목록 세팅
    setDefaultValues(columns) {
        for (const column of columns) {
            let defaultValue = '${default}';
            switch (column.columnType) {
                case 'input': {
                    defaultValue = column.columnElement.defaultValueSelect.split('|');
                    if (defaultValue[0] === 'input') {
                        defaultValue = defaultValue[1];
                    } else {
                        defaultValue = ZSession.get(defaultValue[1]) || '';
                    }
                    break;
                }
                case 'dropdown': {
                    for (let i = 0; i < column.columnElement.options.length; i++) {
                        let checkedYn = column.columnElement.options[i].checked || false;
                        if (checkedYn) {
                            defaultValue = column.columnElement.options[i].value;
                        }
                        defaultValue = defaultValue === '${default}' ? '' : defaultValue;
                    }
                    break;
                }
                case 'time': {
                    defaultValue = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, column.columnType,
                        this.getDefaultValueForTime(column, defaultValue));
                    break;
                }
                case 'date': {
                    defaultValue = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, column.columnType,
                        this.getDefaultValueForDate(column, defaultValue));
                    break;
                }
                case 'dateTime': {
                    defaultValue = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, column.columnType,
                        this.getDefaultValueForDateTime(column, defaultValue));
                    break;
                }
                case 'label': {
                    defaultValue = '';
                    break;
                }
                case 'userSearch': {
                    // 설정된 검색조건에 대해 설정된 기본값이 유효한지 검증한다.
                    this.getDefaultValueForUserSearch(column, column.columnElement.defaultValue.type).then((data) => {
                        column.columnElement.defaultValue.data = data;
                    });
                    defaultValue = defaultValue === '${default}' ?
                        column.columnElement.defaultValue.data : defaultValue;
                    break;
                }
                case 'organizationSearch': {
                    defaultValue = defaultValue === '${default}' ?
                        column.columnElement.defaultValue.data : defaultValue;
                    break;
                }
                default:
                    break;
            }
            this._defaultValues.push(defaultValue);
        }
    },
    // 기본값 조회
    getDefaultValueForDate(column, cellValue) {
        if (cellValue === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = column.columnElement.defaultValueRadio.split('|');
            let date = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NOW: {
                    date = i18n.getDate();
                    break;
                }
                case FORM.DATE_TYPE.DAYS: {
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    date = i18n.getDate(offset);
                    break;
                }
                case FORM.DATE_TYPE.DATE_PICKER: {
                    date = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType,
                        zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
                }
                default:
                    break;
            }
            return date;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, cellValue);
        }
    },
    getDefaultValueForTime(column, cellValue) {
        if (cellValue === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = column.columnElement.defaultValueRadio.split('|');
            let time = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NOW: {
                    time = i18n.getTime();
                    break;
                }
                case FORM.DATE_TYPE.HOURS: {
                    const offset = {
                        hours: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    time = i18n.getTime(offset);
                    break;
                }
                case FORM.DATE_TYPE.TIME_PICKER: {
                    time = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType,
                        zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
                }
                default:
                    break;
            }
            return time;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, cellValue);
        }
    },
    getDefaultValueForDateTime(column, cellValue) {
        if (cellValue === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = column.columnElement.defaultValueRadio.split('|');
            let dateTime = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NOW: {
                    dateTime = i18n.getDateTime();
                    break;
                }
                case FORM.DATE_TYPE.DATETIME: {
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1]),
                        hours: zValidation.isEmpty(defaultValueArray[2]) || isNaN(Number(defaultValueArray[2])) ?
                            0 : Number(defaultValueArray[2])
                    };
                    dateTime = i18n.getDateTime(offset);
                    break;
                }
                case FORM.DATE_TYPE.DATETIME_PICKER: {
                    dateTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType,
                        zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
                }
                default:
                    break;
            }
            return dateTime;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, cellValue);
        }
    },
    // 사용자 검색 컴포넌트 검색 조건과 기본값을 비교하여 유효한 값인지 체크, 아닐 경우 빈 값으로 처리
    async getDefaultValueForUserSearch(column, defaultType) {
        let defaultValue = column.columnElement.defaultValue.data;
        if (defaultType !== FORM.DEFAULT_VALUE_TYPE.NONE) {
            // 사용자 검색 조건이 모두 충족되었는지 확인
            const searchValue = defaultValue.split('|')[2];
            let targetData = column.columnElement.userSearchTarget;
            let targetCriteria = '';
            let searchKeys = '';
            if (!zValidation.isEmpty(targetData)) {
                targetData = JSON.parse(targetData);
                targetCriteria = targetData.targetCriteria;
                targetData.searchKey.forEach( (elem, index) => {
                    searchKeys += (index > 0) ? '+' + elem.id : elem.id;
                });
            }
            await aliceJs.fetchText('/users/searchUsers?searchValue=' + searchValue
                + '&targetCriteria=' + targetCriteria + '&searchKeys=' + searchKeys, {
                method: 'GET'
            }).then((htmlData) => {
                const userListElem = new DOMParser().parseFromString(htmlData.toString(), 'text/html');
                if (!userListElem.querySelectorAll('.table-row').length) {
                    defaultValue = '';
                }
            });
        }
        return defaultValue;
    },
    // 테이블 row 삭제
    removeTableRow(targetTable, row) {
        const removeIndex = targetTable.getIndexUIRow(row);
        targetTable.removeUIRow(row);

        const newValue = JSON.parse(JSON.stringify(this.value));
        newValue.splice(removeIndex, 1);
        this.value = newValue;

        // 데이터가 존재하지 않으면 '데이터가 존재하지 않습니다 ' 문구 표시
        if (Array.isArray(this.value) && this.value.length === 1) {
            this.setEmptyTable(targetTable);
        }
    },
    changeDateTimeFormat(columns, format) {
        for (let i = 0; i < columns.length; i++) {
            switch (columns[i].columnType) {
                case FORM.DATE_TYPE.DAYS: {
                    columns[i].columnValidation.minDate = aliceJs.convertDateFormat(format, columns[i].columnType,
                        columns[i].columnValidation.minDate);
                    columns[i].columnValidation.maxDate = aliceJs.convertDateFormat(format, columns[i].columnType,
                        columns[i].columnValidation.maxDate);
                    break;
                }
                case FORM.DATE_TYPE.HOURS: {
                    columns[i].columnValidation.minTime = aliceJs.convertDateFormat(format, columns[i].columnType,
                        columns[i].columnValidation.minTime);
                    columns[i].columnValidation.maxTime = aliceJs.convertDateFormat(format, columns[i].columnType,
                        columns[i].columnValidation.maxTime);
                    break;
                }
                case FORM.DATE_TYPE.DATETIME: {
                    columns[i].columnValidation.minTime = aliceJs.convertDateFormat(format, columns[i].columnType,
                        columns[i].columnValidation.minDateTime);
                    columns[i].columnValidation.maxTime = aliceJs.convertDateFormat(format, columns[i].columnType,
                        columns[i].columnValidation.maxDateTime);
                    break;
                }
                default:
                    break;
            }
        }
        return columns;
    },
    isBetweenMaxDateTimeAndMinDatetime(target) {  // 최소 날짜 ,최대 날짜 유효성 검증
        let isValidationPass = true;

        switch (target.getAttribute('type').replace('Picker', '')) {
            case FORM.DATE_TYPE.DAYS: {
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-date'))) {
                    isValidationPass = i18n.compareSystemDate(
                        target.getAttribute('data-validation-min-date'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterDate',
                        target.getAttribute('data-validation-min-date')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-date'))) {
                    isValidationPass = i18n.compareSystemDate(
                        target.value, target.getAttribute('data-validation-max-date'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeDate',
                        target.getAttribute('data-validation-max-date')));
                }
                break;
            }
            case FORM.DATE_TYPE.HOURS: {
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-time'))) {
                    isValidationPass = i18n.compareSystemTime(
                        target.getAttribute('data-validation-min-time'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterTime',
                        target.getAttribute('data-validation-min-time')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-time'))) {
                    isValidationPass = i18n.compareSystemTime(
                        target.value, target.getAttribute('data-validation-max-time'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeTime',
                        target.getAttribute('data-validation-max-time')));
                }
                break;
            }
            case FORM.DATE_TYPE.DATETIME: {
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-datetime'))) {
                    isValidationPass = i18n.compareSystemDateTime(
                        target.getAttribute('data-validation-min-datetime'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterDateTime',
                        target.getAttribute('data-validation-min-datetime')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-datetime'))) {
                    isValidationPass = i18n.compareSystemDateTime(
                        target.value, target.getAttribute('data-validation-max-datetime'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeDateTime',
                        target.getAttribute('data-validation-max-datetime')));
                }
                break;
            }
            default:
                break;
        }
        return isValidationPass;
    },
    // 신청서, 처리할문서에서 row에 값 변경시 이벤트 핸들러
    updateValue(e) {
        // input, dropdown, date, time, datetime, customCode 등 여러 타입의 값을 처리
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.type === 'keyup' && (e.keyCode === 13 || e.keyCode === 9)) {
            return false;
        // 입력을 무시할 key event 일 경우
        } else if (e.type === 'keyup' && (IGNORE_EVENT_KEYCODE.indexOf(e.keyCode) > -1)) {
            return true;
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
        const newValue = JSON.parse(JSON.stringify(this.value));
        const cellElement = (e.target instanceof HTMLSelectElement) ? e.target.parentNode.parentNode :
            e.target.parentNode;
        let changeValue = (e.target instanceof HTMLSelectElement) ? e.target.options[e.target.selectedIndex].value :
            e.target.value;

        // 사용자 검색용 컴포넌트일 경우
        if (e.target.classList.contains('i-user-search')) {
            const userSearchData = e.target.getAttribute('data-user-search');
            const userId = e.target.getAttribute('data-user-id');
            changeValue = `${userSearchData}|${e.target.value}|${userId}`;
        } else if (e.target.classList.contains('i-organization-search')) { // 부서 검색용 컴포넌트일 경우
            const organizationSearchData = e.target.getAttribute('data-organization-search');
            changeValue = `${organizationSearchData}|${e.target.value}`;
        }

        newValue[cellElement.parentNode.rowIndex][cellElement.cellIndex] = changeValue;
        this.value = newValue;
    },
    updateDateTimeValue(e) {
        if (!this.isBetweenMaxDateTimeAndMinDatetime(e)) {
            return false;
        }
        const newValue = JSON.parse(JSON.stringify(this.value));
        const rowIndex = e.parentNode.parentNode.parentNode.parentNode.rowIndex;
        const cellIndex = e.parentNode.parentNode.parentNode.cellIndex;
        newValue[rowIndex][cellIndex] = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT,
            e.getAttribute('type').replace('Picker', ''), e.value);
        this.value = newValue;
    },
    // 플러그인 유효성 검증
    pluginCheck() {
        if (!Array.isArray(this.value) || this.value.length < 2) {
            zAlert.warning(i18n.msg('form.msg.failedAllColumnDelete'));
            return false;
        }
        // 신청서는 tokenId가 없음
        const pluginData = {
            'tokenId': zValidation.isDefined(this.data.tokenId) ? this.data.tokenId : '',
            'pluginId': this.pluginScriptType,
            'data': this.value
        };
        aliceJs.fetchJson('/rest/plugins/' + this.pluginScriptType + '?' + pluginData.tokenId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pluginData),
            showProgressbar: true
        }).then((response) => {
            if (response.status !== aliceJs.response.success) {
                return false;
            }
            // primary > 검증 안됨
            if (response.data.result) {
                // success--check > 성공
                this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton
                    .removeUIClass('primary').addUIClass('success--check');
            } else {
                // error > 실패
                this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton
                    .removeUIClass('primary').addUIClass('error');
            }
        });
    },
    // 사용자 선택 모달
    openUserSearchModal(e) {
        const target = e.target; // userSearch 컴포넌트
        const userSearchModal = new modal({
            title: e.target.getAttribute('data-modal-title'),
            body: `<div class="target-user-list">` +
                `<input class="input i-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
                `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
                `<span id="spanTotalCount" class="search-count"></span>` +
                `<div class="table-set" id="searchUserList"></div>` +
                `</div>`,
            classes: 'target-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'button primary',
                bindKey: false,
                callback: (modal) => {
                    const realTimeSelectedUser = target.getAttribute('data-realtime-selected-user').split('|');
                    // 최근 선택값이 있는 경우, 해당 사용자 id와 이름을 전달한다.
                    if (realTimeSelectedUser.length === 1) {
                        zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                        return false;
                    }

                    target.setAttribute('data-user-search', realTimeSelectedUser[0]);
                    target.setAttribute('data-user-id', realTimeSelectedUser[2]);
                    target.value = realTimeSelectedUser[1];
                    target.dispatchEvent(new Event('change'));

                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: () => {
                // 모달 오픈시 선택된 값이 있으면 설정한다.
                const targetUserKey = target.getAttribute('data-user-search');
                const targetUserId = target.getAttribute('data-user-id');
                if (!zValidation.isEmpty(targetUserKey)) {
                    target.setAttribute('data-realtime-selected-user',
                        `${targetUserKey}|${target.value}|${targetUserId}`);
                } else {
                    target.setAttribute('data-realtime-selected-user', '');
                }
                // 검색 이벤트 추가
                document.getElementById('search').addEventListener('keyup', aliceJs.debounce ((e) => {
                    this.getUserSearchList(target, e.target.value, false);
                }), false);
                this.getUserSearchList(target, document.getElementById('search').value, true);
                OverlayScrollbars(document.querySelector('.modal-content'), { className: 'scrollbar' });
            }
        });
        userSearchModal.show();
    },
    // 사용자 선택 모달 - 검색 목록 조회
    getUserSearchList(target, search, showProgressbar) {
        const userSearchTarget = JSON.parse(target.getAttribute('data-user-search-target'));
        let searchKeys = '';
        userSearchTarget.searchKey.forEach( (elem, index) => {
            searchKeys += (index > 0) ? '+' + elem.id : elem.id;
        });

        let strUrl = '/users/searchUsers?searchValue=' + encodeURIComponent(search.trim()) +
            '&targetCriteria=' + userSearchTarget.targetCriteria + '&searchKeys=' + searchKeys;
        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            const searchUserList = document.getElementById('searchUserList');
            searchUserList.innerHTML = htmlData;
            OverlayScrollbars(searchUserList.querySelector('.table-body'), { className: 'scrollbar' });
            // 갯수 가운트
            aliceJs.showTotalCount(searchUserList.querySelectorAll('.table-row').length);
            // 체크 이벤트
            searchUserList.querySelectorAll('input[type=radio]').forEach((element) => {
                element.addEventListener('change', () => {
                    const userId = element.getAttribute('data-user-id');
                    target.setAttribute('data-realtime-selected-user', element.checked ?
                        `${element.id}|${element.value}|${userId}` : '');
                });
            });
            // 기존 선택값 표시
            const targetUserKey = target.getAttribute('data-realtime-selected-user').split('|')[0];
            const targetRadio = searchUserList.querySelector('input[id="' + targetUserKey + '"]');
            if (!zValidation.isEmpty(targetUserKey) && !zValidation.isEmpty(targetRadio)) {
                targetRadio.checked = true;
            }
        });
    },
    // 부서 선택 모달
    openOrganizationSearchModal(e) {
        const organizationSearchData = e.target.getAttribute('data-organization-search');
        tree.load({
            view: 'modal',
            title: e.target.getAttribute('data-modal-title'),
            dataUrl: '/rest/organizations',
            target: 'treeList',
            source: 'organization',
            text: 'organizationName',
            nodeNameLabel: i18n.msg('common.msg.dataSelect', i18n.msg('department.label.deptName')),
            defaultIcon: '/assets/media/icons/tree/icon_tree_organization.svg',
            selectedValue: organizationSearchData,
            callbackFunc: (response) => {
                e.target.value = response.textContent;
                e.target.setAttribute('data-organization-search', response.id);
                e.target.dispatchEvent(new Event('change'));
            }
        });
    },
    // 세부 속성 조회
    getProperty() {
        const plugInOption = FORM.PLUGIN_LIST.reduce((result, option) => {
            option.name = option.pluginName;
            option.value = option.pluginId;
            result.push(option);
            return result;
        }, []);
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('element.columns')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(new ZColumnProperty('elementColumns', '', FORM.COLUMN_PROPERTY.COLUMN,
                    this.elementColumns)),
            new ZGroupProperty('plugin.button')
                .addProperty(new ZSwitchProperty('pluginUseYn', 'plugin.button', this.pluginUseYn))
                .addProperty(new ZSwitchProperty('pluginRequired', 'validation.required', this.pluginRequired))
                .addProperty(new ZInputBoxProperty('pluginButtonText', 'element.buttonText', this.pluginButtonText))
                .addProperty(new ZDropdownProperty('pluginScriptType', 'plugin.scriptType', this.pluginScriptType,
                    plugInOption)),
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
            value: (Array.isArray(this._value) && this._value.length > 0) ? JSON.stringify(this._value) : this._value,
            label: this._label,
            element: this._element,
            plugin: this._plugin,
            validation: this._validation
        };
    },
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        const optionListType = ['radio', 'checkBox', 'dropdown'];
        for (let column of this.element.columns) {
            if (optionListType.includes(column.columnType) &&
                zValidation.isEmptyOptions(column.columnElement.options)) {
                return false;
            }
            // 사용자 검색용 컴포넌트
            if (column.columnType === 'userSearch') {
                // 사용자 목록이 없을 때
                const userSearchTarget = column.columnElement.userSearchTarget;
                const userSearchTargetData = !zValidation.isEmpty(userSearchTarget) ? JSON.parse(userSearchTarget) : '';
                if (zValidation.isEmpty(userSearchTargetData) ||
                    zValidation.isEmpty(userSearchTargetData.searchKey[0])) {
                    zAlert.warning(i18n.msg('common.msg.required', i18n.msg('form.properties.userList')));
                    return false;
                }
                // 조회 대상이 없을 때
                if (zValidation.isEmpty(userSearchTargetData.searchKey[0].value)) {
                    zAlert.warning(i18n.msg('common.msg.required', i18n.msg('form.properties.element.searchTarget')));
                    return false;
                }
            }
        }
        return true;
    }
};
