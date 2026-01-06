// const constGp=document.querySelector('#mode');
let nbImposedPlace = 1;
let nbPlacesSuppr = 1;
let groupes = [[1]];

tables = 0;

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

document.getElementById("deleteImposed1").addEventListener("click", supprimerPlaceImposee);

function supprimerPlaceImposee(event) {
    let idRemove = event.target.id;
    let numConstr = idRemove.charAt(13);

    console.log(idRemove, numConstr);

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `getStudentName?constraint=${encodeURIComponent("removeImposedPlace")}&id=${encodeURIComponent(numConstr)}`, true);

    if (xhr.readyState === XMLHttpRequest.DONE) {
        if (xhr.status === 200)
            console.log("Deleted constraint successfully");
        else
            console.error("Error deleting constraint");
    }
    xhr.send();

    document.querySelector("#impose" + numConstr).remove();

    nbImposedPlace--;

    if (nbImposedPlace === 0)
        document.querySelector("#ajoutImpos").disabled = false;

    decreaseId("#i");
}

function moveFile() {
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
                console.log("tables enregistred");
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
}

function createSuppr() {
    nbPlacesSuppr++;
    let placesSuppr =
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

}

function createEtuGrp() {
    let numGrp = window.event.target.id.charAt(11);
    groupes[numGrp - 1].push(groupes[numGrp - 1].length);
    let numEtu = groupes[numGrp - 1].length;
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
    console.log()
    document.querySelector(`#ajoutEtuGrp${numGrp}`).insertAdjacentHTML("beforebegin", groupEtu);
    document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = true;
}

//function createTable(){
//     tables++;
//     table= `<section id="T${tables}">
// `
// }

function displayID() {
    console.log(window.event.target.id);
}

function displayValOf(id) {
    console.log(document.querySelector('#' + id));
}

function enableZone() {
    if (fileOk) {
        setTableNumber();
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
        document.querySelector("#idEtu1G1").disabled = false;
        document.querySelector("#nomEtu1G1").disabled = false;
        document.querySelector("#supEtu1G1").disabled = false;
        document.querySelector("#walEtu1G1").disabled = false;

        //le bout generer
        document.querySelector("#walid").style.backgroundColor = '#ec400b';

        const tableReset = new XMLHttpRequest();
        tableReset.open("GET", `table?action=${encodeURIComponent("delete")}`, true);
        tableReset.send();
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

        document.querySelector(`#numTabSup${nbPlacesSuppr}`).disabled = true;
        document.querySelector(`#walTabSup${nbPlacesSuppr}`).disabled = true;

    } else {
        let numGrp = groupes.length;
        if (section.includes(`G${numGrp}`)) {
            document.querySelector("#ajoutGroup").disabled = false;
        } else {
            numGrp = section.charAt(3);
            document.querySelector("#ajoutGroup").disabled = false;
        }
        console.log(numGrp);
        numEtu = groupes[numGrp - 1].length;
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = false;
        document.querySelector(`#idEtu${numEtu}G${numGrp}`).disabled = true;
        document.querySelector(`#nomEtu${numEtu}G${numGrp}`).disabled = true;
        document.querySelector(`#walEtu${numEtu}G${numGrp}`).disabled = true;

    }
    if (document.querySelector(".invalid") == null) {
        document.querySelector("#walid").enabled = true;
        document.querySelector("#walid").backgroundColor = "#1AFF009B";

    }

}

function enleverEtuGrp() {
    let idBout = window.event.target.id;
    let numGrp = idBout.charAt(8);
    let numEtu = idBout.charAt(6);
    console.log(`etu : ${numEtu}, grp : ${numGrp}`);
    if (numEtu == groupes[numGrp - 1].length) {
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = false;
        document.querySelector("#ajoutGroup").disabled = false;
        if (numEtu == 1 && numGrp != 1) {
            document.querySelector(`#Gp${numGrp}`).remove();
            document.querySelector(`#h4${numGrp}`).remove();
            groupes.splice(numGrp - 1, 1);
            return;
        }

    }
    document.querySelector(`#E${numEtu}G${numGrp}`).remove();
    if (numEtu != groupes[numGrp - 1].length) {
        for (let g = 1; g <= groupes[numGrp - 1].length; g++) {
            if (g > numEtu) {
                decreaseId(`#E${g}G${numGrp}`);
            }
        }

    }

    groupes[numGrp - 1].splice(numEtu - 1, 1);

}

function validerSectEtuGrp(idBout) {
    numGrp = idBout.charAt(8);
    numEtu = idBout.charAt(6);
    console.log(`validation de la section : E${numEtu}G${numGrp} `)
    setValid(`E${numEtu}G${numGrp}`);
}

function validerSectImpose(idBout) {
    numConstr = idBout.charAt(11);
    console.log(`validation de la section : impose${numConstr} `)
    setValid(`impose${numConstr}`);

}

function validerPlaceSuppr(idBout) {
    numConstr = idBout.charAt(9);
    console.log(`validation de la section : supTable${numConstr} `);
    setValid(`supTable${numConstr}`);
}

function validerEtuGrp() {
    idFind = window.event.target.id;
    numGrp = idFind.charAt(8);
    numEtu = idFind.charAt(6);
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

function decreaseId(idElem) {
    if (idElem.startsWith("#E")) {
        let numGrp = idElem.charAt(4);
        let numEtu = idElem.charAt(2);
        console.log(numEtu);
        let newNumEtu = numEtu - 1;

        document.querySelector(idElem).id = idElem.charAt(1).concat((idElem.charAt(2) - 1).toString(), idElem.substring(3));

        document.querySelector(`#labelidEtu${numEtu}G${numGrp}`).for = `idEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#labelnomEtu${numEtu}G${numGrp}`).for = `nomEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#idEtu${numEtu}G${numGrp}`).id = `idEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#nomEtu${numEtu}G${numGrp}`).id = `nomEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#walEtu${numEtu}G${numGrp}`).id = `walEtu${newNumEtu}G${numGrp}`;
        document.querySelector(`#supEtu${numEtu}G${numGrp}`).id = `supEtu${newNumEtu}G${numGrp}`;
        console.log("changements effectués");

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
    }


}



