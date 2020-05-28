declare namespace WP {
    function getData(): WProfData;
    interface WProfData {
        sys?: any[];
        sys_fields?: string[];
        jvm?: any[];
        jvm_fields?: string[];
        evn?: any[];
        evn_fields?: string[];
        msg?: string;
    }
}
declare namespace WP {
    class DlgEvents extends WUX.WDialog<string, number> {
        lblLab: WUX.WLabel;
        lblVal: WUX.WLabel;
        tabEvn: WUX.WTable;
        constructor(id: string);
        protected updateProps(nextProps: string): void;
        protected updateState(nextState: number): void;
        setEvents(d: any[]): void;
    }
    class GUIAnalyze extends WUX.WComponent<string, WProfData> {
        cnt: WUX.WContainer;
        sel: WUX.WSelect;
        frm: WUX.WFormPanel;
        lbl: WUX.WLabel;
        chr: WUX.WChartJS;
        tbl: WUX.WTable;
        title: string;
        dlg: DlgEvents;
        constructor(id?: string);
        protected render(): WUX.WContainer;
        protected updateProps(nextProps: string): void;
        protected getChartData(): WUX.WChartData;
        protected getEvents(d: string, max?: number): any[];
    }
}
