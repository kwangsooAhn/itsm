/**
 * 문서함 폼 그리기 Class
 *
 * 폼은 폼 디자이너, 신청서, 문서함등에서 그려지며, 약간씩 차이가 있다.
 * 여기서는 문서함에서 열게 되는 처리할 문서, 진행중 문서, 완료된 문서등을 대상으로 폼을 그린다.
 *
 * @author jung hee chan (hcjung@brainz.co.kr)
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import ZComponent from '../form/zComponent.js';
import ZForm from '../form/zForm.js';
import ZGroup from '../form/zGroup.js';
import ZRow from '../form/zRow.js';
import { FORM } from '../lib/zConstants.js';

class ZFormToken {
    constructor() {
    }
    /**
     * 클래스 초기화
     *
     * @param domElement Form 그리고자 하는 대상 DOM Element
     * @param formData
     */
    init(domElement, formData) {
        this.domElement = domElement;
        // 아래 URL 은 추후에 '/rest/tokens/' + formOption.targetTokenId + '/data' 로 수정해야 함.
        aliceJs.fetchJson('/assets/js/document/token_test.json', {
            method: 'GET'
        }).then((formData) => {
            this.formDataJson = formData.form;
            this.sortFormObject(this.formDataJson);
            this.composeForm(this.formDataJson);
        });
    }
    /**
     * Form 의 구성요소 3가지(Group, Row, Component)를 출력 순서로 정렬한다.
     *
     * @param formObject JSON 데이터
     */
    sortFormObject(formObject) {
        if (Object.prototype.hasOwnProperty.call(formObject, 'group')) {
            formObject.group.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            formObject.group.forEach( (g) => {
                this.sortFormObject(g);
            });
        } else if (Object.prototype.hasOwnProperty.call(formObject, 'row')) {
            formObject.row.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
            formObject.row.forEach( (r) => {
                this.sortFormObject(r);
            });
        } else {
            formObject.component.sort((a, b) =>
                a.displayDisplayOrder < b.displayDisplayOrder ? -1 : a.displayDisplayOrder > b.displayDisplayOrder ? 1 : 0
            );
        }
    }
    /**
     * FORM 생성 (Recursive)
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    composeForm(data, parent, index) {
        if (Object.prototype.hasOwnProperty.call(data, 'group')) { // form
            this.form = this.addObjectByType(FORM.LAYOUT.FORM, data);
            this.form.parent = parent;
            this.domElement.appendChild(this.form.UIElement.domElement);
            this.form.afterEvent();

            data.group.forEach( (g, gIndex) => {
                this.composeForm(g, this.form, gIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'row')) { // group
            const group = this.addObjectByType(FORM.LAYOUT.GROUP, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // row 에 포함된 component displayType 이 모두 hidden 이면 group 도 숨긴다.
            const checkDisplay = data.row.some( (row) => row.component.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN));
            if (!checkDisplay) {
                group.UIElement.addUIClass('off');
            }
            data.row.forEach( (r, rIndex) => {
                this.composeForm(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'component')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 모두 hidden 이면 row 도 숨긴다.
            const checkDisplay = data.component.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN);
            if (!checkDisplay) {
                row.UIElement.addUIClass('off');
            }
            data.component.forEach( (c, cIndex) => {
                this.composeForm(c, row, cIndex);
            });
        } else { // component
            const component = this.addObjectByType(FORM.LAYOUT.COMPONENT, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 hidden 이면 component 를 숨긴다.
            if (data.displayType === FORM.DISPLAY_TYPE.HIDDEN) {
                component.UIElement.addUIClass('off');
            }
        }
    }
    /**
     * form, group, row, component 타입에 따른 객체 추가
     * @param type form, group, row, component 타입
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    addObjectByType(type, data, parent, index) {
        let addObject = null; // 추가된 객체

        switch(type) {
        case FORM.LAYOUT.FORM:
            addObject = new ZForm(data);
            break;
        case FORM.LAYOUT.GROUP:
            addObject = new ZGroup(data);
            break;
        case FORM.LAYOUT.ROW:
            addObject = new ZRow(data);
            break;
        case FORM.LAYOUT.COMPONENT:
            addObject = new ZComponent(data);
            break;
        default:
            break;
        }
        if (parent && addObject) {
            parent.add(addObject, index);
            addObject.afterEvent();
        }

        return addObject;
    }
    /**
     * 컴포넌트 value 데이터 조회
     */
    getComponentData(object, array) {
        object.children.forEach((child) => {
            if (child instanceof ZComponent) {
                array.push({ componentId: child.id, value: (Array.isArray(this.value) ? JSON.stringify(child.value) : child.value) });
            } else {
                this.getComponentData(child, array);
            }
        });
        return array;
    }
}

export const zFormToken = new ZFormToken();