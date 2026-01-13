let tables=[]
let noms = []
let active ;
let swap = false ;
//import {long, larg} from "./script.js";

function createTable(){
    let t = "" ;
    let table ;
    let vals ;
    let name ;
    for (let i = 0; i < tables.length; i++ ){
        vals = tables[i].split("!") ;
        table = vals[0] ;
        name = vals[1] ;
        t += `<button type="button" id="T${table}" class="table" > Table ${table} <br>${name}</button>`;
        tables[i] = table ;
        noms[i] = name ;
    }
    if (t !== "") {
        document.querySelector("#here").insertAdjacentHTML("afterend", t);
        if (document.querySelector("#tableExp")!= null ) {
            document.querySelector("#tableExp").remove();
        }

        for (let i = 0; i < tables.length; i++) {
            document.querySelector(`#T${tables[i]}`).addEventListener("click", getInfosTable);
        }
    }

}


function init(){

    const initReq=new XMLHttpRequest();
    initReq.open("GET", `Display?action=${encodeURIComponent("init")}`, true);
    initReq.onreadystatechange = function (){
        if (initReq.readyState===XMLHttpRequest.DONE){
            if (initReq.status===200){
                const numbers = initReq.responseText.split(";") ;
                for (let i =  0; i < numbers.length-1 ; i ++ ) {
                    tables.push(numbers[i]);
                }
            }else {
            }
            createTable() ;
        }
    };
    initReq.send() ;
}
document.querySelector("#swapForm").addEventListener("click",modeSwap) ;
init() ;

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
                const values = reqInfo.responseText.split(";");
                if (values.length === 4) {
                    document.querySelector("#idTabVisu").value = values[0];
                    document.querySelector("#numEtuVisu").value = values[1];
                    document.querySelector("#nomEtuVisu").value = values[2];
                    document.querySelector("#grpEtuVisu").value = values[3];

                    if (active != null && !swap) {document.querySelector(`#T${active}`).style.backgroundColor = "#cccccc";}
                    document.querySelector(`#T${values[0]}`).style.backgroundColor = "#1AFF009B";

                    active = values[0] ;

                }

            }

        }
    };
    reqInfo.send() ;
}



function exportFile(){
    const excel=document.getElementById("Excel").value;
    const list=document.getElementById("Listing").value;
    if (excel==="Excel"){
        const excelRequest=new XMLHttpRequest();
        excelRequest.open("GET", `export?format=${encodeURIComponent(excel)}`);
        excelRequest.onreadystatechange=function (){
            if (excelRequest.readyState===XMLHttpRequest.DONE){
                if (excelRequest.status===200){

                }
            }
        }
        excelRequest.send();
    }
    if (list==="Listing"){
        const listRequest=new XMLHttpRequest();
        listRequest.open("GET", `export?format=${encodeURIComponent(list)}`);
        listRequest.onreadystatechange=function (){
            if (listRequest.readyState===XMLHttpRequest.DONE){
                if (listRequest.status===200){

                }
            }
        }
        listRequest.send() ;
    }

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







