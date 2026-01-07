package Jakarta;

import constraints.PerGroup;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import org.NeoMalokVector.SAE_S3.Table;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/export")
public class ExportServlet extends HttpServlet
{
    private Room salle=null;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (salle == null)
        {
            salle = TableServlet.salle;
        }
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"students.csv\""
        );

        PrintWriter out = response.getWriter();
        out.println("id;nom;table");
        for (int i=0; i<salle.getCrea().getNumberTables(); i++){
            if (salle.getCrea().getTable(i+1)!=null && salle.getCrea().StuFromTable(i+1)!=null){
                out.println(salle.getCrea().StuFromTable(i+1).getId()+";"+salle.getCrea().StuFromTable(i+1).getName()+";"+i+1);
            }
        }
        if (request.getParameter("format").equals("Excel")){
            out.println(";;;;contrainte;detail");
            for (int i=0; i<salle.getCrea().getNbConstr(); i++){
                if (salle.getCrea().getConstr(i+1)!=null){
                    String type=salle.getCrea().getConstr(i+1).getClass().getTypeName();
                    out.print(";;;;"+type);
                    if (type.equals("PerGroup")){
                        for (int j=0; j<((PerGroup) salle.getCrea().getConstr(i+1)).getNbStudent(); j++){
                            out.print(";"+salle.getCrea().getConstr(i+1));
                        }
                    }
                }
            }
        }
        out.flush();
    }
}
