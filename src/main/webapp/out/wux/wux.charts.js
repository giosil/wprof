var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var WUX;
(function (WUX) {
    var WChartJS = (function (_super) {
        __extends(WChartJS, _super);
        function WChartJS(id, type, classStyle, style, attributes) {
            var _this = _super.call(this, id, 'WChartJS', type, classStyle, style, attributes) || this;
            _this.rootTag = 'canvas';
            _this.title = '';
            _this._opset = false;
            _this.legend = type == 'pie' || type == 'doughnut' || type == 'polarArea' ? true : false;
            _this.colors = [];
            _this.bg0 = WUX.global.chart_bg0;
            _this.bg1 = WUX.global.chart_bg1;
            _this.bg2 = WUX.global.chart_bg2;
            _this.bc0 = WUX.global.chart_bc0;
            _this.bc1 = WUX.global.chart_bc1;
            _this.bc2 = WUX.global.chart_bc2;
            _this.p0 = WUX.global.chart_p0;
            _this.p1 = WUX.global.chart_p1;
            _this.p2 = WUX.global.chart_p2;
            _this.pbc = '#fff';
            _this.forceOnChange = true;
            return _this;
        }
        Object.defineProperty(WChartJS.prototype, "options", {
            get: function () {
                return this._options;
            },
            set: function (o) {
                this._options = o;
                if (o) {
                    this._opset = true;
                }
                else {
                    this._opset = false;
                }
            },
            enumerable: true,
            configurable: true
        });
        WChartJS.prototype.componentDidMount = function () {
            if (this._tooltip) {
                this.root.attr('title', this._tooltip);
            }
            if (this.state) {
                this.buildChart();
            }
        };
        WChartJS.prototype.buildChart = function () {
            if (!this.state || !this.root)
                return;
            if (!this.state.labels)
                this.state.labels = [];
            if (!this.state.titles)
                this.state.titles = [];
            if (!this.state.series)
                this.state.series = [[]];
            if (!this.props)
                this.props = 'line';
            if (!this._opset || !this._options) {
                if (this.props == 'pie' || this.props == 'doughnut' || this.props == 'polarArea') {
                    this.legend = true;
                }
                else {
                    if (!this.state.titles || !this.state.titles.length) {
                        if (!this.title)
                            this.legend = false;
                    }
                }
                this._options = {
                    responsive: true,
                    maintainAspectRatio: false,
                    legend: {
                        display: this.legend
                    }
                };
            }
            var cd;
            if (this.props == 'pie' || this.props == 'doughnut' || this.props == 'polarArea') {
                if (!this.colors || !this.colors.length) {
                    this.colors = ["#dfefdf", "#e4f4ff", "#ffffcc", "#ffddaa", "#ffccff", "#e6ccff", "#f1e7cb", "#eeeeee", "#d9f2e6", "#d9e6f2"];
                }
                cd = {
                    labels: this.state.labels,
                    datasets: [{
                            backgroundColor: this.colors,
                            data: this.state.series[0]
                        }]
                };
            }
            else {
                var ds = [];
                for (var i = 0; i < this.state.series.length; i++) {
                    var lbl = this.state.titles[i];
                    if (!lbl)
                        lbl = '';
                    ds.push({
                        label: lbl,
                        backgroundColor: i == 0 ? this.bg0 : i == 1 ? this.bg1 : this.bg2,
                        borderColor: i == 0 ? this.bc0 : i == 1 ? this.bc1 : this.bc2,
                        pointBackgroundColor: i == 0 ? this.p0 : i == 1 ? this.p1 : this.p2,
                        pointBorderColor: this.pbc,
                        data: this.state.series[i]
                    });
                }
                cd = {
                    labels: this.state.labels,
                    datasets: ds
                };
            }
            this.chart = new Chart((this.root[0].getContext('2d')), {
                type: this.props,
                data: cd,
                options: this._options
            });
        };
        return WChartJS;
    }(WUX.WComponent));
    WUX.WChartJS = WChartJS;
})(WUX || (WUX = {}));
//# sourceMappingURL=wux.charts.js.map