let tables=[];
console.log(tables) ;
let active ;
let swap = false ;
//import {long, larg} from "./script.js";

function createTable(){
    let t = "" ;
    let table ;
    for (let i = 0; i < tables.length; i++ ){
        table = tables[i] ;
        t += `<button type="button" id="T${table}" class="table" > Table ${table} </button>`;
    }
    if (t != "") {
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
                const numbers = initReq.responseText.split(";");
                console.log("reponse : "+ numbers) ;
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
        console.log("AHA") ;
        activateSwap(event.target.id) ;
    }

    let numTab = event.target.id.substring(1) ;
    let reqInfo = new XMLHttpRequest() ;
    reqInfo.open("GET", `Display?action=${encodeURIComponent("infos")}&number=${encodeURIComponent(numTab)}`, true);
    reqInfo.onreadystatechange = function (){
        if (reqInfo.readyState===XMLHttpRequest.DONE){
            if (reqInfo.status===200){
                const values = reqInfo.responseText.split(";");
                console.log("infos : "+ values) ;
                if (values.length = 3) {
                    document.querySelector("#idTabVisu").value = values[0];
                    document.querySelector("#numEtuVisu").value = values[1];
                    document.querySelector("#nomEtuVisu").value = values[2];

                    if (active != null ) {document.querySelector(`#T${active}`).style.backgroundColor = "#cccccc";}
                    document.querySelector(`#T${values[0]}`).style.backgroundColor = "#1AFF009B" ;
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

    console.log("swap: ",swap) ;
    if (button === "none" && active != null) {
        swap = !swap;
        console.log("nouveau swap : ", swap) ;
    }else if (!swap) {
        swap = true ;
        document.querySelector(`#T${active}`).style.backgroundColor = "rgba(213,176,55,0.82)";

    }else if (document.querySelector(`#${button}`)!= null){

        const swapReq=new XMLHttpRequest();
        swapReq.open("GET", `Display?action=${encodeURIComponent("swap")}&number1=${active}&number2=${button.substring(1)}`);
        swapReq.onreadystatechange=function (){
            if (swapReq.readyState===XMLHttpRequest.DONE){
                if (swapReq.status===200){
                    console.log(swapReq.responseText) ;
                    swap = false ;
                }
            }
        }
        swapReq.send() ;
    }

}







