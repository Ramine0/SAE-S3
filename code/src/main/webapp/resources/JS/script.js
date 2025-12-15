// const constGp=document.querySelector('#mode');
nbImposedPlace = 1;
nbPlacesSuppr = 1;
groupes = [[]] ;




const studentFile = document.querySelector('#studentFile');

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


function validerEtu(idPartiel)
{
    console.log(idPartiel);
}

function validerTable()
{

}

function validerEtuGroup()
{

}

function createImposed() {
    nbImposedPlace++ ;
    imposedPlace =
`<section class="invalid">
<span>
    <label for="studentImposed${nbImposedPlace}"> id Etudiant </label>
    <input name="idEtuImp${nbImposedPlace}" id="studentImposed${nbImposedPlace}" type="text" disabled></input>
</span>
<span>
    <label for="tableImposed1"> Num Table </label>
    <input name="idTabImp${nbImposedPlace}" id="tableImposed${nbImposedPlace}" type="number" disabled></input>
</span>
<button class="remove" id="supTabSup${nbImposedPlace}" onclick="enleverPlaceSuppr()" disabled>remove</button>
<button class="chercher" id="imposed${nbImposedPlace}" onclick="validerPlaceImposee()" disabled>find</button>
</section>` ;

    document.querySelector('#ajoutImpos').insertAdjacentHTML("beforebegin",imposedPlace) ;
}


function createSuppr() {
    nbPlacesSuppr++ ;
    placesSuppr =
        `<section class = "invalid">
<span>
    <label for="numTabSup${nbPlacesSuppr}"> Num Table </label>
    <input name="idTabSup${nbPlacesSuppr}" id="numTabSup${nbPlacesSuppr}" type="number" disabled></input>
</span>
<button class="remove" id="supTabSup${nbPlacesSuppr}" onclick="enleverPlaceSuppr()" disabled>remove</button>
<button class="chercher" id="walTabSup${nbPlacesSuppr}" onclick="validerPlaceSuppr()" disabled>find</button>
</section>` ;

    document.querySelector('#ajoutSuppr').insertAdjacentHTML("beforebegin",placesSuppr) ;
}


function createGrp() {
    groupes.push([]) ;
    etuGrp = `
    <h4>Mis a distance ${groupes.length} </h4>
    <div class="ligne" id="Gp1">
        <section class = "invalid">
            <span>
                <label for="Etu1groupe${groupes.length}"> Num Etudiant </label>
                <input name="idEtu1G1" id="Etu1groupe${groupes.length}" type="number" disabled></input>
            </span>
            <button class="remove" id="supEtu1G${groupes.length}" onclick="enleverEtuGp()" disabled>remove</button>
            <button class="chercher" id="walEtu1G${groupes.length}" onclick="validerEtu()" disabled>find</button>
        </section>
        <button id="ajoutEtuGrp${groupes.length}" class="boutPlus" onclick="createEtuGrp()" disabled >+</button>
        <h4>ajouter un etudiant au groupe</h4>
    </div>`

    document.querySelector('#ajoutGroup').insertAdjacentHTML("beforebegin",etuGrp) ;

}

function createEtuGrp() {
    numGrp = window.event.target.id.charAt(11) -1;
    groupes[numGrp].push(0) ;
    numEtu = groupes[numGrp].length ;
    groupEtu = `<section class = "invalid" >
    <span>
        <label for="Etu${numEtu}groupe${numGrp}"> Num Etudiant </label>
        <input name="idEtu${numEtu}G${numGrp}" id="Etu${numEtu}groupe${numGrp}" type="number" disabled></input>
    </span>
    <button class="remove" id="supEtu${numEtu}G${numGrp}" onclick="enleverEtuGp()" disabled>remove</button>
    <button class="chercher" id="walEtu${numEtu}G${numGrp}" onclick="validerEtu()" disabled>find</button>`
    console.log()
    document.querySelector(`#ajoutEtuGrp${numGrp+1}`).insertAdjacentHTML("beforebegin",groupEtu) ;

}

function displayID(){
    console.log(window.event.target.id) ;
    "string1 test2"
}
