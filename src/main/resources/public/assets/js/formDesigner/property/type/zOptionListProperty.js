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
import { CLASS_PREFIX, FORM } from '../../../lib/zConstants.js';

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
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        const optionButtonGroup = new UIDiv().setUIClass(CLASS_PREFIX + 'button-list');
        // 옵션 추가 버튼
        const addButton = new UIButton()
            .setUIClass(CLASS_PREFIX + 'button-icon')
            .addUIClass('extra')
            .onUIClick(this.addRow.bind(this));
        const plusIcon = new UISpan().addUIClass(CLASS_PREFIX + 'icon').addUIClass('i-plus');
        addButton.addUI(plusIcon);

        // 옵션 삭제 버튼
        const removeButton = new UIButton()
            .setUIClass(CLASS_PREFIX + 'button-icon')
            .addUIClass('extra')
            .onUIClick(this.removeRow.bind(this));
        const minusIcon = new UISpan().addUIClass(CLASS_PREFIX + 'icon').addUIClass('i-minus');
        removeButton.addUI(minusIcon);

        optionButtonGroup.addUI(addButton);
        optionButtonGroup.addUI(removeButton);
        this.UIElement.UIButtonGroup = optionButtonGroup;
        this.UIElement.addUI(this.UIElement.UIButtonGroup);

        // 옵션 리스트
        const optionTable = new UITable()
            .setUIClass(CLASS_PREFIX + 'option-table')
            .addUIClass('mt-2')
            .setUICSSText('border: 1px solid #000; border-collapse: collapse');

        const header = new UIRow(optionTable).setUIClass(CLASS_PREFIX + 'option-table-header');
        optionTable.addUIRow(header);

        const checkTD = new UICell(header).setUICSSText('width: 20%; border: 1px solid #000');
        const nameTD = new UICell(header).setUICSSText('width: 40%; border: 1px solid #000').setUITextContent('항목 이름');
        const valueTD = new UICell(header).setUICSSText('width: 40%; border: 1px solid #000').setUITextContent('항목 값');
        header.addUICell(checkTD);
        header.addUICell(nameTD);
        header.addUICell(valueTD);

        this.value.forEach((option) => {
            const optionRow = new UIRow(optionTable).setUIClass(CLASS_PREFIX + 'option-table-row');
            optionTable.addUIRow(this.makeRow(optionRow, option));
        });

        this.UIElement.UIOptionTable = optionTable;
        this.UIElement.addUI(this.UIElement.UIOptionTable);
        return this.UIElement;
    }

    makeRow(optionRow, option) {
        const checkTD = new UICell(optionRow).setUICSSText('width: 20%; border: 1px solid #000');
        checkTD.checkBox =  new UICheckbox(false);
        checkTD.addUI(checkTD.checkBox);

        const nameTD = new UICell(optionRow).setUICSSText('width: 40%; border: 1px solid #000');
        nameTD.inputName =  new UIInput().setUIValue(option.name).onUIChange(this.updateProperty.bind(this));
        nameTD.addUI(nameTD.inputName);

        const valueTD = new UICell(optionRow).setUICSSText('width: 40%; border: 1px solid #000');
        valueTD.inputValue =  new UIInput().setUIValue(option.value).onUIChange(this.updateProperty.bind(this));
        valueTD.addUI(valueTD.inputValue);

        optionRow.addUICell(checkTD);
        optionRow.addUICell(nameTD);
        optionRow.addUICell(valueTD);

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
        const optionRow = new UIRow(optionTable).setUIClass(CLASS_PREFIX + 'option-table-row');
        optionTable.addUIRow(this.makeRow(optionRow, FORM.DEFAULT_OPTION_ROW));
        this.panel.update.call(this.panel, this.key, this.getPropertyValue(this.UIElement.UIOptionTable.domElement));
    }

    // 옵션 삭제 버튼 클릭 이벤트
    removeRow(e) {
        e.stopPropagation();
        e.preventDefault();

        const optionTable = this.UIElement.UIOptionTable.domElement;
        for (let row of optionTable.rows) {
            const tdList = row.cells;
            if (row.sectionRowIndex > 0 && tdList[0].children[0].checked) {
                this.UIElement.UIOptionTable.removeUIRow(this.UIElement.UIOptionTable.rows[row.sectionRowIndex]);
            }
        }

        this.panel.update.call(this.panel, this.key, this.getPropertyValue(optionTable));
    }

    // table DOM을 받아서 입력된 옵션 명과 옵션 값을 리스트로 반환
    getPropertyValue(optionTable) {
        const propertyValue = [];
        for (let row of optionTable.rows) {
            const tdList = row.cells;
            if (row.sectionRowIndex > 0) {
                propertyValue.push({name: tdList[1].children[0].value, value: tdList[2].children[0].value});
            }
        }
        return propertyValue;
    }
}