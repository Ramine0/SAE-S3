package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import org.NeoMalokVector.SAE_S3.Student;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getStudentName")
public class CreationServlet extends HttpServlet
{

    private Room salle = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (salle == null)
        {
            salle = new Room(request.getServletContext().getRealPath("/") + "/");
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (request.getParameter("constraint").equals("imposePlace")){
            String id = salle.crea.findEtu(request.getParameter("id"));
            String num = request.getParameter("table");
            if (request.getParameter("fieldToFill").equals("id"))
                out.print(id);
            else if( request.getParameter("fieldToFill").equals("table")){
                out.print(num);
            }else if (request.getParameter("fieldToFill").equals("name"))
            {

                out.print(salle.crea.studentInfo(id));
            }
        }else if (request.getParameter("constraint").equals("supprimeTable")){
            String num = request.getParameter("table");
            if( request.getParameter("fieldToFill").equals("table"))
                out.print(num);
        }else if (request.getParameter("constraint").equals("separeEtu")){
            String id = salle.crea.findEtu(request.getParameter("id"));
            if (request.getParameter("fieldToFill").equals("id"))
                out.print(id);
            else if (request.getParameter("fieldToFill").equals("name"))
            {
                out.print(salle.crea.studentInfo(id));
            }
        }

        out.flush();
    }

}