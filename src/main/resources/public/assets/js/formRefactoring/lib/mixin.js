/**
 * Mixin
 * Mixin이란 프로토타입을 바꾸지 않고 한 객체의 프로퍼티를 다른 객체에게 ‘복사’해서 사용하는 방식이다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
// 믹스인 추가
export function importMixin(target, source) {
    for (const key in source) {
        if (source.hasOwnProperty(key)) {
            target[key] = source[key];
        }
    }
    return target;
}
// 동적 믹스인 추가 (속성을 loop 돌며 setX 메소드 생성)
export function dynamicMixin(properties, target) {
    properties.forEach( property => {
        const fields = property.split('-');
        const field = (fields.length === 1) ? fields[0] : fields[1];
        const method = 'set' + field.substr( 0, 1 ).toUpperCase() +
            field.substr( 1, field.length );
        if (target.hasOwnProperty(field)) {
            if (fields.length === 1) { // 일반 속성
                target[method] = function () {
                    this[property] = arguments;
                    return this;
                };
            } else { // 스타일 속성 : style-padding 등
                target[method] = function () {
                    this[property] = arguments;
                    this.setStyle(property, arguments);
                    return this;
                };
            }
        }
    });
}

// layout 공통 믹스인 ( 부모, 자식 계층 구조용)
export const controlMixin = {
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
// label 공통 믹스인
export const labelMixin = {
    makeLabel() {
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

        return labelBox;
    }
};