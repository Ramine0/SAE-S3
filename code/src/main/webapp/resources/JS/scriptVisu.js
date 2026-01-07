let tables=1;

import {long, larg} from "./script.js";

function createTable(){
    tables++;
    let t= `<button id="T${tables}" class="table"> Table ${tables} </button>`;
    if (tables%larg===0){
        t+=`<br><p id="endLine${tables/larg+1}">`;
    }
    document.querySelector(`#endLine${tables/larg+1}`).insertAdjacentHTML("beforebegin", t);
}

function generate(){
    const generateRequest=new XMLHttpRequest();
    generateRequest.open("POST", "generate", true);
    generateRequest.onreadystatechange = function (){
        if (generateRequest.readyState===XMLHttpRequest.DONE){
            if (generateRequest.status===200){

            }
        }
    };
}

function expOrt(){
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