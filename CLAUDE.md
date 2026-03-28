# CLAUDE.md

## 패키지 구조
- `domain`: 엔티티 클래스 (entity 아님)
- `service.ports.in`: Service 인터페이스
- `service`: Service 구현 클래스
- `repository`: JPA Repository
- `controller`: REST Controller
- `dto`: DTO 클래스

### Service

- 인터페이스는 `service.ports.in` 패키지에 정의
- 구현 클래스는 `service` 패키지에 `Default` 접두사로 네이밍 (예: `DefaultReminderListService`)
- Mock 테스트 사용 금지, `@SpringBootTest` 통합 테스트로 작성

### 테스트

- 기능 추가/수정 시 반드시 검증 테스트 함께 작성
- 도메인 엔티티 테스트: 순수 단위 테스트 (JPA/Spring Context 사용하지 않음)
- Service 테스트: `@SpringBootTest` + `@Transactional` 통합 테스트
- 
## 참고 문서
- spec.md: 기능 명세
- plan.md: 개발 계획
- tasks.md: 구현 테스크  체크리스트
