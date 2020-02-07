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

        // set options
        const pickerId = 'picker-' + targetId;
        options.value = targetElement.value;
        options.el = '#' + pickerId;
        options.inputEl = targetElement;

        // create target element container
        let targetContainer = document.createElement('div');
        targetContainer.style.display = 'inline-block';
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
     * DateTimePicker 초기화 시 호출(Date Only).
     *
     * @param targetId Target element id
     * @param dateType date format (optional) - YYYY-MM-DD(default), YYYY-DD-MM, DD-MM-YYYY, MM-DD-YYYY
     * @param lang lang format (optional) - en, ko, ja
     */
    function initDatePicker(targetId, dateType, lang) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        if (typeof dateType !== 'undefined') {
            options.dateType = dateType;
        }
        if (typeof lang !== 'undefined') {
            options.lang = lang;
        }
        let picker = initPicker(targetId, options);
        picker.el.addEventListener('wdp.change', () => {
            picker.closeDateContainer();
        });
    }

    /**
     * DateTimePicker 초기화 시 호출(include time).
     *
     * @param targetId Target element id
     * @param dateType date format (optional) - YYYY-MM-DD(default), YYYY-DD-MM, DD-MM-YYYY, MM-DD-YYYY
     * @param hourType hour format (optional) - 24(default), 12
     * @param lang lang format (optional) - en, ko, ja
     */
    function initDateTimePicker(targetId, dateType, hourType, lang) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'DATEHOUR';
        if (typeof dateType !== 'undefined') {
            options.dateType = dateType;
        }
        if (typeof hourType !== 'undefined') {
            options.hourType = '' + hourType;
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
        });
    }

    /**
     * TimePicker 초기화 시 호출(time only).
     *
     * @param targetId Target element id
     * @param hourType hour format (optional) - 24(default), 12
     */
    function initTimePicker(targetId, hourType) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'HOUR';
        if (typeof hourType !== 'undefined') {
            options.hourType = hourType;
        }
        initPicker(targetId, options);
    }

    return {
        initDatePicker: initDatePicker,
        initDateTimePicker: initDateTimePicker,
        initTimePicker: initTimePicker
    }
})();
