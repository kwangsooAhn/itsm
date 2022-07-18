/**
 * 문서함 상세 정보용 Tab Class
 *
 * @author jung hee chan (hcjung@brainz.co.kr)
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { DOCUMENT } from '../lib/zConstants.js';
import { ZSession } from '../lib/zSession.js';
import { zValidation } from '../lib/zValidation.js';

class ZFormTokenTab {
    constructor() {
    }

    /**
     * 클래스 초기화
     *
     * @param formDataJson 그리고자 하는 폼에 대한 JSON 데이터
     * @param editable 편집 가능여부
     */
    init(formDataJson, editable) {
        this.propertiesElement = document.getElementById('documentProperties'); // 우측 문서 정보, 의견, 태그가 표시되는 엘리먼트
        this.documentId = formDataJson.documentId || '';
        this.instanceId = formDataJson.instanceId;
        this.tokenId = formDataJson.tokenId;
        this.folderId = formDataJson.folderId;
        this.editable = editable;
        this.viewerList = []; // 참조인 목록
        this.relatedDocList = []; // 관련문서 목록

        // 탭 생성
        aliceJs.fetchText('/tokens/tokenTab', {
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
            // 아이콘 추가
            aliceJs.loadSvg();

            this.reloadTab();

            OverlayScrollbars(document.querySelectorAll('.z-token-panels'), { className: 'scrollbar' });
            OverlayScrollbars(document.getElementById('commentValue'), {
                className: 'scrollbar',
                resize: 'vertical',
                sizeAutoCapable: true,
                textarea: {
                    dynHeight: false,
                    dynWidth: false,
                    inheritedAttrs: 'class'
                }
            });
            aliceJs.initDesignedSelectTag();
        });
    }

    /**
     * 탭 생성 : 우측 문서 정보, 의견, 태그 영역
     */
    reloadTab() {
        const history = this.reloadHistory();
        const viewer = this.reloadViewer();
        const calendar = this.reloadCalendar();
        const relatedInstance = this.reloadRelatedInstance();
        const comment = this.reloadTokenComment();
        const tag = this.reloadTokenTag();
        const documentStorage = this.reloadDocumentStorage();
        Promise.all([history, viewer, calendar, relatedInstance, comment, tag, documentStorage]).then(() => {
            // 날짜 표기 변경
            this.setDateTimeFormat();
        });
    }

    /**
     * 탭 선택시 이벤트 핸들러
     */
    selectTokenTab(e) {
        // 탭 동작
        Array.prototype.filter.call(e.target.parentNode.children, function(child) {
            return child !== e.target;
        }).forEach((siblingElement) => {
            siblingElement.classList.remove('active');
        });
        e.target.classList.add('active');

        // 컨텐츠 내용 동작
        const selectedTab = document.getElementById(e.target.dataset.targetContents);
        if (zValidation.isDefined(selectedTab)) {
            Array.prototype.filter.call(selectedTab.parentNode.children, function(child) {
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
            if (!element.classList.contains('active')) {
                element.classList.add('active');
                element.textContent = dateFormatFromNow(element.textContent);
            }
        });

        document.querySelectorAll('.date-time').forEach((element) => {
            if (!element.classList.contains('active')) {
                element.classList.add('active');
                const userDateTime = i18n.userDateTime(element.textContent);
                element.textContent = userDateTime;
                element.setAttribute('title', userDateTime);
            }
        });
    }

    /***************************************************************************************************************
     * 문서이력 조회
     ***************************************************************************************************************/
    reloadHistory() {
        // 문서이력 clear
        document.getElementById('history').innerHTML = '';

        return aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/history', {
            method: 'GET'
        }).then((response) => {
            if (response.status === aliceJs.response.success && response.data.length > 0) {
                response.data.forEach((token) => {
                    document.getElementById('history').insertAdjacentHTML('beforeend', this.makeHistoryFragment(token));
                });
            } else {
                document.getElementById('history').insertAdjacentHTML('beforeend', this.makeNoDataFragment());
            }
        });
    }

    makeNoDataFragment() {
        return `<tr class="flex-row align-items-center no-data-found-list">` +
            `<td style="width:100%" colspan="6" class="align-center">` + i18n.msg('common.msg.noData') + `</td>` +
            `</tr>`;
    }

    /**
     * 문서이력 리스트 화면 조각
     * @param token 서버에서 응답받은 Token 리스트에서 1개의 정보
     * @return {string} innerHTML 로 넣을 수 있는 String.
     */
    makeHistoryFragment(token) {
        token.assigneeName = token.assigneeName ? token.assigneeName : '';
        return `<tr class="flex-row align-items-center">` +
            `<td style="width: 30%;" class="align-center date-time" name="tokenDt" title="${token.tokenStartDt}">` +
            token.tokenStartDt + `</td>` +
            `<td style="width: 32%;" class="align-center" title="${token.elementName}">` + token.elementName + `</td>` +
            `<td style="width: 20%;" class="align-center" title="${token.assigneeName}">` +
            token.assigneeName + `</td>` +
            `<td style="width: 18%;" class="align-center" title="${i18n.msg(token.tokenAction)}">` +
            i18n.msg(token.tokenAction) + `</td>` +
            `</tr>`;
    }

    /***************************************************************************************************************
     * 관련문서 처리 로직
     * - 이것도 사실 파일을 분리했으면 좋겠다. 그치만, 문서조회 팝업의 전체의 구조 및 디자인 변경을 하게 되면 그때 같이 하자.
     ***************************************************************************************************************/

    /***************************************************************************************************************
     * 참조인 조회
     ***************************************************************************************************************/
    reloadViewer() {
        // 참조인 clear
        this.viewerList = [];
        document.getElementById('viewer').innerHTML = '';

        aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/viewer/', {
            method: 'GET'
        }).then((response) => {
            if (response.status === aliceJs.response.success && response.data.data.length > 0) {
                response.data.data.forEach((viewer) => {
                    this.viewerList.push({
                        documentId: this.documentId,
                        instanceId: this.instanceId,
                        viewerKey: viewer.viewerKey,
                        viewerName: viewer.viewerName,
                        organizationName: viewer.organizationName,
                        avatarPath: viewer.avatarPath,
                        reviewYn: viewer.reviewYn,
                        displayYn: viewer.displayYn,
                        viewerType: DOCUMENT.VIEWER_TYPE.MODIFY
                    });
                    document.getElementById('viewer').insertAdjacentHTML('beforeend', this.makeViewerFragment(viewer));
                });
            }
        });
    }

    /**
     * 참조인 리스트 화면 조각
     */
    makeViewerFragment(viewer) {
        return `<tr class="flex-row align-items-center" id="viewer${viewer.viewerKey}">` +
            `<td style="width: 8%;" class="align-left p-0">` +
            `<img class="z-img i-profile-photo" src="${viewer.avatarPath}" width="30" height="30" alt=""/>` +
            `</td>` +
            `<td style="width: 25%;" class="align-left" title="${viewer.viewerName}">${viewer.viewerName}</td>` +
            `<td style="width: 57%;" class="align-left" title="${viewer.organizationName}">` +
            `${viewer.organizationName}</td>` +
            `<td style="width: 10%;" class="align-center">` +
            (viewer.reviewYn ? `<span class="label normal">${i18n.msg('token.label.read')}</span>` :
                `<button type="button" class="button-icon-sm" tabindex="-1" ` +
                `onclick="zFormTokenTab.removeViewer('${viewer.viewerKey}')">` +
                `<span class="icon i-remove"></span>` +
                `</button>`) +
            `</td>` +
            `</tr>`;
    }

    /**
     * 참조인 등록/수정 모달 오픈
     */
    openViewerModal() {
        const viewerModalTemplate = document.getElementById('viewerModalTemplate');
        const viewerModal = new modal({
            title: i18n.msg('token.label.viewer'),
            body: viewerModalTemplate.content.cloneNode(true),
            classes: 'sub-user-modal',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'button primary',
                bindKey: false,
                callback: (modal) => {
                    if (!this.viewerList.length) {
                        zAlert.warning(i18n.msg('token.msg.selectViewer'));
                        return false;
                    }
                    const substituteUserList = document.getElementById('subUserList');
                    const selectedViewerList = substituteUserList.querySelectorAll('input[type=checkbox]:checked');
                    const clonedViewerList = JSON.parse(JSON.stringify(this.viewerList));
                    const saveViewerData = [];
                    selectedViewerList.forEach((viewer) => {
                        const findIndex = clonedViewerList.findIndex(function(item) {
                            return item.viewerKey === viewer.id;
                        });

                        // 신규 추가
                        if (findIndex === -1) {
                            saveViewerData.push({
                                viewerKey: viewer.id,
                                reviewYn: false,
                                displayYn: false,
                                viewerType: DOCUMENT.VIEWER_TYPE.REGISTER
                            });
                        } else { // 기존 수정
                            const matchingViewer = clonedViewerList[findIndex];
                            saveViewerData.push({
                                viewerKey: matchingViewer.viewerKey,
                                reviewYn: matchingViewer.reviewYn,
                                displayYn: matchingViewer.displayYn,
                                viewerType: DOCUMENT.VIEWER_TYPE.MODIFY
                            });
                            clonedViewerList.splice(findIndex, 1);
                        }
                    });
                    // 자기 자신일 경우, 포함시키고 아닐 경우 삭제
                    clonedViewerList.forEach((viewer) => {
                        if (viewer.viewerKey === ZSession.get('userKey')) {
                            viewer.viewerType = DOCUMENT.VIEWER_TYPE.MODIFY;
                        } else {
                            viewer.viewerType = DOCUMENT.VIEWER_TYPE.DELETE;
                        }
                        saveViewerData.push({
                            viewerKey: viewer.viewerKey,
                            reviewYn: viewer.reviewYn,
                            displayYn: viewer.displayYn,
                            viewerType: viewer.viewerType
                        });
                    });

                    let data = {
                        instanceId: this.instanceId,
                        documentId: this.documentId,
                        viewers: saveViewerData
                    };
                    this.saveViewer(data);

                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: { closable: false },
            onCreate: () => {
                document.getElementById('search').addEventListener('keyup', aliceJs.debounce((e) => {
                    this.getViewerList(e.target.value, false);
                }), false);
                this.getViewerList(document.getElementById('search').value, true);
            }
        });
        viewerModal.show();
    }

    /**
     * 참조인 목록 조회
     * @param search
     * @param showProgressbar
     */
    getViewerList(search, showProgressbar) {
        let strUrl = '/users/substituteUsers?search=' + encodeURIComponent(search.trim())
            + '&from=&to=&userKey=' + ZSession.get('userKey')
            + '&multiSelect=true';
        aliceJs.fetchText(strUrl, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            const viewerList = document.getElementById('subUserList');
            viewerList.innerHTML = htmlData;

            OverlayScrollbars(viewerList.querySelector('.table-body'), { className: 'scrollbar' });
            // 갯수 가운트
            aliceJs.showTotalCount(viewerList.querySelectorAll('.table-row').length);
            this.viewerList.forEach((viewer) => {
                const checkElem = viewerList.querySelector('input[id="' + viewer.viewerKey + '"]');
                if (checkElem) {
                    checkElem.checked = true;
                    // 읽음일 경우 편집 불가능
                    if (viewer.reviewYn) {
                        checkElem.disabled = true;
                    }
                }
            });
            const checkboxList = viewerList.querySelectorAll('input[name=userName]');
            checkboxList.forEach((checkbox) => {
                checkbox.addEventListener('click', (e) => {
                    if (e.target.checked) {
                        this.viewerList.push({
                            viewerKey: e.target.id,
                            reviewYn: false,
                            displayYn: false,
                            viewerType: DOCUMENT.VIEWER_TYPE.REGISTER
                        });
                    } else {
                        let removeIndex = this.viewerList.findIndex(function(key) {
                            return key.viewerKey === e.target.id;
                        });
                        if (removeIndex > -1) {
                            this.viewerList.splice(removeIndex, 1);
                        }
                    }
                });
            });
        });
    }

    /**
     * 참조인 등록/수정
     */
    saveViewer(data) {
        aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/viewer/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then((response) => {
            switch (response.status) {
                case aliceJs.response.success:
                    zAlert.success(i18n.msg('common.msg.save'), () => {
                        this.reloadViewer();
                    });
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 참조인 삭제
     * @param viewerKey 참조인 userKey
     */
    removeViewer(viewerKey) {
        zAlert.confirm(i18n.msg('common.msg.confirmDelete'), () => {
            aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/viewer/' + viewerKey, {
                method: 'DELETE'
            }).then((response) => {
                switch (response.status) {
                    case aliceJs.response.success:
                        zAlert.success(i18n.msg('common.msg.delete'), () => {
                            // 테이블 삭제
                            const removeRow = document.getElementById('viewer' + viewerKey);
                            const parent = document.getElementById('viewer');
                            parent.removeChild(removeRow);
                            // 데이터 삭제
                            const findIndex = this.viewerList.findIndex(function(item) {
                                return item.viewerKey === viewerKey;
                            });
                            this.viewerList.splice(findIndex, 1);
                        });
                        break;
                    case aliceJs.response.error:
                        zAlert.danger(i18n.msg('common.msg.fail'));
                        break;
                    default:
                        break;
                }
            });
        });
    }

    /**
     * 관련 문서 모달 오픈 : 관련문서를 찾기 위한 모달
     */
    openRelatedDocModal() {
        const relatedTokenModalTemplate = document.getElementById('relatedTokenModalTemplate');
        const relatedTokenModal = new modal({
            title: i18n.msg('token.label.relatedInstance'),
            body: relatedTokenModalTemplate.content.cloneNode(true),
            classes: 'token-list',
            buttons: [{
                content: i18n.msg('common.btn.select'),
                classes: 'button primary',
                bindKey: false,
                callback: (modal) => {
                    if (!this.relatedDocList.length) {
                        zAlert.warning(i18n.msg('token.msg.selectToken'));
                        return false;
                    }
                    this.saveRelatedDoc();
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: () => {
                document.getElementById('search').addEventListener('keyup', aliceJs.debounce((e) => {
                    this.getRelatedDoc(e.target.value, false);
                }), false);
                this.getRelatedDoc(document.getElementById('search').value, true);
            }
        });
        relatedTokenModal.show();
    }

    /**
     * 관련 문서 조회 : 현재 조회한 문서와 관련이 있는 문서를 조회
     *
     * @param search 검색어
     * @param showProgressbar ProgressBar 표시여부
     */
    getRelatedDoc(search, showProgressbar) {
        aliceJs.fetchText('/tokens/view-pop/documents?searchValue=' + search.trim() +
            '&instanceId=' + this.instanceId, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            document.getElementById('instanceList').innerHTML = htmlData;
            OverlayScrollbars(document.getElementById('instanceListBody'), { className: 'scrollbar' });
            this.setDateTimeFormat();
            OverlayScrollbars(document.getElementById('instanceList'), { className: 'scrollbar' });
            aliceJs.showTotalCount(document.querySelectorAll('.instance-list').length);

            const relatedDocumentListBody = document.getElementById('instanceListBody');
            this.relatedDocList.forEach((doc) => {
                const checkElem = relatedDocumentListBody.querySelector('input[value="' + doc.instanceId + '"]');
                if (checkElem) {
                    checkElem.checked = true;
                }
            });

            const checkboxList = document.querySelectorAll('input[name=chk]');
            checkboxList.forEach((checkbox) => {
                checkbox.addEventListener('click', (e) => {
                    if (e.target.checked) {
                        this.relatedDocList.push({
                            documentId: this.documentId,
                            folderId: this.folderId,
                            instanceId: e.target.value
                        });
                    } else {
                        let removeIndex = this.relatedDocList.findIndex(function(key) {
                            return key.instanceId === e.target.value;
                        });
                        if (removeIndex > -1) {
                            this.relatedDocList.splice(removeIndex, 1);
                        }
                    }
                });
            });
        });
    }

    /**
     * 관련 문서 저장 : 선택한 문서를 관련문서로 저장
     */
    saveRelatedDoc() {
        let data = {
            instanceId: this.instanceId,
            documentId: this.documentId
        };

        data.folders = this.relatedDocList;
        aliceJs.fetchJson('/rest/folders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then((response) => {
            switch (response.status) {
                case aliceJs.response.success:
                    zAlert.success(i18n.msg('common.msg.save'), () => {
                        this.folderId = response.data.folderId;
                        this.reloadRelatedInstance().then(() => {
                            // 날짜 표기 변경
                            this.setDateTimeFormat();
                        });
                    });
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default :
                    break;
            }
            this.relatedDocList = [];// 저장후 검색리스트 초기화
        });
    }

    /**
     * 관련 문서 삭제
     * @param folderId 삭제 대상 폴더 아이디
     * @param instanceId 삭제 대상 인스턴스
     */
    removeRelatedDoc(folderId, instanceId) {
        zAlert.confirm(i18n.msg('common.msg.confirmDelete'), () => {
            aliceJs.fetchJson('/rest/folders/' + folderId + '/instances/' + instanceId, {
                method: 'DELETE'
            }).then((response) => {
                switch (response.status) {
                    case aliceJs.response.success:
                        zAlert.success(i18n.msg('common.msg.delete'), () => {
                            if (document.getElementById('relatedDoc' + instanceId)) {
                                document.getElementById('relatedDoc' + instanceId).remove();
                            }
                        });
                        break;
                    case aliceJs.response.error:
                        zAlert.danger(i18n.msg('common.msg.fail'));
                        break;
                    default:
                        break;
                }
            });
        });
    }

    /**
     * 관련문서 세부내용 조회를 위해 팝업 오픈 (tokenView.html)
     */
    openTokenEditPop(tokenId) {
        const _width = 1500, _height = 920;
        const _left = Math.ceil((window.screen.width - _width) / 2);
        const _top = Math.ceil((window.screen.height - _height) / 4);
        window.open('/tokens/' + tokenId + '/view', 'token_' + tokenId, 'width=' +
            (screen.width - 50) + ', height=' + (screen.height - 150) + ', left=' + _left + ', top=' + _top);
    }

    /**
     * 관련문서 재로딩
     */
    reloadRelatedInstance() {
        document.querySelectorAll('#related .z-token-related-item:not(.document-add)').forEach((aTag) => {
            aTag.remove();  // 관련문서 clear
        });

        return aliceJs.fetchJson('/rest/folders/' + this.folderId, {
            method: 'GET'
        }).then((response) => {
            if (response.status === aliceJs.response.success && response.data.length > 0) {
                response.data.forEach((instance) => {
                    document.querySelector('#related label')
                        .insertAdjacentElement('afterend', this.makeRelatedInstanceFragment(instance));
                });
                // 날짜 표기 변경
                this.setDateTimeFormat();
            }
        });
    }

    /**
     * 관련문서 부분 화면 조각
     * @param instance 서버에서 응답받은 관련문서(instance) 1개의 데이터
     * @return {Element} 만들어진 HTML Element
     */
    makeRelatedInstanceFragment(instance) {
        let htmlString =
            `<div class="z-token-related-item flex-row" id="relatedDoc` + instance.instanceId + `">` +
            `<div class="document-color" style="background-color: ` + instance.documentColor + `"></div>` +
            `<div class="document-row flex-column">` +
            `<div class="document-row-content flex-row justify-content-between">` +
            `<a onclick="zFormTokenTab.openTokenEditPop('` + instance.tokenId + `')">` +
            `<div class="document-title flex-row align-items-center">` +
            `<h6>` + instance.documentName + `</h6>` +
            `<h6>` + instance.documentNo + `</h6>` +
            `</div>` +
            `</a>` +
            `<button type="button" class="button-icon"` +
            `onclick="zFormTokenTab.removeRelatedDoc('` + instance.folderId + `', '` + instance.instanceId + `')">` +
            `<span class="icon i-delete"></span>` +
            `</button>` +
            `</div>` +
            `<div class="document-row-topic">`;
        if (!zValidation.isEmpty(instance.topics)) {
            htmlString += `<br><h6 class="text-ellipsis">` + instance.topics[0] + `</h6>`;
        }
        htmlString +=
            `</div>` +
            `<div class="document-row-info flex-row align-items-center">` +
            `<div class="flex-row align-items-center">` +
            `<img class="z-img i-profile-photo mr-2" src="` + instance.avatarPath + `" width="30" height="30"/>` +
            `<h6 class="pl-2">` + instance.instanceCreateUserName + `</h6>` +
            `</div>` +
            `<span class="vertical-bar"></span>` +
            `<h6 class="dateFormatFromNow">` + instance.instanceStartDt + `</h6>` +
            `<span class="vertical-bar"></span>` +
            `<h6>` + i18n.msg('common.code.token.status.' + instance.instanceStatus) + `</h6>` +
            `</div>` +
            `</div>` +
            `</div>`;

        return aliceJs.makeElementFromString(htmlString);
    }

    /***************************************************************************************************************
     * 댓글 처리 로직
     * - 이것도 사실 파일을 분리했으면 좋겠다. 그치만, 문서조회 팝업의 전체의 구조 및 디자인 변경을 하게 되면 그때 같이 하자.
     ***************************************************************************************************************/
    /**
     * 댓글 저장
     */
    saveComments() {
        const commentElem = document.getElementById('commentValue');
        if (!zValidation.isDefined(commentElem)) {
            return false;
        }

        if (zValidation.isEmpty(commentElem.value)) {
            zAlert.warning(i18n.msg('comment.msg.enterComments'));
            return false;
        }
        const saveData = {
            documentId: this.documentId,
            commentValue: commentElem.value
        };
        aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/comments', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData)
        }).then((response) => {
            switch (response.status) {
                case aliceJs.response.success:
                    zAlert.success(i18n.msg('common.msg.save'), () => {
                        document.getElementById('commentValue').value = '';
                        this.reloadTokenComment();
                    });
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default :
                    break;
            }
        });
    }

    /**
     * 댓글 삭제
     */
    removeComment(commentId) {
        zAlert.confirm(i18n.msg('common.msg.confirmDelete'), () => {
            aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/comments/' + commentId, {
                method: 'DELETE'
            }).then((response) => {
                switch (response.status) {
                    case aliceJs.response.success:
                        zAlert.success(i18n.msg('common.msg.delete'), () => {
                            document.getElementById('comment' + commentId).remove();
                        });
                        break;
                    case aliceJs.response.error:
                        zAlert.danger(i18n.msg('common.msg.fail'));
                        break;
                    default:
                        break;
                }
            });
        });
    }

    /**
     * 댓글 재로딩
     */
    reloadTokenComment() {
        document.querySelectorAll('#tokenComments .z-token-comment-item').forEach((comment) => {
            comment.remove();  // 댓글 clear
        });

        return aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/comments', {
            method: 'GET'
        }).then((response) => {
            if (response.status === aliceJs.response.success && response.data.length > 0) {
                response.data.forEach((comment) => {
                    document.querySelector('#tokenComments').lastElementChild
                        .insertAdjacentElement('beforebegin', this.makeCommentsFragment(comment));
                    this.setDateTimeFormat();
                });
            }
        });
    }

    /**
     * 댓글 출력을 위한 화면 코드 조각
     * @param comment 댓글 1개 정보
     * @return {Element} 그려진 HTML Element
     */
    makeCommentsFragment(comment) {
        let htmlString =
            `<div class="z-token-comment-item flex-column" id="comment` + comment.commentId + `">` +
            `<div class="z-comment-row-info flex-row align-items-center">` +
            `<div class="flex-row align-items-center">` +
            `<img class="z-img i-profile-photo mr-2" src="` + comment.avatarPath + `" width="30" height="30"/>` +
            `<h6 class="z-user-name pl-2">` + comment.createUserName + `</h6>` +
            `</div>` +
            `<h6 class="z-comment-time date-time">` + comment.createDt + `</h6>` +
            `<div class="ml-auto">`;
        if (ZSession.get('userKey') === comment.createUserKey) {
            htmlString +=
                `<button class="button-icon" onclick="zFormTokenTab.removeComment('` + comment.commentId + `')">` +
                `<span class="icon i-delete"></span>` +
                `</button>`;
        }
        htmlString +=
            `</div>` +
            `</div>` +
            `<div class="z-comment-row-content">` +
            `<h6 class="text-wordWrap">` +
            `${aliceJs.filterXSS(comment.content)}` +
            `</h6>` +
            `</div>` +
            `</div>`;

        return aliceJs.makeElementFromString(htmlString);
    }

    /***************************************************************************************************************
     * 태그 조회
     ***************************************************************************************************************/
    reloadTokenTag() {
        return aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/tags', {
            method: 'GET'
        }).then((response) => {
            if (response.status === aliceJs.response.success) {
                document.getElementById('tokenTags').value = JSON.stringify(response.data);
                new zTag(document.getElementById('tokenTags'), {
                    suggestion: true,
                    realtime: true,
                    tagType: 'instance',
                    targetId: this.instanceId,
                    options: {
                        documentId: this.documentId
                    }
                });
            }
        });
    }

    /**
     * 문서 보관처리 여부 조회
     */
    reloadDocumentStorage() {
        const starIcon = document.getElementById('documentStorage');
        if (starIcon.classList.contains('active')) {
            starIcon.classList.remove('active');
        }

        return aliceJs.fetchJson('/rest/documentStorage/' + this.instanceId + '/exist', {
            method: 'GET'
        }).then((response) => {
            if (response.status === aliceJs.response.success && response.data) {
                // 보관처리
                starIcon.classList.add('active');
            }
        });
    }

    /**
     * 문서 보관
     * @param starIcon 별모양 아이콘
     */
    toggleDocumentStorage(starIcon) {
        if (starIcon.classList.contains('active')) {
            // 문서 보관 삭제
            this.removeDocumentStorage(starIcon);
        } else {
            // 문서 보관 처리
            this.saveDocumentStorage(starIcon);
        }
    }

    /**
     * 문서 저장
     */
    saveDocumentStorage(starIcon) {
        const saveData = {
            instanceId: this.instanceId,
            documentId: this.documentId,
        };

        aliceJs.fetchJson('/rest/documentStorage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saveData),
        }).then((response) => {
            switch (response.status) {
                case aliceJs.response.success:
                    starIcon.classList.add('active');
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default :
                    break;
            }
        });

    }

    /**
     * 보관된 문서 삭제
     * @param starIcon 별모양 아이콘
     */
    removeDocumentStorage(starIcon) {
        zAlert.confirm(i18n.msg('token.msg.confirmDeleteStorageDocument'), () => {
            aliceJs.fetchJson('/rest/documentStorage/' + this.instanceId, {
                method: 'DELETE'
            }).then((response) => {
                switch (response.status) {
                    case aliceJs.response.success:
                        starIcon.classList.remove('active');
                        break;
                    case aliceJs.response.error:
                        zAlert.danger(i18n.msg('common.msg.fail'));
                        break;
                    default:
                        break;
                }
            });
        });
    }

    /**
     * 일정 조회
     */
    reloadCalendar() {
        const calendar = document.getElementById('calendar');
        calendar.innerHTML = '';
        aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/schedule', {
            method: 'GET'
        }).then((response) => {
            if (response.status === aliceJs.response.success && response.data.length > 0) {
                response.data.forEach((schedule) => {
                    calendar.insertAdjacentHTML('beforeend', this.makeCalendarFragment(schedule));
                });
            }
        });
    }

    /**
     * 일정 리스트 화면 조각
     * @param schedule 조회한 스케쥴 데이터 객체
     */
    makeCalendarFragment(schedule) {
        let rangeDateHtml = [];
        if (schedule.allDayYn) {
            rangeDateHtml.push(i18n.userDate(schedule.startDt));
            rangeDateHtml.push(' ~  ');
            rangeDateHtml.push(i18n.userDate(schedule.endDt));
        } else {
            rangeDateHtml.push(i18n.userDateTime(schedule.startDt));
            rangeDateHtml.push(' ~  ');
            rangeDateHtml.push(i18n.userDateTime(schedule.endDt));
        }
        return `<tr class="flex-row align-items-center" id="schedule${schedule.id}">` +
            `<td style="width: 30%;" class="align-left" title="${schedule.title}">${schedule.title}</td>` +
            `<td style="width: 60%;" class="align-left" title="${rangeDateHtml.join('')}">` +
            `${rangeDateHtml.join('')}</td>` +
            `<td style="width: 10%;" class="align-center">` +
                `<button type="button" class="button-icon-sm" tabindex="-1" ` +
                    `onclick="zFormTokenTab.removeSchedule('${schedule.id}')">` +
                    `<span class="icon i-remove"></span>` +
                `</button>` +
            `</td>` +
            `</tr>`;
    }

    /**
     * 일정 등록/수정 모달 오픈
     */
    openCalendarModal() {
        const calendarModalTemplate = document.getElementById('calendarModalTemplate');
        const calendarRegisterModal = new modal({
            title: i18n.msg('token.label.calendar'),
            body: calendarModalTemplate.content.cloneNode(true),
            classes: 'calendar__modal--register document',
            buttons: [{
                content: i18n.msg('common.btn.register'),
                classes: 'button primary',
                bindKey: false,
                callback: (modal) => {
                    // 필수값 체크
                    if (isEmpty('scheduleTitle', 'common.msg.enterTitle')) return false;
                    if (isEmpty('startDt', 'calendar.msg.enterStartDt')) return false;
                    if (isEmpty('endDt', 'calendar.msg.enterEndDt')) return false;

                    const isAllDay = document.getElementById('allDayYn').checked;
                    const startDt = document.getElementById('startDt').value;
                    const endDt = document.getElementById('endDt').value;
                    const saveData = {
                        instanceId: this.instanceId,
                        documentId: this.documentId,
                        title: document.getElementById('scheduleTitle').value,
                        contents: document.getElementById('scheduleContents').value,
                        startDt: isAllDay ? i18n.systemDate(startDt) : i18n.systemDateTime(startDt),
                        endDt: isAllDay ? i18n.systemDate(endDt) : i18n.systemDateTime(endDt),
                        allDayYn: isAllDay
                    };

                    this.saveSchedule(saveData);
                    modal.hide();
                }
            }, {
                content: i18n.msg('common.btn.cancel'),
                classes: 'button secondary',
                bindKey: false,
                callback: (modal) => {
                    modal.hide();
                }
            }],
            close: {
                closable: false,
            },
            onCreate: () => {
                // 종일 여부 변경시 이벤트 추가
                document.getElementById('allDayYn').addEventListener('change', this.onToggleAllDay.bind(this));

                // 현재시간 설정
                const standardDate = this.getCalendarStandardDate();
                document.getElementById('startDt').value = standardDate.start.toFormat(i18n.dateFormat);
                document.getElementById('endDt').value = standardDate.end.toFormat(i18n.dateFormat);
                
                // 이벤트 추가
                zDateTimePicker.initDatePicker(document.getElementById('startDt'),
                    this.onUpdateRangeDateTime.bind(this), { isHalf: true });
                zDateTimePicker.initDatePicker(document.getElementById('endDt'),
                    this.onUpdateRangeDateTime.bind(this), { isHalf: true });

                // 스크롤바
                OverlayScrollbars(document.getElementById('scheduleContents'), {
                    className: 'inner-scrollbar',
                    resize: 'vertical',
                    sizeAutoCapable: true,
                    textarea: {
                        dynHeight: false,
                        dynWidth: false,
                        inheritedAttrs: 'class'
                    }
                });
            }
        });
        calendarRegisterModal.show();
    }

    /**
     * 캘린더 날짜 데이터 조회
     */
    getCalendarStandardDate() {
        const now = luxon.DateTime.local().setZone(i18n.timezone);
        const startDt = now.startOf('hour').plus({ hour: 1 });
        return {
            start: startDt,
            end: startDt.plus({ hour: 1 })
        };
    }

    /**
     * 종일 여부 변경시 이벤트 추가
     * @param e 대상 이벤트
     */
    onToggleAllDay(e) {
        const format = e.target.checked ? i18n.dateFormat : i18n.dateTimeFormat; // 날짜 포맷
        const rangeDate =  document.getElementById('rangeDate');

        // 시작일시 , 종료일시 초기화
        const standardDate = this.getCalendarStandardDate();
        rangeDate.innerHTML = '';
        const template = ` <input type="text" class="input i-datetime-picker schedule__date" id="startDt" 
            value="${standardDate.start.toFormat(format)}"/>
            ~
            <input type="text" class="input i-datetime-picker schedule__date" id="endDt" 
            value="${standardDate.end.toFormat(format)}"/>`.trim();

        rangeDate.insertAdjacentHTML('beforeend', template);

        // 종일 선택시, datePicker 를 사용하고 나머지는 dateTimePicker 사용
        const newStartDt = rangeDate.querySelector('#startDt');
        const newEndDt = rangeDate.querySelector('#endDt');

        // 라이브러리 설정
        if (e.target.checked) {
            zDateTimePicker.initDatePicker(newStartDt, this.onUpdateRangeDateTime.bind(this));
            zDateTimePicker.initDatePicker(newEndDt, this.onUpdateRangeDateTime.bind(this));
        } else {
            zDateTimePicker.initDateTimePicker(newStartDt, this.onUpdateRangeDateTime.bind(this), { isHalf: true });
            zDateTimePicker.initDateTimePicker(newEndDt, this.onUpdateRangeDateTime.bind(this), { isHalf: true });
        }
    }

    /**
     * 시작, 종료일시 변경에 따른 처리
     */
    onUpdateRangeDateTime(e, picker) {
        const isAllDay = document.getElementById('allDayYn').checked;
        if (e.id === 'startDt') {
            // 시작일시는 종료일시보다 크면 안된다.
            const endDt = document.getElementById('endDt');
            const isValidStartDt = isAllDay ? i18n.compareSystemDate(e.value, endDt.value)
                : i18n.compareSystemDateTime(e.value, endDt.value);
            if (!isValidStartDt) {
                zAlert.warning(i18n.msg('common.msg.selectBeforeDateTime', endDt.value), () => {
                    e.value = '';
                    picker.open();
                });
                return false;
            }
        } else {
            // 종료일시는 시작일시보다 작으면 안된다.
            const startDt = document.getElementById('startDt');
            const isValidEndDt = isAllDay ? i18n.compareSystemDate(startDt.value, e.value)
                : i18n.compareSystemDateTime(startDt.value, e.value);
            if (!isValidEndDt) {
                zAlert.warning(i18n.msg('common.msg.selectAfterDateTime', startDt.value), () => {
                    e.value = '';
                    picker.open();
                });
                return false;
            }
        }
    }

    /**
     * 일정 저장
     * @param data 저장할 데이터 객체
     */
    saveSchedule(data) {
        aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/schedule', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then((response) => {
            switch (response.status) {
                case aliceJs.response.success:
                    zAlert.success(i18n.msg('common.msg.save'), () => {
                        this.reloadCalendar();
                    });
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 일정 삭제
     * @param id 스케쥴ID
     */
    removeSchedule(id) {
        zAlert.confirm(i18n.msg('common.msg.confirmDelete'), () => {
            aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/schedule/' + id, {
                method: 'DELETE'
            }).then((response) => {
                switch (response.status) {
                    case aliceJs.response.success:
                        zAlert.success(i18n.msg('common.msg.delete'), () => {
                            // 테이블 삭제
                            const parent = document.getElementById('calendar');
                            const removeRow = document.getElementById('schedule' + id);
                            parent.removeChild(removeRow);
                        });
                        break;
                    case aliceJs.response.error:
                        zAlert.danger(i18n.msg('common.msg.fail'));
                        break;
                    default:
                        break;
                }
            });
        });
    }
}

export const zFormTokenTab = new ZFormTokenTab();
