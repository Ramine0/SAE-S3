package Jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import placement.Data;

import java.io.PrintWriter;
import java.security.MessageDigest;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectionServlet extends HttpServlet {
    private Data data;

    @Resource(name="p2403918")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try (Connection connection = dataSource.getConnection("p2403918", "12403918")) {
            if (request.getParameter("action").equals("connect")){
                String requestConnect="Select id from User where name=? and password=? limit 1";
                String username = request.getParameter("username");
                String password = sha256(request.getParameter("password"));
                try (PreparedStatement preparedStatement = connection.prepareStatement(requestConnect)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    out.print(resultSet.getString(1));
                    out.flush();
                }
            }else if (request.getParameter("action").equals("subscribe")){
                String requestSubscribe="insert into User (name, email, password) values (?, ?, ?)";
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String password = sha256(request.getParameter("password"));
                try (PreparedStatement preparedStatement = connection.prepareStatement(requestSubscribe)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);
                    preparedStatement.executeUpdate();
                }
            }else if (request.getParameter("action").equals("init")){
                String requestInit="Select name from Placement where idUser=?";
                String id=request.getParameter("id");
                try (PreparedStatement preparedStatement = connection.prepareStatement(requestInit)) {
                    preparedStatement.setString(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                }
            }else if (request.getParameter("action").equals("load")){

            }else if (request.getParameter("action").equals("add")){

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    String sha256(String password){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            byte byteData[]=digest.digest();
            StringBuffer sb=new StringBuffer();
            for(int i=0;i<byteData.length;i++){
                sb.append(Integer.toString((byteData[i]&0xff)+0x100, 16).substring(1));
            }
            return sb.toString();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
    }

}
