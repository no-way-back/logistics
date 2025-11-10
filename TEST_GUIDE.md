## 🔬 테스트 가이드
### 테스트 네이밍 규칙
- 테스트 성공 : `[행위]_success()`
- 테스트 실패 : `[행위]_[상황]_[기대결과]()`

### Fixture 사용법
- 목적
    - 테스트 간 중복 데이터를 제거하고 일관된 테스트 데이터를 재사용하기 위함
    - Fixture는 테스트 데이터의 공통 생성 책임을 가짐
- 작성 위치 
    - 각 도메인 별 `fixture` 패키지에 위치
- 관련 코드
    - [DeliveryFixture](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/delivery/src/test/java/com/nowayback/delivery/fixture/DeliveryFixture.java)
    - [OrderFixture](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/order/src/test/java/com/nowayback/order/fixture/OrderFixture.java)

### Mock 사용 가이드
- 목적
    - 외부 의존성(DB, API, 외부 서비스 등)을 제거하고 순수한 서비스 로직만 검증하기 위함
    - `Mockito`와 `@Mock`, `@InjectMocks`를 기본으로 사용
- Mocking 원칙
    - 하나의 테스트에서는 단 하나의 동작만 검증한다.
    - 외부 호출은 반드시 `given()` 또는 `when()`으로 Mocking한다.
    - Mocking이 불필요한 부분은 실제 객체(Fixture 등)를 활용한다.
    - 검증은 `verify()`를 통해 명시적으로 수행한다.
- 주의사항
    - Mock은 테스트 단순화를 위한 도구이며, 비즈니스 로직 자체를 Mocking하지 않는다.
    - Mock 객체의 리턴값이 테스트의 핵심 검증 대상이 되지 않도록 한다.
    - 실제 로직의 흐름을 검증하는 데 집중한다.
