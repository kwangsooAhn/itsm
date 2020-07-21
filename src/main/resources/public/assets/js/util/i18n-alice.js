(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.i18n = global.i18n || {})));
}(this, (function (exports) {
    'use strict';

    let messages = {},
        defaultDateTimeFormat = 'yyyy-MM-dd HH:mm:ss',
        defaultDateFormat = 'yyyy-MM-dd',
        defaultTimeFormat = 'HH:mm:ss', //HH = 24, hh = 12
        defaultLang = 'ko',
        defaultTimezone = 'Asia/Seoul';
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
        const sessionInfo = JSON.parse(userInfo);
        i18n.dateTimeFormat = (typeof sessionInfo.dateTimeFormat !== 'undefined') ? sessionInfo.dateTimeFormat : defaultDateTimeFormat;
        i18n.dateFormat = (typeof sessionInfo.dateFormat !== 'undefined') ? sessionInfo.dateFormat : defaultDateFormat;
        i18n.timeFormat = (typeof sessionInfo.timeFormat !== 'undefined') ? sessionInfo.timeFormat : defaultTimeFormat;
        i18n.lang = (typeof sessionInfo.lang !== 'undefined') ? sessionInfo.lang : defaultLang;
        i18n.timezone = (typeof sessionInfo.timezone !== 'undefined') ? sessionInfo.timezone : defaultTimezone;

        addMessages();
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
    function getDateTime(offset) {
        if (offset === undefined) {
            offset = { 'days' : 0 };
        }
        return convertToUserHourType(luxon.DateTime.local().setZone(i18n.timezone).plus(offset).toFormat(i18n.dateTimeFormat));
    }

    function getDate(offset) {
        if (offset === undefined) {
            offset = { 'days' : 0 };
        }
        return luxon.DateTime.local().setZone(i18n.timezone).plus(offset).toFormat(i18n.dateFormat);
    }

    function getTime(offset) {
        if (offset === undefined) {
            offset = { 'hours' : 0 };
        }
        return convertToUserHourType(luxon.DateTime.local().setZone(i18n.timezone).plus(offset).toFormat(i18n.timeFormat));
    }

    /**
     * 서버로 전송하기 위해서 UTC+0, ISO8601으로 변환
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeUserDateTime 사용자가 입력한 날짜시간.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemDateTime(beforeUserDateTime, offset) {
        if (beforeUserDateTime === null || beforeUserDateTime === '') {
            return ''
        } else {
            if (offset === undefined) {
                offset = { 'days' : 0 };
            }
            return luxon.DateTime.fromFormat(convertToSystemHourType(beforeUserDateTime), i18n.dateTimeFormat, {zone: i18n.timezone}).setZone('utc+0').plus(offset).toISO();
        }
    }

    /**
     * 서버로 전송하기 위해서 UTC+0, ISO8601으로 변환
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeUserDate 변환 대상 날짜.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemDate(beforeUserDate, offset) {
        if (beforeUserDate === null || beforeUserDate === '') {
            return ''
        } else {
            if (offset === undefined) {
                offset = { 'days' : 0 };
            }
            return luxon.DateTime.fromFormat(beforeUserDate, i18n.dateFormat, {zone: i18n.timezone}).setZone('utc+0').plus(offset).toISO();
        }
    }

    /**
     * 서버로 전송하기 위해서 ISO8601으로 변환. (HH:mm)
     *  - 시간은 타임존 개념은 없음.
     *
     * @author Jung Hee chan
     * @since 2020-06-08
     * @param {String}  beforeUserTime 변환 대상 날짜.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemTime(beforeUserTime) {
        if (beforeUserTime === null || beforeUserTime === '') {
            return ''
        } else {
            return luxon.DateTime.fromFormat(convertToSystemHourType(beforeUserTime), i18n.timeFormat).toFormat('HH:mm');
        }
    }

    /**
     * 한글로 '오전','오후'로 표기된 내용을 DB에 넣기 위해 'AM','PM'으로 치환
     * datepicker 에서 '오전', '오후'로 시간 데이터를 표기하기 때문에 필요한 처리이며 datepicker 변경시 수정 필요
     *
     * @author Woo Da Jung
     * @since 2020-06-25
     * @param {String}  beforeTime 변환 대상 시간 데이터.
     * @return {String} 변환된 데이터.
     */
    function convertToSystemHourType(beforeTime) {
        if (beforeTime.indexOf('오후') !== -1) {
            beforeTime = beforeTime.replace('오후', 'PM');
        } else if (beforeTime.indexOf('오전') !== -1) {
            beforeTime = beforeTime.replace('오전', 'AM');
        }
        return beforeTime;
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
            return convertToUserHourType(luxon.DateTime.fromISO(beforeSystemDateTime, {zone: 'utc'}).setZone(i18n.timezone).toFormat(i18n.dateTimeFormat));
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
            return luxon.DateTime.fromISO(beforeSystemDate, {zone: 'utc'}).setZone(i18n.timezone).toFormat(i18n.dateFormat);
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
            return convertToUserHourType(luxon.DateTime.fromISO(beforeSystemTime).toFormat(i18n.timeFormat));
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
        return convertToUserHourType(luxon.DateTime.fromISO(beforeSystemDateTime, {zone: 'utc'}).setZone(i18n.timezone)
            .toFormat(i18n.dateTimeFormat.replace(/(mm)/g, '$1:ss') + ' (z)'));
    }

    /**
     * 12시간제이며 locale이 'ko' 일 경우, 한글로 '오전','오후'로 표기된 내용을 DB에 넣기 위해 'AM','PM'으로 치환
     * 'ko' 일 경우, 'AM','PM'로 표기된 내요을  한글로 '오전','오후'로 치환
     * datepicker 에서 '오전', '오후'로 시간 데이터를 표기하기 때문에 필요한 처리이며 datepicker 변경시 수정 필요
     *
     * @author Woo Da Jung
     * @since 2020-06-25
     * @param {String}  beforeTime 변환 대상 시간 데이터.
     * @return {String} 변환된 데이터.
     */
    function convertToUserHourType(beforeTime) {
        if (i18n.lang === 'ko') {
            if (beforeTime.indexOf('PM') !== -1) {
                beforeTime = beforeTime.replace('PM', '오후');
            } else if (beforeTime.indexOf('AM') !== -1) {
                beforeTime = beforeTime.replace('AM', '오전');
            }
        }
        return beforeTime;
    }

    /**
     * 최소 날짜시간이 최대 날짜시간 보다 큰지 비교하여 조건에 부합할 경우 true를 반환한다.
     *
     * @author Woo Da Jung
     * @param minUserDateTime
     * @param maxUserDateTime
     * @returns {boolean}
     */
    function compareSystemDateTime(minUserDateTime, maxUserDateTime) {
        return (luxon.DateTime.fromFormat(convertToSystemHourType(minUserDateTime), i18n.dateTimeFormat).setZone('utc+0').toISO().valueOf() <
            luxon.DateTime.fromFormat(convertToSystemHourType(maxUserDateTime), i18n.dateTimeFormat).setZone('utc+0').toISO().valueOf())
    }

    /**
     * 최소 날짜가 최대 날짜 보다 큰지 비교하여 조건에 부합할 경우 true를 반환한다.
     *
     * @author Woo Da Jung
     * @param minUserDate
     * @param maxUserDate
     * @returns {boolean}
     */
    function compareSystemDate(minUserDate, maxUserDate) {
        return luxon.DateTime.fromFormat(minUserDate, i18n.dateFormat).setZone('utc+0').toISO().valueOf() <
            luxon.DateTime.fromFormat(maxUserDate, i18n.dateFormat).setZone('utc+0').toISO().valueOf()
    }

    /**
     * 최소 시간이 최대 시간 보다 큰지 비교하여 조건에 부합할 경우 true를 반환한다.
     *
     * @author Woo Da Jung
     * @param minUserTime
     * @param maxUserTime
     * @returns {boolean}
     */
    function compareSystemTime(minUserTime, maxUserTime) {
        return luxon.DateTime.fromFormat(convertToSystemHourType(minUserTime), i18n.timeFormat).setZone('utc+0').toISO().valueOf() <
            luxon.DateTime.fromFormat(convertToSystemHourType(maxUserTime), i18n.timeFormat).setZone('utc+0').toISO().valueOf()
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
            async: false,
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
    exports.getTime = getTime;
    exports.getDateTime = getDateTime;
    exports.systemDateTime = convertToSystemDateTime;
    exports.systemDate = convertToSystemDate;
    exports.systemTime = convertToSystemTime;
    exports.systemHourType = convertToSystemHourType;
    exports.userDateTime = convertToUserDateTime;
    exports.userDate = convertToUserDate;
    exports.userTime = convertToUserTime;
    exports.userHourType = convertToUserHourType;
    exports.printFormat = convertToPrintFormat;
    exports.compareSystemDateTime = compareSystemDateTime;
    exports.compareSystemDate = compareSystemDate;
    exports.compareSystemTime = compareSystemTime;
    exports.get = getMessage; // 앞으로 msg로 사용하고 get은 다 msg로 수정하면 지우자.
    exports.msg = getMessage;

    Object.defineProperty(exports, '__esModule', {value: true});
})));
