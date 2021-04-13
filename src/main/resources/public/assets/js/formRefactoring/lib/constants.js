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
    MAX_ROW: 4, // 한 줄에 최대 컴포넌트 개수
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
            // 그룹의 라벨은 아코디언 위에 표시되기 때문에 항상 top 위치이며 보여주거나 숨기는 기능을 설정 한다.
            isLabelUsed: true, // 라벨 사용여부
            fontSize: '16px',
            fontColor: 'rgba(0,0,0,1)',
            bold: false,
            italic: false,
            underline: false,
            align: 'left',
            text: 'GROUP LABEL'
        },
        COMPONENT_LABEL: {
            position: 'left', // 라벨 위치 hidden | top | left
            fontSize: '16px',
            fontColor: 'rgba(0,0,0,1)',
            bold: false,
            italic: false,
            underline: false,
            align: 'left',
            text: 'COMPONENT LABEL'
        },
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