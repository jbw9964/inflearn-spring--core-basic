package sections.section08.annotation;

import java.lang.annotation.*;
import org.springframework.beans.factory.annotation.*;

@Target({
        ElementType.METHOD, ElementType.TYPE,
        ElementType.FIELD, ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("BBB bbb BBB")
public @interface DistinctB {

}
