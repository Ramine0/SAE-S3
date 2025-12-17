package Jakarta;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name="creation", value = "/creation")
@MultipartConfig
public class CreationServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        response.getWriter().print("""
                    <html>
                    
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1">
                    
                        <title>Saisie des données</title>
                        <link rel="stylesheet" href="resources/css/styles.css">
                    </head>
                    
                    <body class="pageCrea">
                    
                        <header class ="headerGauche">
                            <a href="index.jsp">
                                <div class="logo">
                                    <img class="logoPageSec" src="resources/img/Logo_DSRoomMaker.png" alt="Logo">
                                    <h1>DSRoomMaker Creation</h1>
                                </div>
                            </a>
                            <div class="general">
                                <form class="column" method="post" enctype="multipart/form-data" action="">
                                    <input type="file" name="studentFile" id="studentFile" accept="text/csv">
                                    <select id="mode" name="mode">
                                        <option value="normal" selected> Placement basique</option>
                                        <option value="group"> Par groupe</option>
                                        <option value="sub-group"> Par sous-groupe</option>
                                    </select>
                                    <span class="ligne">
                                        <label for="long"><h4>Nombre de tables par colonnes</h4></label>
                                        <input type="number" name="long" id="long" min="4" max="20" step="1" value="4">
                                    </span>
                                    <span class="ligne">
                                        <label for="larg"><h4>Nombre de tables par lignes</h4></label>
                                        <input type="number" name="larg" id="larg" min="4" max="8" step="1" value="4">
                                    </span>
                                    <button type="submit" id="boutNbPlaces" onclick="">valider</button>
                                    <button type="submit" class="boutWalider" onclick=""> Générer</button>
                                </form>
                    
                            </div>
                    
                        </header>
                    
                    
                        <main>
                            <!-- on va utiliser les maquettes pour faire un truc cool -->
                            <div class="le_Form">
                                <div id="contraintes_impose">
                    
                                    <h2> Places imposées </h2>
                                    <div class="ligne">
                    
                                        <section class="invalid">
                                            <span>
                                                <label for="studentImposed1"> id Etudiant </label>
                                                <input name="idEtuImp1" id="studentImposed1" type="text" disabled></input>
                                            </span>
                                            <span>
                                                <label for="tableImposed1"> Num Table </label>
                                                <input name="idTabImp1" id="tableImposed1" type="number" disabled></input>
                                            </span>
                                            <button class="remove" id="supTabImp1" onclick="enleverPlaceSuppr()" disabled>remove</button>
                                            <button class="chercher" id="imposed1" onclick="validerPlaceImposee()" disabled>find</button>
                                        </section>
                    
                                        <button id="ajoutImpos" class="boutPlus" onclick="createImposed()" disabled >+</button>
                                    </div>
                    
                                </div>
                    
                                <div id="contraintes_suppr">
                    
                                    <h2> Tables Supprimées </h2>
                                    <div class="ligne">
                                        <section class = "invalid">
                    
                                            <span>
                                                <label for="numTabSup1"> Num Table </label>
                                                <input name="idTabSup1" id="numTabSup1" type="number" disabled>
                                            </span>
                                            <button class="remove" id="supTabSup1" onclick="enleverPlaceSuppr()" disabled>remove</button>
                                            <button class="chercher" id="walTabSup1" onclick="validerPlaceSuppr()" disabled>find</button>
                                        </section>
                    
                                        <button id="ajoutSuppr" class="boutPlus" onclick="createSuppr()" disabled >+</button>
                                    </div>
                    
                                </div>
                    
                                <div id="EtuDist">
                    
                                    <h2> Eleves mis a distance  </h2>
                                    <span>
                                        <div class="ligne" id="Gp1">
                                            <section class = "invalid" >
                    
                                                <span>
                                                    <label for="Etu1groupe1"> Num Etudiant </label>
                                                    <input name="idEtu1G1" id="Etu1groupe1" type="number" disabled></input>
                                                </span>
                                                <button class="remove" id="supEtu1G1" onclick="enleverEtuGp()" disabled>remove</button>
                                                <button class="chercher" id="walEtu1G1" onclick="validerEtu()" disabled>find</button>
                                            </section>
                    
                                            <button id="ajoutEtuGrp1" class="boutPlus" onclick="createEtuGrp()" disabled >+</button>
                                            <h4>ajouter un etudiant au groupe</h4>
                    
                                        </div>
                    
                                        <button id="ajoutGroup" class="boutPlus" onclick="createGrp()" disabled >+</button>
                                        <h4>ajouter un groupe</h4>
                                    </span>
                                </div>
                    
                                <button id="hmmm test reussi ?!" value="reussi" onclick="displayID()" disabled></button>
                            </div>
                        </main>
                    
                    
                        <script src = "resources/JS/script.js" ></script>
                    
                    
                    
                    </body>
                    </html>""");

    }
}