package Jakarta;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.RectangularMap;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/table")
@MultipartConfig

public class TableServlet extends HttpServlet
{
    public static CreatingIntermediate crea = null;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (crea == null)
        {
            Room salle = new Room(request.getServletContext().getRealPath("/") + "/");
            crea = salle.getCrea();
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int lon = 0;
        int lar = 0;

        if (request.getParameter("action").equals("define"))
        {
            lon = Integer.parseInt(request.getParameter("long"));
            lar = Integer.parseInt(request.getParameter("larg"));

            crea.createTables(lon, lar);
            crea.setDimensions(lon, lar);

            if (crea.getNumberTables() == 0)
            {
                out.print("Table nulle");
            } else if (crea.getNumberTables() == lon * lar && ((RectangularMap) crea.getMap()).getHeight() == lon && ((RectangularMap) crea.getMap()).getWidth() == lar)
            {
                out.print("Table construite comme il faut");
            } else
            {
                out.print("Mauvaises valeurs");
            }
        } else if (request.getParameter("action").equals("present"))
        {
            int num = Integer.parseInt(request.getParameter("num"));
            if (crea.findTable(num))
            {
                out.print("valide");
            } else
            {
                out.print("table introuvable");
            }
        } else if (request.getParameter("action").equals("delete"))
        {
            crea.createTables(lon, lar);
            crea.resetData();
        }

        out.flush();
    }
}
