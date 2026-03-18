<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>

<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Saisie des données</title>
    <link rel="stylesheet" href="resources/css/styles.css">
    <link rel="icon" type="image/png" href="resources/img/Logo_DSRoomMaker.png">
</head>
<body>

<div id="doubleContent">
    <header class="headerGauche">
        <a href="index.jsp">
            <div class="logo">
                <img class="logoPageSec" src="resources/img/Logo_DSRoomMaker.png" alt="Logo">
                <h1>DSRoomMaker Création</h1>
            </div>
        </a>
        <div class="general">
            <form class="column" method="post" enctype="multipart/form-data" id="fileUploadForm">
                <div class="selecteur">
                    <label for="modeHeader">Mode d'utilisation </label>
                    <select id="modeHeader" name="modeHeader">
                        <option value="create" selected>Paramètres de création</option>
                        <option value="modify">Voir les informations</option>
                        <option value="export">Fonctions d'export</option>
                        <option value="import">Fonctions d'import</option>
                    </select>
                </div>

                <div class="modeDouble invisible" id="exportArea" >
                    <label for="Excel"> Format d'export</label>
                    <input type="checkbox" name="export" id="Excel" value="Excel"> Excel
                    <button id="exporter"> Exporter </button>
                </div>

                <div class="modeDouble invisible" id="importArea" >
                    <label for="importCSV"> Format d'import</label>
                    <input type="checkbox" name="importByFile" id="importCSV" value="importByFile"> Fichier CSV
                    <button id="import"> Importer </button>
                </div>

                <div class="modeDouble" id="parameters">
                    <label for="studentFile">Déposez votre fichier d'étudiants (CSV) </label>
                    <input type="file" name="studentFile" id="studentFile" accept="text/csv">

                    <br>

                    <label for="classMode">Type de séparation</label>
                    <select id="classMode" name="classMode">
                        <option value="normal" selected>Placement basique</option>
                        <option value="group">Par groupe</option>
                        <option value="sub-group">Par sous-groupe</option>
                    </select>

                    <br>

                    <label for="planType">Type de plan</label>

                    <select id="planType" name="planType">
                        <option value="defaultPlan" selected>Plan par défaut</option>
                        <option value="rectangularPlan">Plan rectangulaire</option>
                    </select>


                    <div id="infoRect" class="invisible">
                        <section class="ligne">
                            <label for="long">Nombre de tables par colonne</label>
                            <input type="number" name="long" id="long" min="4" max="20" step="1" value="10">
                        </section>

                        <section class="ligne">
                            <label for="larg">Nombre de tables par ligne</label>
                            <input type="number" name="larg" id="larg" min="4" max="8" step="1" value="4">
                        </section>
                    </div>

                    <button type="button" id="startConstr" class="validNbTable" onclick="enableZone()"> Valider le fichier et le plan</button>

                </div>

                <div class="modeDouble invisible" id="valuesOfTable">
                     <span id="TableNumber" >
                        <label for="idTabVisu"> Numero de Table </label>
                        <input name="idTabVisu" id="idTabVisu" type="number" disabled>
                    </span>
                    <div id="studentInfo">

                        <div>
                            <label for="numEtuVisu"> Numero Etudiant </label>
                            <input name="numEtuVisu" id="numEtuVisu" type="text" disabled>
                        </div>
                        <div>
                            <label for="nomEtuVisu"> Nom de l'etudiant </label>
                            <input name="nomEtuVisu" id="nomEtuVisu" type="text" disabled>
                        </div>
                        <div>
                            <label for="grpEtuVisu"> Groupe classe l'etudiant </label>
                            <input name="grpEtuVisu" id="grpEtuVisu" type="text" disabled>
                        </div>

                        <span>
                            <input type="button" onclick="setTableInfos()" value="appliquer">
                            <input type="button" onclick="modeSwap()" value="echanger">
                        </span>

                    </div>
                </div>

            </form>
            <form method="post" action="Display">
                <input type="text" name="sessionCode" id="sessionCode">
                <button type="button" id="loadSession" onclick="loadData()" > Charger </button>
                <button type="submit" id="walid" class="boutWalider" disabled> Générer
                </button>
            </form>

        </div>

    </header>

    <main class="mainDouble">

        <!-- on va utiliser les maquettes pour faire un truc cool -->
        <div class="le_Form">
            <div id="visuofDouble" style="visibility: hidden">
                <h4 id="here"> Les tables : </h4>
                <section id="lesTables">
                </section>
            </div>

            <div id="EtuDist">

                <h2>Etudiants mis à distance</h2>
                <section>
                    <div class="ligne" id="Gp1">
                        <section id="E1G1" class="invalid">

                            <div>
                                <div class="inputLabel">
                                    <label for="idEtu1G1">Numéro étudiant</label>
                                    <input name="idEtu1G1" id="idEtu1G1" type="text" disabled>
                                </div>
                                <div class="inputLabel">
                                    <label for="nomEtu1G1">Nom de l'étudiant</label>
                                    <input name="nomEtu1G1" id="nomEtu1G1" type="text" disabled>
                                </div>
                            </div>
                            <button class="remove" id="supEtu1G1" disabled>remove</button>
                            <button class="chercher" id="walEtu1G1" disabled>find</button>
                        </section>

                        <button id="ajoutEtuGrp1" class="boutPlus" disabled>+</button>
                        <h4>Ajouter un étudiant au groupe</h4>

                    </div>

                    <button id="ajoutGroup" class="boutPlus" onclick="createGrp()" disabled>+</button>
                    <h4>Ajouter un groupe</h4>
                </section>
            </div>
        </div>

    </main>
</div>

<footer>
    <div> &copy; Copyright</div>
</footer>

<script src="resources/JS/script.js"></script>

</body>
</html>