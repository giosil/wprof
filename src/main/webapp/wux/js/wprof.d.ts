declare namespace WP {
    function getData(): WProfData;
    interface SysData {
        d?: string;
        cup?: number;
        mup?: number;
        dup?: number;
    }
    interface JvmData {
        d?: string;
        msu?: number;
        msm?: number;
        heu?: number;
        hem?: number;
        hsu?: number;
        hsm?: number;
        htu?: number;
        htm?: number;
        ccu?: number;
        ccm?: number;
        lcc?: number;
        tlc?: number;
        ucc?: number;
        ttc?: number;
        ptc?: number;
        stc?: number;
    }
    interface EventData {
        d?: string;
        t?: "a" | "e" | "x" | "i" | "r";
        a?: string;
        c?: string;
        m?: string;
        e?: number;
        s?: number;
        x?: string;
    }
    interface WProfData {
        sys?: SysData[];
        jvm?: JvmData[];
        evn?: EventData[];
        msg?: string;
    }
}
declare namespace WP {
    class GUIAnalyze extends WUX.WComponent<string, WProfData> {
        cnt: WUX.WContainer;
        sel: WUX.WSelect;
        frm: WUX.WFormPanel;
        lbl: WUX.WLabel;
        chr: WUX.WChartJS;
        tbl: WUX.WTable;
        constructor(id?: string);
        protected render(): WUX.WContainer;
        protected updateProps(nextProps: string): void;
        protected getChartData(): WUX.WChartData;
    }
}
