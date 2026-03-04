(function () {
    const intakeInput = document.getElementById("intake-id");
    const checkButton = document.getElementById("intake-status-check");
    const output = document.getElementById("intake-status-output");
    if (!intakeInput || !checkButton || !output) {
        return;
    }

    checkButton.addEventListener("click", async () => {
        const intakeId = (intakeInput.value || "").trim();
        if (intakeId.length < 8) {
            output.textContent = "Enter a valid intake ID.";
            return;
        }
        output.textContent = "Checking status...";
        checkButton.disabled = true;
        try {
            const response = await fetch("/api/quote-match/intakes/" + encodeURIComponent(intakeId));
            if (!response.ok) {
                output.textContent = "No intake found for that ID yet.";
                return;
            }
            const data = await response.json();
            output.textContent = data.statusLabel + ". " + data.expectedResponseWindow
                + ". Submitted: " + data.submittedAtIso + ".";
        } catch (_) {
            output.textContent = "Status lookup failed. Please retry.";
        } finally {
            checkButton.disabled = false;
        }
    });
})();
