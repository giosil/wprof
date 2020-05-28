namespace WP {

    import WUtil = WUX.WUtil;

    export class DlgEvents extends WUX.WDialog<string, number> {
        lblLab: WUX.WLabel;
        lblVal: WUX.WLabel;
        tabEvn: WUX.WTable;

        constructor(id: string) {
            super(id, 'DlgEvents');

            this.title = 'Events';

            this.lblLab = new WUX.WLabel(this.subId('ll'), '', '', '', WUX.CSS.LABEL_INFO);
            this.lblVal = new WUX.WLabel(this.subId('lv'), '', '', '', WUX.CSS.LABEL_NOTICE);

            this.tabEvn = new WUX.WTable(this.subId('te'),
                ['Date time', 'Type', 'Application', 'Class', 'Method', 'Elapsed', 'Size', 'Exception']
            );
            this.tabEvn.selectionMode = 'single';

            this.body
                .addRow()
                .addCol('12', { a: 'right' })
                .add(this.lblLab)
                .addRow()
                .addCol('12', { a: 'right' })
                .add(this.lblVal)
                .addRow()
                .addCol('12').section('Events')
                .add(this.tabEvn);
        }

        protected updateProps(nextProps: string): void {
            super.updateProps(nextProps);
            if (!this.lblLab) return;
            this.lblLab.setState(this.props);
        }

        protected updateState(nextState: number): void {
            super.updateState(nextState);
            if (!this.state) this.state = 0;
            if (!this.lblVal) return;
            if (this._title && this._title.indexOf('perc') >= 0) {
                this.lblVal.setState('' + this.state + ' %');
            }
            else {
                this.lblVal.setState('' + this.state);
            }
        }

        setEvents(d: any[]) {
            if (!d) d = [];
            if (!this.tabEvn) return;
            this.tabEvn.setState(d);
        }
    }

    export class GUIAnalyze extends WUX.WComponent<string, WProfData> {
        cnt: WUX.WContainer;
        sel: WUX.WSelect;
        frm: WUX.WFormPanel;
        lbl: WUX.WLabel;
        chr: WUX.WChartJS;
        tbl: WUX.WTable;

        title: string;
        dlg: DlgEvents;

        constructor(id?: string) {
            super(id ? id : '*', 'GUIAnalyze', '');
            this.state = WP.getData();

            this.dlg = new DlgEvents(this.subId('dlg'));
            this.dlg.onHiddenModal((e: JQueryEventObject) => {
                $(window).scrollTop(0);
            });
        }

        protected render() {

            let opt = new Array<WUX.WEntity>();
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

            if (!this.state) this.state = { msg: "No data", evn: [], sys: [], jvm: [] };
            if (!this.props) {
                this.props = opt[0].id;
                this.title = opt[0].text;
            }

            this.sel = new WUX.WSelect('sel', opt);
            this.sel.on('statechange', (e: WUX.WEvent) => {
                this.title = this.sel.getProps();
                this.updateProps(this.sel.getState());
            });

            this.frm = new WUX.WFormPanel(this.subId('frm'));
            this.frm.addRow();
            this.frm.addComponent('sel', 'Select', this.sel);
            this.frm.addBlankField();
            this.frm.addBlankField();

            this.lbl = new WUX.WLabel(this.subId('lbl'), this.state.msg, WUX.WIcon.WARNING, '', WUX.CSS.LABEL_NOTICE);

            this.chr = new WUX.WChartJS(this.subId('chr'), 'line');
            this.chr.setState(this.getChartData());
            this.chr.onClickChart((e: WUX.WEvent) => {
                let l = this.chr.getLabel(e);
                let v = this.chr.getValue(e);

                if (!this.title) this.title = 'Events';
                this.dlg.title = this.title;
                this.dlg.setProps(l);
                this.dlg.setState(v);
                this.dlg.setEvents(this.getEvents(l, 500));

                setTimeout(() => {
                    this.dlg.show(this);
                }, 500);
            });

            this.tbl = new WUX.WTable(this.subId('tbl'),
                ['Date time', 'Type', 'Application', 'Class', 'Method', 'Elapsed', 'Size', 'Exception'],
            );
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
        }

        protected updateProps(nextProps: string): void {
            super.updateProps(nextProps);
            if (!this.mounted) return;
            this.chr.setState(this.getChartData());
        }

        protected getChartData() {
            let r: WUX.WChartData = {};

            let df = this.props.split('_');
            if (!df || df.length < 2) return r;
            let a = WUtil.toArray(this.state[df[0]]);
            if (!a || !a.length) return r;
            let y = WUtil.toNumber(df[1]);

            let labels = [];
            let series = [];
            let values = [];
            for (let i = 0; i < a.length; i++) {
                let o = a[i];
                let l = WUtil.toString(o[0]);
                let v = o[y];

                labels.push(l);
                values.push(v);
            }
            series.push(values);

            r.labels = labels;
            r.series = series;

            return r;
        }

        protected getEvents(d: string, max?: number): any[] {
            if (!this.state || !this.state.evn) return [];
            if (!max) max = 100;

            let l = this.state.evn.length;
            if (l == 0) return [];

            let e = -1;
            for (let i = 0; i < l; i++) {
                if (this.state.evn[i][0] >= d) {
                    e = i;
                    break;
                }
            }
            if (e < 0) e = l - 1;

            let s = e - max;
            if (s < 0) s = 0;
            return this.state.evn.slice(s, e);
        }
    }

}