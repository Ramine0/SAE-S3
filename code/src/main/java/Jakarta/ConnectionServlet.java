package Jakarta;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import placement.Data;

import java.io.PrintWriter;
import java.security.MessageDigest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/Connection")
public class ConnectionServlet extends HttpServlet {
    private Data data;

    @Resource(name="p2403918")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
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
                String loadSeats="select num, x, y, suppr, number from Seat p left join Student s on p.idStudent=s.number where p.idPlacement=?";
                String loadConstraint="select type, number, num, subgrp, numGrp from Constr c left join Student s on c.idStudent = s.number left join Seat p on c.idSeat=p.num where c.idPlacement=?";
                String idPlacement=request.getParameter("idPlacement");
                try (PreparedStatement preparedStatement = connection.prepareStatement(loadStudents)) {
                    preparedStatement.setString(1, idPlacement);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    StringBuilder students= new StringBuilder();
                    while (!resultSet.wasNull()) {
                        students.append(resultSet.getString(1)).append(",").append(resultSet.getString(2)).append(",").append(resultSet.getString(3));
                        String[] group=resultSet.getString(4).replace(".", ";").split(";");
                        students.append(group[0]);
                        if (group.length>1){
                            students.append(",").append(group[1]);
                        }
                        if (resultSet.next()){
                            students.append(";");
                        }
                    }
                    data.chargerStudents(students.toString());
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(loadSeats)){
                    preparedStatement.setString(1, idPlacement);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    StringBuilder seats= new StringBuilder();
                    while (!resultSet.wasNull()) {
                        seats.append(resultSet.getString(1)).append(",").append(resultSet.getString(2)).append(",").append(resultSet.getString(3)).append(",").append(resultSet.getString(4)).append(",").append(resultSet.getString(5));
                        if (resultSet.next()){
                            seats.append(";");
                        }
                    }
                    data.chargerTables(seats.toString());
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(loadConstraint)) {
                    preparedStatement.setString(1, idPlacement);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    StringBuilder constraints= new StringBuilder();
                    while (!resultSet.wasNull()) {
                        if (resultSet.getString(2).equals("I")){
                            constraints.append(resultSet.getString(1)).append(",").append(resultSet.getString(2)).append(",").append(resultSet.getString(3)).append(",").append(resultSet.getString(4));
                        }else if (resultSet.getString(2).equals("G")){
                            constraints.append(resultSet.getString(1)).append(",").append(resultSet.getString(2)).append(",").append(resultSet.getString(5));
                        }else{
                            if (resultSet.getString(4).equals("true")) {
                                data.changeMode('S');
                            }else{
                                data.changeMode('G');
                            }
                        }
                        if (!resultSet.getString(1).equals("C") && resultSet.next()){
                            constraints.append(";");
                        }
                    }
                    data.chargerConstraint(constraints.toString());

                }
            }else if (request.getParameter("action").equals("add")){
                String addPlacement="Insert into Placement (idUser, name) values (?, ?)";
                String addStudent="Insert into Student (number, idPlacement, name, firstname, grp) values (?, ?, ?, ?, ?)";
                String addSeat="Insert into Seat (num, x, y, idPlacement, idStudent) values (?, ?, ?, ?, ?)";
                String addConstraint="Insert into Constraints (type, idPlacement, idStudent, idSeat, subgrp, numGrp) values (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(addPlacement)){
                    preparedStatement.setString(1, request.getParameter("id"));
                    preparedStatement.setString(2, request.getParameter("name"));
                    preparedStatement.executeQuery();
                }
                int cnt=0;
                while (cnt!=data.getEtus().length){
                    String[] student=data.getEtus()[cnt].textVisu().replace(" ", ";").split(";");
                    try (PreparedStatement preparedStatement = connection.prepareStatement(addStudent)){
                        preparedStatement.setString(1, student[0]);
                        preparedStatement.setString(2, request.getParameter("idP"));
                        preparedStatement.setString(3, student[1]);
                        preparedStatement.setString(4, student[2]);
                        preparedStatement.setString(5, student[3]);
                    }
                    cnt++;
                }
                cnt=0;
                String[] tables=data.getTablesInfos().split(";");
                while (cnt!=tables.length){
                    String[] table=tables[cnt].split("!");
                    try (PreparedStatement preparedStatement = connection.prepareStatement(addSeat)){
                        preparedStatement.setString(1, table[0]);
                        preparedStatement.setString(2, table[1]);
                        preparedStatement.setString(3, table[2]);
                        preparedStatement.setString(4, request.getParameter("idP"));
                        preparedStatement.setString(5, table[3]);
                        preparedStatement.executeQuery();
                    }
                    cnt++;
                }
                cnt=0;
                while (cnt!=data.getNbConstraint()){
                    String[] contrainte=data.getConstr()[cnt].toDatabase().split(",");
                    if (contrainte[0].equals("G")){
                        for (int i=2; i<contrainte.length; i++){
                            try (PreparedStatement preparedStatement = connection.prepareStatement(addConstraint)){
                                preparedStatement.setString(1, contrainte[0]);
                                preparedStatement.setString(2, request.getParameter("idP"));
                                preparedStatement.setString(3, contrainte[i]);
                                preparedStatement.setString(4, null);
                                preparedStatement.setString(5, null);
                                preparedStatement.setString(6, contrainte[1]);
                                preparedStatement.executeQuery();
                            }
                        }
                    } else {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(addConstraint)){
                            if (contrainte[0].equals("I")){
                                preparedStatement.setString(1, contrainte[0]);
                                preparedStatement.setString(2, request.getParameter("idP"));
                                preparedStatement.setString(3, contrainte[1]);
                                preparedStatement.setString(4, contrainte[2]);
                                preparedStatement.setString(5, null);
                                preparedStatement.setString(6, null);
                            } else {
                                preparedStatement.setString(1, contrainte[0]);
                                preparedStatement.setString(2, request.getParameter("idP"));
                                preparedStatement.setString(3, null);
                                preparedStatement.setString(4, null);
                                preparedStatement.setString(5, contrainte[1]);
                                preparedStatement.setString(6, null);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    String sha256(String password){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            byte[] byteData =digest.digest();
            StringBuilder sb=new StringBuilder();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
    }

}
