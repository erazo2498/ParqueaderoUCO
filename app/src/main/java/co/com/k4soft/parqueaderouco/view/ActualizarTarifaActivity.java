package co.com.k4soft.parqueaderouco.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import co.com.k4soft.parqueaderouco.R;
import co.com.k4soft.parqueaderouco.entidades.Tarifa;
import co.com.k4soft.parqueaderouco.persistencia.room.DataBaseHelper;
import co.com.k4soft.parqueaderouco.utilities.ActionBarUtil;


public class ActualizarTarifaActivity extends AppCompatActivity {

    @BindView(R.id.txtNombreTarifa)
    public EditText txtNombreTarifa;

    @BindView(R.id.txtValorTarifa)
    public EditText txtValorTarifa;

    private DataBaseHelper db=DataBaseHelper.getDBMainThread(this);
    private Tarifa tarifa;
    private ActionBarUtil actionBarUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_tarifa);
        ButterKnife.bind(this);
        initComponent();
    }

    private void initComponent() {
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.tarifas));
        int idTarifa= getIntent().getExtras().getInt("ID");
        tarifa=db.getTarifaDAO().getByIdTarifa(Integer.valueOf(idTarifa));
        txtNombreTarifa.setText(tarifa.getNombre());
        txtValorTarifa.setText(String.valueOf(tarifa.getPrecio()));
    }
    private boolean validarInformacion(String nombreTarifa, Double valorTarifa) {
        boolean esValido = true;
        if("".equals(nombreTarifa)){
            esValido = false;
            txtNombreTarifa.setError(getString(R.string.requerido));
        }

        if(valorTarifa== 0){
            esValido = false;
            txtValorTarifa.setError(getString(R.string.requerido));
        }

        return esValido;
    }
    private Double ToDouble(String valor) {
        return  "".equals(valor)?0:Double.parseDouble(valor);
    }

    public void actualizar(View view) {
        String nombreTarifa = txtNombreTarifa.getText().toString();
        Double valorTarifa = toDouble(txtValorTarifa.getText().toString());
        if (validarInformacion(nombreTarifa, valorTarifa)) {
            tarifa = getTarifa(nombreTarifa, valorTarifa);
            new ActualizarTarifa().execute(tarifa);
            finish();
        }
    }

    private Tarifa getTarifa(String nombreTarifa, Double valorTarifa) {
        tarifa.setNombre(nombreTarifa);
        tarifa.setPrecio(valorTarifa);
        return tarifa;
    }

    private Double toDouble(String valor) {
        return "".equals(valor) ? 0:Double.parseDouble(valor);
    }

    private class ActualizarTarifa extends AsyncTask<Tarifa, Void, Void>
    {
        @Override
        protected Void doInBackground(Tarifa... tarifas) {
            DataBaseHelper.getSimpleDB(getApplicationContext()).getTarifaDAO().update(tarifas[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), getString(R.string.actualizacion_satisfactoria), Toast.LENGTH_LONG).show();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
