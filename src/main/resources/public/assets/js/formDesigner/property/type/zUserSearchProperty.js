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

const propertyExtends = {
    selectOptions: [
        {name: i18n.msg('form.properties.userSearch.organization'), value: 'organization'},
        {name: i18n.msg('form.properties.userSearch.custom'), value:'custom'}
    ]
};

const selectedTargetUserList = []; // 기존에 선택된 사용자 목록

export default class ZUserSearchProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'userSearchProperty', value, isAlwaysEditable);

        // 선택된 사용자 검색 조건
        // this.options = propertyExtends.options;
        this.selectOptions = propertyExtends.selectOptions;
    }
    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);
        const defaultCriteria = this.value ? this.value : this.selectOptions[0].value;

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);

        // 조회 대상 기준
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.UILabel.addUIClass('mt-3');
        this.UIElement.addUI(this.UIElement.UILabel);
        this.UIElement.UISelect = new UISelect()
            .setUIId('userSearch')
            .setUIAttribute('name', this.key)
            .setUIOptions(JSON.parse(JSON.stringify(this.selectOptions)))
            .setUIValue(defaultCriteria)
            .setUIAttribute('data-value', 'userSearch')
            .onUIChange(this.updateSearchCriteria.bind(this));
        this.UIElement.addUI(this.UIElement.UISelect);

        if (!this.isEditable) {
            this.UIElement.UISelect.addUIClass('readonly');
        }

        // 조회대상
        // 조회대상 - 그룹
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
        const searchTargetCriteria = typeof e === 'object' ? e.target.value : e;
        const searchTargetGroup = this.UIElement.UIGroup;
        searchTargetGroup.clearUI();
        switch (searchTargetCriteria) {
            // 부서별 조회
            case 'organization':
                // input + button
                searchTargetGroup.UILabel = this.makeLabelProperty('form.properties.element.searchTarget');
                searchTargetGroup.addUI(searchTargetGroup.UILabel);
                searchTargetGroup.UIInputButton = new UIDiv().setUIClass('flex-row z-input-button');
                searchTargetGroup.addUI(searchTargetGroup.UIInputButton);
                const organizationId = '';
                const organizationName = '';
                searchTargetGroup.UIInputButton.UIInput = new UIInput()
                    .setUIReadOnly(true)
                    .setUIId('searchTarget')
                    .setUIAttribute('data-value', organizationId)
                    .setUIValue(organizationName)
                    .onUIChange(this.updateProperty.bind(this));
                searchTargetGroup.UIInputButton.addUI(searchTargetGroup.UIInputButton.UIInput);
                // small icon button
                searchTargetGroup.UIInputButton.UIIconButton = new UIButton()
                    .setUIClass('z-button-icon-sm')
                    .setUIAttribute('tabindex', '-1')
                    .onUIClick(this.clearText.bind(this));
                searchTargetGroup.UIInputButton.UIIconButton.UIIcon = new UISpan()
                    .setUIClass('z-icon')
                    .addUIClass('i-remove');
                searchTargetGroup.UIInputButton.UIIconButton.addUI(searchTargetGroup.UIInputButton.UIIconButton.UIIcon);
                searchTargetGroup.UIInputButton.addUI(searchTargetGroup.UIInputButton.UIIconButton);
                // button
                searchTargetGroup.UIInputButton.UIButton = new UIButton()
                    .setUIClass('z-button-icon')
                    .addUIClass('z-button-code')
                    // .setUIAttribute('data-value', '')
                    .addUI(new UISpan().setUIClass('z-icon').addUIClass('i-search'))
                    .onUIClick(this.openOrganizationData.bind(this));
                searchTargetGroup.UIInputButton.addUI(searchTargetGroup.UIInputButton.UIButton);
                break;
            // 대상목록 지정
            case 'custom':
                searchTargetGroup.UILabel = this.makeLabelProperty('form.properties.userList');
                searchTargetGroup.UILabel.domElement.firstChild.classList.add('pt-2');
                // 사용자 목록 추가 버튼
                searchTargetGroup.UIButton = new UIButton()
                    .setUIClass('z-button-icon')
                    .addUIClass('extra')
                    .addUIClass('float-right')
                    .setUIDisabled(!this.isEditable)
                    // 사용자 검색 모달
                    .onUIClick(this.openUserListModal.bind(this));
                searchTargetGroup.UIButton.addUI(new UISpan().addUIClass('z-icon').addUIClass('i-plus'));
                searchTargetGroup.UILabel.addUI(searchTargetGroup.UIButton);
                searchTargetGroup.addUI(searchTargetGroup.UILabel);

                // 사용자 목록
                const userTable = new UITable().setUIClass('z-option-table');
                const header = new UIRow(userTable).setUIClass('z-option-table-header');
                userTable.addUIRow(header);

                const nameTD = new UICell(header).setUITextContent(i18n.msg('form.properties.userName'));
                // 제거 버튼 추가
                const removeTD = new UICell(header).setUICSSText('width: 15%;');
                header.addUICell(nameTD);
                header.addUICell(removeTD);

                searchTargetGroup.userTable = userTable;
                searchTargetGroup.addUI(searchTargetGroup.userTable);

                // todo : 데이터가 없는 경우
                this.setEmptyTable(this.UIElement.UIGroup.userTable);
                break;
        }
    }

    // 부서별 조회 tree
    openOrganizationData() {
        const selectedValue = this.UIElement.UIGroup.UIInputButton.UIInput.getUIAttribute('data-value');
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
                this.UIElement.UIGroup.UIInputButton.UIInput.setUIAttribute('data-value', response.id);
                this.updateProperty.call(this);
            }
        });
    }

    // 사용자 검색 modal
    openUserListModal() {
        const targetUserModal = new modal({
            title: i18n.msg('form.properties.userList'),
            body: `<div class="sub-user-list">` +
                `<input class="z-input i-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
                `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
                `<span id="spanTotalCount" class="search-count"></span>` +
                `<div class="table-set" id="targetUserList"></div>` +
                `</div>`,
            classes: 'target-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    const selectedTargetUser = document.getElementById('targetUserList')
                        .querySelectorAll('input[type=checkbox]:checked');
                    if (selectedTargetUser.length === 0) {
                        zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                        return false;
                    } else {
                        // selectedTargetUser.forEach((target) => {
                        //     this.addRow(target.id, target.value);
                        // });
                        this.addRow(selectedTargetUser);
                    }
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
            onCreate: () => {
                document.getElementById('search').addEventListener('keyup', (e) => {
                    this.getTargetUserList(e.target.value, false);
                });
                this.getTargetUserList(document.getElementById('search').value, true);
            }
        });
        targetUserModal.show();
    }

    getTargetUserList(search, showProgressbar) {
        let strUrl = '/users/view-pop/users?search=' + encodeURIComponent(search.trim())
            + '&from=&to=&userKey=&multiSelect=true';
        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            document.getElementById('targetUserList').innerHTML = htmlData;
            OverlayScrollbars(document.querySelector('.z-table-body'), {className: 'scrollbar'});
            // 갯수 가운트
            const targetUserList = document.getElementById('targetUserList');
            aliceJs.showTotalCount(targetUserList.querySelectorAll('.z-table-row').length);
            // 기존 선택값 표시
            this.UIElement.UIGroup.userTable.rows.forEach( (row, idx) => {
                if (idx > 0 && row.domElement.childNodes[0].getAttribute('data-value')) {
                    const target = row.domElement.childNodes[0].getAttribute('data-value');
                    const checkElem = targetUserList.querySelector('input[id="' + target + '"]');
                    if (checkElem) {
                        checkElem.checked = true;
                    }
                }
            });
        });
    }

    // 속성 변경시 발생하는 이벤트 핸들러
    updateProperty(elem) {


    }

    // 조회대상 삭제
    clearText(e) {
        this.UIElement.UIGroup.UIInputButton.UIInput.setUIValue('');
        this.UIElement.UIGroup.UIInputButton.UIInput.setUIAttribute('data-value', '');
        this.updateProperty.call(this, e);
    }

    // 옵션 추가 버튼 클릭 이벤트
    addRow(targetList) {
        // 테이블 초기화
        const userListTable = this.UIElement.UIGroup.userTable;
        userListTable.clearUIRow().clearUI();
        const header = new UIRow(userListTable).setUIClass('z-option-table-header');
        userListTable.addUIRow(header);
        const nameTD = new UICell(header).setUITextContent(i18n.msg('form.properties.userName'));
        const removeTD = new UICell(header).setUICSSText('width: 15%;');
        header.addUICell(nameTD);
        header.addUICell(removeTD);

        targetList.forEach((target) => {
            const optionRow = new UIRow(userListTable).setUIClass('z-option-table-row');
            const addedNameTD = new UICell(optionRow).setUITextContent(target.value).setUIAttribute('data-value', target.id);
            const addedRemoveTD = new UICell(optionRow).setUICSSText('width: 15%;');
            addedRemoveTD.removeButton = new UIButton()
                .setUIClass('z-button-icon')
                .setUIDisabled(!this.isEditable)
                .onUIClick(this.removeRow.bind(this));
            addedRemoveTD.removeButton.addUI(new UISpan().setUIClass('z-icon').addUIClass('i-remove'));
            addedRemoveTD.addUI(addedRemoveTD.removeButton);
            optionRow.addUICell(addedNameTD);
            optionRow.addUICell(addedRemoveTD);
            userListTable.addUIRow(optionRow);
        });
        // todo: 선택된 사용자 정보 저장

    }

    // 옵션 삭제 버튼 클릭 이벤트
    removeRow(e) {
        e.stopPropagation();
        e.preventDefault();

        const selectedRowIndex = e.target.parentNode?.parentNode?.rowIndex;
        this.UIElement.UIGroup.userTable.removeUIRow(this.UIElement.UIGroup.userTable.rows[selectedRowIndex]);

        // 데이터가 없을때
        if (this.UIElement.UIGroup.userTable.rows.length === 1) {
            this.setEmptyTable(this.UIElement.UIGroup.userTable);
        }
        // todo: 삭제된 사용자 정보 저장

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
}
