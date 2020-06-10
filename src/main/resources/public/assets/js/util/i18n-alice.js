(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.i18n = global.i18n || {})));
}(this, (function (exports) {
    'use strict';

    let messages = {},
        dateTimeFormat = 'yyyy-MM-dd HH:mm:ss',
        dateFormat = 'yyyy-MM-dd',
        timeFormat = 'HH:mm:ss',
        lang = 'ko',
        timezone = 'Asia/Seoul';

    /**
     * 국제화 관련 초기화
     *  - 사용자 설정에 따른 타임존, 날짜시간 포맷, 언어.
     *  - 다국어 처리를 위한 메시지 로딩.
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String} userInfo 사용자 국제화 정보
     */
    function init(userInfo) {
        addMessages();

        let sessionInfo = JSON.parse(userInfo);
        dateTimeFormat = sessionInfo.dateTimeFormat;
        dateFormat = sessionInfo.dateFormat;
        timeFormat = sessionInfo.timeFormat;
        lang = sessionInfo.lang;
        timezone = sessionInfo.timezone;
    }

    /**
     * 사용자 타임존의 현재 시간을 기준으로 월,일,시간등의 계산된 값을 사용자 포맷으로 반환.
     * 계산식은 아래와 같은 객체 포맷으로 전달. (luxon.js 기준. https://moment.github.io/luxon/docs/manual/math.html)
     *  {'months' : 1, 'days' : 1, 'hours' : 1}
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {Object} offset 날짜시간 계산을 위한 조정 값.
     * @return {String} 사용자 타임존과 포맷이 반영된 날짜 데이터.
     */
    function getDate(offset) {
        if (offset === undefined) {
            offset = { 'days' : 0 };
        }
        return luxon.DateTime.local().setZone(timezone).plus(offset).toFormat(dateFormat);
    }

    /**
     * 서버로 전송하기 위해서 UTC+0, ISO8601으로 변환
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  targetDateTime 사용자가 입력한 날짜시간.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemDateTime(targetDateTime) {
        return luxon.DateTime.fromFormat(targetDateTime, dateTimeFormat).setZone('utc+0').toISO();
    }

    /**
     * 서버로 전송하기 위해서 UTC+0, ISO8601으로 변환
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  targetDate 변환 대상 날짜.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemDate(targetDate) {
        return luxon.DateTime.fromFormat(targetDate, dateFormat).setZone('utc+0').toISO();
    }

    /**
     * 서버에서 받은 ISO 8601 포맷의 데이터를 사용자 포맷과 타임존으로 변경
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  targetDateTime 변환 대상 날짜시간 데이터.
     * @return {String} 변환된 데이터.
     */
    function convertToUserDateTime(targetDateTime) {
        return luxon.DateTime.fromISO(targetDateTime).setZone(timezone).toFormat(dateTimeFormat);
    }

    /**
     * 메시지 세트를 로딩한다.
     *
     * @param callbackFunc 메시지 로드 후 실행할 함수
     */
    function addMessages(callbackFunc) {
        aliceJs.sendXhr({
            method: 'GET',
            url: '/i18n/messages',
            callbackFunc: function(xhr) {
                messages = JSON.parse(xhr.responseText);
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            },
            showProgressbar: false,
            contentType: 'application/json; charset=utf-8'
        });
    }

    /**
     * 메시지 ID로 해당 다국어 메시지를 찾아 반환한다.
     *
     * @param id 메시지 ID
     * @return {string} 다국어 메시지
     */
    function getMessage(id) {
        let message = '';
        if (typeof messages[id] !== 'undefined') {
            message = messages[id];
        }
        if (message.indexOf('{0}') > -1 && arguments.length > 1) {
            let index = 0;
            while (index < arguments.length - 1) {
                message = message.replace('{' + index + '}', arguments[(index + 1)]);
                index++;
            }
        }
        return message;
    }

    exports.init = init;
    exports.initMessages = addMessages;
    exports.getDate = getDate;
    exports.systemDateTime = convertToSystemDateTime;
    exports.systemDate = convertToSystemDate;
    exports.userDateTime = convertToUserDateTime;
    exports.get = getMessage; // 앞으로 msg로 사용하고 get은 다 msg로 수정하면 지우자.
    exports.msg = getMessage;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
