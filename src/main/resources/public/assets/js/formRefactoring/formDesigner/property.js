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
import {MAX_COLUMN_COUNT} from "../lib/constants.js";

export default class Property {
    constructor(name, type) {
        this._name = 'form.properties.' + name;
        this._type = type;
        this._unit = '';
        this._help = '';
        this._columnWidth = MAX_COLUMN_COUNT;
        this._validate = {
            required: false,
            type: '',
            max: '',
            min: '',
            maxLength: '',
            minLength: '128'
        };

    }
    getPropertyConfig() {
        return {
            name: this._name,
            type: this._type,
            unit: this._unit,
            help: this._help,
            columnWidth: this._columnWidth,
            validate: this._validate
        }
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

    get validate() {
        return this._validate;
    }

    set validate(data) {
        this._validate = data;
    }
}