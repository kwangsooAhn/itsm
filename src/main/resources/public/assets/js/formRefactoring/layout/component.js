/**
 * Component 공통 Class
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import { controlMixin } from './lib/mixin.js';
import { FORM } from '../lib/constant.js';

export default class Component {
    constructor(data) {
        const mergeData = Object.assign(data, {
            id: data.id || workflowUtil.generateUUID()
        });

        Object.entries(mergeData).forEach(([key, value]) => {
            this[key] = value;
        });

        Object.assign(this, controlMixin);

        // 컴포넌트
        const component = document.createElement('div');
        component.id = this.id;
        component.className = 'component';
        component.setAttribute('data-type', this.type);
        component.setAttribute('data-displayOrder', this.displayOrder);
        component.setAttribute('data-displayType', this.displayType);

        this.domElem = component;
    }
    // 툴팁 출력
    drawTooltip() {}
    // 라벨 출력
    drawLabel() {
        return new Promise( (resolve, reject) => {
            // 라벨 그룹
            const labelBox = document.createElement('div');
            let labelColumnWidth = CONST.FORM.DEFAULT_COLUMN; // 12
            if (this.label.position === CONST.FORM.LABEL.POSITION.HIDDEN) {
                labelColumnWidth = 0;
            } else if (this.label.position === CONST.FORM.LABEL.POSITION.LEFT) {
                labelColumnWidth -= Number(this.element.columnWidth);
            }
            labelBox.className = `component-label-box ` +
                `align-${this.label.align} ` +
                `position-${this.label.position}`;
            // cssText 사용시 리플로우와 리페인트 최소화됨
            labelBox.style.cssText = `--data-column:${labelColumnWidth};`;

            // 라벨 문구
            const label = document.createElement('label');
            label.className = 'component-label';
            label.style.cssText = `color:${this.label.fontColor};` +
                `font-size:${this.label.fontSize}px;` +
                `${this.label.bold ? 'font-weight:bold;' : ''}` +
                `${this.label.italic ? 'font-style:italic;' : ''}` +
                `${this.label.underline ? 'text-decoration:underline;' : ''}`;
            label.textContent = this.label.text;
            labelBox.appendChild(label);

            // 필수값
            const required = document.createElement('span');
            required.className = 'required';
            labelBox.appendChild(required);

            resolve(labelBox);
        });
    }
}