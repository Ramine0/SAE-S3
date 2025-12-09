package Jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet(value = "/file-upload")
@MultipartConfig
public class FileUpload extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String uploadPath = request.getServletContext().getRealPath("/") + getServletContext().getInitParameter("upload.path");

        try
        {
            // Retrieve the file part from the request
            Part filePart = request.getPart("studentFile");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Save the file to the server
            InputStream inputStream = filePart.getInputStream();
            Files.copy(inputStream, Paths.get(uploadPath + File.separator + fileName), new  CopyOption[] { StandardCopyOption.REPLACE_EXISTING });


        } catch (IOException | ServletException e)
        {
            response.getWriter().println("File upload failed due to an error: " + e.getMessage());
        }


    }
}
