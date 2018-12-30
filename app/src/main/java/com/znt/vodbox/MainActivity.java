package com.znt.vodbox;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.io.File;

public class MainActivity extends Activity {


    private String pluginName1 = "";
    private String pluginName2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        initDirs();

        Button btnStart1 = (Button)findViewById(R.id.btn_start_1);
        Button btnStart2 = (Button)findViewById(R.id.btn_start_2);
        Button btnStart3 = (Button)findViewById(R.id.btn_start_3);
        btnStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlugin1();
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("demo2",
                        "com.qihoo360.replugin.sample.demo2.MainActivity"));
            }
        });
        btnStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlugin2();
                RePlugin.startActivity(MainActivity.this, RePlugin.createIntent(pluginName2,
                        "com.znt.vodbox.activity.WelcomeActivity"));
            }
        });
        btnStart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }

    private File dirFv1 = null;
    private File dirFv2 = null;
    private void initDirs()
    {
        dirFv1 = new File(Environment.getExternalStorageDirectory() + "/dianyin/v1");
        dirFv2 = new File(Environment.getExternalStorageDirectory() + "/dianyin/v2");

        if(!dirFv1.exists())
            dirFv1.mkdirs();

        if(!dirFv2.exists())
            dirFv2.mkdirs();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPlugin1()
    {
        File file = new File(dirFv1.getAbsolutePath() + "/app-debug.apk");
        if(file.exists())
        {
            PluginInfo pluginInfo = RePlugin.install(file.getAbsolutePath());
            if(pluginInfo != null)
            {
                RePlugin.preload(pluginInfo);//耗时
                pluginName1 = pluginInfo.getName();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"插件安装失败",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    private void loadPlugin2()
    {
        File file = new File(dirFv2.getAbsolutePath() + "/app-debug.apk");
        if(file.exists())
        {
            PluginInfo pluginInfo = RePlugin.install(file.getAbsolutePath());
            if(pluginInfo != null)
            {
                RePlugin.preload(pluginInfo);//耗时
                pluginName2 = pluginInfo.getName();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"插件安装失败",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

}
