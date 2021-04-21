/**
 * 이력을 관리하는 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import * as util from '../lib/util.js';

export default class History {
    constructor(editor) {
        this.editor = editor;
        this.undoList = [];
        this.redoList = [];
        this.status = false; // 수정 여부
    }

    save(data, list, flag) {
        if (data.length === 0) { return false; }
        if (data.length === 1 && workflowUtil.compareJson(data[0].from, data[0].to)) {
            return false;
        }

        flag = flag || false;
        if (!flag) { this.redoList = []; }

        (list || this.undoList).push(data);
        this.status = (this.undoList.length > 0); // undoList가 없으면 수정된 항목이 없음을 의미한다.
        this.editor.setFormName();
    }

    undo() {
        if (this.undoList.length) {
            let restoreData = this.undoList.pop();
            this.restore('undo', restoreData);
            this.save(restoreData, this.redoList, true);
        }
    }

    redo() {
        if (this.redoList.length) {
            let restoreData = this.redoList.pop();
            this.restore('redo', restoreData);
            this.save(restoreData, this.undoList, true);
        }
    }

    restore(type, histories) {
        const historiesData = histories.slice();
        if (historiesData.length > 1 && type === 'redo') {
            historiesData.reverse();
        }
        historiesData.forEach((data) => {
            switch(data.type) {
            case 'add':
                if (!util.isEmptyObject(data.to)) {
                    const parent = this.editor.form.getById(data.to.parent.id);
                    if (type === 'redo') { // 복제한 객체를 다시 추가
                        parent.add(data.to.clone({ 'type': data.to.type }), data.to.displayOrder);
                    } else { // 기존 추가한 객체를 삭제
                        const to = this.editor.form.getById(data.to.id);
                        parent.remove(to);
                    }
                }
                break;
            case 'remove':
                if (!util.isEmptyObject(data.from)) {
                    const parent = this.editor.form.getById(data.from.parent.id);
                    if (type === 'redo') {
                        const from = this.editor.form.getById(data.from.id);
                        parent.remove(from);
                    } else { // 기존 삭제한 객체를 다시 추가
                        parent.add(data.from.clone({ 'type': data.from.type }), data.from.displayOrder);
                    }
                }
                break;
            case 'sort': // 정렬 변경시
                const parent = this.editor.form.getById(data.from.parent.id);
                let oldIndex = data.to.displayOrder;
                let newIndex = data.from.displayOrder;
                if (type === 'redo') {
                    oldIndex = data.from.displayOrder;
                    newIndex = data.to.displayOrder;
                }
                util.moveObject(parent.children, oldIndex, newIndex); // 객체 정렬
                parent.sort(0);
                // DOM 객체 정렬
                const nextSibling = (parent.children.length === (newIndex + 1)) ? null :
                    parent.children[newIndex + 1].UIElement.domElement;
                parent.UIElement.domElement.insertBefore(data.from.UIElement.domElement, nextSibling);
                break;
            case 'change':
                const target = this.editor.form.getById(data.id);
                if (type === 'redo') {
                    target[data.method].call(target, data.to);
                } else {
                    target[data.method].call(target, data.from);
                }
                break;
            default:
                break;
            }
        });
    }
}