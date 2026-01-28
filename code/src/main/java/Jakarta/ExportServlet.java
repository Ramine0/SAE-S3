package Jakarta;

import constraints.ImposedPlacement;
import constraints.PerClass;
import constraints.PerGroup;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import placement.CreatingIntermediate;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/export")
public class ExportServlet extends HttpServlet
{
    private CreatingIntermediate crea =null;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (crea == null)
        {
            crea = CreationServlet.crea;
        }
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"students.csv\""
        );

        PrintWriter out = response.getWriter();
        out.println("id;nom;table");
        for (int i = 0; i< crea.getNumberTables(); i++){
            if (crea.getTable(i+1)!=null && crea.StuFromTable(i+1)!=null){
                out.println(crea.StuFromTable(i+1).getId()+";"+ crea.StuFromTable(i+1).getName()+";"+i+1);
            }
        }
        if (request.getParameter("format").equals("Excel")){
            out.println(";;;;contrainte;detail");
            for (int i = 0; i< crea.getNbConstr(); i++){
                if (crea.getConstr(i+1)!=null){
                    String type= crea.getConstr(i+1).getClass().getTypeName();
                    out.print(";;;;"+type);
                    if (type.equals("PerGroup")){
                        for (int j = 0; j<((PerGroup) crea.getConstr(i+1)).getNbStudent(); j++){
                            out.print(";"+((PerGroup) crea.getConstr(i+1)).getNum()+";"+((PerGroup) crea.getConstr(i+1)).getStudent(j));
                        }
                    }else if (type.equals("PerClass")){
                        out.print(";"+((PerClass)crea.getConstr(i+1)).typePerClass());
                    }else if (type.equals("ImposedPlacement")){
                        out.print(";"+((ImposedPlacement)crea.getConstr(i+1)).getNumEtu()+";"+((ImposedPlacement)crea.getConstr(i+1)).getNumTable());
                    }
                }
                out.println();
            }
        }
        out.flush();
    }
}
