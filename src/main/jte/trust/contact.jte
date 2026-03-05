@import com.dumpster.calculator.web.support.JsonLd
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
    <meta property="og:type" content="website">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.ogImageUrl()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${model.seoTitle()}">
    <meta name="twitter:description" content="${model.seoDescription()}">
    <meta name="twitter:image" content="${model.ogImageUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple4">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple4">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Organization",
          "name": "Debris Decision",
          "url": "${JsonLd.content(String.valueOf(model.siteBaseUrl()))}",
          "logo": "${JsonLd.content(String.valueOf(model.siteBaseUrl()))}/favicon.png"
        }
    </script>
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Contact", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Run live estimate")
<main class="app-container app-main">
    <section class="panel">
        <p class="eyebrow">Contact</p>
        <h1>${model.pageTitle()}</h1>
        <p>For data corrections, source updates, or partnership questions, use the details below.</p>
    </section>

    <section class="panel">
        <h2>General inquiries</h2>
        <p>Email: <a href="mailto:hello@dumpsterdecisionos.com">hello@dumpsterdecisionos.com</a></p>
        <p>Response target: 3 business days for content or data inquiries.</p>
    </section>

    <section class="panel">
        <h2>Correction requests</h2>
        <ul>
            <li>Include page URL and the exact statement that needs correction.</li>
            <li>Include source reference if available.</li>
            <li>Provide market context if your request is region-specific.</li>
        </ul>
    </section>

    <section class="panel">
        <h2>Partnership requests</h2>
        <p>For future lead or integration partnerships, include expected coverage area, service constraints, and preferred intake format.</p>
    </section>
</main>
@template.components.appFooter()
</body>
</html>







