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

public class RecycleAdapterPemesanan extends RecyclerView.Adapter<RecycleAdapterPemesanan.MyViewHolder> {
    private List<OrderDataDAO> result;
    private List<OrderDataDAO> listFull;
    private Context context;

    public RecycleAdapterPemesanan(Context context,List<OrderDataDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterPemesanan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_pemesanan,viewGroup,false);
        final RecycleAdapterPemesanan.MyViewHolder holder = new RecycleAdapterPemesanan.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterPemesanan.MyViewHolder myViewHolder, int i) {
        OrderDataDAO dataDAO = result.get(i);
        myViewHolder.mIdOrder.setText(dataDAO.getIdOrder());
        myViewHolder.mCode.setText(dataDAO.getCode_Sparepart());
        myViewHolder.mIdCabang.setText(dataDAO.getIdCabang());
        myViewHolder.mtanggal.setText(dataDAO.getTanggal());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mIdOrder,mCode,mIdCabang,mtanggal;
        private CardView cardViewOrder;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mIdOrder=itemView.findViewById(R.id.idOrder);
            mCode=itemView.findViewById(R.id.sparepartCodeOrder);
            mIdCabang=itemView.findViewById(R.id.idCabangOrder);
            mtanggal=itemView.findViewById(R.id.tanggalOrder);
            cardViewOrder=itemView.findViewById(R.id.cardViewAdapterSal);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

}
