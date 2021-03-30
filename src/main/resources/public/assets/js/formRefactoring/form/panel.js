/**
 * 세부 속성 패널 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

export default class Panel {
    constructor(property = {}) {
        this.property = property;
        this.domElem = document.getElementById('propertyPanel'); // 속성 패널
    }
    // 세부 속성 출력
    on(type) {}
    // 세부 속성 삭제
    off() {
        this.domElem.innerHTML = '';
    }
}