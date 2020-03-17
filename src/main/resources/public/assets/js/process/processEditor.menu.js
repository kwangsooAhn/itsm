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
            type: 'delete', parent: 'action',
            url: iconDirectory + '/tooltip/delete.png',
            action: function(el) {
                deleteElement(el);
            }
        },
        {
            type: 'copy', parent: 'action',
            url: iconDirectory + '/tooltip/copy.png',
            action: function(el) {
                copyElement(el);
            }
        },
        {
            type: 'edit', parent: 'action',
            url: iconDirectory + '/tooltip/edit.png',
            focus_url: iconDirectory + '/tooltip/edit_focus.png',
            action: function(el) {
                setElementCategoryItems(el);
            }
        },
        {
            type: 'suggest', parent: 'action',
            url: iconDirectory + '/tooltip/suggest.png',
            focus_url: iconDirectory + '/tooltip/suggest_focus.png',
            action: function(el) {
                setSuggestElementItems(el);
            }
        },
        {
            type: 'userTask', parent: 'suggest',
            url: iconDirectory + '/suggestion/usertask.png',
            action: function(el) {
                suggestElement(el, 'userTask');
            }
        }, {
            type: 'manualTask', parent: 'suggest',
            url: iconDirectory + '/suggestion/manual.png',
            action: function(el) {
                suggestElement(el, 'manualTask');
            }
        }, {
            type: 'exclusiveGateway', parent: 'suggest',
            url: iconDirectory + '/suggestion/gateways.png',
            action: function(el) {
                suggestElement(el, 'exclusiveGateway');
            }
        }, {
            type: 'commonEnd', parent: 'suggest',
            url: iconDirectory + '/suggestion/end.png',
            action: function(el) {
                suggestElement(el, 'commonEnd');
            }
        }, {
            type: 'commonStart', parent: 'event',
            url: iconDirectory + '/element-type/event-start.png',
            action: function(el) {
                editElementType(el,'commonStart');
            }
        }, {
            type: 'messageStart', parent: 'event',
            url: iconDirectory + '/element-type/event-start-msg.png',
            element_url: iconDirectory + '/element-type/task-receive.png',
            action: function(el) {
                editElementType(el,'messageStart');
            }
        }, {
            type: 'timerStart', parent: 'event',
            url: iconDirectory + '/element-type/event-start-timer.png',
            element_url: iconDirectory + '/element-type/event-start-timer.png',
            action: function(el) {
                editElementType(el,'timerStart');
            }
        }, {
            type: 'commonEnd', parent: 'event',
            url: iconDirectory + '/element-type/event-end.png',
            action: function(el) {
                editElementType(el,'commonEnd');
            }
        }, {
            type: 'messageEnd', parent: 'event',
            url: iconDirectory + '/element-type/event-end-msg.png',
            element_url: iconDirectory + '/element-type/task-send.png',
            action: function(el) {
                editElementType(el,'messageEnd');
            }
        }, {
            type: 'userTask', parent: 'task',
            url: iconDirectory + '/element-type/task-user.png',
            element_url: iconDirectory + '/element-type/task-user.png',
            action: function(el) {
                editElementType(el,'userTask');
            }
        }, {
            type: 'manualTask', parent: 'task',
            url: iconDirectory + '/element-type/task-manual.png',
            element_url: iconDirectory + '/element-type/task-manual.png',
            action: function(el) {
                editElementType(el,'manualTask');
            }
        }, {
            type: 'scriptTask', parent: 'task',
            url: iconDirectory + '/element-type/task-script.png',
            element_url: iconDirectory + '/element-type/task-script.png',
            action: function(el) {
                editElementType(el,'scriptTask');
            }
        }, {
            type: 'sendTask', parent: 'task',
            url: iconDirectory + '/element-type/task-send.png',
            element_url: iconDirectory + '/element-type/task-send.png',
            action: function(el) {
                editElementType(el,'sendTask');
            }
        }, {
            type: 'receiveTask', parent: 'task',
            url: iconDirectory + '/element-type/task-receive.png',
            element_url: iconDirectory + '/element-type/task-receive.png',
            action: function(el) {
                editElementType(el,'receiveTask');
            }
        }, {
            type: 'subprocess', parent: 'subprocess',
            url: iconDirectory + '/element-type/subprocess.png',
            element_url: iconDirectory + '/element-type/subprocess.png',
            action: function(el) {}
        }, {
            type: 'exclusiveGateway', parent: 'gateway',
            url: iconDirectory + '/element-type/gateway-exclusive.png',
            element_url: iconDirectory + '/element-type/gateway-exclusive.png',
            action: function(el) {
                editElementType(el,'exclusiveGateway');
            }
        }, {
            type: 'parallelGateway', parent: 'gateway',
            url: iconDirectory + '/element-type/gateway-parallel.png',
            element_url: iconDirectory + '/element-type/gateway-parallel.png',
            action: function(el) {
                editElementType(el,'parallelGateway');
            }
        }, {
            type: 'inclusiveGateway', parent: 'gateway',
            url: iconDirectory + '/element-type/gateway-inclusive.png',
            element_url: iconDirectory + '/element-type/event-end.png',
            action: function(el) {
                editElementType(el,'inclusiveGateway');
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
     * element 기본 타입을 조회하여 리턴한다.
     *
     * @param category element category
     * @return {String} 기본 타입
     */
    function getElementDefaultType(category) {
        let type = elementsProperties[category][0].type;
        let defaultTypeProperties = elementsProperties[category].filter(function(prop) { return prop.default === 'true'; });
        if (defaultTypeProperties.length > 0) {
            type = defaultTypeProperties[0].type;
        }
        return type;
    }

    /**
     * element 타입을 변경한다.
     *
     * @param element 변경할 대상 element
     * @param type 변경타입
     */
    function editElementType(element, type) {
        const elementId = element.node().id,
              elements = AliceProcessEditor.data.elements;
        const elementData = elements.filter(function(elem) { return elem.id === elementId; })[0];
        console.debug('current element type: %s, edit element type: %s', elementData.type, type);
        if (elementData.type === type) {
            return;
        }
        const category = getElementCategory(type);
        let typeData = getAttributeData(category, type);
        Object.keys(typeData).forEach(function(newKey) {
            Object.keys(elementData.data).forEach(function(oldKey) {
                if (newKey === oldKey) {
                    typeData[newKey] = elementData.data[oldKey];
                }
            });
        });
        elementData.type = type;
        elementData.data = typeData;

        changeElementType(element, type);
        d3.select('g.alice-tooltip').remove();
        setElementMenu(element)
        console.debug('edited element [%s]!!', type);
    }

    /**
     * element 의 타입에 따라 이미지를 변경한다.
     *
     * @param element target element
     * @param type 변경될 타입
     */
    function changeElementType(element, type) {
        const category = getElementCategory(type);
        const typeList = elementsProperties[category];
        typeList.forEach(function(t) { element.classed(t.type, t.type === type); });
        d3.select(element.node().parentNode).select('.element-type').style('fill', 'url(#' + category + '-' + type + '-element)');
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

        const elementId = elem.node().id,
              elements = AliceProcessEditor.data.elements;

        let elemList = elements.filter(function(attr) { return attr.id === elementId; });
        if (elemList.length > 0) {
            return;
        }

        const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem);
        let elemData = {};
        elemData.id = elementId;
        if (elem.classed('node')) {
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let elemType = '';
                    if (elementsKeys[i] === 'artifact' && elem.classed('group')) {
                        elemType = 'groupArtifact';
                    } else {
                        elemType = getElementDefaultType(elementsKeys[i]);
                    }
                    elemData.type = elemType;
                    elemData.display = {'width': bbox.width, 'height': bbox.height, 'position-x': bbox.cx, 'position-y': bbox.cy};
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
            elements.forEach(function(e) {
                if (e.id === data.source.node().id) { elemData.data['start-name'] = e.data.name; }
                if (e.id === data.target.node().id) { elemData.data['end-name'] = e.data.name; }
            });
        }
        if (elemData.data.name) {
            AliceProcessEditor.changeTextToElement(elementId, elemData.data.name);
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
            attributeList.forEach(function(attr) {
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

        let actionTooltip = tooltipItems.filter(function(item) { return item.parent === 'action'; });
        if (elem.classed('subprocess')) {
            actionTooltip = actionTooltip.filter(function(tooltip) { return tooltip.type !== 'edit'; });
        } else if (elem.classed('artifact')) {
            actionTooltip = actionTooltip.filter(function(tooltip) { return tooltip.type === 'delete' || tooltip.type === 'copy'; });
        } else if (elem.classed('connector')) {
            actionTooltip = actionTooltip.filter(function(tooltip) { return tooltip.type === 'delete'; });
        }

        if (!elem.classed('gateway')) {
            let isSuggest = true;
            let elementId = elem.node().id;
            let connectors = AliceProcessEditor.data.elements.filter(function(attr) { return attr.type === 'arrowConnector'; });
            connectors.forEach(function(c) {
                let connectorNode = document.getElementById(c.id);
                if (connectorNode) {
                    const data = connectorNode.__data__;
                    if (data.source.node().id === elementId) {
                        isSuggest = false;
                    }
                }
            });
            if (elem.classed('commonEnd') || elem.classed('messageEnd')) {
                isSuggest = false;
            }
            if (!isSuggest) {
                actionTooltip = actionTooltip.filter(function(tooltip) { return tooltip.type !== 'suggest'; });
            }
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
                d.action(elem);
            });

        const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem),
              gTransform = d3.zoomTransform(d3.select('g.element-container').node()),
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

        let suggestTooltip = tooltipItems.filter(function(item) { return item.parent === 'suggest'; });
        setElementItems(suggestTooltip, elem);
    }

    /**
     * show elements category tooltip.
     *
     * @param elem 선택된 element
     */
    function setElementCategoryItems(elem) {
        let elementTypeItems = [];
        let category = '';
        if (elem.classed('event')) {
            category = 'event';
        } else if (elem.classed('task')) {
            category = 'task';
        } else if (elem.classed('gateway')) {
            category = 'gateway';
        }
        if (category) {
            elementTypeItems = tooltipItems.filter(function(item) { return item.parent === category; });
        }
        setElementItems(elementTypeItems, elem);
    }

    /**
     * element 정보를 조회하여 리턴한다.
     *
     * @param elem element
     * @return {*}
     */
    function getElementData(elem) {
        const elementId = elem.node().id,
              elements = AliceProcessEditor.data.elements;
        return elements.filter(function(e) { return e.id === elementId; })[0];
    }

    /**
     * element 를 저장 데이터 및 화면에서 제거한다.
     *
     * @param elem 대상 element
     */
    function deleteElement(elem) {
        d3.select('g.alice-tooltip').remove();
        const elementId = elem.node().id,
              elements = AliceProcessEditor.data.elements;
        elements.forEach(function(e, i) {
            if (elementId === e.id) { elements.splice(i, 1); }
        });
        d3.select(elem.node().parentNode).remove();

        // Also delete the connector connected to the target element.
        if (!elem.classed('connector')) {
            for (let i = elements.length - 1; i >= 0; i--) {
                if (elements[i].type === 'arrowConnector') {
                    let connectorNode = document.getElementById(elements[i].id);
                    if (connectorNode) {
                        const data = connectorNode.__data__;
                        if (data.source.node().id === elementId || data.target.node().id === elementId) {
                            elements.splice(i, 1);
                            d3.select(connectorNode.parentNode).remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * element 를 복사하고 데이터에 추가한다.
     *
     * @param elem 복제 대상 element
     */
    function copyElement(elem) {
        d3.select('g.alice-tooltip').remove();
        const targetElementData = getElementData(elem);
        let elemData = JSON.parse(JSON.stringify(targetElementData));
        elemData.display['position-x'] = elemData.display['position-x'] + 10;
        elemData.display['position-y'] = elemData.display['position-y'] + 10;
        let node = AliceProcessEditor.addElement(elemData);
        if (node) {
            const nodeId = node.nodeElement.attr('id');
            elemData.id = nodeId;
            AliceProcessEditor.data.elements.push(elemData);

            AliceProcessEditor.removeElementSelected();
        }
    }

    /**
     * suggest element 를 connector 와 함께 추가한다.
     *
     * @param elem source element
     * @param type 추가할 element 타입
     */
    function suggestElement(elem, type) {
        d3.select('g.alice-tooltip').remove();

        const targetElementData = getElementData(elem);
        const targetBbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem);
        const elemData = {};
        elemData.type = type;
        elemData.display = {
            'position-x': targetBbox.cx + (targetBbox.width / 2) + 120,
            'position-y': targetElementData.display['position-y']
        };
        let category = getElementCategory(type);
        elemData.data = getAttributeData(category, type);

        let node = AliceProcessEditor.addElement(elemData);
        if (node) {
            const nodeId = node.nodeElement.attr('id'),
                  bbox = AliceProcessEditor.utils.getBoundingBoxCenter(node.nodeElement);
            elemData.id = nodeId;
            elemData.display.width = bbox.width;
            elemData.display.height = bbox.height;
            AliceProcessEditor.data.elements.push(elemData);

            AliceProcessEditor.removeElementSelected();
            AliceProcessEditor.connectElement(elem, node.nodeElement);
        }
    }

    /**
     * show elements type tooltip.
     *
     * @param items tooltip 에 표시할 항목목록
     * @param elem 선택된 element
     */
    function setElementItems(items, elem) {
        d3.selectAll('.element-tooltip-item').remove();
        d3.selectAll('.element-tooltip').remove();
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
            .on('mousedown', function(d) {
                d3.event.stopPropagation();
                d.action(elem);
            });
    }

    /**
     * 해당 element의 속성을 표시한다.
     *
     * @param elem 선택된 element
     */
    function setProperties(elem) {
        if (typeof elem !== 'undefined') { // show element properties
            const elementId = elem.node().id;
            const elements = AliceProcessEditor.data.elements;
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let property = elements.filter(function(attr) { return attr.id === elementId; })[0];
                    let properties = elementsProperties[elementsKeys[i]];
                    let attributes = properties.filter(function(p){ return p.type === property.type; });
                    if (attributes.length > 0) {
                        makePropertiesItem(elementId, attributes[0], property.data);
                    }
                    break;
                }
            }
        } else { // show process properties
            makePropertiesItem(AliceProcessEditor.data.process.id, processProperties, AliceProcessEditor.data.process);
        }
    }

    /**
     * 변경된 element display 속성 값을 저장한다.
     *
     * @param id element ID
     */
    function changeDisplayValue(id) {
        let elementData = AliceProcessEditor.data.elements.filter(function(attr) { return attr.id === id; });
        if (elementData.length > 0) {
            const nodeElement = d3.select(document.getElementById(id));
            const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(nodeElement);
            elementData[0].display = {'width': bbox.width, 'height': bbox.height, 'position-x': bbox.cx, 'position-y': bbox.cy};
        }
    }

    /**
     * 변경된 element data 속성 값을 저장한다.
     *
     * @param id element ID
     */
    function changePropertiesDataValue(id) {
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

            let connectors = AliceProcessEditor.data.elements.filter(function(attr) { return attr.type === 'arrowConnector'; });
            for (let i = 0, len = connectors.length; i < len; i++) {
                if (connectors[i].data['start-id'] === id) {
                    connectors[i].data['start-name'] = elementData[0].data.name;
                }
                if (connectors[i].data['end-id'] === id) {
                    connectors[i].data['end-name'] = elementData[0].data.name;
                }
            }
        }
    }

    /**
     * 속성 항목을 생성한다.
     *
     * @param id ID
     * @param properties 속성정보
     * @param data 데이터속성
     */
    function makePropertiesItem(id, properties, data) {
        const propertiesContainer = document.querySelector('.alice-process-properties-panel');
        propertiesContainer.innerHTML = '';
        const propertiesDivision = properties.attribute;
        if (id !== AliceProcessEditor.data.process.id) {
            let elementName = document.createElement('h2');
            elementName.textContent = properties.name;
            propertiesContainer.appendChild(elementName);
        }

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
                switch (property.type) {
                    case 'inputbox':
                        elementObject = document.createElement('input');
                        break;
                    case 'inputbox-readonly':
                        elementObject = document.createElement('input');
                        elementObject.readOnly = true;
                        break;
                    case 'textarea':
                        elementObject = document.createElement('textarea');
                        elementObject.style.resize = 'none';
                        break;
                    case 'checkbox':
                        elementObject = document.createElement('input');
                        elementObject.type = 'checkbox';
                        if (data[property.id] && data[property.id] === 'Y') {
                            elementObject.checked = true;
                        }
                        break;
                    case 'select':
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
                    if (property.id === 'name') {
                        let keyupHandler = function(e) {
                            AliceProcessEditor.changeTextToElement(id, this.value);
                        };
                        if (id === AliceProcessEditor.data.process.id) {
                            keyupHandler = function(e) {
                                document.querySelector('.process-name').textContent = this.value;
                            };
                        }
                        elementObject.addEventListener('keyup', keyupHandler);
                    }
                    elementObject.addEventListener('change', function(event) {
                        changePropertiesDataValue(id);
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
     *
     * @param processId 프로세스 ID
     */
    function loadItems(processId) {
        /**
         * load process data.
         */
        const loadProcessData = function() {
            aliceJs.sendXhr({
                method: 'GET',
                url: '/rest/processes/data/' + processId,
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
        };
        /**
         * load element attribute data.
         */
        const loadElementData = function() {
            aliceJs.sendXhr({
                method: 'GET',
                url: '/assets/js/process/elementAttribute.json',
                callbackFunc: function(xhr) {
                    elementsProperties = JSON.parse(xhr.responseText);
                    elementsKeys = Object.getOwnPropertyNames(elementsProperties);
                    loadProcessData();
                },
                contentType: 'application/json; charset=utf-8'
            });
        };
        // load process attribute data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/process/processAttribute.json',
            callbackFunc: function(xhr) {
                processProperties = JSON.parse(xhr.responseText);
                loadElementData();
            },
            contentType: 'application/json; charset=utf-8'
        });

        // add pattern image. for tooltip item image.
        const imageLoadingList = [];
        tooltipItems.forEach(function(item) {
            let data = {};
            data.id = item.parent + '-' + item.type;
            data.url = item.url;
            imageLoadingList.push(data);
            if (item.element_url) {
                let elemData = {};
                elemData.id = item.parent + '-' + item.type + '-element';
                elemData.url = item.element_url;
                imageLoadingList.push(elemData);
            }
            if (item.focus_url) {
                let focusData = {};
                focusData.id = item.parent + '-' + item.type + '-focus';
                focusData.url = item.focus_url;
                imageLoadingList.push(focusData);
            }
        });

        const defs = d3.select('svg').select('defs');
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
            .attr('xlink:href', function(d) { return d.url; });
    }

    exports.data = data;
    exports.loadItems = loadItems;
    exports.addElementProperty = addElementProperty;
    exports.setElementMenu = setElementMenu;
    exports.setActionTooltipItem = setActionTooltipItem;
    exports.getElementCategory = getElementCategory;
    exports.getElementDefaultType = getElementDefaultType;
    exports.changeDisplayValue = changeDisplayValue;
    exports.changeElementType = changeElementType;
    Object.defineProperty(exports, '__esModule', {value: true});
})));