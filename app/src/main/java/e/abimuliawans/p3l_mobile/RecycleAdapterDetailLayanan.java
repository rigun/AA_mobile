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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RecycleAdapterDetailLayanan extends RecyclerView.Adapter<RecycleAdapterDetailLayanan.MyViewHolder> {
    private List<DetailLayananDAO> result;
    private List<DetailLayananDAO> listFull;
    private Context context;
    private List<String> listSpinnerIDService = new ArrayList<>();
    private Spinner spinnerIdServiceEdit;
    private EditText totalEdit;

    public RecycleAdapterDetailLayanan(Context context,List<DetailLayananDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterDetailLayanan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_detail_layanan,viewGroup,false);
        final RecycleAdapterDetailLayanan.MyViewHolder holder = new RecycleAdapterDetailLayanan.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterDetailLayanan.MyViewHolder myViewHolder, int i) {
        final DetailLayananDAO detailLayananDAO = result.get(i);
        myViewHolder.mId.setText(detailLayananDAO.getIdDetailLayanan());
        myViewHolder.mName.setText(detailLayananDAO.getServiceDAO().getNameService());
        myViewHolder.mPrice.setText("Harga :"+detailLayananDAO.getPrice());
        myViewHolder.mTotal.setText("Total :"+detailLayananDAO.getTotalLayanan());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);

        //Cek Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        //Click CardView
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Alert
                AlertDialog.Builder builderSal = new AlertDialog.Builder(context);
                builderSal.setTitle("Pilih Pengaturan :");
                builderSal.setMessage(myViewHolder.mName.getText().toString());
                builderSal.setCancelable(true);

                builderSal.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               //Edit
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                AlertDialog.Builder mBuilderAddDetail = new AlertDialog.Builder(context);
                                View mViewAddDetail = inflater.inflate(R.layout.dialog_edit_detail_service,null);

                                //Set EditText and Spinner
                                spinnerIdServiceEdit = mViewAddDetail.findViewById(R.id.spinnerIdServiceEdit);
                                totalEdit = mViewAddDetail.findViewById(R.id.txtTotalDetailServiceEdit);

                                //Load Spinner
                                loadSpinnerService(httpClient,BASE_URL);

                                mBuilderAddDetail.setView(mViewAddDetail)
                                        .setPositiveButton("Edit Data", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Edit
                                                String spinnerService = spinnerIdServiceEdit.getSelectedItem().toString();
                                                String inputService = Character.toString(spinnerService.charAt(0));

                                                editDetailService(httpClient,BASE_URL,detailLayananDAO.getIdDetailLayanan(),
                                                        inputService,totalEdit.getText().toString());
                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Batal
                                    }
                                });

                                AlertDialog dialogDetail = mBuilderAddDetail.create();
                                dialogDetail.show();

                            }
                        });

                builderSal.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
                                deleteDetailService(httpClient,BASE_URL,detailLayananDAO.getIdDetailLayanan());
                            }
                        });

                builderSal.setNeutralButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel
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

        private TextView mName,mTotal,mPrice,mId;
        private CardView cardView;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mName=itemView.findViewById(R.id.nameDetailLayanan);
            mTotal=itemView.findViewById(R.id.totalDetailLayanan);
            mId=itemView.findViewById(R.id.idDetailLayanan);
            mPrice=itemView.findViewById(R.id.priceDetailLayanan);
            cardView=itemView.findViewById(R.id.cardViewAdapterDetailLayanan);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

    public void loadSpinnerService(OkHttpClient.Builder httpClient, String BASE_URL)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<ServiceDAO>> listCall = apiClient.getService();

        listCall.enqueue(new Callback<List<ServiceDAO>>() {
            @Override
            public void onResponse(Call<List<ServiceDAO>> call, retrofit2.Response<List<ServiceDAO>> response) {
                List<ServiceDAO> serviceDAOS = response.body();
                for(int i=0; i < serviceDAOS.size(); i++)
                {
                    String idSer = serviceDAOS.get(i).getIdService();
                    String nameSer = serviceDAOS.get(i).getNameService();
                    String inputSer = idSer+" - "+nameSer;
                    listSpinnerIDService.add(inputSer);
                }
                listSpinnerIDService.add(0,"-SELECT ID SERVICE-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item,listSpinnerIDService);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIdServiceEdit.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ServiceDAO>> call, Throwable t) {
                //
            }
        });
    }

    public void deleteDetailService(OkHttpClient.Builder httpClient, String BASE_URL, String idTrans)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<DetailLayananDAO> deleteTransaction = apiClient.deleteDetailService(idTrans);

        deleteTransaction.enqueue(new Callback<DetailLayananDAO>() {
            @Override
            public void onResponse(Call<DetailLayananDAO> call, retrofit2.Response<DetailLayananDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(context, "Detail Berhasil Dihapus",
                            Toast.LENGTH_SHORT, true).show();

                    Intent intent = new Intent(context,DasboardActivity.class);
                    context.startActivity(intent);
                }
                else{

                    Toasty.error(context, "Gagal Menhapus Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<DetailLayananDAO> call, Throwable t) {
                Toasty.warning(context,"Data Detail Terhapus",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public void editDetailService(OkHttpClient.Builder httpClient, String BASE_URL, String idTrans,
                                  String idService, String total)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<DetailLayananDAO> deleteTransaction = apiClient.editDetailService(idTrans,idService,total);

        deleteTransaction.enqueue(new Callback<DetailLayananDAO>() {
            @Override
            public void onResponse(Call<DetailLayananDAO> call, retrofit2.Response<DetailLayananDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(context, "Detail Berhasil Diedit",
                            Toast.LENGTH_SHORT, true).show();

                    Intent intent = new Intent(context,DasboardActivity.class);
                    context.startActivity(intent);
                }
                else{

                    Toasty.error(context, "Gagal Menhapus Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<DetailLayananDAO> call, Throwable t) {
                Toasty.warning(context,"Data Detail Teredit",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
