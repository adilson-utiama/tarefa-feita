package com.asuprojects.tarefafeita.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TarefaActivity extends AppCompatActivity {

    private Button btnSelecaoData;
    private Button btnSelecaoHorario;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        toolbar = findViewById(R.id.toolbar_tarefa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnSelecaoData = findViewById(R.id.btnCalendar);
        Calendar data = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        btnSelecaoData.setText(format.format(data.getTime()));
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

        btnSelecaoHorario = findViewById(R.id.btnHorario);
        int hora = data.get(Calendar.HOUR_OF_DAY);
        int minuto = data.get(Calendar.MINUTE);
        btnSelecaoHorario.setText(hora + ":" + minuto);
        btnSelecaoHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar atual = Calendar.getInstance();
                int horaAtual = atual.get(Calendar.HOUR_OF_DAY);
                int minutosAtuais = atual.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(TarefaActivity.this, listenerHorario, horaAtual, minutosAtuais, true);
                dialog.show();
            }
        });

    }

    private TimePickerDialog.OnTimeSetListener listenerHorario = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hora, int minutos) {
            btnSelecaoHorario.setText(hora + ":" + minutos);
        }
    };

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

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.item_baixa:
                if(checked){
                    Toast.makeText(this, "BAIXA selecionada", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.item_media:
                if(checked){
                    Toast.makeText(this, "MEDIA selecionada", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.item_alta:
                if(checked){
                    Toast.makeText(this, "ALTA selecionada", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
