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

public class RecycleAdapterKonsumen extends RecyclerView.Adapter<RecycleAdapterKonsumen.MyViewHolder> implements Filterable {

    private List<KonsumenDAO> result;
    private List<KonsumenDAO> listFull;
    private Context context;

    public RecycleAdapterKonsumen(Context context,List<KonsumenDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterKonsumen.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_konsumen,viewGroup,false);
        final RecycleAdapterKonsumen.MyViewHolder holder = new RecycleAdapterKonsumen.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterKonsumen.MyViewHolder myViewHolder, int i) {
        KonsumenDAO kon = result.get(i);
        myViewHolder.mName.setText(kon.getNameKonsumen());
        myViewHolder.mCity.setText(kon.getCityKonsumen());
        myViewHolder.mAddress.setText(kon.getAddressKonsumen());
        myViewHolder.mPhone.setText(kon.getPhoneKonsumen());
        myViewHolder.mId.setText(kon.getIdKonsumen());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);

        myViewHolder.cardViewkon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSal = new AlertDialog.Builder(context);
                builderSal.setTitle("Pilih Pengaturan :");
                builderSal.setMessage(myViewHolder.mName.getText().toString());
                builderSal.setCancelable(true);

                //Save Konsumen
                SharedPreferences prefSupplier = context.getSharedPreferences("MyKonsumen", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefSupplier.edit();
                editor.putString("name", myViewHolder.mName.getText().toString());
                editor.putString("phone", myViewHolder.mPhone.getText().toString());
                editor.putString("address", myViewHolder.mAddress.getText().toString());
                editor.putString("id", myViewHolder.mId.getText().toString());
                editor.commit();

                builderSal.setNeutralButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel
                            }
                        });

                builderSal.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Edit
                                Intent intent = new Intent(context,EditKonsumenActivity.class);
                                context.startActivity(intent);
                            }
                        });

                builderSal.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
                                Integer idKonsumen = Integer.valueOf(myViewHolder.mId.getText().toString());

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
                                Call<KonsumenDAO> konsumenReq = apiClient.deleteKonsumenReq(idKonsumen);

                                konsumenReq.enqueue(new Callback<KonsumenDAO>() {
                                    @Override
                                    public void onResponse(Call<KonsumenDAO> call, retrofit2.Response<KonsumenDAO> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toasty.success(context, "Konsumen Berhasil Sihapus",
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
                                    public void onFailure(Call<KonsumenDAO> call, Throwable t) {
                                        Toasty.error(context, "Gagal Menghapus Data",
                                                Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                        });

                AlertDialog alertDialog = builderSal.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mName,mAddress,mCity,mId,mPhone;
        private CardView cardViewkon;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mName=itemView.findViewById(R.id.nameKonsumen);
            mAddress=itemView.findViewById(R.id.addressKonsumen);
            mId=itemView.findViewById(R.id.idKonsumen);
            mPhone=itemView.findViewById(R.id.phoneKonsumen);
            mCity=itemView.findViewById(R.id.cityKonsumen);
            cardViewkon=itemView.findViewById(R.id.cardViewAdapterKon);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

    @Override
    public Filter getFilter() {
        return konsumenFilter;
    }

    private Filter konsumenFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<KonsumenDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(KonsumenDAO konsumenDAO : listFull){
                    if (konsumenDAO.getNameKonsumen().toLowerCase().contains(filterPatten)){
                        filterList.add(konsumenDAO);
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
