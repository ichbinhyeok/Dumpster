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
    <link rel="stylesheet" href="/css/tailwind.generated.css">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple4">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple4">
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
            "name": "Debris Decision"
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
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Operational safety", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Run live estimate")

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">Heavy Debris Rules</p>
                <h1 class="font-display">Heavy Debris Rules for Dumpster Decisions</h1>
                <p class="answer-first"><strong>Direct answer:</strong> heavy debris jobs usually need smaller bins, reduced fill ratio, and staged pulls. Included tons control pricing, but max haul tons control pickup feasibility.</p>
                <p>Use this page as a policy explainer with direct launch paths back into the calculator.</p>
                <div class="quick-facts">
                    <div class="fact-chip">
                        <strong>Rule priority</strong>
                        <span>Feasibility before visible volume</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Pricing check</strong>
                        <span>Included tons and overage still apply</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Execution check</strong>
                        <span>Max haul + fill ratio can block pickup</span>
                    </div>
                </div>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
            </article>

            <aside class="launch-card">
                <h2 class="font-display">Run heavy mode preset</h2>
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
                    <button type="submit" data-analytics-event="rules_page_to_calculator_click" data-analytics-params='{"source":"launch_card"}'>Open heavy preset</button>
                </form>
                <div class="policy-chip-group">
                    <span class="scenario-chip">Clean-load checks</span>
                    <span class="scenario-chip">Fill-ratio limits</span>
                    <span class="scenario-chip">Staged hauls</span>
                </div>
            </aside>
        </div>
    </section>

    <section class="policy-summary-card">
        <h2 class="font-display">Policy summary</h2>
        <p>${model.includedVsMaxExplanation()}</p>
        <div class="policy-do-dont">
            <article>
                <h3>Do</h3>
                <ul>
                    @for(var rule : model.rules())
                        <li>${rule}</li>
                    @endfor
                </ul>
            </article>
            <article>
                <h3>Do not</h3>
                <ul>
                    <li>Assume included tons guarantees pickup feasibility.</li>
                    <li>Load dense debris to visual full height without fill-ratio confirmation.</li>
                    <li>Skip clean-load checks when heavy material is mixed.</li>
                </ul>
            </article>
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">Included tons vs Max haul</h2>
        <div class="table-scroll">
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
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">Questions to ask before booking</h2>
        <ol>
            @for(var q : model.operatorQuestions())
                <li>${q}</li>
            @endfor
        </ol>
        <h3>FAQ</h3>
        <div class="compact-faq">
            <details class="faq-item">
                <summary>Why do heavy materials often require smaller dumpsters?</summary>
                <p>Concrete, masonry, and dense soil can exceed transport limits long before a large bin looks full. Smaller bins with controlled fill and planned multi-haul are often safer.</p>
            </details>
            <details class="faq-item">
                <summary>Is included tonnage the same as max haul limit?</summary>
                <p>No. Included tons are quote pricing thresholds, while max haul tons are transport and safety constraints.</p>
            </details>
        </div>
        <div class="inline-cta-row">
            <a class="ui-button ui-button--primary" href="/dumpster/size-weight-calculator?material=concrete&project=concrete_removal" data-analytics-event="rules_page_to_calculator_click" data-analytics-params='{"source":"inline_cta"}'>Run calculator in heavy mode</a>
            <a class="ui-button ui-button--ghost" href="${model.materialGuidesUrl()}">Browse material guides</a>
            <a class="ui-button ui-button--ghost" href="${model.projectGuidesUrl()}">Browse project guides</a>
            <a class="ui-button ui-button--ghost" href="/dumpster/dumpster-vs-junk-removal-which-is-cheaper" data-analytics-event="comparison_hub_entry_click" data-analytics-params='{"source":"heavy_rules","placement":"inline_cta"}'>Compare dumpster vs junk</a>
        </div>
    </section>
</main>
@template.components.appFooter()
</body>
</html>


