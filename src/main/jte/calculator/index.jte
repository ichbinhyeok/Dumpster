@import com.dumpster.calculator.web.support.JsonLd
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
          "description": "Heavy-debris decision tool for choosing dumpster size and route for concrete, dirt, shingles, and other weight-limited loads.",
          "url": "${JsonLd.content(String.valueOf(model.canonicalUrl()))}"
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
          "url": "${JsonLd.content(String.valueOf(model.siteBaseUrl()))}"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "Organization",
          "name": "Debris Decision",
          "url": "${JsonLd.content(String.valueOf(model.siteBaseUrl()))}",
          "logo": "${JsonLd.content(String.valueOf(model.siteBaseUrl()))}/favicon.png"
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "WebPage",
          "name": "${JsonLd.content(String.valueOf(model.pageTitle()))}",
          "url": "${JsonLd.content(String.valueOf(model.canonicalUrl()))}",
          "dateModified": "${JsonLd.content(String.valueOf(model.lastUpdatedIso()))}",
          "reviewedBy": {
            "@type": "Organization",
            "name": "Debris Decision Editorial Team"
          }
        }
    </script>
    <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [
            {"@type": "ListItem", "position": 1, "name": "Calculator", "item": "${JsonLd.content(String.valueOf(model.canonicalUrl()))}"}
          ]
        }
    </script>
    @template.components.analyticsTag()
</head>
<body class="neo-body neo-content">
@template.components.appHeader(trustLabel = "Heavy-debris decision engine", primaryHref = "#estimate-form", primaryLabel = "Run heavy-debris estimate")

<main class="app-container app-main calc-page">
    <section class="calc-hero">
        <div class="calc-hero__head">
            <p class="eyebrow">Answer-led heavy debris sizing</p>
            <h1 class="font-display">${model.pageTitle()}</h1>
        <p>Use the calculator as a conversion layer after the right debris question is framed: shingles, garage loads, remodel plaster, concrete, deck teardown, or other high-risk material mixes.</p>
            <p>This is not a generic cleanup calculator. It is designed for decision-stage debris questions where haul limits, fill rules, and awkward load shapes break naive sizing.</p>
            <p class="meta-note">Reviewed by Debris Decision editorial team · Updated ${model.lastUpdatedIso()}</p>
            <div class="scenario-chip-group">
                @if("High".equals(model.confidenceTier()))
                    <span class="ui-badge ui-badge--ok">Evidence confidence: ${model.confidenceTier()}</span>
                @elseif("Medium".equals(model.confidenceTier()))
                    <span class="ui-badge ui-badge--warn">Evidence confidence: ${model.confidenceTier()}</span>
                @else
                    <span class="ui-badge ui-badge--danger">Evidence confidence: ${model.confidenceTier()}</span>
                @endif
                <span class="scenario-chip">Range-based output</span>
                <span class="scenario-chip">Operator rules vary</span>
            </div>
            <p class="meta-note"><strong>Variance note:</strong> ${model.varianceNote()}</p>
        </div>
        <nav class="decision-strip" aria-label="Quick decision routes">
            <a class="decision-pill" data-decision-mode="heavy_material" href="/dumpster/size-weight-calculator?project=concrete_removal&material=concrete&unit=sqft_4in&qty=180&timing=research&priority=heavy">I have concrete or brick</a>
            <a class="decision-pill" data-decision-mode="roof_job" href="/dumpster/size-weight-calculator?project=roof_tearoff&material=asphalt_shingles&unit=roof_square&qty=22&timing=research&priority=balanced">I have shingles</a>
            <a class="decision-pill" data-decision-mode="remodel_job" href="/dumpster/size-weight-calculator?project=kitchen_remodel&material=plaster&unit=pickup_load&qty=3&timing=research&priority=balanced">I have remodel plaster or mixed demo</a>
            <a class="decision-pill" data-decision-mode="garage_job" href="/dumpster/size-weight-calculator?project=garage_cleanout&material=cardboard_packaging&unit=pickup_load&qty=5&timing=research&priority=balanced">I have bulky garage loads</a>
        </nav>
        <div class="calc-benefits">
            <article class="calc-benefit">
                <strong>Answer-first entry</strong>
                <p>Lead with the real question that wins impressions, then hand off into the estimate only after the debris pattern is clear.</p>
            </article>
            <article class="calc-benefit">
                <strong>Dense and awkward loads</strong>
                <p>Handle shingles, garage overflow, mixed remodel debris, deck teardown, and concrete with the same risk-first engine.</p>
            </article>
            <article class="calc-benefit">
                <strong>Conversion layer</strong>
                <p>Route each winner scenario into heavy-load options, crew-pickup comparison, or staged-haul planning before booking.</p>
            </article>
        </div>
        <div class="inline-cta-row" style="margin-top:1rem;">
            <a class="ui-button ui-button--primary" href="#estimate-form">Run heavy-debris estimate</a>
            <a class="ui-button ui-button--ghost" href="/about/quote-match-beta">Check local heavy-load options</a>
            <a class="ui-button ui-button--ghost" href="/dumpster/can-you-put-concrete-in-a-dumpster">Open concrete rules</a>
        </div>
    </section>

    @if(model.featuredAnswerLinks() != null && !model.featuredAnswerLinks().isEmpty())
        <section class="panel">
            <h2 class="font-display">Winner decision guides</h2>
            <p>These answer pages now match the site’s real acquisition pattern better than the broad calculator head term.</p>
            <ul class="link-grid">
                @for(var link : model.featuredAnswerLinks())
                    <li>
                        <a href="${link.href()}">${link.label()}</a>
                        <span>${link.summary()}</span>
                    </li>
                @endfor
            </ul>
        </section>
    @endif

    <section class="calc-layout">
        <form id="estimate-form" class="calc-workflow">
            <input id="project-id" type="hidden" value="concrete_removal">
            <input id="persona" type="hidden" value="homeowner">
            <input id="material-id" type="hidden" value="concrete">
            <input id="unit-id" type="hidden" value="pickup_load">
            <input id="need-timing" type="hidden" value="research">
            <input id="decision-priority" type="hidden" value="balanced">

            <div class="estimate-mode-toggle" role="group" aria-label="Estimate mode">
                <button type="button" id="quick-mode-button" class="ui-button ui-button--ghost is-active">Quick estimate</button>
                <button type="button" id="advanced-mode-button" class="ui-button ui-button--ghost">Advanced mode</button>
            </div>
            <p id="estimate-mode-note" class="calc-step__hint">Quick mode starts with a heavy-debris job, dominant material, and quantity for the fastest feasibility call.</p>

            <div class="calc-stepper">
                <section class="calc-step" data-choice-target="project-id">
                    <div class="calc-step__head">
                        <div>
                            <h2 class="calc-step__title">Heavy-debris job</h2>
                            <p class="calc-step__hint">Start with the dense-load workflow that best matches the site reality.</p>
                        </div>
                    </div>
                    <div class="choice-grid">
                        <button type="button" class="choice-chip is-active" data-choice-value="concrete_removal">Concrete removal</button>
                        <button type="button" class="choice-chip" data-choice-value="roof_tearoff">Roof tear-off</button>
                        <button type="button" class="choice-chip" data-choice-value="garage_cleanout">Garage cleanout</button>
                        <button type="button" class="choice-chip" data-choice-value="kitchen_remodel">Kitchen remodel</button>
                        <button type="button" class="choice-chip" data-choice-value="deck_demolition">Deck demo</button>
                        <button type="button" class="choice-chip" data-choice-value="dirt_grading">Dirt grading</button>
                        <button type="button" class="choice-chip" data-choice-value="bathroom_remodel">Bathroom remodel</button>
                        <button type="button" class="choice-chip" data-choice-value="light_commercial_fitout">Light commercial fit-out</button>
                    </div>
                    <details class="ui-accordion" style="margin-top:0.9rem;">
                        <summary>
                            <span>Other cleanup cases</span>
                            <small>Fallback scenarios that are not the lead wedge</small>
                        </summary>
                        <div class="ui-accordion__body">
                            <div class="choice-grid choice-grid--compact">
                                <button type="button" class="choice-chip" data-choice-value="estate_cleanout">Estate cleanout</button>
                                <button type="button" class="choice-chip" data-choice-value="moving_cleanout">Moving / apartment cleanout</button>
                                <button type="button" class="choice-chip" data-choice-value="furniture_appliance_disposal">Furniture or appliance disposal</button>
                                <button type="button" class="choice-chip" data-choice-value="general_junk_removal">General cleanup fallback</button>
                                <button type="button" class="choice-chip" data-choice-value="yard_cleanup">Yard cleanup</button>
                            </div>
                        </div>
                    </details>
                </section>

                <section class="calc-step" data-choice-target="material-id">
                    <div class="calc-step__head">
                        <div>
                            <h2 class="calc-step__title">Dominant material</h2>
                            <p class="calc-step__hint">Lead with the material most likely to break haul limits or overage assumptions.</p>
                        </div>
                    </div>
                    <div class="choice-grid">
                        <button type="button" class="choice-chip is-active" data-choice-value="concrete">Concrete</button>
                        <button type="button" class="choice-chip" data-choice-value="asphalt_shingles">Asphalt shingles</button>
                        <button type="button" class="choice-chip" data-choice-value="mixed_cd">Mixed construction debris (C&amp;D)</button>
                        <button type="button" class="choice-chip" data-choice-value="cardboard_packaging">Cardboard / bulky packing</button>
                        <button type="button" class="choice-chip" data-choice-value="plaster">Plaster</button>
                        <button type="button" class="choice-chip" data-choice-value="decking_wood">Decking wood</button>
                        <button type="button" class="choice-chip" data-choice-value="dirt_soil">Dirt / soil</button>
                        <button type="button" class="choice-chip" data-choice-value="tile_ceramic">Tile / ceramic</button>
                        <button type="button" class="choice-chip" data-choice-value="drywall">Drywall</button>
                        <button type="button" class="choice-chip" data-choice-value="brick">Brick</button>
                    </div>
                    <details class="ui-accordion" style="margin-top:0.9rem;">
                        <summary>
                            <span>Other material profiles</span>
                            <small>Fallback materials that are not the current lead wedge</small>
                        </summary>
                        <div class="ui-accordion__body">
                            <div class="choice-grid choice-grid--compact">
                                <button type="button" class="choice-chip" data-choice-value="household_junk">Household junk</button>
                                <button type="button" class="choice-chip" data-choice-value="furniture">Furniture</button>
                                <button type="button" class="choice-chip" data-choice-value="carpet_pad">Carpet and pad</button>
                                <button type="button" class="choice-chip" data-choice-value="insulation_wet">Wet insulation</button>
                                <button type="button" class="choice-chip" data-choice-value="yard_waste">Yard waste</button>
                                <button type="button" class="choice-chip" data-choice-value="gravel_rock">Gravel / rock</button>
                                <button type="button" class="choice-chip" data-choice-value="asphalt_pavement">Asphalt pavement</button>
                            </div>
                        </div>
                    </details>
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
                            <input id="quantity" type="number" step="0.1" min="0.1" value="1" required>
                            <button type="button" id="quantity-inc" class="calc-stepper-btn" aria-label="Increase quantity">+</button>
                        </div>
                    </div>
                    <div id="advanced-material-controls" hidden>
                        <div class="calc-step__head" style="margin-top:1rem;">
                            <div>
                                <h3 class="calc-step__title" style="font-size:1rem;">Additional materials (optional)</h3>
                                <p class="calc-step__hint">Add up to 2 extra line items for mixed jobs.</p>
                            </div>
                        </div>
                        <p>
                            <button type="button" id="add-material-line-2" class="choice-chip">Add second material</button>
                        </p>
                        <div id="material-line-2" class="panel" hidden>
                            <div class="ui-field">
                                <label class="ui-field__label" for="material-id-2">Material</label>
                                <select id="material-id-2">
                                    <option value="">Select material</option>
                                    <option value="asphalt_shingles">Asphalt shingles</option>
                                    <option value="concrete">Concrete</option>
                                    <option value="dirt_soil">Dirt / soil</option>
                                    <option value="brick">Brick</option>
                                    <option value="tile_ceramic">Tile / ceramic</option>
                                    <option value="gravel_rock">Gravel / rock</option>
                                    <option value="asphalt_pavement">Asphalt pavement</option>
                                    <option value="plaster">Plaster</option>
                                    <option value="drywall">Drywall</option>
                                    <option value="mixed_cd">Mixed construction debris (C&amp;D)</option>
                                    <option value="household_junk">Household junk</option>
                                    <option value="furniture">Furniture</option>
                                    <option value="carpet_pad">Carpet and pad</option>
                                    <option value="decking_wood">Decking wood</option>
                                    <option value="insulation_wet">Wet insulation</option>
                                    <option value="yard_waste">Yard waste</option>
                                </select>
                            </div>
                            <div class="ui-field">
                                <label class="ui-field__label" for="unit-id-2">Unit</label>
                                <select id="unit-id-2">
                                    <option value="pickup_load">Pickup load</option>
                                    <option value="truckload_small">Small truckload</option>
                                    <option value="roof_square">Roof square</option>
                                    <option value="sqft_4in">sqft @ 4in</option>
                                    <option value="sqft_2in">sqft @ 2in</option>
                                    <option value="sqft_1in">sqft @ 1in</option>
                                    <option value="contractor_bag">Contractor bag</option>
                                    <option value="drywall_sheet">Drywall sheet</option>
                                    <option value="cubic_yard">Cubic yard</option>
                                </select>
                            </div>
                            <div class="ui-field">
                                <label class="ui-field__label" for="quantity-2">Quantity</label>
                                <input id="quantity-2" type="number" min="0.1" step="0.1" value="2">
                            </div>
                            <p>
                                <button type="button" id="add-material-line-3" class="choice-chip">Add third material</button>
                                <button type="button" id="remove-material-line-2" class="choice-chip">Remove</button>
                            </p>
                        </div>
                        <div id="material-line-3" class="panel" hidden>
                            <div class="ui-field">
                                <label class="ui-field__label" for="material-id-3">Material</label>
                                <select id="material-id-3">
                                    <option value="">Select material</option>
                                    <option value="asphalt_shingles">Asphalt shingles</option>
                                    <option value="concrete">Concrete</option>
                                    <option value="dirt_soil">Dirt / soil</option>
                                    <option value="brick">Brick</option>
                                    <option value="tile_ceramic">Tile / ceramic</option>
                                    <option value="gravel_rock">Gravel / rock</option>
                                    <option value="asphalt_pavement">Asphalt pavement</option>
                                    <option value="plaster">Plaster</option>
                                    <option value="drywall">Drywall</option>
                                    <option value="mixed_cd">Mixed construction debris (C&amp;D)</option>
                                    <option value="household_junk">Household junk</option>
                                    <option value="furniture">Furniture</option>
                                    <option value="carpet_pad">Carpet and pad</option>
                                    <option value="decking_wood">Decking wood</option>
                                    <option value="insulation_wet">Wet insulation</option>
                                    <option value="yard_waste">Yard waste</option>
                                </select>
                            </div>
                            <div class="ui-field">
                                <label class="ui-field__label" for="unit-id-3">Unit</label>
                                <select id="unit-id-3">
                                    <option value="pickup_load">Pickup load</option>
                                    <option value="truckload_small">Small truckload</option>
                                    <option value="roof_square">Roof square</option>
                                    <option value="sqft_4in">sqft @ 4in</option>
                                    <option value="sqft_2in">sqft @ 2in</option>
                                    <option value="sqft_1in">sqft @ 1in</option>
                                    <option value="contractor_bag">Contractor bag</option>
                                    <option value="drywall_sheet">Drywall sheet</option>
                                    <option value="cubic_yard">Cubic yard</option>
                                </select>
                            </div>
                            <div class="ui-field">
                                <label class="ui-field__label" for="quantity-3">Quantity</label>
                                <input id="quantity-3" type="number" min="0.1" step="0.1" value="1">
                            </div>
                            <p>
                                <button type="button" id="remove-material-line-3" class="choice-chip">Remove</button>
                            </p>
                        </div>
                    </div>
                </section>

                <section class="calc-step" id="advanced-context-step" hidden>
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
                <button id="open-advanced-mode" type="button" class="ui-button ui-button--ghost" hidden>Refine with advanced mode</button>
                <p id="live-note" aria-live="polite">Live update is on. Any change refreshes the recommendation.</p>
            </div>
        </form>

        <aside class="result-rail" id="live-dashboard">
            <div class="result-rail__header">
                <p class="eyebrow">Result summary</p>
                <h2 class="font-display">Decision board</h2>
                <p id="live-status" class="result-status">Start with your project and material to see the safest next move.</p>
            </div>

            <section id="result-precalc" class="result-precalc">
                <p class="result-precalc__lead"><strong>Direct answer:</strong> this tool decides the safest heavy-load route before you call anyone.</p>
                <ul>
                    <li><strong>Route:</strong> dumpster, crew pickup, or staged multi-haul.</li>
                    <li><strong>Risk:</strong> where overage or feasibility can break the plan.</li>
                    <li><strong>Action:</strong> exact next CTA based on your timing and material profile.</li>
                </ul>
            </section>

            <div id="gauge-dashboard" class="gauge-dashboard" hidden>
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
                <p id="result-verdict" class="result-verdict">Your recommendation appears here after the first live estimate.</p>
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
            <h2 class="font-display">Check local heavy-load availability</h2>
            <p>Once the route looks feasible, move from estimation into local heavy-load matching so you can validate operator policy before booking.</p>
            <p><a class="ui-button ui-button--primary" href="/about/quote-match-beta">Check local heavy-load options</a></p>
            <p><a href="/dumpster/dumpster-vs-junk-removal-which-is-cheaper">Or compare dumpster vs crew pickup first</a></p>
        </article>

        <article class="calc-support-card">
            <h2 class="font-display">Vendor checklist</h2>
            <p class="calc-step__hint">Evidence confidence: ${model.confidenceTier()} | Confirm before booking.</p>
            <ol>
                @for(var item : model.vendorChecklist())
                    <li>${item}</li>
                @endfor
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
            <h2 class="font-display">Specialist guides</h2>
            <div>
                <h3>Heavy-debris guides</h3>
                <ul>
                    @for(var link : model.featuredMaterialLinks())
                        <li><a href="${link.href()}">${link.label()}</a></li>
                    @endfor
                </ul>
                <p><a href="/dumpster/material-guides">See all material guides</a></p>
            </div>
            <div>
                <h3>Heavy-debris job starts</h3>
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
        <p id="mobile-result-verdict">Your recommendation appears after the first live estimate.</p>
        <a id="mobile-primary-cta" class="ui-button ui-button--primary" href="#estimate-form">View result</a>
    </div>
</div>

<div id="floating-cta" class="floating-cta" hidden>
    <a id="floating-call" href="/about/quote-match-beta">Check local heavy-load options</a>
    <a id="floating-quote" href="#estimate-form">Run live estimate</a>
</div>

<script src="/js/calculator.js?v=stripeapple4"></script>
</body>
</html>
