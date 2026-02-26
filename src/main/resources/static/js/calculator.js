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

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        submitButton.disabled = true;
        submitButton.textContent = "Calculating...";
        clearResult();

        const payload = buildPayload();

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
        const allowanceRaw = document.getElementById("allowance-tons").value;
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
            <a class="button-link primary" href="tel:+18005550123">Call dumpster quote</a>
            <a class="button-link" href="#junk">Compare junk removal</a>
        `;
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
})();

