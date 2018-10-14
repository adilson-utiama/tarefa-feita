package com.asuprojects.tarefafeita;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.asuprojects.tarefafeita.activity.ConfiguracoesActivity;
import com.asuprojects.tarefafeita.activity.SobreActivity;
import com.asuprojects.tarefafeita.activity.TarefaActivity;
import com.asuprojects.tarefafeita.adapter.AbasAdapter;
import com.asuprojects.tarefafeita.database.repository.TarefaRepository;
import com.asuprojects.tarefafeita.fragment.ListaDoDiaFragment;
import com.asuprojects.tarefafeita.fragment.ListaGeralFragment;
import com.asuprojects.tarefafeita.fragment.ListaPrioridadeIndefinidaFragment;
import com.asuprojects.tarefafeita.fragment.ResumoFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private AdView mAdView;
    LinearLayoutCompat container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.linearLayoutCompat);

        configuraAdView();

        if(isConected()){
            mostraAnuncioAdView();
        }else{
            container.removeView(mAdView);
        }

        TarefaRepository repository = new TarefaRepository(getApplication());

        verificaTarefasAntigas(repository);

        Toolbar toolbar = configuraToolbar();

        configuraNavigationDrawer(toolbar);

        configuraViewPager();

        configuraBotaoAdicionaTarefa();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuitem_cofiguracoes:
                startActivity(new Intent(MainActivity.this, ConfiguracoesActivity.class));
                return true;
            case R.id.menuitem_sair:
                mostraDialogSairDoApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            if(!isConected()){
                container.removeView(mAdView);
            } else {
                container.removeView(mAdView);
                mostraAnuncioAdView();
            }
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mAdView != null) {
            if(!isConected()){
                container.removeView(mAdView);
            } else {
                container.removeView(mAdView);
                mostraAnuncioAdView();
            }
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            container.removeView(mAdView);
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void mostraAnuncioAdView() {
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

        container.addView(mAdView, params);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void configuraAdView() {
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");
        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
    }

    private void mostraDialogSairDoApp() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.sair_app_msg)
                .setPositiveButton(getString(R.string.opcao_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(getString(R.string.opcao_nao), null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_configuracoes: {
                startActivity(new Intent(MainActivity.this, ConfiguracoesActivity.class));
                break;
            }
            case R.id.menu_sobre: {
                startActivity(new Intent(MainActivity.this, SobreActivity.class));
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void configuraBotaoAdicionaTarefa() {
        FloatingActionButton btnAdicionarTarefa = findViewById(R.id.btnAdiciona);
        btnAdicionarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaParaNovaTarefa();
            }
        });
    }

    private void vaParaNovaTarefa() {
        Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
        startActivity(intent);
    }

    private void configuraViewPager() {
        AbasAdapter abasAdapter = new AbasAdapter(getSupportFragmentManager());
        abasAdapter.adicionar(new ListaDoDiaFragment(), getString(R.string.aba_tarefas_do_dia));
        abasAdapter.adicionar(new ListaPrioridadeIndefinidaFragment(), getString(R.string.aba_tarefas_sem_data));
        abasAdapter.adicionar(new ListaGeralFragment(), getString(R.string.aba_tarefas_geral));
        abasAdapter.adicionar(new ResumoFragment(), getString(R.string.aba_tarefas_resumo));

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(abasAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void configuraNavigationDrawer(Toolbar toolbar) {
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this,
                        drawerLayout, toolbar,
                        R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private Toolbar configuraToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_tarefa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        return toolbar;
    }

    private void verificaTarefasAntigas(TarefaRepository repository) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean apagarTarefasAntigas = preferences.getBoolean(getString(R.string.apagar_tarefas_antigas), false);
        if(apagarTarefasAntigas){
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DAY_OF_MONTH, -30);
            repository.apagarTarefasAntigas(instance);
        }
    }

    private boolean isConected(){
        ConnectivityManager conmag = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conmag != null ) {
            conmag.getActiveNetworkInfo();
            //Verifica internet pela WIFI
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                return true;
            }
            //Verifica se tem internet móvel
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
                return true;
            }
        }
        return false;
    }
}
