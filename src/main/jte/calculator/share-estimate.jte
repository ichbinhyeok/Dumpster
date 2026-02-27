@param com.dumpster.calculator.web.viewmodel.ShareEstimateViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex,follow">
    <link rel="canonical" href="/dumpster/size-weight-calculator">
    <title>Shared Estimate ${model.estimateId()}</title>
    <link rel="stylesheet" href="/css/app.css">
</head>
<body>
<header class="site-header">
    <div class="shell shell--header">
        <a class="brand" href="/dumpster/size-weight-calculator" aria-label="Dumpster calculator home">
            <span class="brand-mark" aria-hidden="true">DC</span>
            <span class="brand-copy">
                <strong>Dumpster Decision OS</strong>
                <small>Range-based size + weight intelligence</small>
            </span>
        </a>
        <nav class="site-nav" aria-label="Primary">
            <a href="/dumpster/material-guides">Material guides</a>
            <a href="/dumpster/project-guides">Project guides</a>
            <a href="/dumpster/heavy-debris-rules">Heavy rules</a>
        </nav>
        <div class="site-header-actions">
            <span class="trust-pill">Share mode</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator">Run new estimate</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel">
        <p class="eyebrow">Shared Estimate</p>
        <h1>Estimate ${model.estimateId()}</h1>
        <p>Expires at: ${model.expiresAtText()}</p>
    </section>
    <section class="panel">
        <h2>Summary</h2>
        <p>Volume (yd3): ${model.storedEstimate().payload().result().volumeYd3().low()} - ${model.storedEstimate().payload().result().volumeYd3().high()}</p>
        <p>Weight (tons): ${model.storedEstimate().payload().result().weightTons().low()} - ${model.storedEstimate().payload().result().weightTons().high()}</p>
        <p>Price risk: ${model.storedEstimate().payload().result().priceRisk()}</p>
        <p>Feasibility: ${model.storedEstimate().payload().result().feasibility()}</p>
    </section>
    <section class="panel">
        <h2>Recommendations</h2>
        @for(var rec : model.storedEstimate().payload().result().recommendations())
            <article class="result-card">
                <h3>${rec.label()} - ${rec.sizeYd()}yd</h3>
                <p>Risk: ${rec.risk()} / Feasibility: ${rec.feasibility()}</p>
                <p>Multi-haul: ${rec.multiHaul()} (${rec.haulCount()} runs)</p>
            </article>
        @endfor
    </section>
    <section class="panel">
        <a class="button-link" href="/dumpster/size-weight-calculator">Back to calculator</a>
    </section>
</main>
<footer class="site-footer">
    <div class="shell shell--footer">
        <p>Planning estimate only. Final pricing and haul constraints are confirmed by your local vendor contract.</p>
        <div class="micro-links">
            <span>Data sources: EPA baseline conversion references + local table checks</span>
            <a href="/dumpster/heavy-debris-rules">Methodology</a>
            <a href="/legal/terms.html">Terms</a>
            <a href="/legal/privacy.html">Privacy</a>
        </div>
    </div>
</footer>
</body>
</html>

