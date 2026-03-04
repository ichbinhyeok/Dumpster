(function () {
    const pagePath = window.location.pathname || "";
    if (pagePath !== "/dumpster/dumpster-vs-junk-removal-which-is-cheaper") {
        return;
    }

    const toggles = Array.from(document.querySelectorAll("[data-priority-toggle]"));
    const explainer = document.getElementById("priority-explainer");
    const modeDescriptions = {
        cost: "Current mode: Lowest cost. Focus on staged dumpster economics first, then fallback to junk convenience.",
        speed: "Current mode: Fastest completion. Junk-first paths rise when timing pressure is high.",
        labor: "Current mode: Least effort. Routes that include crew-loading and fewer DIY steps are prioritized.",
        heavy: "Current mode: Heavy-load safety. Feasibility and pickup reliability override headline price."
    };
    const calculatorLinks = Array.from(document.querySelectorAll("a[href*='/dumpster/size-weight-calculator']"));

    function postServerEvent(eventName, payload) {
        fetch("/api/events", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ eventName, payload: payload || {} })
        }).catch(() => {
        });
    }

    function trackClientEvent(eventName, payload) {
        const analytics = window.dumpsterAnalytics;
        if (!analytics || typeof analytics.track !== "function") {
            return;
        }
        analytics.track(eventName, payload || {});
    }

    function normalizePriorityMode(mode) {
        const normalized = String(mode || "").toLowerCase();
        if (normalized === "cost" || normalized === "speed" || normalized === "labor" || normalized === "heavy") {
            return normalized;
        }
        return "cost";
    }

    function withPriorityParam(href, mode) {
        if (!href || !href.includes("/dumpster/size-weight-calculator")) {
            return href;
        }
        try {
            const url = new URL(href, window.location.origin);
            url.searchParams.set("priority", normalizePriorityMode(mode));
            if (href.startsWith("http")) {
                return url.toString();
            }
            return url.pathname + url.search + url.hash;
        } catch (_) {
            return href;
        }
    }

    function syncCalculatorLinks(mode) {
        calculatorLinks.forEach((link) => {
            const href = link.getAttribute("href") || "";
            const updated = withPriorityParam(href, mode);
            if (updated) {
                link.setAttribute("href", updated);
            }
        });
    }

    function applyMode(mode, source) {
        const normalizedMode = normalizePriorityMode(mode);
        toggles.forEach((button) => {
            button.classList.toggle("is-active", button.getAttribute("data-priority-mode") === normalizedMode);
        });

        if (explainer) {
            explainer.textContent = modeDescriptions[normalizedMode] || modeDescriptions.cost;
        }

        syncCalculatorLinks(normalizedMode);

        postServerEvent("comparison_priority_selected", {
            mode: normalizedMode,
            source: source || "interaction"
        });
        trackClientEvent("comparison_priority_selected", {
            mode: normalizedMode,
            page_path: pagePath
        });
    }

    if (!toggles.length) {
        return;
    }

    toggles.forEach((button) => {
        button.addEventListener("click", () => {
            const mode = button.getAttribute("data-priority-mode") || "cost";
            applyMode(mode, "toggle_click");
        });
    });

    const initial = toggles.find((button) => button.classList.contains("is-active"));
    const initialMode = initial ? initial.getAttribute("data-priority-mode") : "cost";
    applyMode(initialMode || "cost", "initial_render");
})();
