package Jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.NeoMalokVector.SAE_S3.Room;
import placement.RectangularMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet("/table")
@MultipartConfig

public class TableServlet extends HttpServlet {
    private Room salle=null;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (salle==null){
            salle = new Room(request.getServletContext().getRealPath("/") + "/");
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (request.getParameter("action").equals("define")){
            int lon=Integer.parseInt(request.getParameter("long"));
            int lar=Integer.parseInt(request.getParameter("larg"));
            salle.getCrea().createTables(lon, lar);
            salle.getPositioningIntermediate().setDimensions(lon, lar);
            if(salle.getCrea().getNumberTables()==0){
                out.print("Table nulle");
            }
            else if (salle.getCrea().getNumberTables()==lon*lar && ((RectangularMap)salle.getPositioningIntermediate().getMap()).getHeight()==lon && ((RectangularMap)salle.getPositioningIntermediate().getMap()).getWidth()==lar){
                out.print("Table construite comme il faut");
            }else{
                out.print("Mauvaises valeurs");
            }
        }else if (request.getParameter("action").equals("present")){
            int num=Integer.parseInt(request.getParameter("num"));
            if (salle.getCrea().findTable(num)){
                out.print("valide");
            }else{
                out.print("table introuvable");
            }
        }
        out.flush();
    }
}
