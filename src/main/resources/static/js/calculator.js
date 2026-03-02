(function () {
    const form = document.getElementById("estimate-form");
    if (!form) {
        return;
    }

    const resultPanel = document.getElementById("result-panel");
    const resultStateBanner = document.getElementById("result-state-banner");
    const resultHardStops = document.getElementById("result-hard-stops");
    const resultVerdict = document.getElementById("result-verdict");
    const resultSummary = document.getElementById("result-summary");
    const resultBadges = document.getElementById("result-badges");
    const resultInputImpact = document.getElementById("result-input-impact");
    const resultRecommendations = document.getElementById("result-recommendations");
    const resultCosts = document.getElementById("result-costs");
    const resultAssumptions = document.getElementById("result-assumptions");
    const trustDrawer = document.getElementById("trust-drawer");
    const resultActions = document.getElementById("result-actions");
    const shareLink = document.getElementById("share-link");
    const submitButton = document.getElementById("submit-button");
    const floatingCta = document.getElementById("floating-cta");
    const floatingCall = document.getElementById("floating-call");
    const floatingQuote = document.getElementById("floating-quote");
    const mobileResultDock = document.getElementById("mobile-result-dock");
    const mobileResultVerdict = document.getElementById("mobile-result-verdict");
    const mobilePrimaryCta = document.getElementById("mobile-primary-cta");
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
    const heroKpiPlan = document.getElementById("hero-kpi-plan");
    const heroKpiRisk = document.getElementById("hero-kpi-risk");
    const heroKpiFeasibility = document.getElementById("hero-kpi-feasibility");
    const heroKpiCost = document.getElementById("hero-kpi-cost");
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

    const loadingNarratives = [
        "Checking material density ranges",
        "Estimating low, typical, and high weight window",
        "Comparing container options and haul feasibility"
    ];

    let liveDebounceId = null;
    let activeRequestController = null;
    let requestSequence = 0;
    const leadFormState = {
        zip: "",
        contactMethod: "",
        contactValue: "",
        step: 1,
        statusText: ""
    };

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
                        trackEvent("persona_selected", null, { persona: targetInput.value });
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
            liveNote.textContent = "Live update is on. Recalculating...";
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

        setLoadingState(true, trigger, runId);

        if (trigger === "submit") {
            trackEvent("calc_started", null, {
                projectId: payload.projectId,
                persona: payload.persona
            });
            if (payload.options.allowanceTons !== null) {
                trackEvent("allowance_entered", null, { allowanceTons: payload.options.allowanceTons });
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
                trackEvent("calc_completed_client", data.estimateId, {
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
                setLoadingState(false, trigger, runId);
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
        const recommendations = Array.isArray(result.recommendations) ? result.recommendations : [];
        const costOptions = Array.isArray(result.costComparison) ? result.costComparison : [];
        const assumptions = Array.isArray(result.assumptions) ? result.assumptions : [];
        const inputImpactSummary = Array.isArray(result.inputImpactSummary) ? result.inputImpactSummary : [];
        const hardStops = Array.isArray(result.hardStopReasons) ? result.hardStopReasons : [];

        const topRecommendation = recommendations.length > 0
            ? recommendations[0]
            : null;
        const verdictText = topRecommendation
            ? `You likely need a ${topRecommendation.sizeYd}-yard dumpster.`
            : "No recommendation returned. Check inputs and retry.";

        resultPanel.hidden = false;
        shareLink.href = "/dumpster/estimate/" + apiData.estimateId;
        if (liveNote) {
            liveNote.classList.remove("is-error");
        }

        if (resultVerdict) {
            resultVerdict.textContent = verdictText;
        }
        if (mobileResultVerdict) {
            mobileResultVerdict.textContent = verdictText;
        }
        if (mobileResultDock) {
            mobileResultDock.hidden = false;
        }

        trackEvent("result_viewed", apiData.estimateId, {
            priceRisk: result.priceRisk,
            feasibility: result.feasibility
        });
        if (result.usedAssumedAllowance) {
            trackEvent("used_assumed_allowance", apiData.estimateId, { source: "frontend_render" });
        }
        if (result.feasibility !== "OK") {
            trackEvent("feasibility_not_ok", apiData.estimateId, { feasibility: result.feasibility });
        }
        trackEvent("share_estimate_created", apiData.estimateId, { sharePath: shareLink.href });

        const primaryCtaKey = resolvePrimaryCta(result, inputPayload);
        const primaryCta = ctaConfig(primaryCtaKey);
        const secondaryCtas = ["dumpster_call", "dumpster_form", "junk_call"]
            .filter((key) => key !== primaryCtaKey)
            .map((key) => ctaConfig(key));
        const preferredContactMethod = inputPayload.needTiming === "48h" ? "phone" : "email";
        if (!leadFormState.contactMethod) {
            leadFormState.contactMethod = preferredContactMethod;
        }
        const activeContactMethod = leadFormState.contactMethod || preferredContactMethod;
        const isEmailContact = activeContactMethod === "email";

        const impactLine = inputImpactSummary.length > 0
            ? inputImpactSummary[0]
            : "No major input risk signal.";
        const feasibilityText = translateFeasibility(result.feasibility);
        const riskText = translatePriceRisk(result.priceRisk);

        if (resultStateBanner) {
            resultStateBanner.hidden = false;
            resultStateBanner.className = "result-state-banner " + stateBannerTone(result.feasibility, result.priceRisk);
            resultStateBanner.innerHTML = `
                <strong>${feasibilityText}</strong>
                <p>${riskText}</p>
            `;
        }

        if (resultHardStops) {
            if (hardStops.length > 0) {
                resultHardStops.hidden = false;
                resultHardStops.innerHTML = `
                    <strong>Hard stops</strong>
                    <ul>${hardStops.map((reason) => "<li>" + reason + "</li>").join("")}</ul>
                `;
            } else {
                resultHardStops.hidden = true;
                resultHardStops.innerHTML = "";
            }
        }

        resultBadges.innerHTML = [
            badge("Risk: " + result.priceRisk, toneForRisk(result.priceRisk)),
            badge("Feasibility: " + result.feasibility, result.feasibility === "OK" ? "ok" : "warn"),
            result.usedAssumedAllowance ? badge("Allowance assumed", "warn") : badge("Allowance provided", "ok"),
            result.heavyDebrisWarning ? badge("Heavy debris policy active", "warn") : ""
        ].join("");

        if (resultInputImpact) {
            if (inputImpactSummary.length > 0) {
                resultInputImpact.hidden = false;
                resultInputImpact.innerHTML = `<ul>${inputImpactSummary.map((item) => "<li>" + item + "</li>").join("")}</ul>`;
            } else {
                resultInputImpact.hidden = true;
                resultInputImpact.innerHTML = "";
            }
        }

        resultSummary.innerHTML = `
            <article class="stat">
                <h3>Verdict</h3>
                <p>${topRecommendation ? topRecommendation.label + " " + topRecommendation.sizeYd + "yd" : "Pending"}</p>
            </article>
            <article class="stat">
                <h3>Volume range</h3>
                <p>${fmt(result.volumeYd3.low)} - ${fmt(result.volumeYd3.high)} yd3</p>
            </article>
            <article class="stat">
                <h3>Weight range</h3>
                <p>${fmt(result.weightTons.low)} - ${fmt(result.weightTons.high)} tons</p>
            </article>
            <article class="stat">
                <h3>Input impact</h3>
                <p>${impactLine}</p>
            </article>
        `;

        resultRecommendations.innerHTML = recommendations.map((rec) => `
            <article class="result-card ${recTone(rec.label)}">
                <h3>${rec.label}: ${rec.sizeYd}yd</h3>
                <p>${translateRisk(rec.risk)} / ${translateFeasibility(rec.feasibility)}</p>
                <p>${rec.multiHaul ? "Conditional fit: plan " + rec.haulCount + " hauls." : "Good for a single-haul scenario."}</p>
                <ul>${(Array.isArray(rec.why) ? rec.why : []).map((reason) => "<li>" + reason + "</li>").join("")}</ul>
            </article>
        `).join("");

        resultCosts.innerHTML = costOptions.map((cost) => `
            <article class="result-card">
                <h3>${cost.title}</h3>
                <p>${cost.summary}</p>
                <p>${cost.available ? "$" + fmt(cost.estimatedTotalCostUsd.low) + " - $" + fmt(cost.estimatedTotalCostUsd.high) : "Unavailable"}</p>
                <p>${cost.available ? "Use as a quick compare line." : "Availability depends on market and constraints."}</p>
            </article>
        `).join("");

        resultAssumptions.innerHTML = `
            <h3>Assumptions</h3>
            <ul>${assumptions.map((item) => "<li>" + item + "</li>").join("")}</ul>
            <h3>Input impact summary</h3>
            <ul>${inputImpactSummary.map((item) => "<li>" + item + "</li>").join("")}</ul>
            <h3>Source and engine meta</h3>
            <ul>
                <li>Engine version: ${escapeHtml(result.calcEngineVersion || "unknown")}</li>
                <li>Data version: ${escapeHtml(result.dataVersion || "unknown")}</li>
            </ul>
        `;
        if (trustDrawer) {
            trustDrawer.open = false;
        }

        resultActions.innerHTML = `
            <section class="lead-capture">
                <h3>Request local quotes</h3>
                <div class="lead-step" id="lead-step-1" ${leadFormState.step === 2 ? "hidden" : ""}>
                    <label for="lead-zip">ZIP code</label>
                    <input id="lead-zip" type="text" inputmode="numeric" maxlength="5" placeholder="e.g. 30339" value="${escapeHtml(leadFormState.zip)}">
                    <button type="button" id="lead-next">Next</button>
                </div>
                <div class="lead-step" id="lead-step-2" ${leadFormState.step === 2 ? "" : "hidden"}>
                    <label for="lead-contact-method">Contact preference</label>
                    <select id="lead-contact-method">
                        <option value="phone" ${activeContactMethod === "phone" ? "selected" : ""}>Phone</option>
                        <option value="email" ${activeContactMethod === "email" ? "selected" : ""}>Email</option>
                    </select>
                    <label for="lead-contact-value" id="lead-contact-label">${isEmailContact ? "Email" : "Phone number"}</label>
                    <input id="lead-contact-value" type="${isEmailContact ? "email" : "tel"}" placeholder="${isEmailContact ? "name@company.com" : "(555) 555-5555"}" value="${escapeHtml(leadFormState.contactValue)}">
                    <button type="button" id="lead-submit">Submit lead</button>
                </div>
                <p class="lead-hint" id="lead-status" aria-live="polite">${escapeHtml(leadFormState.statusText)}</p>
            </section>
            <a class="ui-button ui-button--primary result-primary-cta" id="${primaryCta.id}" href="${primaryCta.href}">${primaryCta.label}</a>
            <div class="result-secondary-links">
                ${secondaryCtas.map((item) => `<a class="ui-button ui-button--ghost" id="${item.id}" href="${item.href}">${item.label}</a>`).join("")}
            </div>
            ${inputPayload.persona === "contractor" ? '<p class="lead-hint">Contractor mode: share this estimate with your crew or vendor.</p>' : ""}
        `;

        if (mobilePrimaryCta) {
            mobilePrimaryCta.href = primaryCta.href;
            mobilePrimaryCta.textContent = primaryCta.label;
        }

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
            const zip = sanitizeZip(leadZip.value || leadFormState.zip);
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

        if (leadZip) {
            if (leadFormState.zip && !leadZip.value) {
                leadZip.value = leadFormState.zip;
            }
            leadZip.addEventListener("input", () => {
                leadFormState.zip = sanitizeZip(leadZip.value);
            });
        }

        if (leadContactMethod) {
            if (!leadContactMethod.value) {
                leadContactMethod.value = leadFormState.contactMethod || (prefersPhone ? "phone" : "email");
            }
            leadFormState.contactMethod = leadContactMethod.value;
            updateLeadContactField(leadContactMethod, leadContactValue, leadContactLabel);
            leadContactMethod.addEventListener("change", () => {
                leadFormState.contactMethod = leadContactMethod.value;
                updateLeadContactField(leadContactMethod, leadContactValue, leadContactLabel);
            });
        }

        if (leadContactValue) {
            if (leadFormState.contactValue && !leadContactValue.value) {
                leadContactValue.value = leadFormState.contactValue;
            }
            leadContactValue.addEventListener("input", () => {
                leadFormState.contactValue = (leadContactValue.value || "").trim();
            });
        }

        if (leadNext && leadZip && leadStep1 && leadStep2 && leadStatus) {
            leadNext.addEventListener("click", () => {
                const zip = sanitizeZip(leadZip.value);
                if (!isValidZip(zip)) {
                    leadStatus.textContent = "Enter a valid 5-digit ZIP.";
                    leadFormState.statusText = leadStatus.textContent;
                    return;
                }
                leadStatus.textContent = "";
                leadFormState.statusText = "";
                leadFormState.zip = zip;
                leadFormState.step = 2;
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
                    leadStatus.textContent = "Enter a valid 5-digit ZIP.";
                    leadFormState.statusText = leadStatus.textContent;
                    return;
                }
                if (contact === "") {
                    leadStatus.textContent = "Enter your contact information.";
                    leadFormState.statusText = leadStatus.textContent;
                    return;
                }
                leadFormState.zip = zip;
                leadFormState.contactMethod = leadContactMethod.value;
                leadFormState.contactValue = contact;
                emitLeadSubmitted("lead_form_submit");
                leadStatus.textContent = "Lead submitted. A quote partner can contact you next.";
                leadFormState.statusText = leadStatus.textContent;
            });
        }

        if (floatingCta) {
            floatingCta.hidden = false;
        }
        if (floatingCall) {
            floatingCall.onclick = () => trackEvent("cta_click_dumpster_call", apiData.estimateId, { source: "floating" });
        }
        if (floatingQuote) {
            floatingQuote.onclick = () => trackEvent("cta_click_dumpster_form", apiData.estimateId, { source: "floating" });
        }

        if (dumpsterCall) {
            dumpsterCall.addEventListener("click", () => {
                trackEvent("cta_click_dumpster_call", apiData.estimateId, {});
                emitLeadSubmitted("cta_dumpster_call");
                if (inputPayload.needTiming === "48h") {
                    trackEvent("call_qualified", apiData.estimateId, { source: "call_click_proxy" });
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
        if (heroKpiPlan) {
            heroKpiPlan.textContent = topRecommendation ? `${topRecommendation.label} ${topRecommendation.sizeYd}yd` : "No recommendation";
            heroKpiPlan.dataset.tone = "neutral";
        }
        if (heroKpiRisk) {
            heroKpiRisk.textContent = result.priceRisk || "Unknown";
            heroKpiRisk.dataset.tone = riskTone(result.priceRisk);
        }
        if (heroKpiFeasibility) {
            heroKpiFeasibility.textContent = result.feasibility || "Unknown";
            heroKpiFeasibility.dataset.tone = result.feasibility === "OK" ? "ok" : "warn";
        }
        if (heroKpiCost) {
            heroKpiCost.textContent = extractCostWindow(result.costComparison);
            heroKpiCost.dataset.tone = "neutral";
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

    function setLoadingState(isLoading, trigger, runId) {
        const narrative = loadingNarratives[(runId - 1) % loadingNarratives.length];

        if (form) {
            form.setAttribute("aria-busy", isLoading ? "true" : "false");
        }
        if (submitButton) {
            submitButton.disabled = isLoading;
            submitButton.textContent = isLoading && trigger === "submit" ? "Calculating..." : "Calculate";
        }
        if (liveNote) {
            liveNote.classList.toggle("is-loading", isLoading);
            liveNote.textContent = isLoading
                ? `Calculating: ${narrative}`
                : "Live update is on. Any change refreshes the recommendation.";
        }
        if (liveStatus && isLoading) {
            liveStatus.textContent = narrative;
        }
    }

    function showCalculationError() {
        if (resultPanel) {
            resultPanel.hidden = false;
        }
        if (resultStateBanner) {
            resultStateBanner.hidden = true;
            resultStateBanner.innerHTML = "";
        }
        if (resultHardStops) {
            resultHardStops.hidden = true;
            resultHardStops.innerHTML = "";
        }
        if (resultInputImpact) {
            resultInputImpact.hidden = true;
            resultInputImpact.innerHTML = "";
        }
        if (resultVerdict) {
            resultVerdict.textContent = "Estimate failed. Check inputs and retry.";
        }
        if (resultSummary) {
            resultSummary.innerHTML = '<p class="warn">Estimate failed. Check inputs and retry.</p>';
        }
        if (liveStatus) {
            liveStatus.textContent = "Live update failed. Check values and retry.";
        }
        if (liveNote) {
            liveNote.classList.remove("is-loading");
            liveNote.classList.add("is-error");
            liveNote.textContent = "Live update failed. Verify inputs and retry.";
        }
        if (mobileResultVerdict) {
            mobileResultVerdict.textContent = "Live update failed. Verify inputs and retry.";
        }
        if (mobileResultDock) {
            mobileResultDock.hidden = false;
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

    function riskTone(priceRisk) {
        if (priceRisk === "HIGH") {
            return "danger";
        }
        if (priceRisk === "MEDIUM") {
            return "warn";
        }
        if (priceRisk === "LOW") {
            return "ok";
        }
        return "neutral";
    }

    function toneForRisk(priceRisk) {
        if (priceRisk === "HIGH") {
            return "danger";
        }
        if (priceRisk === "MEDIUM") {
            return "warn";
        }
        return "neutral";
    }

    function translateRisk(risk) {
        if (String(risk).toUpperCase() === "LOW") {
            return "Lower overage risk";
        }
        if (String(risk).toUpperCase() === "MEDIUM") {
            return "Allowance may be crossed";
        }
        return "Overage likely under current assumptions";
    }

    function translateFeasibility(feasibility) {
        if (String(feasibility).toUpperCase() === "OK") {
            return "Operationally feasible";
        }
        if (String(feasibility).toUpperCase().includes("MULTI")) {
            return "Likely needs multiple hauls";
        }
        return "Not recommended as a single load";
    }

    function translatePriceRisk(priceRisk) {
        const risk = String(priceRisk || "").toUpperCase();
        if (risk === "LOW") {
            return "Overage unlikely under current assumptions.";
        }
        if (risk === "MEDIUM") {
            return "Allowance may be crossed depending on mix or moisture.";
        }
        if (risk === "HIGH") {
            return "Overage likely; compare junk removal or staged multi-haul.";
        }
        return "Risk signal unavailable for this scenario.";
    }

    function stateBannerTone(feasibility, priceRisk) {
        if (String(feasibility).toUpperCase() !== "OK") {
            return "is-danger";
        }
        if (String(priceRisk).toUpperCase() === "HIGH") {
            return "is-warn";
        }
        return "is-ok";
    }

    function resolvePrimaryCta(result, inputPayload) {
        const feasibility = String(result.feasibility || "").toUpperCase();
        const risk = String(result.priceRisk || "").toUpperCase();
        const timing = String(inputPayload.needTiming || "").toLowerCase();

        if (feasibility !== "OK") {
            return "junk_call";
        }
        if (timing === "48h") {
            return "dumpster_call";
        }
        if (risk === "HIGH") {
            return "junk_call";
        }
        const routed = normalizeCtaKey(result.ctaRouting && result.ctaRouting.primaryCta);
        return routed || "dumpster_call";
    }

    function normalizeCtaKey(value) {
        const key = String(value || "").toLowerCase();
        if (key === "dumpster_call" || key === "dumpster_form" || key === "junk_call") {
            return key;
        }
        return "";
    }

    function ctaConfig(key) {
        if (key === "dumpster_form") {
            return { id: "cta-dumpster-form", href: "#estimate-form", label: "Request online quote" };
        }
        if (key === "junk_call") {
            return { id: "cta-junk", href: "#junk", label: "Compare junk removal" };
        }
        return { id: "cta-dumpster-call", href: "/about/contact", label: "Contact for quote" };
    }

    function badge(text, style) {
        return '<span class="badge ' + style + '">' + text + '</span>';
    }

    function fmt(value) {
        const numeric = Number(value);
        if (!Number.isFinite(numeric)) {
            return "0.00";
        }
        return numeric.toFixed(2);
    }

    function extractCostWindow(costComparison) {
        if (!Array.isArray(costComparison)) {
            return "Unavailable";
        }
        const available = costComparison.find((item) => item && item.available && item.estimatedTotalCostUsd);
        if (!available) {
            return "Unavailable";
        }
        const low = Number(available.estimatedTotalCostUsd.low);
        const high = Number(available.estimatedTotalCostUsd.high);
        if (!Number.isFinite(low) || !Number.isFinite(high)) {
            return "Unavailable";
        }
        return `$${fmt(low)} - $${fmt(high)}`;
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

    function escapeHtml(value) {
        return String(value || "")
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/\"/g, "&quot;")
            .replace(/'/g, "&#39;");
    }
})();
