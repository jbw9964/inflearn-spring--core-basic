package sections.section10;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final InnerService innerService;

    @RequestMapping("/")
    public ResponseEntity<?> response() {
        innerService.echo();
        return ResponseEntity.ok().body("Hello World");
    }
}
