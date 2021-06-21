/**
 * 공통으로 사용하는 상수 모음.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
// 사용자 세션 정보
export const SESSION = {};
// class 접두사
export const CLASS_PREFIX = 'z-';
// 단위
export const UNIT = {
    PX: 'px'
};
export const MAX_COLUMN_COUNT = 12;
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
        EDITABLE: 'document.displayType.editable',
        READONLY: 'document.displayType.readonly',
        HIDDEN: 'document.displayType.hidden'
    },
    LABEL: {
        POSITION: {
            HIDDEN: 'hidden',
            TOP: 'top',
            LEFT: 'left'
        }
    },
    DATE_TYPE: {
        NONE: 'none',
        NOW: 'now',
        DAYS: 'date',
        TIME: 'time',
        DATETIME: 'datetime',
        DATE_PICKER: 'datepicker',
        TIME_PICKER: 'timepicker',
        DATETIME_PICKER: 'datetimepicker'
    },
    CUSTOM_CODE: [],
    // 옵션 속성에 추가되는 기본 값
    DEFAULT_OPTION_ROW: { name: 'name', value: 'value' },
    DYNAMIC_ROW_TABLE: {
        MAX_COLUMN: 6, // 6개 column 제한
        COLUMN_TYPE_OPTION: [ // input, date, time, datetime, radio, checkbox, select 등
            { name: 'input', value: 'input' }
        ],
        DEFAULT_OPTION_COLUMN: {
            columnName: 'COLUMN',
            columnType: 'input', // input, date, time, datetime, radio, checkbox, select 등
            columnWidth: '12',
            columnHeadFontSize: '14',
            columnHeadFontColor: 'rgba(141, 146, 153, 1)',
            columnHeadBold: true,
            columnHeadItalic: false,
            columnHeadUnderline: false,
            columnAlign: 'left',
            columnBodyFontSize: '14',
            columnBodyFontColor: 'rgba(50, 50, 51, 1)',
            columnBodyBold: false,
            columnBodyItalic: false,
            columnBodyUnderline: false
        },
        COLUMN_INPUT: {
            columnPlaceholder: '',
            columnValidationType: 'none', // none | char | num | numchar | email | phone
            columnMinLength: '0',
            columnMaxLength: '100'
        }
    }
};
// 프로세스
export const PROCESS = {};
// 문서
export const DOCUMENT = {
    ASSIGNEE_TYPE: 'assignee.type.assignee'
};
// CI
export const CI = {
    ACTION_TYPE: {
        REGISTER: 'register',
        DELETE: 'delete',
        MODIFY: 'modify',
        READ: 'read'
    },
    STATUS: {
        USE: 'use',
        DELETE: 'delete'
    }
};