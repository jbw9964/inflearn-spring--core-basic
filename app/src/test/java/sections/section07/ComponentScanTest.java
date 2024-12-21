package sections.section07;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.*;
import sections.section07.corner.*;

public class ComponentScanTest {

    @Test
    void testComponentScan() {
        var context = new AnnotationConfigApplicationContext(Config.class);

        AbstractSupClass bean = context.getBean(AbstractSupClass.class);

        assertThat(bean).isNotNull()
                .isNotInstanceOf(SubSubClass.class)
                .isNotInstanceOf(SubExcludedClass.class)
                .isInstanceOf(SubClass.class);
    }
}

@Configuration
@ComponentScan(
        includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AbstractSupClass.class),
        excludeFilters = {
                // 아니 aspectj 라이브러리 없으면 오류나네... 이게 맞나??
                @Filter(type = FilterType.ASPECTJ, pattern = "*..SubSubClass"),
                @Filter(type = FilterType.ANNOTATION, classes = {MyExclusion.class})
        }
)
class Config {

}
