/**
 * [화면 변경사항 체크]
 * 화면에서 특정 부분의 변경사항을 감시하고
 * 변경 시 변경 여부를 업데이트한다.
 *
 * 옵션 항목에 대한 설명은 아래 사이트 참고
 * https://developer.mozilla.org/en-US/docs/Web/API/MutationObserver/observe
 *
 * @author Jung Hee Chan
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
aliceJs.pageObserver = {
    isChanged: false,
    set: function () {
        this.isChanged = false;
        document.querySelectorAll('.z-edit-form').forEach((element) => {
            element.addEventListener('change', () => {
                this.isChanged = true
            });
        })
    },
    canPageChange: function () {
        if (this.isChanged) {
            if (!confirm(i18n.msg('common.msg.confirmNotSave'))) {
                return;
            }
        }
        return true;
    }
}
