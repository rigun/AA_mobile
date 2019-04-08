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
import android.widget.LinearLayout;
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

public class RecycleAdapterSupplier extends RecyclerView.Adapter<RecycleAdapterSupplier.MyViewHolder> implements Filterable {

    private List<SupplierDAO> result;
    private List<SupplierDAO> listFull;
    private Context context;

    public RecycleAdapterSupplier(Context context,List<SupplierDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSupplier.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_supplier,viewGroup,false);
        final RecycleAdapterSupplier.MyViewHolder holder = new RecycleAdapterSupplier.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSupplier.MyViewHolder myViewHolder, int i) {
        SupplierDAO spl = result.get(i);
        myViewHolder.mName.setText(spl.getNameSupplier());
        myViewHolder.mCity.setText(spl.getCitySupplier());
        myViewHolder.mAddress.setText(spl.getAddressSupplier());
        myViewHolder.mPhone.setText(spl.getPhoneSupplier());
        myViewHolder.mId.setText(spl.getIdSupplier());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);


        myViewHolder.cardViewSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSup = new AlertDialog.Builder(context);
                builderSup.setTitle("Pilih Pengaturan :");
                builderSup.setMessage(myViewHolder.mName.getText().toString());
                builderSup.setCancelable(true);

                //Save Supplier
                SharedPreferences prefSupplier = context.getSharedPreferences("MySupplier", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefSupplier.edit();
                editor.putString("name", myViewHolder.mName.getText().toString());
                editor.putString("city", myViewHolder.mCity.getText().toString());
                editor.putString("phone", myViewHolder.mPhone.getText().toString());
                editor.putString("address", myViewHolder.mAddress.getText().toString());
                editor.putString("id", myViewHolder.mId.getText().toString());
                editor.commit();

                builderSup.setNeutralButton(
                        "Tambah Spartpart",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah Sparepart
                                Intent intent = new Intent(context,TambahSparepartActivity.class);
                                context.startActivity(intent);
                            }
                        }
                );


                builderSup.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Edit Data
                                Intent intent = new Intent(context,EditSupplierActivity.class);
                                context.startActivity(intent);
                            }
                        }
                );

                builderSup.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete Data
                                Integer idSupplier = Integer.valueOf(myViewHolder.mId.getText().toString());

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
                                Call<SupplierDAO> vehicleCall = apiClient.deleteSupplierReq(idSupplier);

                                vehicleCall.enqueue(new Callback<SupplierDAO>() {
                                    @Override
                                    public void onResponse(Call<SupplierDAO> call, retrofit2.Response<SupplierDAO> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toasty.success(context, "Supplier Berhasil Sihapus",
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
                                    public void onFailure(Call<SupplierDAO> call, Throwable t) {
                                        Toasty.error(context, "Gagal Menghapus Data",
                                                Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                            }
                        }
                );

                AlertDialog alertDialog = builderSup.create();
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
        private CardView cardViewSup;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mName=itemView.findViewById(R.id.nameSupplier);
            mAddress=itemView.findViewById(R.id.addressSupplier);
            mId=itemView.findViewById(R.id.idSupplier);
            mPhone=itemView.findViewById(R.id.phoneSupplier);
            mCity=itemView.findViewById(R.id.citySupplier);
            cardViewSup=itemView.findViewById(R.id.cardViewAdapterSup);
            linearLayout=itemView.findViewById(R.id.linearSupplier);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }


    @Override
    public Filter getFilter() {
        return supplierFilter;
    }

    private Filter supplierFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SupplierDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(SupplierDAO supplierDAO : listFull){
                    if (supplierDAO.getNameSupplier().toLowerCase().contains(filterPatten)){
                        filterList.add(supplierDAO);
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
