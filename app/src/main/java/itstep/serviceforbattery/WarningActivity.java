package itstep.serviceforbattery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class WarningActivity extends AppCompatActivity {

    public static final String WARNING_TEXT_TAG = "warning_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        Intent intent = getIntent();
        String warningText = intent.getStringExtra(WARNING_TEXT_TAG);
        TextView textView = (TextView)findViewById(R.id.it_warning_text);
        assert textView != null;
        if(warningText != null){
            textView.setText(warningText);
        }
    }
}
