/**
 * 캘린더
 *
 * 캘린더 라이브러리인 tui-calendar 라이브러리를 wrapping 하여 만든 커스텀 캘린더 라이브러리.
 *
 * Toast UI Calendar : https://nhn.github.io/tui.calendar
 *
 * @author woodajung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const CALENDAR_DEFAULT_OPTIONS = {
    isReadOnly: false, // 사용자 일정 편집 불가능하게 할 경우 true 로 설정한다.
    renderRange: null
};

function zCalendar(target, options) {
    this.options = Object.assign({}, CALENDAR_DEFAULT_OPTIONS, options);

    const modalTemplate = `<div class="calendar__modal--register__main flex-column">
            <div class="flex-row align-items-baseline" id="rangeDate">
                <input type="text" class="z-input i-datetime-picker col-3" id="startDt"/>
                <input type="text" class="z-input i-datetime-picker col-3" id="endDt"/>
                <label class="z-switch">
                    <span class="z-label">${i18n.msg('calendar.label.allDay')}</span>
                    <input type="checkbox" id="allDayYn" name="allDayYn">
                    <span></span>
                </label>
            </div>
            <div class="flex-row align-items-baseline">
                <label class="z-switch col-2">
                    <span class="z-label">${i18n.msg('calendar.label.repeatYn')}</span>
                    <input type="checkbox" id="repeatYn" name="repeatYn">
                    <span></span>
                </label>
                <select class="col-2" id="repeatType">
                    <option value="monthly" selected>${i18n.msg('calendar.label.monthly')}</option>
                    <option value="weekly">${i18n.msg('calendar.label.weekly')}</option>
                </select>
            </div>
            <input type="text" class="z-input" id="scheduleTitle" placeholder="${i18n.msg('common.label.title')}">
            <textarea class="z-textarea textarea-scroll-wrapper" id="scheduleContents" rows="3"
                      placeholder="${i18n.msg('common.label.contents')}"></textarea>
        </div>`.trim();
    /**
     * 스케쥴 등록 모달
     */
    this.modal = new modal({
        title: '',
        body: modalTemplate,
        classes: 'calendar__modal--register',
        buttons: [{
            content: i18n.msg('common.btn.register'),
            classes: 'z-button primary',
            bindKey: false,
            callback: (modal) => {
                this.saveSchedule(modal.customOptions);
            }
        }, {
            content: i18n.msg('common.btn.cancel'),
            classes: 'z-button secondary',
            bindKey: false,
            callback: (modal) => {
                modal.customOptions.guide.clearGuideElement();
                modal.hide();
            }
        }],
        close: {closable: false},
        onCreate: () => {
            // select 디자인
            aliceJs.initDesignedSelectTag(document.querySelector('.calendar__modal--register__main'));
            // 종일 여부 변경시 이벤트 추가
            document.getElementById('allDayYn').addEventListener('change', this.toggleAllDay.bind(this));
            // 이벤트 추가
            zDateTimePicker.initDateTimePicker(document.getElementById('startDt'),
                this.updateRangeDate.bind(this));
            zDateTimePicker.initDateTimePicker(document.getElementById('endDt'),
                this.updateRangeDate.bind(this));
        },
        onShow: (modal) => {
            console.log('onShow');
            // TODO: 초기화 - 등록인지 수정인지 판단
            //console.log(modal);
        },
        onHide: () => {
            // TODO: 알림창
        }
    });

    this.calendar = new tui.Calendar(target, {
        defaultView: 'month',
        taskView: false,
        template: {
            time: function (schedule) {
                const start = luxon.DateTime.fromMillis(schedule.start.getTime()).setZone(i18n.timezone);
                let html = [];
                html.push('<strong>' + start.toFormat(i18n.timeFormat) + '</strong> ');
                html.push(' ' + schedule.title);
                return html.join('');
            }
        },
        calendars: [],
        useCreationPopup: false, // 커스텀 팝업 사용 예정
        useDetailPopup: true,
        isReadOnly: this.options.isReadOnly
    });

    // 이벤트 등록
    this.calendar.on({
        beforeCreateSchedule: (e) => {
            // 초기화
            this.modal.customOptions = e;
            this.setModal(e);
            // 모달 위치 조정
            const boxElement = Object.keys(e.guide.guideElements).length ?
                e.guide.guideElements[Object.keys(e.guide.guideElements)[0]] : null;
            if (boxElement) {
                this.setModalPosition(this.modal.wrapper, boxElement);
            }
            this.modal.show();
        },
        beforeUpdateSchedule: (e) => {
            console.log('beforeUpdateSchedule', e);
            // 초기화
            this.setModal(e.schedule);

            //addSchedule(e.changes);
        },
    });
}

/**
 * 등록 모달 위치 수정
 * @param {modal} 모달
 * @param {guide} 선택된 캘린더의 날짜 rect
 */
zCalendar.prototype.setModalPosition = function (modal, guide) {
    const guideBound = guide.getBoundingClientRect(),
        containerBound = this.calendar._layout.container.getBoundingClientRect(),
        modalSize = {width: modal.offsetWidth, height: modal.offsetHeight},
        guideHorizontalCenter = (guideBound.left + guideBound.right) / 2,
        margin = 3;
    let x = guideHorizontalCenter - (modalSize.width / 2),
        y = guideBound.top - modalSize.height;

    if (x + modalSize.width > containerBound.right) {
        x = guideBound.right - modalSize.width;
    }

    if (x < containerBound.left) {
        x = 0;
    } else {
        x = x - containerBound.left
    }

    if (y < containerBound.top) {
        y = guideBound.bottom - containerBound.top + margin;
    } else {
        y = y - containerBound.top - margin;
    }

    if (y + modalSize.height > containerBound.bottom) {
        y = containerBound.bottom - modalSize.height - containerBound.top - margin;
    }

    modal.style.left = x + 'px';
    modal.style.top = y + 'px';
};

/**
 * 상단 메뉴 현재 날짜 표시
 */
zCalendar.prototype.setRenderRangeText = function () {
    if (!this.options.renderRange) {
        return false;
    }
    const options = this.calendar.getOptions();
    const viewName = this.calendar.getViewName();
    let html = [];
    if (viewName === 'day') {
        html.push(luxon.DateTime.fromMillis(
            this.calendar.getDate().getTime()).setZone(i18n.timezone).toFormat(i18n.dateFormat));
    } else if (viewName === 'month' &&
        (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
        html.push(luxon.DateTime.fromMillis(
            this.calendar.getDate().getTime()).setZone(i18n.timezone).toFormat(this.getMonthFormat()));
    } else {
        html.push(luxon.DateTime.fromMillis(
            this.calendar.getDateRangeStart().getTime()).setZone(i18n.timezone).toFormat(i18n.dateFormat));
        html.push(' ~ ');
        html.push(luxon.DateTime.fromMillis(
            this.calendar.getDateRangeEnd().getTime()).setZone(i18n.timezone).toFormat(i18n.dateFormat));
    }
    this.options.renderRange.innerHTML = html.join('');
};

/**
 * 월, 주, 년, 리스트 변경에 따른 이벤트 처리
 * @param {type} 타입 - month|day|week|task
 */
zCalendar.prototype.setCalendarType = function (type, callback) {
    if (type === 'task') {
        // TODO: 커스텀 목록 만들기 - 구글 캘린더 처럼 목록 나오게
    } else {
        this.calendar.changeView(type, true);
    }
    this.setRenderRangeText();

    if (typeof callback === 'function') {
        callback(this.calendar);
    }
};

/**
 * 시작일시, 종료일시 설정
 * @param {Schedule} schedule - schedule
 */
zCalendar.prototype.setModal = function (schedule) {
    // 반복 여부
    const repeatYn = this.modal.wrapper.querySelector('#repeatYn');
    if (Object.prototype.hasOwnProperty.call(schedule, 'repeatYn')) {
        repeatYn.checked = schedule.repeatYn;
    } else {
        repeatYn.checked = false;
    }
    // 반복 타입
    const repeatType = this.modal.wrapper.querySelector('#repeatType');
    if (Object.prototype.hasOwnProperty.call(schedule, 'repeatType')) {
        repeatType.querySelector('option[value="' + schedule.repeatType + '"]').selected = true;
        repeatType.value = schedule.repeatType;
    } else {
        repeatType.options[0].selected = true;
    }
    // 제목
    const scheduleTitle = this.modal.wrapper.querySelector('#scheduleTitle');
    if (Object.prototype.hasOwnProperty.call(schedule, 'scheduleTitle')) {
        scheduleTitle.value = schedule.scheduleTitle;
    } else {
        scheduleTitle.value = '';
    }
    // 내용
    const scheduleContents = this.modal.wrapper.querySelector('#scheduleContents');
    if (Object.prototype.hasOwnProperty.call(schedule, 'scheduleContents')) {
        scheduleContents.value = schedule.scheduleContents;
    } else {
        scheduleContents.value = '';
    }

    // 날짜 포맷
    const format = schedule.isAllDay ? i18n.dateFormat : i18n.dateTimeFormat;
    // 시작일시
    const startDt = this.modal.wrapper.querySelector('#startDt');
    const start = luxon.DateTime.fromMillis(schedule.start.getTime()).setZone(i18n.timezone).toFormat(format);
    startDt.setAttribute('value', start);
    this.modal.customOptions.systemStart = luxon.DateTime.fromMillis(schedule.start.getTime(),
        {zone: i18n.timezone}).setZone('utc+0').toISO();
    // 종료일시
    const endDt = this.modal.wrapper.querySelector('#endDt');
    const end = luxon.DateTime.fromMillis(schedule.end.getTime()).setZone(i18n.timezone).toFormat(format);
    endDt.setAttribute('value', end);
    this.modal.customOptions.systemEnd = luxon.DateTime.fromMillis(schedule.end.getTime(),
        {zone: i18n.timezone}).setZone('utc+0').toISO();
    // 종일 여부
    const allDayYn = this.modal.wrapper.querySelector('#allDayYn');
    // 이벤트 호출
    if (allDayYn.checked !== schedule.isAllDay) {
        allDayYn.checked = schedule.isAllDay;
        allDayYn.dispatchEvent(new Event('change'));
    }

    console.log(this.modal.customOptions);
};


/**
 * 사용자 포멧에 따른 달력 포멧 조회
 */
zCalendar.prototype.getMonthFormat = function () {
    switch (i18n.dateFormat) {
        case 'yyyy-MM-dd':
        case 'yyyy-dd-MM':
            return 'yyyy-MM';
        case 'dd-MM-yyyy':
        case 'MM-dd-yyyy':
            return 'MM-yyyy';
        default:
            return 'yyyy-MM';
    }
};

/**
 * 현재 시간 조회
 */
zCalendar.prototype.getStandardDate = function () {
    return luxon.DateTime.fromMillis(this.calendar.getDate().getTime()).setZone('utc+0').toISO()
};

/**
 * 메뉴 선택에 따른 처리
 * @param {action} 타입 - prev|next|today
 * @param {callback} 콜백함수
 */
zCalendar.prototype.clickCalendarMenu = function (action, callback) {
    switch (action) {
        case 'prev':
            this.calendar.prev();
            break;
        case 'next':
            this.calendar.next();
            break;
        case 'today':
            this.calendar.today();
            break;
        default:
            break;
    }

    this.setRenderRangeText();

    if (typeof callback === 'function') {
        callback(this.calendar);
    }
};

/**
 * 시작일시, 종료일시 설정
 */
zCalendar.prototype.toggleAllDay = function () {
    const rangeDate = this.modal.wrapper.querySelector('#rangeDate');
    const allDayYn = this.modal.wrapper.querySelector('#allDayYn');
    const startDt = this.modal.wrapper.querySelector('#startDt');
    const start = (allDayYn.checked) ? i18n.systemDateTime(startDt.value) : i18n.systemDate(startDt.value);
    const endDt = this.modal.wrapper.querySelector('#endDt');
    const end = (allDayYn.checked) ? i18n.systemDateTime(endDt.value) : i18n.systemDate(endDt.value);
    // 시작일시 , 종료일시 초기화
    this.modal.wrapper.querySelectorAll('.z-picker-wrapper-date').forEach(function (dt) {
        dt.remove();
    });
    rangeDate.insertAdjacentHTML('afterbegin',
        `<input type="text" class="z-input i-datetime-picker col-3" id="endDt"/>`);
    rangeDate.insertAdjacentHTML('afterbegin',
        `<input type="text" class="z-input i-datetime-picker col-3" id="startDt"/>`);

    // 종일 선택시, datePicker 를 사용하고 나머지는 dateTimePicker 사용
    const newStartDt = this.modal.wrapper.querySelector('#startDt');
    newStartDt.setAttribute('value', (allDayYn.checked) ? i18n.userDate(start) :
        i18n.userDateTime(start));
    const newEndDt = this.modal.wrapper.querySelector('#endDt');
    newEndDt.setAttribute('value', (allDayYn.checked) ? i18n.userDate(end) :
        i18n.userDateTime(end, {hours: 1}));

    // 라이브러리 등록
    if (allDayYn.checked) {
        zDateTimePicker.initDatePicker(newStartDt, this.updateRangeDate.bind(this));
        zDateTimePicker.initDatePicker(newEndDt, this.updateRangeDate.bind(this));
    } else {
        zDateTimePicker.initDateTimePicker(newStartDt, this.updateRangeDate.bind(this));
        zDateTimePicker.initDateTimePicker(newEndDt, this.updateRangeDate.bind(this));
    }

    this.modal.customOptions.isAllDay = allDayYn.checked;
};

/**
 * 시작일시, 종료일시 변경
 */
zCalendar.prototype.updateRangeDate = function (e, picker) {
    // 날짜 포맷
    const format = this.modal.customOptions.isAllDay ? i18n.dateFormat : i18n.dateTimeFormat;
    const convertTime = this.modal.customOptions.isAllDay ? e.value : i18n.systemHourType(e.value);
    if (e.id === 'startDt') {
        // 시작일시는 종료일시보다 크면 안된다.
        const endDt = this.modal.wrapper.querySelector('#endDt');
        const isValidStartDt = this.modal.customOptions.isAllDay ? i18n.compareSystemDate(e.value, endDt.value)
            : i18n.compareSystemDateTime(e.value, endDt.value);
        if (!isValidStartDt) {
            zAlert.warning(i18n.msg('common.msg.selectBeforeDateTime', endDt.value), () => {
                e.value = luxon.DateTime.fromISO(this.modal.customOptions.systemStart, {zone: 'utc'})
                    .setZone(i18n.timezone).toFormat(format);
                picker.open();
            });
            return false;
        }
        this.modal.customOptions.systemStart =
            luxon.DateTime.fromFormat(convertTime, format, {zone: i18n.timezone}).setZone('utc+0').toISO();
    } else {
        // 종료일시는 시작일시보다 작으면 안된다.
        const startDt = this.modal.wrapper.querySelector('#startDt');
        const isValidEndDt = this.modal.customOptions.isAllDay ? i18n.compareSystemDate(startDt.value, e.value)
            : i18n.compareSystemDateTime(startDt.value, e.value);
        if (!isValidEndDt) {
            zAlert.warning(i18n.msg('common.msg.selectAfterDateTime', startDt.value), () => {
                e.value = luxon.DateTime.fromISO(this.modal.customOptions.systemEnd, {zone: 'utc'})
                    .setZone(i18n.timezone).toFormat(format);
                picker.open();
            });
            return false;
        }
        this.modal.customOptions.systemEnd =
            luxon.DateTime.fromFormat(convertTime, format, {zone: i18n.timezone}).setZone('utc+0').toISO();
    }
};

/**
 * 스케쥴 추가
 * @param {schedule} schedule - schedule
 */
zCalendar.prototype.saveSchedule = function (schedule) {
    if (isEmpty('scheduleTitle', 'common.label.title')) return false;

    let url = '/rest/calendar';
    let method = 'POST';
    const saveData = {
        calendarId: this.calendarId,
        scheduleTitle: this.modal.wrapper.querySelector('#scheduleTitle').value,
        scheduleContents: this.modal.wrapper.querySelector('#scheduleContents').value,
        startDt: this.modal.customOptions.systemStart,
        endDt: this.modal.customOptions.systemEnd,
        allDayYn: schedule.isAllDay,
        repeatYn: this.modal.wrapper.querySelector('#repeatYn').checked,
        repeatType: this.modal.wrapper.querySelector('#repeatType').value
    };
    // TODO: 실 데이터 연동 후 주석 해제
    /*const resultMsg = (method === 'POST') ? i18n.msg('common.msg.register') : i18n.msg('common.msg.update');
    aliceJs.fetchJson(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(saveData)
    }).then((response) => {
        switch (response.status) {
            case aliceJs.response.success:
                zAlert.success(resultMsg, function () {
                    this.calendar.createSchedules([
                        {
                            id: response.data.scheduleId,
                            calendarId: saveData.calendarId,
                            title: saveData.scheduleTitle,
                            isAllDay: saveData.allDayYn,
                            body: saveData.scheduleContents,
                            start: saveData.startDt,
                            end: saveData.endDt,
                            category: saveData.allDayYn ? 'allday' : 'time',
                            dueDateClass: ''
                        },
                    ]);
                });
                break;
            case aliceJs.response.error:
                zAlert.danger(i18n.msg('common.msg.fail'));
                break;
            default :
                break;
        }
    });*/
    this.addSchedule([
        {
            id: '1',
            calendarId: saveData.calendarId,
            title: saveData.scheduleTitle,
            isAllDay: saveData.allDayYn,
            body: saveData.scheduleContents,
            start: saveData.startDt,
            end: saveData.endDt,
            category: saveData.allDayYn ? 'allday' : 'time',
            dueDateClass: ''
        },
    ]);
    this.modal.hide();
};

/**
 * 캘린더 등록
 *  @param {schedule} schedule - schedule
 */
zCalendar.prototype.addSchedule = function (schedules) {
    this.calendar.createSchedules(schedules);
};

/**
 * 캘린더 초기화
 */
zCalendar.prototype.destroy = function () {
    this.calendar.clear();
}
