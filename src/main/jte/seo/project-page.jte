@param com.dumpster.calculator.web.viewmodel.ProjectPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.title()}</title>
    <link rel="canonical" href="${model.canonicalPath()}">
    <link rel="stylesheet" href="/css/app.css">
</head>
<body>
<main class="shell">
    <section class="panel">
        <p class="eyebrow">Project Guide</p>
        <h1>${model.title()}</h1>
        <p>Recommended quick input unit: ${model.recommendedUnit()}</p>
        <p>Default material preset: ${model.defaultMaterialId()}</p>
    </section>
    <section class="panel">
        <h2>Mistake to avoid</h2>
        <p>${model.commonMistake()}</p>
        <p><a class="button-link primary" href="/dumpster/size-weight-calculator">Open calculator preset flow</a></p>
    </section>
</main>
</body>
</html>

