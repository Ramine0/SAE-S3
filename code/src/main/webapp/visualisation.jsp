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
        <img src="resources/img/Logo_DSRoomMaker.png" alt="Logo" id="logo">
    </a>
        <div id="topMenu">
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
<div id="content">
<main>
    <button> Table </button>
</main>

<aside>
    <input type="file" name="importvisu" id="importvisu" accept="text/csv">
    <button id="regenerate"> Regenerer </button>
    <button id="exporter"> Exporter </button>
</aside>
</div>
<footer>
    <div> &copy; Copyright </div>
</footer>
</body>
</html>
