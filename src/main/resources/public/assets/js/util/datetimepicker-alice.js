const dateTimePicker = (function() {
    const defaultOptions = {
        inputToggle: true,
        type: 'DATE',
        hourType: '24'
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
        const picker = new WindowDatePicker(options);
        picker.setPosition();
        picker.el.addEventListener('wdp.change', () => {
            //picker.toggle(); // 선택 후 바로 닫고 싶을 경우 comment 해제
        });
    }

    /**
     * DateTimePicker 초기화 시 호출(Date Only).
     *
     * @param targetId Target element id
     */
    function initDatePicker(targetId) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        initPicker(targetId, options);
    }

    /**
     * DateTimePicker 초기화 시 호출(include time).
     *
     * @param targetId Target element id
     */
    function initDateTimePicker(targetId) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'DATEHOUR';
        initPicker(targetId, options);
    }

    /**
     * TimePicker 초기화 시 호출(time only).
     *
     * @param targetId Target element id
     */
    function initTimePicker(targetId) {
        let options = JSON.parse(JSON.stringify(defaultOptions));
        options.type = 'HOUR';
        initPicker(targetId, options);
    }

    return {
        initDatePicker: initDatePicker,
        initDateTimePicker: initDateTimePicker,
        initTimePicker: initTimePicker
    }
})();