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
            return __data;
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
//# sourceMappingURL=wprof_base.js.map