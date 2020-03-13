(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceProcessEditor = global.AliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    const displayOptions = {
        translateLimit: 1000, // drawing board limit.
        gridInterval: 10      // value of grid interval.
    };

    let svg,
        elementsContainer,
        connectors,
        dragLine;

    const nodes= [],
        links = [];

    let mousedownElement,
        mouseoverElement,
        selectedElement;

    let isDrawConnector = false;

    /**
     * reset mouse variables.
     */
    function resetMouseVars() {
        mousedownElement = null;
        mouseoverElement = null;
    }

    /**
     * 선택된 element 를 해제한다.
     */
    function removeElementSelected() {
        selectedElement = null;
        svg.selectAll('.node').classed('selected', false);
        svg.selectAll('.pointer').style('opacity', 0);
        svg.selectAll('.connector').classed('selected', false);
        svg.selectAll('.alice-tooltip').remove();
    }

    /**
     * set connector.
     */
    function setConnectors() {
        connectors = connectors.data(links);
        // remove old links
        connectors.exit().remove();

        let enter = connectors.enter().append('g').attr('class', 'connector');

        // add new links
        enter.append('path')
            .attr('class', 'connector')
            .attr('id', function(d) { return d.id; })
            .style('marker-end', 'url(#end-arrow)')
            .on('mousedown', function() {
                d3.event.stopPropagation();
                if (isDrawConnector) {
                    return;
                }

                removeElementSelected();
                resetMouseVars();

                // select link
                let selectedLink = d3.select(this).classed('selected', true);
                selectedElement = null;

                setConnectors();
                AliceProcessEditor.setElementMenu(selectedLink);
            })
            .call(function(d) {
                AliceProcessEditor.addElementProperty(d);
            });

        // add new paintedPath links
        enter.append('path')
            .attr('class', 'painted-connector')
            .on('mouseout', function() {
                d3.select(this).style('cursor', 'default');
            })
            .on('mouseover', function() {
                if (isDrawConnector) {
                    return;
                }
                d3.select(this).style('cursor', 'pointer');
            })
            .on('mousedown', function(d) {
                d3.event.stopPropagation();
                if (isDrawConnector) {
                    return;
                }
                const event = document.createEvent('MouseEvent');
                event.initMouseEvent('mousedown', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                d3.select(document.getElementById(d.id)).node().dispatchEvent(event);
            });

        enter.append('text').append('textPath')
            .attr('xlink:href', function(d) { return '#' + d.id; })
            .attr('startOffset', '10%')
            .append('tspan')
            .attr('dy', '-10')
            .text(function(d) {
                let name = '';
                const elements = AliceProcessEditor.data.elements;
                elements.forEach(function(elem) {
                    if (elem.id === d.id) { name = elem.data.name; }
                });
                return name;
            });

        connectors = connectors.merge(enter);

        // draw links
        drawConnectors();
    }

    /**
     * draw connector.
     */
    function drawConnectors() {
        const getLinePath = function(d) {
            let target = d.target,
                source = d.source;
            if (target.classed('gateway')) {
                target = d3.select(d.target.node().parentNode);
            }
            if (source.classed('gateway')) {
                source = d3.select(d.source.node().parentNode);
            }
            const targetBBox = AliceProcessEditor.utils.getBoundingBoxCenter(target);
            const sourceBBox = AliceProcessEditor.utils.getBoundingBoxCenter(source);

            let min = Number.MAX_SAFE_INTEGER || 9007199254740991;
            let best = {};
            let sourcePointArray = [[sourceBBox.width / 2, 0], [sourceBBox.width, sourceBBox.height / 2],
                [sourceBBox.width / 2, sourceBBox.height], [0, sourceBBox.height / 2]];
            let targetPointArray = [[targetBBox.width / 2, 0], [targetBBox.width, targetBBox.height / 2],
                [targetBBox.width / 2, targetBBox.height], [0, targetBBox.height / 2]];

            sourcePointArray.forEach(function(s) {
                targetPointArray.forEach(function (d) {
                    let dist = Math.hypot(
                        (targetBBox.x + d[0]) - (sourceBBox.x + s[0]),
                        (targetBBox.y + d[1]) - (sourceBBox.y + s[1])
                    );
                    if (dist < min) {
                        min = dist;
                        best = {
                            s: {x: sourceBBox.x + s[0], y: sourceBBox.y + s[1]},
                            d: {x: targetBBox.x + d[0], y: targetBBox.y + d[1]}
                        };
                    }
                });
            });

            let lineFunction = d3.line().x(function(d) {return  d.x;}).y(function(d){return d.y;}).curve(d3.curveLinear);
            return lineFunction([best.s, best.d]);

            /*
            // 추후 참고 수정 예정.
            const lineGenerator = d3.line();
            //lineGenerator.curve(d3.curveStep);
            //lineGenerator.curve(d3.curveStepAfter);
            //lineGenerator.curve(d3.curveStepBefore);
            lineGenerator.curve(d3.curveLinear);
            return lineGenerator([[sourceBBox.cx, sourceBBox.cy], [targetBBox.cx, targetBBox.cy]]);
            */
        };

        connectors.select('path.connector').attr('d', function(d) {return getLinePath(d);});
        connectors.select('path.painted-connector').attr('d', function(d) {return getLinePath(d);});
    }

    /**
     * connector 를 추가한다.
     *
     * @param source source element
     * @param target target element
     */
    function connectElement(source, target) {
        links.push({id: workflowUtil.generateUUID(), source: source, target: target});
        setConnectors();
    }


    /**
     * element 마우스 이벤트.
     *
     * @type {{mouseover: mouseover, mouseout: mouseout, mouseup: mouseup, mousedown: mousedown}}
     */
    const elementMouseEventHandler = {
        mouseover: function() {
            const elemContainer = d3.select(this.parentNode);
            const elem = elemContainer.select('.node');
            mouseoverElement = null;
            let cursor = 'pointer';
            if (isDrawConnector) {
                cursor = 'crosshair';
            }
            elemContainer.style('cursor', cursor);
            if (!isDrawConnector || !mousedownElement || elem === mousedownElement) {
                return;
            }
            mouseoverElement = elem;
            let availableLink = checkAvailableLink();
            elem.classed('selected', availableLink);
        },
        mouseout: function() {
            const elemContainer = d3.select(this.parentNode);
            const elem = elemContainer.select('.node');
            if (!isDrawConnector || !mousedownElement || elem === mousedownElement) {
                elemContainer.style('cursor', 'default');
                return;
            }
            mouseoverElement = null;
            elem.classed('selected', false);
        },
        mousedown: function () {
            const elemContainer = d3.select(this.parentNode);
            const elem = elemContainer.select('.node');
            if (isDrawConnector) {
                resetMouseVars();
                removeElementSelected();
                mousedownElement = elem;
                selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;

                const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(mousedownElement),
                    gTransform = d3.zoomTransform(d3.select('g.element-container').node()),
                    centerX = bbox.cx + gTransform.x,
                    centerY = bbox.cy + gTransform.y;
                dragLine
                    .style('marker-end', 'url(#end-arrow)')
                    .classed('hidden', false)
                    .attr('d', 'M' + centerX + ',' + centerY + 'L' + centerX + ',' + centerY);
            } else {
                if (selectedElement === elem) {
                    return;
                }
                removeElementSelected();
                mousedownElement = elem;
                selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;
                selectedElement.classed('selected', true);
                if (elem.node().getAttribute('class').match(/\bresizable\b/)) {
                    const selectedElementId = selectedElement.node().id;
                    for (let i = 1; i <= 4; i++) {
                        // svg.select('#' + selectedElementId + '_point' + i).style('opacity', 1); <- querySelector 로 첫번째 글자로 숫자가 오면 오류남.
                        document.getElementById(selectedElementId + '_point' + i).style.opacity = '1';
                    }
                }
                elemContainer.style('cursor', 'move');
                AliceProcessEditor.setElementMenu(elem);
            }
        },
        mouseup: function() {
            const elemContainer = d3.select(this.parentNode);
            const elem = elemContainer.select('.node');
            if (isDrawConnector) {
                elemContainer.style('cursor', 'default');
                dragLine
                    .classed('hidden', true)
                    .style('marker-end', '');
                if (!mousedownElement || !mouseoverElement) {
                    resetMouseVars();
                    return;
                }

                if (mousedownElement !== mouseoverElement) {
                    mouseoverElement
                        .classed('selected', false);

                    if (checkAvailableLink()) {
                        links.push({id: workflowUtil.generateUUID(), source: mousedownElement, target: mouseoverElement});
                        selectedElement = null;
                        setConnectors();
                    }
                }
                resetMouseVars();
            } else {
                elemContainer.style('cursor', 'pointer');
                if (svg.select('.alice-tooltip').node() === null) {
                    AliceProcessEditor.setActionTooltipItem(elem);
                }
            }
        },
        mousedrag: function() {
            const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(mousedownElement),
                gTransform = d3.zoomTransform(d3.select('g.element-container').node()),
                centerX = bbox.cx + gTransform.x,
                centerY = bbox.cy + gTransform.y;
            dragLine.attr('d', 'M' + centerX + ',' + centerY + 'L' + (d3.event.x + gTransform.x) + ',' + (d3.event.y + gTransform.y));
        }
    };

    /**
     * connector 연결 가능여부 체크하여 리턴한다.
     *
     * @return {boolean} 연결 가능 여부
     */
    function checkAvailableLink() {
        let availableLink = true;
        const source = mousedownElement,
            target = mouseoverElement;
        links.forEach(function(l) {
            // it's not a gateway, but several starts
            if (!l.source.classed('gateway') && l.source.node().id === source.node().id) {
                availableLink = false;
            }
            // cannot link to each other
            if ((l.source.node().id === source.node().id && l.target.node().id === target.node().id) ||
                (l.source.node().id === target.node().id && l.target.node().id === source.node().id)) {
                availableLink = false;
            }
        });
        return availableLink;
    }

    /**
     * 리사이즈 가능한 사각 element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @param isShowType 타입표시여부
     * @param width element width
     * @param height element height
     * @constructor
     */
    function RectResizableElement(x, y, isShowType, width, height) {
        const self = this;
        self.width = width ? width : 120;
        self.height = height ? height : 70;
        self.radius = 10;
        const calcX = x - (self.width / 2),
            calcY = y - (self.height / 2),
            typeImageSize = 20;

        const drag = d3.drag()
            .on('start', elementMouseEventHandler.mousedown)
            .on('drag', function() {
                if (isDrawConnector) {
                    elementMouseEventHandler.mousedrag();
                } else {
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    const rectData = self.rectData;
                    for (let i = 0, len = rectData.length; i < len; i++) {
                        self.nodeElement
                            .attr('x', rectData[i].x += d3.event.dx)
                            .attr('y', rectData[i].y += d3.event.dy);
                    }
                    self.nodeElement.style('cursor', 'move');
                    updateRect();
                }
            })
            .on('end', elementMouseEventHandler.mouseup);
        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        self.rectData = [{ x: calcX, y: calcY }, { x: calcX + self.width, y: calcY + self.height }];
        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', self.width)
            .attr('height', self.height)
            .attr('x', self.rectData[0].x)
            .attr('y', self.rectData[0].y)
            .attr('rx', self.radius)
            .attr('ry', self.radius)
            .attr('class', 'node resizable')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        if (isShowType) {
            self.typeElement = elementContainer.append('rect')
                .attr('class', 'element-type')
                .attr('width', typeImageSize)
                .attr('height', typeImageSize)
                .on('mouseover', elementMouseEventHandler.mouseover)
                .on('mouseout', elementMouseEventHandler.mouseout)
                .call(drag);
        }

        self.textElement = elementContainer.append('text')
            .attr('x', x)
            .attr('y', y)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        ['nw-resize', 'se-resize', 'ne-resize', 'sw-resize'].forEach(function(cursor, i) {
            self['pointElement' + (i + 1)] = elementContainer.append('circle')
                .attr('class', 'pointer')
                .style('opacity', 0)
                .on('mouseover', function() { self['pointElement' + (i + 1)].style('cursor', cursor); })
                .on('mouseout', function() { self['pointElement' + (i + 1)].style('cursor', 'default'); })
                .call(d3.drag()
                    .on('start', function() {
                        svg.selectAll('.alice-tooltip').remove();
                    })
                    .on('drag', function() {
                        if (selectedElement && selectedElement.node().id === self.nodeElement.node().id) {
                            const minWidth = 80, minHeight = 60;
                            switch (i + 1) {
                                case 1:
                                    if (self.rectData[1].x - (self.rectData[0].x + d3.event.dx) >= minWidth) {
                                        self.pointElement1.attr('cx', self.rectData[0].x += d3.event.dx);
                                    }
                                    if (self.rectData[1].y - (self.rectData[0].y + d3.event.dy) >= minHeight) {
                                        self.pointElement1.attr('cy', self.rectData[0].y += d3.event.dy);
                                    }
                                    break;
                                case 2:
                                    if ((self.rectData[1].x + d3.event.dx) - self.rectData[0].x >= minWidth) {
                                        self.pointElement2.attr('cx', self.rectData[1].x += d3.event.dx);
                                    }
                                    if ((self.rectData[1].y + d3.event.dy) - self.rectData[0].y >= minHeight) {
                                        self.pointElement2.attr('cy', self.rectData[1].y += d3.event.dy);
                                    }
                                    break;
                                case 3:
                                    if ((self.rectData[1].x + d3.event.dx) - self.rectData[0].x >= minWidth) {
                                        self.pointElement3.attr('cx', self.rectData[1].x += d3.event.dx);
                                    }
                                    if (self.rectData[1].y - (self.rectData[0].y + d3.event.dy) >= minHeight) {
                                        self.pointElement3.attr('cy', self.rectData[0].y += d3.event.dy);
                                    }
                                    break;
                                case 4:
                                    if (self.rectData[1].x - (self.rectData[0].x + d3.event.dx) >= minWidth) {
                                        self.pointElement4.attr('cx', self.rectData[0].x += d3.event.dx);
                                    }
                                    if ((self.rectData[1].y + d3.event.dy) - self.rectData[0].y >= minHeight) {
                                        self.pointElement4.attr('cy', self.rectData[1].y += d3.event.dy);
                                    }
                                    break;
                            }
                            changeTextToElement(self.nodeElement.node().id);
                            updateRect();
                        }
                    })
                    .on('end', function() {
                        AliceProcessEditor.setElementMenu(self.nodeElement);
                    })
                );
        });

        /**
         * element 위치, 크기 등 update.
         */
        function updateRect() {
            const pointerRadius = 4;
            const rectData = self.rectData;

            let updateX = (rectData[1].x - rectData[0].x > 0 ? rectData[0].x : rectData[1].x),
                updateY = (rectData[1].y - rectData[0].y > 0 ? rectData[0].y : rectData[1].y),
                updateWidth = Math.abs(rectData[1].x - rectData[0].x),
                updateHeight = Math.abs(rectData[1].y - rectData[0].y);
            self.nodeElement
                .attr('x', updateX)
                .attr('y', updateY)
                .attr('width', updateWidth)
                .attr('height', updateHeight);
            AliceProcessEditor.changeDisplayValue(self.nodeElement.node().id);

            if (isShowType && self.nodeElement.classed('task')) {
                self.typeElement
                    .attr('x', updateX + (typeImageSize / 2))
                    .attr('y', updateY + (typeImageSize / 2));
            } else if (isShowType && self.nodeElement.classed('subprocess')) {
                self.typeElement
                    .attr('x', updateX + (updateWidth / 2) - (typeImageSize / 2))
                    .attr('y', updateY + updateHeight - typeImageSize - 3);
            }
            self.textElement.attr('x', updateX + (updateWidth / 2));
            if (self.nodeElement.classed('group')) {
                self.textElement.attr('y', updateY + 10);
            } else {
                self.textElement.attr('y', updateY + (updateHeight / 2));
            }

            let pointArray =
                [[rectData[0].x, rectData[0].y], [rectData[1].x, rectData[1].y],
                    [rectData[1].x, rectData[0].y], [rectData[0].x, rectData[1].y]];
            pointArray.forEach(function(point, i) {
                self['pointElement' + (i + 1)]
                    .data(rectData)
                    .attr('id', self.nodeElement.node().id + '_point' + (i + 1))
                    .attr('r', pointerRadius)
                    .attr('cx', point[0])
                    .attr('cy', point[1]);
            });
            drawConnectors();
        }
        updateRect();
    }

    /**
     * Task element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @param width element width
     * @param height element height
     * @returns {TaskElement}
     * @constructor
     */
    function TaskElement(x, y, width, height) {
        this.base = RectResizableElement;
        this.base(x, y, true, width, height);
        const defaultType = AliceProcessEditor.getElementDefaultType('task');
        this.nodeElement
            .classed('task', true)
            .classed(defaultType, true);
        this.typeElement
            .attr('x', this.rectData[0].x + 10)
            .attr('y', this.rectData[0].y + 10)
            .style('fill', 'url(#task-' + defaultType + '-element)');
        return this;
    }

    /**
     * Subprocess element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @param width element width
     * @param height element height
     * @returns {SubprocessElement}
     * @constructor
     */
    function SubprocessElement(x, y, width, height) {
        this.base = RectResizableElement;
        this.base(x, y, true, width, height);
        this.nodeElement.classed('subprocess', true);
        const defaultType = AliceProcessEditor.getElementDefaultType('subprocess');
        this.typeElement
            .attr('x', (this.rectData[0].x + (this.width / 2) - 10))
            .attr('y', (this.rectData[0].y + this.height - 20 - 3))
            .style('fill', 'url(#subprocess-' + defaultType + '-element)');
        return this;
    }

    /**
     * event element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {EventElement}
     * @constructor
     */
    function EventElement(x, y) {
        const self = this;
        const radius = 25, typeImageSize = 20;

        const drag = d3.drag()
            .on('start', elementMouseEventHandler.mousedown)
            .on('drag', function() {
                if (isDrawConnector) {
                    elementMouseEventHandler.mousedrag();
                } else {
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    self.nodeElement
                        .attr('cx', d3.event.x)
                        .attr('cy', d3.event.y);
                    self.typeElement
                        .attr('x', d3.event.x - (typeImageSize / 2))
                        .attr('y', d3.event.y - (typeImageSize / 2));
                    self.nodeElement.style('cursor', 'move');
                    AliceProcessEditor.changeDisplayValue(self.nodeElement.node().id);
                    drawConnectors();
                }
            })
            .on('end', elementMouseEventHandler.mouseup);

        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        const defaultType = AliceProcessEditor.getElementDefaultType('event');

        self.nodeElement = elementContainer.append('circle')
            .attr('id', workflowUtil.generateUUID())
            .attr('r', radius)
            .attr('cx', x)
            .attr('cy', y)
            .attr('class', 'node event ' + defaultType)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);
        self.typeElement = elementContainer.append('rect')
            .attr('class', 'element-type')
            .attr('width', typeImageSize)
            .attr('height', typeImageSize)
            .attr('x', x - (typeImageSize / 2))
            .attr('y', y - (typeImageSize / 2))
            .style('fill', 'url(#event-' + defaultType + '-element)')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        return self;
    }

    /**
     * gateway element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {GatewayElement}
     * @constructor
     */
    function GatewayElement(x, y) {
        const self = this;
        const width = 45, height = 45, typeImageSize = 20;

        const drag = d3.drag()
            .on('start', elementMouseEventHandler.mousedown)
            .on('drag', function() {
                if (isDrawConnector) {
                    elementMouseEventHandler.mousedrag();
                } else {
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    self.nodeElement
                        .attr('x', d3.event.x - (width / 2))
                        .attr('y', d3.event.y - (height / 2))
                        .attr('transform', 'rotate(45, ' + d3.event.x + ', ' + d3.event.y + ')');
                    self.typeElement
                        .attr('x', d3.event.x - (typeImageSize / 2))
                        .attr('y', d3.event.y - (typeImageSize / 2));
                    self.nodeElement.style('cursor', 'move');
                    AliceProcessEditor.changeDisplayValue(self.nodeElement.node().id);
                    drawConnectors();
                }
            })
            .on('end', elementMouseEventHandler.mouseup);

        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        const defaultType = AliceProcessEditor.getElementDefaultType('gateway');

        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('transform', 'rotate(45, ' + x + ', ' + y + ')')
            .attr('class', 'node gateway ' + defaultType)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);
        self.typeElement = elementContainer.append('rect')
            .attr('class', 'element-type')
            .attr('width', typeImageSize)
            .attr('height', typeImageSize)
            .attr('x', x - (typeImageSize / 2))
            .attr('y', y - (typeImageSize / 2))
            .style('fill', 'url(#gateway-' + defaultType + '-element)')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        return self;
    }

    /**
     * Group element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @param width element width
     * @param height element height
     * @returns {GroupElement}
     * @constructor
     */
    function GroupElement(x, y, width, height) {
        this.base = RectResizableElement;
        this.base(x, y, false, width, height);
        this.nodeElement
            .classed('artifact', true)
            .classed('group', true);
        this.textElement.attr('y', this.rectData[0].y + 10);
        return this;
    }

    /**
     * Annotation element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {AnnotationElement}
     * @constructor
     */
    function AnnotationElement(x, y) {
        const self = this;
        const width = 30, height = 30;

        const drag = d3.drag()
            .on('start', elementMouseEventHandler.mousedown)
            .on('drag', function() {
                if (isDrawConnector) {
                    elementMouseEventHandler.mousedrag();
                } else {
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    self.nodeElement
                        .attr('x', d3.event.x - (width / 2))
                        .attr('y', d3.event.y - (height / 2));
                    self.textElement
                        .attr('x', d3.event.x)
                        .attr('y', d3.event.y);
                    self.nodeElement.style('cursor', 'move');
                    AliceProcessEditor.changeDisplayValue(self.nodeElement.node().id);
                }
            })
            .on('end', elementMouseEventHandler.mouseup);

        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('class', 'node artifact annotation')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        self.textElement = elementContainer.append('text')
            .attr('x', x).attr('y', y)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .style('text-anchor', 'start')
            .call(drag);

        return self;
    }

    /**
     * element 에 이벤트를 추가한다.
     */
    function addElementsEvent() {
        d3.selectAll('.alice-process-element-palette, .alice-process-drawing-board')
            .on('dragover', function() {d3.event.preventDefault();});

        d3.select('.alice-process-element-palette').select('.connector')
            .on('click', function() {
                isDrawConnector = !d3.select(this).classed('selected');
                d3.select(this).classed('selected', isDrawConnector);
                // clear
                removeElementSelected();
                resetMouseVars();
                AliceProcessEditor.setElementMenu();
            });

        d3.select('.alice-process-element-palette').selectAll('span.shape')
            .attr('draggable', 'true')
            .on('dragend', function() {
                const svgOffset = svg.node().getBoundingClientRect(),
                    gTransform = d3.zoomTransform(d3.select('g.element-container').node());
                let x = d3.event.pageX - svgOffset.left - window.pageXOffset - gTransform.x,
                    y = d3.event.pageY - svgOffset.top - window.pageYOffset - gTransform.y;
                let _this = d3.select(this);
                let node;
                let type = '';
                if (_this.classed('event')) {
                    node = new EventElement(x, y);
                    type = AliceProcessEditor.getElementDefaultType('event');
                } else if (_this.classed('task')) {
                    node = new TaskElement(x, y);
                    type = AliceProcessEditor.getElementDefaultType('task');
                } else if (_this.classed('subprocess')) {
                    node = new SubprocessElement(x, y);
                    type = AliceProcessEditor.getElementDefaultType('subprocess');
                } else if (_this.classed('gateway')) {
                    node = new GatewayElement(x, y);
                    type = AliceProcessEditor.getElementDefaultType('gateway');
                } else if (_this.classed('group')) {
                    node = new GroupElement(x, y);
                    type = 'groupArtifact';
                } else if (_this.classed('annotation')) {
                    node = new AnnotationElement(x, y);
                    type = 'annotationArtifact';
                }
                if (node) {
                    _this.classed(type, true);
                    nodes.push(node.nodeElement);
                    AliceProcessEditor.addElementProperty(node.nodeElement);
                }
            });
    }

    /**
     * element 에 Text 를 추가한다.
     *
     * @param elemId element ID
     * @param text 추가할 text
     */
    function changeTextToElement(elemId, text) {
        const elementNode = document.getElementById(elemId);
        if (typeof text === 'undefined') {
            const elements = AliceProcessEditor.data.elements;
            elements.forEach(function(elem) {
                if (elem.id === elemId) { text = elem.data.name; }
            });
        }

        if (d3.select(elementNode).classed('connector')) {
            d3.select(elementNode.parentNode).select('tspan').text(text);
        } else {
            const textElement = d3.select(elementNode.parentNode).select('text')
            if (textElement.node()) {
                textElement.text(text);

                // wrap text
                const element = d3.select(elementNode);
                if (text.length > 0 && !element.classed('annotation')) {
                    let textLength = textElement.node().getComputedTextLength(),
                        displayText = textElement.text();
                    const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(element);
                    while (textLength > bbox.width && displayText.length > 0) {
                        displayText = displayText.slice(0, -1);
                        textElement.text(displayText + '...');
                        textLength = textElement.node().getComputedTextLength();
                    }
                }
            }
        }
    }

    /**
     * svg 추가 및 필요한 element 추가.
     */
    function initProcessEdit() {
        const width = 1120,
            height = 879;

        // add svg and svg event
        svg = d3.select('.alice-process-drawing-board').append('svg')
            .attr('width', width)
            .attr('height', height)
            .on('mousedown', function() {
                d3.event.stopPropagation();
                if (isDrawConnector) { return; }
                removeElementSelected();
                AliceProcessEditor.setElementMenu();
            })
            .on('mouseup', function() {
                d3.event.stopPropagation();
                if (isDrawConnector && mousedownElement) {
                    dragLine
                        .classed('hidden', true)
                        .style('marker-end', '');
                    resetMouseVars();
                }
            });

        // add grid line
        let verticalScale = d3.scaleLinear();
        let horizontalScale = d3.scaleLinear();
        let verticalAxis = d3.axisBottom().tickFormat('');
        let horizontalAxis = d3.axisRight().tickFormat('');
        let verticalGrid = svg.append('g').attr('class', 'grid vertical-grid');
        let horizontalGrid = svg.append('g').attr('class', 'grid horizontal-grid');

        const setDrawingBoardGrid = function() {
            const drawingBoard = document.querySelector('.alice-process-drawing-board'),
                drawingBoardWidth = drawingBoard.offsetWidth,
                drawingBoardHeight = drawingBoard.offsetHeight;

            svg.attr('width', drawingBoardWidth).attr('height', drawingBoardHeight);

            verticalScale.domain([0, drawingBoardWidth / displayOptions.gridInterval]).range([0, drawingBoardWidth]);
            horizontalScale.domain([0, drawingBoardHeight / displayOptions.gridInterval]).range([0, drawingBoardHeight]);
            verticalAxis
                .scale(verticalScale)
                .ticks(drawingBoardWidth / displayOptions.gridInterval)
                .tickSize(drawingBoardHeight);
            horizontalAxis
                .scale(horizontalScale)
                .ticks(drawingBoardHeight / displayOptions.gridInterval)
                .tickSize(drawingBoardWidth);

            svg.selectAll('g.grid').selectAll('*').remove();
            verticalGrid.call(verticalAxis);
            horizontalGrid.call(horizontalAxis);
        }
        window.onresize = setDrawingBoardGrid;
        setDrawingBoardGrid();

        // add zoom
        const zoom = d3.zoom()
            .on('start', function() {
                svg.style('cursor', 'grabbing');
                const nodeTopArray = [],
                    nodeRightArray = [],
                    nodeBottomArray = [],
                    nodeLeftArray = [];
                nodes.forEach(function(node){
                    let nodeBBox = AliceProcessEditor.utils.getBoundingBoxCenter(node);
                    nodeTopArray.push(nodeBBox.cy - (nodeBBox.height / 2));
                    nodeRightArray.push(nodeBBox.cx + (nodeBBox.width / 2));
                    nodeBottomArray.push(nodeBBox.cy + (nodeBBox.height / 2));
                    nodeLeftArray.push(nodeBBox.cx - (nodeBBox.width / 2));
                });
                const svgBBox = AliceProcessEditor.utils.getBoundingBoxCenter(svg);
                let minLeft = svgBBox.cx,
                    minTop = svgBBox.cy,
                    maxRight = svgBBox.cx,
                    maxBottom = svgBBox.cy;
                if (nodes.length > 0) {
                    minLeft = d3.min(nodeLeftArray);
                    minTop = d3.min(nodeTopArray);
                    maxRight = d3.max(nodeRightArray);
                    maxBottom = d3.max(nodeBottomArray);
                }
                zoom.translateExtent([
                    [minLeft - displayOptions.translateLimit, minTop - displayOptions.translateLimit],
                    [maxRight + displayOptions.translateLimit, maxBottom + displayOptions.translateLimit]
                ]);
            })
            .on('zoom', function() {
                horizontalGrid
                    .call(horizontalAxis.scale(d3.event.transform.rescaleY(horizontalScale)));
                verticalGrid
                    .call(verticalAxis.scale(d3.event.transform.rescaleX(verticalScale)));
                svg.select('g.element-container')
                    .attr('transform', d3.event.transform);
                svg.select('g.connector-container')
                    .attr('transform', d3.event.transform);
            })
            .on('end', function() {
                svg.style('cursor', 'default');
            });

        svg
            .call(zoom)
            .on('wheel.zoom', null)
            .on('dblclick.zoom', null);

        // define arrow markers for links
        svg.append('defs').append('marker')
            .attr('id', 'end-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 6)
            .attr('markerWidth', 5)
            .attr('markerHeight', 8)
            .attr('orient', 'auto')
            .append('path')
            .attr('d', 'M0,-5L10,0L0,5');

        // line displayed when dragging new nodes
        dragLine = svg.append('path')
            .attr('class', 'connector drag-line hidden')
            .attr('d', 'M0,0L0,0');

        const connectorContainer = svg.append('g').attr('class', 'connector-container');
        connectors = connectorContainer.selectAll('g.connector');
        elementsContainer = svg.append('g').attr('class', 'element-container');
    }

    /**
     * drawing board 에 element 를 추가하고, element 생성자를 리턴한다.
     *
     * @param element element 정보
     * @return {EventElement|TaskElement|SubprocessElement|GatewayElement|GroupElement|AnnotationElement}
     */
    function addElement(element) {
        let node;
        const x = element.display['position-x'],
            y = element.display['position-y'];

        let category = AliceProcessEditor.getElementCategory(element.type);
        switch (category) {
            case 'event':
                node = new EventElement(x, y);
                AliceProcessEditor.changeElementType(node.nodeElement, element.type);
                break;
            case 'task':
                node = new TaskElement(x, y, element.display.width, element.display.height);
                AliceProcessEditor.changeElementType(node.nodeElement, element.type);
                break;
            case 'subprocess':
                node = new SubprocessElement(x, y, element.display.width, element.display.height);
                break;
            case 'gateway':
                node = new GatewayElement(x, y);
                AliceProcessEditor.changeElementType(node.nodeElement, element.type);
                break;
            case 'artifact':
                if (element.type === 'groupArtifact') {
                    node = new GroupElement(x, y, element.display.width, element.display.height);
                } else if (element.type === 'annotationArtifact') {
                    node = new AnnotationElement(x, y);
                }
                break;
            default:
                break;
        }

        if (node) {
            nodes.push(node.nodeElement);
            const nodeId = node.nodeElement.attr('id');
            if (category !== 'event' && category !== 'gateway') {
                changeTextToElement(nodeId, element.data.name);
            }
        }

        return node;
    }

    /**
     * Draw a element with the loaded information.
     *
     * @param elements editor 에 추가할 element 목록
     */
    function drawProcess(elements) {
        // add element
        elements.forEach(function(element) {
            if (element.type === 'arrowConnector') {
                return;
            }
            let node = addElement(element);
            if (node) {
                const nodeId = node.nodeElement.attr('id');
                elements.forEach(function(e) {
                    if (e.type !== 'arrowConnector') {
                        return;
                    }
                    if (e.data['start-id'] === element.id) {
                        e.data['start-id'] = nodeId;
                    } else if (e.data['end-id'] === element.id) {
                        e.data['end-id'] = nodeId;
                    }
                });
                element.id = nodeId;
            }
        });

        // add connector
        elements.forEach(function(element) {
            if (element.type !== 'arrowConnector') {
                return;
            }
            const source = document.getElementById(element.data['start-id']),
                  target = document.getElementById(element.data['end-id']);
            const nodeId = workflowUtil.generateUUID();
            element.id = nodeId;
            if (source && target) {
                element['start-id'] = source.id;
                element['end-id'] = target.id;
                links.push({id:  workflowUtil.generateUUID(), source: d3.select(source), target: d3.select(target)});
            }
        });
        setConnectors();
    }

    /**
     * process designer 초기화.
     *
     * @param processId 프로세스 ID
     */
    function init(processId) {
        console.info('process editor initialization. [PROCESS ID: ' + processId + ']');

        workflowUtil.polyfill();
        initProcessEdit();
        addElementsEvent();
        AliceProcessEditor.loadItems(processId);
        AliceProcessEditor.initUtil();
    }

    exports.init = init;
    exports.drawProcess = drawProcess;
    exports.addElement = addElement;
    exports.changeTextToElement = changeTextToElement;
    exports.removeElementSelected = removeElementSelected;
    exports.connectElement = connectElement;
    Object.defineProperty(exports, '__esModule', {value: true});
})));