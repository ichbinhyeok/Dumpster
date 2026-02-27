@param com.dumpster.calculator.web.viewmodel.ProjectPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.title()}</title>
    <meta name="description" content="Project-specific dumpster guidance for ${model.title()}. Includes recommended input unit, default material, and common mistake warnings.">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <meta property="og:title" content="${model.title()}">
    <meta property="og:description" content="Project guide with input presets and risk-aware dumpster strategy.">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.title()}">
    <meta name="twitter:description" content="Use this project preset to avoid common dumpster sizing mistakes.">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "/dumpster/size-weight-calculator"},
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
                <p>Default material preset: ${model.defaultMaterialId()}</p>
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
            <a href="/dumpster/heavy-debris-rules">Methodology</a>
            <a href="/legal/terms.html">Terms</a>
            <a href="/legal/privacy.html">Privacy</a>
        </div>
    </div>
</footer>
</body>
</html>
