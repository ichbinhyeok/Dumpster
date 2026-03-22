@param String trustLabel
@param String primaryHref
@param String primaryLabel
<header class="site-header">
    <div class="app-container app-header-row">
        <a class="brand" href="/dumpster/size-weight-calculator" aria-label="Dumpster calculator home">
            <span class="brand-mark" aria-hidden="true">DD</span>
            <span class="brand-copy">
                <strong>Debris Decision</strong>
                <small>Job-specific dumpster decisions</small>
            </span>
        </a>
        <nav class="site-nav" aria-label="Primary">
            <a href="/dumpster/project-guides">Job presets</a>
            <a href="/dumpster/material-guides">Debris types</a>
            <a href="/dumpster/heavy-debris-rules">Heavy-load rules</a>
        </nav>
        <div class="site-header-actions">
            <span class="trust-pill">${trustLabel}</span>
            <a class="ui-button ui-button--primary" href="${primaryHref}">${primaryLabel}</a>
        </div>
    </div>
</header>
