(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    const workflowProperties = [
        {'name': 'id', 'type': 'text', 'default': ''},
        {'name': 'name', 'type': 'text', 'default': ''},
        {'name': 'description', 'type': 'textarea', 'default': ''}
    ];

    const elementsProperties = {
        'event': [
            {'name': 'name', 'type': 'text', 'default': ''}
        ],
        'task': [
            {'name': 'name', 'type': 'text', 'default': ''},
            {'name': 'description', 'type': 'textarea', 'default': ''},
            {'name': 'assignee-type', 'type': 'select', 'default': ''},
            {'name': 'assignee', 'type': 'text', 'default': ''},
            {'name': 'notification', 'type': 'checkbox', 'default': ''}
        ],
        'gateway': [
            {'name': 'name', 'type': 'text', 'default': ''}
        ],
        'group': [
            {'name': 'name', 'type': 'text', 'default': ''},
            {'name': 'description', 'type': 'textarea', 'default': ''}
        ],
        'annotation': [
            {'name': 'text', 'type': 'textarea', 'default': ''},
        ],
        'connector': [
            {'name': 'name', 'type': 'text', 'default': ''},
            {'name': 'condition', 'type': 'text', 'textarea': ''},
            {'name': 'start-id', 'type': 'text', 'text': ''},
            {'name': 'end-id', 'type': 'text', 'text': ''}
        ],
    };

    /**
     * 해당 element의 메뉴를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setMenuItem(elem) {
        if (typeof elem === 'undefined') {
            return;
        }

        let x , y;
        if (elem.node() instanceof SVGCircleElement) {
            x = elem.attr('cx') - elem.attr('r');
            y = elem.attr('cy') - elem.attr('r');
        } else {
            x = elem.attr('x');
            y = elem.attr('y');
        }

        // 임시메뉴
        let menu = [{
            title: 'Item #1',
            action: function(e, i) {
                console.log('Item #1 clicked!');
            }
        }, {
            title: 'Item #2',
            action: function(e, i) {
                console.log('Item #2 clicked!');
            }
        }];

        const menuItemContainer = d3.select('.drawing-board').select('svg').append('g')
            .classed('menu', true).style('display', 'none');

        const menuRect = menuItemContainer.append('rect')
            .attr('height', menu.length * 25)
            .style('fill', '#eee');

        const menuItems = menuItemContainer.selectAll('menu_item')
            .data(menu)
            .enter()
            .append('g')
            .attr('transform', (d, i) => {
                return 'translate(' + 10 + ',' + ((i + 1) * 20) + ')';
            })
            .on('mouseover', d => {
                d3.select(this).style('fill', 'steelblue');
            })
            .on('mouseout', d => {
                d3.select(this).style('fill', 'black');
            })
            .on('click', (d, i) => {
                d.action(elem, i);
            })
            .append('text')
            .text(d => {
                return d.title;
            });

        let width = 0;
        menuItems.each(function(d){
            let len = this.getComputedTextLength();
            if (len > width) {
                width = len;
            }
        });
        menuRect.attr('width', width + 20);

        menuItemContainer.attr('transform', 'translate(' + x + ',' + (y - (menu.length * 25)) + ')');
        menuItemContainer.style('display', 'block');
        menuItemContainer.datum(elem);
    }

    /**
     * 해당 element의 properties를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setProperties(elem) {
        const propertiesContainer = d3.select('.properties-panel');
        propertiesContainer.selectAll('*').remove();
        if (typeof elem !== 'undefined') { // show element properties
            propertiesContainer.append('h3').text('Element Properties');

            let _this = elem;
            let properties = [];
            if (_this.classed('event')) {
                properties = elementsProperties['event'];
            } else if (_this.classed('task')) {
                properties = elementsProperties['task'];
            } else if (_this.classed('gateway')) {
                properties = elementsProperties['gateway'];
            } else if (_this.classed('group')) {
                properties = elementsProperties['group'];
            } else if (_this.classed('annotation')) {
                properties = elementsProperties['annotation'];
            }

            for (let i = 0, len = properties.length; i < len; i++) {
                let property = properties[i];
                let propertyContainer = propertiesContainer.append('p');
                let label = propertyContainer.append('label');
                label.attr('for', property.name);
                label.text(property.name);
                let input = propertyContainer.append('input');
                input.attr('id', property.name);
                input.attr('value', property.default);
            }
        } else { // show workflow properties
            propertiesContainer.append('h3').text('Workflow Properties');

            for (let i = 0, len = workflowProperties.length; i < len; i++) {
                let property = workflowProperties[i];
                let propertyContainer = propertiesContainer.append('p');
                let label = propertyContainer.append('label');
                label.attr('for', property.name);
                label.text(property.name);
                let input = propertyContainer.append('input');
                input.attr('id', property.name);
                input.attr('value', property.default);
            }
        }
    }

    function setElementMenu(elem) {
        setMenuItem(elem);
        setProperties(elem);
    }

    exports.setElementMenu = setElementMenu;
    Object.defineProperty(exports, '__esModule', {value: true});
})));