package jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.the_disabled.sae_s3.Room;
import placement.CreatingIntermediate;
import utilitaire.Utilitaire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@WebServlet("/creation")
public class CreationServlet extends HttpServlet {
    static String msg = "";
    private static HashMap<String, Room> rooms;

    private static void defineMapType(HttpServletRequest request, PrintWriter out, Room room, CreatingIntermediate crea) {
        crea.setMode(0);

        if (request.getParameter("planType").equals("defaultPlan")) {
            crea.changeMapMode('D', request.getServletContext().getRealPath("/") + "/");
            crea.loadDefaultMap(request.getServletContext().getRealPath("/") + "/");

            out.print(room.getPositioning().getTablesForVisualisation());
        } else {
            int width = Math.clamp(Integer.parseInt(request.getParameter("long")), 0, 20);
            int height = Math.clamp(Integer.parseInt(request.getParameter("larg")), 0, 8);

            crea.changeMapMode('R', request.getServletContext().getRealPath("/") + "/");

            crea.createTables(width, height);
            crea.setDimensions(width, height);

            out.print(room.getPositioning().getTablesForVisualisation());
        }
    }

    private static void returnTables(HttpServletRequest request, PrintWriter out, Room room, CreatingIntermediate crea) {
        int width = Math.clamp(Integer.parseInt(request.getParameter("long")), 0, 20);
        int height = Math.clamp(Integer.parseInt(request.getParameter("larg")), 0, 8);

        crea.createTables(width, height);
        crea.setDimensions(width, height);

        out.print(room.getPositioning().getTablesForVisualisation());
    }

    public static Room getRoom(String code) {
        return userExists(code) ? rooms.get(code) : null;
    }

    private static boolean userExists(String user) {
        return rooms != null && Utilitaire.in(user, rooms.keySet().toArray(new String[0]));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private static boolean loadSession(String oldId, String newId) {
        if ((!newId.equals(oldId)) && userExists(oldId)) {
            rooms.put(newId, rooms.get(oldId));
            rooms.remove(oldId);

            return true;
        }

        return newId.equals(oldId);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private static void createUser(String user, String path) {
        if (!userExists(user))
            try {
                Room newData = new Room(path);
                rooms.put(user, newData);
            } catch (Exception e) {
                msg = e.getMessage();
                getMessage();
            }
    }

    private static void getMessage() {
        System.out.println(msg);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("Referer") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access is not allowed.");

            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String user = request.getSession().getId();

        if (rooms == null)
            rooms = new HashMap<>();

        if (request.getParameter("load") != null) {
            loadSession(request, user, out);

            return;
        }

        if (request.getParameter("generate") != null) {
            createUser(user, request.getServletContext().getRealPath("/") + "/");

            out.print(user);
            out.print(msg);
        }

        Room room = rooms.get(user);
        CreatingIntermediate crea = room.getCreating();

        if (request.getParameter("mode") != null)
            if (request.getParameter("mode").equals("normal"))
                crea.setMode(0);

            else if (request.getParameter("mode").equals("group"))
                crea.setMode(1);

            else if (request.getParameter("mode").equals("sub-group"))
                crea.setMode(2);

        if (request.getParameter("action") != null)
            tableRequests(request, out, room);

        if (request.getParameter("constraint") != null)
            constraintRequests(request, out, room);

        out.flush();
    }

    private void loadSession(HttpServletRequest request, String user, PrintWriter out) {
        loadWithCode(!request.getParameter("load").isEmpty() ? loadSession(request.getParameter("load"), user) : userExists(user), out, user);
    }

    private void loadWithCode(boolean request, PrintWriter out, String user) {
        if (request) {
            out.print(getUserData(user));
            out.flush();
        } else {
            out.print("null");
            out.flush();
        }
    }

    private void tableRequests(HttpServletRequest request, PrintWriter out, Room room) {
        CreatingIntermediate crea = room.getCreating();

        switch (request.getParameter("action")) {
            case "visu" -> returnTables(request, out, room, crea);

            case "define" -> defineMapType(request, out, room, crea);

            case "getDim" -> out.print(crea.getDimensions());
        }
    }

    private void constraintRequests(HttpServletRequest request, PrintWriter out, Room room) {
        CreatingIntermediate crea = room.getCreating();

        switch (request.getParameter("constraint")) {
            case "imposePlace" -> {
                if (request.getParameter("oldNum") != null && request.getParameter("newNum") != null && request.getParameter("numEtu") != null && !request.getParameter("oldNum").isEmpty() && !request.getParameter("newNum").isEmpty() && !request.getParameter("numEtu").isEmpty())
                    out.print(crea.tableValidateButton(Integer.parseInt(request.getParameter("oldNum")), Integer.parseInt(request.getParameter("newNum")), request.getParameter("numEtu")));

                out.print("null");
            }

            case "removeImposedPlace" -> crea.removeConstraint("I", Integer.parseInt(request.getParameter("id")) - 1);

            case "deleteTable" -> {
                int num = Integer.parseInt(request.getParameter("tableNumber"));

                if (num < crea.minimumTableNumber())
                    num = crea.minimumTableNumber();
                else if (num > crea.maximumTableNumber())
                    num = crea.maximumTableNumber();

                out.print(crea.removeTable(num) + ";" + num);
            }

            case "removeDeletedTable" -> crea.undeleteTable(Integer.parseInt(request.getParameter("tableNumber")));

            case "separeEtu" -> {
                String studentId = crea.findStudent(request.getParameter("studentId"));
                String studentInfo = crea.separeStudentsPerGroup(studentId, Integer.parseInt(request.getParameter("numGrp")));

                out.print(studentInfo.length() == 1 ? studentId + ";" + studentInfo : studentId + ";" + studentInfo.split(";")[1]);
            }

            case "deleteSepareEtu" ->
                    crea.removeConstraint("G", Integer.parseInt(request.getParameter("constraintId").substring(2)));

            case "mode" -> {
            }
        }
    }

    public String getUserData(String user) {
        Room room = getRoom(user);
        String visualisationInfos = "null";

        if (room != null) {
            visualisationInfos = "";

            if (room.getPositioning() != null)
                visualisationInfos += "\n" + room.getPositioning().getTablesForVisualisation() + "<";

            visualisationInfos += room.getCreating().getSeparatedStudents();
            visualisationInfos += "<";
            visualisationInfos += room.getCreating().getStudentList() + "<";
        }

        return visualisationInfos;
    }
}