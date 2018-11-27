package com.asuprojects.tarefafeita.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asuprojects.tarefafeita.MainActivity;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

public class DetalhesActivity extends AppCompatActivity {

    public static final String TAREFA_NOTIFICACAO = "tarefa_notificacao";
    private TextView titulo, anotacao, dataIncluisao, dataConclusao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        configuraComponentes();

        Intent intent = getIntent();
        eHNotificacaoTarefa(intent);

        configuraBtnOk();
    }

    private void configuraComponentes() {
        titulo = findViewById(R.id.tituloTarefaDetalhe);
        anotacao = findViewById(R.id.anotacoesTarefaDetalhes);
        dataIncluisao = findViewById(R.id.dataInclusaoTarefaDetalhe);
        dataConclusao = findViewById(R.id.dataConclusaoTarefaDetalhe);
    }

    private void configuraBtnOk() {
        Button btnOK = findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetalhesActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void eHNotificacaoTarefa(Intent intent) {
        if(intent.hasExtra(TAREFA_NOTIFICACAO)){
            Tarefa tarefa = (Tarefa) intent.getSerializableExtra(TAREFA_NOTIFICACAO);
            if(tarefa != null){
                titulo.setText(tarefa.getTitulo());
                anotacao.setText(tarefa.getAnotacao());
                dataIncluisao.setText(DataFormatterUtil.formatarData(tarefa.getDataIncluida()));
                dataConclusao.setText(DataFormatterUtil.formataDataHora(tarefa.getDataConlusao()));
            }
        }
    }
}
