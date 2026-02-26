@param com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <meta name="description" content="${model.description()}">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <meta property="og:title" content="${model.pageTitle()}">
    <meta property="og:description" content="${model.description()}">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.pageTitle()}">
    <meta name="twitter:description" content="${model.description()}">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "CollectionPage",
          "name": "${model.pageTitle()}",
          "url": "${model.canonicalUrl()}"
        }
    </script>
</head>
<body>
<main class="shell">
    <section class="panel">
        <p class="eyebrow">${model.eyebrow()}</p>
        <h1>${model.pageTitle()}</h1>
        <p>${model.description()}</p>
    </section>
    <section class="panel">
        <h2>All material guides</h2>
        <ul>
            @for(var link : model.links())
                <li><a href="${link.href()}">${link.label()}</a> - ${link.summary()}</li>
            @endfor
        </ul>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Run calculator</a></p>
    </section>
</main>
</body>
</html>
