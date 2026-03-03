@param com.dumpster.calculator.web.viewmodel.SpecialSeoPageViewModel model
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
    <link rel="stylesheet" href="/css/app.css?v=stripeapple2">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple2">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${model.calculatorUrl()}"},
            {"@type": "ListItem", "position": 2, "name": "${model.pageTitle()}", "item": "${model.canonicalUrl()}"}
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
@template.components.appHeader(trustLabel = model.eyebrow(), primaryHref = model.primaryCtaHref(), primaryLabel = model.primaryCtaLabel())

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">${model.eyebrow()}</p>
                <h1 class="font-display">${model.pageTitle()}</h1>
                <p class="answer-first"><strong>Direct answer:</strong> ${model.directAnswer()}</p>
                <p>${model.summary()}</p>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
            </article>

            <aside class="launch-card">
                <h2 class="font-display">Take the next decision step</h2>
                <p>Open a live preset or compare alternatives before booking.</p>
                <div class="inline-cta-row">
                    <a class="ui-button ui-button--primary" href="${model.primaryCtaHref()}" data-analytics-event="comparison_page_cta_click" data-analytics-params='{"source":"primary_cta"}'>${model.primaryCtaLabel()}</a>
                    <a class="ui-button ui-button--ghost" href="${model.secondaryCtaHref()}" data-analytics-event="comparison_page_cta_click" data-analytics-params='{"source":"secondary_cta"}'>${model.secondaryCtaLabel()}</a>
                </div>
                <div class="mistake-strip">
                    <strong>Common mistakes to avoid</strong>
                    <ul>
                        @for(var item : model.quickChecks())
                            <li>${item}</li>
                        @endfor
                    </ul>
                </div>
            </aside>
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">Decision matrix</h2>
        <div class="table-scroll">
            <table class="data-table" aria-label="Decision matrix for ${model.pageTitle()}">
                <caption>Key constraints and recommended direction</caption>
                <thead>
                <tr>
                    <th>Factor</th>
                    <th>Baseline</th>
                    <th>Implication</th>
                </tr>
                </thead>
                <tbody>
                @for(var row : model.decisionRows())
                    <tr>
                        <td>${row.factor()}</td>
                        <td>${row.baseline()}</td>
                        <td>${row.implication()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">FAQ</h2>
        <div class="compact-faq">
            @for(var faq : model.faqItems())
                <details class="faq-item">
                    <summary>${faq.question()}</summary>
                    <p>${faq.answer()}</p>
                </details>
            @endfor
        </div>
    </section>

    @if(model.relatedLinks() != null && !model.relatedLinks().isEmpty())
        <section class="panel">
            <h2 class="font-display">Related decision pages</h2>
            <ul class="link-grid">
                @for(var link : model.relatedLinks())
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
