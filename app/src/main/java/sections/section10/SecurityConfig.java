package sections.section10;

import lombok.*;
import org.springframework.beans.factory.*;
import org.springframework.context.annotation.*;
import org.springframework.core.*;
import org.springframework.core.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectProvider<RequestInfo> requestInfoProvider;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public LoggingFilter loggingFilter() {
        return new LoggingFilter(requestInfoProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 어차피 연습이니까 대충 build
        return http.build();
    }
}
