package com.asuprojects.tarefafeita.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.AlarmManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.AddTarefaViewModel;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TarefaActivity extends AppCompatActivity {

    public static final String EDITAR_TAREFA = "EDITAR_TAREFA";
    public static final String EXECUTAR_ALARME = "EXECUTAR_ALARME";
    public static final String TAREFA = "tarefa";
    public static final int FEED_NUMBER = 2000;
    private TextInputLayout textInputLayout;
    private TextInputEditText inputTitulo;
    private TextInputEditText inputAnotacao;

    private Button btnSelecaoData;
    private Button btnSelecaoHorario;
    private Button btnSalvar;

    private RadioGroup radioGroup;

    private AddTarefaViewModel viewModel;

    private Tarefa tarefa;
    private Prioridade prioridade = Prioridade.NENHUM;

    private ConstraintLayout painelSelecaoData;
    private boolean painelVisivel = false;
    private boolean prioridadeSelecionada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        configuraToolBar();

        textInputLayout = findViewById(R.id.textInputLayout_titulo);
        inputTitulo = findViewById(R.id.input_titulo);
        inputAnotacao = findViewById(R.id.input_anotacao);
        radioGroup = findViewById(R.id.radioGroup);

        painelSelecaoData = findViewById(R.id.painelData);

        viewModel = ViewModelProviders.of(this).get(AddTarefaViewModel.class);

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

        btnSelecaoHorario = findViewById(R.id.btnHorario);
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

                if(validaCampos()) {

                    if (tarefa == null) {
                        tarefa = new Tarefa();
                    }
                    tarefa.setTitulo(inputTitulo.getText().toString());
                    tarefa.setAnotacao(inputAnotacao.getText().toString());
                    tarefa.setDataIncluida(Calendar.getInstance());
                    tarefa.setStatus(Status.ADICIONADO);
                    if(dataFoiSelecionada() && horaFoiSelecionada()){
                        String data = btnSelecaoData.getText().toString();
                        String hora = btnSelecaoHorario.getText().toString();
                        Calendar dataConclusao = buildCalendar(data, hora);
                        tarefa.setDataConlusao(dataConclusao);
                        tarefa.setPrioridade(prioridade);
                    } else {
                        tarefa.setDataConlusao(Calendar.getInstance());
                        tarefa.setPrioridade(prioridade);
                    }

                    if (tarefa.getId() != 0) {
                        viewModel.atualiza(tarefa);
                    } else {
                        viewModel.adiciona(tarefa);
                    }

                    if(!tarefa.getPrioridade().equals(Prioridade.NENHUM)){
                        setAlarmeParaNotificacao(tarefa);
                    }
                    finish();
                }

            }
        });

        Intent intent = getIntent();
        eHEdicaoTarefa(intent);

    }

    private void eHEdicaoTarefa(Intent intent) {
        if(intent.hasExtra(EDITAR_TAREFA)){
            getSupportActionBar().setTitle(getString(R.string.titulo_appbar_editar_tarefa));
            tarefa = (Tarefa) intent.getSerializableExtra(EDITAR_TAREFA);
            if(tarefa != null){
                inputTitulo.setText(tarefa.getTitulo());
                inputAnotacao.setText(tarefa.getAnotacao());
                if(!tarefa.getPrioridade().equals(Prioridade.NENHUM)){
                    btnSelecaoData.setText(DataFormatterUtil.formatarData(tarefa.getDataConlusao()));
                    btnSelecaoHorario.setText(DataFormatterUtil.formataHora(tarefa.getDataConlusao()));
                }
                checkPrioridadeRadioButton(tarefa.getPrioridade());
                btnSalvar.setText(R.string.texto_atualizar_tarefa);
            }
        }
    }

    private void configuraToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_tarefa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.titulo_toolbar_adicionar_tarefa);
    }

    private void setAlarmeParaNotificacao(Tarefa tarefa) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificacaoDesativada = sharedPref.getBoolean(getString(R.string.desativar_notificacao), false);
        if(!notificacaoDesativada){
            Intent intent = new Intent(EXECUTAR_ALARME);
            intent.putExtra(TAREFA, tarefa);

            Random random = new Random();
            int nextInt = random.nextInt(FEED_NUMBER);
            long timeInMillis = tarefa.getDataConlusao().getTimeInMillis();

            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(TarefaActivity.this, nextInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            AlarmManagerCompat.setExact(alarm, AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }

    }

    private boolean validaCampos(){
        if(inputTitulo.getText().toString().isEmpty()){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getString(R.string.validacao_campo_obrigatorio));
            return false;
        }

        if(dataFoiSelecionada() && !horaFoiSelecionada()){
            Toast.makeText(this, R.string.validacao_msg_erro_hora, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!dataFoiSelecionada() && horaFoiSelecionada()){
            Toast.makeText(this, R.string.validacao_msg_erro_data, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(dataFoiSelecionada() && horaFoiSelecionada() && !prioridadeSelecionada) {
            Toast.makeText(this, R.string.validacao_msg_erro_prioridade, Toast.LENGTH_SHORT).show();
            return false;
        }

        textInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean dataFoiSelecionada(){
        return !btnSelecaoData.getText().toString().contentEquals(getString(R.string.selecao_data));
    }

    private boolean horaFoiSelecionada(){
        return !btnSelecaoHorario.getText().toString().contentEquals(getString(R.string.selecao_horario));
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
            String horaS = String.valueOf(hora);
            String minutosS = String.valueOf(minutos);
            if (hora < 10){
                horaS = "0" + horaS;
            }
            if(minutos < 10){
                minutosS = "0" + minutos;
            }
            btnSelecaoHorario.setText(horaS + ":" + minutosS);
        }
    };

    private OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> calendars) {
            Calendar calendar = calendars.get(0);
            String data = DataFormatterUtil.formatarData(calendar);
            btnSelecaoData.setText(data);
        }
    };

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        prioridadeSelecionada = checked;
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

    public void mostrarPainelData(View view){
        painelVisivel = !painelVisivel;
        if (painelVisivel){
            painelSelecaoData.setVisibility(View.VISIBLE);
        } else {
            painelSelecaoData.setVisibility(View.GONE);
        }
    }

}
