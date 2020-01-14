(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    let svg,
        path,
        dragLine;
    let lastNodesId = 0;
    const links = [];

    let mouseupNode,
        mousedownNode,
        selectedNode,
        mousedownLink,
        selectedLink;

    let isSelectedConnector = false;

    /**
     * reset mouse variables.
     */
    function resetMouseVars() {
        mousedownNode = null;
        mouseupNode = null;
        mousedownLink = null;
    }

    const utils = {
        /**
         * 해당 element의 중앙 x,y 좌표와 넓이,높이를 리턴한다.
         *
         * @param selection
         * @returns {{x: number, width: number, y: number, height: number}}
         */
        getBoundingBoxCenter: function(selection) {
            const element = selection.node();
            const bbox = element.getBBox();
            return {x: bbox.x, y: bbox.y, cx: bbox.x + bbox.width / 2, cy: bbox.y + bbox.height / 2, width: bbox.width, height: bbox.height};
        }
    }

    /**
     * 선택된 element 를 해제한다.
     */
    function removeNodeSelected() {
        selectedNode = null;
        svg.selectAll('.node').style('stroke-width', 1);
        svg.selectAll('.pointer').style('opacity', 0);
        svg.selectAll('.menu').remove();
    }

    /**
     * set connector.
     */
    function setConnectors() {
        path = path.data(links);

        // update existing links
        path.classed('selected', d => d === selectedLink)
            .style('marker-start', d => d.left ? 'url(#start-arrow)' : '')
            .style('marker-end', d => d.right ? 'url(#end-arrow)' : '');

        // remove old links
        path.exit().remove();

        // add new links
        path = path.enter().append('path')
            .attr('class', 'link')
            .classed('selected', d => d === selectedLink)
            .style('marker-start', d => d.left ? 'url(#start-arrow)' : '')
            .style('marker-end', d => d.right ? 'url(#end-arrow)' : '')
            .on('mousedown', d => {
                if (d3.event.ctrlKey) {
                    return;
                }

                // select link
                mousedownLink = d;
                selectedLink = (mousedownLink === selectedLink) ? null : mousedownLink;
                selectedNode = null;
                setConnectors();
            })
            .merge(path);

        // draw links
        drawConnectors();
    }

    /**
     * draw connector.
     */
    function drawConnectors() {
        path.attr('d', d => {
            const targetBBox = utils.getBoundingBoxCenter(d.target);
            const sourceBBox = utils.getBoundingBoxCenter(d.source);

            let min = Number.MAX_SAFE_INTEGER;
            let best = {};
            [[sourceBBox.width / 2, 0], [sourceBBox.width, sourceBBox.height / 2],
                [sourceBBox.width / 2, sourceBBox.height], [0, sourceBBox.height / 2]].forEach(s =>
                [[targetBBox.width / 2, 0], [targetBBox.width, targetBBox.height / 2],
                    [targetBBox.width / 2, targetBBox.height], [0, targetBBox.height / 2]].forEach(d => {
                    let dist = Math.hypot(
                        (targetBBox.x + d[0]) - (sourceBBox.x + s[0]),
                        (targetBBox.y + d[1]) - (sourceBBox.y + s[1])
                    );
                    if (dist < min) {
                        min = dist;
                        best = {
                            s: { x: sourceBBox.x + s[0], y: sourceBBox.y + s[1] },
                            d: { x: targetBBox.x + d[0], y: targetBBox.y + d[1] }
                        };
                    }
                })
            );

            let lineFunction = d3.line().x(d => d.x).y(d => d.y).curve(d3.curveLinear);
            return lineFunction([best.s, best.d]);

            /*const lineGenerator = d3.line();
            //lineGenerator.curve(d3.curveStep);
            //lineGenerator.curve(d3.curveStepAfter);
            //lineGenerator.curve(d3.curveStepBefore);
            lineGenerator.curve(d3.curveLinear);
            return lineGenerator([[sourceBBox.cx, sourceBBox.cy], [targetBBox.cx, targetBBox.cy]]);*/
        });
    }

    /**
     * element 마우스 이벤트.
     *
     * @type {{mouseover: mouseover, mouseout: mouseout, mouseup: mouseup, mousedown: mousedown}}
     */
    const elementMouseEventHandler = {
        mouseover: function() {
            const elem = d3.select(this);
            if (!mousedownNode || elem === mousedownNode) {
                return;
            }
            node.attr('stroke-width', 2);
        },
        mouseout: function() {
            const elem = d3.select(this);
            if (!mousedownNode || node === mousedownNode) {
                return;
            }
            elem.attr('stroke-width', 1);
        },
        mousedown: function () {
            const elem = d3.select(this);
            if (d3.event.ctrlKey) {
                removeNodeSelected();
                mousedownNode = elem;
                selectedNode = (mousedownNode === selectedNode) ? null : mousedownNode;
                selectedLink = null;

                const bbox = utils.getBoundingBoxCenter(mousedownNode);
                dragLine
                    .style('marker-end', 'url(#end-arrow)')
                    .classed('hidden', false)
                    .attr('d', `M${bbox.cx},${bbox.cy}L${bbox.cx},${bbox.cy}`);

                setConnectors();
            } else {
                if (selectedNode === elem) {
                    return;
                }
                removeNodeSelected();
                mousedownNode = elem;
                selectedNode = (mousedownNode === selectedNode) ? null : mousedownNode;
                selectedNode.style('stroke-width', 2);
                if (elem.node().classList.contains('task')) {
                    const selectedNodeId = selectedNode.node().id;
                    for (let i = 1; i <= 4; i++) {
                        d3.select('#' + selectedNodeId + '_point' + i).style('opacity', 1);
                    }
                }
                wfEditor.setElementMenu(elem);
            }
            d3.event.preventDefault();
        },
        mouseup: function() {
            const elem = d3.select(this);
            if (d3.event.ctrlKey) {
                if (!mousedownNode) {
                    return;
                }
                dragLine
                    .classed('hidden', true)
                    .style('marker-end', '');

                mouseupNode = elem;
                if (mouseupNode === mousedownNode) {
                    resetMouseVars();
                    return;
                }

                elem.attr('stroke-width', 1);

                const isRight = mousedownNode.node().id < mouseupNode.node().id;
                const source = isRight ? mousedownNode : mouseupNode;
                const target = isRight ? mouseupNode : mousedownNode;

                const link = links.filter((l) => l.source === source && l.target === target)[0];
                if (link) {
                    link[isRight ? 'right' : 'left'] = true;
                } else {
                    links.push({source, target, left: !isRight, right: isRight});
                }

                selectedLink = link;
                selectedNode = null;
                setConnectors();
            }
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
            .attr('id', 'node' + (++lastNodesId))
            .attr('width', self.width)
            .attr('height', self.height)
            .attr('x', self.rectData.x)
            .attr('y', self.rectData.y)
            .style('fill', 'yellow')
            .style('opacity', 1)
            .style('stroke', 'black')
            .style('stroke-width', 1)
            .attr('class', 'node task')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .on('mousedown', elementMouseEventHandler.mousedown)
            .on('mouseup', elementMouseEventHandler.mouseup)
            .call(d3.drag().on('drag', () => {
                if (d3.event.ctrlKey) {
                    return;
                }
                const rectData = this.rectData;
                for (let i = 0, len = rectData.length; i < len; i++) {
                    this.nodeElement
                        .attr('x', rectData[i].x += d3.event.dx)
                        .attr('y', rectData[i].y += d3.event.dy);
                }
                self.nodeElement.style('cursor', 'move');

                updateRect();
            }));

        self.pointElement1 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode.node().id === this.nodeElement.node().id) {
                    this.pointElement1
                        .attr('cx', d => { return d.x += d3.event.dx })
                        .attr('cy', d => { return d.y += d3.event.dy });
                    updateRect();
                }
            }));
        self.pointElement2 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode.node().id === this.nodeElement.node().id) {
                    this.pointElement2
                        .attr('cx', this.rectData[1].x += d3.event.dx)
                        .attr('cy', this.rectData[1].y += d3.event.dy);
                    updateRect();
                }
            }));
        self.pointElement3 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode.node().id === this.nodeElement.node().id) {
                    this.pointElement3
                        .attr('cx', this.rectData[1].x += d3.event.dx)
                        .attr('cy', this.rectData[0].y += d3.event.dy);
                    updateRect();
                }
            }));
        self.pointElement4 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode.node().id === this.nodeElement.node().id) {
                    this.pointElement4
                        .attr('cx', this.rectData[0].x += d3.event.dx)
                        .attr('cy', this.rectData[1].y += d3.event.dy);
                    updateRect();
                }
            }));

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
            .attr('id', 'node' + (++lastNodesId))
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
            .on('mousedown', elementMouseEventHandler.mousedown)
            .on('mouseup', elementMouseEventHandler.mouseup)
            .call(d3.drag().on('drag', () => {
                if (d3.event.ctrlKey) {
                    return;
                }
                self.nodeElement
                    .attr('cx', d3.event.x)
                    .attr('cy', d3.event.y);
                self.nodeElement.style('cursor', 'move');
                drawConnectors();
            }));

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
            .attr('id', 'node' + (++lastNodesId))
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
            .on('mousedown', elementMouseEventHandler.mousedown)
            .on('mouseup', elementMouseEventHandler.mouseup)
            .call(d3.drag().on('drag', () => {
                if (d3.event.ctrlKey) {
                    return;
                }
                self.nodeElement
                    .attr('x', d3.event.x - (width / 2))
                    .attr('y', d3.event.y - (height / 2))
                    .attr('transform', 'rotate(45, ' + d3.event.x + ', ' + d3.event.y + ')');

                self.nodeElement.style('cursor', 'move');
                drawConnectors();
            }));

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
        this.nodeElement.style('fill', 'green');
        return this;
    }

    /**
     * element-palette에 element 를 추가하고, element의 drag & drop 이벤트를 추가한다.
     */
    function addElementsEvent() {
        d3.selectAll('.element-palette, .drawing-board')
            .on('dragover', () => {
                d3.event.preventDefault();
            });

        d3.select('.element-palette').select('.connector')
            .on('click', function() {
                isSelectedConnector = !d3.select(this).classed('selected');
                d3.select(this).classed('selected', isSelectedConnector);
            });

        d3.select('.element-palette').selectAll('span.shape')
            .attr('draggable', 'true')
            .on('dragend', function() {
                let x = d3.event.pageX - 68,
                    y = d3.event.pageY - 48;
                let element;

                if (d3.select(this).classed('event')) {
                    element = new EventElement(x, y);
                } else if (d3.select(this).classed('task')) {
                    element = new TaskElement(x, y);
                } else if (d3.select(this).classed('gateway')) {
                    element = new GatewayElement(x, y);
                } else if (d3.select(this).classed('group')) {
                    element = new GroupElement(x, y);
                }
            });
    }

    /**
     * svg 추가 및 필요한 element 추가.
     */
    function initWorkflow() {
        const width = 1405;
        const height = 750;

        // add svg and svg event
        svg = d3.select('.drawing-board').append('svg')
            .attr('width', width)
            .attr('height', height)
            .on("mousedown", function() {
                if (d3.event.ctrlKey) {
                    return;
                }
                removeNodeSelected();
                wfEditor.setElementMenu();
            })
            .on('mousemove', function() {
                if (!mousedownNode) {
                    return;
                }

                if (d3.event.ctrlKey) {
                    const bbox = utils.getBoundingBoxCenter(mousedownNode);
                    dragLine.attr('d', `M${bbox.cx},${bbox.cy}L${d3.mouse(this)[0]},${d3.mouse(this)[1]}`);
                } else {
                    if (mouseupNode && selectedNode) {
                        svg.selectAll('.menu').remove();
                    }
                }
            })
            .on('mouseup', function() {
                if (d3.event.ctrlKey) {
                    if (mousedownNode) {
                        dragLine
                            .classed('hidden', true)
                            .style('marker-end', '');
                    }
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
            .attr('class', 'link dragline hidden')
            .attr('d', 'M0,0L0,0');

        path = svg.append('g').selectAll('path');
    }

    /**
     * process designer 초기화.
     */
    function init() {
        console.info('Workflow editor initialization.');

        initWorkflow();
        addElementsEvent();
    }

    exports.init = init;
    Object.defineProperty(exports, '__esModule', {value: true});
})));