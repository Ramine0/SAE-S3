package Jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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

@WebServlet("/Connection")
public class ConnectionServlet extends HttpServlet {
    private Data data;

    @Resource(name="p2403918")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (data==null){
            data=new Data();
        }
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
                    resultSet.close();
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
                    while (!resultSet.wasNull()) {
                        out.print(resultSet.getString(1));
                        if (resultSet.next()){
                            out.print(";");
                        }
                    }
                    out.flush();
                    resultSet.close();
                }
            }else if (request.getParameter("action").equals("load")){
                String loadStudents="select number, name, firstname, grp from Student where idPlacement=?";
                String loadSeats="select num, x, y, suppr, number from Seat p left join Student s on p.idStudent=s.id where p.idPlacement=?";
                String loadConstraint="select type, number, num, subgrp, numGrp from Constr c left join Student s on c.idStudent = s.id left join Seat p on c.idSeat=p.id where c.idPlacement=?";
                String idPlacement=request.getParameter("idPlacement");
                try (PreparedStatement preparedStatement = connection.prepareStatement(loadStudents)) {
                    preparedStatement.setString(1, idPlacement);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    String students="";
                    while (!resultSet.wasNull()) {
                        students+=resultSet.getString(1)+","+resultSet.getString(2)+","+resultSet.getString(3);
                        String[] group=resultSet.getString(4).replace(".", ";").split(";");
                        students+=group[0];
                        if (group.length>1){
                            students+=","+group[1];
                        }
                        if (resultSet.next()){
                            students+=";";
                        }
                    }
                    data.chargerStudents(students);
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(loadSeats)){
                    preparedStatement.setString(1, idPlacement);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    String seats="";
                    while (!resultSet.wasNull()) {
                        seats+=resultSet.getString(1)+","+resultSet.getString(2)+","+resultSet.getString(3)+","+resultSet.getString(4)+","+resultSet.getString(5);
                        if (resultSet.next()){
                            seats+=";";
                        }
                    }
                    data.chargerTables(seats);
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(loadConstraint)) {
                    preparedStatement.setString(1, idPlacement);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    String constraints="";
                    while (!resultSet.wasNull()) {
                        if (resultSet.getString(2).equals("I")){
                            constraints+=resultSet.getString(1)+","+resultSet.getString(2)+","+resultSet.getString(3)+","+resultSet.getString(4);
                        }else if (resultSet.getString(2).equals("G")){
                            constraints+= resultSet.getString(1)+","+resultSet.getString(2)+","+resultSet.getString(5);
                        }else{
                            if (resultSet.getString(4).equals("true")) {
                                data.changeMode('S');
                            }else{
                                data.changeMode('G');
                            }
                        }
                        if (!resultSet.getString(1).equals("C") && resultSet.next()){
                            constraints+=";";
                        }
                    }

                }
            }else if (request.getParameter("action").equals("add")){
                String addPlacement="Insert into Placement (idUser, name) values (?, ?)";
                String addStudent="Insert into Student (number, idPlacement, name, firstname, grp) values (?, ?, ?, ?, ?)";
                String addSeat="Insert into Seat (num, x, y, idPlacement, idStudent) values (?, ?, ?, ?, ?)";
                String addConstraint="Insert into Constraints (type, idPlacement, idStudent, idSeat, grpConstr) values (?, ?, ?, ?, ?)";

            }else if (request.getParameter("action").equals("change")){

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
