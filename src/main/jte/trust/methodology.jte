@param com.dumpster.calculator.web.viewmodel.TrustPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.seoTitle()}</title>
    <meta name="description" content="${model.seoDescription()}">
    <meta name="robots" content="max-snippet:-1, max-image-preview:large, max-video-preview:-1">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <link rel="icon" type="image/png" href="/favicon.png">
    <meta property="og:title" content="${model.seoTitle()}">
    <meta property="og:description" content="${model.seoDescription()}">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.ogImageUrl()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${model.seoTitle()}">
    <meta name="twitter:description" content="${model.seoDescription()}">
    <meta name="twitter:image" content="${model.ogImageUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple2">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple2">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Organization",
          "name": "Dumpster Decision OS",
          "url": "${model.siteBaseUrl()}",
          "logo": "${model.siteBaseUrl()}/favicon.png"
        }
    </script>
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Methodology", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Open simulator")
<main class="app-container app-main">
    <section class="panel">
        <p class="eyebrow">Methodology</p>
        <h1>${model.pageTitle()}</h1>
        <p>This calculator uses a range-based model instead of a single-point estimate to reflect real-world variation in debris loading.</p>
    </section>

    <section class="panel">
        <h2>How the estimate is built</h2>
        <ol>
            <li>Normalize user input into comparable volume and weight units.</li>
            <li>Apply material density range and scenario modifiers (wet load, mixed load, compaction).</li>
            <li>Compare resulting weight range against included tons and max haul constraints by dumpster size.</li>
            <li>Generate safe and budget recommendations with feasibility and price-risk flags.</li>
        </ol>
    </section>

    <section class="panel">
        <h2>What each risk signal means</h2>
        <ul>
            <li><strong>Price risk:</strong> likelihood of overage costs relative to included tons.</li>
            <li><strong>Feasibility:</strong> likelihood that haul operations succeed under max-haul constraints.</li>
            <li><strong>Multi-haul:</strong> strategy when dense loads are safer with smaller containers and swaps.</li>
        </ul>
    </section>

    <section class="panel">
        <h2>Source and update policy</h2>
        <p>Source references and version dates are tracked per material profile. Content and assumptions are reviewed when source tables or policy data changes.</p>
        <p><a href="/about/editorial-policy">Read editorial policy</a></p>
    </section>
</main>
@template.components.appFooter()
</body>
</html>





