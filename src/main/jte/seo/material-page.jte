@param com.dumpster.calculator.web.viewmodel.MaterialPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.title()}</title>
    <link rel="canonical" href="/dumpster/weight/${model.materialId()}">
    <link rel="stylesheet" href="/css/app.css">
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

