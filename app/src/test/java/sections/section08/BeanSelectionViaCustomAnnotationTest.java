package sections.section08;

import static org.assertj.core.api.Assertions.*;

import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.*;
import sections.section08.annotation.*;
import sections.section08.corner.*;

public class BeanSelectionViaCustomAnnotationTest {

    @Test
    void doTest() {
        var config = new AnnotationConfigApplicationContext(
                BeanSelectionConfig.class)
                .getBean(BeanSelectionConfig.class);

        assertThat(config.getBeanA())
                .isInstanceOf(SomeInterface.class)
                .isInstanceOf(BeanA.class)
                .isNotInstanceOf(BeanB.class);

        assertThat(config.getBeanB())
                .isInstanceOf(SomeInterface.class)
                .isInstanceOf(BeanB.class)
                .isNotInstanceOf(BeanA.class);
    }
}

@Getter
@Configuration
@ComponentScan(basePackageClasses = SomeInterface.class)
class BeanSelectionConfig {

    private final SomeInterface beanA;
    private final SomeInterface beanB;

    // 어노테이션 ElementType.PARAMETER 해야됨
    public BeanSelectionConfig(
            @DistinctA SomeInterface beanA,
            @DistinctB SomeInterface beanB
    ) {
        this.beanA = beanA;
        this.beanB = beanB;
    }
}