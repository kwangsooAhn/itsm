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
import ZUserSearchProperty from '../../formDesigner/property/type/zUserSearchProperty.js';
import ZGroupProperty from '../../formDesigner/property/type/zGroupProperty.js';
import ZLabelProperty from '../../formDesigner/property/type/zLabelProperty.js';
import ZSliderProperty from '../../formDesigner/property/type/zSliderProperty.js';
import ZSwitchProperty from '../../formDesigner/property/type/zSwitchProperty.js';
import ZDefaultValueSearchProperty from '../../formDesigner/property/type/zDefaultValueSearchProperty.js';
import { FORM } from '../../lib/zConstants.js';
import { UIDiv, UIInput } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';
import { ZSession } from '../../lib/zSession.js';

/**
 * 컴포넌트 별 기본 속성 값
 */
const DEFAULT_COMPONENT_PROPERTY = {
    element: {
        columnWidth: '10',
        defaultValue: {
            target: FORM.SEARCH_COMPONENT.USER_SEARCH,
            type: FORM.DEFAULT_VALUE_TYPE.NONE,
            data: ''
        },
        userSearchTarget: ''
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
        // 컴포넌트에 저장된 값
        const savedValues = this.value.split('|');
        // 컴포넌트에 설정된 기본 값
        const defaultValues = (!zValidation.isEmpty(this.elementDefaultValue.data))
            ? this.elementDefaultValue.data.split('|') : ['', '', ''];

        const element = new UIDiv().setUIClass('element')
            .setUIProperty('--data-column', this.elementColumnWidth);
        element.UIInput = new UIInput()
            .setUIAttribute('type', 'text')
            .setUIClass('ic-user-search text-ellipsis')
            .setUIId('userSearch' + this.id)
            .setUIValue((this.value === '${default}') ? defaultValues[1] : savedValues[1])
            .setUIRequired(this.validationRequired)
            .setUIAttribute('data-user-id', (this.value === '${default}') ? defaultValues[2] : savedValues[2])
            .setUIAttribute('data-user-search', (this.value === '${default}') ? defaultValues[0] : savedValues[0])
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
        // 사용자 검색 조건 미설정 여부
        const emptySearchTarget = zValidation.isEmpty(this.elementUserSearchTarget)
            || zValidation.isEmpty(JSON.parse(this.elementUserSearchTarget).searchKey[0].value);
        // 아래 조건에 모두 해당하는 경우 컴포넌트에 설정된 기본값을 적용합니다.
        // 1. 사용자 검색 조건이 설정되어 있고 | 2. 폼 디자이너가 아닐 경우
        if (!emptySearchTarget && !zValidation.isEmpty(this.elementDefaultValue.data)) {
            const defaultValue = this.elementDefaultValue;
            // 기본값 타입이 session 일 경우 세션값을 사용
            if (this.value === '${default}' && defaultValue.type === 'session') {
                const newElementDefaultValue = JSON.parse(JSON.stringify(defaultValue));
                newElementDefaultValue.data = `${ZSession.get('userKey')}|` +
            `${ZSession.get('userName')}|${ZSession.get('userId')}`;
                this.elementDefaultValue = newElementDefaultValue;
            }
            if (zValidation.isEmpty(document.querySelector('.form-main'))) {
                this.getUserList(this.elementDefaultValue.data.split('|')[2], false);
            }
        }

        // 신청서 양식 편집 화면에 따른 처리
        if (this.displayType === FORM.DISPLAY_TYPE.READONLY) {
            this.UIElement.UIComponent.UIElement.UIInput.setUIReadOnly(true);
            this.UIElement.UIComponent.UIElement.UIInput.setUICSSText('pointer-events:none');
            // 필수값 표시가 된 대상에 대해 Required off 처리한다.
            this.UIElement.UIComponent.UILabel.UIRequiredText.hasUIClass('on') ?
                this.UIElement.UIComponent.UILabel.UIRequiredText.removeUIClass('on').addUIClass('none') : '';
        }
        // 문서의 상태가 사용이 아닌 경우 = 신청서 진행 중이고
        // 신청서 양식 편집 화면에서 처리한 group 컴포넌트가 숨김이 아니며
        // 기본값이 '${default}' 이면 실제 값을 저장한다.
        if (!zValidation.isEmpty(this.parent) && !zValidation.isEmpty(this.parent.parent) &&
            !zValidation.isEmpty(this.parent.parent.parent) && this.parent.parent.parent.status !== FORM.STATUS.EDIT &&
            this.displayType === FORM.DISPLAY_TYPE.EDITABLE  && this.value === '${default}') {
            this.value = this.elementDefaultValue.data;
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
    // 컴포넌트에 설정된 기본 값
    set elementDefaultValue(value) {
        this._element.defaultValue = {
            target: this.type,
            type: value.type,
            data: value.data
        };
        // 컴포넌트에 설정된 기본값 정보
        const defaultData = (!zValidation.isEmpty(value.data)) ? value.data.split('|') : ['', '', ''];
        this.UIElement.UIComponent.UIElement.UIInput.setUIValue(defaultData[1])
            .setUIAttribute('data-user-search', defaultData[0])
            .setUIAttribute('data-user-id', defaultData[2]);
    },
    get elementDefaultValue() {
        return this._element.defaultValue;
    },
    // 컴포넌트에 설정된 검색조건
    set elementUserSearchTarget(value) {
        this._element.userSearchTarget = value;
    },
    get elementUserSearchTarget() {
        return this._element.userSearchTarget;
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
    // input box 값 변경시 이벤트 핸들러
    updateValue(e) {
        e.stopPropagation();
        e.preventDefault();

        // 사용자 검색 결과로 들어간 내용이 있는지 this.value와 'data-user-search' 확인하고 값을 저장한다.
        const userSearchData = e.target.getAttribute('data-user-search');
        const userId = e.target.getAttribute('data-user-id');

        // 값이 입력되었을 경우 error 없애기
        if (zValidation.isRequired(userSearchData)) {
            zValidation.removeDOMElementError(e.target);
        }

        this.value = `${userSearchData}|${e.target.value}|${userId}`;
    },

    // 사용자 선택 모달
    openUserSearchModal(e) {
        e.stopPropagation();
        // 설정된 properties에 따라 사용자 선택 모달이 생성된다.
        this.userSearch = new modal({
            title: this.labelText,
            body: `<div class="target-user-list">` +
                `<input class="ic-search col-5 mr-2" type="text" name="search" id="search" maxlength="100" ` +
                `placeholder="` + i18n.msg('user.label.userSearchPlaceholder') + `">` +
                `<span id="spanTotalCount" class="search-count"></span>` +
                `<div class="table-set" id="searchUserList"></div>` +
                `</div>`,
            classes: 'target-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'btn__text--box primary',
                bindKey: false,
                callback: (modal) => {
                    // 최근 선택값이 있는 경우, 해당 사용자 id와 이름을 전달한다.
                    if (zValidation.isEmpty(this.realTimeSelectedUser)) {
                        zAlert.warning(i18n.msg('form.msg.selectTargetUser'));
                        return false;
                    } else {
                        this.UIElement.UIComponent.UIElement.UIInput
                            .setUIAttribute('data-user-id', this.realTimeSelectedUser.split('|')[2])
                            .setUIAttribute('data-user-search', this.realTimeSelectedUser.split('|')[0])
                            .setUIValue(this.realTimeSelectedUser.split('|')[1]);
                        this.UIElement.UIComponent.UIElement.UIInput.domElement.dispatchEvent(new Event('change'));
                    }
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'btn__text--box secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: () => {
                this.realTimeSelectedUser = '';
                document.getElementById('search').addEventListener('keyup', aliceJs.debounce ((e) => {
                    this.getUserList(e.target.value, false);
                }), false);
                this.getUserList(document.getElementById('search').value, true);
                OverlayScrollbars(document.querySelector('.modal__dialog__body'), { className: 'scrollbar' });
            }
        });

        this.userSearch.show();

    },

    getUserList(search, showProgressbar) {
        let strUrl = '/users/searchUsers?searchValue=' + encodeURIComponent(search.trim());

        // 검색 조건이 있는 경우
        if (!zValidation.isEmpty(this.elementUserSearchTarget)) {
            const targetData = JSON.parse(this.elementUserSearchTarget);
            const targetCriteria = targetData.targetCriteria;
            let searchKeys = '';
            targetData.searchKey.forEach( (elem, index) => {
                searchKeys += (index > 0) ? '+' + elem.id : elem.id;
            });
            strUrl += '&targetCriteria=' + targetCriteria + '&searchKeys=' + searchKeys;
        }

        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            const searchUserList = document.getElementById('searchUserList');
            // 사용자 선택 모달 생성
            if (!zValidation.isEmpty(searchUserList)) {
                searchUserList.innerHTML = htmlData;
                OverlayScrollbars(searchUserList.querySelector('.table__body'), { className: 'scrollbar' });
                // 갯수 가운트
                aliceJs.showTotalCount(searchUserList.querySelectorAll('.table-row').length);
                // 체크 이벤트
                searchUserList.querySelectorAll('input[type=radio]').forEach((element) => {
                    element.addEventListener('change', () => {
                        const userId = element.getAttribute('data-user-id');
                        this.realTimeSelectedUser = element.checked ? `${element.id}|${element.value}|${userId}` : '';
                    });
                });
                // 기존 선택값 표시
                const targetId
                    = this.UIElement.UIComponent.UIElement.UIInput.domElement.getAttribute('data-user-search');
                const targetName
                    = this.UIElement.UIComponent.UIElement.UIInput.getUIValue();
                const targetUserId
                    = this.UIElement.UIComponent.UIElement.UIInput.domElement.getAttribute('data-user-id');
                this.realTimeSelectedUser = (this.elementDefaultValue.type !== FORM.DEFAULT_VALUE_TYPE.NONE)
                    ? `${targetId}|${targetName}|${targetUserId}` : '';
                const checkedTargetId = this.realTimeSelectedUser.split('|')[0];
                const targetRadio = searchUserList.querySelector('input[id="' + checkedTargetId + '"]');
                if (!zValidation.isEmpty(targetUserId) && !zValidation.isEmpty(targetRadio)) {
                    targetRadio.checked = true;
                }
            } else {
                // 기본값 사용자 조회
                const userListElem = new DOMParser().parseFromString(htmlData.toString(), 'text/html');
                if (!userListElem.querySelectorAll('.table-row').length) {
                    this.UIElement.UIComponent.UIElement.UIInput.setUIValue('')
                        .setUIAttribute('data-user-id', '')
                        .setUIAttribute('data-user-search', '');
                }
            }
        });
    },
    // 세부 속성 조회
    getProperty() {
        const defaultValueSearchProperty
            = new ZDefaultValueSearchProperty('elementDefaultValue', 'element.defaultValue', this.elementDefaultValue);
        defaultValueSearchProperty.help = 'form.help.search-default';

        const userSearchProperty
            = new ZUserSearchProperty('elementUserSearchTarget', 'element.searchTargetCriteria', this.elementUserSearchTarget);

        return [
            ...new ZCommonProperty(this).getCommonProperty(),
            ...new ZLabelProperty(this).getLabelProperty(),
            new ZGroupProperty('group.element')
                .addProperty(new ZSliderProperty('elementColumnWidth', 'element.columnWidth', this.elementColumnWidth))
                .addProperty(userSearchProperty)
                .addProperty(defaultValueSearchProperty),
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
