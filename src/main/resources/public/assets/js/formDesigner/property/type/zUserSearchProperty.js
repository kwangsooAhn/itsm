/**
 * User Search Property Class
 *
 * zUserSearch 라이브러리를 사용하는 속성항목이다.
 *
 * @author Lim Ji Young <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { UIButton, UICell, UIDiv, UIInput, UIRow, UISelect, UISpan, UITable } from '../../../lib/zUI.js';
import ZProperty from '../zProperty.js';
import { zValidation } from '../../../lib/zValidation.js';

const propertyExtends = {
    selectOptions: [
        {name: i18n.msg('form.properties.userSearch.organization'), value: 'organization'},
        {name: i18n.msg('form.properties.userSearch.custom'), value:'custom'}
    ]
};
let targetUserArray = [];

export default class ZUserSearchProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'userSearchProperty', value, isAlwaysEditable);
        // 컴포넌트에 저장된 설정 값
        this.defaultValue = (value === '') ? value : JSON.parse(value);
        // 조회 대상 기준
        this.defaultCriteria = this.defaultValue.targetCriteria || propertyExtends.selectOptions[0].value;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우) 또는 (문서가 '사용/발행' 이고 연결된 업무흐름이 없는 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property').setUIProperty('--data-column', this.columnWidth);

        // 조회 대상 기준
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.UILabel.addUIClass('mt-3');
        this.UIElement.addUI(this.UIElement.UILabel);
        this.UIElement.UISelect = new UISelect()
            .setUIId('userSearch')
            .setUIAttribute('name', this.key)
            .setUIOptions(JSON.parse(JSON.stringify(propertyExtends.selectOptions)))
            .setUIValue(this.defaultCriteria)
            .setUIAttribute('data-value', 'userSearch')
            .onUIChange(this.updateSearchCriteria.bind(this));
        this.UIElement.addUI(this.UIElement.UISelect);

        if (!this.isEditable) {
            this.UIElement.UISelect.addUIClass('readonly');
        }

        this.UIElement.UIGroup = new UIDiv().setUIClass('search-type vertical mt-3');
        this.UIElement.addUI(this.UIElement.UIGroup);
        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        this.updateSearchCriteria(this.UIElement.UISelect.getUIValue());
    }

    // 검색조건 변경 시, 하위항목을 업데이트한다.
    updateSearchCriteria(e) {
        // 검색조건 변경 시, 기존 조건 초기화
        const targetCriteria = typeof e === 'object' ? e.target.value : e;
        const targetGroup = this.UIElement.UIGroup;
        targetGroup.clearUI();

        // 검색조건 세부항목
        switch (targetCriteria) {
            // 부서별 조회
            case 'organization':
                targetGroup.UILabel = this.makeLabelProperty('form.properties.element.searchTarget');
                targetGroup.addUI(targetGroup.UILabel);
                targetGroup.UIInputButton = new UIDiv().setUIClass('flex-row');
                targetGroup.addUI(targetGroup.UIInputButton);
                targetGroup.UIInputButton.UIInput = new UIInput()
                    .setUIReadOnly(true)
                    .setUIId('searchTarget')
                    .setUIValue('')
                    .setUIAttribute('data-validation-required', 'true')
                    .onUIChange(this.updateProperty.bind(this));
                if (targetCriteria === this.defaultCriteria && this.defaultValue.searchKey) {
                    targetGroup.UIInputButton.UIInput
                        .setUIValue(this.defaultValue.searchKey[0].value)
                        .setUIAttribute('data-value', this.defaultValue.searchKey[0].id || '');
                }
                targetGroup.UIInputButton.addUI(targetGroup.UIInputButton.UIInput);
                if (this.isEditable) {
                    // small icon button
                    targetGroup.UIInputButton.UIIconButton = new UIButton()
                        .setUIClass('item-remove')
                        .setUIAttribute('tabindex', '-1')
                        .onUIClick(this.clearText.bind(this));
                    targetGroup.UIInputButton.UIIconButton.UIIcon = new UISpan()
                        .setUIClass('ic-remove');
                    targetGroup.UIInputButton.UIIconButton.addUI(targetGroup.UIInputButton.UIIconButton.UIIcon);
                    targetGroup.UIInputButton.addUI(targetGroup.UIInputButton.UIIconButton);
                    // button
                    targetGroup.UIInputButton.UIButton = new UIButton()
                        .setUIClass('btn__ic')
                        // .setUIAttribute('data-value', '')
                        .addUI(new UISpan().setUIClass('ic-search').addUIClass('bg-gray500'))
                        .onUIClick(this.openOrganizationData.bind(this));
                    targetGroup.UIInputButton.addUI(targetGroup.UIInputButton.UIButton);
                }
                break;
            // 대상목록 지정
            case 'custom':
                targetGroup.UILabel = this.makeLabelProperty('form.properties.userList');
                targetGroup.UILabel.domElement.firstChild.classList.add('pt-2');
                // 사용자 목록 추가 버튼
                targetGroup.UIButton = new UIButton()
                    .setUIClass('btn__ic')
                    .addUIClass('btn--theme-extra')
                    .addUIClass('float-right')
                    .setUIDisabled(!this.isEditable)
                    // 사용자 검색 모달
                    .onUIClick(this.openUserListModal.bind(this));
                targetGroup.UIButton.addUI(new UISpan().addUIClass('ic-plus'));
                targetGroup.UILabel.addUI(targetGroup.UIButton);
                targetGroup.addUI(targetGroup.UILabel);

                // 사용자 목록
                const userTable = new UITable().setUIClass('option-table');
                const header = new UIRow(userTable).setUIClass('option-table-header');
                userTable.addUIRow(header);

                const nameTD = new UICell(header).setUITextContent(i18n.msg('form.properties.userName'));
                // 제거 버튼 추가
                const removeTD = new UICell(header).setUICSSText('width: 15%;');
                header.addUICell(nameTD);
                header.addUICell(removeTD);

                targetGroup.userTable = userTable;
                targetGroup.addUI(targetGroup.userTable);

                // 사용자 목록 추가
               if (targetCriteria === this.defaultCriteria && !zValidation.isEmpty(this.defaultValue.searchKey)) {
                    this.addRow(this.defaultValue.searchKey);
                } else {
                    this.setEmptyTable(this.UIElement.UIGroup.userTable);
                }
                break;
            }

        this.updateProperty.call(this, e);
    }

    // 부서별 조회 tree
    openOrganizationData(e) {
        const selectedValue = this.UIElement.UIGroup.UIInputButton.UIInput.getUIAttribute('data-value');
        tree.load({
            view: 'modal',
            title: i18n.msg('department.label.deptList'),
            dataUrl: '/rest/organizations',
            target: 'treeList',
            source: 'organization',
            text: 'organizationName',
            nodeNameLabel: i18n.msg('common.msg.dataSelect', i18n.msg('department.label.deptName')),
            defaultIcon: '/assets/media/icons/tree/icon_tree_organization.svg',
            selectedValue: selectedValue,
            callbackFunc: (response) => {
                this.UIElement.UIGroup.UIInputButton.UIInput.setUIValue(response.textContent);
                this.UIElement.UIGroup.UIInputButton.UIInput.setUIAttribute('data-value', response.id);

                this.updateProperty.call(this, e);
            }
        });
    }

    // 조회대상 삭제
    clearText(e) {
        this.UIElement.UIGroup.UIInputButton.UIInput.setUIValue('');
        this.UIElement.UIGroup.UIInputButton.UIInput.setUIAttribute('data-value', '');

        this.updateProperty.call(this, e);
    }

    // 대상 목록 지정 - 사용자 검색 modal
    openUserListModal() {
        const targetUserModal = new modal({
            title: i18n.msg('form.properties.userList'),
            body: `<div class="target-user-list">` +
                `<input class="input ic-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
                `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
                `<span id="spanTotalCount" class="search-count"></span>` +
                `<div class="table-set" id="targetUserList"></div>` +
                `</div>`,
            classes: 'target-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'btn__text--box btn--theme-primary',
                bindKey: false,
                callback: (modal) => {
                    if (zValidation.isEmpty(targetUserArray)) {
                        zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                        return false;
                    } else {
                        this.addRow(targetUserArray);
                    }
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'btn__text--box btn--theme-secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: () => {
                document.getElementById('search').addEventListener('keyup', aliceJs.debounce ((e) => {
                    this.getTargetUserList(e.target.value, false);
                }), false);
                this.getTargetUserList(document.getElementById('search').value, true);
                // 기존 사용자 목록
                targetUserArray.length = 0;
                this.UIElement.UIGroup.userTable.rows.forEach( (row, idx) => {
                    const targetId = row.domElement.childNodes[0].getAttribute('data-value');
                    const targetName = row.domElement.childNodes[0].textContent;
                    if (targetId) {
                        targetUserArray.push({id: targetId, value: targetName});
                    }
                });
            }
        });
        targetUserModal.show();
    }

    getTargetUserList(search, showProgressbar) {
        let strUrl = '/users/substituteUsers?search=' + encodeURIComponent(search.trim())
            + '&from=&to=&userKey=&multiSelect=true';
        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            const targetUserList = document.getElementById('targetUserList');
            targetUserList.innerHTML = htmlData;
            OverlayScrollbars(targetUserList.querySelector('.table-body'), {className: 'scrollbar'});
            // 갯수 가운트
            aliceJs.showTotalCount(targetUserList.querySelectorAll('.table-row').length);
            // 체크 이벤트
            targetUserList.querySelectorAll('input[type=checkbox]').forEach((element) => {
                element.addEventListener('change', () => {
                    if (element.checked) {
                        targetUserArray.push({id: element.id, value: element.value});
                    } else {
                        targetUserArray = targetUserArray.filter((item) => item.id !== element.id);
                    }
                });
            })
            // 기존 선택값 표시
            targetUserArray.forEach( target => {
                const targetCheckBox = targetUserList.querySelector('input[id="' + target.id + '"]');
                if (!zValidation.isEmpty(targetCheckBox)) {
                    targetCheckBox.checked = true;
                }
            });

        });
    }

    // 대상 목록 추가
    addRow(targetList) {
        // 테이블 초기화
        const userListTable = this.UIElement.UIGroup.userTable;
        userListTable.clearUIRow().clearUI();
        const header = new UIRow(userListTable).setUIClass('option-table-header');
        userListTable.addUIRow(header);
        const nameTD = new UICell(header).setUITextContent(i18n.msg('form.properties.userName'));
        const removeTD = new UICell(header).setUICSSText('width: 15%;');
        header.addUICell(nameTD);
        header.addUICell(removeTD);

        targetList.forEach((target) => {
            const optionRow = new UIRow(userListTable).setUIClass('option-table-row');
            const addedNameTD = new UICell(optionRow)
                .setUIId('targetUser')
                .setUIAttribute('data-value', target.id)
                .setUITextContent(target.value);
            const addedRemoveTD = new UICell(optionRow).setUICSSText('width: 15%;');
            addedRemoveTD.removeButton = new UIButton()
                .setUIClass('btn__ic')
                .setUIDisabled(!this.isEditable)
                .onUIClick(this.removeRow.bind(this));
            addedRemoveTD.removeButton.addUI(new UISpan().setUIClass('ic-remove'));
            addedRemoveTD.addUI(addedRemoveTD.removeButton);
            optionRow.addUICell(addedNameTD);
            optionRow.addUICell(addedRemoveTD);
            userListTable.addUIRow(optionRow);
        });

        this.updateProperty.call(this);
    }

    // 대상 목록 row 삭제
    removeRow(e) {
        e.stopPropagation();
        e.preventDefault();

        const selectedRowIndex = e.target.parentNode?.parentNode?.rowIndex;
        this.UIElement.UIGroup.userTable.removeUIRow(this.UIElement.UIGroup.userTable.rows[selectedRowIndex]);

        // 데이터가 없을때
        if (this.UIElement.UIGroup.userTable.rows.length === 1) {
            this.setEmptyTable(this.UIElement.UIGroup.userTable);
        }

        this.updateProperty.call(this);
    }

    // 데이터가 없을 때
    setEmptyTable(target) {
        const row = new UIRow(target).setUIClass('no-data-found-list');
        const td = new UICell(row).setUIClass('align-center')
            .setColspan(target.rows[0].cells.length)
            .setUITextContent(i18n.msg('form.msg.noOption'));
        row.addUICell(td);
        target.addUIRow(row);

        this.updateProperty.call(this);
    }

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
    }
}
