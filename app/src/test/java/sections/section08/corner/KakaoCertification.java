package sections.section08.corner;

import org.springframework.stereotype.*;

@Component
public class KakaoCertification implements Certification {

    @Override
    public void doCertification() {
        System.out.println("I'm kakao certification");
    }

    @Override
    public CertificationType getCertificationType() {
        return CertificationType.KAKAO;
    }
}
