package sections.section08.corner;

import org.springframework.stereotype.*;

@Component
public class PassCertification implements Certification {

    @Override
    public void doCertification() {
        System.out.println("I'm Pass Certification");
    }

    @Override
    public CertificationType getCertificationType() {
        return CertificationType.PASS;
    }
}
