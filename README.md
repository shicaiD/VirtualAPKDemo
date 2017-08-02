# VirtualAPKDemo
简单使用下滴滴[VirtualAPK](https://github.com/didi/VirtualAPK) <br>

其中遇到几个比较坑的地方 <br>
- **首先插件目录下运行gradle clean assemblePlugin遇到问题** <br>
    ``` 
    * What went wrong:
    A problem occurred configuring project ':app'.
    > Failed to notify project evaluation listener.
      > com/android/builder/dependency/ManifestDependency
    ```

    官方建议修改Gradle和build tools的版本 <br>
    ```
    Gradle                   2.14.1
    com.android.tools.build  2.1.3
    ```
    还别说，换了真的就是没问题了。我之前用的是 Gradle 3.3 , build tools  2.3.1。至于为什么这样我也不清楚，先这样解决吧。<br><br>

- **其次就是插件的资源文件命名不能和宿主资源文件命名重复**  <br><br>
    举个栗子，插件中有个Activity用到的布局文件是R.layout.activity_main，宿主中也存在一个布局文件R.layout.activity_main，结果不用说也应该知道啦，打开插件中使用R.layout.activity_main的Activity显示的却是宿主的R.layout.activity_main。<br>
color、string等都是这样。<br><br>

- **最后就是VirtualAPK框架似乎没有卸载插件的功能** <br><br>

    恩，场景是这样的：<br>
    关闭宿主进程，打开宿主，会重新加载插件。<br>
    若是没有关闭宿主进程，而插件更新了，宿主不会重新加载插件，我也没找到有重新加载插件的方法。
    尝试重新加载，发现这里LoadedPlugin(PluginManager pluginManager, Context context, File apk)有：
    ```
    if (pluginManager.getLoadedPlugin(mPackageInfo.packageName) != null) {
        throw new RuntimeException("plugin has already been loaded : " + mPackageInfo.packageName);
    } 
    ```
    我不想每次都退出应用杀死进程来重新加载插件啊。。没办法，只有自己动手写了个反射来实现了。<br>
    ``` 
    mPluginManager = PluginManager.getInstance(base);
    mPluginManager.init();
    Class cls = mPluginManager.getClass();
    mPluginsField = cls.getDeclaredField("mPlugins");
    ...
    ...
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
      ```
