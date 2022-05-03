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
    SEARCH_COMPONENT: {
        USER_SEARCH: 'userSearch',
        ORGANIZATION_SEARCH: 'organizationSearch',
    },
    DEFAULT_VALUE_TYPE: {
        NONE: 'none',
        SESSION: 'session',
        CUSTOM: 'custom'
    },
    DROPDOWN_CODE: {
        CODE: 'code',
        DEFAULT_CODE: 'defaultCode',
        MAPPING_ID: 'mappingId'
    },
    CUSTOM_CODE: [],
    PLUGIN_LIST: [],
    // 옵션 속성에 추가되는 기본 값
    DEFAULT_OPTION_ROW: { name: '', value: '', checked: false },
    // 필드 옵션 속성에 추가되는 기본 값
    FIELD_OPTION_ROW: { name: '', alias: '', checked: false },
    // 필드 옵션 조회 조건 (Modal Button Component)
    FIELD_ORDER_BY: {
        ASC: 'asc',
        DESC: 'desc'
    },
    // 15개 column 제한 (dynamic table)
    MAX_COLUMN_IN_TABLE: 15,
    // columnProperty 타입
    COLUMN_PROPERTY: {
        COLUMN: 'columnProperty',
        FIELD: 'fieldProperty'
    }
};
// 프로세스
export const PROCESS = {};
// 문서
export const DOCUMENT = {
    ASSIGNEE_TYPE: 'assignee.type.assignee',
    VIEWER_TYPE: {
        REGISTER: 'register',
        MODIFY: 'modify',
        DELETE: 'delete'
    }
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
// Response(응답) 코드
export const RESPONSE_CODE = {
    STATUS_SUCCESS: '0',
    STATUS_ERROR_DUPLICATE: '1'
};
// 차트
export const CHART = {
    TYPE: {
        BASIC_LINE: 'chart.basicLine',
        BASIC_COLUMN: 'chart.basicColumn',
        STACKED_COLUMN: 'chart.stackedColumn',
        STACKED_BAR: 'chart.stackedBar',
        PIE: 'chart.pie',
        GAUGE: 'chart.gauge'
    },
    OPERATION: {
        AVG: 'average',
        COUNT: 'count',
        PERCENT: 'percent'
    },
    PERIOD: {
        YEAR: 'Y',
        MONTH: 'M',
        DAY: 'D',
        HOUR: 'H'
    },
    RANGE_TYPE_NONE: 'chart.range.none',
    DATETIME_FORMAT: 'yyyy-MM-dd HH:mm:ss',
    DATE_FORMAT: {
        TYPE1: 'yyyy-MM-dd',
        TYPE2: 'dd-MM-yyyy',
        TYPE3: 'MM-dd-yyyy',
        TYPE4: 'yyyy-dd-MM'
    },
    TIME_FORMAT: {
        HOUR24: 'HH:mm',
        HOUR12: 'hh:mm a'
    }
};

// 페이징
export const PAGING = {
    ORDER_DIRECTION: {
        ASC: 'asc',
        DESC: 'desc'
    }
};
