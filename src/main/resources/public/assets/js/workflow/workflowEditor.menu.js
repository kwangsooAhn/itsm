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

    const elementsKeys = Object.getOwnPropertyNames(elementsProperties);

    /**
     * elements.
     *
     * @param elem 선택된 element
     */
    function getElementDataProperty(elem) {
        const elemId = elem.node().id;
        let elements = wfEditor.data.elements;
        let filterData = elements.filter(function(attr) { return attr.id === elemId; });
        if (filterData.length === 0) {
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    const bbox = wfEditor.utils.getBoundingBoxCenter(elem);
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
            actionTooltip = actionTooltip.slice(0, 2);
        } else if (elem.classed('connector')) {
            actionTooltip = actionTooltip.slice(0, 1);
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
            .attr('x', function(d, i) { return  5 + (i * 25); })
            .attr('y', 5)
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', function(d) { return d.image; })
            .on('mousedown', function(d, i) {
                d3.event.stopPropagation();
                d.action(elem, i);
            });

        const bbox = wfEditor.utils.getBoundingBoxCenter(elem),
              translateX = bbox.cx - containerWidth / 2,
              translateY = (elem.classed('connector') ? bbox.cy :  bbox.y) - containerHeight - 10;
        tooltipItemContainer
            .attr('transform', 'translate(' + translateX + ',' + translateY + ')')
            .style('display', 'block')
            .datum(elem);
    }

    /**
     * show favorites elements tooltip.
     *
     * @param elem 선택된 element
     */
    function setFavoritesElementItems(elem) {
        if (elem.classed('group') || elem.classed('annotation') || elem.classed('connector')) {
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

        const bbox = wfEditor.utils.getBoundingBoxCenter(actionTooltipContainer),
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
            .attr('y', function(d, i) { return y + 5 + (i * 25); })
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', function(d) { return d.image; })
            .on('mousedown', function(d, i) {
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
        if (typeof elem !== 'undefined') { // show element properties
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let properties = elementsProperties[elementsKeys[i]];
                    makePropertiesItem( elem.node().id, properties, getElementDataProperty(elem));
                    break;
                }
            }
        } else { // show workflow properties
            makePropertiesItem(wfEditor.data.process.id, workflowProperties, wfEditor.data.process);
        }
    }

    /**
     * 변경된 element 속성 값을 저장한다.
     *
     * @param id element ID
     */
    function changePropertiesValue(id) {
        const container = document.querySelector('.alice-workflow-properties-panel');
        const propertyObjects = container.querySelectorAll('input, select, textarea');
        if (id === wfEditor.data.process.id) {
            for (let i = 0, len = propertyObjects.length; i < len; i++) {
                let propertyObject = propertyObjects[i];
                wfEditor.data.process[propertyObject.name] = propertyObject.value;
            }
        } else {
            let elementData = wfEditor.data.elements.filter(function(attr) { return attr.id === id; });
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
        const propertiesContainer = document.querySelector('.alice-workflow-properties-panel');
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
                elementObject.style.width = '180px';
            } else if (property.type === 'textarea') {
                elementObject = document.createElement('textarea');
                elementObject.rows = 5;
                elementObject.style.resize = 'none';
                elementObject.style.width = '180px';
            } else if (property.type === 'checkbox') {
                elementObject = document.createElement('input');
                elementObject.type = 'checkbox';
                if (data[property.attribute] && data[property.attribute] === 'Y') {
                    elementObject.checked = true;
                }
            } else if (property.type === 'selectbox') {
                elementObject = document.createElement('select');
                elementObject.style.width = '180px';
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
                if (id === wfEditor.data.process.id && property.attribute === 'name') {
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

    exports.setElementMenu = setElementMenu;
    exports.setActionTooltipItem = setActionTooltipItem;
    Object.defineProperty(exports, '__esModule', {value: true});
})));