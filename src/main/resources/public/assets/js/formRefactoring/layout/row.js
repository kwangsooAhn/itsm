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

import * as MIXIN from '../lib/mixin.js';

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
        MIXIN.importMixin(this, MIXIN.controlMixin);
        // Dynamic Mixin import
        const properties = ['style-margin', 'style-padding'];
        MIXIN.dynamicMixin(properties, this);

        this.init();
    }
    // 초기화
    init() {
        this.domElem = this.makeDomElement();
    }
    // DOM 엘리먼트 생성
    makeDomElement() {
        const row = document.createElement('div');
        row.id = this.id;
        row.className = this.type;
        row.style.cssText = `margin:${this.margin};padding:${this.padding};`;
        return row;
    }
}