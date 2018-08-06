package sk.rors.androidUpdateServer.servlet;

import sk.rors.androidUpdateServer.model.Apk;
import sk.rors.androidUpdateServer.persistence.Database;
import sk.rors.androidUpdateServer.util.ApkUtil;
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

        File v16 = new File(this.getClass().getResource("/dummy_v16.apk").getFile());

        try {
            FileFactory.getInstance().saveApk(v16);
        } catch (Exception e1){
            new CertificateException("asd");
        }

        try{
            //FileFactory.getInstance().saveApk(v15);
            ApkUtil.getVersion(v16);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = FileFactory.getInstance().getLatestApk("com.p1ro.playonkodi");
        if(file == null){
            throw new RuntimeException("File not saved");
        }

        FileFactory.getInstance().sendNotifications("com.p1ro.playonkodi", "1");

        resp.getWriter().append("Hello, World!");
    }

}
