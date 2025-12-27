package Jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.NeoMalokVector.SAE_S3.Room;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/set-table")
@MultipartConfig

public class TableServlet {
    private Room salle=null;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (salle==null){
            salle = new Room(request.getServletContext().getRealPath("/") + "/");
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        salle.getCrea().createTables(Integer.parseInt(request.getParameter("long")), Integer.parseInt(request.getParameter("larg")));
        salle.getPositioningIntermediate();
    }
}
