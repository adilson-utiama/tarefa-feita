package com.asuprojects.tarefafeita.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.asuprojects.tarefafeita.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TarefaActivity extends AppCompatActivity {

    private Button btnSelecaoData;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        toolbar = findViewById(R.id.toolbar_tarefa);
        setSupportActionBar(toolbar);

        btnSelecaoData = findViewById(R.id.btnCalendar);
        btnSelecaoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerBuilder builder = new DatePickerBuilder(TarefaActivity.this, listener)
                        .date(Calendar.getInstance())
                        .pickerType(CalendarView.ONE_DAY_PICKER);

                DatePicker datePicker = builder.build();
                datePicker.show();
            }
        });

    }

    private OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> calendars) {
            Calendar calendar = calendars.get(0);
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String data = format.format(date);
            btnSelecaoData.setText(data);
        }
    };
}
