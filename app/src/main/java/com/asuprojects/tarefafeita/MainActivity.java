package com.asuprojects.tarefafeita;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asuprojects.tarefafeita.activity.ConfiguracoesActivity;
import com.asuprojects.tarefafeita.activity.TarefaActivity;
import com.asuprojects.tarefafeita.adapter.AbasAdapter;
import com.asuprojects.tarefafeita.fragment.ListaGeralFragment;
import com.asuprojects.tarefafeita.fragment.ListaDoDiaFragment;
import com.asuprojects.tarefafeita.fragment.ResumoFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Toolbar toolbar;
    private FloatingActionButton btnAdicionarTarefa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_tarefa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tarefa Feita");

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this,
                        drawerLayout, toolbar,
                        R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ListaGeralFragment listaFragment = new ListaGeralFragment();
        ListaDoDiaFragment mainFragment = new ListaDoDiaFragment();

        AbasAdapter abasAdapter = new AbasAdapter(getSupportFragmentManager());
        abasAdapter.adicionar(mainFragment, "Atual");
        abasAdapter.adicionar(listaFragment, "Lista Geral");
        abasAdapter.adicionar(new ResumoFragment(), "Resumo");

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(abasAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        
        btnAdicionarTarefa = findViewById(R.id.btnAdiciona);
        btnAdicionarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                startActivity(intent);
            }
        });

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
                Toast.makeText(this, "Saindo do App", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
                Toast.makeText(this, "Menu Sobre Clicado", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
