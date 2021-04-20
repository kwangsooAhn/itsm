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
        if (data.length === 1 && workflowUtil.compareJson(data[0].from, data[0].to)) {
            return false;
        }
        console.log(data);

        flag = flag || false;
        if (!flag) { this.redoList = []; }

        (list || this.undoList).push(data);
        this.status = (this.redoList.length > 0);
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
        if (histories.length > 1 && type === 'redo') {
            histories.reverse();
        }
        histories.forEach((data) => {
            switch(data.type) {
            case 'add':
                if (!util.isEmptyObject(data.to)) {
                    const parent = this.editor.form.getById(data.to.parent.id);
                    if (type === 'redo') {
                        parent.add(data.to, data.to.displayOrder);
                    } else { // 기존 추가한 객체를 삭제
                        parent.remove(data.to);
                    }
                }
                break;
            case 'remove':
                if (!util.isEmptyObject(data.from)) {
                    const parent = this.editor.form.getById(data.from.parent.id);
                    if (type === 'redo') {
                        parent.remove(data.from);
                    } else { // 기존 삭제한 객체를 다시 추가
                        parent.add(data.from, data.from.displayOrder);
                    }
                }
                break;
            case 'sort':
                util.swapObject(data.to.parent.children, data.to.displayOrder, data.from.displayOrder);
                break;
            }
        });
    }
}