package github.shicaid.host;

import android.app.Application;
import android.content.Context;

import com.didi.virtualapk.PluginManager;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shicaid on 17-8-1.
 * Descrip:<al> <al>
 */

public class App extends Application {

    /**
     * virtualapk
     */
    private PluginManager mPluginManager;
    /**
     * PluginManager - > private Map< String, LoadedPlugin> mPlugins = new ConcurrentHashMap<>();
     * 储存插件map
     */
    private  Field mPluginsField;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            //virtualapk初始化
            mPluginManager = PluginManager.getInstance(base);
            mPluginManager.init();
            //反射得到mPlugins域
            Class cls = mPluginManager.getClass();
            mPluginsField = cls.getDeclaredField("mPlugins");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
//        PluginManager.getInstance(base).init();
    }

    public PluginManager getPluginManager() {
        return mPluginManager;
    }

    /**
     * virtualapk没有提供删除插件的方法
     * 只能通过反射来了
     *
     * @param packageName 插件包名
     */
    public void deletePlugin(String packageName){
        if(mPluginsField != null) {
            mPluginsField.setAccessible(true);
            try {
                ConcurrentHashMap mPlugin = (ConcurrentHashMap) mPluginsField.get(mPluginManager);
                mPlugin.remove(packageName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
