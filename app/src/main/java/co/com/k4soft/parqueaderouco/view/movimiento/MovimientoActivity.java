package co.com.k4soft.parqueaderouco.view.movimiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.MediaRouteButton;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.k4soft.parqueaderouco.R;
import co.com.k4soft.parqueaderouco.entidades.Movimiento;
import co.com.k4soft.parqueaderouco.entidades.Tarifa;
import co.com.k4soft.parqueaderouco.persistencia.room.DataBaseHelper;
import co.com.k4soft.parqueaderouco.utilities.ActionBarUtil;

public class MovimientoActivity extends AppCompatActivity {

    private DataBaseHelper db;
    private ActionBarUtil actionBarUtil;
    @BindView(R.id.txtPlaca)
    public EditText txtPlaca;
    @BindView(R.id.tipoTarifaSpinner)
    public Spinner tipoTarifaSpinner;
    @BindView(R.id.btnIngreso)
    public Button btnIngreso;
    @BindView(R.id.btnSalida)
    public Button btnSalida;
    @BindView(R.id.layoutDatos)
    public ConstraintLayout layoutDatos;
    private List<Tarifa> listaTarifas;
    private Movimiento movimiento;
    private Tarifa tarifa;
    private String[] arrayTarifas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento);
        ButterKnife.bind(this);
        initComponents();
        hideComponents();
    }

    private void hideComponents() {
        tipoTarifaSpinner.setVisibility(View.GONE);
        btnIngreso.setVisibility(View.GONE);
        btnSalida.setVisibility(View.GONE);
        layoutDatos.setVisibility(View.GONE);
    }

    private void initComponents() {
        db = DataBaseHelper.getDBMainThread(this);
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.registrar_ingreso_salida));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void buscarPlaca(View view) {
        movimiento = db.getMovimientoDAO().findByPLaca(txtPlaca.getText().toString());
        if(movimiento == null){
            showComponentesIngreso();
        }else{
            showComponentesSalida();
        }
    }

    private void showComponentesSalida() {
        btnSalida.setVisibility(View.VISIBLE);
        layoutDatos.setVisibility(View.VISIBLE);
    }

    private void showComponentesIngreso() {
        tipoTarifaSpinner.setVisibility(View.VISIBLE);
        btnIngreso.setVisibility(View.VISIBLE);

    }
}
