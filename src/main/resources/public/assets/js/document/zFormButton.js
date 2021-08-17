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
import { UIButton, UIDiv } from '../lib/zUI.js';
import { zValidation } from '../lib/zValidation.js';

class ZFormButton {
    constructor() {}
    /**
     * 클래스 초기화
     *
     * @param formDataJson FormType 에 따라 Form 정보 조회 대상이 다르며 FormId, DocumentId, TokenId 값들이 들어 있을 수 있다.
     * @param zForm 버튼의 대상이 되는 Form Class 를 나타낸다. 해당 폼의 값을 이용하기 위해서 필요하다.
     */
    init(formDataJson, zForm) {
        this.isToken = !!(formDataJson.tokenId);
        this.defaultButtonElement = document.getElementById('defaultButtonArea');
        this.actionButtonTopElement = document.getElementById('actionButtonTopArea'); // 상단 동적 버튼 영역
        this.actionButtonBottomElement = document.getElementById('actionButtonBottomArea'); // 하단 동적 버튼 영역
        this.formDataJson = formDataJson;
        this.zForm = zForm;
        // 기본 버튼 추가
        this.makeDefaultButton();
        // 사용자 정의 버튼 추가
        this.makeActionButton(this.formDataJson.actions);
    }
    /**
     * 신청서 상단 프로세스맵, 인쇄 버튼 추가
     * 버튼은 '프로세스맵', '인쇄' 순으로 표기한다.
     */
    makeDefaultButton() {
        if (!zValidation.isDefined(this.defaultButtonElement)) { return false; }
        // 초기화
        this.defaultButtonElement.innerHTML = '';
        
        // 버튼 목록 생성
        const UIButtonGroup = new UIDiv().setUIClass('z-button-list');

        // 닫기 버튼
        const UICloseButton = new UIButton(i18n.msg('common.btn.close')).addUIClass('secondary')
            .onUIClick(this.zForm.close.bind(this.zForm));
        UIButtonGroup.addUI(UICloseButton);

        // 문서함에서 출력되는 경우 프로세스 맵, 인쇄 버튼도 출력
        if (this.isToken) {
            // 프로세스 맵 버튼
            const UIProcessMapButton = new UIButton(i18n.msg('process.label.processMap')).addUIClass('secondary')
                .onUIClick(this.zForm.openProcessStatusPopUp.bind(this.zForm));
            UIButtonGroup.addUI(UIProcessMapButton);

            // 인쇄 버튼
            const UIPrintButton = new UIButton(i18n.msg('common.btn.print')).addUIClass('secondary')
                .onUIClick(this.zForm.print.bind(this.zForm));
            UIButtonGroup.addUI(UIPrintButton);
        }

        this.defaultButtonElement.appendChild(UIButtonGroup.domElement);
    }
    /**
     * 신청서 상단 동적 버튼 목록 추가 및 이벤트 생성
     * 저장과 취소 버튼은 기본적으로 생성된다.
     * 버튼은 ['접수' , '반려', '처리'], '저장', '닫기' 순으로 표기한다.
     * @param actions JSON 데이터
     */
    makeActionButton(actions) {
        if (!zValidation.isDefined(actions) ||
            !zValidation.isDefined(this.actionButtonTopElement) ||
            !zValidation.isDefined(this.actionButtonBottomElement)) { return false; }
        // 초기화
        this.actionButtonTopElement.innerHTML = '';
        this.actionButtonBottomElement.innerHTML = '';

        // 버튼 목록 생성
        const UIButtonTopGroup = new UIDiv().setUIClass('z-button-list').addUIClass('justify-content-end');
        const UIButtonBottomGroup = new UIDiv().setUIClass('z-button-list').addUIClass('justify-content-end');
        // 동적 버튼
        actions.forEach( (btn) => {
            if (zValidation.isEmpty(btn.name)) { return false; }
            const UIActionTopButton = new UIButton(btn.customYn ? btn.name : i18n.msg(btn.name))
                .addUIClass('primary')
                .onUIClick(this.zForm.processAction.bind(this.zForm, btn.value));
            UIButtonTopGroup.addUI(UIActionTopButton);

            const UIActionBottomButton = new UIButton(btn.customYn ? btn.name : i18n.msg(btn.name))
                .addUIClass('primary')
                .onUIClick(this.zForm.processAction.bind(this.zForm, btn.value));
            UIButtonBottomGroup.addUI(UIActionBottomButton);
        });
        this.actionButtonTopElement.appendChild(UIButtonTopGroup.domElement);
        this.actionButtonBottomElement.appendChild(UIButtonBottomGroup.domElement);
    }
}

export const zFormButton = new ZFormButton();