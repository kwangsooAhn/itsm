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
import {DOCUMENT, SESSION} from '../lib/zConstants.js';
import {zValidation} from "../lib/zValidation.js";
import {UIButton, UIDiv} from "../lib/zUI.js";

class ZFormButton {
    constructor() {
    }
    /**
     * 클래스 초기화
     *
     * @param domElement Form 그리고자 하는 대상 DOM Element
     * @param actions FromType 에 따라 Form 정보 조회 대상이 다르며 FormId, DocumentId, TokenId 값이 올 수 있다.
     */
    init(domElement, actions, zForm) {
        this.zForm = zForm;
        this.domElement = domElement;
        this.makeActionButton(actions);
        this.makeDefaultButton();
    }
    /**
     * 신청서 상단 프로세스맵, 인쇄 버튼 추가
     * 버튼은 '프로세스맵', '인쇄' 순으로 표기한다.
     */
    makeDefaultButton() {
        // 버튼 목록 생성
        const UIButtonGroup = new UIDiv().setUIClass('btn-list');

        const UIProcessMapButton = new UIButton(i18n.msg('process.label.processMap')).addUIClass('default-line')
            .onUIClick(this.openProcessStatusPopUp.bind(this));

        // 인쇄 버튼
        const UIPrintButton = new UIButton(i18n.msg('common.btn.print')).addUIClass('default-line')
            .onUIClick(this.printForm.bind(this));

        UIButtonGroup.addUI(UIProcessMapButton);
        UIButtonGroup.addUI(UIPrintButton);

        this.domElement.appendChild(UIButtonGroup.domElement);
    }
    /**
     * 신청서 상단 동적 버튼 목록 추가 및 이벤트 생성
     * 저장과 취소 버튼은 기본적으로 생성된다.
     * 버튼은 ['접수' , '반려', '처리'], '저장', '닫기' 순으로 표기한다.
     * @param data JSON 데이터
     */
    makeActionButton(data) {
        if (!zValidation.isDefined(data)) { return false; }
        // 버튼 목록 생성
        const UIButtonGroup = new UIDiv().setUIClass('btn-list');
        // 동적버튼
        data.forEach( (btn) => {
            if (zValidation.isEmpty(btn.name)) { return false; }
            UIButtonGroup.addUI(new UIButton(btn.customYn ? btn.name : i18n.msg(btn.name)).addUIClass('default-line'));

            switch(btn.value) {
                case 'close':
                    this.closeForm.bind(this);
                    break;
                default :
                    this.updateToken.bind(this, btn.value);
            }
        });
        this.domElement.appendChild(UIButtonGroup.domElement);
    }
    /**
     * 신청서 저장, 처리, 취소, 회수, 즉시 종료 등 동적 버튼 클릭시 호출됨
     */
    updateToken(actionType) {
        // 유효성 체크
        let validationUncheckActionType = ['save', 'cancel', 'terminate', 'reject', 'withdraw'];
        if (!validationUncheckActionType.includes(actionType) && zValidation.hasDOMElementError(this.domElement)) {
            return false;
        }
        // TODO: DR 테이블, CI 테이블 필수값 체크

        const saveData = {
            'documentId': this.data.documentId,
            'instanceId': this.data.instanceId,
            'tokenId': (zValidation.isDefined(this.data.tokenId) ? this.data.tokenId : ''),
            'isComplete': (actionType !== 'save'),
            'assigneeId' : (actionType === 'save') ? SESSION['userKey'] : '',
            'assigneeType' : (actionType === 'save') ? DOCUMENT.ASSIGNEE_TYPE : ''
        };
        // 컴포넌트 값
        saveData.componentData = this.zForm.getComponentData();
        //TODO: #10547 폼 리팩토링 - 신청서 저장 - 서버 진행 후 return false 제거
        console.log(saveData);
        return false;

        const actionMsg = (actionType === 'save') ? 'common.msg.save' : 'document.msg.process';
        const url = (saveData.tokenId === '') ? '/rest/tokens/data' : '/rest/tokens/' + saveData.tokenId + '/data';
        aliceJs.fetchText(url, {
            method: (saveData.tokenId === '') ? 'post' : 'put',
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
     * TODO: 인쇄
     */
    printForm() {}
    closeForm() {}

    openProcessStatusPopUp() {
        window.open('/process/[[${instanceId}]]/status', 'process_status_[[${instanceId}]]', 'width=1300, height=500');
    }
}

export const zFormButton = new ZFormButton();