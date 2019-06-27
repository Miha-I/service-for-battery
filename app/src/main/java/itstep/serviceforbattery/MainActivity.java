package itstep.serviceforbattery;

import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!NotificationManagerCompat.from(this).areNotificationsEnabled()){
            TextView textView = (TextView)findViewById(R.id.id_warning_textView);
            textView.setText(getResources().getString(R.string.monitor_notifications_disabled));
        }
    }

    public void startService(View view) {
        startService(new Intent(this, BatteryService.class));
    }

    public void stopService(View view) {
        stopService(new Intent(this, BatteryService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.id_menu_item_setting:
                showDialogSetting();
                return true;
            case R.id.id_menu_item_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogSetting(){
        Intent settingActivity = new Intent(this, SettingActivity.class);
        startActivity(settingActivity);
    }





    @Override
    protected void onResume() {
        super.onResume();
        //showDialogSetting();
    }
}