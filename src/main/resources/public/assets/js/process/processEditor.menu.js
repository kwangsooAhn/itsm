(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceProcessEditor = global.AliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    const processProperties = [
        {'attribute': 'id', 'name': 'ID', 'type': 'text', 'default': ''},
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

    const tooltipItems = [
        {
            title: 'delete', parent: 'action-tooltip',
            url: '../../assets/media/icons/process/tooltip/delete.png',
            action: function(el, i) {
                removeElementItems();
                console.log('remove');
            }
        },
        {
            title: 'copy', parent: 'action-tooltip',
            url: '../../assets/media/icons/process/tooltip/copy.png',
            action: function(el, i) {
                removeElementItems();
                console.log('copy');
            }
        },
        {
            title: 'edit', parent: 'action-tooltip',
            url: '../../assets/media/icons/process/tooltip/edit.png',
            action: function(el, i) {
                setElementTypeItems(el);
            }
        },
        {
            title: 'suggest', parent: 'action-tooltip',
            url: '../../assets/media/icons/process/tooltip/suggest.png',
            action: function(el, i) {
                setSuggestElementItems(el);
            }
        },
        {
            title: 'userTask', parent: 'suggest-tooltip',
            url: '../../assets/media/icons/process/suggestion/usertask.png',
            action: function(el, i) {
                console.log('add user task');
            }
        }, {
            title: 'manualTask', parent: 'suggest-tooltip',
            url: '../../assets/media/icons/process/suggestion/manual.png',
            action: function(el, i) {
                console.log('add manual task');
            }
        }, {
            title: 'exclusiveGateway', parent: 'suggest-tooltip',
            url: '../../assets/media/icons/process/suggestion/gateways.png',
            action: function(el, i) {
                console.log('add exclusive gateway');
            }
        }, {
            title: 'endEvent', parent: 'suggest-tooltip',
            url: '../../assets/media/icons/process/suggestion/end.png',
            action: function(el, i) {
                console.log('add end event');
            }
        }, {
            title: 'start event', parent: 'event-tooltip',
            url: '../../assets/media/icons/process/element-type/event-start.png',
            action: function(el, i) {
                console.log('edit type start');
            }
        }, {
            title: 'message start event', parent: 'event-tooltip',
            url: '../../assets/media/icons/process/element-type/event-start-msg.png',
            action: function(el, i) {
                console.log('edit type message start');
            }
        }, {
            title: 'timer start event', parent: 'event-tooltip',
            url: '../../assets/media/icons/process/element-type/event-start-timer.png',
            action: function(el, i) {
                console.log('edit type timer start');
            }
        }, {
            title: 'end event', parent: 'event-tooltip',
            url: '../../assets/media/icons/process/element-type/event-end.png',
            action: function(el, i) {
                console.log('edit type end');
            }
        }, {
            title: 'message end event', parent: 'event-tooltip',
            url: '../../assets/media/icons/process/element-type/event-end-msg.png',
            action: function(el, i) {
                console.log('edit type message end');
            }
        }, {
            title: 'user task', parent: 'task-tooltip',
            url: '../../assets/media/icons/process/element-type/task-user.png',
            action: function(el, i) {
                console.log('edit user task');
            }
        }, {
            title: 'manual task', parent: 'task-tooltip',
            url: '../../assets/media/icons/process/element-type/task-manual.png',
            action: function(el, i) {
                console.log('edit manual task');
            }
        }, {
            title: 'script task', parent: 'task-tooltip',
            url: '../../assets/media/icons/process/element-type/task-script.png',
            action: function(el, i) {
                console.log('edit script task');
            }
        }, {
            title: 'send task', parent: 'task-tooltip',
            url: '../../assets/media/icons/process/element-type/task-send.png',
            action: function(el, i) {
                console.log('edit send task');
            }
        }, {
            title: 'receive task', parent: 'task-tooltip',
            url: '../../assets/media/icons/process/element-type/task-receive.png',
            action: function(el, i) {
                console.log('edit receive task');
            }
        }, {
            title: 'exclusive gateway', parent: 'gateway-tooltip',
            url: '../../assets/media/icons/process/element-type/gateway-exclusive.png',
            action: function(el, i) {
                console.log('edit exclusive gateway');
            }
        }, {
            title: 'parallel gateway', parent: 'gateway-tooltip',
            url: '../../assets/media/icons/process/element-type/gateway-parallel.png',
            action: function(el, i) {
                console.log('edit parallel gateway');
            }
        }, {
            title: ' inclusive gateway', parent: 'gateway-tooltip',
            url: '../../assets/media/icons/process/element-type/gateway-inclusive.png',
            action: function(el, i) {
                console.log('edit exclusive gateway');
            }
        }
    ]

    const elementsKeys = Object.getOwnPropertyNames(elementsProperties);

    /**
     * elements.
     *
     * @param elem 선택된 element
     */
    function getElementDataProperty(elem) {
        const elemId = elem.node().id;
        let elements = AliceProcessEditor.data.elements;
        let filterData = elements.filter(function(attr) { return attr.id === elemId; });
        if (filterData.length === 0) {
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem);
                    elements.push({
                        id: elemId,
                        category: elementsKeys[i],
                        type: '',
                        display: {width: bbox.width, height: bbox.height, 'position-x': bbox.x, 'position-y': bbox.y},
                        data: {}
                    });
                    break;
                }
            }
            return {};
        } else {
            return filterData[0].data;
        }
    }

    /**
     * 해당 element의 툴팁메뉴를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setActionTooltipItem(elem) {
        if (typeof elem === 'undefined') {
            return;
        }

        let actionTooltip = tooltipItems.filter(function(item) { return item.parent === 'action-tooltip'; });
        if (elem.classed('group') || elem.classed('annotation')) {
            actionTooltip = actionTooltip.slice(0, 2);
        } else if (elem.classed('connector')) {
            actionTooltip = actionTooltip.slice(0, 1);
        }

        const tooltipItemContainer = d3.select('.alice-process-drawing-board').select('svg').append('g')
            .attr('class', 'alice-tooltip').style('display', 'none');

        const containerWidth = actionTooltip.length * 25 + 5,
              containerHeight = 30;

        tooltipItemContainer.append('rect')
            .attr('class', 'tooltip-container action-tooltip')
            .attr('width', containerWidth)
            .attr('height', containerHeight);

        tooltipItemContainer.selectAll('action-tooltip-item')
            .data(actionTooltip)
            .enter()
            //.append('rect')
            .append('image')
            .attr('class', 'action-tooltip-item')
            .attr('x', function(d, i) { return  5 + (i * 25); })
            .attr('y', 5)
            .attr('width', 20)
            .attr('height', 20)
            //.style('fill', function(d) { return 'url(#alice-tooltip-' + d.title + ')'; })
            .attr('xlink:href', function(d) { return d.url; })
            .on('mousedown', function(d, i) {
                d3.event.stopPropagation();
                d3.selectAll('.action-tooltip-item').nodes().map(function(item) {
                    const url = d3.select(item).attr('xlink:href');
                    const focusIndex = url.indexOf('_focus');
                    if (focusIndex > -1) {
                        d3.select(item).attr('xlink:href', function() {
                            return url.substring(0, focusIndex) + url.substring(focusIndex + 6);
                        });
                    }
                });

                if (d.title === 'edit' || d.title === 'suggest') {
                    d3.select(this).attr('xlink:href', function() {
                        let url = d3.select(this).attr('xlink:href'),
                            lastIndex = url.lastIndexOf('.');
                        return url.substring(0, lastIndex) + '_focus' + url.substring(lastIndex);
                    });
                }
                d.action(elem, i);
            });

        const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem),
              translateX = bbox.cx - containerWidth / 2,
              translateY = (elem.classed('connector') ? bbox.cy :  bbox.y) - containerHeight - 10;
        tooltipItemContainer
            .attr('transform', 'translate(' + translateX + ',' + translateY + ')')
            .style('display', 'block')
            .datum(elem);
    }

    /**
     * show suggest elements tooltip.
     *
     * @param elem 선택된 element
     */
    function setSuggestElementItems(elem) {
        if (elem.classed('group') || elem.classed('annotation') || elem.classed('connector')) {
            return;
        }
        let suggestTooltip = tooltipItems.filter(function(item) { return item.parent === 'suggest-tooltip'; });
        setElementItems(suggestTooltip, elem);
    }

    /**
     * show elements type tooltip.
     *
     * @param elem 선택된 element
     */
    function setElementTypeItems(elem) {
        let elementTypeItems = [];
        let type = '';
        if (elem.classed('event')) {
            type = 'event';
        } else if (elem.classed('task')) {
            type = 'task';
        } else if (elem.classed('gateway')) {
            type = 'gateway';
        }
        if (type) {
            elementTypeItems = tooltipItems.filter(function(item) { return item.parent === type + '-tooltip'; });
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

        const tooltipItemContainer = d3.select('g.alice-tooltip'),
              actionTooltipContainer = tooltipItemContainer.select('.action-tooltip'),
              containerWidth = 30,
              containerHeight = items.length * 25 + 5;

        const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(actionTooltipContainer),
              x = bbox.x + bbox.width + 5,
              y = bbox.y;

        tooltipItemContainer.append('rect')
            .attr('class', 'tooltip-container element-tooltip')
            .attr('x', x)
            .attr('y', y)
            .attr('width', containerWidth)
            .attr('height', containerHeight);

        tooltipItemContainer.selectAll('element-tooltip-item')
            .data(items)
            .enter()
            .append('image')
            .attr('class', 'element-tooltip-item')
            .attr('x', x + 5)
            .attr('y', function(d, i) { return y + 5 + (i * 25); })
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', function(d) { return d.url; })
            .on('mousedown', function(d, i) {
                d3.event.stopPropagation();
                d.action(elem, d, i);
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
        if (typeof elem !== 'undefined') { // show element properties
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let properties = elementsProperties[elementsKeys[i]];
                    makePropertiesItem( elem.node().id, properties, getElementDataProperty(elem));
                    break;
                }
            }
        } else { // show process properties
            makePropertiesItem(AliceProcessEditor.data.process.id, processProperties, AliceProcessEditor.data.process);
        }
    }

    /**
     * 변경된 element 속성 값을 저장한다.
     *
     * @param id element ID
     */
    function changePropertiesValue(id) {
        const container = document.querySelector('.alice-process-properties-panel');
        const propertyObjects = container.querySelectorAll('input, select, textarea');
        if (id === AliceProcessEditor.data.process.id) {
            for (let i = 0, len = propertyObjects.length; i < len; i++) {
                let propertyObject = propertyObjects[i];
                AliceProcessEditor.data.process[propertyObject.name] = propertyObject.value;
            }
        } else {
            let elementData = AliceProcessEditor.data.elements.filter(function(attr) { return attr.id === id; });
            for (let i = 0, len = propertyObjects.length; elementData.length > 0 && i < len; i++) {
                let propertyObject = propertyObjects[i];
                let propertyValue = propertyObject.value;
                if (propertyObject.tagName.toUpperCase() === 'INPUT' && propertyObject.type.toUpperCase() === 'CHECKBOX') {
                    propertyValue = propertyObject.checked ? 'Y' : 'N';
                }
                elementData[0].data[propertyObject.name] = propertyValue;
            }
        }
    }

    /**
     * 속성 항목을 생성한다.
     *
     * @param id ID
     * @param properties 속성정보목록
     * @param data 데이터속성
     */
    function makePropertiesItem(id, properties, data) {
        const propertiesContainer = document.querySelector('.alice-process-properties-panel');
        propertiesContainer.innerHTML = '';

        for (let i = 0, len = properties.length; i < len; i++) {
            let property = properties[i];
            let propertyContainer = document.createElement('p');
            let labelObject = document.createElement('label');
            labelObject.htmlFor =  property.attribute;
            labelObject.textContent = property.name;
            propertyContainer.appendChild(labelObject);

            let elementObject;
            if (property.type === 'text') {
                elementObject = document.createElement('input');
            } else if (property.type === 'textarea') {
                elementObject = document.createElement('textarea');
                elementObject.rows = 5;
                elementObject.style.resize = 'none';
            } else if (property.type === 'checkbox') {
                elementObject = document.createElement('input');
                elementObject.type = 'checkbox';
                if (data[property.attribute] && data[property.attribute] === 'Y') {
                    elementObject.checked = true;
                }
            } else if (property.type === 'selectbox') {
                elementObject = document.createElement('select');
                const optionList = property['sub-list'].split(',');
                for (let j = 0, optionLength = optionList.length; j < optionLength; j++) {
                    let option = document.createElement('option');
                    option.value = optionList[j];
                    option.text = optionList[j];
                    elementObject.appendChild(option);
                }
            }
            if (elementObject) {
                elementObject.id = property.attribute;
                elementObject.name = property.attribute;
                if (data[property.attribute] && property.type !== 'checkbox') {
                    elementObject.value = data[property.attribute];
                }
                if (id === AliceProcessEditor.data.process.id && property.attribute === 'name') {
                    elementObject.addEventListener('keyup', function(event) {
                        document.querySelector('.process-name').textContent = this.value;
                    });
                }
                elementObject.addEventListener('change', function(event) {
                    changePropertiesValue(id);
                });
                propertyContainer.appendChild(elementObject);
            }
            propertiesContainer.appendChild(propertyContainer);
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

    function loadTooltipItems() {
        const defs = d3.select('svg').append('defs');
        defs.selectAll('pattern').data(tooltipItems)
            .enter()
            .append('pattern')
            .attr('id', function(d) { return d.parent + '-' + d.title; })
            .attr('width', 1)
            .attr('height', 1)
            .attr('patternUnits', 'objectBoundingBox')
            .append('image')
            .attr('x', 0)
            .attr('y', 0)
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', function(d) { return d.url; })
    }

    exports.loadTooltipItems = loadTooltipItems;
    exports.setElementMenu = setElementMenu;
    exports.setActionTooltipItem = setActionTooltipItem;
    Object.defineProperty(exports, '__esModule', {value: true});
})));