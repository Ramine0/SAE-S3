let nbImposedPlace = 1;
let nbPlacesSuppr = 1;
let groupes = [[1]];
let long = 0;
let larg = 0;
let swap = false;
let tables = []
let noms = []
let active

let fileOk = false;

loadData()

// TO MODIFY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
function validerPlaceImposee(event) {
    let idFind = event.target.id;
    let numConstr = idFind.charAt(11);

    const studentId = document.getElementById(`imposedStudentId${numConstr}`).value;
    const tableNumber = document.getElementById(`imposedTableId${numConstr}`).value;

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
                    const response = xhr.responseText.split(";");
                    if (response[1] === "null")
                        document.getElementById(`imposedStudentName${numConstr}`).value = "Etudiant non trouvé";
                    else if (response[2] === "null")
                        document.getElementById(`imposedStudentName${numConstr}`).value = "Choisissez une table";
                    else if (response[2] === "1")
                        document.getElementById(`imposedStudentName${numConstr}`).value = "Etudiant déjà pris";
                    else if (response[2] === "2")
                        document.getElementById(`imposedStudentName${numConstr}`).value = "Table déjà prise";
                    else if (response[2] === "3")
                        document.getElementById(`imposedStudentName${numConstr}`).value = "Numéro impossible";
                    else if (response[2] === "-1")
                        document.getElementById(`imposedStudentName${numConstr}`).value = "Table supprimée";
                    else {
                        validerSectImpose(idFind);

                        document.getElementById(`imposedStudentId${numConstr}`).value = response[0];
                        document.getElementById(`imposedStudentName${numConstr}`).value = response[1];
                    }
                } else
                    console.error("Error fetching student data");
        }

        xhr.send();
    }
}

window.addEventListener("scroll", () => {
    document.querySelector("footer").style.transform =
        `translateX(${window.scrollX}px)`;
});


document.querySelector("#classMode").addEventListener("change",changeMode)

function changeMode() {
    const m = document.getElementById("classMode").value;
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

function getInfosTable(id) {

    if (swap) {
         activateSwap(id);
    }
    let numTab = id.substring(1);
    let reqInfo = new XMLHttpRequest();
    reqInfo.open("GET", `Display?action=${encodeURIComponent("infos")}&number=${encodeURIComponent(numTab)}`, true);
    reqInfo.onreadystatechange = function () {
        if (reqInfo.readyState === XMLHttpRequest.DONE) {
            if (reqInfo.status === 200) {
                if (reqInfo.responseText !== "null") {
                    const values = reqInfo.responseText.split(";");
                    if (values.length === 4) {
                        document.querySelector("#idTabVisu").value = values[0];
                        document.querySelector("#numEtuVisu").value = values[1];
                        document.querySelector("#nomEtuVisu").value = values[2];
                        document.querySelector("#grpEtuVisu").value = values[3];

                        if (active != null && !swap) {
                            document.querySelector(`#T${active}`).style.backgroundColor = "#cccccc";
                        }
                        document.querySelector(`#T${values[0]}`).style.backgroundColor = "#1AFF009B";

                        active = values[0];

                    }
                }

            }

        }
    };
    reqInfo.send();
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

function exportFile() {
    const excel = document.getElementById("Excel").value;
    console.log(excel);
    const excelRequest = new XMLHttpRequest();
    excelRequest.open("GET", `export?format=${encodeURIComponent(excel)}`);
    excelRequest.responseType = "blob";
    //excelRequest.onreadystatechange=function (){
    excelRequest.onload = function () {
        if (excelRequest.status === 200) {
            const url = window.URL.createObjectURL(excelRequest.response);
            const a = document.createElement("a");
            a.href = url;
            a.download = "listing.csv";
            a.click();
            window.URL.revokeObjectURL(url);
        }
    }
    excelRequest.send();

}


function modeSwap() {
    activateSwap('none');
}

function activateSwap(button) {

    let numt2 = active;
    if (button === "none" && active != null) {
        swap = !swap;
        document.querySelector(`#T${active}`).style.backgroundColor = "rgba(213,192,55,0.82)";
    } else if (document.querySelector(`#${button}`) != null) {

        const swapReq = new XMLHttpRequest();
        swapReq.open("GET", `Display?action=${encodeURIComponent("swap")}&number1=${active}&number2=${button.substring(1)}`);
        swapReq.onreadystatechange = function () {
            if (swapReq.readyState === XMLHttpRequest.DONE) {
                if (swapReq.status === 200) {
                    console.log(numt2)
                    console.log(button)
                    let numt1 = button.substring(1);
                    let nomt1 = noms[tables.indexOf(numt1)];
                    let nomt2 = noms[tables.indexOf(numt2)];

                    let content = `<span><div class="tableNumber">${numt1}</div><img id="deleteT${numt1}" class="deleteT" src="resources/img/delete.png" alt="delete"></span><p>${nomt1}</p>`
                    console.log(document.querySelector(`#T${numt1}`).innerHTML)
                    document.querySelector(`#T${numt1}`).innerHTML = content;

                    console.log(document.querySelector(`#T${numt2}`).innerHTML)
                    content = `<span><div class="tableNumber">${numt2}</div><img id="deleteT${numt2}" class="deleteT" src="resources/img/delete.png" alt="delete"></span><p>${nomt2}</p>`
                    document.querySelector(`#T${numt2}`).innerHTML = content;

                    noms[tables.indexOf(numt1)] = nomt2;
                    noms[tables.indexOf(numt2)] = nomt1;
                    swap = false;
                }
            }
        }
        swapReq.send();
    }

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

    document.querySelector("#ajoutEtuGrp" + groupes.length).addEventListener("click", createEtuGrpFromString);

    document.querySelector("#walid").disabled = true;
    document.querySelector("#walid").style.backgroundColor = '#ec400b';


}

document.getElementById("ajoutEtuGrp1").addEventListener("click", createEtuGrp);

function createEtuGrp(event) {
    createEtuGrpFromString(event.target.id.substring(11))
}

function createEtuGrpFromString(numGrp) {
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
        // les tables
        document.getElementById("visuofDouble").style.visibility = "visible";

        // les groupes
        document.querySelector("#idEtu1G1").disabled = false;
        document.querySelector("#nomEtu1G1").disabled = false;
        document.querySelector("#supEtu1G1").disabled = false;
        document.querySelector("#walEtu1G1").disabled = false;

        //le bout generer
        document.querySelector("#walid").style.backgroundColor = '#ec400b';
        codeForGeneration()

    }
}

function createTables() {
    let t = ""
    let table
    let vals = []
    let name
    let i = 0;

    for (let hei = 1; hei <= size[1]; hei++) {
        t += "<span>"

        for (let wid = 1; wid <= size[0]; wid++) {
            vals = tables[i]

            if (vals.length === 4) {
                if (parseInt(vals[1]) !== wid || parseInt(vals[2]) !== hei) {
                    t += `<button type="button" class="pasTable" disabled > pas Table <br> aucun etu </button>`
                } else {
                    table = vals[0];
                    name = vals[3];
                    t += `<div id="T${table}" class="table" role="button">`;

                    t += "<span>"
                    t += '<div class="tableNumber">' + table + '</div>';
                    t += `<img id="deleteT${table}" class="deleteT" src="resources/img/delete.png" alt="delete">`;

                    t += "</span>"

                    t += '<p>' + name + '</p>'

                    t += '</div>';

                    tables[i] = table;
                    noms[i] = name;
                    i++;
                }
            } else if (vals.length === 2) {


                table = vals[0];
                name = vals[1];
                t += `<button type="button" id="T${table}" class="table" > Table ${table} <br>${name}</button>`;
                tables[i] = table;
                noms[i] = name;
                i++;

            }

            table = table * table;

            if (i >= tables.length)
                break;
        }

        t += "</span>"

        if (i >= tables.length)
            break;
    }

    if (t !== "") {
        document.querySelector("#lesTables").innerHTML = "";
        document.querySelector("#lesTables").insertAdjacentHTML("beforeend", t);

        for (let i = 0; i < tables.length; i++) {
            if (tables[i] !== "")
                document.getElementById("T" + tables[i]).addEventListener("click", handleTable);
        }
    }
    document.getElementById("visuofDouble").style.visibility = "visible";
}

function handleTable(event) {
    if (!event.target.id)
        return

    const element = document.getElementById(event.target.id)
    element.id = element.id.replace(" select", "")

    // suppression de la table
    if (event.target.id.includes("delete")) {

        element.remove();
        document.getElementById("T" + event.target.id.substring(7)).classList.add("deletedT");

        const xhr = new XMLHttpRequest();
        xhr.open("GET", `creation?constraint=${encodeURIComponent("deleteTable")}&tableNumber=${encodeURIComponent(element.id.substring(7))}`)

        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    console.log("table deleted successfully")
                } else {
                    console.log("error deleting table")
                }
            }
        }
        xhr.send();

    } else if (event.target.id.startsWith("T")) { // désuppression de la table
        if (element.children[0].children.length === 1) {
            const table = event.target.id.substring(1);
            element.classList.remove("deletedT");

            const t = `<img id="deleteT${table}" class="deleteT" src="resources/img/delete.png" alt="delete">`;
            element.children[0].insertAdjacentHTML("beforeend", t);

            const xhr = new XMLHttpRequest();
            xhr.open("GET", `creation?constraint=${encodeURIComponent("removeDeletedTable")}&tableNumber=${encodeURIComponent(element.id.substring(1))}`, true)

            xhr.onreadystatechange = function () {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        console.log("table recovered successfully")
                    } else {
                        console.log("error recovering table")
                    }
                }
            }
            xhr.send();
        } else {
            getInfosTable(element.id)
        }
    }
}


function init() {
    let lon = document.getElementById("long");
    let lar = document.getElementById("larg");

    lon.value = Math.min(20, Math.max(0, lon.value));
    lar.value = Math.min(8, Math.max(0, lar.value));

    let planType = document.getElementById("planType").value;

    const initReq = new XMLHttpRequest();

    initReq.open("GET", `creation?action=${encodeURIComponent("define")}&long=${encodeURIComponent(lon)}&larg=${encodeURIComponent(lar)}&planType=${encodeURIComponent(planType)}`, true);

    initReq.onreadystatechange = function () {
        if (initReq.readyState === XMLHttpRequest.DONE) {
            if (initReq.status === 200) {
                tables = []
                let elem = initReq.responseText.split("/");
                if (initReq.responseText !== "rien") {
                    tables = []

                    size = elem[0].split(";");
                    const numbers = elem[1].split(";");

                    for (let i = 0; i < numbers.length - 1; i++)
                        tables.push(numbers[i].split("!"));

                    createTables()
                }

            }
        }

    }
    initReq.send();

}

function setValid(section) {
    if (!section.startsWith("#")) {
        section = "#" + section;
    }
    document.querySelector(section).classList.remove("invalid");
    document.querySelector(section).classList.add("valid");
    if (section.includes("impose")) {
        document.querySelector("#ajoutImpos").disabled = false;

        document.querySelector(`#imposedStudentId${nbImposedPlace}`
        ).disabled = true;
        document.querySelector(
            `#imposedTableId${nbImposedPlace}`
        ).disabled = true;
        document.querySelector(
            `#findImposed${nbImposedPlace}`
        ).disabled = true;
        document.querySelector(
            `#imposedStudentName${nbImposedPlace}`
        ).disabled = true;

    } else if (section.includes("supTable")) {
        document.querySelector("#ajoutSuppr").disabled = false;
        document.querySelector(
            `#numTabSup${nbPlacesSuppr}`
        ).disabled = true;
        document.querySelector(
            `#findTable${nbPlacesSuppr}`
        ).disabled = true;

    } else {
        let numGrp = groupes.length;
        if (section.includes(
            `G${numGrp}`
        )) {
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
        document.querySelector(`#supEtu${numEtu}G${numGrp}`).disabled = false;
    }

    genererWalid();

}

function validerSectEtuGrp(idBout) {
    let numGrp = idBout.substring(8);
    let numEtu = idBout.charAt(6);
    setValid(
        `E${numEtu}G${numGrp}`
    );
}

function validerSectImpose(idBout) {
    let numConstr = idBout.charAt(11);
    setValid(
        `impose${numConstr}`
    );

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
            children[i].children[0].children[2].id = "supEtu" + numEtu + "G" + numGrp;
            children[i].children[0].children[3].id = "walEtu" + numEtu + "G" + numGrp;
        }
    }


}


function codeForGeneration() {

    let code = document.querySelector("#sessionCode");
    const xhr = new XMLHttpRequest();
    xhr.open("GET",
        `creation?generate=${encodeURIComponent("n'importe quoi")}`
        , true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                code.value = xhr.responseText;

                init()
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

function renduFichierEtu(etudiants) {
    window.confirm(etudiants);
}

function loadData() {

    let students = []

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `creation?load=${encodeURIComponent(document.querySelector("#sessionCode").value)}`)
    console.log("recherche des datas de l'utilisateur")
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                if (xhr.responseText !== "null" && ! xhr.responseText.startsWith("null")) {
                    console.log("user exists here are his informations :")
                    console.log(xhr.responseText)

                    let results = xhr.responseText.split("<")

                    if (!results[1] !== "") {
                        results[1].split(";").forEach(student => {
                            students.push(student)
                        })
                        students.pop()

                        for (let i = 1; i <= students.length; i++) {
                            if (document.getElementById("E" + i + "G1") == null) {
                                createEtuGrpFromString(1)
                            }

                            const etuInfos = students[i - 1].split(":")

                            document.getElementById("idEtu" + i + "G1").value = etuInfos[0]
                            document.getElementById("nomEtu" + i + "G1").value = etuInfos[1]

                            setValid("E" + i + "G1")
                        }
                    }

                    tables = []
                    let elem = results[0].split("/");


                    size = elem[0].split(";");
                    const numbers = elem[1].split(";");
                    for (let i = 0; i < numbers.length - 1; i++)
                        tables.push(numbers[i].split("!"));


                    createTables() ;


                    renduFichierEtu(results[2])
                    console.log("fin des informations :")

                } else {
                    console.log("user do not exists")
                    console.log(xhr.responseText)
                    if (document.getElementById("studentFile").files.length !== 0) {
                        fileOk = true;
                        enableZone()
                    }
                }
            } else {
            }
        }
    }

    xhr.send()

    document.querySelector("#modeHeader").selectedIndex = 0
}


document.querySelector("#planType").addEventListener("change", (e) => {
    if (e.target.value === "rectangularPlan") {
        document.getElementById("infoRect").style.visibility = "visible";
        document.getElementById("infoRect").style.height = "fit-content";
    } else {
        document.getElementById("infoRect").style.visibility = "hidden";
        document.getElementById("infoRect").style.height = "0";
    }
})

function tableInfoMod() {
    document.getElementById("valuesOfTable").style.visibility = "visible";
    document.getElementById("valuesOfTable").style.height = "fit-content";
    document.getElementById("parameters").style.visibility = "hidden";
    document.getElementById("parameters").style.height = "0";

}


document.querySelector("#modeHeader").addEventListener("change", changeHeaderMode)

function changeHeaderMode (event) {
    if (event.target.value === "create") {
        document.getElementById("parameters").style.visibility = "visible";
        document.getElementById("parameters").style.height = "100%";
        document.getElementById("valuesOfTable").style.visibility = "hidden";
        document.getElementById("valuesOfTable").style.height = "0";

    }else if (event.target.value === "modify") {
        tableInfoMod() ;
    }else{
        console.log("ALERTE ALERTE")
    }
}


function setTableInfos() {


}


