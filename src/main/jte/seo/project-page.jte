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
    <link rel="stylesheet" href="/css/app.css?v=stripeapple2">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple2">
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
    <div class="calc-layout">
        <!-- Sidebar / Details (Sticky Layout) -->
        <aside class="sticky-panel">
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
                <div class="mistake-strip" style="background: rgba(239, 68, 68, 0.05); border: 1px solid rgba(239, 68, 68, 0.2);">
                    <strong style="color: #DC2626; display: flex; align-items: center; gap: 6px;">Warning: Mistake prevention</strong>
                    <p style="color: #991B1B; font-weight: 500;">${model.commonMistake()}</p>
                    <p style="color: #991B1B; font-size: 0.8rem;"><strong>Ask vendor:</strong> ${model.operatorQuestion()}</p>
                </div>
            </aside>
        </aside>

        <!-- Main Dashboard Surface -->
        <div class="module-stack">

    <section class="table-card" style="box-shadow: var(--shadow-glass); border: none;">
        <h2 class="font-display">Decision snapshot</h2>
        <div class="decision-snapshot-grid">
            <article class="decision-tile" style="background: var(--surface-muted); box-shadow: none; border-radius: var(--radius-control);">
                <h3 style="color: var(--text-subtle); font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.06em;">Quick rules</h3>
                <ul style="gap: 6px; font-weight: 600; font-size: 0.85rem;">
                    @for(var rule : model.quickRules())
                        <li>${rule}</li>
                    @endfor
                </ul>
            </article>
            <article class="decision-tile" style="background: var(--surface-muted); box-shadow: none; border-radius: var(--radius-control);">
                <h3 style="color: var(--text-subtle); font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.06em;">Planning baseline</h3>
                <p style="font-weight: 800; font-size: 1.1rem; color: var(--text);">${model.sampleInput()}</p>
                <p style="font-weight: 600; color: var(--primary);">${model.sampleDecision()}</p>
            </article>
            <article class="decision-tile" style="background: var(--surface-muted); box-shadow: none; border-radius: var(--radius-control);">
                <h3 style="color: var(--text-subtle); font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.06em;">When it flips</h3>
                <p style="font-size: 0.85rem; color: var(--text-muted); font-weight: 500;">Material mix, moisture, and included-ton constraints are the most common reason the cheapest option stops being cheapest.</p>
                <p style="font-size: 0.85rem; font-weight: 600; border-top: 1px solid var(--line); padding-top: 8px; margin-top: 4px;"><strong>Direct strategy:</strong> ${model.recommendedStrategy()}</p>
            </article>
        </div>
        <div class="inline-cta-row" style="margin-top: 12px; border-top: 1px solid var(--line); padding-top: 16px;">
            <a class="ui-button ui-button--primary" style="flex: 1;" href="/dumpster/size-weight-calculator?project=${model.projectId()}&material=${model.defaultMaterialId()}" data-analytics-event="project_page_to_calculator_click" data-analytics-params='{"source":"inline_cta"}'>Launch preset calculator</a>
            <a class="ui-button ui-button--ghost" style="flex: 1;" href="${model.projectGuidesUrl()}">View all project guides</a>
        </div>
    </section>

        <section class="table-card" style="box-shadow: var(--shadow-glass); border: none;">
            <h2 class="font-display">Related guides and FAQ</h2>
            
            <h3 style="margin-top: 12px;">FAQ</h3>
            <div class="compact-faq">
                @for(var faq : model.faqItems())
                    <details class="faq-item" style="box-shadow: none; border: 1px solid var(--line); border-radius: var(--radius-control);">
                        <summary style="font-size: 0.95rem;">${faq.question()}</summary>
                        <p style="font-size: 0.85rem; line-height: 1.6;">${faq.answer()}</p>
                    </details>
                @endfor
            </div>

            <h3 style="margin-top: 12px;">Related material guides</h3>
            <ul class="link-grid">
                @for(var link : model.relatedMaterialLinks())
                    <li style="box-shadow: none; background: var(--surface-muted); border: 1px solid transparent; transition: all 0.2s;">
                        <a href="${link.href()}" style="color: var(--primary); font-size: 0.95rem;">${link.label()}</a>
                        <span style="font-size: 0.8rem; font-weight: 500;">${link.summary()}</span>
                    </li>
                @endfor
            </ul>

            @if(model.intentClusterLinks() != null && !model.intentClusterLinks().isEmpty())
                <details class="ui-accordion" style="box-shadow: none; border: 1px dashed var(--line); margin-top: 8px;">
                    <summary>
                        <span>Long-tail intent guides</span>
                        <small>Advanced variants</small>
                    </summary>
                    <div class="ui-accordion__body">
                        <ul class="link-grid" style="grid-template-columns: 1fr;">
                            @for(var link : model.intentClusterLinks())
                                <li style="box-shadow: none; border-bottom: 1px solid var(--line); border-radius: 0; padding-bottom: 12px;">
                                    <a href="${link.href()}" style="font-size: 0.9rem;">${link.label()}</a>
                                    <span style="font-size: 0.78rem;">${link.summary()}</span>
                                </li>
                            @endfor
                        </ul>
                    </div>
                </details>
            @endif

            <h3 style="margin-top: 12px;">Related project guides</h3>
            <ul class="link-grid">
                @for(var link : model.relatedProjectLinks())
                    <li style="box-shadow: none; background: var(--surface-muted); border: 1px solid transparent; transition: all 0.2s;">
                        <a href="${link.href()}" style="color: var(--primary); font-size: 0.95rem;">${link.label()}</a>
                        <span style="font-size: 0.8rem; font-weight: 500;">${link.summary()}</span>
                    </li>
                @endfor
            </ul>
        </section>
        
        </div> <!-- End Module Stack -->
    </div> <!-- End Grid Layout -->
</main>
@template.components.appFooter()
</body>
</html>
