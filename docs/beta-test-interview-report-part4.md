# PART 2 (계속): 도메인 전문가 그룹 — Expert #6 ~ #10

---

## 🖥️ Expert #6 — Derek Wu (33세, SEO/AEO 전문 마케터, San Francisco CA)

### 배경 및 상황
샌프란시스코 소재 디지털 에이전시의 Search Strategist. Google AI Overviews, Featured Snippets, Schema markup 최적화를 전문으로 하며, 연간 30개 이상의 client site에 대해 AEO(Answer Engine Optimization) 전략을 수립. 이 사이트를 "AEO 관점에서 Google의 AI가 이 페이지를 어떻게 해석할 것인가"라는 렌즈로 평가.

### 진입 의도
> "I want to audit this page's AEO readiness. Can Google's AI Overviews extract a direct answer from this page? Is the structured data solid? Will this page survive a zero-click search environment?"

### 페이지 전체 분석

**[Schema 구조 평가]**

> "JSON-LD가 5개나 있어: `WebApplication`, `FAQPage`, `WebSite`, `Organization`, `BreadcrumbList`. Schema 다양성은 좋은데..."
>
> "**`WebApplication` schema를 쓴 건 smart choice.** 대부분의 calculator 사이트는 'WebPage'만 쓰거든. 'WebApplication' + 'UtilitiesApplication'은 Google에게 '이건 단순 콘텐츠 페이지가 아니라 interactive tool이다'라는 signal을 줘. **이 구분이 AI Overviews에서 'try this calculator' 형태의 rich citation을 받을 확률을 높여.**"
>
> "`FAQPage` schema의 질문 3개가 페이지 body 안의 actual FAQ section과 **정확히 일치해.** 이건 정석. 구조화 데이터와 visible content가 일치해야 spam penalty를 피하거든."

**✅ Positive — Schema markup의 전문적 구성:**
WebApplication + FAQPage + BreadcrumbList의 조합은 Google의 structured data guidelines를 정확히 따른 것. 특히 FAQPage의 질문/답변이 페이지 body의 visible HTML과 동일한 것은 Google의 "content mismatch" penalty를 방지하는 모범 사례.

**[H1 태그 분석]**

> "H1이 `${model.pageTitle()}`로 dynamic rendering되어 있어. 이 사이트가 **JTE(Java Template Engine) 기반 SSR(Server-Side Rendering)**이라면 Google bot이 crawl할 때 이미 rendered HTML을 받으니까 문제없어. CSR(Client-Side Rendering)이었으면 H1이 빈 문자열로 crawl될 위험이 있었겠지만, **SSR이라면 OK.**"
>
> "**다만 H1의 실제 텍스트가 뭔지가 중요해.** 만약 'Dumpster Size Calculator'처럼 검색자의 query와 직접 match되는 텍스트면 최적. 'Decision OS' 같은 브랜딩 텍스트면 SEO 효과 감소."

**✅ Positive:** SSR 기반의 동적 H1은 SEO 관점에서 안전.
**🟡 Major:** H1의 실제 텍스트가 검색자의 intent query("dumpster size calculator", "what size dumpster do I need")와 정확히 매칭되어야 AEO 효과 극대화.

**[Content Structure 평가]**

> "FAQ가 `<details>`/`<summary>` 태그로 구현되어 있어. Google은 이 태그의 content를 **crawl하고 index함**. 이건 좋아."
>
> "근데 **FAQ가 3개밖에 없어.** AEO 관점에서는 'People Also Ask' 유형의 long-tail 질문을 10~15개 정도 커버해야 AI Overviews citation 확률이 높아져. 예를 들면:"
> - "How much does a 10-yard dumpster weigh when full?"
> - "Can you put concrete in a regular dumpster?"
> - "What is the weight limit for a 20-yard dumpster?"
> - "How many pickup truck loads fit in a 10-yard dumpster?"
>
> "이런 질문들이 추가되면 이 페이지가 'dumpster weight' 관련 query cluster 전체를 지배할 수 있어."

**🟡 Major — FAQ 확장 기회:**
현재 FAQ 3개는 quality는 높지만 quantity가 부족. "People Also Ask" 데이터를 기반으로 10~15개로 확장하면 long-tail keyword coverage가 대폭 증가.

**[어색한 점]**

> "**'Dumpster Decision OS'라는 title이 SEO적으로 문제야.** 아무도 'dumpster decision os'를 검색하지 않거든. title tag는 제일 중요한 SEO element인데, branded term을 넣으면 keyword match를 버리는 거야. **'Dumpster Size & Weight Calculator | Free Estimate'** 같은 게 100배 나아."
>
> "meta description에도 'Decision OS'보다는 사용자의 query를 반영하는 문구가 들어가야 해. 'Calculate dumpster size, weight, and overage risk for your project. Free, instant results.' 이런 식으로."

**🔴 Critical — 브랜드명의 SEO 기회비용:**
"Dumpster Decision OS"라는 브랜딩은 검색자의 어떤 query와도 match되지 않음. Title tag에 브랜드명 대신 target keyword를 배치하면 organic CTR이 즉시 개선될 수 있음.

### Derek Wu 종합 판정

| 항목 | 평가 |
|------|------|
| AEO 준비도 | ✅ 상위 10% — Schema 구성이 업계 최고 수준, SSR 기반으로 crawl 안정적 |
| 핵심 극찬 | WebApplication schema, FAQPage-body content 일치, SSR 렌더링 |
| 핵심 지적 | Title tag에 branded term 사용 → keyword match 손실, FAQ 수량 부족 |
| NPS (0~10) | 7 — "Schema work is A+. But the title tag and thin FAQ are leaving organic traffic on the table." |

---
---

## 🖥️ Expert #7 — Rachel Kim (36세, B2C Product Manager, New York NY)

### 배경 및 상황
뉴욕 소재 consumer marketplace 스타트업의 Senior PM. 연간 수백만 MAU(Monthly Active Users) 규모의 consumer product 경험. Conversion funnel 최적화, onboarding flow, friction 제거에 전문성. 이 페이지를 **"consumer product의 conversion funnel"** 관점에서 해부.

### 진입 의도
> "I want to evaluate this page as a product. Does the user flow make sense? Where are the friction points? What's the conversion goal, and is the page optimized for it?"

### 페이지 전체 분석

**[Conversion Goal 분석]**

> "이 페이지의 conversion goal이 뭐지? 크게 세 가지 가능성이 있어:"
> 1. **정보 제공 (Calculator):** 사이즈 추천 → 사용자가 알아서 업체에 연결
> 2. **Lead generation (Affiliate):** 사이즈 추천 → 업체 연결/매칭 → 수수료
> 3. **Direct booking:** 사이즈 추천 → 즉시 예약 → 결제
>
> "페이지를 봤을 때... **세 가지 중 어디에도 완벽히 속하지 않아.** (1)이면 calculator만 있으면 되는데 'Contact now' CTA와 'Online quote' CTA가 있어서 (2)처럼 보이기도 하고, (3)은 아닌 게 확실하고."
>
> "**이 정체성의 모호함이 이 페이지의 가장 큰 structural 문제야.** 사용자가 '계산 결과를 받았는데 그 다음에 뭘 해야 하지?'라고 느끼는 건, 페이지 자체가 conversion goal을 결정하지 못했기 때문이야."

**🔴 Critical — Conversion goal 미정의:**
페이지가 "calculator tool"인지 "lead generation platform"인지 "marketplace"인지 정체성이 정해지지 않아, **CTA의 방향성, 결과 화면의 구성, 사용자의 next step 모두가 모호한 상태**. Product management 관점에서 이것은 최우선적으로 해결해야 할 전략적 결정.

**[Onboarding Flow 평가]**

> "Step 1~4의 순서는 합리적이야. Project → Material → Quantity → Modifiers. 논리적 위계가 맞아."
>
> "**근데 Step 4('Context and modifiers')가 accordion으로 접혀있어.** 이건 OK인데 문제는 accordion의 제목이야. 'Persona and timing — Optional routing signal'. **'Routing signal'이 뭐예요?** 나는 PM을 10년 했는데 이 문구의 의미를 이해할 수 없어. 이건 내부 database column name이 frontend에 leak된 거야."

**🔴 Critical — "Routing signal"이 internal variable name의 UI 누수:**
PM 입장에서 이것은 **"개발팀이 UI copy review를 거치지 않고 직접 라벨을 작성했다"**는 명백한 증거. Product의 mature level이 "MVP 이하"로 인식되는 결정적 단서.

**[CTA 구조 평가]**

> "하단에 floating CTA가 2개: 'Contact now'와 'Online quote'. 그리고 result panel 안에 'Open share page'가 있어."
>
> "**CTA가 3개나 있는데, 위계(hierarchy)가 없어.** 어떤 게 primary CTA이고 어떤 게 secondary인지 시각적으로 구분이 안 돼. 사용자는 3개 중 뭘 눌러야 할지 모르고, 결국 아무것도 안 눌러."
>
> "게다가 'Contact now'가 이 사이트의 **일반 문의 페이지(`/about/contact`)로 연결돼.** 사용자가 이 CTA를 누를 때 기대하는 건 '실제 dumpster 업체와의 연결'인데, 실제로는 이 웹사이트의 contact form. **이건 bait-and-switch야.** 사용자 신뢰를 치명적으로 훼손해."

**🔴 Critical — CTA의 목적지 불일치(Bait-and-switch):**
"Contact now"라는 action-oriented CTA가 실제 service provider가 아니라 사이트 자체의 general inquiry로 연결되는 것은 **consumer product에서 가장 치명적인 trust violation** 중 하나. 사용자의 기대(dumpster 업체 연결)와 현실(사이트 문의 페이지) 사이의 gap이 너무 큼.

**[기타 관찰]**

> "Vendor checklist의 최초 문항인 'What included tons are in **this** quote?' — **'this'가 뭘 가리키는지 ambiguous해.** 이 사이트의 결과? 아니면 external vendor의 quote? PM으로서 이런 대명사 모호성은 user test에서 100% 혼란을 유발해. **'the vendor's quote'로 즉시 수정해야 해.**"

### Rachel Kim 종합 판정

| 항목 | 평가 |
|------|------|
| Product 평가 | Stage: Pre-PMF (Product-Market Fit 미달) — 도메인 로직은 강하나 conversion strategy가 미정의 |
| 핵심 지적 | Conversion goal 모호, CTA bait-and-switch, "Routing signal" 누수, "this quote" 대명사 |
| PM 관점 조언 | "먼저 '이 페이지의 conversion goal이 뭔가'를 팀이 결정하고, 그에 맞게 CTA를 재설계하세요" |
| NPS (0~10) | 4 — "Incredible domain logic trapped inside a product that doesn't know what it wants to be." |

---
---

## 🖥️ Expert #8 — Dan Morrison (41세, Utility App UX 디자이너, Seattle WA)

### 배경 및 상황
시애틀 소재 UX 에이전시의 Design Lead. Utility/calculator 앱 전문으로, mortgage calculator, tax estimator, home value tool 등 consumer-facing estimation tool의 UX를 다수 설계. "복잡한 계산을 단순한 UI로 전달하는 것"이 핵심 역량.

### 진입 의도
> "I design calculator UIs for a living. I want to see how this one handles the tension between 'professional accuracy' and 'consumer simplicity.'"

### 페이지 전체 분석

**[UI 구조 평가]**

> "전체 레이아웃은 좋아. 왼쪽에 입력(form), 오른쪽에 결과(result rail). **Split-pane dashboard 패턴**이야. Mortgage calculator에서 많이 쓰이는 proven layout. 입력을 바꾸면 실시간으로 오른쪽이 업데이트되는 구조는 감이 좋아."
>
> "**게이지(radial gauge) 3개는 시각적으로 striking.** Volume, Weight, Risk. 좋은 정보 구성이야. 근데..."

**✅ Positive — Split-pane dashboard 패턴:**
입력(좌) + 결과(우)의 split layout은 calculator/estimator에서 검증된 UX 패턴. 실시간 업데이트는 Interactivity를 높여 engagement를 유지.

> "**게이지의 색상 체계(color scheme)에 문제가 있어.** 세 게이지가 다 같은 색상 gradient를 쓰는 것 같은데, Volume 게이지가 높아지는 것은 **나쁜 게 아니잖아** — 그냥 부피가 많다는 정보일 뿐이야. 근데 Volume 게이지가 빨간색으로 차면 사용자는 '위험하다'고 인식해. **Volume은 informational(중립), Weight는 cautionary(주의), Risk는 critical(경고)으로 색상을 분리해야 해.**"

**🟡 Major — 게이지 색상의 의미론적 불일치:**
3개 게이지가 동일한 색상 gradient(초록→노랑→빨강)를 쓰면, "Volume이 높다 = 나쁘다"라는 잘못된 연상을 유발. 실제로는 Volume이 높아도 문제 없을 수 있음(가벼운 잡동사니가 많은 경우). 색상은 **의미에 따라 분리**되어야 함. 예: Volume = 파란색(neutral), Weight = 주황색(caution), Risk = 빨간색(alert).

> "'Verdict'라는 단어... 이건 UX 카피로서 completely wrong tone이야. Verdict는 법정에서 쓰는 말이잖아. **'Your Recommendation'이나 'Your Result'가 훨씬 자연스러워.** Calculator에서 사용자를 judge하면 안 돼."
>
> "'Decision board'도... 사용자는 decision을 내리지 않아. 사용자는 **recommendation을 받아.** 주체가 뒤바뀌어 있어. 도구가 결정을 내려주는 건데, '사용자가 결정한다(Decision board)'라는 naming이 잘못돼. **'Your Estimate'나 'Recommendation Summary'가 맞아.**"

**🟡 Major — UX 카피의 주체(Agency) 혼란:**
"Decision board"는 "사용자가 결정 내리는 곳"이라는 의미인데, 실제로 이 섹션에서 일어나는 건 "시스템이 추천하는 것". UX 카피에서 주체(누가 행동하는가)가 도치되면 사용자가 "내가 뭘 결정해야 하지?"라는 불안을 느낌.

> "helper text 동기화 문제는... **이건 QA 실패야.** 내가 이 프로젝트의 designer라면 이 bug를 P0(최고 우선)로 올릴 거야. 입력과 안내 텍스트가 sync되지 않는 것은 form UI의 기본 중 기본이거든."

### Dan Morrison 종합 판정

| 항목 | 평가 |
|------|------|
| UX 평가 | Layout: A, Copy: C-, Visual semantics: B- |
| 핵심 극찬 | Split-pane dashboard 패턴, 게이지 3분할 구성, 실시간 업데이트 |
| 핵심 지적 | 게이지 색상의 의미론적 분리 미흡, "Verdict/Decision" 카피 톤 불일치, helper text QA 실패 |
| NPS (0~10) | 6 — "Great layout, great information architecture. The copy reads like it was written by the backend developer who built the logic." |

---
---

## 🖥️ Expert #9 — Stephanie Cole (39세, Conversion Copywriter, Austin TX)

### 배경 및 상황
오스틴 소재 프리랜서 conversion copywriter. SaaS landing page 및 B2C product page의 카피라이팅을 전문으로 하며, 연간 50개 이상의 landing page A/B test를 진행. "단어 하나가 전환율을 200% 바꾼다"는 것을 매일 체감하는 직업. 이 페이지의 텍스트를 **"전환율(Conversion Rate)"** 관점에서 한 글자 한 글자 해부.

### 진입 의도
> "I write words that make people click. Show me what this page says and I'll tell you what it should say."

### 페이지 전체 카피 분석

**[히어로 섹션 카피 해부]**

현재 카피:
- Eyebrow: `Dumpster sizing tool`
- Sub: `Get a size recommendation, overage risk, and next action in about 60 seconds.`
- Card 1: `Decision first — Safe fit and budget fit, side by side.`
- Card 2: `Risk visible — Weight and allowance risk in ranges, not guesses.`
- Card 3: `Actionable output — Primary route, backup route, and vendor checklist.`

> **Stephanie의 분석:**
>
> "**이건 Feature copy야, Benefit copy가 아니야.** 전환율을 올리는 카피는 '이 도구가 뭘 하는가(Feature)'가 아니라 **'당신의 삶이 어떻게 나아지는가(Benefit)'**를 말해야 해."
>
> "하나씩 보자:"
>
> ❌ `"Decision first — Safe fit and budget fit, side by side."`
> - **Feature:** 안전한 fit과 예산 fit을 나란히 보여줌
> - **문제:** 'Decision first'가 뭘 의미하는지 일반인이 이해 못함. 'side by side'가 뭘 비교한다는 건지 unclear.
> - ✅ **Benefit으로 전환:** `"Right size, right price — No overpaying, no second trips."`
>
> ❌ `"Risk visible — Weight and allowance risk in ranges, not guesses."`
> - **Feature:** 무게와 허용량 리스크를 범위로 표시
> - **문제:** 'allowance', 'ranges'가 업계 용어. 고객은 '위험이 보인다(Risk visible)'는 말에 겁부터 먹음.
> - ✅ **Benefit으로 전환:** `"No surprise fees — See your overweight risk before you book."`
>
> ❌ `"Actionable output — Primary route, backup route, and vendor checklist."`
> - **Feature:** 주요 경로, 대안 경로, 벤더 체크리스트를 제공
> - **문제:** 'route'가 물리적 경로인지 선택 경로인지 모호. 'vendor checklist'이 뭘 위한 건지 맥락 없음.
> - ✅ **Benefit으로 전환:** `"Your game plan — Get the exact questions to ask when you call."`

**🔴 Critical — Feature copy vs Benefit copy:**
히어로 섹션의 3개 카드가 전부 Feature(기능) 설명. Consumer landing page에서 Feature copy는 전환율을 **떨어뜨림**. 사용자는 "이 도구가 뭘 하는지"가 아니라 **"이 도구가 나를 위해 뭘 해주는지"**에 반응함.

**[Vendor Checklist 카피]**

> "'What included tons are in this quote?' — 이건 **passive voice에 jargon의 조합이야.** 일반인은 'included tons'가 뭔지 모르고, 'this quote'가 누구의 quote인지 모르고, 질문 형식이라 뭘 해야 하는 건지 모르겠어."
>
> "이 섹션 전체에 **intro sentence가 없어.** '업체에 전화할 때 이 질문들을 하세요'라는 한 줄만 있었어도 맥락이 잡혀."
>
> ✅ **개선안:**
> `"📋 Before you book: Ask your hauler these 4 questions"`
> 1. `"How many tons are included in my rental?" (포함 톤수)`
> 2. `"What's the per-ton fee if I go over?" (초과 시 톤당 추가금)`
> 3. `"Do you require separate loads for heavy stuff like concrete?" (중량물 분리 배출 여부)`
> 4. `"If I need a swap, how fast can you do it?" (컨테이너 교체 소요 시간)"`

**🔴 Critical — Vendor Checklist의 intro copy 부재 + jargon:**
질문 리스트만 나열하고 "이게 뭘 위한 리스트인지" 설명하지 않으면, 사용자가 "이 사이트가 주는 quiz인가?" 또는 "이 사이트의 FAQ인가?"라고 오해함. intro sentence의 부재는 conversion-critical section에서의 심각한 카피 실수.

**["Routing signal" 완전 해부]**

> "'Optional routing signal' — **이건 UX copy가 아니라 database column name이야.** 내가 100개의 A/B test를 해봤는데, 이런 internal jargon이 user-facing UI에 나오면 bounce rate가 15~25% 올라가. 이건 카피 문제가 아니라 **프로세스 문제야** — 개발자가 UI label을 직접 쓰고, copywriter review 없이 배포한 거지."
>
> ✅ **대체안:** `"Personalize your result"` 또는 `"Help us tailor your estimate"` 또는 그냥 삭제.

### Stephanie Cole 종합 판정

| 항목 | 평가 |
|------|------|
| 카피 품질 | D+ — Feature copy 남발, internal jargon 누수, intro copy 부재 |
| 핵심 극찬 | 없음. 카피라이터 입장에서 이 페이지는 전면 재작성(rewrite) 대상 |
| 핵심 지적 | Feature→Benefit 전환 필요, "routing signal" 즉시 삭제, Vendor checklist에 intro 추가, "this quote"→"the vendor's quote" |
| 전환율 예측 | "현재 카피로는 qualified visitor의 30~40%가 히어로 섹션에서 bounce. Benefit copy로 전환하면 20~30%는 줄일 수 있어." |
| NPS (0~10) | 3 — "The domain expert who built the logic is brilliant. The person who wrote the words is not a copywriter." |

---
---

## 🖥️ Expert #10 — Andrew Park (44세, CX/Revenue 데이터 분석가, Chicago IL)

### 배경 및 상황
시카고 소재 B2C marketplace의 CX Analytics Lead. Revenue attribution, funnel conversion, 그리고 user journey의 drop-off point를 데이터로 분석. 이전에 home services marketplace(HVAC, plumbing, roofing)에서 lead generation model을 설계한 경험이 있음. 이 페이지를 **"수익화 퍼널(Revenue funnel)"** 관점에서 분석.

### 진입 의도
> "I want to understand the revenue model of this page. Where does the money come from? Where does the user go after the calculation? Is there a clear monetization path?"

### 페이지 전체 분석

**[수익화 모델 분석]**

> "이 페이지의 수익화 경로를 역추적해보면..."
>
> "**CTA 1: 'Contact now'** → `/about/contact`로 연결. 이건 general inquiry. 수익화와 무관."
> "**CTA 2: 'Online quote'** → `#estimate-form`으로 연결. 이건 같은 페이지의 form으로 돌아감. 수익화와 무관."
> "**CTA 3: 'Open share page'** → share 기능. 간접적 traffic generation이지만 직접 수익 아님."
>
> "**이 페이지에는 수익화 경로가 없어.** 아무도 비용을 지불하지 않고, 어떤 transaction도 일어나지 않고, 어떤 lead도 capture되지 않아. 이건 charity tool이야."

**🔴 Critical — 수익화 경로(Revenue path) 완전 부재:**
SEO로 traffic을 모으고, 뛰어난 domain logic으로 사용자를 engage시키고, 그리고... 유저를 **그냥 보내줌**. 이것은 home services 업계에서 가장 아까운 형태의 traffic waste. Angi, HomeAdvisor, Thumbtack 같은 회사가 동일한 traffic에 CPC $3~$15를 지불하는데, 이 사이트는 그 traffic을 무료로 날려버리고 있음.

**[Lead capture 가능성 분석]**

> "만약 이 페이지가 결과 화면에서 **'Complete my estimate → Enter your zip code to see local prices'** 같은 lead capture step을 추가한다면?"
>
> "사용자가 이미 project type, material, quantity를 모두 입력한 상태잖아. **이 데이터는 hauling company에게 gold-level qualified lead야.** Project type(what), Material(weight risk), Quantity(size), Timing(urgency), Persona(who)... **이 5개 데이터 포인트를 가진 lead는 closing rate이 일반 form lead보다 3~5배 높아.**"
>
> "현재 이 데이터가 localstorage나 server에 저장되고 있는지 모르겠지만, **만약 Zip code + email만 추가로 받으면, 이 lead를 지역 hauler에게 $5~$15/lead로 팔 수 있어.** 월 10,000 visit 기준으로 5% conversion이면 500 leads × $10 = **$5,000/month revenue.**"

**🟡 Major — 데이터 활용의 기회 누수:**
사용자가 4단계에 걸쳐 상세히 입력한 프로젝트 데이터(type, material, quantity, timing, persona)가 **어떤 비즈니스 가치로도 전환되지 않고 브라우저에서 사라짐**. 이것은 "user intent data"의 관점에서 백만 달러짜리 기회 손실.

**[Funnel drop-off 예측]**

> "이 페이지의 예상 funnel을 그려보면:"
>
> ```
> 100명 Landing
>  → 70명 Step 1 완료 (Project scope 선택 — 대부분 성공)
>  → 55명 Step 2 완료 (Material 선택 — 일부 혼란으로 이탈)
>  → 40명 Step 3 완료 (Quantity + Unit — Unit 과부하로 상당수 이탈)
>  → 35명 Step 4 + Calculate (대부분 "routing signal"을 무시하고 넘어감)
>  → 35명 결과 확인
>  → ???명 Next action ← 여기서 funnel이 끝남. Revenue = $0.
> ```
>
> "**Step 3에서 15명(약 27%)이 이탈하는 것이 가장 큰 문제.** Unit 선택지의 과부하와 helper text 불일치가 원인. 그리고 결과를 본 35명 중에 **누구도 돈을 내거나, lead를 남기거나, 업체와 연결되지 않음.**"

**🔴 Critical — Dead-end funnel:**
최적화된 calculator funnel은 "계산 결과 → lead capture → monetization"의 3-step 구조를 가져야 하는데, 현재는 "계산 결과"에서 funnel이 **사망(dead-end)**. 아무리 좋은 domain logic이라도 funnel에 monetization layer가 없으면 sustainable business가 될 수 없음.

**[Vendor Checklist의 전략적 문제]**

> "Vendor checklist가 '업체에 이런 걸 물어보세요'라는 가이드인 건 교육적으로 좋아. **근데 이건 사용자를 이 사이트 밖으로 내보내는 행위야.** '이 질문 리스트를 가지고 알아서 업체를 찾으세요'라고 하면, 이 사이트에서의 사용자 여정이 끝나는 거잖아."
>
> "만약 checklist 대신 **'Match me with a hauler who meets these criteria'** 버튼이 있었다면? 사용자의 입력 데이터(project + material + quantity + timing)에 맞는 지역 업체를 자동 매칭해주는 거야. **이게 바로 Angi/Thumbtack이 하는 일이고, 그들이 billion-dollar company인 이유야.**"

**🔴 Critical — Vendor checklist가 anti-conversion 요소로 작동:**
교육적 가치는 인정하지만, 수익화 관점에서 Vendor checklist는 **사용자를 사이트 밖으로 내보내는 exit ramp**. "알아서 찾으세요"보다 "우리가 연결해드릴게요"가 revenue path를 만드는 유일한 방법.

### Andrew Park 종합 판정

| 항목 | 평가 |
|------|------|
| 수익화 평가 | 🔴 Revenue = $0. Funnel dead-end. Gold-level user intent data가 버려지고 있음 |
| 핵심 극찬 | 사용자 입력 데이터의 quality가 매우 높음 (5 data points per session) — lead gen에 즉시 활용 가능 |
| 핵심 지적 | Lead capture 부재, CTA 목적지 불일치, Vendor checklist가 exit ramp로 기능 |
| 수익화 제안 | "Zip code + email lead capture → 지역 hauler에게 $5~15/lead 판매 → 월 $5K+ 가능" |
| NPS (0~10) | 5 — "This is the best free tool no one is making money from. Fix the funnel and you have a real business." |

---
---

# 📊 전체 테스터 20인 종합 결론

## 일반 사용자 10인 패턴

| 공통 이탈 요인 | 빈도 |
|---|---|
| helper text 미동기화 (Unit 변경 시 pickup truck 설명 유지) | 5/10 지적 |
| 가격 정보 부재 | 6/10 지적 |
| "Routing signal" 이해 불가 | 7/10 지적 |
| CTA(Contact now) 목적지의 기대 불일치 | 4/10 지적 |
| "Verdict/Decision board" 톤의 위압감 | 5/10 지적 |
| Unit 선택지 과부하 (비관련 단위 노출) | 6/10 지적 |
| Material 단일 선택 한계 (혼합 폐기물 표현 불가) | 5/10 지적 |
| "this quote" 대명사 오류 | 4/10 지적 |

## 도메인 전문가 10인 패턴

| 공통 극찬 포인트 | 빈도 |
|---|---|
| Wet load 옵션의 현장 정확성 | 8/10 극찬 |
| Volume vs Weight 분리 게이지 | 7/10 극찬 |
| Heavy material 세분화 (shingles, concrete, dirt 분리) | 6/10 극찬 |
| Fill-ratio / clean-load 개념 포함 | 5/10 극찬 |
| Included tons 입력 → overage risk 시각화 | 4/10 극찬 |

| 공통 지적 포인트 | 빈도 |
|---|---|
| Hazmat(asbestos/lead) 사전 경고 부재 | 3/10 지적 |
| "Routing signal" 의미 불명 | 5/10 지적 |
| "Dumpster Decision OS" 네이밍 과장 | 4/10 지적 |
| Conversion goal / Revenue path 부재 | 3/10 지적 (PM, CRO, Data 전문가) |
| Feature copy → Benefit copy 전환 필요 | 3/10 지적 (Copywriter, UX, PM) |

## 🏆 최종 한 줄 평가

> **"The domain logic is best-in-class. The words wrapping it are worst-in-class."**
>
> 이 사이트의 백엔드 도메인 로직(무게 계산, 습윤 보정, 톤수 허용량, fill-ratio)은 업계 25년 베테랑 트럭 드라이버마저 감탄하는 수준입니다. 그러나 그 로직을 감싸는 **프론트엔드의 단어들**(Decision OS, Routing signal, Verdict, Actionable output)과 **전환 구조**(CTA 목적지, 가격 부재, leade capture 없음)가 모든 가치를 차단하고 있습니다.
>
> **백엔드는 건드리지 마세요. 프론트엔드의 단어와 전환 퍼널만 고치면, 이 도구는 미국 dumpster rental 시장에서 독보적인 위치를 차지할 것입니다.**
