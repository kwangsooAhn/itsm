(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    let data = {};

    let svg,
        path,
        paintedPath,
        dragLine;
    const links = [];

    let mousedownElement,
        mouseoverElement,
        selectedElement;

    let isDrawConnector = false;
    const durationTime = 100;

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
        svg.selectAll('.node').style('stroke-width', 1).transition().duration(durationTime);
        svg.selectAll('.pointer').style('opacity', 0);
        svg.selectAll('.tooltip').remove();
        svg.selectAll('.connector').classed('selected', false);
    }

    /**
     * set connector.
     */
    function setConnectors() {
        path = path.data(links);
        paintedPath = paintedPath.data(links);

        // remove old links
        path.exit().remove();
        paintedPath.exit().remove();

        // add new links
        path = path.enter().append('path')
            .attr('class', 'link connector')
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
                wfEditor.setElementMenu(selectedLink);
            })
            .merge(path);

        // add new paintedPath links
        paintedPath = paintedPath.enter().append('path')
            .style('stroke', 'none')
            .style('stroke-width', 20)
            .attr('pointer-events', 'all')
            .on('mouseover', function() {
                if (isDrawConnector) {
                    return;
                }
                d3.select(this).style('cursor', 'pointer')
            })
            .on('mousedown', function(d, i) {
                d3.event.stopPropagation();
                if (isDrawConnector) {
                    return;
                }
                const event = document.createEvent('MouseEvent');
                event.initMouseEvent('mousedown', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                path._groups[0][i].dispatchEvent(event);
            })
            .merge(paintedPath);

        // draw links
        drawConnectors();
    }

    /**
     * draw connector.
     */
    function drawConnectors() {
        const getLinePath = function(d) {
            const targetBBox = wfEditor.utils.getBoundingBoxCenter(d.target);
            const sourceBBox = wfEditor.utils.getBoundingBoxCenter(d.source);

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
                })
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
        }

        path.attr('d', function(d) {return getLinePath(d);});
        paintedPath.attr('d', function(d) {return getLinePath(d);});
    }

    /**
     * element 마우스 이벤트.
     *
     * @type {{mouseover: mouseover, mouseout: mouseout, mouseup: mouseup, mousedown: mousedown}}
     */
    const elementMouseEventHandler = {
        mouseover: function() {
            const elem = d3.select(this);
            mouseoverElement = null;
            let cursor = 'pointer';
            if (isDrawConnector) {
                cursor = 'crosshair';
            }
            elem.style('cursor', cursor);
            if (!isDrawConnector || !mousedownElement || elem === mousedownElement) {
                return;
            }
            mouseoverElement = elem;
            elem.style('stroke', 'red')
                .style('stroke-width', 3)
                .transition()
                .duration(durationTime);
        },
        mouseout: function() {
            const elem = d3.select(this);
            if (!isDrawConnector || !mousedownElement || elem === mousedownElement) {
                elem.style('cursor', 'default');
                return;
            }
            mouseoverElement = null;
            elem.style('stroke', 'black')
                .style('stroke-width', 1)
                .transition()
                .duration(durationTime);
        },
        mousedown: function () {
            const elem = d3.select(this);
            if (isDrawConnector) {
                resetMouseVars();
                removeElementSelected();
                mousedownElement = elem;
                selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;

                const bbox = wfEditor.utils.getBoundingBoxCenter(mousedownElement);
                dragLine
                    .style('marker-end', 'url(#end-arrow)')
                    .classed('hidden', false)
                    .attr('d', 'M' + bbox.cx + ',' + bbox.cy + 'L' + bbox.cx + ',' + bbox.cy);
            } else {
                if (selectedElement === elem) {
                    return;
                }
                removeElementSelected();
                mousedownElement = elem;
                selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;
                selectedElement.style('stroke-width', 2).transition().duration(durationTime);
                if (elem.node().getAttribute('class').match(/\bresizable\b/)) {
                    const selectedElementId = selectedElement.node().id;
                    for (let i = 1; i <= 4; i++) {
                        svg.select('#' + selectedElementId + '_point' + i).style('opacity', 1);
                    }
                }
                elem.style('cursor', 'move');
                wfEditor.setElementMenu(elem);
            }
        },
        mouseup: function() {
            const elem = d3.select(this);
            if (isDrawConnector) {
                elem.style('cursor', 'default');
                dragLine
                    .classed('hidden', true)
                    .style('marker-end', '');
                if (!mousedownElement || !mouseoverElement) {
                    resetMouseVars();
                    return;
                }

                if (mousedownElement !== mouseoverElement) {
                    mouseoverElement
                        .style('stroke', 'black')
                        .style('stroke-width', 1)
                        .transition()
                        .duration(durationTime);

                    const source = mousedownElement;
                    const target = mouseoverElement;
                    const link = links.filter(function(l) {(l.source === source && l.target === target) || (l.source === target && l.target === source)})[0];
                    if (!link) {
                        links.push({source: source, target: target});
                        selectedElement = null;
                        setConnectors();
                    }
                }
                resetMouseVars();
            } else {
                elem.style('cursor', 'pointer');
                wfEditor.setActionTooltipItem(elem);
            }
        },
        mousedrag: function() {
            const bbox = wfEditor.utils.getBoundingBoxCenter(mousedownElement);
            dragLine.attr('d', 'M' + bbox.cx + ',' + bbox.cy + 'L' + d3.event.x + ',' + d3.event.y);
        }
    }

    /**
     * 리사이즈 가능한 사각 element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @constructor
     */
    function RectResizableElement(x, y) {
        const self = this;
        self.width = 80;
        self.height = 50;
        x = x - (self.width / 2);
        y = y - (self.height / 2);

        self.rectData = [{ x: x, y: y }, { x: x + self.width, y: y + self.height }];
        self.nodeElement = svg.append('rect')
            .attr('id', workflowUtil.generateUUID)
            .attr('width', self.width)
            .attr('height', self.height)
            .attr('x', self.rectData.x)
            .attr('y', self.rectData.y)
            .style('fill', 'yellow')
            .style('opacity', 1)
            .style('stroke', 'black')
            .style('stroke-width', 1)
            .attr('class', 'node resizable')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.tooltip').remove();
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
                .on('end', elementMouseEventHandler.mouseup)
            );

        self.pointElement1 = svg.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement1.style('cursor', 'nw-resize'); })
            .on('mouseout', function() { self.pointElement1.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.tooltip').remove();
                })
                .on('drag', function() {
                    if (selectedElement && selectedElement.node().id === self.nodeElement.node().id) {
                        self.pointElement1
                            .attr('cx', function(d) { return d.x += d3.event.dx; })
                            .attr('cy', function(d) { return d.y += d3.event.dy; });
                        updateRect();
                    }
                })
                .on('end', function() {
                    wfEditor.setElementMenu(self.nodeElement);
                })
            );
        self.pointElement2 = svg.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement2.style('cursor', 'se-resize'); })
            .on('mouseout', function() { self.pointElement2.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.tooltip').remove();
                })
                .on('drag', function() {
                    if (selectedElement && selectedElement.node().id === self.nodeElement.node().id) {
                        self.pointElement2
                            .attr('cx', self.rectData[1].x += d3.event.dx)
                            .attr('cy', self.rectData[1].y += d3.event.dy);
                        updateRect();
                    }
                })
                .on('end', function() {
                    wfEditor.setElementMenu(self.nodeElement);
                })
            );
        self.pointElement3 = svg.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement3.style('cursor', 'ne-resize'); })
            .on('mouseout', function() { self.pointElement3.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.tooltip').remove();
                })
                .on('drag', function() {
                    if (selectedElement && selectedElement.node().id === self.nodeElement.node().id) {
                        self.pointElement3
                            .attr('cx', self.rectData[1].x += d3.event.dx)
                            .attr('cy', self.rectData[0].y += d3.event.dy);
                        updateRect();
                    }
                })
                .on('end', function() {
                    wfEditor.setElementMenu(self.nodeElement);
                })
            );
        self.pointElement4 = svg.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement4.style('cursor', 'sw-resize'); })
            .on('mouseout', function() { self.pointElement4.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.tooltip').remove();
                })
                .on('drag', function() {
                    if (selectedElement && selectedElement.node().id === self.nodeElement.node().id) {
                        self.pointElement4
                            .attr('cx', self.rectData[0].x += d3.event.dx)
                            .attr('cy', self.rectData[1].y += d3.event.dy);
                        updateRect();
                    }
                })
                .on('end', function() {
                    wfEditor.setElementMenu(self.nodeElement);
                })
            );

        function updateRect() {
            const pointerRadius = 4;
            const rectData = self.rectData;

            self.nodeElement
                .attr('x', rectData[1].x - rectData[0].x > 0 ? rectData[0].x : rectData[1].x)
                .attr('y', rectData[1].y - rectData[0].y > 0 ? rectData[0].y :  rectData[1].y)
                .attr('width', Math.abs(rectData[1].x - rectData[0].x))
                .attr('height', Math.abs(rectData[1].y - rectData[0].y));

            self.pointElement1
                .data(rectData)
                .attr('id', self.nodeElement.node().id + '_point1')
                .attr('r', pointerRadius)
                .attr('cx', rectData[0].x)
                .attr('cy', rectData[0].y);
            self.pointElement2
                .data(rectData)
                .attr('id', self.nodeElement.node().id + '_point2')
                .attr('r', pointerRadius)
                .attr('cx', rectData[1].x)
                .attr('cy', rectData[1].y);
            self.pointElement3
                .data(rectData)
                .attr('id', self.nodeElement.node().id + '_point3')
                .attr('r', pointerRadius)
                .attr('cx', rectData[1].x)
                .attr('cy', rectData[0].y);
            self.pointElement4
                .data(rectData)
                .attr('id', self.nodeElement.node().id + '_point4')
                .attr('r', pointerRadius)
                .attr('cx', rectData[0].x)
                .attr('cy', rectData[1].y);

            drawConnectors();
        }
        updateRect();
    }

    /**
     * Task element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {TaskElement}
     * @constructor
     */
    function TaskElement(x, y) {
        this.base = RectResizableElement;
        this.base(x, y);
        this.nodeElement.classed('task', true);
        return this;
    }

    /**
     * Subprocess element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {SubprocessElement}
     * @constructor
     */
    function SubprocessElement(x, y) {
        this.base = RectResizableElement;
        this.base(x, y);
        this.nodeElement
            .classed('subprocess', true)
            .style('fill', 'pink');
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
        const radius = 20;

        self.nodeElement = svg.append('circle')
            .attr('id', workflowUtil.generateUUID)
            .attr('r', radius)
            .attr('cx', x)
            .attr('cy', y)
            .style('fill', 'red')
            .style('opacity', 1)
            .style('stroke', 'black')
            .style('stroke-width', 1)
            .attr('class', 'node event')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.tooltip').remove();
                        self.nodeElement
                            .attr('cx', d3.event.x)
                            .attr('cy', d3.event.y);
                        self.nodeElement.style('cursor', 'move');
                        drawConnectors();
                    }
                })
                .on('end', elementMouseEventHandler.mouseup)
            );

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
        const width = 30, height = 30;

        self.nodeElement = svg.append('rect')
            .attr('id', workflowUtil.generateUUID)
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('transform', 'rotate(45, ' + x + ', ' + y + ')')
            .style('fill', 'blue')
            .style('opacity', 1)
            .style('stroke', 'black')
            .style('stroke-width', 1)
            .attr('class', 'node gateway')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.tooltip').remove();
                        self.nodeElement
                            .attr('x', d3.event.x - (width / 2))
                            .attr('y', d3.event.y - (height / 2))
                            .attr('transform', 'rotate(45, ' + d3.event.x + ', ' + d3.event.y + ')');

                        self.nodeElement.style('cursor', 'move');
                        drawConnectors();
                    }
                })
                .on('end', elementMouseEventHandler.mouseup)
            );

        return self;
    }

    /**
     * Group element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {GroupElement}
     * @constructor
     */
    function GroupElement(x, y) {
        this.base = RectResizableElement;
        this.base(x, y);
        this.nodeElement
            .classed('group', true)
            .style('fill-opacity', '0')
            .style('stroke-dasharray', '5,5');
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
        const width = 35, height = 30;

        self.nodeElement = svg.append('rect')
            .attr('id', workflowUtil.generateUUID)
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .style('fill-opacity', 0)
            .style('stroke', 'black')
            .style('stroke-width', 1)
            .style('stroke-dasharray', '5,5,5,5,5,5,0,35,5,5,5,5,5,5,5,5')
            .attr('class', 'node annotation')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.tooltip').remove();
                        self.nodeElement
                            .attr('x', d3.event.x - (width / 2))
                            .attr('y', d3.event.y - (height / 2));

                        self.nodeElement.style('cursor', 'move');
                    }
                })
                .on('end', elementMouseEventHandler.mouseup)
            );

        return self;
    }

    /**
     * element에 이벤트를 추가한다.
     */
    function addElementsEvent() {
        d3.selectAll('.element-palette, .drawing-board').on('dragover', function() {d3.event.preventDefault();});

        d3.select('.element-palette').select('.connector')
            .on('click', function() {
                isDrawConnector = !d3.select(this).classed('selected');
                d3.select(this).classed('selected', isDrawConnector);
                // clear
                removeElementSelected();
                resetMouseVars();
                wfEditor.setElementMenu();
            });

        d3.select('.element-palette').selectAll('span.shape')
            .attr('draggable', 'true')
            .on('dragend', function() {
                const svgOffset = svg.node().getBoundingClientRect();
                let x = d3.event.pageX - svgOffset.left - window.pageXOffset,
                    y = d3.event.pageY - svgOffset.top - window.pageYOffset;
                let _this = d3.select(this);
                if (_this.classed('event')) {
                    new EventElement(x, y);
                } else if (_this.classed('task')) {
                    new TaskElement(x, y);
                } else if (_this.classed('subprocess')) {
                    new SubprocessElement(x, y);
                } else if (_this.classed('gateway')) {
                    new GatewayElement(x, y);
                } else if (_this.classed('group')) {
                    new GroupElement(x, y);
                } else if (_this.classed('annotation')) {
                    new AnnotationElement(x, y);
                }
            });
    }

    /**
     * svg 추가 및 필요한 element 추가.
     */
    function initWorkflowEdit() {
        const width = 1405;
        const height = 750;

        // add svg and svg event
        svg = d3.select('.drawing-board').append('svg')
            .attr('width', width)
            .attr('height', height)
            .on('mousedown', function() {
                d3.event.stopPropagation();
                if (isDrawConnector) {
                    return;
                }
                removeElementSelected();
                wfEditor.setElementMenu();
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

        // define arrow markers for links
        svg.append('defs').append('marker')
            .attr('id', 'end-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 6)
            .attr('markerWidth', 5)
            .attr('markerHeight', 8)
            .attr('orient', 'auto')
            .append('path')
            .attr('d', 'M0,-5L10,0L0,5')
            .attr('fill', '#000');

        svg.append('defs').append('marker')
            .attr('id', 'start-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 4)
            .attr('markerWidth', 5)
            .attr('markerHeight', 8)
            .attr('orient', 'auto')
            .append('path')
            .attr('d', 'M10,-5L0,0L10,5')
            .attr('fill', '#000');

        // line displayed when dragging new nodes
        dragLine = svg.append('path')
            .attr('class', 'link drag-line hidden')
            .attr('d', 'M0,0L0,0');

        path = svg.append('g').selectAll('path');
        paintedPath = svg.append('g').selectAll('path');
    }

    /**
     * Draw a dataflow with the loaded information.
     *
     * @param data
     */
    function drawWorkflow(data) {
        console.debug(JSON.parse(data));
        wfEditor.data = JSON.parse(data);
        document.querySelector('.process-name').textContent = wfEditor.data.process.name;
        wfEditor.setElementMenu();
    }

    /**
     * process designer 초기화.
     *
     * @param process 프로세스 정보  예시) {processId: 'c0ee5ee8-d2fa-44cf-962c-9f853c24ea7b'}
     */
    function init(process) {
        console.info('Workflow editor initialization. [PROCESS ID: ' + process.processId + ']');

        workflowUtil.polyfill();
        initWorkflowEdit();
        addElementsEvent();
        wfEditor.initWorkflowUtil();

        // load process data.
        const xhr = createXmlHttpRequestObject('GET', '/rest/processes/data/' + process.processId);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    drawWorkflow(xhr.responseText);
                } else if (xhr.status === 400) {
                    alert('There was an error 400');
                } else {
                    console.log(xhr);
                    alert('something else other than 200 was returned. ' + xhr.status);
                }
            }
        };
        xhr.send();
    }

    exports.init = init;
    exports.data = data;
    Object.defineProperty(exports, '__esModule', {value: true});
})));