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
    // 캘린더 종류가 여러개일때 색상을 다르게 표현 가능
    bgColors: ['#E0F3FF', '#FEF1F4', '#F8FDF1', '#FEFDEB', '#FBF0FE', '#E8F9FA', '#F1F5FE', '#F6F8FA'],
    colors: ['#2559A8', '#D448AF', '#5CA21B', '#D7BC20', '#8444CA', '#30A8C0', '#4C64D4', '#63686C'],
    // 이전 일정일 경우 색상
    blurColors: ['#83BBED', '#FCA1C9', '#C0EB7A', '#FDF080', '#D89FF9', '#91EBEB', '#A4BAFC', '#CFD5D9'],
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
        i18n.msg('calendar.label.shortDays.mon'),
        i18n.msg('calendar.label.shortDays.tue'),
        i18n.msg('calendar.label.shortDays.wed'),
        i18n.msg('calendar.label.shortDays.thu'),
        i18n.msg('calendar.label.shortDays.fri'),
        i18n.msg('calendar.label.shortDays.sat'),
        i18n.msg('calendar.label.shortDays.sun')
    ],
    // 반복 안함, 매월 X번째 X요일, 매주 X요일
    repeatTypes: ['none', 'dayOfMonth', 'weekOfMonth'],
    dayOfMonthType: ['', 'first', 'second', 'third', 'fourth', 'last'],
    createModalSize: { w: 450, h: 362 }, // 모달 위치
    detailModalSize: { w: 450, h: 230 }, // 모달 위치
    isReadOnly: false, // 사용자 일정 편집 불가능하게 할 경우 true 로 설정한다.
};

const CALENDAR_DEFAULT_THEME = {
    // 공통
    'common.border': '1px solid #CFD5D9',
    'common.backgroundColor': 'white',
    'common.holiday.color': '#424242',
    'common.saturday.color': '#424242',
    'common.today.color': '#3373C4',
    'common.dayname.color': '#424242',

    // 선택된 날짜
    'common.creationGuide.backgroundColor': 'rgba(51, 154, 240, 0.1)',
    'common.creationGuide.border': '1px solid #3373C4',

    // 월별보기 상단 제목
    'month.dayname.height': '38px',
    'month.dayname.borderLeft': 'none',
    'month.dayname.paddingLeft': '0',
    'month.dayname.paddingRight': '0',
    'month.dayname.backgroundColor': '#F5F5F5',
    'month.dayname.fontSize': '14px',
    'month.dayname.fontWeight': '500', // medium
    'month.dayname.textAlign': 'center',
    'month.day.fontSize': '16px',

    // 월별보기 일정 항목
    'month.schedule.borderRadius': '2px',
    'month.schedule.height': '18px',
    'month.schedule.marginTop': '2px',
    'month.schedule.marginLeft': '8px',
    'month.schedule.marginRight': '8px',

    // more 클릭시 모달
    'month.moreView.border': '1px solid #CFD5D9',
    'month.moreView.boxShadow': '0 2px 12px 0 #CFD5D9',
    'month.moreView.backgroundColor': 'white',
    'month.moreView.paddingBottom': '60px',
    'month.moreViewTitle.height': '65px',
    'month.moreViewTitle.marginBottom': '0',
    'month.moreViewTitle.borderBottom': '1px solid #CFD5D9',
    'month.moreViewTitle.padding': '12px 10px 0 10px',
    'month.moreViewList.padding': '10px',

    'month.holidayExceptThisMonth.color': '#9E9E9E',
    'month.dayExceptThisMonth.color': '#9E9E9E',

    // 주별보기 상단 제목
    'week.dayname.height': '70px',
    'week.dayname.borderTop': '1px solid #CFD5D9',
    'week.dayname.borderBottom': '1px solid #CFD5D9',
    'week.dayname.borderLeft': 'none',
    'week.dayname.paddingLeft': '0',
    'week.dayname.backgroundColor': '#F5F5F5',
    'week.dayname.textAlign': 'center',
    'week.today.color': '#3373C4',
    'week.pastDay.color': '#424242', // 이전 날짜

    // 주별 보기 상단 종일 일정
    'week.daygrid.borderRight': '1px solid #CFD5D9',
    'week.daygrid.backgroundColor': 'inherit',

    // 주별보기 좌측 시간 항목
    'week.daygridLeft.width': '80px',
    'week.daygridLeft.backgroundColor': 'inherit',
    'week.daygridLeft.paddingRight': '0',
    'week.daygridLeft.borderRight': '1px solid #CFD5D9',
    'week.today.backgroundColor': 'inherit',
    'week.weekend.backgroundColor': 'inherit',
    'week.timegridLeft.width': '80px',
    'week.timegridLeft.backgroundColor': 'inherit',
    'week.timegridLeft.borderRight': '1px solid #CFD5D9',
    'week.timegridLeft.fontSize': '14px',

    // 좌측 현재시간
    'week.currentTime.color': '#FFAC47',
    'week.currentTime.fontSize': '14px',
    'week.currentTime.fontWeight': '500',

    // 주별보기 그리드 너비
    'week.timegridOneHour.height': '38px',
    'week.timegridHalfHour.height': '19px',
    'week.timegridHalfHour.borderBottom': 'none',
    'week.timegridHorizontalLine.borderBottom': '1px solid #CFD5D9',
    'week.timegrid.paddingRight': '10px',
    'week.timegrid.borderRight': '1px solid #CFD5D9',
    'week.timegridSchedule.borderRadius': '2px',
    'week.timegridSchedule.paddingLeft': '10px', // 동일 시간대의 일정 항목간 간격

    // 주별보기 가이드라인
    'week.currentTimeLinePast.border': '2px dashed #FFAC47',
    'week.currentTimeLineBullet.backgroundColor': '#FFAC47',
    'week.currentTimeLineToday.border': '2px solid #FFAC47',
    'week.currentTimeLineFuture.border': 'none',

    // 선택된 시간
    'week.creationGuide.color': '#2559A8',
    'week.creationGuide.fontSize': '12px',
    'week.creationGuide.fontWeight': '400', // regular

    // 주별보기 일정 항목
    'week.dayGridSchedule.borderRadius': '2px',
    'week.dayGridSchedule.height': '18px',
    'week.dayGridSchedule.marginTop': '2px',
    'week.dayGridSchedule.marginLeft': '8px',
    'week.dayGridSchedule.marginRight': '8px'

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
    // 월 데이터가 변경되지 않는 다면 다시 데이터를 가져오지 않는다.
    // (버튼 클릭시 주, 일 반복 일정을 서버에서 계산하기 때문에 조회 수를 줄여서 부하를 낮추기 위함)
    this.isReload = false; // 데이터 재조회 여부 체크

    // 오늘 날짜
    this.today = luxon.DateTime.local().setZone(i18n.timezone).valueOf();
    
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
            // 배경 색상 제거
            modal.wrapper.querySelector('.modal-backdrop').style.backgroundColor = 'transparent';
            // select 디자인
            aliceJs.initDesignedSelectTag(document.querySelector('.calendar__modal--register__main'));
            // 종일 여부 변경시 이벤트 추가
            document.getElementById('allDayYn').addEventListener('change', this.onToggleAllDay.bind(this));
            // 이벤트 추가
            zDateTimePicker.initDateTimePicker(document.getElementById('startDt'),
                this.onUpdateRangeDateTime.bind(this), { isHalf: true });
            zDateTimePicker.initDateTimePicker(document.getElementById('endDt'),
                this.onUpdateRangeDateTime.bind(this), { isHalf: true });
            // 스크롤바
            OverlayScrollbars(document.querySelector('.textarea-scroll-wrapper'), {
                className: 'inner-scrollbar',
                resize: 'vertical',
                sizeAutoCapable: true,
                textarea: {
                    dynHeight: false,
                    dynWidth: false,
                    inheritedAttrs: 'class'
                }
            });
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
            // 배경 색상 제거
            modal.wrapper.querySelector('.modal-backdrop').style.backgroundColor = 'transparent';
        },
        onShow: (modal) => {
            // 이벤트 추가
            this.bindScheduleEdit = this.onClickScheduleEdit.bind(this, modal);
            modal.wrapper.querySelector('#scheduleEdit').addEventListener('click', this.bindScheduleEdit);

            this.bindScheduleDelete = this.onClickScheduleDelete.bind(this, modal);
            modal.wrapper.querySelector('#scheduleDelete').addEventListener('click', this.bindScheduleDelete);
        },
        onHide: (modal) => {
            modal.wrapper.querySelector('#scheduleEdit').removeEventListener('click', this.bindScheduleEdit);
            modal.wrapper.querySelector('#scheduleDelete').addEventListener('click', this.bindScheduleDelete);
        }
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
        theme: CALENDAR_DEFAULT_THEME,
        taskView: false,
        template: {
            // 시간 단위 등록된 일정 템플릿
            time:(schedule) => {
                const start = luxon.DateTime.fromMillis(schedule.start.getTime()).setZone(i18n.timezone);
                let html = [];
                html.push(start.toFormat(i18n.timeFormat));
                html.push(' ' + schedule.title);
                return `<span class="tui-full-calendar-weekday-schedule-title-time">${html.join('')}</span>`;
            },
            // 월별 달력 상단 문구 템플릿
            monthDayname:(dayName) => {
                let dayOfWeek = dayName.day - 1;
                if (dayOfWeek === -1) {
                    dayOfWeek = 6;
                }
                return `<span class="tui-full-calendar-month-dayname-name">${this.options.shortDays[dayOfWeek]}</span>`;
            },
            // more 모달 템플릿
            monthMoreTitleDate:(date, dayName) => {
                const day = date.split('.')[2];
                return `<span class="tui-full-calendar-month-more-title-day-label">
                ${i18n.msg('calendar.label.shortDays.' + dayName.toLowerCase())}
                </span><br/><span class="tui-full-calendar-month-more-title-day">${day}</span>`.trim();
            },
            monthMoreClose:() => {
                return `${i18n.msg('common.btn.close')}`;
            },
            // 월별 달력 날짜 템플릿
            monthGridHeader:(dayModel) => {
                const date = parseInt(dayModel.date.split('-')[2], 10);
                let classNames = ['tui-full-calendar-weekday-grid-date'];
                if (dayModel.isToday) {
                    classNames.push('underline');
                    classNames.push('tui-full-calendar-weekday-grid-date-decorator');
                }
                return `<span class="${classNames.join(' ')}" data-date="${dayModel.date}">`
                    + `${date}</span>`;
            },
            // 주간 달력 상단 문구 테플릿
            weekDayname:(dayModel) => {
                let classNames = ['tui-full-calendar-dayname-date'];
                if (dayModel.isToday) {
                    classNames.push('underline');
                    classNames.push('tui-full-calendar-dayname-date-decorator');
                }

                return `<span class="tui-full-calendar-dayname-name">` +
                `${i18n.msg('calendar.label.shortDays.' + dayModel.dayName.toLowerCase())}</span>` +
                `<span class="${classNames.join(' ')}">${dayModel.date}</span>`;
            },
            // 주간, 일간 달력 이름 템플릿
            dayGridTitle:(viewName) => {
                return viewName !== 'allday' ? `` :
                    `<span class="tui-full-calendar-center-content">${i18n.msg('calendar.label.weekAllDay')}</span>`;
            },
            // 주간, 일간 달력 좌측 시간 포맷 템플릿
            timegridDisplayPrimaryTime:(time) => {
                const isHour12 = (i18n.timeFormat !== 'HH:mm');
                let hour = time.hour;
                let meridian = '';
                if (isHour12) { // 12 시간제
                    if (time.hour > 12) {
                        hour = time.hour - 12;
                        meridian = 'PM';
                    } else {
                        meridian = 'AM';
                    }
                }
                if ((hour + '').length === 1) {
                    hour = '0' + hour;
                }
                return `<span>${hour + ':00 ' + meridian}</span>`;
            },
            // 현재 날짜
            timegridCurrentTime:(timezone) => {
                return luxon.DateTime.fromMillis(timezone.hourmarker.getTime()).setZone(i18n.timezone)
                .toFormat(i18n.timeFormat);
            }
        },
        calendars: [],
        useCreationPopup: false, // 등록 팝업 사용 거부 - 커스텀 팝업 사용 예정
        useDetailPopup: false, // 상세 팝업 사용 거부 - 커스텀 팝업 사용 예정
        isReadOnly: this.options.isReadOnly
    });

    // 이벤트 등록
    this.calendar.on({
        // more 버튼 클릭
        clickMore: (e) => {
            const curDate = luxon.DateTime.fromMillis(e.date.getTime()).setZone(i18n.timezone);
            const moreModal = e.target.parentNode;
            moreModal.style.width = 'auto';
            moreModal.style.height = 'auto';
            
            // 위치 조정
            const moreModalGuide= e.target.getBoundingClientRect();
            const h = window.innerHeight,
                y = moreModalGuide.top + moreModalGuide.height;
            if (y >= h) {
                const guideElement = document.querySelector('.tui-full-calendar-weekday-grid-date[data-date="' +
                    curDate.toFormat('yyyy-MM-dd') +'"]');
                moreModal.style.top =  guideElement.getBoundingClientRect().top
                    - this.el.getBoundingClientRect().top - moreModal.offsetHeight + 'px';
            }

            // 삭제 버튼 위치 조정
            const closeButton = e.target.querySelector('.tui-full-calendar-month-more-close');
            closeButton.classList.add('z-button', 'secondary');
            closeButton.style.top = moreModalGuide.height - 47 + 'px'; // 47 = button 높이 + 하단 여백
            closeButton.style.right = 13 + 'px'; // 우측 여백

            // 색상 조정
            const isPrev = curDate.valueOf() < this.today;
            e.target.querySelectorAll('.tui-full-calendar-month-more-schedule').forEach((s) => {
                const schedule = this.calendar.getSchedule(s.getAttribute('data-schedule-id')
                    , s.getAttribute('data-calendar-id'));
                if (schedule) {
                    this.setDesignedSchedule(s, schedule, isPrev);
                }
            });
        },
        // 등록
        beforeCreateSchedule: (e) => {
            // 초기화
            e.mode = 'register';
            this.createModal.customOptions = e;
            this.setCreateModal(this.createModal.customOptions);

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
        },
        // 스케쥴을 그려준 후 동그란 아이콘 색상 변경 필요 (UX팀에서 전달받은 색상으로 표현해줘야함)
        afterRenderSchedule: (e) => {
            const schedule = e.schedule;
            // 현재시간보다 이전 스케쥴일 경우 색상 변경
            const isPrev = luxon.DateTime.fromMillis(schedule.end.getTime()).setZone(i18n.timezone).valueOf() <
                this.today;
            const element = this.calendar.getElement(schedule.id, schedule.calendarId);
            this.setDesignedSchedule(element, schedule, isPrev);
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
            const mainColor = this.options.bgColors[idx];
            result.push({
                id: data.calendarId,
                name: data.calendarName,
                type: data.calendarType, // 'user' | 'document'
                color: this.options.colors[idx],
                bgColor: mainColor,
                borderColor: mainColor,
                dragBgColor: this.options.blurColors[idx]
            });
            return result;
        }, []);

        this.calendar.setCalendars(this.calendarList);

        // 사용자별 일정 등록 기능 추가시 보이도록 처리하여 사용한다.
        const calendarList = this.createModal.wrapper.querySelector('#calendarList');
        const calendarOptionTemplate = this.calendarList.map((opt, idx) => {
            if (opt.type !== 'document') { // 문서함에서 등록된 일정은 편집 불가하다.
                return `<option value="${opt.id}" ${idx === 0 ? 'selected=\'true\'' : ''}>${opt.name}</option>`;
            }
        });
        calendarList.innerHTML = '';
        calendarList.insertAdjacentHTML('beforeend',
            `<select id="calendarId">${calendarOptionTemplate}</select>`);

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
            this.isReload = true;
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
            // 주
            let weekDay = (( Number(firstWeekDay) + i ) % 7) - 1;
            if (weekDay === -1) { weekDay = 6; }

            let classNames = ['calendar__rowGroup'];
            const isToday = start.day === (i + 1);
            if (isToday) {
                classNames.push('today');
            }

            const isPrev = start.day > (i + 1);
            if (isPrev) {
                classNames.push('prev');
            }

            const rowContentTemplate = `<div class="calendar__rowHead">
                <span class="calendar__day${isToday ? ' underline' : ''}">${i + 1}</span>
                <span class="calendar__weekDay">${start.monthShort}, ${this.options.shortDays[weekDay]}</span>
            </div>
            <div class="calendar__row" data-day="${i + 1}"></div>`.trim();

            taskViewTemplate += `<div class="${classNames.join(' ')}">${rowContentTemplate}</div>`;
        }
        this.taskView.insertAdjacentHTML('beforeend', taskViewTemplate);

        // 등록 이벤트 추가
        // 셀 클릭이벤트 추가 - 상세 모달 호출
        this.taskView.querySelectorAll('.calendar__row').forEach((row) => {
            row.addEventListener('click', (e) => {
                e.stopPropagation();
                if (aliceJs.clickInsideElement(e, 'calendar__cell')) { return false; }
                // 데이터 설정
                const day = e.target.getAttribute('data-day');
                const now = luxon.DateTime.local().setZone(i18n.timezone);
                const startDt = luxon.DateTime.local(start.year, start.month, Number(day), now.hour)
                    .startOf('hour').plus({ hour: 1 });
                const endDt = startDt.plus({ hour: 1 });
                const schedule = {
                    mod: 'register',
                    isAllDay: false,
                    start: startDt.toJSDate(),
                    end: endDt.toJSDate(),
                };
                this.createModal.customOptions = schedule;
                this.setCreateModal(this.createModal.customOptions);

                // 위치 조정
                this.setModalPosition(this.createModal.wrapper, e.target);

                // 모달 표시
                this.createModal.show();
            }, false);
        });
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

        const raw = ( schedule.mode === 'edit' && schedule.raw !== null) ? schedule.raw : {};
        const repeatType = Object.prototype.hasOwnProperty.call(raw, 'repeatType') && raw.repeatType !== '' ?
            raw.repeatType : 'none';
        // 반복 안함, 매월 X번째 X요일, 매주 X요일
        const repeatTypeOptionTemplate = repeatTypeOptions.map((opt, idx) => {
            return `<option value="${opt.id}" ${opt.id === repeatType ? 'selected=\'true\'' : ''} `
                + ` data-repeatValue="${opt.value}">${opt.text}</option>`;
        }).join('');

        parent.insertAdjacentHTML('beforeend',
            `<select class="schedule__repeat" id="repeatType">${repeatTypeOptionTemplate}</select>`);

        aliceJs.initDesignedSelectTag(parent);
    },
    /**
     * 스케쥴 등록|수정 모달 초기화
     * @param {schedule} schedule - schedule
     */
    setCreateModal: function (schedule) {
        // 모달 버튼명 변경
        this.createModal.wrapper.querySelector('.modal-button').textContent = (schedule.mode === 'register') ?
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

        // 선택된 날짜에 따른 반복 일정 값 변경
        this.setRepeatType(schedule);

        // 종일 여부
        const allDayYn = this.createModal.wrapper.querySelector('#allDayYn');
        if (schedule.mode === 'register') { // 등록일 경우 기본값은 종일여부
            schedule.isAllDay = false;
            allDayYn.checked = true;
        }
        if (allDayYn.checked !== schedule.isAllDay) {
            if (schedule.mode === 'edit') {
                allDayYn.checked = schedule.isAllDay;
            }
            allDayYn.dispatchEvent(new Event('change'));
        }
    },
    /**
     * 스케쥴 등록|수정|상세보기 모달 위치 조정
     * @param {modal} 모달
     * @param {guide} 선택된 캘린더의 날짜 rect
     */
    setModalPosition: function (modal, guide) {
        const modalDialog = modal.querySelector('.modal-dialog') || modal,
            guideBound = guide.getBoundingClientRect(),
            modalSize = {
                w: Number(modal.style.getPropertyValue('--data-modal-width') || modal.offsetWidth),
                h: Number(modal.style.getPropertyValue('--data-modal-height') || modal.offsetHeight)
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
        rangeDateHtml.push('<span class="schedule__repeat">');
        // 반복 : (매주 X요일) | (매월 X번째 X요일)
        if (schedule.raw.repeatYn) {
            const dayOfMonthNumber = this.getWeekNumberOfMonth(start);
            rangeDateHtml.push(this.getRepeatTypeOptionText(schedule.raw.repeatType, start.weekday, dayOfMonthNumber));
        } else {
            rangeDateHtml.push(i18n.msg('calendar.label.none'));
        }
        rangeDateHtml.push('</span>');
        rangeDate.innerHTML = rangeDateHtml.join('');

        // 내용
        const scheduleContents = this.detailModal.wrapper.querySelector('#detailScheduleContents');
        scheduleContents.textContent = schedule.body;
        // 작성자
        const ownerName = this.detailModal.wrapper.querySelector('#detailOwnerName');
        ownerName.textContent = schedule.raw.ownerName;

        // 문서함에서 등록된 일정일 경우 편집 불가능
        this.detailModal.wrapper.querySelector('#scheduleEdit').style.visibility =
            (schedule.raw.calendarType === 'document') ? 'hidden' : 'visible';
        this.detailModal.wrapper.querySelector('#scheduleDelete').style.visibility =
            (schedule.raw.calendarType === 'document') ? 'hidden' : 'visible';
    },
    /**
     * 이전 스케쥴 디자인 추가
     * @param {element} target - 대상 엘리먼트
     * @param {schedule} schedule - schedule
     * @param {boolean} isPrev - 오늘보다 이전인지 여부
     */
    setDesignedSchedule: function (target, schedule, isPrev) {
        if (schedule.isAllDay) { // 종일
            if (isPrev) {
                target.style.color = schedule.dragBgColor;
            }
        } else {
            if (isPrev) {
                target.classList.add('prev');

                if (target.classList.contains('tui-full-calendar-time-schedule')) {
                    target.style.color = schedule.dragBgColor;
                }
            }

            const bullet = target.querySelector('.tui-full-calendar-weekday-schedule-bullet');
            if (bullet) {
                bullet.style.backgroundColor = isPrev ? schedule.dragBgColor : schedule.color;
            }

            const title = target.querySelector('.tui-full-calendar-weekday-schedule-title');
            if (title) {
                title.style.color = 'inherit';
            }
        }
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
            <input type="text" class="z-input schedule__title" id="scheduleTitle" 
                placeholder="${i18n.msg('calendar.label.titlePlaceholder')}">
            <div class="flex-row align-items-baseline" id="rangeDate">
                <input type="text" class="z-input i-datetime-picker schedule__date" id="startDt"/> 
                ~
                <input type="text" class="z-input i-datetime-picker schedule__date" id="endDt"/> 
            </div>
            <div class="flex-row align-items-baseline">
                <label class="z-checkbox">
                    <input type="checkbox" id="allDayYn" name="allDayYn">
                    <span></span>
                    <span class="z-label">${i18n.msg('calendar.label.allDay')}</span>
                </label>
                <div id="repeatDetail">
                    <select class="schedule__repeat" id="repeatType">${repeatTypeOptionTemplate}</select>
                </div>
            </div>
            <textarea class="z-textarea textarea-scroll-wrapper schedule__contents" id="scheduleContents" rows="3"
                      placeholder="${i18n.msg('calendar.label.contentsPlaceholder')}"></textarea>
            <div class="flex-row" id="calendarList"></div>
        </div>`.trim();
    },
    /**
     * 스케쥴 상세보기 모달 템플릿
     */
    getDetailModalTemplate: function () {
        return `<div class="calendar__modal--detail__main flex-column">
            <div class="flex-row">
                <span class="schedule__title text-ellipsis" id="detailScheduleTitle"></span>
                <div class="z-button-list flex-row float-right align-items-end">
                    <button type="button" class="z-button-icon secondary" id="scheduleEdit">
                        <span class="z-icon i-edit"></span>
                    </button>
                    <button type="button" class="z-button-icon secondary" id="scheduleDelete">
                        <span class="z-icon i-delete"></span>
                    </button>
                </div>
            </div>
            <span class="schedule__date flex-column" id="detailRangeDate"></span>
            <span class="schedule__contents" id="detailScheduleContents"></span>
            <span class="schedule__owner" id="detailOwnerName"></span>
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
     * @param {isPrev} isPrev - 이전 데이터 여부
     */
    getTaskViewCellTemplate: function (schedule, isPrev) {
        const calendar = this.calendarList.find((item) => item.id ===schedule.calendarId);
        const roundColor = isPrev ? calendar.dragBgColor : calendar.color;
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
            <span class="calendar__color--round" style="background-color: ${roundColor}"></span>
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
     * viewType에 따라서 조회 날짜 변경
     */
    getStandardDate: function (viewType, date) {
        switch (viewType) {
            case 'month': // 2022-05
            case 'task':
                return date.toFormat('yyyy-MM');
            case 'week': // 2022-05-13
                return date.toFormat('yyyy-MM-dd');
            case 'day': // 2022-05-13
                return date.toFormat('yyyy-MM-dd');
            default:
                break;
        }
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
        this.scheduleList = schedules;
        this.calendar.createSchedules(this.scheduleList);
        //월 단위로 일정 목록 표시
        if (this.options.viewType === 'task') {
            // 정렬
            this.scheduleList.sort((a, b) => {
                return (a.start.getTime() > b.start.getTime()) ? 1 : (a.start.getTime() === b.start.getTime()) ?
                    ((a.isAllDay > a.isAllDay) ? 1 : -1) : -1;
            });
            this.taskView.querySelectorAll('.calendar__row').forEach((row) => {
                const dayNumber = Number(row.getAttribute('data-day'));
                const isPrev = row.parentNode.classList.contains('prev');
                this.scheduleList.forEach((schedule) => {
                    if (dayNumber === schedule.start.getDate()) {
                        row.insertAdjacentHTML('beforeend', this.getTaskViewCellTemplate(schedule, isPrev));
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
        this.isReload = false;
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
    onToggleAllDay: function (e) {
        const format = e.target.checked ? i18n.dateFormat : i18n.dateTimeFormat; // 날짜 포맷
        const rangeDate = this.createModal.wrapper.querySelector('#rangeDate');

        const newStart = luxon.DateTime.fromMillis(this.createModal.customOptions.tempStart)
            .setZone(i18n.timezone).toFormat(format);
        const newEnd = luxon.DateTime.fromMillis(this.createModal.customOptions.tempEnd)
            .setZone(i18n.timezone).toFormat(format)

        // 시작일시 , 종료일시 초기화
        rangeDate.innerHTML = '';
        const dateTemplate = ` <input type="text" class="z-input i-datetime-picker schedule__date" id="startDt" 
            value="${newStart}"/>~<input type="text" class="z-input i-datetime-picker schedule__date" id="endDt" 
            value="${newEnd}"/>`.trim();

        rangeDate.insertAdjacentHTML('beforeend',dateTemplate);

        // 종일 선택시, datePicker 를 사용하고 나머지는 dateTimePicker 사용
        const newStartDt = this.createModal.wrapper.querySelector('#startDt');
        const newEndDt = this.createModal.wrapper.querySelector('#endDt');

        // 라이브러리 설정
        if (e.target.checked) {
            zDateTimePicker.initDatePicker(newStartDt, this.onUpdateRangeDateTime.bind(this));
            zDateTimePicker.initDatePicker(newEndDt, this.onUpdateRangeDateTime.bind(this));
        } else {
            zDateTimePicker.initDateTimePicker(newStartDt, this.onUpdateRangeDateTime.bind(this), { isHalf: true });
            zDateTimePicker.initDateTimePicker(newEndDt, this.onUpdateRangeDateTime.bind(this), { isHalf: true });
        }

        this.createModal.customOptions.isAllDay = e.target.checked;
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
     * 상세보기 모달에서 편집 버튼 클릭시 처리
     */
    onClickScheduleEdit: function (modal) {
        // 수정 모달 호출
        const schedule = modal.customOptions.schedule;
        schedule.mode = 'edit';

        this.createModal.customOptions = schedule;
        this.setCreateModal(this.createModal.customOptions);
        this.setModalPosition(this.createModal.wrapper, modal.customOptions.event.target);

        // 기존 모달 닫고 편집 모달 띄우기
        modal.hide();
        this.createModal.show();
    },
    /**
     * 상세보기 모달에서 삭제 버튼 클릭시 처리
     */
    onClickScheduleDelete: function (modal) {
        this.deleteSchedule(modal.customOptions.schedule);
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
        const raw = ( schedule.mode === 'edit' && schedule.raw !== null) ? schedule.raw : {};
        const repeatId = Object.prototype.hasOwnProperty.call(raw, 'repeatId') ? raw.repeatId : '';
        const repeatType = this.createModal.wrapper.querySelector('#repeatType');
        const repeatYn = !(repeatType.value === 'none'); // 반복 여부 설정

        const start = luxon.DateTime.fromMillis(schedule.tempStart, {zone: i18n.timezone});
        const end = luxon.DateTime.fromMillis(schedule.tempEnd, {zone: i18n.timezone});
        const saveData = {
            dataId: Object.prototype.hasOwnProperty.call(raw, 'dataId') ? raw.dataId : '',
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
        // 일반(신규) -> 일반 = '/schedule'
        // 일반(신규) -> 반복 = '/repeat'
        // 일반 -> 일반,반복 = '/schedule'
        // 반복 -> 일반,반복 = '/repeat'
        if ((repeatId === '' && repeatYn && method === 'POST') || repeatId !== '' ) {
            url += '/repeat';
            saveData.id = repeatId;
        } else { // 스케쥴 등록일 경우
            url += '/schedule';
            saveData.id = Object.prototype.hasOwnProperty.call(schedule, 'id') ? schedule.id : '';
        }

        // 반복일정 수정여부 확인
        if (method === 'PUT' && repeatId !== '' && repeatYn) {
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
            dataId: Object.prototype.hasOwnProperty.call(raw, 'dataId') ? raw.dataId : '',
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
        // 일반 -> 일반,반복 = '/schedule'
        // 반복 -> 일반,반복 = '/repeat'
        if (repeatId !== '' ) {
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
        const start = luxon.DateTime.fromMillis(schedule.start.getTime(), {zone: i18n.timezone})
            .setZone('utc+0').toISO();
        const saveData = {
            dataId: Object.prototype.hasOwnProperty.call(raw, 'dataId') ? raw.dataId : '',
            index: Object.prototype.hasOwnProperty.call(raw, 'repeatSeq') ? raw.repeatSeq : 1,
            repeatYn: repeatYn,
            repeatPeriod: '', // all, today, after
            startDt: start // 현재 날짜 데이터 - 반복 일정 삭제시 오늘 날짜 필요
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
            body: JSON.stringify(data),
            showProgressbar: true
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
                        // 삭제시 화면 reload
                        if (method === 'DELETE') {
                            location.reload();
                        }
                        // 데이터를 새로 가져옴
                        this.isReload = true;
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
