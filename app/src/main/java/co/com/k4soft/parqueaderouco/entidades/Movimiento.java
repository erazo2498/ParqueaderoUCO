package co.com.k4soft.parqueaderouco.entidades;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;

import co.com.k4soft.parqueaderouco.persistencia.Tabla;
import co.com.k4soft.parqueaderouco.utilities.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(tableName = Tabla.MOVIMIENTO)
@NoArgsConstructor
public class Movimiento {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "idMovimiento")
    private Integer idMovimiento;

    @ColumnInfo(name = "idTarifa")
    private Integer idTarifa;

    @ColumnInfo(name ="placa")
    private String placa;

    @ColumnInfo(name ="fechaEntrada")
    private String fechaEntrada;

    @ColumnInfo(name ="fechaSalida")
    private String fechaSalida;

    @ColumnInfo(name ="finalizaMovimiento")
    private boolean finalizaMovimiento;

    @ColumnInfo(name = "valorTotal")
    private String valorTotal;

    public void actualizarValorTotal(double precioTarifa) throws ParseException {
        int horasTranscurridas= DateUtil.hoursElapsed(this.fechaEntrada,this.fechaSalida);
        valorTotal = String.valueOf(precioTarifa*horasTranscurridas);
    }

}
