package owaa.colors_flood;

import java.util.Random;

/**
 * Created by Owaa on 17/11/15.
 */
public class Grille {


    int map [][];
    public  Grille (){


    }
    public  Grille (int nbLigne , int nbColone){

        map = new int [nbLigne][nbColone];
        Random rand = new Random();
        for(int i = 0 ; i< nbLigne ;i++){
            for(int j = 0 ; j < nbColone ; j++){
                map[i][j] = rand.nextInt(6) + 1;
            }
        }
    }




}
