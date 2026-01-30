package Jakarta;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.NeoMalokVector.SAE_S3.Room;
import placement.PositioningIntermediate;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Display")
@MultipartConfig
public class DisplayServlet extends HttpServlet
{


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String code = request.getParameter("testVal");
        if (CreationServlet.getSalle(code) == null){code = request.getSession().getId();}
        Room salle = CreationServlet.getSalle(code);

        if (salle == null)
            response.sendRedirect("index.jsp");
        else
        {
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
            if (salle.positioningMode())
            {
                PositioningIntermediate pos = salle.getPositioningIntermediate();

                if (salle.generate())
                    out.println("""
                            <h4> Génération réussie </h4>
                            <a href="visualisation.jsp">Voir le résultat</a>
                            """);
                else
                {
                    out.println("<p>" + pos.getTablesForVisu() + "</p>");
                    out.println(pos.descripData());

                    out.println("""
                            <h4> Erreur de génération </h4>
                            <a href="creation.jsp"><Retour à la page de création</a>
                            """);
                }
            } else
            {
                out.println(salle.message);
                out.println("""
                        <h4> Erreur lors du changement en visualisation </h4>
                        <a href="index.jsp">Retour à la page d'accueil</a>
                        """);

            }
        }
        //response.sendRedirect("index.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Room salle = CreationServlet.getSalle(request.getSession().getId());
        PositioningIntermediate pos = salle.getPositioningIntermediate();

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        if (salle != null && pos != null)
            switch (request.getParameter("action"))
            {
                case "init" -> out.print(pos.getTablesForVisu());

                case "infos" -> out.print(pos.tabInfoForVisu(Integer.parseInt(request.getParameter("number"))));

                case "swap" ->
                {
                    if (salle.swapPlaces(Integer.parseInt(request.getParameter("number1")), Integer.parseInt(request.getParameter("number2"))))
                        out.println("0");
                    else
                        out.println("1");
                }
            }

        out.flush();
    }
}
