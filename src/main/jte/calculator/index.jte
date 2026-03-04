@param com.dumpster.calculator.web.viewmodel.CalculatorPageViewModel model
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
    <link rel="stylesheet" href="/css/tailwind.generated.css">
    <link rel="stylesheet" href="/css/app.css?v=stripeapple4">
    <link rel="stylesheet" href="/css/calculator-modern.css?v=stripeapple4">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "WebApplication",
          "name": "Debris Disposal Decision Engine",
          "applicationCategory": "UtilitiesApplication",
          "operatingSystem": "Web",
          "description": "Homeowner-first decision tool for choosing between dumpster rental and junk removal while reducing overage risk.",
          "url": "${model.canonicalUrl()}"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "FAQPage",
          "mainEntity": [
            {
              "@type": "Question",
              "name": "Should I pick dumpster size by volume only?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "No. Volume-only planning is the fastest way to trigger overweight surprises. Dense materials such as concrete, shingles, and soil can exceed included tonnage while the container still looks partially full. This calculator evaluates both cubic volume and low/typ/high tonnage before recommending size."
              }
            },
            {
              "@type": "Question",
              "name": "Why can heavy debris require a smaller dumpster?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Heavy debris is usually limited by transport constraints rather than visual capacity. Many markets enforce haul caps, heavy fill-ratio limits, or clean-load requirements that make a smaller bin with planned multi-haul safer than one large overloaded container."
              }
            },
            {
              "@type": "Question",
              "name": "What should I ask before booking?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Ask for included tons, overage fee per ton, max haul limit, clean-load separation rules, and same-day swap availability. Those details determine whether your estimate can be executed without pickup rejection, emergency swaps, or avoidable overage costs."
              }
            }
          ]
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "WebSite",
          "name": "Debris Decision",
          "url": "${model.siteBaseUrl()}"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Organization",
          "name": "Debris Decision",
          "url": "${model.siteBaseUrl()}",
          "logo": "${model.siteBaseUrl()}/favicon.png"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${model.canonicalUrl()}"}
          ]
        }
    </script>
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Homeowner-first guidance", primaryHref = "#estimate-form", primaryLabel = "Run live estimate")

<main class="app-container app-main calc-page">
    <section class="calc-hero">
        <div class="calc-hero__head">
            <p class="eyebrow">Find your best disposal option</p>
            <h1 class="font-display">${model.pageTitle()}</h1>
            <p>Choose the safer route in about 60 seconds: dumpster, junk removal, or staged multi-haul.</p>
            <p>Answer three homeowner-first questions: which route wins, how to avoid overage, and what size is safe on the first try.</p>
        </div>
        <nav class="decision-strip" aria-label="Quick decision routes">
            <a class="decision-pill" data-decision-mode="cheapest_route" href="/dumpster/size-weight-calculator?project=garage_cleanout&material=household_junk&unit=pickup_load&qty=4&timing=research&priority=cost">I want the cheapest route</a>
            <a class="decision-pill" data-decision-mode="easiest_option" href="/dumpster/size-weight-calculator?project=moving_cleanout&material=household_junk&unit=pickup_load&qty=6&timing=this_week&priority=labor">I want the easiest route</a>
            <a class="decision-pill" data-decision-mode="heavy_material" href="/dumpster/size-weight-calculator?project=concrete_removal&material=concrete&unit=sqft_4in&qty=180&timing=research&priority=heavy">I have heavy material</a>
            <a class="decision-pill" data-decision-mode="need_fast" href="/dumpster/size-weight-calculator?project=general_junk_removal&material=household_junk&unit=pickup_load&qty=4&timing=48h&priority=speed">I need it gone fast</a>
        </nav>
        <div class="calc-benefits">
            <article class="calc-benefit">
                <strong>Decision first</strong>
                <p>See your best next move before shopping quotes.</p>
            </article>
            <article class="calc-benefit">
                <strong>Risk visible</strong>
                <p>Weight and allowance risk shown as ranges, not guesswork.</p>
            </article>
            <article class="calc-benefit">
                <strong>Actionable output</strong>
                <p>Dumpsters, junk removal, and heavy-load fallback in one board.</p>
            </article>
        </div>
    </section>

    <section class="calc-layout">
        <form id="estimate-form" class="calc-workflow">
            <input id="project-id" type="hidden" value="roof_tearoff">
            <input id="persona" type="hidden" value="homeowner">
            <input id="material-id" type="hidden" value="asphalt_shingles">
            <input id="unit-id" type="hidden" value="pickup_load">
            <input id="need-timing" type="hidden" value="research">
            <input id="decision-priority" type="hidden" value="balanced">

            <div class="calc-stepper">
                <section class="calc-step" data-choice-target="project-id">
                    <div class="calc-step__head">
                        <div>
                            <h2 class="calc-step__title">Project scope</h2>
                            <p class="calc-step__hint">Choose the closest job type.</p>
                        </div>
                    </div>
                    <div class="choice-grid">
                        <button type="button" class="choice-chip is-active" data-choice-value="roof_tearoff">Roof tear-off</button>
                        <button type="button" class="choice-chip" data-choice-value="kitchen_remodel">Kitchen remodel</button>
                        <button type="button" class="choice-chip" data-choice-value="bathroom_remodel">Bathroom remodel</button>
                        <button type="button" class="choice-chip" data-choice-value="deck_demolition">Deck demo</button>
                        <button type="button" class="choice-chip" data-choice-value="garage_cleanout">Garage cleanout</button>
                        <button type="button" class="choice-chip" data-choice-value="estate_cleanout">Estate cleanout</button>
                        <button type="button" class="choice-chip" data-choice-value="moving_cleanout">Moving / apartment cleanout</button>
                        <button type="button" class="choice-chip" data-choice-value="furniture_appliance_disposal">Furniture or appliance disposal</button>
                        <button type="button" class="choice-chip" data-choice-value="general_junk_removal">General junk removal</button>
                        <button type="button" class="choice-chip" data-choice-value="yard_cleanup">Yard cleanup</button>
                        <button type="button" class="choice-chip" data-choice-value="dirt_grading">Dirt grading</button>
                        <button type="button" class="choice-chip" data-choice-value="concrete_removal">Concrete removal</button>
                        <button type="button" class="choice-chip" data-choice-value="light_commercial_fitout">Light commercial fit-out</button>
                    </div>
                </section>

                <section class="calc-step" data-choice-target="material-id">
                    <div class="calc-step__head">
                        <div>
                            <h2 class="calc-step__title">Material profile</h2>
                            <p class="calc-step__hint">Weight behavior changes the recommendation fastest.</p>
                        </div>
                    </div>
                    <div class="choice-grid">
                        <button type="button" class="choice-chip is-active" data-choice-value="asphalt_shingles">Asphalt shingles</button>
                        <button type="button" class="choice-chip" data-choice-value="concrete">Concrete</button>
                        <button type="button" class="choice-chip" data-choice-value="dirt_soil">Dirt / soil</button>
                        <button type="button" class="choice-chip" data-choice-value="brick">Brick</button>
                        <button type="button" class="choice-chip" data-choice-value="tile_ceramic">Tile / ceramic</button>
                        <button type="button" class="choice-chip" data-choice-value="gravel_rock">Gravel / rock</button>
                        <button type="button" class="choice-chip" data-choice-value="asphalt_pavement">Asphalt pavement</button>
                        <button type="button" class="choice-chip" data-choice-value="plaster">Plaster</button>
                        <button type="button" class="choice-chip" data-choice-value="drywall">Drywall</button>
                        <button type="button" class="choice-chip" data-choice-value="mixed_cd">Mixed construction debris (C&amp;D)</button>
                        <button type="button" class="choice-chip" data-choice-value="household_junk">Household junk</button>
                        <button type="button" class="choice-chip" data-choice-value="furniture">Furniture</button>
                        <button type="button" class="choice-chip" data-choice-value="carpet_pad">Carpet and pad</button>
                        <button type="button" class="choice-chip" data-choice-value="decking_wood">Decking wood</button>
                        <button type="button" class="choice-chip" data-choice-value="insulation_wet">Wet insulation</button>
                        <button type="button" class="choice-chip" data-choice-value="yard_waste">Yard waste</button>
                    </div>
                </section>

                <section class="calc-step">
                    <div class="calc-step__head">
                        <div>
                            <h2 class="calc-step__title">Quantity and unit</h2>
                            <p class="calc-step__hint">Pick the unit you can estimate confidently.</p>
                        </div>
                    </div>
                    <div class="choice-grid choice-grid--compact" data-choice-target="unit-id">
                        <button type="button" class="choice-chip is-active" data-choice-value="pickup_load">Pickup load</button>
                        <button type="button" class="choice-chip" data-choice-value="truckload_small">Small truckload</button>
                        <button type="button" class="choice-chip" data-choice-value="roof_square" data-shingle-only="true">Roof square</button>
                        <button type="button" class="choice-chip" data-choice-value="sqft_4in">sqft @ 4in</button>
                        <button type="button" class="choice-chip" data-choice-value="sqft_2in">sqft @ 2in</button>
                        <button type="button" class="choice-chip" data-choice-value="sqft_1in">sqft @ 1in</button>
                        <button type="button" class="choice-chip" data-choice-value="contractor_bag">Contractor bag</button>
                        <button type="button" class="choice-chip" data-choice-value="drywall_sheet">Drywall sheet</button>
                        <button type="button" class="choice-chip" data-choice-value="cubic_yard">Cubic yard</button>
                    </div>
                    <div class="calc-quantity">
                        <label class="ui-field__label" for="quantity">Quantity</label>
                        <p id="unit-helper-text" class="calc-step__hint" style="margin-top: -0.25rem; margin-bottom: 0.5rem;">Standard 8ft pickup truck bed, level full (~2.5 yd3).</p>
                        <div class="calc-quantity-stepper">
                            <button type="button" id="quantity-dec" class="calc-stepper-btn" aria-label="Decrease quantity">-</button>
                            <input id="quantity" type="number" step="0.1" min="0.1" value="6" required>
                            <button type="button" id="quantity-inc" class="calc-stepper-btn" aria-label="Increase quantity">+</button>
                        </div>
                    </div>
                </section>

                <section class="calc-step">
                    <div class="calc-step__head">
                        <div>
                            <h2 class="calc-step__title">Context and modifiers</h2>
                            <p class="calc-step__hint">Keep defaults for quick mode. Open only if needed.</p>
                        </div>
                    </div>
                    <details class="ui-accordion">
                        <summary>
                            <span>Persona and timing</span>
                            <small>Choose your role</small>
                        </summary>
                        <div class="ui-accordion__body">
                            <div class="choice-grid choice-grid--compact" data-choice-target="persona">
                                <button type="button" class="choice-chip is-active" data-choice-value="homeowner">Homeowner</button>
                                <button type="button" class="choice-chip" data-choice-value="contractor">Contractor</button>
                                <button type="button" class="choice-chip" data-choice-value="property_manager">Property manager</button>
                                <button type="button" class="choice-chip" data-choice-value="investor">Flipper / investor</button>
                                <button type="button" class="choice-chip" data-choice-value="business">Facility / business</button>
                            </div>
                            <div class="choice-grid choice-grid--compact" data-choice-target="need-timing">
                                <button type="button" class="choice-chip is-active" data-choice-value="research">Researching</button>
                                <button type="button" class="choice-chip" data-choice-value="this_week">This week</button>
                                <button type="button" class="choice-chip" data-choice-value="48h">48 hours</button>
                            </div>
                            <div class="ui-field">
                                <label class="ui-field__label" for="market-zip">ZIP code (optional)</label>
                                <input id="market-zip" type="text" inputmode="numeric" maxlength="5" placeholder="e.g. 30339">
                                <p class="calc-step__hint">Enter your ZIP for local pricing.</p>
                            </div>
                        </div>
                    </details>

                    <details class="ui-accordion">
                        <summary>
                            <span>Advanced modifiers</span>
                            <small>Optional load adjustments</small>
                        </summary>
                        <div class="ui-accordion__body">
                            <div class="rule-grid">
                                <label class="switch-field">
                                    <input id="wet" type="checkbox">
                                    <span class="switch-ui" aria-hidden="true"></span>
                                    <span>Wet load</span>
                                </label>
                                <label class="switch-field">
                                    <input id="mixed-load" type="checkbox">
                                    <span class="switch-ui" aria-hidden="true"></span>
                                    <span>Mixed load</span>
                                </label>
                            </div>
                        </div>
                    </details>

                    <div class="ui-field">
                        <label class="ui-field__label" for="allowance-tons">Included tons (optional)</label>
                        <input id="allowance-tons" type="number" step="0.1" min="0" placeholder="e.g. 3.0">
                    </div>
                </section>
            </div>

            <div class="calc-actions">
                <button id="submit-button" type="submit" class="calc-submit">Calculate</button>
                <p id="live-note" aria-live="polite">Live update is on. Any change refreshes the recommendation.</p>
            </div>
        </form>

        <aside class="result-rail" id="live-dashboard">
            <div class="result-rail__header">
                <p class="eyebrow">Result summary</p>
                <h2 class="font-display">Decision board</h2>
                <p id="live-status" class="result-status">Change any input to refresh your estimate.</p>
            </div>

            <div class="gauge-dashboard">
                <article class="radial-card">
                    <h3>Volume</h3>
                    <div class="radial-wrap">
                        <svg class="radial-svg" viewBox="0 0 100 100">
                            <circle class="radial-bg" cx="50" cy="50" r="40" />
                            <circle id="gauge-volume" class="radial-fill" cx="50" cy="50" r="40" stroke-dasharray="251.2" stroke-dashoffset="251.2" />
                        </svg>
                        <div class="radial-label-center" id="gauge-volume-label">...</div>
                    </div>
                </article>
                <article class="radial-card">
                    <h3>Weight</h3>
                    <div class="radial-wrap">
                        <svg class="radial-svg" viewBox="0 0 100 100">
                            <circle class="radial-bg" cx="50" cy="50" r="40" />
                            <circle id="gauge-weight" class="radial-fill" cx="50" cy="50" r="40" stroke-dasharray="251.2" stroke-dashoffset="251.2" />
                        </svg>
                        <div class="radial-label-center" id="gauge-weight-label">...</div>
                    </div>
                </article>
                <article class="radial-card">
                    <h3>Risk</h3>
                    <div class="radial-wrap">
                        <svg class="radial-svg" viewBox="0 0 100 100">
                            <circle class="radial-bg" cx="50" cy="50" r="40" />
                            <circle id="gauge-risk" class="radial-fill" cx="50" cy="50" r="40" stroke-dasharray="251.2" stroke-dashoffset="251.2" />
                        </svg>
                        <div class="radial-label-center" id="gauge-risk-label">...</div>
                    </div>
                </article>
            </div>

            <section class="result-panel" id="result-panel" hidden>
                <div class="result-header">
                    <h3 class="font-display">Decision summary</h3>
                    <a id="share-link" href="#" rel="nofollow">Open share page</a>
                </div>
                <div id="result-state-banner" class="result-state-banner" hidden></div>
                <div id="result-hard-stops" class="result-hard-stops" hidden></div>
                <p id="result-verdict" class="result-verdict">Run calculation to get your verdict.</p>
                <div id="result-badges" class="badges"></div>
                <div id="result-input-impact" class="input-impact-list"></div>
                <div id="result-summary" class="summary-grid"></div>
                <div id="result-recommendations" class="cards"></div>
                <div id="result-costs" class="cards"></div>
                <details id="trust-drawer" class="trust-drawer">
                    <summary>Assumptions, sources, and versions</summary>
                    <div class="trust-drawer__body">
                        <div id="result-assumptions" class="notes"></div>
                    </div>
                </details>
                <div id="result-actions" class="cta"></div>
            </section>
        </aside>
    </section>

    <section class="calc-support-grid">
        <article class="calc-support-card">
            <h2 class="font-display">Vendor checklist</h2>
            <ol>
                <li>What included tons are in this quote?</li>
                <li>What is overage per ton above included allowance?</li>
                <li>For heavy debris, is clean-load and fill-ratio enforcement required?</li>
                <li>If timing is urgent, what swap window is guaranteed?</li>
            </ol>
        </article>

        <article class="calc-support-card">
            <h2 class="font-display">Frequently asked questions</h2>
            <div class="faq-list">
                <details class="faq-item">
                    <summary>Should I pick dumpster size by volume only?</summary>
                    <p>No. Volume-only planning can fail when dense debris pushes tonnage beyond included allowance even if space appears available. Treat weight and volume as paired constraints.</p>
                </details>
                <details class="faq-item">
                    <summary>Why can heavy debris require a smaller dumpster?</summary>
                    <p>Large containers are not always safer for heavy materials because transport rules can cap pickup weight and fill ratio. Smaller bins with planned multi-haul are often safer.</p>
                </details>
                <details class="faq-item">
                    <summary>What should I ask before booking?</summary>
                    <p>Confirm included tons, overage fee per ton, max haul limits, clean-load rules, and swap timing before booking.</p>
                </details>
            </div>
        </article>

        <article class="calc-support-card">
            <h2 class="font-display">Preset guides</h2>
            <div>
                <h3>Popular material guides</h3>
                <ul>
                    @for(var link : model.featuredMaterialLinks())
                        <li><a href="${link.href()}">${link.label()}</a></li>
                    @endfor
                </ul>
                <p><a href="/dumpster/material-guides">See all material guides</a></p>
            </div>
            <div>
                <h3>Project quick starts</h3>
                <ul>
                    @for(var link : model.featuredProjectLinks())
                        <li><a href="${link.href()}">${link.label()}</a></li>
                    @endfor
                    <li><a href="/dumpster/heavy-debris-rules">Heavy debris rules</a></li>
                </ul>
                <p><a href="/dumpster/project-guides">See all project guides</a></p>
            </div>
        </article>
    </section>
</main>

@template.components.appFooter()

<div id="mobile-result-dock" class="mobile-result-dock" hidden>
    <div class="mobile-result-dock__row">
        <p id="mobile-result-verdict">Run calculation to get your recommendation.</p>
        <a id="mobile-primary-cta" class="ui-button ui-button--primary" href="#estimate-form">View result</a>
    </div>
</div>

<div id="floating-cta" class="floating-cta" hidden>
    <a id="floating-call" href="/about/contact">Get dumpster quotes</a>
    <a id="floating-quote" href="#estimate-form">Run live estimate</a>
</div>

<script src="/js/calculator.js?v=stripeapple4"></script>
</body>
</html>


