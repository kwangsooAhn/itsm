(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.wfEditor = global.wfEditor || {})));
}(this, (function (exports) {
    'use strict';

    /**
     * 해당 element의 메뉴를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setMenuItem(elem) {
        if (typeof elem === 'undefined') {
            return;
        }

        let x , y;
        if (elem.node() instanceof SVGCircleElement) {
            x = elem.attr('cx') - elem.attr('r');
            y = elem.attr('cy') - elem.attr('r');
        } else {
            x = elem.attr('x');
            y = elem.attr('y');
        }

        // 임시메뉴
        let menu = [{
            title: 'Item #1',
            action: function(e, i) {
                console.log('Item #1 clicked!');
            }
        }, {
            title: 'Item #2',
            action: function(e, i) {
                console.log('Item #2 clicked!');
            }
        }];

        const menuItemContainer = d3.select('.drawing-board').select('svg').append('g')
            .classed('menu', true).style('display', 'none');

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
                d.action(elem, i);
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
        menuItemContainer.datum(elem);
    }

    /**
     * 해당 element의 properties를 표시한다.
     *
     * @param elem 선택된 element
     */
    function setProperties(elem) {
        const propertiesContainer = d3.select('.properties-panel');
        propertiesContainer.selectAll('*').remove();
        if (typeof elem !== 'undefined') { // show element properties
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

    function setElementMenu(elem) {
        setMenuItem(elem);
        setProperties(elem);
    }

    exports.setElementMenu = setElementMenu;
    Object.defineProperty(exports, '__esModule', {value: true});
})));