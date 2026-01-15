<!DOCTYPE html>

<html lang="fr">
<%@ page contentType="text/html;charset=UTF-8" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Page de visualisation</title>
    <link rel="stylesheet" href="resources/css/styles.css">
    <link rel="icon" type="image/png" href="resources/img/Logo_DSRoomMaker.png">
</head>

<body>

<header id="visualisationHeader">
    <a href="index.jsp">
        <img src="resources/img/Logo_DSRoomMaker.png" alt="Logo" class="logoPageSec">
    </a>
    <div class="topAsside">
        <input type="file" name="importvisu" id="importvisu" accept="text/csv">
        <label> Format d'export</label>
        <input type="checkbox" name="export" id="Excel" value="Excel"> Excel
        <input type="checkbox" name="export" id="Listing" value="Listing"> Listing
        <button id="exporter" onclick="expOrt()"> Exporter </button>
    </div>
</header>
<div class="reverseLigne" id="content">
    <main>
        <h4 id="here"> Les tables : </h4>
        <button id="tableExp" class="table" > Table </button>
    </main>

    <aside class="asideCrea">

        <span class="separateur"></span>
        <div class="valuesOfTable">
             <span id="TableNumber" >
                <label for="idTabVisu"> Numero de Table </label>
                <input name="idTabVisu" id="idTabVisu" type="number" disabled>
            </span>
            <span id="studentInfo">

                <div >
                    <label for="numEtuVisu"> Numero Etudiant </label>
                    <input name="numEtuVisu" id="numEtuVisu" type="text" disabled>
                </div>
                <div >
                    <label for="nomEtuVisu"> Nom de l'etudiant </label>
                    <input name="nomEtuVisu" id="nomEtuVisu" type="text" disabled>
                </div>
                <div >
                    <label for="grpEtuVisu"> Groupe classe l'etudiant </label>
                    <input name="grpEtuVisu" id="grpEtuVisu" type="text" disabled>
                </div>

            </span>
        </div>

        <button class="modif" type="button" id="swapForm" >
            <img src="resources/img/swap.png" alt="Échanger">
            <h2>&Eacute;changer</h2>
        </button>

    </aside>
</div>
<footer>
    <div> &copy; Copyright </div>
</footer>
<script type="module" src="resources/JS/scriptVisu.js">
</script>
</body>
</html>