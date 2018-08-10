package sk.rors.androidUpdateServer.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (!hasAccess((HttpServletRequest)servletRequest)){
            ((HttpServletResponse) servletResponse).sendError(401);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean hasAccess(HttpServletRequest req) {
        String header = req.getHeader("apiKey");
        String apiKey = System.getenv().get("apiKey");
        return (apiKey != null && apiKey.equals(header)) || (req.getRemoteAddr().equals("127.0.0.1") || req.getRemoteAddr().equals("0:0:0:0:0:0:0:1"));
    }
}
