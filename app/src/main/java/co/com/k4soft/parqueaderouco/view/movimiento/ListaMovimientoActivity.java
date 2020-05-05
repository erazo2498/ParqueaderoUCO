package co.com.k4soft.parqueaderouco.view.movimiento;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
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
    @BindView(R.id.txtFechaInicial)
    public TextView fechaInicio;
    @BindView(R.id.txtFechaFinal)
    public TextView fechaFinal;
    @BindView(R.id.btnBuscar)
    public Button btnBuscar;
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
        fechaInicio.setOnClickListener(this::onClick);
        fechaFinal.setOnClickListener(this::onClick);
        btnBuscar.setOnClickListener(this::onClick);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v){
        if (v==fechaInicio){
            showDatePickerDialog(fechaInicio);
        }
        else if(v==fechaFinal){
            showDatePickerDialog(fechaFinal);
        }
        else if(v==btnBuscar){

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePickerDialog(TextView fecha){
        Calendar calendar= Calendar.getInstance();
        int dia=calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int mes=calendar.getInstance().get(Calendar.MONTH);
        int ano=calendar.getInstance().get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                },ano,mes,dia);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }
}
