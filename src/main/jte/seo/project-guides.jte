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
    <meta property="og:image" content="${model.canonicalUrl().replace("/dumpster/project-guides", "")}/og-image.png">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.pageTitle()}">
    <meta name="twitter:description" content="${model.description()}">
    <meta name="twitter:image" content="${model.canonicalUrl().replace("/dumpster/project-guides", "")}/og-image.png">
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
            <span class="trust-pill">Project library</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator">Open simulator</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel">
        <p class="eyebrow">${model.eyebrow()}</p>
        <h1>${model.pageTitle()}</h1>
        <p>${model.description()}</p>
        <p>Each guide maps a real project type to safer input defaults, operator questions, and risk-first booking strategy.</p>
    </section>
    <section class="panel">
        <h2>All project guides</h2>
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
