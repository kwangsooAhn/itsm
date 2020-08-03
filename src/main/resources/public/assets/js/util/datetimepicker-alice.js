/**
 * @projectDescription DatePicker Library
 *
 * - luxon.js 를 기본으로 datepicker를 그려주므로 반드시 함께 import 한다.
 * - i18n-alice.js 의 사용자 설정에 따른 타임존, 날짜시간 포맷, 언어 등을 사용하므로 반드시 함께 import 한다.
 *
 * @author woodajung
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.dateTimePicker = global.dateTimePicker || {})));
}(this, (function (exports) {
    'use strict';

    const defaultOptions = {
            type: 'DATE', // DATE(default), DATEHOUR, HOUR
            title: 'datepicker.label.date'
    };
    const util = {
        /**
         * input box의 날짜시간 문자열 >  luxon object로 변경 한다.
         * 전달 받은 날짜시간 값이 존재하지 않는다면 현재 시간을 보여준다.
         *
         * @param options 옵션
         */
        getDate: function(options) {
            let rtn = '';
            if (options.value === 'now') {
                rtn = luxon.DateTime.local().setZone(i18n.timezone);
            } else {
                switch (options.type) {
                    case 'DATE':
                        rtn = luxon.DateTime.fromFormat(options.value, i18n.dateFormat, {zone: i18n.timezone}).setZone(i18n.timezone);
                        break;
                    case 'DATEHOUR':
                        rtn = luxon.DateTime.fromFormat(options.value, i18n.dateTimeFormat, {zone: i18n.timezone}).setZone(i18n.timezone);
                        break;
                    case 'HOUR':
                        rtn = luxon.DateTime.fromFormat(options.value, i18n.timeFormat, {zone: i18n.timezone}).setZone(i18n.timezone);
                        break;
                }
            }
            return rtn;
        },
        /**
         * 특정 클래스 이름을 가진 요소 내부를 클릭했는지 확인
         * @param {Object} e 이벤트객체
         * @param {String} className 클래스명
         * @return {Object} 존재하면 객제 반환
         */
        clickInsideElement: function(e, className) {
            let el = e.srcElement || e.target;
            if (el.classList.contains(className)) {
                return el;
            } else {
                while (el = el.parentNode) {
                    if (el.classList && el.classList.contains(className)) {
                        return el;
                    }
                }
            }
            return null;
        }
    };
    const numberRegex = /^[0-9]{1,2}$/; // 숫자 유효성 체크
    // callback event
    const callbackEvent = new CustomEvent('changed');

    /**
     * Picker 객체 생성
     * @param options picker options
     */
    function Picker(options) {
        this.id = options.id;
        this.type = options.type;
        this.target = options.inputEl;

        // 선택된 날짜 초기화
        this.selectLuxon = util.getDate(options);
        this.displayLuxon = this.selectLuxon.plus({ days: 0});

        // 객체 초기화
        let el = document.getElementById(this.id);
        if (el === null) { return; }
        this.el = el;

        this.open = this.open.bind(this);
        this.close = this.close.bind(this);
        this.setPosition = this.setPosition.bind(this);
        this.clickWindow = this.clickWindow.bind(this);
        this.changeTarget = this.changeTarget.bind(this);
        this.prevMonth = this.prevMonth.bind(this);
        this.nextMonth = this.nextMonth.bind(this);
        this.setHour = this.setHour.bind(this);
        this.setMinute = this.setMinute.bind(this);

        // create title
        let pickerTitle = document.createElement('div');
        pickerTitle.className = 'picker-modal-title';
        pickerTitle.textContent = i18n.get(options.title);
        el.appendChild(pickerTitle);

        // create title > close icon
        const imgClose = document.createElement('img');
        imgClose.className = 'close';
        imgClose.addEventListener('click', this.close, false);
        pickerTitle.appendChild(imgClose);

        // create sub title > 시간 날짜 표시
        let pickerSubTitle = document.createElement('div');
        pickerSubTitle.className = 'picker-modal-sub-title';
        el.appendChild(pickerSubTitle);
        this.changeDisplay();

        // create content
        let pickerContent = document.createElement('div');
        pickerContent.className = 'picker-modal-content';
        el.appendChild(pickerContent);

        // create content > date
        let pickerContentDate = document.createElement('div');
        pickerContentDate.className = 'picker-modal-content-date';
        if (this.type === 'DATE' || this.type === 'DATEHOUR') {
            pickerContentDate.classList.add('active');
        }
        pickerContent.appendChild(pickerContentDate);
        this.drawDate();

         // create content > time
        if (this.type === 'HOUR' || this.type === 'DATEHOUR') {
           let pickerContentTime = document.createElement('div');
            pickerContentTime.className = 'picker-modal-content-time';
            pickerContentTime.classList.add('active');
            pickerContent.appendChild(pickerContentTime);
            this.drawTime();
        }

        // create button
        let pickerButton = document.createElement('div');
        pickerButton.className = 'picker-modal-button';
        el.appendChild(pickerButton);

        // create button > cancel
        let buttonCancel = document.createElement('button');
        buttonCancel.type = 'button';
        buttonCancel.innerText = i18n.get('common.btn.cancel');
        buttonCancel.addEventListener('click', this.close, false);
        pickerButton.appendChild(buttonCancel);

        // create button > confirm
        let buttonConfirm = document.createElement('button');
        buttonConfirm.type = 'button';
        buttonConfirm.innerText = i18n.get('common.btn.check');
        buttonConfirm.addEventListener('click', this.changeTarget, false);
        pickerButton.appendChild(buttonConfirm);
     }

    Object.assign(Picker.prototype, {
        // Picker open.
        open: function() {
            if (!this.el.classList.contains('active')) {
                this.el.classList.add('active');
                this.setPosition();
                // Detects the target if it's the picker element, if not, closes the picker
                document.addEventListener('mousedown', this.clickWindow, false);
                window.addEventListener('scroll', this.setPosition, false);
                window.addEventListener('resize', this.setPosition, false);
            }
        },
        // Picker close.
        close: function () {
            if (this.el.classList.contains('active')) {
                this.el.classList.remove('active');
                // reset
                let resetValue = (this.target.value !== '' ? this.target.value : 'now');
                let resetLuxon = util.getDate({type: this.type, value: resetValue});
                if (resetLuxon.valueOf() !== this.selectLuxon.valueOf()) {
                    this.selectLuxon = resetLuxon;
                    this.displayLuxon = this.selectLuxon.plus({ days: 0});
                    if (this.type === 'DATE' || this.type === 'DATEHOUR') {
                        this.drawDate();
                    }
                    if (this.type === 'HOUR' || this.type === 'DATEHOUR') {
                        this.drawTime();
                    }
                    this.changeDisplay();
                }
                // remove event
                document.removeEventListener('mousedown', this.clickWindow, false);
                window.removeEventListener('scroll', this.setPosition, false);
                window.removeEventListener('resize', this.setPosition, false);
            }
        },
        // Picker Position.
        setPosition: function() {
            let rect = this.el.parentNode.getBoundingClientRect(),
                ow = this.el.offsetWidth,
                oh = this.el.offsetHeight,
                x = rect.left + ow,
                _x = rect.left - ow,
                y = rect.top + rect.height + oh,
                _y = rect.top - oh,
                w = window.innerWidth,
                h = window.innerHeight;

            if (x >= w && _x > 0) {
                this.el.style.left = rect.left + rect.width - ow + 'px';
            } else {
                this.el.style.left = rect.left + 'px';
            }

            if (y >= h && _y > 0) {
                this.el.style.top = rect.top - oh - 3 + 'px'; // 3은 간격
            } else {
                this.el.style.top = rect.top + rect.height +  3 + 'px';
            }
        },
        // Date picker 생성 및 초기화 처리.
        drawDate: function() {
            let _this = this;
            const pickerDate = _this.el.querySelector('.picker-modal-content-date');
            pickerDate.innerHTML = '';
            // create month panel
            const monthPanel = document.createElement('div');
            monthPanel.className = 'picker-modal-content-date-months';
            pickerDate.appendChild(monthPanel);

            // prev month
            const imgPrevArrow = document.createElement('img');
            imgPrevArrow.className = 'date-prev';
            imgPrevArrow.addEventListener('click', _this.prevMonth, false);
            monthPanel.appendChild(imgPrevArrow);

            // text
            const currentText = document.createElement('span');
            currentText.className = 'date-text';
            currentText.setAttribute('data-value', _this.selectLuxon.toFormat('yyyyMMdd'));
            currentText.textContent = _this.selectLuxon.toFormat("yyyy-MM"); //  2020-06
            monthPanel.appendChild(currentText);

            // next month
            const imgNextArrow = document.createElement('img');
            imgNextArrow.className = 'date-next';
            imgNextArrow.addEventListener('click', _this.nextMonth, false);
            monthPanel.appendChild(imgNextArrow);

            // create calendar
            const calendarPanel = document.createElement('div');
            calendarPanel.className = 'picker-modal-content-date-calendar';
            pickerDate.appendChild(calendarPanel);

            // Sun ~ Sat
            for (let i = 0; i < 7; i++) {
                const calendarTitle =  document.createElement('div');
                calendarTitle.classList.add('calendar-cell', 'calendar-title');
                calendarTitle.textContent = luxon.DateTime.local().set({weekday: i}).toFormat('ccc');
                calendarPanel.appendChild(calendarTitle);
            }

            let firstDayOfDate = _this.selectLuxon.set({ day: 1 });
            let current_month = firstDayOfDate.month;

            firstDayOfDate = firstDayOfDate.minus({ days: (firstDayOfDate.weekday || 8) });

            for (let i = 0; i < 42; i++) {
                let dd = firstDayOfDate.day;
                let mm = firstDayOfDate.month;

                const calendarCell =  document.createElement('div');
                calendarCell.className = 'calendar-cell';
                calendarCell.setAttribute('data-value', firstDayOfDate.toFormat('yyyyMMdd'));
                calendarCell.textContent = dd;
                if (mm === current_month) { // 현재 월
                    calendarCell.classList.add('active');
                } else if (mm < current_month) {
                    calendarCell.classList.add('prev');
                } else {
                    calendarCell.classList.add('next');
                }
                if (_this.displayLuxon.valueOf() === firstDayOfDate.valueOf()) {
                    calendarCell.classList.add('selected');
                }
                calendarCell.addEventListener('click', function(e) {
                    const elem = e.target;
                    const parentElem = elem.parentNode;
                    const isSelected = elem.classList.contains('selected');
                    if (!isSelected) {
                        const prevSelectDay = parentElem.querySelector('.selected');
                        if (prevSelectDay !== null) {
                            prevSelectDay.classList.remove('selected');
                        }
                        elem.classList.add('selected');
                        let selectedDate = elem.getAttribute('data-value');
                        _this.changeDay({ year: selectedDate.substr(0, 4), month: selectedDate.substr(4, 2), day: selectedDate.substr(6, 2)});
                        // 이전, 이후 날짜 선택시 달력이 변경된다. > 상단 제목을 바뀌었는데 달력이 안바뀌면 이상해서 추가함.
                        if (elem.classList.contains('prev') || elem.classList.contains('next')) {
                            _this.drawDate();
                        }
                    }
                }, false);
                calendarPanel.appendChild(calendarCell);
                firstDayOfDate = firstDayOfDate.plus({ days: 1 });
            }
        },
        // Time picker 생성 및 초기화 처리.
        drawTime: function() {
            let _this = this;
            const pickerTime = _this.el.querySelector('.picker-modal-content-time');
            pickerTime.innerHTML = '';

            const hourFormatArr = i18n.timeFormat.split(':');
            _this.hourFormat = hourFormatArr[0];
            const minuteFormatArr = hourFormatArr[1].split(' ');
            _this.minuteFormat = minuteFormatArr[0];

            // create hour start -------------------------------------------------------------------
            const hourGroup = document.createElement('div');
            hourGroup.className = 'hour-group';
            pickerTime.appendChild(hourGroup);
            // △ 버튼
            const hourArrowUp = document.createElement('img');
            hourArrowUp.classList.add('arrow-up', 'hour-up');
            hourArrowUp.addEventListener('click', _this.changeTime.bind(_this, { hours: 1 }), false);
            hourGroup.appendChild(hourArrowUp);
            // 시간
            let digitHour = document.createElement('input');
            digitHour.type = 'text';
            digitHour.className = 'digit';
            digitHour.id = this.id + '-time-hour';
            digitHour.value = _this.selectLuxon.toFormat(_this.hourFormat);
            digitHour.maxLength = 2;
            digitHour.addEventListener('focusout', _this.setHour, false);
            hourGroup.appendChild(digitHour);
            // ▽ 버튼
            const hourArrowDown = document.createElement('img');
            hourArrowDown.classList.add('arrow-down', 'hour-down');
            hourArrowDown.addEventListener('click', _this.changeTime.bind(_this, { hours: -1 }), false);
            hourGroup.appendChild(hourArrowDown);
            // create hour end ---------------------------------------------------------------------

            // 구분자 :
            const separator = document.createElement('div');
            separator.className = 'separator';
            separator.textContent = ':';
            pickerTime.appendChild(separator);

            // create minute start -----------------------------------------------------------------
            const minuteGroup = document.createElement('div');
            minuteGroup.className = 'minute-group';
            pickerTime.appendChild(minuteGroup);
            // △ 버튼
            const minuteArrowUp = document.createElement('img');
            minuteArrowUp.classList.add('arrow-up', 'minute-up');
            minuteArrowUp.addEventListener('click', _this.changeTime.bind(_this, { minutes: 1 }), false);
            minuteGroup.appendChild(minuteArrowUp);
            // 분
            let digitMinute = document.createElement('input');
            digitMinute.type = 'text';
            digitMinute.className = 'digit';
            digitMinute.id = this.id + '-time-minute';
            digitMinute.value = _this.selectLuxon.toFormat(_this.minuteFormat);
            digitMinute.maxLength = 2;
            digitMinute.addEventListener('focusout', _this.setMinute, false);
            minuteGroup.appendChild(digitMinute);
            // ▽ 버튼
            const minuteArrowDown = document.createElement('img');
            minuteArrowDown.classList.add('arrow-down', 'minute-down');
            minuteArrowDown.addEventListener('click', _this.changeTime.bind(_this, { minutes: -1 }), false);
            minuteGroup.appendChild(minuteArrowDown);
            // create minute end -------------------------------------------------------------------

            // 12 시간제일 경우 am/pm
            if (minuteFormatArr.length === 2) {
                _this.hourType = '12';

                const curHourType = _this.selectLuxon.toFormat(minuteFormatArr[1]);
                const hourType = document.createElement('div');
                hourType.className = 'button-group';
                pickerTime.appendChild(hourType);

                 // create button > am
                let buttonAM = document.createElement('button');
                if (curHourType === 'AM') {
                    buttonAM.classList.add('active');
                    _this.meridiem = 'AM';
                }
                buttonAM.type = 'button';
                buttonAM.id = 'AM';
                buttonAM.innerText = i18n.get('datepicker.btn.am');
                buttonAM.addEventListener('click', function(e) {
                    const elem = e.target; // 선택된 toggle 버튼
                    const parentElem = elem.parentNode;
                    const isActive = elem.classList.contains('active');
                    if (!isActive) {
                        for (let i = 0, len = parentElem.childNodes.length ; i< len; i++) {
                            const child = parentElem.childNodes[i];
                            if (child.id === elem.id) {
                                elem.classList.add('active');
                            } else {
                                child.classList.remove('active');
                            }
                        }
                        _this.changeMeridiem(elem.id);
                    }
                }, false);
                hourType.appendChild(buttonAM);

                // create button > pm
                let buttonPM = document.createElement('button');
                if (curHourType === 'PM') {
                    buttonPM.classList.add('active');
                    _this.meridiem = 'PM';
                }
                buttonPM.type = 'button';
                buttonPM.id = 'PM';
                buttonPM.innerText = i18n.get('datepicker.btn.pm');
                buttonPM.addEventListener('click', function(e) {
                    const elem = e.target; // 선택된 toggle 버튼
                    const parentElem = elem.parentNode;
                    const isActive = elem.classList.contains('active');
                    if (!isActive) {
                        for (let i = 0, len = parentElem.childNodes.length ; i< len; i++) {
                            const child = parentElem.childNodes[i];
                            if (child.id === elem.id) {
                                elem.classList.add('active');
                            } else {
                                child.classList.remove('active');
                            }
                        }
                        _this.changeMeridiem(elem.id);
                    }
                }, false);
                hourType.appendChild(buttonPM);
            }
        },
        // Date picker 에서 이전 달력 (<) 아이콘 클릭시 이전 달력으로 변경.
        prevMonth: function() {
            this.selectLuxon = this.selectLuxon.plus({ months: -1 });
            this.drawDate();
        },
        // Date picker 에서 이후 달력 (>) 아이콘 클릭시 이후 달력으로 변경.
        nextMonth: function() {
            this.selectLuxon = this.selectLuxon.plus({ months: 1 });
            this.drawDate();
        },
        // Date picker에 표시되는 선택된 날짜 시간을 변경.
        changeDisplay: function() {
            const displayElem = this.el.querySelector('.picker-modal-sub-title');
            displayElem.innerHTML = '';
            if (this.type === 'DATE') {
                displayElem.textContent = this.selectLuxon.toFormat(i18n.dateFormat);
            } else {
                displayElem.textContent = this.selectLuxon.toFormat(i18n.dateTimeFormat);
            }
        },
        // Date picker 에서 특정 날짜 선택시 표시되는 날짜 변경.
        changeDay: function(offset) {
            this.selectLuxon = this.selectLuxon.set(offset);
            this.displayLuxon = this.selectLuxon.plus({ days: 0});

            this.changeDisplay();
        },
        // Date picker 확인 버튼 클릭시 실제 대상 input box의 날짜 시간 값 변경.
        changeTarget: function() {
            switch (this.type) {
                case 'DATE':
                    this.target.value = this.selectLuxon.toFormat(i18n.dateFormat);
                    break;
                case 'DATEHOUR':
                    this.target.value = this.selectLuxon.toFormat(i18n.dateTimeFormat);
                    break;
                case 'HOUR':
                    this.target.value = this.selectLuxon.toFormat(i18n.timeFormat);
                    break;
            }
            this.close();

            this.target.dispatchEvent(callbackEvent); // 정상적으로 값이 변경되었다면 > callback 이벤트 호출
        },
        // 12 시간제를 사용할 경우, AM PM 버튼 클릭시 처리.
        changeMeridiem: function(meridiem) {
            if (meridiem === 'AM') { // -12
                this.selectLuxon = this.selectLuxon.plus({ hours: -12 });
            } else { // PM + 12
                this.selectLuxon = this.selectLuxon.plus({ hours: 12 });
            }
            this.meridiem = meridiem;
            this.changeDisplay();
        },
        // Time picker 에서 위 아래 화살표 아이콘 클릭시 시간 변경.
        changeTime: function(offset) {
            // 기존 시간 , 분
            const selectLuxonHour = this.selectLuxon.hour;
            const selectLuxonMinute = this.selectLuxon.minute;

            let changeTimeOffset = { hour: selectLuxonHour, minute: selectLuxonMinute };
            const changeLuxon = this.selectLuxon.plus(offset);

            // 시간 변경
            if (typeof offset.hours !== 'undefined' && selectLuxonHour !== changeLuxon.hour) {
                changeTimeOffset.hour = changeLuxon.hour;
                const changeLuxonMeridiem = changeLuxon.toFormat('a');
                if (this.hourType === '12' && this.meridiem !== changeLuxonMeridiem) { // 12 시간제일 경우, 시간이 바뀔때 AM PM이 변경되지 않도록 처리
                    if (changeLuxonMeridiem === 'AM') {
                        changeTimeOffset.hour += (12);
                    } else {
                        changeTimeOffset.hour += (-12);
                    }
                }
            }
            // 분 변경
            if (typeof offset.minutes !== 'undefined' && selectLuxonMinute !== changeLuxon.minute) {
                changeTimeOffset.minute = changeLuxon.minute;
            }

            this.selectLuxon = this.selectLuxon.set(changeTimeOffset);

            const hourInput = this.el.querySelector('#' + this.id + '-time-hour');
            hourInput.value = this.selectLuxon.toFormat(this.hourFormat);

            const minuteInput = this.el.querySelector('#' + this.id + '-time-minute');
            minuteInput.value = this.selectLuxon.toFormat(this.minuteFormat);

            this.changeDisplay();
        },
        // Time picker 에서 input box (Hour) 변경시 처리.
        setHour: function() {
            let rtn = false;
            const hourInput = this.el.querySelector('#' + this.id + '-time-hour');
            const inputValue = hourInput.value;
            // 12 시간제 1 ~ 12 까지만 입력가능
            // 24 시간제 0 ~ 23 까지만 입력가능
            if (numberRegex.test(inputValue)) { // 숫자 여부 체크
                if ((this.hourType === '12' && Number(inputValue) >= 1 && Number(inputValue) <= 12) ||
                    (this.hourType === '24' && Number(inputValue) >= 0 && Number(inputValue) <= 23)) {
                    rtn = true;
                }
            }
            if (rtn) {
                if (inputValue.length === 1) {
                    hourInput.value = '0' + Number(inputValue);
                }
                this.selectLuxon = this.selectLuxon.set({ hour: hourInput.value });
                this.changeDisplay();
            } else {
                hourInput.value = this.selectLuxon.toFormat(this.hourFormat);
            }
        },
        // Time picker 에서 input box (Minute) 변경시 처리.
        setMinute: function() {
             let rtn = false;
             const minuteInput = this.el.querySelector('#' + this.id + '-time-minute');
             const inputValue = minuteInput.value;
             // 0 ~ 59 까지 입력가능
             if (numberRegex.test(inputValue)) {
                 if (Number(inputValue) >= 0 && Number(inputValue) <= 59) {
                     rtn = true;
                 }
             }
             if (rtn) {
                 if (inputValue.length === 1) {
                     minuteInput.value = '0' + Number(inputValue) ;
                 }
                 this.selectLuxon = this.selectLuxon.set({ minute: minuteInput.value });
                 this.changeDisplay();
             } else {
                 minuteInput.value = this.selectLuxon.toFormat(this.minuteFormat);
             }
        },
        // Picker 가 오픈된 상태로 Picker 외부를 선택할 경우 닫음.
        clickWindow: function(e) {
            if (!util.clickInsideElement(e, 'picker-modal')) {
                this.close();
            }
        }
    });

    /**
     * Picker 컨테이너 생성 및 초기화 처리.
     *
     * @param targetId Target element id
     * @param options picker options
     */
    function initPicker(targetId, options) {
        let targetElement = document.getElementById(targetId);
        targetElement.setAttribute('oncontextmenu','return false');
        options.value = (targetElement.value !== '' ? targetElement.value : 'now');
        options.inputEl = targetElement;

        // create target element container
        let targetContainer = document.createElement('div');
        targetContainer.className = 'picker-wrapper-date';
        targetElement.parentElement.insertBefore(targetContainer, targetElement.nextSibling);
        targetElement.parentElement.removeChild(targetElement);
        targetContainer.appendChild(targetElement);

        // class add
        targetElement.classList.add('picker-main');

        // create picker container
        const pickerId = 'picker-' + targetId;
        options.id = pickerId;
        let pickerContainer = document.createElement('div');
        pickerContainer.id = pickerId;
        pickerContainer.className = 'picker-modal';
        targetContainer.appendChild(pickerContainer);

        // initialization picker
        let picker = new Picker(options);

        // click event add
        targetElement.addEventListener('click', picker.open, false);
        return picker;
    }

    /**
     * DatePicker 초기화 시 호출(Date Only).
     *
     * @param targetId Target element id
     * @param callback 콜백 함수
     */
    function initDatePicker(targetId, callback) {
        let options = JSON.parse(JSON.stringify(defaultOptions));

        let picker = initPicker(targetId, options);
        picker.target.addEventListener('changed', function() {
            if (typeof callback === 'function') {
                callback(picker.target, picker);
            }
        });
    }

    /**
     * DateTimePicker 초기화 시 호출(include time).
     *
     * @param targetId Target element id
     * @param callback 콜백 함수
     */
    function initDateTimePicker(targetId, callback) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'DATEHOUR';
        options.title = 'datepicker.label.datetime';

        let picker = initPicker(targetId, options);
        picker.target.addEventListener('changed', function() {
            if (typeof callback === 'function') {
                callback(picker.target, picker);
            }
        });
    }

    /**
     * TimePicker 초기화 시 호출(time only).
     *
     * @param targetId Target element id
     * @param callback 콜백 함수
     */
    function initTimePicker(targetId, callback) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'HOUR';
        options.title = 'datepicker.label.hour';

        let picker = initPicker(targetId, options);
        picker.target.addEventListener('changed', function() {
            if (typeof callback === 'function') {
                callback(picker.target, picker);
            }
        });
    }

    exports.initDatePicker = initDatePicker;
    exports.initDateTimePicker = initDateTimePicker;
    exports.initTimePicker = initTimePicker;

    Object.defineProperty(exports, '__esModule', {value: true});
})));
