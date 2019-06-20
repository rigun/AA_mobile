package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecycleAdapterCabang extends RecyclerView.Adapter<RecycleAdapterCabang.MyViewHolder>  {

    private List<CabangDAO> result;
    private List<CabangDAO> listFull;
    private Context context;

    public RecycleAdapterCabang(Context context,List<CabangDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterCabang.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_cabang,viewGroup,false);
        final RecycleAdapterCabang.MyViewHolder holder = new RecycleAdapterCabang.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterCabang.MyViewHolder myViewHolder, int i) {
        CabangDAO cab = result.get(i);
        myViewHolder.mId.setText(cab.getIdCabang());
        myViewHolder.mName.setText(cab.getNamaCabang());

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mName,mId;
        private CardView cardViewCabang;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mName=itemView.findViewById(R.id.nameCabang);
            mId=itemView.findViewById(R.id.idCabang);
            cardViewCabang=itemView.findViewById(R.id.cardViewAdapterCabang);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }
}
