@param com.dumpster.calculator.web.viewmodel.ShareEstimateViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex,follow">
    <link rel="canonical" href="/dumpster/size-weight-calculator">
    <title>Shared Estimate ${model.estimateId()}</title>
    <link rel="stylesheet" href="/css/app.css">
</head>
<body>
<main class="shell">
    <section class="panel">
        <p class="eyebrow">Shared Estimate</p>
        <h1>Estimate ${model.estimateId()}</h1>
        <p>Expires at: ${model.expiresAtText()}</p>
    </section>
    <section class="panel">
        <h2>Summary</h2>
        <p>Volume (yd3): ${model.storedEstimate().payload().result().volumeYd3().low()} - ${model.storedEstimate().payload().result().volumeYd3().high()}</p>
        <p>Weight (tons): ${model.storedEstimate().payload().result().weightTons().low()} - ${model.storedEstimate().payload().result().weightTons().high()}</p>
        <p>Price risk: ${model.storedEstimate().payload().result().priceRisk()}</p>
        <p>Feasibility: ${model.storedEstimate().payload().result().feasibility()}</p>
    </section>
    <section class="panel">
        <h2>Recommendations</h2>
        @for(var rec : model.storedEstimate().payload().result().recommendations())
            <article class="result-card">
                <h3>${rec.label()} - ${rec.sizeYd()}yd</h3>
                <p>Risk: ${rec.risk()} / Feasibility: ${rec.feasibility()}</p>
                <p>Multi-haul: ${rec.multiHaul()} (${rec.haulCount()} runs)</p>
            </article>
        @endfor
    </section>
    <section class="panel">
        <a class="button-link" href="/dumpster/size-weight-calculator">Back to calculator</a>
    </section>
</main>
</body>
</html>

