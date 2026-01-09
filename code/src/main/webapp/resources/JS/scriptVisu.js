let tables=[];
console.log(tables) ;
//import {long, larg} from "./script.js";

function createTable(){
    let t = "" ;
    let table ;
    for (let i = 0; i < tables.length; i++ ){
        table = tables[i] ;
        t+=`<button id="T${table}" class="table"> Table ${table} </button>`;
    }
    document.querySelector("#here").insertAdjacentHTML("afterend",t);
}


function init(){

    const initReq=new XMLHttpRequest();
    initReq.open("GET", `Display?action=${encodeURIComponent("init")}`, true);
    initReq.onreadystatechange = function (){
        if (initReq.readyState===XMLHttpRequest.DONE){
            if (initReq.status===200){
                const numbers = initReq.responseText.split(";");
                console.log("reponse : "+ numbers) ;
                for (let i =  0; i < numbers.length ; i ++ ) {
                    tables.push(numbers[i]) ;
                }

            }else {

            }
            createTable() ;
        }
    };
    initReq.send() ;
}

init() ;

/*
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
    }

}

 */





