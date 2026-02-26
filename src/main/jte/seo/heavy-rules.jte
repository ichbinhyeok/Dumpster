@param com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Heavy Debris Rules</title>
    <link rel="canonical" href="/dumpster/heavy-debris-rules">
    <link rel="stylesheet" href="/css/app.css">
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

