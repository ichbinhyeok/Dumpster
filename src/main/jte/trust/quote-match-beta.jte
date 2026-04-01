@import com.dumpster.calculator.web.support.JsonLd
@param com.dumpster.calculator.web.viewmodel.TrustPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.seoTitle()}</title>
    <meta name="description" content="${model.seoDescription()}">
    <meta name="robots" content="noindex,follow,max-snippet:-1,max-image-preview:large,max-video-preview:-1">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <link rel="icon" type="image/png" href="/favicon.png">
    <meta property="og:title" content="${model.seoTitle()}">
    <meta property="og:description" content="${model.seoDescription()}">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.ogImageUrl()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${model.seoTitle()}">
    <meta name="twitter:description" content="${model.seoDescription()}">
    <meta name="twitter:image" content="${model.ogImageUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple4">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple4">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "WebPage",
          "name": "Local Heavy-Debris Match",
          "url": "${JsonLd.content(String.valueOf(model.canonicalUrl()))}"
        }
    </script>
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Local Heavy-Debris Match", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Run heavy-debris estimate")
<main class="app-container app-main">
    <section class="panel">
        <p class="eyebrow">Local Heavy-Debris Match</p>
        <h1>${model.pageTitle()}</h1>
        <p>This intake is for local heavy-load coverage review. We use your calculator result to queue follow-up when partner coverage is available for your ZIP and material profile.</p>
    </section>

    <section class="panel">
        <h2>What this queue does</h2>
        <ul>
            <li>Captures your project intent, dominant material, timing, and ZIP details.</li>
            <li>Queues your request for local heavy-load dumpster or crew-pickup follow-up.</li>
            <li>Helps confirm where heavy-debris coverage is strong enough for a real next step.</li>
        </ul>
    </section>

    <section class="panel">
        <h2>What to expect</h2>
        <ul>
            <li>This is not instant booking and does not guarantee same-day dispatch.</li>
            <li>Coverage depends on active partner markets and heavy-load policy fit.</li>
            <li>If your ZIP is covered, typical first response is within one business day.</li>
            <li>If your ZIP is not covered, we provide fallback guidance for direct vendor outreach or crew-pickup comparison.</li>
            <li>For corrections or source updates, use the <a href="/about/contact">contact page</a>.</li>
        </ul>
    </section>

    <section class="panel">
        <h2>Data and consent</h2>
        <ul>
            <li>We store contact info, ZIP, project scope, material profile, and timing to route your request.</li>
            <li>Submitted details are used only for local heavy-debris matching operations and quality review.</li>
            <li>You can request deletion or correction from the <a href="/about/contact">contact page</a>.</li>
        </ul>
    </section>

    <section class="panel">
        <h2>Next step</h2>
        <p>Return to the calculator, confirm the heavy-load route, then submit ZIP and contact details from the result board.</p>
        <p>
            <a class="ui-button ui-button--primary" href="/dumpster/size-weight-calculator">Back to heavy-debris calculator</a>
            <a class="ui-button ui-button--ghost" href="/dumpster/dumpster-vs-junk-removal-which-is-cheaper">Fallback: compare dumpster vs crew pickup</a>
        </p>
    </section>

    <section class="panel">
        <h2>Check beta queue status</h2>
        <p>If you already submitted, enter your intake ID (example: <code>qmb_xxxxxxxxxxxxxxxxxxxx</code>).</p>
        <div class="ui-field" style="max-width:28rem;">
            <label class="ui-field__label" for="intake-id">Intake ID</label>
            <input id="intake-id" type="text" maxlength="64" placeholder="qmb_...">
        </div>
        <p style="margin-top:0.75rem;">
            <button type="button" class="ui-button ui-button--primary" id="intake-status-check">Check status</button>
        </p>
        <p id="intake-status-output" class="calc-step__hint" aria-live="polite"></p>
    </section>
</main>
@template.components.appFooter()
<script src="/js/quote-match-beta.js?v=stripeapple4"></script>
</body>
</html>
