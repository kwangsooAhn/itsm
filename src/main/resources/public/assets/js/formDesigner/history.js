/**
 * 이력을 관리하는 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
export default class History {
    constructor(editor) {
        this.editor = editor;
        this.undoList = [];
        this.redoList = [];
        this.status = false; // 수정 여부
    }

    /**
     * 이력 초기화
     */
    reset() {
        this.undoList = [];
        this.redoList = [];
        this.status = false;
    }
    /**
     * 이력 저장
     * @param data 저장할 데이터
     * @param list undo/redo 목록
     * @param flag redo 목록 유지할지 여부 : false이면 redo 목록을 비운다.
     */
    save(data, list, flag) {
        if (data.length === 0) { return false; }
        if (data.length === 1 && workflowUtil.compareJson(data[0].from, data[0].to)) {
            return false;
        }

        flag = flag || false;
        if (!flag) { this.redoList = []; }

        (list || this.undoList).push(data);
        this.status = (this.undoList.length > 0); // undoList가 없으면 수정된 항목이 없음을 의미한다.
    }
    /**
     * 되돌리기
     */
    undo() {
        if (this.undoList.length) {
            let restoreData = this.undoList.pop();
            this.restore('undo', restoreData);
            this.save(restoreData, this.redoList, true);
            this.editor.deSelectObject();
        }
    }
    /**
     * 재실행
     */
    redo() {
        if (this.redoList.length) {
            let restoreData = this.redoList.pop();
            this.restore('redo', restoreData);
            this.save(restoreData, this.undoList, true);
            this.editor.deSelectObject();
        }
    }
    /**
     * 적용
     * @param type undo | redo
     * @param histories 이력 데이터
     */
    restore(type, histories) {
        const historiesData = histories.slice();
        if (historiesData.length > 1 && type === 'redo') {
            historiesData.reverse();
        }
        historiesData.forEach((data) => {
            switch(data.type) {
            case 'add':
                const toParent = this.editor.form.getById(data.to.id);
                if (type === 'redo') { // 복제한 객체를 다시 추가
                    this.editor.makeForm(data.to.clone, toParent, data.to.clone.displayDisplayOrder);
                    // 추가된 객체 선택
                    toParent.getById(data.to.clone.id).UIElement.domElement.dispatchEvent(new Event('click'));
                } else { // 기존 추가한 객체를 삭제
                    const to = this.editor.form.getById(data.to.clone.id);
                    toParent.remove(to);
                }
                break;
            case 'remove':
                const fromParent = this.editor.form.getById(data.from.id);
                if (type === 'redo') {
                    const from = this.editor.form.getById(data.from.clone.id);
                    fromParent.remove(from);
                } else { // 기존 삭제한 객체를 다시 추가
                    this.editor.makeForm(data.from.clone, fromParent, data.from.clone.displayDisplayOrder);
                    // 추가된 객체 선택
                    fromParent.getById(data.from.clone.id).UIElement.domElement.dispatchEvent(new Event('click'));
                }
                break;
            case 'sort': // 정렬 변경시
                const sortParent = this.editor.form.getById(data.from.id);
                const sortTarget = sortParent.getById(data.from.clone.id);
                let oldIndex = data.to.clone.displayDisplayOrder;
                let newIndex = data.from.clone.displayDisplayOrder;
                if (type === 'redo') {
                    oldIndex = data.from.clone.displayDisplayOrder;
                    newIndex = data.to.clone.displayDisplayOrder;
                }
                aliceJs.moveObject(sortParent.children, oldIndex, newIndex); // 객체 정렬
                sortParent.sort(0);
                // DOM 객체 정렬
                const nextSibling = (sortParent.children.length === (newIndex + 1)) ? null :
                    sortParent.children[newIndex + 1].UIElement.domElement;
                sortParent.UIElement.domElement.insertBefore(sortTarget.UIElement.domElement, nextSibling);
                break;
            case 'change':
                const changeTarget = this.editor.form.getById(data.id);
                if (type === 'redo') {
                    changeTarget[data.method].call(changeTarget, data.to);
                } else {
                    changeTarget[data.method].call(changeTarget, data.from);
                }
                break;
            default:
                break;
            }
        });
    }
}