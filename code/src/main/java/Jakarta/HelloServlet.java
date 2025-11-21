package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet
{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String userInput = request.getParameter("name");


        response.setContentType("text/html");
        response.getWriter().println("<h1>Your input was: " + userInput + "</h1>");
    }

    public void destroy()
    {
    }
}