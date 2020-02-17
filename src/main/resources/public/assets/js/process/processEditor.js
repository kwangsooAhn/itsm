(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceProcessEditor = global.AliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    const displayOptions = {
        translateLimit: 1000, // drawing board limit.
        durationTime: 100,
        boardInterval: 10
    }

    let data = {};

    let svg,
        gNode,
        path,
        paintedPath,
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
        svg.selectAll('.node').style('stroke-width', 1).transition().duration(displayOptions.durationTime);
        svg.selectAll('.pointer').style('opacity', 0);
        svg.selectAll('.alice-tooltip').remove();
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
            .attr('class', 'connector')
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
            .merge(path);

        // add new paintedPath links
        paintedPath = paintedPath.enter().append('path')
            .attr('class', 'painted-connector')
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
                //path._groups[0][i].dispatchEvent(event);
                path.nodes()[i].dispatchEvent(event);
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
            const targetBBox = AliceProcessEditor.utils.getBoundingBoxCenter(d.target);
            const sourceBBox = AliceProcessEditor.utils.getBoundingBoxCenter(d.source);

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
            elem.classed('selected', true)
                .transition()
                .duration(displayOptions.durationTime);
        },
        mouseout: function() {
            const elem = d3.select(this);
            if (!isDrawConnector || !mousedownElement || elem === mousedownElement) {
                elem.style('cursor', 'default');
                return;
            }
            mouseoverElement = null;
            elem.classed('selected', false)
                .transition()
                .duration(displayOptions.durationTime);
        },
        mousedown: function () {
            const elem = d3.select(this);
            if (isDrawConnector) {
                resetMouseVars();
                removeElementSelected();
                mousedownElement = elem;
                selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;

                const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(mousedownElement),
                      gTransform = d3.zoomTransform(d3.select('g.node-container').node()),
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
                selectedElement.style('stroke-width', 2).transition().duration(displayOptions.durationTime);
                if (elem.node().getAttribute('class').match(/\bresizable\b/)) {
                    const selectedElementId = selectedElement.node().id;
                    for (let i = 1; i <= 4; i++) {
                        // svg.select('#' + selectedElementId + '_point' + i).style('opacity', 1); <- querySelector 로 첫번째 글자로 숫자가 오면 오류남.
                        document.getElementById(selectedElementId + '_point' + i).style.opacity = '1';
                    }
                }
                elem.style('cursor', 'move');
                AliceProcessEditor.setElementMenu(elem);
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
                        .classed('selected', false)
                        .transition()
                        .duration(displayOptions.durationTime);

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
                if (svg.select('.alice-tooltip').node() === null) {
                    AliceProcessEditor.setActionTooltipItem(elem);
                }
            }
        },
        mousedrag: function() {
            const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(mousedownElement),
                  gTransform = d3.zoomTransform(d3.select('g.node-container').node()),
                  centerX = bbox.cx + gTransform.x,
                  centerY = bbox.cy + gTransform.y;
            dragLine.attr('d', 'M' + centerX + ',' + centerY + 'L' + (d3.event.x + gTransform.x) + ',' + (d3.event.y + gTransform.y));
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
        self.width = 120;
        self.height = 70;
        self.radius = 10;
        x = x - (self.width / 2);
        y = y - (self.height / 2);

        self.rectData = [{ x: x, y: y }, { x: x + self.width, y: y + self.height }];
        self.nodeElement = gNode.append('rect')
            .attr('id', workflowUtil.generateUUID)
            .attr('width', self.width)
            .attr('height', self.height)
            .attr('x', self.rectData.x)
            .attr('y', self.rectData.y)
            .attr('rx', self.radius)
            .attr('ry', self.radius)
            .attr('class', 'node resizable')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.alice-tooltip').remove();
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

        self.pointElement1 = gNode.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement1.style('cursor', 'nw-resize'); })
            .on('mouseout', function() { self.pointElement1.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.alice-tooltip').remove();
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
                    AliceProcessEditor.setElementMenu(self.nodeElement);
                })
            );
        self.pointElement2 = gNode.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement2.style('cursor', 'se-resize'); })
            .on('mouseout', function() { self.pointElement2.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.alice-tooltip').remove();
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
                    AliceProcessEditor.setElementMenu(self.nodeElement);
                })
            );
        self.pointElement3 = gNode.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement3.style('cursor', 'ne-resize'); })
            .on('mouseout', function() { self.pointElement3.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.alice-tooltip').remove();
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
                    AliceProcessEditor.setElementMenu(self.nodeElement);
                })
            );
        self.pointElement4 = gNode.append('circle')
            .attr('class', 'pointer')
            .style('opacity', 0)
            .on('mouseover', function() { self.pointElement4.style('cursor', 'sw-resize'); })
            .on('mouseout', function() { self.pointElement4.style('cursor', 'default'); })
            .call(d3.drag()
                .on('start', function() {
                    svg.selectAll('.alice-tooltip').remove();
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
                    AliceProcessEditor.setElementMenu(self.nodeElement);
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
        this.nodeElement.classed('subprocess', true);
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
        const radius = 25;

        self.nodeElement = gNode.append('circle')
            .attr('id', workflowUtil.generateUUID)
            .attr('r', radius)
            .attr('cx', x)
            .attr('cy', y)
            .attr('class', 'node event')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.alice-tooltip').remove();
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
        const width = 45, height = 45;

        self.nodeElement = gNode.append('rect')
            .attr('id', workflowUtil.generateUUID)
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('transform', 'rotate(45, ' + x + ', ' + y + ')')
            .attr('class', 'node gateway')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.alice-tooltip').remove();
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
        this.nodeElement.classed('group', true);
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

        self.nodeElement = gNode.append('rect')
            .attr('id', workflowUtil.generateUUID)
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('class', 'node annotation')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(d3.drag()
                .on('start', elementMouseEventHandler.mousedown)
                .on('drag', function() {
                    if (isDrawConnector) {
                        elementMouseEventHandler.mousedrag();
                    } else {
                        svg.selectAll('.alice-tooltip').remove();
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
                      gTransform = d3.zoomTransform(d3.select('g.node-container').node());
                let x = d3.event.pageX - svgOffset.left - window.pageXOffset - gTransform.x,
                    y = d3.event.pageY - svgOffset.top - window.pageYOffset - gTransform.y;
                let _this = d3.select(this);
                let node;
                if (_this.classed('event')) {
                    node = new EventElement(x, y);
                } else if (_this.classed('task')) {
                    node = new TaskElement(x, y);
                } else if (_this.classed('subprocess')) {
                    node = new SubprocessElement(x, y);
                } else if (_this.classed('gateway')) {
                    node = new GatewayElement(x, y);
                } else if (_this.classed('group')) {
                    node = new GroupElement(x, y);
                } else if (_this.classed('annotation')) {
                    node = new AnnotationElement(x, y);
                }
                if (node) {
                    nodes.push(node.nodeElement);
                }
            });
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

            verticalScale.domain([0, drawingBoardWidth / displayOptions.boardInterval]).range([0, drawingBoardWidth]);
            horizontalScale.domain([0, drawingBoardHeight / displayOptions.boardInterval]).range([0, drawingBoardHeight]);
            verticalAxis
                .scale(verticalScale)
                .ticks(drawingBoardWidth / displayOptions.boardInterval)
                .tickSize(drawingBoardHeight);
            horizontalAxis
                .scale(horizontalScale)
                .ticks(drawingBoardHeight / displayOptions.boardInterval)
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

                let minLeft = 0,
                    minTop = 0,
                    maxRight = 0,
                    maxBottom = 0;
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
                    .call(horizontalAxis.scale(d3.event.transform.rescaleX(horizontalScale)));
                verticalGrid
                    .call(verticalAxis.scale(d3.event.transform.rescaleY(verticalScale)));
                svg.select('g.node-container')
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

        svg.append('defs').append('marker')
            .attr('id', 'start-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 4)
            .attr('markerWidth', 5)
            .attr('markerHeight', 8)
            .attr('orient', 'auto')
            .append('path')
            .attr('d', 'M10,-5L0,0L10,5');

        // line displayed when dragging new nodes
        dragLine = svg.append('path')
            .attr('class', 'connector drag-line hidden')
            .attr('d', 'M0,0L0,0');

        gNode = svg.append('g').attr('class', 'node-container');
        path = gNode.selectAll('path.connector');
        paintedPath = gNode.selectAll('path.painted-connector');
    }

    /**
     * Draw a element with the loaded information.
     *
     * @param data
     */
    function drawProcess(data) {
        console.debug(JSON.parse(data));
        AliceProcessEditor.data = JSON.parse(data);
        document.querySelector('.process-name').textContent = AliceProcessEditor.data.process.name;
    }

    /**
     * process designer 초기화.
     *
     * @param process 프로세스 정보  예시) {processId: 'c0ee5ee8-d2fa-44cf-962c-9f853c24ea7b'}
     */
    function init(process) {
        console.info('process editor initialization. [PROCESS ID: ' + process.processId + ']');

        workflowUtil.polyfill();
        initProcessEdit();
        addElementsEvent();
        AliceProcessEditor.loadItems();
        AliceProcessEditor.initUtil();

        // load process data.
        aliceJs.sendXhr({
            method: 'GET',
            url: '/rest/processes/data/' + process.processId,
            callbackFunc: function(xhr) {
                drawProcess(xhr.responseText);
            },
            contentType: 'application/json; charset=utf-8'
        });
    }

    exports.init = init;
    exports.data = data;
    Object.defineProperty(exports, '__esModule', {value: true});
})));