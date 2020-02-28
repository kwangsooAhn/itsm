const colorPalette = (function() {
    'use strict';

    const paletteColors = [
        "#1abc9c"
        , "#2ecc71"
        , "#3498db"
        , "#9b59b6"
        , "#34495e"
        , "#16a085"
        , "#27ae60"
        , "#2980b9"
        , "#8e44ad"
        , "#2c3e50"
        , "#f1c40f"
        , "#e67e22"
        , "#e74c3c"
        , "#ecf0f1"
        , "#95a5a6"
        , "#f39c12"
        , "#d35400"
        , "#c0392b"
        , "#bdc3c7"
        , "#7f8c8d"
    ];

    /**
     * Color Palette 사용을 위한 컨테이너 생성 및 초기화 처리.
     *
     * @param eventTriggerElem 컬러 팔레트를 출력하기 위한 클릭 이벤트를 걸 엘리먼트
     * @param colorPaletteElem 보여줄 컬러 팔레트 엘리먼트
     */
    function initColorPalette(eventTriggerElem, colorPaletteElem) {
        let pk = new Piklor(colorPaletteElem, paletteColors,
            { open: eventTriggerElem });

        // Selector 사용하는 경우를 대비해서 원래 구조를 그대로 둠.
        let wrapperEl = pk.getElm(eventTriggerElem);

        pk.colorChosen(function (col) {
            wrapperEl.style.backgroundColor = col;
            wrapperEl.value = col;
        });
    };

    return {
        initColorPalette: initColorPalette
    }
})();