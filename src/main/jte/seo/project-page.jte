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
            {
              "@type": "Question",
              "name": "Which unit should I enter first?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Start with the recommended unit on this page to reduce conversion uncertainty."
              }
            },
            {
              "@type": "Question",
              "name": "How should I choose between safe and budget options?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Choose budget only if you can tolerate overage risk and possible second haul costs."
              }
            }
          ]
        }
    </script>
</head>
<body>
<main class="shell">
    <section class="panel">
        <p class="eyebrow">Project Guide</p>
        <h1>${model.title()}</h1>
        <p>Recommended quick input unit: ${model.recommendedUnit()}</p>
        <p>Default material preset: ${model.defaultMaterialId()}</p>
        <p>Recommended strategy: ${model.recommendedStrategy()}</p>
        <p>${model.sampleInput()}</p>
        <p>${model.sampleDecision()}</p>
    </section>
    <section class="panel">
        <h2>Mistake to avoid</h2>
        <p>${model.commonMistake()}</p>
        <p><strong>Operator question:</strong> ${model.operatorQuestion()}</p>
        <h3>Related material guides</h3>
        <ul>
            @for(var link : model.relatedMaterialLinks())
                <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
            @endfor
        </ul>
        <p><a href="${model.projectGuidesUrl()}">Browse all project guides</a></p>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Open calculator preset flow</a></p>
    </section>
</main>
</body>
</html>
