package com.example.ravikumar.myservicecomm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class DownloadService extends IntentService {

    private String fileName="indexFile";
    private int result;
    static public String CUSTOM_NOTIFICATION="mindfire.customnotification";

    public DownloadService()
    {
        super("Download service");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String fileName=intent.getStringExtra(KeyConstant.FILE_NAME);
        String urlPath=intent.getStringExtra(KeyConstant.URL_PATH);
        File output=new File( Environment.getExternalStorageDirectory(),fileName);
        if(output.exists())
            output.delete();
        InputStream stream=null;
        FileOutputStream fos=null;

        try {
            URL url = new URL(urlPath);
            stream=url.openConnection().getInputStream();
            InputStreamReader reader=new InputStreamReader(stream);
            fos=new FileOutputStream(output);
            int next=-1;
            while ((next=reader.read())!=-1)
            {
                fos.write(next);
            }
            result= MainActivity.RESULT_OK;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(stream!=null)
            {
                try {
                    stream.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if(fos!=null)
            {
                try {
                    fos.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        updateProgress(result,output.getAbsolutePath());
    }

    private void updateProgress(int result, String filePath) {
        Intent intent=new Intent(CUSTOM_NOTIFICATION);
        intent.putExtra(KeyConstant.RESULT,result);
        intent.putExtra(KeyConstant.FILE_PATH,filePath);
          LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
