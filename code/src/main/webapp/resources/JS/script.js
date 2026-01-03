// const constGp=document.querySelector('#mode');
nbImposedPlace = 1;
nbPlacesSuppr = 1;
groupes = [[1]];

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
    const tableId = document.getElementById("imposedTableId1").value;
    valid=true;
    const idRequest = new XMLHttpRequest();
    idRequest.open("GET", `getStudentName?constraint=${encodeURIComponent("imposePlace")}&id=${encodeURIComponent(studentId)}&fieldToFill=${encodeURIComponent("id")}`, true);

    idRequest.onreadystatechange = function () {
        if (idRequest.readyState === XMLHttpRequest.DONE) {
            if (idRequest.status === 200) {
                document.getElementById("imposedStudentId1").value = idRequest.responseText;
            }else{
                console.error('Error fetching student data');
                valid=false;
            }
        }
    };

    idRequest.send();
    const nameRequest = new XMLHttpRequest();
    nameRequest.open("GET", `getStudentName?constraint=${encodeURIComponent("imposePlace")}&id=${encodeURIComponent(studentId)}&fieldToFill=${encodeURIComponent("name")}`, true);

    nameRequest.onreadystatechange = function () {
        if (nameRequest.readyState === XMLHttpRequest.DONE) {
            if (nameRequest.status === 200) {
                document.getElementById("imposedStudentName1").value = nameRequest.responseText;
            }else {
                console.error('Error fetching student data');
                valid = false;
            }
        }
    };
    nameRequest.send();
    const tableVerif=new XMLHttpRequest();
    tableVerif.open("GET", `table?action=${encodeURIComponent("present")}&table=${encodeURIComponent(tableId)}`, true);
    if (valid && tableVerif.responseText==="valide"){
        console.log("Tout est bon");
    }else{
        console.log("PROBLEME");
    }
};

function moveFile() {
    const data = new FormData(document.getElementById("fileUploadForm"));

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "file-upload");

    xhr.send(data);

    fileOk = true;
}

function setTableNumber(){
    const lon = document.getElementById("long");
    const lar = document.getElementById("larg");

    const xhr=new XMLHttpRequest();
    xhr.open("GET", `table?action=${encodeURIComponent("define")}&long=${encodeURIComponent(lon)}&larg=${encodeURIComponent(lar)}`, true);
    console.log(xhr.responseText);
}

function validerEtu(idPartiel) {
    console.log(idPartiel);
}


function createImposed() {
    nbImposedPlace++;
    imposedPlace =
        `<section class="invalid">
<span>
    <label for="studentImposed${nbImposedPlace}"> id Etudiant </label>
    <input name="idEtuImp${nbImposedPlace}" id="studentImposed${nbImposedPlace}" type="text" ></input>
</span>
<span>
    <label for="tableImposed1"> Num Table </label>
    <input name="idTabImp${nbImposedPlace}" id="tableImposed${nbImposedPlace}" type="number" ></input>
</span>
<button class="remove" id="supTabSup${nbImposedPlace}" onclick="enleverPlaceSuppr()" >remove</button>
<button class="chercher" id="imposed${nbImposedPlace}" onclick="validerPlaceImposee()" >find</button>
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
<button class="remove" id="supTabSup${nbPlacesSuppr}" onclick="enleverPlaceSuppr()" >remove</button>
<button class="chercher" id="walTabSup${nbPlacesSuppr}" onclick="validerPlaceSuppr()" >find</button>
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
            <button class="remove" id="supEtu1G${groupes.length}" onclick="enleverEtuGrp()" >remove</button>
            <button class="chercher" id="walEtu1G${groupes.length}" onclick="validerEtu()" >find</button>
        </section>
        <button id="ajoutEtuGrp${groupes.length}" class="boutPlus" onclick="createEtuGrp()"  >+</button>
        <h4>ajouter un etudiant au groupe</h4>
    </div>`

    document.querySelector('#ajoutGroup').insertAdjacentHTML("beforebegin", etuGrp);
    document.querySelector('#ajoutGroup').disabled = true ;

}

function createEtuGrp() {
    numGrp = window.event.target.id.charAt(11) - 1;
    groupes[numGrp].push(0);
    numEtu = groupes[numGrp].length;
    groupEtu = `<section class = "invalid" >
    <span>
        <label for="Etu${numEtu}groupe${numGrp}"> Num Etudiant </label>
        <input name="idEtu${numEtu}G${numGrp}" id="Etu${numEtu}groupe${numGrp}" type="number" ></input>
    </span>
    <button class="remove" id="supEtu${numEtu}G${numGrp}" onclick="enleverEtuGrp()" >remove</button>
    <button class="chercher" id="walEtu${numEtu}G${numGrp}" onclick="validerEtu()" >find</button>`
    console.log()
    document.querySelector(`#ajoutEtuGrp${numGrp + 1}`).insertAdjacentHTML("beforebegin", groupEtu);
    document.querySelector(`#ajoutEtuGrp${numGrp + 1}`).disabled = true  ;
}

function displayID() {
    console.log(window.event.target.id);
}

function displayValOf(id) {
    console.log(document.querySelector('#' + id));
}

function enableZone() {
    if (fileOk) {
        setTableNumber() ;
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

        //le bout generer
        document.querySelector("#walid").style.backgroundColor = '#ec400b' ;

    }
}

function setValid(section) {
    if (! section.startsWith("#")) {
        section = "#"+section ;
    }
    document.querySelector(section).classList.remove("invalid") ;
    document.querySelector(section).classList.add("valid") ;
    if (section.includes("impose")) {
        document.querySelector("#ajoutImpos").disabled = false;

        document.querySelector(`#imposedStudentId${nbImposedPlace}`).disabled = true;
        document.querySelector(`#imposedTableId${nbImposedPlace}`).disabled = true;
        document.querySelector(`#findImposed${nbImposedPlace}`).disabled = true;
        document.querySelector(`#imposedStudentName${nbImposedPlace}`).disabled = true;

    }else if (section.includes("supTable")) {
        document.querySelector("#ajoutSuppr").disabled = false ;

        document.querySelector(`#numTabSup${nbPlacesSuppr}`).disabled = true;
        document.querySelector(`#walTabSup${nbPlacesSuppr}`).disabled = true;

    }else {
        numGrp = groupes.length ;
        if (section.includes(`G${numGrp}`)) {
            document.querySelector("#ajoutGroup").disabled = false ;
        }else {
            numGrp = section.charAt(3) ;
        }

        numEtu = groupes[numGrp-1].length ;
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled =false ;
        console.log(`desactivation de l'etu : #Etu${numEtu}groupe${numGrp}`)
        document.querySelector(`#Etu${numEtu}groupe${numGrp}`).disabled=true;
        document.querySelector(`#walEtu${numEtu}G${numGrp}`).disabled=true;
        
    }

}

function enleverEtuGrp() {

    numGrp = window.event.target.id.charAt(8) ;
    numEtu = window.event.target.id.charAt(6) ;
    console.log(`validation de la section : E${numEtu}G${numGrp} `)

    setValid(`E${numEtu}G${numGrp}`) ;
}

