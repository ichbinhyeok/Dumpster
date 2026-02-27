@param com.dumpster.calculator.web.viewmodel.MaterialPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.title()}</title>
    <meta name="description" content="Density and weight range reference for ${model.title()}. Includes typical mistakes and calculator flow.">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <meta property="og:title" content="${model.title()}">
    <meta property="og:description" content="Material density range, example tonnage, and heavy-load warning.">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.title()}">
    <meta name="twitter:description" content="Use this material profile to avoid overweight dumpster selections.">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "/dumpster/size-weight-calculator"},
            {"@type": "ListItem", "position": 2, "name": "Material Guides", "item": "${model.materialGuidesUrl()}"},
            {"@type": "ListItem", "position": 3, "name": "Material Weight", "item": "${model.canonicalUrl()}"}
          ]
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "FAQPage",
          "mainEntity": [
            @for(int i = 0; i < model.faqItems().size(); i++)
                {
                  "@type": "Question",
                  "name": "${model.faqItems().get(i).question()}",
                  "acceptedAnswer": {
                    "@type": "Answer",
                    "text": "${model.faqItems().get(i).answer()}"
                  }
                }
                @if(i < model.faqItems().size() - 1),@endif
            @endfor
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
            <span class="trust-pill">Material intelligence</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator?material=${model.materialId()}">Open simulator</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel hero-insight">
        <div class="hero-grid">
            <div>
                <p class="eyebrow">Material Weight Guide</p>
                <h1>${model.title()}</h1>
                <p>Category: ${model.categoryLabel()}</p>
                <p class="answer-first">${model.answerFirst()}</p>
                <p>Density range (lbs/yd3): ${model.densityLow()} / ${model.densityTyp()} / ${model.densityHigh()}</p>
                <p>Example: ${model.exampleVolumeYd3()} yd3 is about ${model.exampleWeightLowTons()} to ${model.exampleWeightHighTons()} tons (typical ${model.exampleWeightTypTons()} tons).</p>
            </div>
            <aside class="hero-proof">
                <h2>Jump into live simulator</h2>
                <form class="intent-widget" action="/dumpster/size-weight-calculator" method="get">
                    <input type="hidden" name="material" value="${model.materialId()}">
                    <label for="intent-qty-material">Quantity</label>
                    <input id="intent-qty-material" name="qty" type="number" min="0.1" step="0.1" value="${model.exampleVolumeYd3()}">
                    <label for="intent-unit-material">Unit</label>
                    <select id="intent-unit-material" name="unit">
                        <option value="cubic_yard">Cubic yard</option>
                        <option value="pickup_load">Pickup load</option>
                        <option value="truckload_small">Small truckload</option>
                    </select>
                    <button type="submit">Run live estimate</button>
                </form>
            </aside>
        </div>
    </section>

    <section class="panel">
        <h2>Quick rules</h2>
        <ul>
            @for(var rule : model.quickRules())
                <li>${rule}</li>
            @endfor
        </ul>
        <p>${model.scenarioInput()}</p>
        <p>${model.scenarioDecision()}</p>
        <p>${model.cautionNote()}</p>
        <p>Source: ${model.source()}</p>
    </section>

    <section class="panel">
        <article class="mistake-callout">
            <h2>Common mistake</h2>
            <p>Picking dumpster size by volume only can create overweight fees on dense materials.</p>
            <p><strong>Operator question:</strong> ${model.operatorQuestion()}</p>
        </article>
        <h3>Related project guides</h3>
        <ul>
            @for(var link : model.relatedProjectLinks())
                <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
            @endfor
        </ul>
        <h3>FAQ</h3>
        <div class="faq-list">
            @for(var faq : model.faqItems())
                <details class="faq-item">
                    <summary>${faq.question()}</summary>
                    <p>${faq.answer()}</p>
                </details>
            @endfor
        </div>
        <p><a href="${model.materialGuidesUrl()}">Browse all material guides</a></p>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator?material=${model.materialId()}">Run calculator with this material</a></p>
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
