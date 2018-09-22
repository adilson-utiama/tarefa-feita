package com.asuprojects.tarefafeita.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asuprojects.tarefafeita.MainActivity;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

public class DetalhesActivity extends AppCompatActivity {

    private TextView titulo, anotacao, dataIncluisao, dataConclusao, prioridade;
    private Button btnOK;

    private Tarefa tarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        titulo = findViewById(R.id.tituloTarefaDetalhe);
        anotacao = findViewById(R.id.anotacoesTarefaDetalhes);
        dataIncluisao = findViewById(R.id.dataInclusaoTarefaDetalhe);
        dataConclusao = findViewById(R.id.dataConclusaoTarefaDetalhe);
        prioridade = findViewById(R.id.prioridadeTarefaDetalhes);

        Intent intent = getIntent();
        if(intent.hasExtra("tarefa_notificacao")){
            tarefa = (Tarefa) intent.getSerializableExtra("tarefa_notificacao");
            if(tarefa != null){
                titulo.setText(tarefa.getTitulo());
                anotacao.setText(tarefa.getAnotacao());
                dataIncluisao.setText(DataFormatterUtil.formatarData(tarefa.getDataIncluida()));
                dataConclusao.setText(DataFormatterUtil.formataDataHora(tarefa.getDataConlusao()));
                if(tarefa.getPrioridade().equals(Prioridade.ALTA)){
                    prioridade.setTextColor(Prioridade.ALTA.getCor());
                } else if(tarefa.getPrioridade().equals(Prioridade.MEDIA)){
                    prioridade.setTextColor(Prioridade.MEDIA.getCor());
                } else {
                    prioridade.setTextColor(Prioridade.BAIXA.getCor());
                }
                prioridade.setText(tarefa.getPrioridade().getDescricao());

            }
        }

        btnOK = findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetalhesActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
