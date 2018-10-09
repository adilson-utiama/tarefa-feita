package com.asuprojects.tarefafeita.activity;

import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.asuprojects.tarefafeita.R;

public class ConfiguracoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        configuraToolBar();

        getFragmentManager().beginTransaction()
                .replace(R.id.configuracoes, new ConfiguracoesFragment())
                .commit();
    }

    private void configuraToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_configuracoes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.configuracoes_titulo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class ConfiguracoesFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
