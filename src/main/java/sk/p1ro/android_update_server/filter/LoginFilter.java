package sk.p1ro.android_update_server.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if (noAuthNeeded(req, resp) || hasAccess(req)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            resp.sendError(401);
        }
    }

    private boolean hasAccess(HttpServletRequest req) {
        return authWithApiKey(req);
    }

    private boolean noAuthNeeded(HttpServletRequest req, HttpServletResponse resp) {
        String uri = req.getRequestURI();

        List<String> allowed = Arrays.asList("css", "js", "download", "/");

        for (String tmp : allowed) {
            if (uri.contains(tmp)) {
                return true;
            }
        }

        return false;
    }

    private boolean authWithApiKey(HttpServletRequest req) {
        String header = req.getHeader("apiKey");
        String apiKey = System.getenv().get("apiKey");
        return (apiKey != null && apiKey.equals(header)); //|| isLocal(req);
    }
}
