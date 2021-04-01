/**
 * Mixin
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { FORM } from './constants.js';

// layout 공통 믹스인 ( 부모, 자식 계층 구조용)
export const controlMixIn = {
    // 자식 객체 추가
    add(object) {
        if (!object) { return false; }

        if (object.parent !== null) {
            object.parent.remove(object);
            object.parent.UIElem.remove(object.UIElem);
        }
        object.parent = this;
        object.displayOrder = this.children.length;
        this.children.push(object);
        this.UIElem.add(object.UIElem);

        return this;
    },
    // 자식 삭제
    remove(object) {
        const idx = this.children.indexOf(object);
        if (idx !== -1) {
            object.parent.UIElem.remove(object.UIElem);
            object.parent = null;
            this.children.splice(idx, 1);
            // 재정렬
            for (let i = idx; i < this.children.length; i++) {
                this.children[i].displayOrder = i;
            }
        }
        return this;
    },
    // 자식 전부 삭제
    clear() {
        this.UIElem.clear();
        for (let i = 0; i < this.children.length; i++) {
            const object = this.children[i];
            object.parent = null;
        }
    },
    // 복사 (자식 포함)
    copy() {},
    // 객체 조회
    getById(id) {
        if (this['id'] === id) {
            return this;
        }

        for (let i = 0, l = this.children.length; i < l; i++) {
            const object = this.children[i].getById(id);

            if (object !== undefined) {
                return object;
            }
        }
        return undefined;
    }
};
// label 공통 믹스인
export const labelMixin = {
    makeLabelElement() {
        // 라벨 그룹
        const labelBox = document.createElement('div');
        let labelColumnWidth = FORM.COLUMN; // 12
        if (this.label.position === FORM.LABEL.POSITION.HIDDEN) {
            labelColumnWidth = 0;
        } else if (this.label.position === FORM.LABEL.POSITION.LEFT) {
            labelColumnWidth -= Number(this.element.columnWidth);
        }
        labelBox.className = `component-label-box ` +
            `align-${this.label.align} ` +
            `position-${this.label.position}`;
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

        return labelBox;
    }
};