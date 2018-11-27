package com.asuprojects.tarefafeita.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.AlarmManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.broadcastreceiver.AlarmReceiver;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.AddTarefaViewModel;
import com.asuprojects.tarefafeita.util.ByteArrayHelper;
import com.asuprojects.tarefafeita.util.CalendarUtil;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TarefaActivity extends AppCompatActivity {

    public static final String EDITAR_TAREFA = "EDITAR_TAREFA";
    public static final String EXECUTAR_ALARME = "EXECUTAR_ALARME";
    public static final String TAREFA_ALARM = "tarefa";
    public static final int FEED_NUMBER = 2000;
    private TextInputLayout textInputLayout;
    private TextInputEditText inputTitulo;
    private TextInputEditText inputAnotacao;

    private Button btnSelecaoData;
    private Button btnSelecaoHorario;
    private Button btnSalvar;
    private AddTarefaViewModel viewModel;

    private AppCompatCheckBox checkBoxNotificacao;

    private Tarefa tarefa;
    private ConstraintLayout painelSelecaoData;
    private boolean painelVisivel = false;
    private SwitchCompat switchPainel;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        viewModel = ViewModelProviders.of(this).get(AddTarefaViewModel.class);

        checkBoxNotificacao = findViewById(R.id.checkBox_notificacao);

        configuraPainelSelecaoData();
        configuraToolBar();
        configuraComponentes();
        configuraBtnSelData();
        configuraBtnSelHorario();
        configuraBtnSalvarTarefa();
        eHEdicaoTarefa(getIntent());
    }

    private void configuraPainelSelecaoData() {
        switchPainel = findViewById(R.id.switch_painel);
        switchPainel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                painelVisivel = !painelVisivel;
                if (painelVisivel){
                    painelSelecaoData.animate().setDuration(500).alpha(1F);
                    togglePainelData(true);
                } else {
                    painelSelecaoData.animate().setDuration(500).alpha(0.4F);
                    togglePainelData(false);
                }
            }
        });
    }

    private void togglePainelData(boolean visibilidade) {
        btnSelecaoData.setEnabled(visibilidade);
        btnSelecaoHorario.setEnabled(visibilidade);
        checkBoxNotificacao.setEnabled(visibilidade);
    }

    private void configuraBtnSalvarTarefa() {
        btnSalvar = findViewById(R.id.btnAdicionar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validaCampos()) {
                    if (tarefa == null) {
                        tarefa = new Tarefa();
                    }
                    salvaTarefa();
                    finish();
                }

            }
        });
    }

    private void salvaTarefa() {
        tarefa.setTitulo(inputTitulo.getText().toString());
        tarefa.setAnotacao(inputAnotacao.getText().toString());
        tarefa.setDataIncluida(Calendar.getInstance());
        tarefa.setStatus(Status.ADICIONADO);
        if(dataFoiSelecionada() && horaFoiSelecionada()){
            String data = btnSelecaoData.getText().toString();
            String hora = btnSelecaoHorario.getText().toString();
            Calendar dataConclusao = CalendarUtil.buildCalendarFrom(data, hora);
            tarefa.setDataConlusao(dataConclusao);
            tarefa.setDataDefinida(true);
        } else {
            tarefa.setDataConlusao(Calendar.getInstance());
            tarefa.setDataDefinida(false);
        }
        if (tarefa.getId() != 0) {
            viewModel.atualiza(tarefa);
        } else {
            viewModel.adiciona(tarefa);
        }

        boolean receberNotificacao = checkBoxNotificacao.isChecked();
        if(receberNotificacao){
            setAlarmeParaNotificacao(tarefa);
        }
    }

    private void configuraBtnSelHorario() {
        btnSelecaoHorario = findViewById(R.id.btnHorario);
        btnSelecaoHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar atual = Calendar.getInstance();
                int horaAtual = atual.get(Calendar.HOUR_OF_DAY);
                int minutosAtuais = atual.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(TarefaActivity.this,
                        listenerHorario, horaAtual, minutosAtuais, true);
                dialog.show();
            }
        });
    }

    private void configuraBtnSelData() {
        btnSelecaoData = findViewById(R.id.btnCalendar);
        btnSelecaoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerBuilder builder = new DatePickerBuilder(TarefaActivity.this,
                        listener)
                        .date(Calendar.getInstance())
                        .pickerType(CalendarView.ONE_DAY_PICKER);
                DatePicker datePicker = builder.build();
                datePicker.show();
            }
        });
    }

    private void configuraComponentes() {
        textInputLayout = findViewById(R.id.textInputLayout_titulo);
        inputTitulo = findViewById(R.id.input_titulo);
        inputAnotacao = findViewById(R.id.input_anotacao);
        painelSelecaoData = findViewById(R.id.painelData);
    }

    private void eHEdicaoTarefa(Intent intent) {
        if(intent.hasExtra(EDITAR_TAREFA)){
            getSupportActionBar().setTitle(getString(R.string.titulo_appbar_editar_tarefa));
            tarefa = (Tarefa) intent.getSerializableExtra(EDITAR_TAREFA);
            if(tarefa != null){
                inputTitulo.setText(tarefa.getTitulo());
                inputAnotacao.setText(tarefa.getAnotacao());
                if(tarefa.isDataDefinida()){
                    btnSelecaoData.setText(DataFormatterUtil.formatarData(tarefa.getDataConlusao()));
                    btnSelecaoHorario.setText(DataFormatterUtil.formataHora(tarefa.getDataConlusao()));
                    togglePainelData(true);
                    switchPainel.setChecked(true);
                }
                btnSalvar.setText(R.string.texto_atualizar_tarefa);
            }
            editMode = true;

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
            Intent intent = new Intent(TarefaActivity.this, AlarmReceiver.class);

            byte[] bytesTarefa = ByteArrayHelper.toByteArray(tarefa);
            intent.putExtra(TAREFA_ALARM, bytesTarefa);

            Random random = new Random();
            int nextInt = random.nextInt(FEED_NUMBER);
            long timeInMillis = tarefa.getDataConlusao().getTimeInMillis();

            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(TarefaActivity.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if(alarm != null){
                Log.i("ALARM", "setAlarmeParaNotificacao: Alarme Setado");
                AlarmManagerCompat.setExact(alarm, AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    AlarmReceiver alarmReceiver = new AlarmReceiver();
                    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                    filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                    registerReceiver(alarmReceiver, filter);
                    AlarmManagerCompat.setExact(alarm, AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                }
            }


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
        textInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean dataFoiSelecionada(){
        return !btnSelecaoData.getText().toString().contentEquals(getString(R.string.selecao_data));
    }

    private boolean horaFoiSelecionada(){
        return !btnSelecaoHorario.getText().toString().contentEquals(getString(R.string.selecao_horario));
    }

    private TimePickerDialog.OnTimeSetListener listenerHorario = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hora, int minutos) {
            btnSelecaoHorario.setText(DataFormatterUtil.formataHorario(hora, minutos));
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

}
