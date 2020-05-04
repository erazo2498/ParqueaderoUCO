package co.com.k4soft.parqueaderouco.view.movimiento;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.k4soft.parqueaderouco.R;
import co.com.k4soft.parqueaderouco.entidades.Movimiento;
import co.com.k4soft.parqueaderouco.entidades.Tarifa;
import co.com.k4soft.parqueaderouco.persistencia.room.DataBaseHelper;
import co.com.k4soft.parqueaderouco.utilities.ActionBarUtil;
import co.com.k4soft.parqueaderouco.utilities.DateUtil;

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
    @BindView(R.id.txtCantidadHoras)
    public TextView txtCantidadHoras;
    @BindView(R.id.lbCantidadHoras)
    public TextView lbCantidadHoras;
    @BindView(R.id.txtTiempoTranscurrido)
    public TextView txtTiempoTrancurrido;
    @BindView(R.id.txtTotal)
    public TextView txtTotal;
    @BindView(R.id.lbTotal)
    public TextView lbTotal;
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
        loadSpinner();
        spinnerOnItemSelected();
    }

    private void spinnerOnItemSelected() {
        tipoTarifaSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tarifa = listaTarifas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadSpinner() {
        listaTarifas = db.getTarifaDAO().listar();
        if(listaTarifas.isEmpty()){
            Toast.makeText(getApplication(),R.string.sin_tarifas,Toast.LENGTH_SHORT).show();
            finish();
        }else{
            arrayTarifas = new String[listaTarifas.size()];
            for (int i=0; i< listaTarifas.size(); i++){
                arrayTarifas[i]= listaTarifas.get(i).getNombre();
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayTarifas);
            tipoTarifaSpinner.setAdapter(arrayAdapter);
        }
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

    public void buscarPlaca(View view) throws ParseException {
        movimiento = db.getMovimientoDAO().findByPLaca(txtPlaca.getText().toString());
        hideComponents();
        if(movimiento == null){
            showComponentesIngreso();
        }else{
            if (movimiento.isFinalizaMovimiento()) {
                btnIngreso.setVisibility(View.VISIBLE);
                showComponentesSalida();
            }{
                btnSalida.setVisibility(View.VISIBLE);
                showComponentesSalida();
            }
        }
    }

    private void showComponentesSalida() throws ParseException {
        layoutDatos.setVisibility(View.VISIBLE);
        String fechaActual = DateUtil.convertDateToStringWithHour(new Date());
        tarifa = db.getTarifaDAO().getByIdTarifa(movimiento.getIdTarifa());
        txtTiempoTrancurrido.setText(DateUtil.timeFromDates(movimiento.getFechaEntrada(),fechaActual));
        if (!movimiento.isFinalizaMovimiento()){
            int cantidadHorasActual = DateUtil.hoursElapsed( movimiento.getFechaEntrada() , fechaActual);
            txtCantidadHoras.setText( cantidadHorasActual + ((cantidadHorasActual>1)?" Horas": " Hora"));
            txtTotal.setText(String.valueOf(tarifa.getPrecio()*cantidadHorasActual));
        }else{
            int cantidadHorasActual = DateUtil.hoursElapsed( movimiento.getFechaEntrada() , movimiento.getFechaSalida());
            txtCantidadHoras.setText( cantidadHorasActual + ((cantidadHorasActual>1)?" Horas": " Hora"));
            txtTotal.setText(String.valueOf(tarifa.getPrecio()*cantidadHorasActual));
        }
    }

    private void showComponentesIngreso() {
        tipoTarifaSpinner.setVisibility(View.VISIBLE);
        btnIngreso.setVisibility(View.VISIBLE);

    }

    public void registrarIngreso(View view) {
        if(tarifa == null){
            Toast.makeText(getApplicationContext(),R.string.debe_seleccionar_tarifa,Toast.LENGTH_SHORT).show();
        }
        else if(movimiento == null){
            movimiento = new Movimiento();
            movimiento.setPlaca(txtPlaca.getText().toString());
            movimiento.setIdTarifa(tarifa.getIdTarifa());
            movimiento.setFechaEntrada(DateUtil.convertDateToStringWithHour(new Date()));
            new insertarMovimiento().execute(movimiento);
            movimiento=null;
            hideComponents();
        }
    }

    public void registrarSalida(View view) throws ParseException {
        Toast.makeText(getApplicationContext(),R.string.informacion_guardada_exitosamente,Toast.LENGTH_SHORT).show();
        tarifa = db.getTarifaDAO().getByIdTarifa(movimiento.getIdTarifa());
        movimiento.setFechaSalida(DateUtil.convertDateToStringWithHour(new Date()));
        movimiento.setFinalizaMovimiento(true);
        movimiento.actualizarValorTotal(tarifa.getPrecio());
        new actualizarMovimiento().execute(movimiento);
        showComponentesSalida();
        btnSalida.setVisibility(View.GONE);
    }

    private class actualizarMovimiento extends AsyncTask<Movimiento,Void,Void> {
        @Override
        protected Void doInBackground(Movimiento... movimientos) {
            DataBaseHelper.getSimpleDB(getApplicationContext()).getMovimientoDAO().update(movimientos[0]);
            return null;
        }
        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),R.string.informacion_guardada_exitosamente,Toast.LENGTH_SHORT).show();
        }
    }
    private class insertarMovimiento extends AsyncTask<Movimiento,Void,Void>{

        @Override
        protected Void doInBackground(Movimiento... movimientos) {
            DataBaseHelper.getSimpleDB(getApplicationContext()).getMovimientoDAO().insert(movimientos[0]);
            return null;
        }

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),R.string.informacion_guardada_exitosamente,Toast.LENGTH_SHORT).show();
        }
    }
}
