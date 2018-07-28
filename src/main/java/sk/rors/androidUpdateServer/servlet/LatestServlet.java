package sk.rors.androidUpdateServer.servlet;

import sk.rors.androidUpdateServer.model.Latest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LatestServlet", urlPatterns = "/latest")
public class LatestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Latest latest = new Latest("1.0", 1);
        String result = latest.serialize();

        resp.getWriter().append(result);
    }
}
