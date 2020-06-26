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

    const defaultType = 'editbox';
    let sessionInfo = {},
        componentProperties = {};

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
                    if (displayDefaultValueArray.length > 1 && displayDefaultValueArray[1] !== '') {
                        if (action === 'save') {
                            switch (component.type) {
                                case 'datetime':
                                    displayDefaultValueArray[1] = i18n.systemDateTime(displayDefaultValueArray[1]);
                                    break;
                                case 'date':
                                    displayDefaultValueArray[1] = i18n.systemDate(displayDefaultValueArray[1]);
                                    break;
                                case 'time':
                                    displayDefaultValueArray[1] = i18n.systemTime(displayDefaultValueArray[1]);
                                    break;
                            }
                        } else if (action === 'read') {
                            switch (component.type) {
                                case 'datetime':
                                    displayDefaultValueArray[1] = i18n.userDateTime(displayDefaultValueArray[1]);
                                    break;
                                case 'date':
                                    displayDefaultValueArray[1] = i18n.userDate(displayDefaultValueArray[1]);
                                    break;
                                case 'time':
                                    displayDefaultValueArray[1] = i18n.userTime(displayDefaultValueArray[1]);
                                    break;
                            }
                        }
                    }
                    components[idx].display.default = displayDefaultValueArray.join('|');
                }
                
                // 2. validate 용 date-min, date-max 변환
                const dateTimeRegExp = /(date|time)\w*/i;
                let validateItems = component.validate;
                Object.keys(validateItems).forEach(function(item) {
                    if (dateTimeRegExp.test(item) && validateItems[item] !== '') {
                        let validateItemValue = validateItems[item];
                        if (action === 'save') {
                            switch (component.type) {
                                case 'datetime':
                                    validateItemValue = i18n.systemDateTime(validateItemValue);
                                    break;
                                case 'date':
                                    validateItemValue = i18n.systemDate(validateItemValue);
                                    break;
                                case 'time':
                                    validateItemValue = i18n.systemTime(validateItemValue);
                                    break;
                            }
                        } else if (action === 'read') {
                            switch (component.type) {
                                case 'datetime':
                                    validateItemValue = i18n.userDateTime(validateItemValue);
                                    break;
                                case 'date':
                                    validateItemValue = i18n.userDate(validateItemValue);
                                    break;
                                case 'time':
                                    validateItemValue = i18n.userTime(validateItemValue);
                                    break;
                            }
                        }
                        components[idx].validate[item] = validateItemValue;
                    }
                });

                // 3. 처리할 문서인 경우 저장된 값도 변경.
                if (component.value !== undefined && component.value !== '') {
                    let componentValue = component.value;
                    switch (component.type) {
                        case 'datetime':
                            componentValue = i18n.userDateTime(componentValue);
                            break;
                        case 'date':
                            componentValue = i18n.userDate(componentValue);
                            break;
                        case 'time':
                            componentValue = i18n.userTime(componentValue);
                            break;
                    }
                    component.value = componentValue;
                }
            }
        });
        return components;
    }

    /**
     * 컴포넌트 기본 속성인 '/assets/js/form/componentProperties.json' 데이터를 조회 후
     * callback(init) 함수를 실행한다.
     *
     * 2020-06-03 Jung Hee Chan
     *   - 최초 기본 속성을 가져와서 사용하는 경우에도 사용자의 포맷으로 변경하기 위해서 변환 추가
     *
     * @param {Function} callback
     */
    function init(callback) {
        let params = Array.prototype.slice.call(arguments, 1);

        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/form/componentProperties.json',
            callbackFunc: function(xhr) {
                Object.assign(componentProperties, JSON.parse(xhr.responseText));
                callback.apply(null, params);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * initSession.
     *
     * @param {String} userInfo 사용자 세션 정보
     */

    function initSession(userInfo) {
        Object.assign(sessionInfo, JSON.parse(userInfo));
    }

    exports.init = init;
    exports.initSession = initSession;
    exports.reformatCalendarFormat = reformatCalendarFormat;

    exports.defaultType = defaultType;
    exports.session = sessionInfo;
    exports.componentProperties = componentProperties;

    Object.defineProperty(exports, '__esModule', { value: true });
})));
