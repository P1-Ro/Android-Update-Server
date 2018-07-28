package sk.rors.androidUpdateServer.servlet;

import sk.rors.androidUpdateServer.util.FileFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;

@WebServlet( name = "TestServlet", value = "")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try{
            FileFactory.getInstance().saveApk(new File(this.getClass().getResource("/dummy.apk").getFile()));
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        File file = FileFactory.getInstance().getLatestApk("jp.nomunomu.dummy");
        if(file == null){
            throw new RuntimeException("File not saved");
        }


        resp.getWriter().append("Hello, World!");
    }

}
