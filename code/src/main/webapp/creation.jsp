<%--
  Created by IntelliJ IDEA.
  User: malimam (aka. le goat)
  Date: 19/11/2025
  Time: 10:22
  To change this template use File | Settings | File Templates.
--%>
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

<body class="pageCrea">

<header class="headerGauche">
    <a href="index.jsp">
        <div class="logo">
            <img class="logoPageSec" src="resources/img/Logo_DSRoomMaker.png" alt="Logo">
            <h1>DSRoomMaker Création</h1>
        </div>
    </a>
    <div class="general">
        <form class="column" method="post" enctype="multipart/form-data" id="fileUploadForm">
            <label for="studentFile">Déposez votre fichier d'étudiants (CSV) </label>
            <input type="file" name="studentFile" id="studentFile" accept="text/csv">
            <label for="mode">Séparation des étudiants par groupes classes </label>
            <select id="mode" name="mode">
                <option value="normal" selected>Placement basique</option>
                <option value="group">Par groupe</option>
                <option value="sub-group">Par sous-groupe</option>
            </select>
            <span class="ligne">
                    <label for="long">Nombre de tables par colonne</label>
                    <input type="number" name="long" id="long" min="4" max="20" step="1" value="10">
                </span>
            <span class="ligne">
                    <label for="larg">Nombre de tables par ligne</label>
                    <input type="number" name="larg" id="larg" min="4" max="8" step="1" value="4">
                </span>
            <button type="button" id="startConstr" class="validNbTable" onclick="enableZone()"> Valider le fichier et le nombre de places</button> <%-- faudra que ça valide le nombre de table. Faut ça avant de faire la génération --%>

        </form>
        <form method="POST" action="Display">
            <input type="text" name="testVal" id="testVal" >
            <button type="submit" id="walid" class="boutWalider" onclick="enableText()" disabled> Générer</button>
        </form>

    </div>

</header>


<main>
    <!-- on va utiliser les maquettes pour faire un truc cool -->
    <div class="le_Form">
        <div id="contraintes_impose">

            <h2>Places imposées</h2>
            <div class="ligne" id="ligneImposed">

                <section id="impose1" class="invalid">
                    <div  class="inputLabel">
                        <label for="imposedStudentId1">Numéro étudiant</label>
                        <input name="imposedStudentId1" id="imposedStudentId1" type="text" disabled>
                    </div>

                    <div  class="inputLabel" >
                        <label for="imposedTableId1">Numéro table</label>
                        <input name="imposedTableId" id="imposedTableId1" type="number" min="1" disabled>
                    </div>

                    <div  class="inputLabel">
                        <label for="imposedStudentName1">Nom de l'étudiant</label>
                        <input name="idStudentImp1" type="text" id="imposedStudentName1" disabled>
                    </div>

                    <button class="remove" id="deleteImposed1" disabled>remove</button>
                    <button class="chercher" id="findImposed1" disabled>find</button>
                </section>

                <button id="ajoutImpos" class="boutPlus" onclick="createImposed()" disabled>+</button>
            </div>

        </div>

        <div id="contraintes_suppr">

            <h2>Tables supprimées</h2>
            <div class="ligne" id="deletedTablesRow">
                <section id="supTable1" class="invalid">

                        <section  class="inputLabel">
                            <label for="numTabSup1">Numéro table</label>
                            <input name="idTabSup1" id="numTabSup1" type="number" min="1" disabled>
                        </section>
                    <button class="remove" id="deleteTable1" disabled>remove</button>
                    <button class="chercher" id="findTable1" disabled>find</button>
                </section>

                <button id="ajoutSuppr" class="boutPlus" onclick="createSuppr()" disabled>+</button>
            </div>

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
                                <div  class="inputLabel">
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


<script src="resources/JS/script.js"></script>


</body>
</html>
