(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    function allowDrop(e) {
        e.preventDefault();
    }

    function drag(e, img) {
        e.dataTransfer.setData("text", img.src);
    }
    function drop(e, x, y) {
        let data = e.dataTransfer.getData("text");
        console.log(data)
        let drag = d3.drag().on("drag", mover);
        d3.select('#box').append("image")
            .attr("x", x)
            .attr("y", y)
            .attr("height", "50px")
            .attr("width", "50px")
            .attr("xlink:href", data)
            .call(drag);
    }

    function mover() {
        d3.select(d3.event.sourceEvent.srcElement)
            .attr("x", d3.event.x - parseInt(d3.select('image').attr("width")) / 2)
            .attr("y", d3.event.y - parseInt(d3.select('image').attr("height")) / 2);
    }

    function addDragEvent() {
        let elementMenu = document.getElementsByClassName('element-menu')[0];
        elementMenu.addEventListener('dragstart', drop);
        elementMenu.addEventListener('dragover', allowDrop);

        let elements = elementMenu.querySelectorAll('image');
        for (let i = 0, len = elements.length; i < len; i++) {
            elements[i].setAttribute("draggable", true);
            elements[i].addEventListener('dragstart', function(e) { drag(e, this) });
        }

        let drawingBoard = document.getElementsByClassName('drawing-board')[0];
        drawingBoard.addEventListener('dragstart', function() {drop(event,(event.x-60),(event.y-44))});
        drawingBoard.addEventListener('dragover', allowDrop);
    }

    function init() {
        console.info('Workflow editor initialization.');
        addDragEvent();
    }

    exports.init = init;
    Object.defineProperty(exports, '__esModule', { value: true });
})));