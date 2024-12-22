package sections.section08.corner;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.springframework.stereotype.*;

@Service
public class CertificationService {

    private final Map<CertificationType, Certification> certifications;

    public CertificationService(List<Certification> certifications) {
        this.certifications = certifications.stream()
                .collect(Collectors.toMap(
                        Certification::getCertificationType, Function.identity()
                ));
    }

    public void doCertification(CertificationType type) {
        certifications.get(type).doCertification();
    }
}
