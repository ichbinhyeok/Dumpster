(function () {
    const QUOTE_MATCH_BETA_PATH = "/about/quote-match-beta";
    const QUOTE_MATCH_INTAKE_ENDPOINT = "/api/quote-match/intakes";
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
    const resultPrecalc = document.getElementById("result-precalc");
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
    const marketZipInput = document.getElementById("market-zip");
    const wetInput = document.getElementById("wet");
    const mixedInput = document.getElementById("mixed-load");
    const quantityInc = document.getElementById("quantity-inc");
    const quantityDec = document.getElementById("quantity-dec");
    const liveNote = document.getElementById("live-note");
    const liveStatus = document.getElementById("live-status");
    const gaugeDashboard = document.getElementById("gauge-dashboard");
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
    const decisionPriorityInput = document.getElementById("decision-priority");
    const quickModeButton = document.getElementById("quick-mode-button");
    const advancedModeButton = document.getElementById("advanced-mode-button");
    const advancedMaterialControls = document.getElementById("advanced-material-controls");
    const advancedContextStep = document.getElementById("advanced-context-step");
    const estimateModeNote = document.getElementById("estimate-mode-note");
    const openAdvancedModeButton = document.getElementById("open-advanced-mode");
    const addMaterialLine2Button = document.getElementById("add-material-line-2");
    const addMaterialLine3Button = document.getElementById("add-material-line-3");
    const removeMaterialLine2Button = document.getElementById("remove-material-line-2");
    const removeMaterialLine3Button = document.getElementById("remove-material-line-3");
    const materialLine2 = document.getElementById("material-line-2");
    const materialLine3 = document.getElementById("material-line-3");
    const materialInput2 = document.getElementById("material-id-2");
    const materialInput3 = document.getElementById("material-id-3");
    const unitInput2 = document.getElementById("unit-id-2");
    const unitInput3 = document.getElementById("unit-id-3");
    const quantityInput2 = document.getElementById("quantity-2");
    const quantityInput3 = document.getElementById("quantity-3");
    const choiceGroups = Array.from(form.querySelectorAll("[data-choice-target]"));
    const decisionModeLinks = Array.from(document.querySelectorAll("[data-decision-mode]"));
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
    const GA_EVENT_MAP = {
        calc_started: "calculator_start",
        calc_completed_client: "calculator_complete",
        cta_click_dumpster_call: "quote_cta_click",
        cta_click_dumpster_form: "quote_cta_click",
        cta_click_junk_call: "junk_compare_cta_click",
        lead_submitted: "lead_submitted"
    };

    const loadingNarratives = [
        "Checking material density ranges",
        "Estimating low, typical, and high weight window",
        "Comparing container options and haul feasibility"
    ];

    const unitHelperText = document.getElementById("unit-helper-text");
    const unitHints = {
        "pickup_load": "Standard 8ft pickup truck bed, level full (~2.5 yd3).",
        "truckload_small": "Small box truck or large trailer (~10 yd3).",
        "roof_square": "100 sq ft of roofing area. Include layers in quantity.",
        "sqft_4in": "Square footage of 4-inch deep concrete/dirt.",
        "sqft_2in": "Square footage of 2-inch deep material.",
        "sqft_1in": "Square footage of 1-inch deep material.",
        "contractor_bag": "42-gallon heavy-duty contractor trash bag.",
        "drywall_sheet": "Standard 4ft x 8ft drywall sheet (1/2\" to 5/8\").",
        "cubic_yard": "3ft x 3ft x 3ft. Roughly the size of a washing machine."
    };

    let liveDebounceId = null;
    let activeRequestController = null;
    let requestSequence = 0;
    let hasRenderedResult = false;
    let estimateMode = "quick";
    const leadFormState = {
        zip: "",
        contactMethod: "",
        contactValue: "",
        step: 1,
        statusText: ""
    };

    applyPresetFromQuery();
    bindDecisionStripTracking();
    initializeChoiceGroups();
    syncUnitOptions();
    bindStepperControls();
    bindFormControls();
    bindAdditionalMaterialControls();
    bindEstimateModeControls();
    setEstimateMode(estimateMode, "init");
    setResultRailState(false);
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
                    if (targetId === "unit-id" && unitHelperText) {
                        unitHelperText.textContent = unitHints[targetInput.value] || "";
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
        const liveInputs = [quantityInput, allowanceInput, marketZipInput, wetInput, mixedInput];
        liveInputs.forEach((input) => {
            if (!input) {
                return;
            }
            input.addEventListener("input", () => queueLiveEstimate());
            input.addEventListener("change", () => queueLiveEstimate());
        });
        if (marketZipInput) {
            marketZipInput.addEventListener("blur", () => {
                const zip = sanitizeZip(marketZipInput.value);
                marketZipInput.value = zip;
                if (isValidZip(zip)) {
                    trackEvent("market_zip_entered", null, { zipPrefix3: zip.slice(0, 3) });
                }
            });
        }
    }

    function bindAdditionalMaterialControls() {
        const extraInputs = [materialInput2, unitInput2, quantityInput2, materialInput3, unitInput3, quantityInput3];
        extraInputs.forEach((input) => {
            if (!input) {
                return;
            }
            input.addEventListener("input", () => queueLiveEstimate());
            input.addEventListener("change", () => {
                syncAdditionalUnitOptions();
                queueLiveEstimate();
            });
        });
        if (addMaterialLine2Button && materialLine2) {
            addMaterialLine2Button.addEventListener("click", () => {
                materialLine2.hidden = false;
                addMaterialLine2Button.hidden = true;
                trackEvent("multi_material_line_added", null, { line: 2 });
                queueLiveEstimate();
            });
        }
        if (addMaterialLine3Button && materialLine3) {
            addMaterialLine3Button.addEventListener("click", () => {
                materialLine3.hidden = false;
                addMaterialLine3Button.hidden = true;
                trackEvent("multi_material_line_added", null, { line: 3 });
                queueLiveEstimate();
            });
        }
        if (removeMaterialLine2Button && materialLine2) {
            removeMaterialLine2Button.addEventListener("click", () => {
                materialLine2.hidden = true;
                if (materialLine3) {
                    materialLine3.hidden = true;
                }
                if (addMaterialLine2Button) {
                    addMaterialLine2Button.hidden = false;
                }
                if (addMaterialLine3Button) {
                    addMaterialLine3Button.hidden = false;
                }
                if (materialInput2) {
                    materialInput2.value = "";
                }
                if (quantityInput2) {
                    quantityInput2.value = "2";
                }
                if (materialInput3) {
                    materialInput3.value = "";
                }
                if (quantityInput3) {
                    quantityInput3.value = "1";
                }
                trackEvent("multi_material_line_removed", null, { line: 2 });
                syncAdditionalUnitOptions();
                queueLiveEstimate();
            });
        }
        if (removeMaterialLine3Button && materialLine3) {
            removeMaterialLine3Button.addEventListener("click", () => {
                materialLine3.hidden = true;
                if (addMaterialLine3Button) {
                    addMaterialLine3Button.hidden = false;
                }
                if (materialInput3) {
                    materialInput3.value = "";
                }
                if (quantityInput3) {
                    quantityInput3.value = "1";
                }
                trackEvent("multi_material_line_removed", null, { line: 3 });
                syncAdditionalUnitOptions();
                queueLiveEstimate();
            });
        }
        syncAdditionalUnitOptions();
    }

    function bindEstimateModeControls() {
        if (quickModeButton) {
            quickModeButton.addEventListener("click", () => {
                setEstimateMode("quick", "toggle");
                queueLiveEstimate();
            });
        }
        if (advancedModeButton) {
            advancedModeButton.addEventListener("click", () => {
                setEstimateMode("advanced", "toggle");
                queueLiveEstimate();
            });
        }
        if (openAdvancedModeButton) {
            openAdvancedModeButton.addEventListener("click", () => {
                setEstimateMode("advanced", "result_refine");
                if (advancedContextStep) {
                    advancedContextStep.scrollIntoView({ behavior: "smooth", block: "start" });
                }
            });
        }
    }

    function setEstimateMode(mode, source) {
        const nextMode = mode === "advanced" ? "advanced" : "quick";
        const changed = estimateMode !== nextMode;
        estimateMode = nextMode;

        const advancedEnabled = estimateMode === "advanced";
        if (advancedMaterialControls) {
            advancedMaterialControls.hidden = !advancedEnabled;
        }
        if (advancedContextStep) {
            advancedContextStep.hidden = !advancedEnabled;
        }

        if (estimateModeNote) {
            estimateModeNote.textContent = advancedEnabled
                ? "Advanced mode includes extra materials, timing, and assumption modifiers."
                : "Quick mode uses project + material + quantity for the fastest answer.";
        }

        if (quickModeButton) {
            quickModeButton.classList.toggle("is-active", !advancedEnabled);
        }
        if (advancedModeButton) {
            advancedModeButton.classList.toggle("is-active", advancedEnabled);
        }
        if (openAdvancedModeButton) {
            openAdvancedModeButton.hidden = !hasRenderedResult || advancedEnabled;
        }
        if (changed && source !== "init") {
            trackEvent("estimate_mode_changed", null, {
                mode: estimateMode,
                source
            });
        }
    }

    function syncAdditionalUnitOptions() {
        syncRoofSquareCompatibility(materialInput2, unitInput2);
        syncRoofSquareCompatibility(materialInput3, unitInput3);
    }

    function syncRoofSquareCompatibility(materialField, unitField) {
        if (!materialField || !unitField) {
            return;
        }
        const roofSquareOption = Array.from(unitField.options || []).find((option) => option.value === "roof_square");
        if (!roofSquareOption) {
            return;
        }
        const shingles = materialField.value === "asphalt_shingles";
        roofSquareOption.disabled = !shingles;
        if (!shingles && unitField.value === "roof_square") {
            unitField.value = "pickup_load";
        }
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
        const advancedEnabled = estimateMode === "advanced";
        const quantity = parseFloat(quantityInput.value || "0");
        const allowanceRaw = advancedEnabled && allowanceInput ? allowanceInput.value : "";
        const marketZip = sanitizeZip(advancedEnabled && marketZipInput ? marketZipInput.value : "");
        const mixed = advancedEnabled && mixedInput ? mixedInput.checked : false;
        const wet = advancedEnabled && wetInput ? wetInput.checked : false;
        const items = [
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
        ];
        if (advancedEnabled) {
            appendAdditionalItem(items, materialLine2, materialInput2, quantityInput2, unitInput2, wet, mixed);
            appendAdditionalItem(items, materialLine3, materialInput3, quantityInput3, unitInput3, wet, mixed);
        }

        return {
            projectId: projectInput.value,
            persona: personaInput.value,
            needTiming: needTimingInput.value,
            items,
            options: {
                mixedLoad: mixed,
                allowanceTons: allowanceRaw === "" ? null : parseFloat(allowanceRaw),
                bulkingFactor: 1.2,
                zipCode: advancedEnabled && isValidZip(marketZip) ? marketZip : null
            }
        };
    }

    function appendAdditionalItem(items, lineElement, materialField, qtyField, unitField, wet, mixed) {
        if (!lineElement || lineElement.hidden) {
            return;
        }
        if (!materialField || !qtyField || !unitField) {
            return;
        }
        const materialId = (materialField.value || "").trim();
        const unitId = (unitField.value || "").trim();
        const quantity = parseFloat(qtyField.value || "0");
        if (!materialId || !unitId || Number.isNaN(quantity) || quantity <= 0) {
            return;
        }
        if (items.length >= 3) {
            return;
        }
        items.push({
            materialId,
            quantity,
            unitId,
            conditions: {
                wet,
                mixedLoad: mixed,
                compaction: "MEDIUM"
            }
        });
    }

    function setResultRailState(hasResult) {
        hasRenderedResult = hasResult;
        if (resultPrecalc) {
            resultPrecalc.hidden = hasResult;
        }
        if (gaugeDashboard) {
            gaugeDashboard.hidden = !hasResult;
        }
        if (resultPanel) {
            resultPanel.hidden = !hasResult;
        }
        if (mobileResultDock) {
            mobileResultDock.hidden = !hasResult;
        }
        if (openAdvancedModeButton) {
            openAdvancedModeButton.hidden = !hasResult || estimateMode === "advanced";
        }
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
        const primaryCtaKey = resolvePrimaryCta(result, inputPayload);
        const primaryCta = ctaConfig(primaryCtaKey);
        const secondaryCtas = ["dumpster_call", "dumpster_form", "junk_call"]
            .filter((key) => key !== primaryCtaKey)
            .map((key) => ctaConfig(key));
        const decisionMode = deriveDecisionMode(primaryCtaKey, topRecommendation, result);
        const verdictText = decisionHeadline(primaryCtaKey, topRecommendation, result);
        const bestMoveDetail = decisionDetail(primaryCtaKey, topRecommendation, result);
        const junkSmartWhen = junkSmartWhenText(primaryCtaKey, result, inputPayload);
        const localPriceRange = extractCostWindow(costOptions);
        const activePriorityMode = currentDecisionPriority();
        const decisionScores = buildDecisionScores(
            result,
            inputPayload,
            topRecommendation,
            primaryCtaKey,
            activePriorityMode
        );

        setResultRailState(true);
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

        trackEvent("decision_mode_selected", apiData.estimateId, {
            mode: decisionMode,
            primaryCta: primaryCtaKey,
            priorityMode: decisionScores.priorityMode,
            priceRisk: result.priceRisk,
            feasibility: result.feasibility
        });
        trackEvent("decision_scorecard_rendered", apiData.estimateId, {
            cost: decisionScores.cost,
            speed: decisionScores.speed,
            effort: decisionScores.effort,
            safety: decisionScores.safety,
            priorityMode: decisionScores.priorityMode
        });
        if (decisionScores.priorityMode !== "balanced") {
            trackEvent("decision_priority_applied", apiData.estimateId, {
                mode: decisionScores.priorityMode,
                source: "query_param"
            });
        }
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
            badge("Priority: " + decisionPriorityLabel(decisionScores.priorityMode), decisionScores.priorityMode === "balanced" ? "neutral" : "ok"),
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
                <h3>Best next move</h3>
                <p>${verdictText}</p>
            </article>
            <article class="stat">
                <h3>Why this route wins</h3>
                <p>${bestMoveDetail}</p>
            </article>
            <article class="stat">
                <h3>Local price range</h3>
                <p>${localPriceRange}</p>
            </article>
            <article class="stat">
                <h3>When junk is smarter</h3>
                <p>${junkSmartWhen}</p>
            </article>
            <article class="stat">
                <h3>Input impact</h3>
                <p>${impactLine}</p>
            </article>
            <article class="stat stat--full decision-scorecard">
                <h3>Decision scorecard</h3>
                <p class="lead-hint" style="margin:0 0 0.75rem 0;">Priority mode: ${decisionPriorityLabel(decisionScores.priorityMode)}</p>
                <div class="decision-score-grid">
                    ${decisionScoreRow("Cost route", decisionScores.cost, decisionScoreNote("cost", decisionScores.cost))}
                    ${decisionScoreRow("Speed route", decisionScores.speed, decisionScoreNote("speed", decisionScores.speed))}
                    ${decisionScoreRow("Labor effort", decisionScores.effort, decisionScoreNote("effort", decisionScores.effort))}
                    ${decisionScoreRow("Safety margin", decisionScores.safety, decisionScoreNote("safety", decisionScores.safety))}
                </div>
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
                <p>${cost.available ? "Use this to choose your next route, not just price-shop." : "Availability depends on market and constraints."}</p>
                ${Array.isArray(cost.notes) && cost.notes.length > 0
                ? `<ul>${cost.notes.map((note) => "<li>" + note + "</li>").join("")}</ul>`
                : ""}
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
                <h3>Get matched after you confirm the safer route</h3>
                <p class="lead-hint" style="margin-top:-0.5rem; margin-bottom:1rem; color:var(--text-ok);">Beta queue for local matching. No spam.</p>
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
                <a class="ui-button ui-button--ghost" id="cta-heavy-rules" href="/dumpster/heavy-debris-rules">Check heavy-load rules first</a>
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
        const heavyRules = document.getElementById("cta-heavy-rules");
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
        const emitContentGateEvent = (status, detail) => {
            trackEvent(status === "pass" ? "content_gate_pass" : "content_gate_fail", apiData.estimateId, {
                step: detail.step,
                reason: detail.reason,
                status,
                contactMethod: leadContactMethod ? leadContactMethod.value : "",
                needTiming: inputPayload.needTiming
            });
        };

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
                    emitContentGateEvent("fail", { step: "zip", reason: "invalid_zip" });
                    return;
                }
                leadStatus.textContent = "";
                leadFormState.statusText = "";
                leadFormState.zip = zip;
                leadFormState.step = 2;
                emitContentGateEvent("pass", { step: "zip", reason: "valid_zip" });
                leadStep1.hidden = true;
                leadStep2.hidden = false;
                if (leadContactValue) {
                    leadContactValue.focus();
                }
            });
        }

        if (leadSubmit && leadZip && leadContactMethod && leadContactValue && leadStatus) {
            leadSubmit.addEventListener("click", async () => {
                const zip = sanitizeZip(leadZip.value);
                const contact = (leadContactValue.value || "").trim();
                if (!isValidZip(zip)) {
                    leadStatus.textContent = "Enter a valid 5-digit ZIP.";
                    leadFormState.statusText = leadStatus.textContent;
                    emitContentGateEvent("fail", { step: "submit", reason: "invalid_zip" });
                    return;
                }
                if (contact === "") {
                    leadStatus.textContent = "Enter your contact information.";
                    leadFormState.statusText = leadStatus.textContent;
                    emitContentGateEvent("fail", { step: "submit", reason: "missing_contact" });
                    return;
                }
                leadFormState.zip = zip;
                leadFormState.contactMethod = leadContactMethod.value;
                leadFormState.contactValue = contact;
                emitContentGateEvent("pass", { step: "submit", reason: "lead_ready" });
                emitLeadSubmitted("lead_form_submit");
                leadSubmit.disabled = true;
                leadStatus.textContent = "Submitting to quote-match beta queue...";
                leadFormState.statusText = leadStatus.textContent;
                const intake = await createQuoteMatchIntake(apiData, inputPayload, result, {
                    zipCode: zip,
                    contactMethod: leadContactMethod.value,
                    contactValue: contact
                });
                leadSubmit.disabled = false;
                if (!intake) {
                    leadStatus.textContent = "Submitted locally. Queue write failed, please retry once.";
                    leadFormState.statusText = leadStatus.textContent;
                    emitContentGateEvent("fail", { step: "submit", reason: "intake_write_failed" });
                    return;
                }
                leadStatus.textContent = "Queued: " + intake.statusLabel + " (" + intake.intakeId
                        + "). " + intake.expectedResponseWindow + ".";
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
        if (heavyRules) {
            heavyRules.addEventListener("click", () => {
                trackEvent("cta_click_heavy_rules", apiData.estimateId, {
                    source: "result_secondary"
                });
            });
        }
    }

    function bindDecisionStripTracking() {
        if (!decisionModeLinks.length) {
            return;
        }
        decisionModeLinks.forEach((link) => {
            link.addEventListener("click", () => {
                const priorityMode = parsePriorityFromHref(link.getAttribute("href"));
                if (decisionPriorityInput) {
                    decisionPriorityInput.value = priorityMode;
                }
                trackEvent("decision_mode_selected", null, {
                    mode: link.getAttribute("data-decision-mode") || "unsure",
                    priorityMode,
                    source: "entry_strip"
                });
            });
        });
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

        setGauge(gaugeWeight, gaugeWeightLabel, weightPct, `${fmt(weightHigh)}<br><span style='font-size:0.6rem;font-weight:600;color:var(--text-muted)'>tons</span>`);
        setGauge(gaugeVolume, gaugeVolumeLabel, volumePct, `${fmt(volumeHigh)}<br><span style='font-size:0.6rem;font-weight:600;color:var(--text-muted)'>yd3</span>`);
        setGauge(gaugeRisk, gaugeRiskLabel, riskPct, `${result.priceRisk || 'UNK'}`);

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
        const bounded = Math.max(0, Math.min(percent, 100));

        // Circular gauge math (r=40, circum = 2 * pi * 40 = ~251.2)
        const circumference = 251.2;
        const offset = circumference - (bounded / 100) * circumference;
        gaugeEl.style.strokeDashoffset = offset;

        gaugeEl.classList.remove("warn", "danger");
        if (bounded >= 80) {
            gaugeEl.classList.add("danger");
        } else if (bounded >= 55) {
            gaugeEl.classList.add("warn");
        }
        labelEl.innerHTML = labelText;
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
        if (!hasRenderedResult) {
            if (liveStatus) {
                liveStatus.textContent = "Live update failed. Adjust inputs and retry.";
            }
            if (liveNote) {
                liveNote.classList.remove("is-loading");
                liveNote.classList.add("is-error");
                liveNote.textContent = "Live update failed. Verify inputs and retry.";
            }
            return;
        }

        setResultRailState(true);
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
        if (unitHelperText) {
            unitHelperText.textContent = unitHints[unitInput.value] || "";
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
        const priorityParam = params.get("priority") || params.get("decision_priority");
        if (decisionPriorityInput) {
            decisionPriorityInput.value = normalizePriorityMode(priorityParam || decisionPriorityInput.value);
        }

        const quantityParam = params.get("qty") || params.get("quantity");
        if (quantityInput && quantityParam) {
            const parsed = parseFloat(quantityParam);
            if (!Number.isNaN(parsed) && parsed > 0) {
                quantityInput.value = parsed.toString();
            }
        }

        const zipParam = params.get("zip") || params.get("zip_code");
        if (marketZipInput && zipParam) {
            marketZipInput.value = sanitizeZip(zipParam);
        }
        estimateMode = params.get("mode") === "advanced" ? "advanced" : "quick";
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

    function parsePriorityFromHref(href) {
        if (!href) {
            return currentDecisionPriority();
        }
        try {
            const url = new URL(href, window.location.origin);
            return normalizePriorityMode(url.searchParams.get("priority"));
        } catch (_) {
            return currentDecisionPriority();
        }
    }

    function normalizePriorityMode(value) {
        const mode = String(value || "").toLowerCase();
        if (mode === "cost" || mode === "speed" || mode === "labor" || mode === "heavy") {
            return mode;
        }
        return "balanced";
    }

    function currentDecisionPriority() {
        if (!decisionPriorityInput) {
            return "balanced";
        }
        return normalizePriorityMode(decisionPriorityInput.value);
    }

    function decisionPriorityLabel(mode) {
        if (mode === "cost") {
            return "Lowest cost";
        }
        if (mode === "speed") {
            return "Fastest completion";
        }
        if (mode === "labor") {
            return "Least effort";
        }
        if (mode === "heavy") {
            return "Heavy-load safety";
        }
        return "Balanced";
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
            return { id: "cta-dumpster-form", href: "#estimate-form", label: "Run the live estimate" };
        }
        if (key === "junk_call") {
            return { id: "cta-junk", href: "/dumpster/dumpster-vs-junk-removal-which-is-cheaper", label: "Compare junk removal" };
        }
        return { id: "cta-dumpster-call", href: QUOTE_MATCH_BETA_PATH, label: "Join quote-match beta" };
    }

    function deriveDecisionMode(primaryCtaKey, topRecommendation, result) {
        if (primaryCtaKey === "junk_call") {
            return "junk";
        }
        if (topRecommendation && topRecommendation.multiHaul) {
            return "multi_haul";
        }
        if (String(result.feasibility || "").toUpperCase().includes("MULTI")) {
            return "multi_haul";
        }
        if (primaryCtaKey === "dumpster_call" || primaryCtaKey === "dumpster_form") {
            return "dumpster";
        }
        return "unsure";
    }

    function decisionHeadline(primaryCtaKey, topRecommendation, result) {
        const feasibility = String(result.feasibility || "").toUpperCase();
        if (primaryCtaKey === "junk_call" || feasibility !== "OK") {
            return "Best next move: compare junk removal first.";
        }
        if (topRecommendation && topRecommendation.multiHaul) {
            return `Best next move: ${topRecommendation.sizeYd}yd staged multi-haul plan.`;
        }
        if (topRecommendation) {
            return `Best next move: join quote-match beta for a ${topRecommendation.sizeYd}-yard dumpster.`;
        }
        return "Best next move unavailable: review inputs and rerun.";
    }

    function decisionDetail(primaryCtaKey, topRecommendation, result) {
        const risk = String(result.priceRisk || "").toUpperCase();
        const feasibility = String(result.feasibility || "").toUpperCase();
        if (primaryCtaKey === "junk_call") {
            return "Current feasibility or overage risk makes crew-based pickup more predictable.";
        }
        if (topRecommendation && topRecommendation.multiHaul) {
            return "Heavy or dense load signals indicate safer execution through staged hauls.";
        }
        if (feasibility !== "OK") {
            return "Operational limits suggest avoiding a single overloaded dumpster run.";
        }
        if (risk === "HIGH") {
            return "Price risk is elevated under current allowance assumptions.";
        }
        return "Weight and volume ranges fit a standard dumpster route with lower execution risk.";
    }

    function junkSmartWhenText(primaryCtaKey, result, inputPayload) {
        const risk = String(result.priceRisk || "").toUpperCase();
        const feasibility = String(result.feasibility || "").toUpperCase();
        const timing = String(inputPayload.needTiming || "").toLowerCase();
        if (primaryCtaKey === "junk_call") {
            return "This scenario already looks like a junk-first route.";
        }
        if (feasibility !== "OK") {
            return "When one-container pickup looks infeasible or likely to fail at dispatch.";
        }
        if (risk === "HIGH") {
            return "When overage exposure is likely and dense material sorting is hard.";
        }
        if (timing === "48h") {
            return "When same-day removal speed matters more than lowest rental cost.";
        }
        return "When access, labor, or sorting burden is higher than expected on site.";
    }

    function buildDecisionScores(result, inputPayload, topRecommendation, primaryCtaKey, priorityMode) {
        const risk = String(result.priceRisk || "").toUpperCase();
        const feasibility = String(result.feasibility || "").toUpperCase();
        const timing = String(inputPayload.needTiming || "").toLowerCase();
        const multiHaul = Boolean(topRecommendation && topRecommendation.multiHaul);
        const normalizedPriority = normalizePriorityMode(priorityMode);

        let cost = risk === "LOW" ? 84 : risk === "MEDIUM" ? 67 : 49;
        if (multiHaul) {
            cost -= 8;
        }
        if (primaryCtaKey === "junk_call" && feasibility !== "OK") {
            cost += 4;
        }

        let speed = timing === "48h" ? 88 : timing === "this_week" ? 74 : 60;
        if (primaryCtaKey === "junk_call") {
            speed += 6;
        }
        if (multiHaul) {
            speed -= 10;
        }

        let effort = primaryCtaKey === "junk_call" ? 82 : 56;
        if (multiHaul) {
            effort -= 12;
        }
        if (String(inputPayload.persona || "").toLowerCase() === "contractor") {
            effort += 6;
        }

        let safety = feasibility === "OK" ? 84 : feasibility.includes("MULTI") ? 62 : 36;
        if (risk === "HIGH") {
            safety -= 10;
        }
        if (multiHaul && feasibility.includes("MULTI")) {
            safety += 8;
        }

        return {
            cost: clampScore(cost),
            speed: clampScore(speed),
            effort: clampScore(effort),
            safety: clampScore(safety),
            priorityMode: normalizedPriority
        };
    }

    function decisionScoreRow(label, score, note) {
        const tone = score >= 75 ? "good" : score >= 55 ? "warn" : "risk";
        return `
            <div class="decision-score-row">
                <div class="decision-score-label">${label}</div>
                <div class="decision-score-meter">
                    <div class="decision-score-fill is-${tone}" style="width:${score}%;"></div>
                </div>
                <div class="decision-score-value">${score}</div>
                <p class="decision-score-note">${note}</p>
            </div>
        `;
    }

    function decisionScoreNote(metric, score) {
        if (metric === "cost") {
            return score >= 75
                ? "Low overage pressure under current assumptions."
                : score >= 55
                    ? "Cost is workable but allowance control matters."
                    : "Cost volatility is high. Compare alternate route first.";
        }
        if (metric === "speed") {
            return score >= 75
                ? "Route aligns with faster completion."
                : score >= 55
                    ? "Timing is acceptable with coordination."
                    : "Expect schedule friction or extra hauls.";
        }
        if (metric === "effort") {
            return score >= 75
                ? "Lower DIY effort expected."
                : score >= 55
                    ? "Moderate self-loading effort expected."
                    : "High handling effort likely on this route.";
        }
        return score >= 75
            ? "Operational safety margin looks strong."
            : score >= 55
                ? "Safety margin is usable with rules checked."
                : "High pickup-risk scenario. Prioritize safer branch.";
    }

    function clampScore(value) {
        return Math.max(0, Math.min(100, Math.round(value)));
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
        trackGaEvent(eventName, estimateId, payload);
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

    function trackGaEvent(eventName, estimateId, payload) {
        const analytics = window.dumpsterAnalytics;
        if (!analytics || typeof analytics.track !== "function") {
            return;
        }
        const mappedEvent = GA_EVENT_MAP[eventName];
        if (!mappedEvent) {
            return;
        }
        const params = normalizeGaParams(payload || {});
        params.page_path = window.location.pathname;
        if (estimateId) {
            params.estimate_id = estimateId;
        }
        analytics.track(mappedEvent, params);
        if (mappedEvent === "calculator_complete") {
            analytics.track("result_view", params);
        }
    }

    function normalizeGaParams(payload) {
        const params = {};
        Object.entries(payload).forEach(([key, value]) => {
            if (value === null || value === undefined) {
                return;
            }
            const valueType = typeof value;
            if (valueType === "string" || valueType === "number" || valueType === "boolean") {
                params[key] = value;
                return;
            }
            params[key] = JSON.stringify(value);
        });
        return params;
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

    async function createQuoteMatchIntake(apiData, inputPayload, result, lead) {
        const safeItems = Array.isArray(inputPayload.items) ? inputPayload.items : [];
        const materialIds = safeItems
            .map((item) => item && item.materialId)
            .filter((materialId) => typeof materialId === "string" && materialId.length > 0)
            .slice(0, 3);
        const primaryCta = normalizeCtaKey(result && result.ctaRouting ? result.ctaRouting.primaryCta : "");
        const decisionMode = primaryCta === "junk_call"
            ? "junk"
            : String(result && result.feasibility || "").toUpperCase() === "OK"
                ? "dumpster"
                : "unsure";
        try {
            const response = await fetch(QUOTE_MATCH_INTAKE_ENDPOINT, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    estimateId: apiData.estimateId,
                    zipCode: lead.zipCode,
                    contactMethod: lead.contactMethod,
                    contactValue: lead.contactValue,
                    persona: inputPayload.persona,
                    needTiming: inputPayload.needTiming,
                    decisionMode,
                    recommendedRoute: primaryCta,
                    projectId: inputPayload.projectId,
                    materialIds
                })
            });
            if (!response.ok) {
                return null;
            }
            return await response.json();
        } catch (_) {
            return null;
        }
    }
})();
