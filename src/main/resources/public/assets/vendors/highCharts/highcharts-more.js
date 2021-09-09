/*
 Highcharts JS v9.2.2 (2021-08-24)

 (c) 2009-2021 Torstein Honsi

 License: www.highcharts.com/license
*/
'use strict';
(function (e) {
    "object" === typeof module && module.exports ? (e["default"] = e, module.exports = e) : "function" === typeof define && define.amd ? define("highcharts/highcharts-more", ["highcharts"], function (z) {
        e(z);
        e.Highcharts = z;
        return e
    }) : e("undefined" !== typeof Highcharts ? Highcharts : void 0)
})(function (e) {
    function z(e, d, h, c) {
        e.hasOwnProperty(d) || (e[d] = c.apply(null, h))
    }

    e = e ? e._modules : {};
    z(e, "Extensions/Pane.js", [e["Core/Chart/Chart.js"], e["Core/Globals.js"], e["Core/Color/Palette.js"], e["Core/Pointer.js"],
        e["Core/Utilities.js"], e["Mixins/CenteredSeries.js"]], function (e, d, h, c, a, t) {
        function m(b, p, a) {
            return Math.sqrt(Math.pow(b - a[0], 2) + Math.pow(p - a[1], 2)) <= a[2] / 2
        }

        var l = a.addEvent, r = a.extend, x = a.merge, b = a.pick, q = a.splat;
        e.prototype.collectionsWithUpdate.push("pane");
        a = function () {
            function b(b, a) {
                this.options = this.chart = this.center = this.background = void 0;
                this.coll = "pane";
                this.defaultOptions = {center: ["50%", "50%"], size: "85%", innerSize: "0%", startAngle: 0};
                this.defaultBackgroundOptions = {
                    shape: "circle",
                    borderWidth: 1,
                    borderColor: h.neutralColor20,
                    backgroundColor: {
                        linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
                        stops: [[0, h.backgroundColor], [1, h.neutralColor10]]
                    },
                    from: -Number.MAX_VALUE,
                    innerRadius: 0,
                    to: Number.MAX_VALUE,
                    outerRadius: "105%"
                };
                this.init(b, a)
            }

            b.prototype.init = function (b, a) {
                this.chart = a;
                this.background = [];
                a.pane.push(this);
                this.setOptions(b)
            };
            b.prototype.setOptions = function (b) {
                this.options = x(this.defaultOptions, this.chart.angular ? {background: {}} : void 0, b)
            };
            b.prototype.render = function () {
                var b = this.options, a = this.options.background,
                    k = this.chart.renderer;
                this.group || (this.group = k.g("pane-group").attr({zIndex: b.zIndex || 0}).add());
                this.updateCenter();
                if (a) for (a = q(a), b = Math.max(a.length, this.background.length || 0), k = 0; k < b; k++) a[k] && this.axis ? this.renderBackground(x(this.defaultBackgroundOptions, a[k]), k) : this.background[k] && (this.background[k] = this.background[k].destroy(), this.background.splice(k, 1))
            };
            b.prototype.renderBackground = function (b, a) {
                var k = "animate", p = {"class": "highcharts-pane " + (b.className || "")};
                this.chart.styledMode ||
                r(p, {fill: b.backgroundColor, stroke: b.borderColor, "stroke-width": b.borderWidth});
                this.background[a] || (this.background[a] = this.chart.renderer.path().add(this.group), k = "attr");
                this.background[a][k]({d: this.axis.getPlotBandPath(b.from, b.to, b)}).attr(p)
            };
            b.prototype.updateCenter = function (b) {
                this.center = (b || this.axis || {}).center = t.getCenter.call(this)
            };
            b.prototype.update = function (b, a) {
                x(!0, this.options, b);
                this.setOptions(this.options);
                this.render();
                this.chart.axes.forEach(function (b) {
                    b.pane === this && (b.pane =
                        null, b.update({}, a))
                }, this)
            };
            return b
        }();
        e.prototype.getHoverPane = function (b) {
            var a = this, k;
            b && a.pane.forEach(function (p) {
                var q = b.chartX - a.plotLeft, v = b.chartY - a.plotTop;
                m(a.inverted ? v : q, a.inverted ? q : v, p.center) && (k = p)
            });
            return k
        };
        l(e, "afterIsInsidePlot", function (b) {
            this.polar && (b.isInsidePlot = this.pane.some(function (a) {
                return m(b.x, b.y, a.center)
            }))
        });
        l(c, "beforeGetHoverData", function (a) {
            var k = this.chart;
            k.polar ? (k.hoverPane = k.getHoverPane(a), a.filter = function (q) {
                return q.visible && !(!a.shared && q.directTouch) &&
                    b(q.options.enableMouseTracking, !0) && (!k.hoverPane || q.xAxis.pane === k.hoverPane)
            }) : k.hoverPane = void 0
        });
        l(c, "afterGetHoverData", function (b) {
            var a = this.chart;
            b.hoverPoint && b.hoverPoint.plotX && b.hoverPoint.plotY && a.hoverPane && !m(b.hoverPoint.plotX, b.hoverPoint.plotY, a.hoverPane.center) && (b.hoverPoint = void 0)
        });
        d.Pane = a;
        return d.Pane
    });
    z(e, "Core/Axis/RadialAxis.js", [e["Core/Axis/AxisDefaults.js"], e["Core/DefaultOptions.js"], e["Core/Globals.js"], e["Core/Utilities.js"]], function (e, d, h, c) {
        var a = d.defaultOptions,
            t = h.noop, m = c.addEvent, l = c.correctFloat, r = c.defined, x = c.extend, b = c.fireEvent, q = c.merge,
            k = c.pick, p = c.relativeLength, v = c.wrap, B;
        (function (c) {
            function d() {
                this.autoConnect = this.isCircular && "undefined" === typeof k(this.userMax, this.options.max) && l(this.endAngleRad - this.startAngleRad) === l(2 * Math.PI);
                !this.isCircular && this.chart.inverted && this.max++;
                this.autoConnect && (this.max += this.categories && 1 || this.pointRange || this.closestPointRange || 0)
            }

            function y() {
                var f = this;
                return function () {
                    if (f.isRadial && f.tickPositions &&
                        f.options.labels && !0 !== f.options.labels.allowOverlap) return f.tickPositions.map(function (g) {
                        return f.ticks[g] && f.ticks[g].label
                    }).filter(function (f) {
                        return !!f
                    })
                }
            }

            function h() {
                return t
            }

            function g(f, g, b) {
                var n = this.pane.center, u = f.value;
                if (this.isCircular) {
                    if (r(u)) f.point && (a = f.point.shapeArgs || {}, a.start && (u = this.chart.inverted ? this.translate(f.point.rectPlotY, !0) : f.point.x)); else {
                        var a = f.chartX || 0;
                        var w = f.chartY || 0;
                        u = this.translate(Math.atan2(w - b, a - g) - this.startAngleRad, !0)
                    }
                    f = this.getPosition(u);
                    a = f.x;
                    w = f.y
                } else r(u) || (a = f.chartX, w = f.chartY), r(a) && r(w) && (b = n[1] + this.chart.plotTop, u = this.translate(Math.min(Math.sqrt(Math.pow(a - g, 2) + Math.pow(w - b, 2)), n[2] / 2) - n[3] / 2, !0));
                return [u, a || 0, w || 0]
            }

            function f(f, g, b) {
                f = this.pane.center;
                var u = this.chart, n = this.left || 0, a = this.top || 0, w = k(g, f[2] / 2 - this.offset);
                "undefined" === typeof b && (b = this.horiz ? 0 : this.center && -this.center[3] / 2);
                b && (w += b);
                this.isCircular || "undefined" !== typeof g ? (g = this.chart.renderer.symbols.arc(n + f[0], a + f[1], w, w, {
                    start: this.startAngleRad,
                    end: this.endAngleRad, open: !0, innerR: 0
                }), g.xBounds = [n + f[0]], g.yBounds = [a + f[1] - w]) : (g = this.postTranslate(this.angleRad, w), g = [["M", this.center[0] + u.plotLeft, this.center[1] + u.plotTop], ["L", g.x, g.y]]);
                return g
            }

            function u() {
                this.constructor.prototype.getOffset.call(this);
                this.chart.axisOffset[this.side] = 0
            }

            function n(f, g, b) {
                var u = this.chart, n = function (f) {
                        if ("string" === typeof f) {
                            var g = parseInt(f, 10);
                            y.test(f) && (g = g * A / 100);
                            return g
                        }
                        return f
                    }, a = this.center, w = this.startAngleRad, A = a[2] / 2, q = Math.min(this.offset,
                        0), p = this.left || 0, v = this.top || 0, y = /%$/, F = this.isCircular,
                    c = k(n(b.outerRadius), A), d = n(b.innerRadius);
                n = k(n(b.thickness), 10);
                if ("polygon" === this.options.gridLineInterpolation) q = this.getPlotLinePath({value: f}).concat(this.getPlotLinePath({
                    value: g,
                    reverse: !0
                })); else {
                    f = Math.max(f, this.min);
                    g = Math.min(g, this.max);
                    f = this.translate(f);
                    g = this.translate(g);
                    F || (c = f || 0, d = g || 0);
                    if ("circle" !== b.shape && F) b = w + (f || 0), w += g || 0; else {
                        b = -Math.PI / 2;
                        w = 1.5 * Math.PI;
                        var l = !0
                    }
                    c -= q;
                    q = u.renderer.symbols.arc(p + a[0], v + a[1], c, c,
                        {start: Math.min(b, w), end: Math.max(b, w), innerR: k(d, c - (n - q)), open: l});
                    F && (F = (w + b) / 2, p = p + a[0] + a[2] / 2 * Math.cos(F), q.xBounds = F > -Math.PI / 2 && F < Math.PI / 2 ? [p, u.plotWidth] : [0, p], q.yBounds = [v + a[1] + a[2] / 2 * Math.sin(F)], q.yBounds[0] += F > -Math.PI && 0 > F || F > Math.PI ? -10 : 10)
                }
                return q
            }

            function w(f) {
                var g = this, b = this.pane.center, n = this.chart, u = n.inverted, a = f.reverse,
                    w = this.pane.options.background ? this.pane.options.background[0] || this.pane.options.background : {},
                    q = w.innerRadius || "0%", A = w.outerRadius || "100%", k = b[0] + n.plotLeft,
                    v = b[1] + n.plotTop, F = this.height, y = f.isCrosshair;
                w = b[3] / 2;
                var c = f.value, d;
                var l = this.getPosition(c);
                var h = l.x;
                l = l.y;
                y && (l = this.getCrosshairPosition(f, k, v), c = l[0], h = l[1], l = l[2]);
                if (this.isCircular) c = Math.sqrt(Math.pow(h - k, 2) + Math.pow(l - v, 2)), a = "string" === typeof q ? p(q, 1) : q / c, n = "string" === typeof A ? p(A, 1) : A / c, b && w && (w /= c, a < w && (a = w), n < w && (n = w)), b = [["M", k + a * (h - k), v - a * (v - l)], ["L", h - (1 - n) * (h - k), l + (1 - n) * (v - l)]]; else if ((c = this.translate(c)) && (0 > c || c > F) && (c = 0), "circle" === this.options.gridLineInterpolation) b =
                    this.getLinePath(0, c, w); else if (b = [], n[u ? "yAxis" : "xAxis"].forEach(function (f) {
                    f.pane === g.pane && (d = f)
                }), d) for (k = d.tickPositions, d.autoConnect && (k = k.concat([k[0]])), a && (k = k.slice().reverse()), c && (c += w), v = 0; v < k.length; v++) w = d.getPosition(k[v], c), b.push(v ? ["L", w.x, w.y] : ["M", w.x, w.y]);
                return b
            }

            function A(f, g) {
                f = this.translate(f);
                return this.postTranslate(this.isCircular ? f : this.angleRad, k(this.isCircular ? g : 0 > f ? 0 : f, this.center[2] / 2) - this.offset)
            }

            function F() {
                var f = this.center, g = this.chart, b = this.options.title;
                return {
                    x: g.plotLeft + f[0] + (b.x || 0),
                    y: g.plotTop + f[1] - {high: .5, middle: .25, low: 0}[b.align] * f[2] + (b.y || 0)
                }
            }

            function J(b) {
                b.beforeSetTickPositions = d;
                b.createLabelCollector = y;
                b.getCrosshairPosition = g;
                b.getLinePath = f;
                b.getOffset = u;
                b.getPlotBandPath = n;
                b.getPlotLinePath = w;
                b.getPosition = A;
                b.getTitlePosition = F;
                b.postTranslate = N;
                b.setAxisSize = E;
                b.setAxisTranslation = z;
                b.setOptions = O
            }

            function B() {
                var f = this.chart, g = this.options, b = this.pane, n = b && b.options;
                f.angular && this.isXAxis || !b || !f.angular && !f.polar || (this.angleRad =
                    (g.angle || 0) * Math.PI / 180, this.startAngleRad = (n.startAngle - 90) * Math.PI / 180, this.endAngleRad = (k(n.endAngle, n.startAngle + 360) - 90) * Math.PI / 180, this.offset = g.offset || 0)
            }

            function P(f) {
                this.isRadial && (f.align = void 0, f.preventDefault())
            }

            function H() {
                if (this.chart && this.chart.labelCollectors) {
                    var f = this.labelCollector ? this.chart.labelCollectors.indexOf(this.labelCollector) : -1;
                    0 <= f && this.chart.labelCollectors.splice(f, 1)
                }
            }

            function K(f) {
                var g = this.chart, b = g.inverted, n = g.angular, u = g.polar, a = this.isXAxis, w = this.coll,
                    k = n && a, A = g.options;
                f = f.userOptions.pane || 0;
                f = this.pane = g.pane && g.pane[f];
                var p;
                if ("colorAxis" === w) this.isRadial = !1; else {
                    if (n) {
                        if (k ? (this.isHidden = !0, this.createLabelCollector = h, this.getOffset = t, this.render = this.redraw = Q, this.setTitle = this.setCategories = this.setScale = t) : J(this), p = !a) this.defaultPolarOptions = R
                    } else u && (J(this), this.defaultPolarOptions = (p = this.horiz) ? S : q("xAxis" === w ? e.defaultXAxisOptions : e.defaultYAxisOptions, T), b && "yAxis" === w && (this.defaultPolarOptions.stackLabels = e.defaultYAxisOptions.stackLabels,
                        this.defaultPolarOptions.reversedStacks = !0));
                    n || u ? (this.isRadial = !0, A.chart.zoomType = null, this.labelCollector || (this.labelCollector = this.createLabelCollector()), this.labelCollector && g.labelCollectors.push(this.labelCollector)) : this.isRadial = !1;
                    f && p && (f.axis = this);
                    this.isCircular = p
                }
            }

            function C() {
                this.isRadial && this.beforeSetTickPositions()
            }

            function D(f) {
                var g = this.label;
                if (g) {
                    var b = this.axis, n = g.getBBox(), u = b.options.labels,
                        a = (b.translate(this.pos) + b.startAngleRad + Math.PI / 2) / Math.PI * 180 % 360,
                        w = Math.round(a),
                        q = r(u.y) ? 0 : .3 * -n.height, A = u.y, v = 20, F = u.align, c = "end",
                        y = 0 > w ? w + 360 : w, l = y, d = 0, h = 0;
                    if (b.isRadial) {
                        var m = b.getPosition(this.pos, b.center[2] / 2 + p(k(u.distance, -25), b.center[2] / 2, -b.center[2] / 2));
                        "auto" === u.rotation ? g.attr({rotation: a}) : r(A) || (A = b.chart.renderer.fontMetrics(g.styles && g.styles.fontSize).b - n.height / 2);
                        r(F) || (b.isCircular ? (n.width > b.len * b.tickInterval / (b.max - b.min) && (v = 0), F = a > v && a < 180 - v ? "left" : a > 180 + v && a < 360 - v ? "right" : "center") : F = "center", g.attr({align: F}));
                        if ("auto" === F && 2 === b.tickPositions.length &&
                            b.isCircular) {
                            90 < y && 180 > y ? y = 180 - y : 270 < y && 360 >= y && (y = 540 - y);
                            180 < l && 360 >= l && (l = 360 - l);
                            if (b.pane.options.startAngle === w || b.pane.options.startAngle === w + 360 || b.pane.options.startAngle === w - 360) c = "start";
                            F = -90 <= w && 90 >= w || -360 <= w && -270 >= w || 270 <= w && 360 >= w ? "start" === c ? "right" : "left" : "start" === c ? "left" : "right";
                            70 < l && 110 > l && (F = "center");
                            15 > y || 180 <= y && 195 > y ? d = .3 * n.height : 15 <= y && 35 >= y ? d = "start" === c ? 0 : .75 * n.height : 195 <= y && 215 >= y ? d = "start" === c ? .75 * n.height : 0 : 35 < y && 90 >= y ? d = "start" === c ? .25 * -n.height : n.height : 215 < y &&
                                270 >= y && (d = "start" === c ? n.height : .25 * -n.height);
                            15 > l ? h = "start" === c ? .15 * -n.height : .15 * n.height : 165 < l && 180 >= l && (h = "start" === c ? .15 * n.height : .15 * -n.height);
                            g.attr({align: F});
                            g.translate(h, d + q)
                        }
                        f.pos.x = m.x + (u.x || 0);
                        f.pos.y = m.y + (A || 0)
                    }
                }
            }

            function G(f) {
                this.axis.getPosition && x(f.pos, this.axis.getPosition(this.pos))
            }

            function N(f, g) {
                var b = this.chart, n = this.center;
                f = this.startAngleRad + f;
                return {x: b.plotLeft + n[0] + Math.cos(f) * g, y: b.plotTop + n[1] + Math.sin(f) * g}
            }

            function Q() {
                this.isDirty = !1
            }

            function E() {
                this.constructor.prototype.setAxisSize.call(this);
                if (this.isRadial) {
                    this.pane.updateCenter(this);
                    var f = this.center = this.pane.center.slice();
                    if (this.isCircular) this.sector = this.endAngleRad - this.startAngleRad; else {
                        var g = this.postTranslate(this.angleRad, f[3] / 2);
                        f[0] = g.x - this.chart.plotLeft;
                        f[1] = g.y - this.chart.plotTop
                    }
                    this.len = this.width = this.height = (f[2] - f[3]) * k(this.sector, 1) / 2
                }
            }

            function z() {
                this.constructor.prototype.setAxisTranslation.call(this);
                this.center && (this.transA = this.isCircular ? (this.endAngleRad - this.startAngleRad) / (this.max - this.min || 1) :
                    (this.center[2] - this.center[3]) / 2 / (this.max - this.min || 1), this.minPixelPadding = this.isXAxis ? this.transA * this.minPointOffset : 0)
            }

            function O(f) {
                f = this.options = q(this.constructor.defaultOptions, this.defaultPolarOptions, a[this.coll], f);
                f.plotBands || (f.plotBands = []);
                b(this, "afterSetOptions")
            }

            function U(f, g, b, n, u, w, a) {
                var k = this.axis;
                k.isRadial ? (f = k.getPosition(this.pos, k.center[2] / 2 + n), g = ["M", g, b, "L", f.x, f.y]) : g = f.call(this, g, b, n, u, w, a);
                return g
            }

            var M = [], S = {
                gridLineWidth: 1, labels: {
                    align: void 0, distance: 15,
                    x: 0, y: void 0, style: {textOverflow: "none"}
                }, maxPadding: 0, minPadding: 0, showLastLabel: !1, tickLength: 0
            }, R = {
                labels: {align: "center", x: 0, y: void 0},
                minorGridLineWidth: 0,
                minorTickInterval: "auto",
                minorTickLength: 10,
                minorTickPosition: "inside",
                minorTickWidth: 1,
                tickLength: 10,
                tickPosition: "inside",
                tickWidth: 2,
                title: {rotation: 0},
                zIndex: 2
            }, T = {
                gridLineInterpolation: "circle",
                gridLineWidth: 1,
                labels: {align: "right", x: -3, y: -2},
                showLastLabel: !1,
                title: {x: 4, text: null, rotation: 90}
            };
            c.compose = function (f, g) {
                -1 === M.indexOf(f) &&
                (M.push(f), m(f, "afterInit", B), m(f, "autoLabelAlign", P), m(f, "destroy", H), m(f, "init", K), m(f, "initialAxisTranslation", C));
                -1 === M.indexOf(g) && (M.push(g), m(g, "afterGetLabelPosition", D), m(g, "afterGetPosition", G), v(g.prototype, "getMarkPath", U));
                return f
            }
        })(B || (B = {}));
        return B
    });
    z(e, "Series/AreaRange/AreaRangePoint.js", [e["Series/Area/AreaSeries.js"], e["Core/Series/Point.js"], e["Core/Utilities.js"]], function (e, d, h) {
        var c = this && this.__extends || function () {
            var a = function (c, l) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof
                    Array && function (b, a) {
                        b.__proto__ = a
                    } || function (b, a) {
                        for (var k in a) a.hasOwnProperty(k) && (b[k] = a[k])
                    };
                return a(c, l)
            };
            return function (c, l) {
                function b() {
                    this.constructor = c
                }

                a(c, l);
                c.prototype = null === l ? Object.create(l) : (b.prototype = l.prototype, new b)
            }
        }(), a = d.prototype, t = h.defined, m = h.isNumber;
        return function (l) {
            function d() {
                var a = null !== l && l.apply(this, arguments) || this;
                a.high = void 0;
                a.low = void 0;
                a.options = void 0;
                a.plotHigh = void 0;
                a.plotLow = void 0;
                a.plotHighX = void 0;
                a.plotLowX = void 0;
                a.plotX = void 0;
                a.series =
                    void 0;
                return a
            }

            c(d, l);
            d.prototype.setState = function () {
                var c = this.state, b = this.series, q = b.chart.polar;
                t(this.plotHigh) || (this.plotHigh = b.yAxis.toPixels(this.high, !0));
                t(this.plotLow) || (this.plotLow = this.plotY = b.yAxis.toPixels(this.low, !0));
                b.stateMarkerGraphic && (b.lowerStateMarkerGraphic = b.stateMarkerGraphic, b.stateMarkerGraphic = b.upperStateMarkerGraphic);
                this.graphic = this.upperGraphic;
                this.plotY = this.plotHigh;
                q && (this.plotX = this.plotHighX);
                a.setState.apply(this, arguments);
                this.state = c;
                this.plotY =
                    this.plotLow;
                this.graphic = this.lowerGraphic;
                q && (this.plotX = this.plotLowX);
                b.stateMarkerGraphic && (b.upperStateMarkerGraphic = b.stateMarkerGraphic, b.stateMarkerGraphic = b.lowerStateMarkerGraphic, b.lowerStateMarkerGraphic = void 0);
                a.setState.apply(this, arguments)
            };
            d.prototype.haloPath = function () {
                var c = this.series.chart.polar, b = [];
                this.plotY = this.plotLow;
                c && (this.plotX = this.plotLowX);
                this.isInside && (b = a.haloPath.apply(this, arguments));
                this.plotY = this.plotHigh;
                c && (this.plotX = this.plotHighX);
                this.isTopInside &&
                (b = b.concat(a.haloPath.apply(this, arguments)));
                return b
            };
            d.prototype.isValid = function () {
                return m(this.low) && m(this.high)
            };
            return d
        }(e.prototype.pointClass)
    });
    z(e, "Series/AreaRange/AreaRangeSeries.js", [e["Series/AreaRange/AreaRangePoint.js"], e["Series/Area/AreaSeries.js"], e["Series/Column/ColumnSeries.js"], e["Core/Globals.js"], e["Core/Series/Series.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h, c, a, t, m) {
        var l = this && this.__extends || function () {
            var b = function (a, k) {
                b = Object.setPrototypeOf ||
                    {__proto__: []} instanceof Array && function (b, g) {
                        b.__proto__ = g
                    } || function (b, g) {
                        for (var f in g) g.hasOwnProperty(f) && (b[f] = g[f])
                    };
                return b(a, k)
            };
            return function (a, k) {
                function q() {
                    this.constructor = a
                }

                b(a, k);
                a.prototype = null === k ? Object.create(k) : (q.prototype = k.prototype, new q)
            }
        }(), r = d.prototype, x = h.prototype;
        h = c.noop;
        var b = a.prototype, q = m.defined, k = m.extend, p = m.isArray, v = m.pick, B = m.merge;
        a = function (a) {
            function c() {
                var b = null !== a && a.apply(this, arguments) || this;
                b.data = void 0;
                b.options = void 0;
                b.points = void 0;
                b.lowerStateMarkerGraphic = void 0;
                b.xAxis = void 0;
                return b
            }

            l(c, a);
            c.prototype.toYData = function (b) {
                return [b.low, b.high]
            };
            c.prototype.highToXY = function (b) {
                var a = this.chart, g = this.xAxis.postTranslate(b.rectPlotX || 0, this.yAxis.len - b.plotHigh);
                b.plotHighX = g.x - a.plotLeft;
                b.plotHigh = g.y - a.plotTop;
                b.plotLowX = b.plotX
            };
            c.prototype.translate = function () {
                var b = this, a = b.yAxis, g = !!b.modifyValue;
                r.translate.apply(b);
                b.points.forEach(function (f) {
                    var u = f.high, n = f.plotY;
                    f.isNull ? f.plotY = null : (f.plotLow = n, f.plotHigh = a.translate(g ?
                        b.modifyValue(u, f) : u, 0, 1, 0, 1), g && (f.yBottom = f.plotHigh))
                });
                this.chart.polar && this.points.forEach(function (f) {
                    b.highToXY(f);
                    f.tooltipPos = [(f.plotHighX + f.plotLowX) / 2, (f.plotHigh + f.plotLow) / 2]
                })
            };
            c.prototype.getGraphPath = function (b) {
                var a = [], g = [], f, u = r.getGraphPath;
                var n = this.options;
                var w = this.chart.polar, k = w && !1 !== n.connectEnds, q = n.connectNulls, c = n.step;
                b = b || this.points;
                for (f = b.length; f--;) {
                    var p = b[f];
                    var l = w ? {plotX: p.rectPlotX, plotY: p.yBottom, doCurve: !1} : {
                        plotX: p.plotX,
                        plotY: p.plotY,
                        doCurve: !1
                    };
                    p.isNull ||
                    k || q || b[f + 1] && !b[f + 1].isNull || g.push(l);
                    var d = {
                        polarPlotY: p.polarPlotY,
                        rectPlotX: p.rectPlotX,
                        yBottom: p.yBottom,
                        plotX: v(p.plotHighX, p.plotX),
                        plotY: p.plotHigh,
                        isNull: p.isNull
                    };
                    g.push(d);
                    a.push(d);
                    p.isNull || k || q || b[f - 1] && !b[f - 1].isNull || g.push(l)
                }
                b = u.call(this, b);
                c && (!0 === c && (c = "left"), n.step = {left: "right", center: "center", right: "left"}[c]);
                a = u.call(this, a);
                g = u.call(this, g);
                n.step = c;
                n = [].concat(b, a);
                !this.chart.polar && g[0] && "M" === g[0][0] && (g[0] = ["L", g[0][1], g[0][2]]);
                this.graphPath = n;
                this.areaPath = b.concat(g);
                n.isArea = !0;
                n.xMap = b.xMap;
                this.areaPath.xMap = b.xMap;
                return n
            };
            c.prototype.drawDataLabels = function () {
                var a = this.points, q = a.length, g, f = [], u = this.options.dataLabels, n, w = this.chart.inverted;
                if (u) {
                    if (p(u)) {
                        var A = u[0] || {enabled: !1};
                        var c = u[1] || {enabled: !1}
                    } else A = k({}, u), A.x = u.xHigh, A.y = u.yHigh, c = k({}, u), c.x = u.xLow, c.y = u.yLow;
                    if (A.enabled || this._hasPointLabels) {
                        for (g = q; g--;) if (n = a[g]) {
                            var v = A.inside ? n.plotHigh < n.plotLow : n.plotHigh > n.plotLow;
                            n.y = n.high;
                            n._plotY = n.plotY;
                            n.plotY = n.plotHigh;
                            f[g] = n.dataLabel;
                            n.dataLabel = n.dataLabelUpper;
                            n.below = v;
                            w ? A.align || (A.align = v ? "right" : "left") : A.verticalAlign || (A.verticalAlign = v ? "top" : "bottom")
                        }
                        this.options.dataLabels = A;
                        b.drawDataLabels && b.drawDataLabels.apply(this, arguments);
                        for (g = q; g--;) if (n = a[g]) n.dataLabelUpper = n.dataLabel, n.dataLabel = f[g], delete n.dataLabels, n.y = n.low, n.plotY = n._plotY
                    }
                    if (c.enabled || this._hasPointLabels) {
                        for (g = q; g--;) if (n = a[g]) v = c.inside ? n.plotHigh < n.plotLow : n.plotHigh > n.plotLow, n.below = !v, w ? c.align || (c.align = v ? "left" : "right") : c.verticalAlign ||
                            (c.verticalAlign = v ? "bottom" : "top");
                        this.options.dataLabels = c;
                        b.drawDataLabels && b.drawDataLabels.apply(this, arguments)
                    }
                    if (A.enabled) for (g = q; g--;) if (n = a[g]) n.dataLabels = [n.dataLabelUpper, n.dataLabel].filter(function (f) {
                        return !!f
                    });
                    this.options.dataLabels = u
                }
            };
            c.prototype.alignDataLabel = function () {
                x.alignDataLabel.apply(this, arguments)
            };
            c.prototype.drawPoints = function () {
                var a = this.points.length, c;
                b.drawPoints.apply(this, arguments);
                for (c = 0; c < a;) {
                    var g = this.points[c];
                    g.origProps = {
                        plotY: g.plotY, plotX: g.plotX,
                        isInside: g.isInside, negative: g.negative, zone: g.zone, y: g.y
                    };
                    g.lowerGraphic = g.graphic;
                    g.graphic = g.upperGraphic;
                    g.plotY = g.plotHigh;
                    q(g.plotHighX) && (g.plotX = g.plotHighX);
                    g.y = v(g.high, g.origProps.y);
                    g.negative = g.y < (this.options.threshold || 0);
                    this.zones.length && (g.zone = g.getZone());
                    this.chart.polar || (g.isInside = g.isTopInside = "undefined" !== typeof g.plotY && 0 <= g.plotY && g.plotY <= this.yAxis.len && 0 <= g.plotX && g.plotX <= this.xAxis.len);
                    c++
                }
                b.drawPoints.apply(this, arguments);
                for (c = 0; c < a;) g = this.points[c], g.upperGraphic =
                    g.graphic, g.graphic = g.lowerGraphic, g.origProps && (k(g, g.origProps), delete g.origProps), c++
            };
            c.defaultOptions = B(d.defaultOptions, {
                lineWidth: 1,
                threshold: null,
                tooltip: {pointFormat: '<span style="color:{series.color}">\u25cf</span> {series.name}: <b>{point.low}</b> - <b>{point.high}</b><br/>'},
                trackByArea: !0,
                dataLabels: {align: void 0, verticalAlign: void 0, xLow: 0, xHigh: 0, yLow: 0, yHigh: 0}
            });
            return c
        }(d);
        k(a.prototype, {
            pointArrayMap: ["low", "high"],
            pointValKey: "low",
            deferTranslatePolar: !0,
            pointClass: e,
            setStackedPoints: h
        });
        t.registerSeriesType("arearange", a);
        "";
        return a
    });
    z(e, "Series/AreaSplineRange/AreaSplineRangeSeries.js", [e["Series/AreaRange/AreaRangeSeries.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h) {
        var c = this && this.__extends || function () {
            var a = function (c, l) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (b, a) {
                    b.__proto__ = a
                } || function (b, a) {
                    for (var c in a) a.hasOwnProperty(c) && (b[c] = a[c])
                };
                return a(c, l)
            };
            return function (c, l) {
                function b() {
                    this.constructor = c
                }

                a(c,
                    l);
                c.prototype = null === l ? Object.create(l) : (b.prototype = l.prototype, new b)
            }
        }(), a = d.seriesTypes.spline, t = h.merge;
        h = h.extend;
        var m = function (a) {
            function l() {
                var c = null !== a && a.apply(this, arguments) || this;
                c.options = void 0;
                c.data = void 0;
                c.points = void 0;
                return c
            }

            c(l, a);
            l.defaultOptions = t(e.defaultOptions);
            return l
        }(e);
        h(m.prototype, {getPointSpline: a.prototype.getPointSpline});
        d.registerSeriesType("areasplinerange", m);
        "";
        return m
    });
    z(e, "Series/BoxPlot/BoxPlotSeries.js", [e["Series/Column/ColumnSeries.js"], e["Core/Globals.js"],
        e["Core/Color/Palette.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h, c, a) {
        var t = this && this.__extends || function () {
            var a = function (b, c) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (b, a) {
                    b.__proto__ = a
                } || function (b, a) {
                    for (var c in a) a.hasOwnProperty(c) && (b[c] = a[c])
                };
                return a(b, c)
            };
            return function (b, c) {
                function k() {
                    this.constructor = b
                }

                a(b, c);
                b.prototype = null === c ? Object.create(c) : (k.prototype = c.prototype, new k)
            }
        }();
        d = d.noop;
        var m = a.extend, l = a.merge, r = a.pick;
        a = function (a) {
            function b() {
                var b = null !== a && a.apply(this, arguments) || this;
                b.data = void 0;
                b.options = void 0;
                b.points = void 0;
                return b
            }

            t(b, a);
            b.prototype.pointAttribs = function () {
                return {}
            };
            b.prototype.translate = function () {
                var b = this.yAxis, c = this.pointArrayMap;
                a.prototype.translate.apply(this);
                this.points.forEach(function (a) {
                    c.forEach(function (c) {
                        null !== a[c] && (a[c + "Plot"] = b.translate(a[c], 0, 1, 0, 1))
                    });
                    a.plotHigh = a.highPlot
                })
            };
            b.prototype.drawPoints = function () {
                var b = this, a = b.options, c = b.chart, v = c.renderer,
                    l, d, h, m, e, g, f = 0, u, n, w, A, F = !1 !== b.doQuartiles, t, x = b.options.whiskerLength;
                b.points.forEach(function (k) {
                    var p = k.graphic, q = p ? "animate" : "attr", y = k.shapeArgs, B = {}, J = {}, L = {}, H = {},
                        I = k.color || b.color;
                    "undefined" !== typeof k.plotY && (u = Math.round(y.width), n = Math.floor(y.x), w = n + u, A = Math.round(u / 2), l = Math.floor(F ? k.q1Plot : k.lowPlot), d = Math.floor(F ? k.q3Plot : k.lowPlot), h = Math.floor(k.highPlot), m = Math.floor(k.lowPlot), p || (k.graphic = p = v.g("point").add(b.group), k.stem = v.path().addClass("highcharts-boxplot-stem").add(p),
                    x && (k.whiskers = v.path().addClass("highcharts-boxplot-whisker").add(p)), F && (k.box = v.path(void 0).addClass("highcharts-boxplot-box").add(p)), k.medianShape = v.path(void 0).addClass("highcharts-boxplot-median").add(p)), c.styledMode || (J.stroke = k.stemColor || a.stemColor || I, J["stroke-width"] = r(k.stemWidth, a.stemWidth, a.lineWidth), J.dashstyle = k.stemDashStyle || a.stemDashStyle || a.dashStyle, k.stem.attr(J), x && (L.stroke = k.whiskerColor || a.whiskerColor || I, L["stroke-width"] = r(k.whiskerWidth, a.whiskerWidth, a.lineWidth),
                        L.dashstyle = k.whiskerDashStyle || a.whiskerDashStyle || a.dashStyle, k.whiskers.attr(L)), F && (B.fill = k.fillColor || a.fillColor || I, B.stroke = a.lineColor || I, B["stroke-width"] = a.lineWidth || 0, B.dashstyle = k.boxDashStyle || a.boxDashStyle || a.dashStyle, k.box.attr(B)), H.stroke = k.medianColor || a.medianColor || I, H["stroke-width"] = r(k.medianWidth, a.medianWidth, a.lineWidth), H.dashstyle = k.medianDashStyle || a.medianDashStyle || a.dashStyle, k.medianShape.attr(H)), g = k.stem.strokeWidth() % 2 / 2, f = n + A + g, p = [["M", f, d], ["L", f, h], ["M",
                        f, l], ["L", f, m]], k.stem[q]({d: p}), F && (g = k.box.strokeWidth() % 2 / 2, l = Math.floor(l) + g, d = Math.floor(d) + g, n += g, w += g, p = [["M", n, d], ["L", n, l], ["L", w, l], ["L", w, d], ["L", n, d], ["Z"]], k.box[q]({d: p})), x && (g = k.whiskers.strokeWidth() % 2 / 2, h += g, m += g, t = /%$/.test(x) ? A * parseFloat(x) / 100 : x / 2, p = [["M", f - t, h], ["L", f + t, h], ["M", f - t, m], ["L", f + t, m]], k.whiskers[q]({d: p})), e = Math.round(k.medianPlot), g = k.medianShape.strokeWidth() % 2 / 2, e += g, p = [["M", n, e], ["L", w, e]], k.medianShape[q]({d: p}))
                })
            };
            b.prototype.toYData = function (b) {
                return [b.low,
                    b.q1, b.median, b.q3, b.high]
            };
            b.defaultOptions = l(e.defaultOptions, {
                threshold: null,
                tooltip: {pointFormat: '<span style="color:{point.color}">\u25cf</span> <b> {series.name}</b><br/>Maximum: {point.high}<br/>Upper quartile: {point.q3}<br/>Median: {point.median}<br/>Lower quartile: {point.q1}<br/>Minimum: {point.low}<br/>'},
                whiskerLength: "50%",
                fillColor: h.backgroundColor,
                lineWidth: 1,
                medianWidth: 2,
                whiskerWidth: 2
            });
            return b
        }(e);
        m(a.prototype, {
            pointArrayMap: ["low", "q1", "median", "q3", "high"], pointValKey: "high",
            drawDataLabels: d, setStackedPoints: d
        });
        c.registerSeriesType("boxplot", a);
        "";
        return a
    });
    z(e, "Series/Bubble/BubbleLegendDefaults.js", [e["Core/Color/Palette.js"]], function (e) {
        return {
            borderColor: void 0,
            borderWidth: 2,
            className: void 0,
            color: void 0,
            connectorClassName: void 0,
            connectorColor: void 0,
            connectorDistance: 60,
            connectorWidth: 1,
            enabled: !1,
            labels: {
                className: void 0,
                allowOverlap: !1,
                format: "",
                formatter: void 0,
                align: "right",
                style: {fontSize: "10px", color: e.neutralColor100},
                x: 0,
                y: 0
            },
            maxSize: 60,
            minSize: 10,
            legendIndex: 0,
            ranges: {value: void 0, borderColor: void 0, color: void 0, connectorColor: void 0},
            sizeBy: "area",
            sizeByAbsoluteValue: !1,
            zIndex: 1,
            zThreshold: 0
        }
    });
    z(e, "Series/Bubble/BubbleLegendItem.js", [e["Core/Color/Color.js"], e["Core/FormatUtilities.js"], e["Core/Globals.js"], e["Core/Utilities.js"]], function (e, d, h, c) {
        var a = e.parse, t = h.noop, m = c.arrayMax, l = c.arrayMin, r = c.isNumber, x = c.merge, b = c.pick,
            q = c.stableSort;
        "";
        return function () {
            function c(b, a) {
                this.options = this.symbols = this.visible = this.selected = this.ranges = this.movementX =
                    this.maxLabel = this.legendSymbol = this.legendItemWidth = this.legendItemHeight = this.legendItem = this.legendGroup = this.legend = this.fontMetrics = this.chart = void 0;
                this.setState = t;
                this.init(b, a)
            }

            c.prototype.init = function (b, a) {
                this.options = b;
                this.visible = !0;
                this.chart = a.chart;
                this.legend = a
            };
            c.prototype.addToLegend = function (b) {
                b.splice(this.options.legendIndex, 0, this)
            };
            c.prototype.drawLegendSymbol = function (a) {
                var c = this.chart, k = this.options, p = b(a.options.itemDistance, 20), l = k.ranges,
                    d = k.connectorDistance;
                this.fontMetrics =
                    c.renderer.fontMetrics(k.labels.style.fontSize);
                l && l.length && r(l[0].value) ? (q(l, function (b, g) {
                    return g.value - b.value
                }), this.ranges = l, this.setOptions(), this.render(), a = this.getMaxLabelSize(), l = this.ranges[0].radius, c = 2 * l, d = d - l + a.width, d = 0 < d ? d : 0, this.maxLabel = a, this.movementX = "left" === k.labels.align ? d : 0, this.legendItemWidth = c + d + p, this.legendItemHeight = c + this.fontMetrics.h / 2) : a.options.bubbleLegend.autoRanges = !0
            };
            c.prototype.setOptions = function () {
                var c = this.ranges, k = this.options, l = this.chart.series[k.seriesIndex],
                    q = this.legend.baseline, d = {zIndex: k.zIndex, "stroke-width": k.borderWidth},
                    h = {zIndex: k.zIndex, "stroke-width": k.connectorWidth}, e = {
                        align: this.legend.options.rtl || "left" === k.labels.align ? "right" : "left",
                        zIndex: k.zIndex
                    }, g = l.options.marker.fillOpacity, f = this.chart.styledMode;
                c.forEach(function (u, n) {
                    f || (d.stroke = b(u.borderColor, k.borderColor, l.color), d.fill = b(u.color, k.color, 1 !== g ? a(l.color).setOpacity(g).get("rgba") : l.color), h.stroke = b(u.connectorColor, k.connectorColor, l.color));
                    c[n].radius = this.getRangeRadius(u.value);
                    c[n] = x(c[n], {center: c[0].radius - c[n].radius + q});
                    f || x(!0, c[n], {bubbleAttribs: x(d), connectorAttribs: x(h), labelAttribs: e})
                }, this)
            };
            c.prototype.getRangeRadius = function (b) {
                var a = this.options;
                return this.chart.series[this.options.seriesIndex].getRadius.call(this, a.ranges[a.ranges.length - 1].value, a.ranges[0].value, a.minSize, a.maxSize, b)
            };
            c.prototype.render = function () {
                var b = this.chart.renderer, a = this.options.zThreshold;
                this.symbols || (this.symbols = {connectors: [], bubbleItems: [], labels: []});
                this.legendSymbol =
                    b.g("bubble-legend");
                this.legendItem = b.g("bubble-legend-item");
                this.legendSymbol.translateX = 0;
                this.legendSymbol.translateY = 0;
                this.ranges.forEach(function (b) {
                    b.value >= a && this.renderRange(b)
                }, this);
                this.legendSymbol.add(this.legendItem);
                this.legendItem.add(this.legendGroup);
                this.hideOverlappingLabels()
            };
            c.prototype.renderRange = function (b) {
                var a = this.options, c = a.labels, k = this.chart, l = k.series[a.seriesIndex], d = k.renderer,
                    q = this.symbols;
                k = q.labels;
                var g = b.center, f = Math.abs(b.radius), u = a.connectorDistance ||
                    0, n = c.align, w = a.connectorWidth, A = this.ranges[0].radius || 0,
                    p = g - f - a.borderWidth / 2 + w / 2, h = this.fontMetrics;
                h = h.f / 2 - (h.h - h.f) / 2;
                var e = d.styledMode;
                u = this.legend.options.rtl || "left" === n ? -u : u;
                "center" === n && (u = 0, a.connectorDistance = 0, b.labelAttribs.align = "center");
                n = p + a.labels.y;
                var m = A + u + a.labels.x;
                q.bubbleItems.push(d.circle(A, g + ((p % 1 ? 1 : .5) - (w % 2 ? 0 : .5)), f).attr(e ? {} : b.bubbleAttribs).addClass((e ? "highcharts-color-" + l.colorIndex + " " : "") + "highcharts-bubble-legend-symbol " + (a.className || "")).add(this.legendSymbol));
                q.connectors.push(d.path(d.crispLine([["M", A, p], ["L", A + u, p]], a.connectorWidth)).attr(e ? {} : b.connectorAttribs).addClass((e ? "highcharts-color-" + this.options.seriesIndex + " " : "") + "highcharts-bubble-legend-connectors " + (a.connectorClassName || "")).add(this.legendSymbol));
                b = d.text(this.formatLabel(b), m, n + h).attr(e ? {} : b.labelAttribs).css(e ? {} : c.style).addClass("highcharts-bubble-legend-labels " + (a.labels.className || "")).add(this.legendSymbol);
                k.push(b);
                b.placed = !0;
                b.alignAttr = {x: m, y: n + h}
            };
            c.prototype.getMaxLabelSize =
                function () {
                    var b, a;
                    this.symbols.labels.forEach(function (c) {
                        a = c.getBBox(!0);
                        b = b ? a.width > b.width ? a : b : a
                    });
                    return b || {}
                };
            c.prototype.formatLabel = function (b) {
                var a = this.options, c = a.labels.formatter;
                a = a.labels.format;
                var k = this.chart.numberFormatter;
                return a ? d.format(a, b) : c ? c.call(b) : k(b.value, 1)
            };
            c.prototype.hideOverlappingLabels = function () {
                var b = this.chart, a = this.symbols;
                !this.options.labels.allowOverlap && a && (b.hideOverlappingLabels(a.labels), a.labels.forEach(function (b, c) {
                    b.newOpacity ? b.newOpacity !==
                        b.oldOpacity && a.connectors[c].show() : a.connectors[c].hide()
                }))
            };
            c.prototype.getRanges = function () {
                var a = this.legend.bubbleLegend, c = a.options.ranges, k, d = Number.MAX_VALUE, q = -Number.MAX_VALUE;
                a.chart.series.forEach(function (a) {
                    a.isBubble && !a.ignoreSeries && (k = a.zData.filter(r), k.length && (d = b(a.options.zMin, Math.min(d, Math.max(l(k), !1 === a.options.displayNegative ? a.options.zThreshold : -Number.MAX_VALUE))), q = b(a.options.zMax, Math.max(q, m(k)))))
                });
                var h = d === q ? [{value: q}] : [{value: d}, {value: (d + q) / 2}, {
                    value: q,
                    autoRanges: !0
                }];
                c.length && c[0].radius && h.reverse();
                h.forEach(function (b, a) {
                    c && c[a] && (h[a] = x(c[a], b))
                });
                return h
            };
            c.prototype.predictBubbleSizes = function () {
                var b = this.chart, a = this.fontMetrics, c = b.legend.options, k = "horizontal" === c.layout,
                    l = k ? b.legend.lastLineHeight : 0, d = b.plotSizeX, q = b.plotSizeY,
                    g = b.series[this.options.seriesIndex];
                b = Math.ceil(g.minPxSize);
                var f = Math.ceil(g.maxPxSize), u = Math.min(q, d);
                g = g.options.maxSize;
                if (c.floating || !/%$/.test(g)) a = f; else if (g = parseFloat(g), a = (u + l - a.h / 2) * g / 100 / (g /
                    100 + 1), k && q - a >= d || !k && d - a >= q) a = f;
                return [b, Math.ceil(a)]
            };
            c.prototype.updateRanges = function (b, a) {
                var c = this.legend.options.bubbleLegend;
                c.minSize = b;
                c.maxSize = a;
                c.ranges = this.getRanges()
            };
            c.prototype.correctSizes = function () {
                var b = this.legend, a = this.chart.series[this.options.seriesIndex];
                1 < Math.abs(Math.ceil(a.maxPxSize) - this.options.maxSize) && (this.updateRanges(this.options.minSize, a.maxPxSize), b.render())
            };
            return c
        }()
    });
    z(e, "Series/Bubble/BubbleLegendComposition.js", [e["Series/Bubble/BubbleLegendDefaults.js"],
        e["Series/Bubble/BubbleLegendItem.js"], e["Core/DefaultOptions.js"], e["Core/Utilities.js"]], function (e, d, h, c) {
        var a = h.setOptions, t = c.addEvent, m = c.objectEach, l = c.wrap, r;
        (function (c) {
            function b(b, a, c) {
                var g = this.legend, f = 0 <= q(this);
                if (g && g.options.enabled && g.bubbleLegend && g.options.bubbleLegend.autoRanges && f) {
                    var u = g.bubbleLegend.options;
                    f = g.bubbleLegend.predictBubbleSizes();
                    g.bubbleLegend.updateRanges(f[0], f[1]);
                    u.placed || (g.group.placed = !1, g.allItems.forEach(function (f) {
                        f.legendGroup.translateY = null
                    }));
                    g.render();
                    this.getMargins();
                    this.axes.forEach(function (f) {
                        f.visible && f.render();
                        u.placed || (f.setScale(), f.updateNames(), m(f.ticks, function (f) {
                            f.isNew = !0;
                            f.isNewLabel = !0
                        }))
                    });
                    u.placed = !0;
                    this.getMargins();
                    b.call(this, a, c);
                    g.bubbleLegend.correctSizes();
                    r(g, k(g))
                } else b.call(this, a, c), g && g.options.enabled && g.bubbleLegend && (g.render(), r(g, k(g)))
            }

            function q(b) {
                b = b.series;
                for (var a = 0; a < b.length;) {
                    if (b[a] && b[a].isBubble && b[a].visible && b[a].zData.length) return a;
                    a++
                }
                return -1
            }

            function k(b) {
                b = b.allItems;
                var a =
                    [], c = b.length, g, f = 0;
                for (g = 0; g < c; g++) if (b[g].legendItemHeight && (b[g].itemHeight = b[g].legendItemHeight), b[g] === b[c - 1] || b[g + 1] && b[g]._legendItemPos[1] !== b[g + 1]._legendItemPos[1]) {
                    a.push({height: 0});
                    var u = a[a.length - 1];
                    for (f; f <= g; f++) b[f].itemHeight > u.height && (u.height = b[f].itemHeight);
                    u.step = g
                }
                return a
            }

            function h(b) {
                var a = this.bubbleLegend, c = this.options, g = c.bubbleLegend, f = q(this.chart);
                a && a.ranges && a.ranges.length && (g.ranges.length && (g.autoRanges = !!g.ranges[0].autoRanges), this.destroyItem(a));
                0 <= f &&
                c.enabled && g.enabled && (g.seriesIndex = f, this.bubbleLegend = new d(g, this), this.bubbleLegend.addToLegend(b.allItems))
            }

            function v() {
                var b = this.chart, a = this.visible, c = this.chart.legend;
                c && c.bubbleLegend && (this.visible = !a, this.ignoreSeries = a, b = 0 <= q(b), c.bubbleLegend.visible !== b && (c.update({bubbleLegend: {enabled: b}}), c.bubbleLegend.visible = b), this.visible = a)
            }

            function r(b, a) {
                var c = b.options.rtl, g, f, u, n = 0;
                b.allItems.forEach(function (b, k) {
                    g = b.legendGroup.translateX;
                    f = b._legendItemPos[1];
                    if ((u = b.movementX) ||
                        c && b.ranges) u = c ? g - b.options.maxSize / 2 : g + u, b.legendGroup.attr({translateX: u});
                    k > a[n].step && n++;
                    b.legendGroup.attr({translateY: Math.round(f + a[n].height / 2)});
                    b._legendItemPos[1] = f + a[n].height / 2
                })
            }

            var x = [];
            c.compose = function (c, k, d) {
                -1 === x.indexOf(c) && (x.push(c), a({legend: {bubbleLegend: e}}), l(c.prototype, "drawChartBox", b));
                -1 === x.indexOf(k) && (x.push(k), t(k, "afterGetAllItems", h));
                -1 === x.indexOf(d) && (x.push(d), t(d, "legendItemClick", v))
            }
        })(r || (r = {}));
        return r
    });
    z(e, "Series/Bubble/BubblePoint.js", [e["Core/Series/Point.js"],
        e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h) {
        var c = this && this.__extends || function () {
            var a = function (c, d) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, c) {
                    a.__proto__ = c
                } || function (a, c) {
                    for (var d in c) c.hasOwnProperty(d) && (a[d] = c[d])
                };
                return a(c, d)
            };
            return function (c, d) {
                function l() {
                    this.constructor = c
                }

                a(c, d);
                c.prototype = null === d ? Object.create(d) : (l.prototype = d.prototype, new l)
            }
        }();
        h = h.extend;
        d = function (a) {
            function d() {
                var c = null !== a && a.apply(this, arguments) ||
                    this;
                c.options = void 0;
                c.series = void 0;
                return c
            }

            c(d, a);
            d.prototype.haloPath = function (a) {
                return e.prototype.haloPath.call(this, 0 === a ? 0 : (this.marker ? this.marker.radius || 0 : 0) + a)
            };
            return d
        }(d.seriesTypes.scatter.prototype.pointClass);
        h(d.prototype, {ttBelow: !1});
        return d
    });
    z(e, "Series/Bubble/BubbleSeries.js", [e["Core/Axis/Axis.js"], e["Series/Bubble/BubbleLegendComposition.js"], e["Series/Bubble/BubblePoint.js"], e["Core/Color/Color.js"], e["Core/Globals.js"], e["Core/Series/Series.js"], e["Core/Series/SeriesRegistry.js"],
        e["Core/Utilities.js"]], function (e, d, h, c, a, t, m, l) {
        var r = this && this.__extends || function () {
            var b = function (f, a) {
                b = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (b, f) {
                    b.__proto__ = f
                } || function (b, f) {
                    for (var a in f) f.hasOwnProperty(a) && (b[a] = f[a])
                };
                return b(f, a)
            };
            return function (f, a) {
                function g() {
                    this.constructor = f
                }

                b(f, a);
                f.prototype = null === a ? Object.create(a) : (g.prototype = a.prototype, new g)
            }
        }(), x = c.parse;
        c = a.noop;
        var b = m.seriesTypes;
        a = b.column;
        var q = b.scatter, k = l.arrayMax, p = l.arrayMin, v =
            l.clamp, B = l.extend, H = l.isNumber, D = l.merge, y = l.pick, I = l.pInt;
        l = function (b) {
            function f() {
                var f = null !== b && b.apply(this, arguments) || this;
                f.data = void 0;
                f.maxPxSize = void 0;
                f.minPxSize = void 0;
                f.options = void 0;
                f.points = void 0;
                f.radii = void 0;
                f.yData = void 0;
                f.zData = void 0;
                return f
            }

            r(f, b);
            f.prototype.animate = function (b) {
                !b && this.points.length < this.options.animationLimit && this.points.forEach(function (b) {
                    var f = b.graphic;
                    f && f.width && (this.hasRendered || f.attr({
                        x: b.plotX,
                        y: b.plotY,
                        width: 1,
                        height: 1
                    }), f.animate(this.markerAttribs(b),
                        this.options.animation))
                }, this)
            };
            f.prototype.getRadii = function (b, f, a) {
                var g = this.zData, c = this.yData, n = a.minPxSize, u = a.maxPxSize, k = [];
                var w = 0;
                for (a = g.length; w < a; w++) {
                    var d = g[w];
                    k.push(this.getRadius(b, f, n, u, d, c[w]))
                }
                this.radii = k
            };
            f.prototype.getRadius = function (b, f, a, g, c, k) {
                var n = this.options, u = "width" !== n.sizeBy, w = n.zThreshold, d = f - b, q = .5;
                if (null === k || null === c) return null;
                if (H(c)) {
                    n.sizeByAbsoluteValue && (c = Math.abs(c - w), d = Math.max(f - w, Math.abs(b - w)), b = 0);
                    if (c < b) return a / 2 - 1;
                    0 < d && (q = (c - b) / d)
                }
                u && 0 <= q &&
                (q = Math.sqrt(q));
                return Math.ceil(a + q * (g - a)) / 2
            };
            f.prototype.hasData = function () {
                return !!this.processedXData.length
            };
            f.prototype.pointAttribs = function (b, f) {
                var a = this.options.marker.fillOpacity;
                b = t.prototype.pointAttribs.call(this, b, f);
                1 !== a && (b.fill = x(b.fill).setOpacity(a).get("rgba"));
                return b
            };
            f.prototype.translate = function () {
                var f, a = this.data, g = this.radii;
                b.prototype.translate.call(this);
                for (f = a.length; f--;) {
                    var c = a[f];
                    var k = g ? g[f] : 0;
                    H(k) && k >= this.minPxSize / 2 ? (c.marker = B(c.marker, {
                        radius: k, width: 2 *
                            k, height: 2 * k
                    }), c.dlBox = {
                        x: c.plotX - k,
                        y: c.plotY - k,
                        width: 2 * k,
                        height: 2 * k
                    }) : c.shapeArgs = c.plotY = c.dlBox = void 0
                }
            };
            f.compose = d.compose;
            f.defaultOptions = D(q.defaultOptions, {
                dataLabels: {
                    formatter: function () {
                        var b = this.series.chart.numberFormatter, f = this.point.z;
                        return H(f) ? b(f, -1) : ""
                    }, inside: !0, verticalAlign: "middle"
                },
                animationLimit: 250,
                marker: {
                    lineColor: null,
                    lineWidth: 1,
                    fillOpacity: .5,
                    radius: null,
                    states: {hover: {radiusPlus: 0}},
                    symbol: "circle"
                },
                minSize: 8,
                maxSize: "20%",
                softThreshold: !1,
                states: {hover: {halo: {size: 5}}},
                tooltip: {pointFormat: "({point.x}, {point.y}), Size: {point.z}"},
                turboThreshold: 0,
                zThreshold: 0,
                zoneAxis: "z"
            });
            return f
        }(q);
        B(l.prototype, {
            alignDataLabel: a.prototype.alignDataLabel,
            applyZones: c,
            bubblePadding: !0,
            buildKDTree: c,
            directTouch: !0,
            isBubble: !0,
            pointArrayMap: ["y", "z"],
            pointClass: h,
            parallelArrays: ["x", "y", "z"],
            trackerGroups: ["group", "dataLabelsGroup"],
            specialGroup: "group",
            zoneAxis: "z"
        });
        e.prototype.beforePadding = function () {
            var b = this, f = this.len, a = this.chart, c = 0, w = f, d = this.isXAxis, q = d ? "xData" :
                "yData", l = this.min, h = {}, e = Math.min(a.plotWidth, a.plotHeight), m = Number.MAX_VALUE,
                r = -Number.MAX_VALUE, x = this.max - l, t = f / x, B = [];
            this.series.forEach(function (f) {
                var g = f.options;
                !f.bubblePadding || !f.visible && a.options.chart.ignoreHiddenSeries || (b.allowZoomOutside = !0, B.push(f), d && (["minSize", "maxSize"].forEach(function (b) {
                    var f = g[b], a = /%$/.test(f);
                    f = I(f);
                    h[b] = a ? e * f / 100 : f
                }), f.minPxSize = h.minSize, f.maxPxSize = Math.max(h.maxSize, h.minSize), f = f.zData.filter(H), f.length && (m = y(g.zMin, v(p(f), !1 === g.displayNegative ?
                    g.zThreshold : -Number.MAX_VALUE, m)), r = y(g.zMax, Math.max(r, k(f))))))
            });
            B.forEach(function (f) {
                var a = f[q], g = a.length;
                d && f.getRadii(m, r, f);
                if (0 < x) for (; g--;) if (H(a[g]) && b.dataMin <= a[g] && a[g] <= b.max) {
                    var n = f.radii ? f.radii[g] : 0;
                    c = Math.min((a[g] - l) * t - n, c);
                    w = Math.max((a[g] - l) * t + n, w)
                }
            });
            B.length && 0 < x && !this.logarithmic && (w -= f, t *= (f + Math.max(0, c) - Math.min(w, f)) / f, [["min", "userMin", c], ["max", "userMax", w]].forEach(function (f) {
                "undefined" === typeof y(b.options[f[0]], b[f[1]]) && (b[f[0]] += f[2] / t)
            }))
        };
        m.registerSeriesType("bubble",
            l);
        "";
        "";
        return l
    });
    z(e, "Series/ColumnRange/ColumnRangePoint.js", [e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d) {
        var h = this && this.__extends || function () {
            var a = function (c, d) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, b) {
                    a.__proto__ = b
                } || function (a, b) {
                    for (var c in b) b.hasOwnProperty(c) && (a[c] = b[c])
                };
                return a(c, d)
            };
            return function (c, d) {
                function l() {
                    this.constructor = c
                }

                a(c, d);
                c.prototype = null === d ? Object.create(d) : (l.prototype = d.prototype, new l)
            }
        }(), c = e.seriesTypes;
        e = c.column.prototype.pointClass;
        var a = d.extend, t = d.isNumber;
        d = function (a) {
            function c() {
                var c = null !== a && a.apply(this, arguments) || this;
                c.series = void 0;
                c.options = void 0;
                c.barX = void 0;
                c.pointWidth = void 0;
                c.shapeType = void 0;
                return c
            }

            h(c, a);
            c.prototype.isValid = function () {
                return t(this.low)
            };
            return c
        }(c.arearange.prototype.pointClass);
        a(d.prototype, {setState: e.prototype.setState});
        return d
    });
    z(e, "Series/ColumnRange/ColumnRangeSeries.js", [e["Series/ColumnRange/ColumnRangePoint.js"], e["Core/Globals.js"], e["Core/Series/SeriesRegistry.js"],
        e["Core/Utilities.js"]], function (e, d, h, c) {
        var a = this && this.__extends || function () {
            var b = function (a, c) {
                b = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (b, a) {
                    b.__proto__ = a
                } || function (b, a) {
                    for (var c in a) a.hasOwnProperty(c) && (b[c] = a[c])
                };
                return b(a, c)
            };
            return function (a, c) {
                function k() {
                    this.constructor = a
                }

                b(a, c);
                a.prototype = null === c ? Object.create(c) : (k.prototype = c.prototype, new k)
            }
        }();
        d = d.noop;
        var t = h.seriesTypes, m = t.arearange, l = t.column, r = l.prototype, x = m.prototype, b = c.clamp,
            q = c.merge,
            k = c.pick;
        c = c.extend;
        var p = {pointRange: null, marker: null, states: {hover: {halo: !1}}};
        t = function (c) {
            function d() {
                var b = null !== c && c.apply(this, arguments) || this;
                b.data = void 0;
                b.points = void 0;
                b.options = void 0;
                return b
            }

            a(d, c);
            d.prototype.setOptions = function () {
                q(!0, arguments[0], {stacking: void 0});
                return x.setOptions.apply(this, arguments)
            };
            d.prototype.translate = function () {
                var a = this, c = a.yAxis, d = a.xAxis, q = d.startAngleRad, g, f = a.chart, u = a.xAxis.isRadial,
                    n = Math.max(f.chartWidth, f.chartHeight) + 999, w;
                r.translate.apply(a);
                a.points.forEach(function (l) {
                    var h = l.shapeArgs || {}, A = a.options.minPointLength;
                    l.plotHigh = w = b(c.translate(l.high, 0, 1, 0, 1), -n, n);
                    l.plotLow = b(l.plotY, -n, n);
                    var e = w;
                    var p = k(l.rectPlotY, l.plotY) - w;
                    Math.abs(p) < A ? (A -= p, p += A, e -= A / 2) : 0 > p && (p *= -1, e -= p);
                    u ? (g = l.barX + q, l.shapeType = "arc", l.shapeArgs = a.polarArc(e + p, e, g, g + l.pointWidth)) : (h.height = p, h.y = e, A = h.x, A = void 0 === A ? 0 : A, h = h.width, h = void 0 === h ? 0 : h, l.tooltipPos = f.inverted ? [c.len + c.pos - f.plotLeft - e - p / 2, d.len + d.pos - f.plotTop - A - h / 2, p] : [d.left - f.plotLeft + A + h / 2,
                        c.pos - f.plotTop + e + p / 2, p])
                })
            };
            d.prototype.crispCol = function () {
                return r.crispCol.apply(this, arguments)
            };
            d.prototype.drawPoints = function () {
                return r.drawPoints.apply(this, arguments)
            };
            d.prototype.drawTracker = function () {
                return r.drawTracker.apply(this, arguments)
            };
            d.prototype.getColumnMetrics = function () {
                return r.getColumnMetrics.apply(this, arguments)
            };
            d.prototype.pointAttribs = function () {
                return r.pointAttribs.apply(this, arguments)
            };
            d.prototype.adjustForMissingColumns = function () {
                return r.adjustForMissingColumns.apply(this,
                    arguments)
            };
            d.prototype.animate = function () {
                return r.animate.apply(this, arguments)
            };
            d.prototype.translate3dPoints = function () {
                return r.translate3dPoints.apply(this, arguments)
            };
            d.prototype.translate3dShapes = function () {
                return r.translate3dShapes.apply(this, arguments)
            };
            d.defaultOptions = q(l.defaultOptions, m.defaultOptions, p);
            return d
        }(m);
        c(t.prototype, {
            directTouch: !0,
            trackerGroups: ["group", "dataLabelsGroup"],
            drawGraph: d,
            getSymbol: d,
            polarArc: function () {
                return r.polarArc.apply(this, arguments)
            },
            pointClass: e
        });
        h.registerSeriesType("columnrange", t);
        "";
        return t
    });
    z(e, "Series/ColumnPyramid/ColumnPyramidSeries.js", [e["Series/Column/ColumnSeries.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h) {
        var c = this && this.__extends || function () {
            var a = function (c, b) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (b, a) {
                    b.__proto__ = a
                } || function (b, a) {
                    for (var c in a) a.hasOwnProperty(c) && (b[c] = a[c])
                };
                return a(c, b)
            };
            return function (c, b) {
                function d() {
                    this.constructor = c
                }

                a(c, b);
                c.prototype =
                    null === b ? Object.create(b) : (d.prototype = b.prototype, new d)
            }
        }(), a = e.prototype, t = h.clamp, m = h.merge, l = h.pick;
        h = function (d) {
            function h() {
                var b = null !== d && d.apply(this, arguments) || this;
                b.data = void 0;
                b.options = void 0;
                b.points = void 0;
                return b
            }

            c(h, d);
            h.prototype.translate = function () {
                var b = this, c = b.chart, k = b.options, d = b.dense = 2 > b.closestPointRange * b.xAxis.transA;
                d = b.borderWidth = l(k.borderWidth, d ? 0 : 1);
                var h = b.yAxis, e = k.threshold, m = b.translatedThreshold = h.getThreshold(e),
                    x = l(k.minPointLength, 5), r = b.getColumnMetrics(),
                    I = r.width, g = b.barW = Math.max(I, 1 + 2 * d), f = b.pointXOffset = r.offset;
                c.inverted && (m -= .5);
                k.pointPadding && (g = Math.ceil(g));
                a.translate.apply(b);
                b.points.forEach(function (a) {
                    var n = l(a.yBottom, m), d = 999 + Math.abs(n), u = t(a.plotY, -d, h.len + d);
                    d = a.plotX + f;
                    var q = g / 2, p = Math.min(u, n);
                    n = Math.max(u, n) - p;
                    var v;
                    a.barX = d;
                    a.pointWidth = I;
                    a.tooltipPos = c.inverted ? [h.len + h.pos - c.plotLeft - u, b.xAxis.len - d - q, n] : [d + q, u + h.pos - c.plotTop, n];
                    u = e + (a.total || a.y);
                    "percent" === k.stacking && (u = e + (0 > a.y) ? -100 : 100);
                    u = h.toPixels(u, !0);
                    var r =
                        (v = c.plotHeight - u - (c.plotHeight - m)) ? q * (p - u) / v : 0;
                    var y = v ? q * (p + n - u) / v : 0;
                    v = d - r + q;
                    r = d + r + q;
                    var K = d + y + q;
                    y = d - y + q;
                    var C = p - x;
                    var B = p + n;
                    0 > a.y && (C = p, B = p + n + x);
                    c.inverted && (K = c.plotWidth - p, v = u - (c.plotWidth - m), r = q * (u - K) / v, y = q * (u - (K - n)) / v, v = d + q + r, r = v - 2 * r, K = d - y + q, y = d + y + q, C = p, B = p + n - x, 0 > a.y && (B = p + n + x));
                    a.shapeType = "path";
                    a.shapeArgs = {
                        x: v,
                        y: C,
                        width: r - v,
                        height: n,
                        d: [["M", v, C], ["L", r, C], ["L", K, B], ["L", y, B], ["Z"]]
                    }
                })
            };
            h.defaultOptions = m(e.defaultOptions, {});
            return h
        }(e);
        d.registerSeriesType("columnpyramid", h);
        "";
        return h
    });
    z(e, "Series/ErrorBar/ErrorBarSeries.js", [e["Series/BoxPlot/BoxPlotSeries.js"], e["Series/Column/ColumnSeries.js"], e["Core/Color/Palette.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h, c, a) {
        var t = this && this.__extends || function () {
            var a = function (b, c) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (b, a) {
                    b.__proto__ = a
                } || function (b, a) {
                    for (var c in a) a.hasOwnProperty(c) && (b[c] = a[c])
                };
                return a(b, c)
            };
            return function (b, c) {
                function d() {
                    this.constructor = b
                }

                a(b, c);
                b.prototype = null === c ? Object.create(c) : (d.prototype = c.prototype, new d)
            }
        }(), m = c.seriesTypes.arearange, l = a.merge;
        a = a.extend;
        var r = function (a) {
            function b() {
                var b = null !== a && a.apply(this, arguments) || this;
                b.data = void 0;
                b.options = void 0;
                b.points = void 0;
                return b
            }

            t(b, a);
            b.prototype.getColumnMetrics = function () {
                return this.linkedParent && this.linkedParent.columnMetrics || d.prototype.getColumnMetrics.call(this)
            };
            b.prototype.drawDataLabels = function () {
                var b = this.pointValKey;
                m && (m.prototype.drawDataLabels.call(this),
                    this.data.forEach(function (a) {
                        a.y = a[b]
                    }))
            };
            b.prototype.toYData = function (b) {
                return [b.low, b.high]
            };
            b.defaultOptions = l(e.defaultOptions, {
                color: h.neutralColor100,
                grouping: !1,
                linkedTo: ":previous",
                tooltip: {pointFormat: '<span style="color:{point.color}">\u25cf</span> {series.name}: <b>{point.low}</b> - <b>{point.high}</b><br/>'},
                whiskerWidth: null
            });
            return b
        }(e);
        a(r.prototype, {pointArrayMap: ["low", "high"], pointValKey: "high", doQuartiles: !1});
        c.registerSeriesType("errorbar", r);
        "";
        return r
    });
    z(e, "Series/Gauge/GaugePoint.js",
        [e["Core/Series/SeriesRegistry.js"]], function (e) {
        var d = this && this.__extends || function () {
            var d = function (c, a) {
                d = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, c) {
                    a.__proto__ = c
                } || function (a, c) {
                    for (var d in c) c.hasOwnProperty(d) && (a[d] = c[d])
                };
                return d(c, a)
            };
            return function (c, a) {
                function h() {
                    this.constructor = c
                }

                d(c, a);
                c.prototype = null === a ? Object.create(a) : (h.prototype = a.prototype, new h)
            }
        }();
        return function (h) {
            function c() {
                var a = null !== h && h.apply(this, arguments) || this;
                a.options = void 0;
                a.series = void 0;
                a.shapeArgs = void 0;
                return a
            }

            d(c, h);
            c.prototype.setState = function (a) {
                this.state = a
            };
            return c
        }(e.series.prototype.pointClass)
    });
    z(e, "Series/Gauge/GaugeSeries.js", [e["Series/Gauge/GaugePoint.js"], e["Core/Globals.js"], e["Core/Color/Palette.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h, c, a) {
        var t = this && this.__extends || function () {
            var b = function (a, c) {
                b = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (b, a) {
                    b.__proto__ = a
                } || function (b, a) {
                    for (var c in a) a.hasOwnProperty(c) &&
                    (b[c] = a[c])
                };
                return b(a, c)
            };
            return function (a, c) {
                function d() {
                    this.constructor = a
                }

                b(a, c);
                a.prototype = null === c ? Object.create(c) : (d.prototype = c.prototype, new d)
            }
        }();
        d = d.noop;
        var m = c.series, l = c.seriesTypes.column, r = a.clamp, x = a.isNumber, b = a.extend, q = a.merge, k = a.pick,
            p = a.pInt;
        a = function (b) {
            function a() {
                var a = null !== b && b.apply(this, arguments) || this;
                a.data = void 0;
                a.points = void 0;
                a.options = void 0;
                a.yAxis = void 0;
                return a
            }

            t(a, b);
            a.prototype.translate = function () {
                var b = this.yAxis, a = this.options, c = b.center;
                this.generatePoints();
                this.points.forEach(function (d) {
                    var g = q(a.dial, d.dial), f = p(k(g.radius, "80%")) * c[2] / 200,
                        u = p(k(g.baseLength, "70%")) * f / 100, n = p(k(g.rearLength, "10%")) * f / 100,
                        w = g.baseWidth || 3, l = g.topWidth || 1, h = a.overshoot,
                        e = b.startAngleRad + b.translate(d.y, null, null, null, !0);
                    if (x(h) || !1 === a.wrap) h = x(h) ? h / 180 * Math.PI : 0, e = r(e, b.startAngleRad - h, b.endAngleRad + h);
                    e = 180 * e / Math.PI;
                    d.shapeType = "path";
                    d.shapeArgs = {
                        d: g.path || [["M", -n, -w / 2], ["L", u, -w / 2], ["L", f, -l / 2], ["L", f, l / 2], ["L", u, w / 2], ["L", -n, w / 2], ["Z"]],
                        translateX: c[0],
                        translateY: c[1],
                        rotation: e
                    };
                    d.plotX = c[0];
                    d.plotY = c[1]
                })
            };
            a.prototype.drawPoints = function () {
                var b = this, a = b.chart, c = b.yAxis.center, d = b.pivot, g = b.options, f = g.pivot, u = a.renderer;
                b.points.forEach(function (f) {
                    var c = f.graphic, d = f.shapeArgs, n = d.d, k = q(g.dial, f.dial);
                    c ? (c.animate(d), d.d = n) : f.graphic = u[f.shapeType](d).attr({
                        rotation: d.rotation,
                        zIndex: 1
                    }).addClass("highcharts-dial").add(b.group);
                    if (!a.styledMode) f.graphic[c ? "animate" : "attr"]({
                        stroke: k.borderColor || "none", "stroke-width": k.borderWidth || 0, fill: k.backgroundColor ||
                            h.neutralColor100
                    })
                });
                d ? d.animate({
                    translateX: c[0],
                    translateY: c[1]
                }) : (b.pivot = u.circle(0, 0, k(f.radius, 5)).attr({zIndex: 2}).addClass("highcharts-pivot").translate(c[0], c[1]).add(b.group), a.styledMode || b.pivot.attr({
                    "stroke-width": f.borderWidth || 0,
                    stroke: f.borderColor || h.neutralColor20,
                    fill: f.backgroundColor || h.neutralColor100
                }))
            };
            a.prototype.animate = function (b) {
                var a = this;
                b || a.points.forEach(function (b) {
                    var c = b.graphic;
                    c && (c.attr({rotation: 180 * a.yAxis.startAngleRad / Math.PI}), c.animate({rotation: b.shapeArgs.rotation},
                        a.options.animation))
                })
            };
            a.prototype.render = function () {
                this.group = this.plotGroup("group", "series", this.visible ? "visible" : "hidden", this.options.zIndex, this.chart.seriesGroup);
                m.prototype.render.call(this);
                this.group.clip(this.chart.clipRect)
            };
            a.prototype.setData = function (b, a) {
                m.prototype.setData.call(this, b, !1);
                this.processData();
                this.generatePoints();
                k(a, !0) && this.chart.redraw()
            };
            a.prototype.hasData = function () {
                return !!this.points.length
            };
            a.defaultOptions = q(m.defaultOptions, {
                dataLabels: {
                    borderColor: h.neutralColor20,
                    borderRadius: 3,
                    borderWidth: 1,
                    crop: !1,
                    defer: !1,
                    enabled: !0,
                    verticalAlign: "top",
                    y: 15,
                    zIndex: 2
                }, dial: {}, pivot: {}, tooltip: {headerFormat: ""}, showInLegend: !1
            });
            return a
        }(m);
        b(a.prototype, {
            angular: !0,
            directTouch: !0,
            drawGraph: d,
            drawTracker: l.prototype.drawTracker,
            fixedBox: !0,
            forceDL: !0,
            noSharedTooltip: !0,
            pointClass: e,
            trackerGroups: ["group", "dataLabelsGroup"]
        });
        c.registerSeriesType("gauge", a);
        "";
        return a
    });
    z(e, "Series/PackedBubble/PackedBubblePoint.js", [e["Core/Chart/Chart.js"], e["Core/Series/Point.js"], e["Core/Series/SeriesRegistry.js"]],
        function (e, d, h) {
            var c = this && this.__extends || function () {
                var a = function (c, d) {
                    a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, c) {
                        a.__proto__ = c
                    } || function (a, c) {
                        for (var d in c) c.hasOwnProperty(d) && (a[d] = c[d])
                    };
                    return a(c, d)
                };
                return function (c, d) {
                    function l() {
                        this.constructor = c
                    }

                    a(c, d);
                    c.prototype = null === d ? Object.create(d) : (l.prototype = d.prototype, new l)
                }
            }();
            return function (a) {
                function h() {
                    var c = null !== a && a.apply(this, arguments) || this;
                    c.degree = NaN;
                    c.mass = NaN;
                    c.radius = NaN;
                    c.options = void 0;
                    c.series = void 0;
                    c.value = null;
                    return c
                }

                c(h, a);
                h.prototype.destroy = function () {
                    this.series.layout && this.series.layout.removeElementFromCollection(this, this.series.layout.nodes);
                    return d.prototype.destroy.apply(this, arguments)
                };
                h.prototype.firePointEvent = function () {
                    var a = this.series.options;
                    if (this.isParentNode && a.parentNode) {
                        var c = a.allowPointSelect;
                        a.allowPointSelect = a.parentNode.allowPointSelect;
                        d.prototype.firePointEvent.apply(this, arguments);
                        a.allowPointSelect = c
                    } else d.prototype.firePointEvent.apply(this,
                        arguments)
                };
                h.prototype.select = function () {
                    var a = this.series.chart;
                    this.isParentNode ? (a.getSelectedPoints = a.getSelectedParentNodes, d.prototype.select.apply(this, arguments), a.getSelectedPoints = e.prototype.getSelectedPoints) : d.prototype.select.apply(this, arguments)
                };
                return h
            }(h.seriesTypes.bubble.prototype.pointClass)
        });
    z(e, "Series/Networkgraph/DraggableNodes.js", [e["Core/Chart/Chart.js"], e["Core/Globals.js"], e["Core/Utilities.js"]], function (e, d, h) {
        var c = h.addEvent;
        d.dragNodesMixin = {
            onMouseDown: function (a,
                                   c) {
                c = this.chart.pointer.normalize(c);
                a.fixedPosition = {chartX: c.chartX, chartY: c.chartY, plotX: a.plotX, plotY: a.plotY};
                a.inDragMode = !0
            }, onMouseMove: function (a, c) {
                if (a.fixedPosition && a.inDragMode) {
                    var d = this.chart, h = d.pointer.normalize(c);
                    c = a.fixedPosition.chartX - h.chartX;
                    h = a.fixedPosition.chartY - h.chartY;
                    var e = void 0, x = void 0, b = d.graphLayoutsLookup;
                    if (5 < Math.abs(c) || 5 < Math.abs(h)) e = a.fixedPosition.plotX - c, x = a.fixedPosition.plotY - h, d.isInsidePlot(e, x) && (a.plotX = e, a.plotY = x, a.hasDragged = !0, this.redrawHalo(a),
                        b.forEach(function (b) {
                            b.restartSimulation()
                        }))
                }
            }, onMouseUp: function (a, c) {
                a.fixedPosition && (a.hasDragged && (this.layout.enableSimulation ? this.layout.start() : this.chart.redraw()), a.inDragMode = a.hasDragged = !1, this.options.fixedDraggable || delete a.fixedPosition)
            }, redrawHalo: function (a) {
                a && this.halo && this.halo.attr({d: a.haloPath(this.options.states.hover.halo.size)})
            }
        };
        c(e, "load", function () {
            var a = this, d, h, e;
            a.container && (d = c(a.container, "mousedown", function (d) {
                var l = a.hoverPoint;
                l && l.series && l.series.hasDraggableNodes &&
                l.series.options.draggable && (l.series.onMouseDown(l, d), h = c(a.container, "mousemove", function (b) {
                    return l && l.series && l.series.onMouseMove(l, b)
                }), e = c(a.container.ownerDocument, "mouseup", function (b) {
                    h();
                    e();
                    return l && l.series && l.series.onMouseUp(l, b)
                }))
            }));
            c(a, "destroy", function () {
                d()
            })
        })
    });
    z(e, "Series/Networkgraph/Integrations.js", [e["Core/Globals.js"]], function (e) {
        e.networkgraphIntegrations = {
            verlet: {
                attractiveForceFunction: function (d, h) {
                    return (h - d) / d
                }, repulsiveForceFunction: function (d, h) {
                    return (h - d) /
                        d * (h > d ? 1 : 0)
                }, barycenter: function () {
                    var d = this.options.gravitationalConstant, h = this.barycenter.xFactor,
                        c = this.barycenter.yFactor;
                    h = (h - (this.box.left + this.box.width) / 2) * d;
                    c = (c - (this.box.top + this.box.height) / 2) * d;
                    this.nodes.forEach(function (a) {
                        a.fixedPosition || (a.plotX -= h / a.mass / a.degree, a.plotY -= c / a.mass / a.degree)
                    })
                }, repulsive: function (d, h, c) {
                    h = h * this.diffTemperature / d.mass / d.degree;
                    d.fixedPosition || (d.plotX += c.x * h, d.plotY += c.y * h)
                }, attractive: function (d, h, c) {
                    var a = d.getMass(), e = -c.x * h * this.diffTemperature;
                    h = -c.y * h * this.diffTemperature;
                    d.fromNode.fixedPosition || (d.fromNode.plotX -= e * a.fromNode / d.fromNode.degree, d.fromNode.plotY -= h * a.fromNode / d.fromNode.degree);
                    d.toNode.fixedPosition || (d.toNode.plotX += e * a.toNode / d.toNode.degree, d.toNode.plotY += h * a.toNode / d.toNode.degree)
                }, integrate: function (d, h) {
                    var c = -d.options.friction, a = d.options.maxSpeed, e = (h.plotX + h.dispX - h.prevX) * c;
                    c *= h.plotY + h.dispY - h.prevY;
                    var m = Math.abs, l = m(e) / (e || 1);
                    m = m(c) / (c || 1);
                    e = l * Math.min(a, Math.abs(e));
                    c = m * Math.min(a, Math.abs(c));
                    h.prevX =
                        h.plotX + h.dispX;
                    h.prevY = h.plotY + h.dispY;
                    h.plotX += e;
                    h.plotY += c;
                    h.temperature = d.vectorLength({x: e, y: c})
                }, getK: function (d) {
                    return Math.pow(d.box.width * d.box.height / d.nodes.length, .5)
                }
            }, euler: {
                attractiveForceFunction: function (d, e) {
                    return d * d / e
                }, repulsiveForceFunction: function (d, e) {
                    return e * e / d
                }, barycenter: function () {
                    var d = this.options.gravitationalConstant, e = this.barycenter.xFactor,
                        c = this.barycenter.yFactor;
                    this.nodes.forEach(function (a) {
                        if (!a.fixedPosition) {
                            var h = a.getDegree();
                            h *= 1 + h / 2;
                            a.dispX += (e - a.plotX) *
                                d * h / a.degree;
                            a.dispY += (c - a.plotY) * d * h / a.degree
                        }
                    })
                }, repulsive: function (d, e, c, a) {
                    d.dispX += c.x / a * e / d.degree;
                    d.dispY += c.y / a * e / d.degree
                }, attractive: function (d, e, c, a) {
                    var h = d.getMass(), m = c.x / a * e;
                    e *= c.y / a;
                    d.fromNode.fixedPosition || (d.fromNode.dispX -= m * h.fromNode / d.fromNode.degree, d.fromNode.dispY -= e * h.fromNode / d.fromNode.degree);
                    d.toNode.fixedPosition || (d.toNode.dispX += m * h.toNode / d.toNode.degree, d.toNode.dispY += e * h.toNode / d.toNode.degree)
                }, integrate: function (d, e) {
                    e.dispX += e.dispX * d.options.friction;
                    e.dispY +=
                        e.dispY * d.options.friction;
                    var c = e.temperature = d.vectorLength({x: e.dispX, y: e.dispY});
                    0 !== c && (e.plotX += e.dispX / c * Math.min(Math.abs(e.dispX), d.temperature), e.plotY += e.dispY / c * Math.min(Math.abs(e.dispY), d.temperature))
                }, getK: function (d) {
                    return Math.pow(d.box.width * d.box.height / d.nodes.length, .3)
                }
            }
        }
    });
    z(e, "Series/Networkgraph/QuadTree.js", [e["Core/Globals.js"], e["Core/Utilities.js"]], function (e, d) {
        d = d.extend;
        var h = e.QuadTreeNode = function (c) {
            this.box = c;
            this.boxSize = Math.min(c.width, c.height);
            this.nodes =
                [];
            this.body = this.isInternal = !1;
            this.isEmpty = !0
        };
        d(h.prototype, {
            insert: function (c, a) {
                this.isInternal ? this.nodes[this.getBoxPosition(c)].insert(c, a - 1) : (this.isEmpty = !1, this.body ? a ? (this.isInternal = !0, this.divideBox(), !0 !== this.body && (this.nodes[this.getBoxPosition(this.body)].insert(this.body, a - 1), this.body = !0), this.nodes[this.getBoxPosition(c)].insert(c, a - 1)) : (a = new h({
                    top: c.plotX,
                    left: c.plotY,
                    width: .1,
                    height: .1
                }), a.body = c, a.isInternal = !1, this.nodes.push(a)) : (this.isInternal = !1, this.body = c))
            }, updateMassAndCenter: function () {
                var c =
                    0, a = 0, d = 0;
                this.isInternal ? (this.nodes.forEach(function (e) {
                    e.isEmpty || (c += e.mass, a += e.plotX * e.mass, d += e.plotY * e.mass)
                }), a /= c, d /= c) : this.body && (c = this.body.mass, a = this.body.plotX, d = this.body.plotY);
                this.mass = c;
                this.plotX = a;
                this.plotY = d
            }, divideBox: function () {
                var c = this.box.width / 2, a = this.box.height / 2;
                this.nodes[0] = new h({left: this.box.left, top: this.box.top, width: c, height: a});
                this.nodes[1] = new h({left: this.box.left + c, top: this.box.top, width: c, height: a});
                this.nodes[2] = new h({
                    left: this.box.left + c, top: this.box.top +
                        a, width: c, height: a
                });
                this.nodes[3] = new h({left: this.box.left, top: this.box.top + a, width: c, height: a})
            }, getBoxPosition: function (c) {
                var a = c.plotY < this.box.top + this.box.height / 2;
                return c.plotX < this.box.left + this.box.width / 2 ? a ? 0 : 3 : a ? 1 : 2
            }
        });
        e = e.QuadTree = function (c, a, d, e) {
            this.box = {left: c, top: a, width: d, height: e};
            this.maxDepth = 25;
            this.root = new h(this.box, "0");
            this.root.isInternal = !0;
            this.root.isRoot = !0;
            this.root.divideBox()
        };
        d(e.prototype, {
            insertNodes: function (c) {
                c.forEach(function (a) {
                    this.root.insert(a, this.maxDepth)
                },
                    this)
            }, visitNodeRecursive: function (c, a, d) {
                var e;
                c || (c = this.root);
                c === this.root && a && (e = a(c));
                !1 !== e && (c.nodes.forEach(function (c) {
                    if (c.isInternal) {
                        a && (e = a(c));
                        if (!1 === e) return;
                        this.visitNodeRecursive(c, a, d)
                    } else c.body && a && a(c.body);
                    d && d(c)
                }, this), c === this.root && d && d(c))
            }, calculateMassAndCenter: function () {
                this.visitNodeRecursive(null, null, function (c) {
                    c.updateMassAndCenter()
                })
            }
        })
    });
    z(e, "Series/Networkgraph/Layouts.js", [e["Core/Chart/Chart.js"], e["Core/Animation/AnimationUtilities.js"], e["Core/Globals.js"],
        e["Core/Utilities.js"]], function (e, d, h, c) {
        var a = d.setAnimation;
        d = c.addEvent;
        var t = c.clamp, m = c.defined, l = c.extend, r = c.isFunction, x = c.pick;
        h.layouts = {
            "reingold-fruchterman": function () {
            }
        };
        l(h.layouts["reingold-fruchterman"].prototype, {
            init: function (b) {
                this.options = b;
                this.nodes = [];
                this.links = [];
                this.series = [];
                this.box = {x: 0, y: 0, width: 0, height: 0};
                this.setInitialRendering(!0);
                this.integration = h.networkgraphIntegrations[b.integration];
                this.enableSimulation = b.enableSimulation;
                this.attractiveForce = x(b.attractiveForce,
                    this.integration.attractiveForceFunction);
                this.repulsiveForce = x(b.repulsiveForce, this.integration.repulsiveForceFunction);
                this.approximation = b.approximation
            }, updateSimulation: function (b) {
                this.enableSimulation = x(b, this.options.enableSimulation)
            }, start: function () {
                var b = this.series, a = this.options;
                this.currentStep = 0;
                this.forces = b[0] && b[0].forces || [];
                this.chart = b[0] && b[0].chart;
                this.initialRendering && (this.initPositions(), b.forEach(function (b) {
                    b.finishedAnimating = !0;
                    b.render()
                }));
                this.setK();
                this.resetSimulation(a);
                this.enableSimulation && this.step()
            }, step: function () {
                var b = this, a = this.series;
                b.currentStep++;
                "barnes-hut" === b.approximation && (b.createQuadTree(), b.quadTree.calculateMassAndCenter());
                b.forces.forEach(function (a) {
                    b[a + "Forces"](b.temperature)
                });
                b.applyLimits(b.temperature);
                b.temperature = b.coolDown(b.startTemperature, b.diffTemperature, b.currentStep);
                b.prevSystemTemperature = b.systemTemperature;
                b.systemTemperature = b.getSystemTemperature();
                b.enableSimulation && (a.forEach(function (b) {
                    b.chart && b.render()
                }),
                    b.maxIterations-- && isFinite(b.temperature) && !b.isStable() ? (b.simulation && h.win.cancelAnimationFrame(b.simulation), b.simulation = h.win.requestAnimationFrame(function () {
                        b.step()
                    })) : b.simulation = !1)
            }, stop: function () {
                this.simulation && h.win.cancelAnimationFrame(this.simulation)
            }, setArea: function (b, a, c, d) {
                this.box = {left: b, top: a, width: c, height: d}
            }, setK: function () {
                this.k = this.options.linkLength || this.integration.getK(this)
            }, addElementsToCollection: function (b, a) {
                b.forEach(function (b) {
                    -1 === a.indexOf(b) && a.push(b)
                })
            },
            removeElementFromCollection: function (b, a) {
                b = a.indexOf(b);
                -1 !== b && a.splice(b, 1)
            }, clear: function () {
                this.nodes.length = 0;
                this.links.length = 0;
                this.series.length = 0;
                this.resetSimulation()
            }, resetSimulation: function () {
                this.forcedStop = !1;
                this.systemTemperature = 0;
                this.setMaxIterations();
                this.setTemperature();
                this.setDiffTemperature()
            }, restartSimulation: function () {
                this.simulation ? this.resetSimulation() : (this.setInitialRendering(!1), this.enableSimulation ? this.start() : this.setMaxIterations(1), this.chart && this.chart.redraw(),
                    this.setInitialRendering(!0))
            }, setMaxIterations: function (b) {
                this.maxIterations = x(b, this.options.maxIterations)
            }, setTemperature: function () {
                this.temperature = this.startTemperature = Math.sqrt(this.nodes.length)
            }, setDiffTemperature: function () {
                this.diffTemperature = this.startTemperature / (this.options.maxIterations + 1)
            }, setInitialRendering: function (b) {
                this.initialRendering = b
            }, createQuadTree: function () {
                this.quadTree = new h.QuadTree(this.box.left, this.box.top, this.box.width, this.box.height);
                this.quadTree.insertNodes(this.nodes)
            },
            initPositions: function () {
                var b = this.options.initialPositions;
                r(b) ? (b.call(this), this.nodes.forEach(function (b) {
                    m(b.prevX) || (b.prevX = b.plotX);
                    m(b.prevY) || (b.prevY = b.plotY);
                    b.dispX = 0;
                    b.dispY = 0
                })) : "circle" === b ? this.setCircularPositions() : this.setRandomPositions()
            }, setCircularPositions: function () {
                function b(a) {
                    a.linksFrom.forEach(function (a) {
                        h[a.toNode.id] || (h[a.toNode.id] = !0, l.push(a.toNode), b(a.toNode))
                    })
                }

                var a = this.box, c = this.nodes, d = 2 * Math.PI / (c.length + 1), e = c.filter(function (b) {
                        return 0 === b.linksTo.length
                    }),
                    l = [], h = {}, m = this.options.initialPositionRadius;
                e.forEach(function (a) {
                    l.push(a);
                    b(a)
                });
                l.length ? c.forEach(function (b) {
                    -1 === l.indexOf(b) && l.push(b)
                }) : l = c;
                l.forEach(function (b, c) {
                    b.plotX = b.prevX = x(b.plotX, a.width / 2 + m * Math.cos(c * d));
                    b.plotY = b.prevY = x(b.plotY, a.height / 2 + m * Math.sin(c * d));
                    b.dispX = 0;
                    b.dispY = 0
                })
            }, setRandomPositions: function () {
                function b(b) {
                    b = b * b / Math.PI;
                    return b -= Math.floor(b)
                }

                var a = this.box, c = this.nodes, d = c.length + 1;
                c.forEach(function (c, e) {
                    c.plotX = c.prevX = x(c.plotX, a.width * b(e));
                    c.plotY =
                        c.prevY = x(c.plotY, a.height * b(d + e));
                    c.dispX = 0;
                    c.dispY = 0
                })
            }, force: function (b) {
                this.integration[b].apply(this, Array.prototype.slice.call(arguments, 1))
            }, barycenterForces: function () {
                this.getBarycenter();
                this.force("barycenter")
            }, getBarycenter: function () {
                var b = 0, a = 0, c = 0;
                this.nodes.forEach(function (d) {
                    a += d.plotX * d.mass;
                    c += d.plotY * d.mass;
                    b += d.mass
                });
                return this.barycenter = {x: a, y: c, xFactor: a / b, yFactor: c / b}
            }, barnesHutApproximation: function (b, a) {
                var c = this.getDistXY(b, a), d = this.vectorLength(c);
                if (b !== a && 0 !==
                    d) if (a.isInternal) if (a.boxSize / d < this.options.theta && 0 !== d) {
                    var e = this.repulsiveForce(d, this.k);
                    this.force("repulsive", b, e * a.mass, c, d);
                    var l = !1
                } else l = !0; else e = this.repulsiveForce(d, this.k), this.force("repulsive", b, e * a.mass, c, d);
                return l
            }, repulsiveForces: function () {
                var b = this;
                "barnes-hut" === b.approximation ? b.nodes.forEach(function (a) {
                    b.quadTree.visitNodeRecursive(null, function (c) {
                        return b.barnesHutApproximation(a, c)
                    })
                }) : b.nodes.forEach(function (a) {
                    b.nodes.forEach(function (c) {
                        if (a !== c && !a.fixedPosition) {
                            var d =
                                b.getDistXY(a, c);
                            var e = b.vectorLength(d);
                            if (0 !== e) {
                                var k = b.repulsiveForce(e, b.k);
                                b.force("repulsive", a, k * c.mass, d, e)
                            }
                        }
                    })
                })
            }, attractiveForces: function () {
                var b = this, a, c, d;
                b.links.forEach(function (e) {
                    e.fromNode && e.toNode && (a = b.getDistXY(e.fromNode, e.toNode), c = b.vectorLength(a), 0 !== c && (d = b.attractiveForce(c, b.k), b.force("attractive", e, d, a, c)))
                })
            }, applyLimits: function () {
                var b = this;
                b.nodes.forEach(function (a) {
                    a.fixedPosition || (b.integration.integrate(b, a), b.applyLimitBox(a, b.box), a.dispX = 0, a.dispY = 0)
                })
            },
            applyLimitBox: function (b, a) {
                var c = b.radius;
                b.plotX = t(b.plotX, a.left + c, a.width - c);
                b.plotY = t(b.plotY, a.top + c, a.height - c)
            }, coolDown: function (a, c, d) {
                return a - c * d
            }, isStable: function () {
                return .00001 > Math.abs(this.systemTemperature - this.prevSystemTemperature) || 0 >= this.temperature
            }, getSystemTemperature: function () {
                return this.nodes.reduce(function (a, c) {
                    return a + c.temperature
                }, 0)
            }, vectorLength: function (a) {
                return Math.sqrt(a.x * a.x + a.y * a.y)
            }, getDistR: function (a, c) {
                a = this.getDistXY(a, c);
                return this.vectorLength(a)
            },
            getDistXY: function (a, c) {
                var b = a.plotX - c.plotX;
                a = a.plotY - c.plotY;
                return {x: b, y: a, absX: Math.abs(b), absY: Math.abs(a)}
            }
        });
        d(e, "predraw", function () {
            this.graphLayoutsLookup && this.graphLayoutsLookup.forEach(function (a) {
                a.stop()
            })
        });
        d(e, "render", function () {
            function b(a) {
                a.maxIterations-- && isFinite(a.temperature) && !a.isStable() && !a.enableSimulation && (a.beforeStep && a.beforeStep(), a.step(), d = !1, c = !0)
            }

            var c = !1;
            if (this.graphLayoutsLookup) {
                a(!1, this);
                for (this.graphLayoutsLookup.forEach(function (a) {
                    a.start()
                }); !d;) {
                    var d =
                        !0;
                    this.graphLayoutsLookup.forEach(b)
                }
                c && this.series.forEach(function (a) {
                    a && a.layout && a.render()
                })
            }
        });
        d(e, "beforePrint", function () {
            this.graphLayoutsLookup && (this.graphLayoutsLookup.forEach(function (a) {
                a.updateSimulation(!1)
            }), this.redraw())
        });
        d(e, "afterPrint", function () {
            this.graphLayoutsLookup && this.graphLayoutsLookup.forEach(function (a) {
                a.updateSimulation()
            });
            this.redraw()
        })
    });
    z(e, "Series/PackedBubble/PackedBubbleComposition.js", [e["Core/Chart/Chart.js"], e["Core/Globals.js"], e["Core/Utilities.js"]],
        function (e, d, h) {
            var c = d.layouts["reingold-fruchterman"], a = h.addEvent, t = h.extendClass, m = h.pick;
            e.prototype.getSelectedParentNodes = function () {
                var a = [];
                this.series.forEach(function (c) {
                    c.parentNode && c.parentNode.selected && a.push(c.parentNode)
                });
                return a
            };
            d.networkgraphIntegrations.packedbubble = {
                repulsiveForceFunction: function (a, c, d, b) {
                    return Math.min(a, (d.marker.radius + b.marker.radius) / 2)
                }, barycenter: function () {
                    var a = this, c = a.options.gravitationalConstant, d = a.box, b = a.nodes, e, k;
                    b.forEach(function (l) {
                        a.options.splitSeries &&
                        !l.isParentNode ? (e = l.series.parentNode.plotX, k = l.series.parentNode.plotY) : (e = d.width / 2, k = d.height / 2);
                        l.fixedPosition || (l.plotX -= (l.plotX - e) * c / (l.mass * Math.sqrt(b.length)), l.plotY -= (l.plotY - k) * c / (l.mass * Math.sqrt(b.length)))
                    })
                }, repulsive: function (a, c, d, b) {
                    var e = c * this.diffTemperature / a.mass / a.degree;
                    c = d.x * e;
                    d = d.y * e;
                    a.fixedPosition || (a.plotX += c, a.plotY += d);
                    b.fixedPosition || (b.plotX -= c, b.plotY -= d)
                }, integrate: d.networkgraphIntegrations.verlet.integrate, getK: d.noop
            };
            d.layouts.packedbubble = t(c, {
                beforeStep: function () {
                    this.options.marker &&
                    this.series.forEach(function (a) {
                        a && a.calculateParentRadius()
                    })
                }, isStable: function () {
                    var a = Math.abs(this.prevSystemTemperature - this.systemTemperature);
                    return 1 > Math.abs(10 * this.systemTemperature / Math.sqrt(this.nodes.length)) && .00001 > a || 0 >= this.temperature
                }, setCircularPositions: function () {
                    var a = this, c = a.box, d = a.nodes, b = 2 * Math.PI / (d.length + 1), e, k,
                        h = a.options.initialPositionRadius;
                    d.forEach(function (d, l) {
                        a.options.splitSeries && !d.isParentNode ? (e = d.series.parentNode.plotX, k = d.series.parentNode.plotY) :
                            (e = c.width / 2, k = c.height / 2);
                        d.plotX = d.prevX = m(d.plotX, e + h * Math.cos(d.index || l * b));
                        d.plotY = d.prevY = m(d.plotY, k + h * Math.sin(d.index || l * b));
                        d.dispX = 0;
                        d.dispY = 0
                    })
                }, repulsiveForces: function () {
                    var a = this, c, d, b, e = a.options.bubblePadding;
                    a.nodes.forEach(function (k) {
                        k.degree = k.mass;
                        k.neighbours = 0;
                        a.nodes.forEach(function (h) {
                            c = 0;
                            k === h || k.fixedPosition || !a.options.seriesInteraction && k.series !== h.series || (b = a.getDistXY(k, h), d = a.vectorLength(b) - (k.marker.radius + h.marker.radius + e), 0 > d && (k.degree += .01, k.neighbours++,
                                c = a.repulsiveForce(-d / Math.sqrt(k.neighbours), a.k, k, h)), a.force("repulsive", k, c * h.mass, b, h, d))
                        })
                    })
                }, applyLimitBox: function (a) {
                    if (this.options.splitSeries && !a.isParentNode && this.options.parentNodeLimit) {
                        var d = this.getDistXY(a, a.series.parentNode);
                        var e = a.series.parentNodeRadius - a.marker.radius - this.vectorLength(d);
                        0 > e && e > -2 * a.marker.radius && (a.plotX -= .01 * d.x, a.plotY -= .01 * d.y)
                    }
                    c.prototype.applyLimitBox.apply(this, arguments)
                }
            });
            a(e, "beforeRedraw", function () {
                this.allDataPoints && delete this.allDataPoints
            })
        });
    z(e, "Series/PackedBubble/PackedBubbleSeries.js", [e["Core/Color/Color.js"], e["Core/Globals.js"], e["Series/PackedBubble/PackedBubblePoint.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"]], function (e, d, h, c, a) {
        var t = this && this.__extends || function () {
                var a = function (b, f) {
                    a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, b) {
                        a.__proto__ = b
                    } || function (a, b) {
                        for (var f in b) b.hasOwnProperty(f) && (a[f] = b[f])
                    };
                    return a(b, f)
                };
                return function (b, f) {
                    function c() {
                        this.constructor = b
                    }

                    a(b,
                        f);
                    b.prototype = null === f ? Object.create(f) : (c.prototype = f.prototype, new c)
                }
            }(), m = e.parse, l = c.series, r = c.seriesTypes.bubble, x = a.addEvent, b = a.clamp, q = a.defined,
            k = a.extend, p = a.fireEvent, v = a.isArray, B = a.isNumber, z = a.merge, D = a.pick, y = d.dragNodesMixin;
        e = function (a) {
            function c() {
                var b = null !== a && a.apply(this, arguments) || this;
                b.chart = void 0;
                b.data = void 0;
                b.layout = void 0;
                b.options = void 0;
                b.points = void 0;
                b.xData = void 0;
                return b
            }

            t(c, a);
            c.prototype.accumulateAllPoints = function (a) {
                var b = a.chart, c = [], f, d;
                for (f = 0; f <
                b.series.length; f++) if (a = b.series[f], a.is("packedbubble") && a.visible || !b.options.chart.ignoreHiddenSeries) for (d = 0; d < a.yData.length; d++) c.push([null, null, a.yData[d], a.index, d, {
                    id: d,
                    marker: {radius: 0}
                }]);
                return c
            };
            c.prototype.addLayout = function () {
                var a = this.options.layoutAlgorithm, b = this.chart.graphLayoutsStorage,
                    c = this.chart.graphLayoutsLookup, g = this.chart.options.chart;
                b || (this.chart.graphLayoutsStorage = b = {}, this.chart.graphLayoutsLookup = c = []);
                var e = b[a.type];
                e || (a.enableSimulation = q(g.forExport) ?
                    !g.forExport : a.enableSimulation, b[a.type] = e = new d.layouts[a.type], e.init(a), c.splice(e.index, 0, e));
                this.layout = e;
                this.points.forEach(function (a) {
                    a.mass = 2;
                    a.degree = 1;
                    a.collisionNmb = 1
                });
                e.setArea(0, 0, this.chart.plotWidth, this.chart.plotHeight);
                e.addElementsToCollection([this], e.series);
                e.addElementsToCollection(this.points, e.nodes)
            };
            c.prototype.addSeriesLayout = function () {
                var a = this.options.layoutAlgorithm, b = this.chart.graphLayoutsStorage,
                    c = this.chart.graphLayoutsLookup,
                    g = z(a, a.parentNodeOptions, {enableSimulation: this.layout.options.enableSimulation});
                var e = b[a.type + "-series"];
                e || (b[a.type + "-series"] = e = new d.layouts[a.type], e.init(g), c.splice(e.index, 0, e));
                this.parentNodeLayout = e;
                this.createParentNodes()
            };
            c.prototype.calculateParentRadius = function () {
                var a = this.seriesBox();
                this.parentNodeRadius = b(Math.sqrt(2 * this.parentNodeMass / Math.PI) + 20, 20, a ? Math.max(Math.sqrt(Math.pow(a.width, 2) + Math.pow(a.height, 2)) / 2 + 20, 20) : Math.sqrt(2 * this.parentNodeMass / Math.PI) + 20);
                this.parentNode && (this.parentNode.marker.radius = this.parentNode.radius = this.parentNodeRadius)
            };
            c.prototype.calculateZExtremes = function () {
                var a = this.options.zMin, b = this.options.zMax, c = Infinity, d = -Infinity;
                if (a && b) return [a, b];
                this.chart.series.forEach(function (a) {
                    a.yData.forEach(function (a) {
                        q(a) && (a > d && (d = a), a < c && (c = a))
                    })
                });
                a = D(a, c);
                b = D(b, d);
                return [a, b]
            };
            c.prototype.checkOverlap = function (a, b) {
                var c = a[0] - b[0], f = a[1] - b[1];
                return -.001 > Math.sqrt(c * c + f * f) - Math.abs(a[2] + b[2])
            };
            c.prototype.createParentNodes = function () {
                var a = this, b = a.chart, c = a.parentNodeLayout, d, g = a.parentNode, e = a.pointClass;
                a.parentNodeMass =
                    0;
                a.points.forEach(function (b) {
                    a.parentNodeMass += Math.PI * Math.pow(b.marker.radius, 2)
                });
                a.calculateParentRadius();
                c.nodes.forEach(function (b) {
                    b.seriesIndex === a.index && (d = !0)
                });
                c.setArea(0, 0, b.plotWidth, b.plotHeight);
                d || (g || (g = (new e).init(this, {
                    mass: a.parentNodeRadius / 2,
                    marker: {radius: a.parentNodeRadius},
                    dataLabels: {inside: !1},
                    dataLabelOnNull: !0,
                    degree: a.parentNodeRadius,
                    isParentNode: !0,
                    seriesIndex: a.index
                })), a.parentNode && (g.plotX = a.parentNode.plotX, g.plotY = a.parentNode.plotY), a.parentNode = g, c.addElementsToCollection([a],
                    c.series), c.addElementsToCollection([g], c.nodes))
            };
            c.prototype.deferLayout = function () {
                var a = this.options.layoutAlgorithm;
                this.visible && (this.addLayout(), a.splitSeries && this.addSeriesLayout())
            };
            c.prototype.destroy = function () {
                this.chart.graphLayoutsLookup && this.chart.graphLayoutsLookup.forEach(function (a) {
                    a.removeElementFromCollection(this, a.series)
                }, this);
                this.parentNode && this.parentNodeLayout && (this.parentNodeLayout.removeElementFromCollection(this.parentNode, this.parentNodeLayout.nodes), this.parentNode.dataLabel &&
                (this.parentNode.dataLabel = this.parentNode.dataLabel.destroy()));
                l.prototype.destroy.apply(this, arguments)
            };
            c.prototype.drawDataLabels = function () {
                var a = this.options.dataLabels.textPath, b = this.points;
                l.prototype.drawDataLabels.apply(this, arguments);
                this.parentNode && (this.parentNode.formatPrefix = "parentNode", this.points = [this.parentNode], this.options.dataLabels.textPath = this.options.dataLabels.parentNodeTextPath, l.prototype.drawDataLabels.apply(this, arguments), this.points = b, this.options.dataLabels.textPath =
                    a)
            };
            c.prototype.drawGraph = function () {
                if (this.layout && this.layout.options.splitSeries) {
                    var a = this.chart;
                    var b = this.layout.options.parentNodeOptions.marker;
                    var c = {
                        fill: b.fillColor || m(this.color).brighten(.4).get(),
                        opacity: b.fillOpacity,
                        stroke: b.lineColor || this.color,
                        "stroke-width": b.lineWidth
                    };
                    this.parentNodesGroup || (this.parentNodesGroup = this.plotGroup("parentNodesGroup", "parentNode", this.visible ? "inherit" : "hidden", .1, a.seriesGroup), this.group.attr({zIndex: 2}));
                    this.calculateParentRadius();
                    b = z({
                        x: this.parentNode.plotX -
                            this.parentNodeRadius,
                        y: this.parentNode.plotY - this.parentNodeRadius,
                        width: 2 * this.parentNodeRadius,
                        height: 2 * this.parentNodeRadius
                    }, c);
                    this.parentNode.graphic || (this.graph = this.parentNode.graphic = a.renderer.symbol(c.symbol).add(this.parentNodesGroup));
                    this.parentNode.graphic.attr(b)
                }
            };
            c.prototype.drawTracker = function () {
                var b = this.parentNode;
                a.prototype.drawTracker.call(this);
                if (b) {
                    var c = v(b.dataLabels) ? b.dataLabels : b.dataLabel ? [b.dataLabel] : [];
                    b.graphic && (b.graphic.element.point = b);
                    c.forEach(function (a) {
                        a.div ?
                            a.div.point = b : a.element.point = b
                    })
                }
            };
            c.prototype.getPointRadius = function () {
                var a = this, c = a.chart, d = a.options, g = d.useSimulation, e = Math.min(c.plotWidth, c.plotHeight),
                    k = {}, h = [], l = c.allDataPoints, q, p, m, r;
                ["minSize", "maxSize"].forEach(function (a) {
                    var b = parseInt(d[a], 10), c = /%$/.test(d[a]);
                    k[a] = c ? e * b / 100 : b * Math.sqrt(l.length)
                });
                c.minRadius = q = k.minSize / Math.sqrt(l.length);
                c.maxRadius = p = k.maxSize / Math.sqrt(l.length);
                var v = g ? a.calculateZExtremes() : [q, p];
                (l || []).forEach(function (c, f) {
                    m = g ? b(c[2], v[0], v[1]) : c[2];
                    r = a.getRadius(v[0], v[1], q, p, m);
                    0 === r && (r = null);
                    l[f][2] = r;
                    h.push(r)
                });
                a.radii = h
            };
            c.prototype.init = function () {
                l.prototype.init.apply(this, arguments);
                this.eventsToUnbind.push(x(this, "updatedData", function () {
                    this.chart.series.forEach(function (a) {
                        a.type === this.type && (a.isDirty = !0)
                    }, this)
                }));
                return this
            };
            c.prototype.onMouseUp = function (a) {
                if (a.fixedPosition && !a.removed) {
                    var b, c, f = this.layout, d = this.parentNodeLayout;
                    d && f.options.dragBetweenSeries && d.nodes.forEach(function (d) {
                        a && a.marker && d !== a.series.parentNode &&
                        (b = f.getDistXY(a, d), c = f.vectorLength(b) - d.marker.radius - a.marker.radius, 0 > c && (d.series.addPoint(z(a.options, {
                            plotX: a.plotX,
                            plotY: a.plotY
                        }), !1), f.removeElementFromCollection(a, f.nodes), a.remove()))
                    });
                    y.onMouseUp.apply(this, arguments)
                }
            };
            c.prototype.placeBubbles = function (a) {
                var b = this.checkOverlap, c = this.positionBubble, f = [], d = 1, g = 0, e = 0;
                var k = [];
                var h;
                a = a.sort(function (a, b) {
                    return b[2] - a[2]
                });
                if (a.length) {
                    f.push([[0, 0, a[0][2], a[0][3], a[0][4]]]);
                    if (1 < a.length) for (f.push([[0, 0 - a[1][2] - a[0][2], a[1][2], a[1][3],
                        a[1][4]]]), h = 2; h < a.length; h++) a[h][2] = a[h][2] || 1, k = c(f[d][g], f[d - 1][e], a[h]), b(k, f[d][0]) ? (f.push([]), e = 0, f[d + 1].push(c(f[d][g], f[d][0], a[h])), d++, g = 0) : 1 < d && f[d - 1][e + 1] && b(k, f[d - 1][e + 1]) ? (e++, f[d].push(c(f[d][g], f[d - 1][e], a[h])), g++) : (g++, f[d].push(k));
                    this.chart.stages = f;
                    this.chart.rawPositions = [].concat.apply([], f);
                    this.resizeRadius();
                    k = this.chart.rawPositions
                }
                return k
            };
            c.prototype.positionBubble = function (a, b, c) {
                var f = Math.sqrt, d = Math.asin, g = Math.acos, e = Math.pow, k = Math.abs;
                f = f(e(a[0] - b[0], 2) + e(a[1] -
                b[1], 2));
                g = g((e(f, 2) + e(c[2] + b[2], 2) - e(c[2] + a[2], 2)) / (2 * (c[2] + b[2]) * f));
                d = d(k(a[0] - b[0]) / f);
                a = (0 > a[1] - b[1] ? 0 : Math.PI) + g + d * (0 > (a[0] - b[0]) * (a[1] - b[1]) ? 1 : -1);
                return [b[0] + (b[2] + c[2]) * Math.sin(a), b[1] - (b[2] + c[2]) * Math.cos(a), c[2], c[3], c[4]]
            };
            c.prototype.render = function () {
                var a = [];
                l.prototype.render.apply(this, arguments);
                this.options.dataLabels.allowOverlap || (this.data.forEach(function (b) {
                    v(b.dataLabels) && b.dataLabels.forEach(function (b) {
                        a.push(b)
                    })
                }), this.options.useSimulation && this.chart.hideOverlappingLabels(a))
            };
            c.prototype.resizeRadius = function () {
                var a = this.chart, b = a.rawPositions, c = Math.min, d = Math.max, g = a.plotLeft, e = a.plotTop,
                    k = a.plotHeight, h = a.plotWidth, l, q, p;
                var m = l = Number.POSITIVE_INFINITY;
                var r = q = Number.NEGATIVE_INFINITY;
                for (p = 0; p < b.length; p++) {
                    var v = b[p][2];
                    m = c(m, b[p][0] - v);
                    r = d(r, b[p][0] + v);
                    l = c(l, b[p][1] - v);
                    q = d(q, b[p][1] + v)
                }
                p = [r - m, q - l];
                c = c.apply([], [(h - g) / p[0], (k - e) / p[1]]);
                if (1e-10 < Math.abs(c - 1)) {
                    for (p = 0; p < b.length; p++) b[p][2] *= c;
                    this.placeBubbles(b)
                } else a.diffY = k / 2 + e - l - (q - l) / 2, a.diffX = h / 2 + g - m - (r -
                    m) / 2
            };
            c.prototype.seriesBox = function () {
                var a = this.chart, b = Math.max, c = Math.min, d,
                    g = [a.plotLeft, a.plotLeft + a.plotWidth, a.plotTop, a.plotTop + a.plotHeight];
                this.data.forEach(function (a) {
                    q(a.plotX) && q(a.plotY) && a.marker.radius && (d = a.marker.radius, g[0] = c(g[0], a.plotX - d), g[1] = b(g[1], a.plotX + d), g[2] = c(g[2], a.plotY - d), g[3] = b(g[3], a.plotY + d))
                });
                return B(g.width / g.height) ? g : null
            };
            c.prototype.setVisible = function () {
                var a = this;
                l.prototype.setVisible.apply(a, arguments);
                a.parentNodeLayout && a.graph ? a.visible ? (a.graph.show(),
                a.parentNode.dataLabel && a.parentNode.dataLabel.show()) : (a.graph.hide(), a.parentNodeLayout.removeElementFromCollection(a.parentNode, a.parentNodeLayout.nodes), a.parentNode.dataLabel && a.parentNode.dataLabel.hide()) : a.layout && (a.visible ? a.layout.addElementsToCollection(a.points, a.layout.nodes) : a.points.forEach(function (b) {
                    a.layout.removeElementFromCollection(b, a.layout.nodes)
                }))
            };
            c.prototype.translate = function () {
                var a = this.chart, b = this.data, c = this.index, d, g = this.options.useSimulation;
                this.processedXData =
                    this.xData;
                this.generatePoints();
                q(a.allDataPoints) || (a.allDataPoints = this.accumulateAllPoints(this), this.getPointRadius());
                if (g) var e = a.allDataPoints; else e = this.placeBubbles(a.allDataPoints), this.options.draggable = !1;
                for (d = 0; d < e.length; d++) if (e[d][3] === c) {
                    var h = b[e[d][4]];
                    var l = D(e[d][2], void 0);
                    g || (h.plotX = e[d][0] - a.plotLeft + a.diffX, h.plotY = e[d][1] - a.plotTop + a.diffY);
                    B(l) && (h.marker = k(h.marker, {radius: l, width: 2 * l, height: 2 * l}), h.radius = l)
                }
                g && this.deferLayout();
                p(this, "afterTranslate")
            };
            c.defaultOptions =
                z(r.defaultOptions, {
                    minSize: "10%",
                    maxSize: "50%",
                    sizeBy: "area",
                    zoneAxis: "y",
                    crisp: !1,
                    tooltip: {pointFormat: "Value: {point.value}"},
                    draggable: !0,
                    useSimulation: !0,
                    parentNode: {allowPointSelect: !1},
                    dataLabels: {
                        formatter: function () {
                            var a = this.series.chart.numberFormatter, b = this.point.value;
                            return B(b) ? a(b, -1) : ""
                        }, parentNodeFormatter: function () {
                            return this.name
                        }, parentNodeTextPath: {enabled: !0}, padding: 0, style: {transition: "opacity 2000ms"}
                    },
                    layoutAlgorithm: {
                        initialPositions: "circle",
                        initialPositionRadius: 20,
                        bubblePadding: 5,
                        parentNodeLimit: !1,
                        seriesInteraction: !0,
                        dragBetweenSeries: !1,
                        parentNodeOptions: {
                            maxIterations: 400,
                            gravitationalConstant: .03,
                            maxSpeed: 50,
                            initialPositionRadius: 100,
                            seriesInteraction: !0,
                            marker: {fillColor: null, fillOpacity: 1, lineWidth: 1, lineColor: null, symbol: "circle"}
                        },
                        enableSimulation: !0,
                        type: "packedbubble",
                        integration: "packedbubble",
                        maxIterations: 1E3,
                        splitSeries: !1,
                        maxSpeed: 5,
                        gravitationalConstant: .01,
                        friction: -.981
                    }
                });
            return c
        }(r);
        k(e.prototype, {
            alignDataLabel: l.prototype.alignDataLabel,
            axisTypes: [],
            directTouch: !0,
            forces: ["barycenter", "repulsive"],
            hasDraggableNodes: !0,
            isCartesian: !1,
            noSharedTooltip: !0,
            onMouseDown: y.onMouseDown,
            onMouseMove: y.onMouseMove,
            pointArrayMap: ["value"],
            pointClass: h,
            pointValKey: "value",
            redrawHalo: y.redrawHalo,
            requireSorting: !1,
            searchPoint: d.noop,
            trackerGroups: ["group", "dataLabelsGroup", "parentNodesGroup"]
        });
        c.registerSeriesType("packedbubble", e);
        "";
        "";
        return e
    });
    z(e, "Series/Polygon/PolygonSeries.js", [e["Core/Globals.js"], e["Core/Legend/LegendSymbol.js"], e["Core/Series/SeriesRegistry.js"],
        e["Core/Utilities.js"]], function (e, d, h, c) {
        var a = this && this.__extends || function () {
            var a = function (b, c) {
                a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, b) {
                    a.__proto__ = b
                } || function (a, b) {
                    for (var c in b) b.hasOwnProperty(c) && (a[c] = b[c])
                };
                return a(b, c)
            };
            return function (b, c) {
                function d() {
                    this.constructor = b
                }

                a(b, c);
                b.prototype = null === c ? Object.create(c) : (d.prototype = c.prototype, new d)
            }
        }();
        e = e.noop;
        var t = h.series, m = h.seriesTypes, l = m.area, r = m.line, x = m.scatter;
        m = c.extend;
        var b = c.merge;
        c = function (c) {
            function d() {
                var a =
                    null !== c && c.apply(this, arguments) || this;
                a.data = void 0;
                a.options = void 0;
                a.points = void 0;
                return a
            }

            a(d, c);
            d.prototype.getGraphPath = function () {
                for (var a = r.prototype.getGraphPath.call(this), b = a.length + 1; b--;) (b === a.length || "M" === a[b][0]) && 0 < b && a.splice(b, 0, ["Z"]);
                return this.areaPath = a
            };
            d.prototype.drawGraph = function () {
                this.options.fillColor = this.color;
                l.prototype.drawGraph.call(this)
            };
            d.defaultOptions = b(x.defaultOptions, {
                marker: {enabled: !1, states: {hover: {enabled: !1}}}, stickyTracking: !1, tooltip: {
                    followPointer: !0,
                    pointFormat: ""
                }, trackByArea: !0
            });
            return d
        }(x);
        m(c.prototype, {
            type: "polygon",
            drawLegendSymbol: d.drawRectangle,
            drawTracker: t.prototype.drawTracker,
            setStackedPoints: e
        });
        h.registerSeriesType("polygon", c);
        "";
        return c
    });
    z(e, "Core/Axis/WaterfallAxis.js", [e["Extensions/Stacking.js"], e["Core/Utilities.js"]], function (e, d) {
        var h = d.addEvent, c = d.objectEach, a;
        (function (a) {
            function d() {
                var a = this.waterfall.stacks;
                a && (a.changed = !1, delete a.alreadyChanged)
            }

            function l() {
                var a = this.options.stackLabels;
                a && a.enabled &&
                this.waterfall.stacks && this.waterfall.renderStackTotals()
            }

            function r() {
                for (var a = this.axes, b = this.series, c = b.length; c--;) b[c].options.stacking && (a.forEach(function (a) {
                    a.isXAxis || (a.waterfall.stacks.changed = !0)
                }), c = 0)
            }

            function x() {
                this.waterfall || (this.waterfall = new b(this))
            }

            var b = function () {
                function a(a) {
                    this.axis = a;
                    this.stacks = {changed: !1}
                }

                a.prototype.renderStackTotals = function () {
                    var a = this.axis, b = a.waterfall.stacks, d = a.stacking && a.stacking.stackTotalGroup,
                        h = new e(a, a.options.stackLabels, !1, 0, void 0);
                    this.dummyStackItem = h;
                    c(b, function (a) {
                        c(a, function (a) {
                            h.total = a.stackTotal;
                            a.label && (h.label = a.label);
                            e.prototype.render.call(h, d);
                            a.label = h.label;
                            delete h.label
                        })
                    });
                    h.total = null
                };
                return a
            }();
            a.Composition = b;
            a.compose = function (a, b) {
                h(a, "init", x);
                h(a, "afterBuildStacks", d);
                h(a, "afterRender", l);
                h(b, "beforeRedraw", r)
            }
        })(a || (a = {}));
        return a
    });
    z(e, "Series/Waterfall/WaterfallPoint.js", [e["Series/Column/ColumnSeries.js"], e["Core/Series/Point.js"], e["Core/Utilities.js"]], function (e, d, h) {
        var c = this && this.__extends ||
            function () {
                var a = function (c, d) {
                    a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, c) {
                        a.__proto__ = c
                    } || function (a, c) {
                        for (var b in c) c.hasOwnProperty(b) && (a[b] = c[b])
                    };
                    return a(c, d)
                };
                return function (c, d) {
                    function e() {
                        this.constructor = c
                    }

                    a(c, d);
                    c.prototype = null === d ? Object.create(d) : (e.prototype = d.prototype, new e)
                }
            }(), a = h.isNumber;
        return function (e) {
            function h() {
                var a = null !== e && e.apply(this, arguments) || this;
                a.options = void 0;
                a.series = void 0;
                return a
            }

            c(h, e);
            h.prototype.getClassName = function () {
                var a =
                    d.prototype.getClassName.call(this);
                this.isSum ? a += " highcharts-sum" : this.isIntermediateSum && (a += " highcharts-intermediate-sum");
                return a
            };
            h.prototype.isValid = function () {
                return a(this.y) || this.isSum || !!this.isIntermediateSum
            };
            return h
        }(e.prototype.pointClass)
    });
    z(e, "Series/Waterfall/WaterfallSeries.js", [e["Core/Axis/Axis.js"], e["Core/Chart/Chart.js"], e["Core/Color/Palette.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Utilities.js"], e["Core/Axis/WaterfallAxis.js"], e["Series/Waterfall/WaterfallPoint.js"]],
        function (e, d, h, c, a, t, m) {
            var l = this && this.__extends || function () {
                var a = function (b, c) {
                    a = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (a, b) {
                        a.__proto__ = b
                    } || function (a, b) {
                        for (var c in b) b.hasOwnProperty(c) && (a[c] = b[c])
                    };
                    return a(b, c)
                };
                return function (b, c) {
                    function d() {
                        this.constructor = b
                    }

                    a(b, c);
                    b.prototype = null === c ? Object.create(c) : (d.prototype = c.prototype, new d)
                }
            }(), r = c.seriesTypes, x = r.column, b = r.line, q = a.arrayMax, k = a.arrayMin, p = a.correctFloat;
            r = a.extend;
            var v = a.isNumber, z = a.merge, D =
                a.objectEach, L = a.pick;
            a = function (a) {
                function c() {
                    var b = null !== a && a.apply(this, arguments) || this;
                    b.chart = void 0;
                    b.data = void 0;
                    b.options = void 0;
                    b.points = void 0;
                    b.stackedYNeg = void 0;
                    b.stackedYPos = void 0;
                    b.stackKey = void 0;
                    b.xData = void 0;
                    b.yAxis = void 0;
                    b.yData = void 0;
                    return b
                }

                l(c, a);
                c.prototype.generatePoints = function () {
                    var a;
                    x.prototype.generatePoints.apply(this);
                    var b = 0;
                    for (a = this.points.length; b < a; b++) {
                        var c = this.points[b];
                        var d = this.processedYData[b];
                        if (c.isIntermediateSum || c.isSum) c.y = p(d)
                    }
                };
                c.prototype.translate =
                    function () {
                        var a = this.options, b = this.yAxis, c = L(a.minPointLength, 5), d = c / 2,
                            e = a.threshold || 0, h = e, k = e;
                        a = a.stacking;
                        var l = b.waterfall.stacks[this.stackKey];
                        x.prototype.translate.apply(this);
                        for (var q = this.points, p = 0; p < q.length; p++) {
                            var m = q[p];
                            var r = this.processedYData[p];
                            var C = m.shapeArgs;
                            if (C && v(r)) {
                                var t = [0, r];
                                var G = m.y;
                                if (a) {
                                    if (l) {
                                        t = l[p];
                                        if ("overlap" === a) {
                                            var y = t.stackState[t.stateIndex--];
                                            y = 0 <= G ? y : y - G;
                                            Object.hasOwnProperty.call(t, "absolutePos") && delete t.absolutePos;
                                            Object.hasOwnProperty.call(t, "absoluteNeg") &&
                                            delete t.absoluteNeg
                                        } else 0 <= G ? (y = t.threshold + t.posTotal, t.posTotal -= G) : (y = t.threshold + t.negTotal, t.negTotal -= G, y -= G), !t.posTotal && Object.hasOwnProperty.call(t, "absolutePos") && (t.posTotal = t.absolutePos, delete t.absolutePos), !t.negTotal && Object.hasOwnProperty.call(t, "absoluteNeg") && (t.negTotal = t.absoluteNeg, delete t.absoluteNeg);
                                        m.isSum || (t.connectorThreshold = t.threshold + t.stackTotal);
                                        b.reversed ? (r = 0 <= G ? y - G : y + G, G = y) : (r = y, G = y - G);
                                        m.below = r <= e;
                                        C.y = b.translate(r, !1, !0, !1, !0) || 0;
                                        C.height = Math.abs(C.y - (b.translate(G,
                                            !1, !0, !1, !0) || 0));
                                        if (G = b.waterfall.dummyStackItem) G.x = p, G.label = l[p].label, G.setOffset(this.pointXOffset || 0, this.barW || 0, this.stackedYNeg[p], this.stackedYPos[p])
                                    }
                                } else y = Math.max(h, h + G) + t[0], C.y = b.translate(y, !1, !0, !1, !0) || 0, m.isSum ? (C.y = b.translate(t[1], !1, !0, !1, !0) || 0, C.height = Math.min(b.translate(t[0], !1, !0, !1, !0) || 0, b.len) - C.y, m.below = t[1] <= e) : m.isIntermediateSum ? (0 <= G ? (r = t[1] + k, G = k) : (r = k, G = t[1] + k), b.reversed && (r ^= G, G ^= r, r ^= G), C.y = b.translate(r, !1, !0, !1, !0) || 0, C.height = Math.abs(C.y - Math.min(b.translate(G,
                                    !1, !0, !1, !0) || 0, b.len)), k += t[1], m.below = r <= e) : (C.height = 0 < r ? (b.translate(h, !1, !0, !1, !0) || 0) - C.y : (b.translate(h, !1, !0, !1, !0) || 0) - (b.translate(h - r, !1, !0, !1, !0) || 0), h += r, m.below = h < e), 0 > C.height && (C.y += C.height, C.height *= -1);
                                m.plotY = C.y = Math.round(C.y || 0) - this.borderWidth % 2 / 2;
                                C.height = Math.max(Math.round(C.height || 0), .001);
                                m.yBottom = C.y + C.height;
                                C.height <= c && !m.isNull ? (C.height = c, C.y -= d, m.plotY = C.y, m.minPointLengthOffset = 0 > m.y ? -d : d) : (m.isNull && (C.width = 0), m.minPointLengthOffset = 0);
                                G = m.plotY + (m.negative ?
                                    C.height : 0);
                                m.below && (m.plotY += C.height);
                                m.tooltipPos && (this.chart.inverted ? m.tooltipPos[0] = b.len - G : m.tooltipPos[1] = G)
                            }
                        }
                    };
                c.prototype.processData = function (b) {
                    var c = this.options, d = this.yData, g = c.data, e = d.length, h = c.threshold || 0, k, l, m, q, r;
                    for (r = l = k = m = q = 0; r < e; r++) {
                        var t = d[r];
                        var v = g && g[r] ? g[r] : {};
                        "sum" === t || v.isSum ? d[r] = p(l) : "intermediateSum" === t || v.isIntermediateSum ? (d[r] = p(k), k = 0) : (l += t, k += t);
                        m = Math.min(l, m);
                        q = Math.max(l, q)
                    }
                    a.prototype.processData.call(this, b);
                    c.stacking || (this.dataMin = m + h, this.dataMax =
                        q)
                };
                c.prototype.toYData = function (a) {
                    return a.isSum ? "sum" : a.isIntermediateSum ? "intermediateSum" : a.y
                };
                c.prototype.updateParallelArrays = function (b, c) {
                    a.prototype.updateParallelArrays.call(this, b, c);
                    if ("sum" === this.yData[0] || "intermediateSum" === this.yData[0]) this.yData[0] = null
                };
                c.prototype.pointAttribs = function (a, b) {
                    var c = this.options.upColor;
                    c && !a.options.color && (a.color = 0 < a.y ? c : null);
                    a = x.prototype.pointAttribs.call(this, a, b);
                    delete a.dashstyle;
                    return a
                };
                c.prototype.getGraphPath = function () {
                    return [["M",
                        0, 0]]
                };
                c.prototype.getCrispPath = function () {
                    var a = this.data, b = this.yAxis, c = a.length, d = Math.round(this.graph.strokeWidth()) % 2 / 2,
                        e = Math.round(this.borderWidth) % 2 / 2, h = this.xAxis.reversed, k = this.yAxis.reversed,
                        l = this.options.stacking, m = [], p;
                    for (p = 1; p < c; p++) {
                        var q = a[p].shapeArgs;
                        var r = a[p - 1];
                        var t = a[p - 1].shapeArgs;
                        var v = b.waterfall.stacks[this.stackKey];
                        var x = 0 < r.y ? -t.height : 0;
                        v && t && q && (v = v[p - 1], l ? (v = v.connectorThreshold, x = Math.round(b.translate(v, 0, 1, 0, 1) + (k ? x : 0)) - d) : x = t.y + r.minPointLengthOffset + e - d, m.push(["M",
                            (t.x || 0) + (h ? 0 : t.width || 0), x], ["L", (q.x || 0) + (h ? q.width || 0 : 0), x]));
                        t && m.length && (!l && 0 > r.y && !k || 0 < r.y && k) && ((r = m[m.length - 2]) && "number" === typeof r[2] && (r[2] += t.height || 0), (r = m[m.length - 1]) && "number" === typeof r[2] && (r[2] += t.height || 0))
                    }
                    return m
                };
                c.prototype.drawGraph = function () {
                    b.prototype.drawGraph.call(this);
                    this.graph.attr({d: this.getCrispPath()})
                };
                c.prototype.setStackedPoints = function () {
                    function a(a, b, c, d) {
                        if (H) for (c; c < H; c++) z.stackState[c] += d; else z.stackState[0] = a, H = z.stackState.length;
                        z.stackState.push(z.stackState[H -
                        1] + b)
                    }

                    var b = this.options, c = this.yAxis.waterfall.stacks, d = b.threshold, e = d || 0, h = e,
                        k = this.stackKey, l = this.xData, m = l.length, p, q, r;
                    this.yAxis.stacking.usePercentage = !1;
                    var t = q = r = e;
                    if (this.visible || !this.chart.options.chart.ignoreHiddenSeries) {
                        var v = c.changed;
                        (p = c.alreadyChanged) && 0 > p.indexOf(k) && (v = !0);
                        c[k] || (c[k] = {});
                        p = c[k];
                        for (var x = 0; x < m; x++) {
                            var y = l[x];
                            if (!p[y] || v) p[y] = {
                                negTotal: 0,
                                posTotal: 0,
                                stackTotal: 0,
                                threshold: 0,
                                stateIndex: 0,
                                stackState: [],
                                label: v && p[y] ? p[y].label : void 0
                            };
                            var z = p[y];
                            var E = this.yData[x];
                            0 <= E ? z.posTotal += E : z.negTotal += E;
                            var B = b.data[x];
                            y = z.absolutePos = z.posTotal;
                            var D = z.absoluteNeg = z.negTotal;
                            z.stackTotal = y + D;
                            var H = z.stackState.length;
                            B && B.isIntermediateSum ? (a(r, q, 0, r), r = q, q = d, e ^= h, h ^= e, e ^= h) : B && B.isSum ? (a(d, t, H), e = d) : (a(e, E, 0, t), B && (t += E, q += E));
                            z.stateIndex++;
                            z.threshold = e;
                            e += z.stackTotal
                        }
                        c.changed = !1;
                        c.alreadyChanged || (c.alreadyChanged = []);
                        c.alreadyChanged.push(k)
                    }
                };
                c.prototype.getExtremes = function () {
                    var a = this.options.stacking;
                    if (a) {
                        var b = this.yAxis;
                        b = b.waterfall.stacks;
                        var c =
                            this.stackedYNeg = [];
                        var d = this.stackedYPos = [];
                        "overlap" === a ? D(b[this.stackKey], function (a) {
                            c.push(k(a.stackState));
                            d.push(q(a.stackState))
                        }) : D(b[this.stackKey], function (a) {
                            c.push(a.negTotal + a.threshold);
                            d.push(a.posTotal + a.threshold)
                        });
                        return {dataMin: k(c), dataMax: q(d)}
                    }
                    return {dataMin: this.dataMin, dataMax: this.dataMax}
                };
                c.defaultOptions = z(x.defaultOptions, {
                    dataLabels: {inside: !0},
                    lineWidth: 1,
                    lineColor: h.neutralColor80,
                    dashStyle: "Dot",
                    borderColor: h.neutralColor80,
                    states: {hover: {lineWidthPlus: 0}}
                });
                return c
            }(x);
            r(a.prototype, {getZonesGraphs: b.prototype.getZonesGraphs, pointValKey: "y", showLine: !0, pointClass: m});
            c.registerSeriesType("waterfall", a);
            t.compose(e, d);
            "";
            return a
        });
    z(e, "Extensions/Polar.js", [e["Core/Animation/AnimationUtilities.js"], e["Core/Chart/Chart.js"], e["Core/Globals.js"], e["Extensions/Pane.js"], e["Core/Pointer.js"], e["Core/Series/Series.js"], e["Core/Series/SeriesRegistry.js"], e["Core/Renderer/SVG/SVGRenderer.js"], e["Core/Utilities.js"]], function (e, d, h, c, a, t, m, l, r) {
        var x = e.animObject;
        m = m.seriesTypes;
        var b = r.addEvent, q = r.defined, k = r.find, p = r.isNumber, v = r.pick, z = r.splat, H = r.uniqueKey;
        e = r.wrap;
        var D = t.prototype;
        a = a.prototype;
        D.searchPointByAngle = function (a) {
            var b = this.chart, c = this.xAxis.pane.center;
            return this.searchKDTree({clientX: 180 + -180 / Math.PI * Math.atan2(a.chartX - c[0] - b.plotLeft, a.chartY - c[1] - b.plotTop)})
        };
        D.getConnectors = function (a, b, c, d) {
            var f = d ? 1 : 0;
            var e = 0 <= b && b <= a.length - 1 ? b : 0 > b ? a.length - 1 + b : 0;
            b = 0 > e - 1 ? a.length - (1 + f) : e - 1;
            f = e + 1 > a.length - 1 ? f : e + 1;
            var g = a[b];
            f = a[f];
            var h = g.plotX;
            g = g.plotY;
            var k =
                f.plotX;
            var l = f.plotY;
            f = a[e].plotX;
            e = a[e].plotY;
            h = (1.5 * f + h) / 2.5;
            g = (1.5 * e + g) / 2.5;
            k = (1.5 * f + k) / 2.5;
            var n = (1.5 * e + l) / 2.5;
            l = Math.sqrt(Math.pow(h - f, 2) + Math.pow(g - e, 2));
            var m = Math.sqrt(Math.pow(k - f, 2) + Math.pow(n - e, 2));
            h = Math.atan2(g - e, h - f);
            n = Math.PI / 2 + (h + Math.atan2(n - e, k - f)) / 2;
            Math.abs(h - n) > Math.PI / 2 && (n -= Math.PI);
            h = f + Math.cos(n) * l;
            g = e + Math.sin(n) * l;
            k = f + Math.cos(Math.PI + n) * m;
            n = e + Math.sin(Math.PI + n) * m;
            f = {rightContX: k, rightContY: n, leftContX: h, leftContY: g, plotX: f, plotY: e};
            c && (f.prevPointCont = this.getConnectors(a,
                b, !1, d));
            return f
        };
        D.toXY = function (a) {
            var b = this.chart, c = this.xAxis, d = this.yAxis, e = a.plotX, g = a.plotY, h = a.series, k = b.inverted,
                l = a.y, m = k ? e : d.len - g;
            k && h && !h.isRadialBar && (a.plotY = g = "number" === typeof l ? d.translate(l) || 0 : 0);
            a.rectPlotX = e;
            a.rectPlotY = g;
            d.center && (m += d.center[3] / 2);
            p(g) && (d = k ? d.postTranslate(g, m) : c.postTranslate(e, m), a.plotX = a.polarPlotX = d.x - b.plotLeft, a.plotY = a.polarPlotY = d.y - b.plotTop);
            this.kdByAngle ? (b = (e / Math.PI * 180 + c.pane.options.startAngle) % 360, 0 > b && (b += 360), a.clientX = b) : a.clientX =
                a.plotX
        };
        m.spline && (e(m.spline.prototype, "getPointSpline", function (a, b, c, d) {
            this.chart.polar ? d ? (a = this.getConnectors(b, d, !0, this.connectEnds), b = a.prevPointCont && a.prevPointCont.rightContX, c = a.prevPointCont && a.prevPointCont.rightContY, a = ["C", p(b) ? b : a.plotX, p(c) ? c : a.plotY, p(a.leftContX) ? a.leftContX : a.plotX, p(a.leftContY) ? a.leftContY : a.plotY, a.plotX, a.plotY]) : a = ["M", c.plotX, c.plotY] : a = a.call(this, b, c, d);
            return a
        }), m.areasplinerange && (m.areasplinerange.prototype.getPointSpline = m.spline.prototype.getPointSpline));
        b(t, "afterTranslate", function () {
            var a = this.chart;
            if (a.polar && this.xAxis) {
                (this.kdByAngle = a.tooltip && a.tooltip.shared) ? this.searchPoint = this.searchPointByAngle : this.options.findNearestPointBy = "xy";
                if (!this.preventPostTranslate) for (var c = this.points, d = c.length; d--;) this.toXY(c[d]), !a.hasParallelCoordinates && !this.yAxis.reversed && c[d].y < this.yAxis.min && (c[d].isNull = !0);
                this.hasClipCircleSetter || (this.hasClipCircleSetter = !!this.eventsToUnbind.push(b(this, "afterRender", function () {
                    if (a.polar) {
                        var b = this.yAxis.pane.center;
                        this.clipCircle ? this.clipCircle.animate({
                            x: b[0],
                            y: b[1],
                            r: b[2] / 2,
                            innerR: b[3] / 2
                        }) : this.clipCircle = a.renderer.clipCircle(b[0], b[1], b[2] / 2, b[3] / 2);
                        this.group.clip(this.clipCircle);
                        this.setClip = h.noop
                    }
                })))
            }
        }, {order: 2});
        e(m.line.prototype, "getGraphPath", function (a, b) {
            var c = this, d;
            if (this.chart.polar) {
                b = b || this.points;
                for (d = 0; d < b.length; d++) if (!b[d].isNull) {
                    var e = d;
                    break
                }
                if (!1 !== this.options.connectEnds && "undefined" !== typeof e) {
                    this.connectEnds = !0;
                    b.splice(b.length, 0, b[e]);
                    var f = !0
                }
                b.forEach(function (a) {
                    "undefined" ===
                    typeof a.polarPlotY && c.toXY(a)
                })
            }
            d = a.apply(this, [].slice.call(arguments, 1));
            f && b.pop();
            return d
        });
        var y = function (a, b) {
            var c = this, d = this.chart, e = this.options.animation, f = this.group, g = this.markerGroup,
                k = this.xAxis.center, l = d.plotLeft, m = d.plotTop, p, q, r, t;
            if (d.polar) if (c.isRadialBar) b || (c.startAngleRad = v(c.translatedThreshold, c.xAxis.startAngleRad), h.seriesTypes.pie.prototype.animate.call(c, b)); else {
                if (d.renderer.isSVG) if (e = x(e), c.is("column")) {
                    if (!b) {
                        var y = k[3] / 2;
                        c.points.forEach(function (a) {
                            p = a.graphic;
                            r = (q = a.shapeArgs) && q.r;
                            t = q && q.innerR;
                            p && q && (p.attr({r: y, innerR: y}), p.animate({r: r, innerR: t}, c.options.animation))
                        })
                    }
                } else b ? (a = {
                    translateX: k[0] + l,
                    translateY: k[1] + m,
                    scaleX: .001,
                    scaleY: .001
                }, f.attr(a), g && g.attr(a)) : (a = {
                    translateX: l,
                    translateY: m,
                    scaleX: 1,
                    scaleY: 1
                }, f.animate(a, e), g && g.animate(a, e))
            } else a.call(this, b)
        };
        e(D, "animate", y);
        if (m.column) {
            var I = m.arearange.prototype;
            m = m.column.prototype;
            m.polarArc = function (a, b, c, d) {
                var e = this.xAxis.center, f = this.yAxis.len, g = e[3] / 2;
                b = f - b + g;
                a = f - v(a, f) + g;
                this.yAxis.reversed &&
                (0 > b && (b = g), 0 > a && (a = g));
                return {x: e[0], y: e[1], r: b, innerR: a, start: c, end: d}
            };
            e(m, "animate", y);
            e(m, "translate", function (a) {
                var b = this.options, c = b.stacking, d = this.chart, e = this.xAxis, g = this.yAxis, h = g.reversed,
                    k = g.center, l = e.startAngleRad, m = e.endAngleRad - l;
                this.preventPostTranslate = !0;
                a.call(this);
                if (e.isRadial) {
                    a = this.points;
                    e = a.length;
                    var t = g.translate(g.min);
                    var v = g.translate(g.max);
                    b = b.threshold || 0;
                    if (d.inverted && p(b)) {
                        var x = g.translate(b);
                        q(x) && (0 > x ? x = 0 : x > m && (x = m), this.translatedThreshold = x + l)
                    }
                    for (; e--;) {
                        b =
                            a[e];
                        var y = b.barX;
                        var z = b.x;
                        var B = b.y;
                        b.shapeType = "arc";
                        if (d.inverted) {
                            b.plotY = g.translate(B);
                            if (c && g.stacking) {
                                if (B = g.stacking.stacks[(0 > B ? "-" : "") + this.stackKey], this.visible && B && B[z] && !b.isNull) {
                                    var D = B[z].points[this.getStackIndicator(void 0, z, this.index).key];
                                    var E = g.translate(D[0]);
                                    D = g.translate(D[1]);
                                    q(E) && (E = r.clamp(E, 0, m))
                                }
                            } else E = x, D = b.plotY;
                            E > D && (D = [E, E = D][0]);
                            if (!h) if (E < t) E = t; else if (D > v) D = v; else {
                                if (D < t || E > v) E = D = 0
                            } else if (D > t) D = t; else if (E < v) E = v; else if (E > t || D < v) E = D = m;
                            g.min > g.max && (E = D =
                                h ? m : 0);
                            E += l;
                            D += l;
                            k && (b.barX = y += k[3] / 2);
                            z = Math.max(y, 0);
                            B = Math.max(y + b.pointWidth, 0);
                            b.shapeArgs = {x: k && k[0], y: k && k[1], r: B, innerR: z, start: E, end: D};
                            b.opacity = E === D ? 0 : void 0;
                            b.plotY = (q(this.translatedThreshold) && (E < this.translatedThreshold ? E : D)) - l
                        } else E = y + l, b.shapeArgs = this.polarArc(b.yBottom, b.plotY, E, E + b.pointWidth);
                        this.toXY(b);
                        d.inverted ? (y = g.postTranslate(b.rectPlotY, y + b.pointWidth / 2), b.tooltipPos = [y.x - d.plotLeft, y.y - d.plotTop]) : b.tooltipPos = [b.plotX, b.plotY];
                        k && (b.ttBelow = b.plotY > k[1])
                    }
                }
            });
            m.findAlignments =
                function (a, b) {
                    null === b.align && (b.align = 20 < a && 160 > a ? "left" : 200 < a && 340 > a ? "right" : "center");
                    null === b.verticalAlign && (b.verticalAlign = 45 > a || 315 < a ? "bottom" : 135 < a && 225 > a ? "top" : "middle");
                    return b
                };
            I && (I.findAlignments = m.findAlignments);
            e(m, "alignDataLabel", function (a, b, c, d, e, h) {
                var f = this.chart, g = v(d.inside, !!this.options.stacking);
                f.polar ? (a = b.rectPlotX / Math.PI * 180, f.inverted ? (this.forceDL = f.isInsidePlot(b.plotX, Math.round(b.plotY)), g && b.shapeArgs ? (e = b.shapeArgs, e = this.yAxis.postTranslate(((e.start || 0) +
                    (e.end || 0)) / 2 - this.xAxis.startAngleRad, b.barX + b.pointWidth / 2), e = {
                    x: e.x - f.plotLeft,
                    y: e.y - f.plotTop
                }) : b.tooltipPos && (e = {
                    x: b.tooltipPos[0],
                    y: b.tooltipPos[1]
                }), d.align = v(d.align, "center"), d.verticalAlign = v(d.verticalAlign, "middle")) : this.findAlignments && (d = this.findAlignments(a, d)), D.alignDataLabel.call(this, b, c, d, e, h), this.isRadialBar && b.shapeArgs && b.shapeArgs.start === b.shapeArgs.end && c.hide(!0)) : a.call(this, b, c, d, e, h)
            })
        }
        e(a, "getCoordinates", function (a, b) {
            var c = this.chart, d = {xAxis: [], yAxis: []};
            c.polar ?
                c.axes.forEach(function (a) {
                    var e = a.isXAxis, f = a.center;
                    if ("colorAxis" !== a.coll) {
                        var g = b.chartX - f[0] - c.plotLeft;
                        f = b.chartY - f[1] - c.plotTop;
                        d[e ? "xAxis" : "yAxis"].push({
                            axis: a,
                            value: a.translate(e ? Math.PI - Math.atan2(g, f) : Math.sqrt(Math.pow(g, 2) + Math.pow(f, 2)), !0)
                        })
                    }
                }) : d = a.call(this, b);
            return d
        });
        l.prototype.clipCircle = function (a, b, c, d) {
            var e = H(), f = this.createElement("clipPath").attr({id: e}).add(this.defs);
            a = d ? this.arc(a, b, c, d, 0, 2 * Math.PI).add(f) : this.circle(a, b, c).add(f);
            a.id = e;
            a.clipPath = f;
            return a
        };
        b(d,
            "getAxes", function () {
            this.pane || (this.pane = []);
            this.options.pane = z(this.options.pane);
            this.options.pane.forEach(function (a) {
                new c(a, this)
            }, this)
        });
        b(d, "afterDrawChartBox", function () {
            this.pane.forEach(function (a) {
                a.render()
            })
        });
        b(t, "afterInit", function () {
            var a = this.chart;
            a.inverted && a.polar && (this.isRadialSeries = !0, this.is("column") && (this.isRadialBar = !0))
        });
        e(d.prototype, "get", function (a, b) {
            return k(this.pane || [], function (a) {
                return a.options.id === b
            }) || a.call(this, b)
        })
    });
    z(e, "masters/highcharts-more.src.js",
        [e["Core/Globals.js"], e["Core/Axis/RadialAxis.js"], e["Series/Bubble/BubbleSeries.js"]], function (e, d, h) {
        d.compose(e.Axis, e.Tick);
        h.compose(e.Chart, e.Legend, e.Series)
    })
});
//# sourceMappingURL=highcharts-more.js.map
