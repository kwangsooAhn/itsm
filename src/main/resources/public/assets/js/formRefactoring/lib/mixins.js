/**
 * Mixin
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { CLASS_PREFIX, FORM } from './constants.js';
import { UILabel, UISpan } from './ui.js';
import { UIRowTooltip } from '../form/row.js';
import { UIComponentTooltip } from '../form/component.js';

// layout 공통 믹스인 ( 부모, 자식 계층 구조용)
export const controlMixin = {
    // 자식 객체 추가
    add(object, index) {
        if (!object) { return false; }

        if (object.parent !== null) {
            object.parent.remove(object);
        }
        object.parent = this;
        object.displayOrder = index;
        this.children.splice(index, 0, object);
        // 재정렬
        this.sort((index + 1));
        // DOM Element 추가
        let parentElement = this.UIElement.domElement;
        if (object.UIElement instanceof UIRowTooltip) {
            parentElement = this.UIElement.UIGroup.domElement;
        } else if (object.UIElement instanceof UIComponentTooltip) {
            parentElement = this.UIElement.UIRow.domElement;
        }
        if ((index + 1) === this.children.length) {
            parentElement.appendChild(object.UIElement.domElement);
        } else {
            const nextSibling = this.children[index + 1].UIElement.domElement;
            parentElement.insertBefore(object.UIElement.domElement, nextSibling);
        }
        return this;
    },
    // 자식 삭제
    remove(object) {
        const index = this.children.indexOf(object);
        if (index !== -1) {
            let parentElement = this.UIElement.domElement;
            if (object.UIElement instanceof UIRowTooltip) {
                parentElement = this.UIElement.UIGroup.domElement;
            } else if (object.UIElement instanceof UIComponentTooltip) {
                parentElement = this.UIElement.UIRow.domElement;
            }
            if (parentElement.contains(object.UIElement.domElement)) {
                parentElement.removeChild(object.UIElement.domElement);
            }
            object.parent = null;
            this.children.splice(index, 1);
            // 재정렬
            this.sort(index);
        }
        return this;
    },
    // 자식 전부 삭제
    clear() {
        this.UIElement.clear();
        for (let i = 0; i < this.children.length; i++) {
            const object = this.children[i];
            object.parent = null;
        }
    },
    // 자식 정렬
    sort(index) {
        for (let i = index; i < this.children.length; i++) {
            this.children[i].displayOrder = i;
        }
    },
    // 복제
    clone(data) {
        return new this.constructor(data).copy(this);
    },
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
export const componentLabelMixin = {
    // 라벨 객체 생성
    makeLabel() {
        const label = new UILabel().setClass(CLASS_PREFIX + 'component-label')
            .addClass((this.label.position === FORM.LABEL.POSITION.HIDDEN ? 'off' : 'on'))
            .setProperty('--data-column', this.getLabelColumnWidth(this.label.position))
            .setTextAlign(this.label.align);
        label.UILabelText = new UISpan().setClass(CLASS_PREFIX + 'component-label-text')
            .setFontSize(this.label.fontSize)
            .setFontWeight((this.label.bold ? 'bold' : ''))
            .setFontStyle((this.label.italic ? 'italic' : ''))
            .setTextDecoration((this.label.underline ? 'underline' : ''))
            .setColor(this.label.fontColor)
            .setTextContent(this.label.text);
        label.add(label.UILabelText);
        // 필수 여부
        label.UIRequiredText = new UISpan().setClass('required');
        label.add(label.UIRequiredText);
        return label;
    },
    // 라벨 너비 계산
    getLabelColumnWidth(position) {
        let labelColumnWidth = FORM.COLUMN; // 12
        if (position === FORM.LABEL.POSITION.HIDDEN) {
            labelColumnWidth = 0;
        } else if (position === FORM.LABEL.POSITION.LEFT) {
            labelColumnWidth -= Number(this.element.columnWidth);
        }
        return labelColumnWidth;
    }
};