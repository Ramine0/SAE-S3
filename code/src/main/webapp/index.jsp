<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>

<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>SAE de goat</title>
    <link rel="stylesheet" href="resources/css/styles.css">
</head>

<body>

<header class="headerCentre">
    <img class="logoHomePage" src="resources/img/Logo_DSRoomMaker.png" alt="Logo">

    <h1>DSRoomMaker</h1>
</header>

<main>
    <div id="butts">
        <a href="ancienCreation.jsp">Générer un placement</a>
        <a href="visualisation.jsp">Visualiser/Modifier</a>
    </div>

    <hr>

    <div id="tutorial">
        <a href="resources/tuto.pdf">Télécharger le tutoriel</a>
    </div>
    <button id="boutest" onclick=""></button>
</main>

<footer>
    <div>&copy; Copyright</div>
</footer>
<script src="resources/JS/script.js">
    start();
    callServlet();

</script>
</body>
</html>