(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    const workflowProperties = [
        {'attribute': 'id', 'name': 'Workflow ID', 'type': 'text', 'default': ''},
        {'attribute': 'name', 'name': '표시명', 'type': 'text', 'default': ''},
        {'attribute': 'description', 'name': '설명', 'type': 'textarea', 'default': ''}
    ];

    const elementsProperties = {
        'event': [
            {'attribute': 'name', 'name': '표시명', 'type': 'text', 'default': ''}
        ],
        'task': [
            {'attribute': 'name', 'name': '표시명', 'type': 'text', 'default': ''},
            {'attribute': 'description', 'name': '설명', 'type': 'textarea', 'default': ''},
            {'attribute': 'assignee-type', 'name': '수행자 타입', 'type': 'selectbox', 'default': '', 'sub-list': 'assignee,candidate users, candidate groups'},
            {'attribute': 'assignee', 'name': '수행자', 'type': 'text', 'default': ''},
            {'attribute': 'notification', 'name': '메일통보 여부', 'type': 'checkbox', 'default': ''}
        ],
        'subprocess': [
            {'attribute': 'sub-process-id', 'name': '서브 프로세스 ID', 'type': 'text', 'default': ''}
        ],
        'gateway': [
            {'attribute': 'name', 'name': '표시명', 'type': 'text', 'default': ''}
        ],
        'group': [
            {'attribute': 'name', 'name': '표시명', 'type': 'text', 'default': ''},
            {'attribute': 'description', 'name': '설명', 'type': 'textarea', 'default': ''}
        ],
        'annotation': [
            {'attribute': 'text', 'name': '텍스트', 'type': 'textarea', 'default': ''},
        ],
        'connector': [
            {'attribute': 'name', 'name': '표시명', 'type': 'text', 'default': ''},
            {'attribute': 'condition', 'name': '조건', 'type': 'text', 'textarea': ''},
            {'attribute': 'start-id', 'name': 'Source Element ID', 'type': 'text', 'text': ''},
            {'attribute': 'end-id', 'name': 'Target Element ID', 'type': 'text', 'text': ''}
        ],
    };

    /**
     * 해당 element의 툴팁메뉴를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setTooltipItem(elem) {
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
            image: '../../assets/media/icons/dataflow/ic_wastebasket_ov.png',
            action: function(el, i) {
                console.log('remove');
            }
        }, {
            title: 'copy',
            image: '../../assets/media/icons/dataflow/ic_doc.png',
            action: function(el, i) {
                console.log('copy');
            }
        }, {
            title: 'edit',
            image: '../../assets/media/icons/dataflow/ic_edit.png',
            action: function(el, i) {
                console.log('edit');
            }
        }, {
            title: 'add',
            image: '../../assets/media/icons/dataflow/ic_doc.png',
            action: function(el, i) {
                console.log('add');
            }
        }];

        const actionTooltipItemContainer = d3.select('.drawing-board').select('svg').append('g')
            .classed('tooltip action', true).style('display', 'none');

        const containerWidth = actionTooltip.length * 25,
              containerHeight = 30;

        actionTooltipItemContainer.append('rect')
            .attr('width', containerWidth)
            .attr('height', containerHeight)
            .style('fill', '#eee');

        actionTooltipItemContainer.selectAll('tooltip-item')
            .data(actionTooltip)
            .enter()
            .append('image')
            .attr('x', (d, i) => { return i * 25 })
            .attr('y', 5)
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', d => { return d.image })
            .on('mousedown', (d, i) => {
                d3.event.stopPropagation();
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
            makePropertiesItem(propertiesContainer, properties);
        } else { // show workflow properties
            propertiesContainer.append('h3').text('Workflow Properties');
            makePropertiesItem(propertiesContainer, workflowProperties);
        }
    }

    /**
     * 속성 항목을 생성한다.
     *
     * @param propertiesContainer
     * @param properties 속성정보목록
     */
    function makePropertiesItem(propertiesContainer, properties) {
        for (let i = 0, len = properties.length; i < len; i++) {
            let property = properties[i];
            let propertyContainer = propertiesContainer.append('p');
            let label = propertyContainer.append('label');
            label.attr('for', property.attribute);
            label.text(property.name);

            if (property.type === 'text') {
                propertyContainer.append('input')
                    .attr('id', property.attribute)
                    .attr('value', property.default);
            } else if (property.type === 'textarea') {
                propertyContainer.append('textarea')
                    .attr('id', property.attribute)
                    .attr('value', property.default);
            } else if (property.type === 'checkbox') {
                propertyContainer.append('input')
                    .attr('type', 'checkbox')
                    .attr('id', property.attribute)
                    .attr('value', property.default);
            } else if (property.type === 'selectbox') {
                propertyContainer.append('select')
                    .attr('id', property.attribute)
                    .attr('value', property.default)
                    .selectAll('option')
                    .data(property['sub-list'].split(','))
                    .enter()
                    .append('option')
                    .attr('value', d => d)
                    .text(d => d);
            }
        }
    }

    /**
     *
     * @param elem
     */
    function setElementMenu(elem) {
        setTooltipItem(elem);
        setProperties(elem);
    }

    exports.setElementMenu = setElementMenu;
    Object.defineProperty(exports, '__esModule', {value: true});
})));