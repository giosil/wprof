namespace WP {

    export function getData(): WProfData {
        let d = window ? window['__data'] as WProfData : undefined;
        if (d) {
            if (!d.sys) d.sys = [];
            if (!d.jvm) d.jvm = [];
            if (!d.evn) d.evn = [];
            return d;
        }
        return {
            sys: [],
            jvm: [],
            evn: [],
            msg: "No data available"
        };
    }

    export interface WProfData {
        /** System data */
        sys?: any[];
        sys_fields?: string[];
        /** JVM data */
        jvm?: any[];
        jvm_fields?: string[];
        /** Event data */
        evn?: any[];
        evn_fields?: string[];
        /** Message */
        msg?: string;
    }
}