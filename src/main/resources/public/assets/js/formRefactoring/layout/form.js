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
 import { controlMixin } from './lib/mixin.js';

export default class Form {
    constructor(data = {}) {
        this.type = 'form';
        this.id =  data.id || workflowUtil.generateUUID();
        this.name = data.name || '';
        this.desc = data.desc || '';
        this.status = data.status || 'form.status.edit';
        this.width = data.width || '905px';
        this.margin = data.margin || '60px 0px 60px 0px';
        this.padding = data.padding || '35px 35px 35px 35px';
        this.category = data.category || 'process'; // process | cmdb

        Object.assign(this, controlMixin);
        
         const form = document.createElement('div');
         form.id = this.id;
         form.className = 'form';
         form.setAttribute('data-type', this.type);
         form.setAttribute('data-category', this.category);

         const formMargins = this.margin.split(' ');
         const formPaddings = this.padding.split(' ');
         form.style.cssText = `width:${this.width};` +
             `margin:${formMargins.join(' ')};` +
             `padding:${formPaddings.join(' ')};`;

         this.domElem = form;
    }
    // 이름 변경
    setName(name) {
        this.name = name;
    }
    // 설명 변경
    setDesc(desc) {
        this.desc = desc;
    }
    // 상태 변경
    setStatus(status) {
        this.status = status;
    }
    // 카테고리 변경
    setCategory(category) {
        this.category = category;
    }
}

// 디자인 속성 메서드 생상
const properties = ['width', 'margin', 'padding'];
properties.forEach( property => {
    const method = 'set' + property.substr( 0, 1 ).toUpperCase() +
        property.substr( 1, property.length );
    Form.prototype[method] = function () {
        this.setStyle(property, arguments);
        return this;
    };
});