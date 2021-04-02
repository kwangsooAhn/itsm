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
    save(data, list, flag) {
        if (data.length === 1 && workflowUtil.compareJson(data[0][0], data[0][1])) { return false; }

        flag = flag || false;
        if (!flag) { this.redoList = []; }

        (list || this.undoList).push(data);
        this.status = (this.redoList.length);
        this.editor.setFormName();
    }
    undo() {
        if (this.undoList.length) {
            let restoreData = this.undoList.pop();
            //restoreForm(restoreData, 'undo');
            this.save(restoreData, this.redoList, true);
        }
    }
    redo() {
        if (this.redoList.length) {
            let restoreData = this.redoList.pop();
            //restoreForm(restoreData, 'redo');
            this.save(restoreData, this.undoList, true);
        }
    }
}