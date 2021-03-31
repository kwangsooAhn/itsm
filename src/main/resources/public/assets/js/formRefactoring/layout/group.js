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
import * as mixin from '../lib/mixin.js';

// 기본값
const LABEL = {
    position: 'top',
    fontSize: '16px',
    fontColor: 'rgba(0,0,0,1)',
    bold: false,
    italic: false,
    underline: false,
    align: 'left',
    text: 'LABEL'
};
const ACCORDION = {
    isUsed: false,
    thickness: '1px',
    color: 'rgba(235, 235, 235, 1)'
};

export default class Group {
    constructor(data = {}) {
        this.type = 'group';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.margin = data.margin || '10px 0px 10px 0px'; // 그룹 간 간격(위 오른쪽 아래 왼쪽)
        this.padding = data.padding || '10px 10px 10px 10px'; // 그룹 내부 여백(위 오른쪽 아래 왼쪽)
        this.label = data.label || LABEL;
        this.accordion = data.accordion || ACCORDION;

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);
        // UI Mixin import
        util.importMixin(this, mixin.uiMixin);
        // Dynamic Mixin import
        //const properties = ['margin', 'padding'];
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
        group.className = this.type;
        group.style.cssText = `margin:${this.margin};padding:${this.padding};`;
        // TODO: 라벨
        // TODO: 아코디언
        return group;
    }
}