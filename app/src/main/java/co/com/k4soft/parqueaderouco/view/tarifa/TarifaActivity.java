package co.com.k4soft.parqueaderouco.view.tarifa;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.k4soft.parqueaderouco.R;
import co.com.k4soft.parqueaderouco.entidades.Tarifa;
import co.com.k4soft.parqueaderouco.persistencia.room.DataBaseHelper;
import co.com.k4soft.parqueaderouco.utilities.ActionBarUtil;

public class TarifaActivity extends AppCompatActivity {

    private ActionBarUtil actionBarUtil;
    @BindView(R.id.listViewTarifas)
    public ListView listViewTarifas;
    public List<Tarifa> listaTarifas;
    DataBaseHelper db;

    public void goToRegistroTarifa(View view) {
        Intent intent = new Intent(this, RegistroTarifaActivity.class);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);
        ButterKnife.bind(this);
        initComponents();
        loadTarifas();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loadTarifas() {
        listaTarifas = db.getTarifaDAO().listar();
        if (listaTarifas.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.sin_tarifas, Toast.LENGTH_SHORT).show();
        } else {
            String[] tarifasArray = new String[listaTarifas.size()];
            for (int i = 0; i < listaTarifas.size(); i++) {
                tarifasArray[i] = listaTarifas.get(i).getNombre();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, tarifasArray);
            listViewTarifas.setAdapter(arrayAdapter);
        }
    }


    private void initComponents() {
        db = DataBaseHelper.getDBMainThread(this);
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.tarifas));
        onItemClickListener();
    }

    private void onItemClickListener() {
        listViewTarifas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ActualizarTarifaActivity.class);
                intent.putExtra("ID", getTarifaId(position));
                startActivity(intent);
            }

            private int getTarifaId(int position) {
                return listaTarifas.get(position).getIdTarifa();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRestart() {
        super.onRestart();
        loadTarifas();
    }

}
