package sk.rors.androidUpdateServer.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (!hasAccess((HttpServletRequest) servletRequest)) {
            ((HttpServletResponse) servletResponse).sendError(401);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean hasAccess(HttpServletRequest req) {
        return noAuthNeeded(req) || authWithApiKey(req) || authWithBasicAuth(req);
    }

    private boolean noAuthNeeded(HttpServletRequest req) {
        String uri = req.getRequestURI();
        List<String> allowed = Arrays.asList("/", "css", "js");
        for (String tmp : allowed) {
            if (uri.contains(tmp)) {
                return true;
            }
        }
        return false;
    }

    private boolean authWithBasicAuth(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if (auth != null) {
            String userNameAndPasswd = new String(Base64.getDecoder().decode(auth.split(" ")[1]));
            String credentials = System.getenv().get("ADMIN_CREDENTIALS");
            req.getSession().setAttribute("loggedIn", true);

            return userNameAndPasswd.equals(credentials);
        }

        Object loggedIn = req.getSession().getAttribute("loggedIn");
        return loggedIn != null && (boolean) loggedIn;
    }

    private boolean authWithApiKey(HttpServletRequest req) {
        String header = req.getHeader("apiKey");
        String apiKey = System.getenv().get("apiKey");
        return (apiKey != null && apiKey.equals(header));
//                || (req.getRemoteAddr().equals("127.0.0.1") || req.getRemoteAddr().equals("0:0:0:0:0:0:0:1"));
    }
}
