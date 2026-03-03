(function () {
    const SEARCH_ENGINES = [
        /google\./i,
        /bing\.com$/i,
        /search\.yahoo\.com$/i,
        /duckduckgo\.com$/i,
        /search\.naver\.com$/i,
        /daum\.net$/i
    ];

    function hasGtag() {
        return typeof window.gtag === "function";
    }

    function track(eventName, params) {
        if (!hasGtag()) {
            return;
        }
        window.gtag("event", eventName, params || {});
    }

    function parseJson(value) {
        if (!value) {
            return {};
        }
        try {
            const parsed = JSON.parse(value);
            return parsed && typeof parsed === "object" ? parsed : {};
        } catch (_) {
            return {};
        }
    }

    function referrerHost() {
        try {
            if (!document.referrer) {
                return "";
            }
            return new URL(document.referrer).hostname.toLowerCase();
        } catch (_) {
            return "";
        }
    }

    function isOrganicReferrer(host) {
        if (!host) {
            return false;
        }
        return SEARCH_ENGINES.some((pattern) => pattern.test(host));
    }

    const host = referrerHost();
    if (isOrganicReferrer(host)) {
        track("organic_landing_page", {
            landing_path: window.location.pathname,
            referrer_host: host
        });
    }

    document.addEventListener("click", function (event) {
        const target = event.target.closest("[data-analytics-event]");
        if (!target) {
            return;
        }

        const eventName = (target.getAttribute("data-analytics-event") || "").trim();
        if (!eventName) {
            return;
        }

        const params = parseJson(target.getAttribute("data-analytics-params"));
        params.page_path = window.location.pathname;
        track(eventName, params);
    });

    window.dumpsterAnalytics = {
        track
    };
})();
