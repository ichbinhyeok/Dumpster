@param com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <meta name="description" content="${model.description()}">
    <meta name="robots" content="noindex,follow,max-snippet:-1,max-image-preview:large,max-video-preview:-1">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <link rel="icon" type="image/png" href="/favicon.png">
    <meta property="og:title" content="${model.pageTitle()}">
    <meta property="og:description" content="${model.description()}">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.ogImageUrl()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${model.pageTitle()}">
    <meta name="twitter:description" content="${model.description()}">
    <meta name="twitter:image" content="${model.ogImageUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/tailwind.generated.css">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple3">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple3">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "CollectionPage",
          "name": "${model.pageTitle()}",
          "url": "${model.canonicalUrl()}"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${model.calculatorUrl()}"},
            {"@type": "ListItem", "position": 2, "name": "Material Guides", "item": "${model.canonicalUrl()}"}
          ]
        }
    </script>
    @if(model.hubFaqItems() != null && !model.hubFaqItems().isEmpty())
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "FAQPage",
          "mainEntity": [
            @for(int i = 0; i < model.hubFaqItems().size(); i++)
                {
                  "@type": "Question",
                  "name": "${model.hubFaqItems().get(i).question()}",
                  "acceptedAnswer": {
                    "@type": "Answer",
                    "text": "${model.hubFaqItems().get(i).answer()}"
                  }
                }
                @if(i < model.hubFaqItems().size() - 1),@endif
            @endfor
          ]
        }
    </script>
    @endif
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Material library", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Open simulator")

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">${model.eyebrow()}</p>
                <h1 class="font-display">${model.pageTitle()}</h1>
                <p class="answer-first"><strong>Direct answer:</strong> use material density first, then confirm overage risk by size.</p>
                <p>${model.description()}</p>
                <div class="quick-facts">
                    <div class="fact-chip">
                        <strong>Navigation model</strong>
                        <span>Grouped by debris class</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Decision basis</strong>
                        <span>Density + allowance risk</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Action path</strong>
                        <span>Each guide has preset launcher</span>
                    </div>
                </div>
            </article>
            <aside class="launch-card">
                <h2 class="font-display">Launch calculator</h2>
                <p>Open live calculator now, or pick a specific material guide below.</p>
                <div class="inline-cta-row">
                    <a class="ui-button ui-button--primary" href="/dumpster/size-weight-calculator">Open simulator</a>
                    <a class="ui-button ui-button--ghost" href="/dumpster/weight/asphalt_shingles">Try shingles preset</a>
                </div>
            </aside>
        </div>
    </section>

    @if(model.materialGroups() != null)
        @for(var group : model.materialGroups())
            <section class="panel">
                <h2 class="font-display">${group.categoryLabel()}</h2>
                <p>${group.categoryIntro()}</p>
                <ul class="link-grid">
                    @for(var link : group.materials())
                        <li>
                            <a href="${link.href()}">${link.label()}</a>
                            <span>${link.summary()}</span>
                        </li>
                    @endfor
                </ul>
            </section>
        @endfor
    @endif

    @if(model.comparisonTable() != null)
        <section class="panel">
            <h2 class="font-display">Material density comparison chart</h2>
            <div class="table-scroll">
                <table class="data-table" aria-label="Material density and example weight comparison">
                    <caption>Typical density and example weight by material category</caption>
                    <thead>
                    <tr>
                        <th>Material</th>
                        <th>Category</th>
                        <th>Typical density</th>
                        <th>Example weight</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var row : model.comparisonTable())
                        <tr>
                            <td><a href="${row.materialHref()}">${row.materialName()}</a></td>
                            <td>${row.category()}</td>
                            <td>${(int) row.densityTyp()} lbs/yd3</td>
                            <td>${row.exampleWeightTons()} tons</td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </section>
    @endif

    @if(model.intentClusterLinks() != null && !model.intentClusterLinks().isEmpty())
        <section class="panel">
            <h2 class="font-display">Intent-driven guide cluster</h2>
            <p>These pages target high-intent questions where users want a direct recommendation, not generic background copy.</p>
            <ul class="link-grid">
                @for(var link : model.intentClusterLinks())
                    <li>
                        <a href="${link.href()}">${link.label()}</a>
                        <span>${link.summary()}</span>
                    </li>
                @endfor
            </ul>
        </section>
    @endif

    <section class="panel">
        <h2 class="font-display">All material guides</h2>
        <ul class="link-grid">
            @for(var link : model.links())
                <li>
                    <a href="${link.href()}">${link.label()}</a>
                    <span>${link.summary()}</span>
                </li>
            @endfor
        </ul>
        @if(model.hubFaqItems() != null)
            <h3>FAQ</h3>
            <div class="faq-list">
                @for(var faq : model.hubFaqItems())
                    <details class="faq-item">
                        <summary>${faq.question()}</summary>
                        <p>${faq.answer()}</p>
                    </details>
                @endfor
            </div>
        @endif
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Run calculator</a></p>
    </section>
</main>
@template.components.appFooter()
</body>
</html>
