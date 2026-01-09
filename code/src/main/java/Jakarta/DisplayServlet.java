package Jakarta;

import jakarta.servlet.ServletException;
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
public class DisplayServlet extends HttpServlet
{
    private static Room salle = null;
    private PositioningIntermediate pos = null ;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

        String code = request.getParameter("testVal") ;
        if ( ! code.isEmpty()) {
            salle = TableServlet.getSalle(code) ;
            if (salle == null) {
                response.sendRedirect("index.jsp");
            }else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter() ;
                out.println("""
                <!DOCTYPE html>
                
                <html lang="fr">
               
                <head>
                   <meta charset="UTF-8">
                   <meta name="viewport" content="width=device-width, initial-scale=1">
               
                   <title>SAE de goat</title>
                   <link rel="icon" type="image/png" href="resources/img/Logo_DSRoomMaker.png">
               
                   <link rel="stylesheet" href="resources/css/styles.css">
                </head>
               
                <body>
               
                <header class="headerCentre">
                   <img class="logoHomePage" src="resources/img/Logo_DSRoomMaker.png" alt="Logo">
               
                   <h1>Generation en cours</h1>
                </header>
                
                <main>
                """) ;
                if (salle.positioningMode()) {
                    pos = salle.getPositioningIntermediate() ;
                    if (salle.generate()) {
                        out.println("""
                        <h4> Generation réussie </h4>
                        <a href="visualisation.jsp">Voir le résultat</a>
                        """);
                    }else {
                        out.println("<p>"+pos.getTablesForVisu()+"</p>") ;
                        out.println(pos.descripData());
                        out.println("""
                        <h4> Erreur de Generation </h4>
                        <a href="creation.jsp">retour a la page de creation</a>
                        """);
                    }
                }else {
                    out.println(salle.message);
                    out.println("""
                    <h4> erreur lors du changement en visualisation </h4>
                    <a href="index.jsp">retour a la page d'acceuil</a>
                    """);

                }



            }

        }
        //response.sendRedirect("index.jsp");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        if (salle != null && pos != null ) {

            if (request.getParameter("action").equals("init")) {

                out.print(pos.getTablesForVisu());

            }else if (request.getParameter("action").equals("infos")) {
                out.print(pos.tabInfoForVisu(Integer.parseInt(request.getParameter("number"))));
            }else if (request.getParameter("action").equals("swap")) {
                salle.swapPlaces(1,2) ;
            }

        }
        out.flush();

    }
}
