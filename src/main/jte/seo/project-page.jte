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
    <link rel="stylesheet" href="/css/app.css">
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
            <span class="trust-pill">Project preset</span>
            <a class="button-link primary" href="/dumpster/size-weight-calculator?project=${model.projectId()}&material=${model.defaultMaterialId()}">Open simulator</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="panel hero-insight">
        <div class="hero-grid">
            <div>
                <p class="eyebrow">Project Guide</p>
                <h1>${model.title()}</h1>
                <p class="answer-first">${model.answerFirst()}</p>
                <p>Recommended quick input unit: ${model.recommendedUnit()}</p>
                <p>Default material preset: ${model.defaultMaterialName()}</p>
                <p>Recommended strategy: ${model.recommendedStrategy()}</p>
            </div>
            <aside class="hero-proof">
                <h2>Fast preset launcher</h2>
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
                    <button type="submit">Launch live simulator</button>
                </form>
            </aside>
        </div>
    </section>

    <section class="panel">
        <h2>What dumpster strategy is recommended for ${model.title()}?</h2>
        <p><strong>Direct answer:</strong> Start with ${model.recommendedStrategy()}</p>
        <p>Use ${model.defaultMaterialName()} as the default material profile and validate assumptions with ${model.recommendedUnit()} input before final booking.</p>
    </section>

    <section class="panel">
        <h2>What size dumpster is typical for ${model.title()}?</h2>
        <p><strong>Quick answer:</strong> ${model.sampleDecision()}</p>
        <p>If your load mix is uncertain or schedule risk is high, keep the safe recommendation instead of downgrading by volume alone.</p>
    </section>

    <section class="panel">
        <h2>How many loads should I plan for ${model.title()}?</h2>
        <p>${model.sampleInput()}</p>
        <p>Use this as a baseline, then confirm included tons, overage fee, and swap timing with your local operator before booking.</p>
    </section>

    <section class="panel">
        <h2>Quick rules</h2>
        <ul>
            @for(var rule : model.quickRules())
                <li>${rule}</li>
            @endfor
        </ul>
        <p>${model.sampleInput()}</p>
        <p>${model.sampleDecision()}</p>
    </section>

    <section class="panel">
        <article class="mistake-callout">
            <h2>Mistake to avoid</h2>
            <p>${model.commonMistake()}</p>
            <p><strong>Operator question:</strong> ${model.operatorQuestion()}</p>
        </article>
        <h3>Related material guides</h3>
        <ul>
            @for(var link : model.relatedMaterialLinks())
                <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
            @endfor
        </ul>
        @if(model.intentClusterLinks() != null && !model.intentClusterLinks().isEmpty())
            <h3>Long-tail intent guides</h3>
            <ul>
                @for(var link : model.intentClusterLinks())
                    <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
                @endfor
            </ul>
        @endif
        <h3>Related project guides</h3>
        <ul>
            @for(var link : model.relatedProjectLinks())
                <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
            @endfor
        </ul>
        <h3>FAQ</h3>
        <div class="faq-list">
            @for(var faq : model.faqItems())
                <details class="faq-item">
                    <summary>${faq.question()}</summary>
                    <p>${faq.answer()}</p>
                </details>
            @endfor
        </div>
        <p><a href="${model.projectGuidesUrl()}">Browse all project guides</a></p>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator?project=${model.projectId()}&material=${model.defaultMaterialId()}">Open calculator preset flow</a></p>
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
