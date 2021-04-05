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

// layout 공통 믹스인 ( 부모, 자식 계층 구조용)
export const controlMixin = {
    // 자식 객체 추가
    add(object) {
        if (!object) { return false; }

        if (object.parent !== null) {
            object.parent.remove(object);
            object.parent.UIElem.remove(
                (object.UITooltip !== undefined) ? object.UITooltip : object.UIElem
            );
        }
        object.parent = this;
        object.displayOrder = this.children.length;
        this.children.push(object);
        // dom 객체 삭제
        if (object.UITooltip !== undefined) {
            this.UIElem.add(object.UITooltip);
        } else {
            this.UIElem.add(object.UIElem);
        }

        return this;
    },
    // 자식 삭제
    remove(object) {
        const idx = this.children.indexOf(object);
        if (idx !== -1) {
            if (object.UITooltip !== undefined) {
                object.parent.UIElem.remove(object.UITooltip);
            } else {
                object.parent.UIElem.remove(object.UIElem);
            }
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
export const componentLabelMixin = {
    // 라벨 객체 생성
    makeLabel() {
        const label = new UILabel().setClass(CLASS_PREFIX + 'component-label')
            .addClass((this.label.position === FORM.LABEL.POSITION.HIDDEN ? 'off' : 'on'))
            .setStyle('--data-column', this.getLabelColumnWidth(this.label.position))
            .setTextAlign(this.label.align);
        label.labelText = new UISpan().setClass(CLASS_PREFIX + 'component-label-text')
            .setFontSize(this.label.fontSize)
            .setFontWeight((this.label.bold ? 'bold' : ''))
            .setFontStyle((this.label.italic ? 'italic' : ''))
            .setTextDecoration((this.label.underline ? 'underline' : ''))
            .setColor(this.label.fontColor)
            .setTextContent(this.label.text);
        label.add(label.labelText);
        // 필수 여부
        label.requiredText = new UISpan().setClass('required');
        label.add(label.requiredText);
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