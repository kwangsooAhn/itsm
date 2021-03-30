/**
 * Form Class.
 *
 * 폼은 1개, 혹은 그 이상의 Group으로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as MIXIN from '../lib/mixin.js';

export default class Form {
    constructor(data = {}) {
        this.type = 'form';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서f
        this.name = data.name || '';
        this.desc = data.desc || '';
        this.status = data.status || 'form.status.edit'; // 문서 상태 : 편집, 발생, 사용, 폐기
        this.width = data.width || '905px';
        this.margin = data.margin || '60px 0px 60px 0px';
        this.padding = data.padding || '35px 35px 35px 35px';
        this.category = data.category || 'process'; // process | cmdb
        this.readyState = 'initialized'; //폼 상태 (initialized, interactive, complete)

        // Control Mixin import
        MIXIN.importMixin(this, MIXIN.controlMixin);
        // Dynamic Mixin import
        const properties = ['name', 'desc', 'status', 'readyState',
            'style-width', 'style-margin', 'style-padding'];
        MIXIN.dynamicMixin(properties, this);

        this.init();
    }
    // 초기화
    init() {
        this.domElem = this.makeDomElement();
    }
    // DOM 엘리먼트 생성
    makeDomElement() {
        const form = document.createElement('div');
        form.id = this.id;
        form.className = this.type;
        form.style.cssText = `width:${this.width};` +
            `margin:${this.margin};` +
            `padding:${this.padding};`;
        return form;
    }
}
