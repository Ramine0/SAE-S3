<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Page de visualisation</title>
    <link rel="stylesheet" href="resources/css/styles.css">
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
        <button class="table"> Table </button>
    </main>

    <aside class="asideCrea">
        <div clas="topAsside">
            <input type="file" name="importvisu" id="importvisu" accept="text/csv">
            <button id="exporter"> Exporter </button>
        </div>
        <div class="valuesOfTable">
            <span>
                <label for="numTabSup1"> Num Table </label>
                <input name="idTabSup1" id="numTabSup1" type="number" disabled>
           </span>

        </div>

    </aside>
</div>
<footer>
    <div> &copy; Copyright </div>
</footer>
</body>
</html>
