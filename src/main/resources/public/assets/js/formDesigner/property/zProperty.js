/**
 * Property Class
 *
 * 컴포넌트의 속성항목들을 위한 기본적인 설정값, 동작을 정의하는 클랙스다.
 * 각각의 속성항목 타입별 클래스들은 이 클래스를 공통적으로 내부에 가지고 있으며
 * 추가적으로 필요한 사항들을 구현하고 있다.
 * 그러므로 이 클래스는 직접 사용되지 않고 속성항목 타입별 클래스에서만 내부적으로 사용한다.
 *
 * 속성항목별 클래스는 ./propertyType 폴더를 참고한다.
 *
 * @author Jung Hee Chan <hcjung@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 *
 * https://www.brainz.co.kr
 */
import { MAX_COLUMN_COUNT } from '../../lib/zConstants.js';
import { UIDiv, UILabel, UISpan } from '../../lib/zUI.js';
import { zValidation } from '../../lib/zValidation.js';

export default class ZProperty {
    constructor(key, name, type, value) {
        this._key = key;
        this._name = zValidation.isEmpty(name) ? '' : 'form.properties.' + name;
        this._type = type;
        this._value = JSON.parse(JSON.stringify(value));
        this._unit = '';
        this._help = '';
        this._columnWidth = MAX_COLUMN_COUNT;
        this._validation = {
            required: false,
            type: '',
            max: '',
            min: '',
            maxLength: '128',
            minLength: ''
        };
    }
    get key() {
        return this._key;
    }

    set key(key) {
        this._key = key;
    }

    get name() {
        return this._name;
    }

    set name(name) {
        this._name = zValidation.isEmpty(name) ? '' : 'form.properties.' + name;
    }

    get type() {
        return this._type;
    }

    set type(type) {
        this._type = type;
    }

    get value() {
        return this._value;
    }

    set value(value) {
        this._value = value;
    }

    get unit() {
        return this._unit;
    }

    set unit(value) {
        this._unit = value;
    }

    get help() {
        return this._help;
    }

    set help(msgKey) {
        this._help = msgKey;
    }

    get columnWidth() {
        return this._columnWidth;
    }

    set columnWidth(value) {
        if (value > MAX_COLUMN_COUNT) {
            this._columnWidth = MAX_COLUMN_COUNT;
        } else {
            this._columnWidth = value;
        }
    }

    get validation() {
        return this._validation;
    }

    set validation(data) {
        this._validation = data;
    }

    setValidation(required, type, min, max, minLength, maxLength) {
        this._validation.required = required;
        this._validation.type = type;
        this._validation.min = min;
        this._validation.max = max;
        this._validation.minLength = minLength;
        this._validation.maxLength = maxLength;
        return this;
    }
    /**
     * 세부 속성 공통 라벨 생성
     * @param data 세부 속성 데이터
     */
    makeLabelProperty(name) {
        const labelText = name || this.name;
        const label = new UILabel().setUIClass('property-label').setUITextAlign('left');
        if (!zValidation.isEmpty(labelText)) {
            // 라벨 문구
            label.addUI(new UISpan().setUIClass('property-label-text')
                .setUITextContent(i18n.msg(labelText)));
        }
        // 필수 여부
        if (this.validation.required) {
            label.addUI(new UISpan().setUIClass('required').addUIClass('ml-1'));
        }
        // 툴팁(도움말) 기능 추가
        if (this.help !== '') {
            label.UITooltip = new UIDiv().setUIClass('help-tooltip');
            label.UITooltip.addUI(new UISpan().setUIClass('z-icon').addUIClass('i-tooltip'));
            label.UITooltip.UIContent = new UIDiv().setUIClass('tooltip-contents');
            label.UITooltip.UIContent.addUI(new UISpan().setUIInnerHTML(i18n.msg(this.help)));
            label.UITooltip.addUI(label.UITooltip.UIContent);
            label.addUI(label.UITooltip);
        }
        return label;
    }
}