/**
 * Group 내부를 이루는 구성 Class.
 *
 *  내부적으로 너비를 가지는 복수개의 엘리먼트들로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import * as util from '../lib/util.js';
import * as mixin from '../lib/mixins.js';
import { UIRow } from '../lib/ui.js';

export default class Row {
    constructor(data = {}) {
        this.type = 'row';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.margin = data.margin || '10px 0px 10px 0px'; // row 간 간격(위 오른쪽 아래 왼쪽)
        this.padding = data.padding || '10px 10px 10px 10px'; // row 내부 여백(위 오른쪽 아래 왼쪽)

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        this.UIElem = new UIRow()
            .setId(this.id)
            .setMargin(this.margin)
            .setPadding(this.padding);
    }
}