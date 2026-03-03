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
    private static HashMap<String, Room> rooms;
    static String msg = ""; // c a 2000% du debug

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // il faut bien creer la hashmap a un momment ou a un autre
        if (rooms == null) {
            rooms = new HashMap<>();
        }

        // c'est la premiere requette qu'on va faire elle cherche des données existentes pour un user donné
        if (request.getParameter("load") != null) {
            out.print(getUserData(request.getSession().getId())); // on renvoie la session
            out.flush();
            return;
        }

        // un recup l'id de session psq on va grave s'en servir
        String user = request.getSession().getId();

        // on cherche cree les données si user pas deja existant
        if (request.getParameter("generate") != null) {
            if( !createUser(user, request.getServletContext().getRealPath("/") + "/")){
                return ;
            }
            out.print(request.getSession().getId());
        }

        // on charge la room du user
        Room salle = rooms.get(user);

        // les actions table
        if (request.getParameter("action") != null)
            tableRequests(request, out, salle);

        // les action constraint
        if (request.getParameter("constraint") != null)
            constraintRequests(request, out, salle);

        out.flush();
    }

    // les action de categorie table
    private void tableRequests(HttpServletRequest request, PrintWriter out, Room salle) {

        // on charge le creating
        CreatingIntermediate crea = salle.getCrea();

        int lon, lar;

        // les differentes action
        switch (request.getParameter("action")) {

            // renvoie les tables
            case "visu" -> {
                lon = Math.min(20, Math.max(0, Integer.parseInt(request.getParameter("long"))));
                lar = Math.min(8, Math.max(0, Integer.parseInt(request.getParameter("larg"))));

                crea.createTables(lon, lar);
                crea.setDimensions(lon, lar);

                out.print(salle.getPositioningIntermediate().getTablesForVisu());
            }

            // definition du type de plan
            case "define" -> {
                crea.setMode(0);

                if (request.getParameter("planType").equals("defaultPlan")) {
                    crea.changePlanMode('D', request.getServletContext().getRealPath("/") + "/");
                    crea.loadPlanDefault(request.getServletContext().getRealPath("/") + "/");

                    out.print(salle.getPositioningIntermediate().getTablesForVisu());
                } else {
                    lon = Math.min(20, Math.max(0, Integer.parseInt(request.getParameter("long"))));
                    lar = Math.min(8, Math.max(0, Integer.parseInt(request.getParameter("larg"))));

                    crea.changePlanMode('R', request.getServletContext().getRealPath("/") + "/");

                    crea.createTables(lon, lar);
                    crea.setDimensions(lon, lar);

                    out.print(salle.getPositioningIntermediate().getTablesForVisu());
                }
            }

            // les dimentions
            case "getDim" ->
                    out.print(crea.getDimentions());
        }
    }

    // les requetes sur les contraintes
    private void constraintRequests(HttpServletRequest request, PrintWriter out, Room salle) {
        CreatingIntermediate crea = salle.getCrea();


        switch (request.getParameter("constraint")) {

            // si on veux imposer une place
            case "imposePlace" -> {
                String studentId = crea.findEtu(request.getParameter("studentId"));

                String result = studentId + ";";
                result += crea.studentInfo(studentId) + ";";

                String tableNumber = request.getParameter("tableNumber");

                // on verifie un minimum les paramettres
                if (Integer.parseInt(tableNumber) <= 0 || Integer.parseInt(tableNumber) > crea.maxTable())
                    result += "3;";
                else if (tableNumber.isEmpty())
                    result += "null;";
                else
                    // on retourne le resultat du find
                    result += crea.findNumsForImp(studentId, Integer.parseInt(tableNumber)) + ";";

                out.print(result);
            }

            // on retire la place imposée si elle existe (l'intermediate fait le controle
            case "removeImposedPlace" -> crea.removeContrainst("I", Integer.parseInt(request.getParameter("id")) - 1);

            // supprime la table
            case "deleteTable" -> {
                int num = Integer.parseInt(request.getParameter("tableNumber"));

                // on fait un minimum de test
                if (num < crea.minTable())
                    num = crea.minTable();
                else if (num > crea.maxTable())
                    num = crea.maxTable();

                out.print(crea.supprTable(num) + ";" + num);
            }

            // rend un table re disponible
            case "removeDeletedTable" -> {
                int num = Integer.parseInt(request.getParameter("tableNumber"));
                crea.unremoveTable(num);
            }

            case "separeEtu" -> {
                String studentId = crea.findEtu(request.getParameter("studentId"));

                String studentInfo = crea.findStudentForGroup(studentId, Integer.parseInt(request.getParameter("numGrp")));

                if (studentInfo.length() == 1)
                    out.print(studentId + ";" + studentInfo);
                else
                    out.print(studentId + ";" + studentInfo.split(";")[1]);
            }

            case "deleteSepareEtu" -> {
                int constraintId = Integer.parseInt(request.getParameter("constraintId").substring(2));
                crea.removeContrainst("G", constraintId);
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

    public static Room getSalle(String code) {
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
        if (userExists(oldId)) {
            // il faudrait une transaction
            rooms.put(newId, rooms.get(oldId));
            rooms.remove(oldId);
            // qui se finirai la
            return true;
        }
        return false;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private static boolean createUser(String user, String path) {
        if (!userExists(user)) {
            try {
                rooms.put(user, new Room(path));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }


    public String getUserData(String user) {
        Room salle = getSalle(user);
        String result = "null";
        if (salle != null) {
            result = "" ;
            // les infos de la visu
            if (salle.getPositioningIntermediate() != null) {
                result += "\n" + salle.getPositioningIntermediate().getTablesForVisu() +"<";
            }
            // les infos d'etudians mis à distance

            result += salle.getCrea().getSeparated();
            result += "<";
            result += salle.getCrea().getStudentList() +"<";


        }

        return result;
    }


}