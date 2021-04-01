/**
 * 세부 속성 패널 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as util from '../lib/util.js';

export default class Panel {
    constructor() {
        this.domElem = document.getElementById('propertyPanel'); // 속성 패널
        // 3. 세부 속성 load
        util.fetchJson({ method: 'GET', url: '/assets/js/formRefactoring/formDesigner/properties.json' })
            .then((propertiesData) => {
                this.property = propertiesData;
            });
    }
    // 세부 속성 출력
    on() {}
    // 세부 속성 삭제
    off() {
        this.domElem.innerHTML = '';
    }
}