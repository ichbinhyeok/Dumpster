@param com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <meta name="description" content="${model.description()}">
    <meta name="robots" content="max-snippet:-1, max-image-preview:large, max-video-preview:-1">
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
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Outfit:wght@500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap">
    <link rel="stylesheet" href="/css/tailwind.generated.css">
    <link rel="stylesheet" href="/css/app.css">
    <link rel="stylesheet" href="/css/calculator-modern.css">
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
</head>
<body class="neo-body neo-content">
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
            <span class="trust-pill">Material library</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator">Open simulator</a>
        </div>
    </div>
</header>

<main class="shell shell--main">
    <section class="hero-insight">
        <div class="hero-grid">
            <div>
                <p class="eyebrow">${model.eyebrow()}</p>
                <h1 class="font-display">${model.pageTitle()}</h1>
                <p class="answer-first"><strong>Direct answer:</strong> use material density first, then confirm overage risk by size. This hub is built to move users from search intent to simulator-ready decisions in one click.</p>
                <p>${model.description()}</p>
            </div>
            <aside class="hero-proof">
                <h2 class="font-display">Why this hub exists</h2>
                <ul>
                    <li>Grouped by debris class so users avoid random trial-and-error sizing.</li>
                    <li>Density and sample tonnage exposed for transparent decision-making.</li>
                    <li>Every guide routes directly to live simulation with presets.</li>
                </ul>
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
