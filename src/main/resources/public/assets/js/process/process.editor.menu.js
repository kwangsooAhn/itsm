(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceProcessEditor = global.aliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    const i18nMsgPrefix = 'process.designer.attribute.';
    const data = {};
    const iconDirectory = '/assets/media/icons/process';
    const itemSize = 16;
    const itemMargin = 5.5;
    const assigneeTypeData = {
        users: [],
        groups: []
    };

    let documents = [];

    let processProperties = {},
        elementsProperties = {},
        elementsKeys = [];

    const tooltipItems = [
        {
            type: 'delete', parent: 'action',
            url: iconDirectory + '/tooltip/delete.png',
            action: function() {
                deleteElements();
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
            url: iconDirectory + '/tooltip/task-userTask.png',
            action: function(el) {
                suggestElement(el, 'userTask');
            }
        }, {
            type: 'manualTask', parent: 'suggest',
            url: iconDirectory + '/tooltip/task-manualTask.png',
            action: function(el) {
                suggestElement(el, 'manualTask');
            }
        }, {
            type: 'exclusiveGateway', parent: 'suggest',
            url: iconDirectory + '/tooltip/gateway-exclusiveGateway.png',
            action: function(el) {
                suggestElement(el, 'exclusiveGateway');
            }
        }, {
            type: 'commonEnd', parent: 'suggest',
            url: iconDirectory + '/tooltip/event-commonEnd.png',
            action: function(el) {
                suggestElement(el, 'commonEnd');
            }
        }, {
            type: 'commonStart', parent: 'event',
            url: iconDirectory + '/tooltip/event-commonStart.png',
            action: function(el) {
                editElementType(el,'commonStart');
            }
        }, {
            type: 'timerStart', parent: 'event',
            url: iconDirectory + '/tooltip/event-timerStart.png',
            element_url: iconDirectory + '/element-type/event-start-timer.png',
            element_selected_url: iconDirectory + '/element-type/event-start-timer-selected.png',
            action: function(el) {
                editElementType(el,'timerStart');
            }
        }, {
            type: 'signalSend', parent: 'event',
            url: iconDirectory + '/tooltip/event-signalSend.png',
            element_url: iconDirectory + '/element-type/event-signal-send.png',
            element_selected_url: iconDirectory + '/element-type/event-signal-send-selected.png',
            action: function(el) {
                editElementType(el,'signalSend');
            }
        }, {
            type: 'commonEnd', parent: 'event',
            url: iconDirectory + '/tooltip/event-commonEnd.png',
            action: function(el) {
                editElementType(el,'commonEnd');
            }
        }, {
            type: 'userTask', parent: 'task',
            url: iconDirectory + '/tooltip/task-userTask.png',
            element_url: iconDirectory + '/element-type/task-user.png',
            element_selected_url: iconDirectory + '/element-type/task-user-selected.png',
            action: function(el) {
                editElementType(el,'userTask');
            }
        }, {
            type: 'manualTask', parent: 'task',
            url: iconDirectory + '/tooltip/task-manualTask.png',
            element_url: iconDirectory + '/element-type/task-manual.png',
            element_selected_url: iconDirectory + '/element-type/task-manual-selected.png',
            action: function(el) {
                editElementType(el,'manualTask');
            }
        }, {
            type: 'scriptTask', parent: 'task',
            url: iconDirectory + '/tooltip/task-scriptTask.png',
            element_url: iconDirectory + '/element-type/task-script.png',
            element_selected_url: iconDirectory + '/element-type/task-script-selected.png',
            action: function(el) {
                editElementType(el,'scriptTask');
            }
        }, {
            type: 'subprocess', parent: 'subprocess',
            url: '',
            element_url: iconDirectory + '/element-type/subprocess.png',
            element_selected_url: iconDirectory + '/element-type/subprocess-selected.png',
            action: function(el) {}
        }, {
            type: 'exclusiveGateway', parent: 'gateway',
            url: iconDirectory + '/tooltip/gateway-exclusiveGateway.png',
            element_url: iconDirectory + '/element-type/gateway-exclusive.png',
            element_selected_url: iconDirectory + '/element-type/gateway-exclusive-selected.png',
            action: function(el) {
                editElementType(el,'exclusiveGateway');
            }
        }, {
            type: 'parallelGateway', parent: 'gateway',
            url: iconDirectory + '/tooltip/gateway-parallelGateway.png',
            element_url: iconDirectory + '/element-type/gateway-parallel.png',
            element_selected_url: iconDirectory + '/element-type/gateway-parallel-selected.png',
            action: function(el) {
                editElementType(el,'parallelGateway');
            }
        }, {
            type: 'inclusiveGateway', parent: 'gateway',
            url: iconDirectory + '/tooltip/gateway-inclusiveGateway.png',
            element_url: iconDirectory + '/element-type/gateway-inclusive.png',
            element_selected_url: iconDirectory + '/element-type/gateway-inclusive-selected.png',
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
              elements = aliceProcessEditor.data.elements;
        const elementData = elements.filter(function(elem) { return elem.id === elementId; });
        if (elementData.length) {
            if (elementData[0].type === type) {
                d3.select('g.alice-tooltip').remove();
                return;
            }
            const originElementData = JSON.parse(JSON.stringify(elementData[0]));
            const category = getElementCategory(type);
            let typeData = getAttributeData(category, type);
            delete typeData.name;
            delete typeData.notification;
            delete typeData.description;
            Object.keys(typeData).forEach(function(newKey) {
                Object.keys(elementData[0].data).forEach(function(oldKey) {
                    if (newKey === oldKey) {
                        typeData[newKey] = elementData[0].data[oldKey];
                    }
                });
            });
            elementData[0].id = elementId;
            elementData[0].type = type;
            elementData[0].data = typeData;
            elementData[0].required = getAttributeRequired(category, type);

            d3.select('g.alice-tooltip').remove();
            changeElementType(element, type, true);
            aliceProcessEditor.utils.history.saveHistory([{0: originElementData, 1: JSON.parse(JSON.stringify(elementData[0]))}]);
        }
    }

    /**
     * element 의 타입에 따라 이미지를 변경한다.
     *
     * @param element target element
     * @param type 변경될 타입
     * @param isSelected 선택여부
     */
    function changeElementType(element, type, isSelected) {
        const category = getElementCategory(type);
        const typeList = elementsProperties[category];
        typeList.forEach(function(t) {
            element.classed(t.type, t.type === type);
            d3.select(element.node().parentNode).select('.element-type').classed(t.type, t.type === type);
        });
        let fillUrl = category + '-' + type + '-element';
        if (isSelected) {
            fillUrl = fillUrl + '-selected';
            setElementMenu(element);
        }
        d3.select(element.node().parentNode).select('.element-type')
            .style('fill', 'url(#' + fillUrl + ')');
    }

    /**
     * 엘리먼트 정보 JSON에 엘리먼트 정보를 추가하고, 속성 정보 JSON에서 해당 key를 제외한다.
     *
     * @param elementData 엘리먼트 정보 JSON
     * @param attributeData 속성 정보 JSON
     */
    function setElementData(elementData, attributeData) {
        const elementDataKeys = [
                {key: 'name', default: ''},
                {key: 'notification', default: 'N'},
                {key: 'description', default: ''}
            ];
        elementDataKeys.forEach(function(e) {
            elementData[e.key] = e.default;
            if (typeof attributeData[e.key] !== 'undefined') {
                elementData[e.key] = attributeData[e.key];
                delete attributeData[e.key];
            }
        });
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
              elements = aliceProcessEditor.data.elements;
        let elemList = elements.filter(function(attr) { return attr.id === elementId; });
        if (elemList.length > 0) {
            return;
        }

        const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(elem);
        const elemData = { id: elementId };
        if (elem.classed('node')) {
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let elemType = '';
                    if (elementsKeys[i] === 'artifact' && elem.classed('group')) {
                        elemType = 'groupArtifact';
                    } else {
                        elemType = getElementDefaultType(elementsKeys[i]);
                    }
                    let attributeData = getAttributeData(elementsKeys[i], elemType);
                    setElementData(elemData, attributeData);
                    elemData.type = elemType;
                    elemData.display = {'width': bbox.width, 'height': bbox.height, 'position-x': bbox.cx, 'position-y': bbox.cy};
                    elemData.data = attributeData;
                    elemData.required = getAttributeRequired(elementsKeys[i], elemType);
                    break;
                }
            }
        } else {
            const data = elem.data()[0];
            elemData.type = 'arrowConnector';

            let attributeData = getAttributeData('connector', 'arrowConnector')
            setElementData(elemData, attributeData);
            elemData.data = attributeData;
            elemData.data['start-id'] = data.sourceId;
            elemData.data['end-id'] = data.targetId;
            elements.forEach(function(e) {
                if (e.id === data.sourceId) { elemData.data['start-name'] = e.name; }
                if (e.id === data.targetId) { elemData.data['end-name'] = e.name; }
            });
            elemData.display = {};
            elemData.required = getAttributeRequired('connector', 'arrowConnector');
        }
        if (elemData.name) {
            aliceProcessEditor.changeTextToElement(elementId, elemData.name);
        }
        elements.push(elemData);
        aliceProcessEditor.utils.history.saveHistory([{0: {}, 1: JSON.parse(JSON.stringify(elemData))}]);
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
                items.forEach(function(item) {
                    data[item.id] = item.default;
                });
            });
            delete data.id;
        }
        return data;
    }

    /**
     * element의 속성 정보에서 required 정보를 추출하여 json 으로 리턴한다.
     *
     * @param category element category
     * @param type element type
     * @returns Object required JSON
     */
    function getAttributeRequired(category, type) {
        const required = [];
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
                items.forEach(function(item) {
                    if (item.required === 'Y') {
                        required.push(item.id);
                    }
                });
            });
        }
        return required;
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
            let isSuggest = true,
                isEdit = true;
            let elementId = elem.node().id;
            let connectors = aliceProcessEditor.data.elements.filter(function(attr) { return attr.type === 'arrowConnector'; });
            connectors.forEach(function(c) {
                let connectorNode = document.getElementById(c.id);
                if (connectorNode) {
                    const data = d3.select(connectorNode).data()[0];
                    if (data.sourceId === elementId) {
                        isSuggest = false;
                    }

                    if (elem.classed('commonEnd') && data.targetId === elementId) {
                        isEdit = false;
                    }
                }
            });
            if (elem.classed('commonEnd')) {
                isSuggest = false;
            }
            if (!isSuggest) {
                actionTooltip = actionTooltip.filter(function(tooltip) { return tooltip.type !== 'suggest'; });
            }
            if (!isEdit) {
                actionTooltip = actionTooltip.filter(function(tooltip) { return tooltip.type !== 'edit'; });
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
            .on('mousedown', function() {
                d3.event.stopPropagation();
                d3.event.preventDefault();
            });

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
            .on('mousedown', function(d) {
                d3.event.stopPropagation();
                d3.event.preventDefault();
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

        const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(elem),
              gTransform = d3.zoomTransform(d3.select('g.element-container').node());

        let targetX = (bbox.x + bbox.width + gTransform.x) - containerWidth,
            targetY = bbox.y + gTransform.y - containerHeight - itemMargin;

        if (elem.classed('connector')) {
            let linkData = elem.data()[0];
            if (linkData.midPoint) {
                targetX = (linkData.midPoint[0] + gTransform.x) - containerWidth / 2;
                targetY = linkData.midPoint[1] + gTransform.y - containerHeight - itemMargin;
            } else {
                targetX = (bbox.cx + gTransform.x) - containerWidth / 2;
                targetY = bbox.cy + gTransform.y - containerHeight - 10;
            }
        } else if (elem.classed('gateway')) {
            const gatewayDist = aliceProcessEditor.utils.calcDist(
                [0, 0],
                [aliceProcessEditor.displayOptions.gatewaySize, aliceProcessEditor.displayOptions.gatewaySize]
            );
            targetX = bbox.cx + (gatewayDist / 2) + gTransform.x - containerWidth;
            targetY = bbox.cy + gTransform.y - (gatewayDist / 2) - containerHeight - itemMargin;
        }

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

        if (elem.classed('event')) {
            const elementId = elem.node().id;
            let isSourceConnected = false,
                isTargetConnected = false;
            let connectors = aliceProcessEditor.data.elements.filter(function(e) { return e.type === 'arrowConnector'; });
            connectors.forEach(function(c) {
                if (c.data['start-id'] === elementId) {
                    isSourceConnected = true;
                }
                if (c.data['end-id'] === elementId) {
                    isTargetConnected = true;
                }
            });
            if (isSourceConnected || isTargetConnected) {
                if (elem.classed('commonEnd')) {
                    elementTypeItems = elementTypeItems.filter(function(item) { return item.type === 'commonEnd'; });
                } else {
                    elementTypeItems = elementTypeItems.filter(function(item) { return item.type !== 'commonEnd'; });
                    if (isTargetConnected) {
                        elementTypeItems = elementTypeItems.filter(function(item) { return item.type !== 'commonStart'; });
                    }
                }
            }
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
              elements = aliceProcessEditor.data.elements;
        return elements.filter(function(e) { return e.id === elementId; })[0];
    }

    /**
     * reset element position.
     */
    function resetElementPosition() {
        const containerTransform = d3.zoomTransform(d3.select('g.element-container').node());
        console.debug('container transform : x(%s), y(%s)', containerTransform.x, containerTransform.y);
        if (containerTransform.x !== 0 || containerTransform.y !== 0) {
            aliceProcessEditor.data.elements.forEach(function(elem) {
                const nodeElement = d3.select(document.getElementById(elem.id));
                if (elem.type !== 'arrowConnector') {
                    const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(nodeElement);
                    elem.display['position-x'] = bbox.cx + containerTransform.x;
                    elem.display['position-y'] = bbox.cy + containerTransform.y;
                } else {
                    const linkData = nodeElement.data()[0];
                    if (typeof linkData.midPoint !== 'undefined') {
                        elem.display['mid-point'] =
                            [linkData.midPoint[0] + containerTransform.x, linkData.midPoint[1] + containerTransform.y];
                    }
                    if (typeof linkData.sourcePoint !== 'undefined') {
                        elem.display['source-point'] =
                            [linkData.sourcePoint[0] + containerTransform.x, linkData.sourcePoint[1] + containerTransform.y];
                    }
                    if (typeof linkData.targetPoint !== 'undefined') {
                        elem.display['target-point'] =
                            [linkData.targetPoint[0] + containerTransform.x, linkData.targetPoint[1] + containerTransform.y];
                    }
                }
            });
        }
    }

    /**
     * 선택한 대상을 삭제 한다.
     */
    function deleteElements() {
        let selectedNodes = d3.selectAll('.node.selected, .connector.selected').nodes();
        let histories = [];
        selectedNodes.forEach(function(node) {
            let history = deleteElement(d3.select(node));
            histories = [].concat(history, histories);
        });
        aliceProcessEditor.utils.history.saveHistory(histories);
    }

    /**
     * element 를 저장 데이터 및 화면에서 제거한다.
     *
     * @param elem 대상 element
     */
    function deleteElement(elem) {
        const histories = [];
        d3.select('g.alice-tooltip').remove();
        const elementId = elem.node().id,
              elements = aliceProcessEditor.data.elements;


        elements.forEach(function(e, i) {
            if (elementId === e.id) {
                let originElementData = JSON.parse(JSON.stringify(e));
                elements.splice(i, 1);
                histories.push({0: originElementData, 1: {}});
            }
        });

        let links = aliceProcessEditor.elements.links;
        if (!elem.classed('connector')) {
            // delete the connector connected to the target element.
            for (let i = elements.length - 1; i >= 0; i--) {
                if (elements[i].type === 'arrowConnector') {
                    if (elements[i].data['start-id'] === elementId || elements[i].data['end-id'] === elementId) {
                        for (let j = 0, len = links.length; j < len; j++) {
                            if (elements[i].id === links[j].id) {
                                links.splice(j, 1);
                                aliceProcessEditor.setConnectors(true);
                                break;
                            }
                        }
                        let originElementData = JSON.parse(JSON.stringify(elements[i]));
                        elements.splice(i, 1);
                        histories.push({0: originElementData, 1: {}});
                    }
                }
            }
            // delete node.
            d3.select(elem.node().parentNode).remove();
        } else {
            // delete connector.
            for (let i = 0, len = links.length; i < len; i++) {
                if (links[i].id === elementId) {
                    links.splice(i, 1);
                    aliceProcessEditor.setConnectors(true);
                    break;
                }
            }
        }
        aliceProcessEditor.setElementMenu();
        return histories;
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
        let node = aliceProcessEditor.addElement(elemData);
        if (node) {
            elemData.id = node.nodeElement.attr('id');
            aliceProcessEditor.data.elements.push(elemData);

            aliceProcessEditor.removeElementSelected();
            aliceProcessEditor.utils.history.saveHistory([{0: {}, 1: JSON.parse(JSON.stringify(elemData))}]);
            aliceProcessEditor.setElementMenu();
        }
    }

    /**
     * suggest element 를 connector 와 함께 추가 한다.
     *
     * @param elem source element
     * @param type 추가할 element 타입
     */
    function suggestElement(elem, type) {
        d3.select('g.alice-tooltip').remove();

        const targetBbox = aliceProcessEditor.utils.getBoundingBoxCenter(elem);
        let category = getElementCategory(type);
        let attributeData = getAttributeData(category, type);
        const elemData = { type: type };
        setElementData(elemData, attributeData);
        elemData.data = attributeData;
        let addDistance = 100,
            addElemWidth = 0,
            addElemHeight = 0;
        switch (type) {
            case 'userTask':
            case 'manualTask':
                addElemWidth = 160;
                addElemHeight = 40;
                break;
            case 'exclusiveGateway':
                let gatewaySize = Math.sqrt(
                    Math.pow(aliceProcessEditor.displayOptions.gatewaySize, 2) + Math.pow(aliceProcessEditor.displayOptions.gatewaySize, 2)
                );
                addElemWidth = gatewaySize;
                addElemHeight = gatewaySize;
                break;
            case 'commonEnd':
                addElemWidth = 40;
                addElemHeight = 40;
                break;
        }

        elemData.display = {
            'position-x': targetBbox.cx + (targetBbox.width / 2) + addDistance + (addElemWidth / 2),
            'position-y': targetBbox.cy
        };

        if (elem.classed('gateway')) {
            const distance = 10;
            let bottom = targetBbox.cy - distance - (addElemHeight / 2);
            aliceProcessEditor.elements.links.forEach(function(e) {
                if (e.sourceId === elem.node().id) {
                    const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(d3.select(document.getElementById(e.targetId)));
                    bottom = Math.max(bottom, (bbox.y + bbox.height));
                }
            });
            elemData.display['position-y'] = bottom + distance + (addElemHeight / 2);
        }

        elemData.required = getAttributeRequired(category, type);

        let node = aliceProcessEditor.addElement(elemData);
        if (node) {
            elemData.id = node.nodeElement.attr('id');
            const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(node.nodeElement);
            elemData.display.width = bbox.width;
            elemData.display.height = bbox.height;
            aliceProcessEditor.data.elements.push(elemData);

            aliceProcessEditor.removeElementSelected();

            const connectorElementId = workflowUtil.generateUUID();
            aliceProcessEditor.elements.links.push({id: connectorElementId, sourceId: elem.node().id, targetId: node.nodeElement.node().id, isDefault: 'N'});
            aliceProcessEditor.setConnectors();

            const connectorElementData = aliceProcessEditor.data.elements.filter(function(elem) { return elem.id === connectorElementId; })[0];
            aliceProcessEditor.utils.history.undo_list.pop(); // remove add connector history.
            aliceProcessEditor.utils.history.saveHistory([
                {0: {}, 1: JSON.parse(JSON.stringify(elemData))},
                {0: {}, 1: JSON.parse(JSON.stringify(connectorElementData))}
            ]);
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

        const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(actionTooltipContainer),
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
            const elements = aliceProcessEditor.data.elements;
            for (let i = 0, len = elementsKeys.length; i < len; i++) {
                if (elem.classed(elementsKeys[i])) {
                    let property = JSON.parse(JSON.stringify(elements.filter(function(attr) { return attr.id === elementId; })[0]));
                    let properties = elementsProperties[elementsKeys[i]];
                    let attributes = properties.filter(function(p) { return p.type === property.type; });
                    if (attributes.length > 0) {
                        if (typeof property.name !== 'undefined') {
                            property.data.name = property.name;
                        }
                        if (typeof property.notification !== 'undefined') {
                            property.data.notification = property.notification;
                        }
                        if (typeof property.description !== 'undefined') {
                            property.data.description = property.description;
                        }
                        makePropertiesItem(elementId, attributes[0], property.data);
                    }
                    break;
                }
            }
        } else { // show process properties
            if (!aliceProcessEditor.data.process) { return; }
            makePropertiesItem(aliceProcessEditor.data.process.id, processProperties, aliceProcessEditor.data.process);
        }
    }

    /**
     * 변경된 element display 속성 값을 저장한다.
     *
     * @param id element ID
     * @param isSaveHistory 이력저장여부
     * @return {{0: JSON, 1: JSON}} 이력정보
     */
    function changeDisplayValue(id, isSaveHistory) {
        let elementData = aliceProcessEditor.data.elements.filter(function(attr) { return attr.id === id; });
        if (elementData.length) {
            const originElementData = JSON.parse(JSON.stringify(elementData[0])),
                  nodeElement = d3.select(document.getElementById(id));
            if (nodeElement.classed('connector')) {
                const linkData = nodeElement.data()[0];
                elementData[0].data['start-id'] = linkData.sourceId;
                elementData[0].data['end-id'] = linkData.targetId;

                elementData[0].display = {};
                if (typeof linkData.midPoint !== 'undefined') {
                    elementData[0].display['mid-point'] = linkData.midPoint;
                } else {
                    delete elementData[0].display['mid-point'];
                }
                if (typeof linkData.sourcePoint !== 'undefined') {
                    elementData[0].display['source-point'] = linkData.sourcePoint;
                } else {
                    delete elementData[0].display['source-point'];
                }
                if (typeof linkData.targetPoint !== 'undefined') {
                    elementData[0].display['target-point'] = linkData.targetPoint;
                } else {
                    delete elementData[0].display['target-point'];
                }
                if (typeof linkData.textPoint !== 'undefined') {
                    elementData[0].display['text-point'] = linkData.textPoint;
                }
            } else {
                const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(nodeElement);
                elementData[0].display = {'width': bbox.width, 'height': bbox.height, 'position-x': bbox.cx, 'position-y': bbox.cy};
            }

            let historyData = {0: originElementData, 1: JSON.parse(JSON.stringify(elementData[0]))};
            if (isSaveHistory !== false) {
                aliceProcessEditor.utils.history.saveHistory([historyData]);
            }
            return historyData;
        }
    }

    /**
     * 변경된 element data 속성 값을 저장 한다.
     *
     * @param id process ID or element ID
     */
    function changePropertiesDataValue(id) {
        const container = document.querySelector('.alice-process-properties-panel .properties-container'),
              propertyObjects = container.querySelectorAll('input:not([type=radio]), select, textarea');
        if (id === aliceProcessEditor.data.process.id) {
            const originProcessData = JSON.parse(JSON.stringify(aliceProcessEditor.data.process));
            for (let i = 0, len = propertyObjects.length; i < len; i++) {
                let propertyObject = propertyObjects[i];
                aliceProcessEditor.data.process[propertyObject.name] = propertyObject.value;
            }
            aliceProcessEditor.utils.history.saveHistory([{0: originProcessData, 1: JSON.parse(JSON.stringify(aliceProcessEditor.data.process))}]);
        } else {
            let elementData = aliceProcessEditor.data.elements.filter(function(attr) { return attr.id === id; });
            if (elementData.length) {
                const originElementData = JSON.parse(JSON.stringify(elementData[0]));
                elementData[0].data = {};
                for (let i = 0, len = propertyObjects.length; i < len; i++) {
                    let propertyObject = propertyObjects[i];
                    if (!propertyObject.name) { continue; }
                    let propertyValue = propertyObject.value;
                    if (propertyObject.classList.contains('multiple')) {
                        propertyValue = propertyObject.value.split(',');
                    }
                    if (propertyObject.tagName.toUpperCase() === 'INPUT' && propertyObject.type.toUpperCase() === 'CHECKBOX') {
                        propertyValue = propertyObject.checked ? 'Y' : 'N';
                        if (propertyObject.id === 'is-default') {
                            let conditionAttrObject = container.querySelector('input[name=condition-value]');
                            if (conditionAttrObject && propertyObject.checked) {
                                conditionAttrObject.value = '';
                            }
                        }
                    }
                    let filerKeys = ['id', 'name', 'notification', 'description'].filter(function(key) {
                        return key === propertyObject.name;
                    });
                    if (filerKeys.length) {
                        elementData[0][propertyObject.name] = propertyValue;
                    } else {
                        elementData[0].data[propertyObject.name] = propertyValue;
                    }
                }

                const changeElementData = JSON.parse(JSON.stringify(elementData[0]));
                aliceProcessEditor.utils.history.saveHistory([{0: originElementData, 1: changeElementData}]);

                if (originElementData.name !== changeElementData.name) {
                    let connectors = aliceProcessEditor.data.elements.filter(function(attr) { return attr.type === 'arrowConnector'; });
                    for (let i = 0, len = connectors.length; i < len; i++) {
                        if (connectors[i].data['start-id'] === id) {
                            connectors[i].data['start-name'] = elementData[0].name;
                        }
                        if (connectors[i].data['end-id'] === id) {
                            connectors[i].data['end-name'] = elementData[0].name;
                        }
                    }
                }
            }
        }
    }

    /**
     * Assignee Type 에 따라 속성 항목을 변경한다.
     *
     * @param assigneeTypeObject Assignee Type object
     * @param value assignee value
     */
    function changePropertyAssigneeType(assigneeTypeObject, value) {
        let assigneeObject = document.getElementById('assignee');
        if (assigneeObject.parentNode.querySelector('select') !== null) {
            assigneeObject.parentNode.querySelector('select').remove();
            assigneeObject.parentNode.querySelector('button').remove();
            assigneeObject.parentNode.parentNode.querySelector('table').remove();
        }
        assigneeObject.value = '';

        if (assigneeTypeObject.value === 'assignee.type.assignee') {
            assigneeObject.style.display = 'inline-block';
            assigneeObject.classList.remove('multiple');
            if (typeof value !== 'undefined') {
                assigneeObject.value = value;
            }
        } else {
            let dataList = assigneeTypeData.users;
            let dataKeys = {value: 'userKey', text: 'userName'};
            if (assigneeTypeObject.value === 'assignee.type.candidate.groups') {
                dataList = assigneeTypeData.groups;
                dataKeys = {value: 'roleId', text: 'roleName'};
            }
            setMultipleDatatable(assigneeObject, dataList, dataKeys, value);
        }
    }

    /**
     * Script Type 에 따라 속성 항목을 변경한다.
     *
     * @param scriptTypeObject Script Type object
     */
    function changePropertyScriptType(scriptTypeObject) {
        let scriptDetailObject = document.getElementById('script-detail');
        if (scriptDetailObject.parentNode.parentNode.querySelector('.script-detail-container') !== null) {
            scriptDetailObject.parentNode.parentNode.querySelector('.script-detail-container').remove();
        }
        let scriptActionObject = document.getElementById('script-action');
        if (scriptActionObject.parentNode.querySelector('.script-action-container') !== null) {
            scriptActionObject.parentNode.querySelector('.script-action-container').remove();
        }
        scriptDetailObject.value = '';
        scriptActionObject.value = '';
        setMultipleScriptDetail(scriptDetailObject, scriptTypeObject.value);
        setMultipleScriptAction(scriptActionObject, scriptTypeObject.value);
    }

    /**
     *
     * @param inputObject 값을 넣는 input object
     * @param scriptType 선택된 Script Type
     * @param valueAttr 선택된 값이 있을 경우 그 값을 전달한다.
     */
    function setMultipleScriptDetail(inputObject, scriptType, valueAttr) {
        inputObject.style.display = 'none';
        inputObject.classList.add('multiple');
        if (scriptType === 'script.type.document.attachFile') {
            //script 하단에 붙는 내용
            let subContainer = document.createElement('div');
            subContainer.className = 'script-detail-container';

            let targetMappingProperties = document.createElement('div');
            targetMappingProperties.className = 'properties';

            let targetMappingLabel = document.createElement('label');
            targetMappingLabel.textContent = i18n.msg(i18nMsgPrefix + 'targetMappingId');
            targetMappingLabel.className = 'required';

            let targetMappingInput = document.createElement('input');
            targetMappingInput.id = 'target-mapping-id';
            targetMappingInput.name = 'target-mapping-id';
            targetMappingInput.required = true;

            targetMappingProperties.appendChild(targetMappingLabel);
            targetMappingProperties.appendChild(targetMappingInput);

            let sourceMappingProperties = document.createElement('div');
            sourceMappingProperties.className = 'properties';

            let sourceMappingLabel = document.createElement('label');
            sourceMappingLabel.textContent = i18n.msg(i18nMsgPrefix + 'sourceMappingId');
            sourceMappingLabel.className = 'required';

            let sourceMappingInput = document.createElement('input');
            sourceMappingInput.id = 'source-mapping-id';
            sourceMappingInput.name = 'source-mapping-id';
            sourceMappingInput.required = true;

            sourceMappingProperties.appendChild(sourceMappingLabel);
            sourceMappingProperties.appendChild(sourceMappingInput);

            let keyupHandler = function() {
                inputObject.value = targetMappingInput.value + '|' + sourceMappingInput.value;
                const evt = document.createEvent('HTMLEvents');
                evt.initEvent('change', false, true);
                inputObject.dispatchEvent(evt);
            };

            targetMappingInput.addEventListener('keyup', keyupHandler);
            sourceMappingInput.addEventListener('keyup', keyupHandler);

            subContainer.appendChild(targetMappingProperties);
            subContainer.appendChild(sourceMappingProperties);

            inputObject.parentNode.parentNode.querySelector('#script-type').parentNode.after(subContainer);

            if (typeof valueAttr !== 'undefined' && valueAttr !== '') {
                if (valueAttr[0] !== '') {
                    let data = valueAttr[0].split('\|');
                    targetMappingInput.value = data[0];
                    sourceMappingInput.value = data[1];
                }
            }
        }
    }

    /**
     * Script Type의 하위 속성을 생성한다.
     *
     * @param inputObject 값을 넣는 input object(선택된 데이터가 'condition|fileName'의 콤마 구분으로 등록된다.)
     * @param scriptType 선택된 Script Type
     * @param valueAttr 선택된 값이 있을 경우 그 값을 전달한다.
     */
    function setMultipleScriptAction(inputObject, scriptType, valueAttr) {
        inputObject.style.display = 'none';
        inputObject.classList.add('multiple');

        if (scriptType === 'script.type.document.attachFile') {
            let actionContainer = document.createElement('div');
            actionContainer.className = 'script-action-container';

            // condition 생성
            let conditionLabel = document.createElement('label');
            conditionLabel.textContent = i18n.msg(i18nMsgPrefix + 'condition');
            actionContainer.appendChild(conditionLabel);

            let conditionInput = document.createElement('input');
            actionContainer.appendChild(conditionInput);

            // file 생성
            let fileLabel = document.createElement('label');
            fileLabel.textContent = i18n.msg(i18nMsgPrefix + 'file');
            actionContainer.appendChild(fileLabel);

            let fileInput = document.createElement('input');
            fileInput.id = 'script-file';
            fileInput.className = 'file';
            fileInput.readOnly = true;

            let fileBtnContainer = document.createElement('div');
            fileBtnContainer.className = 'file-tooltip';
            let fileBtn = document.createElement('span');
            fileBtn.className = 'file-tooltip-button';
            fileBtn.addEventListener('click', function() {
                window.open('/processes/attachFile/view?callback=' + fileInput.id, 'fileUploadPop', 'width=1200, height=700');
            });
            fileBtnContainer.appendChild(fileBtn);

            // button
            let btnContainer = document.createElement('div');
            btnContainer.className = 'btn-container right';
            let btnAdd = document.createElement('button');
            btnAdd.textContent = i18n.msg('common.btn.add');
            btnContainer.appendChild(btnAdd);

            const saveData = function() {
                let dataBody = inputObject.parentNode.querySelector('tbody');
                let rows = dataBody.querySelectorAll('tr');
                let scriptValue = '';
                let rowLength = rows.length;
                if (rowLength > 0) {
                    for (let i = 0; i < rowLength; i++) {
                        if (i !== 0) { scriptValue += ','; }
                        scriptValue += rows[i].querySelector('.condition-txt').textContent;
                        scriptValue += '|';
                        scriptValue += rows[i].querySelector('.file-txt').textContent;
                    }
                }
                inputObject.value = scriptValue;

                const evt = document.createEvent('HTMLEvents');
                evt.initEvent('change', false, true);
                inputObject.dispatchEvent(evt);
            };

            btnAdd.addEventListener('click', function() {
                if (conditionInput.value.trim() === '' || fileInput.value.trim() === '') {
                    return false;
                }
                let dataBody = inputObject.parentNode.parentNode.querySelector('tbody'),
                    rows = dataBody.querySelectorAll('tr');
                let isDuplicate = false,
                    rowLength = rows.length;
                if (rowLength > 0) {
                    for (let i = 0; i < rowLength; i++) {
                        if (conditionInput.value === rows[i].querySelector('.condition-txt').textContent
                            && fileInput.value === rows[i].querySelector('.file-txt').textContent) {
                            isDuplicate = true;
                            break;
                        }
                    }
                }
                if (!isDuplicate) {
                    addDataRow(conditionInput.value, fileInput.value);
                    conditionInput.value = '';
                    fileInput.value = '';
                } else {
                    aliceJs.alertWarning(i18n.msg('process.msg.duplicateData'));
                }
            });

            const addDataRow = function(conditionValue, fileValue) {
                let dataBody = inputObject.parentNode.parentNode.querySelector('tbody');
                let row = document.createElement('tr');
                let conditionColumn = document.createElement('td');
                conditionColumn.className = 'condition-txt';
                conditionColumn.textContent = conditionValue;

                let fileColumn = document.createElement('td');
                fileColumn.className = 'file-txt';
                fileColumn.textContent = fileValue;

                let btnColumn = document.createElement('td');
                let btnDel = document.createElement('span');
                btnDel.className = 'remove';
                btnDel.addEventListener('click', function() {
                    this.parentNode.parentNode.remove();
                    saveData();
                });
                btnColumn.appendChild(btnDel);

                row.appendChild(conditionColumn);
                row.appendChild(fileColumn);
                row.appendChild(btnColumn);
                dataBody.appendChild(row);

                saveData();
            }

            let selectDev = document.createElement('div');
            selectDev.appendChild(fileInput);
            selectDev.appendChild(fileBtnContainer);
            selectDev.appendChild(btnContainer);
            selectDev.classList.add('flex-row');
            actionContainer.appendChild(selectDev);
            actionContainer.classList.add('flex-column');

            // table
            let dataTable = document.createElement('table');
            dataTable.className = 'script-data';
            let thead = document.createElement('thead');
            let headRow = document.createElement('tr');
            let headValueColumn = document.createElement('th');
            let headReturnColumn = document.createElement('th');
            let delColumn = document.createElement('th');
            headValueColumn.textContent = i18n.msg(i18nMsgPrefix + 'condition');
            headReturnColumn.textContent = i18n.msg(i18nMsgPrefix + 'file');
            headRow.appendChild(headValueColumn);
            headRow.appendChild(headReturnColumn);
            headRow.appendChild(delColumn);
            thead.appendChild(headRow);
            dataTable.appendChild(thead);
            let tbody = document.createElement('tbody');
            dataTable.appendChild(tbody);
            actionContainer.appendChild(dataTable);

            inputObject.parentNode.appendChild(actionContainer);

            if (typeof valueAttr !== 'undefined' && valueAttr !== '') {
                for (let i = 0, len = valueAttr.length; i < len; i++) {
                    if (valueAttr[i] !== '') {
                        let rowData = valueAttr[i].split('\|');
                        addDataRow(rowData[0], rowData[1]);
                    }
                }
            }
        }
    }

    /**
     * 다중 선택 속성 테이블을 생성한다.
     *
     * @param inputObject 값을 넣는 input object(선택된 데이터가 콤마 구분으로 등록된다.)
     * @param dataList 선택 목록
     * @param dataKeys dropdown 의 value/text 키 값. 예시: { value: 'id', text: 'name' }
     * @param valueArr 선택된 값이 있을 경우 그 값을 전달한다.
     */
    function setMultipleDatatable(inputObject, dataList, dataKeys, valueArr) {
        inputObject.style.display = 'none';
        inputObject.classList.add('multiple');
        let dataSelect = document.createElement('select');
        dataSelect.className = 'candidate';
        for (let i = 0, optionLength = dataList.length; i < optionLength; i++) {
            let option = document.createElement('option');
            let optionData = dataList[i];
            option.value = optionData[dataKeys.value];
            option.text = optionData[dataKeys.text];
            dataSelect.appendChild(option);
        }

        let btnAdd = document.createElement('button');
        btnAdd.innerText = i18n.msg('common.btn.add');

        const saveData = function() {
            let dataBody = inputObject.parentNode.parentNode.querySelector('tbody');
            let rows = dataBody.querySelectorAll('tr');
            let assigneeValue = '';
            let rowLength = rows.length;
            if (rowLength > 0) {
                for (let i = 0; i < rowLength; i++) {
                    if (i !== 0) { assigneeValue += ','; }
                    assigneeValue += rows[i].querySelector('input').value;
                }
            }
            inputObject.value = assigneeValue;

            const evt = document.createEvent('HTMLEvents');
            evt.initEvent('change', false, true);
            inputObject.dispatchEvent(evt);
        };

        const addDataRow = function(dataVal, dataText) {
            let dataBody = inputObject.parentNode.parentNode.querySelector('tbody');
            let row = document.createElement('tr');
            let nameColumn = document.createElement('td');
            nameColumn.textContent = dataText;
            let hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.value = dataVal;
            nameColumn.appendChild(hiddenInput);
            row.appendChild(nameColumn);
            let btnColumn = document.createElement('td');
            let btnDel = document.createElement('span');
            btnDel.className = 'remove';
            btnDel.addEventListener('click', function() {
                this.parentNode.parentNode.remove();
                saveData();
            });
            btnColumn.appendChild(btnDel);
            row.appendChild(btnColumn);
            dataBody.appendChild(row);

            saveData();
        };

        btnAdd.addEventListener('click', function() {
            let dataSelect = this.parentNode.querySelector('select'),
                dataBody = inputObject.parentNode.parentNode.querySelector('tbody'),
                rows = dataBody.querySelectorAll('tr');

            let isDuplicate = false,
                selectedValue = dataSelect.value,
                rowLength = rows.length;
            if (rowLength > 0) {
                for (let i = 0; i < rowLength; i++) {
                    if (selectedValue === rows[i].querySelector('input').value) {
                        isDuplicate = true;
                        break;
                    }
                }
            }
            if (!isDuplicate) {
                addDataRow(dataSelect.value, dataSelect.options[dataSelect.selectedIndex].text);
            }
        });

        let propertiesDiv = inputObject.parentNode;
        propertiesDiv.classList.add('flex-column');
        let selectRow = document.createElement('div');
        selectRow.classList.add('flex-row');
        selectRow.appendChild(inputObject);
        selectRow.appendChild(dataSelect);
        selectRow.appendChild(btnAdd);
        propertiesDiv.appendChild(selectRow);

        let dataTable = document.createElement('table');
        let thead = document.createElement('thead');
        let headRow = document.createElement('tr');
        let headNameColumn = document.createElement('th');
        headNameColumn.textContent = i18n.msg('common.label.name');
        headNameColumn.colSpan = 2;
        headRow.appendChild(headNameColumn);
        thead.appendChild(headRow);
        dataTable.appendChild(thead);
        let tbody = document.createElement('tbody');
        dataTable.appendChild(tbody);
        propertiesDiv.appendChild(dataTable);

        if (typeof valueArr !== 'undefined') {
            for (let i = 0, len = valueArr.length; i < len; i++) {
                for (let j = 0, dataLen = dataList.length; j < dataLen; j++) {
                    if (valueArr[i] === dataList[j][dataKeys.value]) {
                        addDataRow(valueArr[i], dataList[j][dataKeys.text]);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 속성 항목을 생성한다.
     *
     * @param id process/component ID
     * @param properties 속성정보
     * @param elemData 속성데이터
     */
    function makePropertiesItem(id, properties, elemData) {
        const propertiesContainer = document.querySelector('.alice-process-properties-panel .properties-container');
        const elementContainer = propertiesContainer.querySelector('.element-properties');
        elementContainer.innerHTML = '';
        const propertiesDivision = properties.attribute;
        let propertiesPanelTitle = i18n.msg(i18nMsgPrefix + 'process');
        if (id !== aliceProcessEditor.data.process.id) {
            propertiesPanelTitle = i18n.msg(i18nMsgPrefix + properties.type);
        }
        propertiesContainer.querySelector('.element-title > h2').textContent = propertiesPanelTitle;

        for (let idx = 0, len = propertiesDivision.length; idx < len; idx++) {
            // property 구분 타이틀
            let title = document.createElement('h3');
            let span = document.createElement('span');
            span.textContent = i18n.msg(i18nMsgPrefix + propertiesDivision[idx].title);
            title.appendChild(span);
            elementContainer.appendChild(title);

            const items = propertiesDivision[idx].items;
            for (let i = 0, attrLen = items.length; i < attrLen; i++) {
                const property = items[i];
                let propertyContainer = document.createElement('div');
                propertyContainer.className = 'properties';
                if (typeof property.fieldset !== 'undefined') {
                    // property fieldset
                    let fieldsetContainer = elementContainer.querySelector('fieldset[name="' + i18n.msg(i18nMsgPrefix + property.fieldset) + '"]');
                    if (fieldsetContainer === null) {
                        fieldsetContainer = addFieldset(property, elementContainer);
                    }
                    fieldsetContainer.appendChild(propertyContainer);
                } else {
                    elementContainer.appendChild(propertyContainer);
                }

                // property title
                let labelObject = document.createElement('label');
                if (property.type === 'checkbox') {
                    labelObject.className = 'checkbox';
                }
                labelObject.htmlFor = property.id;
                labelObject.textContent = i18n.msg(i18nMsgPrefix + property.id);
                if (property.display === 'none') {
                    labelObject.style.display = 'none';
                }
                if (property.required === 'Y') {
                    // property required
                    labelObject.classList.add('required');
                }
                propertyContainer.appendChild(labelObject);

                // property object (input, select, textarea ..)
                let elementObject = addPropertyObject(id, property, properties, elemData, propertyContainer);
                if (elementObject) {
                    // id, name, value 등 기본 값 설정
                    elementObject.id = property.id;
                    elementObject.name = property.id;
                    if (elemData[property.id] && property.type !== 'checkbox') {
                        if (property.type === 'rgb') {
                            if (aliceJs.isHexCode(elemData[property.id])) {
                                elementObject.value = aliceJs.hexToRgba(elemData[property.id], 0.5)
                            } else {
                                elementObject.value = aliceJs.rgbaToHex(elemData[property.id]);
                            }
                            let opacityValue = aliceJs.rgbaOpacity(elemData[property.id]) * 100;
                            let opacityLayer = elementObject.parentElement.nextElementSibling.querySelector('.color-palette-opacity');
                            if (opacityLayer !== null) {
                                opacityValue = opacityValue.toFixed(0); // 실제 값은 정상이나 소수점이 나오는 현상이 있어 소수점 처리 추가
                                opacityLayer.querySelector('.slide-object').value = opacityValue;
                                opacityLayer.querySelector('.slide-input').value = opacityValue;
                            }
                            elementObject.value = elemData[property.id];
                        } else {
                            elementObject.value = elemData[property.id];
                        }
                    } else if (property.id === 'id') {
                        elementObject.value = id;
                    }

                    // change 이벤트 설정
                    let changeEventHandler = function() {
                        changePropertiesDataValue(id);
                        if (property.id === 'is-default') {
                            let conditionValueObject = elementContainer.querySelector('input[name=condition-value]');
                            conditionValueObject.disabled = this.checked;
                            connectorIsDefaultChangeHandler(this, id);
                        }
                        if (property.id === 'condition-item') {
                            connectorConditionChangeHandler(this, id);
                        }
                    };
                    if (property.id !== 'id' && !(id === aliceProcessEditor.data.process.id && property.id === 'name')) {
                        elementObject.addEventListener('change', changeEventHandler);
                    }

                    // 그 외 이벤트 설정
                    switch (property.id) {
                        case 'name':
                            let keyupHandler = function() {
                                aliceProcessEditor.changeTextToElement(id, this.value);
                            };
                            if (id === aliceProcessEditor.data.process.id) {
                                keyupHandler = changeEventHandler;
                            }
                            elementObject.addEventListener('keyup', keyupHandler);
                            break;
                        case 'reject-id':
                            const addRejectClass = function(e) {
                                e.stopPropagation();
                                const elementData = aliceProcessEditor.data.elements.filter(function(elem) { return elem.id === e.target.value; });
                                if (elementData.length) {
                                    d3.select(document.getElementById(elementData[0].id)).classed('reject-element', true);
                                } else {
                                    d3.selectAll('.node').classed('reject-element', false);
                                }
                            };
                            elementObject.addEventListener('keyup', addRejectClass);
                            elementObject.addEventListener('focus', addRejectClass);
                            elementObject.addEventListener('focusout', function() {
                                d3.selectAll('.node').classed('reject-element', false);
                            });
                            break;
                        case 'target-document-list':
                            setMultipleDatatable(elementObject, documents, {value: 'documentId', text: 'documentName'}, elemData[property.id]);
                            break;
                        case 'script-detail' :
                            setMultipleScriptDetail(elementObject, elemData['script-type'], elemData[property.id]);
                            break;
                        case 'script-action' :
                            setMultipleScriptAction(elementObject, elemData['script-type'], elemData[property.id]);
                            break;
                    }
                }
            }
        }
        if (id !== aliceProcessEditor.data.process.id) {
            addSpecialProperties(id, elemData);
        } else {
            const temp = document.getElementById('process-info');
            let clone = temp.content.cloneNode(true);
            elementContainer.appendChild(clone);
            setProcessInformation();
        }
    }

    /**
     * assignee-type, fieldset 등의 특별 케이스의 property 처리.
     *
     * @param id element ID
     * @param elemData element data
     */
    function addSpecialProperties(id, elemData) {
        const propertiesContainer = document.querySelector('.alice-process-properties-panel .properties-container');
        const elementContainer = propertiesContainer.querySelector('.element-properties');

        const selectedElement = d3.select(document.getElementById(id));

        if (selectedElement.classed('userTask')) {
            let assigneeTypeObject = propertiesContainer.querySelector('#assignee-type');
            if (assigneeTypeObject) {
                changePropertyAssigneeType(assigneeTypeObject, elemData.assignee);
            }
        }

        if (selectedElement.classed('connector')) { // 현재는 arrowConnector만 적용
            let actionFieldset = elementContainer.querySelector('fieldset[name=action]');
            let conditionFieldset = elementContainer.querySelector('fieldset[name=condition]');
            if (actionFieldset && conditionFieldset) {
                let element = aliceProcessEditor.data.elements.filter(function(e) { return e.id === id; })[0];
                let sourceElement = aliceProcessEditor.data.elements.filter(function(e) { return e.id === element.data['start-id']; })[0];

                let enableFieldset = actionFieldset;
                let disabledFieldset = conditionFieldset;
                if (sourceElement.type.indexOf('Gateway') > -1 && sourceElement.data['condition-item'].startsWith('$')) {
                    enableFieldset = conditionFieldset;
                    disabledFieldset = actionFieldset;
                }
                disabledFieldset.querySelector('input[type=radio]').checked = false;
                disabledFieldset.disabled = true;
                disabledFieldset.querySelectorAll('input:not([type=radio])').forEach(function(inputObject) {
                    if (inputObject.tagName.toUpperCase() === 'INPUT' && inputObject.type.toUpperCase() === 'CHECKBOX') {
                        inputObject.checked = false;
                        inputObject.value = 'N';
                    } else {
                        inputObject.value = '';
                    }
                    const evt = document.createEvent('HTMLEvents');
                    evt.initEvent('change', false, true);
                    inputObject.dispatchEvent(evt);
                });

                enableFieldset.querySelector('input[type=radio]').checked = true;
                enableFieldset.disabled = false;

                let isDefaultObject = enableFieldset.querySelector('input[name=is-default]');
                if (isDefaultObject) {
                    let conditionValueObject = enableFieldset.querySelector('input[name=condition-value]');
                    conditionValueObject.disabled = isDefaultObject.checked;
                }
            }
        }
    }

    /**
     * properties panel에 fieldset을 추가한다.
     *
     * @param property
     * @param elementContainer
     * @return {HTMLFieldSetElement}
     */
    function addFieldset(property, elementContainer) {
        const fieldsetContainer = document.createElement('fieldset');
        fieldsetContainer.name = property.fieldset;
        let legend = document.createElement('legend');
        let selectRadio = document.createElement('input');
        selectRadio.type = 'radio';
        selectRadio.disabled = true;
        selectRadio.name = 'fieldset_' + id;
        selectRadio.value = property.fieldset;
        legend.appendChild(selectRadio);
        let legendLabel = document.createElement('label');
        legendLabel.textContent = property.fieldset;
        legend.appendChild(legendLabel);
        fieldsetContainer.appendChild(legend);
        elementContainer.appendChild(fieldsetContainer);
        return fieldsetContainer;
    }

    /**
     * property object(input, textarea, select 등) 생성 후 해당 object 를 반환한다.
     *
     * @param id
     * @param property
     * @param properties
     * @param elemData
     * @param propertyContainer
     * @return {HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement}
     */
    function addPropertyObject(id, property, properties, elemData, propertyContainer) {
        let elementObject;
        switch (property.type) {
            case 'inputbox':
                elementObject = document.createElement('input');
                propertyContainer.appendChild(elementObject);
                break;
            case 'inputbox-readonly':
                elementObject = document.createElement('input');
                elementObject.readOnly = true;
                propertyContainer.appendChild(elementObject);
                break;
            case 'inputbox-copy':
                elementObject = document.createElement('input');
                elementObject.className = 'copy';
                elementObject.readOnly = true;
                propertyContainer.appendChild(elementObject);

                let copyBtnContainer = document.createElement('div');
                copyBtnContainer.className = 'clipboard-tooltip';
                let copyBtn = document.createElement('span');
                copyBtn.className = 'clipboard-tooltip-button';
                copyBtn.addEventListener('click', function() {
                    elementObject.select();
                    elementObject.setSelectionRange(0, 99999);
                    document.execCommand('copy');

                    let tooltip = document.getElementById('clipboard-tooltip-text');
                    tooltip.textContent = 'Copy Success';
                });
                copyBtn.addEventListener('mouseout', function() {
                    let tooltip = document.getElementById('clipboard-tooltip-text');
                    tooltip.textContent = 'Copy to clipboard';
                });
                let tooltip = document.createElement('span');
                tooltip.id = 'clipboard-tooltip-text';
                tooltip.className = 'clipboard-tooltip-text';
                tooltip.textContent = 'Copy to clipboard';
                copyBtn.appendChild(tooltip);
                copyBtnContainer.appendChild(copyBtn);

                propertyContainer.appendChild(copyBtnContainer);
                break;
            case 'textarea':
                elementObject = document.createElement('textarea');
                elementObject.style.resize = 'none';
                propertyContainer.appendChild(elementObject);
                break;
            case 'checkbox':
                elementObject = document.createElement('input');
                elementObject.type = 'checkbox';
                if (elemData[property.id] && elemData[property.id] === 'Y') {
                    elementObject.checked = true;
                }
                propertyContainer.insertBefore(elementObject,propertyContainer.childNodes[propertyContainer.childNodes.length-1]);
                break;
            case 'select':
                elementObject = document.createElement('select');
                let optionList = JSON.parse(JSON.stringify(property['sub-list']));
                if (property.id === 'sub-document-id') {
                    documents.forEach(function(d) {
                        optionList.push({id: d.documentId, name: d.documentName});
                    });
                }
                for (let j = 0, optionLength = optionList.length; j < optionLength; j++) {
                    let option = document.createElement('option');
                    option.value = optionList[j].id;
                    option.text = optionList[j].name;
                    elementObject.appendChild(option);
                }
                if (property.id === 'assignee-type') {
                    elementObject.addEventListener('change', function() {
                        changePropertyAssigneeType(this);
                    });
                }
                if (property.id === 'script-type') {
                    elementObject.addEventListener('change', function() {
                        changePropertyScriptType(this);
                    });
                }
                propertyContainer.appendChild(elementObject);
                break;
            case 'rgb':
                let colorPicker = document.createElement('div');
                colorPicker.className = 'color-picker';
                let colorInput = document.createElement('div');
                colorInput.className = 'color-input';
                let selectedColorBox = document.createElement('span');
                let selectedColor = document.createElement('span');
                selectedColorBox.className = 'selected-color-box'
                selectedColor.className = 'selected-color';
                if (property.id === 'line-color') {
                    selectedColor.style.backgroundColor = '';
                    selectedColor.style.borderColor = elemData[property.id];
                }
                if (property.id === 'background-color') {
                    selectedColor.style.backgroundColor = elemData[property.id];
                    selectedColor.style.border = 'transparent';
                }
                selectedColorBox.appendChild(selectedColor);
                colorInput.appendChild(selectedColorBox);
                colorPicker.appendChild(colorInput);
                propertyContainer.appendChild(colorPicker);

                elementObject = document.createElement('input');
                elementObject.className = 'color';
                elementObject.readOnly = true;
                elementObject.addEventListener('change', function() {
                    let opacity = 0;
                    if (this.dataset['opacity'] !== '') {
                        opacity = Number(this.dataset['opacity']) / 100;
                    }
                    if (!aliceJs.isHexCode(this.value)) {
                        this.value = aliceJs.rgbaToHex(this.value); // opacity 값 갱신하기 위해 Hex로 변환
                    }
                    this.value = aliceJs.hexToRgba(this.value, opacity);
                    if (properties.type === 'groupArtifact') {
                        const groupElement = d3.select(document.getElementById(id));
                        const selectedElement = elementObject.parentNode.querySelector('span.selected-color');
                        if (this.id === 'line-color') {
                            selectedElement.style.borderColor = this.value;
                            selectedElement.style.backgroundColor = 'transparent';
                            groupElement.style('stroke', this.value);
                        }
                        if (this.id === 'background-color') {
                            selectedElement.style.backgroundColor = this.value;
                            selectedElement.style.border = 'transparent';
                            if (this.value.trim() === '') {
                                groupElement.style('fill-opacity', 0);
                            } else {
                                groupElement.style('fill', this.value).style('fill-opacity', opacity);
                            }
                        }
                    }
                });
                colorInput.appendChild(elementObject);

                let colorPaletteLayer = document.createElement('div');
                colorPaletteLayer.className = 'color-palette-layer';
                let colorPaletteBox = document.createElement('div');
                colorPaletteBox.id = property.id + '-colorPalette';
                colorPaletteBox.className = 'color-palette';
                colorPaletteLayer.appendChild(colorPaletteBox);
                colorPicker.appendChild(colorPaletteLayer)

                let option = {
                    isOpacity: true,
                    data: {
                        isSelected: true,
                        selectedClass: 'selected',
                        value: elemData[property.id]
                    }
                }
                colorPalette.initColorPalette(colorPaletteLayer, selectedColor, elementObject, option);
                break;
            default:
                break;
        }
        return elementObject;
    }

    /**
     * arrowConnector is-default 관련 이벤트.
     * 해당 옵션 체크 시 동일 sourceId를 가진 connector의 is-default 속성을 false로 만든다.
     *
     * @param target is-default object(checkbox)
     * @param id component ID
     */
    function connectorIsDefaultChangeHandler(target, id) {
        d3.select(document.getElementById(id)).classed('is-default', target.checked);
        let sourceId;
        aliceProcessEditor.elements.links.forEach(function(l) {
            if (l.id === id) {
                l.isDefault = target.checked ? 'Y' : 'N';
                sourceId = l.sourceId;
            }
        });
        aliceProcessEditor.elements.links.forEach(function(l) {
            if (l.sourceId === sourceId && l.id !== id && l.isDefault === 'Y') {
                d3.select(document.getElementById(l.id)).classed('is-default', false);
                l.isDefault = 'N';
                aliceProcessEditor.data.elements.forEach(function(e) {
                    if (e.id === l.id) {
                        e.data['is-default'] = 'N';
                    }
                });
            }
        });
    }

    /**
     * 게이트웨이 condition item change에 따른 connector condition 값 초기화.
     *
     * @param target condition-item object(input)
     * @param id element ID
     */
    function connectorConditionChangeHandler(target, id) {
        let arrowConnectors = aliceProcessEditor.data.elements.filter(function(e) {
            return e.type === 'arrowConnector' && e.data['start-id'] === id;
        });
        for (let i = 0, len = arrowConnectors.length; i < len; i++) {
            let connectorData = arrowConnectors[i].data;
            if (target.value.startsWith('$')) {
                connectorData['action-name'] = '';
                connectorData['action-value'] = '';
            } else {
                if (connectorData['is-default'] === 'Y') {
                    connectorData['is-default'] = 'N';
                    let targetConnector = aliceProcessEditor.elements.links.filter(function(l) {
                        return l.id === arrowConnectors[i].id;
                    })[0];
                    d3.select(document.getElementById(targetConnector.id)).classed('is-default', false);
                    targetConnector.isDefault = 'N';
                }
                connectorData['condition-value'] = '';
            }
        }
    }

    function setProcessInformation() {
        const elements = aliceProcessEditor.data.elements;
        let categories = [];
        elements.forEach(function(elem) {
            categories.push(aliceProcessEditor.getElementCategory(elem.type));
        });

        let uniqList =  categories.reduce(function(a, b) {
            if (a.indexOf(b) < 0 ) { a.push(b); }
            return a;
        },[]);
        const countList = [];
        uniqList.forEach(function(item) {
            let count = 0;
            categories.forEach(function(category) {
                if (item === category) {
                    count++;
                }
            });
            countList.push({category: item, count: count});
        });
        let infoContainer = document.querySelector('.alice-process-properties-panel .info');
        infoContainer.querySelectorAll('label').forEach(function(label) {
            label.textContent = '0';
        });
        countList.forEach(function(countInfo) {
            infoContainer.querySelector('#' + countInfo.category + '_count').textContent = countInfo.count;
        });
        infoContainer.querySelector('#element_count').textContent = elements.length;
    }

    /**
     *
     * 선택된 element 의 속성 및 tooltip 메뉴를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setElementMenu(elem) {
        setActionTooltipItem(elem);
        setProperties(elem);
    }

    /**
     * tooltip item 에 사용된 이미지 및 속성 데이터 로딩.
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
                url: '/rest/processes/' + processId + '/data',
                contentType: 'application/json; charset=utf-8',
                callbackFunc: function(xhr) {
                    aliceProcessEditor.data = JSON.parse(xhr.responseText);
                    const elements = aliceProcessEditor.data.elements;
                    elements.forEach(function(element) {
                        const category = getElementCategory(element.type);
                        element.required = getAttributeRequired(category, element.type);
                    });
                    setElementMenu();
                    aliceProcessEditor.drawProcess(processId, elements);
                }
            });
        };

        /**
         * load element attribute data.
         */
        const loadElementData = function() {
            aliceJs.sendXhr({
                method: 'GET',
                url: '/assets/js/process/elementAttribute.json',
                contentType: 'application/json; charset=utf-8',
                callbackFunc: function(xhr) {
                    elementsProperties = JSON.parse(xhr.responseText);
                    elementsKeys = Object.getOwnPropertyNames(elementsProperties);
                    loadProcessData();
                }
            });
        };
        // load process attribute data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/assets/js/process/processAttribute.json',
            contentType: 'application/json; charset=utf-8',
            callbackFunc: function(xhr) {
                processProperties = JSON.parse(xhr.responseText);
                loadElementData();
            }
        });

        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/users',
            contentType: 'application/json; charset=utf-8',
            callbackFunc: function(xhr) {
                assigneeTypeData.users = JSON.parse(xhr.responseText);
            }
        });

        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/roles',
            contentType: 'application/json; charset=utf-8',
            callbackFunc: function(xhr) {
                assigneeTypeData.groups = JSON.parse(xhr.responseText);
            }
        });

        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/documents?searchDocumentStatus=document.status.use',
            contentType: 'application/json; charset=utf-8',
            callbackFunc: function(xhr) {
                documents = JSON.parse(xhr.responseText);
            }
        });

        // add pattern image. for tooltip item image.
        const imageLoadingList = [];
        tooltipItems.forEach(function(item) {
            if (item.url) {
                let data = {};
                data.id = item.parent + '-' + item.type;
                data.url = item.url;
                imageLoadingList.push(data);
            }
            if (item.element_url) {
                let elemData = {};
                elemData.id = item.parent + '-' + item.type + '-element';
                elemData.url = item.element_url;
                imageLoadingList.push(elemData);

                elemData = {};
                elemData.id = item.parent + '-' + item.type + '-element-selected';
                elemData.url = item.element_selected_url;
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
    exports.resetElementPosition = resetElementPosition;
    exports.deleteElements = deleteElements;
    Object.defineProperty(exports, '__esModule', {value: true});
})));
