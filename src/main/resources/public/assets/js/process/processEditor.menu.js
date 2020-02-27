(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceProcessEditor = global.AliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    const data = {};

    const iconDirectory = '../../assets/media/icons/process';
    const itemSize = 20;
    const itemMargin = 8;

    let processProperties = {},
        elementsProperties = {},
        elementsKeys = [];

    const tooltipItems = [
        {
            type: 'delete', parent: 'action-tooltip',
            url: iconDirectory + '/tooltip/delete.png',
            action: function(el, i) {
                removeElementItems();
                console.log('remove');
            }
        },
        {
            type: 'copy', parent: 'action-tooltip',
            url: iconDirectory + '/tooltip/copy.png',
            action: function(el, i) {
                removeElementItems();
                console.log('copy');
            }
        },
        {
            type: 'edit', parent: 'action-tooltip',
            url: iconDirectory + '/tooltip/edit.png',
            focus_url: iconDirectory + '/tooltip/edit_focus.png',
            action: function(el, i) {
                setElementTypeItems(el);
            }
        },
        {
            type: 'suggest', parent: 'action-tooltip',
            url: iconDirectory + '/tooltip/suggest.png',
            focus_url: iconDirectory + '/tooltip/suggest_focus.png',
            action: function(el, i) {
                setSuggestElementItems(el);
            }
        },
        {
            type: 'userTask', parent: 'suggest-tooltip',
            url: iconDirectory + '/suggestion/usertask.png',
            action: function(el, i) {
                console.log('add user task');
            }
        }, {
            type: 'manualTask', parent: 'suggest-tooltip',
            url: iconDirectory + '/suggestion/manual.png',
            action: function(el, i) {
                console.log('add manual task');
            }
        }, {
            type: 'exclusiveGateway', parent: 'suggest-tooltip',
            url: iconDirectory + '/suggestion/gateways.png',
            action: function(el, i) {
                console.log('add exclusive gateway');
            }
        }, {
            type: 'commonEnd', parent: 'suggest-tooltip',
            url: iconDirectory + '/suggestion/end.png',
            action: function(el, i) {
                console.log('add end event');
            }
        }, {
            type: 'commonStart', parent: 'event-tooltip',
            url: iconDirectory + '/element-type/event-start.png',
            action: function(el, i) {
                console.log('edit type start');
            }
        }, {
            type: 'messageStart', parent: 'event-tooltip',
            url: iconDirectory + '/element-type/event-start-msg.png',
            action: function(el, i) {
                console.log('edit type message start');
            }
        }, {
            type: 'timerStart', parent: 'event-tooltip',
            url: iconDirectory + '/element-type/event-start-timer.png',
            action: function(el, i) {
                console.log('edit type timer start');
            }
        }, {
            type: 'commonEnd', parent: 'event-tooltip',
            url: iconDirectory + '/element-type/event-end.png',
            action: function(el, i) {
                console.log('edit type end');
            }
        }, {
            type: 'messageEnd', parent: 'event-tooltip',
            url: iconDirectory + '/element-type/event-end-msg.png',
            action: function(el, i) {
                console.log('edit type message end');
            }
        }, {
            type: 'userTask', parent: 'task-tooltip',
            url: iconDirectory + '/element-type/task-user.png',
            action: function(el, i) {
                console.log('edit user task');
            }
        }, {
            type: 'manualTask', parent: 'task-tooltip',
            url: iconDirectory + '/element-type/task-manual.png',
            action: function(el, i) {
                console.log('edit manual task');
            }
        }, {
            type: 'scriptTask', parent: 'task-tooltip',
            url: iconDirectory + '/element-type/task-script.png',
            action: function(el, i) {
                console.log('edit script task');
            }
        }, {
            type: 'sendTask', parent: 'task-tooltip',
            url: iconDirectory + '/element-type/task-send.png',
            action: function(el, i) {
                console.log('edit send task');
            }
        }, {
            type: 'receiveTask', parent: 'task-tooltip',
            url: iconDirectory + '/element-type/task-receive.png',
            action: function(el, i) {
                console.log('edit receive task');
            }
        }, {
            type: 'exclusiveGateway', parent: 'gateway-tooltip',
            url: iconDirectory + '/element-type/gateway-exclusive.png',
            action: function(el, i) {
                console.log('edit exclusive gateway');
            }
        }, {
            type: 'parallelGateway', parent: 'gateway-tooltip',
            url: iconDirectory + '/element-type/gateway-parallel.png',
            action: function(el, i) {
                console.log('edit parallel gateway');
            }
        }, {
            type: 'inclusiveGateway', parent: 'gateway-tooltip',
            url: iconDirectory + '/element-type/gateway-inclusive.png',
            action: function(el, i) {
                console.log('edit exclusive gateway');
            }
        }
    ];

    /**
     * element 타입으로 종류를 조회하여 리턴한다.
     *
     * @param type element 타입
     * @return {string} element 종류
     */
    function getElementCategory(type) {
        let category = '';
        for (let i = 0, len = elementsKeys.length; i < len; i++) {
            let key = elementsKeys[i];
            let typeList = elementsProperties[key];
            for (let j = 0, typeListLen  = typeList.length; j < typeListLen; j++) {
                if (typeList[j].type === type) {
                    category = key;
                    break;
                }
            }
            if (category.length > 0) {
                break;
            }
        }
        return category;
    }

    /**
     * 추가된 element properties 를 data 에 추가한다.
     *
     * @param elem 추가된 element
     */
    function addElementProperty(elem) {
        if (!elem || !elem.node()) {
            return;
        }

        const elemId = elem.node().id,
              elements = AliceProcessEditor.data.elements;

        let elemList = elements.filter(function(attr) { return attr.id === elemId; });
        if (elemList.length > 0) {
            return;
        }

        const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem);
        let elemData = {};
        elemData.id = elemId;
        if (elem.classed('node')) {
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let elemType = elementsProperties[elementsKeys[i]][0].type;
                    if (elementsKeys[i] === 'artifact' && elem.classed('group')) {
                        elemType = 'groupArtifact';
                    }
                    elemData.type = elemType;
                    elemData.display = {'width': bbox.width, 'height': bbox.height, 'position-x': bbox.x, 'position-y': bbox.y};
                    elemData.data = getAttributeData(elementsKeys[i], elemType);
                    break;
                }
            }
        } else {
            const data = elem.node().__data__;
            elemData.type = 'arrowConnector';
            elemData.data = getAttributeData('connector', 'arrowConnector');
            elemData.data['start-id'] = data.source.node().id;
            elemData.data['end-id'] = data.target.node().id;
        }
        elements.push(elemData);
    }

    /**
     * element 속성 정보에서 data 정보를 추출하여 json 으로 리턴한다.
     *
     * @param category element category
     * @param type element type
     * @return Object data JSON
     */
    function getAttributeData(category, type) {
        const data = {};
        let elementTypeList = elementsProperties[category];
        if (!elementTypeList) {
            console.error('No information found for category(%s), type(%s) in the configuration file.', category, type);
            return data;
        }
        let elementTypeData = elementTypeList.filter(function(elem){ return elem.type === type; });
        if (elementTypeData.length > 0) {
            let attributeList = elementTypeData[0].attribute;
            attributeList.forEach(function(attr){
                let items = attr.items;
                items.forEach(function(item){
                    data[item.id] = item.default;
                });
            });
        }
        return data;
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
        if (elem.classed('artifact')) {
            actionTooltip = actionTooltip.slice(0, 2);
        } else if (elem.classed('connector')) {
            actionTooltip = actionTooltip.slice(0, 1);
        }

        const tooltipItemContainer = d3.select('.alice-process-drawing-board').select('svg').append('g')
            .attr('class', 'alice-tooltip').style('display', 'none');

        const containerWidth = actionTooltip.length * (itemSize + itemMargin) + itemMargin,
              containerHeight = itemSize + (itemMargin * 2);

        tooltipItemContainer.append('rect')
            .attr('class', 'tooltip-container action-tooltip')
            .attr('width', containerWidth)
            .attr('height', containerHeight)
            .on('mousedown', function() { d3.event.stopPropagation(); });

        tooltipItemContainer.selectAll('action-tooltip-item')
            .data(actionTooltip)
            .enter()
            .append('rect')
            .attr('class', 'action-tooltip-item')
            .attr('id', function(d) { return 'action-tooltip-item-' + d.type; })
            .attr('x', function(d, i) { return  itemMargin + (i * (itemSize + itemMargin) ); })
            .attr('y', itemMargin)
            .attr('width', itemSize)
            .attr('height', itemSize)
            .style('fill', function(d) { return 'url(#' + d.parent + '-' + d.type + ')'; })
            .on('mousedown', function(d, i) {
                d3.event.stopPropagation();
                actionTooltip.forEach(function(t) {
                    if (t.focus_url) {
                        let item = document.getElementById('action-tooltip-item-' + t.type);
                        d3.select(item).style('fill', 'url(#' + t.parent + '-' + t.type + ')');
                    }
                });
                if (d.focus_url) {
                    d3.select(this).style('fill', 'url(#' + d.parent + '-' + d.type + '-focus)');
                }
                d.action(elem, i);
            });

        const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem),
            gTransform = d3.zoomTransform(d3.select('g.node-container').node()),
            targetX = (bbox.cx + gTransform.x) - containerWidth / 2,
            targetY = (elem.classed('connector') ? bbox.cy + gTransform.y : bbox.y + gTransform.y) - containerHeight - 10;

        tooltipItemContainer
            .attr('transform', 'translate(' + targetX + ',' + targetY + ')')
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
              containerWidth = itemSize + (itemMargin * 2),
              containerHeight = items.length * (itemSize + itemMargin) + itemMargin;

        const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(actionTooltipContainer),
              x = bbox.x + bbox.width + itemMargin,
              y = bbox.y;

        tooltipItemContainer.append('rect')
            .attr('class', 'tooltip-container element-tooltip')
            .attr('x', x)
            .attr('y', y)
            .attr('width', containerWidth)
            .attr('height', containerHeight)
            .on('mousedown', function() { d3.event.stopPropagation(); });

        tooltipItemContainer.selectAll('element-tooltip-item')
            .data(items)
            .enter()
            .append('rect')
            .attr('class', 'element-tooltip-item')
            .attr('x', x + itemMargin)
            .attr('y', function(d, i) { return y + itemMargin + (i * (itemSize + itemMargin)); })
            .attr('width', itemSize)
            .attr('height', itemSize)
            .style('fill', function(d) { return 'url(#' + d.parent + '-' + d.type + ')'; })
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
            const elemId = elem.node().id;
            const elements = AliceProcessEditor.data.elements;
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let property = elements.filter(function(attr) { return attr.id === elemId; })[0];
                    let properties = elementsProperties[elementsKeys[i]];
                    let attributes = properties.filter(function(p){ return p.type === property.type; });
                    if (attributes.length > 0) {
                        makePropertiesItem(elemId, attributes[0].attribute, property.data);
                    }
                    break;
                }
            }
        } else { // show process properties
            makePropertiesItem(AliceProcessEditor.data.process.id, processProperties.attribute, AliceProcessEditor.data.process);
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
    function makePropertiesItem(id, propertiesDivision, data) {
        const propertiesContainer = document.querySelector('.alice-process-properties-panel');
        propertiesContainer.innerHTML = '';

        for (let idx = 0, len = propertiesDivision.length; idx < len; idx++) {
            let title = document.createElement('h3');
            title.textContent = propertiesDivision[idx].title;
            propertiesContainer.appendChild(title);
            let divideLine = document.createElement('hr');
            propertiesContainer.appendChild(divideLine);

            const properties = propertiesDivision[idx].items;
            for (let i = 0, attrLen = properties.length; i < attrLen; i++) {
                const property = properties[i];
                let propertyContainer = document.createElement('p');
                let labelObject = document.createElement('label');
                labelObject.htmlFor =  property.id;
                labelObject.textContent = property.name;
                propertyContainer.appendChild(labelObject);

                let elementObject;
                if (property.type === 'inputbox') {
                    elementObject = document.createElement('input');
                } else if (property.type === 'textarea') {
                    elementObject = document.createElement('textarea');
                    elementObject.style.resize = 'none';
                } else if (property.type === 'checkbox') {
                    elementObject = document.createElement('input');
                    elementObject.type = 'checkbox';
                    if (data[property.id] && data[property.id] === 'Y') {
                        elementObject.checked = true;
                    }
                } else if (property.type === 'select') {
                    elementObject = document.createElement('select');
                    const optionList = property['sub-list'];
                    for (let j = 0, optionLength = optionList.length; j < optionLength; j++) {
                        let option = document.createElement('option');
                        option.value = optionList[j].id;
                        option.text = optionList[j].name;
                        elementObject.appendChild(option);
                    }
                }
                if (elementObject) {
                    elementObject.id = property.id;
                    elementObject.name = property.id;
                    if (data[property.id] && property.type !== 'checkbox') {
                        elementObject.value = data[property.id];
                    }
                    if (id === AliceProcessEditor.data.process.id && property.id === 'name') {
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

    /**
     * tooltip item 에 사용된 이미지 로딩.
     */
    function loadItems(process) {
        d3.json('../../assets/js/process/processAttribute.json').then(function(data) {
            processProperties = data;
            d3.json('../../assets/js/process/elementAttribute.json').then(function(data) {
                elementsProperties = data;
                elementsKeys = Object.getOwnPropertyNames(elementsProperties);

                // load process data.
                aliceJs.sendXhr({
                    method: 'GET',
                    url: '/rest/processes/data/' + process.processId,
                    callbackFunc: function(xhr) {
                        const data = xhr.responseText;
                        console.debug(JSON.parse(data));
                        AliceProcessEditor.data = JSON.parse(data);
                        document.querySelector('.process-name').textContent = AliceProcessEditor.data.process.name;
                        const elements = AliceProcessEditor.data.elements;
                        setElementMenu();
                        AliceProcessEditor.drawProcess(elements);
                    },
                    contentType: 'application/json; charset=utf-8'
                });
            });
        });

        // add pattern image. for tooltip item image.
        const imageLoadingList = [];
        tooltipItems.forEach(function(item){
            let data = {};
            data.id = item.parent + '-' + item.type;
            data.url = item.url;
            imageLoadingList.push(data);
            if (item.focus_url) {
                let focusData = {};
                focusData.id = item.parent + '-' + item.type + '-focus';
                focusData.url = item.focus_url;
                imageLoadingList.push(focusData);
            }
        });

        const defs = d3.select('svg').append('defs');
        defs.selectAll('pattern').data(imageLoadingList)
            .enter()
            .append('pattern')
            .attr('id', function(d) { return d.id; })
            .attr('width', 1)
            .attr('height', 1)
            .attr('patternUnits', 'objectBoundingBox')
            .append('image')
            .attr('x', 0)
            .attr('y', 0)
            .attr('width', 20)
            .attr('height', 20)
            .attr('xlink:href', function(d) { return d.url; });
    }

    exports.data = data;
    exports.loadItems = loadItems;
    exports.addElementProperty = addElementProperty;
    exports.setElementMenu = setElementMenu;
    exports.setActionTooltipItem = setActionTooltipItem;
    exports.getElementCategory = getElementCategory;
    Object.defineProperty(exports, '__esModule', {value: true});
})));