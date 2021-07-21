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
    type: 'fill', // fill or line
    colors: [
        ['#F52525', '#FF850A', '#FADF2D', '#76BD26', '#339AF0', '#6885F7', '#F763C1', '#A95EEB', '#FFFFFF', '#000000'],
        ['#FEE2D3', '#FFF0CE', '#FEFBD4', '#F1FCE0', '#E3F5FC', '#E1E9FE', '#FEE0E7', '#F6DFFE', '#F5F5F5', '#6D6D6D'],
        ['#FEBEA7', '#FFDC9C', '#FEF6AB', '#DBF8A9', '#ADE6FD', '#C3D2FE', '#FEC1D5', '#EAC0FD', '#EEEEEE', '#494949'],
        ['#F9685A', '#FFAC47', '#FBE961', '#A0D756', '#64BBF6', '#8DA5FA', '#FA89C6', '#C585F3', '#BDBDBD', '#373737'],
        ['#D21B2A', '#DB6707', '#D7BC20', '#5CA21B', '#2578CE', '#4C64D4', '#D448AF', '#8444CA', '#757575', '#242424'],
        ['#8E0B2D', '#933603', '#907A0E', '#316D0C', '#103F8B', '#21308F', '#731376', '#451D88', '#424242', '#121212']
    ]
};

function zColorPicker(targetElement, options) {
    this.options = Object.assign({}, DEFAULT_OPTIONS, options);
    this.isOpen = false;

    // input box
    targetElement.classList.add(aliceJs.CLASS_PREFIX + 'color-input');
    this.value = targetElement.value.toUpperCase() || 'transparent';
    this.inputEl = targetElement;

    // wrapper
    const wrapperContainer = document.createElement('div');
    wrapperContainer.className = aliceJs.CLASS_PREFIX + 'color-picker-wrapper';
    targetElement.parentElement.insertBefore(wrapperContainer, targetElement.nextSibling);
    targetElement.parentElement.removeChild(targetElement);
    wrapperContainer.appendChild(targetElement);
    this.containerEl = wrapperContainer;

    // color picker
    const colorPicker = document.createElement('div');
    colorPicker.className = aliceJs.CLASS_PREFIX + 'color-picker';
    colorPicker.tabIndex = 0;
    wrapperContainer.appendChild(colorPicker);
    this.colorPicker = colorPicker;

    // color box
    const colorBox = document.createElement('div');
    colorBox.className = aliceJs.CLASS_PREFIX + 'color-box';
    colorPicker.appendChild(colorBox);
    this.colorEl = colorBox;

    // 아이콘
    const paletteIcon = document.createElement('span');
    paletteIcon.className = 'z-icon i-color-palette ml-1';
    colorPicker.appendChild(paletteIcon);

    // color picker modal
    let pickerModal = document.createElement('div');
    pickerModal.id = targetElement.id + 'Picker';
    pickerModal.className = aliceJs.CLASS_PREFIX + 'color-picker-modal';
    wrapperContainer.appendChild(pickerModal);
    this.modalEl = pickerModal;
    // 팔레트 출력
    this.drawPalette();
    // 사용자 색상 출력
    this.drawCustomColorPalette();
    // 색상 초기화
    this.setColor(this.value);

    // color picker toggle event
    const self = this;
    this.colorPicker.addEventListener('click', function (e) {
        e.stopPropagation();

        self.isOpen ? self.close() : self.open();
    });
}
Object.assign(zColorPicker.prototype, {
    // open
    open: function () {
        if (!this.modalEl.classList.contains('active')) {
            this.modalEl.classList.add('active');
            this.setPosition();
            this.isOpen = true;

            // Detects the target if it's the picker element, if not, closes the picker
            document.addEventListener('mousedown', this.autoClose.bind(this), false);
            window.addEventListener('scroll', this.setPosition.bind(this), false);
            window.addEventListener('resize', this.setPosition.bind(this), false);
        }
    },
    // close
    close: function () {
        if (this.modalEl.classList.contains('active')) {
            this.modalEl.classList.remove('active');
            this.isOpen = false;

            // remove event
            document.removeEventListener('mousedown', this.autoClose.bind(this), false);
            window.removeEventListener('scroll', this.setPosition.bind(this), false);
            window.removeEventListener('resize', this.setPosition.bind(this), false);
        }
    },
    // Palette 가 오픈된 상태로 modal 외부를 선택할 경우 닫음.
    autoClose: function(e) {
        if (!aliceJs.clickInsideElement(e, aliceJs.CLASS_PREFIX + 'color-picker-modal') &&
            !aliceJs.clickInsideElement(e, aliceJs.CLASS_PREFIX + 'color-picker')) {
            this.close();
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
    // set color
    setColor: function (color) {
        if (this.options.type === 'fill') {
            this.colorEl.style.backgroundColor = color;
        } else { // line
            this.colorEl.style.borderColor = color;
        }
    },
    // palette draw
    drawPalette() {
        let paletteContainer = document.createElement('div');
        paletteContainer.className = aliceJs.CLASS_PREFIX + 'palette-container';
        this.modalEl.appendChild(paletteContainer);

        // point color
        let template = `<div class="${aliceJs.CLASS_PREFIX}palette-row">`;
        for (let i = 0; i < this.options.colors[0].length; i++) {
            const itemColor = this.options.colors[0][i];
            template += `<span class="${aliceJs.CLASS_PREFIX}palette-item point-color`+
                `${(itemColor === '#FFFFFF') ? ' border-inset' : ''}${this.value === itemColor ? ' selected' : ''}"`+
                ` data-color="${itemColor}" style="background-color: ${this.value === itemColor ? 'transparent' : itemColor};" >`+
                `<sapn class="${aliceJs.CLASS_PREFIX}palette-item-inner" style="background-color: ${itemColor}"></sapn>` +
                `</span>`;
        }
        template += `</div>`;
        paletteContainer.insertAdjacentHTML('beforeend', template);

        // material color
        template = ``;
        for (let i = 1; i < this.options.colors.length; i++) {
            template += `<div class="${aliceJs.CLASS_PREFIX}palette-row">`;
            for (let j = 0; j < this.options.colors[i].length; j++) {
                const itemColor = this.options.colors[i][j];
                template += `<span class="${aliceJs.CLASS_PREFIX}palette-item material-color${(i === 1) ? ' first' : ''}`+
                    `${(i === (this.options.colors.length - 1)) ? ' last' : ''}${this.value === itemColor ? ' selected' : ''}"`+
                    ` data-color="${itemColor}" style="background-color: ${this.value === itemColor ? 'transparent' : itemColor};" >`+
                    `<sapn class="${aliceJs.CLASS_PREFIX}palette-item-inner" style="background-color: ${itemColor}"></sapn>` +
                    `</span>`;
            }
            template += `</div>`;
        }
        paletteContainer.insertAdjacentHTML('beforeend', template);
        
        // 이벤트 등록
        this.selectedEl = paletteContainer.querySelector('.selected');
        const self = this;
        paletteContainer.querySelectorAll('.' + aliceJs.CLASS_PREFIX + 'palette-item').forEach(function (item) {
            item.addEventListener('click', self.clickColorPalette.bind(self), false);
        });
    },
    // 사용자 색상 draw
    drawCustomColorPalette() {
        let paletteContainer = document.createElement('div');
        paletteContainer.className = aliceJs.CLASS_PREFIX + 'custom-color-palette-container';
        this.modalEl.appendChild(paletteContainer);

        // 사용자 색상 문구
        const textTemplate = `<span class="${aliceJs.CLASS_PREFIX}custom-color-text">${i18n.msg('common.label.customColor')}</span>`;
        paletteContainer.insertAdjacentHTML('beforeend', textTemplate);

        // 편집 아이콘
        const editButton = document.createElement('button');
        editButton.type = 'button';
        editButton.className = aliceJs.CLASS_PREFIX + 'button-icon extra ' + aliceJs.CLASS_PREFIX + 'custom-color-edit';
        editButton.insertAdjacentHTML('beforeend', `<span class="${aliceJs.CLASS_PREFIX}icon i-edit"></span>`);
        editButton.addEventListener('click', this.toggleCustomColorControl.bind(this), false);
        paletteContainer.appendChild(editButton);

        // 사용자 색상 목록
        const customColorContainer = document.createElement('div');
        customColorContainer.className = aliceJs.CLASS_PREFIX + 'custom-color-container';
        paletteContainer.appendChild(customColorContainer);

        // 저장된 사용자 색상 값을 가져와서 표기한다.

        // 사용자 색상 목록 추가 버튼 (최대 10개까지 등록가능)
        const addButton = document.createElement('button');
        addButton.type = 'button';
        addButton.className = aliceJs.CLASS_PREFIX + 'button-icon';
        addButton.insertAdjacentHTML('beforeend', `<span class="${aliceJs.CLASS_PREFIX}icon i-plus"></span>`);
        addButton.addEventListener('click', this.toggleCustomColorControl.bind(this), false);
        paletteContainer.appendChild(addButton);

        // hex, rgba control container
        const customColorControl = document.createElement('div');
        customColorControl.className = aliceJs.CLASS_PREFIX + 'custom-color-control';
        paletteContainer.appendChild(customColorControl);
        this.customColorControlEl = customColorControl;
    },
    // 사용자 색상 control
    drawCustomColorControl() {
        // 물방울
        const waterDrop = document.createElement('span');
        waterDrop.className = aliceJs.CLASS_PREFIX + 'icon i-water-drop';
        this.customColorControlEl.appendChild(waterDrop);
        this.waterDropEl = waterDrop;

        // hex
        const hexInput = document.createElement('input');
        hexInput.type = 'text';
        hexInput.className = aliceJs.CLASS_PREFIX + 'input ' + aliceJs.CLASS_PREFIX + 'color-hex';
        hexInput.placeholder = '#FFFFFF';
        //hexInput.value = '';
        this.customColorControlEl.appendChild(hexInput);
        this.hexEl = hexInput;

        // rgb
        ['r', 'g', 'b'].forEach(function (str) {
            const rgbInput = document.createElement('input');
            rgbInput.type = 'text';
            rgbInput.className = aliceJs.CLASS_PREFIX + 'input ' + aliceJs.CLASS_PREFIX + 'color-' + str;
            rgbInput.placeholder = '255';
            this[str + 'El'] = rgbInput;
        });

        // 추가 버튼
        const addButton = document.createElement('button');
        addButton.type = 'button';
        addButton.className = aliceJs.CLASS_PREFIX + 'button secondary';
        addButton.textContent = i18n.msg('common.btn.add');
        addButton.addEventListener('click', this.addCustomColor.bind(this), false);
        this.customColorControlEl.appendChild(addButton);

        let bottomButtonList = document.createElement('div');
        bottomButtonList.className = aliceJs.CLASS_PREFIX + 'button-list';
        this.customColorControlEl.appendChild(bottomButtonList);

        // 저장 버튼
        const saveButton = document.createElement('button');
        saveButton.type = 'button';
        saveButton.className = aliceJs.CLASS_PREFIX + 'button primary';
        saveButton.textContent = i18n.msg('common.btn.save');
        saveButton.addEventListener('click', this.saveCustomColor.bind(this), false);
        bottomButtonList.appendChild(saveButton);

        // 취소 버튼
        const cancelButton = document.createElement('button');
        cancelButton.type = 'button';
        cancelButton.className = aliceJs.CLASS_PREFIX + 'button extra';
        cancelButton.textContent = i18n.msg('common.btn.cancel');
        cancelButton.addEventListener('click', this.cancelCustomColor.bind(this), false);
        bottomButtonList.appendChild(cancelButton);
    },
    // Color Palette 선택
    clickColorPalette(e) {
        e.preventDefault();
        // 기존 색상 조기화
        if (this.selectedEl) {
            this.selectedEl.classList.remove('selected');
            this.selectedEl.style.backgroundColor = this.value;
        }
        this.value = e.target.getAttribute('data-color');

        // 현재 색상 선택
        if (!e.target.classList.contains('selected')) {
            e.target.classList.add('selected');
            e.target.style.backgroundColor = 'transparent';
            this.selectedEl = e.target;
        }
        // color element 색상 변경
        this.setColor(this.value);
        this.inputEl.value = this.value;

        // 모달 close
        this.close();
    },
    // 사용자 색상 오픈 / 닫기
    toggleCustomColorControl(e) {
        console.log(e);
        // 사용자 색상 편집 영역 오픈
        
        //버튼 숨기기
        
        // 사용자 색상 삭제(x) 아이콘 추가
    },
    // 사용자 색상 추가
    addCustomColor() {
        console.log(e);
    },
    // 사용자 색상 저장
    saveCustomColor(e) {
        console.log(e);
    },
    // 사용자 색상 취소
    cancelCustomColor(e) {
        // 기존 색상 선택
    },
    // Force a hex value to have 2 characters
    pad2(c) {
        return c.length == 1 ? '0' + c : '' + c;
    },
    // rgb to hex
    rgbToHex(r, g, b) {
        //  16진수 문자로 변환하기.
        const hex = [
            this.pad2(Math.round(r).toString(16)),
            this.pad2(Math.round(g).toString(16)),
            this.pad2(Math.round(b).toString(16))
        ];
        return '#'  + hex.join('');
    },
    // hex to rgb
    hexToRGB(hex) {
        // 맨 앞의 "#" 기호를 삭제하기.
        hex = hex.trim().replace('#', '');

        // rgb로 각각 분리해서 배열에 담기
        let rgb = ( 3 === hex.length ) ? hex.match( /[a-f\d]/gi ) : hex.match( /[a-f\d]{2}/gi );

        rgb.forEach(function (str, x, arr) {
            // rgb 각각의 헥사값이 한자리일 경우, 두자리로 변경하기.
            if ( str.length == 1 ) { str = str + str; }

            /* 10진수로 변환하기. */
            arr[x] = parseInt(str, 16);
        });
        //return 'rgb(' + rgb.join(', ') + ')';
        return rgb;
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
