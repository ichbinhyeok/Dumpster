@param com.dumpster.calculator.web.viewmodel.CalculatorPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <link rel="stylesheet" href="/css/app.css">
</head>
<body>
<main class="wrap">
    <section class="hero">
        <p class="eyebrow">v1.2 Lock Build</p>
        <h1>${model.pageTitle()}</h1>
        <p>Spring Boot + JTE + H2 scaffold is active. Engine and full UX are being wired in next steps.</p>
    </section>
</main>
</body>
</html>
