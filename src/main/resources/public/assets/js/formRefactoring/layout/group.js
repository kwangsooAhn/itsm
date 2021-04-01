/**
 * 컴포넌트 내부를 이루는 구성 Class.
 *
 *  Group은 1개 이상의 Row로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as util from '../lib/util.js';
import * as mixin from '../lib/mixins.js';
import { FORM } from '../lib/constants.js';

export default class Group {
    constructor(data = {}) {
        this.type = 'group';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.margin = data.margin || '10px 0px 10px 0px'; // 그룹 간 간격(위 오른쪽 아래 왼쪽)
        this.label = util.mergeObject(data.label || FORM.DEFAULT.GROUP_LABEL);
        this.isAccordionUsed = data.isAccordionUsed;

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);
        // UI Mixin import
        util.importMixin(this, mixin.uiMixin);
        // Dynamic Mixin import
        //const properties = ['margin'];
        //util.importDesignedSetter(properties, this);

        this.init();
    }
    // 초기화
    init() {
        this.domElem = this.makeDomElement();
    }
    // DOM 엘리먼트 생성
    makeDomElement() {
        const group = document.createElement('div');
        group.id = this.id;
        group.className = this.type + ' align-left' +
            (this.isAccordionUsed ? ' group-accordion': '');
        group.style.cssText = `margin:${this.margin};`;
        // 아코디언용 체크박스
        if (this.isAccordionUsed) {
            const chkTemplate = `<input type="checkbox" id="chk-${this.id}" class="group-accordion-checkBox" checked="true"/>`;
            group.insertAdjacentHTML('beforeend', chkTemplate);
        }
        // 라벨
        const labelTemplate =
            `<label class="align-${this.label.align} group-labelBox` +
                `${this.isAccordionUsed ? ' group-accordion-labelBox' : ''}" for="chk-${this.id}">`+
                `<span class="group-label" style="` +
                    `color: ${this.label.fontColor}; font-size: ${this.label.fontSize};` +
                    `${this.label.bold ? ' font-weight: bold;' : ''}` +
                    `${this.label.italic ? ' font-style: italic;' : ''}` +
                    `${this.label.underline ? ' text-decoration: underline;' : ''}">` +
                    `${aliceJs.filterXSS(this.label.text)}` +
                `</span>` +
                `<span class="arrow-left"></span>` +
            `</label>`;
        group.insertAdjacentHTML('beforeend', labelTemplate);
        return group;
    }
}