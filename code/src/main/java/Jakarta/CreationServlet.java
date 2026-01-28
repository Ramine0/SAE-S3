package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.RectangularMap;
import utilitaire.Utilitaire;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/creation")
public class CreationServlet extends HttpServlet
{
    private static HashMap<String, Room> rooms;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (rooms == null) {
            rooms = new HashMap<>();
        }
        String user = request.getSession().getId();
        if (!userExists(user)){
            rooms.put(user, new Room(request.getServletContext().getRealPath("/") + "/"));
        }
        Room salle= rooms.get(user);


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (request.getParameter("action") != null)
            tableRequests(request, out,salle);

        if (request.getParameter("constraint") != null)
            constraintRequests(request, out,salle);

        out.flush();
    }

    private void constraintRequests(HttpServletRequest request, PrintWriter out,Room salle)
    {

        CreatingIntermediate crea = salle.getCrea() ;
        if (crea == null) {out.println("heheheheh"); return ;}
        switch (request.getParameter("constraint"))
        {
            case "imposePlace" ->
            {
                String studentId = crea.findEtu(request.getParameter("studentId"));

                String result = studentId + ";";
                result += crea.studentInfo(studentId) + ";";

                String tableNumber = request.getParameter("tableNumber");

                if (Integer.parseInt(tableNumber) <= 0 || Integer.parseInt(tableNumber) > crea.maxTable())
                    result += "3;";
                else if (tableNumber.isEmpty())
                    result += "null;";
                else
                    result += crea.findNumsForImp(studentId, Integer.parseInt(tableNumber)) + ";";

                out.print(result);
            }

            case "removeImposedPlace" -> crea.removeContrainst("I", Integer.parseInt(request.getParameter("id")) - 1);

            case "deleteTable" ->
            {
                int num = Integer.parseInt(request.getParameter("tableNumber"));

                if (num < crea.minTable())
                    num = crea.minTable();
                else if (num > crea.maxTable())
                    num = crea.maxTable();

                out.print(crea.supprTable(num) + ";" + num);
            }

            case "removeDeletedTable" ->
            {
                int num = Integer.parseInt(request.getParameter("tableNumber"));
                crea.unremoveTable(num);
            }

            case "separeEtu" ->
            {
                String studentId = crea.findEtu(request.getParameter("studentId"));

                String studentInfo = crea.findStudentForGroup(studentId, Integer.parseInt(request.getParameter("numGrp")));

                if (studentInfo.length() == 1)
                    out.print(studentId + ";" + studentInfo);
                else
                    out.print(studentId + ";" + studentInfo.split(";")[1]);
            }

            case "deleteSepareEtu" ->
            {
                int constraintId = Integer.parseInt(request.getParameter("constraintId").substring(2));
                crea.removeContrainst("G", constraintId);
            }

            case "mode" ->
            {
                if (crea != null)
                {
                    if (request.getParameter("mode").equals("normal"))
                        crea.setMode(0);

                    else if (request.getParameter("mode").equals("group"))
                        crea.setMode(1);

                    else if (request.getParameter("mode").equals("sub-group"))
                        crea.setMode(2);
                } else
                    out.println("erreur");
            }
        }
    }

    private static void tableRequests(HttpServletRequest request, PrintWriter out, Room salle) throws FileNotFoundException
    {
        CreatingIntermediate crea = salle.getCrea() ;
        int lon, lar;

        switch (request.getParameter("action"))
        {
            case "define" ->
            {

                crea.setMode(0);

                if (request.getParameter("planType").equals("defaultPlan"))
                {
                    out.print(crea.loadPlanDefault(request.getServletContext().getRealPath("/") + "/"));
                } else
                {
                    lon = Integer.parseInt(request.getParameter("long"));
                    lar = Integer.parseInt(request.getParameter("larg"));

                    if (lon < 4)
                        lon = 4;
                    else if (lon > 20)
                        lon = 20;

                    if (lar < 4)
                        lar = 4;
                    else if (lar > 8)
                        lar = 8;

                    crea.createTables(lon, lar);
                    crea.setDimensions(lon, lar);

                    if (crea.getNumberTables() == 0)
                        out.print(0);
                    else if (crea.getNumberTables() == lon * lar && ((RectangularMap) crea.getMap()).getHeight() == lon && ((RectangularMap) crea.getMap()).getWidth() == lar)
                        out.print(lon + ";" + lar);
                    else
                        out.print(-1);
                }
            }

            case "present" ->
            {
                int num = Integer.parseInt(request.getParameter("num"));

                if (crea.findTable(num))
                    out.print("valide");
                else
                    out.print("table introuvable");
            }

            case "generate" ->
                out.print(request.getSession().getId());


            case "getDim" ->
                    out.print(((RectangularMap) salle.getCrea().getMap()).getHeight() + ";" + ((RectangularMap) salle.getCrea().getMap()).getWidth());
        }
    }

    public static Room getSalle(String code)
    {
        if (userExists(code))
            return rooms.get(code);
        else
            return null;
    }

    private static boolean userExists(String user) {
        return Utilitaire.in(user, rooms.keySet().toArray(new String[0]));
    }


}