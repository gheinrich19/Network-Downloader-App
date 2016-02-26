package com.example.yams.networkpicturedownloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainDownloadActivity extends Activity
    implements View.OnClickListener{
    URL url = null;
    Button BtnStartProgress;
    Button MoonButton;
    Button PlumButton;
    Button UshimachiButton;
    ImageView ImageView;
    Handler ProgressBarHandlerOnMainThread = new Handler();
    final int buffer = 8192;
    long FileSize = 0;
    ProgressDialog MyProgressBar;
    int MyProgressBarStatus = 0;
    final static String DYERS_URL = "http://www.ibiblio.org/wm/paint/auth/hiroshige/dyers.jpg";
    final static String MOON_PINE_URL = "http://www.ibiblio.org/wm/paint/auth/hiroshige/moonpine.jpg";
    final static String PLUM_ESTATE_URL = "http://www.ibiblio.org/wm/paint/auth/hiroshige/plum.jpg";
    final static String USHIMACHI_URL = "http://www.ibiblio.org/wm/paint/auth/hiroshige/takanawa.jpg";
    static final String FILE_PATH = Environment.getExternalStorageDirectory()
            .toString() + "/downloadedfile.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_download);
        ImageView = (ImageView) findViewById(R.id.DloadImageView);
        BtnStartProgress = (Button)findViewById(R.id.dyersButton);
        MoonButton = (Button)findViewById(R.id.MoonPineButton);
        PlumButton = (Button)findViewById(R.id.PlumeestateButton);
        UshimachiButton = (Button)findViewById(R.id.UshimachiButton);
        BtnStartProgress.setOnClickListener(this);
        MoonButton.setOnClickListener(this);
        PlumButton.setOnClickListener(this);
        UshimachiButton.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        MyProgressBar = new ProgressDialog(v.getContext());
        MyProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        MyProgressBar.setProgress(0);
        MyProgressBar.setMax(100);
        MyProgressBar.show();

        MyProgressBarStatus = 0;
        FileSize = 0 ;

            if(v.getId() == R.id.dyersButton)
            {
                try {
                    url = new URL(DYERS_URL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else if (v.getId() == R.id.MoonPineButton){
                try {
                    url = new URL(MOON_PINE_URL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else if (v.getId() == R.id.PlumeestateButton)
            {
                try {
                    url = new URL(PLUM_ESTATE_URL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else if (v.getId() == R.id.UshimachiButton)
            {
                try {
                    url = new URL(USHIMACHI_URL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        Thread thread = new Thread(){
            public void run(){
                DownloadTheFile();
            }
        };
        thread.start();

    }

    public void DownloadTheFile(){

        while (MyProgressBarStatus != 100) {
            int count;

            try {


                URLConnection connection = url.openConnection();
                connection.connect();
                int LengthofFileFetched = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), buffer);

                final OutputStream output = new FileOutputStream(FILE_PATH);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                    MyProgressBarStatus = (int) ((total * 100) / LengthofFileFetched);
                }

                ProgressBarHandlerOnMainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        MyProgressBar.setProgress(MyProgressBarStatus);
                        ImageView.setImageDrawable(Drawable.createFromPath(FILE_PATH));

                    }
                });

                output.flush();
                output.close();
                // close up the input stream object too

                input.close();

            } catch (Exception e) {
            }

            if (MyProgressBarStatus >= 100) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }

                MyProgressBar.dismiss();
            }

        }
    }
}
