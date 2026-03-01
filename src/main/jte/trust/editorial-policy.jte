@param com.dumpster.calculator.web.viewmodel.TrustPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.seoTitle()}</title>
    <meta name="description" content="${model.seoDescription()}">
    <meta name="robots" content="max-snippet:-1, max-image-preview:large, max-video-preview:-1">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <link rel="icon" type="image/png" href="/favicon.png">
    <meta property="og:title" content="${model.seoTitle()}">
    <meta property="og:description" content="${model.seoDescription()}">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.canonicalUrl().replace("/about/editorial-policy", "")}/og-image.png">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.seoTitle()}">
    <meta name="twitter:description" content="${model.seoDescription()}">
    <meta name="twitter:image" content="${model.canonicalUrl().replace("/about/editorial-policy", "")}/og-image.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
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
            <span class="trust-pill">Editorial</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator">Open simulator</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel">
        <p class="eyebrow">Editorial Policy</p>
        <h1>${model.pageTitle()}</h1>
        <p>Guides are written to support decision clarity, not traffic inflation. Every page should be specific, testable, and operationally useful.</p>
    </section>

    <section class="panel">
        <h2>Content standards</h2>
        <ul>
            <li>Use measurable ranges and explicit assumptions.</li>
            <li>Differentiate pricing allowance from operational haul constraints.</li>
            <li>Avoid unsupported guarantees or universal claims.</li>
            <li>Prefer practical operator questions over generic advice.</li>
        </ul>
    </section>

    <section class="panel">
        <h2>Update and correction process</h2>
        <ol>
            <li>Review source tables and conversion factors after data updates.</li>
            <li>Rebuild pages when material profiles or size policies change.</li>
            <li>Apply corrections when users or partners report factual errors.</li>
        </ol>
        <p>To request a correction, use <a href="/about/contact">the contact page</a>.</p>
    </section>
</main>
<footer class="site-footer">
    <div class="shell shell--footer">
        <p>Planning estimate only. Final pricing and haul constraints are confirmed by your local vendor contract.</p>
        <div class="micro-links">
            <span>Data sources: EPA baseline conversion references + local table checks</span>
            <a href="/about/methodology">Methodology</a>
            <a href="/about/editorial-policy">Editorial policy</a>
            <a href="/about/contact">Contact</a>
            <a href="/legal/terms.html">Terms</a>
            <a href="/legal/privacy.html">Privacy</a>
        </div>
    </div>
</footer>
</body>
</html>
