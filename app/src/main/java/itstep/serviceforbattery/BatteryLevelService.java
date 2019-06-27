package itstep.serviceforbattery;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class BatteryLevelService extends Service {

    private static final String NOTIFICATION_TAG = "BatteryLevel";
    private boolean isWorking;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isWorking) {
            isWorking = true;
            showBatteryLevel();
        }
        return START_STICKY;
    }

    private void showBatteryLevel(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                Resources resources = getResources();
                String message = resources.getString(R.string.notification_text_battery_undefined);
                Bitmap picture = BitmapFactory.decodeResource(resources, R.drawable.battery_undefined);
                //int smallIcon;
                if(batteryStatus != null) {
                    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    //int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    if (chargePlug != -1 && level != -1 && scale != -1) {
                        boolean isCharging = chargePlug != 0;
                        int batteryPct = level * 100 / scale;

                        if (isCharging && batteryPct == 100) {
                            message = resources.getString(R.string.notification_text_batteryChargingFull) + " - " + batteryPct + "%";
                            picture = BitmapFactory.decodeResource(resources, R.drawable.battery_charging_full);
                        } else if (isCharging && batteryPct < 100 && batteryPct > 15) {
                            message = resources.getString(R.string.notification_text_batteryCharging) + " - " + batteryPct + "%";
                            picture = BitmapFactory.decodeResource(resources, R.drawable.battery_charging_okay);
                        } else if (isCharging && batteryPct <= 15) {
                            message = resources.getString(R.string.notification_text_batteryCharging) + " - " + batteryPct + "%";
                            picture = BitmapFactory.decodeResource(resources, R.drawable.battery_charging_low);
                        } else if (!isCharging && batteryPct == 100) {
                            message = resources.getString(R.string.notification_text_batteryOkay) + " - " + batteryPct + "%";
                            picture = BitmapFactory.decodeResource(resources, R.drawable.battery_full);
                        } else if (!isCharging && batteryPct < 100 && batteryPct > 15) {
                            message = resources.getString(R.string.notification_text_batteryOkay) + " - " + batteryPct + "%";
                            picture = BitmapFactory.decodeResource(resources, R.drawable.battery_okay);
                        } else if (!isCharging && batteryPct <= 15) {
                            message = resources.getString(R.string.notification_text_batteryLow) + " - " + batteryPct + "%";
                            picture = BitmapFactory.decodeResource(resources, R.drawable.battery_low);
                            showWarningDialog(resources.getString(R.string.warning_activity_text));
                        }
                    }
                }
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext())
                        .setContentTitle(resources.getString(R.string.notification_title))
                        .setContentText(message)
                        .setLargeIcon(picture)
                        .setSmallIcon(R.drawable.ic_stat_battery_level)
                        .setAutoCancel(true)
                        .setContentIntent(
                                PendingIntent.getActivity(BatteryLevelService.this, 0, new Intent(), 0));

                showNotification(mBuilder.build());
                stopSelf();
            }
        }).start();
    }

    private void showNotification(Notification notification){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    private void showWarningDialog(String text){
        Intent dialogIntent = new Intent(this, WarningActivity.class);
        dialogIntent.putExtra(WarningActivity.WARNING_TEXT_TAG, text);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }
}
