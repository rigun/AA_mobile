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

public class RecycleAdapterKendaraanKonsumen extends RecyclerView.Adapter<RecycleAdapterKendaraanKonsumen.MyViewHolder>{
    private List<KendaraanKonsumenDAO> result;
    private List<KendaraanKonsumenDAO> listFull;
    private Context context;

    public RecycleAdapterKendaraanKonsumen(Context context,List<KendaraanKonsumenDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterKendaraanKonsumen.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_kendaraan_konsumen,viewGroup,false);
        final RecycleAdapterKendaraanKonsumen.MyViewHolder holder = new RecycleAdapterKendaraanKonsumen.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterKendaraanKonsumen.MyViewHolder myViewHolder, int i) {
        final KendaraanKonsumenDAO ken = result.get(i);
        myViewHolder.mId.setText(ken.getIdKendaraanKonsumen());
        myViewHolder.mPlat.setText(ken.getPlatNomorKendaraanKonsumen());
        myViewHolder.mMerk.setText(ken.getDetailKendaraanKonsumen().getMerkVehicle());
        myViewHolder.mNama.setText(ken.getDetailKendaraanKonsumen().getTypeVehicle());

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mId,mPlat,mMerk,mNama;
        private CardView cardView;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mId=itemView.findViewById(R.id.idKendaraanKonsumen);
            mPlat=itemView.findViewById(R.id.platKendaraanKonsumen);
            mMerk=itemView.findViewById(R.id.merkKendaraanKonsumen);
            mNama=itemView.findViewById(R.id.namaKendaraanKonsumen);
            cardView=itemView.findViewById(R.id.cardViewAdapterTransDetail);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }
}
