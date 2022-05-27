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
    viewType: 'month', // 달력 타입 (월, 주, 일, 일정 리스트)
    fontColor: '#ffffff', // 일정 폰트 색상
    // 캘린더 종류가 여러개일때 색상을 다르게 표현 가능
    colors: ['#339AF0', '#76BD26', '#a95eeb', '#6885F7', '#FF850A', '#316D0C', '#F763C1', '#BDBDBD'],
    days: [
        i18n.msg('calendar.label.days.monday'),
        i18n.msg('calendar.label.days.tuesday'),
        i18n.msg('calendar.label.days.wednesday'),
        i18n.msg('calendar.label.days.thursday'),
        i18n.msg('calendar.label.days.friday'),
        i18n.msg('calendar.label.days.saturday'),
        i18n.msg('calendar.label.days.sunday')
    ],
    shortDays: [
        i18n.msg('calendar.label.shortDays.monday'),
        i18n.msg('calendar.label.shortDays.tuesday'),
        i18n.msg('calendar.label.shortDays.wednesday'),
        i18n.msg('calendar.label.shortDays.thursday'),
        i18n.msg('calendar.label.shortDays.friday'),
        i18n.msg('calendar.label.shortDays.saturday'),
        i18n.msg('calendar.label.shortDays.sunday')
    ],
    // 반복 안함, 매월 X번째 X요일, 매주 X요일
    repeatTypes: ['dayOfMonth', 'weekOfMonth'],
    dayOfMonthType: ['', 'first', 'second', 'third', 'fourth', 'last'],
    createModalSize: { w: 620, h: 320 }, // 모달 위치
    detailModalSize: { w: 300, h: 280 }, // 모달 위치
    isReadOnly: false, // 사용자 일정 편집 불가능하게 할 경우 true 로 설정한다.
};

function zCalendar(id, options) {
    this.el = document.getElementById(id); // 달력을 그려줄 엘리먼트
    this.taskView = this.el.parentNode.querySelector('.calendar__view--task'); // 일정 보기 엘리먼트
    if (!this.taskView) {
        const taskViewEl = document.createElement('div');
        taskViewEl.className = 'calendar__view calendar__view--task';
        taskViewEl.id = 'taskView';
        this.taskView = taskViewEl;
    }
    this.renderRange = this.el.parentNode.querySelector('.calendar__render-range-text'); // 달력 문구 엘리먼트
    this.calendarList = []; // 월별 캘린더 목록 - 현재는 내 일정 , 문서 2가지만 사용됨
    this.scheduleList = []; // 월별 일정 목록
    // 옵션
    this.options = Object.assign({}, CALENDAR_DEFAULT_OPTIONS, options);
    
    // 스케쥴 등록|수정 모달
    this.createModal = new modal({
        title: '',
        body: this.getCreateModalTemplate(),
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
                if (modal.customOptions.guide) {
                    modal.customOptions.guide.clearGuideElement();
                }
                modal.hide();
            }
        }],
        close: { closable: false },
        onCreate: (modal) => {
            modal.wrapper.style.setProperty('--data-modal-width', this.options.createModalSize.w);
            modal.wrapper.style.setProperty('--data-modal-height', this.options.createModalSize.h);
            // select 디자인
            aliceJs.initDesignedSelectTag(document.querySelector('.calendar__modal--register__main'));
            // 종일 여부 변경시 이벤트 추가
            document.getElementById('allDayYn').addEventListener('change', this.onToggleAllDay.bind(this));
            // 반복 여부 변경시 이벤트 추가
            document.getElementById('repeatYn').addEventListener('change', this.onToggleRepeat.bind(this));
            // 이벤트 추가
            zDateTimePicker.initDateTimePicker(document.getElementById('startDt'),
                this.onUpdateRangeDateTime.bind(this));
            zDateTimePicker.initDateTimePicker(document.getElementById('endDt'),
                this.onUpdateRangeDateTime.bind(this));
        },
        onShow: () => {},
        onHide: () => {}
    });
    
    // 스케쥴 상세보기 모달
    this.detailModal = new modal({
        title: '',
        body: this.getDetailModalTemplate(),
        classes: 'calendar__modal--detail',
        buttons: [{
            content: i18n.msg('common.btn.toModify'),
            classes: 'z-button primary',
            bindKey: false,
            callback: (modal) => {
                // 수정 모달 호출
                const schedule = modal.customOptions.schedule;
                schedule.mode = 'edit';

                this.createModal.customOptions = schedule;
                this.setCreateModal(schedule);
                this.setModalPosition(this.createModal.wrapper, modal.customOptions.event.target);

                // 기존 모달 닫고 편집 모달 띄우기
                modal.hide();
                this.createModal.show();
            }
        }, {
            content: i18n.msg('common.btn.delete'),
            classes: 'z-button secondary',
            bindKey: false,
            callback: (modal) => {
                this.deleteSchedule(modal.customOptions.schedule);
            }
        },{
            content: i18n.msg('common.btn.close'),
            classes: 'z-button secondary',
            bindKey: false,
            callback: (modal) => {
                modal.hide();
            }
        }],
        close: { closable: false },
        onCreate: (modal) => {
            modal.wrapper.style.setProperty('--data-modal-width', this.options.detailModalSize.w);
            modal.wrapper.style.setProperty('--data-modal-height', this.options.detailModalSize.h);
        },
        onShow: () => {},
        onHide: () => {}
    });

    // 반복 일정 수정 모달
    this.repeatModal = new modal({
        title: i18n.msg('calendar.label.repeatModify'),
        body: this.getRepeatModalTemplate(),
        classes: 'calendar__modal--repeat',
        buttons: [{
            content: i18n.msg('common.btn.check'),
            classes: 'z-button primary',
            bindKey: false,
            callback: (modal) => {
                modal.saveData.repeatPeriod = document.querySelector('input[name="repeatPeriod"]:checked').value;
                this.restSubmit(modal.customOptions.method, modal.customOptions.url, modal.saveData, modal);
            }
        }, {
            content: i18n.msg('common.btn.cancel'),
            classes: 'z-button secondary',
            bindKey: false,
            callback: (modal) => {
                modal.hide();
            }
        }],
        onCreate: () => {},
        onShow: (modal) => {
            // 초기화
            modal.saveData.repeatPeriod = '';
            modal.wrapper.querySelector('#repeatPeriodToday').checked = true;
        },
        onHide: () => {}
    });

    // TUI 캘린더 초기화
    this.calendar = new tui.Calendar(this.el, {
        defaultView: this.options.viewType,
        taskView: false,
        template: {
            // 시간 단위 등록된 일정 템플릿
            time: (schedule) => {
                const start = luxon.DateTime.fromMillis(schedule.start.getTime()).setZone(i18n.timezone);
                let html = [];
                html.push('<strong>' + start.toFormat(i18n.timeFormat) + '</strong> ');
                html.push(' ' + schedule.title);
                return html.join('');
            },
            // 월별 달력 상단 문구 템플릿
            monthDayname: (dayName) => {
                let dayOfWeek = dayName.day - 1;
                if (dayOfWeek === -1) {
                    dayOfWeek = 6;
                }
                return '<span class="calendar-week-dayname-name">' + this.options.shortDays[dayOfWeek] + '</span>';
            }
        },
        calendars: [],
        useCreationPopup: false, // 등록 팝업 사용 거부 - 커스텀 팝업 사용 예정
        useDetailPopup: false, // 상세 팝업 사용 거부 - 커스텀 팝업 사용 예정
        isReadOnly: this.options.isReadOnly
    });

    // 이벤트 등록
    this.calendar.on({
        // 등록
        beforeCreateSchedule: (e) => {
            // 초기화
            e.mode = 'register';
            this.createModal.customOptions = e;
            this.setCreateModal(e);

            // 모달 위치 조정
            const guideElement = e.guide.guideElement ? e.guide.guideElement :
                e.guide.guideElements[Object.keys(e.guide.guideElements)[0]];
            this.setModalPosition(this.createModal.wrapper, guideElement);

            // 모달 표시
            this.createModal.show();

        },
        // 편집 - drag & drop 시
        beforeUpdateSchedule: (e) => {
            const schedule = e.schedule;
            const changes = e.changes;

            // drap & drop 시 동일하면 변경 안함
            if (schedule.start.getTime() === changes.start.getTime() &&
                schedule.end.getTime() === changes.end.getTime()) {
                return false;
            }

            if (changes && !changes.isAllDay && schedule.category === 'allday') {
                changes.category = 'time';
            }

            this.updateScheduleOnDrop(schedule, changes);
        },
        // 스케쥴 클릭
        clickSchedule: (e) => {
            this.detailModal.customOptions = e;
            this.setDetailModal(e.schedule);

            // 모달 위치 조정
            this.setModalPosition(this.detailModal.wrapper, e.event.target);
            
            // 모달 표시
            this.detailModal.show();
        }
    });
}
Object.assign(zCalendar.prototype, {
    /**
     * 캘린더 목록 설정 - 캘린더 종류가 여러개일때 아이디, 이름, 색상을 다르게 표현 가능
     * @param {dataList} 캘린더 데이터
     */
    setCalendars: function (dataList) {
        this.calendarList.length = 0;
        this.calendarList = dataList.reduce((result, data, idx) => {
            const mainColor = this.options.colors[idx];
            result.push({
                id: data.calendarId,
                name: data.calendarName,
                color: this.options.fontColor,
                bgColor: mainColor,
                borderColor: mainColor,
                dragBgColor: mainColor
            });
            return result;
        }, []);

        this.calendar.setCalendars(this.calendarList);

        // TODO: 사용자별 일정 등록 기능 추가시 보이도록 처리하여 사용한다.
        const calendarList = this.createModal.wrapper.querySelector('#calendarList');
        const calendarOptionTemplate = this.calendarList.map((opt, idx) => {
            return `<option value="${opt.id}" ${idx === 0 ? 'selected=\'true\'' : ''}>${opt.name}</option>`;
        });
        calendarList.innerHTML = '';
        calendarList.insertAdjacentHTML('beforeend',
            `<select class="col-3" id="calendarId">${calendarOptionTemplate}</select>`);

        // select box 디자인
        aliceJs.initDesignedSelectTag(document.querySelector('.calendar__modal--register__main'));
    },
    /**
     * 월, 주, 년, 리스트 변경에 따른 이벤트 처리
     * @param {type} 타입 - month|day|week|task
     */
    setCalendarViewType: function (type, callback) {
        this.options.viewType = type;

        if (type === 'task') {
            this.el.classList.remove('on');
            this.taskView.classList.add('on');
            this.calendar.changeView('month', true);
            this.setTaskView();
        } else {
            this.el.classList.add('on');
            this.taskView.classList.remove('on');
            this.calendar.changeView(type, true);
        }
        this.setRenderRangeText();

        if (typeof callback === 'function') {
            callback(this.calendar);
        }
    },
    /**
     * 일정 리스트 생성
     */
    setTaskView: function () {
        this.taskView.innerHTML = '';
        const start = this.getDate().setZone(i18n.timezone);
        // 현재 월의 날짜
        const daysInMonth = start.daysInMonth;
        const startOfMonth = new Date(start.toJSDate().setDate(1));
        const firstWeekDay = startOfMonth.getDay(); // 0: Sun ~ 6: Sat

        let taskViewTemplate = ``;
        for (let i = 0; i < daysInMonth; i++) {
            const weekDay = ( Number(firstWeekDay) + i ) % 7;
            const rowContentTemplate = `<div class="calendar__rowHead">
                <span class="calendar__day">${i + 1}</span>
                <span class="calendar__weekDay">${this.options.days[weekDay]}</span>
            </div>
            <div class="calendar__row" data-day="${i + 1}"></div>`.trim();

            taskViewTemplate += `<div class="calendar__rowGroup">${rowContentTemplate}</div>`;
        }
        this.taskView.insertAdjacentHTML('beforeend', taskViewTemplate);
    },
    /**
     * 상단 메뉴 현재 날짜 표시
     */
    setRenderRangeText: function () {
        if (!this.renderRange) {
            return false;
        }
        const options = this.calendar.getOptions();
        const viewName = this.calendar.getViewName();
        let html = [];
        if (viewName === 'day') {
            html.push(luxon.DateTime.fromMillis(
                this.calendar.getDate().getTime()).toFormat(i18n.dateFormat));
        } else if (viewName === 'month' &&
            (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
            html.push(luxon.DateTime.fromMillis(
                this.calendar.getDate().getTime()).toFormat(this.getYearAndMonthFormat()));
        } else {
            html.push(luxon.DateTime.fromMillis(
                this.calendar.getDateRangeStart().getTime()).toFormat(i18n.dateFormat));
            html.push(' ~ ');
            html.push(luxon.DateTime.fromMillis(
                this.calendar.getDateRangeEnd().getTime()).toFormat(i18n.dateFormat));
        }
        this.renderRange.innerHTML = html.join('');
    },
    /**
     * 날짜 선택시 반복 일정 옵션 변경 - Designed Selectbox에 맞춰져 있음
     * @param {schedule} schedule - schedule
     */
    setRepeatType(schedule) {
        const target = this.createModal.wrapper.querySelector('#repeatType');
        if (!target) { return false; }

        const parent = target.parentNode.parentNode;
        const start = luxon.DateTime.fromMillis(schedule.tempStart).setZone(i18n.timezone);
        const selectedWeekNumber = start.weekday;
        const selectedDayOfMonthNumber = this.getWeekNumberOfMonth(start); // 매월 X번째

        // 월요일 1, 화요일 2... 일요일 7  =  3번쨰주 일요일 = 3_7
        const getRepeatTypeOptionValue = function (type) {
            switch (type) {
                case 'none':
                    return '';
                case 'dayOfMonth':
                    return selectedDayOfMonthNumber + '_' + selectedWeekNumber;
                case 'weekOfMonth':
                    return selectedWeekNumber;
            }
        }
        // 초기화
        target.parentNode.remove();

        const repeatTypeOptions = this.options.repeatTypes.reduce((result, opt, idx) => {
            const text = this.getRepeatTypeOptionText(opt, selectedWeekNumber, selectedDayOfMonthNumber);
            const value = getRepeatTypeOptionValue(opt);
            result[idx] = { 'id': opt, 'text': text, 'value': value };
            return result;
        }, []);

        // 반복 안함, 매월 X번째 X요일, 매주 X요일
        const repeatTypeOptionTemplate = repeatTypeOptions.map((opt, idx) => {
            return `<option value="${opt.id}" ${idx === 0 ? 'selected=\'true\'' : ''} data-repeatValue="${opt.value}">`
                + `${opt.text}</option>`;
        }).join('');

        parent.insertAdjacentHTML('beforeend',
            `<select class="col-2" id="repeatType">${repeatTypeOptionTemplate}</select>`);

        aliceJs.initDesignedSelectTag(parent);
    },
    /**
     * 스케쥴 등록|수정 모달 초기화
     * @param {schedule} schedule - schedule
     */
    setCreateModal: function (schedule) {
        // 모달 버튼명 변경
        this.detailModal.wrapper.querySelector('.modal-button').textContent = (schedule.mod === 'register') ?
            i18n.msg('common.btn.register') : i18n.msg('common.btn.toModify');

        // 캘린더 ID
        const calendarId = this.createModal.wrapper.querySelector('#calendarId');
        if (Object.prototype.hasOwnProperty.call(schedule, 'calendarId')) {
            calendarId.querySelector('option[value="' + schedule.calendarId + '"]').selected = true;
            calendarId.value = schedule.calendarId;
        } else {
            calendarId.options[0].selected = true;
        }
        // 제목
        const scheduleTitle = this.createModal.wrapper.querySelector('#scheduleTitle');
        scheduleTitle.value = Object.prototype.hasOwnProperty.call(schedule, 'title') ? schedule.title : '';

        // 내용
        const scheduleContents = this.createModal.wrapper.querySelector('#scheduleContents');
        scheduleContents.value = Object.prototype.hasOwnProperty.call(schedule, 'body') ? schedule.body : '';

        const format = schedule.isAllDay ? i18n.dateFormat : i18n.dateTimeFormat; // 날짜 포맷
        // 시작일시
        const startDt = this.createModal.wrapper.querySelector('#startDt');
        const start = luxon.DateTime.fromMillis(schedule.start.getTime()).setZone(i18n.timezone);
        startDt.setAttribute('value', start.toFormat(format));
        this.createModal.customOptions.tempStart = start.toMillis();

        // 종료일시
        const endDt = this.createModal.wrapper.querySelector('#endDt');
        const end = luxon.DateTime.fromMillis(schedule.end.getTime()).setZone(i18n.timezone);
        endDt.setAttribute('value', end.toFormat(format));
        this.createModal.customOptions.tempEnd = end.toMillis();

        const raw = ( schedule.mode === 'edit' && schedule.raw !== null) ? schedule.raw : {};
        // 반복 여부
        const repeatYn = this.createModal.wrapper.querySelector('#repeatYn');
        repeatYn.checked = schedule.mode === 'edit' && Object.prototype.hasOwnProperty.call(raw, 'repeatYn') ?
            raw.repeatYn : false;
        repeatYn.dispatchEvent(new Event('change'));

        // 선택된 날짜에 따른 반복 일정 값 변경
        this.setRepeatType(schedule);

        // 반복 타입
        const repeatType = this.createModal.wrapper.querySelector('#repeatType');
        if (Object.prototype.hasOwnProperty.call(raw, 'repeatType') && raw.repeatType !== '') {
            repeatType.querySelector('option[value="' + raw.repeatType + '"]').selected = true;
            repeatType.value = raw.repeatType;
        } else {
            repeatType.options[0].selected = true;
        }

        // 종일 여부
        const allDayYn = this.createModal.wrapper.querySelector('#allDayYn');
        // 이벤트 호출
        if (allDayYn.checked !== schedule.isAllDay) {
            allDayYn.checked = schedule.isAllDay;
            allDayYn.dispatchEvent(new Event('change'));
        }
    },
    /**
     * 스케쥴 등록|수정|상세보기 모달 위치 조정
     * @param {modal} 모달
     * @param {guide} 선택된 캘린더의 날짜 rect
     */
    setModalPosition: function (modal, guide) {
        const modalDialog = modal.querySelector('.modal-dialog'),
            guideBound = guide.getBoundingClientRect(),
            modalSize = {
                w: Number(modal.style.getPropertyValue('--data-modal-width')),
                h: Number(modal.style.getPropertyValue('--data-modal-height'))
            },
            x = guideBound.left + modalSize.w,
            _x = guideBound.left - modalSize.w,
            y = guideBound.top + guideBound.height + modalSize.h,
            _y = guideBound.top - modalSize.h,
            w = window.innerWidth,
            h = window.innerHeight,
            margin = 8;  // 3은 간격

        if (x >= w && _x > 0) {
            modalDialog.style.left = guideBound.left + guideBound.width - modalSize.w + 'px';
        } else {
            modalDialog.style.left = guideBound.left + 'px';
        }

        if (y >= h && _y > 0) {
            modalDialog.style.top = guideBound.top - modalSize.h - margin + 'px';
        } else {
            modalDialog.style.top = guideBound.top + guideBound.height +  margin + 'px';
        }
    },
    /**
     * 스케쥴 상세 보기 모달 초기화
     * @param {schedule} schedule - schedule
     */
    setDetailModal: function (schedule) {
        // 아이콘
        const scheduleIcon = this.detailModal.wrapper.querySelector('#detailScheduleIcon');
        scheduleIcon.style.backgroundColor = schedule.bgColor;
        // 제목
        const scheduleTitle = this.detailModal.wrapper.querySelector('#detailScheduleTitle');
        scheduleTitle.textContent = schedule.title;
        // 날짜
        const rangeDate = this.detailModal.wrapper.querySelector('#detailRangeDate');
        const format = schedule.isAllDay ? i18n.dateFormat : i18n.dateTimeFormat; // 날짜 포맷
        const start = luxon.DateTime.fromMillis(schedule.start.getTime()).setZone(i18n.timezone);
        // 종일 : 2022-05-20
        let rangeDateHtml = [];
        rangeDateHtml.push(start.toFormat(format));
        // 종일X : 2022-05-20 13:00 ~ 2022-05-20 14:00
        if (!schedule.isAllDay) {
            rangeDateHtml.push(' ~ ');
            rangeDateHtml.push(
                luxon.DateTime.fromMillis(schedule.end.getTime()).setZone(i18n.timezone).toFormat(format)
            );
        }
        // 반복 : (매주 X요일) | (매월 X번째 X요일)
        if (schedule.raw.repeatYn) {
            rangeDateHtml.push('<br/>');
            rangeDateHtml.push('( ');

            const dayOfMonthNumber = this.getWeekNumberOfMonth(start);
            rangeDateHtml.push(this.getRepeatTypeOptionText(schedule.raw.repeatType, start.weekday, dayOfMonthNumber));
            rangeDateHtml.push(' )');
        }
        rangeDate.innerHTML = rangeDateHtml.join('');

        // 내용
        const scheduleContents = this.detailModal.wrapper.querySelector('#detailScheduleContents');
        scheduleContents.textContent = schedule.body;
        // 작성자
        const ownerName = this.detailModal.wrapper.querySelector('#detailOwnerName');
        ownerName.textContent = schedule.raw.ownerName;
    },
    /**
     * 년도 + 월 포멧 반환
     */
    getYearAndMonthFormat: function () {
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
    },
    /**
     * 매월 X번째 X요일, 매주 X요일 문구 반환
     * @param {type} dayOfMonth | weekOfMonth
     * @param {weekNumber} 1 - 월 ... 7 - 일
     * @param {dayOfMonthNumber} 매월 몇번째인지를 가리키는 숫자
     */
    getRepeatTypeOptionText: function (type, weekNumber, dayOfMonthNumber) {
        const selectedWeek = this.options.days[weekNumber - 1]; // X요일
        const selectedDayOfMonth = this.options.dayOfMonthType[dayOfMonthNumber];

        switch (type) {
            case 'dayOfMonth':
                return i18n.msg('calendar.label.dayOfMonth',
                    i18n.msg('calendar.label.dayOfMonthType.' + selectedDayOfMonth)) + ' ' + selectedWeek;
            case 'weekOfMonth':
                return i18n.msg('calendar.label.weekOfMonth') + ' ' + selectedWeek;
            default:
                return i18n.msg('calendar.label.none');
        }
    },
    /**
     * 스케쥴 등록|수정 모달 템플릿
     */
    getCreateModalTemplate: function () {
        const repeatTypeOptionTemplate = this.options.repeatTypes.map((opt, idx) => {
            return `<option value="${opt}" ${idx === 0 ? 'selected=\'true\'' : ''}></option>`;
        }).join('');
        return `<div class="calendar__modal--register__main flex-column">
            <div class="flex-row" id="calendarList" style="display: none;"></div>
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
                <div id="repeatDetail" style="visibility: hidden;">
                    <select class="col-2" id="repeatType">${repeatTypeOptionTemplate}</select>
                </div>
            </div>
            <input type="text" class="z-input" id="scheduleTitle" placeholder="${i18n.msg('common.label.title')}">
            <textarea class="z-textarea textarea-scroll-wrapper" id="scheduleContents" rows="3"
                      placeholder="${i18n.msg('common.label.contents')}"></textarea>
        </div>`.trim();
    },
    /**
     * 스케쥴 상세보기 모달 템플릿
     */
    getDetailModalTemplate: function () {
        return `<div class="calendar__modal--detail__main flex-column">
            <div class="calendar__modal--detail__title flex-row">
                <span class="calendar__color--round" id="detailScheduleIcon" style="background-color: transparent">
                </span>
                <span id="detailScheduleTitle"></span>
            </div>
            <span id="detailRangeDate"></span>
            <span id="detailScheduleContents"></span>
            <span id="detailOwnerName"></span>
        </div>`.trim();
    },
    /**
     * 반복 일정 모달 템플릿
     */
    getRepeatModalTemplate: function () {
        return `<div class="calendar__modal--repeat__main flex-column">
            <label class="z-radio" for="repeatPeriodToday" tabindex="0">
                <input type="radio" name="repeatPeriod" id="repeatPeriodToday" value="today" checked="true"/>
                <span></span>
                <span class="z-label">${i18n.msg('calendar.label.repeatPeriod.today')}</span>
            </label>
            <label class="z-radio" for="repeatPeriodAfter" tabindex="0">
                <input type="radio" name="repeatPeriod" id="repeatPeriodAfter" value="after"/>
                <span></span>
                <span class="z-label">${i18n.msg('calendar.label.repeatPeriod.after')}</span>
            </label>
            <label class="z-radio" for="repeatPeriodAll" tabindex="0">
                <input type="radio" name="repeatPeriod" id="repeatPeriodAll" value="all"/>
                <span></span>
                <span class="z-label">${i18n.msg('calendar.label.repeatPeriod.all')}</span>
            </label>
        </div>`.trim();
    },
    /**
     * 일정보기 템플릿
     * @param {schedule} schedule - schedule
     */
    getTaskViewCellTemplate: function (schedule) {
        const calendar = this.calendarList.find((item) => item.id ===schedule.calendarId);
        let rangeDateHtml = [];
        if (schedule.isAllDay) {
            rangeDateHtml.push(i18n.msg('calendar.label.allDay'));
        } else {
            // 시간 추출
            rangeDateHtml.push(
                luxon.DateTime.fromMillis(schedule.start.getTime()).setZone(i18n.timezone).toFormat(i18n.timeFormat)
            );
            rangeDateHtml.push(' ~ ');
            rangeDateHtml.push(
                luxon.DateTime.fromMillis(schedule.end.getTime()).setZone(i18n.timezone).toFormat(i18n.timeFormat)
            );
        }
        // 템플릿 반환
        return `<div class="calendar__cell" data-calendarId="${schedule.calendarId}" data-scheduleId="${schedule.id}">
            <span class="calendar__color--round" style="background-color: ${calendar.bgColor}"></span>
            <span>${rangeDateHtml.join('')}</span>
            <span>${schedule.title}</span>
        </div>`.trim();
    },
    /**
     * viewType에 따라서 조회 날짜 변경
     */
    getDate: function () {
        return luxon.DateTime.fromMillis(this.calendar.getDate().getTime());
    },
    /**
     * 몇 주차인지 가져오기
     * @param {standardDate} luxon 날짜
     */
    getWeekNumberOfMonth: function (date) {
        const monthStart = date.startOf('month');
        const offset = (monthStart.weekday + 1) % 7 - 1; // -1 is for a week starting on Monday
        return Math.ceil((date.day + offset) / 7);
    },
    /**
     * 스케쥴 등록
     * @param {type} 타입 - month|day|week|task
     * @param {schedule} schedule - schedule
     */
    addSchedule: function (type, schedules) {
        // 캘린더 표시
        this.calendar.createSchedules(schedules);
        this.scheduleList = schedules;
        //월 단위로 일정 목록 표시
        if (this.options.viewType === 'task') {
            // 정렬
            this.scheduleList.sort((a, b) => {
                return (a.start.getTime() > b.start.getTime()) ? 1 : (a.start.getTime() === b.start.getTime()) ?
                    ((a.isAllDay > a.isAllDay) ? 1 : -1) : -1;
            });
            this.taskView.querySelectorAll('.calendar__row').forEach((row) => {
                const dayNumber = Number(row.getAttribute('data-day'));
                this.scheduleList.forEach((schedule) => {
                    if (dayNumber === schedule.start.getDate()) {
                        row.insertAdjacentHTML('beforeend', this.getTaskViewCellTemplate(schedule));
                    }
                });
            });
            // 셀 클릭이벤트 추가 - 상세 모달 호출
            this.taskView.querySelectorAll('.calendar__cell').forEach((cell) => {
                cell.addEventListener('click', (e) => {
                    const elem = aliceJs.clickInsideElement(e, 'calendar__cell');
                    const scheduleId = elem.getAttribute('data-scheduleId');
                    const calendarId = elem.getAttribute('data-calendarId');
                    const schedule = this.calendar.getSchedule(scheduleId, calendarId);
                    if (schedule) {
                        this.setDetailModal(schedule);
                        this.setModalPosition(this.detailModal.wrapper, elem.querySelector('.calendar__color--round'));
                        // 모달 표시
                        this.detailModal.show();
                    }
                }, false);
            });

        }
    },
    /**
     * 캘린더에 등록된 모든 일정을 지운다.
     */
    clear:  function () {
        this.scheduleList.length = 0;
        this.calendar.clear();
        if (this.options.viewType === 'task') {
            this.setTaskView();
        }
    },
    /**
     * 메뉴 선택에 따른 처리
     * @param {action} 타입 - prev|next|today
     * @param {callback} 콜백함수
     */
    onClickCalendarMenu: function (action, callback) {
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
        // 일정 보기일 경우 목록 변경
        if (this.options.viewType === 'task') {
            this.setTaskView();
        }

        this.setRenderRangeText();

        if (typeof callback === 'function') {
            callback(this.calendar);
        }
    },
    /**
     * 종일 여부 선택에 따른 처리
     */
    onToggleAllDay: function () {
        const format = this.createModal.customOptions.isAllDay ? i18n.dateTimeFormat : i18n.dateFormat; // 날짜 포맷
        const rangeDate = this.createModal.wrapper.querySelector('#rangeDate');

        const newStart = luxon.DateTime.fromMillis(this.createModal.customOptions.tempStart)
            .setZone(i18n.timezone).toFormat(format);
        const newEnd = luxon.DateTime.fromMillis(this.createModal.customOptions.tempEnd)
            .setZone(i18n.timezone).toFormat(format)

        // 시작일시 , 종료일시 초기화
        this.createModal.wrapper.querySelectorAll('.z-picker-wrapper-date').forEach((dt) => {
            dt.remove();
        });

        rangeDate.insertAdjacentHTML('afterbegin',
            `<input type="text" class="z-input i-datetime-picker col-3" id="endDt" value="${newEnd}"/>`);
        rangeDate.insertAdjacentHTML('afterbegin',
            `<input type="text" class="z-input i-datetime-picker col-3" id="startDt" value="${newStart}"/>`);

        // 종일 선택시, datePicker 를 사용하고 나머지는 dateTimePicker 사용
        const newStartDt = this.createModal.wrapper.querySelector('#startDt');
        const newEndDt = this.createModal.wrapper.querySelector('#endDt');

        // 라이브러리 설정
        if (!this.createModal.customOptions.isAllDay) {
            zDateTimePicker.initDatePicker(newStartDt, this.onUpdateRangeDateTime.bind(this));
            zDateTimePicker.initDatePicker(newEndDt, this.onUpdateRangeDateTime.bind(this));
        } else {
            zDateTimePicker.initDateTimePicker(newStartDt, this.onUpdateRangeDateTime.bind(this));
            zDateTimePicker.initDateTimePicker(newEndDt, this.onUpdateRangeDateTime.bind(this));
        }

        this.createModal.customOptions.isAllDay = !this.createModal.customOptions.isAllDay;
    },
    /**
     * 반복 여부 변경에 따른 처리
     */
    onToggleRepeat: function () {
        const repeatYn = this.createModal.wrapper.querySelector('#repeatYn');
        const repeatDetail = this.createModal.wrapper.querySelector('#repeatDetail');
        repeatDetail.style.visibility = repeatYn.checked ? 'visible' : 'hidden';
    },
    /**
     * 시작일시, 종료일시 변경에 따른 처리
     */
    onUpdateRangeDateTime: function (e, picker) {
        const format = this.createModal.customOptions.isAllDay ? i18n.dateFormat : i18n.dateTimeFormat; // 날짜 포맷
        const convertTime = this.createModal.customOptions.isAllDay ? e.value : i18n.systemHourType(e.value);
        if (e.id === 'startDt') {
            // 시작일시는 종료일시보다 크면 안된다.
            const endDt = this.createModal.wrapper.querySelector('#endDt');
            const isValidStartDt = this.createModal.customOptions.isAllDay ? i18n.compareSystemDate(e.value, endDt.value)
                : i18n.compareSystemDateTime(e.value, endDt.value);
            if (!isValidStartDt) {
                zAlert.warning(i18n.msg('common.msg.selectBeforeDateTime', endDt.value), () => {
                    e.value = luxon.DateTime.fromMillis(this.createModal.customOptions.tempStart)
                        .setZone(i18n.timezone).toFormat(format);
                    picker.open();
                });
                return false;
            }
            this.createModal.customOptions.tempStart = luxon.DateTime.fromFormat(convertTime, format,
                {zone: i18n.timezone}).toMillis();
            // 시작일시 변경시 반복여부 설정 시간도 변경된다.
            this.setRepeatType(this.createModal.customOptions);
        } else {
            // 종료일시는 시작일시보다 작으면 안된다.
            const startDt = this.createModal.wrapper.querySelector('#startDt');
            const isValidEndDt = this.createModal.customOptions.isAllDay ? i18n.compareSystemDate(startDt.value, e.value)
                : i18n.compareSystemDateTime(startDt.value, e.value);
            if (!isValidEndDt) {
                zAlert.warning(i18n.msg('common.msg.selectAfterDateTime', startDt.value), () => {
                    e.value = luxon.DateTime.fromMillis(this.createModal.customOptions.tempEnd)
                        .setZone(i18n.timezone).toFormat(format);
                    picker.open();
                });
                return false;
            }
            this.createModal.customOptions.tempEnd = luxon.DateTime.fromFormat(convertTime, format,
                {zone: i18n.timezone}).toMillis();
        }
    },
    /**
     * 스케쥴 저장
     * @param {schedule} schedule - schedule
     */
    saveSchedule: function (schedule) {
        // 스케쥴 필수값 저장
        if (isEmpty('scheduleTitle', 'common.label.title')) return false;

        const method = (schedule.mode === 'register') ? 'POST' : 'PUT';
        const calendarId = this.createModal.wrapper.querySelector('#calendarId').value;
        const repeatYn = this.createModal.wrapper.querySelector('#repeatYn').checked; // 반복 여부 설정
        const raw = ( schedule.mode === 'edit' && schedule.raw !== null) ? schedule.raw : {};
        const repeatId = Object.prototype.hasOwnProperty.call(raw, 'repeatId') ? raw.repeatId : '';
        const repeatType = this.createModal.wrapper.querySelector('#repeatType');
        const start = luxon.DateTime.fromMillis(schedule.tempStart, {zone: i18n.timezone});
        const end = luxon.DateTime.fromMillis(schedule.tempEnd, {zone: i18n.timezone});
        const saveData = {
            index: Object.prototype.hasOwnProperty.call(raw, 'repeatSeq') ? raw.repeatSeq : 1,
            title: this.createModal.wrapper.querySelector('#scheduleTitle').value,
            contents: this.createModal.wrapper.querySelector('#scheduleContents').value,
            startDt: (schedule.isAllDay) ? start.startOf('day').setZone('utc+0').toISO() :
                start.setZone('utc+0').toISO(),
            endDt: (schedule.isAllDay) ? end.endOf('day').setZone('utc+0').toISO() :
                end.setZone('utc+0').toISO(),
            allDayYn: schedule.isAllDay,
            repeatYn: repeatYn,
            repeatType: repeatYn ?  repeatType.options[repeatType.selectedIndex].value : '',
            repeatValue: repeatType.options[repeatType.selectedIndex].getAttribute('data-repeatvalue'),
            repeatPeriod: '' // all, today, after
        };

        let url = '/rest/calendars/' + calendarId;
        // 반복 일정일 경우
        if (repeatId !== '' || (repeatId === '' && repeatYn)) {
            url += '/repeat';
            saveData.id = repeatId;
        } else { // 스케쥴 등록일 경우
            url += '/schedule';
            saveData.id = Object.prototype.hasOwnProperty.call(schedule, 'id') ? schedule.id : '';
        }

        // 반복일정 수정여부 확인
        if (method === 'PUT' && repeatId !== '') {
            this.repeatModal.customOptions = { url: url, method: method };
            this.repeatModal.saveData = saveData;
            this.repeatModal.parentModal = this.createModal;
            this.repeatModal.show();
        } else {
            this.restSubmit(method, url, saveData, this.createModal);
        }
    },
    /**
     * drag & drop 시 스케쥴 수정
     * @param {schedule} schedule - schedule
     * @param {changes} changes - 변경된 시작일시, 종료일시
     */
    updateScheduleOnDrop: function (schedule, changes) {
        const method = 'PUT';
        const raw = schedule.raw !== null ? schedule.raw : {};
        const repeatId = Object.prototype.hasOwnProperty.call(raw, 'repeatId') ? raw.repeatId : '';
        const repeatYn = Object.prototype.hasOwnProperty.call(raw, 'repeatYn') ? raw.repeatYn : '';
        const start = luxon.DateTime.fromMillis(changes.start.getTime(), {zone: i18n.timezone})
            .setZone('utc+0').toISO();
        const end = luxon.DateTime.fromMillis(changes.end.getTime(), {zone: i18n.timezone})
            .setZone('utc+0').toISO();
        const saveData = {
            index: Object.prototype.hasOwnProperty.call(raw, 'repeatSeq') ? raw.repeatSeq : 1,
            title: schedule.title,
            contents: schedule.body,
            startDt: start,
            endDt: end,
            allDayYn: schedule.isAllDay,
            repeatYn: repeatYn,
            repeatType: Object.prototype.hasOwnProperty.call(raw, 'repeatType') ? raw.repeatType : '',
            repeatValue: Object.prototype.hasOwnProperty.call(raw, 'repeatValue') ? raw.repeatValue : '',
            repeatPeriod: '' // all, today, after
        };
        let url = '/rest/calendars/' + schedule.calendarId;
        // 반복 일정일 경우
        if (repeatId !== '') {
            url += '/repeat';
            saveData.id = repeatId;
        } else { // 스케쥴 등록일 경우
            url += '/schedule';
            saveData.id = schedule.id;
        }

        // 반복일정 수정여부 확인
        if (repeatId !== '') {
            this.repeatModal.customOptions = { url: url, method: method };
            this.repeatModal.saveData = saveData;
            this.repeatModal.parentModal = null;
            this.repeatModal.show();
        } else {
            this.restSubmit(method, url, saveData, null);
        }
    },
    /**
     * 스케쥴 삭제
     * @param {schedule} schedule - schedule
     */
    deleteSchedule: function (schedule) {
        const method = 'DELETE';
        const raw = schedule.raw !== null ? schedule.raw : {};
        const repeatId = Object.prototype.hasOwnProperty.call(raw, 'repeatId') ? raw.repeatId : '';
        const repeatYn = Object.prototype.hasOwnProperty.call(raw, 'repeatYn') ? raw.repeatYn : '';
        const saveData = {
            index: Object.prototype.hasOwnProperty.call(raw, 'repeatSeq') ? raw.repeatSeq : 1,
            repeatYn: repeatYn,
            repeatPeriod: '' // all, today, after
        };

        let url = '/rest/calendars/' + schedule.calendarId;
        // 반복 일정일 경우
        if (repeatId !== '') {
            url += '/repeat';
            saveData.id = repeatId;
        } else { // 스케쥴 등록일 경우
            url += '/schedule';
            saveData.id = schedule.id;
        }

        // 반복일정 수정여부 확인
        if (repeatId !== '') {
            this.repeatModal.customOptions = { url: url, method: method };
            this.repeatModal.saveData = saveData;
            this.repeatModal.parentModal = this.detailModal;
            this.repeatModal.show();
        } else {
            this.restSubmit(method, url, saveData, this.detailModal);
        }
    },
    /**
     * 데이터 전송
     */
    restSubmit: function (method, url, data, modal) {
        console.log('method', method);
        console.log('url', url);
        console.log('data', data);

        const resultMsg = function () {
            switch (method) {
                case 'POST':
                    return i18n.msg('common.msg.register');
                case 'PUT':
                    return i18n.msg('common.msg.update');
                case 'DELETE':
                    return i18n.msg('common.msg.delete');
            }
        }
        // 전송
        aliceJs.fetchJson(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then((response) => {
            switch (response.status) {
                case aliceJs.response.success:
                    zAlert.success(resultMsg(),  () => {
                        if (modal) {
                            modal.hide();
                            // 부모 모달 닫기
                            if (Object.prototype.hasOwnProperty.call(modal, 'parentModal') &&
                                modal.parentModal) {
                                modal.parentModal.hide();
                            }
                        }
                        // 데이터를 새로 가져옴
                        const menu = document.querySelector('#calendarViewType .z-button-switch.selected');
                        if (menu) {
                            menu.dispatchEvent(new Event('click'))
                        }
                    });
                    break;
                case aliceJs.response.error:
                    zAlert.danger(i18n.msg('common.msg.fail'));
                    break;
                default :
                    break;
            }
        });
    }
});
