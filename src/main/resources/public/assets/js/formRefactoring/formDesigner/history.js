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
    console.log('undo');
    console.log(this);
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

    restore(type, data) {
        const histories = JSON.parse(JSON.stringify(data));
        if (histories.length > 1 && type === 'redo') {
            histories.reverse();
        }
        histories.forEach((data) => {
            console.log(data.to);
            console.log(data.from);
            switch(data.type) {
            case 'add':
                if (type === 'redo') { // add

                } else { // remove
                    data.to.parent.remove(data.to);
                }
                break;
            case 'remove':
                if (type === 'redo') { // remove

                } else { // add
                    if (!util.isEmptyObject(data.from)) {
                        data.from.parent.add(data.from, data.displayOrder);
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