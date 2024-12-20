# Section 05 - 스프링 컨테이너와 스프링 빈

---

강의를 수강하다 `spring` 이 `bean` 들의 `role` 을 구분하는 걸 알게 되었다. [`[1]`](#reference)

```java
package org.springframework.beans.factory.config;

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

    /**
     * Role hint indicating that a {@code BeanDefinition} is a major part
     * of the application. Typically corresponds to a user-defined bean.
     */
    int ROLE_APPLICATION = 0;

    /**
     * Role hint indicating that a {@code BeanDefinition} is a supporting
     * part of some larger configuration, typically an outer
     * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
     * {@code SUPPORT} beans are considered important enough to be aware
     * of when looking more closely at a particular
     * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
     * but not when looking at the overall configuration of an application.
     * @noinspection JavadocReference
     */
    int ROLE_SUPPORT = 1;

    /**
     * Role hint indicating that a {@code BeanDefinition} is providing an
     * entirely background role and has no relevance to the end-user. This hint is
     * used when registering beans that are completely part of the internal workings
     * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
     * @noinspection JavadocReference
     */
    int ROLE_INFRASTRUCTURE = 2;

    /* ... */
}
```

간략하게 검색 [`[2]`](#reference) 해서 알아보니 다음처럼 각 `ROLE` 이 구분된다 한다.

- `ROLE_APPLICATION_` :

  해당 `Bean` 이 어플리케이션의 주요한 역할을 담당함을 나타냄. 대게 사용자가 직접 만든 `Bean` 들이 이에 해당됨.
- `ROLE_SUPPORT` :

  해당 `Bean` 이 좀 더 복합적인 설정에 사용됨을 나타냄. 특히 `ComponentDefinition` 에 벗어나는 `Bean` 들이 이에 해당된다고 함.
- `ROLE_INFRASTRUCTURE` :

  해당 `Bean` 이 사용자가 건드릴 일이 없는 `Bean` 임을 나타냄. 이 `ROLE` 은 `ComponentDefinition Beans` 의 내부 동작을 수행하기 위해
  사용되었다고 함.

> ### `ComponentDefinition?`
>
> 문서 [`[3]`](#reference) 에 따르면 `BeanDefinitions`, `BeanReferences` 의 논리적 `view` 를 나타내기 위한 interface
> 라고 한다.
>
> 문서에는 _"`XML tag` 의 도입으로 하나의 `configuration` 에 여러 `BeanDefinitions` 이 가능해 더 편해졌습니다."_ 라고 되어있다.
>
> 또한 해당 interface 가 `Spring 2.0` 부터 지원된걸로 보아 `"옛날 Spring 이 복합적인 beanDefinition 을 관리하기 위한 무언가"` 라고
> 생각된다.
>
> ```java
> package org.springframework.beans.factory.parsing;
>
> public interface ComponentDefinition extends BeanMetadataElement {
> 
>     String getName();
>     String getDescription();
> 
>     BeanDefinition[] getBeanDefinitions();
>     BeanDefinition[] getInnerBeanDefinitions();
>     BeanReference[] getBeanReferences();
> }
> ```
>
> 무엇보다 직접 보면 `getName`, `getBeanReferences` 등 뭔가 `복합적인 Bean 관리를 위한` 속성들이 있어 내 말이 맞는 것 같다.


그렇게 사용할 일은 없지만 `@Role` 어노테이션을 이용해 지정할 수 있다고 한다.
`(@Role(BeanDefinition.ROLE_APPLICATION))`

---

# Reference

- [`[1] : Annotation Interface Role - Spring javadoc`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Role.html)

- [`[2] : [토비의 스프링 - Vol.2] 1장 - 1.5 스프링 3.1의 IoC 컨테이너와 DI - 기억나 노트's Tistory`](https://milenote.tistory.com/163)

- [`[3] : Interface ComponentDefinition - Spring javadoc`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/parsing/ComponentDefinition.html)

---