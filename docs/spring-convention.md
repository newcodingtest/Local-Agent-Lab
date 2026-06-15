# Spring Convention

## Dependency Injection

생성자 주입 사용

금지

@Autowired Field Injection

예시

private final UserService userService;

---

## Transaction

@Transactional은 Service 계층에서 사용

Controller 사용 금지

---

## Optional

금지

optional.get()

권장

optional.orElseThrow()

예시

User user = userRepository.findById(id)
.orElseThrow(...)

---

## Null 처리

null 반환 지양

Optional 사용

---

## Logging

log.info 사용

System.out.println 금지

---

## Stream

복잡한 Stream 체인은 지양

가독성을 우선한다.