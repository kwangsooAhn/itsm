/**
 * DOM Element 를 생성하는 클래스
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

class UIElement {
    constructor(domElement) {
        this.domElement = domElement;
    }

    add() {
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

    remove() {
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

    clear() {
        while (this.domElement.children.length) {
            this.domElement.removeChild(this.domElement.lastChild);
        }
    }

    setId(id) {
        this.domElement.id = id;
        return this;
    }

    getId() {
        return this.domElement.id;
    }

    setClass(name) {
        this.domElement.className = name;
        return this;
    }

    addClass(name) {
        this.domElement.classList.add(name);
        return this;
    }

    removeClass(name) {
        this.domElement.classList.remove(name);
        return this;
    }

    setAttribute(name, value) {
        this.domElement.setAttribute(name, value);
        return this;
    }

    setCSSText(value) {
        this.domElement.style.cssText = value;
        return this;
    }

    setStyle(style, array) {
        for (let i = 0; i < array.length; i++) {
            this.domElement.style[style] = array[i];
        }
        return this;
    }

    setProperty(style, value) {
        this.domElement.style.setProperty(style, value);
        return this;
    }

    setDisabled(value) {
        this.domElement.disabled = value;
        return this;
    }

    setTextContent(value) {
        this.domElement.textContent = value;
        return this;
    }

    getIndexOfChild(element) {
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
    const method = 'set' + property.substr(0, 1).toUpperCase() +
        property.substr(1, property.length);
    UIElement.prototype[method] = function () {
        this.setStyle(property, arguments);
        return this;
    };
});

// events
const events = ['KeyUp', 'KeyDown', 'MouseOver', 'MouseOut', 'Click', 'DblClick', 'Change', 'Input'];
events.forEach(function (event) {
    const method = 'on' + event;
    UIElement.prototype[method] = function (callback) {
        this.domElement.addEventListener(event.toLowerCase(), callback.bind(this), false);
        return this;
    };
});

class UISpan extends UIElement {
    constructor() {
        super(document.createElement('span'));
    }
    setInnerHTML(value) {
        this.domElement.innerHTML = value;
        return this;
    }
}

class UILabel extends UIElement {
    constructor() {
        super(document.createElement('label'));
    }

    setFor(id) {
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

        this.setValue(text);
    }

    getValue() {
        return this.domElement.textContent;
    }

    setValue(value) {
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
        this.domElement.className = 'input';

        this.domElement.addEventListener(
            'keydown',
            function (event) {
                event.stopPropagation();
            },
            false
        );

        this.setValue(text);
    }

    getValue() {
        return this.domElement.value;
    }

    setValue(value) {
        this.domElement.value = value;
        return this;
    }

    setPlaceholder(value) {
        this.domElement.placeholder = value;
        return this;
    }

    setRequired(boolean) {
        this.domElement.required = boolean;
        return this;
    }

    setReadOnly(boolean) {
        this.domElement.readOnly = boolean;
        return this;
    }
}

class UITextArea extends UIElement {
    constructor() {
        super(document.createElement('textarea'));
        this.domElement.className = 'textArea';
        this.domElement.style.padding = '10px';
        this.domElement.spellcheck = false;

        this.domElement.addEventListener(
            'keydown',
            function (event) {
                event.stopPropagation();

                if (event.keyCode === 9) {
                    // TAB 일 경우
                    event.preventDefault();

                    const cursor = this.domElement.selectionStart;
                    this.domElement.value = this.domElement.value.substring(0, cursor) + '\t' + this.domElement.value.substring(cursor);
                    this.domElement.selectionStart = cursor + 1;
                    this.domElement.selectionEnd = this.domElement.selectionStart;
                }
            },
            false
        );
    }

    getValue() {
        return this.domElement.value;
    }

    setValue(value) {
        this.domElement.value = value;
        return this;
    }
}
// TODO: 디자인용 selectbox로 변경
class UISelect extends UIElement {
    constructor() {
        super(document.createElement('select'));
        this.domElement.className = 'select';
        this.domElement.style.padding = '0 2px';
    }

    setMultiple(boolean) {
        this.domElement.multiple = boolean;
        return this;
    }

    setOptions(options) {
        const selected = this.domElement.value;
        while (this.domElement.children.length > 0) {
            this.domElement.removeChild(this.domElement.firstChild);
        }

        for (const key in options) {
            const option = document.createElement('option');
            option.value = key;
            option.innerHTML = options[key];
            this.domElement.appendChild(option);
        }
        this.domElement.value = selected;

        return this;
    }

    getValue() {
        return this.domElement.value;
    }

    setValue(value) {
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
        this.setValue(boolean);
    }

    getValue() {
        return this.domElement.checked;
    }

    setValue(value) {
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
        this.domElement.className = 'clipboard';
        // input
        this.UIInput = new UIInput().addClass('copy').setReadOnly(true);
        this.add(this.UIInput);

        // tooptip
        this.UITooltip = new UIDiv().setClass('clipboard-tooltip');
        this.add(this.UITooltip);

        // copy button
        const scope = this;
        this.UITooltip.UIButton = new UIButton().setClass('btn-clipboard-tooltip').addClass('ghost-line');
        this.UITooltip.UIButton.domElement.addEventListener('click', function () {
            scope.UIInput.domElement.select();
            scope.UIInput.domElement.setSelectionRange(0, 99999);
            document.execCommand('copy');

            scope.UITooltip.UITooptipText.setTextContent('Copy success');
        });
        this.UITooltip.UIButton.domElement.addEventListener('mouseout', function () {
            scope.UITooltip.UITooptipText.setTextContent('Copy to clipboard');
        });
        this.UITooltip.add(this.UITooltip.UIButton);

        // copy button icon
        const UIButtonIcon = new UISpan().setClass('icon').addClass('icon-clipboard');
        this.UITooltip.UIButton.add(UIButtonIcon);

        // tooltip text
        this.UITooltip.UITooptipText = new UISpan().setClass('clipboard-tooltip-text')
            .setTextContent('Copy to clipboard');
        this.UITooltip.UIButton.add(this.UITooltip.UITooptipText);
    }
}

// TODO: color picker 라이브러리 참조하여 엘리먼트 만들기
class UIColor extends UIElement {
    constructor(option) {
        super(document.createElement('div'));
        this.domElement.className = 'color-picker';

        // input box
        this.UIColor = new UIDiv().setClass('color-input');
        this.UIColor.UIBox = new UIDiv().setClass('selected-color-box');

        this.UIColor.UIBox.UISpan = new UISpan().setClass('selected-color')
            .setBackgroundColor(option.data.value);
        this.UIColor.UIBox.add(this.UIColor.UIBox.UISpan);
        this.UIColor.add(this.UIColor.UIBox);

        this.UIColor.UIInput = new UIInput().addClass('color').setReadOnly(true)
            .setValue(option.data.value);
        this.UIColor.add(this.UIColor.UIInput);
        this.add(this.UIColor);

        // color palette layer
        this.UIColorPalette = new UIDiv();
        this.UIColorPalette.UIColor = new UIDiv().setClass('color-palette');
        this.UIColorPalette.add(this.UIColorPalette.UIColor);

        this.UIColorPalette.UIOpacity = new UIDiv().setClass('color-palette-opacity');
        this.UIColorPalette.add(this.UIColorPalette.UIOpacity);
        this.add(this.UIColorPalette);
        
        // color picker 초기화
        colorPalette.initColorPalette(this.UIColorPalette.domElement,
            this.UIColor.UIBox.UISpan.domElement, this.UIColor.UIInput.domElement, option);
    }

    setId(id) {
        this.UIColor.UIInput.setId(id);
        this.UIColorPalette.setId('colorPaletteLayer-' + id);
        return this;
    }
}

class UISwitch extends UIElement {
    constructor(boolean) {
        super(document.createElement('label'));
        this.domElement.className = 'switch';

        // checkbox
        this.UICheckbox = new UICheckbox(boolean);
        this.add(this.UICheckbox);
        this.add(new UISpan());

        // label
        this.UISpan = new UISpan().setClass('label');
        this.add(this.UISpan);
    }

    setId(id) {
        this.domElement.id = id;
        this.UICheckbox.setId(id);
        return this;
    }

    setTextContent(value) {
        this.UISpan.setTextContent(value);
        return this;
    }
}

class UINumber extends UIElement {
    constructor(number) {
        super(document.createElement('input'));

        this.domElement.style.cursor = 'ns-resize';
        this.domElement.className = 'Number';
        this.domElement.value = '0.00';
        this.value = 0;
        this.min = -Infinity;
        this.max = Infinity;
        this.precision = 2;
        this.step = 1;
        this.unit = '';
        this.nudge = 0.01;

        this.setValue(number);

        const scope = this;
        const changeEvent = document.createEvent('HTMLEvents');
        changeEvent.initEvent('change', true, true);

        let distance = 0;
        let onMouseDownValue = 0;

        const pointer = { x: 0, y: 0 };
        const prevPointer = { x: 0, y: 0 };

        function onMouseDown(event) {
            event.preventDefault();

            distance = 0;
            onMouseDownValue = scope.value;

            prevPointer.x = event.clientX;
            prevPointer.y = event.clientY;

            document.addEventListener('mousemove', onMouseMove, false);
            document.addEventListener('mouseup', onMouseUp, false);
        }

        function onMouseMove(event) {
            const currentValue = scope.value;

            pointer.x = event.clientX;
            pointer.y = event.clientY;

            distance += pointer.x - prevPointer.x - (pointer.y - prevPointer.y);

            let value = onMouseDownValue + (distance / (event.shiftKey ? 5 : 50)) * scope.step;
            value = Math.min(scope.max, Math.max(scope.min, value));

            if (currentValue !== value) {
                scope.setValue(value);
                scope.domElement.dispatchEvent(changeEvent);
            }
            prevPointer.x = event.clientX;
            prevPointer.y = event.clientY;
        }

        function onMouseUp() {
            document.removeEventListener('mousemove', onMouseMove, false);
            document.removeEventListener('mouseup', onMouseUp, false);

            if (Math.abs(distance) < 2) {
                scope.domElement.focus();
                scope.domElement.select();
            }
        }

        function onTouchStart(event) {
            if (event.touches.length === 1) {
                distance = 0;
                onMouseDownValue = scope.value;

                prevPointer.x = event.touches[0].pageX;
                prevPointer.y = event.touches[0].pageY;

                document.addEventListener('touchmove', onTouchMove, false);
                document.addEventListener('touchend', onTouchEnd, false);
            }
        }

        function onTouchMove(event) {
            const currentValue = scope.value;

            pointer.x = event.touches[0].pageX;
            pointer.y = event.touches[0].pageY;

            distance += pointer.x - prevPointer.x - (pointer.y - prevPointer.y);

            let value = onMouseDownValue + (distance / (event.shiftKey ? 5 : 50)) * scope.step;
            value = Math.min(scope.max, Math.max(scope.min, value));

            if (currentValue !== value) {
                scope.setValue(value);
                scope.domElement.dispatchEvent(changeEvent);
            }

            prevPointer.x = event.touches[0].pageX;
            prevPointer.y = event.touches[0].pageY;
        }

        function onTouchEnd(event) {
            if (event.touches.length === 0) {
                document.removeEventListener('touchmove', onTouchMove, false);
                document.removeEventListener('touchend', onTouchEnd, false);
            }
        }

        function onChange() {
            scope.setValue(scope.domElement.value);
        }

        function onFocus() {
            scope.domElement.style.backgroundColor = '';
            scope.domElement.style.cursor = '';
        }

        function onBlur() {
            scope.domElement.style.backgroundColor = 'transparent';
            scope.domElement.style.cursor = 'ns-resize';
        }

        function onKeyDown(event) {
            event.stopPropagation();

            switch (event.keyCode) {
            case 13: // enter
                scope.domElement.blur();
                break;
            case 38: // up
                event.preventDefault();
                scope.setValue(scope.getValue() + scope.nudge);
                scope.domElement.dispatchEvent(changeEvent);
                break;
            case 40: // down
                event.preventDefault();
                scope.setValue(scope.getValue() - scope.nudge);
                scope.domElement.dispatchEvent(changeEvent);
                break;
            }
        }

        onBlur();

        this.domElement.addEventListener('keydown', onKeyDown, false);
        this.domElement.addEventListener('mousedown', onMouseDown, false);
        this.domElement.addEventListener('touchstart', onTouchStart, false);
        this.domElement.addEventListener('change', onChange, false);
        this.domElement.addEventListener('focus', onFocus, false);
        this.domElement.addEventListener('blur', onBlur, false);
    }

    getValue() {
        return this.value;
    }

    setValue(value) {
        if (value !== undefined) {
            value = parseFloat(value);

            if (value < this.min) value = this.min;
            if (value > this.max) value = this.max;

            this.value = value;
            this.domElement.value = value.toFixed(this.precision);

            if (this.unit !== '') this.domElement.value += ' ' + this.unit;
        }
        return this;
    }

    setPrecision(precision) {
        this.precision = precision;
        return this;
    }

    setStep(step) {
        this.step = step;
        return this;
    }

    setNudge(nudge) {
        this.nudge = nudge;
        return this;
    }

    setRange(min, max) {
        this.min = min;
        this.max = max;

        return this;
    }

    setUnit(unit) {
        this.unit = unit;
        return this;
    }
}

class UIInteger extends UIElement {
    constructor(number) {
        super(document.createElement('input'));

        this.domElement.style.cursor = 'ns-resize';
        this.domElement.className = 'Number';
        this.domElement.value = '0';

        this.value = 0;

        this.min = -Infinity;
        this.max = Infinity;

        this.step = 1;
        this.nudge = 1;

        this.setValue(number);

        const scope = this;

        const changeEvent = document.createEvent('HTMLEvents');
        changeEvent.initEvent('change', true, true);

        let distance = 0;
        let onMouseDownValue = 0;

        const pointer = { x: 0, y: 0 };
        const prevPointer = { x: 0, y: 0 };

        function onMouseDown(event) {
            event.preventDefault();

            distance = 0;

            onMouseDownValue = scope.value;

            prevPointer.x = event.clientX;
            prevPointer.y = event.clientY;

            document.addEventListener('mousemove', onMouseMove, false);
            document.addEventListener('mouseup', onMouseUp, false);
        }

        function onMouseMove(event) {
            const currentValue = scope.value;

            pointer.x = event.clientX;
            pointer.y = event.clientY;

            distance += pointer.x - prevPointer.x - (pointer.y - prevPointer.y);

            let value = onMouseDownValue + (distance / (event.shiftKey ? 5 : 50)) * scope.step;
            value = Math.min(scope.max, Math.max(scope.min, value)) | 0;

            if (currentValue !== value) {
                scope.setValue(value);
                scope.domElement.dispatchEvent(changeEvent);
            }

            prevPointer.x = event.clientX;
            prevPointer.y = event.clientY;
        }

        function onMouseUp() {
            document.removeEventListener('mousemove', onMouseMove, false);
            document.removeEventListener('mouseup', onMouseUp, false);

            if (Math.abs(distance) < 2) {
                scope.domElement.focus();
                scope.domElement.select();
            }
        }

        function onChange() {
            scope.setValue(scope.domElement.value);
        }

        function onFocus() {
            scope.domElement.style.backgroundColor = '';
            scope.domElement.style.cursor = '';
        }

        function onBlur() {
            scope.domElement.style.backgroundColor = 'transparent';
            scope.domElement.style.cursor = 'ns-resize';
        }

        function onKeyDown(event) {
            event.stopPropagation();

            switch (event.keyCode) {
            case 13: // enter
                scope.domElement.blur();
                break;

            case 38: // up
                event.preventDefault();
                scope.setValue(scope.getValue() + scope.nudge);
                scope.domElement.dispatchEvent(changeEvent);
                break;

            case 40: // down
                event.preventDefault();
                scope.setValue(scope.getValue() - scope.nudge);
                scope.domElement.dispatchEvent(changeEvent);
                break;
            }
        }

        onBlur();

        this.domElement.addEventListener('keydown', onKeyDown, false);
        this.domElement.addEventListener('mousedown', onMouseDown, false);
        this.domElement.addEventListener('change', onChange, false);
        this.domElement.addEventListener('focus', onFocus, false);
        this.domElement.addEventListener('blur', onBlur, false);
    }

    getValue() {
        return this.value;
    }

    setValue(value) {
        if (value !== undefined) {
            value = parseInt(value);

            this.value = value;
            this.domElement.value = value;
        }

        return this;
    }

    setStep(step) {
        this.step = parseInt(step);

        return this;
    }

    setNudge(nudge) {
        this.nudge = nudge;

        return this;
    }

    setRange(min, max) {
        this.min = min;
        this.max = max;

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
}

class UIButton extends UIElement {
    constructor(value) {
        super(document.createElement('button'));
        this.domElement.className = 'btn';
        this.domElement.type = 'button';
        this.domElement.textContent = value;
    }
}

class UISlider extends UIElement {
    constructor(value) {
        super(document.createElement('div'));
        this.domElement.className = 'slider';
        // range
        this.UIRange = new UIInput(value).setClass('range');
        this.UIRange.domElement.type = 'range';
        this.add(this.UIRange);
        // input
        this.UIInput = new UIInput(value).setReadOnly(true);
        this.add(this.UIInput);

        const scope = this;
        function onInput() {
            scope.UIInput.setValue(scope.UIRange.getValue());

            const changeEvent = document.createEvent('HTMLEvents');
            changeEvent.initEvent('change', true, true);
            scope.UIInput.domElement.dispatchEvent(changeEvent);
        }

        this.UIRange.domElement.addEventListener('input', onInput, false);
    }

    setMin(value) {
        this.UIRange.domElement.setAttribute('min', value);
        return this;
    }

    setMax(value) {
        this.UIRange.domElement.setAttribute('max', value);
        return this;
    }
}

class UITabbedPanel extends UIDiv {
    constructor() {
        super();

        this.domElement.className = 'TabbedPanel';

        this.tabs = [];
        this.panels = [];

        this.tabsDiv = new UIDiv();
        this.tabsDiv.setClass('Tabs');

        this.panelsDiv = new UIDiv();
        this.panelsDiv.setClass('Panels');

        this.add(this.tabsDiv);
        this.add(this.panelsDiv);

        this.selected = '';
    }

    select(id) {
        let tab;
        let panel;
        const scope = this;

        // Deselect current selection
        if (this.selected && this.selected.length) {
            tab = this.tabs.find(function (item) {
                return item.domElement.id === scope.selected;
            });
            panel = this.panels.find(function (item) {
                return item.domElement.id === scope.selected;
            });

            if (tab) {
                tab.removeClass('selected');
            }

            if (panel) {
                panel.setDisplay('none');
            }
        }

        tab = this.tabs.find(function (item) {
            return item.domElement.id === id;
        });
        panel = this.panels.find(function (item) {
            return item.domElement.id === id;
        });

        if (tab) {
            tab.addClass('selected');
        }

        if (panel) {
            panel.setDisplay('');
        }

        this.selected = id;

        return this;
    }

    addTab(id, label, items) {
        const tab = new UITab(label, this);
        tab.setId(id);
        this.tabs.push(tab);
        this.tabsDiv.add(tab);

        const panel = new UIDiv();
        panel.setId(id);
        panel.add(items);
        panel.setDisplay('none');
        this.panels.push(panel);
        this.panelsDiv.add(panel);

        this.select(id);
    }
}

class UITab extends UIText {
    constructor(text, parent) {
        super(text);

        this.domElement.className = 'Tab';

        this.parent = parent;

        const scope = this;

        this.domElement.addEventListener('click', function () {
            scope.parent.select(scope.domElement.id);
        });
    }
}

class UIListbox extends UIDiv {
    constructor() {
        super();

        this.domElement.className = 'Listbox';
        this.domElement.tabIndex = 0;

        this.items = [];
        this.listitems = [];
        this.selectedIndex = 0;
        this.selectedValue = null;
    }

    setItems(items) {
        if (Array.isArray(items)) {
            this.items = items;
        }

        this.render();
    }

    render() {
        while (this.listitems.length) {
            const item = this.listitems[0];

            item.domElement.remove();

            this.listitems.splice(0, 1);
        }

        for (let i = 0; i < this.items.length; i++) {
            const item = this.items[i];

            const listitem = new UIListbox.ListboxItem(this);
            listitem.setId(item.id || `Listbox-${i}`);
            listitem.setTextContent(item.name || item.type);
            this.add(listitem);
        }
    }

    add() {
        const items = Array.from(arguments);

        this.listitems = this.listitems.concat(items);

        UIElement.prototype.add.apply(this, items);
    }

    selectIndex(index) {
        if (index >= 0 && index < this.items.length) {
            this.setValue(this.listitems[index].getId());
        }

        this.selectedIndex = index;
    }

    getValue() {
        return this.selectedValue;
    }

    setValue(value) {
        for (let i = 0; i < this.listitems.length; i++) {
            const element = this.listitems[i];

            if (element.getId() === value) {
                element.addClass('active');
            } else {
                element.removeClass('active');
            }
        }

        this.selectedValue = value;

        const changeEvent = document.createEvent('HTMLEvents');
        changeEvent.initEvent('change', true, true);
        this.domElement.dispatchEvent(changeEvent);
    }
}

class ListboxItem extends UIDiv {
    constructor(parent) {
        super();

        this.domElement.className = 'ListboxItem';
        this.parent = parent;

        const scope = this;

        function onClick() {
            if (scope.parent) {
                scope.parent.setValue(scope.getId());
            }
        }

        this.domElement.addEventListener('click', onClick, false);
    }
}

export {
    UIElement, UISpan, UILabel, UIDiv, UIText, UIInput, UITextArea,
    UISelect, UICheckbox, UIClipboard, UIColor, UISwitch, UINumber, UIInteger, UIBreak,
    UIHorizontalRule, UIButton, UISlider, UITabbedPanel, UIListbox, ListboxItem,
};
