<%@ page import="org.NeoMalokVector.SAE_S3.*" %>
<%@ page import="utilitaire.Utilitaire" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>SAE de goat</title>
    <link rel="stylesheet" href="resources/css/styles.css">
</head>

<body>

<header>
    <img src="resources/img/logo.gif" alt="Logo" id="logo">

    <h1>DSRoomMaker</h1>
</header>

<main>
    <div id="butts">
        <a href="">G&eacute;n&eacute;rer un placement</a>
        <a href="visualisation.jsp">Visualiser/Modifier</a>
    </div>

    <form method="post" action="">
        <label for="userName">Nom:</label>
        <input type="text" id="userName" name="userName" required>
        <button type="submit">Walider</button>
    </form>

    <%
        // Check if the form has been submitted
        String name = request.getParameter("userName");
        if (name != null && !name.isEmpty())
        {
            // Call the Java function
            String greeting = Utilitaire.printName(name);
            out.println("<h2>" + greeting + "</h2>"); // Display the greeting
        }
    %>

    <hr>

    <div id="tutorial">
        <a href="">T&eacute;l&eacute;charger le tutoriel</a>
    </div>

</main>

<footer>
    <div>&copy; Copyright</div>
</footer>

<script>
    document.getElementById("logo").addEventListener("mouseenter", function () {
        <%
        out.println("<h2> logo <h2>");
        %>
    });
</script>

</body>
</html>