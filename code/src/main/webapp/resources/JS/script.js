const constGp=document.querySelector('#mode');

// dans les fonctions javascript a faire il y a :
/*
    generer() ; genere le plcament !!!! nessecite les contraintes OK et le fichier OK !!!!!!!!!
    sinon message en rouge "Generation Impossible un numero ne correspond a aucun etudiant " par exemple

    les fonction walider vont etre appelée par les fonctions qui vont valider dans les sections
    string validerEtu(String idPartiel) ; renvoie l'id de l'etu si il existe (on peu donner un id incomplet et le completer si unique
    boolean validerTable() ; dit si la table existe et est pas supprimée

    boolean validerEtuGroup() ; utilise valider Etu pour valider le group
    void validerPlaceImposee() ; utiliser validerEtu et table pour valider la contrainte de place imposee

    boolean chercherEtu() ; cherche l'etu via son num etu si pas trouvé la section ou on cherche
    (dans l'ideal la section est rouge mais deviens vert si on trouve !!!! pas important c'est apres quand tout marche)

    createImposed() ; Crée une contrainte de place imposée
    createGroup() ; ajoute un nouveau groupe
    addToGroup() ; ajoute au groupe l'etudiant trouvé
    setClassMode() ; change le mode de contrainte par classe

    importFichier() ; enregistre le fichier etudiants.csv

*/

