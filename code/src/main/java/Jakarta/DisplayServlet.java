package Jakarta;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;
import placement.PositioningIntermediate;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Display")
@MultipartConfig
public class DisplayServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("Referer") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access is not allowed.");
            return;
        }

        String code = request.getParameter("testVal");
        if (CreationServlet.getRoom(code) == null)
            code = request.getSession().getId();

        Room room = CreationServlet.getRoom(code);

        if (room == null)
            response.sendRedirect("index.jsp");
        else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("""
                    <!DOCTYPE html>
                    
                    <html lang="fr">
                    
                    <head>
                       <meta charset="UTF-8">
                       <meta name="viewport" content="width=device-width, initial-scale=1">
                    
                       <title>Placement salle DS</title>
                       <link rel="icon" type="image/png" href="resources/img/Logo_DSRoomMaker.png">
                    
                       <link rel="stylesheet" href="resources/css/styles.css">
                    </head>
                    
                    <body>
                    
                    <header class="headerCentre">
                       <img class="logoHomePage" src="resources/img/Logo_DSRoomMaker.png" alt="Logo">
                    
                       <h1>Génération en cours</h1>
                    </header>
                    
                    <main>
                    """);


            PositioningIntermediate pos = room.getPositioning();

            if (room.generate())
                out.println("""
                        <h4> Génération réussie </h4>
                        <a href="double.jsp">Voir le résultat</a>
                        """);

            else {

                out.println("<p>" + pos.getTablesForVisu() + "</p>");
                out.println(pos.describeData());
                out.println("""
                        <h4> Erreur de génération </h4>
                        <a href="creation.jsp"><Retour à la page de création</a>
                        """);
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("Referer") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access is not allowed.");
            return;
        }

        Room room = CreationServlet.getRoom(request.getSession().getId());
        PositioningIntermediate pos = null;

        if (room != null)
            pos = room.getPositioning();
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        if (pos != null)
            switch (request.getParameter("action")) {
                // on recup le visuel des tables
                case "init" -> out.print(pos.getTablesForVisu());

                // on récupère les information de la table
                case "infos" -> {
                    try {
                        out.print(pos.tabInfoForVisu(Integer.parseInt(request.getParameter("number"))));
                    } catch (Exception e) {
                        out.print("null");
                    }
                }

                // on swap les étudiants des tables donnees
                case "swap" -> {
                    if (room.swapPlaces(Integer.parseInt(request.getParameter("number1")), Integer.parseInt(request.getParameter("number2"))))
                        out.println("0");
                    else
                        out.println("1");
                }
            }
        else
            out.print("rien");

        out.flush();
    }
}
