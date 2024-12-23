# Section 10 - 빈 스코프

---

`Bean Scope` 는 이전에 공부하며 잘 알고 있었다.
하지만 공부하면서도 `"Prototype Bean Scope 을 잘 제공하는 방법이 없을까?"` 하는 고민이 있었는데, 이번 강의에서 이를 정확하게 꼬집어 설명하기에 정리하고자
한다.

---

## I. `ObjectProvider<T>`

이전 `Spring Starts Here` 를 공부하며 `Singleton 속 Prototype Bean` 을 다음처럼 사용하라 나와있었다.

```java

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_PROTOTYPE)
class PrototypeBean {

}

@Service
@RequiredArgsConstructor
class MyService {

    private final ApplicationContext context;

    public void doSomething() {
        PrototypeBean prototypeBean
                = context.getBean(PrototypeBean.class);

        /* ... */
    }
}
```

하지만 보이다시피 `MyService` 가 `Spring context` 를 의존하는건 배보다 배꼽이 커보이고 무엇보다 코드가 더럽다.

이러한 상황에서 강의는 `ObjectProvider` 를 제시한다.

```java

@Service
@RequiredArgsConstructor
class MyService {

    private final private final ObjectProvider<PrototypeBean> prototypeProvider;

    public void doSomething() {
        PrototypeBean prototypeBean
                = prototypeProvider.getObject();

        /* ... */
    }
}
```

`ObjectProvider` 자체는 아래처럼 추상적으로 정의되어 있다.

```java
package org.springframework.beans.factory;

public interface ObjectProvider<T> extends ObjectFactory<T>, Iterable<T> {

    @Override
    default T getObject() throws BeansException {/* ... */}

    default T getObject(Object... args) throws BeansException {/* ... */}

    @Nullable
    default T getIfAvailable() throws BeansException {/* ... */}

    default T getIfAvailable(Supplier<T> defaultSupplier) throws BeansException {/* ... */}

    /* ... */
}
```

`default` 메서드 구현 내용이 너무 길어 생략했지만 대게 `지원하지 않는 경우의 에러처리` 가 되어 있었다.

`Bean Scope` 를 공부하면서 분명히 `ObjectProvider` 같은게 있을거라 생각했는데 그 당시에는 찾지 못했었다.

지금에서라도 이 존재를 알게 되었으므로 종종 이용할 것 같다.

---

## II. `WEB_SCOPE` 로 요청별 로그 남기기

사실 `Prototype Scope` 처럼 수명이 제한된 `Bean` 은 사용할 일이 정말로 거의 없다.

그나마 한번 필요했던 적이 있었는데, 바로 이전 프로젝트에서
진행했던 [`MDC 를 이용한 로그 분리`](https://velog.io/@jbw9964/Devcourse-%EC%B5%9C%EC%A2%85-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Devcourse-%ED%9A%8C%EA%B3%A0#mdc-%EB%A1%9C%EA%B7%B8-%EB%B6%84%EB%A6%AC)
에서다.

`MDC 를 이용한 로그 분리` 는 배포된 서버의 에러 추적에 용이하기 위해 구현했었다. 이 때 `요청별 고유한 UUID` 를 설정하였는데, 그 당시에 `WEB_SCOPE` 를
이용하고 싶었지만 `ObjectProvider` 를 몰라 아래처럼 `HttpServletRequest` 에 몰래 끼워 넣었었다.

```java
public class MDCLoggingFilter extends OncePerRequestFilter {

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // request 에 요청 UUID 몰래 끼워넣기
        String requestUUID = UUID.randomUUID().toString();
        request.setAttribute("custom-request-uuid", requestUUID);

        /* ... */
    }
}
```

하지만 이제 `ObjectProvider` 를 알게 되었으므로 좀 더 완전하게 이를 재구성 할 수 있다.

아래 코드는 `ObjectProvider` 를 이용한 간단한 로그 예시이다.
`MDC Logback` 설정은 `ObjectProvider` 외의 내용이므로 그냥 생략했다.

- 요청별 임시 객체

    ```java
    /**
     * 요청별 고유한 로그를 남기기위한 임시 객체
     */
    @Data
    @Component
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
    public class RequestInfo {
    
        private UUID requestUUID;
        private String requestUrl;
    }
    ```

- 로깅 필터와 `SecurityConfig`

    ```java
    /**
     * 요청별 UUID 설정하는 최상위(?) filter
     */
    public class LoggingFilter extends OncePerRequestFilter {
    
        private final ObjectProvider<RequestInfo> requestInfoProvider;
    
        public LoggingFilter(ObjectProvider<RequestInfo> requestInfoProvider) {
            this.requestInfoProvider = requestInfoProvider;
        }
    
        @Override
        protected void doFilterInternal(
                HttpServletRequest request, HttpServletResponse response,
                FilterChain filterChain
        ) throws ServletException, IOException {
    
            RequestInfo requestInfo = requestInfoProvider.getObject();
            requestInfo.setRequestUUID(UUID.randomUUID());
            requestInfo.setRequestUrl(request.getRequestURL().toString());
    
            filterChain.doFilter(request, response);
        }
    }
    
    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {
    
        private final ObjectProvider<RequestInfo> requestInfoProvider;
    
        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)  // filter 중 가장 빠른(?) 필터로 설정
        public LoggingFilter loggingFilter() {
            return new LoggingFilter(requestInfoProvider);
        }
    
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // 어차피 연습이니까 대충 build
            return http.build();
        }
    }
    ```

- `Service` 계층에서 간단한 사용 예시

    ```java
    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class InnerService {
    
        private final ObjectProvider<RequestInfo> requestInfoProvider;
    
        public void echo()  {
            this.log("echo from InnerService!");
    
            try {Thread.sleep(200L);}
            catch (Exception ignored) {}
    
            this.log("[Thread {}] echo end! ", Thread.currentThread().getName());
        }
    
        private RequestInfo getRequestInfo() {
            return requestInfoProvider.getObject();
        }
    
        private void log(String format, Object... args) {
            RequestInfo requestInfo = getRequestInfo();
            UUID requestId = requestInfo.getRequestUUID();
            String requestUrl = requestInfo.getRequestUrl();
    
            Object[] loggingArguments = new Object[args.length + 2];
            loggingArguments[0] = requestId;
            loggingArguments[1] = requestUrl;
            System.arraycopy(args, 0, loggingArguments, 2, args.length);
    
            log.info("[{}][{}] : " + format, loggingArguments);
        }
    }
    ```

- 간단한 `Controller` 와 로그 결과

    ```java
    @RestController
    @RequiredArgsConstructor
    public class Controller {
    
        private final InnerService innerService;
    
        @RequestMapping("/")
        public ResponseEntity<?> response()    {
            innerService.echo();
            return ResponseEntity.ok().body("Hello World");
        }
    }
    ```
    ```
    2024-12-23T22:39:06.131+09:00  INFO 36432 --- [app] [nio-8080-exec-5] sections.section10.InnerService          : [fa30a28b-ffe5-468d-95da-dde56457d59e][http://localhost:8080/] : echo from InnerService!
    2024-12-23T22:39:06.275+09:00  INFO 36432 --- [app] [nio-8080-exec-6] sections.section10.InnerService          : [56381bcb-3814-4176-b51a-c8591b2b646e][http://localhost:8080/] : echo from InnerService!
    2024-12-23T22:39:06.334+09:00  INFO 36432 --- [app] [nio-8080-exec-5] sections.section10.InnerService          : [fa30a28b-ffe5-468d-95da-dde56457d59e][http://localhost:8080/] : [Thread http-nio-8080-exec-5] echo end! 
    2024-12-23T22:39:06.401+09:00  INFO 36432 --- [app] [nio-8080-exec-7] sections.section10.InnerService          : [771cd22c-b4fe-4e33-80ea-8c9d7a7be301][http://localhost:8080/] : echo from InnerService!
    2024-12-23T22:39:06.481+09:00  INFO 36432 --- [app] [nio-8080-exec-6] sections.section10.InnerService          : [56381bcb-3814-4176-b51a-c8591b2b646e][http://localhost:8080/] : [Thread http-nio-8080-exec-6] echo end! 
    2024-12-23T22:39:06.549+09:00  INFO 36432 --- [app] [nio-8080-exec-8] sections.section10.InnerService          : [f37df35b-8842-4943-ab02-12ae763fbf56][http://localhost:8080/] : echo from InnerService!
    2024-12-23T22:39:06.606+09:00  INFO 36432 --- [app] [nio-8080-exec-7] sections.section10.InnerService          : [771cd22c-b4fe-4e33-80ea-8c9d7a7be301][http://localhost:8080/] : [Thread http-nio-8080-exec-7] echo end! 
    2024-12-23T22:39:06.755+09:00  INFO 36432 --- [app] [nio-8080-exec-8] sections.section10.InnerService          : [f37df35b-8842-4943-ab02-12ae763fbf56][http://localhost:8080/] : [Thread http-nio-8080-exec-8] echo end!
    ```

프로젝트 진행하며 `ObjectProvider` 를 알았으면 더 깔끔하게 만들 수 있었을 텐데 살짝 아쉽다.

---