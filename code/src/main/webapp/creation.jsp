<%--
  Created by IntelliJ IDEA.
  User: malimam
  Date: 19/11/2025
  Time: 10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Saisie des données</title>
    <link rel="stylesheet" href="resources/css/styles.css">
</head>

<body class="pageCrea">

<header>
    <a href="index.jsp">
        <div class="logo">
            <img class="logoPageSec" src="resources/img/logo.gif" alt="Logo">
            <h1>DSRoomMaker Creation</h1>
        </div>
    </a>
    <div class="gen">
        <form action="" method="post" enctype="multipart/form-data">
            <input type="file" name="studentFile" id="studentFile" accept="text/csv">
            <select id="mode" name="mode">
                <option value="normal" selected> Placement basique</option>
                <option value="group"> Par groupe</option>
                <option value="sub-group"> Par sous-groupe</option>
            </select>
            <label for="long"><h3>Nombre de tables par colonnes</h3></label>
            <input type="number" name="long" id="long" min="4" max="20" step="1" value="4">
            <label for="larg"><h3>Nombre de tables par lignes</h3></label>
            <input type="number" name="larg" id="larg" min="4" max="8" step="1" value="4">
            <button type="submit" class="boutWalider" onclick=""> Générer</button>
        </form>
    </div>

</header>


<main>
    <!-- on va utiliser les maquettes pour faire un truc cool -->
    <div class="le_Form">
        <form action="" method="post">

            <div id="contraintes_gen">
                <label><h2> Places imposées </h2></label>
                <button class="boutPlus placeImpos" onclick="createImposed()">+</button>
            </div>


        </form>


    </div>
</main>

<script src="resources/JS/script.js"></script>
</body>
</html>
