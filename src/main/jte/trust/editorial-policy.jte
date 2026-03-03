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
@template.components.appHeader(trustLabel = "Editorial", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Open simulator")
<main class="app-container app-main">
    <section class="panel">
        <p class="eyebrow">Editorial Policy</p>
        <h1>${model.pageTitle()}</h1>
        <p>Guides are written to support decision clarity, not traffic inflation. Every page should be specific, testable, and operationally useful.</p>
    </section>

    <section class="panel">
        <h2>Content standards</h2>
        <ul>
            <li>Use measurable ranges and explicit assumptions.</li>
            <li>Differentiate pricing allowance from operational haul constraints.</li>
            <li>Avoid unsupported guarantees or universal claims.</li>
            <li>Prefer practical operator questions over generic advice.</li>
        </ul>
    </section>

    <section class="panel">
        <h2>Update and correction process</h2>
        <ol>
            <li>Review source tables and conversion factors after data updates.</li>
            <li>Rebuild pages when material profiles or size policies change.</li>
            <li>Apply corrections when users or partners report factual errors.</li>
        </ol>
        <p>To request a correction, use <a href="/about/contact">the contact page</a>.</p>
    </section>
</main>
@template.components.appFooter()
</body>
</html>





