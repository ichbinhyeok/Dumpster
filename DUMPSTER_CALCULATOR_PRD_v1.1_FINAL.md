# 덤스터 사이즈·중량(초과톤) 계산기 — 바이블급 기획서 v1.2 Lock

(US / 신규 도메인 Safe / Anti-Scaled / ROI-First / 1~2주 배포 기준)
작성일: 2026-02-26 (KST)

## 변경 요약 (v1.1 -> v1.2 Lock)

이번 버전은 개발 시작 전 반드시 고정할 결정사항을 문서에 반영했다.

1. `included tons(가격)`과 `max haul tons(운영)`을 분리한다.
2. `feasibility` 판정(`OK/Multi-haul required/Not recommended`)을 결과에 포함한다.
3. 밀도 의미를 `덤스터 적재 실효 밀도`로 고정한다.
4. `data_quality/confidence`로 범위 폭을 조정한다.
5. 결과 화면에 `업체 문의 체크리스트 3개`를 고정한다.
6. 결과 화면에 `입력 영향 요약 1줄`을 고정한다.
7. CTA 라우팅 최소 룰(heavy/high-risk/persona/timing)을 MVP에서 고정한다.
8. Phase1 인덱스 규모를 20~35 URL로 축소한다.
9. `/estimate/{id}`를 UUID/ULID + TTL + noindex 헤더로 강화한다.
10. QA에 `실패해야 하는 케이스(불가능/경고)`를 명시한다.

## 개발 선결정 10개 (Lock)

1. 포함 톤은 가격 리스크 계산용, 최대 허용 톤은 운반 가능 판정용으로 분리한다.
2. 운반 불가/멀티홀 필수 판정을 결과에 노출한다.
3. 밀도는 "덤스터 적재 실효 밀도" 기준으로 해석한다.
4. Phase1 인덱스는 20~35 URL에서 시작한다.
5. 프리셋 프리필은 URL 쿼리 대신 hash/local storage를 우선 사용한다.
6. estimate 링크는 UUID/ULID를 쓰고 30일 TTL을 둔다.
7. estimate 페이지는 `meta noindex`, `X-Robots-Tag`, canonical 대표 URL을 강제한다.
8. 결과 화면에 "업체에 물어볼 질문" 3개를 고정한다.
9. 결과 화면에 "내 입력이 결과에 준 영향" 1줄 요약을 고정한다.
10. 경계값/불가능/경고 케이스를 QA 통과 기준으로 둔다.

## 0. 한 문장 요약 (North Star)

"내 폐기물이 10/15/20/30/40yd 중 무엇에 들어가고, 중량 초과로 추가요금을 맞을지"를 60초 안에 판단하게 해주고, 결과 화면에서 즉시 `덤스터 견적/전화` 또는 `Junk Removal 비교`로 연결한다.

## 1. 성공 조건과 절대 제약 (헌법)

### 1.1 절대 제약

- 신규 도메인에서 도어웨이/스케일드 콘텐츠 의심 최소화.
- Phase1 인덱싱 3,000 URL 이하. 실제 운영 시작은 20~35 URL.
- 무한 조합 URL 인덱싱 금지.
- 모든 계산은 대표 URL 1개에서 처리.
- 파라미터(`?material=...`, `?estimate=...`)는 `canonical -> 대표 URL` + `noindex,follow`.
- ZIP/City 무한 생성 금지.
- 지역 기반은 IP 동적 표시(비인덱스) 또는 사용자 입력 폼만 사용.
- 가격/규정은 항상 `추정` 프레이밍 + 출처 + 경고문 고정.

### 1.2 정확도 제약

- `allowance(포함 톤)` 미입력 시 결과 상단에 "가정 기반" 고정 배지 노출.
- Heavy 재료 비중이 높으면 부피보다 무게를 우선 계산한다.
- 단정값 금지. 핵심 결과는 항상 `low/typ/high` 범위로 반환한다.
- `included tons(가격)`과 `max haul tons(운영)`을 혼용하지 않는다.

### 1.3 수익 구조 성공 조건 (6개월 ROI)

- 수익화 2축 필수.
- 축 A: Dumpster rental (전화/견적).
- 축 B: Junk removal 또는 Demo/Cleanup 매칭.
- 결과 화면은 정보글이 아니라 `결정 엔진`이어야 함.
- 추천 사이즈 + 무게 범위 + 초과 위험 + 비용 비교 + 대안 제시가 동시에 존재해야 함.

## 2. 문제 정의

### 2.1 사용자의 실제 불안

- 중량 초과 요금(ton overage) 공포.
- 작은 사이즈 선택으로 2회 이상 호출 위험.
- Heavy debris는 "큰 덤스터 1회"보다 "작은 덤스터 다회"가 현실적인 경우가 많음.
- 업체별 포함 톤/운영 규칙 편차가 커서 의사결정이 지연됨.

### 2.2 제품이 제공해야 하는 판단

- 단정 추천이 아니라 `안전 추천 / 예산 추천 / 주의` 3분기.
- 이유를 숫자(부피/무게 범위, allowance 대비 위험)로 설명.
- "운반 가능 여부(feasibility)"를 분리해 보여준다.
- "업체에 확인할 질문"까지 함께 제공한다.

## 3. 타겟 페르소나 6종

1. DIY Homeowner: 간단한 입력과 폭탄 회피.
2. Roofer: 부피보다 무게 중심 판단.
3. Small Contractor: 고객 설명 가능한 근거.
4. Property Manager: 다회 운반 판단.
5. Landscape/Excavation: heavy 규칙과 fill ratio 이해.
6. "그냥 치워줘" 성향: junk removal CTA 선호.

공통 검증 질문: "결과 화면 캡처 하나만으로도 의사결정/견적 요청이 가능한가?"

## 4. 제품 범위

### 4.1 MVP (1~2주)

필수:

- 부피(yd^3) 범위 계산.
- 무게(tons) 범위 계산.
- 사이즈 추천 + 위험 표시.
- `feasibility` 판정.
- 2축 CTA.
- 비용 비교 카드(덤스터 1회/10yd 다회/Junk).
- 분기 질문 3개.

MVP에서 제외:

- ZIP/City 인덱싱.
- 지역 평균 가격 대량 페이지.
- 프로젝트 x 재료 조합 자동 인덱싱.
- Detailed mode 무제한 라인아이템.

### 4.2 V1 (2~4주)

- 프로젝트 프리셋 Top 20 확장.
- 입력 단위 확장.
- 비교 모듈 고도화(덤스터 vs Junk vs Trailer vs Bag).
- 이메일 리포트.

### 4.3 V1.5

- 파트너 라우팅 고도화.
- persona/재료/긴급도 기반 오퍼 분기.

## 5. UX 설계 (결과 화면이 상품)

### 5.1 60초 플로우

1. 랜딩 -> Quick Mode.
2. 프로젝트 선택.
3. 규모 입력(sqft/squares/pickup loads 등).
4. Heavy 재료 토글.
5. `allowance 입력`(권장 필수): "모르면 보통값으로 계산" 옵션 제공.
6. 결과 + 비교 + CTA.

### 5.2 입력 모드

- Quick Mode(기본): 2~4개 입력, 범위 결과.
- Detailed Mode(옵션): 최대 3개 라인아이템 추가.

### 5.3 결과 화면 필수 컴포넌트 (11개)

1. Recommended Size (Safe).
2. Budget Option.
3. Feasibility (`OK/Multi-haul required/Not recommended`).
4. Volume Range (low/typ/high).
5. Weight Range (low/typ/high).
6. Overweight Risk Meter.
7. Heavy Debris Rule 카드.
8. Cost Comparison 카드(덤스터 1회 / 10yd 다회 / Junk).
9. 입력 영향 요약 1줄(예: 젖음 ON, 혼합 ON 반영).
10. 업체 문의 체크리스트 3개(포함 톤/초과요금/heavy 허용조건).
11. 신뢰 블록(계산 방식, 출처, 오차 요인, 업데이트 날짜).

### 5.4 공유 가능 결과

- 대표 계산 URL 고정: `/dumpster/size-weight-calculator/`.
- 서버 저장: `estimate_id`.
- 공유 URL: `/dumpster/estimate/{id}`.
- 공유 URL 정책:
- `estimate_id`는 UUIDv4 또는 ULID 사용(auto-increment 금지).
- `meta robots noindex,follow`.
- `X-Robots-Tag: noindex, noarchive`.
- canonical은 대표 계산 URL.
- sitemap 완전 제외.
- 내부 링크 최소화(공유 버튼으로만 노출).
- TTL 30일 만료 후 404 또는 재계산 유도.

## 6. 계산 엔진 설계 (핵심)

핵심 철학: 평균값 단정이 아니라 `범위 + 리스크 + 대안`.

### 6.1 계산 대상

- V: 부피 범위 (`V_low`, `V_typ`, `V_high`).
- W: 무게 범위 (`W_low`, `W_typ`, `W_high`).
- R_price: 포함 톤 기준 가격 리스크(`Low/Medium/High`).
- F_ops: 운반 가능 판정(`OK/Multi-haul required/Not recommended`).
- C: 옵션별 예상 총비용 범위(`C_low`, `C_typ`, `C_high`).

### 6.2 데이터 모델

1. `material_factors`
- `material_id`, `name`, `category(light/mixed/heavy)`
- `density_lbs_per_yd3_low/typ/high`
- `density_definition = effective_loaded_bulk_density`
- `wet_multiplier_low/high`
- `data_quality(low/med/high)`
- `source`, `source_version_date`

2. `unit_conversions`
- `unit_id`, `formula_type(volume|weight|area_thickness)`
- `to_volume_yd3_formula` 또는 `to_weight_lbs_formula`
- `material_required`
- `uncertainty_pct`

3. `project_presets`
- `project_id`, `default_materials[]`
- `recommended_units[]`
- `default_bulking_factor`

4. `dumpster_sizes`
- `size_yd` (10/15/20/30/40)
- `dimensions_approx`
- `included_tons_low/typ/high` (가격 정책용)
- `max_haul_tons_low/typ/high` (운영 상한용)
- `heavy_debris_max_fill_ratio`
- `clean_load_required_for_heavy(bool)`

5. `pricing_assumptions`
- `size_yd`, `rental_fee_low/typ/high`
- `overage_fee_per_ton_low/typ/high`
- `haul_fee_low/typ/high`
- `junk_removal_rate_basis`

### 6.3 권장 알고리즘

#### Step A) 입력 표준화

`line_item = { material_id, quantity, unit_id, conditions }`

`conditions`: wet, mixed_load, compacted, contamination_risk.

#### Step B) 부피 범위 계산

- 부피 기반 입력: 변환 + 입력 불확실성 적용.
- 면적x두께 입력: `area_sqft * (thickness_in / 12) / 27`.
- 무게 기반 입력: W 계산 후 밀도로 V 역산.

#### Step C) 무게 범위 계산

- `W = V * density / 2000`.
- wet 보정: 카테고리별 +10~30%.
- mixed 보정: V +10~20%.
- `data_quality=low`면 범위 폭 추가 확대.

#### Step D) 합산 및 bulking

- `V_total_typ = sum(V_typ)`.
- `V_total_safe = V_total_typ * bulking_factor` (1.15~1.30).
- `W_total_low/typ/high` 합산.

#### Step E) 추천 분기

1. `heavy_mode` 판단:
- heavy 카테고리 비중 >= 35% 또는
- 사용자 heavy 토글 on 또는
- `W_total_typ`가 10yd `included_tons_typ`의 80% 이상.

2. `standard_mode`:
- 부피 필터(`size_yd >= V_total_safe`) 후
- `included_tons` 대비 가격 리스크가 낮은 후보 우선 선택.

3. `heavy_mode`:
- 운반 가능성(`max_haul_tons`)을 우선 검사.
- "큰 용량 1회"보다 "10yd/15yd 다회"를 후보에 포함.
- fill ratio/clean-load 규칙 위반 시 자동 경고.

#### Step F) 리스크와 가능성 분리

- 가격 리스크(`R_price`) 판정:
- `High`: `W_low > included_tons`
- `Low`: `W_high < included_tons`
- `Medium`: 그 사이

- 운반 가능성(`F_ops`) 판정:
- `Not recommended`: `W_low > max_haul_tons_high`
- `Multi-haul required`: `W_typ > max_haul_tons_typ` 또는 heavy 정책 위반
- `OK`: 그 외

allowance 미입력 시:

- `included_tons_typ`를 임시값으로 사용.
- 결과 상단에 "추정 allowance 사용" 배지를 강제 노출.

#### Step G) 비용 비교

옵션별 총비용(범위) 계산:

- `Dumpster_single(size)`
- `Dumpster_multi(10yd 또는 15yd x n회)`
- `Junk_removal`

기본식:

`Total = rental + haul + max(0, W - included_tons) * overage_fee + optional_fees`

출력은 단정 금지:

- "가장 저렴할 가능성 높음"
- "비용 비슷함"
- "초과 위험 때문에 역전 가능"

#### Step H) 결과 문장 생성

반드시 포함:

- "예상 부피 X~Y yd^3, 무게 Z~Q tons"
- "안전 추천 / 예산 추천"
- "이 경우 큰 덤스터 1회가 정답이 아닐 수 있음"
- "업체에 확인할 3개 질문"
- "예산 옵션은 초과요금 가능성을 감수할 때만 선택"

## 7. 데이터 수급/구축 및 QA

### 7.1 필수 데이터

- 재료별 부피->무게 변환 계수.
- 프로젝트 프리셋.
- 덤스터 사이즈 정의.
- 비용 가정치(범위형).

### 7.2 추천 소스

- EPA: volume-weight conversion factors.
- 로컬 정부 conversion table 1~2개 교차검증.
- 주요 덤스터 업체 가이드(치수/현장 관행 참고).

### 7.3 데이터 해석 고정 문구

- "이 계산은 현장 적재(공극/혼합) 상황을 반영한 범위 추정입니다."
- "같은 재료라도 파쇄/압축/젖음에 따라 결과가 달라질 수 있습니다."

### 7.4 데이터 QA 운영 규칙

- `source_registry`에 원본 링크/접근일/버전 기록.
- 데이터 반영 시 자동 검증:
- 음수/0 값 금지.
- density 범위 역전 금지(`low <= typ <= high`).
- 단위 변환 허용 범위 벗어나면 실패.
- `included_tons <= max_haul_tons` 위반 시 실패.
- 변경 배포 전 회귀 테스트: 기준 시나리오 30개 결과 diff 확인.

## 8. SEO/정보구조 (Anti-Scaled)

### 8.1 Phase1 인덱싱 전략

- 목표 인덱스: 20~35 URL.
- 대표 URL 1개:
- `/dumpster/size-weight-calculator/`

- 보조 indexable URL:
- `/dumpster/weight/{material}/` 10~20개.
- `/dumpster/size/{project}/` 5~10개.
- `/dumpster/heavy-debris-rules/` 1개.

- 금지:
- City/ZIP 대량 페이지.
- 조합형 자동 생성 인덱싱.

### 8.2 파라미터/무한조합 통제

- 계산 상태는 URL 쿼리 대신 local state + server estimate_id.
- 프리셋 프리필은 hash(`#preset=...`) 또는 local storage 우선.
- 파라미터 URL은 noindex + canonical 대표 URL.

### 8.3 인덱스 페이지 품질 게이트

다음 4개가 없으면 발행 금지:

1. 실제 계산 인터랙션.
2. 고유 데이터 테이블.
3. 재료/프로젝트 맞춤 실수 방지 섹션.
4. 결과와 연결된 CTA.

### 8.4 스키마

- 대표 계산기: `WebApplication` 또는 `SoftwareApplication` + FAQ 4~6개.
- 재료 페이지: FAQ 3~5개 + Breadcrumb.
- 초기 단계 리뷰/평점 스키마 금지.

## 9. 전환/수익화 설계

### 9.1 CTA 원칙

CTA는 결과 숫자의 논리적 결과여야 한다.

- CTA A: Dumpster 견적/전화.
- CTA B: Junk Removal 비교.

### 9.2 리드 품질 분기 질문 3개

1. Homeowner / Contractor / Business.
2. 필요 시점(48시간/이번주/비교중).
3. Heavy 재료 포함 여부.

### 9.3 MVP CTA 라우팅 룰 (고정)

- `R_price=High` 또는 `F_ops != OK`면 Junk CTA를 동급 또는 1순위 배치.
- `heavy_debris_warning=true`면 multi-haul 안내 카드 + Junk CTA 강화.
- `persona=contractor`면 공유 링크/질문 체크리스트를 상단 배치.
- `need_timing=48h`면 전화 CTA와 준비물 체크리스트를 우선 노출.

### 9.4 전화 전환 보조

버튼 상단 체크리스트 3개 고정:

- 포함 톤?
- 초과 요금?
- heavy 재료 허용조건(clean load 필요 여부)?

## 10. 테크 설계

### 10.1 아키텍처

- Front: Next.js SSR + React.
- API: Next API routes / Serverless.
- DB: Postgres (초기 SQLite 가능).
- Cache: Edge + DB.
- Deploy: Vercel 또는 Cloudflare.

### 10.2 폴더 구조 예시

- `/app/dumpster/size-weight-calculator/page.tsx`
- `/app/dumpster/weight/[material]/page.tsx`
- `/app/dumpster/estimate/[id]/page.tsx` (noindex)
- `/lib/calc/estimate.ts`
- `/lib/calc/units.ts`
- `/lib/calc/risk.ts`
- `/lib/calc/feasibility.ts`
- `/lib/calc/cost.ts`
- `/lib/routing/cta.ts`
- `/data/materials.csv`
- `/data/units.json`
- `/data/dumpster_sizes.json`
- `/data/pricing_assumptions.json`
- `/app/api/estimate/route.ts`

### 10.3 API 인터페이스

입력/출력 스키마는 v1.1을 유지하되 다음 필드를 포함한다.

입력:

- `allowance_tons(optional)`
- `need_timing(48h|this_week|research)`
- `price_context(optional)`

출력:

- `price_risk`
- `feasibility`
- `hard_stop_reasons[]`
- `cost_comparison[]`
- `used_assumed_allowance(bool)`
- `input_impact_summary[]`
- `confidence_score`

### 10.4 SEO 기술 체크리스트

- 대표 URL index 허용.
- estimate/파라미터 페이지 noindex + canonical.
- sitemap에는 indexable URL만 포함.
- internal link는 대표 계산기 중심.

## 11. Anti-Scaled 품질 게이트

### 11.1 발행 규칙

아래 3개 미충족 시 발행 금지:

1. 고유 데이터/예시 계산.
2. 커스텀 실수 방지 섹션.
3. 결과 기반 CTA 연결.

### 11.2 운영 규칙

- 90일 무성과 페이지: sitemap 제외 + 내부 링크 축소 + 필요시 noindex.
- 얕은 페이지 대량 생성 금지.

## 12. KPI/계측 설계

### 12.1 필수 이벤트

- `calc_started`
- `calc_completed`
- `result_viewed`
- `cta_click_dumpster_call`
- `cta_click_dumpster_form`
- `cta_click_junk_call`
- `persona_selected`
- `heavy_debris_flagged`
- `allowance_entered`
- `used_assumed_allowance`
- `feasibility_not_ok`

### 12.2 다운스트림 이벤트

- `lead_submitted_valid`
- `call_connected_60s`
- `quote_received`
- `job_booked`
- `revenue_postback_received`

### 12.3 초기 목표

- calc start -> complete: 40~60%
- result -> CTA click: 2~6%
- valid lead rate: 20%+
- connected call(60s+): 25%+

## 13. 14일 실행 로드맵 (v1.2 Lock)

### Day 1~2: 데이터/모델

- Material CSV v0.
- Unit conversion JSON v0.
- Dumpster size JSON v0(`included`/`max_haul` 분리).
- Source registry + QA 스크립트.

### Day 3~5: 계산 엔진

- line_item 표준화.
- 범위 계산(low/typ/high).
- heavy_mode 분기.
- price_risk + feasibility 분리 판정.
- 비용 비교 모듈 v0.
- 테스트 30개.

### Day 6~8: 프론트/UI

- Quick mode 완성.
- Detailed mode(최대 3개 아이템).
- 결과 11개 컴포넌트.
- allowance 배지/입력 영향 요약/질문 체크리스트 구현.
- 모바일 퍼스트.

### Day 9~10: 수익화/트래킹

- 2축 CTA + 분기 질문.
- CTA 라우팅 룰 고정 구현.
- 콜 트래킹/폼 연동.
- 이벤트 + 다운스트림 이벤트 파이프라인.

### Day 11~12: SEO 소량 고품질

- material 페이지 10~20개.
- heavy rules 1개.
- noindex/canonical/sitemap 검증.

### Day 13~14: QA/런치

- sanity check + 회귀 테스트.
- 실패해야 하는 케이스 통과 여부 확인.
- CWV 점검.
- Search Console 등록 및 인덱싱 확인.

## 14. 실패 원인 TOP1 + 방지책

### TOP1 실패 원인

결과가 모호하거나 반대로 단정적이면 불신/이탈이 발생한다.

### 방지책

- 모든 결과를 범위 + 가정 + 리스크로 표시.
- 오차 요인(젖음/혼합/압축)을 사용자가 제어.
- 안전 추천 + 예산 추천 동시 제시.
- allowance 미입력 시 "추정 기반" 경고를 강제 표시.
- `feasibility`가 OK가 아니면 멀티홀/Junk 대안을 우선 제시.

## 15. Phase2(10,000+) 안전장치

1. Demand-gating: 수요 확인된 주제만 인덱스.
2. 중복 클러스터링: 유사 페이지 대표 1개만 인덱스.
3. 자동 롤백: 90일 무성과 페이지 noindex 전환.

## 16. 핵심 인사이트 7개

1. 덤스터 문제의 본질은 부피보다 무게 불확실성.
2. heavy debris는 작은 덤스터 다회가 유리할 수 있다.
3. 신규 도메인은 페이지 수보다 결과 신뢰가 중요하다.
4. 정답보다 리스크 관리가 전환을 만든다.
5. 결과는 업체 문의 질문까지 포함한 의사결정 키트여야 한다.
6. 롱테일은 material 페이지, 전환은 대표 계산기로 집중해야 한다.
7. 오퍼가 바뀌어도 사용자 불안 구조는 동일하므로 제품 중심 전략이 유효하다.

## 부록 A. 우선순위 재료 Top 25

- Heavy: concrete, dirt/soil, brick, gravel/rock, asphalt, roofing shingles.
- Mixed/common: mixed C&D, household junk, furniture, carpet, drywall, lumber.
- Special caution: yard waste(wet), plaster, tile/ceramic, wet insulation.

## 부록 B. QA 시나리오 최소 세트

1. shingles 20 squares: heavy_mode 진입 여부.
2. concrete 100 sqft x 4in: 10yd + fill ratio 경고 여부.
3. mixed household 6 pickup loads: 20yd 합리성.
4. wet yard waste on: 리스크 상승 반영.
5. allowance 2 tons: 경계값 반응.
6. budget 옵션 노출 + 위험 라벨 동시 표시.
7. 동일 입력 단위 변환 경로 차이 오차 검증.
8. 입력량 증가 시 추천 역전 금지.
9. noindex 대상 URL의 robots/canonical 검증.
10. 공유 링크 만료(TTL) 동작 검증.
11. 콘크리트 대량 입력 시 20/30yd 단일 추천 금지(멀티홀 유도).
12. 부피는 작고 무게는 큰(tile/brick/plaster) 케이스에서 다회/경고 판정.
13. `R_price=High` 또는 `F_ops!=OK`에서 Junk CTA 우선 배치 검증.
14. heavy 정책(clean load required) 위반 시 `Not recommended` 판정.
15. allowance 미입력 시 "추정 allowance" 배지 강제 노출.
