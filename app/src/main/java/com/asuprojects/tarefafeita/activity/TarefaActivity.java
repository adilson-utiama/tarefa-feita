package com.asuprojects.tarefafeita.activity;

import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.asuprojects.tarefafeita.MainActivity;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.database.converters.CalendarTypeConverter;
import com.asuprojects.tarefafeita.database.repository.TarefaRepository;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.AddTarefaViewModel;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TarefaActivity extends AppCompatActivity {

    private TextInputEditText inputTitulo;
    private TextInputEditText inputAnotacao;

    private Button btnSelecaoData;
    private Button btnSelecaoHorario;
    private Button btnSalvar;

    private RadioGroup radioGroup;

    private Toolbar toolbar;

    private AddTarefaViewModel viewModel;

    private Tarefa tarefa;
    private Prioridade prioridade = Prioridade.MEDIA;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        toolbar = findViewById(R.id.toolbar_tarefa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        inputTitulo = findViewById(R.id.input_titulo);
        inputAnotacao = findViewById(R.id.input_anotacao);
        radioGroup = findViewById(R.id.radioGroup);

        viewModel = ViewModelProviders.of(this).get(AddTarefaViewModel.class);

        btnSelecaoData = findViewById(R.id.btnCalendar);
        Calendar data = Calendar.getInstance();

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
        btnSelecaoHorario.setText(DataFormatterUtil.formataHora(data));
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

        btnSalvar = findViewById(R.id.btnAdicionar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tarefa == null){
                    tarefa = new Tarefa();
                }
                tarefa.setTitulo(inputTitulo.getText().toString());
                tarefa.setAnotacao(inputAnotacao.getText().toString());
                tarefa.setDataIncluida(Calendar.getInstance());

                String data = btnSelecaoData.getText().toString();
                String hora = btnSelecaoHorario.getText().toString();
                Calendar dataConclusao = buildCalendar(data, hora);
                tarefa.setDataConlusao(dataConclusao);
                tarefa.setStatus(Status.ADICIONADO);
                tarefa.setPrioridade(prioridade);

                if(tarefa.getId() != 0){
                    viewModel.atualiza(tarefa);
                }else{
                    viewModel.adiciona(tarefa);
                }

                finish();
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra("EDITAR_TAREFA")){
            tarefa = (Tarefa) intent.getSerializableExtra("EDITAR_TAREFA");
            if(tarefa != null){
                inputTitulo.setText(tarefa.getTitulo());
                inputAnotacao.setText(tarefa.getAnotacao());
                Calendar dataR = tarefa.getDataConlusao();
                btnSelecaoData.setText(format.format(dataR.getTime()));
                int horaR = data.get(Calendar.HOUR_OF_DAY);
                int minutoR = data.get(Calendar.MINUTE);
                btnSelecaoHorario.setText(horaR + ":" + minutoR);
                checkPrioridadeRadioButton(tarefa.getPrioridade());
                btnSalvar.setText("Atualizar");
            }
        }

    }

    private void checkPrioridadeRadioButton(Prioridade prioridade) {
        RadioButton baixa = findViewById(R.id.item_baixa);
        int baixaId = baixa.getId();
        RadioButton media = findViewById(R.id.item_media);
        int mediaId = media.getId();
        RadioButton alta = findViewById(R.id.item_alta);
        int altaId = alta.getId();
        switch (prioridade.getCod()){
            case 1:
                radioGroup.check(baixaId);
                break;
            case 2:
                radioGroup.check(mediaId);
                break;
            case 3:
                radioGroup.check(altaId);
                break;
        }

    }

    private Calendar buildCalendar(String data, String horario) {
        int dia, mes, ano, hora, minuto = 0;
        String[] arrayData = data.split("/");
        String[] arrayHorario = horario.split(":");
        dia = Integer.parseInt(arrayData[0]);
        mes = Integer.parseInt(arrayData[1]);
        ano = Integer.parseInt(arrayData[2]);
        hora = Integer.parseInt(arrayHorario[0]);
        minuto = Integer.parseInt(arrayHorario[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes - 1, dia, hora, minuto);
        return calendar;
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
                    prioridade = Prioridade.BAIXA;
                }
                break;
            case R.id.item_media:
                if(checked){
                    prioridade = Prioridade.MEDIA;
                }
                break;
            case R.id.item_alta:
                if(checked){
                    prioridade = Prioridade.ALTA;
                }
                break;
        }
    }

}
