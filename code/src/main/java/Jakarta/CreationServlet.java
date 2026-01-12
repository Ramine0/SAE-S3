package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getStudentName")
public class CreationServlet extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (request.getParameter("constraint").equals("imposePlace"))
        {
            String studentId = TableServlet.crea.findEtu(request.getParameter("studentId"));

            String result = studentId + ";";
            result += TableServlet.crea.studentInfo(studentId) + ";";

            String tableNumber = request.getParameter("tableNumber");

            if (Integer.parseInt(tableNumber) < 0 || Integer.parseInt(tableNumber) > TableServlet.crea.maxTable())
                result += "3;";
            else if (tableNumber.isEmpty())
            {
                result += "null;";
            } else
            {
                result += TableServlet.crea.findNumsForImp(studentId, Integer.parseInt(tableNumber)) + ";";
            }

            out.print(result);
        } else if (request.getParameter("constraint").equals("removeImposedPlace"))
        {
            TableServlet.crea.removeContrainst("I", Integer.parseInt(request.getParameter("id")));
        } else if (request.getParameter("constraint").equals("deleteTable"))
        {
            int num = Integer.parseInt(request.getParameter("tableNumber"));
            if (num < 0 ||num > TableServlet.crea.maxTable())
             out.print("-5");
            else
            out.print(TableServlet.crea.supprTable(num));

        } else if (request.getParameter("constraint").equals("removeDeletedTable"))
        {
            int num = Integer.parseInt(request.getParameter("tableNumber"));
            TableServlet.crea.unremoveTable(num);
        } else if (request.getParameter("constraint").equals("separeEtu"))
        {
            String studentId = TableServlet.crea.findEtu(request.getParameter("studentId"));

            String studentInfo = TableServlet.crea.findStudentForGroup(studentId, Integer.parseInt(request.getParameter("numGrp")));

            if (studentInfo.length() == 1)
                out.print(studentId + ";" + studentInfo);
            else
                out.print(studentId + ";" + studentInfo.split(";")[1]);
        } else if (request.getParameter("constraint").equals("deleteSepareEtu"))
        {
            int constraintId = Integer.parseInt(request.getParameter("constraintId").substring(2));
            TableServlet.crea.removeContrainst("G", constraintId);
        } else if (request.getParameter("constraint").equals("mode"))
        {
            if (TableServlet.crea != null)
            {
                if (request.getParameter("mode").equals("normal"))
                {
                    TableServlet.crea.setMode(0);
                } else if (request.getParameter("mode").equals("group"))
                {
                    TableServlet.crea.setMode(1);
                } else if (request.getParameter("mode").equals("sub-group"))
                {
                    TableServlet.crea.setMode(2);
                }
            } else
            {
                out.println("erreur");
            }
        } else if (request.getParameter("constraint").equals("reset"))
        {
        }

        out.flush();
    }

}