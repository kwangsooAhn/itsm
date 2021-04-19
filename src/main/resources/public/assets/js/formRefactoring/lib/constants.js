/**
 * 공통으로 사용하는 상수 모음.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

export const SESSION = {}; // 사용자 세션 정보
// class 접두사
export const CLASS_PREFIX = 'zenius-';
// 폼
export const FORM = {
    COLUMN: 12,
    MAX_COMPONENT_IN_ROW: 4, // row의 최대 컴포넌트 포함 개수
    LAYOUT: {
        FORM: 'form',
        GROUP: 'group',
        ROW: 'row',
        COMPONENT: 'component'
    },
    DISPLAY_TYPE: {
        EDITABLE: 'editable',
        READONLY: 'readonly',
        REQUIRED: 'required',
        HIDDEN: 'hidden'
    },
    LABEL: {
        POSITION: {
            HIDDEN: 'hidden',
            TOP: 'top',
            LEFT: 'left'
        }
    },
    CUSTOM_CODE: [],
    // 기본 값
    DEFAULT: {
        INPUTBOX: {
            ELEMENT: {
                placeholder: '',
                columnWidth: '10',
                defaultType: 'none|',
            },
            VALIDATE: {
                validateType: 'none', // none | char | num | numchar | email | phone
                lengthMin: '0',
                lengthMax: '100'
            }
        }
    }
};
// 프로세스
export const PROCESS = {};