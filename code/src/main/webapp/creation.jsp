<%--
  Created by IntelliJ IDEA.
  User: malimam
  Date: 19/11/2025
  Time: 10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Saisie des données</title>
    <link rel="stylesheet" href="resources/css/styles.css">
</head>

<body class="pageCrea">

    <header >
        <img class="logoPageSec" src="resources/img/logo.gif" alt="Logo">
        <h1>DSRoomMaker Creation</h1>

    </header>


    <main>
        <!-- on va utiliser les maquettes pour faire un truc cool -->
        <div class="le_Form">

            <form action="" method="post">
                <div id="creation">
                    <button id="import"> Importer un fichier </button>
                    <select id="mode" name="mode">
                        <option value="normal" selected> Placement basique</option>
                        <option value="group"> Par groupe </option>
                        <option value="sub-group"> Par sous-groupe </option>
                    </select>
                </div>

                <div id="contraintes_gen">

                </div>
                <label></label>
                <button></button>

                <label></label>
                <button></button>

                <label></label>
                <button></button>

                <label></label>
                <button class="boutPlus">+</button>

                <button id="create"> Générer </button>

            </form>


        </div>
    </main>
</body>
</html>
