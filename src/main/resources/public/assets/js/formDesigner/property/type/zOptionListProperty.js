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
import { UIButton, UICell, UICheckbox, UIDiv, UIInput, UIRow, UISpan, UITable } from '../../../lib/zUI.js';
import { FORM } from '../../../lib/zConstants.js';

const propertyExtends = {
    /* 추가되는 기본 속성외에 속성이 없음 */
};

export default class ZOptionListProperty extends ZProperty {
    constructor(key, name, value) {
        super(key, name, 'optionListProperty', value);
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
        this.UIElement = new UIDiv().setUIClass('property').setUIProperty('--data-column', this.columnWidth);

        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty().setUICSSText('padding-bottom: 0;');
        const UILabelText = this.UIElement.UILabel.domElement.firstChild;
        UILabelText.style.display = 'inline-block';
        UILabelText.style.margin = '0.5rem 0';

        // 옵션 추가 버튼
        this.UIElement.UIButton = new UIButton()
            .setUIClass('z-button-icon')
            .addUIClass('extra')
            .addUIClass('float-right')
            .onUIClick(this.addRow.bind(this));
        const plusIcon = new UISpan()
            .addUIClass('z-icon')
            .addUIClass('i-plus');
        this.UIElement.UIButton.addUI(plusIcon);
        this.UIElement.UILabel.addUI(this.UIElement.UIButton);
        this.UIElement.addUI(this.UIElement.UILabel);

        // 옵션 삭제 버튼
        const optionButtonGroup = new UIDiv().setUIClass('z-button-list');
        const removeButton = new UIButton()
            .setUIClass('z-button-icon')
            .addUIClass('extra')
            .onUIClick(this.removeRow.bind(this));
        const minusIcon = new UISpan().addUIClass('z-icon').addUIClass('i-minus');
        removeButton.addUI(minusIcon);

        optionButtonGroup.addUI(removeButton);
        this.UIElement.UIButtonGroup = optionButtonGroup;
        this.UIElement.addUI(this.UIElement.UIButtonGroup);

        // 옵션 리스트
        const optionTable = new UITable()
            .setUIClass('z-option-table')

        const header = new UIRow(optionTable).setUIClass('z-option-table-header');
        optionTable.addUIRow(header);

        const nameTD = new UICell(header).setUITextContent(i18n.msg('form.properties.optionList.name'));
        const valueTD = new UICell(header).setUITextContent(i18n.msg('form.properties.optionList.value'));
        const checkTD = new UICell(header).setUICSSText('width: 10%;');

        // 마이너스 버튼 추가
        const removeTd = new UICell(header).setUICSSText('width: 10%;');

        header.addUICell(nameTD);
        header.addUICell(valueTD);
        header.addUICell(checkTD);
        header.addUICell(removeTd);

        this.value.forEach((option) => {
            const optionRow = new UIRow(optionTable).setUIClass('z-option-table-row');
            optionTable.addUIRow(this.makeRow(optionRow, option));
        });

        this.UIElement.UIOptionTable = optionTable;
        this.UIElement.addUI(this.UIElement.UIOptionTable);
        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    makeRow(optionRow, option) {
        const nameTD = new UICell(optionRow);
        nameTD.inputName =  new UIInput()
            .setUIPlaceholder(i18n.msg('form.properties.optionList.namePlaceholder'))
            .setUIValue(option.name).onUIChange(this.updateProperty.bind(this));
        nameTD.addUI(nameTD.inputName);

        const valueTD = new UICell(optionRow);
        valueTD.inputValue =  new UIInput()
            .setUIPlaceholder(i18n.msg('form.properties.optionList.valuePlaceholder'))
            .setUIValue(option.value).onUIChange(this.updateProperty.bind(this));
        valueTD.addUI(valueTD.inputValue);

        const checkTD = new UICell(optionRow).setUICSSText('width: 10%;');
        checkTD.checkBox =  new UICheckbox(false);
        checkTD.addUI(checkTD.checkBox);

        // todo: removeRow 기능 수정 후 적용
        const removeTd = new UICell(optionRow).setUICSSText('width: 15%;');
        removeTd.removeButton = new UIButton()
            .addUIClass('z-icon')
            .addUIClass('i-clear')
            // .onUIClick(this.removeRow.bind(this));
        removeTd.addUI(removeTd.removeButton);

        optionRow.addUICell(nameTD);
        optionRow.addUICell(valueTD);
        optionRow.addUICell(checkTD);
        optionRow.addUICell(removeTd);

        return optionRow;
    }

    // 옵션 값 입력 시 데이터 반영 이벤트
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();

        const optionTable = e.target.parentNode.parentNode.parentNode;
        this.panel.update.call(this.panel, this.key, this.getPropertyValue(optionTable));
    }

    // 옵션 추가 버튼 클릭 이벤트
    addRow(e) {
        e.stopPropagation();
        e.preventDefault();

        const optionTable = this.UIElement.UIOptionTable;

        if (optionTable.rows.length === 2 && optionTable.rows[1].hasUIClass('no-data-found-list')) {
            optionTable.removeUIRow(optionTable.rows[1]);
        }

        const optionRow = new UIRow(optionTable).setUIClass('z-option-table-row');
        optionTable.addUIRow(this.makeRow(optionRow, FORM.DEFAULT_OPTION_ROW));

        this.panel.update.call(this.panel, this.key, this.getPropertyValue(this.UIElement.UIOptionTable.domElement));
    }

    // 옵션 삭제 버튼 클릭 이벤트
    // todo: 다중 선택 제거가 제대로 되지 않는 이슈 있음.
    //       클릭한 remove Button의 row만 삭제하도록 수정.
    removeRow(e) {
        e.stopPropagation();
        e.preventDefault();

        const optionTable = this.UIElement.UIOptionTable.domElement;
        for (let row of optionTable.rows) {
            const tdList = row.cells;
            if (row.sectionRowIndex > 0 && tdList[2].children[0].checked) {
                this.UIElement.UIOptionTable.removeUIRow(this.UIElement.UIOptionTable.rows[row.sectionRowIndex]);
            }
        }

        if (optionTable.rows.length === 1) {
            const row = new UIRow(this.UIElement.UIOptionTable).setUIClass('no-data-found-list');
            this.UIElement.UIOptionTable.addUIRow(row);

            const td = new UICell(row).setUIClass('align-center')
                .setColspan(4)
                .setUITextContent(i18n.msg('common.msg.noData'));
            row.addUICell(td);
        }

        this.panel.update.call(this.panel, this.key, this.getPropertyValue(optionTable));
    }

    // 데이터가 없을 때
    setEmptyTable(targetTable) {
        const row = new UIRow(targetTable).setUIClass('no-data-found-list');
        targetTable.addUI(row);

        const td = new UICell(row).setUIClass('align-center')
            .setColspan(this.elementColumns.length + 1)
            .setUITextContent(i18n.msg('common.msg.noData'));
        row.addUICell(td);
    }

    // table DOM을 받아서 입력된 옵션 명과 옵션 값을 리스트로 반환
    getPropertyValue(optionTable) {
        const propertyValue = [];
        for (let row of optionTable.rows) {
            const tdList = row.cells;
            if (row.sectionRowIndex > 0) {
                let itemName = tdList[0].children[0].value || i18n.msg('form.properties.optionList.enterValue');
                let itemValue = tdList[1].children[0].value;
                propertyValue.push({name: itemName, value: itemValue});
            }
        }
        return propertyValue;
    }
}