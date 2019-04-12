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


public class RecycleAdapterVehicle extends RecyclerView.Adapter<RecycleAdapterVehicle.MyViewHolder> implements Filterable {

    private List<VehicleDAO> result;
    private List<VehicleDAO> listFull;
    private Context context;


    public RecycleAdapterVehicle(Context context,List<VehicleDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterVehicle.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_vehicle,viewGroup,false);
        final RecycleAdapterVehicle.MyViewHolder holder = new RecycleAdapterVehicle.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        VehicleDAO vhc = result.get(i);
        myViewHolder.mMerk.setText(vhc.getMerkVehicle());
        myViewHolder.mType.setText(vhc.getTypeVehicle());
        myViewHolder.mId.setText(vhc.getIdVehicle());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pilih Pengaturan :");
                builder.setMessage(myViewHolder.mType.getText().toString());
                builder.setCancelable(true);

                //Save Vehicle
                SharedPreferences prefVehicle = context.getSharedPreferences("MyVehicle", MODE_PRIVATE);
                SharedPreferences.Editor editorVehicle = prefVehicle.edit();
                editorVehicle.putString("merk", myViewHolder.mMerk.getText().toString());
                editorVehicle.putString("type", myViewHolder.mType.getText().toString());
                editorVehicle.putString("idVeh", myViewHolder.mId.getText().toString());
                editorVehicle.commit();

                builder.setNeutralButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                builder.setNegativeButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Edit
                                Intent intent = new Intent(context,EditVehicleActivity.class);
                                context.startActivity(intent);
                            }
                        });

                builder.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
                                Integer idVehicle = Integer.valueOf(myViewHolder.mId.getText().toString());
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
                                Call<VehicleDAO> vehicleCall = apiClient.deleteVehicle(idVehicle);

                                vehicleCall.enqueue(new Callback<VehicleDAO>() {
                                    @Override
                                    public void onResponse(Call<VehicleDAO> call, retrofit2.Response<VehicleDAO> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toasty.success(context, "Kendaraan Berhasil Sihapus",
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
                                    public void onFailure(Call<VehicleDAO> call, Throwable t) {
                                        Toasty.error(context, "Gagal Menghapus Data",
                                                Toast.LENGTH_SHORT, true).show();
                                    }
                                });

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

    @Override
    public Filter getFilter() {
        return supplierVehicle;
    }

    private Filter supplierVehicle = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<VehicleDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(VehicleDAO vehicleDAO : listFull){
                    if (vehicleDAO.getTypeVehicle().toLowerCase().contains(filterPatten)){
                        filterList.add(vehicleDAO);
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
