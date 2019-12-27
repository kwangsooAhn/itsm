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

    /**
     * reset mouse variables.
     */
    function resetMouseVars() {
        mousedownNode = null;
        mouseupNode = null;
        mousedownLink = null;
    }

    /**
     * 해당 element의 메뉴를 표시한다.
     *
     * @param node 선택된 element
     */
    function setMenuItem(node) {
        let x , y;
        if (node.node() instanceof SVGCircleElement) {
            x = node.attr('cx') - node.attr('r');
            y = node.attr('cy') - node.attr('r');
        } else {
            x = node.attr('x');
            y = node.attr('y');
        }
        let menu = [{
            title: 'Item #1',
            action: function(elem, i) {
                console.log('Item #1 clicked!');
                console.log(elem);
            }
        }, {
            title: 'Item #2',
            action: function(elem, i) {
                console.log('Item #2 clicked!');
                console.log(elem);
            }
        }];

        let menuItemContainer = svg.append('g').classed('menu', true).style('display', 'none');

        let menuRect = menuItemContainer.append('rect')
            .attr('height', menu.length * 25)
            .style('fill', '#eee');

        let menuItems = menuItemContainer.selectAll('menu_item')
            .data(menu)
            .enter()
            .append('g')
            .attr('transform', (d, i) => {
                return 'translate(' + 10 + ',' + ((i + 1) * 20) + ')';
            })
            .on('mouseover', d => {
                d3.select(this).style('fill', 'steelblue');
            })
            .on('mouseout', d => {
                d3.select(this).style('fill', 'black');
            })
            .on('click', (d, i) => {
                d.action(node, i);
            })
            .append('text')
            .text(d => {
                return d.title;
            });

        let width = 0;
        menuItems.each(function(d){
            let len = this.getComputedTextLength();
            if (len > width) {
                width = len;
            }
        })
        menuRect.attr('width', width + 20);

        menuItemContainer.attr('transform', 'translate(' + x + ',' + (y - (menu.length * 25)) + ')');
        menuItemContainer.style('display', 'block');
        menuItemContainer.datum(node);
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
        console.log(links)
        path = path.data(links);
    }

    /**
     * element 마우스 이벤트.
     *
     * @type {{mouseover: mouseover, mouseout: mouseout, mouseup: mouseup, mousedown: mousedown}}
     */
    const elementMouseEventHandler = {
        mouseover: function() {
            let node = d3.select(this);
            if (!mousedownNode || node === mousedownNode) {
                return;
            }
            node.attr('stroke-width', 2);
        },
        mouseout: function() {
            let node = d3.select(this);
            if (!mousedownNode || node === mousedownNode) {
                return;
            }
            node.attr('stroke-width', 1);
        },
        mousedown: function () {
            let node = d3.select(this);
            if (d3.event.ctrlKey) {
                removeNodeSelected();
                mousedownNode = node;
                selectedNode = (mousedownNode === selectedNode) ? null : mousedownNode;
                selectedLink = null;

                dragLine
                    .style('marker-end', 'url(#end-arrow)')
                    .classed('hidden', false);
                if (node.node() instanceof SVGCircleElement) {
                    dragLine
                        .attr('d', `M${mousedownNode.attr('cx')},${mousedownNode.attr('cy')}L${mousedownNode.attr('cx')},${mousedownNode.attr('cy')}`);
                } else {
                    dragLine
                        .attr('d', `M${mousedownNode.attr('x')},${mousedownNode.attr('y')}L${mousedownNode.attr('x')},${mousedownNode.attr('y')}`);
                }
                setConnectors();
            } else {
                if (selectedNode === node) {
                    return;
                }
                removeNodeSelected();
                mousedownNode = node;
                selectedNode = (mousedownNode === selectedNode) ? null : mousedownNode;
                selectedNode.style('stroke-width', 2);
                if (false) {
                    node.parent.pointElement1.style('opacity', 1);
                    node.parent.pointElement2.style('opacity', 1);
                    node.parent.pointElement3.style('opacity', 1);
                    node.parent.pointElement4.style('opacity', 1);
                }

                setMenuItem(node);
            }
            d3.event.preventDefault();
        },
        mouseup: function() {
            let node = d3.select(this);
            if (!mousedownNode) {
                return;
            }
            dragLine
                .classed('hidden', true)
                .style('marker-end', '');

            mouseupNode = node;
            if (mouseupNode === mousedownNode) {
                resetMouseVars();
                return;
            }

            node.attr('stroke-width', 1);

            const isRight = mousedownNode.node().id < mouseupNode.node().id;
            const source = isRight ? mousedownNode : mouseupNode;
            const target = isRight ? mouseupNode : mousedownNode;

            const link = links.filter((l) => l.source === source && l.target === target)[0];
            if (link) {
                link[isRight ? 'right' : 'left'] = true;
            } else {
                links.push({ source, target, left: !isRight, right: isRight });
            }

            selectedLink = link;
            selectedNode = null;
            setConnectors();
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

        self.rectData = [{ x: x, y: y }, { x: x + self.width, y: y + self.height }];
        self.nodeElement = svg.append('rect')
            .attr('id', ++lastNodesId)
            .attr('width', self.width)
            .attr('height', self.height)
            .attr('x', self.rectData.x)
            .attr('y', self.rectData.y)
            .style('fill', 'yellow')
            .style('opacity', 0.5)
            .style('stroke', 'black')
            .style('stroke-width', 1)
            .attr('class', 'node activity')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .on('mousedown', elementMouseEventHandler.mousedown)
            .on('mouseup', elementMouseEventHandler.mouseup)
            .call(d3.drag().on('drag', () => {
                if (d3.event.ctrlKey) {
                    return;
                }
                for (let i = 0, len = self.rectData.length; i < len; i++) {
                    self.nodeElement
                        .attr('x', self.rectData[i].x += d3.event.dx)
                        .attr('y', self.rectData[i].y += d3.event.dy);
                }
                self.nodeElement.style('cursor', 'move');
                updateRect();
            }));

        self.pointElement1 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode === self.nodeElement) {
                    self.pointElement1
                        .attr('cx', d => { return d.x += d3.event.dx })
                        .attr('cy', d => { return d.y += d3.event.dy });
                    updateRect();
                }
            }));
        self.pointElement2 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode === self.nodeElement) {
                    self.pointElement2
                        .attr('cx', self.rectData[1].x += d3.event.dx)
                        .attr('cy', self.rectData[1].y += d3.event.dy);
                    updateRect();
                }
            }));
        self.pointElement3 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode === self.nodeElement) {
                    self.pointElement3
                        .attr('cx', self.rectData[1].x += d3.event.dx)
                        .attr('cy', self.rectData[0].y += d3.event.dy);
                    updateRect();
                }
            }));
        self.pointElement4 = svg.append('circle')
            .classed('pointer', true)
            .style('opacity', 0)
            .call(d3.drag().on('drag', () => {
                if (selectedNode && selectedNode === self.nodeElement) {
                    self.pointElement4
                        .attr('cx', self.rectData[0].x += d3.event.dx)
                        .attr('cy', self.rectData[1].y += d3.event.dy);
                    updateRect();
                }
            }));

        function updateRect() {
            const pointerRadius = 4;

            self.nodeElement
                .attr('x', self.rectData[1].x - self.rectData[0].x > 0 ? self.rectData[0].x : self.rectData[1].x)
                .attr('y', self.rectData[1].y - self.rectData[0].y > 0 ? self.rectData[0].y :  self.rectData[1].y)
                .attr('width', Math.abs(self.rectData[1].x - self.rectData[0].x))
                .attr('height', Math.abs(self.rectData[1].y - self.rectData[0].y));

            self.pointElement1
                .data(self.rectData)
                .attr('r', pointerRadius)
                .attr('cx', self.rectData[0].x)
                .attr('cy', self.rectData[0].y);
            self.pointElement2
                .data(self.rectData)
                .attr('r', pointerRadius)
                .attr('cx', self.rectData[1].x)
                .attr('cy', self.rectData[1].y);
            self.pointElement3
                .data(self.rectData)
                .attr('r', pointerRadius)
                .attr('cx', self.rectData[1].x)
                .attr('cy', self.rectData[0].y);
            self.pointElement4
                .data(self.rectData)
                .attr('r', pointerRadius)
                .attr('cx', self.rectData[0].x)
                .attr('cy', self.rectData[1].y);
        }
        updateRect();
    }

    /**
     * Activity element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {ActivityElement}
     * @constructor
     */
    function ActivityElement(x, y) {
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
            .attr('id', ++lastNodesId)
            .attr('r', radius)
            .attr('cx', x)
            .attr('cy', y)
            .style('fill', 'red')
            .style('opacity', 0.5)
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
            .attr('id', ++lastNodesId)
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('transform', 'rotate(45, ' + x + ', ' + y + ')')
            .style('fill', 'blue')
            .style('opacity', 0.5)
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
            }));

        return self;
    }

    /**
     * pool element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {PoolElement}
     * @constructor
     */
    function PoolElement(x, y) {
        this.base = RectResizableElement;
        this.base(x, y);
        this.nodeElement.style('fill', 'green');
        return this;
    }

    /**
     * drop 시 호출.
     *
     * @param e
     */
    function elementDrop(e) {
        let x = e.x - 68,
            y = e.y - 48;

        let element;
        if (e.target.classList.contains('event')) {
            element = new EventElement(x, y);
        } else if (e.target.classList.contains('activity')) {
            element = new ActivityElement(x, y);
        } else if (e.target.classList.contains('gateway')) {
            element = new GatewayElement(x, y);
        } else if (e.target.classList.contains('pool')) {
            element = new PoolElement(x, y);
        }
    }

    /**
     *
     */
    function addElements() {

    }

    /**
     * element의 drag & drop 이벤트를 추가한다.
     */
    function addDragEvent() {
        let elementMenu = document.getElementsByClassName('element-menu')[0];
        elementMenu.addEventListener('dragover', e => {e.preventDefault();});

        let elements = elementMenu.querySelectorAll('span');
        for (let i = 0, len = elements.length; i < len; i++) {
            elements[i].setAttribute('draggable', 'true');
            elements[i].addEventListener('dragend', e => { elementDrop(e) });
        }

        let drawingBoard = document.getElementsByClassName('drawing-board')[0];
        drawingBoard.addEventListener('dragover', e => {e.preventDefault();});
    }

    /**
     * svg 추가.
     */
    function initD3() {
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
            })
            .on('mousemove', function() {
                if (!mousedownNode) {
                    return;
                }
                if (mousedownNode.node() instanceof SVGCircleElement) {
                    dragLine.attr('d', `M${mousedownNode.attr('cx')},${mousedownNode.attr('cy')}L${d3.mouse(this)[0]},${d3.mouse(this)[1]}`);
                } else {
                    dragLine.attr('d', `M${mousedownNode.attr('x')},${mousedownNode.attr('y')}L${d3.mouse(this)[0]},${d3.mouse(this)[1]}`);
                }

            })
            .on('mouseup', function() {
                if (mousedownNode) {
                    dragLine
                        .classed('hidden', true)
                        .style('marker-end', '');
                }
                resetMouseVars();
            });

        // define arrow markers for links
        svg.append('svg:defs').append('svg:marker')
            .attr('id', 'end-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 6)
            .attr('markerWidth', 3)
            .attr('markerHeight', 3)
            .attr('orient', 'auto')
            .append('svg:path')
            .attr('d', 'M0,-5L10,0L0,5')
            .attr('fill', '#000');

        svg.append('svg:defs').append('svg:marker')
            .attr('id', 'start-arrow')
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 4)
            .attr('markerWidth', 3)
            .attr('markerHeight', 3)
            .attr('orient', 'auto')
            .append('svg:path')
            .attr('d', 'M10,-5L0,0L10,5')
            .attr('fill', '#000');

        // line displayed when dragging new nodes
        dragLine = svg.append('svg:path')
            .attr('class', 'link dragline hidden')
            .attr('d', 'M0,0L0,0');

        path = svg.append('svg:g').selectAll('path');
    }

    /**
     * process designer 초기화.
     */
    function init() {
        console.info('Workflow editor initialization.');

        initD3();
        addElements();
        addDragEvent();
    }

    exports.init = init;
    Object.defineProperty(exports, '__esModule', {value: true});
})));