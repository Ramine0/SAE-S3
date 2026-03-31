package jakarta;

import jakarta.annotation.Resource;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.the_disabled.sae_s3.Room;
import placement.Data;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/Connection")
public class ConnectionServlet extends HttpServlet {
    private Room room;
    private Data data;
    private String user;

    @Resource(name = "jdbc/p2403918")
    private DataSource dataSource;

    private static void initPlacements(Connection connection, PrintWriter out, String user) throws SQLException {
        String initRequest = "Select id, name from Placement where idUser=?";

        try (PreparedStatement initialisationAttempt = connection.prepareStatement(initRequest)) {
            initialisationAttempt.setString(1, user);
            ResultSet placements = initialisationAttempt.executeQuery();
            if (placements.next()) {
                out.print(placements.getString(1)+","+placements.getString(2));
            }
            while (placements.next()) {
                out.print(";" + placements.getString(1)+","+placements.getString(2));
            }

            out.flush();
            placements.close();
        }
    }

    private static void addPlacement(HttpServletRequest request, Connection connection, String addPlacement) throws SQLException {
        try (PreparedStatement addAttempt = connection.prepareStatement(addPlacement)) {
            addAttempt.setString(1, request.getParameter("id"));
            addAttempt.setString(2, request.getParameter("name"));

            addAttempt.executeQuery();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("Referer") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Direct access is not allowed.");
            return;
        }
        if (data == null && (request.getParameter("action").equals("load") || request.getParameter("action").equals("add")))
            data = new Data();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try (Connection connection = dataSource.getConnection()) {
            if ("connect".equals(request.getParameter("action")))
                connect(request, connection, out);
            else if ("subscribe".equals(request.getParameter("action")))
                subscribe(request, connection, out);
             else if ("init".equals(request.getParameter("action")))
                initPlacements(connection, out, user);
            else if ("load".equals(request.getParameter("action")))
                load(request, connection, out);
            else if ("add".equals(request.getParameter("action")))
                add(request, connection);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
        out.flush();
    }

    private void connect(HttpServletRequest request, Connection connection, PrintWriter out) {
        String connectRequest = "Select id from User where email=? and password=? limit 1";
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try (PreparedStatement connexionAttempt = connection.prepareStatement(connectRequest)) {
            connexionAttempt.setString(1, email);
            connexionAttempt.setString(2, password);

            ResultSet login = connexionAttempt.executeQuery();
            if (login.next()) {
                user = login.getString(1);
                out.print(user);
            }else{
                out.print("Adresse mail ou mot de passe incorrect");
            }

            login.close();
        } catch (Exception e) {
            out.print(e.getMessage());
        }
    }

    private void subscribe(HttpServletRequest request, Connection connection, PrintWriter out) throws SQLException{
        String subscribeRequest = "insert into User (name, email, password) values (?, ?, ?)";
        String existRequest= "Select * from User where email=? limit 1";
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean exist = false;
        try (PreparedStatement existingCheckAttempt = connection.prepareStatement(existRequest)) {
            existingCheckAttempt.setString(1, email);
            ResultSet existingCheck = existingCheckAttempt.executeQuery();
            if (existingCheck.next()) {
                exist = true;
            }
        }
        if (!exist) {
            try (PreparedStatement subscribeAttempt = connection.prepareStatement(subscribeRequest)) {
                subscribeAttempt.setString(1, username);
                subscribeAttempt.setString(2, email);
                subscribeAttempt.setString(3, password);

                int result = subscribeAttempt.executeUpdate();
                if (result > 0) {
                    out.print("Abonnement réussi");
                } else {
                    out.print("Echec de l'abonnement");
                }
            } catch (Exception e) {
                out.print(e.getMessage());
            }
        }else{
            out.print("Un compte similaire existe déjà");
        }
    }

    private void load(HttpServletRequest request, Connection connection, PrintWriter out) throws SQLException {
        String loadStudentsRequest = "select number, name, firstname, grp from Student where idPlacement=?";
        String loadSeatsRequest = "select num, x, y, suppr, number from Seat p left join Student s on p.idStudent=s.number where p.idPlacement=?";
        String loadConstraintsRequest = "select type, number, num, subgrp, numGrp from Constr c left join Student s on c.idStudent = s.number left join Seat p on c.idSeat=p.num where c.idPlacement=?";
        String idPlacement = request.getParameter("idPlacement");

        loadStudents(connection, loadStudentsRequest, idPlacement);
        loadTables(connection, loadSeatsRequest, idPlacement);
        loadConstraints(connection, loadConstraintsRequest, idPlacement);
        out.println(room.getPositioning().getTablesForVisualisation());
    }

    private void loadStudents(Connection connection, String loadStudents, String idPlacement) throws SQLException {
        try (PreparedStatement loadAttempt = connection.prepareStatement(loadStudents)) {
            loadAttempt.setString(1, idPlacement);
            ResultSet studentsData = loadAttempt.executeQuery();
            StringBuilder students = new StringBuilder();
            if (studentsData.next()) {
                students.append(studentsData.getString(1)).append(",").append(studentsData.getString(2)).append(",").append(studentsData.getString(3));
                String[] group = studentsData.getString(4).replace(".", ";").split(";");
                students.append(group[0]);

                if (group.length > 1)
                    students.append(",").append(group[1]);
            }
            while (studentsData.next()) {
                students.append(studentsData.getString(1)).append(";").append(",").append(studentsData.getString(2)).append(",").append(studentsData.getString(3));
                String[] group = studentsData.getString(4).replace(".", ";").split(";");
                students.append(group[0]);

                if (group.length > 1)
                    students.append(",").append(group[1]);
            }

            data.loadStudents(students.toString());
        }
    }

    private void loadTables(Connection connection, String loadSeats, String idPlacement) throws SQLException {
        try (PreparedStatement loadAttempt = connection.prepareStatement(loadSeats)) {
            loadAttempt.setString(1, idPlacement);
            ResultSet tablesData = loadAttempt.executeQuery();
            StringBuilder tables = new StringBuilder();

            if (tablesData.next()) {
                tables.append(tablesData.getString(1)).append(",").append(tablesData.getString(2)).append(",").append(tablesData.getString(3)).append(",").append(tablesData.getString(4)).append(",").append(tablesData.getString(5));

            }
            while (tablesData.next()) {
                tables.append(tablesData.getString(1)).append(";").append(",").append(tablesData.getString(2)).append(",").append(tablesData.getString(3)).append(",").append(tablesData.getString(4)).append(",").append(tablesData.getString(5));
            }

            data.loadTables(tables.toString());
        }
    }

    private void loadConstraints(Connection connection, String loadConstraint, String idPlacement) throws SQLException {
        try (PreparedStatement loadAttempt = connection.prepareStatement(loadConstraint)) {
            loadAttempt.setString(1, idPlacement);
            ResultSet constraintsData = loadAttempt.executeQuery();
            StringBuilder constraints = new StringBuilder();

            while (constraintsData.next()) {
                if (constraintsData.getString(2).equals("I"))
                    constraints.append(constraintsData.getString(1)).append(",").append(constraintsData.getString(2)).append(",").append(constraintsData.getString(3)).append(",").append(constraintsData.getString(4));
                else if (constraintsData.getString(2).equals("G"))
                    constraints.append(constraintsData.getString(1)).append(",").append(constraintsData.getString(2)).append(",").append(constraintsData.getString(5));
                else if (constraintsData.getString(4).equals("true"))
                    data.changeMode('S');
                else
                    data.changeMode('G');
                constraints.append(";");
            }

            data.loadConstraints(constraints.toString());
        }
    }

    private void add(HttpServletRequest request, Connection connection) throws SQLException {
        String addPlacementRequest = "Insert into Placement (idUser, name) values (?, ?)";
        String addStudentRequest = "Insert into Student (number, idPlacement, name, firstname, grp) values (?, ?, ?, ?, ?)";
        String addSeatRequest = "Insert into Seat (num, x, y, idPlacement, idStudent) values (?, ?, ?, ?, ?)";
        String addConstraintRequest = "Insert into Constraints (type, idPlacement, idStudent, idSeat, subgrp, numGrp) values (?, ?, ?, ?, ?)";

        addPlacement(request, connection, addPlacementRequest);
        addStudents(request, connection, addStudentRequest);
        addPlaces(request, connection, addSeatRequest);
        addConstraints(request, connection, addConstraintRequest);
    }

    private void addStudents(HttpServletRequest request, Connection connection, String addStudent) throws SQLException {
        int cnt = 0;

        while (cnt != data.getStudents().length) {
            String[] student = data.getStudents()[cnt].textVisualisation().replace(" ", ";").split(";");

            try (PreparedStatement addAttempt = connection.prepareStatement(addStudent)) {
                addAttempt.setString(1, student[0]);
                addAttempt.setString(2, request.getParameter("idP"));
                addAttempt.setString(3, student[1]);
                addAttempt.setString(4, student[2]);
                addAttempt.setString(5, student[3]);
                addAttempt.executeUpdate();

            }

            cnt++;
        }
    }

    private void addPlaces(HttpServletRequest request, Connection connection, String addSeat) throws SQLException {
        int cnt = 0;
        String[] tables = data.getTablesInfos().split(";");

        while (cnt != tables.length) {
            String[] table = tables[cnt].split("!");

            try (PreparedStatement addAttempt = connection.prepareStatement(addSeat)) {
                addAttempt.setString(1, table[0]);
                addAttempt.setString(2, table[1]);
                addAttempt.setString(3, table[2]);
                addAttempt.setString(4, request.getParameter("idP"));
                addAttempt.setString(5, table[3]);

                addAttempt.executeUpdate();
            }

            cnt++;
        }
    }

    private void addConstraints(HttpServletRequest request, Connection connection, String addConstraint) throws SQLException {
        int cnt = 0;

        while (cnt != data.getConstraintsNumber()) {
            String[] constraint = data.getConstraints()[cnt].toDatabase().split(",");

            if (constraint[0].equals("G"))
                for (int i = 2; i < constraint.length; i++)
                    try (PreparedStatement addAttempt = connection.prepareStatement(addConstraint)) {
                        addAttempt.setString(1, constraint[0]);
                        addAttempt.setString(2, request.getParameter("idP"));
                        addAttempt.setString(3, constraint[i]);
                        addAttempt.setString(4, null);
                        addAttempt.setString(5, null);
                        addAttempt.setString(6, constraint[1]);

                        addAttempt.executeUpdate();
                    }
            else
                try (PreparedStatement addAttempt = connection.prepareStatement(addConstraint)) {
                    if (constraint[0].equals("I")) {
                        addAttempt.setString(1, constraint[0]);
                        addAttempt.setString(2, request.getParameter("idP"));
                        addAttempt.setString(3, constraint[1]);
                        addAttempt.setString(4, constraint[2]);
                        addAttempt.setString(5, null);
                        addAttempt.setString(6, null);
                    } else {
                        addAttempt.setString(1, constraint[0]);
                        addAttempt.setString(2, request.getParameter("idP"));
                        addAttempt.setString(3, null);
                        addAttempt.setString(4, null);
                        addAttempt.setString(5, constraint[1]);
                        addAttempt.setString(6, null);
                    }
                    addAttempt.executeUpdate();

                }
        }
    }
}
