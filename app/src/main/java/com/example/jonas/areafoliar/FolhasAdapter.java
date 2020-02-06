package com.example.jonas.areafoliar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonas.areafoliar.database.DadosOpenHelper;
import com.example.jonas.areafoliar.repositorio.FolhasRepositorio;

import java.util.List;

public class FolhasAdapter extends RecyclerView.Adapter<FolhasAdapter.ViewHolderFolhas>{
    public List<String> values;

    private List<Folha> folhas;

    FolhasAdapter(List<Folha> folhas){
        this.folhas = folhas;
    }

    @NonNull
    @Override
    public FolhasAdapter.ViewHolderFolhas onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.linha_config_dados,viewGroup,false);
        return new ViewHolderFolhas(view, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull FolhasAdapter.ViewHolderFolhas viewHolder, int i) {
        if (folhas!= null && (folhas.size() > 0)){
            Folha folha = folhas.get(i);
            viewHolder.edtNome.setText(folha.getNome());
            viewHolder.edtArea.setText(folha.getArea());
            viewHolder.edtArea.setText(folha.getAltura());
            viewHolder.edtLargura.setText(folha.getLargura());
        }
    }

    @Override
    public int getItemCount() {
        return folhas.size();
    }

    public class ViewHolderFolhas extends RecyclerView.ViewHolder{
        public EditText edtNome,edtArea,edtAltura,edtLargura;
        private DadosOpenHelper dadosOpenHelper;
        private FolhasRepositorio folhasRepositorio;
        private SQLiteDatabase conexao;
        private Context contextoApp;



        ViewHolderFolhas(@NonNull final View itemView, final Context context) {
            super(itemView);
            edtNome = itemView.findViewById(R.id.edtNome);
            edtArea = itemView.findViewById(R.id.edtArea);
            edtAltura = itemView.findViewById(R.id.edtAltura);
            edtLargura = itemView.findViewById(R.id.edtLargura);
            contextoApp = context;
            criarConexao();
            folhasRepositorio = new FolhasRepositorio(conexao);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    if(folhas.size() > 0){
                        final Folha folha;
                        folha = folhas.get(getLayoutPosition());
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        final EditText input = new EditText(v.getContext());
                        final TextView txtNomeFolha = new TextView(v.getContext());
                        txtNomeFolha.setText(folha.getNome());
                        builder.setView(txtNomeFolha);
                        builder.setView(input);
                        builder.setPositiveButton("Confirmar Alterações",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //Corrigir isso aqui!!!!
                                Toast.makeText(context, input.getText().toString(), Toast.LENGTH_SHORT).show();
                                try{
                                    folhasRepositorio.alterar(folha.getCodigo(),input.getText().toString());
                                }catch (SQLException ignored){

                                }
                            }
                        });
                        builder.setNegativeButton("Excluir Folha", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                folhasRepositorio.excluir(folha.getCodigo());
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();*/
                        final Dialog dialog = new Dialog(v.getContext());

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_alterarfolha); // seu layout
                        dialog.setCancelable(false);

                        Button cancelar = dialog.findViewById(R.id.excBtn);
                        Button confirmar = dialog.findViewById(R.id.confBtn);
                        final EditText input = dialog.findViewById(R.id.edtNomeAlterarFolha);
                        input.setText(folha.getNome());
                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try{
                                    folhasRepositorio.alterar(folha.getCodigo(),input.getText().toString());
                                    dialog.dismiss(); // fecha o dialog
                                }catch (SQLException ignored){

                                }
                            }
                        });
                        confirmar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                folhasRepositorio.excluir(folha.getCodigo());
                                dialog.dismiss(); // fecha o dialog
                            }
                        });
                        dialog.show();
                    }
                }
            });
        }

        void criarConexao() {
            try {
                dadosOpenHelper = new DadosOpenHelper(contextoApp);
                conexao = dadosOpenHelper.getWritableDatabase();
                folhasRepositorio = new FolhasRepositorio(conexao);
            } catch (SQLException ex) {
                Toast.makeText(contextoApp, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
