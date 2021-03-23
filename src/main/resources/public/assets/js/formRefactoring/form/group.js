/**
 * 컴포넌트 내부를 이루는 구성 Class.
 *
 *  Group은 1개 이상의 Row로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import Row from './row.js';

const DEFAULT = {
    margin: '10 0 10 0', // 그룹 간 간격(위 오른쪽 아래 왼쪽)
    padding: '10 10 10 10', // 그룹 내부 여백(위 오른쪽 아래 왼쪽)
    label: {
        position: 'top',
        fontSize: '16',
        fontColor: 'rgba(0,0,0,1)',
        bold: false,
        italic: false,
        underline: false,
        align: 'left',
        text: 'GROUP LABEL'
    },
    accordion: {
        isUsed: false,
        thickness: 1,
        color: 'rgba(235, 235, 235, 1)'
    }
};

let displayOrder = 0; // 그룹 출력 순서

export default class Group {
    constructor(data = {}) {
        const mergeData = Object.assign(DEFAULT, data, {
            id: data.id || workflowUtil.generateUUID(),
            displayOrder: data.displayOrder || ++displayOrder
        });

        Object.entries(mergeData).forEach(([key, value]) => {
            if (key !== 'rows') {
                this[key] = value;
            }
        });

        // row 추가
        this.rows = [];
        if (mergeData.hasOwnProperty('rows')) {
            mergeData.rows.forEach( r => {
                this.addRow(r);
            });
        }
    }
    // Row 추가
    addRow(data) {
        this.rows.push(new Row(data));
    }
    // Row 삭제
    removeRow() {}
    // Row 수정
    modifyRow() {}
    // Row 복사
    copyRow() {}
}