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
        'subprocess': [
            {'name': 'sub-process-id', 'type': 'text', 'default': ''}
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

        let actionTooltip = [{
            title: 'remove',
            image: '../../assets/media/icons/dataflow/ic_edit.png',
            action: function(e, i) {
                console.log('Item #1 clicked!');
            }
        }, {
            title: 'copy',
            image: '../../assets/media/icons/dataflow/ic_edit.png',
            action: function(e, i) {
                console.log('Item #2 clicked!');
            }
        }, {
            title: 'edit',
            image: '../../assets/media/icons/dataflow/ic_edit.png',
            action: function(e, i) {
                console.log('Item #3 clicked!');
            }
        }, {
            title: 'add',
            image: '../../assets/media/icons/dataflow/ic_edit.png',
            action: function(e, i) {
                console.log('Item #4 clicked!');
            }
        }];

        const actionTooltipItemContainer = d3.select('.drawing-board').select('svg').append('g')
            .classed('tooltip action', true).style('display', 'none');

        const containerWidth = actionTooltip.length * 25,
              containerHeight = 30;
        const containerRect = actionTooltipItemContainer.append('rect')
            .attr('width', containerWidth)
            .attr('height', containerHeight)
            .style('fill', '#eee');

        containerRect.selectAll('tooltip-item')
            .data(actionTooltip)
            .enter()
            .append('image')
            .attr('x', 0)
            .attr('y', 5)
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', d => { return d.image })
            .on('click', (d, i) => {
                d.action(elem, i);
            });

        const bbox = elem.node().getBBox();
        actionTooltipItemContainer.attr('transform', 'translate(' + (bbox.x + bbox.width / 2 - containerWidth / 2) + ', ' + (bbox.y - containerHeight - 10) + ')');
        actionTooltipItemContainer.style('display', 'block');
        actionTooltipItemContainer.datum(elem);
    }

    /**
     * 해당 element의 속성을 표시한다.
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
            } else if (_this.classed('subprocess')) {
                properties = elementsProperties['subprocess'];
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