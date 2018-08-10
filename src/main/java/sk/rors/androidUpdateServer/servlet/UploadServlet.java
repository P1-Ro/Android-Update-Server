package sk.rors.androidUpdateServer.servlet;

import org.apache.commons.io.FileUtils;
import sk.rors.androidUpdateServer.util.ErrorResponse;
import sk.rors.androidUpdateServer.util.Response;
import sk.rors.androidUpdateServer.util.FileFactory;
import sk.rors.androidUpdateServer.util.exception.VersionException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.UUID;

@MultipartConfig
@WebServlet( name = "UploadServlet", urlPatterns = "/upload")
public class UploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Part file = req.getPart("file");
            File result = new File(UUID.randomUUID().toString());
            FileUtils.copyInputStreamToFile(file.getInputStream(), result);
            FileFactory.getInstance().saveApk(result);
            new Response("Success", 200).serialize(resp);
        } catch (CertificateException e) {
            new ErrorResponse(e.getMessage(), -1).serialize(resp);
        } catch (VersionException e) {
            new ErrorResponse(e.getMessage(), -2).serialize(resp);
        }
    }
}
