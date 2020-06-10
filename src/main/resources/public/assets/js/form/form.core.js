/**
 * @projectDescription Form Designer Core Library
 *
 * @author woodajung
 * @version 1.0
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceForm = global.aliceForm || {})));
}(this, (function (exports) {
    'use strict';

    let options = {
        lang: 'en',
        datetimeFormat: 'YYYY-MM-DD HH:mm',
        dateFormat: 'YYYY-MM-DD',
        hourFormat: 'HH:mm',
        timezone: 'Asia/Seoul',
        sessionInfo: {},
        columnWidth: 8.33  //폼 양식을 12등분 하였을 때, 1개의 너비
    };

    /**
     * 날짜와 관련있는 컴포넌트들에 대해서 사용자의 타임존과 출력 포맷에 따라 변환.
     * 폼 디자이너 모듈 Refactoring 시까지 임시로 사용할 가능성이 있음.
     *
     * @author Jung Hee Chan
     * @since 2020-05-22
     * @param {String} action save, read 중에서 1개. save인 경우는 시스템 공통 포맷으로, read인 경우 사용자 포맷으로 변환.
     * @param {Object} components 변환 대상이 되는 컴포넌트 목록.
     * @return {Object} resultComponents 변경된 결과
     */
    function reformatCalendarFormat(action, components) {
        if (action !== 'save' && action !== 'read') {
            return components;
        }

        components.forEach(function(component, idx) {
            if (component.type === 'datetime' || component.type === 'date' || component.type === 'time') {
                // 1. 기본값 타입 중에서 직접 Calendar로 입력한 값인 경우는 변환
                if (component.display.default.indexOf('picker') !== -1) {
                    let displayDefaultValueArray = component.display.default.split('|'); // 속성 값을 파싱한 배열
                    if (action === 'save') {
                        switch(component.type) {
                            case 'datetime':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToSystemDatetimeFormatWithTimezone(displayDefaultValueArray[1],
                                        aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                break;
                            case 'date':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToSystemDateFormat(displayDefaultValueArray[1],
                                        aliceForm.options.dateFormat);
                                break;
                            case 'time':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToSystemTimeFormat(displayDefaultValueArray[1],
                                        aliceForm.options.hourFormat);
                                break;
                        }
                    } else if (action === 'read') {
                        switch(component.type) {
                            case 'datetime':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToUserDatetimeFormatWithTimezone(displayDefaultValueArray[1],
                                        aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                break;
                            case 'date':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToUserDateFormat(displayDefaultValueArray[1],
                                        aliceForm.options.dateFormat);
                                break;
                            case 'time':
                                displayDefaultValueArray[1] =
                                    aliceJs.convertToUserTimeFormat(displayDefaultValueArray[1],
                                        aliceForm.options.hourFormat);
                                break;
                        }

                    }
                    components[idx].display.default = displayDefaultValueArray.join('|');
                }

                // 2. validate 용 date-min, date-max 변환
                let validateItems = component.validate;
                Object.keys(validateItems).forEach(function(validateItem) {
                    if (validateItem.indexOf('date') !== -1) {
                        let validateItemValueArray = validateItems[validateItem].split('|'); // 속성 값을 파싱한 배열
                        if (action === 'save') {
                            switch(component.type) {
                                case 'datetime':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToSystemDatetimeFormatWithTimezone(validateItemValueArray[0],
                                            aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                    break;
                                case 'date':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToSystemDateFormat(validateItemValueArray[0],
                                            aliceForm.options.dateFormat);
                                    break;
                                case 'time':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToSystemDateFormat(validateItemValueArray[0],
                                            aliceForm.options.hourFormat);
                                    break;
                            }
                        } else if (action === 'read') {
                            switch(component.type) {
                                case 'datetime':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToUserDatetimeFormatWithTimezone(validateItemValueArray[0],
                                            aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                                    break;
                                case 'date':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToUserDateFormat(validateItemValueArray[0],
                                            aliceForm.options.dateFormat);
                                    break;
                                case 'time':
                                    validateItemValueArray[0] =
                                        aliceJs.convertToUserTimeFormat(validateItemValueArray[0],
                                            aliceForm.options.hourFormat);
                                    break;
                            }
                        }
                        components[idx].validate[validateItem] = validateItemValueArray.join('|')
                    }
                });

                // 3. 처리할 문서인 경우 저장된 값도 변경.
                if (component.value !== undefined && component.value !== '') {
                    let componentValue = component.value;
                    switch (component.type) {
                        case 'datetime':
                            componentValue =
                                aliceJs.convertToUserDatetimeFormatWithTimezone(componentValue,
                                    aliceForm.options.datetimeFormat, aliceForm.options.timezone);
                            break;
                        case 'date':
                            componentValue =
                                aliceJs.convertToUserDateFormat(componentValue, aliceForm.options.dateFormat);
                            break;
                        case 'time':
                            componentValue =
                                aliceJs.convertToUserTimeFormat(componentValue, aliceForm.options.hourFormat);
                            break;
                    }
                    component.value = componentValue;
                }
            }
        });
        return components;
    }

    /**
     * Init.
     *
     * @param {String} userInfo 사용자 세션 정보
     */
    function init(userInfo) {
        Object.assign(options.sessionInfo, JSON.parse(userInfo));
        //사용자 정보에 따라 날짜 및 시간 포맷 추출.
        options.timezone = options.sessionInfo.timezone;
        options.lang = options.sessionInfo.lang;
        options.datetimeFormat = options.sessionInfo.timeFormat; // 사용자 날짜 포맷 스트링 원본

        // 임시. (아래 내용 구현 후 .toUpperCase() 삭제예정)
        // 현재 사용자 날짜 포맷은 yyyy-MM-dd와 같은 포맷임.
        // dd처럼 소문자인 경우 요일 정보가 나오게 할 수 있으므로 일단 현재는 모두 대문자 처리
        // 추후 기능 확장을 위해서 사용자 정보에서 현재 포맷은 DD로 넣게 하고
        // 요일 출력 옵션도 추가하려면 그때 소문자 dd를 추가하기.
        let splitDatetimeFormat = options.datetimeFormat.split(' ');
        options.dateFormat = splitDatetimeFormat[0].toUpperCase();
        options.hourFormat = splitDatetimeFormat[1];
        options.datetimeFormat = options.dateFormat + ' ' + options.hourFormat

        if (splitDatetimeFormat.length > 2) {
            options.hourFormat = options.hourFormat + ' ' + splitDatetimeFormat[2];
            options.datetimeFormat = options.datetimeFormat + ' ' + splitDatetimeFormat[2];
        }
    }

    /**
     * 컴포넌트 기본 속성인 '/assets/js/form/componentAttribute.json' 데이터를 조회 후
     * callback(init) 함수를 실행한다.
     *
     * 2020-06-03 Jung Hee Chan
     *   - 최초 기본 속성을 가져와서 사용하는 경우에도 사용자의 포맷으로 변경하기 위해서 변환 추가
     *
     * @param {Function} callback
     */
    function initSync(callback) {
        let params = Array.prototype.slice.call(arguments, 1);

        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentAttribute.json',
            callbackFunc: function(xhr) {
                options.componentAttribute = JSON.parse(xhr.responseText);
                Object.keys(options.componentAttribute).forEach(function(componentType) {
                    if (componentType === 'datetime' || componentType === 'date' || componentType === 'time') {
                        Object.values(options.componentAttribute[componentType].validate).forEach(function(validateItem) {
                            if (!(validateItem.id.indexOf('date') < 0 && validateItem.id.indexOf('time') < 0)) {
                                switch (componentType) {
                                    case 'datetime':
                                        validateItem.value = aliceJs.convertToUserDatetimeFormatWithTimezone(validateItem.value,
                                            options.datetimeFormat, options.timezone);
                                        break;
                                    case 'date':
                                        validateItem.value =
                                            aliceJs.convertToUserDateFormat(validateItem.value, options.dateFormat);
                                        break;
                                    case 'time':
                                        validateItem.value =
                                            aliceJs.convertToUserTimeFormat(validateItem.value, options.hourFormat);
                                        break;
                                }
                            }
                        });
                    }
                });
                callback.apply(null, params);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    exports.init = init;
    exports.initSync = initSync;
    exports.reformatCalendarFormat = reformatCalendarFormat;
    exports.options = options;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
