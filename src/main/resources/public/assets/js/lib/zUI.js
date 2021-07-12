/**
 * DOM Element 를 생성하는 클래스
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import { CLASS_PREFIX } from './zConstants.js';

class UIElement {
    constructor(domElement) {
        this.domElement = domElement;
    }

    addUI() {
        for (let i = 0; i < arguments.length; i++) {
            const argument = arguments[i];
            if (argument instanceof UIElement) {
                this.domElement.appendChild(argument.domElement);
            } else {
                console.error('UIElement:', argument, 'is not an instance of UIElement.');
            }
        }
        return this;
    }

    removeUI() {
        for (let i = 0; i < arguments.length; i++) {
            const argument = arguments[i];
            if (argument instanceof UIElement) {
                this.domElement.removeChild(argument.domElement);
            } else {
                console.error('UIElement:', argument, 'is not an instance of UIElement.');
            }
        }
        return this;
    }

    clearUI() {
        while (this.domElement.children.length) {
            this.domElement.removeChild(this.domElement.lastChild);
        }
    }

    setUIId(id) {
        this.domElement.id = id;
        return this;
    }

    getUIId() {
        return this.domElement.id;
    }

    setUIClass(name) {
        this.domElement.className = name;
        return this;
    }

    addUIClass(name) {
        this.domElement.classList.add(name);
        return this;
    }

    removeUIClass(name) {
        this.domElement.classList.remove(name);
        return this;
    }

    hasUIClass(name) {
        return this.domElement.classList.contains(name);
    }

    setUIAttribute(name, value) {
        this.domElement.setAttribute(name, value);
        return this;
    }

    setUICSSText(value) {
        this.domElement.style.cssText = value;
        return this;
    }

    setUIStyle(style, array) {
        for (let i = 0; i < array.length; i++) {
            this.domElement.style[style] = array[i];
        }
        return this;
    }

    setUIProperty(style, value) {
        this.domElement.style.setProperty(style, value);
        return this;
    }

    setUIDisabled(value) {
        this.domElement.disabled = value;
        return this;
    }

    setUITextContent(value) {
        this.domElement.textContent = value;
        return this;
    }

    getUIIndexOfChild(element) {
        return Array.prototype.indexOf.call(this.domElement.children, element.domElement);
    }
}

// properties
const properties = [
    'position', 'left', 'top', 'right', 'bottom', 'width', 'height',
    'border', 'borderLeft', 'borderTop', 'borderRight', 'borderBottom', 'borderColor',
    'display', 'overflow', 'margin', 'marginLeft', 'marginTop', 'marginRight', 'marginBottom',
    'padding', 'paddingLeft', 'paddingTop', 'paddingRight', 'paddingBottom', 'color',
    'background', 'backgroundColor', 'opacity', 'fontSize', 'fontWeight', 'fontStyle', 'textAlign',
    'textDecoration', 'textTransform', 'cursor', 'zIndex',
];
properties.forEach(function (property) {
    const method = 'setUI' + property.substr(0, 1).toUpperCase() +
        property.substr(1, property.length);
    UIElement.prototype[method] = function () {
        this.setUIStyle(property, arguments);
        return this;
    };
});

// events
const events = ['KeyUp', 'KeyDown', 'MouseOver', 'MouseOut', 'Click', 'DblClick', 'Change', 'Input', 'Focusout'];
events.forEach(function (event) {
    const method = 'onUI' + event;
    UIElement.prototype[method] = function (callback) {
        this.domElement.addEventListener(event.toLowerCase(), callback.bind(this), false);
        return this;
    };
});

class UISpan extends UIElement {
    constructor() {
        super(document.createElement('span'));
    }
    setUIInnerHTML(value) {
        this.domElement.innerHTML = value;
        return this;
    }
}

class UILabel extends UIElement {
    constructor() {
        super(document.createElement('label'));
    }

    setUIFor(id) {
        this.domElement.htmlFor = id;
        return this;
    }
}

class UIDiv extends UIElement {
    constructor() {
        super(document.createElement('div'));
    }
}

class UIText extends UISpan {
    constructor(text) {
        super();
        this.domElement.className = 'text';
        this.domElement.style.cursor = 'default';
        this.domElement.style.display = 'inline-block';
        this.domElement.style.verticalAlign = 'middle';

        this.setUIValue(text);
    }

    getUIValue() {
        return this.domElement.textContent;
    }

    setUIValue(value) {
        if (value !== undefined) {
            this.domElement.textContent = value;
        }
        return this;
    }
}

class UIInput extends UIElement {
    constructor(text) {
        super(document.createElement('input'));
        this.domElement.type = 'text';
        this.domElement.className = CLASS_PREFIX + 'input';

        this.domElement.addEventListener(
            'keydown',
            function (event) { event.stopPropagation(); },
            false
        );

        this.setUIValue(text);
    }

    getUIValue() {
        return this.domElement.value;
    }

    setUIValue(value) {
        this.domElement.value = value;
        return this;
    }

    setUIType(type) {
        this.domElement.type = type;
        return this;
    }

    setUIPlaceholder(value) {
        this.domElement.placeholder = value;
        return this;
    }

    setUIRequired(boolean) {
        this.domElement.required = boolean;
        return this;
    }

    setUIReadOnly(boolean) {
        this.domElement.readOnly = boolean;
        return this;
    }
}

class UITextArea extends UIElement {
    constructor() {
        super(document.createElement('textarea'));
        this.domElement.className = CLASS_PREFIX + 'textarea';
        this.domElement.style.padding = '10px';
        this.domElement.spellcheck = false;

        this.domElement.addEventListener (
            'keydown',
            function (event) {
                event.stopPropagation();

                if (event.keyCode === 9) {
                    // TAB 일 경우
                    event.preventDefault();
                    const cursor = event.target.selectionStart;
                    event.target.value = event.target.value.substring(0, cursor) + '\t' + event.target.value.substring(cursor);
                    event.target.selectionStart = cursor + 1;
                    event.target.selectionEnd = event.target.selectionStart;
                }
            }, false);
    }

    getUIValue() {
        return this.domElement.value;
    }

    setUIValue(value) {
        this.domElement.value = value;
        return this;
    }

    setUIPlaceholder(value) {
        this.domElement.placeholder = value;
        return this;
    }
}
// TODO: 디자인용 selectbox로 변경
class UISelect extends UIElement {
    constructor() {
        super(document.createElement('select'));
        this.domElement.className = 'select';
    }

    setUIMultiple(boolean) {
        this.domElement.multiple = boolean;
        return this;
    }

    setUIOptions(options) {
        const selected = this.domElement.value;
        while (this.domElement.children.length > 0) {
            this.domElement.removeChild(this.domElement.firstChild);
        }

        for (const index in options) {
            const option = document.createElement('option');
            option.value = options[index].value;
            option.innerHTML = options[index].name;
            this.domElement.appendChild(option);
        }
        this.domElement.value = selected;

        return this;
    }

    getUIValue() {
        return this.domElement.value;
    }

    setUIValue(value) {
        value = String(value);

        if (this.domElement.value !== value) {
            this.domElement.value = value;
        }

        return this;
    }
}

class UICheckbox extends UIElement {
    constructor(boolean) {
        super(document.createElement('input'));
        this.domElement.className = 'checkbox';
        this.domElement.type = 'checkbox';
        this.setUIValue(boolean);
    }

    getUIValue() {
        return this.domElement.checked;
    }

    setUIValue(value) {
        if (value !== undefined) {
            this.domElement.checked = value;
        }

        return this;
    }
}

class UIRadioButton extends UIElement {
    constructor(boolean) {
        super(document.createElement('input'));
        this.domElement.className = 'radio';
        this.domElement.type = 'radio';
        this.setUIValue(boolean);
    }

    getUIValue() {
        return this.domElement.checked;
    }

    setUIValue(value) {
        if (value !== undefined) {
            this.domElement.checked = value;
        }

        return this;
    }
}
// 공통으로 사용되는 복사용 inputbox + button
class UIClipboard extends UIElement {
    constructor() {
        super(document.createElement('div'));
        this.domElement.className = CLASS_PREFIX + 'clipboard';
        // input
        this.UIInput = new UIInput().addUIClass('copy').setUIReadOnly(true);
        this.addUI(this.UIInput);

        // tooptip
        this.UITooltip = new UIDiv().setUIClass(CLASS_PREFIX + 'clipboard-tooltip');
        this.addUI(this.UITooltip);

        // copy button
        const scope = this;
        this.UITooltip.UIButton = new UIButton().setUIClass(CLASS_PREFIX + 'button-icon').addUIClass('form');
        this.UITooltip.UIButton.domElement.addEventListener('click', function () {
            scope.UIInput.domElement.select();
            scope.UIInput.domElement.setSelectionRange(0, 99999);
            document.execCommand('copy');

            scope.UITooltip.UITooptipText.setUITextContent('Copy success');
        });
        this.UITooltip.UIButton.domElement.addEventListener('mouseout', function () {
            scope.UITooltip.UITooptipText.setUITextContent('Copy to clipboard');
        });
        this.UITooltip.addUI(this.UITooltip.UIButton);

        // copy button icon
        const UIButtonIcon = new UISpan().setUIClass(CLASS_PREFIX + 'icon').addUIClass('i-clipboard');
        this.UITooltip.UIButton.addUI(UIButtonIcon);

        // tooltip text
        this.UITooltip.UITooptipText = new UISpan().setUIClass(CLASS_PREFIX + 'clipboard-tooltip-text')
            .setUITextContent('Copy to clipboard');
        this.UITooltip.UIButton.addUI(this.UITooltip.UITooptipText);
    }
}
// TODO: color picker 라이브러리 참조하여 엘리먼트 만들기
class UIColor extends UIElement {
    constructor(option) {
        super(document.createElement('div'));
        this.domElement.className = 'color-picker';

        // input box
        this.UIColor = new UIDiv().setUIClass('color-input');
        this.UIColor.UIBox = new UIDiv().setUIClass('selected-color-box');

        this.UIColor.UIBox.UISpan = new UISpan().setUIClass('selected-color')
            .setUIBackgroundColor(option.data.value);
        this.UIColor.UIBox.addUI(this.UIColor.UIBox.UISpan);
        this.UIColor.addUI(this.UIColor.UIBox);

        this.UIColor.UIInput = new UIInput().addUIClass('color').setUIReadOnly(true)
            .setUIValue(option.data.value);
        this.UIColor.addUI(this.UIColor.UIInput);
        this.addUI(this.UIColor);

        // color palette layer
        this.UIColorPalette = new UIDiv();
        this.UIColorPalette.UIColor = new UIDiv().setUIClass('color-palette');
        this.UIColorPalette.addUI(this.UIColorPalette.UIColor);

        this.UIColorPalette.UIOpacity = new UIDiv().setUIClass('color-palette-opacity');
        this.UIColorPalette.addUI(this.UIColorPalette.UIOpacity);
        this.addUI(this.UIColorPalette);

        // color picker 초기화
        zColorPalette.initColorPalette(this.UIColorPalette.domElement,
            this.UIColor.UIBox.UISpan.domElement, this.UIColor.UIInput.domElement, option);
    }

    setUIId(id) {
        this.UIColor.UIInput.setUIId(id);
        this.UIColorPalette.setUIId('colorPaletteLayer-' + id);
        return this;
    }
}

class UISwitch extends UIElement {
    constructor(boolean) {
        super(document.createElement('label'));
        this.domElement.className = CLASS_PREFIX + 'switch';

        // checkbox
        this.UICheckbox = new UICheckbox(boolean);
        this.addUI(this.UICheckbox);
        this.addUI(new UISpan());

        // label
        this.UISpan = new UISpan().setUIClass('label');
        this.addUI(this.UISpan);
    }

    setUIId(id) {
        this.domElement.id = id;
        this.UICheckbox.setUIId(id);
        return this;
    }

    setUITextContent(value) {
        this.UISpan.setUITextContent(value);
        return this;
    }
}

class UIBreak extends UIElement {
    constructor() {
        super(document.createElement('br'));
        this.domElement.className = 'Break';
    }
}

class UIHorizontalRule extends UIElement {
    constructor() {
        super(document.createElement('hr'));
        this.domElement.className = 'HorizontalRule';
    }

    setUIThickness(value) {
        this.domElement.style.borderTopWidth = value;
    }

    setUIStyle(color, type) {
        this.domElement.style.borderTopColor = color;
        this.domElement.style.borderTopStyle = type;
    }
}

class UIButton extends UIElement {
    constructor(value) {
        super(document.createElement('button'));
        this.domElement.className = CLASS_PREFIX + 'button';
        this.domElement.type = 'button';
        this.domElement.textContent = value;
    }
}

class UISlider extends UIElement {
    constructor(value) {
        super(document.createElement('div'));
        this.domElement.className = 'slider';
        // range
        this.UIRange = new UIInput(value).setUIClass('range');
        this.UIRange.domElement.type = 'range';
        this.addUI(this.UIRange);
        // input
        this.UIInput = new UIInput(value).setUIReadOnly(true);
        this.addUI(this.UIInput);

        const scope = this;
        function onInput() {
            scope.UIInput.setUIValue(scope.UIRange.getUIValue());

            const changeEvent = document.createEvent('HTMLEvents');
            changeEvent.initEvent('change', true, true);
            scope.UIInput.domElement.dispatchEvent(changeEvent);
        }

        this.UIRange.domElement.addEventListener('input', onInput, false);
    }

    setUIMin(value) {
        this.UIRange.domElement.setAttribute('min', value);
        return this;
    }

    setUIMax(value) {
        this.UIRange.domElement.setAttribute('max', value);
        return this;
    }
}

class UIUl extends UIElement {
    constructor() {
        super(document.createElement('ul'));
    }
}

class UILi extends UIElement {
    constructor() {
        super(document.createElement('li'));
    }
}

class UIImg extends UIElement {
    constructor() {
        super(document.createElement('img'));
    }

    setUISrc(value) {
        this.domElement.src = value;
        return this;
    }
}

class UITable extends UIElement {
    constructor() {
        super(document.createElement('table'));
        this.rows = [];
    }

    addUIRow(row) {
        this.rows.push(row);
        this.addUI(row);
        return this;
    }

    removeUIRow(row) {
        const index = this.rows.indexOf(row);
        if (index !== -1) {
            this.removeUI(row);
            row.parent = null;
            this.rows.splice(index, 1);
        }
        return this;
    }

    clearUIRow() {
        this.rows = [];
        return this;
    }

    updateUIRow(index, row) {
        if (index !== -1) {
            const updateRow = this.rows[index];
            this.domElement.insertBefore(row.domElement, updateRow.domElement);
            this.removeUIRow(updateRow);
            this.rows.splice(index, 1, row);
        }
        return this;
    }

    getIndexUIRow(row) {
        return this.rows.indexOf(row);
    }
}

class UIRow extends UIElement {
    constructor(table) {
        super(document.createElement('tr'));

        this.parent = table;
        this.cells = [];
    }

    addUICell(cell) {
        this.cells.push(cell);
        this.addUI(cell);
        return this;
    }

    removeUICell(cell) {
        const index = this.cells.indexOf(cell);
        if (index !== -1) {
            this.removeUI(cell);
            cell.parent = null;
            this.cells.splice(index, 1);
        }
        return this;
    }
    getUIIndex() {
        return this.domElement.rowIndex;
    }
}

class UICell extends UIElement {
    constructor(row) {
        super(document.createElement('td'));

        this.parent = row;
    }

    setRowspan(value) {
        this.domElement.setAttribute('rowspan', value);
        return this;
    }

    setColspan(value) {
        this.domElement.setAttribute('colspan', value);
        return this;
    }
}

export {
    UIElement, UISpan, UILabel, UIDiv, UIText, UIInput, UITextArea,
    UISelect, UICheckbox, UIClipboard, UIColor, UISwitch, UIBreak,
    UIHorizontalRule, UIButton, UISlider, UIUl, UILi, UIImg, UITable,
    UIRow, UICell, UIRadioButton
};
