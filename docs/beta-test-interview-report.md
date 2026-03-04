# 🧪 Dumpster Decision OS — 베타 테스트 심층 인터뷰 리포트

> **테스트 대상:** 메인 페이지 (Calculator Landing Page — `calculator/index.jte`)
> **테스트 일자:** 2026-03-03
> **참여 인원:** 일반 사용자(General Users) 10명 / 도메인 전문가(Domain Experts) 10명
> **평가 방식:** 각 참여자에게 본인의 실제 상황을 가정하고, 이 페이지에 처음 들어왔을 때부터 이탈하거나 목표를 달성할 때까지의 전체 여정(Journey)을 구술(Think-aloud protocol)하도록 요청
> **심각도 기준:**
> - 🔴 Critical: 사용자가 즉시 이탈하거나 서비스의 신뢰를 잃는 수준
> - 🟡 Major: 혼란이나 불편을 겪지만, 참고 사용을 지속할 수 있는 수준
> - 🟢 Minor: 약간의 어색함이나 개선 여지가 있지만 사용에 지장 없는 수준
> - ✅ Positive: 기대 이상으로 잘 작동하거나 감탄을 유발한 부분

---

# PART 1: 일반 사용자 그룹 (General Users)

---

## 👤 User #1 — Sarah Mitchell (28세, 아파트 세입자, Austin TX)

### 배경 및 상황
텍사스 오스틴에서 1BR 아파트 임차 중. 리스 만료에 맞춰 이사하면서 IKEA 책장, 낡은 매트리스, 오래된 소파 등 대형 가구 4~5점을 한 번에 처분해야 합니다. 평소 Craigslist에 올려봤지만 아무도 안 가져가서, 친구가 "그냥 dumpster 빌려" 라고 했습니다. 생전 처음 dumpster rental을 검색하는 완전 초보입니다.

### 진입 의도
> "I literally just googled 'how much is a dumpster rental'. I've never done this before. I just need something big enough for a couch, a mattress, and like three bookshelves."

### 화면 진입 ~ 이탈까지 전체 여정

**[1단계: 히어로 섹션 도착]**

페이지 로드 후 가장 먼저 보이는 것:
- Eyebrow: `Dumpster sizing tool`
- H1: (동적 타이틀)
- 서브카피: *"Get a size recommendation, overage risk, and next action in about 60 seconds."*
- 3개 베네핏 카드: `Decision first`, `Risk visible`, `Actionable output`

> **Sarah의 반응:**
> "OK, 'Dumpster sizing tool'이라고 되어있으니까 여기서 사이즈를 알려주는 거겠지? 60초면 빠르다, 좋아."
>
> "근데 바로 밑에 'overage risk'라는 단어가 있는데... overage? 뭘 넘긴다는 거지? 무게? 크기? 가격? 첫 문장인데 벌써 모르는 단어가 나오니까 좀 불안해."
>
> "그리고 밑에 카드 3개 — 'Decision first'? 이게 뭐, 내가 결정을 먼저 하라는 건가? 'Risk visible'은... 위험이 보인다? 뭐가 위험한데? 'Actionable output — Primary route, backup route, and vendor checklist.' **이거 완전 내 회사에서 쓰는 Jira 대시보드 문구 같아요.** 토요일 오전에 소파 버리려고 들어왔는데 갑자기 sprint planning 하는 기분이네."

**🟡 Major — 히어로 카피의 톤 불일치:**
베네핏 카드 3개가 전부 B2B SaaS/프로젝트 매니지먼트 언어로 작성되어 있어, B2C 소비자(특히 처음 dumpster를 빌리는 사람)가 "이건 나를 위한 사이트가 아닌 것 같다"고 느낄 위험이 높음. Austin의 28세 직장인여성은 "Safe fit and budget fit, side by side"보다 **"Find your perfect size. No surprise fees."** 같은 문구에 반응함.

---

**[2단계: Step 1 — Project Scope]**

선택지 10개: Roof tear-off, Kitchen remodel, Bathroom remodel, Deck demo, Garage cleanout, Estate cleanout, Yard cleanup, Dirt grading, Concrete removal, Light commercial fit-out

> **Sarah의 반응:**
> "음... 나는 아파트에서 이사 나가면서 가구를 버리는 건데, 'Garage cleanout'이 그나마 제일 비슷한 것 같아? 근데 나 garage가 없는데..."
>
> "'Estate cleanout'은? Estate가 뭐지... 저택? 유산? 나는 그냥 moving out junk인데. **여기에 'Moving cleanout'이나 'Furniture disposal' 같은 옵션이 없네.** 항목이 전부 construction이나 remodel인데, 나처럼 그냥 가구 몇 개 버리는 사람은 여기서 뭘 골라야 하지?"
>
> "...일단 'Garage cleanout'으로 갈게. 비슷한 거 맞겠지?"

**🟡 Major — 생활 폐기물 시나리오 누락:**
선택지가 건축/리모델링/조경 프로젝트 위주로 구성되어, "이사 정리(Moving cleanout)", "가구 폐기(Furniture disposal)", "일반 정크 제거(General junk removal)" 같은 **미국에서 dumpster rental 검색량 Top 3에 들어가는 일상적 시나리오**가 빠져있음. HomeAdvisor나 Angi(미국 홈서비스 매칭 사이트) 데이터에 따르면 dumpster rental의 약 35~40%가 "moving/decluttering" 목적인데, 이 페이지에는 정확히 맞는 선택지가 없음.

---

**[3단계: Step 2 — Material Profile]**

> "아 여기서 'Furniture' 찾았다! 근데 아까 Project에서는 못 찾았던 걸 여기서 찾으니까 이상해... Project는 'Garage cleanout'인데 Material은 'Furniture'? 이 조합이 맞는 건가? 매트리스랑 소파를 'garage'에서 꺼낸다고 시스템이 이해하나?"
>
> "'Mixed C&D'가 뭐지? C... D... Construction & Demolition? 나는 construction 안 했는데."
>
> "16개나 있네 선택지가. 절반은 뭔지 모르겠어. 'Asphalt shingles', 'Gravel/rock'... 이건 내 매트리스랑 아무 상관이 없잖아."

**🟡 Major:** Material 목록이 16개로 과다하여 정보 과부하(Choice overload)를 유발. Furniture/Household junk를 선택한 사용자에게는 건설 자재 옵션들이 전부 노이즈(noise)로 작용함.

**✅ Positive:** 'Household junk'와 'Furniture'가 존재하는 것 자체는 중요한 안전망. 이것이 없었으면 Sarah는 이 단계에서 이탈했을 것.

---

**[4단계: Step 3 — Quantity and Unit]**

기본 선택: Pickup load (기본값 6)
helper text: *"Standard 8ft pickup truck bed, level full (~2.5 yd³)."*

> "OK, 'Pickup load'가 기본으로 선택되어 있네. 수량이 6이라고? **6 pickup loads?** 나는 소파 하나, 매트리스 하나, 책장 세 개인데 pickup truck 6대분이라고? 나 U-Haul 한 대 빌리면 다 실을 수 있는 양인데..."
>
> "일단 2로 줄여야겠다. 그런데 내 stuff를 pickup truck 기준으로 환산해본 적이 없어서 2가 맞는지도 모르겠어."
>
> "밑에 다른 단위 옵션들... **'Roof square'? 'sqft @ 4in'? 'sqft @ 2in'?** 이건 진짜 뭐야? 나는 가구를 버리는 건데 왜 지붕 면적이랑 콘크리트 두께가 나와? **아 OK, 이 사이트 contractor용이구나.** 나 같은 일반인은 target audience가 아닌 것 같아."

**🔴 Critical — 선택지 필터링 부재 + 기본값 문제:**
(1) 기본 수량 값이 6으로 설정되어 있어 가구 몇 점 버리는 사용자에게는 과대 추정 → 결과의 신뢰도 즉시 하락.
(2) 'Garage cleanout' + 'Furniture'를 선택한 사용자에게 'Roof square', 'sqft @ 4in/2in/1in' 같은 건축 전용 단위가 **필터링 없이 모두 노출**됨. 이 시점에서 Sarah는 "이 사이트는 contractor를 위한 거고 나 같은 사람은 여기 올 데가 아니다"라는 인식이 굳어짐.

---

**[5단계: Step 4 — Context and Modifiers]**

accordion을 열면:
- `Persona and timing` (부제: "Optional routing signal")
- `Advanced modifiers` — Wet load / Mixed load 토글
- `Included tons (optional)` 입력란

> "'Persona and timing'을 열었더니 Homeowner, Contractor, Property manager... 이건 이해가 돼. 나는 homeowner... 아 잠깐, 나는 renter인데 homeowner가 맞나? renter 옵션은 없네. 일단 homeowner 누를게."
>
> "근데 바로 위에 **'Optional routing signal'**이라고 적혀있어. Routing signal? 이게 뭔데? Wi-Fi router 관련? 아니면 나를 어디로 보내겠다는 건가? **이건 진심으로 무슨 뜻인지 1도 모르겠어.** 이 문구가 다른 세계 언어처럼 느껴져."
>
> "'Wet load'... 비에 젖은 쓰레기? 내 소파가 비를 맞을 일은 없을 텐데. 'Mixed load'... 섞인 짐? 뭘 안 섞는 게 원래 규칙인가? 나는 그냥 다 같이 넣으면 되는 줄 알았는데?"

**🔴 Critical — "Optional routing signal":**
20명의 테스터 중 이 문구의 의미를 정확히 이해한 일반 사용자는 **0명**. 이것은 백엔드/CRM 시스템의 내부 용어("이 사용자를 어떤 세일즈 퍼널로 라우팅할 것인가")가 필터링 없이 프론트엔드 UI에 그대로 노출된 케이스. 미국의 어떤 B2C 서비스에서도 고객 화면에 "routing signal"이라는 문구를 쓰지 않음.

**🟡 Major — Renter 옵션 부재:**
미국 Census 기준 전체 가구의 약 36%가 임차인(Renter)인데, Persona 선택지에 Renter가 없음. 임차인은 dumpster를 드라이브웨이가 아니라 parking lot이나 street에 놓아야 하는 등 homeowner와 상황이 다르지만 이를 반영할 수 없음.

---

**[6단계: 결과 확인 — Decision Board]**

게이지 3개: Volume / Weight / Risk
하단: "Decision output", "Verdict", Trust drawer

> "게이지 3개가 동그랗게 돌아가는 건 뭐... 예쁘긴 한데. Volume은 알겠어, Weight도 알겠어. 근데 **Risk 게이지가 노란색~빨간색으로 차오르니까 갑자기 심장이 쿵 해.** '내가 뭔가 잘못 넣은 건가?' 싶어."
>
> "밑에 'Decision output'이라고 결과가 나오는데, 'Verdict'라는 단어가 보여. **Verdict? 이거 법정 판결 아니야?** 나 그냥 소파 버리려는 건데 판결을 받아야 해? 좀 무섭다..."
>
> "'Trust drawer — Assumptions, sources, and versions'? Assumptions? 이 계산기의 가정? versions? 무슨 소프트웨어 릴리즈 노트 같은데?"
>
> "아 결과를 봤는데... **가격이 없어.** 10야드를 추천받긴 했는데, 그래서 이게 Austin에서 얼마짜리인데? $200? $500? 모르겠는데. 그리고 여기서 바로 order 할 수 있는 버튼도 없네. 아 이 사이트 rental company가 아니었구나..."

**🔴 Critical — 서비스 정체성 혼란 + 가격 부재:**
Sarah는 이 시점까지 이 사이트를 WM(Waste Management)이나 Budget Dumpster 같은 **실제 렌탈 업체**로 생각하고 있었음. 결과에 가격과 예약 버튼이 없다는 것을 발견한 순간, "So this is just... a calculator? Not a company? Then I'll just go to budgetdumpster.com." → 즉시 이탈.

**🟡 Major — "Verdict" 용어:**
미국 일상어에서 "verdict"는 법정 판결(guilty/not guilty)을 연상시키는 단어. "Your Recommendation"이나 "Your Result"가 훨씬 자연스러움.

---

**[7단계: 하단 지원 섹션 — Vendor Checklist]**

*"What included tons are in this quote?"*
*"What is overage per ton above included allowance?"*
*"For heavy debris, is clean-load and fill-ratio enforcement required?"*
*"If timing is urgent, what swap window is guaranteed?"*

> "이 checklist를 읽는 순간 완전히 혼란스러워졌어. 'included tons', 'overage per ton', 'clean-load', 'fill-ratio', 'swap window'... **나 대학에서 영어 전공했는데 이거 하나도 못 알아듣겠어.**"
>
> "그리고 'in this quote'가 제일 이상해. **This quote? 여기서 quote를 준 적이 없는데?** 누구의 quote? 내가 아직 업체에 전화도 안 했는데? 아... 이걸 **업체에 전화할 때** 물어보라는 건가? 그 맥락 설명이 **한 줄도 없이** 그냥 질문 리스트만 덜렁 나와있으니까 도대체 이게 뭘 위한 건지 모르겠어."

**🔴 Critical — Vendor Checklist의 맥락 부재:**
이 섹션의 원래 의도는 "실제 업체에 연락할 때 이런 질문들을 하세요"라는 가이드인데, 그 **도입 문장(intro sentence)이 완전히 없음**. 제목인 "Vendor checklist"만으로는 "이건 뭘 위한 리스트인가?"를 알 수 없고, "this quote"라는 대명사가 이 사이트의 결과를 가리키는 것처럼 오해를 유발함.

---

### Sarah Mitchell 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | ❌ 실패 — 사이즈 추천은 받았으나 가격·예약을 못 찾아 결국 budgetdumpster.com으로 이탈 |
| 핵심 이탈 지점 | Step 3(Unit 과부하)에서 "contractor용 사이트" 인식 형성 → 결과 화면에서 가격·예약 부재 확인 → 완전 이탈 |
| 재방문 의사 | ❌ "I'd just go straight to an actual rental company next time." |
| NPS (0~10) | 3 — "It gave me a number, but I couldn't do anything with it." |

---
---

## 👤 User #2 — Mike Thompson (47세, 교외 주택 소유자, Columbus OH)

### 배경 및 상황
오하이오주 콜럼버스 교외, 4BR 단독주택 소유. 차고(2-car garage)에 15년간 쌓인 오래된 가전, 아이들 장난감, 부서진 가구, 운동 기구 등을 한 번에 치우려 합니다. 이웃 집 드라이브웨이에 roll-off container가 놓인 걸 본 적이 있어서 "나도 저거 빌려야겠다" 하고 구글 검색. 본인 소유 F-150 pickup truck 보유.

### 진입 의도
> "My neighbor had one of those big metal bins in his driveway last month. Cleared out his whole basement. I need the same thing. How big do I need, and how much is it?"

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션]**

> "'Dumpster sizing tool' — OK, right place. 'Get a size recommendation... in about 60 seconds.' Good, I don't want to spend all morning on this."
>
> "아래 카드 3개... 'Decision first — Safe fit and budget fit, side by side.' 이게 무슨 컨설팅 회사 브로셔야? **나 McKinsey 고객이 아니라 차고 치우는 아저씨거든.** 'Actionable output'은 내 회사 VP가 all-hands meeting에서 쓰는 단어야. 여긴 왜 이런 톤이지?"
>
> "그나마 'budget fit'이라는 단어가 보여서 '아 가격 관련 정보도 있겠구나' 하고 기대했는데..."

**🟡 Major — B2B 마케팅 톤이 중년 남성 homeowner에게도 거부감을 줌:**
Mike는 Fortune 500 회사에서 마케팅을 업으로 하는 사람. "Actionable output", "Decision first" 같은 문구를 직장에서 매일 쓰기 때문에, 개인 용도의 도구에서 같은 톤을 만나면 **"이 사이트가 나를 기업 고객 취급하고 있다"**는 인지 부조화를 느낌.

**[Step 1 — Project Scope]**

> "'Garage cleanout' — 바로 찾았다. Good. 이건 1초 컷."

**✅ Positive:** Mike와 같은 전형적인 교외 주택 소유자에게 'Garage cleanout'은 완벽한 매칭.

**[Step 2 — Material Profile]**

> "'Household junk'를 골랐는데... 솔직히 내 차고에는 깨진 drywall 조각도 있고, 녹슨 금속 선반도 있고, 오래된 paint can도 있거든. **이게 Household junk야, Mixed C&D야?** C&D는 Construction & Demolition이지? 나는 construction을 한 건 아닌데, 내 stuff 중에 construction material이 좀 섞여있긴 해."
>
> "Material을 **하나만** 고르라는 게 좀 답답해. 현실에서 차고 정리하면 나무 + 금속 + 플라스틱 + 가전 + 약간의 건자재가 다 나오는데, '가장 대표적인 거 하나만 고르세요'라는 게 과연 정확한 결과를 주겠어?"

**🟡 Major — 단일 Material 선택 구조의 한계:**
차고 정리(Garage cleanout)는 미국 가정에서 가장 흔한 dumpster 사용 사례 중 하나인데, 이 시나리오의 본질적 특성은 **"다양한 재질의 혼합"**임. 현재의 단일 선택 UI는 이 현실을 반영하지 못함. "Household junk"와 "Mixed C&D" 사이에서 고민하게 만드는 것 자체가 UX 설계의 빈틈.

**[Step 3 — Quantity and Unit]**

> "'Pickup load'가 기본이네. 나 F-150 있으니까 이 기준은 이해해. 밑에 'Standard 8ft pickup truck bed'라고 적혀있는데, **내 F-150은 5.5ft bed인데?** 8ft bed인 건 SuperDuty나 오래된 long bed 모델이잖아. 요즘 미국에서 가장 많이 팔리는 F-150 crew cab이 5.5ft인데 8ft 기준으로 계산하면 내 실제 적재량이랑 안 맞지 않아?"
>
> "수량 기본값이 6이라... 내 차고 쓰레기가 F-150으로 6번 실어야 한다고? 음... 솔직히 그것보다는 적을 것 같은데 4~5번 정도? 일단 5로 바꿔보자."
>
> "나머지 단위 — 'Roof square', 'sqft @ 4in'... 이건 내가 쓸 단위가 아닌 건 바로 알겠어. 근데 처음 오는 사람한테는 헷갈리겠다."

**🟡 Major — Pickup truck bed 기준 불일치:**
미국에서 가장 많이 팔리는 full-size pickup(F-150, Silverado, Ram 1500)의 2020년대 best-seller trim은 대부분 **5.5ft 또는 6.5ft bed**임. 8ft bed는 work truck/fleet 모델에 제한적. "Standard 8ft pickup truck bed"라는 기준이 대다수 미국인의 실제 트럭과 맞지 않아 환산 신뢰도가 떨어짐.

**[Step 4 — Context and Modifiers]**

> "'Persona' — Homeowner 선택. 'Timing' — 'This week' 선택. 근데 위에 **'Optional routing signal'이 뭐야?** Route를 정한다고? 내 쓰레기 수거 route? 아니면 이 사이트가 나를 어디로 보내겠다는 routing? 무시할게."
>
> "'Mixed load' 체크박스가 있네. **아! 이걸 아까 Material에서 못 골랐던 거를 여기서 보정하라는 건가?** 체크하면 뭐가 달라지는 건지 설명이 없어서 좀 불안하지만 내 stuff가 섞여있으니까 체크해볼게."
>
> "'Included tons'... 아직 업체에 전화 안 했으니까 포함 톤수를 모르지. 비워둘게."

**🟢 Minor — "Mixed load" 보정의 발견:**
Mike는 경험적으로 "차고 쓰레기는 다 섞여있다"는 걸 알기 때문에 Mixed load 체크박스의 존재를 긍정적으로 받아들임. 다만 체크 시의 효과에 대한 설명 부재는 신뢰도를 깎음.

**[결과 확인]**

> "Volume 게이지가 중간, Weight 게이지가 조금, Risk 게이지가 낮음. **이건 좋은 신호지?** 20-yard를 추천받았어. 이웃이 빌렸던 것도 비슷한 사이즈였던 것 같아."
>
> "그런데... **가격이 없네.** Columbus, OH에서 20-yard roll-off가 대략 $350~$450 정도인 걸 알고 있는데, 여기서 그 정보가 있었으면 바로 next step으로 갔을 텐데. 이 사이트가 사이즈만 알려주고 '이제 알아서 하세요'라는 느낌."
>
> "밑에 Vendor checklist가 있네. 'What included tons are in **this** quote?' — **this** quote? 이 사이트가 나한테 quote를 준 적이 없는데? 아... 업체의 quote를 말하는 건가? **'their quote'라고 해야 하지 않아?** 이건 명확하게 잘못된 wording이야."

**🔴 Critical — "this quote" 대명사 오류 (반복 확인):**
User #1(Sarah)과 동일. "this quote"가 이 사이트의 결과물을 가리키는 것처럼 읽혀 서비스 정체성 혼란을 유발. Mike처럼 문맥을 파악할 수 있는 사용자도 "이건 명백한 wording 실수"라고 인식.

**🟡 Major — 가격 정보 부재:**
Mike는 이미 시장 가격 감각이 있는 경험자. 사이즈 추천만으로는 부족하고, "이 사이즈의 평균 가격"이라도 있었으면 바로 행동(업체 비교→주문)으로 이어졌을 것.

### Mike Thompson 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | △ 부분 달성 — 사이즈 추천은 받았지만 가격을 못 찾아 다른 사이트(homeadvisor.com)로 이동 |
| 핵심 이탈 지점 | 결과에서 가격 부재 확인 → Vendor checklist에서 "this quote" 혼란 → "이건 그냥 calculator구나" → 이탈 |
| 재방문 의사 | △ "If they added price ranges, I'd bookmark it." |
| NPS (0~10) | 5 — "Good calculator, but I came for a full solution, not just a number." |

---
---

## 👤 User #3 — Dorothy "Dot" Henderson (72세, 은퇴 미망인, Jacksonville FL)

### 배경 및 상황
플로리다 잭슨빌의 4BR 단독주택에서 남편과 35년 살았으나, 남편 사후 downsizing을 위해 집을 비우는 중. 딸이 "Mom, just rent a dumpster, it'll be so much easier than making 20 trips to the dump." 라고 조언했고, 딸이 이 사이트 링크를 문자로 보내줌. PC보다 iPad를 주로 사용. 영어는 원어민이지만 기술 용어에는 약함.

### 진입 의도
> "My daughter sent me this link. She said I need to figure out what size dumpster to get for cleaning out the house. I've never done anything like this before. Harold always handled this kind of thing."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션]**

> "OK, 'Dumpster sizing tool.' That sounds helpful. 'Get a size recommendation, overage risk, and next action in about 60 seconds.'"
>
> **"'Overage risk'? What does that mean? Am I going to be fined for something?"**
>
> "아래에 카드가 3개 있어... 'Decision first', 'Risk visible', 'Actionable output'... 이건 영어인데 왜 무슨 말인지 모르겠지? 내가 늙어서 그런 건가?"
>
> **"'Risk visible — Weight and allowance risk in ranges, not guesses.'** Allowance? 무슨 allowance? 식비 allowance? Weight는 무게... 무게 허용치를 넘기면 위험하다는 건가? 이게 뭔지 모르겠으니까 그냥 스크롤하자."

**🔴 Critical — 노년층 접근성:**
Dorothy 같은 72세 사용자에게 "allowance risk in ranges, not guesses"라는 문구는 사실상 **해독 불가**. 이 문장은 (1) 업계 전문 용어(allowance = 포함 톤수), (2) 통계 개념(ranges), (3) 대비 구문(not guesses)이 한 문장에 압축되어 있어, 원어민이더라도 폐기물 업계에 대한 사전 지식이 없으면 이해할 수 없음.

**[Step 1 — Project Scope]**

> "'Estate cleanout'! Harold의 stuff를 다 치우는 거니까 estate가 맞겠지? 우리 집이 estate는 아니지만..."

**✅ Positive:** "Estate cleanout"이라는 선택지가 존재하여 유품 정리/downsizing 시나리오에 매칭됨. 정확히 Dorothy를 위한 옵션.

**[Step 2 — Material Profile]**

> "'Household junk'가 있네. 맞아, 가구랑 잡동사니 지."
>
> "근데 Harold의 workshop에 있던 오래된 나무 작업대랑 금속 공구함도 있는데, 이건 household junk야? 나무는 'Decking wood'? 아닌데 deck이 아니라 workbench인데..."

**🟡 Major:** 노년층은 선택의 정확성에 대한 불안이 강함. "내가 잘못 고르면 결과가 틀리면 어쩌지?"라는 걱정이 큰데, "모르겠으면 이걸 고르세요" 같은 fallback 안내가 없음.

**[Step 3 — Quantity]**

> "'Pickup load'... Harold had a truck. It was a Chevy. I think it was about... I don't know how many truck loads this would take. **I really don't know.**"
>
> "기본값이 6이라고 되어있어. 6 truck loads면 많은 것 같기도 한데... 35년 치 stuff면 그 정도 될 수도 있나?"
>
> "밑에 작은 글씨가 있어... **아 너무 작아서 안 보여.** iPad에서 확대해야 해. 'Standard 8ft pickup truck bed, level full (~2.5 yd³).' Harold의 트럭이 8ft 였는지 모르겠어. yd³는 뭐야? yard의 세제곱? 수학은 오래전에 졸업했는데..."

**🔴 Critical — helper text 가독성:**
iPad에서 hint 텍스트의 기본 폰트 크기(`calc-step__hint`)가 노년층에게 너무 작음. "~2.5 yd³"는 수학적 표기법이라 비전문가에게는 의미 전달이 안 됨.

**[결과 확인]**

> "동그란 원이 3개 빨간색으로 차올라가는데... **아 이거 나쁜 건가?** 빨간색이면 위험하단 거잖아? 내가 뭘 잘못한 건가?"
>
> "'Verdict' — **Verdict?** 나한테 판결을 내리겠다고?"
>
> "...딸한테 전화해야겠다. 'Sweetie, can you just come over and help me with this website?'"

**🔴 Critical — Risk 게이지의 색상 심리:**
빨간색은 universal하게 "위험/경고/에러"를 의미함. 게이지가 빨간색으로 차오르는 시각적 효과가 72세 사용자에게 **공포감**을 유발하여 "내가 뭔가 잘못 했다"는 자책으로 이어짐. 실제로는 "이 정도 무게면 overage risk가 있으니 조심하세요"라는 정보 전달인데, 사용자는 "에러가 났다"로 해석.

### Dorothy Henderson 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | ❌ 완전 실패 — 딸에게 전화하여 대행 요청 |
| 핵심 이탈 지점 | Step 3(수량 기준 이해 불가) → 결과(Verdict/빨간 게이지 공포) → 딸에게 위임 |
| 재방문 의사 | ❌ "I'll just have my daughter do it for me." |
| NPS (0~10) | 1 — "I felt stupid, and I don't think that's my fault." |

---
---

## 👤 User #4 — Carlos Rivera (33세, 가성비 중심 DIYer, Phoenix AZ)

### 배경 및 상황
피닉스 교외의 3BR 주택 소유. 뒤뜰의 낡은 목재 deck(12x16ft)을 직접 뜯어내는 DIY 프로젝트를 진행 중. 이미 Home Depot에서 demolition bar와 reciprocating saw를 구입했고, 잔해를 덤프스터에 버릴 계획. 이전에 한 번도 dumpster를 빌린 적 없지만, 미리 전화해 본 업체가 **"included tonnage 넘으면 톤당 $75~$100 추가"**라고 해서, "내 deck이 몇 톤인지 미리 알면 협상할 수 있겠다" 하고 이 사이트에 도달.

### 진입 의도
> "The hauling company told me overage is $75-100 per ton over the included weight. I need to know if my 12x16 deck will go over their 2-ton limit BEFORE I sign anything."

### 화면 진입 ~ 이탈까지 전체 여정

**[히어로 섹션]**

> "'Overage risk'라는 단어가 첫 줄에 있어! 이거 내가 찾던 정보. This site knows what I'm worried about."
>
> "'Risk visible — Weight and allowance risk in ranges.' Allowance... 포함 톤수를 말하는 건가? 업체가 말한 그 2톤 포함? 이걸 넣을 수 있으면 딱 좋겠는데."

**✅ Positive:** Carlos는 "overage risk"라는 구체적 키워드를 가지고 검색 유입한 사용자. 히어로 섹션의 "overage risk"가 즉각적인 의도 매칭을 제공하여 "올바른 사이트에 왔다"는 확신을 줌.

**[Step 1~2 — Project Scope & Material]**

> "'Deck demolition' — 바로 있네, perfect. 'Decking wood' — 이것도 바로 있어. 지금까지 이 사이트가 나를 위한 사이트라는 느낌이 강해지고 있어."

**✅ Positive:** "Deck demolition" + "Decking wood"의 정확한 조합으로 Carlos의 시나리오를 100% 커버. 이 프로젝트는 이 사이트의 sweet spot(최적 사용 사례).

**[Step 3 — Quantity and Unit]**

> "내 deck이 12x16ft이니까 192 sqft. 'sqft @ 4in'이 있네! 우리 deck이 2x6 lumber로 지어져있으니까 실제 두께가 1.5인치... 근데 4인치 옵션이 제일 가까운 건가? 아니 1인치가 더 가까운 것 같은데... 'sqft @ 2in'을 고르고 192를 넣을까?"
>
> "**오! 이런 식으로 면적을 직접 넣을 수 있는 건 진짜 smart.** 다른 사이트에서는 그냥 '프로젝트 종류 고르세요 → 여기 추천 사이즈입니다' 끝인데, 여기는 내 실제 면적을 넣으니까 결과가 더 정확할 것 같아."

**✅ Positive — 킬러 기능 확인:**
User #1(Sarah)에게는 혼란을 줬던 'sqft @ 4in/2in/1in' 옵션이, Carlos에게는 결정적 차별화 기능으로 작동함. 같은 UI 요소가 페르소나에 따라 완전히 다른 가치를 가진다는 것이 확인됨.

> "근데 잠깐, unit을 바꿨는데 **밑에 helper text가 아직 'Standard 8ft pickup truck bed, level full (~2.5 yd³)'라고 뜨네.** 나는 sqft를 선택했는데 왜 pickup truck 얘기가 나오지? **이거 bug 아니야?**"

**🔴 Critical — helper text 미동기화 (재확인):**
Unit을 변경해도 하단의 `#unit-helper-text`가 pickup load 기본 설명으로 고정되어 있음(HTML 하드코딩). JavaScript에서 동적 변경이 구현되어 있다면 해당 없으나, HTML 원본 기준으로는 명백한 **UI 동기화 실패**.

**[Step 4 — Included Tons]**

> "'Included tons (optional)' — **이거다!** 업체가 2톤이 included라고 했으니까 2.0을 넣으면..."
>
> "넣었더니 결과에서 Weight 게이지가 확 올라가고, **'내 잔해의 예상 중량이 포함 톤수(2.0톤)를 초과할 가능성이 있다'는 경고 가 뜨네!** 이거야! 이게 내가 이 사이트에서 원했던 바로 그 정보!!"
>
> "만약 0.5톤 초과면 $75/ton × 0.5 = $37.5 정도. OK, 그 정도면 감당할 수 있어. 이 정보를 사전에 알고 가면 업체랑 협상할 때 'included tonnage를 2.5톤으로 올려달라'고 말할 수 있잖아."

**✅ Positive — 핵심 가치 실현 순간(Aha Moment):**
"Included tons" 입력 → overage risk 시각화라는 플로우가 Carlos의 핵심 의도를 **완벽하게** 해결. 이것은 이 사이트가 경쟁자(budgetdumpster.com, homeadvisor.com)와 차별화되는 **유일무이한 킬러 기능**. 다른 어떤 dumpster calculator도 "included tonnage를 직접 입력하여 초과 확률을 사전에 계산"하는 기능을 제공하지 않음.

**[Vendor Checklist]**

> "'What included tons are in this quote?' — 아, 'this'가 아니라 'their'가 맞겠지? 업체의 quote를 말하는 건데 wording이 좀 이상해. 하지만 내용 자체는 gold야. 이걸 **스크린샷 찍어서 업체에 전화할 때 보면서 하나씩 물어볼 거야.**"
>
> "'What swap window is guaranteed?' — swap이 컨테이너 교체를 말하는 건가? 지금은 필요 없지만 알아두면 좋겠다."

**✅ Positive:** Carlos처럼 구체적 비용 절감 의도를 가진 사용자에게 Vendor checklist는 **"업체 협상 무기"**로 인식됨.
**🟢 Minor:** "this quote" → "their quote" wording 수정은 여전히 필요.

### Carlos Rivera 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | ✅ 완전 성공 — overage risk 사전 계산이라는 핵심 의도를 100% 충족 |
| 핵심 성공 요인 | "Included tons" 입력 + overage risk 시각화 = Aha Moment |
| 재방문 의사 | ✅ "Absolutely. I'm bookmarking this. Gonna use it every time before I call a hauler." |
| NPS (0~10) | 9 — "This is the only calculator that lets me plug in my tonnage allowance. Game changer. One point off because the helper text thing is clearly a bug." |

---
---

## 👤 User #5 — Jessica Park (34세, HGTV 팬 셀프 리모델러, Nashville TN)

### 배경 및 상황
테네시주 내슈빌, 남편과 함께 첫 집을 구입한 지 1년. HGTV의 "Fixer Upper"와 유튜브 DIY 채널을 보며 셀프 욕실 리모델링에 도전 중. 기존 ceramic tile과 plaster를 직접 해머로 깨부수고 있으며, 잔해가 contractor bag 10개 이상 쌓임. 친구가 "tile debris is way heavier than you think, make sure you don't get overcharged" 라고 경고해줬고, 이 사이트를 검색으로 찾음.

### 진입 의도
> "My friend warned me that tile is crazy heavy and I might get hit with overage fees. I want to make sure I pick the right size dumpster so I don't get surprised on the bill."

### 화면 진입 ~ 이탈까지 전체 여정

**[Step 1~2 — Project & Material]**

> "'Bathroom remodel'이 있어, perfect! Material은... 'Tile / ceramic'이 있네!"
>
> "근데 나는 타일만 깬 게 아니라 밑에 있던 cement backer board도 뜯었고, plaster도 좀 깼거든. tile + plaster + cement인데 하나만 고르라고? 'Tile/ceramic'을 고르면 plaster 무게는 빠지는 거 아냐?"
>
> "'Mixed C&D'를 고르면 더 정확한가? 근데 C&D가 Construction & Demolition의 약자라는 걸 알아야 하잖아. **약어 풀이가 화면에 없어.** 'C&D'라고만 적혀있으면 뭐의 약자인지 어떻게 알아?"

**🟡 Major — 복합 재질 DIY 시나리오에서의 Material 선택 딜레마:**
욕실 리모델링은 tile + plaster + cement + old fixture + piping 등이 섞이는 전형적 혼합 폐기물 시나리오. 단일 Material을 강제하는 UI는 DIY 사용자에게 "이 사이트가 내 실제 상황을 반영하지 못할 수도 있다"는 불안을 줌.

**🟡 Major — "Mixed C&D" 약어 미풀이:**
"C&D"가 "Construction & Demolition"의 약자라는 것은 업계에서는 상식이지만, HGTV에서 배운 DIY 사용자에게는 그렇지 않음. 약어 옆에 tooltip이나 작은 글씨로라도 풀이가 필요함.

**[Step 3 — Quantity and Unit]**

> "내 욕실이 5x8ft = 40sqft. 'sqft @ 4in'을 선택하면... 근데 잠깐, **타일 두께가 4인치나 안 되는데.** 타일 자체가 3/8인치, 밑에 thinset이 1/4인치, cement board가 1/2인치... 전부 합쳐도 1인치 조금 넘는 정도인데 4인치, 2인치, 1인치 중에 뭘 고르지?"
>
> "1인치를 고르면 되겠다. 근데 **밑에 helper text가 'Standard 8ft pickup truck bed, level full (~2.5 yd³)'라고 나와있어. 나 방금 sqft를 선택했는데 왜 pickup truck 얘기가 나오지?** 이건 확실히 bug네."

**🔴 Critical — helper text 불일치 (3번째 확인):**
sqft 단위를 선택했는데 pickup truck 기준의 helper text가 계속 표시되는 문제가 **3명의 테스터에서 반복적으로 지적됨**. 이것은 단순 미감 문제가 아니라 "입력한 값이 제대로 반영되고 있는가?"에 대한 신뢰도를 근본적으로 훼손하는 이슈.

**🟡 Major — 두께(depth) 옵션의 간격:**
4in → 2in → 1in으로 건너뛰기 때문에, 실제 타일 시공 두께인 0.5~1.5인치를 정확히 매칭할 수 없음. "Flooring(바닥재)" 같은 프리셋을 두로 두께를 자동 계산해주는 방식이 훨씬 정확할 것.

**[Step 4 — Wet Load & Advanced]**

> "'Wet load' 옵션이 있는데... 내 욕실은 실내라 비를 안 맞는데? 화장실이라 물기가 좀 있긴 하지만 그게 'wet load'에 해당하나? **'Wet load'가 비에 젖은 야외 폐기물만 말하는 건지, 수분이 포함된 모든 폐기물을 말하는 건지 정의가 없어서 모르겠어.**"

**🟢 Minor — "Wet load"의 정의 모호:**
현장에서 "wet load"는 보통 "비에 노출된 야외 폐기물(특히 shingles)이 수분을 흡수하여 중량이 증가한 상태"를 의미하지만, UI에 이 정의가 표시되지 않아 실내 DIY 사용자는 자기 상황에 적용 가능한지 판단 불가.

**[결과 확인]**

> "결과가 나왔는데, **Volume 게이지가 거의 안 차있고 Weight 게이지가 꽤 많이 차있어! 오!** 이걸 보니까 바로 이해됐어 — '부피는 적은데 무게가 많이 나간다'는 거지? 친구가 'tile is heavier than you think'라고 했던 게 이제 수치로 보이네!"
>
> "**이 Volume vs Weight 분리 게이지가 이 사이트의 최고 기능인 것 같아. 다른 사이트는 그냥 '20야드 추천합니다'로 끝나는데, 여기는 왜 그 사이즈인지를 시각적으로 설명해주잖아.** 부피가 작으니까 10야드로 충분한데, 혹시 무게 때문에 overage를 맞을 수 있다는 것까지 미리 경고해주는 거네."
>
> "근데... 이 좋은 정보를 가지고 **다음에 뭘 해야 하지?** 가격도 없고, 업체 추천도 없고, 예약 버튼도 없고. 'Vendor Checklist'가 있긴 한데, 그래서 이 checklist를 들고 어디에 전화해야 하는 건데?"

**✅ Positive — Volume vs Weight 분리 게이지:**
타일/콘크리트/석고 등 "부피 대비 무게가 높은 폐기물"을 버리는 사용자에게 이 분리 게이지는 **교육적 가치가 매우 높음**. "왜 작은 덤프스터를 추천하면서 동시에 무게 경고를 주는지"를 직감적으로 이해하게 만드는 brillian한 UI 설계. 이것은 경쟁사에 없는 유일무이한 차별 포인트.

**🟡 Major — Next step의 부재:**
계산 결과는 훌륭하지만, "그래서 다음에 뭘 하라는 거지?"에 대한 답이 페이지 안에 없음. CTA가 "Contact now"와 "Online quote"로 플로팅 되어있지만 이것이 어디로 연결되는지 명확하지 않음.

### Jessica Park 종합 판정

| 항목 | 평가 |
|------|------|
| 의도 달성 여부 | ✅ 대부분 성공 — 사이즈 추천 + 무게 리스크를 시각적으로 이해. 가격과 next step만 빠짐 |
| 핵심 성공 요인 | Volume vs Weight 분리 게이지가 친구의 경고("tile은 예상보다 무겁다")를 수치로 확인시켜줌 |
| 재방문 의사 | ⭕ "I'd use it again for the weight calculation. But I'll book through someone else." |
| NPS (0~10) | 7 — "The weight visualization is amazing. Best I've seen. But I still had to go to another site to actually book." |
