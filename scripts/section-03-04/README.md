# Section 3 • 4 - 예제 만들기

---

이번 섹션에서는 `Spring` 의 이점을 알아보기 위해 한 예제를 `Java` 로만 만들어 본다.

## i. 비즈니스 요구사항과 설계

1. 회원가입하고 조회할 수 있다.
2. 회원은 일반과 VIP 등급으로 나뉜다.
3. 회원 데이터는 DB 든 In-memory 든 알아서 한다.
4. 회원은 상품을 주문할 수 있다.
5. 회원 등급에 따라 할인 정책을 적용할 수 있다.
    - 일단 VIP 1000 원 할인으로 생각하고 있지만, 변경 가능성이 크다.

> ### 회원 등급에 따른 할인 정책 _(tip)_
>
> 지금 요구사항에 `등급에 따른 할인 정책` 이 `fix` 되지 않았다. 그러니까 interface 이용해서 `역할과 구현` 을 분리하는 것이 좋다.

음... 요구 사항 그대로 만들면 된다.

`Repo` 들은 문제에서 말한대로 `DB`, `In-memory` 둘다 사용할 수 있게 `interface` 를 먼저 정의했고, `Service` 들은 `변경될 가능성이 낮다`
고 생각해 안 정의했다.

또 `DB 외래키` 처럼 엔티티간 연관관계를 단단히 하고 싶어서 `Repo` 간 의존성을 만들었다.

```java
public class ShippingDetailRepositoryMap
        implements ShippingDetailRepository {

    private final ProductRepository productRepo;
    private final ShippingSummaryRepository shippingSummaryRepo;

    /* ... */
}
```

만드는게 어렵진 않았지만 `Java` 가 왜 `Horse factory` 라 불리는지 알 수 있었다.
~~간단한건데 뭐 이리 만들어야 되는게 많어...~~

그러니까 프레임웍이 있는거겠지?

---

이번 절의 핵심은 결국 `Spring 의 역할이 무엇인지 알려주는` 데 있다고 생각한다.

강의에서는 `AppConfig` 를 통해 역할과 구현을 분리하고 **나중에 변경이 필요할 때 구현만 바꾸는 형태**로 편하게 사용할 수 있는걸 보여준다.

즉, `AppConfig` 의 역할이 `Spring` 이 해주는 것과 동치한다는 것이다.
물론 `Spring` 이 제공하는 역할은 이거보다 더 많지만 그 핵심 중 하나임은 분명하다.

어떤 새로운 내용을 배우거나 하진 않았지만 `처음 Spring 을 배우는 사람` 에게는 `Spring 이 왜 좋은지` 확실히 체감시켜 줄 것 같다.

---