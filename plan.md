# TakiReminder 개발 계획

단순한 기능부터 점진적으로 완성도를 높여가는 4단계 개발 계획.
작업 진행 상황은 [tasks.md](tasks.md)에서 관리한다.

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

- 프로젝트 설정 (CORS, H2)
- ReminderList 도메인 (Entity → Repository → Service → Controller)
- Reminder 도메인 (Entity → Repository → Service → Controller)
- 초기 데이터 (`data.sql`)

**완료 기준**: H2 Console에서 데이터 확인 가능, cURL로 모든 API 동작 확인

---

## Phase 2: Frontend 기본 레이아웃 + 리스트 연동

> 목표: Apple Reminders 스타일의 사이드바 레이아웃을 구현하고, 리스트 CRUD를 연동한다.

- Next.js 프로젝트 초기화 (Tailwind, TypeScript, API 클라이언트)
- 2-column 레이아웃 (사이드바 280px + 메인 영역)
- 스마트 리스트 2×2 그리드 카드
- 사용자 리스트 목록 + CRUD 연동

**완료 기준**: 사이드바에 리스트가 표시되고, 추가/수정/삭제 동작

---

## Phase 3: 리마인더 목록 + CRUD

> 목표: 메인 영역에 리마인더를 표시하고, 생성/수정/삭제/완료를 구현한다.

- 리마인더 목록 표시 (체크박스 + 제목 + 마감일 + 우선순위)
- 인라인 리마인더 생성
- 완료 토글 + 애니메이션
- 디테일 패널 (편집)
- 삭제

**완료 기준**: 리마인더 전체 CRUD, 완료 토글 애니메이션, 디테일 패널 편집

---

## Phase 4: 스마트 리스트 + 정렬 + 반응형

> 목표: 스마트 리스트 필터, 정렬 기능, 반응형 레이아웃을 완성한다.

- 스마트 리스트 Backend API (today, scheduled, all, completed)
- 스마트 리스트 Frontend 연동 + 카운트
- 정렬 드롭다운 (마감일/우선순위/생성일)
- 반응형 모바일 레이아웃
- 마무리 (empty state, 에러 처리, 인터랙션 정리)

**완료 기준**: 스마트 리스트 4종, 정렬, 모바일 네비게이션, Apple Reminders 수준 완성도
