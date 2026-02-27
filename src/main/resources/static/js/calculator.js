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
    const floatingCta = document.getElementById("floating-cta");
    const floatingCall = document.getElementById("floating-call");
    const floatingQuote = document.getElementById("floating-quote");
    const projectSelect = document.getElementById("project-id");
    const personaSelect = document.getElementById("persona");
    const materialSelect = document.getElementById("material-id");
    const unitSelect = document.getElementById("unit-id");
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

    applyPresetFromQuery();
    syncUnitOptions();

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
            syncUnitOptions();
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
            renderResult(data, payload);
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

    function renderResult(apiData, inputPayload) {
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
            <article class="result-card ${recTone(rec.label)}">
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
            <section class="lead-capture">
                <h3>Get local quotes</h3>
                <div class="lead-step" id="lead-step-1">
                    <label for="lead-zip">ZIP code</label>
                    <input id="lead-zip" type="text" inputmode="numeric" maxlength="5" placeholder="e.g. 30339">
                    <button type="button" id="lead-next">Next</button>
                </div>
                <div class="lead-step" id="lead-step-2" hidden>
                    <label for="lead-contact-method">Contact preference</label>
                    <select id="lead-contact-method">
                        <option value="phone">Phone</option>
                        <option value="email">Email</option>
                    </select>
                    <label for="lead-contact-value" id="lead-contact-label">Phone number</label>
                    <input id="lead-contact-value" type="tel" placeholder="(555) 555-5555">
                    <button type="button" id="lead-submit">Submit lead</button>
                </div>
                <p class="lead-hint" id="lead-status" aria-live="polite"></p>
            </section>
            <a class="button-link primary" id="cta-dumpster-call" href="tel:+18005550123">Call dumpster quote</a>
            <a class="button-link" id="cta-dumpster-form" href="#dumpster-form">Request online quote</a>
            <a class="button-link" id="cta-junk" href="#junk">Compare junk removal</a>
        `;

        const dumpsterCall = document.getElementById("cta-dumpster-call");
        const dumpsterForm = document.getElementById("cta-dumpster-form");
        const junkCall = document.getElementById("cta-junk");
        const leadStep1 = document.getElementById("lead-step-1");
        const leadStep2 = document.getElementById("lead-step-2");
        const leadZip = document.getElementById("lead-zip");
        const leadNext = document.getElementById("lead-next");
        const leadSubmit = document.getElementById("lead-submit");
        const leadStatus = document.getElementById("lead-status");
        const leadContactMethod = document.getElementById("lead-contact-method");
        const leadContactValue = document.getElementById("lead-contact-value");
        const leadContactLabel = document.getElementById("lead-contact-label");
        const prefersPhone = inputPayload.needTiming === "48h";
        const emitLeadSubmitted = (source) => {
            if (!leadZip || !leadContactMethod || !leadContactValue) {
                return;
            }
            const zip = sanitizeZip(leadZip.value);
            const contact = (leadContactValue.value || "").trim();
            if (!isValidZip(zip) || contact === "") {
                return;
            }
            trackEvent("lead_submitted", apiData.estimateId, {
                source,
                zipCode: zip,
                contactMethod: leadContactMethod.value,
                needTiming: inputPayload.needTiming,
                projectId: inputPayload.projectId,
                primaryCta: result.ctaRouting.primaryCta
            });
        };
        if (leadContactMethod) {
            leadContactMethod.value = prefersPhone ? "phone" : "email";
            updateLeadContactField(leadContactMethod, leadContactValue, leadContactLabel);
            leadContactMethod.addEventListener("change", () => {
                updateLeadContactField(leadContactMethod, leadContactValue, leadContactLabel);
            });
        }
        if (leadNext && leadZip && leadStep2 && leadStatus) {
            leadNext.addEventListener("click", () => {
                const zip = sanitizeZip(leadZip.value);
                if (!isValidZip(zip)) {
                    leadStatus.textContent = "Enter a valid 5-digit ZIP code.";
                    return;
                }
                leadStatus.textContent = "";
                leadStep1.hidden = true;
                leadStep2.hidden = false;
                if (leadContactValue) {
                    leadContactValue.focus();
                }
            });
        }
        if (leadSubmit && leadZip && leadContactMethod && leadContactValue && leadStatus) {
            leadSubmit.addEventListener("click", () => {
                const zip = sanitizeZip(leadZip.value);
                const contact = (leadContactValue.value || "").trim();
                if (!isValidZip(zip)) {
                    leadStatus.textContent = "Enter a valid 5-digit ZIP code.";
                    return;
                }
                if (contact === "") {
                    leadStatus.textContent = "Enter your contact information.";
                    return;
                }
                emitLeadSubmitted("lead_form_submit");
                leadStatus.textContent = "Lead submitted. A quote partner can contact you next.";
            });
        }
        if (floatingCta) {
            floatingCta.hidden = false;
        }
        if (floatingCall) {
            floatingCall.onclick = () => trackEvent("cta_click_dumpster_call", apiData.estimateId, {source: "floating"});
        }
        if (floatingQuote) {
            floatingQuote.onclick = () => trackEvent("cta_click_dumpster_form", apiData.estimateId, {source: "floating"});
        }
        if (dumpsterCall) {
            dumpsterCall.addEventListener("click", () => {
                trackEvent("cta_click_dumpster_call", apiData.estimateId, {});
                emitLeadSubmitted("cta_dumpster_call");
                if (inputPayload.needTiming === "48h") {
                    trackEvent("call_qualified", apiData.estimateId, {source: "call_click_proxy"});
                }
            });
        }
        if (dumpsterForm) {
            dumpsterForm.addEventListener("click", () => {
                trackEvent("cta_click_dumpster_form", apiData.estimateId, {});
                emitLeadSubmitted("cta_dumpster_form");
            });
        }
        if (junkCall) {
            junkCall.addEventListener("click", () => {
                trackEvent("cta_click_junk_call", apiData.estimateId, {});
                emitLeadSubmitted("cta_junk");
            });
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
        if (floatingCta) {
            floatingCta.hidden = true;
        }
    }

    function badge(text, style) {
        return `<span class="badge ${style}">${text}</span>`;
    }

    function fmt(value) {
        return Number(value).toFixed(2);
    }

    function recTone(label) {
        const tone = String(label || "").toLowerCase();
        if (tone.includes("safe")) {
            return "safe";
        }
        if (tone.includes("budget")) {
            return "budget";
        }
        return "";
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

    function applyPresetFromQuery() {
        const params = new URLSearchParams(window.location.search);
        const presetProject = params.get("project");
        const presetMaterial = params.get("material");
        if (projectSelect && hasOption(projectSelect, presetProject)) {
            projectSelect.value = presetProject;
        }
        if (materialSelect && hasOption(materialSelect, presetMaterial)) {
            materialSelect.value = presetMaterial;
        }
    }

    function syncUnitOptions() {
        if (!unitSelect || !materialSelect) {
            return;
        }
        const roofSquareOption = unitSelect.querySelector("option[value='roof_square']");
        if (!roofSquareOption) {
            return;
        }
        const shingles = materialSelect.value === "asphalt_shingles";
        roofSquareOption.hidden = !shingles;
        roofSquareOption.disabled = !shingles;
        if (!shingles && unitSelect.value === "roof_square") {
            unitSelect.value = "pickup_load";
        }
    }

    function hasOption(select, value) {
        if (!select || !value) {
            return false;
        }
        return Array.from(select.options).some((option) => option.value === value);
    }

    function sanitizeZip(value) {
        return String(value || "").replace(/[^0-9]/g, "").slice(0, 5);
    }

    function isValidZip(value) {
        return /^[0-9]{5}$/.test(value);
    }

    function updateLeadContactField(methodEl, valueEl, labelEl) {
        if (!methodEl || !valueEl || !labelEl) {
            return;
        }
        if (methodEl.value === "email") {
            valueEl.type = "email";
            valueEl.placeholder = "name@company.com";
            labelEl.textContent = "Email";
        } else {
            valueEl.type = "tel";
            valueEl.placeholder = "(555) 555-5555";
            labelEl.textContent = "Phone number";
        }
    }
})();
