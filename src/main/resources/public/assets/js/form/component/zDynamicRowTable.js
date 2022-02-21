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
import { zDocument } from '../../document/zDocument.js';

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
    initProperty: function () {
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
    },
    // component 엘리먼트 생성
    makeElement() {
        const element = new UIDiv().setUIClass('z-element').addUIClass('align-left')
            .setUIProperty('--data-column', this.elementColumnWidth);

        // 테이블
        element.UITable = new UITable()
            .setUIClass('z-option-table')
            .addUIClass('z-dr-table')
            .addUIClass('mt-2')
            .setUIId('drTable' + this.id)
            .setUIAttribute('tabindex', '-1')
            .setUIAttribute('data-validation-required', this.validationRequired);
        element.addUI(element.UITable);

        this.makeTable(element.UITable);

        element.UIDiv = new UIDiv().setUIClass('z-dr-table-button-group');
        element.addUI(element.UIDiv);

        // 플러그인 검증 버튼 생성
        element.UIDiv.plugInUIButton = new UIButton()
            .addUIClass('plugIn-check')
            .addUIClass('primary')
            .addUIClass('mr-2')
            .addUIClass(this.pluginUseYn ? 'on' : 'off')
            .setUIAttribute('data-validation-required', this.pluginRequired)
            .setUIAttribute('data-validation-type', this.pluginScriptType)
            .onUIClick(this.pluginCheck.bind(this));
        element.UIDiv.plugInUIButton.UIText = new UISpan().setUITextContent(this.pluginButtonText);
        element.UIDiv.plugInUIButton.addUI(element.UIDiv.plugInUIButton.UIText);
        element.UIDiv.addUI(element.UIDiv.plugInUIButton);

        // 추가 버튼
        element.UIDiv.addUIButton = new UIButton()
            .setUIClass('z-button-icon')
            .addUIClass('extra')
            .onUIClick(this.addTableRow.bind(this, element.UITable, {}))
            .addUI(new UISpan().addUIClass('z-icon').addUIClass('i-plus'));
        element.UIDiv.addUI(element.UIDiv.addUIButton);

        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIDiv.addUIButton.setUIDisabled(true);
            this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.setUIDisabled(true);
            // 모든 cell을 readonly 처리하고 버튼은 disabled 처리한다.
            const drTable = this.UIElement.UIComponent.UIElement.UITable.domElement;
            for (const row of drTable.rows) {
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
            this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.removeUIClass('off').addUIClass('on');
        } else {
            this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton.removeUIClass('on').addUIClass('off');
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
    makeTable(table) {
        // 테이블 제목
        const row = new UIRow(table).setUIClass('z-option-table-header').addUIClass('z-dr-table-header');
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
                .addUI(new UISpan().addUIClass('z-dr-table-header-cell').setUIInnerHTML(column.columnName));
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

        const td = new UICell(row).setUIClass('on align-center first-column last-column')
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
        const row = new UIRow(targetTable).setUIClass('z-dr-table-row');
        // td 추가
        const columnData = [];
        this.elementColumns.forEach((column, index) => {
            if (this.parent?.parent?.parent?.status !== FORM.STATUS.EDIT &&
                this.displayType !== FORM.DISPLAY_TYPE.HIDDEN) {
                if (zValidation.isEmpty(data[index])) {
                    let defaultValue = this.setDefaultValue(column);
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
            .setUIClass('z-button-icon')
            .addUIClass('extra')
            .onUIClick(this.removeTableRow.bind(this, targetTable, row))
            .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-remove'));
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
        let dateWrapper = new UIDiv().setUIClass('z-element');
        let date = new UIInput().setUIPlaceholder(i18n.dateFormat)
            .setUIClass('z-input i-date-picker text-ellipsis')
            .setUIId('date' + index +  ZWorkflowUtil.generateUUID())
            .setUIAttribute('name', 'date' + index)
            .setUIValue(this.getDefaultValueForDate(column, cellValue))
            .setUIAttribute('type', FORM.DATE_TYPE.DATE_PICKER)
            .setUIAttribute('data-validation-min-date', this._element.columns[index].columnValidation.minDate)
            .setUIAttribute('data-validation-max-date', this._element.columns[index].columnValidation.maxDate);
        dateWrapper.addUI(date);

        if (this.displayType === FORM.DISPLAY_TYPE.EDITABLE) {
            zDateTimePicker.initDatePicker(date.domElement, this.updateDateTimeValue.bind(this));
        }
        return dateWrapper;
    },
    getTimeForColumn(column, cellValue, index) {
        let timeWrapper = new UIDiv().setUIClass('z-element');

        let time = new UIInput().setUIPlaceholder(i18n.timeFormat)
            .setUIClass('z-input i-time-picker text-ellipsis')
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
        let dateTimeWrapper = new UIDiv().setUIClass('z-element');

        let dateTime = new UIInput().setUIPlaceholder(i18n.dateTimeFormat)
            .setUIClass('z-input i-datetime-picker text-ellipsis')
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
    getDefaultValueForDate(column, cellValue) {
        if (cellValue === '${default}') {
            // none, now, date|-3, time|2, datetime|7|0, datetimepicker|2020-03-20 09:00 등 기본 값이 전달된다.
            const defaultValueArray = column.columnElement.defaultValueRadio.split('|');
            let date = '';
            switch (defaultValueArray[0]) {
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    date = i18n.getDate();
                    break;
                case FORM.DATE_TYPE.DAYS:
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    date = i18n.getDate(offset);
                    break;
                case FORM.DATE_TYPE.DATE_PICKER:
                    date = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
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
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    time = i18n.getTime();
                    break;
                case FORM.DATE_TYPE.HOURS:
                    const offset = {
                        hours: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1])
                    };
                    time = i18n.getTime(offset);
                    break;
                case FORM.DATE_TYPE.TIME_PICKER:
                    time = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
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
                case FORM.DATE_TYPE.NONE:
                    break;
                case FORM.DATE_TYPE.NOW:
                    dateTime = i18n.getDateTime();
                    break;
                case FORM.DATE_TYPE.DATETIME:
                    const offset = {
                        days: zValidation.isEmpty(defaultValueArray[1]) || isNaN(Number(defaultValueArray[1])) ?
                            0 : Number(defaultValueArray[1]),
                        hours: zValidation.isEmpty(defaultValueArray[2]) || isNaN(Number(defaultValueArray[2])) ?
                            0 : Number(defaultValueArray[2])
                    };
                    dateTime = i18n.getDateTime(offset);
                    break;
                case FORM.DATE_TYPE.DATETIME_PICKER:
                    dateTime = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, zValidation.isEmpty(defaultValueArray[1]) ? '' : defaultValueArray[1]);
                    break;
            }
            return dateTime;
        } else {
            return aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.USERFORMAT, column.columnType, cellValue);
        }
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
                case FORM.DATE_TYPE.DAYS:
                    columns[i].columnValidation.minDate = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.minDate);
                    columns[i].columnValidation.maxDate = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.maxDate);
                    break;
                case FORM.DATE_TYPE.HOURS:
                    columns[i].columnValidation.minTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.minTime);
                    columns[i].columnValidation.maxTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.maxTime);
                    break;
                case FORM.DATE_TYPE.DATETIME:
                    columns[i].columnValidation.minTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.minDateTime);
                    columns[i].columnValidation.maxTime = aliceJs.convertDateFormat(format, columns[i].columnType, columns[i].columnValidation.maxDateTime);
                    break;
                default:
                    break;
            }
        }
        return columns;
    },
    isBetweenMaxDateTimeAndMinDatetime(target) {  // 최소 날짜 ,최대 날짜 유효성 검증
        let isValidationPass = true;

        switch (target.getAttribute('type').replace('Picker', '')) {
            case FORM.DATE_TYPE.DAYS:
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-date'))) {
                    isValidationPass = i18n.compareSystemDate(target.getAttribute('data-validation-min-date'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterDate', target.getAttribute('data-validation-min-date')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-date'))) {
                    isValidationPass = i18n.compareSystemDate(target.value, target.getAttribute('data-validation-max-date'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeDate', target.getAttribute('data-validation-max-date')));
                }
                break;
            case FORM.DATE_TYPE.HOURS:
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-time'))) {
                    isValidationPass = i18n.compareSystemTime(target.getAttribute('data-validation-min-time'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterTime', target.getAttribute('data-validation-min-time')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-time'))) {
                    isValidationPass = i18n.compareSystemTime(target.value, target.getAttribute('data-validation-max-time'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeTime', target.getAttribute('data-validation-max-time')));
                }
                break;
            case FORM.DATE_TYPE.DATETIME:
                if (!zValidation.isEmpty(target.getAttribute('data-validation-min-datetime'))) {
                    isValidationPass = i18n.compareSystemDateTime(target.getAttribute('data-validation-min-datetime'), target.value);
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectAfterDateTime', target.getAttribute('data-validation-min-datetime')));
                }
                if (isValidationPass && !zValidation.isEmpty(target.getAttribute('data-validation-max-datetime'))) {
                    isValidationPass = i18n.compareSystemDateTime(target.value, target.getAttribute('data-validation-max-datetime'));
                    zValidation.setDOMElementError(isValidationPass, target, i18n.msg('common.msg.selectBeforeDateTime', target.getAttribute('data-validation-max-datetime')));
                }
                break;
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
        const changeValue = (e.target instanceof HTMLSelectElement) ? e.target.options[e.target.selectedIndex].value :
            e.target.value;
        const cellElement = (e.target instanceof HTMLSelectElement) ? e.target.parentNode.parentNode :
            e.target.parentNode;
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
            'data': this.value
        };
        this.UIElement.UIComponent.UIElement.UIDiv.plugInUIButton
            .removeUIClass('primary').addUIClass('success--check');
        aliceJs.fetchJson('/rest/plugins/' + this.pluginScriptType, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pluginData),
            showProgressbar: true
        }).then((response) => {
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
                .addProperty(new ZColumnProperty('elementColumns', '', this.elementColumns)),
            new ZGroupProperty('plugin.button')
                .addProperty(new ZSwitchProperty('pluginUseYn', 'plugin.button', this.pluginUseYn))
                .addProperty(new ZSwitchProperty('pluginRequired', 'validation.required', this.pluginRequired))
                .addProperty(new ZInputBoxProperty('pluginButtonText', 'element.buttonText', this.pluginButtonText))
                .addProperty(new ZDropdownProperty('pluginScriptType', 'plugin.scriptType', this.pluginScriptType, plugInOption)),
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
            if (optionListType.includes(column.columnType) && zValidation.isEmptyOptions(column.columnElement.options)) {
                return false;
            }
        }
        return true;
    },
    // 컴포넌트별 기본값 세팅
    setDefaultValue(column) {
        let defaultValue = '${default}';
        switch (column.columnType) {
            case 'input':
                defaultValue = column.columnElement.defaultValueSelect.split('|');
                if (defaultValue[0] === 'input') {
                    defaultValue = defaultValue[1];
                } else {
                    defaultValue = ZSession.get(defaultValue[1]) || '';
                }
                break;
            case 'dropdown':
                for (let i = 0; i < column.columnElement.options.length; i++) {
                    let checkedYn = column.columnElement.options[i].checked || false;
                    if (checkedYn) {
                        defaultValue = column.columnElement.options[i].value;
                    }
                    defaultValue = defaultValue === '${default}' ? '' : defaultValue;
                }
                break;
            case 'time':
                defaultValue = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, column.columnType, this.getDefaultValueForTime(column, defaultValue));
                break;
            case 'date':
                defaultValue = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, column.columnType, this.getDefaultValueForDate(column, defaultValue));
                break;
            case 'dateTime':
                defaultValue = aliceJs.convertDateFormat(FORM.DATE_TYPE.FORMAT.SYSTEMFORMAT, column.columnType, this.getDefaultValueForDateTime(column, defaultValue));
                break;
            default:
                break;
        }
        return defaultValue;
    }
};
