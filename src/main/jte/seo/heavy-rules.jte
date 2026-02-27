@param com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Heavy Debris Rules</title>
    <meta name="description" content="Heavy debris dumpster rules: why smaller bins and multi-haul strategy can beat larger containers for dense materials.">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <meta property="og:title" content="Heavy Debris Rules for Dumpster Decisions">
    <meta property="og:description" content="Understand included tons vs haul limits, fill-ratio caps, and operator questions before booking.">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="Heavy Debris Rules">
    <meta name="twitter:description" content="Avoid overweight and rejection scenarios with heavy debris load rules.">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "FAQPage",
          "mainEntity": [
            {
              "@type": "Question",
              "name": "Why do heavy materials often require smaller dumpsters?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Dense debris can exceed haul limits quickly, so operators frequently use smaller bins and multiple hauls."
              }
            },
            {
              "@type": "Question",
              "name": "Is included tonnage the same as max haul limit?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "No. Included tons are a pricing threshold, while max haul limits are operational constraints."
              }
            }
          ]
        }
    </script>
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
            <span class="trust-pill">Operational safety</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator">Open simulator</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel hero-insight">
        <div class="hero-grid">
            <div>
                <p class="eyebrow">Heavy Debris</p>
                <h1>Heavy Debris Rules for Dumpster Decisions</h1>
                <p>Dense materials behave differently from light cleanouts. Use these rules to avoid rejected hauls and surprise overage costs.</p>
            </div>
            <aside class="hero-proof">
                <h2>Quick simulator jump</h2>
                <form class="intent-widget" action="/dumpster/size-weight-calculator" method="get">
                    <input type="hidden" name="material" value="concrete">
                    <input type="hidden" name="project" value="concrete_removal">
                    <label for="intent-qty-heavy">Quantity</label>
                    <input id="intent-qty-heavy" name="qty" type="number" min="0.1" step="0.1" value="4">
                    <label for="intent-unit-heavy">Unit</label>
                    <select id="intent-unit-heavy" name="unit">
                        <option value="pickup_load">Pickup load</option>
                        <option value="cubic_yard">Cubic yard</option>
                        <option value="sqft_4in">sqft @ 4in</option>
                    </select>
                    <button type="submit">Open live estimate</button>
                </form>
            </aside>
        </div>
    </section>
    <section class="panel">
        <ul>
            @for(var rule : model.rules())
                <li>${rule}</li>
            @endfor
        </ul>
    </section>
    <section class="panel">
        <article class="mistake-callout">
            <h2>Questions to ask before booking</h2>
            <ol>
                @for(var q : model.operatorQuestions())
                    <li>${q}</li>
                @endfor
            </ol>
        </article>
        <p><a href="${model.materialGuidesUrl()}">Browse material guides</a></p>
        <p><a href="${model.projectGuidesUrl()}">Browse project guides</a></p>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Run calculator</a></p>
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
