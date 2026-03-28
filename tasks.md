# TakiReminder 작업 목록

## Phase 1: Backend 기본 CRUD

### 1-1. 프로젝트 설정
- [x] Spring Boot 프로젝트 초기화 (build.gradle.kts, application.yml)
- [x] `WebMvcConfigurer` 구현하여 CORS 설정 (localhost:3000 허용)
- [x] H2 데이터베이스 연결 및 콘솔 접근 확인

### 1-2. ReminderList 도메인
- [x] `ReminderList` JPA Entity 작성 (id, name, color, createdAt, updatedAt)
- [x] `ReminderListRepository` 인터페이스 작성 (JpaRepository 상속)
- [x] `ReminderListService` 작성 (전체 조회, 단건 조회, 생성, 수정, 삭제)
- [x] `ReminderListController` 작성
  - [x] `GET /api/lists` — 전체 리스트 조회 (미완료 카운트 포함)
  - [x] `POST /api/lists` — 리스트 생성
  - [x] `PUT /api/lists/{id}` — 리스트 수정
  - [x] `DELETE /api/lists/{id}` — 리스트 삭제 (소속 리마인더 cascade)

### 1-3. Reminder 도메인
- [x] `Priority` Enum 작성 (NONE, LOW, MEDIUM, HIGH)
- [x] `Reminder` JPA Entity 작성 (id, title, notes, dueDate, priority, completed, completedAt, list 연관관계, createdAt, updatedAt)
- [x] `ReminderRepository` 인터페이스 작성 (JpaRepository 상속 + 커스텀 쿼리)
- [x] `ReminderService` 작성 (리스트별 조회, 생성, 수정, 완료 토글, 삭제)
- [x] `ReminderController` 작성
  - [x] `GET /api/lists/{listId}/reminders` — 리스트별 리마인더 조회
  - [x] `POST /api/lists/{listId}/reminders` — 리마인더 생성
  - [x] `PUT /api/reminders/{id}` — 리마인더 수정
  - [x] `PATCH /api/reminders/{id}/complete` — 완료 토글
  - [x] `DELETE /api/reminders/{id}` — 리마인더 삭제

### 1-4. 초기 데이터 & 검증
- [x] `data.sql` 작성 (샘플 리스트 3개 + 리마인더 10개)
- [x] H2 Console에서 데이터 확인
- [x] cURL로 전체 API 엔드포인트 동작 검증

---

## Phase 2: Frontend 기본 레이아웃 + 리스트 연동

### 2-1. Next.js 프로젝트 초기화
- [x] `frontend/` 디렉토리에 Next.js 프로젝트 생성 (App Router, TypeScript)
- [x] Tailwind CSS 설치 및 설정
- [x] `globals.css`에 Apple 시스템 폰트 및 색상 변수 정의
- [x] `src/lib/types.ts` — ReminderList, Reminder, Priority 타입 정의
- [x] `src/lib/api.ts` — fetch 기반 API 클라이언트 함수 작성

### 2-2. 레이아웃 구현
- [x] `layout.tsx` — 2-column 레이아웃 (사이드바 280px + 메인 영역)
- [x] 사이드바 컨테이너: 배경색 `#F2F2F7`, 스크롤 가능

### 2-3. 사이드바 — 스마트 리스트 카드
- [x] `SmartListGrid` 컴포넌트 — 2×2 그리드 배치
- [x] 각 카드: 둥근 모서리, 아이콘, 카운트(0), 라벨
- [x] 카드 색상 적용 (오늘=파랑, 예정=빨강, 전체=검정, 완료됨=회색)

### 2-4. 사이드바 — 사용자 리스트
- [x] `Sidebar` 컴포넌트 — 리스트 목록 렌더링
- [x] 리스트 아이템: 색상 원형 아이콘(●) + 이름 + 미완료 카운트
- [x] 리스트 선택 시 배경 하이라이트
- [x] `+ 목록 추가` 버튼

### 2-5. 리스트 CRUD 연동
- [x] 리스트 조회 API 연동 (GET /api/lists → 사이드바에 표시)
- [x] 리스트 생성 다이얼로그 (이름 입력 + 색상 선택 팔레트)
- [x] 리스트 수정 (이름/색상 변경)
- [x] 리스트 삭제 (확인 다이얼로그 포함)

---

## Phase 3: 리마인더 목록 + CRUD

### 3-1. 리마인더 목록 표시
- [ ] 메인 영역에 선택된 리스트 제목 표시 (34px bold, iOS Large Title)
- [ ] `GET /api/lists/{id}/reminders` 연동하여 리마인더 목록 렌더링
- [ ] `ReminderItem` 컴포넌트: 원형 체크박스 + 제목 + 메모 미리보기 + 마감일 + 우선순위
- [ ] 체크박스 색상을 소속 리스트 색상으로 연동

### 3-2. 리마인더 생성
- [ ] 목록 하단 `+ 새로운 리마인더` 클릭 → 인라인 입력 필드 활성화
- [ ] 제목 입력 후 Enter → `POST /api/lists/{id}/reminders` 호출
- [ ] 생성 후 목록에 즉시 반영

### 3-3. 완료 토글
- [ ] 체크박스 클릭 → `PATCH /api/reminders/{id}/complete` 호출
- [ ] 완료 애니메이션: 체크 표시 + 취소선 + opacity 0.5
- [ ] 0.5초 딜레이 후 목록에서 페이드아웃 제거

### 3-4. 디테일 패널 (편집)
- [ ] 리마인더 클릭 시 우측 디테일 패널 슬라이드 인
- [ ] 제목 편집 필드
- [ ] 메모 편집 필드 (textarea)
- [ ] 마감일 날짜 피커
- [ ] 우선순위 세그먼트 컨트롤 (없음/낮음/보통/높음)
- [ ] 소속 리스트 변경 드롭다운
- [ ] Apple 스타일 폼 UI: 둥근 모서리 그룹 카드, 라벨-값 행 구조
- [ ] 변경 사항 저장 → `PUT /api/reminders/{id}` 호출

### 3-5. 리마인더 삭제
- [ ] 리마인더 아이템 호버 시 삭제 아이콘(✕) 표시
- [ ] 클릭 시 확인 후 `DELETE /api/reminders/{id}` 호출
- [ ] 삭제 후 목록에서 즉시 제거

---

## Phase 4: 스마트 리스트 + 정렬 + 반응형

### 4-1. 스마트 리스트 Backend API
- [ ] `GET /api/reminders/today` — 오늘 마감 & 미완료
- [ ] `GET /api/reminders/scheduled` — 예정된 리마인더
- [ ] `GET /api/reminders/all` — 미완료 전체
- [ ] `GET /api/reminders/completed` — 완료된 항목
- [ ] 공통 정렬 쿼리 파라미터 지원 (`?sort=...&direction=...`)

### 4-2. 스마트 리스트 Frontend 연동
- [ ] 스마트 리스트 카드 클릭 → 해당 API 호출 → 메인 영역에 결과 표시
- [ ] 각 스마트 리스트 카드의 카운트를 실제 데이터로 갱신

### 4-3. 정렬 기능
- [ ] 메인 영역 상단에 정렬 드롭다운 UI 추가
- [ ] 정렬 옵션: 마감일 / 우선순위 / 생성일 (asc/desc)
- [ ] 선택 시 `?sort=...&direction=...` 쿼리로 API 재호출

### 4-4. 반응형 레이아웃 (모바일)
- [ ] 모바일 (<768px): 사이드바를 전체 화면으로 전환
- [ ] 리스트 선택 시 리마인더 목록 화면으로 전환 (← 뒤로가기 버튼)
- [ ] 디테일 패널: 모바일에서는 전체 화면 모달로 표시

### 4-5. 마무리 & 폴리싱
- [ ] 리스트 삭제 시 소속 리마인더 cascade 삭제 동작 확인
- [ ] 빈 상태(empty state) UI — 리마인더 없을 때 안내 메시지
- [ ] 호버/포커스 인터랙션 일관성 점검
- [ ] API 실패 시 에러 토스트 표시
