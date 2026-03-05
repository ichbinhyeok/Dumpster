@import com.dumpster.calculator.web.support.AnalyticsConfig
<script>
  (function () {
    const measurementId = "G-2QEL56XSNL";
    const ga4EnabledByServer = ${AnalyticsConfig.ga4Enabled()};
    const host = (window.location && window.location.hostname ? window.location.hostname : "").toLowerCase();
    const isLoopbackHost = host === "localhost" || host === "127.0.0.1" || host === "[::1]";
    const isPrivateIpv4Host = /^10\./.test(host)
      || /^192\.168\./.test(host)
      || /^172\.(1[6-9]|2[0-9]|3[0-1])\./.test(host);
    const isAutomation = typeof navigator !== "undefined" && navigator.webdriver === true;
    const hasTestQuery = /(?:[?&](?:e2e|test|qa)=1)(?:&|$)/i.test(window.location.search || "");
    const isGa4Enabled = ga4EnabledByServer && !isLoopbackHost && !isPrivateIpv4Host && !isAutomation && !hasTestQuery;

    window.__GA4_ENABLED__ = isGa4Enabled;
    window.__GA4_MEASUREMENT_ID__ = measurementId;
    if (!isGa4Enabled) {
      return;
    }

    window.dataLayer = window.dataLayer || [];
    window.gtag = function () { window.dataLayer.push(arguments); };
    window.gtag("js", new Date());
    window.gtag("config", measurementId, {
      anonymize_ip: true
    });

    const script = document.createElement("script");
    script.async = true;
    script.src = "https://www.googletagmanager.com/gtag/js?id=" + encodeURIComponent(measurementId);
    document.head.appendChild(script);
  })();
</script>
<script defer src="/js/analytics.js"></script>
