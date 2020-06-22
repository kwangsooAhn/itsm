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

    function getDateTime(offset) {
        if (offset === undefined) {
            offset = { 'days' : 0 };
        }
        return luxon.DateTime.local().setZone(timezone).plus(offset).toFormat(dateTimeFormat);
    }

    /**
     * 서버로 전송하기 위해서 UTC+0, ISO8601으로 변환
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeUserDateTime 사용자가 입력한 날짜시간.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemDateTime(beforeUserDateTime,offset) {
        if (offset === undefined) {
            offset = { 'days' : 0 };
        }
        return luxon.DateTime.fromFormat(beforeUserDateTime, dateTimeFormat, {zone: timezone}).setZone('utc+0').plus(offset).toISO();
    }

    /**
     * 서버로 전송하기 위해서 UTC+0, ISO8601으로 변환
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeUserDate 변환 대상 날짜.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemDate(beforeUserDate,offset) {
        if (offset === undefined) {
            offset = { 'days' : 0 };
        }
        return luxon.DateTime.fromFormat(beforeUserDate, dateFormat, {zone: timezone}).setZone('utc+0').plus(offset).toISO();
    }

    /**
     * 서버로 전송하기 위해서 ISO8601으로 변환. (HH:mm:ss)
     *  - 시간은 타임존 개념은 없음.
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeUserTime 변환 대상 날짜.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemTime(beforeUserTime) {
        return luxon.DateTime.fromFormat(beforeUserTime, timeFormat).toFormat('HH:mm');
    }

    /**
     * 서버에서 받은 ISO 8601 포맷의 데이터를 사용자 포맷과 타임존으로 변경
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeSystemDateTime 변환 대상 날짜시간 데이터.
     * @return {String} 변환된 데이터.
     */
    function convertToUserDateTime(beforeSystemDateTime) {
        if (beforeSystemDateTime === null || beforeSystemDateTime === '') {
            return ''
        } else {
            return luxon.DateTime.fromISO(beforeSystemDateTime, {zone: 'utc'}).setZone(timezone).toFormat(dateTimeFormat);
        }
    }

    /**
     * 서버에서 받은 ISO 8601 포맷의 데이터를 사용자 포맷과 타임존으로 변경
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeSystemDate 변환 대상 날짜 데이터.
     * @return {String} 변환된 데이터.
     */
    function convertToUserDate(beforeSystemDate) {
        if (beforeSystemDate === null || beforeSystemDate === '') {
            return ''
        } else {
            return luxon.DateTime.fromISO(beforeSystemDate, {zone: 'utc'}).setZone(timezone).toFormat(dateFormat);
        }
    }

    /**
     * 서버에서 받은 ISO 8601 포맷의 데이터를 사용자 포맷으로 변경
     *  - 시간은 타임존 개념은 없음.
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeSystemTime 변환 대상 시간 데이터.
     * @return {String} 변환된 데이터.
     */
    function convertToUserTime(beforeSystemTime) {
        if (beforeSystemTime === null || beforeSystemTime === '') {
            return ''
        } else {
            return luxon.DateTime.fromISO(beforeSystemTime).toFormat(timeFormat);
        }
    }

    /**
     * 서버에서 받은 ISO 8601 포맷의 데이터를 프린트 포맷으로 변경
     *
     * @author Lee So Hyun
     * @since 2020-06-17
     * @param {String} beforeSystemDateTime 변환 대상 날짜시간 데이터.
     * @return {String} 변환된 데이터.
     */
    function convertToPrintFormat(beforeSystemDateTime) {
        return luxon.DateTime.fromISO(beforeSystemDateTime).setZone(timezone)
            .toFormat(dateTimeFormat.replace(/(mm)/g, '$1:ss') + ' (z)');
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
    exports.getDateTime = getDateTime;
    exports.systemDateTime = convertToSystemDateTime;
    exports.systemDate = convertToSystemDate;
    exports.systemTime = convertToSystemTime;
    exports.userDateTime = convertToUserDateTime;
    exports.userDate = convertToUserDate;
    exports.userTime = convertToUserTime;
    exports.printFormat = convertToPrintFormat;
    exports.get = getMessage; // 앞으로 msg로 사용하고 get은 다 msg로 수정하면 지우자.
    exports.msg = getMessage;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
