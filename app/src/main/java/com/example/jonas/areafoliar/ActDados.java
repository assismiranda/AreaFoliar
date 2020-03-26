package com.example.jonas.areafoliar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jonas.areafoliar.database.DadosOpenHelper;
import com.example.jonas.areafoliar.repositorio.FolhasRepositorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActDados extends AppCompatActivity{
    private RecyclerView listDados;
    private SQLiteDatabase conexao;
    private FolhasRepositorio folhasRepositorio;
    private String diaAtual,mesAtual,nomeMesAtual,diaFolha,mesFolha;
    public static List<Folha> dados;
    private ArrayList<Folha> maisAntigo = new ArrayList<>();
    private ArrayList<Folha> mesPassado = new ArrayList<>();
    private ArrayList<Folha> mesPresente = new ArrayList<>();
    private ArrayList<Folha> maisRecentes = new ArrayList<>();
    private Folhas2Adapter folhas2Adapter;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dados);
        //ArrayList<Historico> historicos = new ArrayList<>();
        listDados = findViewById(R.id.listDados);
        //ConstraintLayout layoutContentMain = findViewById(R.id.layoutContentDados);
        //CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        criarConexao();
        listDados.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listDados.setLayoutManager(linearLayoutManager);
        folhasRepositorio = new FolhasRepositorio(conexao);
        dados = folhasRepositorio.consultar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        diaAtual = data_completa.substring(0,2);
        mesAtual = data_completa.substring(3,5);
        switch (mesAtual) {
            case "01":
                nomeMesAtual = "Janeiro";
                break;
            case "02":
                nomeMesAtual = "Fevereiro";
                break;
            case "03":
                nomeMesAtual = "Março";
                break;
            case "04":
                nomeMesAtual = "Abril";
                break;
            case "05":
                nomeMesAtual = "Maio";
                break;
            case "06":
                nomeMesAtual = "Junho";
                break;
            case "07":
                nomeMesAtual = "Julho";
                break;
            case "08":
                nomeMesAtual = "Agosto";
                break;
            case "09":
                nomeMesAtual = "Setembro";
                break;
            case "10":
                nomeMesAtual = "Outubro";
                break;
            case "11":
                nomeMesAtual = "Novembro";
                break;
            default:
                nomeMesAtual = "Dezembro";
                break;
        }
        for(int i = 0; i < dados.size(); i ++){
            diaFolha = dados.get(i).getData().substring(0,2);
            mesFolha = dados.get(i).getData().substring(3,5);
            if(diaFolha.equals(diaAtual) && mesFolha.equals(mesAtual)){
                maisRecentes.add(dados.get(i));
            }else if(mesFolha.equals(mesAtual)){
                mesPresente.add(dados.get(i));
            }else if(Integer.parseInt(mesFolha) == (Integer.parseInt((mesAtual)) - 1)){
                mesPassado.add(dados.get(i));
            }else{
                maisAntigo.add(dados.get(i));
            }
        }
        ArrayList<Historico> historicos = new ArrayList<>();
        Historico antigos = new Historico("Mais antigo",calculaMedia(maisAntigo));
        historicos.add(antigos);
        Historico passado = new Historico("Mês passado",calculaMedia(mesPassado));
        historicos.add(passado);
        Historico presente = new Historico(nomeMesAtual,calculaMedia(mesPresente));
        historicos.add(presente);
        Historico recente = new Historico("Recentes",calculaMedia(maisRecentes));
        historicos.add(recente);
        folhas2Adapter = new Folhas2Adapter(historicos);
        //folhasAdapter = new FolhasAdapter(dados);
        //listDados.setAdapter(folhasAdapter);
        listDados.setAdapter(folhas2Adapter);
    }

    protected ArrayList<Folha> calculaMedia(ArrayList<Folha> folhasCarregadas){
        //Array de medias dos testes
        ArrayList<Folha> medias = new ArrayList<>();
        for(int i = 0; i < folhasCarregadas.size(); i ++){
            if(folhasCarregadas.get(i).getTipo() == 1){
                medias.add(folhasCarregadas.get(i));
            }
        }
        return medias;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        dados = folhasRepositorio.consultar();
        maisRecentes.clear();
        mesPresente.clear();
        mesPassado.clear();
        maisAntigo.clear();
        for(int i = 0; i < dados.size(); i ++){
            diaFolha = dados.get(i).getData().substring(0,2);
            mesFolha = dados.get(i).getData().substring(3,5);
            if(diaFolha.equals(diaAtual) && mesFolha.equals(mesAtual)){
                maisRecentes.add(dados.get(i));
            }else if(mesFolha.equals(mesAtual)){
                mesPresente.add(dados.get(i));
            }else if(Integer.parseInt(mesFolha) == (Integer.parseInt((mesAtual)) - 1)){
                mesPassado.add(dados.get(i));
            }else{
                maisAntigo.add(dados.get(i));
            }
        }
        ArrayList<Historico> historicos = new ArrayList<>();
        Historico antigos = new Historico("Mais antigo",calculaMedia(maisAntigo));
        historicos.add(antigos);
        Historico passado = new Historico("Mês passado",calculaMedia(mesPassado));
        historicos.add(passado);
        Historico presente = new Historico(nomeMesAtual,calculaMedia(mesPresente));
        historicos.add(presente);
        Historico recente = new Historico("Recentes",calculaMedia(maisRecentes));
        historicos.add(recente);

        folhas2Adapter = new Folhas2Adapter(historicos);
        //folhasAdapter = new FolhasAdapter(dados);
        //listDados.setAdapter(folhasAdapter);
        listDados.setAdapter(folhas2Adapter);
    }

    public void criarConexao(){
        try{
            DadosOpenHelper dadosOpenHelper = new DadosOpenHelper(this);
            conexao = dadosOpenHelper.getWritableDatabase();
            folhasRepositorio = new FolhasRepositorio(conexao);
            //Toast.makeText(getApplicationContext(), "Conexão criada com sucesso", Toast.LENGTH_SHORT).show();
        }catch (SQLException ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_dados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_dados) {
            /*final AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
            final View customLayout = getLayoutInflater().inflate(R.layout.dialog_dados_info, null);
            builderDialog.setView(customLayout);
            final AlertDialog dialog = builderDialog.create();
            dialog.show();*/

            final Dialog dialog = new Dialog(this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_dados_info); // seu layout
            dialog.setCancelable(false);

            Button fechar = dialog.findViewById(R.id.fecharBtn);
            fechar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss(); // fecha o dialog
                    } catch (SQLException ignored) {

                    }
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent it2 = new Intent(this, ActMain.class);
        startActivity(it2);
    }
}
