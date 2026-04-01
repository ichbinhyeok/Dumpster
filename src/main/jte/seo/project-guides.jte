@import com.dumpster.calculator.web.support.JsonLd
@param com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <meta name="description" content="${model.description()}">
    <meta name="robots" content="max-snippet:-1,max-image-preview:large,max-video-preview:-1">
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
    <link rel="stylesheet" href="/css/app.css?v=stripeapple4">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple4">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "CollectionPage",
          "name": "${JsonLd.content(String.valueOf(model.pageTitle()))}",
          "url": "${JsonLd.content(String.valueOf(model.canonicalUrl()))}",
          "dateModified": "${JsonLd.content(String.valueOf(model.lastUpdatedIso()))}",
          "author": {
            "@type": "Organization",
            "name": "${JsonLd.content(String.valueOf(model.reviewedBy()))}"
          }
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${JsonLd.content(String.valueOf(model.calculatorUrl()))}"},
            {"@type": "ListItem", "position": 2, "name": "Project Guides", "item": "${JsonLd.content(String.valueOf(model.canonicalUrl()))}"}
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
                  "name": "${JsonLd.content(String.valueOf(model.hubFaqItems().get(i).question()))}",
                  "acceptedAnswer": {
                    "@type": "Answer",
                    "text": "${JsonLd.content(String.valueOf(model.hubFaqItems().get(i).answer()))}"
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
@template.components.appHeader(trustLabel = "Heavy-job library", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Run heavy-debris estimate")

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">${model.eyebrow()}</p>
                <h1 class="font-display">${model.pageTitle()}</h1>
                <p class="answer-first"><strong>Direct answer:</strong> start with the heavy-debris job preset, then validate with the material profile that controls haul risk.</p>
                <p>${model.description()}</p>
                <p class="meta-note">Reviewed by ${model.reviewedBy()} · Updated ${model.lastUpdatedIso()}</p>
                <div class="quick-facts">
                    <div class="fact-chip">
                        <strong>Preset-first flow</strong>
                        <span>Heavy-debris job before volume guess</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Risk trigger</strong>
                        <span>Weight + fill rule + allowance</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Output mode</strong>
                        <span>Feasible vs risky route</span>
                    </div>
                </div>
            </article>
            <aside class="launch-card">
                <h2 class="font-display">Jump into a heavy-debris workflow</h2>
                <p>Use a concrete, roofing, dirt, or remodel preset first, then fall back to the base calculator only when the job is unusual.</p>
                <div class="inline-cta-row">
                    <a class="ui-button ui-button--primary" href="/dumpster/size-weight-calculator">Run heavy-debris estimate</a>
                    <a class="ui-button ui-button--ghost" href="/about/quote-match-beta">Check local heavy-load options</a>
                    <a class="ui-button ui-button--ghost" href="/dumpster/size/concrete-removal">Open concrete removal</a>
                    <a class="ui-button ui-button--ghost" href="/dumpster/dumpster-vs-junk-removal-which-is-cheaper" data-analytics-event="comparison_hub_entry_click" data-analytics-params='{"source":"project_guides_hub","placement":"hero_cta"}'>Compare dumpster vs crew pickup</a>
                </div>
            </aside>
        </div>
    </section>

    <section class="panel">
        <h2 class="font-display">All project guides</h2>
        <ul class="link-grid">
            @for(var link : model.links())
                <li>
                    <a href="${link.href()}">${link.label()}</a>
                    <span>${link.summary()}</span>
                </li>
            @endfor
        </ul>
    </section>

    @if(model.intentClusterLinks() != null && !model.intentClusterLinks().isEmpty())
        <section class="panel">
            <h2 class="font-display">High-intent answer pages</h2>
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
        <h2 class="font-display">FAQ</h2>
        <div class="faq-list">
            @if(model.hubFaqItems() != null)
                @for(var faq : model.hubFaqItems())
                    <details class="faq-item">
                        <summary>${faq.question()}</summary>
                        <p>${faq.answer()}</p>
                    </details>
                @endfor
            @endif
        </div>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Run calculator</a></p>
        <p><a href="/about/quote-match-beta">Check local heavy-load availability for this project path</a></p>
        <p><a href="/dumpster/dumpster-vs-junk-removal-which-is-cheaper" data-analytics-event="comparison_hub_entry_click" data-analytics-params='{"source":"project_guides_hub","placement":"footer_link"}'>Compare dumpster vs crew pickup for this project flow</a></p>
    </section>
</main>
@template.components.appFooter()
</body>
</html>
