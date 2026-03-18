package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import utilitaire.Utilitaire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@WebServlet("/creation")
public class CreationServlet extends HttpServlet {
    static String msg = ""; // c a 2000% du debug
    private static HashMap<String, Room> rooms;

    private static void defineMapType(HttpServletRequest request, PrintWriter out, Room room, CreatingIntermediate crea) {
        int length;
        int width;
        crea.setMode(0);

        if (request.getParameter("planType").equals("defaultPlan")) {
            crea.changeMapMode('D', request.getServletContext().getRealPath("/") + "/");
            crea.loadDefaultMap(request.getServletContext().getRealPath("/") + "/");

            out.print(room.getPositioning().getTablesForVisu());
        } else {
            length = Math.min(20, Math.max(0, Integer.parseInt(request.getParameter("long"))));
            width = Math.min(8, Math.max(0, Integer.parseInt(request.getParameter("larg"))));

            crea.changeMapMode('R', request.getServletContext().getRealPath("/") + "/");

            crea.createTables(length, width);
            crea.setDimensions(length, width);

            out.print(room.getPositioning().getTablesForVisu());
        }
    }

    private static void returnTables(HttpServletRequest request, PrintWriter out, Room room, CreatingIntermediate crea) {
        int width;
        int length;
        length = Math.min(20, Math.max(0, Integer.parseInt(request.getParameter("long"))));
        width = Math.min(8, Math.max(0, Integer.parseInt(request.getParameter("larg"))));

        crea.createTables(length, width);
        crea.setDimensions(length, width);

        out.print(room.getPositioning().getTablesForVisu());
    }

    public static Room getRoom(String code) {
        if (userExists(code))
            return rooms.get(code);
        else
            return null;
    }

    private static boolean userExists(String user) {
        if (rooms == null) {
            return false;
        }
        return Utilitaire.in(user, rooms.keySet().toArray(new String[0]));
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
        if (!userExists(user)) {
            try {
                Room newData = new Room(path);
                rooms.put(user, newData);

            } catch (Exception e) {
                msg = e.getMessage();
                getMessage();
            }
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

        if (rooms == null) {
            rooms = new HashMap<>();
        }


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


        if (request.getParameter("action") != null)
            tableRequests(request, out, room);

        if (request.getParameter("constraint") != null)
            constraintRequests(request, out, room);

        out.flush();
    }

    private void loadSession(HttpServletRequest request, String user, PrintWriter out) {
        if (!request.getParameter("load").isEmpty()) {

            loadWithCode(loadSession(request.getParameter("load"), user), out, user);

        } else loadWithCode(userExists(user), out, user);
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

        int length, width;


        switch (request.getParameter("action")) {

            case "visu" -> {
                returnTables(request, out, room, crea);
            }

            case "define" -> {
                defineMapType(request, out, room, crea);
            }

            // les dimentions
            case "getDim" -> out.print(crea.getDimentions());
        }
    }

    private void constraintRequests(HttpServletRequest request, PrintWriter out, Room room) {
        CreatingIntermediate crea = room.getCreating();


        switch (request.getParameter("constraint")) {

            case "imposePlace" -> {
                if (request.getParameter("oldNum") != null && request.getParameter("newNum") != null && request.getParameter("numEtu") != null && !request.getParameter("oldNum").isEmpty() && !request.getParameter("newNum").isEmpty() && !request.getParameter("numEtu").isEmpty()) {
                    out.print(crea.tableValidateButton(Integer.parseInt(request.getParameter("oldNum")), Integer.parseInt(request.getParameter("newNum")), request.getParameter("numEtu")));
                }
                out.print("null");
            }

            case "removeImposedPlace" -> crea.removeConstraint("I", Integer.parseInt(request.getParameter("id")) - 1);

            // supprime la table
            case "deleteTable" -> {
                int num = Integer.parseInt(request.getParameter("tableNumber"));

                if (num < crea.minTable())
                    num = crea.minTable();
                else if (num > crea.maxTable())
                    num = crea.maxTable();

                out.print(crea.supprTable(num) + ";" + num);
            }

            case "removeDeletedTable" -> {
                int num = Integer.parseInt(request.getParameter("tableNumber"));
                crea.unremoveTable(num);
            }

            case "separeEtu" -> {
                String studentId = crea.findStudent(request.getParameter("studentId"));

                String studentInfo = crea.findStudentForGroup(studentId, Integer.parseInt(request.getParameter("numGrp")));

                if (studentInfo.length() == 1)
                    out.print(studentId + ";" + studentInfo);
                else
                    out.print(studentId + ";" + studentInfo.split(";")[1]);
            }

            case "deleteSepareEtu" -> {
                int constraintId = Integer.parseInt(request.getParameter("constraintId").substring(2));
                crea.removeConstraint("G", constraintId);
            }

            case "mode" -> {
                if (request.getParameter("mode").equals("normal"))
                    crea.setMode(0);

                else if (request.getParameter("mode").equals("group"))
                    crea.setMode(1);

                else if (request.getParameter("mode").equals("sub-group"))
                    crea.setMode(2);
            }
        }
    }

    public String getUserData(String user) {
        Room room = getRoom(user);
        String visualisationInfos = "null";
        if (room != null) {
            visualisationInfos = "";
            if (room.getPositioning() != null) {
                visualisationInfos += "\n" + room.getPositioning().getTablesForVisu() + "<";
            }
            // les infos d'etudians mis a distance

            visualisationInfos += room.getCreating().getSeparated();
            visualisationInfos += "<";
            visualisationInfos += room.getCreating().getStudentList() + "<";
        }

        return visualisationInfos;
    }


}