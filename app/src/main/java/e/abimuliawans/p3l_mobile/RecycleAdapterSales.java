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

public class RecycleAdapterSales extends RecyclerView.Adapter<RecycleAdapterSales.MyViewHolder> implements Filterable {

    private List<SalesDAO> result;
    private List<SalesDAO> listFull;
    private Context context;

    public RecycleAdapterSales(Context context,List<SalesDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSales.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_sales,viewGroup,false);
        final RecycleAdapterSales.MyViewHolder holder = new RecycleAdapterSales.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSales.MyViewHolder myViewHolder, int i) {
        SalesDAO sal = result.get(i);
        myViewHolder.mName.setText(sal.getNameSales());
        myViewHolder.mCity.setText(sal.getCitySales());
        myViewHolder.mAddress.setText(sal.getAddressSales());
        myViewHolder.mPhone.setText(sal.getPhoneSales());
        myViewHolder.mId.setText(sal.getIdSales());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);


        myViewHolder.cardViewSal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSal = new AlertDialog.Builder(context);
                builderSal.setTitle("Pilih Pengaturan :");
                builderSal.setMessage(myViewHolder.mName.getText().toString());
                builderSal.setCancelable(true);

                //Save Sales
                SharedPreferences prefSupplier = context.getSharedPreferences("MySales", MODE_PRIVATE);
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
                                Intent intent = new Intent(context,EditSalesActivity.class);
                                context.startActivity(intent);
                            }
                        });

                builderSal.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
                                Integer idSales = Integer.valueOf(myViewHolder.mId.getText().toString());

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
                                Call<SalesDAO> salesReq = apiClient.deleteSalesReq(idSales);

                                salesReq.enqueue(new Callback<SalesDAO>() {
                                    @Override
                                    public void onResponse(Call<SalesDAO> call, retrofit2.Response<SalesDAO> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toasty.success(context, "Sales Berhasil Sihapus",
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
                                    public void onFailure(Call<SalesDAO> call, Throwable t) {
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
        private CardView cardViewSal;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mName=itemView.findViewById(R.id.nameSales);
            mAddress=itemView.findViewById(R.id.addressSales);
            mId=itemView.findViewById(R.id.idSales);
            mPhone=itemView.findViewById(R.id.phoneSales);
            mCity=itemView.findViewById(R.id.citySales);
            cardViewSal=itemView.findViewById(R.id.cardViewAdapterSal);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

    @Override
    public Filter getFilter() {
        return salesFilter;
    }

    private Filter salesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SalesDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(SalesDAO salesDAO : listFull){
                    if (salesDAO.getNameSales().toLowerCase().contains(filterPatten)){
                        filterList.add(salesDAO);
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
