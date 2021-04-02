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
import * as util from '../lib/util.js';
import * as mixin from '../lib/mixins.js';
import { UIForm } from '../lib/ui.js';

export default class Form {
    constructor(data = {}) {
        this.type = 'form';
        this.id =  data.id || workflowUtil.generateUUID();
        this.parent = null;        // 부모 객체
        this.children = [];        // 자식 객체
        this.displayOrder = 0;     // 표시 순서
        this.name = data.name || '';
        this.desc = data.desc || '';
        this.status = data.status || 'form.status.edit'; // 문서 상태 : 편집, 발생, 사용, 폐기
        this.width = data.width || '905px';
        this.margin = data.margin || '60px 0px 60px 0px';
        this.padding = data.padding || '35px 35px 35px 35px';
        this.category = data.category || 'process'; // process | cmdb

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        this.UIElem = new UIForm()
            .setId(this.id)
            .setWidth(this.width)
            .setMargin(this.margin)
            .setPadding(this.padding);
    }
}
