@param com.dumpster.calculator.web.viewmodel.CalculatorPageViewModel model
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.pageTitle()}</title>
    <meta name="description" content="Estimate dumpster size and weight range in under 60 seconds. Compare overweight risk, multi-haul feasibility, and next best action.">
    <link rel="canonical" href="${model.canonicalPath()}">
    <meta property="og:title" content="${model.pageTitle()}">
    <meta property="og:description" content="Range-based dumpster sizing with weight risk and heavy debris warnings.">
    <meta property="og:type" content="website">
    <meta property="og:url" content="${model.canonicalPath()}">
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
          "url": "/dumpster/size-weight-calculator"
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
<main class="shell">
    <section class="hero">
        <p class="eyebrow">Decision Engine</p>
        <h1>${model.pageTitle()}</h1>
        <p>Get a range-based size and weight recommendation with overweight risk and next action routing.</p>
    </section>

    <section class="panel">
        <h2>Quick Input</h2>
        <form id="estimate-form" class="form-grid">
            <label>
                Project
                <select id="project-id" required>
                    <option value="roof_tearoff">Roof tear-off</option>
                    <option value="kitchen_remodel">Kitchen remodel</option>
                    <option value="bathroom_remodel">Bathroom remodel</option>
                    <option value="deck_demolition">Deck demolition</option>
                    <option value="garage_cleanout">Garage cleanout</option>
                    <option value="estate_cleanout">Estate cleanout</option>
                    <option value="yard_cleanup">Yard cleanup</option>
                    <option value="dirt_grading">Dirt grading</option>
                    <option value="concrete_removal">Concrete removal</option>
                    <option value="light_commercial_fitout">Light commercial fit-out</option>
                </select>
            </label>
            <label>
                Persona
                <select id="persona" required>
                    <option value="homeowner">Homeowner</option>
                    <option value="contractor">Contractor</option>
                    <option value="business">Business</option>
                </select>
            </label>
            <label>
                Material
                <select id="material-id" required>
                    <option value="asphalt_shingles">Asphalt shingles</option>
                    <option value="concrete">Concrete</option>
                    <option value="dirt_soil">Dirt/soil</option>
                    <option value="brick">Brick</option>
                    <option value="tile_ceramic">Tile/ceramic</option>
                    <option value="gravel_rock">Gravel/rock</option>
                    <option value="asphalt_pavement">Asphalt pavement</option>
                    <option value="plaster">Plaster</option>
                    <option value="drywall">Drywall</option>
                    <option value="mixed_cd">Mixed C&D</option>
                    <option value="household_junk">Household junk</option>
                    <option value="furniture">Furniture</option>
                    <option value="carpet_pad">Carpet and pad</option>
                    <option value="decking_wood">Decking wood</option>
                    <option value="insulation_wet">Wet insulation</option>
                    <option value="yard_waste">Yard waste</option>
                </select>
            </label>
            <label>
                Unit
                <select id="unit-id" required>
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
            </label>
            <label>
                Quantity
                <input id="quantity" type="number" step="0.1" min="0.1" value="6" required>
            </label>
            <label>
                Included tons (optional)
                <input id="allowance-tons" type="number" step="0.1" min="0">
            </label>
            <label>
                Need timing
                <select id="need-timing">
                    <option value="research">Researching</option>
                    <option value="this_week">This week</option>
                    <option value="48h">48 hours</option>
                </select>
            </label>
            <label class="check">
                <input id="wet" type="checkbox">
                Wet load
            </label>
            <label class="check">
                <input id="mixed-load" type="checkbox">
                Mixed load
            </label>
            <button id="submit-button" type="submit">Calculate</button>
        </form>
    </section>

    <section class="panel result-panel" id="result-panel" hidden>
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

    <section class="panel">
        <h2>Vendor Checklist</h2>
        <ol>
            <li>What included tons are in this quote?</li>
            <li>What is overage per ton?</li>
            <li>For heavy debris, is clean load required?</li>
        </ol>
    </section>
</main>
<script src="/js/calculator.js"></script>
</body>
</html>
