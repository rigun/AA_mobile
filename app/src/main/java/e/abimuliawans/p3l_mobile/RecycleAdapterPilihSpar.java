package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RecycleAdapterPilihSpar extends RecyclerView.Adapter<RecycleAdapterPilihSpar.MyViewHolder> {

    private List<VehicleDAO> result;
    private List<VehicleDAO> listFull;
    private Context context;

    public RecycleAdapterPilihSpar(Context context,List<VehicleDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterPilihSpar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_vehicle,viewGroup,false);
        final RecycleAdapterPilihSpar.MyViewHolder holder = new RecycleAdapterPilihSpar.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterPilihSpar.MyViewHolder myViewHolder, final int i) {
        final VehicleDAO vhc = result.get(i);
        myViewHolder.mMerk.setText(vhc.getMerkVehicle());
        myViewHolder.mType.setText(vhc.getTypeVehicle());
        myViewHolder.mId.setText(vhc.getIdVehicle());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TambahSparepartActivity.class);
                intent.putExtra("id", vhc.getIdVehicle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mType,mMerk,mId;
        private CardView cardView;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mType=itemView.findViewById(R.id.typeVehicle);
            mMerk=itemView.findViewById(R.id.merkVehicle);
            mId=itemView.findViewById(R.id.idVehicle);
            cardView=itemView.findViewById(R.id.cardViewVeh);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
