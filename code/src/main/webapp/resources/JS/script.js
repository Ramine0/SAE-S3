// const constGp=document.querySelector('#mode');
let nbImposedPlace = 1;
let nbPlacesSuppr = 1;
let groupes = [[1]];
let long = 0;
let larg = 0;

let tables = 1;

let fileOk = false;

if (document.getElementById("studentFile").files.length !== 0) {
    fileOk = true;
    enableZone();
}


// TO MODIFY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
function validerPlaceImposee(event) {
    let idFind = event.target.id;
    let numConstr = idFind.charAt(11);

    //TO MODIFY  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    if (studentId === "")
        document.getElementById(`imposedStudentName${numConstr}`).value = "Etudiant non trouvé";
    else if (tableNumber === "")
        document.getElementById(`imposedStudentName${numConstr}`).value = "Choisissez une table";
    else {
        const xhr = new XMLHttpRequest();
        xhr.open("GET", `creation?constraint=${encodeURIComponent("imposePlace")}&studentId=${encodeURIComponent(studentId)}&tableNumber=${encodeURIComponent(tableNumber)}`, true);

        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE)
                if (xhr.status === 200) {
                        // TO MODIFY
                } else
                    console.error("Error fetching student data");
        }

        xhr.send();
    }
}

function changeMode() {
    const m = document.getElementById("mode").value;
    const mode = new XMLHttpRequest();
    mode.open("GET", `creation?constraint=${encodeURIComponent("mode")}&mode=${encodeURIComponent(m)}`, true);
    mode.onreadystatechange = function () {
        if (mode.readyState === XMLHttpRequest.DONE) {
            if (mode.status === 200) {
                if (mode.responseText === "error") {
                    console.log("table nulle");
                } else {
                }

            } else {
                console.log(mode.status);
            }
        }
    };
    mode.send();
}


document.getElementById("walEtu1G1").addEventListener("click", validerEtuGrp);

function validerEtuGrp(event) {
    let idFind = event.target.id;
    let numGrp = idFind.substring(8);
    let numEtu = idFind.charAt(6);

    const studentId = document.getElementById(`idEtu${numEtu}G${numGrp}`).value;
    let valid = true;
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `creation?constraint=${encodeURIComponent("separeEtu")}&studentId=${encodeURIComponent(studentId)}&numGrp=${encodeURIComponent(numGrp)}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const response = xhr.responseText.split(";");

                if (response[1] === "1")
                    document.getElementById(`nomEtu${numEtu}G${numGrp}`).value = "Etudiant non trouvé";
                else if (response[1] === "2")
                    document.getElementById(`nomEtu${numEtu}G${numGrp}`).value = "Etudiant déjà pris";
                else {
                    validerSectEtuGrp(idFind);

                    document.getElementById(`nomEtu${numEtu}G${numGrp}`).value = response[1];
                    document.getElementById(`idEtu${numEtu}G${numGrp}`).value = response[0];
                }
            } else {
                console.error('Error fetching group data');
                valid = false;
            }
        }
    };
    xhr.send();
}

document.getElementById("supEtu1G1").addEventListener("click", enleverEtuGrp);

function enleverEtuGrp(event) {
    let idBout = event.target.id;
    let numGrp = idBout.substring(8);
    let numEtu = idBout.charAt(6);

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `creation?constraint=${encodeURIComponent("deleteSepareEtu")}&constraintId=${encodeURIComponent(numEtu + "G" + numGrp)}`);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status !== 200)
                console.log("error deleting etu group");
        }
    };

    xhr.send();

    if (numEtu === groupes[numGrp - 1].length) {
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = false;
        document.querySelector("#ajoutGroup").disabled = false;
        genererWalid();

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

        document.getElementById("ajoutEtuGrp" + numGrp).disabled = false;

    }

    if (numEtu <= groupes[numGrp.length]) {
        groupes[numGrp - 1].splice(numEtu - 1, 1);
    }
    genererWalid();

}

document.getElementById("fileUploadForm").addEventListener("change", moveFile)

function moveFile(event) {
    if (event.target.id !== "studentFile")
        return;
    const data = new FormData(document.getElementById("fileUploadForm"));

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "file-upload");

    xhr.send(data);

    fileOk = true;
}

function setTableNumber() {
    let lon = document.getElementById("long").value;
    let lar = document.getElementById("larg").value;

    let planType = document.getElementById("planType").value;

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `creation?action=${encodeURIComponent("define")}&long=${encodeURIComponent(lon)}&larg=${encodeURIComponent(lar)}&planType=${encodeURIComponent(planType)}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                if (planType === "defaultPlan")
                    console.log("Default plan generated : " + xhr.responseText);
                else if (xhr.responseText !== "-1" || xhr.responseText !== "0") {
                    let l = xhr.responseText.split(";");
                    long = l[0];
                    document.getElementById("long").value = l[0];
                    larg = l[1];
                    document.getElementById("larg").value = l[1];
                }
                document.getElementById("imposedTableId1").max = lon * lar;
                document.getElementById("numTabSup1").max = lon * lar;
            } else {
                console.log("error number tables")
            }
        }
    };
    xhr.send();
}


function createGrp() {
    groupes.push([0]);

    let etuGrp = `
    <h4 id="h4${groupes.length}">Mis à distance ${groupes.length} </h4>
    <div class="ligne" id="Gp${groupes.length}">       
        <section id="E1G${groupes.length}" class = "invalid">
            <section>
                <div>
                    <label for="idEtu1G${groupes.length}">Numéro étudiant</label>
                    <input name="idEtu1G${groupes.length}" id="idEtu1G${groupes.length}" type="text">
                </div>
                <div>
                    <label for="nomEtu1G${groupes.length}">Nom de l'étudiant</label>
                    <input name="nomEtu1G${groupes.length}" id="nomEtu1G${groupes.length}" type="text" >
                </div>
            </section>
            <button class="remove" id="supEtu1G${groupes.length}" >remove</button>
            <button class="chercher" id="walEtu1G${groupes.length}" >find</button>
        </section>
        <button id="ajoutEtuGrp${groupes.length}" class="boutPlus" disabled >+</button>
        <h4>Ajouter un étudiant au groupe</h4>
    </div>`
    document.querySelector('#ajoutGroup').disabled = true;
    document.querySelector('#ajoutGroup').insertAdjacentHTML("beforebegin", etuGrp);

    document.querySelector("#walEtu1G" + groupes.length).addEventListener("click", validerEtuGrp);
    document.querySelector("#supEtu1G" + groupes.length).addEventListener("click", enleverEtuGrp);

    document.querySelector("#ajoutEtuGrp" + groupes.length).addEventListener("click", createEtuGrp);

    document.querySelector("#walid").disabled = true;
    document.querySelector("#walid").style.backgroundColor = '#ec400b';


}

document.getElementById("ajoutEtuGrp1").addEventListener("click", createEtuGrp);

function createEtuGrp(event) {
    let numGrp = event.target.id.substring(11);
    groupes[numGrp - 1].push(groupes[numGrp - 1].length);
    let numEtu = groupes[numGrp - 1].length;
    if (numEtu < 10) {
        let groupEtu = `<section id="E${numEtu}G${numGrp}" class = "invalid" >
        <section>
            <div>
                <label for="idEtu${numEtu}G${numGrp}">Numéro étudiant</label>
                <input name="idEtu${numEtu}G${numGrp}" id="idEtu${numEtu}G${numGrp}" type="text" >
            </div>
            <div>
                <label for="nomEtu${numEtu}G${numGrp}">Nom de l'étudiant</label>
                <input name="nomEtu${numEtu}G${numGrp}" id="nomEtu${numEtu}G${numGrp}" type="text" >
            </div>
        </section>
        <button class="remove" id="supEtu${numEtu}G${numGrp}">remove</button>
        <button class="chercher" id="walEtu${numEtu}G${numGrp}">find</button>`

        document.querySelector(`#ajoutEtuGrp${numGrp}`).insertAdjacentHTML("beforebegin", groupEtu);
        document.querySelector(`#ajoutEtuGrp${numGrp}`).disabled = true;

        document.querySelector("#supEtu" + numEtu + "G" + numGrp).addEventListener("click", enleverEtuGrp);
        document.querySelector("#walEtu" + numEtu + "G" + numGrp).addEventListener("click", validerEtuGrp);

        document.querySelector("#walid").disabled = true;
        document.querySelector("#walid").style.backgroundColor = '#ec400b';

    }
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
        codeForGeneration();
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

    genererWalid();

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
        let numEtu = idElem.charAt(2) - 1;

        let children = document.getElementById("Gp1").children;

        for (let i = 0; i < children.length - 2; i++) {
            children[i].id = "E" + numEtu + "G" + numGrp;

            children[i].children[0].children[0].children[0].for = "idEtu" + numEtu + "G" + numGrp;
            children[i].children[0].children[0].children[1].id = "idEtu" + numEtu + "G" + numGrp;
            children[i].children[0].children[0].children[1].name = "idEtu" + numEtu + "G" + numGrp;

            children[i].children[0].children[1].children[0].for = "nomEtu" + numEtu + "G" + numGrp;
            children[i].children[0].children[1].children[1].id = "nomEtu" + numEtu + "G" + numGrp;
            children[i].children[0].children[1].children[1].name = "nomEtu" + numEtu + "G" + numGrp;
            console.log(children[i].children[0].children[2]);
            children[i].children[0].children[2].id = "supEtu" + numEtu + "G" + numGrp;
            children[i].children[0].children[3].id = "walEtu" + numEtu + "G" + numGrp;
        }
    }


}

function enableText() {
    let code = document.querySelector("#testVal").disabled = false;
}

function codeForGeneration() {

    let code = document.querySelector("#testVal");
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `creation?action=${encodeURIComponent("generate")}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                code.value = xhr.responseText;
                code.disabled = true;
            } else {
            }
        }
    }
    xhr.send();

}

function genererWalid() {
    if (document.getElementsByClassName("invalid").length === 0) {
        document.querySelector("#walid").disabled = false;
        document.querySelector("#walid").style.backgroundColor = "#1AFF009B";
    }
}
