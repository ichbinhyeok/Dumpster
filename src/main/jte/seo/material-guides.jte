@param com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <meta name="description" content="${model.description()}">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <meta property="og:title" content="${model.pageTitle()}">
    <meta property="og:description" content="${model.description()}">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.pageTitle()}">
    <meta name="twitter:description" content="${model.description()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "CollectionPage",
          "name": "${model.pageTitle()}",
          "url": "${model.canonicalUrl()}"
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
            <span class="trust-pill">Material library</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator">Open simulator</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel">
        <p class="eyebrow">${model.eyebrow()}</p>
        <h1>${model.pageTitle()}</h1>
        <p>${model.description()}</p>
        <p>Use these guides to benchmark density, compare overage risk, and launch the simulator with better defaults in one click.</p>
    </section>

    @if(model.materialGroups() != null)
        @for(var group : model.materialGroups())
            <section class="panel">
                <h2>${group.categoryLabel()}</h2>
                <p>${group.categoryIntro()}</p>
                <ul>
                    @for(var link : group.materials())
                        <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
                    @endfor
                </ul>
            </section>
        @endfor
    @endif

    @if(model.comparisonTable() != null)
        <section class="panel">
            <h2>Material density comparison chart</h2>
            <table class="data-table">
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
        </section>
    @endif

    <section class="panel">
        <h2>All material guides</h2>
        <ul>
            @for(var link : model.links())
                <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
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
            <a href="/dumpster/heavy-debris-rules">Methodology</a>
            <a href="/legal/terms.html">Terms</a>
            <a href="/legal/privacy.html">Privacy</a>
        </div>
    </div>
</footer>
</body>
</html>
