# TakiReminder Spec

Apple Reminder App의 핵심 기능을 웹으로 구현하는 프로젝트

## 기술 스택

| 영역 | 기술 |
|------|------|
| Backend | Spring Boot 4.0.5, Java 25, Spring WebMVC |
| ORM / DB | Spring Data JPA / H2 (embedded) |
| Frontend | Next.js (latest, App Router) |
| 통신 | REST API (JSON) |

## 핵심 기능

### 1. 리마인더 (Reminder)

- 리마인더 생성 / 수정 / 삭제
- 제목 (필수), 메모 (선택)
- 마감일시 설정 (선택)
- 우선순위: 없음 / 낮음 / 보통 / 높음
- 완료 체크 / 해제
- 리스트에 소속

### 2. 리스트 (ReminderList)

- 리스트 생성 / 수정 / 삭제
- 이름, 색상 지정
- 리스트 삭제 시 소속 리마인더 함께 삭제

### 3. 스마트 리스트 (Smart List) - 고정 필터 뷰

서버에서 필터링하여 제공하는 가상 리스트 (CRUD 없음)

| 이름 | 조건 |
|------|------|
| 오늘 | dueDate = 오늘 & 미완료 |
| 예정 | dueDate > 오늘 & 미완료 |
| 전체 | 미완료 전체 |
| 완료됨 | 완료된 항목 |

### 4. 정렬

- 리스트 내 리마인더를 마감일 / 우선순위 / 생성일 기준으로 정렬

## 데이터 모델

### ReminderList

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long (PK) | 자동 생성 |
| name | String | 리스트 이름 |
| color | String | HEX 색상코드 (e.g. #FF6B6B) |
| createdAt | LocalDateTime | 생성일시 |
| updatedAt | LocalDateTime | 수정일시 |

### Reminder

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long (PK) | 자동 생성 |
| title | String | 제목 (필수) |
| notes | String (nullable) | 메모 |
| dueDate | LocalDateTime (nullable) | 마감일시 |
| priority | Enum (NONE, LOW, MEDIUM, HIGH) | 우선순위 |
| completed | boolean | 완료 여부 |
| completedAt | LocalDateTime (nullable) | 완료 시각 |
| listId | Long (FK) | 소속 리스트 |
| createdAt | LocalDateTime | 생성일시 |
| updatedAt | LocalDateTime | 수정일시 |

## API 설계

### 리스트 API

| Method | Path | 설명 |
|--------|------|------|
| GET | /api/lists | 전체 리스트 조회 (각 리스트별 미완료 카운트 포함) |
| POST | /api/lists | 리스트 생성 |
| PUT | /api/lists/{id} | 리스트 수정 |
| DELETE | /api/lists/{id} | 리스트 삭제 (소속 리마인더 함께 삭제) |

### 리마인더 API

| Method | Path | 설명 |
|--------|------|------|
| GET | /api/lists/{listId}/reminders | 특정 리스트의 리마인더 조회 |
| POST | /api/lists/{listId}/reminders | 리마인더 생성 |
| PUT | /api/reminders/{id} | 리마인더 수정 |
| PATCH | /api/reminders/{id}/complete | 완료 토글 |
| DELETE | /api/reminders/{id} | 리마인더 삭제 |

### 스마트 리스트 API

| Method | Path | 설명 |
|--------|------|------|
| GET | /api/reminders/today | 오늘 마감 & 미완료 |
| GET | /api/reminders/scheduled | 예정된 리마인더 |
| GET | /api/reminders/all | 미완료 전체 |
| GET | /api/reminders/completed | 완료된 항목 |

공통: `?sort=dueDate|priority|createdAt&direction=asc|desc` 쿼리 파라미터 지원

## 화면 구성

Apple Reminders 앱의 UI/UX를 최대한 유사하게 웹으로 구현한다.

### 메인 화면 (사이드바 + 콘텐츠)

```
┌─────────────────────────────────────────────────────────────┐
│  ◀  Search                                    [Edit]       │
├──────────────────┬──────────────────────────────────────────┤
│                  │                                          │
│  ┌─────┐┌─────┐ │                                          │
│  │📅  3││📆  5│ │  리스트명                                 │
│  │오늘 ││예정 │ │  ───────────────────────────────────────  │
│  └─────┘└─────┘ │                                          │
│  ┌─────┐┌─────┐ │  ○  우유 사기                    3/28    │
│  │📋 12││✓   8│ │     메모: 저지방으로          🔴 높음     │
│  │전체 ││완료됨│ │                                          │
│  └─────┘└─────┘ │  ○  보고서 작성                  3/30    │
│                  │                                     보통  │
│  ─────────────── │                                          │
│  내 목록         │  ◉  이메일 확인                  3/27    │
│                  │     (완료 시 취소선 + 흐리게)             │
│  🔴 개인      4  │                                          │
│  🔵 업무      6  │                                          │
│  🟢 쇼핑      2  │                                          │
│                  │                                          │
│  + 목록 추가     │  + 새로운 리마인더                        │
│                  │                                          │
├──────────────────┴──────────────────────────────────────────┤
│  ● 오늘  ● 예정  ● 전체  (하단 요약 바 - 선택 시 필터)      │
└─────────────────────────────────────────────────────────────┘
```

### Apple Reminders 스타일 디자인 가이드

#### 전체 레이아웃
- **좌측 사이드바** (280px): 스마트 리스트 그리드 (2×2) + 사용자 리스트 목록
- **우측 메인 영역**: 선택된 리스트의 리마인더 목록
- 배경색: 사이드바 `#F2F2F7` (iOS 시스템 그레이), 메인 영역 `#FFFFFF`
- 반응형: 모바일에서는 사이드바가 전체 화면으로 전환 (네비게이션 방식)

#### 스마트 리스트 (상단 그리드)
- 2×2 카드 그리드로 배치 (오늘, 예정, 전체, 완료됨)
- 각 카드: 둥근 모서리 (`border-radius: 12px`), 아이콘 + 카운트 + 라벨
- 카드 색상: 오늘=파랑, 예정=빨강, 전체=검정, 완료됨=회색
- 카운트는 카드 우상단에 굵게 표시

#### 사용자 리스트 목록
- 리스트 아이콘: 색상이 적용된 원형 아이콘 (●)
- 리스트 이름 + 우측에 미완료 카운트 (회색)
- 하단에 "목록 추가" 버튼

#### 리마인더 아이템
- **미완료**: 빈 원형 체크박스 (`○`) + 제목 (좌측), 마감일/우선순위 (우측)
- **완료**: 채워진 체크박스 (`◉`) + 취소선 제목 + opacity 0.5
- 우선순위 표시: 높음=`🔴`/`!!!`, 보통=`🟠`/`!!`, 낮음=`🔵`/`!`
- 체크박스 색상: 소속 리스트의 색상을 따름
- 클릭 시 우측에 디테일 패널 슬라이드 또는 인라인 확장

#### 리마인더 디테일 (편집 패널)
- 리마인더 클릭 시 우측에 디테일 패널 표시 (Apple 스타일 슬라이드)
- 필드: 제목, 메모, 마감일 (날짜 피커), 우선순위 (세그먼트 컨트롤), 리스트 선택
- Apple 스타일 폼 UI: 둥근 모서리 그룹 카드, 라벨-값 행 구조

#### 인터랙션
- 체크박스 토글 시 완료 애니메이션 (체크 + 페이드아웃, 0.5초 딜레이 후 목록에서 제거)
- 리스트 항목 호버 시 배경색 하이라이트
- 새 리마인더 추가: 목록 하단 `+ 새로운 리마인더` 클릭 → 인라인 입력 필드 활성화
- 스와이프/우클릭 삭제 (웹에서는 호버 시 삭제 아이콘 표시)

#### 타이포그래피 & 색상
- 폰트: `-apple-system, BlinkMacSystemFont, 'SF Pro Text', system-ui, sans-serif`
- 리스트 제목: 34px bold (iOS Large Title 스타일)
- 리마인더 제목: 17px regular
- 서브텍스트 (메모, 날짜): 15px, `#8E8E93`
- 주요 액센트 색상: `#007AFF` (iOS 블루)
- 구분선: `#C6C6C8`, 0.5px

## 프로젝트 구조

```
takireminder/
├── src/main/java/taki/ai/takireminder/   # Spring Boot Backend
│   ├── list/
│   │   ├── ReminderList.java              # Entity
│   │   ├── ReminderListRepository.java
│   │   ├── ReminderListService.java
│   │   └── ReminderListController.java
│   ├── reminder/
│   │   ├── Reminder.java                  # Entity
│   │   ├── Priority.java                  # Enum
│   │   ├── ReminderRepository.java
│   │   ├── ReminderService.java
│   │   └── ReminderController.java
│   └── TakireminderApplication.java
├── frontend/                              # Next.js Frontend
│   ├── src/app/
│   │   ├── layout.tsx
│   │   ├── page.tsx
│   │   └── globals.css
│   ├── src/components/
│   │   ├── Sidebar.tsx
│   │   ├── ReminderList.tsx
│   │   ├── ReminderItem.tsx
│   │   └── ReminderForm.tsx
│   └── src/lib/
│       ├── api.ts                         # API 클라이언트
│       └── types.ts                       # TypeScript 타입
└── build.gradle.kts
```

## 제외 범위 (v1)

- 사용자 인증 / 로그인
- 리마인더 반복 설정
- 태그 / 플래그
- 위치 기반 알림
- 드래그 앤 드롭 정렬
- 서브 리마인더 (하위 항목)
