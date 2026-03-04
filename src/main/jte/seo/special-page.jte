@param com.dumpster.calculator.web.viewmodel.SpecialSeoPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.seoTitle()}</title>
    <meta name="description" content="${model.metaDescription()}">
    <meta name="robots" content="max-snippet:-1, max-image-preview:large, max-video-preview:-1">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <link rel="icon" type="image/png" href="/favicon.png">
    <meta property="og:title" content="${model.seoTitle()}">
    <meta property="og:description" content="${model.metaDescription()}">
    <meta property="og:type" content="article">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta property="og:image" content="${model.ogImageUrl()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${model.seoTitle()}">
    <meta name="twitter:description" content="${model.metaDescription()}">
    <meta name="twitter:image" content="${model.ogImageUrl()}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Space+Grotesk:wght@500;600;700&display=swap">
    <link rel="stylesheet" href="/css/tailwind.generated.css">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple4">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple4">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${model.calculatorUrl()}"},
            {"@type": "ListItem", "position": 2, "name": "${model.pageTitle()}", "item": "${model.canonicalUrl()}"}
          ]
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "FAQPage",
          "mainEntity": [
            @for(int i = 0; i < model.faqItems().size(); i++)
                {
                  "@type": "Question",
                  "name": "${model.faqItems().get(i).question()}",
                  "acceptedAnswer": {
                    "@type": "Answer",
                    "text": "${model.faqItems().get(i).answer()}"
                  }
                }
                @if(i < model.faqItems().size() - 1),@endif
            @endfor
          ]
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Article",
          "headline": "${model.pageTitle()}",
          "datePublished": "${model.modifiedDateIso()}",
          "dateModified": "${model.modifiedDateIso()}",
          "author": {
            "@type": "Organization",
            "name": "Debris Decision"
          },
          "mainEntityOfPage": "${model.canonicalUrl()}"
        }
    </script>
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = model.eyebrow(), primaryHref = model.primaryCtaHref(), primaryLabel = model.primaryCtaLabel())

<main class="app-container app-main">
    <section class="entry-surface">
        <div class="entry-grid">
            <article class="quick-answer-card">
                <p class="eyebrow">${model.eyebrow()}</p>
                <h1 class="font-display">${model.pageTitle()}</h1>
                <p class="answer-first"><strong>Direct answer:</strong> ${model.directAnswer()}</p>
                <p>${model.summary()}</p>
                <p class="meta-note">Last updated: ${model.modifiedDateIso()}</p>
            </article>

            <aside class="launch-card">
                <h2 class="font-display">Take the next decision step</h2>
                <p>Open a live preset or compare alternatives before booking.</p>
                <div class="inline-cta-row">
                    <a class="ui-button ui-button--primary" href="${model.primaryCtaHref()}" data-analytics-event="comparison_page_cta_click" data-analytics-params='{"source":"primary_cta"}'>${model.primaryCtaLabel()}</a>
                    <a class="ui-button ui-button--ghost" href="${model.secondaryCtaHref()}" data-analytics-event="comparison_page_cta_click" data-analytics-params='{"source":"secondary_cta"}'>${model.secondaryCtaLabel()}</a>
                </div>
                <div class="mistake-strip">
                    <strong>Common mistakes to avoid</strong>
                    <ul>
                        @for(var item : model.quickChecks())
                            <li>${item}</li>
                        @endfor
                    </ul>
                </div>
            </aside>
        </div>
    </section>

    @if("dumpster-vs-junk-removal-which-is-cheaper".equals(model.slug()))
        <section class="table-card comparison-hub">
            <h2 class="font-display">Dumpster Wins vs Junk Wins</h2>
            <div class="comparison-win-grid">
                <article class="comparison-win-card is-dumpster" data-priority-cost="5" data-priority-speed="2" data-priority-labor="2" data-priority-heavy="3">
                    <h3>Dumpster usually wins when</h3>
                    <ul>
                        <li>You can load over multiple days on your own schedule.</li>
                        <li>Total debris is large enough to spread rental and haul cost.</li>
                        <li>You can pre-sort heavy materials before pickup day.</li>
                        <li>You want lower unit cost and can manage labor.</li>
                    </ul>
                </article>
                <article class="comparison-win-card is-junk" data-priority-cost="2" data-priority-speed="5" data-priority-labor="5" data-priority-heavy="2">
                    <h3>Junk removal usually wins when</h3>
                    <ul>
                        <li>You need fast removal and cannot wait for staged hauling.</li>
                        <li>Access is difficult or carrying labor is the main pain point.</li>
                        <li>Load composition is mixed and uncertain until cleanup starts.</li>
                        <li>You prefer convenience even if quoted cost is not lowest.</li>
                    </ul>
                </article>
            </div>
            <div class="decision-snapshot-grid comparison-metrics">
                <article class="decision-tile" data-priority-cost="4" data-priority-speed="2" data-priority-labor="2" data-priority-heavy="3">
                    <h3>Labor</h3>
                    <p>Dumpster lowers disposal unit cost but increases self-loading burden.</p>
                </article>
                <article class="decision-tile" data-priority-cost="5" data-priority-speed="2" data-priority-labor="2" data-priority-heavy="2">
                    <h3>Cost</h3>
                    <p>Dumpster often wins on larger staged jobs; junk can win for small urgent jobs.</p>
                </article>
                <article class="decision-tile" data-priority-cost="2" data-priority-speed="5" data-priority-labor="4" data-priority-heavy="1">
                    <h3>Speed</h3>
                    <p>Junk removal usually wins when same-day completion is the priority.</p>
                </article>
                <article class="decision-tile" data-priority-cost="1" data-priority-speed="1" data-priority-labor="2" data-priority-heavy="5">
                    <h3>Heavy-Risk</h3>
                    <p>If dense debris threatens pickup feasibility, route by safety first, then price.</p>
                </article>
            </div>
        </section>

        <section class="table-card comparison-priority-card" id="comparison-priority-tool">
            <h2 class="font-display">Pick your priority and let the plan order adapt</h2>
            <p>Use this like a homeowner decision shortcut before you request quotes.</p>
            <div class="priority-toggle-group" role="group" aria-label="Comparison priority">
                <button type="button" class="priority-toggle is-active" data-priority-toggle data-priority-mode="cost">Lowest cost</button>
                <button type="button" class="priority-toggle" data-priority-toggle data-priority-mode="speed">Fastest completion</button>
                <button type="button" class="priority-toggle" data-priority-toggle data-priority-mode="labor">Least effort</button>
                <button type="button" class="priority-toggle" data-priority-toggle data-priority-mode="heavy">Heavy-load safety</button>
            </div>
            <p class="priority-explainer" id="priority-explainer">Current mode: Lowest cost. Focus on staged dumpster economics first, then fallback to junk convenience.</p>
        </section>

        <section class="table-card">
            <h2 class="font-display">Scenario Plans (Kit-Inspired)</h2>
            <div class="comparison-plan-grid" id="comparison-plan-grid">
                <article class="comparison-plan-card" data-priority-cost="2" data-priority-speed="5" data-priority-labor="4" data-priority-heavy="1">
                    <h3>Starter Cleanout</h3>
                    <p class="plan-subtitle">Small, urgent, labor-limited</p>
                    <p class="plan-price">$280 to $640</p>
                    <ul>
                        <li>3 pickup-load level scope</li>
                        <li>Priority: fastest completion</li>
                        <li>Best fit: junk removal branch</li>
                    </ul>
                </article>
                <article class="comparison-plan-card is-emphasis" data-priority-cost="5" data-priority-speed="3" data-priority-labor="2" data-priority-heavy="3">
                    <h3>Remodel Standard</h3>
                    <p class="plan-subtitle">Medium to large staged volume</p>
                    <p class="plan-price">$520 to $1,050</p>
                    <ul>
                        <li>8 to 10 pickup-load level scope</li>
                        <li>Priority: lower unit economics</li>
                        <li>Best fit: dumpster with risk checks</li>
                    </ul>
                </article>
                <article class="comparison-plan-card" data-priority-cost="1" data-priority-speed="1" data-priority-labor="2" data-priority-heavy="5">
                    <h3>Heavy High-Risk</h3>
                    <p class="plan-subtitle">Dense debris, strict constraints</p>
                    <p class="plan-price">Route by feasibility first</p>
                    <ul>
                        <li>Concrete/soil/shingle dominant</li>
                        <li>Priority: avoid pickup refusal</li>
                        <li>Best fit: staged small-haul strategy</li>
                    </ul>
                </article>
            </div>
        </section>

        <section class="table-card">
            <h2 class="font-display">Decision Stats</h2>
            <dl class="comparison-stat-grid">
                <div>
                    <dt>Fastest Route</dt>
                    <dd>Junk Removal</dd>
                </div>
                <div>
                    <dt>Lowest Unit Cost (Large Jobs)</dt>
                    <dd>Dumpster</dd>
                </div>
                <div>
                    <dt>Most Common Risk</dt>
                    <dd>Overage + Pickup Failure</dd>
                </div>
                <div>
                    <dt>Safer Heavy Strategy</dt>
                    <dd>Smaller Staged Hauls</dd>
                </div>
            </dl>
        </section>

        <section class="table-card">
            <h2 class="font-display">Worked Cost Examples</h2>
            <div class="table-scroll">
                <table class="data-table" aria-label="Worked examples for dumpster vs junk removal scenarios">
                    <caption>Illustrative homeowner scenarios (ranges, not binding quotes)</caption>
                    <thead>
                    <tr>
                        <th>Scenario</th>
                        <th>Dumpster route</th>
                        <th>Junk route</th>
                        <th>Likely better move</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Small urgent cleanout (~3 pickup loads)</td>
                        <td>$340 to $620 plus loading labor</td>
                        <td>$280 to $640 with labor included</td>
                        <td>Junk removal if speed and labor convenience dominate</td>
                    </tr>
                    <tr>
                        <td>Large staged remodel (~8 to 10 pickup loads)</td>
                        <td>$520 to $1,050 depending on overage</td>
                        <td>$760 to $1,650 typical full-service range</td>
                        <td>Dumpster for lower unit economics when schedule is controlled</td>
                    </tr>
                    <tr>
                        <td>Heavy dense debris (concrete/soil mix)</td>
                        <td>Single large bin can fail pickup; staged 10yd may be safer</td>
                        <td>Premium pricing likely due to dense-load handling</td>
                        <td>Choose route by feasibility and safety before headline price</td>
                    </tr>
                    <tr>
                        <td>When smaller twice beats bigger once</td>
                        <td>Two 10yd hauls can avoid rejection and surprise overage</td>
                        <td>Not always practical for dense DIY demolition volume</td>
                        <td>Staged smaller dumpster pulls for heavy high-risk loads</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </section>
@endif

    <section class="table-card">
        <h2 class="font-display">Decision matrix</h2>
        <div class="table-scroll">
            <table class="data-table" aria-label="Decision matrix for ${model.pageTitle()}">
                <caption>Key constraints and recommended direction</caption>
                <thead>
                <tr>
                    <th>Factor</th>
                    <th>Baseline</th>
                    <th>Implication</th>
                </tr>
                </thead>
                <tbody>
                @for(var row : model.decisionRows())
                    <tr>
                        <td>${row.factor()}</td>
                        <td>${row.baseline()}</td>
                        <td>${row.implication()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </section>

    <section class="table-card">
        <h2 class="font-display">FAQ</h2>
        <div class="compact-faq">
            @for(var faq : model.faqItems())
                <details class="faq-item">
                    <summary>${faq.question()}</summary>
                    <p>${faq.answer()}</p>
                </details>
            @endfor
        </div>
    </section>

    @if(model.relatedLinks() != null && !model.relatedLinks().isEmpty())
        <section class="panel">
            <h2 class="font-display">Related decision pages</h2>
            <ul class="link-grid">
                @for(var link : model.relatedLinks())
                    <li>
                        <a href="${link.href()}" data-analytics-event="decision_stage_link_click" data-analytics-params='{"source":"special_page","section":"related_links"}'>${link.label()}</a>
                        <span>${link.summary()}</span>
                    </li>
                @endfor
            </ul>
        </section>
    @endif
</main>
@template.components.appFooter()
@if("dumpster-vs-junk-removal-which-is-cheaper".equals(model.slug()))
    <script src="/js/comparison-hub.js?v=stripeapple4"></script>
@endif
</body>
</html>

