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
// 폼
export const FORM = {
    COLUMN: 12,
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
        GROUP_LABEL: {
            fontSize: '16px',
            fontColor: 'rgba(0,0,0,1)',
            bold: false,
            italic: false,
            underline: false,
            align: 'left',
            text: 'GROUP LABEL'
        },
        COMPONENT_LABEL: {
            position: 'top',
            fontSize: '16px',
            fontColor: 'rgba(0,0,0,1)',
            bold: false,
            italic: false,
            underline: false,
            align: 'left',
            text: 'COMPONENT LABEL'
        }
    }
};
// 프로세스
export const PROCESS = {};