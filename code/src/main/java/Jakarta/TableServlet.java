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
import java.util.Random;

@WebServlet("/table")
@MultipartConfig

public class TableServlet extends HttpServlet
{
    public static CreatingIntermediate crea = null;
    private static Room salle = null;
    private static String secretCode;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (request.getParameter("action").equals("define"))
        {
            if (crea == null)
            {
                salle = new Room(request.getServletContext().getRealPath("/") + "/");
                crea = salle.getCrea();
            } else
                crea.resetData();

            crea.setMode(0);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int lon = 0;
        int lar = 0;

        if (request.getParameter("action").equals("define"))
        {
            lon = Integer.parseInt(request.getParameter("long"));
            lar = Integer.parseInt(request.getParameter("larg"));
            if (lon < 4)
            {
                lon = 4;
            } else if (lon > 20)
            {
                lon = 20;
            }
            if (lar < 4)
            {
                lar = 4;
            } else if (lar > 8)
            {
                lar = 8;
            }

            crea.createTables(lon, lar);
            crea.setDimensions(lon, lar);

            if (crea.getNumberTables() == 0)
            {
                out.print(0);
            } else if (crea.getNumberTables() == lon * lar && ((RectangularMap) crea.getMap()).getHeight() == lon && ((RectangularMap) crea.getMap()).getWidth() == lar)
            {
                out.print(lon + ";" + lar);
            } else
            {
                out.print(-1);
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
        } else if (request.getParameter("action").equals("generate"))
        {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuilder result = new StringBuilder();
            int length = 10;
            for (int i = 0; i < length; i++)
            {
                int index = random.nextInt(characters.length());
                result.append(characters.charAt(index));
            }
            secretCode = result.toString();
            out.print(secretCode);
        }else if (request.getParameter("action").equals("getDim")){
            out.print(((RectangularMap)salle.getPositioningIntermediate().getMap()).getHeight()+";"+((RectangularMap)salle.getPositioningIntermediate().getMap()).getWidth());
        }

        out.flush();
    }

    public static Room getSalle(String code)
    {
        if (code.equals(secretCode))
        {
            return salle;
        } else
        {
            return null;
        }
    }
}
