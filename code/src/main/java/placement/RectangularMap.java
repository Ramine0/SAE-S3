package placement;

import utilitaire.Utilitaire;


public class RectangularMap extends Map
{
    private int width, height;

    // a revoir car on ne prends pas en compte les tables supprimées et les L et l
    // je v m'en charger
    // oo ee aa
    @Override
    public int[] neighbours(int table, int[] dispo)
    {
        // on manipule seulement des numeros
        // l'odre est le suivant :
        // les 3 en haut
        // les cotés
        // les 3 en bas
        int[] neighboursId = {0,0,0,0,0,0,0,0};

        // je v jouer sur les positions des tables dispo par rapport a la taille de la salle
        // je v supprimer tout ce que je sais imposible comme voisin et le reste je le maj

        // si on est tout en haut
        if (Utilitaire.pos(table, dispo) <= width ) {
            neighboursId[0] = -1 ;
            neighboursId[1] = -1 ;
            neighboursId[2] = -1 ; }

        // si on est tout en bas
        if (Utilitaire.pos(table, dispo) >= dispo.length - width) {
            neighboursId[5] = -1 ;
            neighboursId[6] = -1 ;
            neighboursId[7] = -1 ; }

        // si on est tout a gauche
        if (Utilitaire.pos(table, dispo)% width == 0 ) {
            neighboursId[0] = -1 ;
            neighboursId[3] = -1 ;
            neighboursId[5] = -1 ; }

        // si on est a droite
        if (Utilitaire.pos(table, dispo)% width == width-1 ) {
            neighboursId[2] = -1 ;
            neighboursId[4] = -1 ;
            neighboursId[7] = -1 ; }

        // mtn on met tout ce qu'on peut

        if (neighboursId[0] != -1 ) {
            neighboursId[0] = dispo[table-width-1] ; }
        if (neighboursId[1] != -1 ) {
            neighboursId[1] = dispo[table-width] ; }
        if (neighboursId[2] != -1 ) {
            neighboursId[2] = dispo[table-width+1] ; }
        if (neighboursId[3] != -1 ) {
            neighboursId[3] = dispo[table-1] ; }
        if (neighboursId[4] != -1 ) {
            neighboursId[4] = dispo[table+1] ; }
        if (neighboursId[5] != -1 ) {
            neighboursId[5] = dispo[table+width-1] ; }
        if (neighboursId[6] != -1 ) {
            neighboursId[6] = dispo[table+width] ; }
        if (neighboursId[7] != -1 ) {
            neighboursId[7] = dispo[table+width+1] ; }

        return neighboursId ;

    }

    public RectangularMap(int longu, int larg) {
        width = larg ;
        height = longu;
    }

}

