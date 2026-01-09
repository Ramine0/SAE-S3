// const constGp=document.querySelector('#mode');
let nbImposedPlace = 1;
let nbPlacesSuppr = 1;
let groupes = [[1]];
let long=0;
let larg=0;

let tables=1;

let fileOk = false;

if (document.querySelector("#studentFile").files.length !== 0) {
    fileOk = true;
    enableZone();
}

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

document.getElementById("findImposed1").addEventListener("click", validerPlaceImposee);

function validerPlaceImposee(event) {
    let idFind = event.target.id;
    let numConstr = idFind.charAt(11);

    const studentId = document.getElementById(`imposedStudentId${numConstr}`).value;
    const tableNumber = document.getElementById(`imposedTableId${numConstr}`).value;

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `getStudentName?constraint=${encodeURIComponent("imposePlace")}&studentId=${encodeURIComponent(studentId)}&tableNumber=${encodeURIComponent(tableNumber)}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE)
            if (xhr.status === 200) {
                const response = xhr.responseText.split(";");

                if (response[1] === "null")
                    document.getElementById(`imposedStudentName${numConstr}`).value = "Etudiant non trouvé";
                else if (response[2] === "null")
                    document.getElementById(`imposedStudentName${numConstr}`).value = "Choisissez une table";
                else if (response[2] === "1")
                    document.getElementById(`imposedStudentName${numConstr}`).value = "Etudiant déjà pris";
                else if (response[2] === "2")
                    document.getElementById(`imposedStudentName${numConstr}`).value = "Table déjà prise";
                else {
                    validerSectImpose(idFind);

                    document.getElementById(`imposedStudentId${numConstr}`).value = response[0];
                    document.getElementById(`imposedStudentName${numConstr}`).value = response[1];
                }
            } else
                console.error("Error fetching student data");
    };
    xhr.send();
}
function changeMode(){
    const m=document.getElementById("mode").value;
    const mode=new XMLHttpRequest();
    mode.open("GET", `getStudentName?constraints=mode&mode=${encodeURIComponent(m)}`, true);
    mode.onreadystatechange= function(){
        if (mode.readyState===XMLHttpRequest.DONE){
            if (mode.state===200){
                console.log("ça marche à priori");
            }else{
                console.log("Oh helllll naaah");
            }
        }
    };
    mode.send();
}

document.getElementById("deleteImposed1").addEventListener("click", supprimerPlaceImposee);

function supprimerPlaceImposee(event) {
    let idRemove = event.target.id;
    let numConstr = idRemove.charAt(13);

    console.log(idRemove, numConstr);

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `getStudentName?constraint=${encodeURIComponent("removeImposedPlace")}&id=${encodeURIComponent(numConstr)}`, true);

    document.querySelector("#impose" + numConstr).remove();

    nbImposedPlace--;
    document.querySelector("#ajoutImpos").disabled = false;

    decreaseId("#i");

    if (document.querySelector(".invalid") === null) {
        document.querySelector("#walid").disabled = false;
        document.querySelector("#walid").style.backgroundColor = "#1AFF009B";
    }

}

document.getElementById("findTable1").addEventListener("click", validateDeletedTable);

function validateDeletedTable(event) {
    const findId = event.target.id;
    const contraintId = findId.charAt(9);
    const tableNumber = document.getElementById("numTabSup" + contraintId).value;


    if (tableNumber === "")
        return;

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `getStudentName?constraint=${encodeURIComponent("deleteTable")}&tableNumber=${encodeURIComponent(tableNumber)}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                if (xhr.responseText !== "1" && xhr.responseText !== "2") {
                    setValid(`supTable${contraintId}`);
                    console.log("Deleted table successfully");
                }
            } else
                console.error("Error deleting table");
        }
    }
    xhr.send();
}

document.getElementById("deleteTable1").addEventListener("click", removeDeletedTable);

function removeDeletedTable(event) {
    const findId = event.target.id;
    const contraintId = findId.charAt(11);
    const tableNumber = document.getElementById("numTabSup" + contraintId).value;


    if (tableNumber !== "") {
        const xhr = new XMLHttpRequest();
        xhr.open("GET", `getStudentName?constraint=${encodeURIComponent("removeDeletedTable")}&tableNumber=${encodeURIComponent(tableNumber)}`, true);
        xhr.send();
    }

    nbPlacesSuppr--;
    document.querySelector("#ajoutSuppr").disabled = false;

    document.querySelector("#supTable" + contraintId).remove();
    decreaseId("#DT");

    if (document.querySelector(".invalid") === null) {
        document.querySelector("#walid").disabled = false;
        document.querySelector("#walid").style.backgroundColor = "#1AFF009B";
    }

}

function validerEtuGrp() {
    let idFind = window.event.target.id;
    let numGrp = idFind.substring(8);
    let numEtu = idFind.charAt(6);

    const studentId = document.getElementById(`idEtu${numEtu}G${numGrp}`).value;
    let valid = true;
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `getStudentName?constraint=${encodeURIComponent("separeEtu")}&id=${encodeURIComponent(studentId)}&numGrp=${encodeURIComponent(numGrp)}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const response = xhr.responseText.split(";");

                console.log("rep :",response);

                document.getElementById(`nomEtu${numEtu}G${numGrp}`).value = response[0];
                if (response.length > 1) {
                    validerSectEtuGrp(idFind);
                    document.getElementById(`idEtu${numEtu}G${numGrp}`).value = response[0];
                    document.getElementById(`nomEtu${numEtu}G${numGrp}`).value = response[1];
                }
            } else {
                console.error('Error fetching group data');
                valid = false;
            }
        }
    };
    xhr.send();
}

function enleverEtuGrp() {
    let idBout = window.event.target.id;
    let numGrp = idBout.substring(8);
    let numEtu = idBout.charAt(6);
    if (numEtu === groupes[numGrp - 1].length) {
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = false;
        document.querySelector("#ajoutGroup").disabled = false;
        if (document.querySelector(".invalid") === null) {
            document.querySelector("#walid").disabled = false;
            document.querySelector("#walid").style.backgroundColor = "#1AFF009B";
        }
        if (numEtu === 1 && numGrp !== 1) {
            document.querySelector(`#Gp${numGrp}`).remove();
            document.querySelector(`#h4${numGrp}`).remove();
            groupes.splice(numGrp - 1, 1);

            return;
        }

    }
    document.querySelector(`#E${numEtu}G${numGrp}`).remove();
    if (numEtu !== groupes[numGrp - 1].length) {
        for (let g = 1; g <= groupes[numGrp - 1].length; g++) {
            if (g > numEtu) {
                decreaseId(`#E${g}G${numGrp}`);
            }
        }

    }

    if (numEtu <= groupes[numGrp.length]) {
        groupes[numGrp - 1].splice(numEtu - 1, 1);
        if (document.querySelector(".invalid") === null) {
            document.querySelector("#walid").disabled = false;
            document.querySelector("#walid").style.backgroundColor = "#1AFF009B";
        }
    }

}

document.getElementById("fileUploadForm").addEventListener("change", moveFile)

function moveFile(event) {
    if (event.target.id !== "studentFile")
        return;
    const data = new FormData(document.getElementById("fileUploadForm"));

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "file-upload");

    xhr.send(data);

    console.log("File uploaded. I guess...")

    fileOk = true;
}

function setTableNumber() {
    const lon = document.getElementById("long").value;
    const lar = document.getElementById("larg").value;

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `table?action=${encodeURIComponent("define")}&long=${encodeURIComponent(lon)}&larg=${encodeURIComponent(lar)}`, true);
    console.log(xhr.responseText);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                console.log("tables saved");
                long=lon;
                larg=lar;
            } else {
                console.log("error number tables")
            }
        }
    };
    xhr.send();
}


function createImposed() {
    nbImposedPlace++;
    let imposedPlace =
        `<section id="impose${nbImposedPlace}" class="invalid">
<span>
    <label for="studentImposedId${nbImposedPlace}"> id Etudiant </label>
    <input name="idEtuImp${nbImposedPlace}" id="imposedStudentId${nbImposedPlace}" type="text" >
</span>
<span>
    <label for="imposedTableId${nbImposedPlace}"> Num Table </label>
    <input name="idTabImp${nbImposedPlace}" id="imposedTableId${nbImposedPlace}" type="number" >
</span>
<span>
    <label for="imposedStudentName${nbImposedPlace}"> Nom de l'étudiant </label>
    <input name="idStudentImp${nbImposedPlace}" id="imposedStudentName${nbImposedPlace}" type="text" >
</span>
<button class="remove" id="deleteImposed${nbImposedPlace}">remove</button>
<button class="chercher" id="findImposed${nbImposedPlace}">find</button>
</section>`;

    document.querySelector('#ajoutImpos').insertAdjacentHTML("beforebegin", imposedPlace);
    document.querySelector("#ajoutImpos").disabled = true;

    document.querySelector("#findImposed" + nbImposedPlace).addEventListener("click", validerPlaceImposee);
    document.querySelector("#deleteImposed" + nbImposedPlace).addEventListener("click", supprimerPlaceImposee);

    document.querySelector("#walid").disabled = true;
    document.querySelector("#walid").style.backgroundColor = '#ec400b';


}

function createSuppr() {
    nbPlacesSuppr++;
    let placesSuppr =
        `<section id="supTable${nbPlacesSuppr}" class = "invalid">
<span>
    <label for="numTabSup${nbPlacesSuppr}"> Num Table </label>
    <input name="idTabSup${nbPlacesSuppr}" id="numTabSup${nbPlacesSuppr}" type="number">
</span>
<button class="remove" id="deleteTable${nbPlacesSuppr}">remove</button>
<button class="chercher" id="findTable${nbPlacesSuppr}">find</button>
</section>`;

    document.querySelector("#ajoutSuppr").disabled = true;
    document.querySelector('#ajoutSuppr').insertAdjacentHTML("beforebegin", placesSuppr);

    document.querySelector("#findTable" + nbPlacesSuppr).addEventListener("click", validateDeletedTable);
    document.querySelector("#deleteTable" + nbPlacesSuppr).addEventListener("click", removeDeletedTable);

    document.querySelector("#walid").disabled = true;
    document.querySelector("#walid").style.backgroundColor = '#ec400b';


}

function createGrp() {
    groupes.push([0]);
    let etuGrp = `
    <h4 id="h4${groupes.length}">Mis a distance ${groupes.length} </h4>
    <div class="ligne" id="Gp${groupes.length}">       
        <section id="E1G${groupes.length}" class = "invalid">
            <span>
                <div>
                    <label for="idEtu1G${groupes.length}"> Num Etudiant </label>
                    <input name="idEtu1G${groupes.length}" id="idEtu1G${groupes.length}" type="text">
                </div>
                <div>
                    <label for="nomEtu1G${groupes.length}"> Nom de l'étudiant </label>
                    <input name="nomEtu1G${groupes.length}" id="nomEtu1G${groupes.length}" type="text" >
                </div>
            </span>
            <button class="remove" id="supEtu1G${groupes.length}" onclick="enleverEtuGrp()" >remove</button>
            <button class="chercher" id="walEtu1G${groupes.length}" onclick="validerEtuGrp('waletu1G${groupes.length}')" >find</button>
        </section>
        <button id="ajoutEtuGrp${groupes.length}" class="boutPlus" onclick="createEtuGrp()"  disabled >+</button>
        <h4>ajouter un etudiant au groupe</h4>
    </div>`

    document.querySelector('#ajoutGroup').insertAdjacentHTML("beforebegin", etuGrp);
    document.querySelector('#ajoutGroup').disabled = true;

    document.querySelector("#walid").disabled = true;
    document.querySelector("#walid").style.backgroundColor = '#ec400b';


}

function createEtuGrp() {
    let numGrp = window.event.target.id.substring(11);
    groupes[numGrp - 1].push(groupes[numGrp - 1].length);
    let numEtu = groupes[numGrp - 1].length;
    if (numEtu < 10) {
        let groupEtu = `<section id="E${numEtu}G${numGrp}" class = "invalid" >
        <span>
            <div>
                <label for="idEtu${numEtu}G${numGrp}" id="labelidEtu${numEtu}G${numGrp}"> Num Etudiant </label>
                <input name="idEtu${numEtu}G${numGrp}" id="idEtu${numEtu}G${numGrp}" type="text" >
            </div>
            <div>
                <label for="nomEtu${numEtu}G${numGrp}" id="labelnomEtu${numEtu}G${numGrp}"> Nom de l'étudiant </label>
                <input name="nomEtu${numEtu}G${numGrp}" id="nomEtu${numEtu}G${numGrp}" type="text" >
            </div>
        </span>
        <button class="remove" id="supEtu${numEtu}G${numGrp}" onclick="enleverEtuGrp()" >remove</button>
        <button class="chercher" id="walEtu${numEtu}G${numGrp}" onclick="validerEtuGrp('walEtu${numEtu}G${numGrp}')" >find</button>`

        document.querySelector(`#ajoutEtuGrp${numGrp}`).insertAdjacentHTML("beforebegin", groupEtu);
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = true;

        document.querySelector("#walid").disabled = true;
        document.querySelector("#walid").style.backgroundColor = '#ec400b';

    }
}

function createTable(){
    tables++;
    let t= `<button id="T${tables}" class="table"> Table ${tables} </button>`;
    if (tables%larg===0){
        t+=`<br><p id="endLine${tables/larg+1}">`;
    }
    document.querySelector(`#endLine${tables/larg+1}`).insertAdjacentHTML("beforebegin", t);
}

function displayID() {
    console.log(window.event.target.id);
}


function enableZone() {
    if (fileOk) {
        setTableNumber();
        changeMode();
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
        document.querySelector("#findTable1").disabled = false;
        document.querySelector("#numTabSup1").disabled = false;
        document.querySelector("#deleteTable1").disabled = false;

        // groupe
        document.querySelector("#idEtu1G1").disabled = false;
        document.querySelector("#nomEtu1G1").disabled = false;
        document.querySelector("#supEtu1G1").disabled = false;
        document.querySelector("#walEtu1G1").disabled = false;

        //le bout generer
        document.querySelector("#walid").style.backgroundColor = '#ec400b';

        const tableReset = new XMLHttpRequest();
        tableReset.open("GET", `table?action=${encodeURIComponent("delete")}`, true);
        tableReset.send();
        codeForGeneration() ;
    }
}

function setValid(section) {
    if (!section.startsWith("#")) {
        section = "#" + section;
    }
    document.querySelector(section).classList.remove("invalid");
    document.querySelector(section).classList.add("valid");
    if (section.includes("impose")) {
        document.querySelector("#ajoutImpos").disabled = false;

        document.querySelector(`#imposedStudentId${nbImposedPlace}`).disabled = true;
        document.querySelector(`#imposedTableId${nbImposedPlace}`).disabled = true;
        document.querySelector(`#findImposed${nbImposedPlace}`).disabled = true;
        document.querySelector(`#imposedStudentName${nbImposedPlace}`).disabled = true;

    } else if (section.includes("supTable")) {
        document.querySelector("#ajoutSuppr").disabled = false;

        document.querySelector(`#findTable${nbPlacesSuppr}`).disabled = true;

    } else {
        let numGrp = groupes.length;
        if (section.includes(`G${numGrp}`)) {
            document.querySelector("#ajoutGroup").disabled = false;
        } else {
            numGrp = section.substring(4);
        }


        let numEtu = groupes[numGrp - 1].length;

        if (numEtu < 9) {
            document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = false;
        }
        document.querySelector(`#idEtu${numEtu}G${numGrp}`).disabled = true;
        document.querySelector(`#nomEtu${numEtu}G${numGrp}`).disabled = true;
        document.querySelector(`#walEtu${numEtu}G${numGrp}`).disabled = true;
    }

    if (document.querySelector(".invalid") === null) {
        document.querySelector("#walid").disabled = false;
        document.querySelector("#walid").style.backgroundColor = "#1AFF009B";

    }

}

function validerSectEtuGrp(idBout) {
    let numGrp = idBout.substring(8);
    let numEtu = idBout.charAt(6);
    setValid(`E${numEtu}G${numGrp}`);
}

function validerSectImpose(idBout) {
    let numConstr = idBout.charAt(11);
    setValid(`impose${numConstr}`);

}

function decreaseId(idElem) {
    if (idElem.startsWith("#E")) {
        let numGrp = idElem.substring(4);
        let numEtu = idElem.charAt(2);
        let newNumEtu = numEtu - 1;

        document.querySelector(idElem).id = idElem.charAt(1).concat((idElem.charAt(2) - 1).toString(), idElem.substring(3));

        document.querySelector(`#labelidEtu${numEtu}G${numGrp}`).for = `idEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#labelnomEtu${numEtu}G${numGrp}`).for = `nomEtu${newNumEtu}G${numGrp}`;

        document.querySelector(`#labelidEtu${numEtu}G${numGrp}`).id = `labelidEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#labelnomEtu${numEtu}G${numGrp}`).id = `labelnomEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#idEtu${numEtu}G${numGrp}`).id = `idEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#nomEtu${numEtu}G${numGrp}`).id = `nomEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#walEtu${numEtu}G${numGrp}`).id = `walEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#supEtu${numEtu}G${numGrp}`).id = `supEtu${newNumEtu}G${numGrp}`;

    } else if (idElem.startsWith("#i")) {
        let children = document.getElementById("ligneImposed").children;

        for (let i = 0; i < children.length - 1; i++) {
            const newId = i + 1;

            children[i].id = "impose" + newId;

            children[i].children[0].children[1].id = "imposedStudentId" + newId;
            children[i].children[1].children[1].id = "imposedTableId" + newId;
            children[i].children[2].children[1].id = "imposedStudentName" + newId;

            children[i].children[3].id = "deleteImposed" + newId;
            children[i].children[4].id = "findImposed" + newId;
        }
    } else if (idElem.startsWith("#DT")) {
        let children = document.getElementById("deletedTableRow").children;

        for (let i = 0; i < children.length - 1; i++) {
            const newId = i + 1;

            children[i].id = "supTable" + newId;

            children[i].children[0].children[1].id = "numTabSup" + newId;

            children[i].children[1].id = "deleteTable" + newId;
            children[i].children[2].id = "findTable" + newId;
        }
    }


}

function enableText () {
    let code = document.querySelector("#testVal").disabled = false ;
}

function codeForGeneration (){

    let code = document.querySelector("#testVal");
    console.log(code.id) ;
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `table?action=${encodeURIComponent("generate")}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                code.value = xhr.responseText;
                code.disabled = true ;
            }else {
                console.log("ca ... marche pas ....") ;
            }
        }
    }
    xhr.send();

}



