package github.shicaid.host;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.didi.virtualapk.internal.LoadedPlugin;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    /**
     * /sdcard/Test.apk
     */
    private String pluginPath;

    /**
     * 插件github.shicaid.plugin
     */
    private LoadedPlugin loadedPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "----------------host mainactivity oncreate");
        pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Test.apk");

    }

    /**
     * 加载插件
     */
    private void loadPlugin(){
        File plugin = new File(pluginPath);
        try {
            if(loadedPlugin != null){
                //确保每次都是加载的最新插件
                ((App)getApplicationContext()).deletePlugin(loadedPlugin.getPackageName());
            }

            ((App)getApplicationContext()).getPluginManager().loadPlugin(plugin);
            loadedPlugin = ((App)getApplicationContext()).getPluginManager().getLoadedPlugin("github.shicaid.plugin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手动加载插件
     *
     * @param v
     */
    public void toLoadPlugin(View v){
        loadPlugin();
    }

    /**
     * 打开插件activity
     *
     * @param v
     */
    public void toPluginMainActivity(View v) {
        if(loadedPlugin == null){
            loadPlugin();
        }
        Intent intent = new Intent();
        intent.setClassName("github.shicaid.plugin", "github.shicaid.plugin.MainActivity");
        startActivity(intent);
    }

    /**
     * 打开插件service
     *
     * @param v
     */
    public void toPluginService(View v){
        if(loadedPlugin == null){
            loadPlugin();
        }

        Intent intent = new Intent();
        intent.setClassName("github.shicaid.plugin", "github.shicaid.plugin.PluginService");
        startService(intent);
    }
}
