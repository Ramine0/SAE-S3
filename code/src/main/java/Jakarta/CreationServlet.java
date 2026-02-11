package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.RectangularMap;
import utilitaire.Utilitaire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/creation")
public class CreationServlet extends HttpServlet
{
    private static HashMap<String, Room> rooms;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (rooms == null)
        {
            rooms = new HashMap<>();
        }
        String user = request.getSession().getId();
        if (!userExists(user))
        {
            rooms.put(user, new Room(request.getServletContext().getRealPath("/") + "/"));
        }
        Room salle = rooms.get(user);


        if (request.getParameter("action") != null)
            tableRequests(request, out, salle);

        if (request.getParameter("constraint") != null)
            constraintRequests(request, out, salle);

        out.flush();
    }

    private static void tableRequests(HttpServletRequest request, PrintWriter out, Room salle)
    {
        salle.creatingMode();
        CreatingIntermediate crea = salle.getCrea();

        int lon, lar;

        switch (request.getParameter("action"))
        {
            case "isGenerated" -> out.print(salle.getPositioningIntermediate().isGenerated());

            case "visu" -> out.print(salle.getPositioningIntermediate().getTablesForVisu());

            case "define" ->
            {
                crea.setMode(0);
                crea.resetData();

                if (request.getParameter("planType").equals("defaultPlan"))
                {
                    crea.changePlanMode('D', request.getServletContext().getRealPath("/") + "/");
                    crea.loadPlanDefault(request.getServletContext().getRealPath("/") + "/");

                    out.print(salle.getPositioningIntermediate().getTablesForVisu());
                } else
                {
                    lon = Math.min(20, Math.max(0, Integer.parseInt(request.getParameter("long"))));
                    lar = Math.min(8, Math.max(0, Integer.parseInt(request.getParameter("larg"))));

                    crea.changePlanMode('R', request.getServletContext().getRealPath("/") + "/");

                    crea.createTables(lon, lar);
                    crea.setDimensions(lon, lar);

                    out.print(salle.getPositioningIntermediate().getTablesForVisu());
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

            case "generate" -> out.print(request.getSession().getId());


            case "getDim" ->
                    out.print(((RectangularMap) salle.getCrea().getMap()).getHeight() + ";" + ((RectangularMap) salle.getCrea().getMap()).getWidth());
        }
    }

    private void constraintRequests(HttpServletRequest request, PrintWriter out, Room salle)
    {
        CreatingIntermediate crea = salle.getCrea();
        if (crea == null)
        {
            out.println("heheheheh");
            return;
        }
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
                if (request.getParameter("mode").equals("normal"))
                    crea.setMode(0);

                else if (request.getParameter("mode").equals("group"))
                    crea.setMode(1);

                else if (request.getParameter("mode").equals("sub-group"))
                    crea.setMode(2);
            }
        }
    }

    public static Room getSalle(String code)
    {
        if (userExists(code))
            return rooms.get(code);
        else
            return null;
    }

    private static boolean userExists(String user)
    {
        if (rooms == null)
        {
            return false;
        }
        return Utilitaire.in(user, rooms.keySet().toArray(new String[0]));
    }

    private static boolean loadSession(String oldId, String newId)
    {
        if (userExists(oldId))
        {
            // il faudrait une transaction
            rooms.put(newId, rooms.get(oldId));
            rooms.remove(oldId);
            // qui se finirai la
            return true;
        }
        return false;
    }


}