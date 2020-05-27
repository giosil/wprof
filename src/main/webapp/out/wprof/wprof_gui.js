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
var WP;
(function (WP) {
    var WUtil = WUX.WUtil;
    var DlgEvents = (function (_super) {
        __extends(DlgEvents, _super);
        function DlgEvents(id) {
            var _this = _super.call(this, id, 'DlgEvents') || this;
            _this.lblLab = new WUX.WLabel(_this.subId('ll'), '', '', '', WUX.CSS.LABEL_INFO);
            _this.lblVal = new WUX.WLabel(_this.subId('lv'), '', '', '', WUX.CSS.LABEL_NOTICE);
            _this.tabEvn = new WUX.WTable(_this.subId('te'), ['Date time', 'Type', 'Application', 'Class', 'Method', 'Elapsed', 'Size', 'Exception'], ['d', 't', 'a', 'c', 'm', 'e', 's', 'x']);
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
            this.lblVal.setState('' + this.state);
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
            _this.dlg = new DlgEvents(_this.subId('dlg'));
            _this.dlg.onHiddenModal(function (e) {
                $(window).scrollTop(0);
            });
            return _this;
        }
        GUIAnalyze.prototype.render = function () {
            var _this = this;
            if (!this.state)
                this.state = { msg: "No data", evn: [], sys: [], jvm: [] };
            if (!this.props)
                this.props = 'sys_cup';
            var opt = new Array();
            opt.push({ id: 'sys_cup', text: 'CPU usage percentage' });
            opt.push({ id: 'sys_mup', text: 'Memory usage percentage' });
            opt.push({ id: 'sys_dup', text: 'Disk usage percentage' });
            opt.push({ id: 'jvm_msu', text: 'Metaspace usage' });
            opt.push({ id: 'jvm_heu', text: 'Heap Eden usage' });
            opt.push({ id: 'jvm_hsu', text: 'Heap Survivor usage' });
            opt.push({ id: 'jvm_htu', text: 'Heap Tenured usage' });
            opt.push({ id: 'jvm_ccu', text: 'Code Cache usage' });
            opt.push({ id: 'jvm_lcc', text: 'Loaded Class count' });
            opt.push({ id: 'jvm_tlc', text: 'Total Loaded class count' });
            opt.push({ id: 'jvm_ucc', text: 'Unloaded class count' });
            opt.push({ id: 'jvm_ttc', text: 'Thread count' });
            opt.push({ id: 'jvm_ptc', text: 'Peak Thread count' });
            opt.push({ id: 'jvm_stc', text: 'Total Started Thread count' });
            this.sel = new WUX.WSelect('sel', opt);
            this.sel.on('statechange', function (e) {
                _this.updateProps(_this.sel.getState());
            });
            this.frm = new WUX.WFormPanel(this.subId('frm'));
            this.frm.addRow();
            this.frm.addComponent('sel', 'Select', this.sel);
            this.frm.addBlankField();
            this.frm.addBlankField();
            this.lbl = new WUX.WLabel(this.subId('lbl'), this.state.msg, WUX.WIcon.WARNING, '', WUX.CSS.LABEL_NOTICE);
            this.chr = new WUX.WChartJS(this.subId('chr'), 'line');
            this.chr.setState(this.getChartData());
            this.chr.onClickChart(function (e) {
                var l = _this.chr.getLabel(e);
                var v = _this.chr.getValue(e);
                _this.dlg.setProps(l);
                _this.dlg.setState(v);
                _this.dlg.setEvents(_this.getEvents(l, 500));
                setTimeout(function () {
                    _this.dlg.show(_this);
                }, 500);
            });
            this.tbl = new WUX.WTable(this.subId('tbl'), ['Date time', 'Type', 'Application', 'Class', 'Method', 'Elapsed', 'Size', 'Exception'], ['d', 't', 'a', 'c', 'm', 'e', 's', 'x']);
            this.tbl.selectionMode = 'single';
            this.tbl.setState(this.state.evn);
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
        GUIAnalyze.prototype.updateProps = function (nextProps) {
            _super.prototype.updateProps.call(this, nextProps);
            if (!this.mounted)
                return;
            this.chr.setState(this.getChartData());
        };
        GUIAnalyze.prototype.getChartData = function () {
            var r = {};
            if (!this.props)
                this.props = 'sys_cup';
            var df = this.props.split('_');
            if (!df || df.length < 2)
                return r;
            var a = WUtil.toArray(this.state[df[0]]);
            if (!a || !a.length)
                return r;
            var labels = [];
            var series = [];
            var values = [];
            for (var i = 0; i < a.length; i++) {
                var o = a[i];
                var l = WUtil.toString(o['d']);
                var v = WUtil.toNumber(o[df[1]]);
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
                if (this.state.evn[i].d >= d) {
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
        return GUIAnalyze;
    }(WUX.WComponent));
    WP.GUIAnalyze = GUIAnalyze;
})(WP || (WP = {}));
//# sourceMappingURL=wprof_gui.js.map