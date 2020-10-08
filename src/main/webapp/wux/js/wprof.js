var WP;
(function (WP) {
    function getData() {
        var d = window ? window['__data'] : undefined;
        if (d) {
            if (!d.sys)
                d.sys = [];
            if (!d.jvm)
                d.jvm = [];
            if (!d.evn)
                d.evn = [];
            return d;
        }
        return {
            sys: [],
            jvm: [],
            evn: [],
            msg: "No data available"
        };
    }
    WP.getData = getData;
})(WP || (WP = {}));
var WP;
(function (WP) {
    var WUtil = WUX.WUtil;
    var DlgEvents = (function (_super) {
        __extends(DlgEvents, _super);
        function DlgEvents(id) {
            var _this = _super.call(this, id, 'DlgEvents') || this;
            _this.title = 'Events';
            _this.lblLab = new WUX.WLabel(_this.subId('ll'), '', '', '', WUX.CSS.LABEL_INFO);
            _this.lblVal = new WUX.WLabel(_this.subId('lv'), '', '', '', WUX.CSS.LABEL_NOTICE);
            _this.tabEvn = new WUX.WTable(_this.subId('te'), ['Date time', 'Type', 'Application', 'Class', 'Method', 'Elapsed', 'Size', 'Exception']);
            _this.tabEvn.selectionMode = 'single';
            _this.body
                .addRow()
                .addCol('12', { a: 'right' })
                .add(_this.lblLab)
                .addRow()
                .addCol('12', { a: 'right' })
                .add(_this.lblVal)
                .addRow()
                .addCol('12').section('Events')
                .add(_this.tabEvn);
            return _this;
        }
        DlgEvents.prototype.updateProps = function (nextProps) {
            _super.prototype.updateProps.call(this, nextProps);
            if (!this.lblLab)
                return;
            this.lblLab.setState(this.props);
        };
        DlgEvents.prototype.updateState = function (nextState) {
            _super.prototype.updateState.call(this, nextState);
            if (!this.state)
                this.state = 0;
            if (!this.lblVal)
                return;
            if (this._title && this._title.indexOf('perc') >= 0) {
                this.lblVal.setState('' + this.state + ' %');
            }
            else {
                this.lblVal.setState('' + this.state);
            }
        };
        DlgEvents.prototype.setEvents = function (d) {
            if (!d)
                d = [];
            if (!this.tabEvn)
                return;
            this.tabEvn.setState(d);
        };
        return DlgEvents;
    }(WUX.WDialog));
    WP.DlgEvents = DlgEvents;
    var GUIAnalyze = (function (_super) {
        __extends(GUIAnalyze, _super);
        function GUIAnalyze(id) {
            var _this = _super.call(this, id ? id : '*', 'GUIAnalyze', '') || this;
            _this.state = WP.getData();
            _this.frh = '00:00:00';
            _this.toh = '24:00:00';
            _this.dlg = new DlgEvents(_this.subId('dlg'));
            _this.dlg.onHiddenModal(function (e) {
                $(window).scrollTop(0);
            });
            return _this;
        }
        GUIAnalyze.prototype.render = function () {
            var _this = this;
            var opt = new Array();
            opt.push({ id: 'sys_1', text: 'CPU usage percentage' });
            opt.push({ id: 'sys_2', text: 'Memory usage percentage' });
            opt.push({ id: 'sys_3', text: 'Disk usage percentage' });
            opt.push({ id: 'jvm_1', text: 'Metaspace used' });
            opt.push({ id: 'jvm_3', text: 'Heap Eden used' });
            opt.push({ id: 'jvm_5', text: 'Heap Survivor used' });
            opt.push({ id: 'jvm_7', text: 'Heap Tenured used' });
            opt.push({ id: 'jvm_9', text: 'Code Cache used' });
            opt.push({ id: 'jvm_11', text: 'Loaded Class count' });
            opt.push({ id: 'jvm_12', text: 'Total Loaded class count' });
            opt.push({ id: 'jvm_13', text: 'Unloaded class count' });
            opt.push({ id: 'jvm_14', text: 'Thread count' });
            opt.push({ id: 'jvm_15', text: 'Peak Thread count' });
            opt.push({ id: 'jvm_16', text: 'Total Started Thread count' });
            var hhmm = [];
            for (var h = 0; h < 24; h++) {
                var hh = h < 10 ? '0' + h : '' + h;
                for (var m = 0; m < 60; m += 15) {
                    var mm = m < 10 ? '0' + m : '' + m;
                    hhmm.push({ id: hh + ':' + mm + ':00', text: hh + ':' + mm });
                }
            }
            hhmm.push({ id: '24:00:00', text: '24:00' });
            if (!this.state)
                this.state = { msg: "No data", evn: [], sys: [], jvm: [] };
            if (!this.props) {
                this.props = opt[0].id;
                this.title = opt[0].text;
            }
            this.sel = new WUX.WSelect('sel', opt, false, 'form-control');
            this.sel.setState(this.props);
            this.sel.on('statechange', function (e) {
                _this.refresh();
            });
            this.sfh = new WUX.WSelect('frh', hhmm, false, 'form-control');
            this.sfh.setState('00:00:00');
            this.sfh.on('statechange', function (e) {
                _this.refresh();
            });
            this.sth = new WUX.WSelect('toh', hhmm, false, 'form-control');
            this.sth.setState('24:00:00');
            this.sth.on('statechange', function (e) {
                _this.refresh();
            });
            this.frm = new WUX.WFormPanel(this.subId('frm'));
            this.frm.addRow();
            this.frm.addComponent('sel', 'Select', this.sel);
            this.frm.addComponent('frh', 'From time', this.sfh);
            this.frm.addComponent('toh', 'To time', this.sth);
            this.lbl = new WUX.WLabel(this.subId('lbl'), this.state.msg, WUX.WIcon.WARNING, '', WUX.CSS.LABEL_NOTICE);
            this.chr = new WUX.WChartJS(this.subId('chr'), 'line');
            this.chr.setState(this.getChartData());
            this.chr.onClickChart(function (e) {
                var l = _this.chr.getLabel(e);
                var v = _this.chr.getValue(e);
                if (!_this.title)
                    _this.title = 'Events';
                _this.dlg.title = _this.title;
                _this.dlg.setProps(l);
                _this.dlg.setState(v);
                _this.dlg.setEvents(_this.getEvents(l, 500));
                setTimeout(function () {
                    _this.dlg.show(_this);
                }, 500);
            });
            this.tbl = new WUX.WTable(this.subId('tbl'), ['Date time', 'Type', 'Application', 'Class', 'Method', 'Elapsed', 'Size', 'Exception']);
            this.tbl.selectionMode = 'single';
            this.tbl.setState(this.getAllEvents());
            this.cnt = new WUX.WContainer();
            this.cnt
                .addRow()
                .addCol('12').section('Filter')
                .add(this.frm);
            if (this.state.msg) {
                this.cnt
                    .addRow()
                    .addCol('12').section('Message')
                    .add(this.lbl);
            }
            this.cnt
                .addRow()
                .addCol('12').section('Chart')
                .add(this.chr)
                .addRow()
                .addCol('12').section('Events')
                .add(this.tbl);
            return this.cnt;
        };
        GUIAnalyze.prototype.refresh = function () {
            var f = this.frm.getState();
            this.frh = WUtil.getString(f, 'frh');
            this.toh = WUtil.getString(f, 'toh');
            this.title = this.sel.getProps();
            this.updateProps(this.sel.getState());
            return this;
        };
        GUIAnalyze.prototype.updateProps = function (nextProps) {
            _super.prototype.updateProps.call(this, nextProps);
            if (!this.mounted)
                return;
            this.chr.setState(this.getChartData());
            this.tbl.setState(this.getAllEvents());
        };
        GUIAnalyze.prototype.getChartData = function () {
            var r = {};
            var df = this.props.split('_');
            if (!df || df.length < 2)
                return r;
            var a = WUtil.toArray(this.state[df[0]]);
            if (!a || !a.length)
                return r;
            var y = WUtil.toNumber(df[1]);
            var labels = [];
            var series = [];
            var values = [];
            for (var i = 0; i < a.length; i++) {
                var o = a[i];
                var l = WUtil.toString(o[0]);
                var v = o[y];
                var h = this.getTime(l);
                if (h < this.frh)
                    continue;
                if (h > this.toh)
                    break;
                labels.push(l);
                values.push(v);
            }
            series.push(values);
            r.labels = labels;
            r.series = series;
            return r;
        };
        GUIAnalyze.prototype.getEvents = function (d, max) {
            if (!this.state || !this.state.evn)
                return [];
            if (!max)
                max = 100;
            var l = this.state.evn.length;
            if (l == 0)
                return [];
            var e = -1;
            for (var i = 0; i < l; i++) {
                if (this.state.evn[i][0] >= d) {
                    e = i;
                    break;
                }
            }
            if (e < 0)
                e = l - 1;
            var s = e - max;
            if (s < 0)
                s = 0;
            return this.state.evn.slice(s, e);
        };
        GUIAnalyze.prototype.getAllEvents = function () {
            var minh = !this.frh || this.frh == '00:00:00';
            var maxh = !this.toh || this.toh == '24:00:00';
            if (minh && maxh) {
                return this.state.evn;
            }
            var l = this.state.evn.length;
            if (l == 0)
                return [];
            var s = -1;
            var e = -1;
            for (var i = 0; i < l; i++) {
                var o = this.state.evn[i];
                var h = this.getTime(o[0]);
                if (h < this.frh)
                    continue;
                if (h > this.toh)
                    break;
                if (s < 0)
                    s = i;
                e = i;
            }
            if (s < 0)
                return [];
            return this.state.evn.slice(s, e);
        };
        GUIAnalyze.prototype.getTime = function (dt) {
            if (!dt)
                return "00:00:00";
            var s = dt.indexOf(' ');
            if (s <= 0)
                return "00:00:00";
            return dt.substring(s + 1);
        };
        return GUIAnalyze;
    }(WUX.WComponent));
    WP.GUIAnalyze = GUIAnalyze;
})(WP || (WP = {}));
//# sourceMappingURL=wprof.js.map