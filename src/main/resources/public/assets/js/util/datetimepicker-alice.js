const dateTimePicker = (function() {
    'use strict';

    const defaultOptions = {
        inputToggle: true,
        type: 'DATE', // DATE(default), DATEHOUR, HOUR
        dateType: 'YYYY-MM-DD', // YYYY-MM-DD(default), YYYY-DD-MM, DD-MM-YYYY, MM-DD-YYYY
        hourType: '24', // 12(default), 24
        lang: 'en' //en(default), ko, ja
    }

    /**
     * DateTimePicker 컨테이너 생성 및 초기화 처리.
     *
     * @param targetId Target element id
     * @param options picker options
     */
    function initPicker(targetId, options) {
        let targetElement = document.getElementById(targetId);
        targetElement.setAttribute('oncontextmenu','return false');

        // set options
        const pickerId = 'picker-' + targetId;
        options.value = targetElement.value;
        options.el = '#' + pickerId;
        options.inputEl = targetElement;

        // create target element container
        let targetContainer = document.createElement('div');
        targetContainer.className = 'picker-wrapper';
        targetElement.parentElement.insertBefore(targetContainer, targetElement.nextSibling);
        targetElement.parentElement.removeChild(targetElement);
        targetContainer.appendChild(targetElement);

        // create picker container
        let pickerContainer = document.createElement('div');
        pickerContainer.id = pickerId;
        pickerContainer.className = 'picker';
        targetContainer.appendChild(pickerContainer);
        
        // initialization picker
        let picker = new WindowDatePicker(options);
        picker.setPosition();
        return picker;
    }

    /**
     * DatePicker 초기화 시 호출(Date Only).
     *
     * @param targetId Target element id
     * @param dateType date format (optional) - YYYY-MM-DD(default), YYYY-DD-MM, DD-MM-YYYY, MM-DD-YYYY
     * @param lang lang format (optional) - en, ko, ja
     * @param callback 콜백 함수
     */
    function initDatePicker(targetId, dateType, lang, callback) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        if (typeof dateType !== 'undefined') {
            options.dateType = dateType.toUpperCase();
        }
        if (typeof lang !== 'undefined') {
            options.lang = lang;
        }
        let picker = initPicker(targetId, options);
        picker.el.addEventListener('wdp.change', () => {
            picker.closeDateContainer();
        });
        picker.el.addEventListener('wdp.close', () => {
            if (typeof callback === 'function') {
                callback(picker.inputEl, picker);
            }
        });
    }

    /**
     * DateTimePicker 초기화 시 호출(include time).
     *
     * @param targetId Target element id
     * @param dateType date format (optional) - YYYY-MM-DD(default), YYYY-DD-MM, DD-MM-YYYY, MM-DD-YYYY
     * @param hourType hour format (optional) - 24(default), 12
     * @param lang lang format (optional) - en, ko, ja
     * @param callback 콜백 함수
     */
    function initDateTimePicker(targetId, dateType, hourType, lang, callback) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'DATEHOUR';
        if (typeof dateType !== 'undefined') {
            options.dateType = dateType;
        }
        if (typeof hourType !== 'undefined') {
            options.hourType = '' + getHourTypeForCalendar(hourType);
        }
        if (typeof lang !== 'undefined') {
            options.lang = '' + lang;
        }
        let picker = initPicker(targetId, options);
        picker.el.addEventListener('wdp.change', () => {
            if (picker.page === 'DATE') {
                picker.changePage();
            }
        });
        picker.el.addEventListener('wdp.close', () => {
            if (picker.page === 'HOUR') {
                picker.changePage();
            }
            if (typeof callback === 'function') { 
                callback(picker.inputEl); 
            }
        });
    }

    /**
     * TimePicker 초기화 시 호출(time only).
     *
     * @param targetId Target element id
     * @param hourType hour format (optional) - 24(default), 12
     * @param lang lang: ko, en
     * @param callback 콜백 함수
     */
    function initTimePicker(targetId, hourType, lang, callback) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'HOUR';
        if (typeof hourType !== 'undefined') {
            options.hourType = '' + getHourTypeForCalendar(hourType);
        }
        if (typeof lang !== 'undefined') {
            options.lang = lang;
        }

        let picker = initPicker(targetId, options);
        picker.el.addEventListener('wdp.close', () => {
            if (typeof callback === 'function') { 
                callback(picker.inputEl);
            }
        });
    }

    /**
     * 시스템 공통 포맷과 캘린더 라이브러리의 시간 포맷 차이를 처리하기 위한 함수.
     * 시스템에서는 HH 와 hh 로 12,24시간 표기를 구분하는데
     * 현재 사용하는 window-date-picker는 12,24 값으로 구분 함.
     *
     * @author Jung Hee Chan
     * @since 2020-05-27
     * @param {String} userHourType 사용자의 시간 설정 값.
     * @return {String} 변경된 calendar hour 설정 값.
     */
    function getHourTypeForCalendar(userHourType) {
            // 2020-05-22 Jung Hee Chan
            // 기존 12,24 값 외에 Alice Datetime Format(HH:mm, hh:mm)도 받으면 처리하도록 함.
        let calendarHourType = userHourType;
        switch (userHourType.substr(0,2)) {
            case 'HH' :
                calendarHourType = '24';
                break;
            case 'hh' :
                calendarHourType = '12';
                break;
        }
        return calendarHourType;
    }

    return {
        initDatePicker: initDatePicker,
        initDateTimePicker: initDateTimePicker,
        initTimePicker: initTimePicker
    }
})();
