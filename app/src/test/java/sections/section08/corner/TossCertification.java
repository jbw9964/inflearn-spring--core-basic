package sections.section08.corner;

import org.springframework.stereotype.*;

@Component
public class TossCertification implements Certification {

    @Override
    public void doCertification() {
        System.out.println("I'm Toss Certification");
    }

    @Override
    public CertificationType getCertificationType() {
        return CertificationType.TOSS;
    }
}
