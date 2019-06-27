package itstep.serviceforbattery;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    public static final String FILE_NAME_SETTING = "settingServiceForBattery";
    public static final String KEY_CHECKBOX_SOUND = "key_checkBox_sound";
    public static final String KEY_CHECKBOX_VIBRATION  = "key_checkBox_vibration";
    public static final String KEY_NIGHT_TIME_IN = "key_night_time_in";
    public static final String KEY_NIGHT_TIME_TO = "key_night_time_to";
    public static final String KEY_LOW_BATTERY_LEVEL = "key_low_battery_level";
    public static final String KEY_CHECKBOX_SHOW_WARNING_DIALOG = "key_checkBox_show_warningDialog";
    public static final String KEY_CHECKBOX_DECREASE_BRIGHTNESS = "key_checkBox_decrease_brightness";

    private SharedPreferences sharedPreferences;
    private Resources resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        resource = getResources();
        sharedPreferences = getSharedPreferences(FILE_NAME_SETTING, MODE_PRIVATE);
        loadSetting();
    }

    // Загрузка настроек
    private void loadSetting(){
        // Настройка звука
        CheckBox checkBoxSound = (CheckBox)findViewById(R.id.id_checkBox_sound);
        checkBoxSound.setChecked(sharedPreferences.getBoolean(KEY_CHECKBOX_SOUND, false));
        // Настройка вибрации
        CheckBox checkBoxVibration = (CheckBox)findViewById(R.id.id_checkBox_vibration);
        checkBoxVibration.setChecked(sharedPreferences.getBoolean(KEY_CHECKBOX_VIBRATION, false));
        // Настройка начала ночного режима
        TextView textViewNightTimeIn = (TextView)findViewById(R.id.id_night_time_in);
        textViewNightTimeIn.setText(sharedPreferences.getString(KEY_NIGHT_TIME_IN, "00:00"));
        // Настройка ончания ночного режима
        TextView textViewNightTimeTo = (TextView)findViewById(R.id.id_night_time_to);
        textViewNightTimeTo.setText(sharedPreferences.getString(KEY_NIGHT_TIME_TO, "00:00"));
        // Настройка уровня низкого заряда
        int maxLowBatteryLevel = resource.getInteger(R.integer.max_low_battery_level);
        final int minLowBatteryLevel = resource.getInteger(R.integer.min_low_battery_level);
        int defaultLowBatteryLevel = resource.getInteger(R.integer.default_low_battery_level);
        int lowBatteryLevel = sharedPreferences.getInt(KEY_LOW_BATTERY_LEVEL, defaultLowBatteryLevel);
        final TextView textViewLowBatteryLevel = (TextView)findViewById(R.id.id_text_low_battery_level);
        String stringBatteryLevel = lowBatteryLevel + "%";
        textViewLowBatteryLevel.setText(stringBatteryLevel);
        SeekBar seekBarLowBatteryLevel = (SeekBar)findViewById(R.id.id_seekBar_low_battery_level);
        seekBarLowBatteryLevel.setMax(maxLowBatteryLevel - minLowBatteryLevel);
        seekBarLowBatteryLevel.setProgress(lowBatteryLevel - minLowBatteryLevel);
        seekBarLowBatteryLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewLowBatteryLevel.setText(progress + minLowBatteryLevel + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_LOW_BATTERY_LEVEL, seekBar.getProgress() + minLowBatteryLevel);
                editor.apply();
            }
        });
        // Настройка показа диалогового окна
        CheckBox checkBoxShowWarningDialog = (CheckBox)findViewById(R.id.id_checkBox_show_warningDialog);
        checkBoxShowWarningDialog.setChecked(sharedPreferences.getBoolean(KEY_CHECKBOX_SHOW_WARNING_DIALOG, true));
        // Настройка уменьшения яркости
        CheckBox checkBoxDecreaseBrightness = (CheckBox)findViewById(R.id.id_checkBox_decrease_brightness);
        checkBoxDecreaseBrightness.setChecked(sharedPreferences.getBoolean(KEY_CHECKBOX_DECREASE_BRIGHTNESS, true));
    }

    // Сохранение настроек при изменении
    public void onClickListener(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        CheckBox checkBox;
        switch (view.getId()){
            case R.id.id_checkBox_sound:
                checkBox = (CheckBox) view;
                editor.putBoolean(KEY_CHECKBOX_SOUND, checkBox.isChecked());
                break;
            case R.id.id_checkBox_vibration:
                checkBox = (CheckBox) view;
                editor.putBoolean(KEY_CHECKBOX_VIBRATION, checkBox.isChecked());
                break;
            case R.id.id_night_time_in:
                setTime(view, KEY_NIGHT_TIME_IN);
                break;
            case R.id.id_night_time_to:
                setTime(view, KEY_NIGHT_TIME_TO);
                break;
            case R.id.id_checkBox_show_warningDialog:
                checkBox = (CheckBox) view;
                editor.putBoolean(KEY_CHECKBOX_SHOW_WARNING_DIALOG, checkBox.isChecked());
                break;
            case R.id.id_checkBox_decrease_brightness:
                checkBox = (CheckBox) view;
                editor.putBoolean(KEY_CHECKBOX_DECREASE_BRIGHTNESS, checkBox.isChecked());
                break;
        }
        editor.apply();
    }

    // Установка и сохранение времени
    public void setTime(View view, final String key) {
        if(view != null){
            final TextView textView = (TextView) view;
            String[] str = textView.getText().toString().split(":");
            new TimePickerDialog(
                    this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String time = String.format(Locale.getDefault(), "%d:%02d", hourOfDay, minute);
                            textView.setText(time);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(key, time);
                            editor.apply();
                        }
                    },
                    Integer.valueOf(str[0]),
                    Integer.valueOf(str[1]),
                    true).show();
        }
    }
}