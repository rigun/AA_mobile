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
import android.widget.Filter;
import android.widget.Filterable;
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

public class RecycleAdapterPemesanan extends RecyclerView.Adapter<RecycleAdapterPemesanan.MyViewHolder> implements Filterable {
    private List<ShowPemesananDAO> result;
    private List<ShowPemesananDAO> listFull;
    private Context context;

    public RecycleAdapterPemesanan(Context context,List<ShowPemesananDAO> result) {
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
        ShowPemesananDAO dataDAO = result.get(i);
        myViewHolder.mIdOrder.setText(dataDAO.getIdPemesanan());
        myViewHolder.mIdSupplier.setText(dataDAO.getIdSupplierPemesanan());
        myViewHolder.mIdCabang.setText(dataDAO.getIdCabangPemesanan());
        myViewHolder.mtanggal.setText("Tanggal : "+dataDAO.getTanggalPemesanan());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);

        myViewHolder.cardViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pilih Pengaturan :");
                builder.setCancelable(true);

                builder.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
                                Integer idSupplier = Integer.valueOf(myViewHolder.mIdSupplier.getText().toString());
                                Integer inputIdCabang = Integer.valueOf(myViewHolder.mIdCabang.getText().toString());

                                final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                                httpClient.addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                                        return chain.proceed(request);
                                    }
                                });

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(BASE_URL)
                                        .client(httpClient.build())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                ApiClient apiClient = retrofit.create(ApiClient.class);
                                Call<ShowPemesananDAO> pemesananDAOCall = apiClient.deleteOrder(idSupplier,inputIdCabang);

                                pemesananDAOCall.enqueue(new Callback<ShowPemesananDAO>() {
                                    @Override
                                    public void onResponse(Call<ShowPemesananDAO> call, retrofit2.Response<ShowPemesananDAO> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toasty.success(context, "Pemesanan Berhasil Sihapus",
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
                                    public void onFailure(Call<ShowPemesananDAO> call, Throwable t) {
                                        Toasty.error(context, "Gagal Menghapus Data",
                                                Toast.LENGTH_SHORT, true).show();
                                    }
                                });

                            }
                        });

                builder.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Edit

                            }
                        });

                builder.setNeutralButton(
                        "Konfirmasi",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Konfirmasi

                            }
                        });

                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });
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

    @Override
    public Filter getFilter() {
        return pemesanan;
    }

    private Filter pemesanan = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ShowPemesananDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(ShowPemesananDAO pemesananDAO : listFull){
                    if (pemesananDAO.getIdPemesanan().toLowerCase().contains(filterPatten)){
                        filterList.add(pemesananDAO);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;

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
