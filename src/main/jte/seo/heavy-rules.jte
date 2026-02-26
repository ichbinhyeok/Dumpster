@param com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Heavy Debris Rules</title>
    <meta name="description" content="Heavy debris dumpster rules: why smaller bins and multi-haul strategy can beat larger containers for dense materials.">
    <link rel="canonical" href="/dumpster/heavy-debris-rules">
    <meta property="og:title" content="Heavy Debris Rules for Dumpster Decisions">
    <meta property="og:description" content="Understand included tons vs haul limits, fill-ratio caps, and operator questions before booking.">
    <meta property="og:type" content="article">
    <meta property="og:url" content="/dumpster/heavy-debris-rules">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="Heavy Debris Rules">
    <meta name="twitter:description" content="Avoid overweight and rejection scenarios with heavy debris load rules.">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "FAQPage",
          "mainEntity": [
            {
              "@type": "Question",
              "name": "Why do heavy materials often require smaller dumpsters?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Dense debris can exceed haul limits quickly, so operators frequently use smaller bins and multiple hauls."
              }
            },
            {
              "@type": "Question",
              "name": "Is included tonnage the same as max haul limit?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "No. Included tons are a pricing threshold, while max haul limits are operational constraints."
              }
            }
          ]
        }
    </script>
</head>
<body>
<main class="shell">
    <section class="panel">
        <p class="eyebrow">Heavy Debris</p>
        <h1>Heavy Debris Rules for Dumpster Decisions</h1>
        <ul>
            @for(var rule : model.rules())
                <li>${rule}</li>
            @endfor
        </ul>
    </section>
    <section class="panel">
        <h2>Questions to ask before booking</h2>
        <ol>
            @for(var q : model.operatorQuestions())
                <li>${q}</li>
            @endfor
        </ol>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Run calculator</a></p>
    </section>
</main>
</body>
</html>
