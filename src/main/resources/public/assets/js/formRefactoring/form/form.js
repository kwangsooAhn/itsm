/**
 * Form Class.
 *
 * 폼은 1개, 혹은 그 이상의 Group으로 구성된다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import Group from './group.js';

export default class Form {
    constructor(data = {}, render) {
        this.id =  data.id || workflowUtil.generateUUID();
        this.name = data.name || 'form.label.form';
        this.desc = data.desc || '';
        this.status = data.status || 'form.status.edit'; // 편집 | 발생 | 사용 | 폐기
        this.width = data.width || '1600';
        this.height = data.height || '1000';
        this.padding = data.padding || '20 20 20 20'; // 문서 내부 여백(위 오른쪽 아래 왼쪽)
        this.type = data.type || 'process'; // process | cmdb
        this.render = render;
        
        // 그룹 추가
        this.groups = [];
        if (data.hasOwnProperty('groups')) {
            data.groups.forEach( g => {
                this.addGroup(g);
            });
        }
    }
    // 그룹 추가
    addGroup(data) {
        this.groups.push(new Group(data));
    }
    // 그룹 삭제
    removeGroup() {}
    // 그룹 수정
    modifyGroup() {}
    // 그룹 복사
    copyGroup() {}
}