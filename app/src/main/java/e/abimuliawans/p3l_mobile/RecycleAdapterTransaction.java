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

public class RecycleAdapterTransaction extends RecyclerView.Adapter<RecycleAdapterTransaction.MyViewHolder> implements Filterable {

    private List<TransactionByCabangDAO> result;
    private List<TransactionByCabangDAO> listFull;
    private Context context;
    private Spinner spinnerKedaraan;
    private EditText platNomor;
    private List<String> listSpinnerKendaraan = new ArrayList<String>();

    public RecycleAdapterTransaction(Context context,List<TransactionByCabangDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterTransaction.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_transaction,viewGroup,false);
        final RecycleAdapterTransaction.MyViewHolder holder = new RecycleAdapterTransaction.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterTransaction.MyViewHolder myViewHolder, final int i) {
        final TransactionByCabangDAO transactionByCabangDAO = result.get(i);
        myViewHolder.mIdTrans.setText(transactionByCabangDAO.getIdTransaction());
        myViewHolder.mNumber.setText(transactionByCabangDAO.getTransactionNumber());
        myViewHolder.mService.setText("Total Service :"+transactionByCabangDAO.getTotalServices());
        myViewHolder.mSparepart.setText("Total Sparepart :"+transactionByCabangDAO.getTotalSpareparts());
        myViewHolder.mIDCustomer.setText(transactionByCabangDAO.getCustomer_id());
        myViewHolder.mPayment.setText("Payment :"+transactionByCabangDAO.getPayment());
        myViewHolder.mDiskon.setText("Diskon :"+transactionByCabangDAO.getDiskon());

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);

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
                //Pilih Aturan
                //New Alert
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = inflater.inflate(R.layout.dialog_option_transaction,null);

                final String idTrans2 = transactionByCabangDAO.getIdTransaction();

                CardView tambahDetail = mView.findViewById(R.id.cardViewAddDetailTrans);
                tambahDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        AlertDialog.Builder mBuilderAddDetail = new AlertDialog.Builder(context);
                        View mViewAddDetail = inflater.inflate(R.layout.dialog_add_detail_transaction,null);

                        spinnerKedaraan = mViewAddDetail.findViewById(R.id.spinnerVehicleAddDetail);
                        platNomor = mViewAddDetail.findViewById(R.id.txtPlatMotorDT);
                        loadVehicleSpinner(httpClient,BASE_URL);

                        final String idTrans = transactionByCabangDAO.getIdTransaction();


                        mBuilderAddDetail.setView(mViewAddDetail)
                                .setPositiveButton("Tambah Data", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Tambah
                                        String temp = spinnerKedaraan.getSelectedItem().toString();
                                        final String inputVehicle = Character.toString(temp.charAt(0));

                                        addDetailTransaction(httpClient,BASE_URL, platNomor.getText().toString(),
                                                inputVehicle,idTrans );
                                        listSpinnerKendaraan.clear();
                                        spinnerKedaraan.setAdapter(null);

                                        Intent intent = new Intent(context,DasboardActivity.class);
                                        context.startActivity(intent);
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

                mBuilder.setView(mView)
                        .setPositiveButton("Tampil Data", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tambah
                                Intent intent = new Intent(context,TransaksiDetailActivity.class);
                                intent.putExtra("idTrans",idTrans2 );
                                context.startActivity(intent);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Batal
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

        private TextView mNumber,mIDCustomer,mService,mIdTrans,mSparepart,mPayment,mDiskon;
        private CardView cardView;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mIdTrans=itemView.findViewById(R.id.idTransaction);
            mNumber=itemView.findViewById(R.id.transNumber);
            mIDCustomer=itemView.findViewById(R.id.idCustomerTrans);
            mService=itemView.findViewById(R.id.totalServiceTrans);
            mSparepart=itemView.findViewById(R.id.totalSparepartTrans);
            mPayment=itemView.findViewById(R.id.paymentTrans);
            mDiskon=itemView.findViewById(R.id.diskonTrans);

            cardView=itemView.findViewById(R.id.cardViewAdapterTrans);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
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
                listSpinnerKendaraan.add(0,"-SELECT ID VEHICLE-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item,listSpinnerKendaraan);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerKedaraan.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<VehicleDAO>> call, Throwable t) {

            }
        });
    }

    public void addDetailTransaction(OkHttpClient.Builder httpClient, String BASE_URL, String plat,
                                     String idKendaraan, String idTransaksi)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> transactionDAOCall = apiClient.addDetailTransaction(plat,idKendaraan,idTransaksi);

        transactionDAOCall.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {

                if(response.isSuccessful())
                {
                    Toasty.success(context, "Detail Berhasil Ditambah",
                            Toast.LENGTH_SHORT, true).show();

                }
                else{

                    Toasty.error(context, "Gagal Menambahkan Data",
                            Toast.LENGTH_SHORT, true).show();
                }

            }

            @Override
            public void onFailure(Call<TransactionDAO> call, Throwable t) {
                Toasty.error(context, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
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
            List<TransactionByCabangDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(TransactionByCabangDAO transaction : listFull){
                    if (transaction.getTransactionNumber().toLowerCase().contains(filterPatten)){
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
