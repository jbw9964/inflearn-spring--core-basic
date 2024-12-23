package sections.section10;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import org.springframework.beans.factory.*;
import org.springframework.web.filter.*;


public class LoggingFilter extends OncePerRequestFilter {

    private final ObjectProvider<RequestInfo> requestInfoProvider;

    public LoggingFilter(ObjectProvider<RequestInfo> requestInfoProvider) {
        this.requestInfoProvider = requestInfoProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        RequestInfo requestInfo = requestInfoProvider.getObject();
        requestInfo.setRequestUUID(UUID.randomUUID());
        requestInfo.setRequestUrl(request.getRequestURL().toString());

        filterChain.doFilter(request, response);
    }
}
