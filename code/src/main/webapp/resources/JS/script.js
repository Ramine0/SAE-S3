// const constGp=document.querySelector('#mode');
nbImposedPlace = 1;
nbPlacesSuppr = 1;
groupes = [[]];

fileOk = false ;


// dans les fonctions javascript a faire il y a :
/*
    generer() ; genere le placement !!!! nessecite les contraintes OK et le fichier OK !!!!!!!!!
    sinon message en rouge "Generation Impossible un numero ne correspond a aucun etudiant " par exemple

    les fonction walider vont etre appelée par les fonctions qui vont valider dans les sections
    string validerEtu(String idPartiel) ; renvoie l'id de l'etu si il existe (on peu donner un id incomplet et le completer si unique
    boolean validerTable() ; dit si la table existe et est pas supprimée

    boolean validerEtuGroup() ; utilise valider Etu pour valider le group
    void validerPlaceImposee() ; utiliser validerEtu et table pour valider la contrainte de place imposee

    (dans l'ideal la section est rouge mais deviens vert si on trouve !!!! pas important c'est apres quand tout marche)


    addToGroup() ; ajoute au groupe l'etudiant trouvé
    setClassMode() ; change le mode de contrainte par classe

    importFichier() ; enregistre le fichier etudiants.csv

*/

document.getElementById("findImposed1").onclick = function () {
    const studentId = document.getElementById("imposedStudentId1").value;

    const idRequest = new XMLHttpRequest();
    idRequest.open("GET", `getStudentName?id=${encodeURIComponent(studentId)}&fieldToFill=${encodeURIComponent("id")}`, true);

    idRequest.onreadystatechange = function () {
        if (idRequest.readyState === XMLHttpRequest.DONE) {
            if (idRequest.status === 200)
                document.getElementById("imposedStudentId").value = idRequest.responseText;
            else
                console.error('Error fetching student data');
        }
    };

    idRequest.send();

    const nameRequest = new XMLHttpRequest();
    nameRequest.open("GET", `getStudentName?id=${encodeURIComponent(studentId)}&fieldToFill=${encodeURIComponent("name")}`, true);

    nameRequest.onreadystatechange = function () {
        if (nameRequest.readyState === XMLHttpRequest.DONE) {
            if (nameRequest.status === 200)
                document.getElementById("imposedStudentName1").value = nameRequest.responseText;
            else
                console.error('Error fetching student data');
        }
    };

    nameRequest.send();
};

function moveFile() {
    const data = new FormData(document.getElementById("fileUploadForm"));

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "file-upload");

    xhr.send(data);

    fileOk = true;
    console.log("no soucy") ;

}

function setTableNumber(){
    const lon = document.getElementById("long");
    const lar = document.getElementById("larg");

    const xhr=new XMLHttpRequest();
    xhr.open("GET", `set-table?long=${encodeURIComponent(lon)}&larg=${encodeURIComponent(lar)}`, true);
    const res=xhr.responseText;
    console.log(res);
}

function validerEtu(idPartiel) {
    console.log(idPartiel);
}

function validerTable() {

}

function validerEtuGroup() {

}

function createImposed() {
    nbImposedPlace++;
    imposedPlace =
        `<section class="invalid">
<span>
    <label for="studentImposed${nbImposedPlace}"> id Etudiant </label>
    <input name="idEtuImp${nbImposedPlace}" id="studentImposed${nbImposedPlace}" type="text" disabled></input>
</span>
<span>
    <label for="tableImposed1"> Num Table </label>
    <input name="idTabImp${nbImposedPlace}" id="tableImposed${nbImposedPlace}" type="number" disabled></input>
</span>
<button class="remove" id="supTabSup${nbImposedPlace}" onclick="enleverPlaceSuppr()" disabled>remove</button>
<button class="chercher" id="imposed${nbImposedPlace}" onclick="validerPlaceImposee()" disabled>find</button>
</section>`;

    document.querySelector('#ajoutImpos').insertAdjacentHTML("beforebegin", imposedPlace);
}


function createSuppr() {
    nbPlacesSuppr++;
    placesSuppr =
        `<section class = "invalid">
<span>
    <label for="numTabSup${nbPlacesSuppr}"> Num Table </label>
    <input name="idTabSup${nbPlacesSuppr}" id="numTabSup${nbPlacesSuppr}" type="number" disabled></input>
</span>
<button class="remove" id="supTabSup${nbPlacesSuppr}" onclick="enleverPlaceSuppr()" disabled>remove</button>
<button class="chercher" id="walTabSup${nbPlacesSuppr}" onclick="validerPlaceSuppr()" disabled>find</button>
</section>`;

    document.querySelector('#ajoutSuppr').insertAdjacentHTML("beforebegin", placesSuppr);
}

function createGrp() {
    groupes.push([]);
    etuGrp = `
    <h4>Mis a distance ${groupes.length} </h4>
    <div class="ligne" id="Gp1">
        <section class = "invalid">
            <span>
                <label for="Etu1groupe${groupes.length}"> Num Etudiant </label>
                <input name="idEtu1G1" id="Etu1groupe${groupes.length}" type="number" disabled></input>
            </span>
            <button class="remove" id="supEtu1G${groupes.length}" onclick="enleverEtuGp()" disabled>remove</button>
            <button class="chercher" id="walEtu1G${groupes.length}" onclick="validerEtu()" disabled>find</button>
        </section>
        <button id="ajoutEtuGrp${groupes.length}" class="boutPlus" onclick="createEtuGrp()" disabled >+</button>
        <h4>ajouter un etudiant au groupe</h4>
    </div>`

    document.querySelector('#ajoutGroup').insertAdjacentHTML("beforebegin", etuGrp);

}

function createEtuGrp() {
    numGrp = window.event.target.id.charAt(11) - 1;
    groupes[numGrp].push(0);
    numEtu = groupes[numGrp].length;
    groupEtu = `<section class = "invalid" >
    <span>
        <label for="Etu${numEtu}groupe${numGrp}"> Num Etudiant </label>
        <input name="idEtu${numEtu}G${numGrp}" id="Etu${numEtu}groupe${numGrp}" type="number" disabled></input>
    </span>
    <button class="remove" id="supEtu${numEtu}G${numGrp}" onclick="enleverEtuGp()" disabled>remove</button>
    <button class="chercher" id="walEtu${numEtu}G${numGrp}" onclick="validerEtu()" disabled>find</button>`
    console.log()
    document.querySelector(`#ajoutEtuGrp${numGrp + 1}`).insertAdjacentHTML("beforebegin", groupEtu);

}

function displayID() {
    console.log(window.event.target.id);
}

function displayValOf(id) {
    console.log(document.querySelector('#' + id));
}

function enableZone() {
    if (fileOk) { // donc j'ai fais des tests, il rentre bien là dedans et il fait les trucs, mais après il actualise
        //on valide les nb de tables
        setTableNumber() ; //autre test fait, ça vient pas de ça, j'ai essayé en mettant en comm et ça règle rien du tout
        // ça vient peut être du bouton, mais je vois pas trop d'où ça viendrait
        //pk le prof pourrait pas modifier après???
        //document.querySelector("#studentFile").disabled = true;
        //document.querySelector("#long").disabled = true;
        //document.querySelector("#larg").disabled = true;

        // imposed
        document.querySelector("#imposedStudentId1").disabled = false;
        document.querySelector("#imposedTableId1").disabled = false;
        document.querySelector("#findImposed1").disabled = false;
        document.querySelector("#imposedStudentName1").disabled = false;
        document.querySelector("#deleteImposed1").disabled = false;

        // deleted
        document.querySelector("#supTabSup1").disabled = false;
        document.querySelector("#numTabSup1").disabled = false;
        document.querySelector("#walTabSup1").disabled = false;

        // groupe
        document.querySelector("#Etu1groupe1").disabled=false;
        document.querySelector("#supEtu1G1").disabled=false;
        document.querySelector("#walEtu1G1").disabled=false;
    }
}