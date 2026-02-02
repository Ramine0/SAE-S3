let tables=[]
let noms = []
let active ;
let swap = false ;
let size ;

function createTable(){
    let t = ""
    let table
    let vals
    let name
    let i = 0 ;
    for (let hei= 1; hei <= size[1]; hei++){
        t+="<span>"
        for (let wid = 1; wid <= size[0]; wid++) {
            vals = tables[i]
            console.log("i : ",i)
            console.log("val", vals)
            if (vals.length == 4){
                console.log("etape 1")
                if (vals[1] != wid || vals[2] != hei) {
                    console.log("car ",vals[1],vals[2],"differents de ",wid,hei)
                    t += `<button type="button" class="pasTable" disabled > pas Table <br> aucun etu </button>`
                }else {
                    console.log("etape 2")
                    table = vals[0];
                    name = vals[3];
                    t += `<button type="button" id="T${table}" class="table" > Table ${table} <br>${name}</button>`;
                    tables[i] = table;
                    noms[i] = name;
                    i++;

                }
            }else if (vals.length == 2){
                console.log("situation 3")
                table = vals[0];
                name = vals[1];
                t += `<button type="button" id="T${table}" class="table" > Table ${table} <br>${name}</button>`;
                tables[i] = table;
                noms[i] = name;
                i++;

            }
            if (i >= tables.length) {
                break ;
            }

        }
        t += "</span>"
        if (i >= tables.length) {
            break ;
        }
    }
    if (t !== "") {
        document.querySelector("#tableExp").insertAdjacentHTML("beforebegin", t);
        for (let i = 0; i < tables.length; i++) {
            if (tables[i] !== "") {
                document.querySelector(`#T${tables[i]}`).addEventListener("click", getInfosTable);
            }
        }
        if (document.querySelector("#tableExp")!= null ) {
            document.querySelector("#tableExp").remove();
        }
    }

}


function init() {

    const initReq = new XMLHttpRequest();
    initReq.open("GET", `Display?action=${encodeURIComponent("init")}`, true);
    initReq.onreadystatechange = function () {
        if (initReq.readyState === XMLHttpRequest.DONE) {
            if (initReq.status === 200) {
                if (initReq.responseText !== "rien") {

                    let elem = initReq.responseText.split("/")
                    size = elem[0].split(";");
                    console.log(elem[1].split(";"));
                    const numbers = elem[1].split(";");
                    for (let i = 0; i < numbers.length - 1; i++) {
                        tables.push(numbers[i].split("!"));
                    }
                    createTable()
                }
            }

        }
    };

    initReq.send();



}

document.querySelector("#swapForm").addEventListener("click",modeSwap) ;
init() ;

document.querySelector("#exporter").addEventListener("click",exportFile) ;

function getInfosTable(event) {

    if(swap) {
        activateSwap(event.target.id) ;
    }
    let numTab = event.target.id.substring(1) ;
    let reqInfo = new XMLHttpRequest() ;
    reqInfo.open("GET", `Display?action=${encodeURIComponent("infos")}&number=${encodeURIComponent(numTab)}`, true);
    reqInfo.onreadystatechange = function (){
        if (reqInfo.readyState===XMLHttpRequest.DONE){
            if (reqInfo.status===200){
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
    reqInfo.send() ;
}



function exportFile(){
    const excel=document.getElementById("Excel").value;
    console.log(excel);
    const excelRequest=new XMLHttpRequest();
    excelRequest.open("GET", `export?format=${encodeURIComponent(excel)}`);
    excelRequest.responseType="blob";
    //excelRequest.onreadystatechange=function (){
    excelRequest.onload=function (){
        if (excelRequest.status===200){
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
    activateSwap('none') ;
}


function activateSwap(button) {

    let numt2 = active ;
    if (button === "none" && active != null) {
        swap = !swap;
        document.querySelector(`#T${active}`).style.backgroundColor = "rgba(213,192,55,0.82)";
    }else if (document.querySelector(`#${button}`)!= null){

        const swapReq=new XMLHttpRequest();
        swapReq.open("GET", `Display?action=${encodeURIComponent("swap")}&number1=${active}&number2=${button.substring(1)}`);
        swapReq.onreadystatechange=function (){
            if (swapReq.readyState===XMLHttpRequest.DONE){
                if (swapReq.status===200){
                    console.log(numt2)
                    console.log(button)
                    let numt1 = button.substring(1) ;
                    let nomt1 = noms[tables.indexOf(numt1)] ;
                    let nomt2 = noms[tables.indexOf(numt2)] ;

                    let content = ` Table ${numt1} <br>${nomt2}`
                    console.log(document.querySelector(`#T${numt1}`).innerHTML)
                    document.querySelector(`#T${numt1}`).innerHTML = content ;

                    console.log(document.querySelector(`#T${numt2}`).innerHTML)
                    content = ` Table ${numt2} <br>${nomt1}`
                    document.querySelector(`#T${numt2}`).innerHTML = content ;

                    noms[tables.indexOf(numt1)] = nomt2 ;
                    noms[tables.indexOf(numt2)] = nomt1 ;
                    swap = false ;
                }
            }
        }
        swapReq.send() ;
    }

}







