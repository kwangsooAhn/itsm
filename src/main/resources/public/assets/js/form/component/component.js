/**
 * Component 공통 Class
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

let displayOrder = 0; // 컴포넌트 출력 순서

export default class Component {
    constructor(data) {
        const mergeData = Object.assign(data, {
            id: data.id || workflowUtil.generateUUID(),
            displayOrder: data.displayOrder || ++displayOrder
        });

        Object.entries(mergeData).forEach(([key, value]) => {
            this[key] = value;
        });

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
    drawTooltip() {

    }
    // 라벨 출력
    drawLabel() {
        // 라벨 그룹
        const labelBlock = document.createElement('div');
        labelBlock.className = `component-label-block ` +
            `align-${this.label.align} ` +
            `position-${this.label.position}`;
        // cssText 사용시 리플로우와 리페인트 최소화됨
        labelBlock.style.cssText = `--data-column:${this.label.column};`;
        
        // 라벨 문구
        const label = document.createElement('label');
        label.className = 'component-label';
        label.style.cssText = `color:${this.label.fontColor};font-size:${this.label.fontSize}px;` +
            `${this.label.bold ? 'font-weight:bold;' : ''}` +
            `${this.label.italic ? 'font-style:italic;' : ''}` +
            `${this.label.underline ? 'text-decoration:underline;' : ''}`;
        label.textContent = this.label.text;
        labelBlock.appendChild(label);

        // 필수값
        const required = document.createElement('span');
        required.className = 'required';
        labelBlock.appendChild(required);

        this.domElem.appendChild(labelBlock);

        // 영역을 할당하기 위한 Empty Block
        //const emptyBlock = document.createElement('div');
        //emptyBlock.className = 'component-label-block';
        //emptyBlock.style.cssText = `--data-column:${this.label.column};`;
        //this.domElem.appendChild(labelBlock);
    }
}