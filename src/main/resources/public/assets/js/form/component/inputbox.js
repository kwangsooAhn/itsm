/**
 * Inputbox Class
 *
 * 사용자 입력을 받는 input box.
 * 정규식을 이용하여 숫자, 문자, 전화번호, 이메일 등 제어가 가능하다.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import Component from './component';

const DEFAULT = {
    name: 'form.component.inputbox',
    type: 'inputbox',
    value: '${default}',
    column: '12',
    displayType: 'editable', // 컴포넌트 출력 타입 (readonly, editable, editable(required), hidden)
    mapId: '', // 매핑 ID
    isTopic: false,
    tags: [],
    label: {
        position: 'left',
        column: '2',
        fontSize: '16',
        fontColor: 'rgba(0,0,0,1)',
        bold: false,
        italic: false,
        underline: false,
        align: 'left',
        text: 'COMPONENT LABEL'
    },
    display: {
        placeholder: '',
        column: '10',
        default: 'none|',
    },
    validate: {
        regexp: 'none',
        regexpMsg: '',
        lengthMin: '0',
        lengthMax: '100'
    }
};



export default class InputBox extends Component {
    constructor(data = {}) {
        super(Object.assign(DEFAULT, data));

        if (data.hasOwnProperty('label')) {
            super.drawLabel();
        }
    }
    // input box 출력
    drawElement() {

    }
}