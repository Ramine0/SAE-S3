package Jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.NeoMalokVector.SAE_S3.Room;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Display")
@MultipartConfig
public class DisplayServlet extends HttpServlet
{
    private static Room salle;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

        String code = request.getParameter("testVal") ;
        if ( ! code.isEmpty()) {
            salle = TableServlet.getSalle(code) ;
            if (salle == null) {
                response.sendRedirect("index.jsp");
            }else {
                //response.sendRedirect("visualisation.jsp");
            }

        }
        //response.sendRedirect("index.jsp");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("je tente un truc");
        out.flush();
    }
}
