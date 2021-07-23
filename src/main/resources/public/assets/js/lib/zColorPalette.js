/**
 * Color Picker Library
 *
 * Color Picker는 기본 색상을 제공하는 color palette와 사용자 색상을 추가하는 custom color palette 로 구성되어 있다.
 *
 * [적용 방법]
 * HTML : <input type="text" id="pickerId" value="#FFFFFF"/>
 * Javascript : new zColorPicker(document.getElementById('pickerId')[, { type: 'fill' }])
 *
 * @author Woo Da Jung <wdj@brainz.co.kr>
 * @version 1.0
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
const DEFAULT_OPTIONS = {
    type: 'fill', // fill or line (사용자 색상에 추가된 색상은 Fill, Line Color Picker 서로 공유된다.)
    colors: [
        ['#F52525', '#FF850A', '#FADF2D', '#76BD26', '#339AF0', '#6885F7', '#F763C1', '#A95EEB', '#FFFFFF', '#000000'],
        ['#FEE2D3', '#FFF0CE', '#FEFBD4', '#F1FCE0', '#E3F5FC', '#E1E9FE', '#FEE0E7', '#F6DFFE', '#F5F5F5', '#6D6D6D'],
        ['#FEBEA7', '#FFDC9C', '#FEF6AB', '#DBF8A9', '#ADE6FD', '#C3D2FE', '#FEC1D5', '#EAC0FD', '#EEEEEE', '#494949'],
        ['#F9685A', '#FFAC47', '#FBE961', '#A0D756', '#64BBF6', '#8DA5FA', '#FA89C6', '#C585F3', '#BDBDBD', '#373737'],
        ['#D21B2A', '#DB6707', '#D7BC20', '#5CA21B', '#2578CE', '#4C64D4', '#D448AF', '#8444CA', '#757575', '#242424'],
        ['#8E0B2D', '#933603', '#907A0E', '#316D0C', '#103F8B', '#21308F', '#731376', '#451D88', '#424242', '#121212']
    ],
    rgbReg: /^\d{1,3}$/,
    hexReg: /^\#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$/,
};

function zColorPicker(targetElement, options) {
    this.options = Object.assign({}, DEFAULT_OPTIONS, options);
    this.isOpen = false;

    // input box
    targetElement.classList.add(aliceJs.CLASS_PREFIX + 'color-input');
    this.inputEl = targetElement;
    // 기존 저장된 색상 : 사용자가 색상을 변경하더라도, 사용자 색상을 저장하지 않고 color picker를 닫으면 원래 색상으로 변경되어야 한다. 
    this.savedValue = targetElement.value.toUpperCase();
    // 사용자에 의해 변경 중인 색상
    this.value = targetElement.value.toUpperCase() || 'transparent';
    // 서버에 저장된 사용자 색상 목록
    this.savedCustomColors = [];
    // 사용자에 의해 변경 중인 사용자 색상 목록
    this.customColors = [];
    // 선택된 색상 엘리먼트
    this.selectedEl = null;
    
    // 서버에 저장된 사용자 색상 조회
    aliceJs.fetchJson('/rest/users/colors', {
        method: 'GET'
    }).then((userColors) => {
        if (userColors.length > 0) {
            this.savedCustomColors = userColors[0].split('|');
            this.customColors = userColors[0].split('|');
        }
    });

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

    // 컬러 팔레트 아이콘
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

        if (self.isOpen) {
            // 사용자 색상이 저장된 색상과 다를 경우 알림창을 띄워 사용자에게 확인 요청
            if (JSON.stringify(self.savedCustomColors) !== JSON.stringify(self.customColors)) {
                aliceAlert.confirm(i18n.msg('user.msg.customColorSave'), () => {
                    // 색상 초기화
                    self.resetCustomColor();
                    // 닫기
                    self.close();
                });
            } else {
                self.close();
            }
        } else {
            self.open();
        }
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
            // 경고창 - 색상을 선택하세요.
            if (!this.selectedEl) {
                aliceAlert.alertWarning(i18n.msg('common.msg.selectColor'));
                return false;
            }

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
            // 사용자 색상이 저장된 색상과 다를 경우 알림창을 띄워 사용자에게 확인 요청
            if (JSON.stringify(this.savedCustomColors) !== JSON.stringify(this.customColors)) {
                aliceAlert.confirm(i18n.msg('user.msg.customColorSave'), () => {
                    // 색상 초기화
                    this.resetCustomColor();
                    // 닫기
                    this.close();
                });
            } else {
                this.close();
            }
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
        paletteContainer.querySelectorAll('.' + aliceJs.CLASS_PREFIX + 'palette-item').forEach((item) => {
            item.addEventListener('click', this.clickColorPalette.bind(this), false);
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
        editButton.className = aliceJs.CLASS_PREFIX + 'button-icon extra ' + aliceJs.CLASS_PREFIX + 'custom-color-edit on';
        editButton.insertAdjacentHTML('beforeend', `<span class="${aliceJs.CLASS_PREFIX}icon i-edit"></span>`);
        editButton.addEventListener('click', this.openCustomColorControl.bind(this), false);
        paletteContainer.appendChild(editButton);
        this.editButtonEl = editButton;

        // 사용자 색상 목록
        const customColorList = document.createElement('div');
        customColorList.className = aliceJs.CLASS_PREFIX + 'custom-color-list';
        paletteContainer.appendChild(customColorList);
        this.customColorListEl = customColorList;

        // 저장된 사용자 색상 값을 가져와서 표기한다.
        this.savedCustomColors.forEach(color => {
            this.customColorListEl.insertAdjacentHTML('beforeend', this.getCustomColorTemplate(color, (this.value === color)));
            // 이벤트 등록
            const colorItem = this.customColorListEl.lastChild;
            colorItem.addEventListener('click', this.clickCustomColorPalette.bind(this), false);
            colorItem.querySelector('.' + aliceJs.CLASS_PREFIX + 'custom-color-palette-item-clear')
                .addEventListener('click', this.removeCustomColor.bind(this), false);
        });

        // 사용자 색상 목록 추가 버튼 (최대 10개까지 등록가능)
        const addButton = document.createElement('button');
        addButton.type = 'button';
        addButton.className = aliceJs.CLASS_PREFIX + 'button-icon ' + aliceJs.CLASS_PREFIX + 'custom-color-plus';
        addButton.insertAdjacentHTML('beforeend', `<span class="${aliceJs.CLASS_PREFIX}icon i-plus"></span>`);
        addButton.addEventListener('click', this.openCustomColorControl.bind(this), false);
        this.addButtonEl = addButton;
        customColorList.appendChild(addButton);

        // 사용자 색상 control container
        const customColorControlContainer = document.createElement('div');
        customColorControlContainer.className = aliceJs.CLASS_PREFIX + 'custom-color-control-container';
        paletteContainer.appendChild(customColorControlContainer);
        this.customColorControlContainerEl = customColorControlContainer;
        
        // 사용자 색상 control 영역 출력
        this.drawCustomColorControl();
    },
    // 사용자 색상 control(물방울, hex, rgb 영역) draw
    drawCustomColorControl() {
        const customColorControl = document.createElement('div');
        customColorControl.className = aliceJs.CLASS_PREFIX + 'custom-color-control';
        this.customColorControlContainerEl.appendChild(customColorControl);

        // 물방울
        const waterDrop = document.createElement('span');
        waterDrop.className = aliceJs.CLASS_PREFIX + 'icon i-water-drop';
        waterDrop.style.setProperty('--data-color', '#8B9094');
        waterDrop.insertAdjacentHTML('beforeend', this.getWaterDropSvg());
        customColorControl.appendChild(waterDrop);
        this.waterDropEl = waterDrop;

        // hex
        const hexInput = document.createElement('input');
        hexInput.type = 'text';
        hexInput.className = aliceJs.CLASS_PREFIX + 'input ' + aliceJs.CLASS_PREFIX + 'color-hex';
        hexInput.placeholder = '#FFFFFF';
        hexInput.setAttribute('maxlength', '7');
        //hexInput.value = '';
        hexInput.addEventListener('keyup', this.setHex.bind(this), false);
        customColorControl.appendChild(hexInput);
        this.hexEl = hexInput;

        // rgb
        ['r', 'g', 'b'].forEach((str) => {
            const rgbInput = document.createElement('input');
            rgbInput.type = 'text';
            rgbInput.className = aliceJs.CLASS_PREFIX + 'input ' + aliceJs.CLASS_PREFIX + 'color-' + str;
            rgbInput.placeholder = '255';
            rgbInput.setAttribute('maxlength', '3');
            rgbInput.addEventListener('keyup', this.setRgb.bind(this), false);
            customColorControl.appendChild(rgbInput);
            this[str + 'El'] = rgbInput;
        });

        // 추가 버튼
        const addButton = document.createElement('button');
        addButton.type = 'button';
        addButton.className = aliceJs.CLASS_PREFIX + 'button secondary';
        addButton.textContent = i18n.msg('common.btn.add');
        addButton.addEventListener('click', this.addCustomColor.bind(this), false);
        customColorControl.appendChild(addButton);

        // Hex, R,G,B 문구 추가
        ['', 'Hex', 'R', 'G', 'B'].forEach((str) => {
            const colorText = document.createElement('span');
            colorText.className = aliceJs.CLASS_PREFIX + 'color-text';
            colorText.textContent = str;
            customColorControl.appendChild(colorText);
        });
        
        // 버튼 그룹
        let bottomButtonList = document.createElement('div');
        bottomButtonList.className = aliceJs.CLASS_PREFIX + 'button-list justify-content-end';
        this.customColorControlContainerEl.appendChild(bottomButtonList);

        // 버튼 그룹 > 저장 버튼
        const saveButton = document.createElement('button');
        saveButton.type = 'button';
        saveButton.className = aliceJs.CLASS_PREFIX + 'button primary';
        saveButton.textContent = i18n.msg('common.btn.save');
        saveButton.addEventListener('click', this.saveCustomColor.bind(this), false);
        bottomButtonList.appendChild(saveButton);

        // 버튼 그룹 > 취소 버튼
        const cancelButton = document.createElement('button');
        cancelButton.type = 'button';
        cancelButton.className = aliceJs.CLASS_PREFIX + 'button extra';
        cancelButton.textContent = i18n.msg('common.btn.cancel');
        cancelButton.addEventListener('click', () => {
            // 알림창 - 사용자 색상이 아직 저장되지 않았습니다.
            if (JSON.stringify(this.savedCustomColors) !== JSON.stringify(this.customColors)) {
                aliceAlert.confirm(i18n.msg('user.msg.customColorSave'), () => {
                    // 색상 초기화
                    this.resetCustomColor();
                    // 사용자 색상 control 영역 닫기
                    this.cancelCustomColor();
                });
            } else {
                this.cancelCustomColor();
            }
        });
        bottomButtonList.appendChild(cancelButton);
    },
    // 물방울 svg 생성
    getWaterDropSvg() {
        // 물방울 아이콘은 색상 미입력시 투명하게 비워져 있고 색상 입력시 채워진다.
        return `<svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 28 28">
            <g transform="translate(-37 -386)">
                <path class="water-drop-inner" fill="#8b9094" fill-opacity="0"
                      d="M191,1574.949a9,9,0,1,1-18,0,8.916,8.916,0,0,1,.709-3.518,9.121,9.121,0,0,1,1.935-2.888L182,1561l6.356,7.543A9.08,9.08,0,0,1,191,1574.949Z"
                      transform="translate(-131 -1173)"/>
                <path class="water-drop-outer" fill="#8b9094"
                      d="M150.36,1568.54,144,1561l-6.36,7.54a9.153,9.153,0,0,0-1.93,2.89,9.011,9.011,0,1,0,17.29,3.52A9.09,9.09,0,0,0,150.36,1568.54ZM144,1582a7.031,7.031,0,0,1-7-7.05,6.878,6.878,0,0,1,.55-2.74,7.225,7.225,0,0,1,1.51-2.26l.06-.06.05-.06,4.83-5.73Z"
                      transform="translate(-93 -1173)"/>
                <rect fill="none" width="28" height="28" rx="2" transform="translate(37 386)"/>
            </g>
        </svg>`;
    },
    // Color Palette 선택
    clickColorPalette(e) {
        e.stopPropagation();
        // 기존 색상 조기화
        if (this.selectedEl) {
            this.selectedEl.classList.remove('selected');
            this.selectedEl.style.backgroundColor = this.selectedEl.getAttribute('data-color');
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
        // 메인 색상 선택시 원래 사용자 색상을 변경한다.
        this.savedValue = this.value;

        // 사용자 색상이 저장된 색상과 다를 경우 알림창을 띄워 사용자에게 확인 요청
        if (JSON.stringify(this.savedCustomColors) !== JSON.stringify(this.customColors)) {
            aliceAlert.confirm(i18n.msg('user.msg.customColorSave'), () => {
                // 색상 초기화
                this.resetCustomColor();
                // 닫기
                this.close();
            });
        } else {
            this.close();
        }
    },
    // 사용자 색상 오픈
    openCustomColorControl() {
        // 사용자 색상 편집 영역 오픈
        if (!this.customColorControlContainerEl.classList.contains('active')) {
            this.customColorControlContainerEl.classList.add('active');
            this.setPosition();

            // edit 버튼 숨기기
            this.editButtonEl.classList.remove('on');

            // 사용자 색상 삭제(x) 아이콘 표시
            this.customColorListEl.classList.add('editable');
        }
    },
    // 커스텀 색상 아이템 템플릿
    getCustomColorTemplate(color, isSelected) {
        return `<span class="${aliceJs.CLASS_PREFIX}custom-color-palette-item custom-color${isSelected ? ' selected' : ''}"`+
            ` data-color="${color}" style="background-color: ${isSelected ? 'transparent' : color};" >`+
            `<sapn class="${aliceJs.CLASS_PREFIX}custom-color-palette-item-inner" style="background-color: ${color}"></sapn>` +
            `<button type="button" class="${aliceJs.CLASS_PREFIX}button-icon ${aliceJs.CLASS_PREFIX}custom-color-palette-item-clear">` +
            `<span class="${aliceJs.CLASS_PREFIX}icon i-clear"></span>` +
            `</button>` +
            `</span>`;
    },
    // 추가 버튼 클릭 > 사용자 색상 추가
    addCustomColor() {
        // hex 유효성 검증
        if (!this.options.hexReg.test(this.hexEl.value)) { return false; }
        
        // 색상 추가
        this.customColorListEl.insertAdjacentHTML('beforeend', this.getCustomColorTemplate(this.hexEl.value, true));
        this.customColors.push(this.hexEl.value);

        // 이벤트 등록
        const colorItem = this.customColorListEl.lastChild;
        colorItem.addEventListener('click', this.clickCustomColorPalette.bind(this), false);
        colorItem.querySelector('.' + aliceJs.CLASS_PREFIX + 'custom-color-palette-item-clear')
            .addEventListener('click', this.removeCustomColor.bind(this), false);

        // 순서 변경
        aliceJs.swapNode(colorItem, this.addButtonEl);

        // 색상 선택
        colorItem.dispatchEvent(new Event('click'));
        this.selectedEl = colorItem;
        
        // 기존 색상 초기화
        this.waterDropEl.classList.remove('on');
        this.hexEl.value = '';
        ['r', 'g', 'b'].forEach((str) => {
            this[str + 'El'].value = '';
        });
        
    },
    // x 버튼 클릭 > 사용자 색상 삭제
    removeCustomColor(e) {
        e.stopPropagation();
        
        const removeItem = e.target.parentNode;
        const removeColor = removeItem.getAttribute('data-color');

        const index = this.customColors.indexOf(removeColor);
        if (index !== -1) {
            this.customColors.splice(index, 1);
        }

        if (removeColor === this.selectedEl.getAttribute('data-color')) {
            this.selectedEl = null;
        }
        
        // 엘리먼트 삭제
        removeItem.parentNode.removeChild(removeItem);

    },
    // 사용자 색상 선택
    clickCustomColorPalette(e) {
        e.stopPropagation();
        
        // 기존 색상 초기화
        if (this.selectedEl) {
            this.selectedEl.classList.remove('selected');
            this.selectedEl.style.backgroundColor = this.selectedEl.getAttribute('data-color');
            this.selectedEl = null;
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
    },
    // 사용자 색상 저장
    // ※ Hex, RGB코드를 입력하여 추가된 색상이 사용자 색상에 저장되기 위해 반드시 추가한 후 하단의 저장 버튼을 선택 해야한다.
    saveCustomColor() {
        console.log(this.customColors);
        aliceJs.fetchJson('/rest/users/colors', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ customValue: this.customColors.join('|') }),
            showProgressbar: true
        }).then((rtn) => {
            if (rtn) {
                this.savedCustomColors = JSON.stringify(this.customColors);
            }
        });

    },
    // 취소 버튼 클릭 > 사용자 색상 취소
    cancelCustomColor() {
        // 사용자 색상 편집 영역 닫기
        if (this.customColorControlContainerEl.classList.contains('active')) {
            this.customColorControlContainerEl.classList.remove('active');
            this.setPosition();

            // edit 버튼 표시
            this.editButtonEl.classList.add('on');

            // 사용자 색상 삭제(x) 아이콘 삭제
            this.customColorListEl.classList.remove('editable');
        }
    },
    // 사용자 색상 초기화
    resetCustomColor(e) {
        console.log('reset=======');
        console.log(this);
        console.log(e);
        console.log('============');
    },
    // Rgb 입력시 호출
    setRgb() {
        // 초기화
        this.waterDropEl.classList.remove('on');
        this.hexEl.value = '';

        // rgb 유효성 검증
        let isRgbValid = true;
        ['r', 'g', 'b'].some((str) => {
            isRgbValid = this.options.rgbReg.test(this[str + 'El'].value);
            return !isRgbValid;
        });
        if (!isRgbValid) { return false; }
        
        // hex 자동 변경
        const hexColor = this.rgbToHex(this.rEl.value, this.gEl.value, this.bEl.value);
        this.hexEl.value = hexColor;
        
        // 물방울 변경
        this.waterDropEl.style.setProperty('--data-color', hexColor);
        if (!this.waterDropEl.classList.contains('on')) {
            this.waterDropEl.classList.add('on');
        }
    },
    // Hex 입력시 호출
    setHex(e) {
        // 초기화
        this.waterDropEl.classList.remove('on');
        ['r', 'g', 'b'].forEach((str) => {
            this[str + 'El'].value = '';
        });

        // hex 유효성 검증
        if (!this.options.hexReg.test(e.target.value)) { return false; }
        
        // rgb 입력
        const rgbColor = this.hexToRGB(e.target.value);
        this.rEl.value = rgbColor[0];
        this.gEl.value = rgbColor[1];
        this.bEl.value = rgbColor[2];

        // 물방울 변경
        this.waterDropEl.style.setProperty('--data-color', e.target.value);
        if (!this.waterDropEl.classList.contains('on')) {
            this.waterDropEl.classList.add('on');
        }
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

