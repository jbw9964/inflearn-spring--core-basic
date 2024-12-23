package sections.section10;

import java.util.*;
import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.*;

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
