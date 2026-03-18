package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/export")
public class ExportServlet extends HttpServlet
{

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (request.getHeader("Referer") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access is not allowed.");
            return;
        }

        Room room = CreationServlet.getRoom(request.getSession().getId());
        assert room != null;
        CreatingIntermediate crea = room.getCreating();
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"students.csv\""
        );

        PrintWriter out = response.getWriter();
        out.println("id;nom;table");

        for (int i = 0; i< crea.getNumberTables(); i++){
            if (crea.tableExist(i+1) && crea.stuFromTable(i+1)!=null){
                out.println(crea.stuFromTable(i+1).getId()+";"+ crea.stuFromTable(i+1).getName()+" "+crea.stuFromTable(i+1).getFirstName()+";"+(i+1));
            }
        }
        out.flush();
    }
}
