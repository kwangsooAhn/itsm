/**
 * 문서함 폼 그리기 Class
 *
 * 폼은 폼 디자이너, 신청서, 문서함등에서 그려지며, 약간씩 차이가 있다.
 * 여기서는 문서함에서 열게 되는 처리할 문서, 진행중 문서, 완료된 문서등을 대상으로 폼을 그린다.
 *
 * @author jung hee chan (hcjung@brainz.co.kr)
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import ZComponent from '../form/zComponent.js';
import ZForm from '../form/zForm.js';
import ZGroup from '../form/zGroup.js';
import ZRow from '../form/zRow.js';
import { DOCUMENT, FORM, SESSION } from '../lib/zConstants.js';
import { zValidation } from '../lib/zValidation.js';

class ZFormToken {
    constructor() {}
    /**
     * 클래스 초기화
     *
     * @param formDataJson 그리고자 하는 폼에 대한 JSON 데이터
     * @param editable 편집 가능여부
     */
    init(formDataJson, editable) {
        this.domElement = document.getElementById('documentDrawingBoard'); // 문서 엘리먼트
        this.propertiesElement = document.getElementById('documentProperties'); // 우측 문서 정보, 의견, 태그가 표시되는 엘리먼트
        this.data = formDataJson;
        this.formDataJson = this.data.form;
        this.editable = editable;
        // 정렬
        this.sortFormObject(this.formDataJson);
        // 화면 출력
        this.makeForm(this.formDataJson);
        this.makeTab();
    }
    /**
     * Form 의 구성요소 3가지(Group, Row, Component)를 출력 순서로 정렬한다.
     *
     * @param formObject JSON 데이터
     */
    sortFormObject(formObject) {
        if (Object.prototype.hasOwnProperty.call(formObject, 'group')) {
            formObject.group.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            formObject.group.forEach( (g) => {
                this.sortFormObject(g);
            });
        } else if (Object.prototype.hasOwnProperty.call(formObject, 'row')) {
            formObject.row.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            formObject.row.forEach( (r) => {
                this.sortFormObject(r);
            });
        } else {
            formObject.component.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
        }
    }
    /**
     * FORM 생성 (Recursive)
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    makeForm(data, parent, index) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            this.form = this.addObjectByType(FORM.LAYOUT.FORM, data);
            this.form.parent = parent;
            this.domElement.appendChild(this.form.UIElement.domElement);
            this.form.afterEvent();

            data.group.forEach( (g, gIndex) => {
                this.makeForm(g, this.form, gIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            const group = this.addObjectByType(FORM.LAYOUT.GROUP, data, parent, index);
            data.row.forEach( (r, rIndex) => {
                this.makeForm(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            data.component.forEach( (c, cIndex) => {
                this.makeForm(c, row, cIndex);
            });
        } else { // component
            this.addObjectByType(FORM.LAYOUT.COMPONENT, data, parent, index);
        }
    }
    /**
     * 탭 생성 : 우측 문서 정보, 의견, 태그 영역
     * @param tokenId 토큰 ID
     */
    makeTab() {
        // 탭 생성
        aliceJs.fetchText('/tokens/' + this.data.tokenId + '/edit-tab?mode=' + (this.editable ? 'edit' : 'view'), {
            method: 'GET'
        }).then((htmlData) => {
            this.propertiesElement.innerHTML = htmlData;

            // 탭 이벤트
            document.querySelectorAll('.z-token-tab').forEach((tab) => {
                tab.addEventListener('click', this.selectTokenTab, false);
            });

            const selectedTabId = sessionStorage.getItem('alice_token-tab-selected') ?
                sessionStorage.getItem('alice_token-tab-selected') : 'tokenInformation';
            document.querySelector('.z-token-tab[data-target-contents="' + selectedTabId + '"]').click();

            // 날짜 초기화
            this.setDateTimeFormat();

            // 스크롤바
            OverlayScrollbars(document.querySelectorAll('.z-token-panels'), { className: 'scrollbar' });

            // 디자인된 selectbox
            aliceJs.initDesignedSelectTag();

            // 태그 초기화
            new zTag(document.getElementById('tokenTags'), {
                suggestion: this.editable,
                realtime: this.editable,
                tagType: 'instance',
                targetId: this.data.instanceId
            });
        });
    }
    /**
     * form, group, row, component 타입에 따른 객체 추가
     * @param type form, group, row, component 타입
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    addObjectByType(type, data, parent, index) {
        let addObject = null; // 추가된 객체

        switch(type) {
            case FORM.LAYOUT.FORM:
                addObject = new ZForm(data);
                break;
            case FORM.LAYOUT.GROUP:
                addObject = new ZGroup(data);
                break;
            case FORM.LAYOUT.ROW:
                addObject = new ZRow(data);
                break;
            case FORM.LAYOUT.COMPONENT:
                addObject = new ZComponent(data);
                break;
            default:
                break;
        }
        if (parent && addObject) {
            parent.add(addObject, index);
            addObject.afterEvent();
        }

        return addObject;
    }
    /**
     * 컴포넌트 value 데이터 조회
     */
    getComponentData(object, array) {
        object.children.forEach((child) => {
            if (child instanceof ZComponent) {
                array.push({ componentId: child.id, value: (typeof child.value === 'object' ? JSON.stringify(child.value) : child.value) });
            } else {
                this.getComponentData(child, array);
            }
        });
        return array;
    }
    /**
     * 신청서 저장, 처리, 취소, 회수, 즉시 종료 등 동적 버튼 클릭시 호출됨
     */
    processAction(actionType) {
        // 유효성 체크
        const validationUncheckActionType = ['save', 'cancel', 'terminate', 'reject', 'withdraw'];
        if (!validationUncheckActionType.includes(actionType) && zValidation.hasDOMElementError(this.domElement)) {
            return false;
        }
        // DR 테이블, CI 테이블 필수인 경우 row를 1개 이상 등록하라는 경고 후 포커싱
        let validationCheck = true;
        const validationCheckTables = document.querySelectorAll('table[data-validation-required="true"]');
        for (let i = 0; i < validationCheckTables.length; i ++) {
            const table = validationCheckTables[i];
            if (table.rows.length === 2 && table.querySelector('.no-data-found-list')) {
                validationCheck = false;
                aliceAlert.alertWarning(i18n.msg('form.msg.failedAllColumnDelete'), function() {
                    table.focus();
                });
                return false;
            }
        }
        if (!validationCheck) { return false; }

        const saveData = {
            'documentId': this.data.documentId,
            'instanceId': this.data.instanceId,
            'tokenId': (zValidation.isDefined(this.data.tokenId) ? this.data.tokenId : ''),
            'isComplete': (actionType !== 'save'),
            'assigneeId' : (actionType === 'save') ? SESSION['userKey'] : '',
            'assigneeType' : (actionType === 'save') ? DOCUMENT.ASSIGNEE_TYPE : '',
            'action': actionType
        };
        // 컴포넌트 값
        saveData.componentData = this.getComponentData(this.form, []);
        console.debug(saveData);

        const actionMsg = (actionType === 'save') ? 'common.msg.save' : 'document.msg.process';
        const url = zValidation.isEmpty(saveData.tokenId) ? '/rest/tokens/data' :
            '/rest/tokens/' + saveData.tokenId + '/data';
        aliceJs.fetchText(url, {
            method: zValidation.isEmpty(saveData.tokenId) ? 'POST' : 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData)
        }).then(rtn => {
            if (rtn === 'true') {
                aliceAlert.alertSuccess(i18n.msg(actionMsg),  () => {
                    if (zValidation.isDefined(window.opener)) {
                        opener.location.reload();
                        window.close();
                    } else {
                        this.documentModal.hide();
                    }
                });
            } else {
                aliceAlert.alertDanger(i18n.msg('common.msg.fail'));
            }
        });
    }

    /**
     * 프로세스 맵 팝업 호출
     */
    openProcessStatusPopUp() {
        window.open('/process/' + this.data.instanceId + '/status', 'process_status_' + this.data.instanceId,
            'width=1300, height=500');
    }

    /**
     * 탭 선택시 이벤트 핸들러
     */
    selectTokenTab(e) {
        // 탭 동작
        Array.prototype.filter.call(e.target.parentNode.children, function (child) {
            return child !== e.target;
        }).forEach((siblingElement) => {
            siblingElement.classList.remove('active');
        });
        e.target.classList.add('active');

        // 컨텐츠 내용 동작
        const selectedTab = document.getElementById(e.target.dataset.targetContents);
        if (zValidation.isDefined(selectedTab)) {
            Array.prototype.filter.call(selectedTab.parentNode.children, function (child) {
                return child !== selectedTab;
            }).forEach((siblingElement) => {
                siblingElement.classList.remove('on');
            });
            selectedTab.classList.add('on');
        }

        // 선택된 탭을 저장 > 새로고침시 초기화를 막기 위함
        sessionStorage.setItem('alice_token-tab-selected', e.target.dataset.targetContents);
    }

    /**
     * 서버에서 전달받은 데이터의 날짜 포맷을 변경한다.
     */
    setDateTimeFormat() {
        document.querySelectorAll('.dateFormatFromNow').forEach((element) => {
            element.textContent = dateFormatFromNow(element.textContent);
        });

        document.querySelectorAll('.date-time').forEach((element) => {
            element.textContent = i18n.userDateTime(element.textContent);
        });
    }

    /**
     * 관련 문서 모달 오픈
     */
    openRelatedDocModal() {
        const relatedTokenModalTemplate = document.getElementById('relatedTokenModalTemplate');
        const relatedTokenModal = new modal({
            title: i18n.msg('token.label.relatedInstance'),
            body: relatedTokenModalTemplate.content.cloneNode(true),
            classes: 'token-list',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    this.saveRelatedDoc(modal);
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
                    this.getRelatedDoc(e.target.value, false);
                });
                this.getRelatedDoc(document.getElementById('search').value, true);
            }
        });
        relatedTokenModal.show();
    }
    /**
     * 관련 문서 조회
     * @param search 검색어
     * @param showProgressbar ProgressBar 표시여부
     */
    getRelatedDoc(search, showProgressbar) {
        aliceJs.fetchText('/tokens/view-pop/documents?searchValue=' + search.trim() + '&tokenId=' + this.data.tokenId, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            document.getElementById('instanceList').innerHTML = htmlData;
            aliceJs.showTotalCount(document.querySelectorAll('.instance-list').length);
            // 서버에서 전달받은 데이터의 날짜 포맷을 변경
            this.setDateTimeFormat();
            // 스크롤바
            OverlayScrollbars(document.querySelector('.z-list-body'), { className: 'scrollbar' });
        });
    }

    /**
     * 관련 문서 저장
     */
    saveRelatedDoc(target) {
        let checked = document.querySelectorAll('input[name=chk]:checked');
        if (checked.length === 0) {
            aliceAlert.alertWarning(i18n.msg('token.msg.selectToken'));
        } else {
            let jsonArray = [];
            for (let i = 0; i < checked.length; i++) {
                let jsonObject = {};
                jsonObject.folderId = this.data.folderId;
                jsonObject.instanceId = checked[i].value;
                jsonArray.push(jsonObject);
            }
            aliceJs.fetchText('/rest/folders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jsonArray)
            }).then((rtn) => {
                if (rtn === 'true') {
                    // TODO: 관련 문서 추가시 화면을 전체 그려주는 구조 변경 필요
                    // 서버 단일 조회 구현 필요 > WfFolderRepositoryImpl.kt - findRelatedDocumentListByTokenId 로직 확인 필요.. (조인하는게 너무 많아서 구조가 이상함)
                    this.makeTab();
                    target.hide();
                } else {
                    aliceAlert.alertDanger(i18n.msg('common.msg.fail'));
                }
            });
        }
    }

    /**
     * 관련 문서 삭제
     * @param data 삭제시 필요한 데이터
     */
    removeRelatedDoc(data) {
        aliceAlert.confirm(i18n.msg('common.msg.confirmDelete'),  () => {
            aliceJs.fetchText('/rest/folders/' + data.folderId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            }).then((rtn) => {
                if (rtn === 'true') {
                    aliceAlert.alertSuccess(i18n.msg('common.msg.delete'), () => {
                        document.getElementById('relatedDoc' + data.instanceId).remove();
                    });
                } else {
                    aliceAlert.alertDanger(i18n.msg('common.msg.fail'));
                }
            });
        });
    }

    /**
     * 문서 팝업 오픈 (tokenView.html)
     */
    openTokenEditPop(tokenId) {
        const _width = 1500, _height = 920;
        const _left = Math.ceil((window.screen.width - _width) / 2);
        const _top = Math.ceil((window.screen.height - _height) / 4);
        window.open('/tokens/' + tokenId + '/view', 'token_' + tokenId, 'width=' + (screen.width - 50) + ', height=' + (screen.height - 150) + ', left=' + _left + ', top=' + _top);
    }

    /**
     * 댓글 저장
     */
    saveComment() {
        const commentElem = document.getElementById('commentValue');
        if (!zValidation.isDefined(commentElem)) { return false; }

        if (zValidation.isEmpty(commentElem.value)) {
            aliceAlert.alertWarning(i18n.msg('comment.msg.enterComments'));
            return false;
        }
        // 저장
        const saveCommentData = {
            instanceId: this.data.instanceId,
            content: commentElem.value
        };
        aliceJs.fetchText('/rest/comments', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveCommentData)
        }).then((rtn) => {
            if (rtn === 'true') {
                this.makeTab();
            } else {
                aliceAlert.alertDanger(i18n.msg('common.msg.fail'));
            }
        });
    }

    /**
     * 댓글 삭제
     */
    removeComment(commentId) {
        aliceAlert.confirm(i18n.msg('common.msg.confirmDelete'),  () => {
            aliceJs.fetchText('/rest/comments/' + commentId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((rtn) => {
                if (rtn === 'true') {
                    aliceAlert.alertSuccess(i18n.msg('common.msg.delete'), () => {
                        document.getElementById('comment' + commentId).remove();
                    });
                } else {
                    aliceAlert.alertDanger(i18n.msg('common.msg.fail'));
                }
            });
        });
    }
    
    /**
     * TODO: 문서 인쇄
     */
    print() {}
    /**
     * 문서 닫기
     */
    close() {
        window.close();
    }
}

export const zFormToken = new ZFormToken();