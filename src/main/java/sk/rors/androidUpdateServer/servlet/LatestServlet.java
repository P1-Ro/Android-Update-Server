package sk.rors.androidUpdateServer.servlet;

import sk.rors.androidUpdateServer.model.Apk;
import sk.rors.androidUpdateServer.persistence.Database;
import sk.rors.androidUpdateServer.util.ErrorResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LatestServlet", urlPatterns = "/latest")
public class LatestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String packageName = req.getParameter("packagename");
        try{
            Apk apk = (Apk) Database.getInstance().getDao(Apk.class).queryForId(packageName);
            if(apk != null){
                resp.getWriter().append(apk.serialize());
            } else {
                resp.getWriter().append(new ErrorResponse("No apk with such packageName", 404).serialize());
            }

        }catch (Exception e){
            // TODO better handling of exceptions
            resp.getWriter().append(e.toString());
        }

    }
}
