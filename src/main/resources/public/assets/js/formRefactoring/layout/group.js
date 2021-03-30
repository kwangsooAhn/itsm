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
import { controlMixin } from './lib/mixin.js';

const DEFAULT = {
    margin: '10px 0px 10px 0px', // 그룹 간 간격(위 오른쪽 아래 왼쪽)
    padding: '10px 10px 10px 10px', // 그룹 내부 여백(위 오른쪽 아래 왼쪽)
    label: {
        position: 'top',
        fontSize: '16px',
        fontColor: 'rgba(0,0,0,1)',
        bold: false,
        italic: false,
        underline: false,
        align: 'left',
        text: 'GROUP LABEL'
    },
    accordion: {
        isUsed: false,
        thickness: 1,
        color: 'rgba(235, 235, 235, 1)'
    }
};

export default class Group {
    constructor(data = {}) {
        this.type = 'group';
        const mergeData = Object.assign(DEFAULT, data, {
            id: data.id || workflowUtil.generateUUID()
        });

        Object.entries(mergeData).forEach(([key, value]) => {
            if (key !== 'rows') {
                this[key] = value;
            }
        });

        Object.assign(this, controlMixin);

        const group = document.createElement('div');
        group.id = this.id;
        group.className = 'group';
        group.setAttribute('data-type', this.type);
        // TODO: 라벨
        // TODO: 아코디언

        this.domElem = group;
    }
}