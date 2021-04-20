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
import { UIDiv } from '../lib/ui.js';
import { CLASS_PREFIX } from '../lib/constants.js';

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
        this.padding = data.padding || '15px 15px 15px 15px';
        this.category = data.category || 'process'; // process | cmdb
        this.selectedElement = null;

        // Control Mixin import
        util.importMixin(this, mixin.controlMixin);

        this.init();
    }
    // 초기화
    init() {
        this.UIElement = new UIForm()
            .setId(this.id)
            .setWidth(this.width)
            .setMargin(this.margin)
            .setPadding(this.padding);
    }

    // 복사 (자식 포함)
    copy(source) {
        this.type = source.type;
        this.id =  source.id;
        this.displayOrder = source.displayOrder;
        this.name = source.name;
        this.desc = source.desc;
        this.status = source.status;
        this.width = source.width;
        this.margin = source.margin;
        this.padding = source.padding;
        this.category = source.category;
        this.parent = source.parent;
        this.UIElement = source.UIElement;

        for (let i = 0; i < source.children.length; i ++) {
            const child = source.children[i];
            this.add(child.clone(), i);
        }
        return this;
    }
    toJSon() {
        return {
            id: this.id,
            name: this.name,
            desc: this.desc,
            status: this.status,
            width: this.width,
            margin: this.margin,
            padding: this.padding,
            category: this.category
        };
    }
}

export class UIForm extends UIDiv {
    constructor() {
        super();
        this.domElement.className = CLASS_PREFIX + 'form';
    }
}