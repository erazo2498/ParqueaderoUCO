package co.com.k4soft.parqueaderouco.view.movimiento;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.k4soft.parqueaderouco.R;
import co.com.k4soft.parqueaderouco.adapters.MovimientoAdapter;
import co.com.k4soft.parqueaderouco.entidades.Movimiento;
import co.com.k4soft.parqueaderouco.persistencia.room.DataBaseHelper;
import co.com.k4soft.parqueaderouco.utilities.ActionBarUtil;

public class ListaMovimientoActivity extends AppCompatActivity {
    private ActionBarUtil actionBarUtil;
    @BindView(R.id.listViewMovimiento)
    public ListView listViewMovimiento;
    private MovimientoAdapter movimientoAdapter;
    public List<Movimiento> listaMovimiento;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_movimiento);
        ButterKnife.bind(this);
        initComponents();
        loadMovimientos();
    }

    private void initComponents() {
        db = DataBaseHelper.getDBMainThread(this);
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.movimientos));
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
        loadMovimientos();
    }

    private void loadMovimientos() {
        listaMovimiento = db.getMovimientoDAO().listar();
        if (listaMovimiento.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.sin_movimientos, Toast.LENGTH_SHORT).show();
        } else {
            movimientoAdapter = new MovimientoAdapter(this, listaMovimiento);
            listViewMovimiento.setAdapter(movimientoAdapter);
        }
    }
}
