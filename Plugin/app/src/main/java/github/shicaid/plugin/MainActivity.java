package github.shicaid.plugin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_main);
        setTitle("Here is Plugin");
        Log.d("MainActivity", "----------------plugin mainactivity oncreate");

    }
}
