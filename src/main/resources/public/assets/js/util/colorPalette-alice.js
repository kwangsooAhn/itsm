const colorPalette = (function() {
    'use strict';

    const basicPaletteColors = [
        '#000000'
        , '#1abc9c'
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
        , '#ffffff'
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
     * 커스텀 색상표 설정.
     *
     * @param paletteColors 색상표 이름
     * @return 색상표 목록
     */
    function getPaletteColors(paletteColors) {
        return paletteColors ? eval(paletteColors) : basicPaletteColors;
    }

    /**
     * 색상표 설정 (기본값: basicPaletteColors).
     *
     * @param option 옵션
     * @return {string[]} 색상표 목록
     */
    function setPaletteColors(option) {
        let colors = basicPaletteColors;
        if (option !== null && option !== undefined && option['colors'] !== undefined) {
            colors = option['colors'];
        }
        return colors;
    }

    /**
     * 색상표 템플릿 설정 (기본값: null).
     *   - 색상표에 디자인 적용시 사용 (ex: 그라데이션, 모양 등)
     *
     * @param option 옵션
     * @return {string} template
     */
    function setPaletteTemplate(option) {
        let template = '';
        if (option !== null && option !== undefined && option['template'] !== undefined) {
            template = option['template'];
        }
        return template;
    }

    /**
     * 데이터 옵션.
     *
     * @param option 옵션
     * @return {object} data
     */
    function setData(option) {
        let data = {};
        if (option !== null && option !== undefined && option['data'] !== undefined) {
            data.isSelected = option['data']['isSelected'] !== undefined ? option['data']['isSelected'] : false
            data.selectedClass = option['data']['selectedClass'] !== undefined ? option['data']['selectedClass'] : null

            // data 값이 RGBA 인 경우
            data.value = null;
            let dataValue = option['data']['value'];
            if (dataValue !== undefined) {
                data.value = aliceJs.isRgba(dataValue) ? aliceJs.rgbaToHex(dataValue) : dataValue
            }
        }
        return data;
    }

    /**
     * 불투명도 사용여부.
     *
     * @param option 옵션
     * @return {boolean} 사용여부
     */
    function isPaletteOpacity(option) {
        let isOpacity = false;
        if (option !== null && option !== undefined && option['isOpacity'] !== undefined) {
            isOpacity = option['isOpacity']
        }
        return isOpacity
    }

    /**
     * 불투명도 슬라이드 생성.
     *
     * @return {HTMLInputElement}
     */
    function createRangeElement() {
        let slide = document.createElement('input');
        slide.type = 'range';
        slide.min = '0';
        slide.max = '100';
        slide.value = '100';
        slide.className = 'slide-object'
        return slide;
    }

    /**
     * 불투명도 슬라이드 값 input 생성.
     *
     * @param slide 슬라이드 엘리먼트
     * @return {HTMLInputElement}
     */
    function createInputElement(slide) {
        let input = document.createElement('input');
        input.type = 'text';
        input.className = 'slide-input';
        input.value = slide.value;
        input.maxLength = 3;
        return input;
    }

    /**
     * 색상표 생성.
     *
     * @param colorLayout 색상표 전체 레이아웃 (색상표, 불투명도 부모 엘리멘트)
     * @param selectedBox 선택된 색상 box
     * @param selectedInput 선택된 색상 input
     * @param option 옵션
     */
    function initColorPalette(colorLayout, selectedBox, selectedInput, option) {
        let palette = colorLayout.querySelector(".color-palette");

        let colors = setPaletteColors(option);
        let template = setPaletteTemplate(option);
        let data = setData(option);
        let isOpacity = isPaletteOpacity(option);
        let pk = new Piklor(palette, colors, { open: selectedBox, closeOnBlur: true, template: template});

        // 기존에 선택된 color 에 class 적용 (border)
        let paletteElement = pk.getElm(palette);
        if (data.isSelected) {
            paletteElement.childNodes.forEach(function(item){
                item.addEventListener('click', function(e) {
                    e.stopPropagation();
                    e.preventDefault();
                    pk.set(item.dataset.col, true);
                });
                if (item.dataset.col === data.value) {
                    item.classList.add(data.selectedClass);
                }
            });
        }

        // Opacity
        let opacityElement;
        let slideObject;
        if (isOpacity) {
            opacityElement = document.createElement('div');
            opacityElement.className = 'color-palette-opacity';
            opacityElement.id = palette.id + '-' +  'opacity';

            let slideBox = document.createElement('span');
            slideObject = createRangeElement();
            let slideInput = createInputElement(slideObject);

            let slideUnit = document.createElement('label');
            slideUnit.innerText = '%';

            slideBox.appendChild(slideObject);
            slideBox.appendChild(slideInput);
            slideBox.appendChild(slideUnit);
            opacityElement.appendChild(slideBox);
            colorLayout.appendChild(opacityElement);

            slideObject.addEventListener('click', function (event) {
                slideEvent(event, selectedInput, slideInput, this);
            });

            slideObject.addEventListener('input', function (event) {
                slideEvent(event, selectedInput, slideInput, this);
            });

            slideInput.addEventListener('input', function (event) {
                if (!numberReg.test(this.value)) {
                    this.value = this.value.replace(/[^0-9]/g, '');
                    return false;
                }
                if (this.value < 0 || this.value > 100) {
                    this.value = 100;
                }
                if (this.value === '') {
                    this.value = 0;
                }
                slideObject.value = this.value;
                slideEvent(event, selectedInput, slideInput, this);
            });

            opacityElement.addEventListener('click', function(ev) {
                ev.stopPropagation();
                ev.preventDefault();
            });

            window.addEventListener('click', function(e) {
                if (pk.isOpen) {
                    opacityElement.style.display = 'table';
                } else {
                    opacityElement.style.display = 'none';
                }
            });
        }

        pk.colorChosen(function (color) {
            selectedBox.style.backgroundColor = color;
            selectedInput.value = color;
            if (isOpacity && opacityElement !== null) {
                selectedInput.dataset['opacity'] = slideObject.value;

                // 기존 선택된 값의 테두리를 없애고 새로운 값 테두리 생성
                paletteElement.childNodes.forEach(function(item){
                    item.classList.remove(data.selectedClass);
                    if (item.dataset.col === color) {
                        item.classList.add(data.selectedClass);
                    }
                });
            }
            const evt = document.createEvent('HTMLEvents');
            evt.initEvent('change', false, true);
            selectedInput.dispatchEvent(evt);
        });
    }

    /**
     * 불투명도 조절 이벤트.
     *
     * @param event 이벤트
     * @param selectedInput 컬러 색상값 엘리먼트
     * @param slideInput 슬라이드 Input 오브젝트
     * @param slideObject 슬라이드 오브젝트
     */
    function slideEvent(event, selectedInput, slideInput, slideObject) {
        event.stopPropagation();
        event.preventDefault();
        slideInput.value = Number(slideObject.value).toFixed(0);
        selectedInput.dataset['opacity'] = slideInput.value;
        const evt = document.createEvent('HTMLEvents');
        evt.initEvent('change', false, true);
        selectedInput.dispatchEvent(evt);
    }

    return {
        getPaletteColors: getPaletteColors,
        initColorPalette: initColorPalette,
    }
})();
