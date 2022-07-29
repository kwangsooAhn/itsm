/**
 * Default Value Search Property Class
 *
 * Search modal 을 사용하는 컴포넌트 들의 기본값을 어떤 방식으로 제공할 지 선택하는 속성항목이다.
 *
 * @author Lim Ji Young <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { FORM } from '../../../lib/zConstants.js';
import { UIButton, UIDiv, UILabel, UIRadioButton, UISpan, UIInput } from '../../../lib/zUI.js';
import { zValidation } from '../../../lib/zValidation.js';
import { ZSession } from '../../../lib/zSession.js';
import ZProperty from '../zProperty.js';

const propertyExtends = {
    options: [
        {name: 'form.properties.option.none', value: FORM.DEFAULT_VALUE_TYPE.NONE},
        {name: 'form.properties.default.session', value: FORM.DEFAULT_VALUE_TYPE.SESSION},
        {name: 'form.properties.default.custom', value: FORM.DEFAULT_VALUE_TYPE.CUSTOM}
    ]
};

export default class ZDefaultValueSearchProperty extends ZProperty {
    constructor(key, name, value, isAlwaysEditable) {
        super(key, name, 'ZDefaultValueSearchProperty', value, isAlwaysEditable);

        this.options = propertyExtends.options;
        // 대상 컴포넌트
        this.targetComponent = this.value.target;
        // 기본값 타입
        this.defaultType = this.value.type;
        // 기본 설정 값. 해당 값이 없는 경우 빈 array를 생성합니다. (defaultSearchValues = key|id|name|(realTimeSelected))
        this.data = (!zValidation.isEmpty(this.value.data)) ? this.value.data.split('|') : ['', '', ''];
    }

    // DOM Element 생성
    makeProperty(panel) {
        this.panel = panel;
        // 속성 편집 가능여부 체크 - 문서가 '편집'이거나 또는 (문서가 '사용/발행' 이고 항시 편집 가능한 경우)
        this.isEditable = this.panel.editor.isEditable || (!this.panel.editor.isDestory && this.isAlwaysEditable);

        this.UIElement = new UIDiv().setUIClass('property')
            .setUIProperty('--data-column', this.columnWidth);
        // 라벨
        this.UIElement.UILabel = this.makeLabelProperty();
        this.UIElement.addUI(this.UIElement.UILabel);

        // 기본 값 radio group
        this.UIElement.UIGroup = new UIDiv().setUIClass('default-type-radio');
        this.UIElement.addUI(this.UIElement.UIGroup);

        this.options.forEach((item, idx) => {
            const radioGroup = new UIDiv().setUIClass('radio-property-group').addUIClass('vertical');
            const radioId = item.value.substr(0, 1).toUpperCase() + item.value.substr(1, item.value.length) +
                ZWorkflowUtil.generateUUID();
            // 라벨
            radioGroup.UILabel = new UILabel()
                .setUIClass('radio')
                .addUIClass('mb-1')
                .setUIFor('radioProperty' + radioId);
            radioGroup.addUI(radioGroup.UILabel);
            // 라디오 버튼
            radioGroup.UILabel.UIRadio = new UIRadioButton(this.defaultType === item.value)
                .setUIId('radioProperty' + radioId)
                .setUIAttribute('name', this.key)
                .setUIClass('hidden-radio')
                .setUIAttribute('data-value', item.value)
                .onUIChange(this.updateProperty.bind(this));
            radioGroup.UILabel.addUI(radioGroup.UILabel.UIRadio);
            radioGroup.UILabel.addUI(new UISpan());

            if (!this.isEditable) {
                radioGroup.UILabel.addUIClass('readonly');
                radioGroup.UILabel.UIRadio.addUIClass('readonly');
            }

            if (!zValidation.isEmpty(item.name)) {
                radioGroup.UILabel.addUI(new UISpan().setUIClass('label').setUIInnerHTML(i18n.msg(item.name)));
            }
            // 기본값 - 지정 타입 추가 설정 (inputButton)
            if (item.value === FORM.DEFAULT_VALUE_TYPE.CUSTOM) {
                this.data.forEach((elem, idx) => {
                    this.data[idx] = (this.defaultType === item.value) ? elem : '';
                });
                radioGroup.UIInputButton = new UIDiv()
                    .setUIClass('flex-row input--remove');
                radioGroup.addUI(radioGroup.UIInputButton);
                // input
                radioGroup.UIInputButton.UIInput = new UIInput()
                    .setUIReadOnly(true)
                    .setUIId('customValue')
                    .setUIAttribute('data-search-value', this.data[0])
                    .setUIAttribute('data-value', this.data[2])
                    .setUIValue(this.data[1])
                    .onUIChange(this.updateProperty.bind(this));
                // 컴포넌트 타입 별로 필요한 속성값 추가
                switch (this.targetComponent) {
                    case FORM.SEARCH_COMPONENT.USER_SEARCH:

                        break;
                }
                radioGroup.UIInputButton.addUI(radioGroup.UIInputButton.UIInput);
                if (this.isEditable) {
                    // small icon button
                    radioGroup.UIInputButton.UIIconButton = new UIButton()
                        .setUIClass('item-remove')
                        .setUIAttribute('tabindex', '-1')
                        .onUIClick(this.clearText.bind(this));
                    radioGroup.UIInputButton.UIIconButton.UIIcon = new UISpan()
                        .setUIClass('ic-remove');
                    radioGroup.UIInputButton.UIIconButton.addUI(radioGroup.UIInputButton.UIIconButton.UIIcon);
                    radioGroup.UIInputButton.addUI(radioGroup.UIInputButton.UIIconButton);
                    // button
                    radioGroup.UIInputButton.UIButton = new UIButton()
                        .setUIClass('btn__ic')
                        .addUI(new UISpan().setUIClass('ic-search').addUIClass('bg-gray500'))
                        .onUIClick(this.openDataSelect.bind(this));
                    radioGroup.UIInputButton.addUI(radioGroup.UIInputButton.UIButton);
                }
            }
            this.UIElement.UIGroup['UIRadioGroup' + idx] = radioGroup;
            this.UIElement.UIGroup.addUI(radioGroup);
        });
        return this.UIElement;
    }

    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {}

    // 코드 선택 모달
    openDataSelect(e) {
        e.stopPropagation();

        // target
        const targetInput = e.target.parentNode.parentNode.childNodes[0];
        // 컴포넌트 종류에 따라 알맞은 modal 을 생성한다.
        switch (this.targetComponent) {
            case FORM.SEARCH_COMPONENT.USER_SEARCH:
                const userModalTemplate = `<div class="target-user-list">` +
                    `<input class="ic-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
                    `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
                    `<span id="spanTotalCount" class="search-count"></span>` +
                    `<div id="searchUserList"></div>` +
                    `</div>`;

                const userSearchModal = new modal({
                    title: i18n.msg('user.label.user') + ' ' + i18n.msg('form.properties.default.custom'),
                    body: userModalTemplate,
                    classes: 'target-user-modal',
                    buttons: [{
                        content: i18n.msg('common.btn.select'),
                        classes: 'btn__text--box primary',
                        bindKey: false,
                        callback: (modal) => {
                            // 최근 선택값이 있는 경우, 해당 사용자 id와 이름을 전달한다.
                            if (zValidation.isEmpty(this.data[3])) {
                                zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                                return false;
                            } else {
                                targetInput.setAttribute('data-search-value', this.data[3].split('|')[0])
                                targetInput.setAttribute('data-value', this.data[3].split('|')[2]);
                                targetInput.value = this.data[3].split('|')[1];
                                targetInput.dispatchEvent(new Event('change'));
                            }
                            // 임시값 제거
                            this.data.pop();
                            modal.hide();
                        }
                    }, {
                        content: i18n.msg('common.btn.cancel'),
                        classes: 'btn__text--box secondary',
                        bindKey: false,
                        callback: (modal) => {
                            // 임시값 제거
                            this.data.pop();
                            modal.hide();
                        }
                    }],
                    close: {
                        closable: false,
                    },
                    onCreate: () => {
                        // Realtime Selected User Key
                        this.data.push(targetInput.getAttribute('data-search-value') || '');
                        document.getElementById('search').addEventListener('keyup', (e) => {
                            this.getUserList(e.target.value, false);
                        });

                        this.getUserList(document.getElementById('search').value, true);
                        OverlayScrollbars(document.querySelector('.modal__dialog__body'), {className: 'scrollbar'});
                    }
                });

                userSearchModal.show();
                break;
            case FORM.SEARCH_COMPONENT.ORGANIZATION_SEARCH:
                tree.load({
                    view: 'modal',
                    title: i18n.msg('organization.label.organization') + ' ' + i18n.msg('form.properties.default.custom'),
                    dataUrl: '/rest/organizations',
                    target: 'treeList',
                    source: 'organization',
                    text: 'organizationName',
                    nodeNameLabel: i18n.msg('common.msg.dataSelect', i18n.msg('department.label.deptName')),
                    defaultIcon: '/assets/media/icons/tree/icon_tree_organization.svg',
                    selectedValue: targetInput.getAttribute('data-value'),
                    callbackFunc: (response) => {
                        targetInput.setAttribute('data-search-value', response.id)
                        targetInput.setAttribute('data-value', response.id);
                        targetInput.value = response.textContent;
                        targetInput.dispatchEvent(new Event('change'));
                    }
                });
                break;
        }
    }

    // 지정 - 사용자 목록
    getUserList(search, showProgressbar) {
        aliceJs.fetchText('/users/searchUsers?searchValue=' + encodeURIComponent(search.trim()), {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            const searchUserList = document.getElementById('searchUserList');
            // 사용자 선택 리스트 생성
            searchUserList.innerHTML = htmlData;
            OverlayScrollbars(searchUserList.querySelector('.tbl__body'), {className: 'scrollbar'});
            // 갯수 가운트
            aliceJs.showTotalCount(searchUserList.querySelectorAll('.table-row').length);
            // 체크 이벤트
            searchUserList.querySelectorAll('input[type=radio]').forEach((element) => {
                element.addEventListener('change', () => {
                    const userId = element.getAttribute('data-user-id');
                    this.data[3] = element.checked ? `${element.id}|${element.value}|${userId}` : '';
                });
            });
            // 기존 선택값 표시
            const checkedTargetId = this.data[3].includes('|') ? this.data[3].split('|')[0] : this.data[3];
            const targetRadio = searchUserList.querySelector('input[id="' + checkedTargetId + '"]');
            if (!zValidation.isEmpty(this.data[3]) && !zValidation.isEmpty(targetRadio)) {
                targetRadio.checked = true;
                this.data[3] = `${targetRadio.id}|${targetRadio.value}|${targetRadio.getAttribute('data-user-id')}`;
            }
        });
    }

    // 코드 삭제
    clearText(e) {
        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIAttribute('data-search-value', '');
        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIAttribute('data-value', '');
        this.UIElement.UIGroup['UIRadioGroup2'].UIInputButton.UIInput.setUIValue('');

        this.updateProperty.call(this, e);
    }

    // 속성 변경 시, 발생하는 이벤트 핸들러
    updateProperty(e) {
        if (e && e.preventDefault) {
            e.preventDefault();

            const elem = e.target || e;

            const parentElem = elem.parentNode.parentNode;
            const curRadioElem = parentElem.querySelector('input[type=radio]:checked');
            const customInputElem = parentElem.querySelector('input[type=text]');
            const radioType = curRadioElem.getAttribute('data-value');
            // 업데이트 할 컴포넌트 설정 값
            const defaultValue = {
                target: this.targetComponent,
                type: radioType,
                data: ''
            }

            switch (radioType) {
                case FORM.DEFAULT_VALUE_TYPE.NONE:
                    this.panel.update.call(this.panel, this.key, defaultValue);
                    break;
                case FORM.DEFAULT_VALUE_TYPE.SESSION:
                    if (this.targetComponent === FORM.SEARCH_COMPONENT.USER_SEARCH) {
                        // 사용자 검색 컴포넌트
                        defaultValue.data = ZSession.get('userKey') + '|' + ZSession.get('userName') + '|' + ZSession.get('userId');
                    } else {
                        // 부서 검색 컴포넌트
                        defaultValue.data = ZSession.get('department') + '|' + ZSession.get('departmentName');
                    }
                    this.panel.update.call(this.panel, this.key, defaultValue);
                    break;
                case FORM.DEFAULT_VALUE_TYPE.CUSTOM:
                    // 지정 - 선택 값이 있을 경우
                    if (!zValidation.isEmpty(customInputElem.value)) {
                        defaultValue.data = customInputElem.getAttribute('data-search-value') + '|'
                         + customInputElem.value + '|' + customInputElem.getAttribute('data-value');
                    }
                    this.panel.update.call(this.panel, this.key, defaultValue);
                    break;
            }

        }
    }
}
