package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;


import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getStudentName")
public class CreationServlet extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (TableServlet.crea == null)
        {
            Room salle = new Room(request.getServletContext().getRealPath("/") + "/");
            TableServlet.crea = salle.getCrea();
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (request.getParameter("constraint").equals("imposePlace"))
        {
            String studentId = TableServlet.crea.findEtu(request.getParameter("studentId"));

            String result = studentId + ";";
            result += TableServlet.crea.studentInfo(studentId) + ";";

            String tableNumber = request.getParameter("tableNumber");

            if (tableNumber.isEmpty())
            {
                result += "null;";
            } else
                result += TableServlet.crea.findNumsForImp(studentId, Integer.parseInt(tableNumber)) + ";";

            out.print(result);
        } else if (request.getParameter("constraint").equals("supprimeTable"))
        {
            String num = request.getParameter("table");
            if (request.getParameter("fieldToFill").equals("table"))
                out.print(num);
        } else if (request.getParameter("constraint").equals("separeEtu"))
        {
            String result = TableServlet.crea.findStudentForGroup(request.getParameter("id"), Integer.parseInt(request.getParameter("numGrp")))  ;
            out.print(result);
        } else if (request.getParameter("constraint").equals("removeImposedPlace"))
        {
            TableServlet.crea.removeImp(Integer.parseInt(request.getParameter("id")));
        }

        out.flush();
    }

}