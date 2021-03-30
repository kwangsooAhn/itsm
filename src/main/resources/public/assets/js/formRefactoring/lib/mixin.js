/**
 * Control Mixin
 * Form, Group, Row, Component 모두 Control에 의해 조작되는 객체이다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
export const controlMixin = {
    parent: null, // 부모 객체
    children: null, // 자식 객체
    displayOrder: 0, // 표시 순서
    domElem: document.body,
    // 자식 객체 추가
    add(object) {
        if (!object) { return false; }

        if (object.parent !== null) {
            object.parent.remove(object);
            object.parent.domElem.removeChild(object.domElem);
        }
        object.parent = this;
        object.displayOrder = this.children.length;
        this.children.push(object);
        this.domElem.appendChild(object.domElem);

        return this;
    },
    // 자식 삭제
    remove(object) {
        const idx = this.children.indexOf(object);
        if (idx !== -1) {
            object.parent.domElem.removeChild(object.domElem);
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
        for (let i = 0; i < this.children.length; i++) {
            const object = this.children[i];
            this.domElem.removeChild(object.domElem);
            object.parent = null;
        }
    },
    // 복사 (자식 포함)
    copy() {},
    // 객체 조회
    getObjectById(id) {
        if (this['id'] === id) {
            return this;
        }

        for (let i = 0, l = this.children.length; i < l; i++) {
            const object = this.children[i].getComponentById(id);

            if (object !== undefined) {
                return object;
            }
        }
        return undefined;
    },
    // domElem 스타일 변경
    setStyle(style, args) {
        for (let i = 0; i < args.length; i++) {
            this.domElem.style[style] = args[i];
        }
        return this;
    }
};