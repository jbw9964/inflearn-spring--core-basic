package sections.section10;

import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InnerService {

    private final ObjectProvider<RequestInfo> requestInfoProvider;

    public void echo() {
        this.log("echo from InnerService!");

        try {
            Thread.sleep(200L);
        } catch (Exception ignored) {
        }

        this.log("[Thread {}] echo end! ", Thread.currentThread().getName());
    }

    private RequestInfo getRequestInfo() {
        return requestInfoProvider.getObject();
    }

    private void log(String format, Object... args) {
        RequestInfo requestInfo = getRequestInfo();
        UUID requestId = requestInfo.getRequestUUID();
        String requestUrl = requestInfo.getRequestUrl();

        Object[] loggingArguments = new Object[args.length + 2];
        loggingArguments[0] = requestId;
        loggingArguments[1] = requestUrl;
        System.arraycopy(args, 0, loggingArguments, 2, args.length);

        log.info("[{}][{}] : " + format, loggingArguments);
    }
}
