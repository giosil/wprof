namespace WP {

    import WUtil = WUX.WUtil;

    export class GUIAnalyze extends WUX.WComponent<string, WProfData> {
        cnt: WUX.WContainer;
        sel: WUX.WSelect;
        frm: WUX.WFormPanel;
        lbl: WUX.WLabel;
        chr: WUX.WChartJS;
        tbl: WUX.WTable;

        constructor(id?: string) {
            super(id ? id : '*', 'GUIAnalyze', '');
            this.state = WP.getData();
        }

        protected render() {
            if (!this.state) this.state = { msg: "No data", evn: [], sys: [], jvm: [] };
            if (!this.props) this.props = 'sys_cup';

            let opt = new Array<WUX.WEntity>();
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
            this.sel.on('statechange', (e: WUX.WEvent) => {
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

            this.tbl = new WUX.WTable(this.subId('tbl'),
                ['Date time', 'Type', 'Application', 'Class', 'Method', 'Elapsed', 'Size', 'Exception'],
                ['d', 't', 'a', 'c', 'm', 'e', 's', 'x']
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

            if (!this.props) this.props = 'sys_cup';

            let df = this.props.split('_');
            if (!df || df.length < 2) return r;
            let a = WUtil.toArray(this.state[df[0]]);
            if (!a || !a.length) return r;

            let labels = [];
            let series = [];
            let values = [];
            for (let i = 0; i < a.length; i++) {
                let o = a[i];
                let l = WUtil.toString(o['d']);
                let v = WUtil.toNumber(o[df[1]]);

                labels.push(l);
                values.push(v);
            }
            series.push(values);

            r.labels = labels;
            r.series = series;

            return r;
        }
    }

}