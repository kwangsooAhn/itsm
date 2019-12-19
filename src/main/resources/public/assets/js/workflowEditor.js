(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    function allowDrop(e) {
        e.preventDefault();
    }

    function elementDrop(img, x, y) {
        let drag = d3.drag().on("drag", elementMover);
        d3.select('#box').append("image")
            .attr("x", x)
            .attr("y", y)
            .attr("width", img.clientWidth + "px")
            .attr("height",  img.clientHeight + "px")
            .attr("xlink:href", img.src)
            .call(drag);
    }

    function elementMover() {
        d3.select(d3.event.sourceEvent.srcElement)
            .attr("x", d3.event.x - parseInt(d3.select('image').attr("width")) / 2)
            .attr("y", d3.event.y - parseInt(d3.select('image').attr("height")) / 2);
    }

    function addDragEvent() {
        let elementMenu = document.getElementsByClassName('element-menu')[0];
        elementMenu.addEventListener('dragover', allowDrop);

        let elements = elementMenu.querySelectorAll('img');
        for (let i = 0, len = elements.length; i < len; i++) {
            elements[i].setAttribute("draggable", 'true');
            elements[i].addEventListener('dragend', function() { elementDrop(this,(event.x-68),(event.y-48)) });
        }

        let drawingBoard = document.getElementsByClassName('drawing-board')[0];
        drawingBoard.addEventListener('dragover', allowDrop);
    }

    function init() {
        console.info('Workflow editor initialization.');
        addDragEvent();
    }

    exports.init = init;
    Object.defineProperty(exports, '__esModule', { value: true });
})));