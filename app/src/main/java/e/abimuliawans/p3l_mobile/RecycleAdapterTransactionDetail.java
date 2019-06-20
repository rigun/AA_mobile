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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
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

public class RecycleAdapterTransactionDetail extends RecyclerView.Adapter<RecycleAdapterTransactionDetail.MyViewHolder> implements Filterable {
    private List<TransactionDetailDAO> result;
    private List<TransactionDetailDAO> listFull;
    private Context context;
    private CardView cardViewSparepart,cardViewLayanan,cardViewEdit,cardViewDelete;
    private Spinner spinnerKendaraan;
    private EditText platNomor;
    private List<String> listSpinnerKendaraan = new ArrayList<String>();

    public RecycleAdapterTransactionDetail(Context context,List<TransactionDetailDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterTransactionDetail.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_detail_trans,viewGroup,false);
        final RecycleAdapterTransactionDetail.MyViewHolder holder = new RecycleAdapterTransactionDetail.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterTransactionDetail.MyViewHolder myViewHolder, int i) {
        final TransactionDetailDAO trans = result.get(i);
        myViewHolder.mId.setText(trans.getIdDetailTransaction());
        myViewHolder.mIdTrans.setText(trans.getIdTransaction());
        myViewHolder.mPlat.setText(trans.getVehicleCustomer().getNomorPlat());

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


        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //New Alert
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = inflater.inflate(R.layout.dialog_menu_click_transaction,null);

                //Get Jenis Sparepart
                SharedPreferences prefJenis = context.getSharedPreferences("MyJenisTransaksi", MODE_PRIVATE);
                final String tempJenisTransaksi = prefJenis.getString("jenis_transaksi", null);

                //Get Status Transaction
                SharedPreferences prefStatus = context.getSharedPreferences("MyStatus", MODE_PRIVATE);
                final String statusTransaction = prefStatus.getString("status", null);

                //Set Card View
                cardViewSparepart = mView.findViewById(R.id.cardDetailSparepartDT);
                cardViewLayanan = mView.findViewById(R.id.cardDetailServiceDT);
                cardViewEdit = mView.findViewById(R.id.cardTransactionDetailEdit);
                cardViewDelete = mView.findViewById(R.id.cardTransactionDetailDelete);

                //Set Status Transaction
                if(statusTransaction.equals("3"))
                {
                    //Selesai
                    cardViewEdit.setVisibility(View.GONE);
                    cardViewDelete.setVisibility(View.GONE);
                }

                //Set Visibility For Transaction (SS,SV,SP)
                if(tempJenisTransaksi.equals("SV"))
                {
                    //For Service SV
                    cardViewSparepart.setVisibility(View.GONE);
                }
                else if(tempJenisTransaksi.equals("SP"))
                {
                    //For Sparepart SP
                    cardViewLayanan.setVisibility(View.GONE);
                }

                // Click Detail Sparepart
                cardViewSparepart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences prefIDTransDetail = context.getSharedPreferences("MyIdDetailTrans", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefIDTransDetail.edit();
                        editor.putString("id_detail_trans", trans.getIdDetailTransaction());
                        editor.commit();

                        Intent intent = new Intent(context,DetailSparepartActivity.class);
                        context.startActivity(intent);
                    }
                });

                // Click Detail Service
                cardViewLayanan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences prefIDTransDetail = context.getSharedPreferences("MyIdDetailTrans", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefIDTransDetail.edit();
                        editor.putString("id_detail_trans", trans.getIdDetailTransaction());
                        editor.commit();

                        Intent intent = new Intent(context,DetailLayananActivity.class);
                        context.startActivity(intent);
                    }
                });

                // Click Edit
                cardViewEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        AlertDialog.Builder mBuilderAddDetail = new AlertDialog.Builder(context);
                        View mViewAddDetail = inflater.inflate(R.layout.dialog_add_detail_transaction,null);

                        spinnerKendaraan = mViewAddDetail.findViewById(R.id.spinnerVehicleAddDetail);
                        platNomor = mViewAddDetail.findViewById(R.id.txtPlatMotorDT);
                        platNomor.setText(trans.getVehicleCustomer().getNomorPlat());
                        loadVehicleSpinner(httpClient,BASE_URL);

                        final String idVehicleCustomer = trans.getIdDetailTransaction();


                        mBuilderAddDetail.setView(mViewAddDetail)
                                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Tambah
                                        String temp = spinnerKendaraan.getSelectedItem().toString();
                                        final String inputVehicle = Character.toString(temp.charAt(0));

                                        updateKendaraanKonsumen(httpClient,BASE_URL,platNomor.getText().toString(),inputVehicle,idVehicleCustomer);

                                        listSpinnerKendaraan.clear();
                                        spinnerKendaraan.setAdapter(null);

                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Batal
                            }
                        });

                        AlertDialog dialog = mBuilderAddDetail.create();
                        dialog.show();
                    }
                });

                //Click Delete
                cardViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String idTransDetail = trans.getIdDetailTransaction();
                        deleteTransDetail(httpClient,BASE_URL,idTransDetail);
                    }
                });

                mBuilder.setView(mView)
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Cancel

                            }
                        });

                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mId,mPlat,mIdTrans;
        private CardView cardView;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mId=itemView.findViewById(R.id.idDetailTransaction);
            mPlat=itemView.findViewById(R.id.platNomorDetail);
            mIdTrans=itemView.findViewById(R.id.showIDTransactio);
            cardView=itemView.findViewById(R.id.cardViewAdapterTransDetail);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

    public void deleteTransDetail(OkHttpClient.Builder httpClient, String BASE_URL, String id)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> delete = apiClient.deleteTransactionDetail(id);

        delete.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(context, "Kendaraan Berhasil Dihapus",
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
                Toasty.warning(context,"Data Kendaraan Terhapus",
                        Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public void loadVehicleSpinner(OkHttpClient.Builder httpClient, String BASE_URL)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<VehicleDAO>> listCall = apiClient.getVehicle();

        listCall.enqueue(new Callback<List<VehicleDAO>>() {
            @Override
            public void onResponse(Call<List<VehicleDAO>> call, retrofit2.Response<List<VehicleDAO>> response) {
                List<VehicleDAO> vehicleDAOList = response.body();
                for(int i=0; i < vehicleDAOList.size(); i++ ){
                    String id = vehicleDAOList.get(i).getIdVehicle();
                    String nameVehicle = vehicleDAOList.get(i).getTypeVehicle();
                    String input = id+" - "+nameVehicle;

                    listSpinnerKendaraan.add(input);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item,listSpinnerKendaraan);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerKendaraan.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<VehicleDAO>> call, Throwable t) {

            }
        });
    }

    public void updateKendaraanKonsumen(OkHttpClient.Builder httpClient, String BASE_URL, String plat,
                                     String idKendaraan, String idTransaksi)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> transactionDAOCall = apiClient.updateDetailTransaction(plat,idKendaraan,idTransaksi);

        transactionDAOCall.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {
                    Toasty.success(context, "Detail Berhasil Diedit",
                            Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(context,DasboardActivity.class);
                    context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<TransactionDAO> call, Throwable t) {
                Toasty.warning(context, "Detail Berhasil Diedit",
                        Toast.LENGTH_SHORT, true).show();
                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return transactionFilter;
    }

    private Filter transactionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TransactionDetailDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(TransactionDetailDAO transaction : listFull){
                    if (transaction.getVehicleCustomer().getNomorPlat().toLowerCase().contains(filterPatten)){
                        filterList.add(transaction);
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
