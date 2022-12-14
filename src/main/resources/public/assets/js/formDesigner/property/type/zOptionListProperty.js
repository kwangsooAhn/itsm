/**
 * Option List Property Class
 *
 * select box, check box, radio button 과 같은 컴포넌트는 선택할 수 있는 옵션들을 복수개 포함한다.
 * 이러한 옵션 리스트를 관리하는 속성 클래스이다.
 *
 * 2021-06-15 hcjung UX 가 수정되면 전면적인 수정이 필요할 수 있다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import ZProperty from '../zProperty.js';
import { UIButton, UICell, UICheckbox, UIDiv, UIInput, UILabel, UIRadioButton,
    UIRow, UISpan, UITable } from '../../../lib/zUI.js';
import { FORM } from '../../../lib/zConstants.js';
import { zValidation } from '../../../lib/zValidation.js';
const propertyExtends = {
    validation: {
        maxLength: 75
    }
};

export default class ZOptionListProperty extends ZProperty {
    constructor(key, name, value, flag, isAlwaysEditable) {
        super(key, name, 'optionListProperty', value, isAlwaysEditable);
        this.multipleSelect = flag; // 다중 선택 여부
    }

    /**
     * 옵션 리스트 속성을 그리는 작업은 크게 아래와 같이 구성된다.
     *
     *  1) 라벨 : 속성명 출력 (속성 공통)
     *  2) 추가/삭제 버튼 : 옵션 항목을 추가하거나 선택한 옵션을 삭제하기 위한 버튼.
     *  3) 옵션 리스트 : 현재 입력된 옵션들의 목록
     *
     * @param panel
     * @return {UIElement}
     */
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우) 또는 (문서가 '사용/발행' 이고 연결된 업무흐름이 없는 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property').setUIProperty('--data-column', this.columnWidth);

        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.UILabel.domElement.firstChild.classList.add('pt-2');

        // 옵션 추가 버튼
        this.UIElement.UIButton = new UIButton()
            .setUIClass('btn__ic')
            .addUIClass('extra')
            .addUIClass('float-right')
            .setUIDisabled(!this.isEditable)
            .onUIClick(this.addRow.bind(this));
        this.UIElement.UIButton.addUI(new UISpan().addUIClass('ic-plus'));
        this.UIElement.UILabel.addUI(this.UIElement.UIButton);
        this.UIElement.addUI(this.UIElement.UILabel);

        // 옵션 리스트
        const optionTable = new UITable()
            .setUIClass('tbl--option');

        const header = new UIRow(optionTable).setUIClass('tbl__head');
        optionTable.addUIRow(header);

        const checkedTD = new UICell(header).setUICSSText('width: 15%');
        const nameTD = new UICell(header).setUITextContent(i18n.msg('form.properties.optionList.name'));
        const valueTD = new UICell(header).setUITextContent(i18n.msg('form.properties.optionList.value'));

        // 마이너스 버튼 추가
        const removeTD = new UICell(header).setUICSSText('width: 15%;');

        header.addUICell(checkedTD);
        header.addUICell(nameTD);
        header.addUICell(valueTD);
        header.addUICell(removeTD);

        this.value.forEach((option) => {
            const optionRow = new UIRow(optionTable).setUIClass('tbl__body__row');
            optionTable.addUIRow(this.makeRow(optionRow, option));
        });

        this.UIElement.UIOptionTable = optionTable;
        this.UIElement.addUI(this.UIElement.UIOptionTable);
        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 옵션이 없을때
        if (!this.value.length) {
            this.setEmptyTable(this.UIElement.UIOptionTable);
        }
    }

    makeRow(optionRow, option) {
        const checkedTD = new UICell(optionRow);
        // 다중 선택 가능하면 checkbox 형태이고, 아니면 radio 버튼 형태로 표시
        const checkedId = ZWorkflowUtil.generateUUID();
        checkedTD.labelGroup = new UILabel()
            .setUIFor(checkedId);
        checkedTD.addUI(checkedTD.labelGroup);

        if (!this.isEditable) {
            checkedTD.labelGroup.addUIClass('readonly');
        }

        if (this.multipleSelect) {
            checkedTD.labelGroup.addUIClass('checkbox');
            checkedTD.labelGroup.checbox = new UICheckbox(option.checked || false)
                .setUIId(checkedId)
                .setUIAttribute('value', option.value)
                .onUIChange(this.updateProperty.bind(this));
            checkedTD.labelGroup.addUI(checkedTD.labelGroup.checbox);

            if (!this.isEditable) {
                checkedTD.labelGroup.checbox.addUIClass('readonly');
            }
        } else {
            checkedTD.labelGroup.addUIClass('radio');
            checkedTD.labelGroup.radio = new UIRadioButton(option.checked || false)
                .setUIId(checkedId)
                .setUIAttribute('name', 'option-radio')
                .onUIChange(this.updateProperty.bind(this));
            checkedTD.labelGroup.addUI(checkedTD.labelGroup.radio);

            if (!this.isEditable) {
                checkedTD.labelGroup.radio.addUIClass('readonly');
            }
        }
        checkedTD.labelGroup.addUI(new UISpan());

        const nameTD = new UICell(optionRow);
        nameTD.inputName =  new UIInput()
            .setUIPlaceholder(i18n.msg('form.properties.optionList.namePlaceholder'))
            .setUIValue(option.name).onUIChange(this.updateProperty.bind(this))
            .setUIValue(option.name).onUIKeyUp(this.updateProperty.bind(this))
            .setUIAttribute('data-validation-max-length', propertyExtends.validation.maxLength)
            .setUIAttribute('data-validation-required', 'true')
            .setUIAttribute('data-validation-required-name', i18n.msg(this.name))
            .setUIAttribute('name', 'optionName')
            .setUIReadOnly(!this.isEditable)
            .onUIFocusout(this.updateProperty.bind(this));
        nameTD.addUI(nameTD.inputName);

        const valueTD = new UICell(optionRow);
        valueTD.inputValue =  new UIInput()
            .setUIPlaceholder(i18n.msg('form.properties.optionList.valuePlaceholder'))
            .setUIValue(option.value).onUIChange(this.updateProperty.bind(this))
            .setUIValue(option.value).onUIKeyUp(this.updateProperty.bind(this))
            .setUIAttribute('data-validation-max-length', propertyExtends.validation.maxLength)
            .setUIAttribute('data-validation-required', 'true')
            .setUIAttribute('data-validation-required-name', i18n.msg(this.name))
            .setUIAttribute('name', 'optionValue')
            .setUIReadOnly(!this.isEditable)
            .onUIFocusout(this.updateProperty.bind(this));
        valueTD.addUI(valueTD.inputValue);

        const removeTD = new UICell(optionRow);
        removeTD.removeButton = new UIButton()
            .setUIClass('btn__ic')
            .setUIDisabled(!this.isEditable)
            .onUIClick(this.removeRow.bind(this));
        removeTD.removeButton.addUI(new UISpan().addUIClass('ic-remove'));
        removeTD.addUI(removeTD.removeButton);

        optionRow.addUICell(checkedTD);
        optionRow.addUICell(nameTD);
        optionRow.addUICell(valueTD);
        optionRow.addUICell(removeTD);

        return optionRow;
    }

    // 옵션 값 입력 시 데이터 반영 이벤트
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        // 유효성 검증
        // keyup 일 경우 type, min, max 체크
        if (e.type === 'keyup' && !zValidation.keyUpValidationCheck(e.target)) {
            return false;
        }

        // change 일 경우 minLength, maxLength & 중복 값 체크
        if (e.type === 'change') {
            let propertyValue = this.getPropertyValue(this.UIElement.UIOptionTable.domElement);
            let optionListName = propertyValue.map(v => v.name).filter(optName => optName === e.target.value);
            let optionListValue = propertyValue.map(v => v.value).filter(optValue => optValue === e.target.value);

            if (!zValidation.changeValidationCheck(e.target)) { return false; }
            if (e.target.name === 'optionName' && optionListName.length > 1) {
                zAlert.warning(i18n.msg('form.msg.duplicateOptionsName'), function () {
                    e.target.value = '';
                    e.target.focus();
                    return false;
                });
            } else if (e.target.name === 'optionValue' && optionListValue.length > 1) {
                zAlert.warning(i18n.msg('form.msg.duplicateOptionsValue'), function () {
                    e.target.value = '';
                    e.target.focus();
                    return false;
                });
            }
        }
        this.panel.update.call(this.panel, this.key, this.getPropertyValue(this.UIElement.UIOptionTable.domElement));
    }

    // 옵션 추가 버튼 클릭 이벤트
    addRow(e) {
        e.stopPropagation();
        e.preventDefault();

        const optionTable = this.UIElement.UIOptionTable;
        if (optionTable.rows.length === 2 && optionTable.rows[1].hasUIClass('no-data-found-list')) {
            optionTable.removeUIRow(optionTable.rows[1]);
        }

        const optionRow = new UIRow(optionTable).setUIClass('tbl__body__row');
        optionTable.addUIRow(this.makeRow(optionRow, FORM.DEFAULT_OPTION_ROW));

        this.panel.update.call(this.panel, this.key, this.getPropertyValue(this.UIElement.UIOptionTable.domElement));
    }

    // 옵션 삭제 버튼 클릭 이벤트
    removeRow(e) {
        e.stopPropagation();
        e.preventDefault();

        const selectedRowIndex = e.target.parentNode?.parentNode?.rowIndex;
        this.UIElement.UIOptionTable.removeUIRow(this.UIElement.UIOptionTable.rows[selectedRowIndex]);

        // 데이터가 없을때
        if (this.UIElement.UIOptionTable.rows.length === 1) {
            this.setEmptyTable(this.UIElement.UIOptionTable);
        }
        this.panel.update.call(this.panel, this.key, this.getPropertyValue(this.UIElement.UIOptionTable.domElement));
    }

    // 데이터가 없을 때
    setEmptyTable(targetTable) {
        const row = new UIRow(targetTable).setUIClass('no-data-found-list');
        const td = new UICell(row).setUIClass('align-center')
            .setColspan(targetTable.rows[0].cells.length)
            .setUITextContent(i18n.msg('form.msg.noOption'));
        row.addUICell(td);
        targetTable.addUIRow(row);
    }

    // table DOM을 받아서 입력된 옵션 명과 옵션 값을 리스트로 반환
    getPropertyValue(optionTable) {
        const propertyValue = [];
        for (let row of optionTable.rows) {
            const tdList = row.cells;
            if (row.sectionRowIndex > 0 && !row.classList.contains('no-data-found-list')) {
                let itemChecked = tdList[0].children[0].children[0].checked;
                let itemName = tdList[1].children[0].value;
                let itemValue = tdList[2].children[0].value;
                propertyValue.push({ name: itemName, value: itemValue, checked: itemChecked});
            }
        }
        return propertyValue;
    }
}
