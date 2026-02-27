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
    const quantityInput = document.getElementById("quantity");
    const allowanceInput = document.getElementById("allowance-tons");
    const wetInput = document.getElementById("wet");
    const mixedInput = document.getElementById("mixed-load");
    const quantityInc = document.getElementById("quantity-inc");
    const quantityDec = document.getElementById("quantity-dec");
    const liveNote = document.getElementById("live-note");
    const liveStatus = document.getElementById("live-status");
    const gaugeWeight = document.getElementById("gauge-weight");
    const gaugeVolume = document.getElementById("gauge-volume");
    const gaugeRisk = document.getElementById("gauge-risk");
    const gaugeWeightLabel = document.getElementById("gauge-weight-label");
    const gaugeVolumeLabel = document.getElementById("gauge-volume-label");
    const gaugeRiskLabel = document.getElementById("gauge-risk-label");
    const projectInput = document.getElementById("project-id");
    const personaInput = document.getElementById("persona");
    const materialInput = document.getElementById("material-id");
    const unitInput = document.getElementById("unit-id");
    const needTimingInput = document.getElementById("need-timing");
    const choiceGroups = Array.from(form.querySelectorAll("[data-choice-target]"));
    const roofSquareChip = form.querySelector("[data-choice-target='unit-id'] [data-choice-value='roof_square']");
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

    let liveDebounceId = null;
    let activeRequestController = null;
    let requestSequence = 0;

    applyPresetFromQuery();
    initializeChoiceGroups();
    syncUnitOptions();
    bindStepperControls();
    bindFormControls();
    queueLiveEstimate();

    form.addEventListener("submit", (event) => {
        event.preventDefault();
        runEstimate("submit");
    });

    function initializeChoiceGroups() {
        choiceGroups.forEach((group) => {
            const targetId = group.getAttribute("data-choice-target");
            const targetInput = document.getElementById(targetId);
            if (!targetInput) {
                return;
            }
            const buttons = Array.from(group.querySelectorAll("[data-choice-value]"));
            buttons.forEach((button) => {
                button.addEventListener("click", () => {
                    if (button.disabled) {
                        return;
                    }
                    targetInput.value = button.getAttribute("data-choice-value");
                    syncChoiceButtons(group);
                    if (targetId === "material-id") {
                        if (heavyMaterials.has(targetInput.value)) {
                            trackEvent("heavy_debris_flagged", null, {
                                materialId: targetInput.value,
                                source: "material_chip"
                            });
                        }
                        syncUnitOptions();
                    }
                    if (targetId === "persona") {
                        trackEvent("persona_selected", null, {persona: targetInput.value});
                    }
                    queueLiveEstimate();
                });
            });
            syncChoiceButtons(group);
        });
    }

    function bindStepperControls() {
        if (quantityInc) {
            quantityInc.addEventListener("click", () => {
                stepQuantity(1);
            });
        }
        if (quantityDec) {
            quantityDec.addEventListener("click", () => {
                stepQuantity(-1);
            });
        }
    }

    function bindFormControls() {
        const liveInputs = [quantityInput, allowanceInput, wetInput, mixedInput];
        liveInputs.forEach((input) => {
            if (!input) {
                return;
            }
            input.addEventListener("input", () => queueLiveEstimate());
            input.addEventListener("change", () => queueLiveEstimate());
        });
    }

    function queueLiveEstimate() {
        if (liveDebounceId) {
            window.clearTimeout(liveDebounceId);
        }
        if (liveNote) {
            liveNote.textContent = "Live update is on. Refreshing recommendation...";
        }
        liveDebounceId = window.setTimeout(() => {
            runEstimate("live");
        }, 360);
    }

    async function runEstimate(trigger) {
        if (!isPayloadReady()) {
            return;
        }
        const payload = buildPayload();

        if (activeRequestController) {
            activeRequestController.abort();
        }
        const controller = new AbortController();
        activeRequestController = controller;
        const runId = ++requestSequence;

        setLoadingState(true, trigger);

        if (trigger === "submit") {
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
        }

        try {
            const response = await fetch("/api/estimates", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload),
                signal: controller.signal
            });
            if (!response.ok) {
                throw new Error("Estimate request failed");
            }
            const data = await response.json();
            if (runId !== requestSequence) {
                return;
            }

            if (trigger === "submit") {
                trackEvent("calc_completed", data.estimateId, {
                    projectId: payload.projectId,
                    persona: payload.persona
                });
            }
            renderResult(data, payload);
        } catch (error) {
            if (error && error.name === "AbortError") {
                return;
            }
            showCalculationError();
        } finally {
            if (runId === requestSequence) {
                setLoadingState(false, trigger);
            }
        }
    }

    function isPayloadReady() {
        if (!projectInput || !personaInput || !materialInput || !unitInput || !needTimingInput || !quantityInput) {
            return false;
        }
        const quantity = parseFloat(quantityInput.value || "0");
        return !Number.isNaN(quantity) && quantity > 0;
    }

    function buildPayload() {
        const quantity = parseFloat(quantityInput.value || "0");
        const allowanceRaw = allowanceInput.value;
        const mixed = mixedInput.checked;
        const wet = wetInput.checked;

        return {
            projectId: projectInput.value,
            persona: personaInput.value,
            needTiming: needTimingInput.value,
            items: [
                {
                    materialId: materialInput.value,
                    quantity,
                    unitId: unitInput.value,
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
                <ul>${rec.why.map((reason) => "<li>" + reason + "</li>").join("")}</ul>
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
            <ul>${result.assumptions.map((item) => "<li>" + item + "</li>").join("")}</ul>
            <h3>Input Impact</h3>
            <ul>${result.inputImpactSummary.map((item) => "<li>" + item + "</li>").join("")}</ul>
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
            <a class="button-link" id="cta-dumpster-form" href="#estimate-form">Request online quote</a>
            <a class="button-link" id="cta-junk" href="#junk">Compare junk removal</a>
        `;

        updateLiveDashboard(result, inputPayload);
        wireResultActionTracking(apiData, inputPayload, result);
    }

    function wireResultActionTracking(apiData, inputPayload, result) {
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
        if (leadNext && leadZip && leadStep1 && leadStep2 && leadStatus) {
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

    function updateLiveDashboard(result, payload) {
        const allowance = payload.options.allowanceTons;
        const weightHigh = Number(result.weightTons.high || 0);
        const volumeHigh = Number(result.volumeYd3.high || 0);
        const topRecommendation = Array.isArray(result.recommendations) && result.recommendations.length > 0
                ? result.recommendations[0]
                : null;
        const capacityReference = topRecommendation ? Math.max(Number(topRecommendation.sizeYd || 1), 1) : 30;

        const weightPct = allowance && allowance > 0
                ? Math.min((weightHigh / allowance) * 100, 100)
                : riskToPercent(result.priceRisk);
        const volumePct = Math.min((volumeHigh / capacityReference) * 100, 100);
        const riskPct = riskToPercent(result.priceRisk);

        setGauge(gaugeWeight, gaugeWeightLabel, weightPct, `High estimate ${fmt(weightHigh)} tons`);
        setGauge(gaugeVolume, gaugeVolumeLabel, volumePct, `High estimate ${fmt(volumeHigh)} yd3`);
        setGauge(gaugeRisk, gaugeRiskLabel, riskPct, `Risk tier ${result.priceRisk}`);

        if (liveStatus) {
            const lead = topRecommendation ? `${topRecommendation.label} ${topRecommendation.sizeYd}yd` : "No recommendation";
            liveStatus.textContent = `Updated: ${lead} | Feasibility ${result.feasibility}`;
        }
    }

    function setGauge(gaugeEl, labelEl, percent, labelText) {
        if (!gaugeEl || !labelEl) {
            return;
        }
        const bounded = Math.max(4, Math.min(percent, 100));
        gaugeEl.style.width = bounded + "%";
        gaugeEl.classList.remove("warn", "danger");
        if (bounded >= 80) {
            gaugeEl.classList.add("danger");
        } else if (bounded >= 55) {
            gaugeEl.classList.add("warn");
        }
        labelEl.textContent = labelText;
    }

    function setLoadingState(isLoading, trigger) {
        if (submitButton) {
            submitButton.disabled = isLoading;
            submitButton.textContent = isLoading && trigger === "submit" ? "Calculating..." : "Calculate";
        }
        if (liveNote) {
            liveNote.textContent = isLoading
                    ? "Live update is on. Refreshing recommendation..."
                    : "Live update is on. Input changes refresh your recommendation automatically.";
        }
    }

    function showCalculationError() {
        if (resultPanel) {
            resultPanel.hidden = false;
        }
        if (resultSummary) {
            resultSummary.innerHTML = "<p class=\"warn\">Could not calculate estimate. Check inputs and retry.</p>";
        }
        if (liveStatus) {
            liveStatus.textContent = "Live update failed. Check values and retry.";
        }
    }

    function stepQuantity(direction) {
        if (!quantityInput) {
            return;
        }
        const step = parseFloat(quantityInput.step || "0.1");
        const min = parseFloat(quantityInput.min || "0.1");
        const current = parseFloat(quantityInput.value || String(min));
        const next = Math.max(min, (Number.isNaN(current) ? min : current) + (direction * step));
        quantityInput.value = next.toFixed(1).replace(/\.0$/, "");
        queueLiveEstimate();
    }

    function syncChoiceButtons(group) {
        const targetId = group.getAttribute("data-choice-target");
        const targetInput = document.getElementById(targetId);
        if (!targetInput) {
            return;
        }
        const activeValue = targetInput.value;
        Array.from(group.querySelectorAll("[data-choice-value]")).forEach((button) => {
            const value = button.getAttribute("data-choice-value");
            button.classList.toggle("is-active", value === activeValue);
        });
    }

    function syncUnitOptions() {
        if (!roofSquareChip || !materialInput || !unitInput) {
            return;
        }
        const shingles = materialInput.value === "asphalt_shingles";
        roofSquareChip.disabled = !shingles;
        if (!shingles && unitInput.value === "roof_square") {
            unitInput.value = "pickup_load";
            const group = roofSquareChip.closest("[data-choice-target]");
            if (group) {
                syncChoiceButtons(group);
            }
        }
    }

    function applyPresetFromQuery() {
        const params = new URLSearchParams(window.location.search);
        setPresetValue(projectInput, params.get("project"), "project-id");
        setPresetValue(materialInput, params.get("material"), "material-id");
        setPresetValue(personaInput, params.get("persona"), "persona");
        setPresetValue(unitInput, params.get("unit"), "unit-id");
        setPresetValue(needTimingInput, params.get("timing"), "need-timing");
        setPresetValue(needTimingInput, params.get("need_timing"), "need-timing");

        const quantityParam = params.get("qty") || params.get("quantity");
        if (quantityInput && quantityParam) {
            const parsed = parseFloat(quantityParam);
            if (!Number.isNaN(parsed) && parsed > 0) {
                quantityInput.value = parsed.toString();
            }
        }
    }

    function setPresetValue(input, value, targetId) {
        if (!input || !value) {
            return;
        }
        if (!hasChoiceOption(targetId, value)) {
            return;
        }
        input.value = value;
    }

    function hasChoiceOption(targetId, value) {
        const group = form.querySelector("[data-choice-target='" + targetId + "']");
        if (!group || !value) {
            return false;
        }
        return Array.from(group.querySelectorAll("[data-choice-value]"))
                .some((button) => button.getAttribute("data-choice-value") === value);
    }

    function riskToPercent(priceRisk) {
        if (priceRisk === "HIGH") {
            return 90;
        }
        if (priceRisk === "MEDIUM") {
            return 62;
        }
        return 34;
    }

    function badge(text, style) {
        return "<span class=\"badge " + style + "\">" + text + "</span>";
    }

    function fmt(value) {
        const numeric = Number(value);
        if (!Number.isFinite(numeric)) {
            return "0.00";
        }
        return numeric.toFixed(2);
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
        }).catch(() => {
        });
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
