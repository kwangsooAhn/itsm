/**
 * 문서함 버튼 그리기 Class
 *
 * 문서함에서 열리는 폼에는 문서의 상태나 사용자 권한과 무관하게 기본적으로 제공하는 버튼들과
 * 문서 상태와 사용자 권한에 따라 선택적으로 출력되는 버튼들이 있다.
 * 여기서는 그러한 버튼들의 출력과 동작을 정의한다.
 *
 * @author jung hee chan (hcjung@brainz.co.kr)
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { DOCUMENT, SESSION } from '../lib/zConstants.js';
import { UIButton, UIDiv } from '../lib/zUI.js';
import { zValidation } from '../lib/zValidation.js';

class ZFormButton {
    constructor() {
    }
    /**
     * 클래스 초기화
     *
     * @param domElement Form 그리고자 하는 대상 DOM Element
     * @param formDataJson FromType 에 따라 Form 정보 조회 대상이 다르며 FormId, DocumentId, TokenId 값들이 들어 있을 수 있다.
     * @param zForm 버튼의 대상이 되는 Form Class 를 나타낸다. 해당 폼의 값을 이용하기 위해서 필요하다.
     */
    init(domElement, formDataJson, zForm) {
        this.domElement = domElement;
        this.formDataJson = formDataJson;
        this.zForm = zForm;
        this.makeActionButton(this.formDataJson.actions);
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
     * @param actions JSON 데이터
     */
    makeActionButton(actions) {
        if (!zValidation.isDefined(actions)) { return false; }
        // 버튼 목록 생성
        const UIButtonGroup = new UIDiv().setUIClass('btn-list');
        // 동적버튼
        actions.forEach( (btn) => {
            if (zValidation.isEmpty(btn.name)) { return false; }
            let UIActionButton = new UIButton(btn.customYn ? btn.name : i18n.msg(btn.name))
                .addUIClass('default-line')

            switch(btn.value) {
                case 'close':
                    UIActionButton.onUIClick(this.closeForm.bind(this));
                    break;
                default :
                    UIActionButton.onUIClick(this.updateToken.bind(this, btn.value));
            }
            UIButtonGroup.addUI(UIActionButton);
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
            'documentId': this.formDataJson.documentId,
            'instanceId': this.formDataJson.instanceId,
            'tokenId': (zValidation.isDefined(this.formDataJson.tokenId) ? this.formDataJson.tokenId : ''),
            'isComplete': (actionType !== 'save'),
            'assigneeId' : (actionType === 'save') ? SESSION['userKey'] : '',
            'assigneeType' : (actionType === 'save') ? DOCUMENT.ASSIGNEE_TYPE : ''
        };
        // 컴포넌트 값
        saveData.componentData = this.zForm.getComponentData(this.zForm.form, []);

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
    closeForm() { window.close();}

    openProcessStatusPopUp() {
        window.open('/process/[[${instanceId}]]/status', 'process_status_[[${instanceId}]]', 'width=1300, height=500');
    }
}

export const zFormButton = new ZFormButton();