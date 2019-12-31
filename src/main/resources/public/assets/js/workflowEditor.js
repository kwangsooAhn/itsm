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
            return {x: bbox.x + bbox.width / 2, y: bbox.y + bbox.height / 2, width: bbox.width, height: bbox.height};
        }
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

        // 임시메뉴
        let menu = [{
            title: 'Item #1',
            action: function(elem, i) {
                console.log('Item #1 clicked!');
            }
        }, {
            title: 'Item #2',
            action: function(elem, i) {
                console.log('Item #2 clicked!');
            }
        }];

        const menuItemContainer = svg.append('g').classed('menu', true).style('display', 'none');

        const menuRect = menuItemContainer.append('rect')
            .attr('height', menu.length * 25)
            .style('fill', '#eee');

        const menuItems = menuItemContainer.selectAll('menu_item')
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
     * 해당 element의 properties를 표시한다.
     *
     * @param node 선택된 element
     */
    function setProperties(node) {
        const propertiesContainer = d3.select('.properties-menu');
        propertiesContainer.selectAll('*').remove();
        if (typeof node !== 'undefined') { // show element properties
            propertiesContainer.append('h3').text('Element Properties');
            for (let i = 0; i < 10; i++) {
                let propertyContainer = propertiesContainer.append('p');
                let label = propertyContainer.append('label');
                label.attr('for', 'attr_' + i);
                label.text('속성 ' + (i + 1));
                let input = propertyContainer.append('input');
                input.attr('id','attr_' + (i + 1));
                input.attr('value','property value ' + (i + 1));
            }
        } else { // show workflow properties
            propertiesContainer.append('h3').text('Workflow Properties');
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
        path = path.enter().append('svg:path')
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

            const deltaX = targetBBox.x - sourceBBox.x;
            const deltaY = targetBBox.y - sourceBBox.y;
            const dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            const normX = deltaX / dist;
            const normY = deltaY / dist;
            const sourcePadding = sourceBBox.width / 2;
            const targetPadding = targetBBox.width / 2;
            const sourceX = sourceBBox.x + (sourcePadding * normX);
            const sourceY = sourceBBox.y + (sourcePadding * normY);
            const targetX = targetBBox.x - (targetPadding * normX);
            const targetY = targetBBox.y - (targetPadding * normY);

            return `M${sourceX},${sourceY}L${targetX},${targetY}`;
        });
    }

    /**
     * element 마우스 이벤트.
     *
     * @type {{mouseover: mouseover, mouseout: mouseout, mouseup: mouseup, mousedown: mousedown}}
     */
    const elementMouseEventHandler = {
        mouseover: function() {
            const node = d3.select(this);
            if (!mousedownNode || node === mousedownNode) {
                return;
            }
            node.attr('stroke-width', 2);
        },
        mouseout: function() {
            const node = d3.select(this);
            if (!mousedownNode || node === mousedownNode) {
                return;
            }
            node.attr('stroke-width', 1);
        },
        mousedown: function () {
            const node = d3.select(this);
            if (d3.event.ctrlKey) {
                removeNodeSelected();
                mousedownNode = node;
                selectedNode = (mousedownNode === selectedNode) ? null : mousedownNode;
                selectedLink = null;

                const bbox = utils.getBoundingBoxCenter(mousedownNode);
                dragLine
                    .style('marker-end', 'url(#end-arrow)')
                    .classed('hidden', false)
                    .attr('d', `M${bbox.x},${bbox.y}L${bbox.x},${bbox.y}`);

                setConnectors();
            } else {
                if (selectedNode === node) {
                    return;
                }
                removeNodeSelected();
                mousedownNode = node;
                selectedNode = (mousedownNode === selectedNode) ? null : mousedownNode;
                selectedNode.style('stroke-width', 2);
                if (node.node().classList.contains('activity')) {
                    const selectedNodeId = selectedNode.node().id;
                    for (let i = 1; i <= 4; i++) {
                        d3.select('#' + selectedNodeId + '_point' + i).style('opacity', 1);
                    }
                }
                setMenuItem(node);
                setProperties(node);
            }
            d3.event.preventDefault();
        },
        mouseup: function() {
            const node = d3.select(this);
            if (d3.event.ctrlKey) {
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
            .attr('class', 'node activity')
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
     * element-menu에 element 를 추가한다.
     */
    function addElements() {
        //TODO: add element
    }

    /**
     * element의 drag & drop 이벤트를 추가한다.
     */
    function addDragEvent() {
        const elementMenu = document.getElementsByClassName('element-menu')[0];
        elementMenu.addEventListener('dragover', e => {e.preventDefault();});

        const elements = elementMenu.querySelectorAll('span');
        for (let i = 0, len = elements.length; i < len; i++) {
            elements[i].setAttribute('draggable', 'true');
            elements[i].addEventListener('dragend', e => {
                let x = e.x - 68, //TODO: element-menu 넓이 및 margin 등 제외 필요
                    y = e.y - 48; //TODO: toolbar 높이 및 margin 등 제외 필요

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

                if (element) {
                    //TODO:
                }
            });
        }

        const drawingBoard = document.getElementsByClassName('drawing-board')[0];
        drawingBoard.addEventListener('dragover', e => {e.preventDefault();});
    }

    /**
     * svg 추가 및 필요한 element 추가.
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
                setProperties();
            })
            .on('mousemove', function() {
                if (!mousedownNode) {
                    return;
                }

                if (d3.event.ctrlKey) {
                    const bbox = utils.getBoundingBoxCenter(mousedownNode);
                    dragLine.attr('d', `M${bbox.x},${bbox.y}L${d3.mouse(this)[0]},${d3.mouse(this)[1]}`);
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