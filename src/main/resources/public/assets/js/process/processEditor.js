(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.AliceProcessEditor = global.AliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    const displayOptions = {
        translateLimit: 1000, // drawing board limit.
        gridInterval: 10,     // value of grid interval.
        pointerRadius: 5,
        connectorRadius: 8,
        connectorLabelPos: 20
    };

    let svg,
        groupArtifactContainer,
        elementsContainer,
        connectors,
        dragLine;

    const elements = {
        links: []
    };

    let mousedownElement,
        mouseoverElement,
        selectedElement,
        dragElement;

    let isDrawConnector = false,
        isMovableDrawingboard = false;

    /**
     * reset mouse variables.
     */
    function resetMouseVars() {
        mousedownElement = null;
        mouseoverElement = null;
        selectedElement = null;
        dragElement = null;
    }

    /**
     * snap to grid.
     *
     * @param p
     * @return {number}
     */
    function snapToGrid(p) {
        const r = displayOptions.gridInterval;
        return Math.round(p / r) * r;
    }

    /**
     * 선택된 element 를 해제한다.
     */
    function removeElementSelected() {
        selectedElement = null;
        svg.selectAll('.node').classed('selected', false);
        svg.selectAll('.connector').classed('selected', false);
        svg.selectAll('.pointer').style('opacity', 0).style('cursor', 'default');
        svg.selectAll('.alice-tooltip').remove();
    }

    /**
     * set connector.
     *
     * @param recycle 모두 지우고 다시 그리기 옵션
     */
    function setConnectors(recycle) {
        if (recycle) {
            connectors = connectors.data([]);
            connectors.exit().remove(); // remove old links
        }
        connectors = connectors.data(elements.links);
        connectors.exit().remove(); // remove old links

        let enter = connectors.enter().append('g').attr('class', 'connector');

        // add new links
        enter.append('path')
            .attr('class', 'connector')
            .classed('is-default', function(d) { return d.isDefault === 'Y'; })
            .attr('id', function(d) { return d.id; })
            .style('marker-end', 'url(#end-arrow)')
            .on('mousedown', function(d) {
                d3.event.stopPropagation();
                if (isDrawConnector) {
                    return;
                }

                removeElementSelected();
                resetMouseVars();

                // select link
                let selectedLink = d3.select(this).classed('selected', true);
                selectedElement = null;
                d3.select(document.getElementById(d.id + '_midPoint')).style('opacity', 1).style('cursor', 'move');

                setConnectors();
                AliceProcessEditor.setElementMenu(selectedLink);
            })
            .call(function(d) {
                AliceProcessEditor.addElementProperty(d);
            });

        /**
         * connector event handler.
         *
         * @type {{mouseover: mouseover, mouseout: mouseout, mousedown: mousedown}}
         */
        const connectorMouseEventHandler = {
            mouseover: function() {
                if (isDrawConnector) {
                    return;
                }
                if (!dragElement) {
                    d3.select(this).style('cursor', 'pointer');
                }
            },
            mouseout: function() {
                if (!dragElement) {
                    d3.select(this).style('cursor', 'default');
                }
            },
            mousedown: function(d) {
                d3.event.stopPropagation();
                if (isDrawConnector) {
                    return;
                }
                const event = document.createEvent('MouseEvent');
                event.initMouseEvent('mousedown', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                d3.select(document.getElementById(d.id)).node().dispatchEvent(event);
            }
        };

        // add new paintedPath links
        enter.append('path')
            .attr('class', 'painted-connector')
            .on('mouseout', connectorMouseEventHandler.mouseout)
            .on('mouseover', connectorMouseEventHandler.mouseover)
            .on('mousedown', connectorMouseEventHandler.mousedown);

        enter.append('text')
            .on('mouseout', connectorMouseEventHandler.mouseout)
            .on('mouseover', connectorMouseEventHandler.mouseover)
            .text(function(d) {
                let name = '';
                const elements = AliceProcessEditor.data.elements;
                elements.forEach(function(elem) {
                    if (elem.id === d.id) { name = elem.data.name; }
                });
                return name;
            })
            .call(
                d3.drag()
                    .on('start', function() {
                        if (isDrawConnector) {
                            return;
                        }
                        d3.select(this)
                            .classed('selected', true)
                            .style('cursor', 'move');
                    })
                    .on('drag', function(d) {
                        const textElem = d3.select(this);
                        let mouseX = d3.event.dx,
                            mouseY = d3.event.dy;
                        textElem
                            .attr('x', Number(textElem.attr('x')) + mouseX)
                            .attr('y', Number(textElem.attr('y')) + mouseY);
                        if (typeof d.textPoint !== 'undefined') {
                            mouseX += d.textPoint[0];
                            mouseY += d.textPoint[1];
                        }
                        d.textPoint = [mouseX, mouseY];
                    })
                    .on('end', function(d) {
                        d3.select(this)
                            .classed('selected', false)
                            .style('cursor', 'pointer');
                        AliceProcessEditor.changeDisplayValue(d.id);
                    })
            );

        enter.append('circle')
            .attr('class', 'pointer')
            .attr('id', function(d) { return d.id + '_midPoint'; })
            .attr('r', displayOptions.pointerRadius)
            .style('opacity', 0)
            .on('mouseout', connectorMouseEventHandler.mouseout)
            .on('mouseover', connectorMouseEventHandler.mouseover)
            .on('mousedown', connectorMouseEventHandler.mousedown)
            .call(d3.drag()
                .on('drag', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        svg.selectAll('.alice-tooltip').remove();
                        d.midPoint = [snapToGrid(d3.event.x), snapToGrid(d3.event.y)];
                        drawConnectors();
                    }
                })
                .on('end', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        AliceProcessEditor.setElementMenu(d3.select(document.getElementById(d.id)));
                        AliceProcessEditor.changeDisplayValue(d.id);
                    }
                })
            );

        enter.append('circle')
            .attr('class', 'pointer')
            .attr('id', function(d) { return d.id + '_sourcePoint'; })
            .attr('r', displayOptions.pointerRadius)
            .style('opacity', 0)
            .on('mouseout', connectorMouseEventHandler.mouseout)
            .on('mouseover', connectorMouseEventHandler.mouseover)
            .on('mousedown', connectorMouseEventHandler.mousedown)
            .call(d3.drag()
                .on('drag', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        d.sourcePoint = [snapToGrid(d3.event.x), snapToGrid(d3.event.y)];
                        drawConnectors();
                    }
                })
                .on('end', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        AliceProcessEditor.changeDisplayValue(d.id);
                    }
                })
            );

        enter.append('circle')
            .attr('class', 'pointer')
            .attr('id', function(d) { return d.id + '_targetPoint'; })
            .attr('r', displayOptions.pointerRadius)
            .style('opacity', 0)
            .on('mouseout', connectorMouseEventHandler.mouseout)
            .on('mouseover', connectorMouseEventHandler.mouseover)
            .on('mousedown', connectorMouseEventHandler.mousedown)
            .call(d3.drag()
                .on('drag', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        d.targetPoint = [snapToGrid(d3.event.x), snapToGrid(d3.event.y)];
                        drawConnectors();
                    }
                })
                .on('end', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        AliceProcessEditor.changeDisplayValue(d.id);
                    }
                })
            );

        connectors = connectors.merge(enter);

        // draw links
        drawConnectors();
    }

    /**
     * 시작지점과 종료지점의 라인을 좌표배열로 리턴한다.
     *
     * @param sourceBBox source element bbox.
     * @param targetBBox target element bbox.
     * @param sourcePointArray source element 시작 지점 배열
     * @param targetPointArray target element 시작 지점 배열
     * @return {[]} 라인배열
     */
    function getBestLine(sourceBBox, targetBBox, sourcePointArray, targetPointArray) {
        let best = [];
        let min = Number.MAX_SAFE_INTEGER || 9007199254740991;
        sourcePointArray.forEach(function(s) {
            targetPointArray.forEach(function(t) {
                let dist = Math.hypot(
                    (targetBBox.cx + t[0]) - (sourceBBox.cx + s[0]),
                    (targetBBox.cy + t[1]) - (sourceBBox.cy + s[1])
                );
                if (dist < min) {
                    min = dist;
                    let x1 = sourceBBox.cx + s[0],
                        x2 = targetBBox.cx + t[0],
                        y1 = sourceBBox.cy + s[1],
                        y2 = targetBBox.cy + t[1];
                    best = [[x1, y1], [x2, y2]];
                }
            });
        });
        return best;
    }

    /**
     * 라인 사이의 좌표를 구해서 리턴한다.
     *
     * @param line 시작/종료 라인좌표
     * @return {number[]} middle point 좌표
     */
    function getMidPointCoords(line) {
        return [(line[1][0] + line[0][0]) / 2, (line[1][1] + line[0][1]) / 2];
    }

    /**
     * draw connector.
     */
    function drawConnectors() {
        /**
         * 종료좌표에서 일정거리 떨어진 좌표를 구한다.
         *
         * @param sourceCoords 시작좌표
         * @param targetCoords 종료좌표
         * @return {[*, *]} 좌표
         */
        const calcDistancePointCoordinate = function(sourceCoords, targetCoords, distance) {
            if (typeof distance === 'undefined') {
                distance = displayOptions.connectorRadius;
            }
            let dx = Number(sourceCoords[0]) - Number(targetCoords[0]),
                dy = Number(sourceCoords[1]) - Number(targetCoords[1]),
                dist = Math.sqrt(dx * dx + dy * dy),
                x = Number(targetCoords[0]) + dx * distance / dist,
                y = Number(targetCoords[1]) + dy * distance / dist;
            return [x, y];
        };

        /**
         * convert 3 points to an Arc Path.
         *
         * @param startPoint 시작좌표
         * @param midPoint 중간좌표
         * @param endPoint 종료좌표
         * @return {string} path
         */
        const calcCirclePath = function(startPoint, midPoint, endPoint) {
            let distA = AliceProcessEditor.utils.calcDist(endPoint, midPoint);
            let distB = AliceProcessEditor.utils.calcDist(midPoint, startPoint);
            let distC = AliceProcessEditor.utils.calcDist(startPoint, endPoint);
            let angle = Math.acos((distA * distA + distB * distB - distC * distC) / (2 * distA * distB));

            //calc radius of circle
            let K = 0.3 * distA * distB * Math.sin(angle);
            let r = distA * distB * distC / 4 / K;
            //console.debug('A: %s, B: %s, C: %s', distA, distB, distC);
            r = Math.round(r * 1000) / 1000;

            //large arc flag
            let laf = +(Math.PI / 2 > angle);

            //sweep flag
            let saf = +((endPoint[0] - startPoint[0]) * (midPoint[1] - startPoint[1]) - (endPoint[1] - startPoint[1]) * (midPoint[0] - startPoint[0]) < 0);
            //console.debug('angle: %s, K: %s, r: %s, laf: %s, saf: %s', angle, K, r, laf, saf);

            //return ['L', startPoint, 'A', r, r, 0, laf, saf, endPoint].join(' ');
            return [' L', startPoint, 'A', r, r, 0, 0, saf, endPoint].join(' ');
        };

        /**
         * line path.
         *
         * @param d line data
         * @return {string} line path
         */
        const getLinePath = function(d) {
            const targetNode = document.getElementById(d.targetId),
                  sourceNode = document.getElementById(d.sourceId);
            if (!targetNode || !sourceNode) {
                return '';
            }
            let target = d3.select(targetNode),
                source = d3.select(sourceNode);
            const targetBBox = AliceProcessEditor.utils.getBoundingBoxCenter(target);
            const sourceBBox = AliceProcessEditor.utils.getBoundingBoxCenter(source);

            let targetWidth = targetBBox.width,
                targetHeight = targetBBox.height,
                sourceWidth = sourceBBox.width,
                sourceHeight = sourceBBox.height;
            if (target.classed('gateway') || source.classed('gateway')) {
                let gatewayDist = AliceProcessEditor.utils.calcDist([0, 0], [40, 40]);
                if (target.classed('gateway')) {
                    targetWidth = gatewayDist;
                    targetHeight = gatewayDist;
                }
                if (source.classed('gateway')) {
                    sourceWidth = gatewayDist;
                    sourceHeight = gatewayDist;
                }
            }
            let targetPointArray = [[0, -(targetHeight / 2)], [targetWidth / 2, 0],
                [0, targetHeight / 2], [-(targetWidth / 2), 0]];
            let sourcePointArray = [[0, -(sourceHeight / 2)], [sourceWidth / 2, 0],
                [0, sourceHeight / 2], [-(sourceWidth / 2), 0]];
            const midPoint = d3.select(document.getElementById(d.id + '_midPoint'));
            let linePath = '';
            if (typeof d.midPoint !== 'undefined') {
                midPoint.attr('cx', d.midPoint[0]).attr('cy', d.midPoint[1]);
                const sourcePoint = d3.select(document.getElementById(d.id + '_sourcePoint')),
                      targetPoint = d3.select(document.getElementById(d.id + '_targetPoint'));
                if (d3.select(document.getElementById(d.id)).classed('selected')) {
                    sourcePoint.style('opacity', 1).style('cursor', 'move');
                    targetPoint.style('opacity', 1).style('cursor', 'move');
                }

                const roundedCoords = [];
                let bestLine1,
                    bestLine2;
                if (typeof d.sourcePoint === 'undefined' && typeof d.targetPoint === 'undefined') {
                    bestLine1 = getBestLine(sourceBBox, {cx: d.midPoint[0], cy: d.midPoint[1]}, sourcePointArray, [[0, 0]]);
                    bestLine2 = getBestLine({cx: d.midPoint[0], cy: d.midPoint[1]}, targetBBox, [[0, 0]], targetPointArray);
                    roundedCoords.push(
                        [calcDistancePointCoordinate(bestLine1[0], d.midPoint), d.midPoint, calcDistancePointCoordinate(bestLine2[1], d.midPoint)]
                    );

                    let sourcePointCoords = getMidPointCoords(bestLine1);
                    sourcePoint.attr('cx', sourcePointCoords[0]).attr('cy', sourcePointCoords[1]);
                    let targetPointCoords = getMidPointCoords(bestLine2);
                    targetPoint.attr('cx', targetPointCoords[0]).attr('cy', targetPointCoords[1]);
                } else if (typeof d.sourcePoint === 'undefined') {
                    bestLine1 = getBestLine(sourceBBox, {cx: d.midPoint[0], cy: d.midPoint[1]}, sourcePointArray, [[0, 0]]);
                    bestLine2 = getBestLine({cx: d.targetPoint[0], cy: d.targetPoint[1]}, targetBBox, [[0, 0]], targetPointArray);
                    roundedCoords.push(
                        [calcDistancePointCoordinate(bestLine1[0], d.midPoint), d.midPoint, calcDistancePointCoordinate(d.targetPoint, d.midPoint)],
                        [calcDistancePointCoordinate(d.midPoint, d.targetPoint), d.targetPoint, calcDistancePointCoordinate(bestLine2[1], d.targetPoint)]
                    );

                    let sourcePointCoords = getMidPointCoords(bestLine1);
                    sourcePoint.attr('cx', sourcePointCoords[0]).attr('cy', sourcePointCoords[1]);
                    targetPoint.attr('cx', d.targetPoint[0]).attr('cy', d.targetPoint[1]);
                } else if (typeof d.targetPoint === 'undefined') {
                    bestLine1 = getBestLine(sourceBBox, {cx: d.sourcePoint[0], cy: d.sourcePoint[1]}, sourcePointArray, [[0, 0]]);
                    bestLine2 = getBestLine({cx: d.midPoint[0], cy: d.midPoint[1]}, targetBBox, [[0, 0]], targetPointArray);
                    roundedCoords.push(
                        [calcDistancePointCoordinate(bestLine1[0], d.sourcePoint), d.sourcePoint, calcDistancePointCoordinate(d.midPoint, d.sourcePoint)],
                        [calcDistancePointCoordinate(d.sourcePoint, d.midPoint), d.midPoint, calcDistancePointCoordinate(bestLine2[1], d.midPoint)]
                    );

                    let targetPointCoords = getMidPointCoords(bestLine2);
                    sourcePoint.attr('cx', d.sourcePoint[0]).attr('cy', d.sourcePoint[1]);
                    targetPoint.attr('cx', targetPointCoords[0]).attr('cy', targetPointCoords[1]);
                } else {
                    bestLine1 = getBestLine(sourceBBox, {cx: d.sourcePoint[0], cy: d.sourcePoint[1]}, sourcePointArray, [[0, 0]]);
                    bestLine2 = getBestLine({cx: d.targetPoint[0], cy: d.targetPoint[1]}, targetBBox, [[0, 0]], targetPointArray);
                    roundedCoords.push(
                        [calcDistancePointCoordinate(bestLine1[0], d.sourcePoint), d.sourcePoint, calcDistancePointCoordinate(d.midPoint, d.sourcePoint)],
                        [calcDistancePointCoordinate(d.sourcePoint, d.midPoint), d.midPoint, calcDistancePointCoordinate(d.targetPoint, d.midPoint)],
                        [calcDistancePointCoordinate(d.midPoint, d.targetPoint), d.targetPoint, calcDistancePointCoordinate(bestLine2[1], d.targetPoint)]
                    );

                    sourcePoint.attr('cx', d.sourcePoint[0]).attr('cy', d.sourcePoint[1]);
                    targetPoint.attr('cx', d.targetPoint[0]).attr('cy', d.targetPoint[1]);
                }

                linePath  = ['M', bestLine1[0]].join(' ');
                roundedCoords.forEach(function(coords) {
                    linePath += calcCirclePath(coords[0], coords[1], coords[2]);
                });
                linePath += [' L', bestLine2[1]].join(' ');
            } else {
                let bestLine = getBestLine(sourceBBox, targetBBox, sourcePointArray, targetPointArray);
                linePath = ['M', bestLine[0], 'L', bestLine[1]].join(' ');
                let midPointCoords = getMidPointCoords(bestLine);
                midPoint.attr('cx', midPointCoords[0]).attr('cy', midPointCoords[1]);
            }
            return linePath;
        };

        connectors.select('path.connector').attr('d', function(d) {
            let linePath = getLinePath(d);

            let paths = linePath.split(' ');
            let startCoords = paths[1].split(','),
                endCoords = paths[paths.length - 1].split(',');
            if (typeof d.sourcePoint !== 'undefined') {
                endCoords = d.sourcePoint;
            } else if (typeof d.midPoint !== 'undefined') {
                endCoords = d.midPoint;
            }
            const angleDeg = Math.atan2(endCoords[1] - startCoords[1], endCoords[0] - startCoords[0]) * 180 / Math.PI,
                  coords = calcDistancePointCoordinate(endCoords, startCoords, displayOptions.connectorLabelPos);
            d3.select(document.getElementById(d.id).parentNode).select('text')
                .attr('x', d.textPoint ? d.textPoint[0] + coords[0] : coords[0])
                .attr('y', d.textPoint ? d.textPoint[1] + coords[1] : coords[1])
                .style('text-anchor', Math.abs(angleDeg) > 90 ? 'end' : 'start')

            d3.select(document.getElementById(d.id).parentNode).select('path.painted-connector').attr('d', linePath);
            return linePath;
        });
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
            if (!dragElement) {
                let cursor = 'pointer';
                if (isDrawConnector) {
                    if (!elem.classed('group')) {
                        cursor = 'crosshair';
                    } else {
                        cursor = 'default';
                    }
                }
                elemContainer.style('cursor', cursor);
            }
            if (!isDrawConnector || !mousedownElement || elem.node().id === mousedownElement.node().id) {
                return;
            }
            mouseoverElement = elem;
            let availableLink = checkAvailableLink();
            elem.classed('selected', availableLink);
        },
        mouseout: function() {
            const elemContainer = d3.select(this.parentNode);
            const elem = elemContainer.select('.node');
            if (!isDrawConnector || !mousedownElement || elem.node().id === mousedownElement.node().id) {
                if (!dragElement) {
                    elemContainer.style('cursor', 'default');
                }
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

                if (!elem.classed('group')) {
                    const bbox = AliceProcessEditor.utils.getBoundingBoxCenter(mousedownElement),
                        gTransform = d3.zoomTransform(d3.select('g.element-container').node()),
                        centerX = bbox.cx + gTransform.x,
                        centerY = bbox.cy + gTransform.y;
                    dragLine
                        .style('marker-end', 'url(#end-arrow)')
                        .classed('hidden', false)
                        .attr('d', 'M' + centerX + ',' + centerY + 'L' + centerX + ',' + centerY);
                }
            } else {
                if (selectedElement === elem) {
                    return;
                }
                removeElementSelected();
                mousedownElement = elem;
                selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;
                selectedElement.classed('selected', true);
                if (elem.node().getAttribute('class').match(/\bresizable\b/)) {
                    d3.select(selectedElement.node().parentNode).selectAll('.pointer').nodes().forEach(function(elem) {
                        elem.style.opacity = 1;
                    });
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

                if (mousedownElement.node().id !== mouseoverElement.node().id) {
                    mouseoverElement
                        .classed('selected', false);

                    if (checkAvailableLink()) {
                        elements.links.push({id: workflowUtil.generateUUID(), sourceId: mousedownElement.node().id, targetId: mouseoverElement.node().id, isDefault: 'N'});
                        selectedElement = null;
                        setConnectors();
                    }
                }
                resetMouseVars();
            } else {
                dragElement = null;
                elemContainer.style('cursor', 'pointer');
                AliceProcessEditor.changeDisplayValue(elem.node().id);
                if (svg.select('.alice-tooltip').node() === null) {
                    AliceProcessEditor.setActionTooltipItem(elem);
                }
                svg.selectAll('line.guides-line').style('stroke-width', 0);
            }
        },
        mousedrag: function() {
            if (mousedownElement.classed('commonEnd') || mousedownElement.classed('messageEnd')) {
                return false;
            }
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
        elements.links.forEach(function(l) {
            // it's not a gateway, but several starts
            if (!source.classed('gateway') && l.sourceId === source.node().id) {
                availableLink = false;
            }
            // cannot link to each other
            if ((l.sourceId === source.node().id && l.targetId === target.node().id) ||
                (l.sourceId === target.node().id && l.targetId === source.node().id)) {
                availableLink = false;
            }
        });
        // common start cannot be a target.
        if (target.classed('commonStart')) {
            availableLink = false;
        }
        // common end/message end cannot be a source.
        if (source.classed('commonEnd') || source.classed('messageEnd')) {
            availableLink = false;
        }
        // group is not connected.
        if (source.classed('group') || target.classed('group')) {
            availableLink = false;
        }
        return availableLink;
    }

    /**
     * 현재 위치와 크기를 계산하여 guides line 처리를 한다.
     *
     * @param elem 대상 element
     */
    function drawGuides(elem) {
        const errorRange = 3;
        const elementBbox = AliceProcessEditor.utils.getBoundingBoxCenter(elem),
              gatewayDist = AliceProcessEditor.utils.calcDist([0, 0], [40, 40]);
        let elemLeft = elementBbox.cx - (elementBbox.width / 2),
            elemRight = elementBbox.cx + (elementBbox.width / 2),
            elemTop = elementBbox.cy - (elementBbox.height / 2),
            elemBottom = elementBbox.cy + (elementBbox.height / 2);
        if (elem.classed('gateway')) {
            elemLeft = elementBbox.cx - (gatewayDist / 2);
            elemRight = elementBbox.cx + (gatewayDist / 2);
            elemTop = elementBbox.cy - (gatewayDist / 2);
            elemBottom = elementBbox.cy + (gatewayDist / 2);
        }

        let isDrawCenterX = false,
            isDrawCenterY = false,
            isDrawLeft = false,
            isDrawRight = false,
            isDrawTop = false,
            isDrawBottom = false;
        const elements = AliceProcessEditor.data.elements.filter(function(e) { return e.type !== 'arrowConnector' && e.id !== elem.node().id; });
        elements.forEach(function(e) {
            let left = e.display['position-x'] - (e.display.width / 2),
                right = e.display['position-x'] + (e.display.width / 2),
                top = e.display['position-y'] - (e.display.height / 2),
                bottom = e.display['position-y'] + (e.display.height / 2);
            if (e.type.indexOf('Gateway') > -1) {
                left = e.display['position-x'] - (gatewayDist / 2);
                right = e.display['position-x'] + (gatewayDist / 2);
                top = e.display['position-y'] - (gatewayDist / 2);
                bottom = e.display['position-y'] + (gatewayDist / 2);
            }
            isDrawCenterX = isDrawCenterX || Math.abs(elementBbox.cx - e.display['position-x']) < errorRange;
            isDrawCenterY = isDrawCenterY || Math.abs(elementBbox.cy - e.display['position-y']) < errorRange;
            isDrawLeft = isDrawLeft || Math.abs(elemLeft - left) < errorRange;
            isDrawRight = isDrawRight || Math.abs(elemRight - right) < errorRange;
            isDrawTop = isDrawTop || Math.abs(elemTop - top) < errorRange;
            isDrawBottom = isDrawBottom || Math.abs(elemBottom - bottom) < errorRange;
        });
        //console.debug('center-x: %s, center-y: %s, left: %s, right: %s, top: %s, bottom: %s', isDrawCenterX, isDrawCenterY, isDrawLeft, isDrawRight, isDrawTop, isDrawBottom);

        const drawingBoard = document.querySelector('.alice-process-drawing-board'),
              gTransform = d3.zoomTransform(d3.select('g.guides-container').node());
        if (isDrawCenterX) {
            svg.select('#guides-center-x')
                .style('stroke-width', 1)
                .attr('x1', elementBbox.cx)
                .attr('x2', elementBbox.cx)
                .attr('y1', -gTransform.y)
                .attr('y2', drawingBoard.offsetHeight - gTransform.y);
        } else {
            svg.select('#guides-center-x').style('stroke-width', 0);
        }
        if (isDrawCenterY) {
            svg.select('#guides-center-y')
                .style('stroke-width', 1)
                .attr('x1', -gTransform.x)
                .attr('x2', drawingBoard.offsetWidth - gTransform.x)
                .attr('y1', elementBbox.cy)
                .attr('y2', elementBbox.cy);
        } else {
            svg.select('#guides-center-y').style('stroke-width', 0);
        }
        if (isDrawLeft) {
            svg.select('#guides-left')
                .style('stroke-width', 1)
                .attr('x1', elemLeft)
                .attr('x2', elemLeft)
                .attr('y1', -gTransform.y)
                .attr('y2', drawingBoard.offsetHeight - gTransform.y);
        } else {
            svg.select('#guides-left').style('stroke-width', 0);
        }
        if (isDrawRight) {
            svg.select('#guides-right')
                .style('stroke-width', 1)
                .attr('x1', elemRight)
                .attr('x2', elemRight)
                .attr('y1', -gTransform.y)
                .attr('y2', drawingBoard.offsetHeight - gTransform.y);
        } else {
            svg.select('#guides-right').style('stroke-width', 0);
        }
        if (isDrawTop) {
            svg.select('#guides-top')
                .style('stroke-width', 1)
                .attr('x1', -gTransform.x)
                .attr('x2', drawingBoard.offsetWidth - gTransform.x)
                .attr('y1', elemTop)
                .attr('y2', elemTop);
        } else {
            svg.select('#guides-top').style('stroke-width', 0);
        }
        if (isDrawBottom) {
            svg.select('#guides-bottom')
                .style('stroke-width', 1)
                .attr('x1', -gTransform.x)
                .attr('x2', drawingBoard.offsetWidth - gTransform.x)
                .attr('y1', elemBottom)
                .attr('y2', elemBottom);
        } else {
            svg.select('#guides-bottom').style('stroke-width', 0);
        }
    }

    /**
     * Resizable rectangle element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @param type element type
     * @param width element width
     * @param height element height
     * @constructor
     */
    function RectResizableElement(x, y, type, width, height) {
        const self = this;
        self.width = width ? width : 120;
        self.height = height ? height : 80;
        self.radius = 8;
        const minWidth = 80, minHeight = 60;
        const calcX = x - (self.width / 2),
              calcY = y - (self.height / 2),
              typeImageSize = 20;

        const drag = d3.drag()
            .on('start', elementMouseEventHandler.mousedown)
            .on('drag', function() {
                if (isDrawConnector) {
                    elementMouseEventHandler.mousedrag();
                } else {
                    dragElement = self.nodeElement;
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    const mouseX = snapToGrid(d3.event.dx),
                          mouseY = snapToGrid(d3.event.dy);
                    for (let i = 0, len = self.rectData.length; i < len; i++) {
                        self.rectData[i].x += mouseX;
                        self.rectData[i].y += mouseY;
                    }
                    drawGuides(self.nodeElement);
                    updateRect();
                }
            })
            .on('end', elementMouseEventHandler.mouseup);
        let elementContainer = elementsContainer.append('g').attr('class', 'element');
        if (type === 'group') {
            elementContainer = groupArtifactContainer.append('g').attr('class', 'element');
        }
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

        if (type !== 'group') {
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
                .attr('r', displayOptions.pointerRadius)
                .style('opacity', 0)
                .on('mouseover', function() { self['pointElement' + (i + 1)].style('cursor', cursor); })
                .on('mouseout', function() { self['pointElement' + (i + 1)].style('cursor', 'default'); })
                .call(d3.drag()
                    .on('start', function() {
                        svg.selectAll('.alice-tooltip').remove();
                    })
                    .on('drag', function() {
                        if (selectedElement && selectedElement.node().id === self.nodeElement.node().id) {
                            const mouseX = snapToGrid(d3.event.dx),
                                  mouseY = snapToGrid(d3.event.dy);
                            const rectData = self.rectData;
                            switch (i + 1) {
                                case 1:
                                    if (rectData[1].x - (rectData[0].x + mouseX) >= minWidth) {
                                        rectData[0].x += mouseX;
                                    }
                                    if (rectData[1].y - (rectData[0].y + mouseY) >= minHeight) {
                                        rectData[0].y += mouseY;
                                    }
                                    break;
                                case 2:
                                    if ((rectData[1].x + mouseX) - rectData[0].x >= minWidth) {
                                        rectData[1].x += mouseX;
                                    }
                                    if ((rectData[1].y + mouseY) - rectData[0].y >= minHeight) {
                                        rectData[1].y += mouseY;
                                    }
                                    break;
                                case 3:
                                    if ((rectData[1].x + mouseX) - rectData[0].x >= minWidth) {
                                        rectData[1].x += mouseX;
                                    }
                                    if (rectData[1].y - (rectData[0].y + mouseY) >= minHeight) {
                                        rectData[0].y += mouseY;
                                    }
                                    break;
                                case 4:
                                    if (rectData[1].x - (rectData[0].x + mouseX) >= minWidth) {
                                        rectData[0].x += mouseX;
                                    }
                                    if ((rectData[1].y + mouseY) - rectData[0].y >= minHeight) {
                                        rectData[1].y += mouseY;
                                    }
                                    break;
                            }
                            changeTextToElement(self.nodeElement.node().id);
                            updateRect();
                        }
                    })
                    .on('end', function() {
                        AliceProcessEditor.setElementMenu(self.nodeElement);
                        AliceProcessEditor.changeDisplayValue(self.nodeElement.node().id);
                    })
                );
        });

        /**
         * element 위치, 크기 등 update.
         */
        function updateRect() {
            const rectData = self.rectData;

            let updateX = rectData[0].x,
                updateY = rectData[0].y,
                updateWidth = rectData[1].x - rectData[0].x,
                updateHeight = rectData[1].y - rectData[0].y;
            self.nodeElement
                .attr('x', updateX)
                .attr('y', updateY)
                .attr('width', updateWidth)
                .attr('height', updateHeight);

            if (self.nodeElement.classed('task')) {
                self.typeElement
                    .attr('x', updateX + (typeImageSize / 2))
                    .attr('y', updateY + (typeImageSize / 2));
            } else if (self.nodeElement.classed('subprocess')) {
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
        this.base(x, y, 'task', width, height);
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
        this.base(x, y, 'subprocess', width, height);
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
        const radius = 20, typeImageSize = 20;

        const drag = d3.drag()
            .on('start', elementMouseEventHandler.mousedown)
            .on('drag', function() {
                if (isDrawConnector) {
                    elementMouseEventHandler.mousedrag();
                } else {
                    dragElement = self.nodeElement;
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    const mouseX = snapToGrid(d3.event.x),
                          mouseY = snapToGrid(d3.event.y);
                    self.nodeElement
                        .attr('cx', mouseX)
                        .attr('cy', mouseY);
                    self.typeElement
                        .attr('x', mouseX - (typeImageSize / 2))
                        .attr('y', mouseY - (typeImageSize / 2));
                    self.textElement
                        .attr('x', mouseX)
                        .attr('y', mouseY + (radius * 2));
                    drawGuides(self.nodeElement);
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
        self.textElement = elementContainer.append('text')
            .attr('x', x)
            .attr('y', y + (radius * 2))
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
        const size = 40, typeImageSize = 20;

        const drag = d3.drag()
            .on('start', elementMouseEventHandler.mousedown)
            .on('drag', function() {
                if (isDrawConnector) {
                    elementMouseEventHandler.mousedrag();
                } else {
                    dragElement = self.nodeElement;
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    const mouseX = snapToGrid(d3.event.x),
                          mouseY = snapToGrid(d3.event.y);
                    self.nodeElement
                        .attr('x', mouseX - (size / 2))
                        .attr('y', mouseY - (size / 2))
                        .attr('transform', 'rotate(45, ' + mouseX + ', ' + mouseY + ')');
                    self.typeElement
                        .attr('x', mouseX - (typeImageSize / 2))
                        .attr('y', mouseY - (typeImageSize / 2));
                    self.textElement
                        .attr('x', mouseX)
                        .attr('y', mouseY + size + 10);
                    drawGuides(self.nodeElement);
                    drawConnectors();
                }
            })
            .on('end', elementMouseEventHandler.mouseup);

        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        const defaultType = AliceProcessEditor.getElementDefaultType('gateway');

        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', size)
            .attr('height', size)
            .attr('x', x - (size / 2))
            .attr('y', y - (size / 2))
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
        self.textElement = elementContainer.append('text')
            .attr('x', x)
            .attr('y', y + size + 10)
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
        this.base(x, y, 'group', width, height);
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
                    dragElement = self.nodeElement;
                    svg.selectAll('.alice-tooltip').remove();
                    d3.select(self.nodeElement.node().parentNode).raise();
                    const mouseX = snapToGrid(d3.event.x),
                          mouseY = snapToGrid(d3.event.y);
                    self.nodeElement
                        .attr('x', mouseX - (width / 2))
                        .attr('y', mouseY - (height / 2));
                    self.textElement
                        .attr('x', mouseX)
                        .attr('y', mouseY);
                    drawGuides(self.nodeElement);
                    drawConnectors();
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
                let x = snapToGrid(d3.event.pageX - svgOffset.left - window.pageXOffset - gTransform.x),
                    y = snapToGrid(d3.event.pageY - svgOffset.top - window.pageYOffset - gTransform.y);
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
                    AliceProcessEditor.addElementProperty(node.nodeElement);
                }
            });
    }

    /**
     * element 에 Text 를 추가한다.
     *
     * @param elementId element ID
     * @param text 추가할 text
     */
    function changeTextToElement(elementId, text) {
        const elementNode = document.getElementById(elementId);
        if (typeof text === 'undefined') {
            const elements = AliceProcessEditor.data.elements;
            elements.forEach(function(elem) {
                if (elem.id === elementId) { text = elem.data.name; }
            });
        }

        if (d3.select(elementNode).classed('connector')) {
            d3.select(elementNode.parentNode).select('text').text(text);
        } else {
            const textElement = d3.select(elementNode.parentNode).select('text');
            if (textElement.node()) {
                textElement.text(text);

                // wrap text
                const element = d3.select(elementNode);
                if (text.length > 0 && element.classed('resizable')) {
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
        const drawingBoard = document.querySelector('.alice-process-drawing-board');

        // add svg and svg event
        svg = d3.select('.alice-process-drawing-board').append('svg')
            .attr('width', drawingBoard.offsetWidth)
            .attr('height', drawingBoard.offsetHeight)
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
            let drawingBoardWidth = drawingBoard.offsetWidth,
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
        };
        window.onresize = setDrawingBoardGrid;
        window.onkeydown = function(e) {
            let keyCode = e.keyCode ? e.keyCode : e.which;
            isMovableDrawingboard = keyCode === 32;
            d3.select(document.getElementById('icon-move-drawingboard')).classed('selected', isMovableDrawingboard);
        };
        window.onkeyup = function() {
            if (isMovableDrawingboard) {
                isMovableDrawingboard = false;
            }
            d3.select(document.getElementById('icon-move-drawingboard')).classed('selected', isMovableDrawingboard);
        };
        setDrawingBoardGrid();

        // add zoom
        const zoom = d3.zoom()
            .on('start', function() {
                if (!isMovableDrawingboard) {
                    dismovableDrawingboard();
                } else {
                    svg.style('cursor', 'grabbing');
                    const nodeTopArray = [],
                          nodeRightArray = [],
                          nodeBottomArray = [],
                          nodeLeftArray = [];
                    const nodes = svg.selectAll('.node').nodes();
                    nodes.forEach(function(node){
                        let nodeBBox = AliceProcessEditor.utils.getBoundingBoxCenter(d3.select(node));
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
                }
            })
            .on('zoom', function() {
                if (!isMovableDrawingboard) {
                    dismovableDrawingboard();
                    svg.style('cursor', 'default');
                }
                horizontalGrid
                    .call(horizontalAxis.scale(d3.event.transform.rescaleY(horizontalScale)));
                verticalGrid
                    .call(verticalAxis.scale(d3.event.transform.rescaleX(verticalScale)));
                svg.select('g.element-container')
                    .attr('transform', d3.event.transform);
                svg.select('g.connector-container')
                    .attr('transform', d3.event.transform);
                svg.select('g.group-artifact-container')
                    .attr('transform', d3.event.transform);
                svg.select('g.guides-container')
                    .attr('transform', d3.event.transform);
            })
            .on('end', function() {
                svg.style('cursor', 'default');
            });

        svg
            .call(zoom)
            .on('wheel.zoom', null)
            .on('dblclick.zoom', null);

        /**
         * disable the movable function of the drawing board.
         */
        function dismovableDrawingboard() {
            const drawingBoard = document.querySelector('.alice-process-drawing-board'),
                  gTransform = d3.zoomTransform(d3.select('g.element-container').node());
            zoom.translateExtent([
                [-gTransform.x, -gTransform.y],
                [drawingBoard.offsetWidth - gTransform.x, drawingBoard.offsetHeight - gTransform.y]
            ]);
            svg.style('cursor', 'default');
        }

        groupArtifactContainer = svg.append('g').attr('class', 'group-artifact-container');
        const connectorContainer = svg.append('g').attr('class', 'connector-container');
        connectors = connectorContainer.selectAll('g.connector');
        elementsContainer = svg.append('g').attr('class', 'element-container');
        const guidesContainer = svg.append('g').attr('class', 'guides-container');
        guidesContainer.selectAll('line')
            .data(['center-x','center-y','left','right','top','bottom'])
            .enter()
            .append('line')
            .attr('id', function(d) { return 'guides-' + d; })
            .attr('class', 'guides-line');

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

        document.getElementById('icon-move-drawingboard').addEventListener('click', function() {
            let isSelected = d3.select(this).classed('selected');
            d3.select(this).classed('selected', !isSelected);
            isMovableDrawingboard = !isSelected;
        });
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
                    node.nodeElement.style('stroke', element.data['line-color'])
                        .style('fill', element.data['background-color']);
                    if (element.data['background-color'] === '') {
                        node.nodeElement.style('fill-opacity', 0);
                    } else {
                        node.nodeElement.style('fill-opacity', 0.5);
                    }
                } else if (element.type === 'annotationArtifact') {
                    node = new AnnotationElement(x, y);
                }
                break;
            default:
                break;
        }

        if (node) {
            const nodeId = node.nodeElement.attr('id');
            changeTextToElement(nodeId, element.data.name);
        }

        return node;
    }

    /**
     * Draw a element with the loaded information.
     *
     * @param elementList editor 에 추가할 element 목록
     */
    function drawProcess(elementList) {
        // add element
        elementList.forEach(function(element) {
            if (element.type === 'arrowConnector') {
                return;
            }
            let node = addElement(element);
            if (node) {
                node.nodeElement.attr('id', element.id);
            }
        });

        // add connector
        elementList.forEach(function(element) {
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
                let linkData = {id: nodeId, sourceId: source.id, targetId: target.id, isDefault: element.data['is-default']};
                if (element.display) {
                    if (typeof element.display['mid-point'] !== 'undefined') {
                        linkData.midPoint = element.display['mid-point'];
                    }
                    if (typeof element.display['source-point'] !== 'undefined') {
                        linkData.sourcePoint = element.display['source-point'];
                    }
                    if (typeof element.display['target-point'] !== 'undefined') {
                        linkData.targetPoint = element.display['target-point'];
                    }
                    if (typeof element.display['text-point'] !== 'undefined') {
                        linkData.textPoint = element.display['text-point'];
                    }
                }
                elements.links.push(linkData);
            }
        });
        setConnectors();
        AliceProcessEditor.initUtil();
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
    }

    exports.init = init;
    exports.elements = elements;
    exports.drawProcess = drawProcess;
    exports.addElement = addElement;
    exports.changeTextToElement = changeTextToElement;
    exports.removeElementSelected = removeElementSelected;
    exports.setConnectors = setConnectors;
    Object.defineProperty(exports, '__esModule', {value: true});
})));