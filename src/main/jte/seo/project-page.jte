@param com.dumpster.calculator.web.viewmodel.ProjectPageViewModel model
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
            {"@type": "ListItem", "position": 2, "name": "Project Guides", "item": "${model.projectGuidesUrl()}"},
            {"@type": "ListItem", "position": 3, "name": "Project Guide", "item": "${model.canonicalUrl()}"}
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
          "name": "How to choose dumpster size for ${model.title()}",
          "description": "Follow project preset, compare strategy, and confirm operator constraints before booking.",
          "step": [
            {
              "@type": "HowToStep",
              "name": "Start from project preset",
              "text": "Use ${model.title()} preset with ${model.defaultMaterialName()} as baseline material."
            },
            {
              "@type": "HowToStep",
              "name": "Use the recommended input unit",
              "text": "Enter quantity in ${model.recommendedUnit()} and review safe versus budget outcomes."
            },
            {
              "@type": "HowToStep",
              "name": "Validate vendor constraints",
              "text": "Before booking, confirm included tons, overage fee, and haul-limit policy."
            }
          ]
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Article",
          "headline": "${model.title()}",
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
@template.components.appHeader(trustLabel = "Project preset", primaryHref = "/dumpster/size-weight-calculator?project=" + model.projectId() + "&material=" + model.defaultMaterialId(), primaryLabel = "Open simulator")

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">Project Guide</p>
                <h1 class="font-display">${model.title()}</h1>
                <p class="answer-first">${model.answerFirst()}</p>
                <p>Use this page as a workflow template: start with project preset, then validate risk and feasibility before booking.</p>
                <div class="quick-facts">
                    <div class="fact-chip">
                        <strong>Recommended unit</strong>
                        <span>${model.recommendedUnit()}</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Default material</strong>
                        <span>${model.defaultMaterialName()}</span>
                    </div>
                    <div class="fact-chip">
                        <strong>Primary strategy</strong>
                        <span>${model.recommendedStrategy()}</span>
                    </div>
                </div>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
            </article>

            <aside class="launch-card">
                <h2 class="font-display">Launch project preset</h2>
                <p>Open calculator with project + default material preloaded.</p>
                <form class="intent-widget" action="/dumpster/size-weight-calculator" method="get">
                    <input type="hidden" name="project" value="${model.projectId()}">
                    <input type="hidden" name="material" value="${model.defaultMaterialId()}">
                    <label for="intent-qty-project">Quantity</label>
                    <input id="intent-qty-project" name="qty" type="number" min="0.1" step="0.1" value="6">
                    <label for="intent-persona-project">Persona</label>
                    <select id="intent-persona-project" name="persona">
                        <option value="homeowner">Homeowner</option>
                        <option value="contractor">Contractor</option>
                        <option value="property_manager">Property manager</option>
                        <option value="investor">Flipper / investor</option>
                        <option value="business">Facility / business</option>
                    </select>
                    <button type="submit" data-analytics-event="project_page_to_calculator_click" data-analytics-params='{"source":"launch_card"}'>Start with preset</button>
                </form>
                <div class="mistake-strip">
                    <strong>Mistake prevention</strong>
                    <p>${model.commonMistake()}</p>
                    <p><strong>Ask vendor:</strong> ${model.operatorQuestion()}</p>
                </div>
            </aside>
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">Decision snapshot</h2>
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
                <h3>Typical planning baseline</h3>
                <p>${model.sampleInput()}</p>
                <p>${model.sampleDecision()}</p>
            </article>
            <article class="decision-tile">
                <h3>When recommendation flips</h3>
                <p>Material mix, moisture, and included-ton constraints are the most common reason the cheapest option stops being cheapest.</p>
                <p><strong>Direct strategy:</strong> ${model.recommendedStrategy()}</p>
            </article>
        </div>
        <div class="inline-cta-row">
            <a class="ui-button ui-button--primary" href="/dumpster/size-weight-calculator?project=${model.projectId()}&material=${model.defaultMaterialId()}" data-analytics-event="project_page_to_calculator_click" data-analytics-params='{"source":"inline_cta"}'>Open calculator preset flow</a>
            <a class="ui-button ui-button--ghost" href="${model.projectGuidesUrl()}">Browse all project guides</a>
        </div>
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

        <h3>Related material guides</h3>
        <ul class="link-grid">
            @for(var link : model.relatedMaterialLinks())
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

        <h3>Related project guides</h3>
        <ul class="link-grid">
            @for(var link : model.relatedProjectLinks())
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
