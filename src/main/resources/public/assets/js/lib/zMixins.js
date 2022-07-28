/**
 * Mixin
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { FORM } from './zConstants.js';
import { UIDiv, UISpan, UIUl, UILi } from './zUI.js';
import { UIGroupTooltip } from '../form/zGroup.js';
import { UIRowTooltip } from '../form/zRow.js';
import { UIComponentTooltip } from '../form/zComponent.js';

// layout 공통 믹스인 ( 부모, 자식 계층 구조용)
export const controlMixin = {
    /**
     * 자식 객체 추가
     * @param object group, row, component 객체
     * @param index 인덱스
     */
    add(object, index) {
        if (!object) { return false; }

        if (object.parent !== null) {
            object.parent.remove(object);
        }
        object.parent = this;
        object.displayDisplayOrder = index;
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
    /**
     * 자식 객체 삭제 (단일)
     * @param object group, row, component 객체
     */
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
    /**
     * 자식 객체 전부 삭제
     */
    clear() {
        this.UIElement.clearUI();
        for (let i = 0; i < this.children.length; i++) {
            const object = this.children[i];
            object.parent = null;
        }
    },
    /**
     * 자식 객체 정렬
     * @param index 인덱스
     */
    sort(index) {
        for (let i = index; i < this.children.length; i++) {
            this.children[i].displayDisplayOrder = i;
        }
    },
    /**
     * 복제
     * @param flag 객체의 키가되는 id도 복제할지 여부 (true이면 id도 복제됨)
     * @param data 인스턴스에 전달되는 데이터
     */
    clone(flag, data) {
        return new this.constructor(data).copy(this, flag);
    },
    /**
     * 아이디로 객체 조회
     * @param id 아이디
     */
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

// tooltip menu 공통 믹스인
export const toolTipMenuMixin = {
    /**
     * 툴팁 DOM 객체 생성
     */
    makeTooltip() {
        const tooltipMenu = new UIDiv().setUIClass('context-menu');
        tooltipMenu.UIUl = new UIUl().setUIClass('context-group');

        // copy
        tooltipMenu.UIUl.UILiCopy = new UILi().setUIClass('context-menu__item')
            .addUIClass('tooltip')
            .setUIAttribute('data-action', 'copy')
            .addUI(new UISpan().setUIClass('ic-copy'))
            .onUIClick(this.copyObject.bind(this))
            .addUI(new UIDiv().setUIClass('tooltip__box')
                .addUI(new UISpan().setUIClass('tooltip__box__text').setUITextContent(i18n.msg('tooltip.label.copy')))
            );
        tooltipMenu.UIUl.addUI(tooltipMenu.UIUl.UILiCopy);

        // remove
        tooltipMenu.UIUl.UILiRemove = new UILi().setUIClass('context-menu__item')
            .addUIClass('tooltip')
            .setUIAttribute('data-action', 'remove')
            .addUI(new UISpan().setUIClass('ic-delete'))
            .onUIClick(this.removeObject.bind(this))
            .addUI(new UIDiv().setUIClass('tooltip__box')
                .addUI(new UISpan().setUIClass('tooltip__box__text').setUITextContent(i18n.msg('tooltip.label.delete')))
            );
        tooltipMenu.UIUl.addUI(tooltipMenu.UIUl.UILiRemove);

        tooltipMenu.addUI(tooltipMenu.UIUl);
        return tooltipMenu;
    },

    /**
     * 복사한 객제의 ID를 신규로 변경
     * @param data
     */
    replaceCopyObjectToNewId(data) {
        if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            data.id = ZWorkflowUtil.generateUUID();
            data.row.forEach( (r) => {
                this.replaceCopyObjectToNewId(r);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            data.id = ZWorkflowUtil.generateUUID();
            data.component.forEach( (c) => {
                this.replaceCopyObjectToNewId(c);
            });
        } else { // component
            data.id = ZWorkflowUtil.generateUUID();
        }
    },
    /**
     * group, row, component 객체 복제
     */
    copyObject() {
        // 부모 타입이 row이고, 컴포넌트 최대 개수를 초과할 경우 추가되지 않는다.
        if (this.UIElement instanceof UIComponentTooltip &&
            this.parent.children.length >= FORM.MAX_COMPONENT_IN_ROW) {
            return false;
        }
        // 복사본 생성
        let cloneData = JSON.parse(JSON.stringify(this.toJson()));
        this.replaceCopyObjectToNewId(cloneData);

        let editor = this.parent;
        if (this.UIElement instanceof UIGroupTooltip) {
            editor = this.parent.parent;
        } else if (this.UIElement instanceof UIRowTooltip) {
            editor = this.parent.parent.parent;
        } else if (this.UIElement instanceof UIComponentTooltip) {
            editor = this.parent.parent.parent.parent;
        }
        // 복사하여 바로 아래 추가
        editor.makeForm(cloneData, this.parent, (this.displayDisplayOrder + 1));
        const copyObject = this.parent.getById(cloneData.id);
        editor.history.save([{
            type: 'add',
            from: { id: '', clone: null },
            to: { id: this.parent.id, clone: copyObject.clone(true, { type: this.type }).toJson() }
        }]);
        // 복사한 객체 선택
        copyObject.UIElement.domElement.dispatchEvent(new Event('click'));
    },
    /**
     * group, row, component 객체 삭제
     */
    removeObject(e) {
        if (e) { // tooltip 선택시 drag & drop 이벤트 중지
            e.stopPropagation();
            e.preventDefault();
        }

        const histories = [];  // 이력

        let editor = this.parent;
        if (this.UIElement instanceof UIGroupTooltip) {
            editor = this.parent.parent;
        } else if (this.UIElement instanceof UIRowTooltip) {
            editor = this.parent.parent.parent;
        } else if (this.UIElement instanceof UIComponentTooltip) {
            editor = this.parent.parent.parent.parent;
        }
        const cloneObject = this.clone(true, { type: this.type });
        const parentObject = this.parent;
        // 삭제
        histories.push({
            type: 'remove',
            from: { id: this.parent.id, clone: cloneObject.toJson() },
            to: { id: '', clone: null }
        });
        parentObject.remove(this);
        // 컴포넌트 삭제시, row, group 내에 컴포넌트가 하나라도 존재하지 않으면 모두 삭제
        editor.deleteRowChildrenEmpty(parentObject, histories);
        // 이력 저장
        editor.history.save(histories.reverse());
        // 타입이 동일한 바로 이전 객체 선택, 하나도 존재하지 않으면 form 선택
        if (parentObject.children[cloneObject.displayDisplayOrder - 1]) {
            parentObject.children[cloneObject.displayDisplayOrder - 1].UIElement
                .domElement.dispatchEvent(new Event('click'));
        } else {
            editor.form.UIElement.domElement.dispatchEvent(new Event('click'));
        }
    }
};