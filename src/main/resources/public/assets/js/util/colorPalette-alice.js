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
     * @param isOpacity 불투명도 사용여부
     * @param colorName 커스텀 컬러명
     */
    function initColorPalette(eventTriggerElem, colorCodeElem, colorPaletteElem, isOpacity, colorName) {
        if (colorName === null || colorName === '' || colorName === undefined) {
            colorName = basicPaletteColors;
        }

        let paletteEl = colorPaletteElem.querySelector(".color-palette");
        let pk = new Piklor(paletteEl, eval(colorName), { open: eventTriggerElem, closeOnBlur: true});

        // Selector 사용하는 경우를 대비해서 원래 구조를 그대로 둠.
        let wrapperEl = pk.getElm(eventTriggerElem);

        // Opacity
        let opacityEl;
        let slideObject;
        if (isOpacity) {
            opacityEl = document.createElement('div');
            opacityEl.className = 'color-palette-opacity';
            opacityEl.id = paletteEl.id + '-' +  'opacity';

            let slideBox = document.createElement('span');
            slideObject = document.createElement('input');
            slideObject.type = 'range';
            slideObject.min = '0';
            slideObject.max = '100';
            slideObject.value = '100';
            slideObject.className = 'slide-object'

            let slideInput = document.createElement('input');
            slideInput.type = 'text';
            slideInput.readOnly = true;
            slideInput.className = 'slide-input';
            slideInput.value = slideObject.value;

            let slideUnit = document.createElement('label');
            slideUnit.innerText = '%';
            slideBox.appendChild(slideObject);
            slideBox.appendChild(slideInput);
            slideBox.appendChild(slideUnit);
            opacityEl.appendChild(slideBox);
            colorPaletteElem.appendChild(opacityEl);

            slideObject.addEventListener('click', function (event) {
                slideObjectEvent(event, colorCodeElem, slideInput, this);
            });

            slideObject.addEventListener('input', function (event) {
                slideObjectEvent(event, colorCodeElem, slideInput, this);
            });

            opacityEl.addEventListener('click', function(ev) {
                ev.stopPropagation();
                ev.preventDefault();
            });

            window.addEventListener('click', function(e) {
                if (pk.isOpen) {
                    opacityEl.style.display = 'table';
                } else {
                    opacityEl.style.display = 'none';
                }
            });
        }

        pk.colorChosen(function (col) {
            wrapperEl.style.backgroundColor = col;

            colorCodeElem.value = col;
            if (isOpacity && opacityEl !== null) {
                colorCodeElem.dataset['opacity'] = slideObject.value;
                opacityEl.style.display = 'none';
            }
            const evt = document.createEvent('HTMLEvents');
            evt.initEvent('change', false, true);
            colorCodeElem.dispatchEvent(evt);
        });

        eventTriggerElem.addEventListener('blur', function(e) {

        });
    }

    /**
     * 불투명도 조절 이벤트.
     *
     * @param event 이벤트
     * @param colorCodeElem 컬러 색상값 엘리먼트
     * @param slideInput 슬라이드 Input 오브젝트
     * @param slideObject 슬라이드 오브젝트
     */
    function slideObjectEvent(event, colorCodeElem, slideInput, slideObject) {
        event.stopPropagation();
        event.preventDefault();
        slideInput.value = Number(slideObject.value).toFixed(0);
        colorCodeElem.dataset['opacity'] = slideInput.value;
        const evt = document.createEvent('HTMLEvents');
        evt.initEvent('change', false, true);
        colorCodeElem.dispatchEvent(evt);
    }

    return {
        initColorPalette: initColorPalette
    }
})();
