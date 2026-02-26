@param com.dumpster.calculator.web.viewmodel.MaterialPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.title()}</title>
    <meta name="description" content="Density and weight range reference for ${model.title()}. Includes typical mistakes and calculator flow.">
    <link rel="canonical" href="/dumpster/weight/${model.materialId()}">
    <meta property="og:title" content="${model.title()}">
    <meta property="og:description" content="Material density range, example tonnage, and heavy-load warning.">
    <meta property="og:type" content="article">
    <meta property="og:url" content="/dumpster/weight/${model.materialId()}">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.title()}">
    <meta name="twitter:description" content="Use this material profile to avoid overweight dumpster selections.">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "/dumpster/size-weight-calculator"},
            {"@type": "ListItem", "position": 2, "name": "Material Weight", "item": "/dumpster/weight/${model.materialId()}"}
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
              "name": "Why is material density shown as a range?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Actual hauled weight changes with moisture, packing, and contamination. A range prevents false certainty."
              }
            },
            {
              "@type": "Question",
              "name": "Can a larger dumpster still be the wrong choice?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Yes. Heavy debris may hit haul limits before volume is filled, so smaller bins with multiple hauls are safer."
              }
            }
          ]
        }
    </script>
</head>
<body>
<main class="shell">
    <section class="panel">
        <p class="eyebrow">Material Weight Guide</p>
        <h1>${model.title()}</h1>
        <p>Density range (lbs/yd3): ${model.densityLow()} / ${model.densityTyp()} / ${model.densityHigh()}</p>
        <p>Example: ${model.exampleVolumeYd3()} yd3 is typically about ${model.exampleWeightTypTons()} tons.</p>
        <p>Source: ${model.source()}</p>
    </section>
    <section class="panel">
        <h2>Common mistake</h2>
        <p>Picking dumpster size by volume only can create overweight fees on dense materials.</p>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Run calculator with this material</a></p>
    </section>
</main>
</body>
</html>
