package owaa.colors_flood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by Owaa on 16/11/15.
 */

public class ViewCanvas extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private     Thread  cv_thread;
    SurfaceHolder holder;


    public ViewCanvas(Context cxt, AttributeSet attrs) {
        super(cxt, attrs);
        setMinimumHeight(100);
        setMinimumWidth(100);
        initparameters();
        cv_thread   = new Thread(this);

          holder = getHolder();
          holder.addCallback(this);

        // prise de focus pour gestion des touches
        setFocusable(true);
    }


    static final int carteWidth = 20;
    static final int carteHeight = 24;


    //int [][] map = new int[carteWidth][carteHeight];
    Grille grille = new Grille(carteWidth,carteHeight );

    int tailleMapX , tailleMapY;
    int colorSelected;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        //Here you can get the size!
        tailleMapX = getWidth();
        tailleMapY = getHeight();
    }

    private void paintcarte(Canvas canvas) {

        Paint paint = new Paint();

        int tailleCarre  = tailleMapX / carteWidth ;
        int tailleCarreY = tailleMapY / carteWidth  ;

        paint.setColor(Color.RED);

        float left = 0;
        float top =  0;
        float right = tailleCarre;
        float bottom = tailleCarre;


        for(int i = 0 ; i< carteWidth ;i++){
            for(int j = 0 ; j < carteHeight ; j++){

                // Change l'emplacement du carre
                left   =  tailleCarre * i;
                top    =  tailleCarre * j;
                right  = (tailleCarre * i) + tailleCarre;
                bottom = (tailleCarre * j) + tailleCarre;

                switch( grille.map[i][j]){

                    case 1 :  paint.setColor(Color.RED); break;
                    case 2 :  paint.setColor(Color.YELLOW); break;
                    case 3 :  paint.setColor(Color.GREEN); break;
                    case 4 :  paint.setColor(Color.BLUE); break;
                    case 5 :  paint.setColor(Color.CYAN); break;
                    case 6 :  paint.setColor(Color.MAGENTA); break;
                }
                canvas.drawRect(left, top, right, bottom, paint);


            }

        }

    }

    private void paintColorButton(Canvas canvas, int nbColors) {

        Paint paint = new Paint();

        int tailleCarre  = tailleMapX / nbColors ;
        // Initialisation de la position
        float leftx = 0;
        float topy =  tailleMapY - tailleCarre - 1;
        float rightx = tailleCarre;
        float bottomy = tailleMapY;

            for(int j = 0 ; j < nbColors ; j++){
                // Dessine en coulleur nbColors en bas
                paint.setStyle(Paint.Style.FILL);
                switch( j + 1){

                    case 1 :  paint.setColor(Color.RED); break;
                    case 2 :  paint.setColor(Color.YELLOW); break;
                    case 3 :  paint.setColor(Color.GREEN); break;
                    case 4 :  paint.setColor(Color.BLUE); break;
                    case 5 :  paint.setColor(Color.CYAN); break;
                    case 6 :  paint.setColor(Color.MAGENTA); break;
                }
                canvas.drawRect(leftx, topy, rightx, bottomy, paint);

                // Dessine en Noir les contours
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                canvas.drawRect(leftx, topy, rightx, bottomy, paint);

                // Change l'emplacement du carre
                leftx += tailleCarre;
                rightx += tailleCarre;

            }
    }

    private void paintBarreTemps(Canvas canvas , float ratioBarre) {

        Paint paint = new Paint();


        int tailleCarre  = tailleMapX / carteWidth ;
        // gere le decalage de la barre de temps.



        float left = 0;
        float top =  tailleCarre * carteHeight;
        float right = (float) (tailleMapX *  ratioBarre);
        float bottom = (tailleCarre * carteHeight) + tailleCarre;

        // Si la barre atteint un certain seuil elle devient rouge
        if(ratioBarre <= (float) 0.40)
        paint.setColor(Color.RED);
        else  paint.setColor(Color.GREEN);

        canvas.drawRect(left, top, right, bottom, paint);
        // dessein en NOIR du contour de la bare de temps
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void drawAll(Canvas cv) {


        float ratioBarre = (float) 0.5;

        cv.drawColor(Color.GRAY);

        paintcarte(cv);
        paintColorButton(cv, 6);
        paintBarreTemps(cv, ratioBarre);
    }


    public void initparameters() {


    // met au hasard la map pour les test
        Random rand = new Random();
        for(int i = 0 ; i< carteWidth ;i++){
            for(int j = 0 ; j < carteHeight ; j++){

                grille.map[i][j] = rand.nextInt(6) + 1;

            }
        }


        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> FCT <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceCreated");
    }


    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceDestroyed");
    }

    /**
     * run (run du thread cr��)
     * on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib�re le canvas
     */
    public void run() {
        Canvas c = null;
        while (true) {
            try {
                cv_thread.sleep(40);

                try {
                    c = holder.lockCanvas(null);
                    drawAll(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch(Exception e) {
                Log.e("-> RUN <-", "PB DANS RUN");
            }
        }
    }



    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {
        Log.i("-> FCT <-", "onTouchEvent: x :"+ event.getX()+ " y :  " + event.getY());

        if (event.getY() > 900) {
            // 6 = nbCouleurs !
            int tailleCarre = tailleMapX/6 ;
            colorSelected =(int) event.getX()/tailleCarre +1;
            Log.i("-> FCT <-", "onTouchEvent: Couleur selectioner = "  + colorSelected );


        }
        return super.onTouchEvent(event);
    }


}