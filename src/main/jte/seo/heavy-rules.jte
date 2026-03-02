@param com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Heavy Debris Dumpster Rules: Included Tons vs Max Haul Limits</title>
    <meta name="description" content="Heavy debris dumpster rules with size-by-size haul limits, fill-ratio caps, clean-load requirements, and safer multi-haul strategy guidance.">
    <meta name="robots" content="max-snippet:-1, max-image-preview:large, max-video-preview:-1">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <link rel="icon" type="image/png" href="/favicon.png">
    <meta property="og:title" content="Heavy Debris Dumpster Rules: Included Tons vs Max Haul Limits">
    <meta property="og:description" content="Understand included tons vs haul limits, fill-ratio caps, and clean-load constraints before booking.">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.ogImageUrl()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="Heavy Debris Dumpster Rules">
    <meta name="twitter:description" content="Avoid overweight and rejected hauls by comparing heavy-debris limits per dumpster size.">
    <meta name="twitter:image" content="${model.ogImageUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
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
                "text": "Heavy materials such as concrete, brick, and soil can hit transport limits before the container looks full. Operators often cap fill ratio and schedule smaller containers with multiple hauls because that approach is more reliable than one oversized load near pickup."
              }
            },
            {
              "@type": "Question",
              "name": "Is included tonnage the same as max haul limit?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "No. Included tons are the pricing allowance in your quote, while max haul limit is the operational transport ceiling. A load can be inside quote allowance yet still be rejected if hauled weight exceeds the truck or route constraint."
              }
            }
          ]
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Article",
          "headline": "Heavy Debris Dumpster Rules: Included Tons vs Max Haul Limits",
          "datePublished": "${model.modifiedDateIso()}",
          "dateModified": "${model.modifiedDateIso()}",
          "author": {
            "@type": "Organization",
            "name": "Dumpster Decision OS"
          },
          "mainEntityOfPage": "${model.canonicalUrl()}"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${model.calculatorUrl()}"},
            {"@type": "ListItem", "position": 2, "name": "Heavy Debris Rules", "item": "${model.canonicalUrl()}"}
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
                <p class="answer-first"><strong>Direct answer:</strong> heavy debris jobs usually need smaller bins, reduced fill ratio, and staged pulls. Included tons control pricing, but max haul tons control pickup feasibility, so both limits must be checked before loading.</p>
                <p>Dense materials behave differently from light cleanouts. Use these rules to avoid rejected hauls and surprise overage costs.</p>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
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
        <h2>Included tons vs max haul tons</h2>
        <p>${model.includedVsMaxExplanation()}</p>
        <table class="data-table" aria-label="Heavy debris haul limits by dumpster size">
            <caption>Heavy debris pickup constraints by dumpster size</caption>
            <thead>
            <tr>
                <th>Dumpster</th>
                <th>Dimensions</th>
                <th>Max haul (typ)</th>
                <th>Heavy fill ratio</th>
                <th>Effective yd3</th>
                <th>Clean load required</th>
            </tr>
            </thead>
            <tbody>
            @for(var row : model.heavyLimits())
                <tr>
                    <td>${row.sizeYd()} yd</td>
                    <td>${row.dimensions()}</td>
                    <td>${row.maxHaulTonsTyp()} tons</td>
                    <td>${row.fillRatioPercent()}%</td>
                    <td>${row.effectiveYd3()}</td>
                    <td>@if(row.cleanLoadRequired()) Yes @else No @endif</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </section>
    <section class="panel">
        <h2>Frequently asked questions</h2>
        <div class="faq-list">
            <details class="faq-item">
                <summary>Why do heavy materials often require smaller dumpsters?</summary>
                <p>Concrete, masonry, and dense soil can exceed transport limits long before a large bin looks full. Smaller bins with controlled fill and planned multi-haul are often safer because they respect haul caps and reduce pickup rejection risk.</p>
            </details>
            <details class="faq-item">
                <summary>Is included tonnage the same as max haul limit?</summary>
                <p>No. Included tons are quote pricing thresholds, while max haul tons are transport and safety constraints. If hauled weight exceeds haul limits, a load may fail pickup even when included-ton pricing appears acceptable.</p>
            </details>
        </div>
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
