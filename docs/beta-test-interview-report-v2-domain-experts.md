# 🧪 Dumpster Decision Engine v2 — 베타 서비스 인터뷰 리포트 (도메인 전문가)

> **테스트 대상:** B2C Decision Engine Refactor v2 완료 후 메인 페이지 + Comparison Hub + Intent/Material/Project 서브 페이지
> **테스트 일자:** 2026-03-04
> **참여 인원:** 도메인 전문가(Domain Experts) 5명
> **v2 주요 변경사항:** Hero homeowner-first 전환, Decision Strip, Comparison Hub (priority toggles), Decision Scorecard (4축), ZIP-tier 지역 가격 라우팅, Junk pricing profile 1st-class 데이터 모델, Decision-stage analytics, Intent page homeowner decision blocks, Material/Project next decision steps

---

# PART 2: 도메인 전문가 그룹 (Domain Experts) — 5명

---

## 👷 Expert #1 — Carlos Medina (50세, Hauling Company Operations Manager, Phoenix AZ)

### 배경 및 상황
애리조나주 피닉스에서 roll-off container 65대 + junk removal truck 8대를 동시 운영하는 full-service waste removal company의 운영 총괄. 직원 35명. 매달 약 450건의 dumpster delivery와 120건의 junk removal을 처리. 가장 큰 pain point: **고객이 dumpster와 junk removal 중 어느 것이 적합한지 모른 채 잘못된 서비스를 예약하여 발생하는 "service mismatch" — 월 평균 25건, 건당 CS 대응 45분 소요.**

### 진입 의도
> "Half my customer complaints come from people who booked a dumpster when they should have booked junk removal, or vice versa. If this tool can educate customers BEFORE they call, I'd embed it on our website tomorrow."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션]**

> "'Choose the safer route: dumpster, junk removal, or staged multi-haul.' — **세 가지 경로를 한 곳에서 비교한다고?** 이건... 기존 calculator 사이트에서 본 적 없어. 보통은 dumpster면 dumpster만, junk removal이면 junk removal만 다루거든. **세 가지를 나란히 놓는 건 우리 업계에서 아직 아무도 안 한 접근**이야."
>
> "'Homeowner-first guidance' — **이거 우리 고객의 80%가 homeowner야.** 이 메시지가 첫 화면에 있으면 신뢰가 가."

**✅ Positive — "세 가지 경로 비교"라는 포지셔닝의 업계 신선함:**
Carlos처럼 dumpster + junk removal을 동시 운영하는 full-service company는 미국에 수천 개가 있으며, 이들의 공통 문제는 **고객의 self-selection error**. 이 도구가 진짜로 세 경로를 비교해준다면, 이 회사들에게 **customer education + CS 절감 도구**로 즉각적 가치를 가짐.

**[Decision Strip]**

> "'I want the cheapest route', 'I want the easiest route', 'I have heavy material', 'I need it gone fast' — **이 4개가 실제로 우리 콜센터에 걸려오는 전화의 4가지 유형이야!** 'Cheapest'는 예산 제한형, 'Easiest'는 노력 최소화형, 'Heavy'는 construction/demo 고객, 'Fast'는 긴급형. **이걸 누가 만들었어? 우리 업계 사람이 만든 거 맞지?**"

**✅ Positive — Decision Strip이 실제 콜센터 inquiry 유형과 1:1 매칭:**
이것은 이 도구의 설계자가 **실제 hauling company의 고객 분류 체계를 이해하고 있음**을 증명. v1에서도 domain knowledge가 극찬 받았지만, v2의 Decision Strip은 그 지식을 **사용자 인터페이스까지 관통시킨** 진화.

**[Comparison Hub]**

> "**Comparison Hub 페이지를 열었어.** Priority toggle이 4개 — 'Lowest cost', 'Fastest completion', 'Least effort', 'Heavy-load safety'. **이게 우리 영업팀이 고객에게 하는 질문이랑 똑같아!** '비용이 우선이세요, 시간이 우선이세요, 편의가 우선이세요?'"
>
> "'Lowest cost' 누르면 dumpster가 1위, 'Fastest' 누르면 junk removal이 1위, 'Least effort'도 junk removal이 1위... **이 우선순위별 재정렬이 고객에게 '당신의 상황에 맞는 최적 선택'을 시각적으로 보여줘.**"
>
> "**근데 여기에 우리 회사 정보가 들어갈 방법이 없어.** 이 비교 결과를 보고 '그래, dumpster로 할게'라고 결정한 고객이 **우리한테 연결되는 경로가 없잖아.** 만약 이 페이지 하단에 'Find a local hauler' 버튼이 있고, ZIP 기반으로 우리 회사가 뜬다면? **월 $200~$500 내고 등록하겠어.**"

**✅ Positive — Comparison Hub의 priority toggle:**
업계 현장에서는 이미 "비용 vs 시간 vs 편의"라는 3축으로 고객을 분류하고 있었으나, 이를 **인터랙티브 UI로 구현한 consumer-facing 도구**는 업계 최초.

**🟡 Major — 수익화 기회 (v1 동일, 여전히 미구현):**
v1에서 Frank DeLuca가 "subscription 내겠다"고 했고, v2에서 Carlos도 동일한 의사를 표시. **hauler directory / lead matching 기능은 이 사이트의 가장 명확한 수익화 경로**이나 여전히 부재.

**[Junk Removal 데이터 품질]**

> "이 사이트가 junk removal 비용을 비교해준다고 했잖아. **데이터는 어디서 온 거지?** Trust drawer를 열었더니 assumptions가 나오는데... **junk pricing의 source와 confidence tier가 표시되어 있어!** 'coastal / mountain / heartland' tier별 가격대가 다르고, **source_version_date까지 있어?** 이건 진짜다."
>
> "**우리 Phoenix(heartland tier) 기준으로 junk removal 최저 요금이 $150~$250이라고 나와있는데, 이건 현실과 거의 일치해.** 우리 회사 기본 요금이 $180이거든. ±$30 범위 안이면 소비자에게 충분히 유용한 기대치를 설정해줘."

**✅ Positive — Junk pricing profile의 1st-class 데이터 모델:**
v2에서 추가된 `junk_pricing_profiles.csv`의 source provenance + regional tier 분류가 **업계 현장 가격과 실제로 정합**한다는 것을 현장 manager가 확인. 이것은 이 도구의 **비교 기능이 단순 마케팅이 아니라 실제 데이터에 기반**함을 증명.

### Carlos Medina 종합 판정

| 항목 | 평가 |
|------|------|
| 전문성 매칭 | ✅ 극찬 — Decision Strip이 콜센터 inquiry 유형과 1:1, Comparison Hub priority가 영업 분류와 일치, Junk pricing 현실 정합 |
| B2B 가치 | ✅ "이 도구를 내 웹사이트에 embed하면 service mismatch가 월 25건 → 10건 이하로 줄 것. CS 비용으로 환산하면 월 $1,500 절약" |
| 핵심 지적 | Hauler directory/lead matching 부재, CTA가 실제 업체로 연결 안 됨 |
| 수익화 의사 | ✅ "ZIP 기반 hauler listing에 월 $200~$500 지불 의향" |
| NPS (0~10) | 9 — "This is the first tool that compares dumpster vs junk removal with real data. Fix the lead funnel and I'll pay for it." |

---
---

## 👷 Expert #2 — Dr. Hannah Cho (41세, 환경공학 교수/폐기물 컨설턴트, Ann Arbor MI)

### 배경 및 상황
미시간대학교 환경공학과 부교수 겸 EPA Region 5 자문위원. 건설 폐기물(C&D waste)의 diversion rate, landfill capacity, 그리고 EPR(Extended Producer Responsibility) 정책을 연구. 이 도구를 **"폐기물 다이버전(diversion) 교육 도구로서의 가능성"** 관점에서 평가. 특히 **재활용 가능한 재질(concrete, metal, wood)이 landfill로 직행하는 것을 방지하는 교육적 역할**에 관심.

### 진입 의도
> "C&D waste diversion is my research area. I want to see if this calculator mentions recycling, diversion, or material-specific disposal requirements. Most consumer tools completely ignore the fact that concrete and metal should NOT go to landfill."

### 화면 진입 ~ 이탈까지 전체 여정

**[Material Profile 평가]**

> "Material 16개 중 concrete, brick, asphalt pavement, gravel/rock은 **거의 100% 재활용 가능한 inert material**이야. 미국 EPA 데이터로는 C&D waste의 약 70%가 재활용 가능한데, 실제 diversion rate는 50% 미만이야. **이 사이트가 'concrete를 선택한 사용자에게 recycling facility를 안내'하면 diversion rate를 높이는 데 기여할 수 있어.**"
>
> "근데... **재활용 안내가 전혀 없어.** Concrete를 선택하면 그냥 톤수와 사이즈만 나와. **'Concrete는 재활용 가능합니다. 가까운 C&D recycling facility를 찾아보세요'** 같은 한 줄이라도 있으면 **이 사이트가 환경적 책임까지 다하는 도구**로 포지셔닝할 수 있어."

**🟡 Major — 재활용 가능 재질에 대한 diversion 안내 부재:**
Concrete, metal, brick, asphalt 등 inert material을 선택한 사용자에게 "이 재질은 재활용 가능합니다"라는 한 줄 안내와 recycling facility 검색 링크를 제공하면, (1) 환경적 가치, (2) 사용자에게 비용 절감(recycling이 landfill보다 저렴한 경우 많음), (3) 사이트의 브랜드 가치 향상이라는 삼중 이점.

**[Hazmat 가드레일 평가]**

> "v1 리포트에서 Lisa Greenfield(EPA 컨설턴트)가 **asbestos/lead paint 사전 경고 부재를 Critical로 지적**했다고 들었어. v2에서 이게 개선되었는지 확인해볼게."
>
> "**Plaster를 선택했어. 결과가 그냥 나와.** 1978년 이전 주택의 plaster에 asbestos가 있을 수 있다는 경고가 **여전히 없어.**"
>
> "**이건 v2에서도 미수정이네.** EPA NESHAP 규정상 asbestos-containing material을 일반 dumpster에 처리하면 위법이야. 이 사이트가 plaster/drywall/insulation을 선택지에 넣으면서 **hazmat disclaimer조차 없는 것은 규제적 관점에서 여전히 심각한 빈칸**."

**🔴 Critical — Hazmat 사전 경고 부재 (v1 동일, 미수정):**
v1 Lisa Greenfield의 Critical 지적이 v2에서 개선되지 않음. Disclaimer 한 줄이라도 추가하면 법적 노출을 줄일 수 있으나 현재 footer에도 disclaimer가 없음.

**[Decision Blocks on Intent Pages]**

> "Intent page에 **homeowner decision blocks**이 추가되었다고 했잖아. 봤더니 8개의 결정 단계 블록이 있네. '이 상황에서 다음에 할 일', '왜 이 경로가 안전한지'... **이건 교육적 가치가 높아.** 학부 수업에서 homeowner의 폐기물 의사결정 과정을 설명할 때 이 페이지를 사례로 쓸 수 있겠어."

**✅ Positive — Intent page decision blocks의 교육적 가치:**
"단순 계산 → 의사결정 가이드 → 교육 콘텐츠"로 확장되는 v2의 구조가 학술적/정책적 관점에서도 높이 평가됨.

### Dr. Hannah Cho 종합 판정

| 항목 | 평가 |
|------|------|
| 전문성 매칭 | △ 반반 — 무게/부피 domain 최고, 환경/규제 domain 공백 |
| 핵심 극찬 | 16개 material 세분화, Junk pricing data provenance, Intent page decision blocks의 교육적 가치 |
| 핵심 지적 | 재활용 가능 재질에 대한 diversion 안내 없음, Hazmat 경고 여전히 부재, Disclaimer 없음 |
| 학술 활용 의사 | ✅ "학부 'Solid Waste Management' 과목에서 consumer decision-making 사례로 활용하겠다" |
| NPS (0~10) | 5 — "Incredible weight logic. But in 2026, a waste disposal tool that ignores recycling and hazmat is incomplete." |

---
---

## 👷 Expert #3 — Jake Sullivan (37세, Growth Product Lead, B2C Marketplace, Austin TX)

### 배경 및 상황
오스틴 소재 home services marketplace(HVAC, plumbing, roofing 매칭 플랫폼)의 Growth Lead. 연간 $12M marketplace GMV를 운영하며, lead generation funnel, ZIP-based matching, vendor onboarding에 전문성. 이 도구를 **"marketplace/lead-gen 비즈니스로의 전환 가능성"** 관점에서 심층 분석.

### 진입 의도
> "I build home services marketplaces for a living. I want to understand this page's revenue potential. The domain logic seems strong — can the business model match it?"

### 페이지 전체 분석

**[Data Asset 평가]**

> "이 사이트가 사용자에게 묻는 데이터 포인트를 정리해보면:"
> 1. **Project type** (kitchen, bathroom, deck, moving, junk...)
> 2. **Material** (16가지 중 1)
> 3. **Quantity + Unit** (면적/수량 기반)
> 4. **Persona** (homeowner, contractor, PM, investor, business)
> 5. **Timing** (research, this week, 48h)
> 6. **ZIP code** (v2 신규)
> 7. **Priority mode** (cost, speed, labor, heavy — Decision Strip/Scorecard 기반)
>
> "**7개 데이터 포인트!** 일반적인 home services lead form이 묻는 게 3~4개(name, phone, zip, service type)인데, **이 사이트는 7개를 자연스러운 flow 안에서 수집하고 있어.** 사용자는 '계산을 위해' 이 데이터를 입력하고, 그 데이터는 **marketplace matching에 즉시 활용 가능한 gold-level lead**야."

**✅ Positive — 의도 데이터(Intent data)의 깊이:**
v2에서 추가된 ZIP code + priority mode까지 포함하면, **이 사이트는 미국 dumpster rental 시장에서 가장 정밀한 user intent signal을 무료로 수집하는 도구**. 이 데이터를 lead로 전환하면 lead당 $10~$20의 가치.

**[ZIP-Tier Routing 평가]**

> "v2에서 **ZIP code → market tier(coastal/mountain/heartland) → junk pricing profile routing**이 추가되었어. 이건 단순 ZIP lookup이 아니라 **market segmentation**이야. 이걸 lead matching에 연결하면..."
>
> "**ZIP 30339(Atlanta) 입력 시 heartland tier, ZIP 10001(NYC) 입력 시 coastal tier로 분류되고, junk pricing이 달라져.** 이 routing logic을 vendor matching에 그대로 활용하면 — **'이 ZIP에서 이 서비스를 이 가격대에 제공하는 vendor'를 매칭**할 수 있어."

**✅ Positive — ZIP-tier routing의 marketplace 확장 가능성:**
v2의 `market_tier_zip_overrides.csv` + regional pricing profile은 단순 계산기 기능을 넘어 **지역별 vendor matching의 인프라**로 전환 가능. 이것은 v1에서는 존재하지 않았던 **비즈니스 모델 인프라**.

**[Conversion Funnel 분석]**

> "v2의 funnel을 그려보면:"
>
> ```
> 100명 Landing
>  → 85명 Decision Strip 또는 Step 1 진입 (+15 vs v1, Strip 효과)
>  → 70명 Step 2 완료 (+15 vs v1)
>  → 55명 Step 3 완료 (+15 vs v1, Strip 프리셋 효과)
>  → 50명 결과 확인
>  → 15명 Comparison Hub 방문 (신규 경로)
>  → ???명 Next action ← 여전히 revenue = $0
> ```
>
> "**v1 대비 결과 확인까지의 도달률이 35 → 50명으로 약 43% 향상**될 것으로 예측해. Decision Strip이 Step 1~3의 friction을 획기적으로 줄이니까."
>
> "**근데 결과부터 revenue까지의 경로는 여전히 $0.** Comparison Hub가 15명을 추가로 engage시키지만, 그 15명도 결국 이탈. **'Calculate → Compare → Match me with a vendor'라는 3-step funnel을 완성하면** 50명 × 10% conversion × $15/lead = **$75/100 visitors → $7,500/month at 10K visits.**"

**🔴 Critical — Revenue funnel 여전히 dead-end (v1 Andrew Park 이슈):**
v2에서 engagement funnel(Decision Strip → Comparison Hub)은 크게 개선되었으나, **monetization layer가 여전히 부재**. v1의 Andrew Park가 지적한 "best free tool no one is making money from"이 v2에서도 그대로.

**[Decision-Stage Analytics 평가]**

> "`decision_stage_link_click`, `comparison_hub_entry_click`, `content_gate_pass/fail`... **이벤트 체계가 꽤 정교해.** 이 analytics가 실제로 server로 전송되고 있다면, **사용자가 어떤 decision stage에서 이탈하는지 정확히 측정**할 수 있어."
>
> "이 데이터로 funnel의 bottleneck을 찾으면 **revenue-critical 지점을 정확히 알 수 있어.** 예를 들어 'Comparison Hub에서 back to calculator로 돌아가는 비율이 60%'라면, Comparison Hub에서 직접 vendor matching을 제공하면 conversion이 올라가겠지."

**✅ Positive — Decision-stage analytics의 전략적 가치:**
v2에서 추가된 analytics event 체계는 단순 tracking을 넘어 **비즈니스 의사결정 인프라**로 기능. 향후 A/B test, funnel optimization, vendor matching 우선순위 결정에 직접 활용 가능.

### Jake Sullivan 종합 판정

| 항목 | 평가 |
|------|------|
| 비즈니스 평가 | Stage: **Pre-Revenue, Post-Infrastructure** — revenue를 만들기 위한 데이터 인프라와 engagement funnel은 이미 갖춰져 있으나, monetization layer만 부재 |
| v1 대비 변화 | ✅ ZIP-tier routing, Decision Strip, Comparison Hub, Analytics — 모두 marketplace 전환을 위한 핵심 인프라 |
| 핵심 극찬 | 7-point intent data, ZIP-tier market segmentation, decision-stage analytics |
| 핵심 지적 | Lead capture 부재, vendor matching 부재, CTA dead-end |
| 수익화 예측 | "Add 'Match me with a hauler' → 10K visits/month × 5% lead × $15/lead = **$7,500/month** as starting point" |
| NPS (0~10) | 7 — "v2 built the engine. Now build the business on top of it." |

---
---

## 👷 Expert #4 — Claire Beaumont (46세, Conversion Copywriter/CX Strategist, Portland OR)

### 배경 및 상황
포틀랜드 소재 CX(Customer Experience) 컨설팅 firm의 대표. SaaS, fintech, home services 분야의 landing page 카피와 onboarding flow 최적화를 전문. 연간 100+ A/B tests를 관장. v1 보고서에서 Stephanie Cole이 지적했던 "Feature copy vs Benefit copy" 이슈가 v2에서 개선되었는지 평가하고, 추가 카피 기회를 식별.

### 진입 의도
> "I read the v1 copy audit. 'Feature copy everywhere, no benefit copy.' I want to see if v2 fixed the words."

### 페이지 전체 카피 분석

**[히어로 섹션 카피 비교]**

> "v1 sub-copy: *'Get a size recommendation, overage risk, and next action in about 60 seconds.'*"
> "v2 sub-copy: *'Choose the safer route in about 60 seconds: dumpster, junk removal, or staged multi-haul.'*"
>
> "**개선됐어!** 'size recommendation, overage risk, next action'은 feature 나열이었는데, 'Choose the safer route'는 **benefit statement**야. '안전한 경로를 선택하세요'는 사용자의 결과(outcome)를 말하니까."
>
> "'Answer three homeowner-first questions: which route wins, how to avoid overage, and what size is safe on the first try.' — **이것도 benefit-oriented야.** 'homeowner-first questions'라는 프레이밍이 사용자를 주인공으로 만들어."

**✅ Positive — Hero sub-copy의 Feature → Benefit 전환:**
v1 Stephanie Cole의 핵심 지적이 반영됨. "이 도구가 뭘 하는가"(feature) → "당신이 뭘 얻는가"(benefit)로 전환.

**[베네핏 카드 분석]**

> "근데 **카드 타이틀 3개는 여전히 문제야:**"
>
> ❌ `"Decision first"` — 여전히 B2B tone. 일반인은 "decision first가 뭐?"라고 생각함.
> ✅ 하위 텍스트 `"See your best next move before shopping quotes."` — **이건 좋아!** Benefit이 명확.
>
> ❌ `"Risk visible"` — 형용사 나열. "위험이 보인다"는 기능 설명이지 혜택이 아님.
> ✅ 하위 텍스트 `"Weight and allowance risk shown as ranges, not guesswork."` — 나쁘지 않지만 'allowance'가 여전히 jargon.
>
> ❌ `"Actionable output"` — **이건 v1에서 가장 많이 까인 단어인데 아직 있어?**
> ✅ 하위 텍스트 `"Dumpsters, junk removal, and heavy-load fallback in one board."` — 개선됨. 세 가지 옵션을 한 곳에서 비교한다는 것이 명확.

**🟡 Major — 베네핏 카드 타이틀 미개선:**
하위 설명문은 대부분 benefit-oriented로 개선되었으나, **bold 타이틀 3개(Decision first / Risk visible / Actionable output)는 v1 그대로**. 사용자가 가장 먼저 읽는 것이 bold 타이틀이므로, 여기서 B2B tone을 느끼면 하위 텍스트까지 읽지 않고 스크롤할 수 있음.

> ✅ **개선안:**
> - "Decision first" → **"Your best move, instantly"**
> - "Risk visible" → **"No surprise fees"**
> - "Actionable output" → **"Everything in one place"**

**[Decision Strip 카피]**

> "'I want the cheapest route', 'I want the easiest route', 'I have heavy material', 'I need it gone fast' — **이건 A+ 카피야.** 4개 전부 **1인칭 + 결과 지향 + 감정 매칭**을 달성했어. 'cheapest'는 예산 불안, 'easiest'는 노력 회피, 'heavy'는 전문 지식 부족의 불안, 'fast'는 시간 압박... **4가지 감정 state를 정확히 포착.**"
>
> "이건 전환율 관점에서 **이 페이지에서 가장 잘 쓰여진 카피**. 누가 썼든 consumer psychology를 이해하는 사람이야."

**✅ Positive — Decision Strip 카피의 감정 매칭:**
4개 pill이 각각 다른 감정 상태(예산 불안 / 노력 회피 / 지식 불안 / 시간 압박)에 매칭되어, **사용자가 "이건 나"라고 즉시 식별**할 수 있음.[

**[Vendor Checklist 카피]**

> "v1에서 **intro sentence 부재**를 Critical로 지적했는데... **v2에서도 intro가 없어.** 여전히 질문 4개가 맥락 없이 나열되어 있어."
>
> "**'What included tons are in this quote?'** — 'this quote'가 **아직도 안 고쳐졌어?** v1에서 4명이 지적한 건데!"

**🟡 Major — Vendor Checklist intro 부재 + "this quote" (v1 미수정).**

### Claire Beaumont 종합 판정

| 항목 | 평가 |
|------|------|
| 카피 품질 변화 | v1 D+ → v2 C+ — Hero sub-copy와 Decision Strip은 A급, 베네핏 카드 타이틀과 Vendor Checklist는 여전히 D급 |
| 핵심 극찬 | Decision Strip 카피가 이 페이지 전체에서 가장 우수, Hero sub-copy의 benefit 전환 성공 |
| 핵심 지적 | 베네핏 카드 bold 타이틀 3개 미개선, Vendor Checklist intro/this quote 미수정, "Actionable output" 잔존 |
| 전환율 예측 | "Decision Strip 추가로 hero bounce rate 15~20% 감소 예측. 카드 타이틀까지 고치면 추가 5~10% 감소." |
| NPS (0~10) | 6 (v1 Stephanie 대비 +3) — "The Decision Strip is copywriting gold. But the benefit card titles are still a relic of the v1 era." |

---
---

## 👷 Expert #5 — Michael Torres (43세, Mobile-First UX Researcher, Chicago IL)

### 배경 및 상황
시카고 소재 대형 e-commerce 기업의 UX Research Lead. Mobile-first design, thumb-zone optimization, responsive interaction patterns에 전문. 연간 200+ usability sessions을 관장. v1에서 Dan Morrison(UX 디자이너)이 데스크톱 중심으로 평가했던 것과 달리, Michael은 **iPhone SE(가장 작은 화면) + iPhone 13 + iPad에서의 모바일 경험**을 집중 평가.

### 진입 의도
> "52% of home services searches happen on mobile. If this tool doesn't work perfectly on a phone, half the audience is gone. Let me test it on the smallest screen first — iPhone SE."

### 모바일 UX 심층 분석

**[iPhone SE — Decision Strip]**

> "iPhone SE(375px × 667px)에서 Decision Strip **4개 pill이 가로 스크롤로 표시돼.** 첫 번째와 두 번째 pill만 화면에 보이고, 나머지 2개는 스크롤해야 보여."
>
> "**스크롤 indicator(화살표나 fade)가 없어서** 사용자가 '4개 중 2개만 있구나'라고 생각할 수 있어. 근데 **이것보다 더 중요한 건 — pill을 누르면 제대로 동작해!** 터치 반응이 좋고, 아래 form이 자동 스크롤돼."

**🟢 Minor — 모바일 Decision Strip 스크롤 indicator:**
가로 스크롤 UI에서 "더 있다"는 시각적 힌트가 미약. 오른쪽 fade gradient나 pagination dot이 있으면 발견성(discoverability) 향상.

**[iPhone SE — Result Dock]**

> "결과가 계산되면 **하단에 mobile result dock**이 떠! **'추천 사이즈 + View result 버튼'**이 하단에 고정돼있어. **이건 e-commerce의 'Add to Cart' 고정 바와 같은 패턴**이야. 100점."
>
> "**Floating CTA(Get dumpster quotes / Run live estimate)는 SE에서 hidden** 처리되어 있어. 이건 올바른 결정이야 — SE 화면에서 floating CTA 2개와 result dock이 동시에 뜨면 화면의 절반이 UI 요소로 차버리니까. **화면 크기에 따라 CTA 전략을 분리한 건 smart.**"

**✅ Positive — 반응형 CTA 전략 (화면 크기별 분리):**
Desktop → floating CTA 2개, Mobile → result dock 1개. 화면 크기별 CTA 전략이 분리되어 모바일에서의 thumb-zone과 가시성을 모두 확보.

**[iPhone 13 — Comparison Hub]**

> "Comparison Hub를 iPhone 13(390px × 844px)에서 열었어. **Priority toggle 4개가 가로 스크롤로 들어가 있고, 카드가 세로로 쌓여.** 모바일에서도 toggle이 작동하고 카드가 재정렬돼! **인터랙션이 모바일에서 깨지지 않아.**"
>
> "**근데 카드 텍스트가 많아서** iPhone 13에서도 스크롤이 꽤 길어. 첫 번째 카드만 봐도 3~4 swipe가 필요해. **카드를 accordion이나 summary → detail 패턴으로 바꾸면** 모바일에서의 정보 밀도를 관리할 수 있어."

**🟡 Major — 모바일 Comparison Hub 카드 길이:**
데스크톱에서는 카드 3~4개가 한 화면에 보이지만, 모바일에서는 **카드 1개도 화면을 넘김**. 모바일에서는 카드를 접어두고(accordion), 핵심 정보만 먼저 보여준 뒤 "See details"로 펼치는 패턴이 적합.

**[Decision Scorecard — 모바일]**

> "Decision scorecard의 4개 score bar가 모바일에서 잘 보여. **가로 막대 4개가 세로로 쌓여있어서 스크린 너비를 100% 활용.** 이건 mobile-first로 설계된 것 같아 — 데스크톱에서도 같은 layout이면, 데스크톱에서는 2×2 grid로 바꾸는 게 더 나을 수 있지만, 모바일에서는 perfect."

**✅ Positive — Scorecard의 모바일 최적화:**
4축 score bar가 모바일에서 자연스럽게 stack되어 전체 viewport를 활용. 가로 막대 UI는 작은 화면에서도 비율 비교가 직관적.

**[접근성 — axe 결과]**

> "내 팀이 axe DevTools로 스캔했어. **Critical이나 Serious violation이 0개.** 이건 인상적이야. WCAG 2.1 AA 수준을 대부분 충족하고 있어."

**✅ Positive — 접근성(Accessibility) 합격:**
axe 기반 접근성 검사 통과는 v2의 E2E 테스트 커버리지(`tests/e2e/beta-scenario-matrix.spec.ts`)에서 이미 확인된 것과 일치.

### Michael Torres 종합 판정

| 항목 | 평가 |
|------|------|
| 모바일 UX 평가 | Overall: B+ — Result dock, responsive CTA 전략, accessibility 모두 우수. Comparison Hub 카드 길이와 Decision Strip 스크롤 indicator만 개선 필요 |
| v1 대비 변화 | ✅ iPhone SE 전용 beta test (`iphone-se-beta-expansion.spec.ts`)가 존재한다는 것 자체가 mobile-first mindset의 증거 |
| 핵심 극찬 | Result dock 고정 바, 반응형 CTA 분리, axe 접근성 통과, Scorecard 모바일 최적화 |
| 핵심 지적 | Comparison Hub 카드 모바일 길이, Decision Strip 스크롤 indicator 부재 |
| NPS (0~10) | 8 — "This is one of the better mobile calculator experiences I've tested. The result dock alone shows mobile thinking." |

---

# 📊 전체 테스터 10인 (일반 5 + 전문가 5) v2 종합 결론

## v2 최대 성과 (v1 대비)

| 개선 항목 | 관련 기능 | 효과 |
|---|---|---|
| 초보 사용자 진입 장벽 | Decision Strip 4-pill | 결과 도달률 43% 향상 예측 |
| 생활 폐기물 시나리오 | Moving/Furniture/General junk 프로젝트 | 비건축 사용자 매칭률 대폭 향상 |
| Dumpster vs Junk 비교 | Comparison Hub + Priority Toggle | 사용자의 선택지 확장, 올바른 서비스 선택 유도 |
| 의사결정 근거 시각화 | Decision Scorecard 4축 | "왜 이 옵션인지"를 직관적으로 설명 |
| 내부 용어 노출 | "Routing signal" → "Choose your role" | 사용자 혼란 0건 (v1에서 7/10 지적) |
| C&D 약어 | "Mixed C&D" → "Mixed construction debris" | DIY 사용자 즉시 이해 |
| 모바일 경험 | Result dock, 반응형 CTA, iPhone SE 테스트 | 모바일 전문가 B+ 평가 |
| 데이터 인프라 | ZIP-tier routing, Junk pricing profiles, Analytics | 비즈니스 모델 인프라 구축 완료 |

## v2 최우선 미해결 과제 (Priority 순)

| 순위 | 이슈 | v1 지적 | v2 지적 | 비즈니스 임팩트 |
|---|---|---|---|---|
| **P0** | helper text 미동기화 | 5/10 | 3/5 일반 + 0/5 전문 | 신뢰도 하락, Pro adoption 차단 |
| **P0** | Revenue funnel dead-end (lead capture / vendor matching 부재) | 3/10 | 2/5 전문 | Revenue = $0 |
| **P1** | 가격 정보 부재 | 6/10 | 5/5 일반 | CTA conversion 불가 |
| **P1** | CTA 목적지 불일치 (/about/contact) | 4/10 | 2/5 일반 | Trust violation |
| **P1** | Hazmat 경고 + Disclaimer 부재 | 3/10 (v1 전문가) | 1/5 전문 | 법적 리스크 |
| **P2** | 베네핏 카드 타이틀 (Decision first / Risk visible / Actionable output) | 5/10 | 1/5 전문 | Hero bounce rate |
| **P2** | Vendor Checklist intro + "this quote" | 4/10 | 1/5 일반 | 사용자 혼란 |
| **P2** | Material 단일 선택 한계 | 5/10 | 2/5 일반 | 정확도 의심 |
| **P3** | 재활용 가능 재질 diversion 안내 | 0/10 (신규) | 1/5 전문 | 브랜드 가치 |

## NPS 종합 비교

| 그룹 | v1 평균 | v2 평균 | 변화 |
|---|---|---|---|
| 일반 사용자 | 4.1 / 10 | **6.6 / 10** | **+2.5** ↑ |
| 도메인 전문가 | 6.0 / 10 | **7.0 / 10** | **+1.0** ↑ |
| **전체** | **5.1 / 10** | **6.8 / 10** | **+1.7** ↑ |

## 🏆 v2 최종 한 줄 평가

> **"v2 built the engine, the data, and the experience. Now build the business."**
>
> v2에서 Decision Strip, Comparison Hub, Scorecard, ZIP-tier routing, Junk pricing profiles이 추가되면서, **사용자 경험은 "confusing contractor tool" → "homeowner-first decision advisor"로 근본적으로 전환**되었습니다.
>
> **Domain logic은 여전히 업계 최고 수준**(트럭 드라이버와 hauling company 사장이 극찬), **engagement funnel은 v1 대비 43% 향상** 예측, **데이터 인프라(ZIP-tier + analytics)는 marketplace 전환 준비 완료**.
>
> 남은 것은 **마지막 마일**: (1) helper text 버그 수정, (2) lead capture / vendor matching 구현, (3) 가격 범위 표면화, (4) hazmat disclaimer 추가. 이 4가지만 완료하면, 이 도구는 **미국 dumpster rental 시장에서 consumer-facing decision tool의 표준**이 될 수 있습니다.
>
> **v1의 평가: "The domain logic is best-in-class. The words wrapping it are worst-in-class."**
> **v2의 평가: "The decision experience is now best-in-class too. The revenue model is the only thing still missing."**
