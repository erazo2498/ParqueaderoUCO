package co.com.k4soft.parqueaderouco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.k4soft.parqueaderouco.R;
import co.com.k4soft.parqueaderouco.entidades.Movimiento;
import co.com.k4soft.parqueaderouco.entidades.Tarifa;
import co.com.k4soft.parqueaderouco.persistencia.room.DataBaseHelper;
import co.com.k4soft.parqueaderouco.utilities.DateUtil;

public class MovimientoAdapter extends BaseAdapter implements Filterable {

    private final LayoutInflater inflater;
    private List<Movimiento> listaMovimientoOut;
    private  List<Movimiento> listaMovimientoIn;

    public MovimientoAdapter(Context context, List<Movimiento> listaMovimientos) {
        listaMovimientoOut = listaMovimientos;
        listaMovimientoIn = listaMovimientoOut;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listaMovimientoOut.size();
    }

    @Override
    public Object getItem(int position) {
        return listaMovimientoOut.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.movimiento_item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if(!listaMovimientoOut.get(position).isFinalizaMovimiento()) {
            holder.cardView.setBackgroundColor(Color.RED);
            holder.txtplaca.setText("Placa: " + listaMovimientoOut.get(position).getPlaca());
            holder.txtFechaIngreso.setText("Fecha de ingreso: " + listaMovimientoOut.get(position).getFechaEntrada());
            holder.txtFechaSalida.setText("El vehiculo aún se encuentra en el parqueadero");
            holder.txtTotal.setVisibility(View.GONE);
        }else {
                holder.cardView.setBackgroundColor(Color.WHITE);
                holder.txtTotal.setVisibility(View.VISIBLE);
                holder.txtplaca.setText("Placa: " + listaMovimientoOut.get(position).getPlaca());
                holder.txtFechaIngreso.setText("Fecha de ingreso: " + listaMovimientoOut.get(position).getFechaEntrada());
                holder.txtFechaSalida.setText("Fecha de salida: " + listaMovimientoOut.get(position).getFechaSalida());
                holder.txtTotal.setText("Valor pagado: " + listaMovimientoOut.get(position).getValorTotal());
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaMovimientoOut = (List<Movimiento>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Movimiento> FilteredArrList = new ArrayList<>();
                if (listaMovimientoIn == null) {
                    listaMovimientoIn = new ArrayList<>(listaMovimientoOut);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = listaMovimientoIn.size();
                    results.values = listaMovimientoIn;
                } else {

                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < listaMovimientoIn.size(); i++) {
                        String data = listaMovimientoIn.get(i).getPlaca();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(listaMovimientoIn.get(i));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    class ViewHolder{
        @BindView(R.id.textPlaca)
        TextView txtplaca;
        @BindView(R.id.textFechaIngreso)
        TextView txtFechaIngreso;
        @BindView(R.id.textFechaSalida)
        TextView txtFechaSalida;
        @BindView(R.id.textValorPagado)
        TextView txtTotal;
        @BindView(R.id.cardView)
        CardView cardView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
