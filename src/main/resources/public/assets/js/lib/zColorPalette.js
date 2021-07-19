/**
 * Color Palette Library
 *
 * - piklor.js 를 기본으로 color palette 그려주므로 반드시 함께 import 한다.
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const DEFAULT_OPTIONS = {

};

function zColorPalette(targetElement, options) {
    this.options = Object.assign({}, DEFAULT_OPTIONS, options);
    this.isOpen = true;
    console.log(this);

    // input box
    targetElement.classList.add(aliceJs.CLASS_PREFIX + 'color-input');
    this.inputEl = targetElement;

    // wrapper
    const wrapperContainer = document.createElement('div');
    wrapperContainer.className = aliceJs.CLASS_PREFIX + 'color-palette-wrapper';
    wrapperContainer.tabIndex = 0;
    targetElement.parentElement.insertBefore(wrapperContainer, targetElement.nextSibling);
    targetElement.parentElement.removeChild(targetElement);
    wrapperContainer.appendChild(targetElement);
    this.containerEl = wrapperContainer;

    // color box
    const colorBox = document.createElement('div');
    colorBox.className = aliceJs.CLASS_PREFIX + 'color-box';
    colorBox.style.backgroundColor = this.inputEl.value || '#ffffff';
    wrapperContainer.appendChild(colorBox);
    this.colorEl = wrapperContainer;

    // 아이콘
    const paletteIcon = document.createElement('span');
    paletteIcon.className = 'z-icon i-color-palette';
    wrapperContainer.appendChild(paletteIcon);

    // color palette modal
    let paletteModal = document.createElement('div');
    paletteModal.id = targetElement.id + 'Palette';
    paletteModal.className = aliceJs.CLASS_PREFIX + 'palette-modal';
    wrapperContainer.appendChild(paletteModal);
    this.modalEl = paletteModal;

    const self = this;
    this.containerEl.addEventListener('click', function () {
        self.isOpen ? self.close() : self.open();
    });
}
Object.assign(zColorPalette.prototype, {
    // open
    open: function () {
        if (!this.modalEl.classList.contains('active')) {
            this.modalEl.classList.add('active');
            this.setPosition();
            this.isOpen = true;

            // Detects the target if it's the picker element, if not, closes the picker
            document.addEventListener('mousedown', this.clickWindow, false);
            window.addEventListener('scroll', this.setPosition, false);
            window.addEventListener('resize', this.setPosition, false);
        }
    },
    // close
    close: function () {
        if (this.modalEl.classList.contains('active')) {
            this.modalEl.classList.remove('active');
            this.isOpen = false;

            // remove event
            document.removeEventListener('mousedown', this.autoClose, false);
            window.removeEventListener('scroll', this.setPosition, false);
            window.removeEventListener('resize', this.setPosition, false);
        }
    },
    // Palette set Position.
    setPosition: function() {
        let rect = this.modalEl.parentNode.getBoundingClientRect(),
            ow = this.modalEl.offsetWidth,
            oh = this.modalEl.offsetHeight,
            x = rect.left + ow,
            _x = rect.left - ow,
            y = rect.top + rect.height + oh,
            _y = rect.top - oh,
            w = window.innerWidth,
            h = window.innerHeight;

        if (x >= w && _x > 0) {
            this.modalEl.style.left = rect.left + rect.width - ow + 'px';
        } else {
            this.modalEl.style.left = rect.left + 'px';
        }

        if (y >= h && _y > 0) {
            this.modalEl.style.top = rect.top - oh - 3 + 'px'; // 3은 간격
        } else {
            this.modalEl.style.top = rect.top + rect.height +  3 + 'px';
        }
    },
    // Palette 가 오픈된 상태로 modal 외부를 선택할 경우 닫음.
    autoClose: function(e) {
        if (!aliceJs.clickInsideElement(e, aliceJs.CLASS_PREFIX + 'palette-modal') &&
            !aliceJs.clickInsideElement(e, aliceJs.CLASS_PREFIX + 'color-box')) {
            this.close();
        }
    }
});


/*const zColorPalette = (function() {
    'use strict';

    const basicPaletteColors = [
        '#EBEBEB'
        , '#ACB4BF'
        , '#929AA6'
        , '#586872'
        , '#3F4B56'
        , '#534666'
        , '#283238'
        , '#78D5F5'
        , '#0098FF'
        , '#3498DB'
        , '#0060A5'
        , '#3C4CAD'
        , '#787FF6'
        , '#240E88'
        , '#9B59B6'
        , '#DDB0A7'
        , '#DF8053'
        , '#825A2C'
        , '#E74C3C'
        , '#FF405A'
        , '#A20025'
        , '#A8B293'
        , '#0A3D64'
        , '#138086'
        , '#1ABC9C'
        , '#00C462'
        , '#EAAD5A'
        , '#F1C40F'
        , '#FFFFFF'
    ];

    /!**
     * 커스텀 색상표 설정.
     *
     * @param paletteColors 색상표 이름
     * @return 색상표 목록
     *!/
    function getPaletteColors(paletteColors) {
        return paletteColors ? eval(paletteColors) : basicPaletteColors;
    }

    /!**
     * 색상표 설정 (기본값: basicPaletteColors).
     *
     * @param option 옵션
     * @return {string[]} 색상표 목록
     *!/
    function setPaletteColors(option) {
        let colors = basicPaletteColors;
        if (option !== null && option !== undefined && option['colors'] !== undefined) {
            colors = option['colors'];
        }
        return colors;
    }

    /!**
     * 색상표 템플릿 설정 (기본값: null).
     *   - 색상표에 디자인 적용시 사용 (ex: 그라데이션, 모양 등)
     *
     * @param option 옵션
     * @return {string} template
     *!/
    function setPaletteTemplate(option) {
        let template = '';
        if (option !== null && option !== undefined && option['template'] !== undefined) {
            template = option['template'];
        }
        return template;
    }

    /!**
     * 데이터 옵션.
     *
     * @param option 옵션
     * @return {object} data
     *!/
    function setData(option) {
        let data = {};
        if (option !== null && option !== undefined && option['data'] !== undefined) {
            data.isSelected = option['data']['isSelected'] !== undefined ? option['data']['isSelected'] : false;
            data.selectedClass = option['data']['selectedClass'] !== undefined ? option['data']['selectedClass'] : null;

            // data 값이 RGBA 인 경우
            data.value = null;
            let dataValue = option['data']['value'];
            if (dataValue !== undefined) {
                data.value = aliceJs.isRgba(dataValue) ? aliceJs.rgbaToHex(dataValue) : dataValue;
            }
        }
        return data;
    }

    /!**
     * 불투명도 사용여부.
     *
     * @param option 옵션
     * @return {boolean} 사용여부
     *!/
    function isPaletteOpacity(option) {
        let isOpacity = false;
        if (option !== null && option !== undefined && option['isOpacity'] !== undefined) {
            isOpacity = option['isOpacity'];
        }
        return isOpacity;
    }

    /!**
     * 불투명도 슬라이드 생성.
     *
     * @return {HTMLInputElement}
     *!/
    function createRangeElement() {
        let slide = document.createElement('input');
        slide.type = 'range';
        slide.min = '0';
        slide.max = '100';
        slide.value = '100';
        slide.className = 'slide-object';
        return slide;
    }

    /!**
     * 불투명도 슬라이드 값 input 생성.
     *
     * @param slide 슬라이드 엘리먼트
     * @return {HTMLInputElement}
     *!/
    function createInputElement(slide) {
        let input = document.createElement('input');
        input.type = 'text';
        input.className = 'slide-input';
        input.value = slide.value;
        input.maxLength = 3;
        return input;
    }

    /!**
     * 색상표 생성.
     *
     * @param colorLayout 색상표 전체 레이아웃 (색상표, 불투명도 부모 엘리멘트)
     * @param selectedBox 선택된 색상 box
     * @param selectedInput 선택된 색상 input
     * @param option 옵션
     *!/
    function initColorPalette(colorLayout, selectedBox, selectedInput, option) {
        let palette = colorLayout.querySelector('.color-palette');

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
            }
            // 기존 선택된 값의 테두리를 없애고 새로운 값 테두리 생성
            paletteElement.childNodes.forEach(function(item){
                item.classList.remove(data.selectedClass);
                if (item.dataset.col === color) {
                    item.classList.add(data.selectedClass);
                }
            });
            const evt = document.createEvent('HTMLEvents');
            evt.initEvent('change', false, true);
            selectedInput.dispatchEvent(evt);
        });
    }

    /!**
     * 불투명도 조절 이벤트.
     *
     * @param event 이벤트
     * @param selectedInput 컬러 색상값 엘리먼트
     * @param slideInput 슬라이드 Input 오브젝트
     * @param slideObject 슬라이드 오브젝트
     *!/
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
    };
})();*/
