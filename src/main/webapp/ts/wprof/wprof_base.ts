namespace WP {

    export function getData(): WProfData {
        let d = window ? window['__data'] as WProfData : undefined;
        if (d) {
            if (!d.sys) d.sys = [];
            if (!d.jvm) d.jvm = [];
            if (!d.evn) d.evn = [];
            return __data;
        }
        return {
            sys: [],
            jvm: [],
            evn: [],
            msg: "No data available"
        };
    }

    export interface SysData {
        /** Date time */
        d?: string;

        /** CPU Usage Percentage */
        cup?: number;
        /** Memory Usage Percentage */
        mup?: number;
        /** Disk Usage Percentage */
        dup?: number;
    }

    export interface JvmData {
        /** Date time */
        d?: string;

        /** Metaspace used */
        msu?: number;
        /** Metaspace max */
        msm?: number;

        /** Heap Eden used */
        heu?: number;
        /** Heap Eden max */
        hem?: number;

        /** Heap Survivor used */
        hsu?: number;
        /** Heap Survivor max */
        hsm?: number;

        /** Heap Tenured used */
        htu?: number;
        /** Heap Tenured max */
        htm?: number;

        /** Code cache used */
        ccu?: number;
        /** Code cache max */
        ccm?: number;

        /** Loaded class count */
        lcc?: number;
        /** Totale loaded class count */
        tlc?: number;
        /** Unloaded class count */
        ucc?: number;

        /** Thread count */
        ttc?: number;
        /** Peak thread count */
        ptc?: number;
        /** Unloaded class count */
        stc?: number;
    }

    export interface EventData {
        /** Date time */
        d?: string;

        /** Event type */
        t?: "a" | "e" | "x" | "i" | "r";
        /** Application name */
        a?: string;
        /** Class name */
        c?: string;
        /** Method event */
        m?: string;

        /** Elapsed [ms] */
        e?: number;
        /** Result size */
        s?: number;
        /** Exception */
        x?: string;
    }

    export interface WProfData {
        /** System data */
        sys?: SysData[];
        /** JVM data */
        jvm?: JvmData[];
        /** Event data */
        evn?: EventData[];
        /** Message */
        msg?: string;
    }
}