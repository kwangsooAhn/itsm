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
            {'attribute': 'assignee-type', 'name': '수행자 타입', 'type': 'selectbox', 'default': '', 'sub-list': 'assignee,candidate users, candidate groups'},
            {'attribute': 'assignee', 'name': '수행자', 'type': 'text', 'default': ''},
            {'attribute': 'notification', 'name': '메일통보 여부', 'type': 'checkbox', 'default': ''},
            {'attribute': 'description', 'name': '설명', 'type': 'textarea', 'default': ''}
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
    function setActionTooltipItem(elem) {
        if (typeof elem === 'undefined') {
            return;
        }

        let actionTooltip = [{
            title: 'remove',
            image: '../../assets/media/icons/workflow/ic_wastebasket_ov.png',
            action: function(el, i) {
                removeElementItems();
                console.log('remove');
            }
        }, {
            title: 'copy',
            image: '../../assets/media/icons/workflow/ic_doc.png',
            action: function(el, i) {
                removeElementItems();
                console.log('copy');
            }
        }, {
            title: 'edit',
            image: '../../assets/media/icons/workflow/ic_edit.png',
            action: function(el, i) {
                setElementTypeItems(el);
            }
        }, {
            title: 'add',
            image: '../../assets/media/icons/workflow/ic_doc.png',
            action: function(el, i) {
                setFavoritesElementItems(el);
            }
        }];

        if (elem.classed('group') || elem.classed('annotation')) {
            actionTooltip = actionTooltip.slice(2);
        }

        const tooltipItemContainer = d3.select('.drawing-board').select('svg').append('g')
            .attr('class', 'tooltip').style('display', 'none');

        const containerWidth = actionTooltip.length * 25,
              containerHeight = 30;

        tooltipItemContainer.append('rect')
            .attr('class', 'action-tooltip')
            .attr('width', containerWidth)
            .attr('height', containerHeight)
            .style('fill', '#eee');

        tooltipItemContainer.selectAll('action-tooltip-item')
            .data(actionTooltip)
            .enter()
            .append('image')
            .attr('x', (d, i) => { return  5 + (i * 25) })
            .attr('y', 5)
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', d => { return d.image })
            .on('mousedown', (d, i) => {
                d3.event.stopPropagation();
                d.action(elem, i);
            });

        const bbox = elem.node().getBBox();
        tooltipItemContainer.attr('transform', 'translate(' + (bbox.x + bbox.width / 2 - containerWidth / 2) + ', ' + (bbox.y - containerHeight - 10) + ')');
        tooltipItemContainer.style('display', 'block');
        tooltipItemContainer.datum(elem);
    }

    /**
     * show favorites elements tooltip.
     *
     * @param elem 선택된 element
     */
    function setFavoritesElementItems(elem) {
        if (elem.classed('group') || elem.classed('annotation')) {
            return;
        }

        let favoritesElements = [{
            title: 'user task',
            image: '../../assets/media/icons/workflow/ic_doc.png',
            action: function(el, i) {
                console.log('add user task');
            }
        }, {
            title: 'manual task',
            image: '../../assets/media/icons/workflow/ic_doc.png',
            action: function(el, i) {
                console.log('add manual task');
            }
        }, {
            title: 'exclusive gateway',
            image: '../../assets/media/icons/workflow/ic_doc.png',
            action: function(el, i) {
                console.log('add exclusive gateway');
            }
        }, {
            title: 'end event',
            image: '../../assets/media/icons/workflow/ic_doc.png',
            action: function(el, i) {
                console.log('add end event');
            }
        }];
        setElementItems(favoritesElements, elem);
    }

    /**
     * show elements type tooltip.
     *
     * @param elem 선택된 element
     */
    function setElementTypeItems(elem) {
        let elementTypeItems = [];
        if (elem.classed('event')) {
            elementTypeItems = [{
                title: 'start event',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit type start');
                }
            }, {
                title: 'end event',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit type end');
                }
            }];
        } else if (elem.classed('task')) {
            elementTypeItems = [{
                title: 'user task',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit user task');
                }
            }, {
                title: 'manual task',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit manual task');
                }
            }, {
                title: 'script task',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit script task');
                }
            }, {
                title: 'send task',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit send task');
                }
            }, {
                title: 'receive task',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit receive task');
                }
            }];
        } else if (elem.classed('gateway')) {
            elementTypeItems = [{
                title: 'exclusive gateway',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit exclusive gateway');
                }
            }, {
                title: 'parallel gateway',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit parallel gateway');
                }
            }, {
                title: ' inclusive gateway',
                image: '../../assets/media/icons/workflow/ic_info.png',
                action: function(el, i) {
                    console.log('edit exclusive gateway');
                }
            }];
        }
        setElementItems(elementTypeItems, elem);
    }

    /**
     * show elements type tooltip.
     *
     * @param items tooltip에 표시할 항목목록
     * @param elem 선택된 element
     */
    function setElementItems(items, elem) {
        removeElementItems();

        if (items.length === 0) {
            return;
        }

        const tooltipItemContainer = d3.select('g.tooltip'),
              actionTooltipContainer = tooltipItemContainer.select('.action-tooltip'),
              containerWidth = 30,
              containerHeight = items.length * 25;

        const bbox = actionTooltipContainer.node().getBBox(),
              x = bbox.x + bbox.width + 5,
              y = bbox.y;

        tooltipItemContainer.append('rect')
            .attr('class', 'element-tooltip')
            .attr('x', x)
            .attr('y', y)
            .attr('width', containerWidth)
            .attr('height', containerHeight)
            .style('fill', '#eee');

        tooltipItemContainer.selectAll('element-tooltip-item')
            .data(items)
            .enter()
            .append('image')
            .attr('class', 'element-tooltip-item')
            .attr('x', x + 5)
            .attr('y', (d, i) => { return y + 5 + (i * 25) })
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', d => { return d.image })
            .on('mousedown', (d, i) => {
                d3.event.stopPropagation();
                d.action(elem, i);
            });
    }

    /**
     * remove element tooltip items.
     */
    function removeElementItems() {
        d3.selectAll('.element-tooltip-item').remove();
        d3.selectAll('.element-tooltip').remove();
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

            let _this = elem,
                properties = [];
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
            propertyContainer.append('label')
                .attr('for', property.attribute)
                .text(property.name);

            if (property.type === 'text') {
                propertyContainer.append('input')
                    .attr('id', property.attribute)
                    .attr('value', property.default)
                    .style('width', '180px');
            } else if (property.type === 'textarea') {
                propertyContainer.append('textarea')
                    .attr('id', property.attribute)
                    .attr('value', property.default)
                    .attr('rows', 5)
                    .style('resize', 'none')
                    .style('width', '180px');
            } else if (property.type === 'checkbox') {
                propertyContainer.append('input')
                    .attr('type', 'checkbox')
                    .attr('id', property.attribute)
                    .attr('value', property.default);
            } else if (property.type === 'selectbox') {
                propertyContainer.append('select')
                    .attr('id', property.attribute)
                    .attr('value', property.default)
                    .style('width', '180px')
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
     * 선택된 element 의 속성 및 tooltip 메뉴를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setElementMenu(elem) {
        setActionTooltipItem(elem);
        setProperties(elem);
    }

    exports.setElementMenu = setElementMenu;
    Object.defineProperty(exports, '__esModule', {value: true});
})));