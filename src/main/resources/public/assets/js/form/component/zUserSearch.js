/**
 * User Search Mixin
 *
 *
 * @author Lim Ji Young <jy.lim@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */

import ZCommonProperty from '../../formDesigner/property/type/zCommonProperty.js';
import ZUserSearchProperty from '../../formDesigner/property/type/ZUserSearchProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv, UIInput } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        defaultValueUserSearch: ''
    },
    validation: {
        required: false // 필수값 여부
    }
};

Object.freeze(DEFAULT_COMPONENT_PROPERTY);

export const userSearchMixin = {
    // 전달 받은 데이터와 기본 property merge
    initProperty() {
        // 엘리먼트 property 초기화
        this._element = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.element, this.data.element);
        this._validation = Object.assign({}, DEFAULT_COMPONENT_PROPERTY.validation, this.data.validation);
        this._value = this.data.value || '${default}';
        this._realTimeSelectedUser = '';
    },
    // component 엘리먼트 생성
    makeElement() {
        const defaultValues = this.value.split('|');
        const element = new UIDiv().setUIClass('z-element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInput = new UIInput()
            .setUIClass('z-input i-user-search text-ellipsis')
            .setUIId('userSearch' + this.id)
            .setUIValue((this.value === '${default}') ? '' : defaultValues[1])
            .setUIRequired(this.validationRequired)
            .setUIAttribute('data-user-search', (this.value === '${default}') ? '' : defaultValues[0])
            .setUIAttribute('data-validation-required', this.validationRequired)
            .setUIAttribute('oncontextmenu', 'return false;')
            .setUIAttribute('onkeypress', 'return false;')
            .setUIAttribute('onkeydown', 'return false;')
            .onUIClick(this.openUserSearchModal.bind(this))
            .onUIChange(this.updateValue.bind(this));

        element.addUI(element.UIInput);

        return element;
    },
    // DOM 객체가 모두 그려진 후 호출되는 이벤트 바인딩
    afterEvent() {
        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIInput.setUIReadOnly(true);
            this.UIElement.UIComponent.UIElement.UIInput.setUICSSText('pointer-events:none');
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('off') : '';
        }
    },
    // set, get
    set elementColumnWidth(width) {
        this._element.columnWidth = width;
        this.UIElement.UIComponent.UIElement.setUIProperty('--data-column', width);
        this.UIElement.UIComponent.UILabel.setUIProperty('--data-column', this.getLabelColumnWidth(this.labelPosition));
    },
    get elementColumnWidth() {
        return this._element.columnWidth;
    },
    // 컴포넌트에 설정된 검색조건
    set elementUserSearchTarget(value) {
        this._element.defaultValueUserSearch = value;
    },
    get elementUserSearchTarget() {
        return this._element.defaultValueUserSearch;
    },
    // 컴포넌트 > 모달에서 선택된 임시 값
    set realTimeSelectedUser(userKey) {
        this._realTimeSelectedUser = userKey;
    },
    get realTimeSelectedUser() {
        return this._realTimeSelectedUser;
    },
    set validation(validation) {
        this._validation = validation;
    },
    get validation() {
        return this._validation;
    },
    set validationRequired(boolean) {
        this._validation.required = boolean;
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
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();

        // 사용자 검색 결과로 들어간 내용이 있는지 this.value와 'data-user-search' 확인하고 값을 저장한다.
        const userSearchData = e.target.getAttribute('data-user-search');

        // 값이 입력되었을 경우 error 없애기
        if (zValidation.isRequired(userSearchData)) {
            zValidation.removeDOMElementError(e.target);
        }

        this.value = userSearchData + '|' + e.target.value;
    },

    // 사용자 선택 모달
    openUserSearchModal(e) {
        e.stopPropagation();
        // 설정된 properties에 따라 사용자 선택 모달이 생성된다.
        this.userSearch = new modal({
            title: this.labelText,
            body: `<div class="target-user-list">` +
                `<input class="z-input i-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
                `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
                `<span id="spanTotalCount" class="search-count"></span>` +
                `<div class="table-set" id="searchUserList"></div>` +
                `</div>`,
            classes: 'target-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    this.realTimeSelectedUser = '';
                    const selectedUserRadio = document.querySelector('input[type=radio]:checked');
                    if (selectedUserRadio === null) {
                        zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                        return false;
                    } else {
                        this.UIElement.UIComponent.UIElement.UIInput
                            .setUIValue(selectedUserRadio.value)
                            .setUIAttribute('data-user-search', selectedUserRadio.id);
                        this.UIElement.UIComponent.UIElement.UIInput.domElement.dispatchEvent(new Event('change'));
                    }
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'z-button secondary',
                bindKey: false,
                callback: (modal) => {
                    this.realTimeSelectedUser = '';
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: () => {
                document.getElementById('search').addEventListener('keyup', (e) => {
                    this.getUserList(e.target.value, false);
                });
                this.getUserList(document.getElementById('search').value, true);
                OverlayScrollbars(document.querySelector('.modal-content'), {className: 'scrollbar'});
            }
        });

        this.userSearch.show();

    },

    getUserList(search, showProgressbar) {
        const targetData = JSON.parse(this.elementUserSearchTarget);
        const targetCriteria = targetData.targetCriteria;
        let searchKeys = '';
        targetData.searchKey.forEach( (elem, index) => {
            searchKeys += (index > 0) ? '+' + elem.id : elem.id;
        });

        let strUrl = '/users/searchUsers?searchValue=' + encodeURIComponent(search.trim()) +
            '&targetCriteria=' + targetCriteria + '&searchKeys=' + searchKeys;
        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            const searchUserList = document.getElementById('searchUserList');
            searchUserList.innerHTML = htmlData;
            OverlayScrollbars(searchUserList.querySelector('.z-table-body'), {className: 'scrollbar'});
            // 갯수 가운트
            aliceJs.showTotalCount(searchUserList.querySelectorAll('.z-table-row').length);
            // 체크 이벤트
            searchUserList.querySelectorAll('input[type=radio]').forEach((element) => {
                element.addEventListener('change', () => {
                    this.realTimeSelectedUser = element.checked ? element.id : '';
                });
            });
            // 기존 선택값 표시
            const targetUserId = !zValidation.isEmpty(this.realTimeSelectedUser) ? this.realTimeSelectedUser
                : this.UIElement.UIComponent.UIElement.UIInput.domElement.getAttribute('data-user-search');
            if (targetUserId !== null && targetUserId !== '') {
                searchUserList.querySelector('input[id="' + targetUserId + '"]').checked = true;
            }
        });
    },
    // 세부 속성 조회
    getProperty() {
        const userSearchProperty = new ZUserSearchProperty('elementUserSearchTarget', 'element.searchTargetCriteria',
            this.elementUserSearchTarget);
        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(userSearchProperty),
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
            value: this._value,
            label: this._label,
            element: this._element,
            validation: this._validation
        };
    },
    // 발행을 위한 validation 체크
    validationCheckOnPublish() {
        const targetData = JSON.parse(this.elementUserSearchTarget);

        // 사용자 목록이 없을 떄
        if (zValidation.isEmpty(targetData.searchKey[0])) {
            zAlert.warning(i18n.msg('common.msg.required', i18n.msg('form.properties.userList')));
            return false;
        }
        // 조회 대상이 없을 떄
        if (zValidation.isEmpty(targetData.searchKey[0].value)) {
            zAlert.warning(i18n.msg('common.msg.required', i18n.msg('form.properties.element.searchTarget')));
            return false;
        }
        return true;
    }
};
