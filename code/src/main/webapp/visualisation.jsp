<!DOCTYPE html>
<html lang="fr">

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
    <div class="topMenu">
        <div>
            <button class="modif">
                <img src="resources/img/delete.png" alt="Supprimer">
                <h2>Supprimer</h2>
            </button>
        </div>

        <div>
            <button class="modif">
                <img src="resources/img/swap.png" alt="Échanger">
                <h2>&Eacute;changer</h2>
            </button>
        </div>
    </div>
</header>
<div class="reverseLigne" id="content">
    <main>
        <h4 id="here"> Les tables : </h4>
        <button class="table" > Table </button>
    </main>

    <aside class="asideCrea">
        <div class="topAsside">
            <input type="file" name="importvisu" id="importvisu" accept="text/csv">
            <label> Format d'export</label>
            <input type="checkbox" name="export" id="Excel" value="Excel"> Excel
            <input type="checkbox" name="export" id="Listing" value="Listing"> Listing
            <button id="exporter" onclick="expOrt()"> Exporter </button>
        </div>
        <span class="separateur"></span>
        <div class="valuesOfTable">
             <span id="TableNumber" >
                <label for="numTabVisu"> Numero de Table </label>
                <input name="idTabVisu" id="numTabVisu" type="number" disabled>
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

            </span>


        </div>

    </aside>
</div>
<footer>
    <div> &copy; Copyright </div>
</footer>
<script type="module" src="resources/JS/scriptVisu.js">
</script>
</body>
</html>