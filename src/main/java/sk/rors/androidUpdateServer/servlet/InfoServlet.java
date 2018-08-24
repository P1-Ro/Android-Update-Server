package sk.rors.androidUpdateServer.servlet;

import sk.rors.androidUpdateServer.model.Apk;
import sk.rors.androidUpdateServer.model.OverviewInfo;
import sk.rors.androidUpdateServer.persistence.Database;
import sk.rors.androidUpdateServer.util.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet( name = "InfoServlet", urlPatterns = "/info")
public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            List<Apk> apps = Database.getInstance().getDao(Apk.class).queryForAll();
            OverviewInfo info = new OverviewInfo(apps);
            resp.getWriter().write(info.serialize());
        } catch (SQLException e){
            new ErrorResponse(e.getMessage(), 1).serialize(resp);
        }

    }
}
