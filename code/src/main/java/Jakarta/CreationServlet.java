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

            if (tableNumber.isEmpty())
            {
                result += "null;";
            } else
                result += TableServlet.crea.findNumsForImp(studentId, Integer.parseInt(tableNumber)) + ";";

            out.print(result);
        } else if (request.getParameter("constraint").equals("deleteTable"))
        {
            int num = Integer.parseInt(request.getParameter("tableNumber"));

            out.print(TableServlet.crea.supprTable(num));

        } else if (request.getParameter("constraint").equals("separeEtu"))
        {
            String studentId = TableServlet.crea.findEtu(request.getParameter("studentId"));

            String studentInfo = TableServlet.crea.findStudentForGroup(request.getParameter("studentId"), Integer.parseInt(request.getParameter("numGrp")));

            if (studentInfo.length() == 1)
                out.print(studentId + ";" + studentInfo);
            else
                out.print(studentId + ";" + studentInfo.split(";")[1]);
        } else if (request.getParameter("constraint").equals("removeImposedPlace"))
        {
            TableServlet.crea.removeImp(Integer.parseInt(request.getParameter("id")));
        }

        out.flush();
    }

}