package jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/file-upload")
@MultipartConfig
public class FileUpload extends HttpServlet {
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getHeader("Referer") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access is not allowed.");

            return;
        }

        String uploadPath = request.getServletContext().getRealPath("/") + "/" + getServletContext().getInitParameter("upload.path");

        Part filePart = request.getPart("studentFile");
        String fileName = "etudiants.csv";

        InputStream inputStream = filePart.getInputStream();
        Files.copy(inputStream, Paths.get(uploadPath + File.separator + fileName), StandardCopyOption.REPLACE_EXISTING);

    }
}
