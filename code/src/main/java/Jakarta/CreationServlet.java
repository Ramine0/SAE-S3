package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.NeoMalokVector.SAE_S3.Room;
import placement.CreatingIntermediate;
import placement.RectangularMap;
import utilitaire.Utilitaire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


@WebServlet("/creation")
public class CreationServlet extends HttpServlet {
    private static HashMap<String, Room> rooms;
    static String msg = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String user = request.getSession().getId();

        if (rooms == null) {
            rooms = new HashMap<>();
        }

        // quand on charge la page ou un id de session
        if (request.getParameter("load") != null) {

            // si c pas un chargement d'un nouveau user (un sans code)
            if (!request.getParameter("load").isEmpty()){

                // si jamais je suis en chargement de page
                if(loadSession(request.getParameter("load"),user)){
                    out.print(getUserData(user)) ;
                    out.flush();
                    return ;
                }else {
                    out.print("null") ;
                    out.flush();
                    return ;
                }

            }else if (userExists(user)) {
                out.print(getUserData(user)) ;
                out.flush();
                return ;
            }else {
                out.print("null") ;
                out.flush();
                return ;
            }

        }



        if (request.getParameter("generate") != null) {
            createUser(user, request.getServletContext().getRealPath("/") + "/") ;
            out.print(user);
            out.print(msg) ;
        }



        Room salle = rooms.get(user);


        if (request.getParameter("action") != null)
            tableRequests(request, out, salle);

        if (request.getParameter("constraint") != null)
            constraintRequests(request, out, salle);

        out.flush();
    }

    private void tableRequests(HttpServletRequest request, PrintWriter out, Room salle) {

        CreatingIntermediate crea = salle.getCrea();

        int lon, lar;

        switch (request.getParameter("action")) {
            case "isGenerated" -> out.print(salle.isGenerated());

            case "visu" -> {
                lon = Math.min(20, Math.max(0, Integer.parseInt(request.getParameter("long"))));
                lar = Math.min(8, Math.max(0, Integer.parseInt(request.getParameter("larg"))));

                crea.createTables(lon, lar);
                crea.setDimensions(lon, lar);

                out.print(salle.getPositioningIntermediate().getTablesForVisu());
            }

            case "define" -> {
                crea.setMode(0);

                if (request.getParameter("planType").equals("defaultPlan")) {
                    crea.changePlanMode('D', request.getServletContext().getRealPath("/") + "/");
                    crea.loadPlanDefault(request.getServletContext().getRealPath("/") + "/");

                    out.print(salle.getPositioningIntermediate().getTablesForVisu()+"wtf");

                } else {
                    lon = Math.min(20, Math.max(0, Integer.parseInt(request.getParameter("long"))));
                    lar = Math.min(8, Math.max(0, Integer.parseInt(request.getParameter("larg"))));

                    crea.changePlanMode('R', request.getServletContext().getRealPath("/") + "/");

                    crea.createTables(lon, lar);
                    crea.setDimensions(lon, lar);

                    out.print(salle.getPositioningIntermediate().getTablesForVisu());
                }
            }

            case "present" -> {
                int num = Integer.parseInt(request.getParameter("num"));

                if (crea.findTable(num))
                    out.print("valide");
                else
                    out.print("table introuvable");
            }


            case "getDim" ->
                    out.print(((RectangularMap) salle.getCrea().getMap()).getHeight() + ";" + ((RectangularMap) salle.getCrea().getMap()).getWidth());
        }
    }

    private void constraintRequests(HttpServletRequest request, PrintWriter out, Room salle) {
        CreatingIntermediate crea = salle.getCrea();

        switch (request.getParameter("constraint")) {
            case "imposePlace" -> {
                if (request.getParameter("oldNum") != null  && request.getParameter("newNum") != null && request.getParameter("numEtu") != null && ! request.getParameter("oldNum").isEmpty() && ! request.getParameter("numEtu").isEmpty()) {
                    out.print(crea.tableValidateButton(Integer.parseInt(request.getParameter("oldNum")), request.getParameter("newNum").isEmpty() ? 0 : Integer.parseInt(request.getParameter("newNum")), request.getParameter("numEtu")));
                }else {
                    out.print("champs vides !!");
                }
            }

            case "removeImposedPlace" -> crea.removeContrainst("I", Integer.parseInt(request.getParameter("id")) - 1);

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

    @Transactional(Transactional.TxType.REQUIRED)
    private static boolean loadSession(String oldId, String newId) {
        if ((!newId.equals(oldId)) && userExists(oldId) ) {
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
                Room newData = new Room(path) ;
                rooms.put(user,newData);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                msg = e.getMessage() ;
            }
        }
    }


    public String getUserData(String user) {
        String result = userExists(user) ? user : "null";
        if (!result.equals("null")) {
            Room salle = getSalle(user);
            // les infos de la visu
            if (salle!=null){
                result = "" ;
                if (salle.getPositioningIntermediate() != null) {
                    result += "\n" + salle.getPositioningIntermediate().getTablesForVisu() +"<";
                }
                // les infos d'etudians mis a distance

                result += salle.getCrea().getSeparated();
                result += "<";
                result += salle.getCrea().getStudentList() +"<";
            }

            
        }
        return result;
    }


}