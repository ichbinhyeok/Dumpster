@param com.dumpster.calculator.web.viewmodel.IntentPageViewModel model
@param boolean indexableIntent
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.seoTitle()}</title>
    <meta name="description" content="${model.metaDescription()}">
    @if(indexableIntent)
        <meta name="robots" content="max-snippet:-1,max-image-preview:large,max-video-preview:-1">
    @else
        <meta name="robots" content="noindex,follow,max-snippet:-1,max-image-preview:large,max-video-preview:-1">
    @endif
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
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = model.intentLabel(), primaryHref = "/dumpster/size-weight-calculator?project=" + model.projectId() + "&material=" + model.materialId(), primaryLabel = "Open simulator")

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">Intent Guide</p>
                <h1 class="font-display">${model.pageTitle()}</h1>
                <p class="answer-first"><strong>Direct answer:</strong> ${model.directAnswer()}</p>
                <p>${model.intentSummary()}</p>
                <p>${model.evidenceNote()}</p>
                <div class="scenario-chip-group">
                    <span class="scenario-chip">${model.projectTitle()}</span>
                    <span class="scenario-chip">${model.materialName()}</span>
                    <span class="scenario-chip">${model.intentLabel()}</span>
                </div>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
            </article>
            <aside class="launch-card">
                <h2 class="font-display">Scenario launcher</h2>
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

    <section class="table-card">
        <h2 class="font-display">Size-by-size load comparison</h2>
        <div class="table-scroll">
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
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">Decision checklist</h2>
        <div class="decision-snapshot-grid">
            @for(var item : model.decisionChecklist())
                <article class="decision-tile">
                    <h3>Checklist item</h3>
                    <p>${item}</p>
                </article>
            @endfor
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">Frequently asked questions</h2>
        <div class="compact-faq">
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
            <h2 class="font-display">Related intent guides</h2>
            <ul class="link-grid">
                @for(var link : model.relatedIntentLinks())
                    <li>
                        <a href="${link.href()}">${link.label()}</a>
                        <span>${link.summary()}</span>
                    </li>
                @endfor
            </ul>
            <p><a href="${model.materialGuidesUrl()}">Browse material guides</a> | <a href="${model.projectGuidesUrl()}">Browse project guides</a></p>
        </section>
    @endif

    @if(model.relatedMaterialLinks() != null && !model.relatedMaterialLinks().isEmpty())
        <section class="panel">
            <h2 class="font-display">Related material guides</h2>
            <ul class="link-grid">
                @for(var link : model.relatedMaterialLinks())
                    <li>
                        <a href="${link.href()}">${link.label()}</a>
                        <span>${link.summary()}</span>
                    </li>
                @endfor
            </ul>
        </section>
    @endif

    @if(model.relatedProjectLinks() != null && !model.relatedProjectLinks().isEmpty())
        <section class="panel">
            <h2 class="font-display">Related project guides</h2>
            <ul class="link-grid">
                @for(var link : model.relatedProjectLinks())
                    <li>
                        <a href="${link.href()}">${link.label()}</a>
                        <span>${link.summary()}</span>
                    </li>
                @endfor
            </ul>
        </section>
    @endif
</main>
@template.components.appFooter()
</body>
</html>
