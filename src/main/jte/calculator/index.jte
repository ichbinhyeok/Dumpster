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
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Outfit:wght@500;600;700;800&display=swap">
    <link rel="stylesheet" href="/css/tailwind.generated.css">
    <link rel="stylesheet" href="/css/app.css">
    <link rel="stylesheet" href="/css/calculator-modern.css">
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
          "name": "Dumpster Decision OS",
          "url": "${model.siteBaseUrl()}"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Organization",
          "name": "Dumpster Decision OS",
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
</head>
<body class="neo-body neo-content">
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
            <span class="trust-pill">Live decision engine</span>
            <a class="button-link primary" href="#estimate-form">Request quote</a>
        </div>
    </div>
</header>

<main class="neo-app shell shell--main">
    <section class="neo-hero">
        <article class="neo-hero-main">
            <p class="neo-eyebrow">Planning Cockpit</p>
            <h1 class="font-display">${model.pageTitle()}</h1>
            <p class="answer-first"><strong>Direct answer:</strong> set your scenario once and this cockpit streams safe size, budget option, weight pressure, and haul feasibility in real time.</p>
            <p>Built for homeowners and contractors who need a confident booking decision before calling vendors.</p>
            <div class="neo-pill-group">
                <span class="neo-pill">Live recommendations</span>
                <span class="neo-pill">Risk-first sizing</span>
                <span class="neo-pill">Overage visibility</span>
                <span class="neo-pill">Feasibility checks</span>
            </div>
            <div class="neo-kpi-grid">
                <article class="neo-kpi-card">
                    <span>Primary plan</span>
                    <strong id="hero-kpi-plan">Waiting</strong>
                </article>
                <article class="neo-kpi-card">
                    <span>Price risk</span>
                    <strong id="hero-kpi-risk">Waiting</strong>
                </article>
                <article class="neo-kpi-card">
                    <span>Feasibility</span>
                    <strong id="hero-kpi-feasibility">Waiting</strong>
                </article>
                <article class="neo-kpi-card">
                    <span>Cost window</span>
                    <strong id="hero-kpi-cost">Waiting</strong>
                </article>
            </div>
        </article>

        <aside class="neo-hero-side">
            <h2 class="font-display">What makes this different</h2>
            <ul>
                <li>Separates price allowance and pickup feasibility instead of mixing both into one vague score.</li>
                <li>Surfaces low/typ/high ranges so uncertainty is visible before you book.</li>
                <li>Converts dense-material scenarios into safer multi-haul strategy when needed.</li>
            </ul>
            <div class="mini-metrics">
                <article class="mini-metric">
                    <span>Mode</span>
                    <strong>Real-time simulation</strong>
                </article>
                <article class="mini-metric">
                    <span>Last updated</span>
                    <strong>${model.lastUpdatedIso()}</strong>
                </article>
                <article class="mini-metric">
                    <span>Coverage</span>
                    <strong>Material + project presets</strong>
                </article>
            </div>
        </aside>
    </section>

    <section class="neo-cockpit">
        <div class="neo-cockpit-head">
            <p class="neo-eyebrow">Scenario Builder</p>
            <h2 class="font-display">Configure inputs and watch the decision board react</h2>
            <p>No submit-wait cycle. Every meaningful input change updates strategy, risk, and route instantly.</p>
        </div>

        <div class="neo-cockpit-grid">
            <form id="estimate-form" class="neo-form-surface">
                <input id="project-id" type="hidden" value="roof_tearoff">
                <input id="persona" type="hidden" value="homeowner">
                <input id="material-id" type="hidden" value="asphalt_shingles">
                <input id="unit-id" type="hidden" value="pickup_load">
                <input id="need-timing" type="hidden" value="research">

                <section class="neo-form-group" data-choice-target="project-id">
                    <div class="neo-group-head">
                        <h3>Project type</h3>
                        <p>Choose the closest real-world scope.</p>
                    </div>
                    <div class="neo-chip-grid">
                        <button type="button" class="choice-chip neo-chip is-active" data-choice-value="roof_tearoff">Roof tear-off</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="kitchen_remodel">Kitchen remodel</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="bathroom_remodel">Bathroom remodel</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="deck_demolition">Deck demo</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="garage_cleanout">Garage cleanout</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="estate_cleanout">Estate cleanout</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="yard_cleanup">Yard cleanup</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="dirt_grading">Dirt grading</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="concrete_removal">Concrete removal</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="light_commercial_fitout">Light commercial fit-out</button>
                    </div>
                </section>

                <section class="neo-form-group" data-choice-target="material-id">
                    <div class="neo-group-head">
                        <h3>Material profile</h3>
                        <p>Material density usually drives the largest decision delta.</p>
                    </div>
                    <div class="neo-chip-grid">
                        <button type="button" class="choice-chip neo-chip is-active" data-choice-value="asphalt_shingles">Asphalt shingles</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="concrete">Concrete</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="dirt_soil">Dirt / soil</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="brick">Brick</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="tile_ceramic">Tile / ceramic</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="gravel_rock">Gravel / rock</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="asphalt_pavement">Asphalt pavement</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="plaster">Plaster</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="drywall">Drywall</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="mixed_cd">Mixed C&amp;D</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="household_junk">Household junk</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="furniture">Furniture</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="carpet_pad">Carpet and pad</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="decking_wood">Decking wood</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="insulation_wet">Wet insulation</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="yard_waste">Yard waste</button>
                    </div>
                </section>

                <section class="neo-form-group" data-choice-target="unit-id">
                    <div class="neo-group-head">
                        <h3>Input unit</h3>
                        <p>Use the unit you can estimate confidently.</p>
                    </div>
                    <div class="neo-chip-grid neo-chip-grid--compact">
                        <button type="button" class="choice-chip neo-chip is-active" data-choice-value="pickup_load">Pickup load</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="truckload_small">Small truckload</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="roof_square" data-shingle-only="true">Roof square</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="sqft_4in">sqft @ 4in</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="sqft_2in">sqft @ 2in</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="sqft_1in">sqft @ 1in</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="contractor_bag">Contractor bag</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="drywall_sheet">Drywall sheet</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="cubic_yard">Cubic yard</button>
                    </div>
                </section>

                <section class="neo-form-group neo-quantity-group">
                    <div class="neo-group-head">
                        <h3>Quantity</h3>
                        <p>Step rapidly through scenarios.</p>
                    </div>
                    <label for="quantity" class="neo-label">Quantity amount</label>
                    <div class="neo-quantity-stepper">
                        <button type="button" id="quantity-dec" class="neo-stepper" aria-label="Decrease quantity">-</button>
                        <input id="quantity" type="number" step="0.1" min="0.1" value="6" required>
                        <button type="button" id="quantity-inc" class="neo-stepper" aria-label="Increase quantity">+</button>
                    </div>
                </section>

                <details class="neo-form-group neo-fold" data-choice-target="persona">
                    <summary>
                        <span>Persona context</span>
                        <small>Optional routing signal</small>
                    </summary>
                    <div class="neo-chip-grid neo-chip-grid--compact">
                        <button type="button" class="choice-chip neo-chip is-active" data-choice-value="homeowner">Homeowner</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="contractor">Contractor</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="property_manager">Property manager</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="investor">Flipper / investor</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="business">Facility / business</button>
                    </div>
                </details>

                <details class="neo-form-group neo-fold" data-choice-target="need-timing">
                    <summary>
                        <span>Need timing</span>
                        <small>Optional urgency signal</small>
                    </summary>
                    <div class="neo-chip-grid neo-chip-grid--compact">
                        <button type="button" class="choice-chip neo-chip is-active" data-choice-value="research">Researching</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="this_week">This week</button>
                        <button type="button" class="choice-chip neo-chip" data-choice-value="48h">48 hours</button>
                    </div>
                </details>

                <details class="neo-form-group neo-fold advanced-group">
                    <summary>
                        <span>Advanced modifiers</span>
                        <small>Optional load adjustments</small>
                    </summary>
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
                </details>

                <section class="neo-form-group">
                    <div class="neo-group-head">
                        <h3>Allowance</h3>
                        <p>Optional included tonnage from your quote.</p>
                    </div>
                    <label for="allowance-tons" class="neo-label">Included tons</label>
                    <input id="allowance-tons" type="number" step="0.1" min="0" placeholder="e.g. 3.0">
                </section>

                <div class="neo-actions">
                    <button id="submit-button" type="submit" class="neo-primary-btn">Run decision engine</button>
                    <p id="live-note" aria-live="polite">Live update is on. Any change refreshes the recommendation automatically.</p>
                </div>
            </form>

            <aside class="neo-dashboard" id="live-dashboard">
                <div>
                    <p class="neo-eyebrow">Decision Board</p>
                    <h2 class="font-display">Risk + feasibility stream</h2>
                    <p id="live-status">Change any input to see the estimate update.</p>
                </div>

                <div class="neo-gauges">
                    <article class="neo-gauge-card">
                        <h3>Weight pressure</h3>
                        <div class="gauge-track"><span id="gauge-weight" class="gauge-fill"></span></div>
                        <p id="gauge-weight-label">Waiting for estimate</p>
                    </article>
                    <article class="neo-gauge-card">
                        <h3>Volume load</h3>
                        <div class="gauge-track"><span id="gauge-volume" class="gauge-fill"></span></div>
                        <p id="gauge-volume-label">Waiting for estimate</p>
                    </article>
                    <article class="neo-gauge-card">
                        <h3>Overage risk</h3>
                        <div class="gauge-track"><span id="gauge-risk" class="gauge-fill"></span></div>
                        <p id="gauge-risk-label">Waiting for estimate</p>
                    </article>
                </div>

                <section class="result-panel" id="result-panel" hidden>
                    <div class="result-header">
                        <h3 class="font-display">Decision output</h3>
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

    <section class="neo-support-grid">
        <article class="neo-support-card">
            <h2 class="font-display">Vendor checklist</h2>
            <ol>
                <li>What included tons are in this quote?</li>
                <li>What is overage per ton above included allowance?</li>
                <li>For heavy debris, is clean-load and fill-ratio enforcement required?</li>
                <li>If timing is urgent, what swap window is guaranteed?</li>
            </ol>
        </article>

        <article class="neo-support-card">
            <h2 class="font-display">Frequently asked questions</h2>
            <div class="faq-list">
                <details class="faq-item">
                    <summary>Should I pick dumpster size by volume only?</summary>
                    <p>No. Volume-only planning can fail when dense debris pushes tonnage beyond included allowance even if space appears available. Treat weight and volume as paired constraints, then choose a size that keeps high-side tonnage below both pricing and haul-limit thresholds.</p>
                </details>
                <details class="faq-item">
                    <summary>Why can heavy debris require a smaller dumpster?</summary>
                    <p>Large containers are not always safer for heavy materials because transport rules can cap pickup weight and fill ratio. In those cases, operators often prefer smaller containers with planned multi-haul because it lowers rejection risk and keeps loading within operational limits.</p>
                </details>
                <details class="faq-item">
                    <summary>What should I ask before booking?</summary>
                    <p>Confirm included tons, overage fee per ton, max haul limits, clean-load requirements, and swap timing. Those five checks determine whether your selected strategy can actually be executed at pickup time without avoidable extra charges.</p>
                </details>
            </div>
        </article>

        <article class="neo-support-card">
            <h2 class="font-display">Knowledge links</h2>
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

<footer class="site-footer">
    <div class="shell shell--footer">
        <p>Planning estimate only. Final pricing and haul constraints are confirmed by your local vendor contract.</p>
        <div class="micro-links">
            <span>Data sources: EPA baseline conversion references + local table checks</span>
            <a href="/about/methodology">Methodology</a>
            <a href="/about/editorial-policy">Editorial policy</a>
            <a href="/about/contact">Contact</a>
            <a href="/legal/terms.html">Terms</a>
            <a href="/legal/privacy.html">Privacy</a>
        </div>
    </div>
</footer>

<div id="floating-cta" class="floating-cta" hidden>
    <a id="floating-call" href="/about/contact">Contact now</a>
    <a id="floating-quote" href="#estimate-form">Online quote</a>
</div>

<script src="/js/calculator.js"></script>
</body>
</html>
