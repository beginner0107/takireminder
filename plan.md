# TakiReminder 개발 계획

단순한 기능부터 점진적으로 완성도를 높여가는 4단계 개발 계획.

## 기술 스택 상세

| 영역 | 기술 | 비고 |
|------|------|------|
| Backend | Spring Boot 4.0.5 / Java 25 | Spring WebMVC (REST API) |
| ORM | Spring Data JPA | Hibernate 기반 |
| DB | H2 (embedded) | 파일 모드, 개발용 인메모리 |
| Frontend | Next.js (latest) | App Router, React 19 |
| 스타일 | Tailwind CSS | Apple 디자인 시스템 커스텀 |
| HTTP 통신 | fetch API | Next.js 내장 |
| 빌드 | Gradle (Kotlin DSL) | Spring Boot 플러그인 |

### 개발 환경

- Backend: `localhost:8080`
- Frontend: `localhost:3000` (Next.js dev server)
- H2 Console: `localhost:8080/h2-console`
- CORS: Backend에서 `localhost:3000` 허용

---

## Phase 1: Backend 기본 CRUD

> 목표: 리스트와 리마인더의 기본 CRUD API를 동작시킨다.

### 1-1. 프로젝트 설정
- [x] Spring Boot 프로젝트 초기화 (build.gradle.kts, application.yml)
- [ ] CORS 설정 (`WebMvcConfigurer`)
- [ ] H2 데이터베이스 설정 확인

### 1-2. ReminderList (리스트)
- [ ] `ReminderList` Entity (id, name, color, createdAt, updatedAt)
- [ ] `ReminderListRepository` (JpaRepository)
- [ ] `ReminderListService` (CRUD 로직)
- [ ] `ReminderListController` (GET/POST/PUT/DELETE /api/lists)

### 1-3. Reminder (리마인더)
- [ ] `Priority` Enum (NONE, LOW, MEDIUM, HIGH)
- [ ] `Reminder` Entity (id, title, notes, dueDate, priority, completed, completedAt, listId, createdAt, updatedAt)
- [ ] `ReminderRepository` (JpaRepository)
- [ ] `ReminderService` (CRUD + 완료 토글)
- [ ] `ReminderController` (GET/POST/PUT/PATCH/DELETE)

### 1-4. 초기 데이터
- [ ] `data.sql`로 샘플 리스트 3개 + 리마인더 10개 세팅

### 완료 기준
- H2 Console에서 데이터 확인 가능
- cURL/Postman으로 모든 API 동작 확인

---

## Phase 2: Frontend 기본 레이아웃 + 리스트 연동

> 목표: Apple Reminders 스타일의 사이드바 레이아웃을 구현하고, 리스트 CRUD를 연동한다.

### 2-1. Next.js 프로젝트 초기화
- [ ] `frontend/` 디렉토리에 Next.js 프로젝트 생성 (App Router)
- [ ] Tailwind CSS 설정
- [ ] 글로벌 스타일: Apple 시스템 폰트, 색상 변수 정의
- [ ] TypeScript 타입 정의 (`types.ts`)
- [ ] API 클라이언트 유틸 (`api.ts`)

### 2-2. 사이드바 레이아웃
- [ ] 2-column 레이아웃 (사이드바 280px + 메인 영역)
- [ ] 사이드바 배경색 `#F2F2F7`
- [ ] 스마트 리스트 2×2 그리드 카드 (오늘/예정/전체/완료됨) — 카운트는 0으로 표시 (Phase 3에서 연동)
- [ ] 사용자 리스트 목록 (색상 원형 아이콘 + 이름 + 카운트)
- [ ] 리스트 선택 시 하이라이트

### 2-3. 리스트 CRUD 연동
- [ ] 리스트 조회 (GET /api/lists)
- [ ] 리스트 생성 다이얼로그 (이름 + 색상 선택)
- [ ] 리스트 수정/삭제

### 완료 기준
- 사이드바에 리스트 목록이 표시됨
- 리스트 추가/수정/삭제가 동작함
- Apple Reminders와 유사한 사이드바 외관

---

## Phase 3: 리마인더 목록 + CRUD

> 목표: 메인 영역에 리마인더를 표시하고, 생성/수정/삭제/완료를 구현한다.

### 3-1. 리마인더 목록 표시
- [ ] 선택된 리스트의 리마인더 조회 (GET /api/lists/{id}/reminders)
- [ ] 리마인더 아이템 컴포넌트: 원형 체크박스 + 제목 + 마감일 + 우선순위
- [ ] 체크박스 색상: 소속 리스트 색상 연동
- [ ] 리스트 제목 34px bold (iOS Large Title)

### 3-2. 리마인더 생성
- [ ] 하단 `+ 새로운 리마인더` → 인라인 입력 필드 활성화
- [ ] 제목 입력 후 Enter로 생성 (POST /api/lists/{id}/reminders)

### 3-3. 리마인더 완료 토글
- [ ] 체크박스 클릭 → PATCH /api/reminders/{id}/complete
- [ ] 완료 시: 체크 애니메이션 + 취소선 + opacity 0.5
- [ ] 0.5초 딜레이 후 목록에서 페이드아웃 제거

### 3-4. 리마인더 편집 (디테일 패널)
- [ ] 리마인더 클릭 시 우측 디테일 패널 슬라이드
- [ ] 편집 필드: 제목, 메모, 마감일 (날짜 피커), 우선순위 (세그먼트 컨트롤), 리스트 이동
- [ ] Apple 스타일 폼: 둥근 모서리 그룹 카드, 라벨-값 행 구조
- [ ] 수정 저장 (PUT /api/reminders/{id})

### 3-5. 리마인더 삭제
- [ ] 호버 시 삭제 아이콘 표시
- [ ] 삭제 확인 후 DELETE /api/reminders/{id}

### 완료 기준
- 리마인더 전체 CRUD 동작
- 완료 토글 애니메이션
- 디테일 패널에서 편집 가능

---

## Phase 4: 스마트 리스트 + 정렬 + 반응형

> 목표: 스마트 리스트 필터, 정렬 기능, 반응형 레이아웃을 완성한다.

### 4-1. 스마트 리스트 API 연동
- [ ] Backend: 스마트 리스트 API 구현 (today, scheduled, all, completed)
- [ ] 스마트 리스트 카드 클릭 → 해당 필터 결과를 메인 영역에 표시
- [ ] 각 카드의 카운트를 실제 데이터로 표시

### 4-2. 정렬
- [ ] 정렬 드롭다운 UI (마감일 / 우선순위 / 생성일)
- [ ] `?sort=dueDate|priority|createdAt&direction=asc|desc` 쿼리 파라미터 연동

### 4-3. 반응형 (모바일)
- [ ] 모바일 (<768px): 사이드바가 전체 화면으로 전환
- [ ] 리스트 선택 시 리마인더 화면으로 네비게이션 (← 뒤로가기)
- [ ] 디테일 패널: 모바일에서는 전체 화면 모달

### 4-4. 마무리
- [ ] 리스트 삭제 시 소속 리마인더 cascade 삭제 확인
- [ ] 빈 상태 (empty state) UI
- [ ] 호버/포커스 인터랙션 정리
- [ ] 에러 처리 (API 실패 시 토스트 등)

### 완료 기준
- 스마트 리스트 4종 동작
- 정렬 전환 동작
- 모바일에서 자연스러운 네비게이션
- Apple Reminders와 유사한 전체적인 완성도
