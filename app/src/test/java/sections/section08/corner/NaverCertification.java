package sections.section08.corner;

import org.springframework.stereotype.*;

@Component
public class NaverCertification implements Certification {

    @Override
    public void doCertification() {
        System.out.println("I'm Naver Certification");
    }

    @Override
    public CertificationType getCertificationType() {
        return CertificationType.NAVER;
    }
}
