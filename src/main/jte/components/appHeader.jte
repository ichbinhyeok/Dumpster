@param String trustLabel
@param String primaryHref
@param String primaryLabel
<header class="site-header">
    <div class="app-container app-header-row">
        <a class="brand" href="/dumpster/size-weight-calculator" aria-label="Dumpster calculator home">
            <span class="brand-mark" aria-hidden="true">DD</span>
            <span class="brand-copy">
                <strong>Debris Decision</strong>
                <small>Dumpster vs junk, made simple</small>
            </span>
        </a>
        <nav class="site-nav" aria-label="Primary">
            <a href="/dumpster/material-guides">Material guides</a>
            <a href="/dumpster/project-guides">Project guides</a>
            <a href="/dumpster/heavy-debris-rules">Heavy rules</a>
        </nav>
        <div class="site-header-actions">
            <span class="trust-pill">${trustLabel}</span>
            <a class="ui-button ui-button--primary" href="${primaryHref}">${primaryLabel}</a>
        </div>
    </div>
</header>
