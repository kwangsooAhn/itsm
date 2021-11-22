/**
 * 공통으로 사용하는 상수 모음.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
// 단위
export const UNIT = {
    PX: 'px',
    PERCENT: '%'
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
    STATUS: {
        EDIT: 'form.status.edit',
        PUBLISH: 'form.status.publish',
        USE: 'form.status.use',
        DESTROY: 'form.status.destroy'
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
        },
        SIZE: {
            MAX: 100,
            MIN: 10
        }
    },
    ELEMENT: {
        ALIGN: {
            VERTICAL: 'vertical',
            HORIZONTAL: 'horizontal'
        },
        POSITION: {
            LEFT: 'left',
            RIGHT: 'right'
        }
    },
    DATE_TYPE: {
        NONE: 'none',
        NOW: 'now',
        DAYS: 'date',
        HOURS: 'time',
        DATETIME: 'dateTime',
        DATE_PICKER: 'datePicker',
        TIME_PICKER: 'timePicker',
        DATETIME_PICKER: 'dateTimePicker',
        FORMAT: {
            SYSTEMFORMAT: 'systemFormat',
            USERFORMAT: 'userFormat'
        }
    },
    CUSTOM: {
        NONE: 'none',
        SESSION: 'session',
        CODE: 'code'
    },
    CUSTOM_CODE: [],
    // 옵션 속성에 추가되는 기본 값
    DEFAULT_OPTION_ROW: { name: '', value: '', checked: false },
    // 6개 column 제한 (dynamic table)
    MAX_COLUMN_IN_TABLE: 6
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
