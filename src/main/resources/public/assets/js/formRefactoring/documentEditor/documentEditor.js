/**
 * @projectDescription 신청서 Library.
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as util from '../lib/util.js';
import { FORM } from '../lib/constants.js';
import { validation } from '../lib/validation.js';
import { UIButton, UIDiv } from '../lib/ui.js';
import Form from '../form/form.js';
import Group from '../form/group.js';
import Row from '../form/row.js';
import Component from '../form/component.js';

class DocumentEditor {
    constructor() {
        // 초기화
        this.initDocumentModal();
    }

    /**
     * 신청서를 표시하는 모달 생성
     */
    initDocumentModal() {
        const documentModalTemplate = document.getElementById('documentModalTemplate');
        this.documentModal = new modal({
            title: '',
            body: documentModalTemplate.content.cloneNode(true),
            classes: 'document-modal-dialog document-container',
            buttons:[],
            close: { closable: false },
            onCreate: () => {
                this.domElement = document.getElementById('documentDrawingBoard');
                this.btnDomElement = document.getElementById('documentMainHeader');
            },
            onHide: () => {
                this.domElement.innerHTML = '';
                this.btnDomElement.innerHTML = '';
            }
        });
    }
    /**
     * 신청서 데이터 조회 후 모달 오픈
     * @param documentId 신청서 아이디
     */
    openDocument(documentId) {
        // TODO: 신청서 데이터 load. > 가데이터 삭제 필요
        //util.fetchJson({ method: 'GET', url: '/rest/documents/' + documentId + '/data' })
        util.fetchJson({
            method: 'GET',
            url: '/assets/js/formRefactoring/documentEditor/data_210430.json'
        }).then((documentData) => {
            // TODO: 전달된 데이터의 서버 시간에 따른 날짜/시간 처리
            //this.data = aliceForm.reformatCalendarFormat('read', formData);
            // 정렬 (기준 : displayOrder)
            this.sortJson(documentData.form);
            this.data = documentData;

            const documentMainHeader =  document.getElementById('documentMainHeader');
            documentMainHeader.innerHTML = '';

            this.makeButton();
            this.makeActionButton(this.data.actions);
            this.makeDocument(this.data.form); // Form 생성
            this.documentModal.show(); // 모달 표시
        });
    }
    /**
     * JSON 데이터 정렬 (Recursive)
     * @param data JSON 데이터
     */
    sortJson(data) {
        if (Object.prototype.hasOwnProperty.call(data, 'groups')) { // form
            data.groups.sort((a, b) =>
                a.displayOrder < b.displayOrder ? -1 : a.displayOrder > b.displayOrder ? 1 : 0
            );
            data.groups.forEach( (g) => {
                this.sortJson(g);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'rows')) { // group
            data.rows.sort((a, b) =>
                a.displayOrder < b.displayOrder ? -1 : a.displayOrder > b.displayOrder ? 1 : 0
            );
            data.rows.forEach( (r) => {
                this.sortJson(r);
            });
        } else { // row
            data.components.sort((a, b) =>
                a.displayOrder < b.displayOrder ? -1 : a.displayOrder > b.displayOrder ? 1 : 0
            );
        }
    }
    /**
     * 신청서 상단 프로세스맵, 인쇄 버튼 추가
     * 버튼은 '프로세스맵', '인쇄' 순으로 표기한다.
     * @param data JSON 데이터
     */
    makeButton() {
        // TODO: 인쇄 버튼 추가
        // 버튼 목록 생성
        const UIButtonGroup = new UIDiv().setUIClass('btn-list');
        // 인쇄 버튼
        const UIPrintButton = new UIButton(i18n.msg('common.btn.print')).addUIClass('default-line')
            .onUIClick(this.printDocument.bind(this));
        UIButtonGroup.addUI(UIPrintButton);

        this.btnDomElement.appendChild(UIButtonGroup.domElement);
    }
    /**
     * 신청서 상단 동적 버튼 목록 추가 및 이벤트 생성
     * 저장과 취소 버튼은 기본적으로 생성된다.
     * 버튼은 ['접수' , '반려', '처리'], '저장', '닫기' 순으로 표기한다.
     * @param data JSON 데이터
     */
    makeActionButton(data) {
        if (!validation.isDefined(data)) { return false; }
        // 버튼 목록 생성
        const UIButtonGroup = new UIDiv().setUIClass('btn-list');
        // 동적버튼
        data.forEach( (btn) => {
            if (validation.isEmpty(btn.name)) { return false; }
            UIButtonGroup.addUI(new UIButton(btn.customYn ? btn.name : i18n.msg(btn.name))
                .addUIClass('default-fill')
                .onUIClick(this[btn.value + 'Document'].bind(this)));
        });
        this.btnDomElement.appendChild(UIButtonGroup.domElement);
    }
    /**
     * FORM 생성 (Recursive)
     * @param data JSON 데이터
     * @param parent 부모 객체
     * @param index 추가될 객체의 index
     */
    makeDocument(data, parent, index) {
        if (Object.prototype.hasOwnProperty.call(data, 'groups')) { // form
            this.form = this.addObjectByType(FORM.LAYOUT.FORM, data);
            this.form.parent = parent;
            this.domElement.appendChild(this.form.UIElement.domElement);

            data.groups.forEach( (g, gIndex) => {
                this.makeDocument(g, this.form, gIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'rows')) { // group
            const group = this.addObjectByType(FORM.LAYOUT.GROUP, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // row 에 포함된 component displayType이 모두 hidden이면 group도 숨긴다.
            const checkDisplay = data.rows.some( (row) => row.components.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN));
            if (!checkDisplay) {
                group.UIElement.addUIClass('off');
            }
            data.rows.forEach( (r, rIndex) => {
                this.makeDocument(r, group, rIndex);
            });
        } else if (Object.prototype.hasOwnProperty.call(data, 'components')) { // row
            const row = this.addObjectByType(FORM.LAYOUT.ROW, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 모두 hidden이면 row도 숨긴다.
            const checkDisplay = data.components.some((component) =>
                component.displayType !== FORM.DISPLAY_TYPE.HIDDEN);
            if (!checkDisplay) {
                row.UIElement.addUIClass('off');
            }
            data.components.forEach( (c, cIndex) => {
                this.makeDocument(c, row, cIndex);
            });
        } else { // component
            const component = this.addObjectByType(FORM.LAYOUT.COMPONENT, data, parent, index);
            // TODO: #10540 폼 리팩토링 - 신청서 양식 편집시 설계에 따라 바뀔 수 있음
            // component displayType 이 hidden이면 component를 숨긴다.
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
            addObject = new Form(data);
            break;
        case FORM.LAYOUT.GROUP:
            addObject = new Group(data);
            break;
        case FORM.LAYOUT.ROW:
            addObject = new Row(data);
            break;
        case FORM.LAYOUT.COMPONENT:
            addObject = new Component(data);
            break;
        default:
            break;
        }
        if (parent !== undefined) {
            parent.add(addObject, index);
        }

        return addObject;
    }
    /**
     * 신청서 닫기
     */
    closeDocument() {
        this.documentModal.hide();
    }
    /**
     * TODO: 신청서 처리
     */
    progressDocument() {}
    /**
     * TODO: 신청서 저장
     */
    saveDocument() {}
    /**
     * TODO: 신청서 인쇄
     */
    printDocument() {}
}

export const documentEditor = new DocumentEditor();