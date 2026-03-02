@param com.dumpster.calculator.web.viewmodel.MaterialPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.seoTitle()}</title>
    <meta name="description" content="${model.metaDescription()}">
    <meta name="robots" content="max-snippet:-1, max-image-preview:large, max-video-preview:-1">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <link rel="icon" type="image/png" href="/favicon.png">
    <meta property="og:title" content="${model.seoTitle()}">
    <meta property="og:description" content="${model.metaDescription()}">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.ogImageUrl()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${model.seoTitle()}">
    <meta name="twitter:description" content="${model.metaDescription()}">
    <meta name="twitter:image" content="${model.ogImageUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/tailwind.generated.css">
    <link rel="stylesheet" href="/css/app.css">
    <link rel="stylesheet" href="/css/calculator-modern.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${model.calculatorAbsoluteUrl()}"},
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
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "HowTo",
          "name": "How to estimate dumpster weight for ${model.materialName()}",
          "description": "Use density range and size-level overage risk to choose a safer dumpster strategy.",
          "step": [
            {
              "@type": "HowToStep",
              "name": "Set material and quantity",
              "text": "Start with ${model.materialName()} and enter a measured quantity in the simulator."
            },
            {
              "@type": "HowToStep",
              "name": "Check size-by-size weight table",
              "text": "Compare low, typical, and high weight ranges against included tons for each dumpster size."
            },
            {
              "@type": "HowToStep",
              "name": "Select safer booking strategy",
              "text": "If overage risk is medium or high, use safer sizing or plan multi-haul."
            }
          ]
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Article",
          "headline": "${model.title()}",
          "datePublished": "${model.publishedDateIso()}",
          "dateModified": "${model.modifiedDateIso()}",
          "author": {
            "@type": "Organization",
            "name": "Dumpster Decision OS"
          },
          "mainEntityOfPage": "${model.canonicalUrl()}"
        }
    </script>
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Material intelligence", primaryHref = "/dumpster/size-weight-calculator?material=" + model.materialId(), primaryLabel = "Open simulator")

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">Material Guide</p>
                <h1 class="font-display">${model.title()}</h1>
                <p class="answer-first">${model.answerFirst()}</p>
                <p>${model.materialName()} is usually a <strong>weight-first</strong> decision surface. Treat volume as secondary after tonnage limits.</p>
                <div class="quick-facts">
                    <div class="fact-chip">
                        <strong>Typical density</strong>
                        <span>${model.densityTyp()} lbs/yd3</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Typical load</strong>
                        <span>${model.exampleVolumeYd3()} yd3 ~= ${model.exampleWeightTypTons()} tons</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Wet swing</strong>
                        <span>${(int) ((model.wetMultiplierLow() - 1) * 100)}% to ${(int) ((model.wetMultiplierHigh() - 1) * 100)}%</span>
                    </div>
                </div>
                <div class="scenario-chip-group">
                    <span class="scenario-chip">Weight-limited first</span>
                    <span class="scenario-chip">Range-based output</span>
                    <span class="scenario-chip">Overage risk visible</span>
                </div>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()} | Source snapshot: ${model.sourceDateDisplay()}</p>
            </article>

            <aside class="launch-card">
                <h2 class="font-display">Launch calculator with this material</h2>
                <p>Start with a preset scenario and move into the live decision workflow.</p>
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
                    <button type="submit">Open calculator preset</button>
                </form>
                <div class="mistake-strip">
                    <strong>Common mistake</strong>
                    <p>Picking size by volume only can create heavy overage fees.</p>
                    <p><strong>Ask vendor:</strong> ${model.operatorQuestion()}</p>
                </div>
            </aside>
        </div>
    </section>

    <section class="module-stack">
        <section class="table-card">
            <h2 class="font-display">What changes the answer</h2>
            <div class="decision-snapshot-grid">
                <article class="decision-tile">
                    <h3>Quick rules</h3>
                    <ul>
                        @for(var rule : model.quickRules())
                            <li>${rule}</li>
                        @endfor
                    </ul>
                </article>
                <article class="decision-tile">
                    <h3>Decision snapshot</h3>
                    <p><strong>Scenario input:</strong> ${model.scenarioInput()}</p>
                    <p><strong>Decision direction:</strong> ${model.scenarioDecision()}</p>
                    <p>${model.cautionNote()}</p>
                </article>
                <article class="decision-tile">
                    <h3>Feasible starting sizes</h3>
                    <ul>
                        @for(var row : model.sizeWeightTable())
                            @if(row.overageRisk().equals("Low") || row.overageRisk().equals("Medium"))
                                <li>${row.sizeYd()} yd: ${row.overageRisk()} risk (${row.weightLowTons()} to ${row.weightHighTons()} tons)</li>
                            @endif
                        @endfor
                    </ul>
                </article>
            </div>
            <div class="inline-cta-row">
                <a class="ui-button ui-button--primary" href="/dumpster/size-weight-calculator?material=${model.materialId()}">Run calculator with this material</a>
                <a class="ui-button ui-button--ghost" href="${model.materialGuidesUrl()}">Browse all material guides</a>
            </div>
        </section>

        <section class="table-card">
            <h2 class="font-display">Estimated weight by dumpster size</h2>
            <div class="table-scroll">
                <table class="data-table" aria-label="Dumpster size weight comparison table for ${model.materialName()}">
                    <caption>Estimated load tonnage and overage risk by dumpster size for ${model.materialName()}</caption>
                    <thead>
                    <tr>
                        <th>Dumpster</th>
                        <th>Dimensions</th>
                        <th>Effective yd3</th>
                        <th>Weight low</th>
                        <th>Weight typical</th>
                        <th>Weight high</th>
                        <th>Included tons</th>
                        <th>Overage risk</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var row : model.sizeWeightTable())
                        <tr>
                            <td>${row.sizeYd()} yd</td>
                            <td>${row.dimensions()}</td>
                            <td>${row.effectiveVolumeYd3()}</td>
                            <td>${row.weightLowTons()} tons</td>
                            <td>${row.weightTypTons()} tons</td>
                            <td>${row.weightHighTons()} tons</td>
                            <td>${row.includedTonsTyp()} tons</td>
                            <td>${row.overageRisk()}</td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
            <p>Source: ${model.source()} (updated ${model.sourceDateDisplay()})</p>
        </section>
    </section>

    <section class="table-card">
        <h2 class="font-display">Related guides and FAQ</h2>
        <h3>FAQ</h3>
        <div class="compact-faq">
            @for(var faq : model.faqItems())
                <details class="faq-item">
                    <summary>${faq.question()}</summary>
                    <p>${faq.answer()}</p>
                </details>
            @endfor
        </div>

        <h3>Related project guides</h3>
        <ul class="link-grid">
            @for(var link : model.relatedProjectLinks())
                <li>
                    <a href="${link.href()}">${link.label()}</a>
                    <span>${link.summary()}</span>
                </li>
            @endfor
        </ul>

        @if(model.intentClusterLinks() != null && !model.intentClusterLinks().isEmpty())
            <details class="ui-accordion">
                <summary>
                    <span>Long-tail intent guides</span>
                    <small>Expanded only when needed</small>
                </summary>
                <div class="ui-accordion__body">
                    <ul class="link-grid">
                        @for(var link : model.intentClusterLinks())
                            <li>
                                <a href="${link.href()}">${link.label()}</a>
                                <span>${link.summary()}</span>
                            </li>
                        @endfor
                    </ul>
                </div>
            </details>
        @endif

        <h3>Related material guides</h3>
        <ul class="link-grid">
            @for(var link : model.relatedMaterialLinks())
                <li>
                    <a href="${link.href()}">${link.label()}</a>
                    <span>${link.summary()}</span>
                </li>
            @endfor
        </ul>
    </section>
</main>
@template.components.appFooter()
</body>
</html>
