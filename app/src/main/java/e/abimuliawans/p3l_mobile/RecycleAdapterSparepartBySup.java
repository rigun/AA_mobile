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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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

public class RecycleAdapterSparepartBySup extends RecyclerView.Adapter<RecycleAdapterSparepartBySup.MyViewHolder> {
    private List<SparepartDAO> result;
    private List<SparepartDAO> listFull;
    private Context context;

    public RecycleAdapterSparepartBySup(Context context,List<SparepartDAO> result){
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSparepartBySup.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_sparepart,viewGroup,false);
        final RecycleAdapterSparepartBySup.MyViewHolder holder = new RecycleAdapterSparepartBySup.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSparepartBySup.MyViewHolder myViewHolder, int i){
        final SparepartDAO spr = result.get(i);
        myViewHolder.nameSparepart.setText(spr.getNamaSparepart());
        myViewHolder.codeSparepart.setText(spr.getCodeSparepart());
        myViewHolder.typeSparepart.setText(spr.getTypeSparepart());
        myViewHolder.merkSparepart.setText(spr.getMerkSparepart());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_cloud_upload);
        requestOptions.error(R.drawable.ic_cloud_upload);

        Glide.with(context)
                .load("http://10.53.15.204/images/sparepart/"+spr.getPicSparepart())
                .apply(requestOptions)
                .into(myViewHolder.gambarSpart);


        myViewHolder.cardSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Asset Order
                SharedPreferences prefSupplier = context.getSharedPreferences("MyOrder", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefSupplier.edit();
                editor.putString("code", myViewHolder.codeSparepart.getText().toString());
                editor.commit();
                Intent intent = new Intent(context,TambahPemasananActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView codeSparepart,nameSparepart,merkSparepart,typeSparepart;
        private ImageView gambarSpart;
        private CardView cardSparepart;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            codeSparepart=(TextView) itemView.findViewById(R.id.codeSparepart);
            nameSparepart=(TextView) itemView.findViewById(R.id.nameSparepart);
            merkSparepart=(TextView) itemView.findViewById(R.id.merkSparepart);
            typeSparepart=(TextView) itemView.findViewById(R.id.typeSparepart);
            gambarSpart=(ImageView) itemView.findViewById(R.id.gambarSparepart);
            cardSparepart=(CardView) itemView.findViewById(R.id.cardSparepart);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }
}
