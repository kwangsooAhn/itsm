/**
 * DOM Element 를 생성하는 클래스
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

import { CLASS_PREFIX } from './constants.js';

class UIElement {
    constructor(domElem) {
        this.domElem = domElem;
    }

    add() {
        for (let i = 0; i < arguments.length; i++) {
            const argument = arguments[i];
            if (argument instanceof UIElement) {
                this.domElem.appendChild(argument.domElem);
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
                this.domElem.removeChild(argument.domElem);
            } else {
                console.error( 'UIElement:', argument, 'is not an instance of UIElement.' );
            }
        }
        return this;
    }

    clear() {
        while (this.domElem.children.length) {
            this.domElem.removeChild(this.domElem.lastChild);
        }
    }

    setId(id) {
        this.domElem.id = id;
        return this;
    }

    getId() {
        return this.domElem.id;
    }

    setClass(name) {
        this.domElem.className = name;
        return this;
    }

    addClass(name) {
        this.domElem.classList.add(name);
        return this;
    }

    removeClass(name) {
        this.domElem.classList.remove(name);
        return this;
    }

    setAttribute(name, value) {
        this.domElem.setAttribute('data-' + name, value);
        return this;
    }

    setStyle(style, array) {
        for (let i = 0; i < array.length; i++) {
            this.domElem.style[style] = array[i];
        }
        return this;
    }

    setDisabled(value) {
        this.domElem.disabled = value;
        return this;
    }

    setTextContent(value) {
        this.domElem.textContent = value;
        return this;
    }

    getIndexOfChild(element) {
        return Array.prototype.indexOf.call(this.domElem.children, element.domElem);
    }
}

// properties
const properties = [ 'position', 'left', 'top', 'right', 'bottom', 'width', 'height',
    'border', 'borderLeft', 'borderTop', 'borderRight', 'borderBottom', 'borderColor',
    'display', 'overflow', 'margin', 'marginLeft', 'marginTop', 'marginRight', 'marginBottom',
    'padding', 'paddingLeft', 'paddingTop', 'paddingRight', 'paddingBottom', 'color',
    'background', 'backgroundColor', 'opacity', 'fontSize', 'fontWeight', 'fontStyle',
    'textAlign', 'textDecoration', 'textTransform', 'cursor', 'zIndex' ];
properties.forEach(function (property) {
    const method = 'set' + property.substr( 0, 1 ).toUpperCase() +
        property.substr( 1, property.length );
    UIElement.prototype[method] = function () {
        this.setStyle(property, arguments);
        return this;
    };
});

// events
const events = ['KeyUp', 'KeyDown', 'MouseOver', 'MouseOut', 'Click', 'DblClick', 'Change', 'Input'];
events.forEach(function (event) {
    const method = 'on' + event;
    UIElement.prototype[ method ] = function (callback) {
        this.domElem.addEventListener(event.toLowerCase(), callback.bind(this), false );
        return this;
    };
});

class UISpan extends UIElement {
    constructor() {
        super(document.createElement( 'span' ));
    }
}

class UILabel extends UIElement {
    constructor() {
        super(document.createElement( 'label' ));
    }

    setFor(id) {
        this.domElem.htmlFor = id;
        return this;
    }
}

class UIDiv extends UIElement {
    constructor() {
        super(document.createElement( 'div' ));
    }
}

class UIForm extends UIDiv {
    constructor() {
        super();
        this.domElem.className = CLASS_PREFIX + 'form';
    }
}

class UIGroup extends UIDiv {
    constructor(boolean) {
        super();
        this.domElem.className = CLASS_PREFIX + 'group';

        if (boolean) {
            this.addClass('accordion');
        }

        this.checkbox = new UICheckbox(true);
        this.add(this.checkbox);

        this.label = new UILabel().setClass(CLASS_PREFIX + 'group-label');
        this.label.labelText = new UISpan().setClass(CLASS_PREFIX + 'group-label-text');
        this.label.arrowIcon = new UISpan().setClass(CLASS_PREFIX + 'group-label-icon arrow-left');
        this.label.add(this.label.labelText).add(this.label.arrowIcon);
        this.add(this.label);
    }
}

class UIRow extends UIDiv {
    constructor() {
        super();
        this.domElem.className = CLASS_PREFIX + 'row';
    }
}

class UIComponent extends UIDiv {
    constructor() {
        super();
        this.domElem.className = CLASS_PREFIX + 'component';
        this.domElem.tabIndex = 0;
    }
}

class UIText extends UISpan {
    constructor(text) {
        super();
        this.domElem.className = 'text';
        this.domElem.style.cursor = 'default';
        this.domElem.style.display = 'inline-block';
        this.domElem.style.verticalAlign = 'middle';

        this.setValue(text);
    }

    getValue() {
        return this.domElem.textContent;
    }

    setValue(value) {
        if (value !== undefined) {
            this.domElem.textContent = value;
        }
        return this;
    }
}

class UIInput extends UIElement {
    constructor( text ) {
        super(document.createElement('input'));
        this.domElem.className = 'input';

        this.domElem.addEventListener('keydown', function ( event ) {
            event.stopPropagation();
        }, false);

        this.setValue(text);
    }

    getValue() {
        return this.domElem.value;
    }

    setValue(value) {
        this.domElem.value = value;
        return this;
    }

    setPlaceholder(value) {
        this.domElem.placeholder = value;
        return this;
    }

    setRequired(boolean) {
        this.domElem.required = boolean;
        return this;
    }
}

class UITextArea extends UIElement {
    constructor() {
        super(document.createElement('textarea'));
        this.domElem.className = 'textArea';
        this.domElem.style.padding = '2px';
        this.domElem.spellcheck = false;

        this.domElem.addEventListener('keydown', function (event) {
            event.stopPropagation();

            if (event.keyCode === 9) { // TAB 일 경우
                event.preventDefault();

                const cursor = this.domElem.selectionStart;
                this.domElem.value = this.domElem.value.substring( 0, cursor ) + '\t' +
                    this.domElem.value.substring( cursor );
                this.domElem.selectionStart = cursor + 1;
                this.domElem.selectionEnd = this.domElem.selectionStart;
            }
        }, false);

    }

    getValue() {
        return this.domElem.value;
    }

    setValue(value) {
        this.domElem.value = value;
        return this;
    }
}

class UISelect extends UIElement {
    constructor() {
        super(document.createElement('select'));
        this.domElem.className = 'select';
        this.domElem.style.padding = '2px';
    }

    setMultiple(boolean) {
        this.domElem.multiple = boolean;
        return this;
    }

    setOptions(options) {
        const selected = this.domElem.value;
        while (this.domElem.children.length > 0) {
            this.domElem.removeChild(this.domElem.firstChild);
        }

        for (const key in options) {
            const option = document.createElement('option');
            option.value = key;
            option.innerHTML = options[key];
            this.domElem.appendChild(option);

        }
        this.domElem.value = selected;

        return this;
    }

    getValue() {
        return this.domElem.value;
    }

    setValue(value) {
        value = String(value);

        if (this.domElem.value !== value) {
            this.domElem.value = value;
        }

        return this;
    }
}

class UICheckbox extends UIElement {
    constructor(boolean) {
        super( document.createElement('input'));
        this.domElem.className = 'checkbox';
        this.domElem.type = 'checkbox';
        this.setValue(boolean);
    }

    getValue() {
        return this.domElem.checked;
    }

    setValue(value) {
        if (value !== undefined) {
            this.domElem.checked = value;
        }

        return this;
    }
}

// TODO: color picker 라이브러리 참조하여 엘리먼트 만들기
class UIColor extends UIElement {
    constructor() {
        super( document.createElement('input'));
        this.domElem.className = 'color';
        this.domElem.style.width = '32px';
        this.domElem.style.height = '16px';
        this.domElem.style.border = '0px';
        this.domElem.style.padding = '2px';
        this.domElem.style.backgroundColor = 'transparent';

        try {
            this.domElem.type = 'color';
            this.domElem.value = '#ffffff';
        } catch ( exception ) {

        }
    }

    getValue() {
        return this.domElem.value;
    }

    getHexValue() {
        return parseInt(this.domElem.value.substr( 1 ), 16);
    }

    setValue(value) {
        this.domElem.value = value;
        return this;
    }

    setHexValue(hex) {
        this.domElem.value = '#' + ( '000000' + hex.toString( 16 ) ).slice( - 6 );
        return this;
    }
}

class UINumber extends UIElement {
    constructor(number) {
        super( document.createElement( 'input' ) );

        this.domElem.style.cursor = 'ns-resize';
        this.domElem.className = 'Number';
        this.domElem.value = '0.00';
        this.value = 0;
        this.min = - Infinity;
        this.max = Infinity;
        this.precision = 2;
        this.step = 1;
        this.unit = '';
        this.nudge = 0.01;

        this.setValue(number);

        const scope = this;
        const changeEvent = document.createEvent( 'HTMLEvents' );
        changeEvent.initEvent( 'change', true, true );

        let distance = 0;
        let onMouseDownValue = 0;

        const pointer = { x: 0, y: 0 };
        const prevPointer = { x: 0, y: 0 };

        function onMouseDown( event ) {
            event.preventDefault();

            distance = 0;
            onMouseDownValue = scope.value;

            prevPointer.x = event.clientX;
            prevPointer.y = event.clientY;

            document.addEventListener( 'mousemove', onMouseMove, false );
            document.addEventListener( 'mouseup', onMouseUp, false );

        }

        function onMouseMove( event ) {
            const currentValue = scope.value;

            pointer.x = event.clientX;
            pointer.y = event.clientY;

            distance += ( pointer.x - prevPointer.x ) - ( pointer.y - prevPointer.y );

            let value = onMouseDownValue + ( distance / ( event.shiftKey ? 5 : 50 ) ) * scope.step;
            value = Math.min( scope.max, Math.max( scope.min, value ) );

            if (currentValue !== value) {
                scope.setValue( value );
                scope.domElem.dispatchEvent(changeEvent);
            }
            prevPointer.x = event.clientX;
            prevPointer.y = event.clientY;
        }

        function onMouseUp() {
            document.removeEventListener( 'mousemove', onMouseMove, false );
            document.removeEventListener( 'mouseup', onMouseUp, false );

            if (Math.abs( distance ) < 2) {
                scope.domElem.focus();
                scope.domElem.select();
            }
        }

        function onTouchStart( event ) {
            if ( event.touches.length === 1 ) {
                distance = 0;
                onMouseDownValue = scope.value;

                prevPointer.x = event.touches[0].pageX;
                prevPointer.y = event.touches[0].pageY;

                document.addEventListener( 'touchmove', onTouchMove, false );
                document.addEventListener( 'touchend', onTouchEnd, false );
            }
        }

        function onTouchMove( event ) {
            const currentValue = scope.value;

            pointer.x = event.touches[ 0 ].pageX;
            pointer.y = event.touches[ 0 ].pageY;

            distance += (pointer.x - prevPointer.x) - (pointer.y - prevPointer.y);

            let value = onMouseDownValue + (distance / (event.shiftKey ? 5 : 50)) * scope.step;
            value = Math.min(scope.max, Math.max(scope.min, value));

            if (currentValue !== value) {

                scope.setValue( value );
                scope.domElem.dispatchEvent( changeEvent );

            }

            prevPointer.x = event.touches[ 0 ].pageX;
            prevPointer.y = event.touches[ 0 ].pageY;
        }

        function onTouchEnd( event ) {
            if (event.touches.length === 0) {
                document.removeEventListener('touchmove', onTouchMove, false);
                document.removeEventListener('touchend', onTouchEnd, false);
            }
        }

        function onChange() {
            scope.setValue(scope.domElem.value);
        }

        function onFocus() {
            scope.domElem.style.backgroundColor = '';
            scope.domElem.style.cursor = '';
        }

        function onBlur() {
            scope.domElem.style.backgroundColor = 'transparent';
            scope.domElem.style.cursor = 'ns-resize';
        }

        function onKeyDown( event ) {
            event.stopPropagation();

            switch (event.keyCode) {
            case 13: // enter
                scope.domElem.blur();
                break;
            case 38: // up
                event.preventDefault();
                scope.setValue( scope.getValue() + scope.nudge );
                scope.domElem.dispatchEvent( changeEvent );
                break;
            case 40: // down
                event.preventDefault();
                scope.setValue( scope.getValue() - scope.nudge );
                scope.domElem.dispatchEvent( changeEvent );
                break;
            }
        }

        onBlur();

        this.domElem.addEventListener( 'keydown', onKeyDown, false );
        this.domElem.addEventListener( 'mousedown', onMouseDown, false );
        this.domElem.addEventListener( 'touchstart', onTouchStart, false );
        this.domElem.addEventListener( 'change', onChange, false );
        this.domElem.addEventListener( 'focus', onFocus, false );
        this.domElem.addEventListener( 'blur', onBlur, false );
    }

    getValue() {
        return this.value;
    }

    setValue(value) {
        if ( value !== undefined ) {
            value = parseFloat( value );

            if (value < this.min) value = this.min;
            if (value > this.max) value = this.max;

            this.value = value;
            this.domElem.value = value.toFixed( this.precision );

            if (this.unit !== '') this.domElem.value += ' ' + this.unit;
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

        this.domElem.style.cursor = 'ns-resize';
        this.domElem.className = 'Number';
        this.domElem.value = '0';

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

            let value =
                onMouseDownValue + (distance / (event.shiftKey ? 5 : 50)) * scope.step;
            value = Math.min(scope.max, Math.max(scope.min, value)) | 0;

            if (currentValue !== value) {
                scope.setValue(value);
                scope.domElem.dispatchEvent(changeEvent);
            }

            prevPointer.x = event.clientX;
            prevPointer.y = event.clientY;
        }

        function onMouseUp() {
            document.removeEventListener('mousemove', onMouseMove, false);
            document.removeEventListener('mouseup', onMouseUp, false);

            if (Math.abs(distance) < 2) {
                scope.domElem.focus();
                scope.domElem.select();
            }
        }

        function onChange() {
            scope.setValue(scope.domElem.value);
        }

        function onFocus() {
            scope.domElem.style.backgroundColor = '';
            scope.domElem.style.cursor = '';
        }

        function onBlur() {
            scope.domElem.style.backgroundColor = 'transparent';
            scope.domElem.style.cursor = 'ns-resize';
        }

        function onKeyDown(event) {
            event.stopPropagation();

            switch (event.keyCode) {
            case 13: // enter
                scope.domElem.blur();
                break;

            case 38: // up
                event.preventDefault();
                scope.setValue(scope.getValue() + scope.nudge);
                scope.domElem.dispatchEvent(changeEvent);
                break;

            case 40: // down
                event.preventDefault();
                scope.setValue(scope.getValue() - scope.nudge);
                scope.domElem.dispatchEvent(changeEvent);
                break;
            }
        }

        onBlur();

        this.domElem.addEventListener('keydown', onKeyDown, false);
        this.domElem.addEventListener('mousedown', onMouseDown, false);
        this.domElem.addEventListener('change', onChange, false);
        this.domElem.addEventListener('focus', onFocus, false);
        this.domElem.addEventListener('blur', onBlur, false);
    }

    getValue() {
        return this.value;
    }

    setValue(value) {
        if (value !== undefined) {
            value = parseInt(value);

            this.value = value;
            this.domElem.value = value;
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
        super( document.createElement( 'br' ) );
        this.domElem.className = 'Break';
    }
}

class UIHorizontalRule extends UIElement {
    constructor() {
        super(document.createElement('hr'));
        this.domElem.className = 'HorizontalRule';
    }
}

class UIButton extends UIElement {
    constructor(value) {
        super(document.createElement('button'));
        this.domElem.className = 'Button';
        this.domElem.textContent = value;
    }
}

class UIProgress extends UIElement {
    constructor(value) {
        super(document.createElement('progress'));

        this.domElem.value = value;
    }

    setValue(value) {
        this.domElem.value = value;
    }
}

class UITabbedPanel extends UIDiv {
    constructor() {
        super();

        this.domElem.className = 'TabbedPanel';

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
                return item.domElem.id === scope.selected;
            });
            panel = this.panels.find(function (item) {
                return item.domElem.id === scope.selected;
            });

            if (tab) {
                tab.removeClass('selected');
            }

            if (panel) {
                panel.setDisplay('none');
            }
        }

        tab = this.tabs.find(function (item) {
            return item.domElem.id === id;
        });
        panel = this.panels.find(function (item) {
            return item.domElem.id === id;
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

        this.domElem.className = 'Tab';

        this.parent = parent;

        const scope = this;

        this.domElem.addEventListener('click', function () {
            scope.parent.select(scope.domElem.id);
        });
    }
}

class UIListbox extends UIDiv {
    constructor() {
        super();

        this.domElem.className = 'Listbox';
        this.domElem.tabIndex = 0;

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

            item.domElem.remove();

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
        this.domElem.dispatchEvent(changeEvent);
    }
}

class ListboxItem extends UIDiv {
    constructor(parent) {
        super();

        this.domElem.className = 'ListboxItem';
        this.parent = parent;

        const scope = this;

        function onClick() {
            if (scope.parent) {
                scope.parent.setValue(scope.getId());
            }
        }

        this.domElem.addEventListener('click', onClick, false);
    }
}

export { UIElement, UISpan, UILabel, UIDiv, UIForm, UIGroup, UIRow, UIComponent,
    UIText, UIInput, UITextArea, UISelect, UICheckbox, UIColor, UINumber, UIInteger,
    UIBreak, UIHorizontalRule, UIButton, UIProgress, UITabbedPanel, UIListbox, ListboxItem
};
