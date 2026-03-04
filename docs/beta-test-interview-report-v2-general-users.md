# 🧪 Dumpster Decision Engine v2 — 베타 서비스 인터뷰 리포트 (일반 사용자)

> **테스트 대상:** B2C Decision Engine Refactor v2 완료 후 메인 페이지 (`calculator/index.jte`)
> **테스트 일자:** 2026-03-04
> **참여 인원:** 일반 사용자(General Users) 5명 / 도메인 전문가(Domain Experts) 5명
> **v1 대비 주요 변경 사항:**
> - Hero 섹션: homeowner-first 언어로 전면 개편
> - Decision strip: 4개 빠른 결정 경로 추가 (cheapest / easiest / heavy / fast)
> - Project Scope: "Moving cleanout", "Furniture disposal", "General junk removal" 신규 추가
> - Persona accordion: "Optional routing signal" → "Choose your role" 변경
> - CTA: "Get dumpster quotes" / "Run live estimate" 명확화
> - Comparison hub: Dumpster vs Junk removal 비교 페이지 신설
> - Decision scorecard: cost/speed/labor/safety 4축 점수판 추가
> - ZIP code 입력: 지역 기반 시장 가격 반영
> - "Mixed C&D" → "Mixed construction debris (C&D)" 약어 풀이
>
> **평가 방식:** Think-aloud protocol (구술 사고법)
> **심각도 기준:**
> - 🔴 Critical: 즉시 이탈 또는 서비스 신뢰 상실
> - 🟡 Major: 혼란/불편이 있으나 계속 사용 가능
> - 🟢 Minor: 약간의 어색함, 사용에 지장 없음
> - ✅ Positive: 기대 이상의 긍정적 반응

---

# PART 1: 일반 사용자 그룹 (General Users) — 5명

---

## 👤 User #1 — Emily Chen (31세, 콘도 소유 첫 이사, Seattle WA)

### 배경 및 상황
시애틀의 2BR 콘도 소유자. 결혼하면서 남편 집으로 이사하게 되어 콘도에 남겨둘 수 없는 가구들(IKEA 소파, 오래된 매트리스, 책상 2개, 박스 약 20개)을 한꺼번에 처분해야 합니다. 이전에 dumpster를 빌린 적이 없고, 친구가 "요즘은 인터넷에서 사이즈 먼저 알아보고 나서 업체에 전화해"라고 조언. Google에서 "dumpster rental size calculator" 검색.

### 진입 의도
> "I'm moving out and need to get rid of a bunch of furniture and boxes. I want to know what size dumpster I need before calling anyone."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션]**

> "'Find your best disposal option' — OK, 이건 내가 찾는 거야. Disposal option을 찾는 거니까."
>
> "'Choose the safer route in about 60 seconds: dumpster, junk removal, or staged multi-haul.' — **오, junk removal도 비교해주는 거야?** 나는 dumpster만 생각했는데, junk removal이 더 나을 수도 있다는 거지? 이건 좀 신선한데."
>
> "'Answer three homeowner-first questions: which route wins, how to avoid overage, and what size is safe on the first try.' — **homeowner-first**라는 말이 나한테 맞는 것 같아. 나는 contractor가 아니라 그냥 집주인이니까."

**✅ Positive — v1 대비 Hero 카피 개선 확인:**
v1에서 "Actionable output", "Decision first" 등 B2B SaaS 톤이 지적되었던 베네핏 카드가 "See your best next move before shopping quotes" 등 소비자 친화적 표현으로 변경됨. Emily처럼 처음 dumpster를 빌리는 사용자에게 "homeowner-first"라는 단어가 **"이 사이트는 나를 위한 것"**이라는 신호를 줌.

**🟡 Major — 베네핏 카드 타이틀 잔존 이슈:**
다만 카드 타이틀인 "Decision first", "Risk visible", "Actionable output"은 여전히 B2B 톤이 남아 있음. 하위 설명문은 개선되었으나 **타이틀 자체**가 여전히 기술적.

**[Decision Strip]**

> "**아래에 4개 버튼이 있어!** 'I want the cheapest route', 'I want the easiest route', 'I have heavy material', 'I need it gone fast'. **이거 완전 내 상황이야!** 'I want the easiest route'를 누르면 되는 거 아냐?"
>
> "눌렀더니 — 아래 폼이 자동으로 채워졌어! **'Moving / apartment cleanout' + 'Household junk' + 'Pickup load × 6'이 자동 선택**되었어. **이건 진짜 편해.** 내가 하나씩 뭘 선택해야 할지 고민할 필요가 없잖아."

**✅ Positive — Decision Strip이 초보 사용자의 진입 장벽을 획기적으로 낮춤:**
v1에서 Sarah Mitchell(User #1)이 "이 사이트는 contractor용"이라고 느끼며 Step 1에서 막혔던 문제가, Decision Strip 하나로 해결됨. **"나는 잘 모르지만 '제일 쉬운 방법'을 원한다"는 사용자의 본능적 선택**을 4개 pill이 정확히 포착.

**[Step 1 — Project Scope]**

> "Decision Strip이 이미 'Moving / apartment cleanout'을 선택해줬네! **이 옵션이 v1에는 없었을 것 같은데?** 'Moving cleanout'이 있으면 나 같은 이사 정리 사람이 바로 매칭이 되잖아. Perfect."
>
> "'Furniture or appliance disposal'도 있고, 'General junk removal'도 있네. 이 3개만 있어도 **일반인이 쓸 수 있는 옵션이 충분해.**"

**✅ Positive — v1 피드백 반영 확인 (Project Scope 확장):**
v1에서 가장 빈번히 지적된 "생활 폐기물 시나리오 누락" 이슈가 3개 신규 옵션으로 해결됨. 13개 프로젝트 중 5개(Garage cleanout, Estate cleanout, Moving cleanout, Furniture disposal, General junk removal)가 비건축 생활 시나리오를 커버.

**[Step 3 — Quantity and Unit]**

> "기본값이 6 Pickup load로 되어있네. 내 stuff가 그 정도 될까? 잘 모르겠어... 근데 helper text가 **'Standard 8ft pickup truck bed, level full (~2.5 yd³)'**라고 나와. 나 pickup truck이 없어서 이 기준이 감이 안 와."
>
> "'Contractor bag'이 있네! **이게 더 이해하기 쉬울 것 같아.** 검은 쓰레기봉투 몇 개 분량인지는 대략 짐작이 가니까. 20개? 25개?"
>
> "근데 'Contractor bag'을 눌렀는데... **helper text가 아직도 'Standard 8ft pickup truck bed'라고 나와 있어.** 내가 방금 Contractor bag을 선택한 건데?"

**🔴 Critical — helper text 미동기화 (v2에서도 미수정):**
v1에서 **5명의 테스터**가 반복 지적했던 helper text 동기화 문제가 v2에서도 그대로 존재. 이 이슈는 v1 보고서에서 Priority 0으로 분류되었으나 코드 상으로는 JavaScript에 `unitHints` 객체가 존재하면서도 HTML 기본값이 pickup_load 설명으로 고정되어 있음. **v2의 가장 큰 미해결 기술 부채.**

**[Step 4 — Context and Modifiers]**

> "Accordion을 열었더니 'Persona and timing' 아래에 **'Choose your role'**이라고 적혀있어. v1에서는 'Optional routing signal'이었다고 들었는데, **'Choose your role'이 훨씬 자연스러워.** 무슨 뜻인지 바로 이해됨."
>
> "'ZIP code (optional)' — **이 사이트에서 지역 가격을 반영해준다고?** 30339를 넣어볼게. 아 근데 결과에 가격이 직접 나오는 건 아닌 것 같은데... ZIP을 넣으면 뭐가 달라지지?"

**✅ Positive — "Routing signal" → "Choose your role" 개선:**
v1에서 20명 중 0명이 이해하지 못했던 "Optional routing signal"이 "Choose your role"로 변경되어 자연스럽게 수용됨.

**🟢 Minor — ZIP code 입력의 효과 불투명:**
ZIP을 입력해도 결과 화면에서 "지역 가격이 반영되었는지" **명시적 피드백이 없어** 사용자가 이 입력의 효과를 체감하지 못함.

**[결과 확인 — Decision Board]**

> "게이지가 3개 — Volume이 중간, Weight가 낮아, Risk도 낮아. 이건 좋은 신호인 것 같아!"
>
> "밑에 **'Decision summary'**라고 나오네. '결과 요약'이라는 뜻이지? 사이즈 추천이 나왔어. 근데... **여전히 가격이 없어.** 이 사이즈가 Seattle에서 대략 얼마인지 알 수 없으면 다음 단계로 못 넘어가잖아."
>
> "'Get dumpster quotes' 버튼이 있어! **이건 업체에 연결해주는 건가?** 눌러볼게... 아, `/about/contact` 페이지로 갔네. **실제 Seattle 업체가 아니라 이 사이트의 문의 페이지야.** 기대가 좀 무너졌어."

**🟡 Major — 가격 정보 부재 (v1 동일):**
v2에서 ZIP code 입력과 market_tier 라우팅이 추가되었지만, 결과 화면에서 **지역 기반 가격 범위**가 사용자에게 직접 표시되지는 않음. 비용 비교 엔진이 내부적으로 동작하지만 사용자 가시성(visibility)이 부족.

**🟡 Major — CTA 목적지 (부분 개선):**
v1의 "Contact now"가 "Get dumpster quotes"로 label이 개선되었으나, **목적지가 여전히 `/about/contact`**. 라벨이 "quotes를 받겠다"고 약속하면서 실제로는 일반 문의 페이지로 보내는 것은 v1보다 **기대-현실 gap이 더 커질 수 있음**.

### Emily Chen 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | △ 부분 달성 — 사이즈 추천 성공, 가격·업체 연결 미달성 |
| v1 대비 개선 체감 | ✅ Decision Strip + Moving cleanout 옵션 + "Choose your role" — 진입 장벽 대폭 하락 |
| 잔존 이슈 | helper text 미동기화, 가격 부재, CTA 목적지 불일치 |
| 재방문 의사 | △ "The quick buttons are great, but I still had to go to Yelp to actually find a company." |
| NPS (0~10) | 6 (v1 Sarah 대비 +3) — "Way easier to start than most calculator sites. But the end feels incomplete." |

---
---

## 👤 User #2 — Marcus Johnson (44세, 교외 주택 소유자/DIYer, Atlanta GA)

### 배경 및 상황
조지아주 애틀랜타 교외 4BR 주택 소유. 뒤뜰의 오래된 나무 fence(60 linear feet)를 직접 해체하고 새 fence를 설치하는 DIY 프로젝트 진행 중. 이전에 한 번 dumpster를 빌려본 경험이 있어서 대략적인 프로세스는 알지만, 정확한 사이즈와 무게 초과 리스크를 사전에 확인하고 싶어서 Google에서 "fence demolition dumpster size" 검색.

### 진입 의도
> "I'm tearing down an old cedar fence. Did this once before with a deck — got hit with a $150 overage charge because the wood was wet from rain. I want to make sure that doesn't happen again."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션 + Decision Strip]**

> "'Choose the safer route in about 60 seconds' — 좋아, 빨라서 좋아."
>
> "Decision Strip에 **'I have heavy material'**이 있네. 근데 내 fence는 cedar wood니까 heavy는 아니거든... 'I want the cheapest route'가 더 맞을 것 같아. 근데 이걸 누르면 **Garage cleanout + Household junk**으로 세팅되네? **내 fence는 garage cleanout이 아닌데?**"

**🟡 Major — Decision Strip 프리셋의 시나리오 커버리지 한계:**
4개 pill이 대표적 의도를 잘 포착하지만, "fence demolition like mine"처럼 **특정 DIY 프로젝트**에는 프리셋이 정확히 매칭되지 않음. "cheapest route" = Garage cleanout이라는 매핑이 모든 cost-conscious 사용자에게 맞지는 않음.

**[Step 1 — Project Scope]**

> "'Deck demo' — fence도 wood structure이니까 deck demo가 제일 비슷하겠지? 근데 fence demolition이 따로 있으면 더 좋겠어. 뭐 이 정도면 충분해."

**[Step 2 — Material]**

> "'Decking wood'를 골랐어. Cedar fence도 wood니까 밀도가 비슷할 거야."

**[Step 3 — Quantity]**

> "60 linear feet × 6ft 높이 = 360 sqft. **'sqft @ 1in'**을 선택해볼까... fence picket이 **3/4인치 두께**니까 1인치가 가장 가까워."
>
> "**오, 이 단위 시스템은 진짜 smart해.** 다른 calculator는 그냥 '작은/중간/큰 프로젝트' 3개 버튼인데, 여기는 내 실제 면적 넣으면 정확h 무게가 나올 거잖아."
>
> "근데 helper text... 또 **'Standard 8ft pickup truck bed'**라고 나와. 나 sqft를 선택한 건데."

**✅ Positive — sqft 면적 입력 기능:**
v1 Carlos Rivera와 동일한 반응. 면적 기반 입력은 DIY 사용자에게 이 사이트의 **가장 강력한 차별 포인트**.

**🔴 Critical — helper text 미동기화 (재확인).**

**[Step 4 — Advanced Modifiers]**

> "'Wet load' 체크박스! **이거 내가 이 사이트에 온 이유 중 하나야!** 지난번 deck 프로젝트에서 비 맞은 wood 때문에 overage 맞았거든. 이번에는 dry weather에 할 거지만, 만약 비가 오면 어떻게 되는지 미리 확인해보고 싶어."
>
> "Wet load 체크하고 다시 돌렸더니 **Weight 게이지가 확 올라갔어! 이거야!** Dry vs Wet 비교가 이렇게 직관적으로 보이는 건 처음이야."
>
> "'Included tons'에 **2.0을 넣었어** — 지난번 업체가 2톤을 포함해줬거든. 넣었더니... **Risk 게이지가 올라가면서 경고가 뜨네!** 'Wet load일 때 포함 톤수 초과 가능성이 있다'는 거지? **이 정보를 미리 알면 업체에 전화할 때 '3톤 포함으로 올려달라'고 협상할 수 있잖아!**"

**✅ Positive — Wet load + Included tons 조합의 킬러 기능:**
v1 Carlos Rivera의 "Aha Moment"가 Marcus에서도 정확히 재현됨. **이 기능 조합은 경쟁사에 없는 유일무이한 차별 포인트**로, 경험 있는 DIY 사용자에게 즉각적 가치를 제공.

**[결과 — Comparison 섹션]**

> "결과 아래에 **'Compare dumpster vs junk removal'** 링크가 있어! 클릭해봤더니... **Comparison hub 페이지로 갔어.** 여기서 dumpster와 junk removal 비용을 나란히 비교할 수 있네?"
>
> "**Priority 토글이 있어!** 'Lowest cost', 'Fastest completion', 'Least effort', 'Heavy-load safety'. 이걸 누르면 카드가 재정렬돼. **이건 진짜 유용해.** Lowest cost로 누르면 dumpster가 1위, Fastest로 누르면 junk removal이 1위... 상황에 따라 최적이 다르다는 걸 시각적으로 보여주는 거네."

**✅ Positive — Comparison Hub의 priority toggle:**
v2 신규 기능. 사용자가 **자신의 우선순위에 맞게** 비교 결과를 재정렬할 수 있는 인터랙션은 "이 사이트가 나의 상황을 존중한다"는 인상을 줌.

### Marcus Johnson 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | ✅ 대부분 성공 — 사이즈+무게 리스크+wet load 비교 모두 달성. 가격만 부재 |
| v1 대비 개선 체감 | ✅ Comparison hub, Decision strip, "Choose your role" 모두 체감 |
| 핵심 성공 요인 | Wet load + Included tons + Comparison priority toggle |
| 재방문 의사 | ✅ "Bookmarking this. The wet load calculator alone is worth it." |
| NPS (0~10) | 8 — "If they added price ranges and fixed that helper text bug, this would be a 10." |

---
---

## 👤 User #3 — Aisha Williams (38세, 싱글맘, 이사 예정, Houston TX)

### 배경 및 상황
텍사스주 휴스턴. 4살, 7살 두 아이를 키우며 2BR 아파트에서 더 큰 3BR로 이사 예정. 옮기지 않을 물건들(아이들의 outgrow한 가구, 오래된 가전, 잡동사니)을 한 번에 처분해야 함. 시간과 예산이 모두 빠듯하며, "가장 적은 노력으로 가장 빠르게 처분하는 방법"이 최우선. 이전에 dumpster를 빌린 적 없음. 모바일(iPhone 13)로 접속.

### 진입 의도
> "I'm a busy single mom. Moving next week. I have a ton of stuff to get rid of and zero time to deal with it. I just want the fastest, easiest option. I don't even care if it costs a little more."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션 — 모바일]**

> "모바일에서 히어로가 깔끔하게 보여. **'Find your best disposal option'** — OK."
>
> "Decision Strip의 **'I need it gone fast'**가 딱 나야! 이거 눌러볼게."

**[Decision Strip → 자동 세팅]**

> "눌렀더니 **'General junk removal' + 'Household junk' + '48 hours'**로 세팅됐어! **어머, 내가 뭘 선택할지 다 알고 있네!** 나는 일반 잡동사니를, 가능한 빨리 없애고 싶은 사람이니까 이게 딱 맞아."
>
> "근데 speed priority로 세팅되니까... 결과에서 **junk removal이 dumpster보다 나을 수도 있다고 나올까?** 나는 솔직히 junk removal이 뭔지도 잘 모르는데."

**✅ Positive — "I need it gone fast" → 48 hours + General junk removal 프리셋:**
시간 압박이 심한 사용자에게 **원클릭으로 최적 설정**을 제공. v1에서는 이런 사용자가 Step 1부터 막혔으나, v2에서는 Decision Strip이 **초보 사용자의 의사결정 부담을 제거**.

**[결과 확인 — 모바일]**

> "모바일 하단에 **result dock**이 떠있어! '추천 사이즈 보기' 버튼이 있어서... 눌렀더니 결과 섹션으로 스크롤돼."
>
> "결과에 **Decision scorecard**가 있네! 4개 막대 — **Cost route, Speed route, Labor effort, Safety margin**. Speed가 높게 표시되어있어! Junk removal이 speed에서 이기고, dumpster가 cost에서 이긴다... **이 비교가 한눈에 보여!**"
>
> "근데... 결과를 다 보고 나서 **'그래서 내가 지금 뭘 해야 해?'**라는 느낌은 여전해. 'Get dumpster quotes'를 눌러도 실제 업체가 아니잖아. **'Houston에서 내일 와줄 수 있는 junk removal 업체'**를 직접 연결해줬으면 좋겠는데."

**✅ Positive — Decision Scorecard의 직관적 비교:**
v2 신규 기능. 4축 점수판이 "왜 이 옵션이 당신에게 더 나은지"를 시각적으로 설명하여, 의사결정 근거를 명확하게 전달.

**🟡 Major — Urgent 사용자의 next-step 단절 (v1과 동일):**
"48 hours" + "I need it gone fast"를 선택한 사용자에게 실제 업체 연결이 제공되지 않는 것은 v1의 Bob Williams 이슈와 동일. Decision scorecard가 "junk removal이 더 빠르다"고 알려줘도, **실제로 junk removal을 예약할 수 있는 경로가 없으면 정보의 가치가 반감.**

**[Vendor Checklist]**

> "밑에 리스트가 있는데... **'What included tons are in this quote?'** — 'this quote'? 이 사이트에서 quote를 받은 적이 없는데? **누구의 quote?** 아.. 업체 quote를 말하는 건가?"

**🟡 Major — "this quote" 대명사 오류 (v1 미수정):**
v1에서 다수 테스터가 지적했으나 여전히 미수정.

### Aisha Williams 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | △ 부분 달성 — 사이즈+옵션 비교 OK, 실제 예약/연결 불가 |
| v1 대비 개선 체감 | ✅ Decision Strip "I need it gone fast" 원클릭 진입이 게임 체인저 |
| 핵심 성공 요인 | Decision Strip → 자동 세팅 → Scorecard 비교 → 1분 내 정보 획득 |
| 잔존 이슈 | urgent 사용자 next-step 단절, "this quote" 대명사, 가격 부재 |
| 재방문 의사 | △ "I'd come back if they connected me to actual companies nearby." |
| NPS (0~10) | 5 — "The quick start buttons saved me so much time. But I still had to Google 'junk removal Houston' after." |

---
---

## 👤 User #4 — Thomas Greene (67세, 은퇴 교사, 유품 정리, Jacksonville FL)

### 배경 및 상황
플로리다주 잭슨빌. 아내가 작년에 세상을 떠났고, 41년 살던 4BR 집을 downsizing하기 위해 비우는 중. 아들이 dumpster 빌리는 것을 도와주기로 했고, 아들이 이 사이트 링크를 보내줌. iPad로 접속. 기술에 약하지만 이전 세대(v1)보다는 좀 더 차분한 톤이면 직접 해볼 수 있다고 생각함.

### 진입 의도
> "My son sent me this link. He said to pick the right size before he calls the rental company. I don't want to bother him with every little question, so I'll try to do this myself."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션]**

> "'Find your best disposal option.' OK, simple enough."
>
> "'Choose the safer route in about 60 seconds' — **safer route? 안전한 경로?** 위험한 경로도 있다는 건가? 뭐가 위험한데?"
>
> "Decision Strip 4개... **'I want the easiest route'**가 있네. 이거 나한테 딱인데? 눌러볼까?"

**[Decision Strip 사용]**

> "'I want the easiest route'를 눌렀어. 그러니까 아래가 자동으로 채워졌어! **'Moving / apartment cleanout' + 'Household junk'**... 음, 나는 이사하는 게 아니라 집을 비우는 건데... 'Estate cleanout'이 더 맞을 것 같아."
>
> "아래로 스크롤해서 **직접 'Estate cleanout'을 골랐어.** 이건 나와 Martha의 물건을 정리하는 거니까. Decision Strip이 자동으로 해줬지만, 내 상황에 완벽히 맞지는 않아서 **수동 조정이 필요했어.** 근데 수동 조정이 가능한 건 좋아!"

**✅ Positive — Decision Strip + 수동 조정 가능의 유연성:**
Decision Strip으로 빠르게 시작하고, 필요하면 개별 필드를 수동 조정할 수 있는 **하이브리드 접근**. Thomas처럼 "자동 세팅이 거의 맞지만 약간 다른" 사용자에게 적합.

**🟢 Minor — "Estate cleanout"용 Decision Strip 부재:**
현재 4개 pill 중 estate/downsizing에 직접 매칭되는 것이 없음. "I need help clearing a house" 같은 5번째 pill이 있었다면 Thomas를 더 정확하게 포착.

**[Step 3 — Quantity]**

> "Pickup load 6이 기본값... 41년 치 물건이면 6보다 많을 수도 있는데... **10으로 올려봤어.** 근데 내 Chevy Avalanche가 8ft bed인지 잘 모르겠어."
>
> "**Helper text가 너무 작아서 iPad에서 잘 안 보여.**"

**🟡 Major — iPad에서의 hint 가독성 (v1 Dorothy 이슈 동일):**
v1에서 Dorothy Henderson이 지적한 노년층 iPad 가독성 이슈가 v2에서도 개선되지 않음.

**[결과 확인]**

> "게이지가 3개 나왔어. Volume이 높고, Weight는 낮고, Risk도 낮아. **색깔이 다 초록색이니까... 이건 괜찮다는 거지? 안전하다는 거지?** v1에서는 빨간색이 무서웠다고 들었는데, 내 결과는 초록이야."
>
> "**'Decision summary'**라고 나왔어. 'Verdict'가 아니라 'summary'? **이건 무서울 게 없는 단어야.** 좋아."
>
> "'Get dumpster quotes' — 이걸 아들한테 보여주면 되겠다! ... 눌렀더니 이 사이트 문의 페이지야. **아 이건 아들한테 링크를 보내는 용도가 아니구나.**"
>
> "근데 위에 **'Open share page'**라는 링크가 있네! 이걸 누르면 — 아, **결과를 공유할 수 있는 페이지**가 생기네! **이거다!** 이 링크를 아들한테 문자로 보내면 아들이 바로 결과를 보고 업체에 전화할 수 있어!"

**✅ Positive — Share 기능의 가족 위임 활용:**
노년층 사용자가 "결과를 가족에게 공유"하는 것은 매우 자연스러운 use case. 'Open share page' 기능이 이를 정확히 지원. v1의 Dorothy는 "딸한테 전화해야겠다"로 끝났지만, v2의 Thomas는 **스스로 결과를 공유**할 수 있음.

### Thomas Greene 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | ✅ 성공 — 사이즈 추천을 받고, share page로 아들에게 전달 |
| v1 대비 개선 체감 | ✅ "Decision summary"가 "Verdict"보다 편안, Decision Strip으로 빠른 시작, Share 기능 활용 |
| 잔존 이슈 | iPad hint 가독성, Estate cleanout 전용 Decision Strip 부재 |
| 재방문 의사 | ✅ "I'll use it again when I clear the garage next month. My son showed me how to bookmark it." |
| NPS (0~10) | 7 (v1 Dorothy 대비 +6) — "I could actually do this by myself! The share page is wonderful." |

---
---

## 👤 User #5 — Natalie Rivera (30세, 첫 집 구매 리노베이션, Denver CO)

### 배경 및 상황
콜로라도주 덴버. 남편과 첫 집(1970년대 ranch house)을 구입하여 입주 전 셀프 욕실+주방 리노베이션 진행 중. YouTube DIY 채널과 Reddit r/HomeImprovement의 열성 팔로워. 현재 주방의 old laminate countertop, backsplash tile, 그리고 욕실의 낡은 vanity + 타일을 한꺼번에 처분해야 함. 이전에 Bagster(3yd³ 일회용 bag)는 써봤지만 dumpster는 처음.

### 진입 의도
> "I'm renovating my first house. Two rooms at once — kitchen and bathroom. The tile, countertops, and old vanity gotta go. My husband and I are doing it ourselves. I need to figure out if one dumpster can handle both rooms."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션]**

> "'Dumpster, junk removal, or staged multi-haul' — **staged multi-haul? 그게 뭐지?** 여러 번에 나눠 운반한다는 건가? 이런 옵션도 있는 줄 몰랐어."

**🟢 Minor — "staged multi-haul" 용어:**
일반 소비자에게 "multi-haul"은 약간 전문적으로 들릴 수 있으나, 맥락에서 대략 유추 가능. tooltip이나 한 줄 설명이 있으면 더 명확.

**[Decision Strip]**

> "4개 pill 중에 나한테 정확히 맞는 게 없어. 'Cheapest'도 아니고 'heavy'도 아니고... **여러 방 리노베이션은 어디에 해당하지?** 일단 그냥 직접 선택할게."

**[Step 1 — Project Scope]**

> "'Kitchen remodel'이랑 'Bathroom remodel' 둘 다 있는데... **하나만 선택할 수 있어.** 나는 두 방을 동시에 하는 건데. 주방이 더 크니까 'Kitchen remodel'로 갈게."

**🟡 Major — Multi-room/Multi-project 입력 불가 (v1 동일):**
v1의 Kevin Huang(house flipper)이 동일하게 지적. 여러 방을 동시에 리모델링하는 DIY 사용자에게 단일 project scope 선택은 여전히 제한적.

**[Step 2 — Material]**

> "내 잔해는 tile + laminate countertop + plywood + vanity(MDF + porcelain sink)... **역시 하나만 선택인데, 'Tile/ceramic'을 고르면 나머지 무게가 빠지는 거 아냐?**"
>
> "'Mixed construction debris (C&D)' — **아, C&D가 Construction & Demolition이란 걸 여기서 처음 알았어!** 예전에는 약어만 있었다고 들었는데, 풀어써있으니까 바로 이해돼. **이걸 고르면 혼합 잔해가 반영되겠지?**"

**✅ Positive — "Mixed C&D" → "Mixed construction debris (C&D)" 약어 풀이:**
v1에서 Jessica Park가 "C&D가 뭔지 모르겠다"고 지적했던 이슈가 해결. 약어 풀이로 DIY 사용자의 이해도가 즉시 향상.

**[결과 + Comparison Hub]**

> "결과가 나왔어. 추천 사이즈가 OK인 것 같고... 밑에 **'Compare dumpster vs junk removal'** 링크가 있어! 클릭!"
>
> "Comparison hub에서 **4가지 priority mode**로 비교할 수 있어. 'Least effort'를 누르니까 junk removal이 1위로 올라왔어. 나도 남편도 직장인이니까 **누군가 와서 다 가져가는 게 더 편할 수도 있겠다**는 생각이 드네."
>
> "근데 **junk removal 비용과 dumpster 비용의 구체적 숫자가 없어.** 단지 '이쪽이 더 싼 경향이 있다' 정도만 알 수 있어. **구체적 가격 range가 있었으면** 바로 결정했을 텐데."

**✅ Positive — Comparison Hub가 사용자의 선택지를 확장:**
Natalie처럼 "dumpster만 생각하고 왔는데 junk removal도 고려하게 된" 케이스는 이 사이트의 **의사결정 엔진으로서의 가치**를 증명. 단순 calculator가 아니라 **decision advisor**로 포지셔닝.

**🟡 Major — Comparison Hub에서의 가격 부재:**
Priority toggle과 카드 재정렬은 훌륭하나, **구체적 비용 비교 없이는** "그래서 어느 쪽이 실제로 얼마나 싼 건데?"라는 핵심 질문에 답하지 못함.

### Natalie Rivera 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | △ 부분 달성 — 사이즈 추천 + dumpster vs junk 비교는 성공, 가격·예약은 미달성 |
| v1 대비 개선 체감 | ✅ "Mixed construction debris (C&D)" 약어 풀이, Comparison Hub, Scorecard |
| 핵심 잔존 이슈 | Multi-room 입력 불가, 가격 부재, helper text 버그 |
| 재방문 의사 | ⭕ "I'll use it for the next room. But I'll still need to shop around for actual prices." |
| NPS (0~10) | 7 — "The comparison page opened my eyes to junk removal as an option. The C&D label fix was helpful too." |

---

# 📊 일반 사용자 5인 종합 패턴

## v2 개선 효과 확인

| v1 지적 사항 | v2 변경 | 효과 |
|---|---|---|
| "Routing signal" 이해 불가 | → "Choose your role" | ✅ 5/5 자연스럽게 이해 |
| 생활 폐기물 시나리오 누락 (Moving, Furniture) | → 3개 신규 Project scope | ✅ 4/5 정확 매칭 |
| 진입 장벽 높음 (Step 1부터 막힘) | → Decision Strip 4개 pill | ✅ 4/5 원클릭 시작 |
| "Mixed C&D" 약어 미풀이 | → "Mixed construction debris (C&D)" | ✅ 1/1 즉시 이해 |
| Comparison 기능 부재 | → Comparison Hub + Priority Toggle | ✅ 3/5 적극 활용 |
| Decision scorecard 부재 | → 4축 점수판 추가 | ✅ 2/5 직관적 이해 |

## v2에서도 미수정된 잔존 이슈

| 이슈 | v1 지적 빈도 | v2 지적 빈도 | 상태 |
|---|---|---|---|
| helper text 미동기화 | 5/10 | 3/5 | 🔴 여전히 미수정 |
| 가격 정보 부재 | 6/10 | 5/5 | 🟡 여전히 미수정 |
| "this quote" 대명사 오류 | 4/10 | 1/5 | 🟡 여전히 미수정 |
| CTA 목적지 불일치 (/about/contact) | 4/10 | 2/5 | 🟡 라벨만 개선, 목적지 동일 |
| Material 단일 선택 한계 | 5/10 | 2/5 | 🟡 여전히 미수정 |
| Multi-project 입력 불가 | 2/10 | 1/5 | 🟡 여전히 미수정 |

## 일반 사용자 NPS 비교

| | v1 평균 | v2 평균 | 변화 |
|---|---|---|---|
| NPS | 4.1 / 10 | 6.6 / 10 | **+2.5** ↑ |

> **v2 한 줄 평가 (일반 사용자):**
> *"The Decision Strip and Comparison Hub transformed a confusing contractor tool into something a first-timer can actually use. But the last mile — prices, real vendor connections, and that helper text bug — still keeps it from being a complete solution."*
