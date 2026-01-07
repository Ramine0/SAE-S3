package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;


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
            salle = TableServlet.salle;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (request.getParameter("constraint").equals("imposePlace"))
        {
            String studentId = salle.getCrea().findEtu(request.getParameter("studentId"));

            String result = studentId + ";";
            result += salle.getCrea().studentInfo(studentId) + ";";

            String tableNumber = request.getParameter("tableNumber");

            if (tableNumber.isEmpty())
            {
                result += "null;";
            } else
                result += salle.getCrea().findNumsForImp(studentId, Integer.parseInt(tableNumber)) + ";";

            out.print(result);
        } else if (request.getParameter("constraint").equals("supprimeTable"))
        {
            String num = request.getParameter("table");
            if (request.getParameter("fieldToFill").equals("table"))
                out.print(num);
        } else if (request.getParameter("constraint").equals("separeEtu"))
        {
            String studentId = salle.getCrea().findEtu(request.getParameter("id"));

            String result = studentId + ";";
            result += salle.getCrea().studentInfo(studentId) + ";";

            result += salle.getCrea().findStudentForGroup(studentId, Integer.parseInt(request.getParameter("numGrp"))) + ";";

            out.print(result);
        } else if (request.getParameter("constraint").equals("removeImposedPlace"))
        {
            salle.getCrea().removeImp(Integer.parseInt(request.getParameter("id")));
        }

        out.flush();
    }

}