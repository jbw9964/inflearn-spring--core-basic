package sections.section08;

import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.bean.override.mockito.*;
import sections.section08.corner.*;

@SpringBootTest
public class StrategyPatternTest {

    // 잉? @MockBean, @SpyBean 은
    // spring boot test 3.4.0 부터 deprecated 되서
    // 3.6.0 에 없어진다 되어있네? 언제 또 업데이트 됬데 그랴

    @MockitoSpyBean
    KakaoCertification kakao;
    @MockitoSpyBean
    NaverCertification naver;
    @MockitoSpyBean
    PassCertification pass;
    @MockitoSpyBean
    TossCertification toss;

    @Autowired
    CertificationService certificationService;

    @Test
    void doTest() {
        Arrays.stream(CertificationType.values())
                .forEach(certificationService::doCertification);

        List.of(kakao, naver, pass, toss).forEach(
                c -> verify(c, times(1)).doCertification()
        );
    }
}