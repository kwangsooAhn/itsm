const colorPalette = (function() {
    'use strict';

    const basicPaletteColors = [
        '#1abc9c'
        , '#2ecc71'
        , '#3498db'
        , '#9b59b6'
        , '#34495e'
        , '#16a085'
        , '#27ae60'
        , '#2980b9'
        , '#8e44ad'
        , '#2c3e50'
        , '#f1c40f'
        , '#e67e22'
        , '#e74c3c'
        , '#ecf0f1'
        , '#95a5a6'
        , '#f39c12'
        , '#d35400'
        , '#c0392b'
        , '#bdc3c7'
        , '#7f8c8d'
    ];

    const documentPaletteColors = [
        '#ff0000'
        , '#ff8000'
        , '#ffff00'
        , '#00ff00'
        , '#00ffff'
        , '#0000ff'
        , '#ff00ff'
        , '#f7be81'
        , '#f3f781'
        , '#81f781'
        , '#81f7f3'
        , '#8181f7'
        , '#fbefef'
        , '#f8eCe0'
        , '#f7f8e0'
        , '#e0f8e0'
        , '#e0f8f7'
        , '#e0e0f8'
        , '#f8e0f7'
        , '#fafafa'
        , '#000000'
    ];


    /**
     * Color Palette 사용을 위한 컨테이너 생성 및 초기화 처리.
     *
     * @param eventTriggerElem 컬러 팔레트를 출력하기 위한 클릭 이벤트를 걸 엘리먼트
     * @param colorPaletteElem 보여줄 컬러 팔레트 엘리먼트
     */
    function initColorPalette(eventTriggerElem, colorCodeElem, colorPaletteElem, colorName) {
        if (colorName === '' || colorName === undefined) {
            colorName = basicPaletteColors;
        }

        let pk = new Piklor(colorPaletteElem, eval(colorName),
            { open: eventTriggerElem, closeOnBlur: true});

        // Selector 사용하는 경우를 대비해서 원래 구조를 그대로 둠.
        let wrapperEl = pk.getElm(eventTriggerElem);

        pk.colorChosen(function (col) {
            wrapperEl.style.backgroundColor = col;
            colorCodeElem.value = col;

            const evt = document.createEvent('HTMLEvents');
            evt.initEvent('change', false, true);
            colorCodeElem.dispatchEvent(evt);
        });

        eventTriggerElem.addEventListener('blur', function(e) {

        });
    };

    return {
        initColorPalette: initColorPalette
    }
})();