package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Student;
import placement.Data;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@WebServlet("/getStudentName")
public class CreationServlet extends HttpServlet
{

    private Data data = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (data == null)
        {
            data = new Data();
            data.chargerFichier(request.getServletContext().getRealPath("/") + getServletContext().getInitParameter("upload.path"));
            data.init();
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        Student student = data.getStudentFromId(id);

        if (Objects.equals(request.getParameter("forCompletingId"), "0"))
            out.print(student.getName() + " " + student.getFirstName());
        else
            out.print("loooooollll");

        out.flush();
    }

}