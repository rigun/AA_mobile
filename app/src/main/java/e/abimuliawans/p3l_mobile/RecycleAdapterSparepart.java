package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

public class RecycleAdapterSparepart extends RecyclerView.Adapter<RecycleAdapterSparepart.MyViewHolder> implements Filterable {

    private List<SparepartDAO> result;
    private List<SparepartDAO> listFull;
    private Context context;

    public RecycleAdapterSparepart(Context context,List<SparepartDAO> result){
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSparepart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_sparepart,viewGroup,false);
        final RecycleAdapterSparepart.MyViewHolder holder = new RecycleAdapterSparepart.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSparepart.MyViewHolder myViewHolder, int i){
        SparepartDAO spr = result.get(i);
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
                .load("https://api1.thekingcorp.org/images/sparepart/"+spr.getPicSparepart())
                .apply(requestOptions)
                .into(myViewHolder.gambarSpart);

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);

        myViewHolder.cardSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pilih Pengaturan :");
                builder.setMessage(myViewHolder.nameSparepart.getText().toString());
                builder.setCancelable(true);

                builder.setNeutralButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel
                            }
                        });

                builder.setNegativeButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Edit
                            }
                        });

                builder.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
                                String code = myViewHolder.codeSparepart.getText().toString();
                                final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                                httpClient.addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                                        return chain.proceed(request);
                                    }
                                });

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("https://api1.thekingcorp.org/")
                                        .client(httpClient.build())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                ApiClient apiClient = retrofit.create(ApiClient.class);
                                Call<SparepartDAO> sparepartDAOCall = apiClient.deleteSparepart(code);

                                sparepartDAOCall.enqueue(new Callback<SparepartDAO>() {
                                    @Override
                                    public void onResponse(Call<SparepartDAO> call, retrofit2.Response<SparepartDAO> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toasty.success(context, "Sparepart Berhasil Sihapus",
                                                    Toast.LENGTH_SHORT, true).show();

                                            Intent intent = new Intent(context,DasboardActivity.class);
                                            context.startActivity(intent);
                                        }
                                        else{

                                            Toasty.error(context, "Gagal Menghapus Data",
                                                    Toast.LENGTH_SHORT, true).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SparepartDAO> call, Throwable t) {
                                        Toasty.error(context, "Gagal Menghapus Data",
                                                Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
            cardSparepart= (CardView) itemView.findViewById(R.id.cardSparepart);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

    @Override
    public Filter getFilter() {
        return sparepartFil;
    }

    private Filter sparepartFil = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SparepartDAO> filterlist = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterlist.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(SparepartDAO sparepartDAO : listFull){
                    if (sparepartDAO.getNamaSparepart().toLowerCase().contains(filterPatten)){
                        filterlist.add(sparepartDAO);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterlist;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            result.clear();
            result.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };



}
