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

public class RecycleAdapterDetailSparepart extends RecyclerView.Adapter<RecycleAdapterDetailSparepart.MyViewHolder> {
    private List<TransactionDAO> result;
    private List<TransactionDAO> listFull;
    private Context context;
    private Spinner spinnerCodeSpare;
    private EditText totalText;
    private List<String> listSpinnerCode = new ArrayList<>();

    public RecycleAdapterDetailSparepart(Context context,List<TransactionDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterDetailSparepart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_detail_sparepart,viewGroup,false);
        final RecycleAdapterDetailSparepart.MyViewHolder holder = new RecycleAdapterDetailSparepart.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterDetailSparepart.MyViewHolder myViewHolder, int i) {
        final TransactionDAO transactionDAO = result.get(i);
        myViewHolder.mId.setText(transactionDAO.getDetailSparepart().getIdDetailSpare());
        myViewHolder.mCode.setText(transactionDAO.getDetailSparepart().getCodeSpare());
        myViewHolder.mTotal.setText("Total : "+transactionDAO.getDetailSparepart().getTotalSpare());
        myViewHolder.mPrice.setText("Harga :"+transactionDAO.getDetailSparepart().getPrice());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);

        //Ambil ID Cabang
        SharedPreferences prefCabang = context.getSharedPreferences("MyCabang", Context.MODE_PRIVATE);
        final String id_cabang = prefCabang.getString("id_cabang",null);

        //Cek Token
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert
                AlertDialog.Builder builderSal = new AlertDialog.Builder(context);
                builderSal.setTitle("Pilih Pengaturan :");
                builderSal.setMessage(myViewHolder.mCode.getText().toString());
                builderSal.setCancelable(true);

                builderSal.setPositiveButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Edit
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                AlertDialog.Builder mBuilderAddDetail = new AlertDialog.Builder(context);
                                View mViewAddDetail = inflater.inflate(R.layout.dialog_edit_detail_sparepart,null);

                                //Set EditText and Spinner
                                spinnerCodeSpare = mViewAddDetail.findViewById(R.id.spinnerCodeSpareByCabangDetailEdit);
                                totalText = mViewAddDetail.findViewById(R.id.txtTotalDetailSpareEdit);

                                //Load Spinner
                                loadCodeSparepartSpinner(httpClient,id_cabang,BASE_URL);

                                mBuilderAddDetail.setView(mViewAddDetail)
                                        .setPositiveButton("Edit Data", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Edit
                                                editDetailSparepart(httpClient,BASE_URL,transactionDAO.getDetailSparepart().getIdDetailSpare(),
                                                        spinnerCodeSpare.getSelectedItem().toString(),totalText.getText().toString());
                                                listSpinnerCode.clear();
                                                spinnerCodeSpare.setAdapter(null);
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
                                deleteDetailSparepart(httpClient,BASE_URL,transactionDAO.getDetailSparepart().getIdDetailSpare());
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

        private TextView mCode,mTotal,mPrice,mId,mIdDetail;
        private CardView cardView;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mCode=itemView.findViewById(R.id.nameSparepartDetailSpare);
            mTotal=itemView.findViewById(R.id.totalDetailSpare);
            mId=itemView.findViewById(R.id.idDetailSpare);
            mPrice=itemView.findViewById(R.id.priceDetailSpare);
            cardView=itemView.findViewById(R.id.cardViewAdapterDetailSpare);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

    private void loadCodeSparepartSpinner(OkHttpClient.Builder httpClient, String id_cabang, String BASE_URL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<SparepartCabangSupDAO>> sparepartCabang = apiClient.getSparepartCabang(id_cabang);

        sparepartCabang.enqueue(new Callback<List<SparepartCabangSupDAO>>() {
            @Override
            public void onResponse(Call<List<SparepartCabangSupDAO>> call, retrofit2.Response<List<SparepartCabangSupDAO>> response) {
                List<SparepartCabangSupDAO> sparepartDAOS = response.body();
                for(int i=0; i < sparepartDAOS.size(); i++)
                {
                    String code = sparepartDAOS.get(i).getCodeSpareScp();
                    listSpinnerCode.add(code);
                }
                listSpinnerCode.add(0,"-SELECT CODE-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item,listSpinnerCode);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCodeSpare.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<SparepartCabangSupDAO>> call, Throwable t) {
                //
                Toasty.error(context, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void editDetailSparepart(OkHttpClient.Builder httpClient, String BASE_URL, String idTrans,
                                  String codeSpare, String total)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> deleteTransaction = apiClient.editDetailSparepart(idTrans,codeSpare,total);

        deleteTransaction.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {

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
            public void onFailure(Call<TransactionDAO> call, Throwable t) {
                Toasty.warning(context,"Data Detail Teredit",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public void deleteDetailSparepart(OkHttpClient.Builder httpClient, String BASE_URL, String idTrans)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<DetailSparepartDAO> deleteTransaction = apiClient.deleteDetailSparepart(idTrans);

        deleteTransaction.enqueue(new Callback<DetailSparepartDAO>() {
            @Override
            public void onResponse(Call<DetailSparepartDAO> call, retrofit2.Response<DetailSparepartDAO> response) {

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
            public void onFailure(Call<DetailSparepartDAO> call, Throwable t) {
                Toasty.warning(context,"Data Detail Terhapus",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
