@param com.dumpster.calculator.web.viewmodel.IntentPageViewModel model
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
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${model.calculatorUrl()}"},
            {"@type": "ListItem", "position": 2, "name": "Project Guides", "item": "${model.projectGuidesUrl()}"},
            {"@type": "ListItem", "position": 3, "name": "Material Guides", "item": "${model.materialGuidesUrl()}"},
            {"@type": "ListItem", "position": 4, "name": "${model.intentLabel()}", "item": "${model.canonicalUrl()}"}
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
          "@type": "Article",
          "headline": "${model.pageTitle()}",
          "datePublished": "${model.modifiedDateIso()}",
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
            <span class="trust-pill">${model.intentLabel()}</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator?project=${model.projectId()}&material=${model.materialId()}">Open simulator</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel hero-insight">
        <div class="hero-grid">
            <div>
                <p class="eyebrow">Intent Guide</p>
                <h1>${model.pageTitle()}</h1>
                <p class="answer-first"><strong>Direct answer:</strong> ${model.directAnswer()}</p>
                <p>${model.intentSummary()}</p>
                <p>${model.evidenceNote()}</p>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
            </div>
            <aside class="hero-proof">
                <h2>Scenario launcher</h2>
                <form class="intent-widget" action="/dumpster/size-weight-calculator" method="get">
                    <input type="hidden" name="project" value="${model.projectId()}">
                    <input type="hidden" name="material" value="${model.materialId()}">
                    <label for="intent-qty-cluster">Quantity</label>
                    <input id="intent-qty-cluster" name="qty" type="number" min="0.1" step="0.1" value="6">
                    <label for="intent-unit-cluster">Unit</label>
                    <select id="intent-unit-cluster" name="unit">
                        <option value="pickup_load">Pickup load</option>
                        <option value="cubic_yard">Cubic yard</option>
                        <option value="truckload_small">Small truckload</option>
                    </select>
                    <button type="submit">Run live estimate</button>
                </form>
            </aside>
        </div>
    </section>

    <section class="panel">
        <h2>Size-by-size load comparison</h2>
        <table class="data-table" aria-label="Intent-specific dumpster load comparison table">
            <caption>Size-by-size load and overage comparison for this project and material scenario</caption>
            <thead>
            <tr>
                <th>Dumpster</th>
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
        <h2>Decision checklist</h2>
        <ol>
            @for(var item : model.decisionChecklist())
                <li>${item}</li>
            @endfor
        </ol>
    </section>

    <section class="panel">
        <h2>Frequently asked questions</h2>
        <div class="faq-list">
            @for(var faq : model.faqItems())
                <details class="faq-item">
                    <summary>${faq.question()}</summary>
                    <p>${faq.answer()}</p>
                </details>
            @endfor
        </div>
    </section>

    @if(model.relatedIntentLinks() != null && !model.relatedIntentLinks().isEmpty())
        <section class="panel">
            <h2>Related intent guides</h2>
            <ul>
                @for(var link : model.relatedIntentLinks())
                    <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
                @endfor
            </ul>
            <p><a href="${model.materialGuidesUrl()}">Browse material guides</a> | <a href="${model.projectGuidesUrl()}">Browse project guides</a></p>
        </section>
    @endif

    @if(model.relatedMaterialLinks() != null && !model.relatedMaterialLinks().isEmpty())
        <section class="panel">
            <h2>Related material guides</h2>
            <ul>
                @for(var link : model.relatedMaterialLinks())
                    <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
                @endfor
            </ul>
        </section>
    @endif

    @if(model.relatedProjectLinks() != null && !model.relatedProjectLinks().isEmpty())
        <section class="panel">
            <h2>Related project guides</h2>
            <ul>
                @for(var link : model.relatedProjectLinks())
                    <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
                @endfor
            </ul>
        </section>
    @endif
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
