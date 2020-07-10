(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
        typeof define === 'function' && define.amd ? define(['exports'], factory) :
            (factory((global.aliceProcessEditor = global.aliceProcessEditor || {})));
}(this, (function (exports) {
    'use strict';

    const displayOptions = {
        translateLimit: 1000, // drawing board limit.
        gridInterval: 10,     // value of grid interval.
        pointerRadius: 4,
        connectorRadius: 4,
        connectorLabelPos: 20,
        gatewaySize: 28
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
        isMoveConnector = false,
        isEndMoveConnector = false,
        isMovableDrawingboard = false;

    let isView = true;            //view 모드 여부

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
     * @param p current point
     * @return {number} snap point
     */
    function snapToGrid(p) {
        const r = displayOptions.gridInterval;
        return Math.round(p / r) * r;
    }

    /**
     * 선택된 element 를 해제 한다.
     */
    function removeElementSelected() {
        selectedElement = null;
        svg.selectAll('.node').nodes().forEach(function(node) {
            setDeselectedElement(d3.select(node));
        });
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
                d3.event.preventDefault();
                if (isDrawConnector) {
                    return;
                }

                removeElementSelected();
                resetMouseVars();

                // select link
                let selectedLink = d3.select(this).classed('selected', true);
                selectedElement = null;
                d3.select(document.getElementById(d.id + '_midPoint')).style('opacity', 1).style('cursor', 'move');
                d3.select(document.getElementById(d.id + '_startPoint')).style('opacity', 1).style('cursor', 'move');
                d3.select(document.getElementById(d.id + '_endPoint')).style('opacity', 1).style('cursor', 'move');

                setConnectors();
                aliceProcessEditor.setElementMenu(selectedLink);
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
                d3.event.preventDefault();
                if (isDrawConnector) {
                    return;
                }
                const event = document.createEvent('MouseEvent');
                event.initMouseEvent('mousedown', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                d3.select(document.getElementById(d.id)).node().dispatchEvent(event);
            },
            moveDragStart: function(d, target) {
                svg.selectAll('.alice-tooltip').remove();
                d3.select(target).style('opacity', 0);
                d3.select(target.parentNode).selectAll('.pointer:not(.move)').style('opacity', 0);
            },
            moveDragEnd: function(d, target) {
                dragLine.classed('hidden', true);
                d3.select(document.getElementById(d.id)).classed('hidden', false);
                d3.select(target).style('opacity', 1);
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
                const elements = aliceProcessEditor.data.elements;
                elements.forEach(function(elem) {
                    if (elem.id === d.id) { name = elem.name; }
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
                        aliceProcessEditor.changeDisplayValue(d.id);
                    })
            );

        enter.append('circle')
            .attr('class', 'pointer move')
            .attr('id', function(d) { return d.id + '_startPoint'; })
            .attr('r', displayOptions.pointerRadius)
            .style('opacity', 0)
            .call(d3.drag()
                .on('start', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        connectorMouseEventHandler.moveDragStart(d, this);
                        elements.links.forEach(function (l) {
                            if (l.id === d.id) {
                                mousedownElement = d3.select(document.getElementById(d.targetId));
                                l.movable = true;
                            }
                        });
                        isMoveConnector = true;
                        isEndMoveConnector = true;
                    }
                })
                .on('drag', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        d3.select(document.getElementById(d.id)).classed('hidden', true);
                        let targetElement = d3.select(document.getElementById(d.id + '_endPoint'));
                        const gTransform = d3.zoomTransform(d3.select('g.element-container').node());
                        dragLine
                            .style('marker-end', 'url(#end-arrow)')
                            .classed('hidden', false)
                            .attr('d', `M${d3.event.x + gTransform.x},${d3.event.y + gTransform.y} L${Number(targetElement.attr('cx')) + gTransform.x},${Number(targetElement.attr('cy')) + gTransform.y}`);
                    }
                })
                .on('end', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        connectorMouseEventHandler.moveDragEnd(d, this);

                        if (mouseoverElement && mousedownElement.node().id !== mouseoverElement.node().id) {
                            setDeselectedElement(mouseoverElement);
                            if (checkAvailableLink(mouseoverElement, mousedownElement)) {
                                elements.links.forEach(function(l) {
                                    delete l.movable;
                                    if (l.id === d.id && l.sourceId !== mouseoverElement.node().id) {
                                        l.sourceId = mouseoverElement.node().id;
                                        delete l.midPoint;
                                        delete l.sourcePoint;
                                        delete l.targetPoint;
                                        delete l.textPoint;
                                        aliceProcessEditor.changeDisplayValue(d.id);
                                    }
                                });
                                selectedElement = null;
                            }
                        }
                        resetMouseVars();
                        isMoveConnector = false;
                        isEndMoveConnector = false;

                        const event = document.createEvent('MouseEvent');
                        event.initMouseEvent('mousedown', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                        d3.select(document.getElementById(d.id)).node().dispatchEvent(event);
                    }
                })
            );
        enter.append('circle')
            .attr('class', 'pointer move')
            .attr('id', function(d) { return d.id + '_endPoint'; })
            .attr('r', displayOptions.pointerRadius)
            .style('opacity', 0)
            .call(d3.drag()
                .on('start', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        connectorMouseEventHandler.moveDragStart(d, this);
                        elements.links.forEach(function(l) {
                            if (l.id === d.id) {
                                mousedownElement = d3.select(document.getElementById(d.sourceId));
                                l.movable = true;
                            }
                        });
                        isMoveConnector = true;
                    }
                })
                .on('drag', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        d3.select(document.getElementById(d.id)).classed('hidden', true);
                        let sourceElement = d3.select(document.getElementById(d.id + '_startPoint'));
                        const gTransform = d3.zoomTransform(d3.select('g.element-container').node());
                        dragLine
                            .style('marker-end', 'url(#end-arrow)')
                            .classed('hidden', false)
                            .attr('d', `M${Number(sourceElement.attr('cx')) + gTransform.x},${Number(sourceElement.attr('cy')) + gTransform.y} L${d3.event.x + gTransform.x},${d3.event.y + gTransform.y}`);
                    }
                })
                .on('end', function(d) {
                    if (d3.select(document.getElementById(d.id)).classed('selected')) {
                        connectorMouseEventHandler.moveDragEnd(d, this);

                        if (mouseoverElement && mousedownElement.node().id !== mouseoverElement.node().id) {
                            setDeselectedElement(mouseoverElement);
                            if (checkAvailableLink(mousedownElement, mouseoverElement)) {
                                elements.links.forEach(function (l) {
                                    delete l.movable;
                                    if (l.id === d.id && l.targetId !== mouseoverElement.node().id) {
                                        l.targetId = mouseoverElement.node().id;
                                        delete l.midPoint;
                                        delete l.sourcePoint;
                                        delete l.targetPoint;
                                        delete l.textPoint;
                                        aliceProcessEditor.changeDisplayValue(d.id);
                                    }
                                });
                                selectedElement = null;
                            }
                        }
                        resetMouseVars();
                        isMoveConnector = false;

                        const event = document.createEvent('MouseEvent');
                        event.initMouseEvent('mousedown', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                        d3.select(document.getElementById(d.id)).node().dispatchEvent(event);
                    }
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
                        svg.selectAll('.node').nodes().forEach(function(node) {
                            if (checkDuplicatePosition(node, d.midPoint)) {
                                delete d.midPoint;
                                delete d.sourcePoint;
                                delete d.targetPoint;
                                drawConnectors();
                            }
                        });
                        aliceProcessEditor.setElementMenu(d3.select(document.getElementById(d.id)));
                        aliceProcessEditor.changeDisplayValue(d.id);
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
                        svg.selectAll('.node').nodes().forEach(function(node) {
                            if (checkDuplicatePosition(node, d.sourcePoint)) {
                                delete d.sourcePoint;
                                drawConnectors();
                            }
                        });
                        aliceProcessEditor.changeDisplayValue(d.id);
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
                        svg.selectAll('.node').nodes().forEach(function(node) {
                            if (checkDuplicatePosition(node, d.targetPoint)) {
                                delete d.targetPoint;
                                drawConnectors();
                            }
                        });
                        aliceProcessEditor.changeDisplayValue(d.id);
                    }
                })
            );

        connectors = connectors.merge(enter);

        // draw links
        drawConnectors();

        enter.select('path.connector').call(function(d) { aliceProcessEditor.addElementProperty(d); });
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
     * 중복 체크.
     *
     * @param node 엘리먼트 node
     * @param point 포인트
     * @return {boolean} true: duplicate, false: not duplicate
     */
    function checkDuplicatePosition(node, point) {
        if (!node || !point || d3.select(node).classed('artifact')) { return false; }
        let bbox = aliceProcessEditor.utils.getBoundingBoxCenter(d3.select(node));
        return bbox.x <= point[0] && (bbox.x + bbox.width) >= point[0] &&
            bbox.y <= point[1] && (bbox.y + bbox.height) >= point[1];
    }

    /**
     * draw connector.
     */
    function drawConnectors() {
        /**
         * 종료좌표에서 일정 거리 떨어진 좌표를 구한다.
         *
         * @param sourceCoords 시작좌표
         * @param targetCoords 종료좌표
         * @param distance 거리
         * @return {[*, *]} 좌표
         */
        const calcDistancePointCoordinate = function(sourceCoords, targetCoords, distance) {
            if (typeof distance === 'undefined') {
                distance = displayOptions.connectorRadius;
            }
            let dx = Number(sourceCoords[0]) - Number(targetCoords[0]),
                dy = Number(sourceCoords[1]) - Number(targetCoords[1]),
                calcDist = Math.sqrt(dx * dx + dy * dy),
                x = Number(targetCoords[0]) + dx * distance / calcDist,
                y = Number(targetCoords[1]) + dy * distance / calcDist;
            if (calcDist === 0) {
                x = Number(targetCoords[0]);
                y = Number(targetCoords[1]);
            }
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
            let distA = aliceProcessEditor.utils.calcDist(endPoint, midPoint);
            let distB = aliceProcessEditor.utils.calcDist(midPoint, startPoint);
            let distC = aliceProcessEditor.utils.calcDist(startPoint, endPoint);
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
            const targetBBox = aliceProcessEditor.utils.getBoundingBoxCenter(target);
            const sourceBBox = aliceProcessEditor.utils.getBoundingBoxCenter(source);

            let targetWidth = targetBBox.width,
                targetHeight = targetBBox.height,
                sourceWidth = sourceBBox.width,
                sourceHeight = sourceBBox.height;
            if (target.classed('gateway') || source.classed('gateway')) {
                let gatewayDist = aliceProcessEditor.utils.calcDist([0, 0], [displayOptions.gatewaySize, displayOptions.gatewaySize]);
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
            const sourcePoint = d3.select(document.getElementById(d.id + '_sourcePoint')),
                  targetPoint = d3.select(document.getElementById(d.id + '_targetPoint')),
                  startPoint = d3.select(document.getElementById(d.id + '_startPoint')),
                  endPoint = d3.select(document.getElementById(d.id + '_endPoint'));
            if (typeof d.midPoint !== 'undefined') {
                midPoint.attr('cx', d.midPoint[0]).attr('cy', d.midPoint[1]);
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
                startPoint.attr('cx', bestLine1[0][0]).attr('cy', bestLine1[0][1]);
                endPoint.attr('cx', bestLine2[1][0]).attr('cy', bestLine2[1][1]);
            } else {
                let bestLine = getBestLine(sourceBBox, targetBBox, sourcePointArray, targetPointArray);
                linePath = ['M', bestLine[0], 'L', bestLine[1]].join(' ');
                let midPointCoords = getMidPointCoords(bestLine);
                midPoint.attr('cx', midPointCoords[0]).attr('cy', midPointCoords[1]);
                sourcePoint.style('opacity', 0).style('cursor', 'default');
                targetPoint.style('opacity', 0).style('cursor', 'default');
                startPoint.attr('cx', bestLine[0][0]).attr('cy', bestLine[0][1]);
                endPoint.attr('cx', bestLine[1][0]).attr('cy', bestLine[1][1]);
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
                .attr('x', d.textPoint ? Number(d.textPoint[0]) + coords[0] : coords[0])
                .attr('y', d.textPoint ? Number(d.textPoint[1]) + coords[1] : coords[1])
                .attr('dy', '0.188rem')
                .style('text-anchor', Math.abs(angleDeg) > 90 ? 'end' : 'start');

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
            if (!(isDrawConnector || isMoveConnector) || !mousedownElement || elem.node().id === mousedownElement.node().id) {
                return;
            }
            mouseoverElement = elem;

            let source = mousedownElement,
                target = mouseoverElement;
            if (isMoveConnector && isEndMoveConnector) {
                source = mouseoverElement;
                target = mousedownElement;
            }
            elem.classed('selected', checkAvailableLink(source, target));
        },
        mouseout: function() {
            const elemContainer = d3.select(this.parentNode);
            const elem = elemContainer.select('.node');
            if (!(isDrawConnector || isMoveConnector) || !mousedownElement || elem.node().id === mousedownElement.node().id) {
                if (!dragElement) {
                    elemContainer.style('cursor', 'default');
                }
                return;
            }
            mouseoverElement = null;
            setDeselectedElement(elem);
        },
        mousedown: function() {
            const elemContainer = d3.select(this.parentNode);
            const elem = elemContainer.select('.node');

            if (isDrawConnector) {
                resetMouseVars();
                removeElementSelected();
                mousedownElement = elem;
                selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;

                if (!elem.classed('group')) {
                    const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(mousedownElement),
                        gTransform = d3.zoomTransform(d3.select('g.element-container').node()),
                        centerX = bbox.cx + gTransform.x,
                        centerY = bbox.cy + gTransform.y;
                    dragLine
                        .style('marker-end', 'url(#end-arrow)')
                        .classed('hidden', false)
                        .attr('d', `M${centerX},${centerY} L${centerX},${centerY}`);
                }
            } else {
                let selectedNodes = d3.selectAll('.node.selected').nodes();
                let isSelectedElem = false;
                selectedNodes.forEach(function(node) {
                    if (node.id === elem.node().id) {
                        isSelectedElem = true;
                    }
                });
                elemContainer.style('cursor', 'move');
                if (!isSelectedElem) {
                    if (d3.event.sourceEvent.ctrlKey && selectedNodes.length > 0) {
                        setSelectedElement(elem);

                        mousedownElement = null;
                        selectedElement = null;
                        svg.selectAll('.pointer').style('opacity', 0).style('cursor', 'default');
                        svg.selectAll('.alice-tooltip').remove();
                        aliceProcessEditor.setElementMenu();
                    } else {
                        removeElementSelected();
                        mousedownElement = elem;
                        selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;
                        setSelectedElement(selectedElement);

                        if (elem.node().getAttribute('class').match(/\bresizable\b/)) {
                            d3.select(selectedElement.node().parentNode).selectAll('.pointer').nodes().forEach(function(elem) {
                                elem.style.opacity = '1';
                            });
                        }
                        aliceProcessEditor.setElementMenu(elem);
                    }
                } else if (isSelectedElem && d3.event.sourceEvent.ctrlKey) {
                    setDeselectedElement(elem);

                    selectedNodes = d3.selectAll('.node.selected').nodes();
                    if (selectedNodes.length === 1) {
                        mousedownElement = d3.select(selectedNodes[0]);
                        selectedElement = (mousedownElement === selectedElement) ? null : mousedownElement;
                        aliceProcessEditor.setElementMenu(selectedElement);
                        if (selectedElement.classed('resizable')) {
                            d3.select(selectedElement.node().parentNode).selectAll('.pointer').style('opacity', 1);
                        }
                    } else if (selectedNodes.length === 0) {
                        svg.selectAll('.alice-tooltip').remove();
                    }
                    elemContainer.style('cursor', 'default');
                }
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
                    setDeselectedElement(mouseoverElement);
                    if (checkAvailableLink(mousedownElement, mouseoverElement)) {
                        elements.links.push({id: workflowUtil.generateUUID(), sourceId: mousedownElement.node().id, targetId: mouseoverElement.node().id, isDefault: 'N'});
                        selectedElement = null;
                        setConnectors();
                    }
                }
                resetMouseVars();
            } else {
                let histories = [];
                const selectedNodes = d3.selectAll('.node.selected').nodes();
                selectedNodes.forEach(function(node) {
                    let targetElement = d3.select(node);
                    const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(targetElement);
                    dragged(targetElement, snapToGrid(bbox.cx) - bbox.cx, snapToGrid(bbox.cy) - bbox.cy);

                    let history = aliceProcessEditor.changeDisplayValue(node.id, false);
                    histories.push(history);
                });

                elements.links.forEach(function(l) {
                    let isExistSource = false,
                        isExistTarget = false,
                        isDeletedPoint = false;
                    selectedNodes.forEach(function(node) {
                        if (l.sourceId === node.id) {
                            isExistSource = true;
                        } else if (l.targetId === node.id) {
                            isExistTarget = true;
                        }
                        if (isExistSource || isExistTarget) {
                            if (typeof l.midPoint !== 'undefined' && checkDuplicatePosition(node, l.midPoint)) {
                                delete l.midPoint;
                                delete l.sourcePoint;
                                delete l.targetPoint;
                                isDeletedPoint = true;
                                drawConnectors();
                            }
                            if (typeof l.sourcePoint !== 'undefined' && checkDuplicatePosition(node, l.sourcePoint)) {
                                delete l.sourcePoint;
                                isDeletedPoint = true;
                                drawConnectors();
                            }
                            if (typeof l.targetPoint !== 'undefined' && checkDuplicatePosition(node, l.targetPoint)) {
                                delete l.targetPoint;
                                isDeletedPoint = true;
                                drawConnectors();
                            }
                        }
                    });

                    if ((isExistSource && isExistTarget) || isDeletedPoint) {
                        let history = aliceProcessEditor.changeDisplayValue(l.id, false);
                        histories.push(history);
                    }
                });
                aliceProcessEditor.utils.history.saveHistory(histories);

                dragElement = null;
                elemContainer.style('cursor', 'pointer');

                if (svg.select('.alice-tooltip').node() === null && selectedNodes.length === 1) {
                    aliceProcessEditor.setActionTooltipItem(elem);
                }
                svg.selectAll('line.guides-line').style('stroke-width', 0);
            }
        },
        mousedrag: function() {
            if (mousedownElement.classed('commonEnd')) {
                return false;
            }
            const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(mousedownElement),
                gTransform = d3.zoomTransform(d3.select('g.element-container').node()),
                centerX = bbox.cx + gTransform.x,
                centerY = bbox.cy + gTransform.y;
            dragLine.attr('d', `M${centerX},${centerY} L${d3.event.x + gTransform.x},${d3.event.y + gTransform.y}`);
        }
    };

    /**
     * 엘리먼트ID로 저장된 엘리먼트 데이터를 조회하여 반환한다.
     *
     * @param id 엘리먼트ID
     * @return {null} 엘리먼트 데이터
     */
    function getElementDataById(id) {
        const elementDataList = aliceProcessEditor.data.elements.filter(function(e) { return e.id === id; });
        let elementData = null;
        if (elementDataList) {
            elementData = elementDataList[0];
        }
        return elementData;
    }

    /**
     * 엘리먼트 선택 효과 추가.
     *
     * @param elem 엘리먼트 object
     */
    function setSelectedElement(elem) {
        elem.classed('selected', true);
        d3.select(elem.node().parentNode).classed('selected', true);

        let elementData = getElementDataById(elem.node().id);
        if (elementData) {
            let category = aliceProcessEditor.getElementCategory(elementData.type);
            let type = d3.select(elem.node().parentNode).select('.element-type.' + category + '.' + elementData.type);
            if (type) {
                type.style('fill', 'url(#' + category + '-' + elementData.type + '-element-selected)');
            }
        }
    }

    /**
     * 엘리먼트 선택해제.
     *
     * @param elem 엘리먼트 object
     */
    function setDeselectedElement(elem) {
        elem.classed('selected', false);
        d3.select(elem.node().parentNode).classed('selected', false);

        let elementData = getElementDataById(elem.node().id);
        if (elementData) {
            let category = aliceProcessEditor.getElementCategory(elementData.type);
            let type = d3.select(elem.node().parentNode).select('.element-type.' + category + '.' + elementData.type);
            if (type) {
                type.style('fill', 'url(#' + category + '-' + elementData.type + '-element)');
            }
        }
    }

    /**
     * connector 연결 가능여부 체크하여 리턴한다.
     *
     * @param source
     * @param target
     * @return {boolean} 연결 가능 여부
     */
    function checkAvailableLink(source, target) {
        let availableLink = true;
        elements.links.forEach(function(l) {
            if (!l.movable) {
                // it's not a gateway, but several starts
                if (!source.classed('gateway') && l.sourceId === source.node().id) {
                    availableLink = false;
                }
                // cannot link to each other
                if ((l.sourceId === source.node().id && l.targetId === target.node().id) ||
                    (l.sourceId === target.node().id && l.targetId === source.node().id)) {
                    availableLink = false;
                }
            }
        });
        // common start cannot be a target.
        if (target.classed('commonStart')) {
            availableLink = false;
        }
        // common end/message end cannot be a source.
        if (source.classed('commonEnd')) {
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
        const elementBbox = aliceProcessEditor.utils.getBoundingBoxCenter(elem),
              gatewayDist = aliceProcessEditor.utils.calcDist([0, 0], [displayOptions.gatewaySize, displayOptions.gatewaySize]);
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

        svg.selectAll('.node:not(.selected)').nodes().forEach(function(node) {
            const element = getElementDataById(node.id);
            if (element) {
                let left = element.display['position-x'] - (element.display.width / 2),
                    right = element.display['position-x'] + (element.display.width / 2),
                    top = element.display['position-y'] - (element.display.height / 2),
                    bottom = element.display['position-y'] + (element.display.height / 2);
                if (element.type.indexOf('Gateway') > -1) {
                    left = element.display['position-x'] - (gatewayDist / 2);
                    right = element.display['position-x'] + (gatewayDist / 2);
                    top = element.display['position-y'] - (gatewayDist / 2);
                    bottom = element.display['position-y'] + (gatewayDist / 2);
                }
                isDrawCenterX = isDrawCenterX || Math.abs(elementBbox.cx - element.display['position-x']) < errorRange;
                isDrawCenterY = isDrawCenterY || Math.abs(elementBbox.cy - element.display['position-y']) < errorRange;
                isDrawLeft = isDrawLeft || Math.abs(elemLeft - left) < errorRange;
                isDrawRight = isDrawRight || Math.abs(elemRight - right) < errorRange;
                isDrawTop = isDrawTop || Math.abs(elemTop - top) < errorRange;
                isDrawBottom = isDrawBottom || Math.abs(elemBottom - bottom) < errorRange;
            }
        });

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
     * Drag the action element.
     *
     * @param nodeElement 드래그 대상 element
     * @param dx position x
     * @param dy position y
     */
    function dragged(nodeElement, dx, dy) {
        svg.selectAll('.alice-tooltip').remove();
        const gElement = d3.select(nodeElement.node().parentNode),
              typeElement = gElement.select('.element-type'),
              textElement = gElement.select('text');

        let mouseX = Number(nodeElement.attr('x')) + dx,
            mouseY = Number(nodeElement.attr('y')) + dy;

        if (nodeElement.classed('event')) {
            mouseX = Number(nodeElement.attr('cx')) + dx;
            mouseY = Number(nodeElement.attr('cy')) + dy;
            nodeElement
                .attr('cx', mouseX)
                .attr('cy', mouseY);

        } else {
            nodeElement
                .attr('x', mouseX)
                .attr('y', mouseY);
            textElement
                .attr('x', Number(nodeElement.attr('x')) + (Number(nodeElement.attr('width')) / 2) + (Number(nodeElement.attr('height')) / 2))
                .attr('y', Number(nodeElement.attr('y')) + (Number(nodeElement.attr('height')) / 2));
        }

        if (nodeElement.classed('task') || nodeElement.classed('subprocess')) {
            typeElement
                .attr('x', Number(nodeElement.attr('x')))
                .attr('y', Number(nodeElement.attr('y')));
        } else if (nodeElement.classed('event')) {
            typeElement
                .attr('x', mouseX - (Number(typeElement.attr('width')) / 2))
                .attr('y', mouseY - (Number(typeElement.attr('height')) / 2));
            textElement
                .attr('x', mouseX)
                .attr('y', mouseY + (20 * 2));
        } else if (nodeElement.classed('gateway')) {
            nodeElement.attr('transform', 'rotate(45, ' + (mouseX + (Number(nodeElement.attr('width')) / 2)) + ', ' + (mouseY + (Number(nodeElement.attr('height')) / 2))+ ')')
            typeElement
                .attr('x', mouseX + (Number(nodeElement.attr('width')) / 2) - (Number(typeElement.attr('width')) / 2))
                .attr('y', mouseY + (Number(nodeElement.attr('height')) / 2) - (Number(typeElement.attr('height')) / 2));
            textElement
                .attr('x', mouseX + (Number(nodeElement.attr('width')) / 2))
                .attr('y', mouseY + (Number(nodeElement.attr('height')) / 2) + Number(nodeElement.attr('height')) + 10);
        } else if (nodeElement.classed('group')) {
            textElement
                .attr('x', Number(nodeElement.attr('x')) + (Number(nodeElement.attr('width')) / 2))
                .attr('y', mouseY + 10);
            let rectData = [
                {x: Number(nodeElement.attr('x')), y: Number(nodeElement.attr('y'))},
                {x: Number(nodeElement.attr('x')) + Number(nodeElement.attr('width')), y: Number(nodeElement.attr('y')) + Number(nodeElement.attr('height'))}
            ];
            let pointArray =
                [[rectData[0].x, rectData[0].y], [rectData[1].x, rectData[1].y],
                    [rectData[1].x, rectData[0].y], [rectData[0].x, rectData[1].y]];
            pointArray.forEach(function(point, i) {
                d3.select(gElement.selectAll('circle').nodes()[i])
                    .attr('cx', point[0])
                    .attr('cy', point[1]);
            });
        } else if (nodeElement.classed('annotation')) {
            textElement
                .attr('x', Number(nodeElement.attr('x')) + (Number(nodeElement.attr('width')) / 2))
                .attr('y', mouseY + 15);
            if (textElement.selectAll('tspan').nodes().length > 0) {
                textElement.selectAll('tspan').nodes().forEach(function(tspan) {
                    d3.select(tspan).attr('x', textElement.attr('x'))
                        .attr('y', textElement.attr('y'));
                });
            }
        }

        if (dragElement && dragElement.node().id === nodeElement.node().id) {
            elements.links.forEach(function(l) {
                let isExistSource = false,
                    isExistTarget = false;
                d3.selectAll('.node.selected').each(function() {
                    let nodeId =  d3.select(this).node().id;
                    if (l.sourceId === nodeId) {
                        isExistSource = true;
                    } else if (l.targetId === nodeId) {
                        isExistTarget = true;
                    }
                });
                if (isExistSource && isExistTarget) {
                    if (typeof l.midPoint !== 'undefined') {
                        l.midPoint = [l.midPoint[0] + dx, l.midPoint[1] + dy];
                    }
                    if (typeof l.sourcePoint !== 'undefined') {
                        l.sourcePoint = [l.sourcePoint[0] + dx, l.sourcePoint[1] + dy];
                    }
                    if (typeof l.targetPoint !== 'undefined') {
                        l.targetPoint = [l.targetPoint[0] + dx, l.targetPoint[1] + dy];
                    }
                    if (typeof l.textPoint !== 'undefined') {
                        l.textPoint = [l.textPoint[0] + dx, l.textPoint[1] + dy];
                    }
                }
            });

            gElement.raise();
            drawGuides(dragElement);
            drawConnectors();
        }
    }

    /**
     * element drag event.
     *
     * @type {EventEmitter}
     */
    const drag = d3.drag()
        .filter(function() { return d3.event.button === 0 || d3.event.button === 2; })
        .on('start', elementMouseEventHandler.mousedown)
        .on('drag', function() {
            if (isDrawConnector) {
                elementMouseEventHandler.mousedrag();
            } else {
                if (!dragElement) {
                    const allowedTargetTagNames = ['rect', 'circle', 'text'];
                    if (allowedTargetTagNames.indexOf(d3.event.sourceEvent.target.tagName.toLowerCase()) === -1) {
                        return false;
                    }

                    let targetElement = d3.event.sourceEvent.target.parentNode;
                    if (d3.event.sourceEvent.target.tagName.toLowerCase() === 'tspan') {
                        targetElement = targetElement.parentNode;
                    }
                    dragElement = d3.select(targetElement).select('.node');
                }
                if (dragElement.node().classList && dragElement.classed('selected')) {
                    const dx = d3.event.dx,
                          dy = d3.event.dy;
                    d3.selectAll('.node.selected').each(function() {
                        dragged(d3.select(this), dx, dy);
                    });
                }
            }
        })
        .on('end', elementMouseEventHandler.mouseup);

    /**
     * Task element.
     *
     * @param x drop할 마우스 x좌표
     * @param y drop할 마우스 y좌표
     * @returns {TaskElement}
     * @constructor
     */
    function TaskElement(x, y) {
        const self = this;
        const width = 160, height = 40, radius = 4;
        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        self.defaultType = aliceProcessEditor.getElementDefaultType('task');

        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('rx', radius)
            .attr('ry', radius)
            .attr('class', 'node task ' + self.defaultType)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('rect')
            .attr('class', 'element-type task ' + self.defaultType)
            .attr('width', height)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .style('fill', 'url(#task-' + self.defaultType + '-element)')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('text')
            .attr('x', x + (height / 2))
            .attr('y', y)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        return self;
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
        const self = this;
        const width = 152, height = 40, radius = 4;
        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        self.defaultType = aliceProcessEditor.getElementDefaultType('subprocess');

        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', width)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .attr('rx', radius)
            .attr('ry', radius)
            .attr('class', 'node subprocess ' + self.defaultType)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('rect')
            .attr('class', 'element-type subprocess ' + self.defaultType)
            .attr('width', height)
            .attr('height', height)
            .attr('x', x - (width / 2))
            .attr('y', y - (height / 2))
            .style('fill', 'url(#subprocess-' + self.defaultType + '-element)')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('text')
            .attr('x', x + (height / 2))
            .attr('y', y)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        return self;
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
        const radius = 20, typeImageSize = 21;
        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        self.defaultType = aliceProcessEditor.getElementDefaultType('event');

        self.nodeElement = elementContainer.append('circle')
            .attr('id', workflowUtil.generateUUID())
            .attr('r', radius)
            .attr('cx', x)
            .attr('cy', y)
            .attr('class', 'node event ' + self.defaultType)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('rect')
            .attr('class', 'element-type event')
            .attr('width', typeImageSize)
            .attr('height', typeImageSize)
            .attr('x', x - (typeImageSize / 2))
            .attr('y', y - (typeImageSize / 2))
            .style('fill', 'url(#event-' + self.defaultType + '-element)')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('text')
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
        const size = displayOptions.gatewaySize, typeImageSize = 21;
        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        self.defaultType = aliceProcessEditor.getElementDefaultType('gateway');

        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', size)
            .attr('height', size)
            .attr('x', x - (size / 2))
            .attr('y', y - (size / 2))
            .attr('rx', 2)
            .attr('ry', 2)
            .attr('transform', 'rotate(45, ' + x + ', ' + y + ')')
            .attr('class', 'node gateway ' + self.defaultType)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('rect')
            .attr('class', 'element-type gateway')
            .attr('width', typeImageSize)
            .attr('height', typeImageSize)
            .attr('x', x - (typeImageSize / 2))
            .attr('y', y - (typeImageSize / 2))
            .style('fill', 'url(#gateway-' + self.defaultType + '-element)')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        elementContainer.append('text')
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
        const self = this;
        self.width = width ? width : 360;
        self.height = height ? height : 240;
        self.defaultType = 'groupArtifact'
        const minWidth = 120, minHeight = 80;
        const calcX = x - (self.width / 2),
              calcY = y - (self.height / 2);

        let elementContainer = groupArtifactContainer.append('g').attr('class', 'element');
        self.nodeElement = elementContainer.append('rect')
            .attr('id', workflowUtil.generateUUID())
            .attr('width', self.width)
            .attr('height', self.height)
            .attr('x', calcX)
            .attr('y', calcY)
            .attr('class', 'node resizable artifact group')
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        self.textElement = elementContainer.append('text')
            .attr('x', x)
            .attr('y', calcY + 10)
            .on('mouseover', elementMouseEventHandler.mouseover)
            .on('mouseout', elementMouseEventHandler.mouseout)
            .call(drag);

        [{x: calcX, y: calcY, cursor: 'nw-resize'}, {x: calcX + self.width, y: calcY + self.height, cursor: 'se-resize'},
            {x: calcX + self.width, y: calcY, cursor: 'ne-resize'}, {x: calcX, y: calcY + self.height, cursor: 'sw-resize'}].forEach(function(p, i) {
            self['pointElement' + (i + 1)] = elementContainer.append('circle')
                .attr('class', 'pointer')
                .attr('r', displayOptions.pointerRadius)
                .attr('cx', p.x)
                .attr('cy', p.y)
                .style('opacity', 0)
                .on('mouseover', function() { self['pointElement' + (i + 1)].style('cursor', p.cursor); })
                .on('mouseout', function() { self['pointElement' + (i + 1)].style('cursor', 'default'); })
                .call(d3.drag()
                    .on('start', function() {
                        svg.selectAll('.alice-tooltip').remove();
                    })
                    .on('drag', function() {
                        if (selectedElement && selectedElement.node().id === self.nodeElement.node().id) {
                            const mouseX = d3.event.dx,
                                  mouseY = d3.event.dy;
                            let rectData = [
                                {x: Number(self.nodeElement.attr('x')), y: Number(self.nodeElement.attr('y'))},
                                {x: Number(self.nodeElement.attr('x')) + Number(self.nodeElement.attr('width')), y: Number(self.nodeElement.attr('y')) + Number(self.nodeElement.attr('height'))}
                            ];
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
                            self.nodeElement
                                .attr('x', rectData[0].x)
                                .attr('y', rectData[0].y)
                                .attr('width', rectData[1].x - rectData[0].x)
                                .attr('height', rectData[1].y - rectData[0].y);

                            self.textElement
                                .attr('x', rectData[0].x + ((rectData[1].x - rectData[0].x) / 2))
                                .attr('y', rectData[0].y + 10);
                            changeTextToElement(self.nodeElement.node().id);

                            let pointArray =
                                [[rectData[0].x, rectData[0].y], [rectData[1].x, rectData[1].y],
                                    [rectData[1].x, rectData[0].y], [rectData[0].x, rectData[1].y]];
                            pointArray.forEach(function(point, i) {
                                self['pointElement' + (i + 1)]
                                    .attr('cx', point[0])
                                    .attr('cy', point[1]);
                            });
                        }
                    })
                    .on('end', function() {
                        aliceProcessEditor.setElementMenu(self.nodeElement);
                        aliceProcessEditor.changeDisplayValue(self.nodeElement.node().id);
                    })
                );
        });

        return self;
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
        const elementContainer = elementsContainer.append('g').attr('class', 'element');
        self.defaultType = 'annotationArtifact';

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

        elementContainer.append('text')
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
                aliceProcessEditor.setElementMenu();
            });

        d3.select('.alice-process-element-palette').selectAll('span.shape')
            .attr('draggable', 'true')
            .on('dragend', function() {
                const svgOffset = svg.node().getBoundingClientRect(),
                      gTransform = d3.zoomTransform(d3.select('g.element-container').node());
                let x = snapToGrid(d3.event.pageX - svgOffset.left - window.pageXOffset - gTransform.x),
                    y = snapToGrid(d3.event.pageY - svgOffset.top - window.pageYOffset - gTransform.y);
                const drawingBoard = document.querySelector('.alice-process-drawing-board');
                if (d3.event.pageX - svgOffset.left - window.pageXOffset < 0 ||
                    d3.event.pageY - svgOffset.top - window.pageYOffset  < 0 ||
                    d3.event.pageX - svgOffset.left - window.pageXOffset > drawingBoard.offsetWidth ||
                    d3.event.pageY - svgOffset.top - window.pageYOffset > drawingBoard.offsetHeight) {
                    return false;
                }
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
                    _this.classed(node.defaultType, true);
                    aliceProcessEditor.addElementProperty(node.nodeElement);
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
            const elements = aliceProcessEditor.data.elements;
            elements.forEach(function(elem) {
                if (elem.id === elementId) { text = elem.name; }
            });
        }

        if (d3.select(elementNode).classed('connector')) {
            d3.select(elementNode.parentNode).select('text').text(text);
        } else {
            const textElement = d3.select(elementNode.parentNode).select('text');
            if (textElement.node()) {
                const textArr = text.split('\n');
                if (textArr.length > 1) {
                    textElement.selectAll('*').remove();
                    textElement.selectAll('tspan')
                        .data(textArr)
                        .enter()
                        .append('tspan')
                        .attr('x', textElement.attr('x'))
                        .attr('y', textElement.attr('y'))
                        .attr('dy', function(d, i) { return (i * 1.2) + 'rem'; })
                        .text(function(d) { return d; });

                    if (d3.select(elementNode).classed('annotation')) {
                        const textBbox = aliceProcessEditor.utils.getBoundingBoxCenter(textElement),
                              textElementHeight = textBbox.height + 20;
                        let strokeDashArray = '6,6,6,6,6,' + textElementHeight + ',6,6,6,6,6';
                        for (let i = 0, len = Math.trunc(textElementHeight / 12); i < len; i++) {
                            strokeDashArray += ',6,6';
                        }
                        d3.select(elementNode)
                            .attr('height', textElementHeight)
                            .style('stroke-dasharray', strokeDashArray);
                    }
                } else {
                    textElement.text(textArr[0]);
                    // wrap text
                    const element = d3.select(elementNode);
                    if (textArr[0].length > 0 && (d3.select(elementNode).classed('task') || d3.select(elementNode).classed('subprocess') || d3.select(elementNode).classed('group'))) {
                        let textLength = textElement.node().getComputedTextLength(),
                            displayText = textElement.text();
                        const bbox = aliceProcessEditor.utils.getBoundingBoxCenter(element);
                        let writableWidth = bbox.width;
                        if (!d3.select(elementNode).classed('group')) {
                            writableWidth = bbox.width - 40;
                        }
                        while (textLength > writableWidth && displayText.length > 0) {
                            displayText = displayText.slice(0, -1);
                            textElement.text(displayText + '...');
                            textLength = textElement.node().getComputedTextLength();
                        }
                    }
                }
            }
        }

    }

    /**
     * svg 추가 및 필요한 element 추가.
     */
    function initProcessEdit() {
        document.addEventListener('contextmenu', function(e) {
            e.preventDefault();
        });

        const drawingBoard = document.querySelector('.alice-process-drawing-board');

        // add svg and svg event
        svg = d3.select('.alice-process-drawing-board').append('svg')
            .attr('width', drawingBoard.offsetWidth)
            .attr('height', drawingBoard.offsetHeight)
            .on('mousedown', function() {
                d3.event.stopPropagation();
                if (isDrawConnector) { return; }
                removeElementSelected();
                aliceProcessEditor.setElementMenu();
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

            let nodes = d3.select('div.minimap').selectAll('g.element, g.connector').nodes();
            let minimapTranslate = '';
            if (nodes.length > 0) {
                let transform = d3.zoomTransform(d3.select(drawingBoard).select('.element-container').node());
                minimapTranslate = 'translate(' + -transform.x + ',' + -transform.y + ')';
            }
            d3.select('rect.minimap-guide')
                .attr('width', drawingBoardWidth)
                .attr('height', drawingBoardHeight)
                .attr('transform', minimapTranslate);
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
                    nodes.forEach(function(node) {
                        let nodeBBox = aliceProcessEditor.utils.getBoundingBoxCenter(d3.select(node));
                        nodeTopArray.push(nodeBBox.cy - (nodeBBox.height / 2));
                        nodeRightArray.push(nodeBBox.cx + (nodeBBox.width / 2));
                        nodeBottomArray.push(nodeBBox.cy + (nodeBBox.height / 2));
                        nodeLeftArray.push(nodeBBox.cx - (nodeBBox.width / 2));
                    });
                    const svgBBox = aliceProcessEditor.utils.getBoundingBoxCenter(svg);
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

                let nodes = d3.select('div.minimap').selectAll('g.element, g.connector').nodes();
                let minimapTranslate = '';
                if (nodes.length > 0) {
                    minimapTranslate = 'translate(' + -d3.event.transform.x + ',' + -d3.event.transform.y + ')';
                }
                d3.select('rect.minimap-guide').attr('transform', minimapTranslate);
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
        elementsContainer = svg.append('g').attr('class', 'element-container');
        const connectorContainer = svg.append('g').attr('class', 'connector-container');
        connectors = connectorContainer.selectAll('g.connector');
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
            .attr('refX', 10)
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

        let category = aliceProcessEditor.getElementCategory(element.type);
        switch (category) {
            case 'event':
                node = new EventElement(x, y);
                aliceProcessEditor.changeElementType(node.nodeElement, element.type);
                break;
            case 'task':
                node = new TaskElement(x, y);
                aliceProcessEditor.changeElementType(node.nodeElement, element.type);
                break;
            case 'subprocess':
                node = new SubprocessElement(x, y);
                break;
            case 'gateway':
                node = new GatewayElement(x, y);
                aliceProcessEditor.changeElementType(node.nodeElement, element.type);
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
            changeTextToElement(nodeId, element.name);
        }

        return node;
    }

    /**
     * Draw a element with the loaded information.
     *
     * @param processId process ID
     * @param elementList editor 에 추가할 element 목록
     */
    function drawProcess(processId, elementList) {
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
            if (source && target) {
                element['start-id'] = source.id;
                element['end-id'] = target.id;
                let linkData = {id: element.id, sourceId: source.id, targetId: target.id, isDefault: element.data['is-default']};
                if (element.display) {
                    Object.keys(element.display).forEach(function(key) {
                        if (typeof element.display[key] !== 'undefined') {
                            let keyCamelCased = key.replace(/-([a-z])/g, function (g) { return g[1].toUpperCase(); });
                            linkData[keyCamelCased] = element.display[key];
                        }
                    });
                }
                elements.links.push(linkData);
            }
        });
        setConnectors();

        const loadingKeyName = 'alice_processes-edit-' + processId;
        aliceProcessEditor.initUtil();

        const loadingStatus = sessionStorage.getItem(loadingKeyName);
        if (loadingStatus === 'firstLoading') {
            sessionStorage.removeItem(loadingKeyName);
            let node = new EventElement(100, 200);
            aliceProcessEditor.addElementProperty(node.nodeElement);
            aliceProcessEditor.autoSave();
            aliceProcessEditor.setElementMenu();
        }
    }

    /**
     * process designer 초기화.
     *
     * @param processId 프로세스 ID
     * @param flag view 모드 = false, edit 모드 = true
     */
    function init(processId, flag) {
        console.info('process editor initialization. [PROCESS ID: ' + processId + ']');
        if (flag === 'true') { aliceProcessEditor.isView = false; }
        workflowUtil.polyfill();
        initProcessEdit();
        addElementsEvent();
        aliceProcessEditor.loadItems(processId);
    }

    exports.init = init;
    exports.isView = isView;
    exports.displayOptions = displayOptions;
    exports.elements = elements;
    exports.drawProcess = drawProcess;
    exports.addElement = addElement;
    exports.changeTextToElement = changeTextToElement;
    exports.removeElementSelected = removeElementSelected;
    exports.setConnectors = setConnectors;
    exports.setDeselectedElement = setDeselectedElement;
    Object.defineProperty(exports, '__esModule', {value: true});
})));