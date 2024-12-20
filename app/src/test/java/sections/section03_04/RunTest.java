package sections.section03_04;

import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.*;
import sections.*;

class RunTest {

    @Test
    void showBeansWithRole() {

        // 지금 알았는데 AnnotationConfigApplicationContext 생성하면서 bean 들이 등록되네?
        // 난 @SpringBootTest 붙여야지 될 줄 알았는데 아니었음.
        var context = new AnnotationConfigApplicationContext(App.class);

        var names = context.getBeanDefinitionNames();
        Map<Object, List<Object>> beanMap = new LinkedHashMap<>();

        for (String name : names) {
            BeanDefinition def = context.getBeanDefinition(name);

            /*
             * spring 은 bean 의 role 들을 다음처럼 분류한다.
             * - 0: ROLE_APPLICATION
             * - 1: ROLE_SUPPORT
             * - 2: ROLE_INFRASTRUCTURE
             */
            Object role = def.getRole();

            if (!beanMap.containsKey(role)) {
                beanMap.put(role, new LinkedList<>());
            }

            List<Object> beanList = beanMap.get(role);

            beanList.add(context.getBean(name));
        }

        for (Object role : beanMap.keySet().stream().sorted().toList()) {
            List<Object> beans = beanMap.get(role);
            printBeansOnRole(role, beans);
        }
    }

    private void printBeansOnRole(Object role, List<Object> beans) {
        System.out.println("----------------------------------------------------------------");
        System.out.printf("Role [%3s] : \n", role);
        beans.forEach(b -> System.out.println("\t- " + b.getClass().getSimpleName()));
        System.out.println();
    }
}