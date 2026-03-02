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
    <link rel="stylesheet" href="/css/app.css">
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
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
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
        <h2>How much does ${model.materialName()} weigh in a dumpster?</h2>
        <p><strong>Direct answer:</strong> ${model.materialName()} is typically ${model.densityTyp()} lbs/yd3.</p>
        <p>A ${model.exampleVolumeYd3()} yd3 load is usually ${model.exampleWeightLowTons()} to ${model.exampleWeightHighTons()} tons, with a typical estimate of ${model.exampleWeightTypTons()} tons.</p>
    </section>

    <section class="panel">
        <h2>Best dumpster sizes for ${model.materialName()}</h2>
        <p>Low and medium overage-risk sizes are usually the safest starting points when timeline certainty matters.</p>
        <ul>
            @for(var row : model.sizeWeightTable())
                @if(row.overageRisk().equals("Low") || row.overageRisk().equals("Medium"))
                    <li>${row.sizeYd()} yd: ${row.overageRisk()} overage risk at ${row.weightLowTons()} to ${row.weightHighTons()} tons.</li>
                @endif
            @endfor
        </ul>
    </section>

    <section class="panel">
        <h2>Can ${model.materialName()} fit in a 10, 20, or 30-yard dumpster?</h2>
        @for(var row : model.sizeWeightTable())
            @if(row.sizeYd() == 10 || row.sizeYd() == 20 || row.sizeYd() == 30)
                <p><strong>${row.sizeYd()}-yard dumpster:</strong> ${row.overageRisk()} overage risk (typical ${row.weightTypTons()} tons vs ${row.includedTonsTyp()} included tons).</p>
            @endif
        @endfor
    </section>

    <section class="panel">
        <h2>Estimated weight by dumpster size</h2>
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
    </section>

    <section class="panel">
        <h2>How moisture affects weight</h2>
        <p>Wet loads can increase weight by ${(int) ((model.wetMultiplierLow() - 1) * 100)}% to ${(int) ((model.wetMultiplierHigh() - 1) * 100)}% for this material profile.</p>
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
        <p>Source: ${model.source()} (updated ${model.sourceDateDisplay()})</p>
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
        @if(model.intentClusterLinks() != null && !model.intentClusterLinks().isEmpty())
            <h3>Long-tail intent guides</h3>
            <ul>
                @for(var link : model.intentClusterLinks())
                    <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
                @endfor
            </ul>
        @endif
        <h3>Related material guides</h3>
        <ul>
            @for(var link : model.relatedMaterialLinks())
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
