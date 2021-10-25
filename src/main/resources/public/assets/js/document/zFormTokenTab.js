/**
 * 문서함 상세 정보용 Tab Class
 *
 * @author jung hee chan (hcjung@brainz.co.kr)
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { SESSION } from '../lib/zConstants.js';
import { zValidation } from '../lib/zValidation.js';

class ZFormTokenTab {
    constructor() {}
    /**
     * 클래스 초기화
     *
     * @param formDataJson 그리고자 하는 폼에 대한 JSON 데이터
     * @param editable 편집 가능여부
     */
    init(formDataJson, editable) {
        this.propertiesElement = document.getElementById('documentProperties'); // 우측 문서 정보, 의견, 태그가 표시되는 엘리먼트
        this.instanceId = formDataJson.instanceId;
        this.tokenId = formDataJson.tokenId;
        this.folderId = formDataJson.folderId;
        this.editable = editable;

        // 탭 생성
        aliceJs.fetchText('/tokens/' + this.tokenId + '/tokenTab', {
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

            this.reloadTab();

            new zTag(document.getElementById('tokenTags'), {
                suggestion: true,
                realtime: true,
                tagType: 'instance',
                targetId: this.instanceId
            });
        });
    }
    /**
     * 탭 생성 : 우측 문서 정보, 의견, 태그 영역
     */
    reloadTab() {
        this.reloadHistory();
        this.reloadRelatedInstance();
        this.reloadTokenComment();

        OverlayScrollbars(document.querySelectorAll('.z-token-panels'), { className: 'scrollbar' });
        aliceJs.initDesignedSelectTag();
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

    /***************************************************************************************************************
     * 문서이력 조회
     ***************************************************************************************************************/
    reloadHistory() {
        // 문서이력 clear
        document.getElementById('history').childNodes.innerHTML = '';

        aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/history', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((rtn) => {
            if (rtn) {
                rtn.forEach((token) => {
                    document.getElementById('history').innerHTML = this.makeHistoryFragment(token);
                });
            } else {
                zAlert.danger(i18n.msg('common.msg.fail'));
            }
        });
    }

    /**
     * 문서이력 리스트 화면 조각
     * @param token 서버에서 응답받은 Token 리스트에서 1개의 정보
     * @return {string} innerHTML 로 넣을 수 있는 String.
     */
    makeHistoryFragment(token) {
        return `<tr class="flex-row align-items-center">` +
            `<td style="width: 35%;" class="align-left date-time" name="tokenDt">` + token.tokenStartDt + `</td>` +
            `<td style="width: 25%;" class="align-left">` + token.elementName + `</td>` +
            `<td style="width: 20%;" class="align-left">` + token.assigneeName + `</td>` +
            `<td style="width: 20%;" class="align-left">` + i18n.msg(token.tokenAction) + `</td>` +
            `</tr>`;
    }
    /***************************************************************************************************************
     * 관련문서 처리 로직
     * - 이것도 사실 파일을 분리했으면 좋겠다. 그치만, 문서조회 팝업의 전체의 구조 및 디자인 변경을 하게 되면 그때 같이 하자.
     ***************************************************************************************************************/
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
                classes: 'z-button primary',
                bindKey: false,
                callback: (modal) => {
                    this.saveRelatedDoc();
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
                    this.getRelatedDoc(e.target.value, false);
                });
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
        aliceJs.fetchText('/tokens/view-pop/documents?searchValue=' + search.trim() + '&tokenId=' + this.tokenId, {
            method: 'GET',
            showProgressbar: showProgressbar
        }).then((htmlData) => {
            document.getElementById('instanceList').innerHTML = htmlData;
            aliceJs.showTotalCount(document.querySelectorAll('.instance-list').length);
        });
    }
    /**
     * 관련 문서 저장 : 선택한 문서를 관련문서로 저장
     */
    saveRelatedDoc() {
        let checked = document.querySelectorAll('input[name=chk]:checked');
        if (checked.length === 0) {
            zAlert.warning(i18n.msg('token.msg.selectToken'));
        } else {
            let jsonArray = [];
            for (let i = 0; i < checked.length; i++) {
                let jsonObject = {};
                jsonObject.folderId = this.folderId;
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
                    this.reloadTab();
                } else {
                    zAlert.danger(i18n.msg('common.msg.fail'));
                }
            });
        }
    }
    /**
     * 관련 문서 삭제
     * @param folderId 삭제 대상 폴더 아이디
     * @param instanceId 삭제 대상 인스턴스
     */
    removeRelatedDoc(folderId, instanceId) {
        zAlert.confirm(i18n.msg('common.msg.confirmDelete'), () => {
            aliceJs.fetchText('/rest/folders/' + folderId + '/instances/' + instanceId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((rtn) => {
                if (rtn === 'true') {
                    zAlert.success(i18n.msg('common.msg.delete'), () => {
                        if (document.getElementById('relatedDoc' + instanceId)) {
                            document.getElementById('relatedDoc' + instanceId).remove();
                        }
                    });
                } else {
                    zAlert.danger(i18n.msg('common.msg.fail'));
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
        window.open('/tokens/' + tokenId + '/view', 'token_' + tokenId, 'width=' + (screen.width - 50) + ', height=' + (screen.height - 150) + ', left=' + _left + ', top=' + _top);
    }

    /**
     * 관련문서 재로딩
     */
    reloadRelatedInstance() {
        document.querySelectorAll('#related .z-token-related-item:not(.z-document-add)').forEach((aTag) => {
            aTag.remove();  // 관련문서 clear
        });

        aliceJs.fetchJson('/rest/folders/' + this.folderId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((rtn) => {
            if (rtn) {
                rtn.forEach((instance) => {
                    document.querySelector('#related label').insertAdjacentElement('afterend', this.makeRelatedInstanceFragment(instance));
                });
                // 날짜 표기 변경
                this.setDateTimeFormat();
            } else {
                zAlert.danger(i18n.msg('common.msg.fail'));
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
            `<div class="z-document-color" style="background-color: ` + instance.documentColor + `"></div>` +
            `<div class="z-document-row flex-column">` +
            `<div class="z-document-row-content flex-row justify-content-between">` +
            `<a onclick="zFormTokenTab.openTokenEditPop('` + instance.tokenId + `')">` +
            `<div class="z-document-title flex-row align-items-center">` +
            `<h6>` + instance.documentName + `</h6>` +
            `<h6>` + instance.documentNo + `</h6>` +
            `</div>` +
            `</a>` +
            `<button type="button" class="z-button-icon"` +
            `onclick="zFormTokenTab.removeRelatedDoc('` + instance.folderId + `', '` + instance.instanceId + `')">` +
            `<span class="z-icon i-delete"></span>` +
            `</button>` +
            `</div>` +
            `<div class="z-document-row-topic">`;
        if (zValidation.isEmpty(instance.topics)) {
            instance.topics?.forEach((topic) => {
                htmlString += `<h6 class="text-ellipsis">` + topic + `</h6>`;
            });
        }
        htmlString +=
            `</div>` +
            `<div class="z-document-row-info flex-row align-items-center">` +
            `<div class="flex-row align-items-center">` +
            `<img class="z-img i-profile-photo mr-2" src="` + instance.avatarPath + `" width="30" height="30"/>` +
            `<h6 class="pl-2">` + instance.instanceCreateUserName + `</h6>` +
            `</div>` +
            `<span class="vertical-bar"></span>` +
            `<h6 class="dateFormatFromNow">` + instance.instanceStartDt + `</h6>` +
            `<span class="vertical-bar"></span>` +
            `<h6>` +  i18n.msg('common.code.token.status.' + instance.instanceStatus) + `</h6>` +
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
        if (!zValidation.isDefined(commentElem)) { return false; }

        if (zValidation.isEmpty(commentElem.value)) {
            zAlert.warning(i18n.msg('comment.msg.enterComments'));
            return false;
        }
        aliceJs.fetchText('/rest/instances/' + this.instanceId + '/comments', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: commentElem.value
        }).then((rtn) => {
            if (rtn === 'true') {
                document.getElementById('commentValue').value = '';
                this.reloadTab();
            } else {
                zAlert.danger(i18n.msg('common.msg.fail'));
            }
        });
    }
    /**
     * 댓글 삭제
     */
    removeComment(commentId) {
        zAlert.confirm(i18n.msg('common.msg.confirmDelete'),  () => {
            aliceJs.fetchText('/rest/instances/' + this.instanceId + '/comments/' + commentId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((rtn) => {
                if (rtn === 'true') {
                    zAlert.success(i18n.msg('common.msg.delete'), () => {
                        document.getElementById('comment' + commentId).remove();
                    });
                } else {
                    zAlert.danger(i18n.msg('common.msg.fail'));
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

        aliceJs.fetchJson('/rest/instances/' + this.instanceId + '/comments', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then((rtn) => {
            if (rtn) {
                rtn.forEach((comment) => {
                    document.querySelector('#tokenComments').lastElementChild.insertAdjacentElement('beforebegin', this.makeCommentsFragment(comment));
                });
            } else {
                zAlert.danger(i18n.msg('common.msg.fail'));
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
        if (SESSION.userKey === comment.createUserKey) {
            htmlString +=
                `<button class="z-button-icon" onclick="zFormTokenTab.removeComment('` + comment.commentId + `')">` +
                `<span class="z-icon i-delete"></span>` +
                `</button>`;
        }
        htmlString +=
            `</div>` +
            `</div>` +
            `<div class="z-comment-row-content">` +
            `<h6 class="text-ellipsis">` + comment.content + `</h6>` +
            `</div>` +
            `</div>`;

        return aliceJs.makeElementFromString(htmlString);
    }
}

export const zFormTokenTab = new ZFormTokenTab();
