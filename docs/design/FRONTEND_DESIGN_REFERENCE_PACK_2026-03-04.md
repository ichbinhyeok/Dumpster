# Frontend Reference Pack (2026-03-04)

Goal: keep frontend upgrades anchored to modern SaaS patterns instead of ad-hoc styling.

## Source set (web-researched)

1. Vercel Web Interface Guidelines  
   https://vercel.com/design/guidelines  
   - Used for: focus states, hit-target minimums, layered shadows, interaction contrast, accessibility guardrails.

2. Vercel Design / Geist ecosystem  
   https://vercel.com/design  
   - Used for: calm density, spacing rhythm, product-grade typography hierarchy.

3. Tailwind Plus UI Blocks  
   https://tailwindcss.com/plus/ui-blocks  
   - Used for: proven SaaS section patterns (hero, pricing-style cards, app-shell ergonomics).

4. shadcn/ui docs  
   https://ui.shadcn.com/docs  
   - Used for: token-first component composition and consistent API-like styling structure.

5. Radix Themes docs  
   https://www.radix-ui.com/themes/docs/components/theme  
   - Used for: theme knobs (radius/scaling/panel style) and stable UI primitives.

6. Linear changelog (product UI evolution signal)  
   https://linear.app/changelog  
   - Used for: high-information-density SaaS interaction patterns.

## Downloaded kit snapshots (local)

- HyperUI (MIT):
  - [hyperui-LICENSE.txt](C:\Development\Owner\dumpster-calculator\docs\design\kits\snapshots\hyperui-LICENSE.txt)
  - [hyperui-header-3.html](C:\Development\Owner\dumpster-calculator\docs\design\kits\snapshots\hyperui-header-3.html)
  - [hyperui-feature-grid-4.html](C:\Development\Owner\dumpster-calculator\docs\design\kits\snapshots\hyperui-feature-grid-4.html)
  - [hyperui-stats-2.html](C:\Development\Owner\dumpster-calculator\docs\design\kits\snapshots\hyperui-stats-2.html)

- Flowbite Admin Dashboard (MIT):
  - [flowbite-admin-LICENSE.txt](C:\Development\Owner\dumpster-calculator\docs\design\kits\snapshots\flowbite-admin-LICENSE.txt)
  - [flowbite-pricing-page.html](C:\Development\Owner\dumpster-calculator\docs\design\kits\snapshots\flowbite-pricing-page.html)

These snapshots were pulled from upstream kits and used as direct layout references for comparison cards, matrix sections, and CTA density.

## Canonical baseline (locked)

- Primary baseline: **Flowbite pricing/dashboard card density**
- Secondary accents: **HyperUI feature/stat block spacing**
- Applied in:
  - comparison scenario cards
  - matrix + stat strips
  - multi-CTA action rows
  - priority-toggle control group

## Copy / Styling strategy to apply

- Hero and value proposition:
  - Keep message short, high-contrast, action-first.
  - Use decision-oriented copy, not utility-jargon.

- Cards and surfaces:
  - Use layered shadow + crisp border (not flat cards).
  - Distinguish primary decision card from supporting data cards.

- CTA model:
  - 1 clear primary action.
  - 2-3 explicit secondary actions with intent labels.

- Motion:
  - Single stagger-in sequence for first paint.
  - No decorative micro-animation spam.

- Input density:
  - Maintain mobile min target >= 44px.
  - Maintain visible focus ring and keyboard reachability.

## Applied in current wave

- Decision-strip quick routes added to calculator entry.
- Result panel reframed as decision summary.
- CTA labels upgraded to explicit homeowner actions.
- Comparison hub now includes priority toggles (cost/speed/labor/heavy safety) with adaptive card ordering.
- Calculator result board now includes a decision scorecard (cost/speed/effort/safety) with meter bars.
- Tap-target and typography checks kept under Playwright quality suite.
