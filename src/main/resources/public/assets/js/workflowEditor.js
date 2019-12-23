(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    function ActivityElement(x, y) {
        const self = this;
        const width = 80, height = 50;

        self.rectData = [{ x: x, y: y }, { x: x + width, y: y + height }];
        self.rectangleElement = d3.select('svg').append('rect')
            .attr('width', width)
            .attr('height', height)
            .attr('x', x)
            .attr('y', y)
            .style("fill", 'yellow')
            .style("opacity", 0.5)
            .style("stroke", 'black')
            .style("stroke-width", '2px')
            .attr('class', 'rectangle')
            .call(d3.drag().on('drag', function() {
                for (let i = 0; i < self.rectData.length; i++) {
                    self.rectangleElement
                        .attr('x', self.rectData[i].x += d3.event.dx)
                        .attr('y', self.rectData[i].y += d3.event.dy);
                }
                self.rectangleElement.style('cursor', 'move');
                updateRect();
            }));

        self.pointElement1 = d3.select('svg').append('circle').call(d3.drag().on('drag', function() {
            d3.select(this)
                .attr('cx', function(d) { return d.x += d3.event.dx })
                .attr('cy', function(d) { return d.y += d3.event.dy });
            updateRect();
        }));
        self.pointElement2 = d3.select('svg').append('circle').call(d3.drag().on('drag', function() {
            d3.select(this)
                .attr('cx', self.rectData[1].x += d3.event.dx )
                .attr('cy', self.rectData[1].y += d3.event.dy );
            updateRect();
        }));
        self.pointElement3 = d3.select('svg').append('circle').call(d3.drag().on('drag', function() {
            d3.select(this)
                .attr('cx', self.rectData[1].x += d3.event.dx )
                .attr('cy', self.rectData[0].y += d3.event.dy );
            updateRect();
        }));
        self.pointElement4 = d3.select('svg').append('circle').call(d3.drag().on('drag', function() {
            d3.select(this)
                .attr('cx', self.rectData[0].x += d3.event.dx )
                .attr('cy', self.rectData[1].y += d3.event.dy );
            updateRect();
        }));

        function updateRect() {
            self.rectangleElement
                .attr('x', self.rectData[1].x - self.rectData[0].x > 0 ? self.rectData[0].x : self.rectData[1].x)
                .attr('y', self.rectData[1].y - self.rectData[0].y > 0 ? self.rectData[0].y :  self.rectData[1].y)
                .attr('width', Math.abs(self.rectData[1].x - self.rectData[0].x))
                .attr('height', Math.abs(self.rectData[1].y - self.rectData[0].y));

            self.pointElement1
                .data(self.rectData)
                .attr('r', 5)
                .attr('cx', self.rectData[0].x)
                .attr('cy', self.rectData[0].y);
            self.pointElement2
                .data(self.rectData)
                .attr('r', 5)
                .attr('cx', self.rectData[1].x)
                .attr('cy', self.rectData[1].y);
            self.pointElement3
                .data(self.rectData)
                .attr('r', 5)
                .attr('cx', self.rectData[1].x)
                .attr('cy', self.rectData[0].y);
            self.pointElement4
                .data(self.rectData)
                .attr('r', 5)
                .attr('cx', self.rectData[0].x)
                .attr('cy', self.rectData[1].y);
        }
        updateRect();
    }

    function elementDrop(x, y) {
        new ActivityElement(x, y);
    }

    function addDragEvent() {
        let elementMenu = document.getElementsByClassName('element-menu')[0];
        elementMenu.addEventListener('dragover', function (e) {e.preventDefault();});

        let elements = elementMenu.querySelectorAll('span');
        for (let i = 0, len = elements.length; i < len; i++) {
            elements[i].setAttribute('draggable', 'true');
            elements[i].addEventListener('dragend', function(e) { elementDrop(e.x - 68, e.y - 48) });
        }

        let drawingBoard = document.getElementsByClassName('drawing-board')[0];
        drawingBoard.addEventListener('dragover', function (e) {e.preventDefault();});
    }

    function init() {
        console.info('Workflow editor initialization.');

        let drawingBoard = document.getElementsByClassName('drawing-board')[0];
        addDragEvent();
    }

    exports.init = init;
    Object.defineProperty(exports, '__esModule', { value: true });
})));