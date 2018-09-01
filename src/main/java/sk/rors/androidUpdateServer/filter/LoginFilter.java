package sk.rors.androidUpdateServer.filter;

import sk.rors.androidUpdateServer.util.ErrorHandler;

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

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String url = req.getRequestURL().toString();
        if(url.startsWith("http://") && !isLocal(req)){
            resp.sendRedirect(url.replace("http://", "https://"));
            return;
        }

        if (noAuthNeeded(req, resp) || hasAccess(req)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            resp.sendError(401);
        }
    }

    private boolean hasAccess(HttpServletRequest req) {
        return authWithApiKey(req) || authWithBasicAuth(req);
    }

    private boolean noAuthNeeded(HttpServletRequest req, HttpServletResponse resp) {
        String uri = req.getRequestURI();

        List<String> allowed = Arrays.asList("css", "js");
        for (String tmp : allowed) {
            if (uri.contains(tmp)) {
                return true;
            }
        }

        if (uri.equals("/")) {
            Object loggedIn = req.getSession().getAttribute("loggedIn");
            boolean alreadyLoggedIn = loggedIn != null && (boolean) loggedIn;
            if (alreadyLoggedIn) {
                try {
                    resp.sendRedirect("/overview.html");
                    return true;
                } catch (IOException e) {
                    ErrorHandler.handle(e);
                }
            }
            return true;
        }

        return false;
    }

    private boolean authWithBasicAuth(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if (auth != null) {
            String userNameAndPasswd = new String(Base64.getDecoder().decode(auth.split(" ")[1]));
            String credentials = System.getenv().get("ADMIN_CREDENTIALS");

            boolean result = userNameAndPasswd.equals(credentials);
            if (result){
                req.getSession().setAttribute("loggedIn", true);
            }
            return result;
        }

        Object loggedIn = req.getSession().getAttribute("loggedIn");
        return loggedIn != null && (boolean) loggedIn;
    }

    private boolean authWithApiKey(HttpServletRequest req) {
        String header = req.getHeader("apiKey");
        String apiKey = System.getenv().get("apiKey");
        return (apiKey != null && apiKey.equals(header))|| isLocal(req);
    }

    private boolean isLocal(HttpServletRequest req){
        return (req.getRemoteAddr().equals("127.0.0.1") || req.getRemoteAddr().equals("0:0:0:0:0:0:0:1"));
    }
}
