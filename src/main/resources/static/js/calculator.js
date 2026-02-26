(function () {
    const form = document.getElementById("estimate-form");
    if (!form) {
        return;
    }

    const resultPanel = document.getElementById("result-panel");
    const resultSummary = document.getElementById("result-summary");
    const resultBadges = document.getElementById("result-badges");
    const resultRecommendations = document.getElementById("result-recommendations");
    const resultCosts = document.getElementById("result-costs");
    const resultAssumptions = document.getElementById("result-assumptions");
    const resultActions = document.getElementById("result-actions");
    const shareLink = document.getElementById("share-link");
    const submitButton = document.getElementById("submit-button");
    const personaSelect = document.getElementById("persona");
    const materialSelect = document.getElementById("material-id");
    const allowanceInput = document.getElementById("allowance-tons");
    const heavyMaterials = new Set([
        "asphalt_shingles",
        "concrete",
        "dirt_soil",
        "brick",
        "tile_ceramic",
        "gravel_rock",
        "asphalt_pavement",
        "metal_scrap_light"
    ]);

    if (personaSelect) {
        personaSelect.addEventListener("change", () => {
            trackEvent("persona_selected", null, {persona: personaSelect.value});
        });
    }

    if (materialSelect) {
        materialSelect.addEventListener("change", () => {
            if (heavyMaterials.has(materialSelect.value)) {
                trackEvent("heavy_debris_flagged", null, {
                    materialId: materialSelect.value,
                    source: "material_selector"
                });
            }
        });
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        submitButton.disabled = true;
        submitButton.textContent = "Calculating...";
        clearResult();

        const payload = buildPayload();
        trackEvent("calc_started", null, {
            projectId: payload.projectId,
            persona: payload.persona
        });
        if (payload.options.allowanceTons !== null) {
            trackEvent("allowance_entered", null, {allowanceTons: payload.options.allowanceTons});
        }
        if (payload.items.some((item) => heavyMaterials.has(item.materialId))) {
            trackEvent("heavy_debris_flagged", null, {
                projectId: payload.projectId,
                source: "submit_payload"
            });
        }

        try {
            const response = await fetch("/api/estimates", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });
            if (!response.ok) {
                throw new Error("Estimate request failed");
            }
            const data = await response.json();
            trackEvent("calc_completed", data.estimateId, {
                projectId: payload.projectId,
                persona: payload.persona
            });
            renderResult(data);
        } catch (error) {
            resultPanel.hidden = false;
            resultSummary.innerHTML = "<p class=\"warn\">Could not calculate estimate. Check inputs and retry.</p>";
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Calculate";
        }
    });

    function buildPayload() {
        const materialId = document.getElementById("material-id").value;
        const unitId = document.getElementById("unit-id").value;
        const quantity = parseFloat(document.getElementById("quantity").value || "0");
        const allowanceRaw = allowanceInput.value;
        const mixed = document.getElementById("mixed-load").checked;
        const wet = document.getElementById("wet").checked;

        return {
            projectId: document.getElementById("project-id").value,
            persona: document.getElementById("persona").value,
            needTiming: document.getElementById("need-timing").value,
            items: [
                {
                    materialId,
                    quantity,
                    unitId,
                    conditions: {
                        wet,
                        mixedLoad: mixed,
                        compaction: "MEDIUM"
                    }
                }
            ],
            options: {
                mixedLoad: mixed,
                allowanceTons: allowanceRaw === "" ? null : parseFloat(allowanceRaw),
                bulkingFactor: 1.2
            }
        };
    }

    function renderResult(apiData) {
        const result = apiData.result;
        resultPanel.hidden = false;
        shareLink.href = "/dumpster/estimate/" + apiData.estimateId;
        trackEvent("result_viewed", apiData.estimateId, {
            priceRisk: result.priceRisk,
            feasibility: result.feasibility
        });
        if (result.usedAssumedAllowance) {
            trackEvent("used_assumed_allowance", apiData.estimateId, {source: "frontend_render"});
        }
        if (result.feasibility !== "OK") {
            trackEvent("feasibility_not_ok", apiData.estimateId, {feasibility: result.feasibility});
        }
        trackEvent("share_estimate_created", apiData.estimateId, {sharePath: shareLink.href});

        resultBadges.innerHTML = [
            badge("Risk: " + result.priceRisk, result.priceRisk === "HIGH" ? "danger" : "neutral"),
            badge("Feasibility: " + result.feasibility, result.feasibility === "OK" ? "ok" : "warn"),
            result.usedAssumedAllowance ? badge("Allowance assumed", "warn") : badge("Allowance provided", "ok")
        ].join("");

        resultSummary.innerHTML = `
            <article class="stat">
                <h3>Volume (yd3)</h3>
                <p>${fmt(result.volumeYd3.low)} - ${fmt(result.volumeYd3.high)}</p>
            </article>
            <article class="stat">
                <h3>Weight (tons)</h3>
                <p>${fmt(result.weightTons.low)} - ${fmt(result.weightTons.high)}</p>
            </article>
            <article class="stat">
                <h3>CTA Route</h3>
                <p>${result.ctaRouting.primaryCta} -> ${result.ctaRouting.secondaryCta}</p>
            </article>
        `;

        resultRecommendations.innerHTML = result.recommendations.map((rec) => `
            <article class="result-card">
                <h3>${rec.label} - ${rec.sizeYd}yd</h3>
                <p>Risk: ${rec.risk} / Feasibility: ${rec.feasibility}</p>
                <p>Multi-haul: ${rec.multiHaul ? "Yes (" + rec.haulCount + ")" : "No"}</p>
                <ul>${rec.why.map((reason) => `<li>${reason}</li>`).join("")}</ul>
            </article>
        `).join("");

        resultCosts.innerHTML = result.costComparison.map((cost) => `
            <article class="result-card">
                <h3>${cost.title}</h3>
                <p>${cost.summary}</p>
                <p>${cost.available ? "$" + fmt(cost.estimatedTotalCostUsd.low) + " - $" + fmt(cost.estimatedTotalCostUsd.high) : "Unavailable"}</p>
            </article>
        `).join("");

        resultAssumptions.innerHTML = `
            <h3>Assumptions</h3>
            <ul>${result.assumptions.map((item) => `<li>${item}</li>`).join("")}</ul>
            <h3>Input Impact</h3>
            <ul>${result.inputImpactSummary.map((item) => `<li>${item}</li>`).join("")}</ul>
        `;

        resultActions.innerHTML = `
            <a class="button-link primary" id="cta-dumpster-call" href="tel:+18005550123">Call dumpster quote</a>
            <a class="button-link" id="cta-dumpster-form" href="#dumpster-form">Request online quote</a>
            <a class="button-link" id="cta-junk" href="#junk">Compare junk removal</a>
        `;

        const dumpsterCall = document.getElementById("cta-dumpster-call");
        const dumpsterForm = document.getElementById("cta-dumpster-form");
        const junkCall = document.getElementById("cta-junk");
        if (dumpsterCall) {
            dumpsterCall.addEventListener("click", () => trackEvent("cta_click_dumpster_call", apiData.estimateId, {}));
        }
        if (dumpsterForm) {
            dumpsterForm.addEventListener("click", () => trackEvent("cta_click_dumpster_form", apiData.estimateId, {}));
        }
        if (junkCall) {
            junkCall.addEventListener("click", () => trackEvent("cta_click_junk_call", apiData.estimateId, {}));
        }
    }

    function clearResult() {
        resultPanel.hidden = true;
        resultBadges.innerHTML = "";
        resultSummary.innerHTML = "";
        resultRecommendations.innerHTML = "";
        resultCosts.innerHTML = "";
        resultAssumptions.innerHTML = "";
        resultActions.innerHTML = "";
        shareLink.href = "#";
    }

    function badge(text, style) {
        return `<span class="badge ${style}">${text}</span>`;
    }

    function fmt(value) {
        return Number(value).toFixed(2);
    }

    function trackEvent(eventName, estimateId, payload) {
        fetch("/api/events", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                eventName,
                estimateId,
                payload: payload || {}
            })
        }).catch(() => {});
    }
})();
