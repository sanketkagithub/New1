package com.example.a10580.new1;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a10580.new1.iservicessss.MyIntentService;
import com.example.a10580.new1.normalService.WorkingService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    String pathhh = Environment.getExternalStorageDirectory() + "/download/" + "new2.apk";
    String downloadPath = Environment.getExternalStorageDirectory()
            + "/download/" + "gobaby.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()

        // getApkFromFm();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.INSTALL_PACKAGES,
                Manifest.permission.DELETE_PACKAGES,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }


    public void sendToNew2(View view) {
        //    getApkFromFm();

      //  downloadFile(apkUrl, new File(downloadPath));
        downloadApk();

        // installAPK(pathhh);
        Intent intent = new Intent();
        intent.setAction("com.example.a10580.new1");
        intent.putExtra("KeyName", "code1id");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);


//        intent.setComponent(
//                new ComponentName("com.pkg.AppB","com.pkg.AppB.MainActivity"));
    }


    void getApkFromFm() {


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(pathhh)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void installAPK(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                String command;
                command = "adb install -r " + filename;
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startIs(View view) {
/*
       Intent intent = new Intent(this, MyIntentService.class);
       intent.putExtra("carName","tarzan");
       startService(intent);
*/

//startRepeatingStartingService();

        restartHearbeatRepeatingAlarm();
    }


    void startRepeatingStartingService() {
        Intent ishintent = new Intent(this, WorkingService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, ishintent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pintent);

    }


    private void restartHearbeatRepeatingAlarm() {
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // For Heart Beat
        // (1000 * 60 * 10) previously we are calling every 10 mints.but now changed to every 9 mins
        Intent heartBeatIntent = new Intent(this, WorkingService.class);
        PendingIntent mHeartBeatIntent = PendingIntent.getBroadcast(this,
                0, heartBeatIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2000, mHeartBeatIntent);
        }
        Log.i("alarmCall","alarmCalled");
    }

    String apkUrl = "https://firebasestorage.googleapis.com/v0/b/chat01-87e0d.appspot.com/o/edv4.apk?alt=media&token=738e78fb-1792-471d-9fac-7b469f157d60";

    void downloadApk() {
        //  new InstallAPK().execute("http://xyz/android/gamedownload.aspx?name=mygame.apk");
        new InstallAPK().execute(apkUrl);

        /*Intent i;
        PackageManager manager = getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage("com.mycompany.mygame");
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            InstallAPK downloadAndInstall = new InstallAPK();
           // progress.setCancelable(false);
           // progress.setMessage("Downloading...");
            downloadAndInstall.setContext(getApplicationContext(), progress);
            downloadAndInstall.execute("http://xyz/android/gamedownload.aspx?name=mygame.apk");
        }*/
    }


    class InstallAPK extends AsyncTask<String, Void, Void> {


        /*  public void setContext(Context context, ProgressDialog progress){
              this.context = context;
              this.progressDialog = progress;
          }
      */
        public void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                File sdcard = Environment.getExternalStorageDirectory();
                File myDir = new File(sdcard, "temp");
                myDir.mkdirs();
                File outputFile = new File(myDir, "temp.apk");
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();

               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(sdcard, "Android/data/com.mycompany.android.games/temp/temp.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                startActivity(intent);*/


            } catch (FileNotFoundException fnfe) {
                // status = 1;
                Log.e("File", "FileNotFoundException! " + fnfe);
            } catch (Exception e) {
                Log.e("UpdateAPP", "Exception " + e);
            }
            return null;
        }

        public void onPostExecute(Void unused) {
            progressBar.setVisibility(View.GONE);
           /* if(status == 1)
                Toast.makeText(context,"Game Not Available",Toast.LENGTH_LONG).show();
        }*/
        }
    }


    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }


}

