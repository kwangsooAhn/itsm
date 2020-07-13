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
     * @param colorCodeElem 컬러 색상값 엘리먼트
     * @param colorPaletteElem 보여줄 컬러 팔레트 엘리먼트
     * @param colorName 커스텀 컬러명
     */
    function initColorPalette(eventTriggerElem, colorCodeElem, colorPaletteElem, colorName) {
        if (colorName === '' || colorName === undefined) {
            colorName = basicPaletteColors;
        }

        let paletteEl = colorPaletteElem.querySelector(".color-palette");
        let pk = new Piklor(paletteEl, eval(colorName), { open: eventTriggerElem, closeOnBlur: true});

        // Selector 사용하는 경우를 대비해서 원래 구조를 그대로 둠.
        let wrapperEl = pk.getElm(eventTriggerElem);

        // Opacity
        let opacityEl = colorPaletteElem.querySelector(".color-palette-opacity");
        let slideInput;
        if (opacityEl !== null) {
            slideInput = this.initColorPaletteOpacity(paletteEl, wrapperEl, opacityEl, pk, colorCodeElem);
        }

        pk.colorChosen(function (col) {
            wrapperEl.style.backgroundColor = col;
            colorCodeElem.value = col;
            if (opacityEl !== null) {
                colorCodeElem.dataset['opacity'] = slideInput.value;
                opacityEl.style.display = 'none';
            }
            const evt = document.createEvent('HTMLEvents');
            evt.initEvent('change', false, true);
            colorCodeElem.dispatchEvent(evt);
        });

        eventTriggerElem.addEventListener('blur', function(e) {

        });
    };

    function initColorPaletteOpacity(paletteEl, wrapperEl, opacityEl, pk, colorCodeElem) {
        let slideBox = document.createElement('span');
        let slideObject = document.createElement('input');
        slideObject.type = 'range';
        slideObject.min = '0';
        slideObject.max = '100';
        slideObject.value = '100';
        slideObject.id = 'slideObject';
        slideObject.style.width = '122px';
        slideObject.style.marginLeft = '9.5px';
        slideObject.style.marginRight = '10px';

        let slideInput = document.createElement('input');
        slideInput.type = 'number';
        slideInput.min = '0';
        slideInput.max = '100';
        slideInput.style.width = '39px';
        slideInput.style.height = '20px';
        slideInput.value = slideObject.value;
        slideBox.appendChild(slideObject);
        slideBox.appendChild(slideInput);
        opacityEl.appendChild(slideBox);

        slideObject.addEventListener('click', function (e) {
            e.stopPropagation();
            e.preventDefault();
            slideInput.value = this.value;
            colorCodeElem.dataset['opacity'] = slideInput.value;
            const evt = document.createEvent('HTMLEvents');
            evt.initEvent('change', false, true);
            colorCodeElem.dispatchEvent(evt);
        });

        slideInput.addEventListener('click', function (e) {
            e.stopPropagation();
            e.preventDefault();
            slideInput.oninput = function() {
                slideObject.value = this.value;
                colorCodeElem.dataset['opacity'] = slideObject.value;
                const evt = document.createEvent('HTMLEvents');
                evt.initEvent('change', false, true);
                colorCodeElem.dispatchEvent(evt);
            }
        });

        wrapperEl.addEventListener('click', function () {
            if (pk.isOpen) {
                opacityEl.style.display = 'block';
            } else {
                opacityEl.style.display = 'none';
            }
        });

        window.addEventListener("click", function (ev) {
            if (ev.target !== wrapperEl && ev.target !== paletteEl && ev.target !== slideInput) {
                opacityEl.style.display = 'none';
            }
        });

        return slideInput
    }

    return {
        initColorPalette: initColorPalette,
        initColorPaletteOpacity: initColorPaletteOpacity
    }
})();
