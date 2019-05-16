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
import android.widget.Button;
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
    private Spinner spinnerKedaraan,spinnerJenisTransEdit,spinnerCity;
    private EditText platNomor,nameKonsumen,phoneKonsumen,alamatKonsumen;
    private List<String> listSpinnerKendaraan = new ArrayList<String>();
    private List<String> listSpinner = new ArrayList<>();

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
        myViewHolder.mNumber.setText(transactionByCabangDAO.getTransactionNumber()+"-"+transactionByCabangDAO.getIdTransaction());
        myViewHolder.mService.setText("Total Service :"+transactionByCabangDAO.getTotalServices());
        myViewHolder.mSparepart.setText("Total Sparepart :"+transactionByCabangDAO.getTotalSpareparts());
        myViewHolder.mIDCustomer.setText(transactionByCabangDAO.getKonsumenTrans().getNameKonsumen());
        myViewHolder.mPayment.setText("Payment :"+transactionByCabangDAO.getPayment());
        myViewHolder.mDiskon.setText("Diskon :"+transactionByCabangDAO.getDiskon());

        //Set Progres View Button, Cek Status, and Send Status to Detail
        String statusTransaction = transactionByCabangDAO.getStatus();
        SharedPreferences prefStatus = context.getSharedPreferences("MyStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefStatus.edit();
        editor.putString("status", statusTransaction);
        editor.commit();

        if(statusTransaction.equals("3"))
        {
            //Selesai
            myViewHolder.btnStatus.setBackgroundResource(R.drawable.btn_progres_selesai);
            myViewHolder.btnStatus.setText("Selesai");
        }
        else if(statusTransaction.equals("0"))
        {
            //Pengisisan Data
        }

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
                final View mView = inflater.inflate(R.layout.dialog_option_transaction,null);
                final String status = transactionByCabangDAO.getStatus();
                final String idTrans2 = transactionByCabangDAO.getIdTransaction();
                final String string = transactionByCabangDAO.getTransactionNumber();
                final String[] parts = string.split("-");
                final String partTipeTransaction = parts[0]; // SS,SV,SP

                // Cek Status
                CardView tambahDetail = mView.findViewById(R.id.cardViewAddDetailTrans);
                if(status.equals("3"))
                {
                    //Selesai
                    tambahDetail.setVisibility(View.GONE);
                }
                // Click Tambah
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

                //Cek Status
                CardView cardViewEditTransaction = mView.findViewById(R.id.cardViewEditTransaction);
                if(status.equals("3"))
                {
                    //Selesai
                    cardViewEditTransaction.setVisibility(View.GONE);
                }
                // Click Edit
                cardViewEditTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Edit Transaction

                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        AlertDialog.Builder mBuilderEditTrans = new AlertDialog.Builder(context);
                        View mViewEditTrans = inflater.inflate(R.layout.dialog_edit_transaction,null);

                        //Set Edit Text and Spinner
                        spinnerJenisTransEdit = mViewEditTrans.findViewById(R.id.spinnerPilihTransEdit);
                        nameKonsumen = mViewEditTrans.findViewById(R.id.txtNameTransEdit);
                        alamatKonsumen = mViewEditTrans.findViewById(R.id.txtAddressTransEdit);
                        phoneKonsumen = mViewEditTrans.findViewById(R.id.txtPhoneTransEdit);
                        spinnerCity = mViewEditTrans.findViewById(R.id.spinnerCityTransEdit);

                        //Load Spinner City
                        loadSpinnerCities(httpClient,BASE_URL);

                        //Set Text Data Edit
                        nameKonsumen.setText(transactionByCabangDAO.getKonsumenTrans().getNameKonsumen());
                        alamatKonsumen.setText(transactionByCabangDAO.getKonsumenTrans().getAddressKonsumen());
                        phoneKonsumen.setText(transactionByCabangDAO.getKonsumenTrans().getPhoneKonsumen());

                        final String idTrans = transactionByCabangDAO.getIdTransaction();

                        mBuilderEditTrans.setView(mViewEditTrans)
                                .setPositiveButton("Edit Data", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Edit
                                        editTransaction(httpClient,BASE_URL,idTrans,
                                                spinnerJenisTransEdit.getSelectedItem().toString(),
                                                nameKonsumen.getText().toString(),
                                                phoneKonsumen.getText().toString(),
                                                alamatKonsumen.getText().toString(),
                                                spinnerCity.getSelectedItem().toString(),
                                                "konsumen",
                                                transactionByCabangDAO.getCustomer_id());
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Cancel
                            }
                        });

                        AlertDialog dialog = mBuilderEditTrans.create();
                        dialog.show();

                    }
                });

                // Cek Status
                CardView cardViewDeleteTransaction = mView.findViewById(R.id.cardViewDeleteTransaction);
                if(status.equals("3"))
                {
                    //Selesai
                    cardViewDeleteTransaction.setVisibility(View.GONE);
                }

                // Click Delete
                cardViewDeleteTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Delete Transaction
                        deleteTransaction(httpClient,BASE_URL,idTrans2);
                    }
                });

                //Option Bawah
                mBuilder.setView(mView)
                        .setPositiveButton("Tampil Kendaraan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tampil Data Detail (Kendaraan)
                                //Save Jenis Transaksi
                                SharedPreferences prefTrans = context.getSharedPreferences("MyJenisTransaksi", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefTrans.edit();
                                editor.putString("jenis_transaksi",partTipeTransaction );
                                editor.commit();

                                //Intent to Detail Transaksi
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
        private Button btnStatus;

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
            btnStatus=itemView.findViewById(R.id.btnShowStatusTransaction);
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

    public void deleteTransaction(OkHttpClient.Builder httpClient, String BASE_URL, String idTrans)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> transactionDAOCall = apiClient.deleteTransaction(idTrans);

        transactionDAOCall.enqueue(new Callback<TransactionDAO>() {
            @Override
            public void onResponse(Call<TransactionDAO> call, retrofit2.Response<TransactionDAO> response) {

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
            public void onFailure(Call<TransactionDAO> call, Throwable t) {
                Toasty.warning(context,"Semua Data Transaksi Terhapus",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public void editTransaction(OkHttpClient.Builder httpClient, String BASE_URL, String idTrans, String jenisTrans,
                                String nameCosTrans, String phoneTrans, String addresTrans, String cityTrans,
                                String role, String idKonsumen)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<TransactionDAO> transactionDAOCall = apiClient.updateTransaction(idTrans,jenisTrans,nameCosTrans,
                phoneTrans,addresTrans,cityTrans,role,idKonsumen);

        transactionDAOCall.enqueue(new Callback<TransactionDAO>() {
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
                Toasty.error(context, t.getMessage(),
                        Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void loadSpinnerCities(OkHttpClient.Builder httpClient, String BASE_URL)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<List<CitiesDAO>> listCall = apiClient.getCities();

        listCall.enqueue(new Callback<List<CitiesDAO>>() {
            @Override
            public void onResponse(Call<List<CitiesDAO>> call, retrofit2.Response<List<CitiesDAO>> response) {
                List<CitiesDAO> citiesDAOS = response.body();
                for(int i=0; i < citiesDAOS.size(); i++ ){
                    String name = citiesDAOS.get(i).getNameCities();
                    listSpinner.add(name);
                }
                listSpinner.add(0,"-SELECT NAME CITY-");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item,listSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CitiesDAO>> call, Throwable t) {

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
