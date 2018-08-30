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
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asuprojects.tarefafeita.activity.TarefaActivity;
import com.asuprojects.tarefafeita.adapter.AbasAdapter;
import com.asuprojects.tarefafeita.fragment.ListaFragment;
import com.asuprojects.tarefafeita.fragment.MainFragment;
import com.asuprojects.tarefafeita.fragment.ResumoFragment;
import com.asuprojects.tarefafeita.util.GeradorTarefa;

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

        ListaFragment listaFragment = new ListaFragment();
        MainFragment mainFragment = new MainFragment();

        AbasAdapter abasAdapter = new AbasAdapter(getSupportFragmentManager());
        abasAdapter.adicionar(mainFragment, "Atual");
        abasAdapter.adicionar(listaFragment, "Lista Geral");
        abasAdapter.adicionar(new ResumoFragment(), "Sumario");

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(abasAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        
        btnAdicionarTarefa = findViewById(R.id.btnAdiciona);
        btnAdicionarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Adicionando Tarefa....", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, TarefaActivity.class);
                startActivity(intent);
            }
        });

    }

    public void itemOpcoes(View view){
        Toast.makeText(this, "Menu de Opcoes", Toast.LENGTH_SHORT).show();
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
            case R.id.menu_inbox: {
                Toast.makeText(this, "Menu Inbox", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_starred: {
                Toast.makeText(this, "Menu Favoritos", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_sent_email: {
                Toast.makeText(this, "Menu Emails Enviados", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
