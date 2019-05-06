package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterSparepartCabang extends RecyclerView.Adapter<RecycleAdapterSparepartCabang.MyViewHolder> {
    private List<SparepartCabangSupDAO> result;
    private List<SparepartCabangSupDAO> listFull;
    private Context context;

    public RecycleAdapterSparepartCabang(Context context,List<SparepartCabangSupDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSparepartCabang.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_pemesanan,viewGroup,false);
        final RecycleAdapterSparepartCabang.MyViewHolder holder = new RecycleAdapterSparepartCabang.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSparepartCabang.MyViewHolder myViewHolder, int i) {
        SparepartCabangSupDAO sparepartCabangSupDAO = result.get(i);

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mIdOrder,mIdSupplier,mIdCabang,mtanggal;
        private CardView cardViewOrder;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mIdOrder=itemView.findViewById(R.id.idOrder);
            mIdSupplier=itemView.findViewById(R.id.supplierIDOrder);
            mIdCabang=itemView.findViewById(R.id.idCabangOrder);
            mtanggal=itemView.findViewById(R.id.tanggalOrder);
            cardViewOrder=itemView.findViewById(R.id.cardViewAdapterOrder);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

}
