@param com.dumpster.calculator.web.viewmodel.CalculatorPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <meta name="description" content="Estimate dumpster size and weight range in under 60 seconds. Compare overweight risk, multi-haul feasibility, and next best action.">
    <link rel="canonical" href="${model.canonicalUrl()}">
    <meta property="og:title" content="${model.pageTitle()}">
    <meta property="og:description" content="Range-based dumpster sizing with weight risk and heavy debris warnings.">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${model.canonicalUrl()}">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${model.pageTitle()}">
    <meta name="twitter:description" content="Get a safe size recommendation, budget option, and overweight risk signal.">
    <link rel="stylesheet" href="/css/app.css">
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "WebApplication",
          "name": "Dumpster Size and Weight Calculator",
          "applicationCategory": "UtilitiesApplication",
          "operatingSystem": "Web",
          "description": "Range-based estimator for dumpster size, debris weight, and overweight risk.",
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
                "text": "No. Dense debris can exceed weight limits even if volume fits."
              }
            },
            {
              "@type": "Question",
              "name": "Why can heavy debris require a smaller dumpster?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Operational haul limits and safety rules can require small bins with multiple hauls."
              }
            },
            {
              "@type": "Question",
              "name": "What should I ask before booking?",
              "acceptedAnswer": {
                "@type": "Answer",
                "text": "Confirm included tons, overage fee per ton, and clean-load requirements for heavy materials."
              }
            }
          ]
        }
    </script>
</head>
<body>
<header class="site-header">
    <div class="shell shell--header">
        <a class="brand" href="/dumpster/size-weight-calculator" aria-label="Dumpster calculator home">
            <span class="brand-mark" aria-hidden="true">DC</span>
            <span class="brand-copy">
                <strong>Dumpster Decision OS</strong>
                <small>Range-based size + weight intelligence</small>
            </span>
        </a>
        <nav class="site-nav" aria-label="Primary">
            <a href="/dumpster/material-guides">Material guides</a>
            <a href="/dumpster/project-guides">Project guides</a>
            <a href="/dumpster/heavy-debris-rules">Heavy rules</a>
        </nav>
        <div class="site-header-actions">
            <span class="trust-pill">EPA baseline aligned</span>
            <a class="button-link primary" href="#estimate-form">Request quote</a>
        </div>
    </div>
</header>
<main class="shell shell--main">
    <section class="hero">
        <div class="hero-grid">
            <div>
                <p class="eyebrow">Live Decision Cockpit</p>
                <h1>${model.pageTitle()}</h1>
                <p>Use visual controls to test scenarios and get a live risk-aware recommendation before booking.</p>
                <div class="hero-tags">
                    <span class="tag">Live update</span>
                    <span class="tag">Weight + volume</span>
                    <span class="tag">Safe vs budget</span>
                    <span class="tag">Heavy debris guardrails</span>
                </div>
            </div>
            <aside class="hero-proof">
                <h2>Built for trust, not hype</h2>
                <ul>
                    <li>Data-backed density ranges with scenario context</li>
                    <li>Separate signals for pricing risk and haul feasibility</li>
                    <li>Operator checklist before you place a call</li>
                </ul>
            </aside>
        </div>
    </section>

    <section class="panel simulator-shell">
        <div class="simulator-grid">
            <form id="estimate-form" class="control-form">
                <input id="project-id" type="hidden" value="roof_tearoff">
                <input id="persona" type="hidden" value="homeowner">
                <input id="material-id" type="hidden" value="asphalt_shingles">
                <input id="unit-id" type="hidden" value="pickup_load">
                <input id="need-timing" type="hidden" value="research">

                <section class="control-group" data-choice-target="project-id">
                    <div class="control-label-row">
                        <h2>Project</h2>
                        <p>Choose the scenario closest to your job.</p>
                    </div>
                    <div class="chip-grid">
                        <button type="button" class="choice-chip is-active" data-choice-value="roof_tearoff">Roof tear-off</button>
                        <button type="button" class="choice-chip" data-choice-value="kitchen_remodel">Kitchen remodel</button>
                        <button type="button" class="choice-chip" data-choice-value="bathroom_remodel">Bathroom remodel</button>
                        <button type="button" class="choice-chip" data-choice-value="deck_demolition">Deck demo</button>
                        <button type="button" class="choice-chip" data-choice-value="garage_cleanout">Garage cleanout</button>
                        <button type="button" class="choice-chip" data-choice-value="estate_cleanout">Estate cleanout</button>
                        <button type="button" class="choice-chip" data-choice-value="yard_cleanup">Yard cleanup</button>
                        <button type="button" class="choice-chip" data-choice-value="dirt_grading">Dirt grading</button>
                        <button type="button" class="choice-chip" data-choice-value="concrete_removal">Concrete removal</button>
                        <button type="button" class="choice-chip" data-choice-value="light_commercial_fitout">Light commercial fit-out</button>
                    </div>
                </section>

                <section class="control-group" data-choice-target="persona">
                    <div class="control-label-row">
                        <h2>Persona</h2>
                        <p>Context helps prioritize recommendations and CTA routing.</p>
                    </div>
                    <div class="chip-grid chip-grid--compact">
                        <button type="button" class="choice-chip is-active" data-choice-value="homeowner">Homeowner</button>
                        <button type="button" class="choice-chip" data-choice-value="contractor">Contractor</button>
                        <button type="button" class="choice-chip" data-choice-value="property_manager">Property manager</button>
                        <button type="button" class="choice-chip" data-choice-value="investor">Flipper / investor</button>
                        <button type="button" class="choice-chip" data-choice-value="business">Facility / business</button>
                    </div>
                </section>

                <section class="control-group" data-choice-target="material-id">
                    <div class="control-label-row">
                        <h2>Material</h2>
                        <p>Material density drives most pricing and haul risks.</p>
                    </div>
                    <div class="chip-grid">
                        <button type="button" class="choice-chip is-active" data-choice-value="asphalt_shingles">Asphalt shingles</button>
                        <button type="button" class="choice-chip" data-choice-value="concrete">Concrete</button>
                        <button type="button" class="choice-chip" data-choice-value="dirt_soil">Dirt / soil</button>
                        <button type="button" class="choice-chip" data-choice-value="brick">Brick</button>
                        <button type="button" class="choice-chip" data-choice-value="tile_ceramic">Tile / ceramic</button>
                        <button type="button" class="choice-chip" data-choice-value="gravel_rock">Gravel / rock</button>
                        <button type="button" class="choice-chip" data-choice-value="asphalt_pavement">Asphalt pavement</button>
                        <button type="button" class="choice-chip" data-choice-value="plaster">Plaster</button>
                        <button type="button" class="choice-chip" data-choice-value="drywall">Drywall</button>
                        <button type="button" class="choice-chip" data-choice-value="mixed_cd">Mixed C&amp;D</button>
                        <button type="button" class="choice-chip" data-choice-value="household_junk">Household junk</button>
                        <button type="button" class="choice-chip" data-choice-value="furniture">Furniture</button>
                        <button type="button" class="choice-chip" data-choice-value="carpet_pad">Carpet and pad</button>
                        <button type="button" class="choice-chip" data-choice-value="decking_wood">Decking wood</button>
                        <button type="button" class="choice-chip" data-choice-value="insulation_wet">Wet insulation</button>
                        <button type="button" class="choice-chip" data-choice-value="yard_waste">Yard waste</button>
                    </div>
                </section>

                <section class="control-group" data-choice-target="unit-id">
                    <div class="control-label-row">
                        <h2>Unit</h2>
                        <p>Pick the measurement unit for your quantity.</p>
                    </div>
                    <div class="chip-grid chip-grid--compact">
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
                </section>

                <section class="control-group">
                    <div class="control-label-row">
                        <h2>Quantity</h2>
                        <p>Large stepper for fast scenario testing.</p>
                    </div>
                    <div class="quantity-stepper">
                        <button type="button" class="stepper-button" id="quantity-dec" aria-label="Decrease quantity">-</button>
                        <input id="quantity" type="number" step="0.1" min="0.1" value="6" required>
                        <button type="button" class="stepper-button" id="quantity-inc" aria-label="Increase quantity">+</button>
                    </div>
                </section>

                <section class="control-group">
                    <div class="control-label-row">
                        <h2>Allowance</h2>
                        <p>Optional. Included tonnage in your quote.</p>
                    </div>
                    <label for="allowance-tons" class="field-label">Included tons (optional)</label>
                    <input id="allowance-tons" type="number" step="0.1" min="0" placeholder="e.g. 3.0">
                </section>

                <section class="control-group" data-choice-target="need-timing">
                    <div class="control-label-row">
                        <h2>Need timing</h2>
                        <p>Urgency influences routing and CTA strategy.</p>
                    </div>
                    <div class="chip-grid chip-grid--compact">
                        <button type="button" class="choice-chip is-active" data-choice-value="research">Researching</button>
                        <button type="button" class="choice-chip" data-choice-value="this_week">This week</button>
                        <button type="button" class="choice-chip" data-choice-value="48h">48 hours</button>
                    </div>
                </section>

                <section class="control-group advanced-group">
                    <details>
                        <summary>Advanced modifiers</summary>
                        <div class="advanced-grid">
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
                    </details>
                </section>

                <div class="control-actions">
                    <button id="submit-button" type="submit">Calculate</button>
                    <p id="live-note" aria-live="polite">Live update is on. Input changes refresh your recommendation automatically.</p>
                </div>
            </form>

            <aside class="live-dashboard" id="live-dashboard">
                <div class="dashboard-head">
                    <p class="eyebrow">Live Dashboard</p>
                    <h2>Risk and recommendation stream</h2>
                    <p id="live-status">Change any input to see the estimate update.</p>
                </div>
                <div class="gauge-list">
                    <article class="gauge-card">
                        <h3>Weight pressure</h3>
                        <div class="gauge-track"><span id="gauge-weight" class="gauge-fill"></span></div>
                        <p id="gauge-weight-label">Waiting for estimate</p>
                    </article>
                    <article class="gauge-card">
                        <h3>Volume load</h3>
                        <div class="gauge-track"><span id="gauge-volume" class="gauge-fill"></span></div>
                        <p id="gauge-volume-label">Waiting for estimate</p>
                    </article>
                    <article class="gauge-card">
                        <h3>Overage risk</h3>
                        <div class="gauge-track"><span id="gauge-risk" class="gauge-fill"></span></div>
                        <p id="gauge-risk-label">Waiting for estimate</p>
                    </article>
                </div>

                <section class="result-panel" id="result-panel" hidden>
                    <div class="result-header">
                        <h2>Result</h2>
                        <a id="share-link" href="#" rel="nofollow">Open share page</a>
                    </div>
                    <div id="result-badges" class="badges"></div>
                    <div id="result-summary" class="summary-grid"></div>
                    <div id="result-recommendations" class="cards"></div>
                    <div id="result-costs" class="cards"></div>
                    <div id="result-assumptions" class="notes"></div>
                    <div id="result-actions" class="cta"></div>
                </section>
            </aside>
        </div>
    </section>

    <section class="panel eeat-panel">
        <h2>How estimate quality is maintained</h2>
        <div class="eeat-grid">
            <article class="eeat-card">
                <h3>Experience</h3>
                <p>Operator-first questions are included so you can validate assumptions before booking.</p>
            </article>
            <article class="eeat-card">
                <h3>Expertise</h3>
                <p>Calculations convert your scenario into volume and weight ranges with allowance-aware risk logic.</p>
            </article>
            <article class="eeat-card">
                <h3>Authority</h3>
                <p>Reference baselines are aligned with EPA conversion data and local table cross-checks.</p>
            </article>
            <article class="eeat-card">
                <h3>Trust</h3>
                <p>Output clearly separates planning guidance from final vendor quote and contract terms.</p>
            </article>
        </div>
    </section>

    <section class="panel knowledge-panel">
        <div class="link-grid">
            <article class="link-card">
                <h2>Popular material guides</h2>
                <ul>
                    @for(var link : model.featuredMaterialLinks())
                        <li><a href="${link.href()}">${link.label()}</a></li>
                    @endfor
                </ul>
                <p><a href="/dumpster/material-guides">See all material guides</a></p>
            </article>
            <article class="link-card">
                <h2>Project quick starts</h2>
                <ul>
                    @for(var link : model.featuredProjectLinks())
                        <li><a href="${link.href()}">${link.label()}</a></li>
                    @endfor
                    <li><a href="/dumpster/heavy-debris-rules">Heavy debris rules</a></li>
                </ul>
                <p><a href="/dumpster/project-guides">See all project guides</a></p>
            </article>
        </div>
    </section>

    <section class="panel">
        <h2>Vendor Checklist</h2>
        <ol>
            <li>What included tons are in this quote?</li>
            <li>What is overage per ton?</li>
            <li>For heavy debris, is clean load required?</li>
        </ol>
    </section>
</main>
<footer class="site-footer">
    <div class="shell shell--footer">
        <p>Planning estimate only. Final pricing and haul constraints are confirmed by your local vendor contract.</p>
        <div class="micro-links">
            <span>Data sources: EPA baseline conversion references + local table checks</span>
            <a href="/dumpster/heavy-debris-rules">Methodology</a>
            <a href="/legal/terms.html">Terms</a>
            <a href="/legal/privacy.html">Privacy</a>
        </div>
    </div>
</footer>
<div id="floating-cta" class="floating-cta" hidden>
    <a id="floating-call" href="tel:+18005550123">Call now</a>
    <a id="floating-quote" href="#estimate-form">Online quote</a>
</div>
<script src="/js/calculator.js"></script>
</body>
</html>
