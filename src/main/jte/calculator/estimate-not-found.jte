@param String estimateId
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex,follow">
    <link rel="canonical" href="/dumpster/size-weight-calculator">
    <title>Estimate Not Found</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple2">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple2">
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Share mode", primaryHref = "/dumpster/size-weight-calculator", primaryLabel = "Run new estimate")
<main class="app-container app-main">
    <section class="panel">
        <p class="eyebrow">Estimate Link</p>
        <h1>Estimate unavailable</h1>
        <p>The estimate `${estimateId}` was not found or has expired.</p>
        <a class="button-link" href="/dumpster/size-weight-calculator">Run a new estimate</a>
    </section>
</main>
@template.components.appFooter()
</body>
</html>






