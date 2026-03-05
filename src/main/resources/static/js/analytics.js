(function () {
    const SEARCH_ENGINES = [
        /google\./i,
        /bing\.com$/i,
        /search\.yahoo\.com$/i,
        /duckduckgo\.com$/i,
        /search\.naver\.com$/i,
        /daum\.net$/i
    ];

    function isGa4Enabled() {
        return window.__GA4_ENABLED__ !== false;
    }

    function hasGtag() {
        return isGa4Enabled() && typeof window.gtag === "function";
    }

    function track(eventName, params) {
        if (!hasGtag()) {
            return;
        }
        window.gtag("event", eventName, params || {});
    }

    function postServerEvent(eventName, payload) {
        fetch("/api/events", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                eventName,
                payload: payload || {}
            })
        }).catch(() => {
        });
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
    const pagePath = window.location.pathname || "";
    if (isOrganicReferrer(host)) {
        track("organic_landing_page", {
            landing_path: pagePath,
            referrer_host: host
        });
    }

    if (pagePath === "/dumpster/dumpster-vs-junk-removal-which-is-cheaper") {
        postServerEvent("comparison_page_view", { path: pagePath });
    }

    if (pagePath === "/dumpster/pickup-truck-loads-to-dumpster-size") {
        postServerEvent("pickup_converter_used", { source: "page_view" });
    }

    if (pagePath.startsWith("/dumpster/answers/")) {
        const parts = pagePath.split("/").filter(Boolean);
        if (parts.length >= 5) {
            postServerEvent("answer_page_group", {
                projectId: parts[2],
                materialId: parts[3],
                intentSlug: parts[4]
            });
        }
    }

    if (pagePath === "/dumpster/dumpster-vs-junk-removal-which-is-cheaper") {
        document.addEventListener("click", function (event) {
            const target = event.target.closest("a[href*='/dumpster/size-weight-calculator']");
            if (!target) {
                return;
            }
            postServerEvent("comparison_page_exit_to_calculator", {
                href: target.getAttribute("href") || ""
            });
        });
    }

    if (pagePath === "/dumpster/size-weight-calculator") {
        const vendorDetails = Array.from(document.querySelectorAll("details"));
        vendorDetails.forEach((node) => {
            node.addEventListener("toggle", function () {
                if (!node.open) {
                    return;
                }
                const summary = node.querySelector("summary");
                postServerEvent("vendor_questions_expand", {
                    section: summary ? (summary.textContent || "").trim() : ""
                });
            });
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
        if (!params.link_href && target.tagName === "A") {
            params.link_href = target.getAttribute("href") || "";
        }
        if (!params.link_label) {
            params.link_label = ((target.textContent || "").trim() || "").slice(0, 120);
        }
        track(eventName, params);
        postServerEvent(eventName, params);
    });

    window.dumpsterAnalytics = {
        track
    };
})();
