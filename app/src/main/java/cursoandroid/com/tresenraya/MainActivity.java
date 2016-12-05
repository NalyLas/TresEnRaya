package cursoandroid.com.tresenraya;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static RelativeLayout layout1;
    public static int vacia = 0;
    public static int fichax = 1;
    public static int fichao = 2;
    public int temp = 30000;

    private int[][] tablero;
    private static int fichaActiva;
    private ProgressBar pBar;
    private int mProgressStatus = 0;
    private TextView tv;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pBar = (ProgressBar) findViewById(R.id.pbar);
        tv = (TextView) findViewById(R.id.tvTime);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.relative1);
        final DibujaTablero tab = new DibujaTablero(this);
        layout1.addView(tab);


        inicializacion();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.easy) {
            inicializacion();
            temp=30000;
            tv.setText("Tienes "+temp/1000+ " segundos");
        } else if (id == R.id.normal) {
            inicializacion();
            temp=20000;
            tv.setText("Tienes "+temp/1000+ " segundos");
        } else if (id == R.id.hard) {
            inicializacion();
            temp=10000;
            tv.setText("Tienes "+temp/1000+ " segundos");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void controlarTiempo(){

        new Thread(new Runnable() {
            public void run() {
                pBar.setMax(temp);
                mProgressStatus=0;
                while (mProgressStatus < temp) {
                    mProgressStatus += 1;
                    mHandler.post(new Runnable() {
                        public void run() {
                            pBar.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();

    }

    public void inicializacion() {
        tablero = new int[3][3];
        limpiar();
        tv.setText("Tienes "+temp/1000+ " segundos");
        fichaActiva = fichax;
    }

    public void limpiar() {
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                tablero[i][j] = vacia;
    }

    public void cambiarFicha(View v) {
        haGanado();
        if(fichaActiva == fichao)
            fichaActiva = fichax;
        else
            fichaActiva = fichao;

        controlarTiempo();

    }

    public void haGanado(){
        for(int fil=0; fil<3; fil++) {
            for(int col=0; col<3; col++) {

                if(tablero[0][col] == fichax && tablero[1][col] ==fichax && tablero[2][col] ==fichax) {
                    tv.setText("X ha ganado");

                }else if(tablero[0][col] == fichao && tablero[1][col] ==fichao && tablero[2][col] ==fichao){
                    tv.setText("O ha ganado");

                }
            }
        }
    }

    public int getCasilla(int fil, int col) {
        return tablero[fil][col];
    }

    class DibujaTablero extends View {
        float x = 50;
        float y = 50;
        String accion = "accion";
        Path path = new Path();


        public DibujaTablero(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            //Pintar tablero
            int alto = 980;
            int ancho = 980;

            Paint borde = new Paint();
            borde.setStyle(Paint.Style.STROKE);
            borde.setColor(Color.GRAY);
            borde.setStrokeWidth(5);

            canvas.drawLine(ancho/3, 0, ancho/3, alto, borde);
            canvas.drawLine(2*ancho/3, 0, 2*ancho/3, alto, borde);
            canvas.drawLine(0, alto/3, ancho, alto/3, borde);
            canvas.drawLine(0, 2*alto/3, ancho, 2*alto/3, borde);
            canvas.drawRect(0, 0, ancho, alto, borde);


            Paint circulo = new Paint();
            circulo.setStyle(Paint.Style.STROKE);
            circulo.setStrokeWidth(8);
            circulo.setColor(Color.MAGENTA);

            Paint cruz = new Paint();
            cruz.setStyle(Paint.Style.STROKE);
            cruz.setStrokeWidth(8);
            cruz.setColor(Color.GREEN);


            for(int fil=0; fil<3; fil++) {
                for(int col=0; col<3; col++) {

                    if(tablero[fil][col] == fichax) {
                        //Cruz
                        canvas.drawLine(
                                col * (ancho / 3) + (ancho / 3) * 0.1f, fil * (alto / 3) + (alto / 3) * 0.1f, col * (ancho / 3) + (ancho / 3) * 0.9f, fil * (alto / 3) + (alto / 3) * 0.9f, cruz);

                        canvas.drawLine(
                                col * (ancho / 3) + (ancho / 3) * 0.1f, fil * (alto / 3) + (alto / 3) * 0.9f, col * (ancho / 3) + (ancho / 3) * 0.9f, fil * (alto / 3) + (alto / 3) * 0.1f, cruz);
                    }
                    else if(tablero[fil][col] == fichao) {
                        //Circulo
                        canvas.drawCircle(
                                col * (ancho / 3) + (ancho / 6), fil * (alto / 3) + (alto / 6), (ancho / 6) * 0.8f, circulo);
                    }
                }
            }
        }


        public boolean onTouchEvent(MotionEvent e) {
            if(mProgressStatus!=temp){
                int fil = (int) (e.getY() / (980/3));
                int col = (int) (e.getX() / (980/3));

                tablero[fil][col] = fichaActiva;
            }



            this.invalidate();
            return super.onTouchEvent(e);
        }
    }
}
