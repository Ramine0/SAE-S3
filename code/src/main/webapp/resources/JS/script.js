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

function validerPlaceImposee()  {
    idFind = window.event.target.id ;
    numConstr = idFind.charAt(11) ;
    console.log(idFind,numConstr) ;
    const studentId = document.getElementById(`imposedStudentId${numConstr}`).value;
    const tableId = document.getElementById(`imposedTable${numConstr}`).value;
    valid=true;
    const idRequest = new XMLHttpRequest();
    idRequest.open("GET", `getStudentName?constraint=${encodeURIComponent("imposePlace")}&id=${encodeURIComponent(studentId)}&fieldToFill=${encodeURIComponent("id")}`, true);

    idRequest.onreadystatechange = function () {
        if (idRequest.readyState === XMLHttpRequest.DONE) {
            if (idRequest.status === 200) {
                document.getElementById(`imposedStudentId${numConstr}`).value = idRequest.responseText;
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
                document.getElementById(`imposedStudentName${numConstr}`).value = nameRequest.responseText;
                validerSectImpose(idFind) ;
            }else {
                console.error('Error fetching student data');
                valid = false;
            }
        }
    };
    nameRequest.send();
    const tableVerif=new XMLHttpRequest();
    tableVerif.open("GET", `table?action=${encodeURIComponent("present")}&table=${encodeURIComponent(tableId)}`, true);
    tableVerif.send();
    if (valid && tableVerif.responseText==="valide"){
        console.log("Tout est bon");
    }else{
        console.log("PROBLEME");
    }
}

function moveFile() {
    const data = new FormData(document.getElementById("fileUploadForm"));

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "file-upload");

    xhr.send(data);

    fileOk = true;
}

function setTableNumber(){
    const lon = document.getElementById("long").value;
    const lar = document.getElementById("larg").value;

    const xhr=new XMLHttpRequest();
    xhr.open("GET", `table?action=${encodeURIComponent("define")}&long=${encodeURIComponent(lon)}&larg=${encodeURIComponent(lar)}`, true);
    console.log(xhr.responseText);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                document.getElementById(`imposedStudentId${numConstr}`).value = idRequest.responseText;
            }else{
                console.error('Error fetching student data');
            }
        }
    };
    xhr.send();
}



function createImposed() {
    nbImposedPlace++;
    imposedPlace =
        `<section id="impose${nbImposedPlace}" class="invalid">
<span>
    <label for="studentImposedId${nbImposedPlace}"> id Etudiant </label>
    <input name="idEtuImp${nbImposedPlace}" id="imposedStudentId${nbImposedPlace}" type="text" >
</span>
<span>
    <label for="imposedTable${nbImposedPlace}"> Num Table </label>
    <input name="idTabImp${nbImposedPlace}" id="imposedTable${nbImposedPlace}" type="number" >
</span>
<span>
    <label for="imposedStudentName${nbImposedPlace}"> Nom de l'étudiant </label>
    <input name="idStudentImp${nbImposedPlace}" id="imposedStudentName${nbImposedPlace}" type="text" >
</span>
<button class="remove" id="supTabSup${nbImposedPlace}" onclick="enleverPlaceSuppr()" >remove</button>
<button class="chercher" id="findImposed${nbImposedPlace}" onclick="validerPlaceImposee()" >find</button>
</section>`;

    document.querySelector('#ajoutImpos').insertAdjacentHTML("beforebegin", imposedPlace);
    document.querySelector("#ajoutImpos").disabled = true ;
}


function createSuppr() {
    nbPlacesSuppr++;
    placesSuppr =
        `<section id="supTable${nbPlacesSuppr}" class = "invalid">
<span>
    <label for="numTabSup${nbPlacesSuppr}"> Num Table </label>
    <input name="idTabSup${nbPlacesSuppr}" id="numTabSup${nbPlacesSuppr}" type="number" disabled>
</span>
<button class="remove" id="supTabSup${nbPlacesSuppr}" onclick="enleverPlaceSuppr()" >remove</button>
<button class="chercher" id="walTabSup${nbPlacesSuppr}" onclick="validerPlaceSuppr()" >find</button>
</section>`;

    document.querySelector('#ajoutSuppr').insertAdjacentHTML("beforebegin", placesSuppr);
}

function createGrp() {
    groupes.push([0]);
    etuGrp = `
    <h4>Mis a distance ${groupes.length} </h4>
    <div class="ligne" id="Gp${groupes.length}">
        <section id="E1G${groupes.length}" class = "invalid">
            <span>
                <label for="idEtu1G${groupes.length}"> Num Etudiant </label>
                <input name="idEtu1G${groupes.length}" id="idEtu1G${groupes.length}" type="text">
                <label for="nomEtu1G${groupes.length}"> Nom de l'étudiant </label>
                <input name="nomEtu1G${groupes.length}" id="numEtu1G${groupes.length}" type="text" >
            </span>
            <button class="remove" id="supEtu1G${groupes.length}" onclick="enleverEtuGrp()" >remove</button>
            <button class="chercher" id="walEtu1G${groupes.length}" onclick="validerEtuGrp('waletu1G${groupes.length}')" >find</button>
        </section>
        <button id="ajoutEtuGrp${groupes.length}" class="boutPlus" onclick="createEtuGrp()"  disabled >+</button>
        <h4>ajouter un etudiant au groupe</h4>
    </div>`

    document.querySelector('#ajoutGroup').insertAdjacentHTML("beforebegin", etuGrp);
    document.querySelector('#ajoutGroup').disabled = true ;

}

function createEtuGrp() {
    numGrp = window.event.target.id.charAt(11);
    groupes[numGrp-1].push(0);
    numEtu = groupes[numGrp-1].length;
    groupEtu = `<section id="E${numEtu}G${numGrp}" class = "invalid" >
    <span>
        <label for="Etu${numEtu}groupe${numGrp}"> Num Etudiant </label>
        <input name="idEtu${numEtu}G${numGrp}" id="Etu${numEtu}groupe${numGrp}" type="text" >
        <label for="nomEtu${numEtu}G${numGrp}"> Nom de l'étudiant </label>
        <input name="nomEtu${numEtu}G${numGrp}" id="numEtu${numEtu}G${numGrp}" type="text" >
    </span>
    <button class="remove" id="supEtu${numEtu}G${numGrp}" onclick="enleverEtuGrp()" >remove</button>
    <button class="chercher" id="walEtu${numEtu}G${numGrp}" onclick="validerEtuGrp('walEtu${numEtu}G${numGrp}')" >find</button>`
    console.log()
    document.querySelector(`#ajoutEtuGrp${numGrp}`).insertAdjacentHTML("beforebegin", groupEtu);
    document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = true  ;
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
        document.querySelector("#imposedTable1").disabled = false;
        document.querySelector("#findImposed1").disabled = false;
        document.querySelector("#imposedStudentName1").disabled = false;
        document.querySelector("#deleteImposed1").disabled = false;

        // deleted
        document.querySelector("#supTabSup1").disabled = false;
        document.querySelector("#numTabSup1").disabled = false;
        document.querySelector("#walTabSup1").disabled = false;

        // groupe
        document.querySelector("#idEtu1G1").disabled=false;
        document.querySelector("#nomEtu1G1").disabled=false;
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
        document.querySelector(`#imposedTable${nbImposedPlace}`).disabled = true;
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
            numGrp = section.charAt(4) ;
        }
        console.log(numGrp) ; // !!!!!!!!!!!!!!!!!!!!!erreur avec bcp
        numEtu = groupes[numGrp-1].length ;
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled =false ;
        document.querySelector(`#idEtu${numEtu}G${numGrp}`).disabled=true;
        document.querySelector(`#nomEtu${numEtu}G${numGrp}`).disabled=true;
        document.querySelector(`#walEtu${numEtu}G${numGrp}`).disabled=true;
        
    }

}

function enleverEtuGrp() {


}

function validerSectEtuGrp(idBout) {
    numGrp = idBout.charAt(8) ;
    numEtu = idBout.charAt(6) ;
    console.log(`validation de la section : E${numEtu}G${numGrp} `)
    setValid(`E${numEtu}G${numGrp}`) ;
}

function validerSectImpose(idBout) {
    numConstr = idBout.charAt(11) ;
    console.log(`validation de la section : impose${numConstr} `)
    setValid(`impose${numConstr}`) ;

}

function validerPlaceSuppr(idBout) {
    numConstr = idBout.charAt(9) ;
    console.log(`validation de la section : supTable${numConstr} `) ;
    setValid(`supTable${numConstr}`) ;
}


function validerEtuGrp() {
    idFind = window.event.target.id;
    numGrp = idFind.charAt(8) ;
    numEtu = idFind.charAt(6) ;
    console.log(idFind, numEtu, numGrp);
    const studentId = document.getElementById(`idEtu${numEtu}G${numGrp}`).value;
    const studentName = document.getElementById(`nomEtu${numEtu}G${numGrp}`).value;
    valid = true;
    const idRequest = new XMLHttpRequest();
    idRequest.open("GET", `getStudentName?constraint=${encodeURIComponent("separeEtu")}&id=${encodeURIComponent(studentId)}&fieldToFill=${encodeURIComponent("id")}`, true);

    idRequest.onreadystatechange = function () {
        if (idRequest.readyState === XMLHttpRequest.DONE) {
            if (idRequest.status === 200) {
                document.getElementById(`idEtu${numEtu}G${numGrp}`).value = idRequest.responseText;
            } else {
                console.error('Error fetching student data');
                valid = false;
            }
        }
    };

    idRequest.send();
    const nameRequest = new XMLHttpRequest();
    nameRequest.open("GET", `getStudentName?constraint=${encodeURIComponent("separeEtu")}&id=${encodeURIComponent(studentId)}&fieldToFill=${encodeURIComponent("name")}`, true);

    nameRequest.onreadystatechange = function () {
        if (nameRequest.readyState === XMLHttpRequest.DONE) {
            if (nameRequest.status === 200) {
                document.getElementById(`nomEtu${numEtu}G${numGrp}`).value = nameRequest.responseText;
                validerSectEtuGrp(idFind);
            } else {
                console.error('Error fetching student data');
                valid = false;
            }
        }
    };
    nameRequest.send();

}





