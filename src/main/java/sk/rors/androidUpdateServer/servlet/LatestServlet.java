package sk.rors.androidUpdateServer.servlet;

import sk.rors.androidUpdateServer.model.Apk;
import sk.rors.androidUpdateServer.persistence.Database;
import sk.rors.androidUpdateServer.util.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LatestServlet", urlPatterns = {"/latest/*", "/download/*"})
public class LatestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String[] uriParts = req.getRequestURI().split("/");
        String packageName = uriParts[uriParts.length-1];
        try {
            Apk apk = Database.getInstance().getDao(Apk.class).queryForId(packageName);
            if (apk != null) {

                if (req.getRequestURI().contains("/download")) {
                    resp.setHeader("content-disposition", "attachment; filename=\"" + packageName + ".apk\"");
                    resp.getOutputStream().write(apk.getApk());
                } else {
                    resp.getWriter().append(apk.serialize());
                }
            } else {
                resp.getWriter().append(new ErrorResponse("No apk with such packageName", 404).serialize());
                resp.setStatus(404);
            }

        } catch (Exception e) {
            resp.getWriter().append(new ErrorResponse(e.toString(), 500).serialize());
            resp.setStatus(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
