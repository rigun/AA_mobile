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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RecycleAdapterStokSparepart extends RecyclerView.Adapter<RecycleAdapterStokSparepart.MyViewHolder> {

    private List<StokSparepartDAO> result;
    private List<StokSparepartDAO> listFull;
    private Context context;
    private EditText txtCodeSparepart,txtPosition,txtSell,txtBuy,txtStock,txtLimitStock;

    public RecycleAdapterStokSparepart(Context context,List<StokSparepartDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterStokSparepart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_stok_sparepart,viewGroup,false);
        final RecycleAdapterStokSparepart.MyViewHolder holder = new RecycleAdapterStokSparepart.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterStokSparepart.MyViewHolder myViewHolder, int i) {
        StokSparepartDAO stokSparepartDAO = result.get(i);
        myViewHolder.mCode.setText(stokSparepartDAO.getCodeSpareScp());
        myViewHolder.mBuy.setText("Harga Beli : "+stokSparepartDAO.getBuyScp());
        myViewHolder.mSell.setText("Harga Jual : "+stokSparepartDAO.getSellScp());
        myViewHolder.mStok.setText("Stok : "+stokSparepartDAO.getStockScp());
        myViewHolder.mLimitStok.setText("Limit Stok : "+stokSparepartDAO.getLimitstockScp());
        myViewHolder.mPosition.setText(stokSparepartDAO.getPositionScp());


        final String code = stokSparepartDAO.getCodeSpareScp();
        final String buy = stokSparepartDAO.getBuyScp();
        final String sell = stokSparepartDAO.getSellScp();
        final String stok = stokSparepartDAO.getStockScp();
        final String limitStok = stokSparepartDAO.getLimitstockScp();
        final String position = stokSparepartDAO.getPositionScp();
        final String idSpare = stokSparepartDAO.getIdScp();

        //Get Token
        SharedPreferences pref = context.getSharedPreferences("MyToken", MODE_PRIVATE);
        final String token = pref.getString("token_access", null);
        final String BASE_URL = pref.getString("BASE_URL",null);

        //For Konsumen
        SharedPreferences pref2 = context.getSharedPreferences("ForKonsumen", MODE_PRIVATE);
        final String forKonusmen = pref2.getString("answer", null);

        if(forKonusmen.equals("yes"))
        {
            myViewHolder.mSell.setVisibility(View.GONE);
            myViewHolder.mPosition.setVisibility(View.GONE);
            myViewHolder.mLimitStok.setVisibility(View.GONE);
        }

        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(request);
            }
        });

        myViewHolder.cardViewStok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(forKonusmen.equals("yes"))
                {
                    // Tidak Dapat di klik
                }
                else if(forKonusmen.equals("no"))
                {
                    AlertDialog.Builder builderSal = new AlertDialog.Builder(context);
                    builderSal.setTitle("Pilih Pengaturan :");
                    builderSal.setMessage(myViewHolder.mCode.getText().toString());
                    builderSal.setCancelable(true);



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
                                    //Edit Alert
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                    AlertDialog.Builder mBuilderEditSpare = new AlertDialog.Builder(context);
                                    View mViewEditSpare = inflater.inflate(R.layout.dialog_edit_sparepart_cabang,null);

                                    //Set Edit Text
                                    txtCodeSparepart = mViewEditSpare.findViewById(R.id.txtSparepart_codeEdit);
                                    txtPosition = mViewEditSpare.findViewById(R.id.txtPositionEdit);
                                    txtBuy = mViewEditSpare.findViewById(R.id.txtBuyEdit);
                                    txtSell = mViewEditSpare.findViewById(R.id.txtSellEdit);
                                    txtStock = mViewEditSpare.findViewById(R.id.txtStockEdit);
                                    txtLimitStock = mViewEditSpare.findViewById(R.id.txtLimitStokEdit);

                                    txtCodeSparepart.setText(code);
                                    txtPosition.setText(position);
                                    txtBuy.setText(buy);
                                    txtSell.setText(sell);
                                    txtStock.setText(stok);
                                    txtLimitStock.setText(limitStok);

                                    mBuilderEditSpare.setView(mViewEditSpare)
                                            .setPositiveButton("Edit Data", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Edit
                                                    editSparepartCabang(httpClient,BASE_URL,idSpare,code,position,
                                                            sell,buy,stok,limitStok);

                                                }
                                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Cancel
                                        }
                                    });

                                    AlertDialog dialogEdit = mBuilderEditSpare.create();
                                    dialogEdit.show();

                                }
                            });

                    AlertDialog alertDialog = builderSal.create();
                    alertDialog.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mBuy,mSell,mPosition,mCode,mStok,mLimitStok;
        private CardView cardViewStok;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mBuy=itemView.findViewById(R.id.buyStokSparepart);
            mSell=itemView.findViewById(R.id.sellStokSparepart);
            mPosition=itemView.findViewById(R.id.positionStokSprepart);
            mCode=itemView.findViewById(R.id.codeStokSparepart);
            mStok=itemView.findViewById(R.id.stockStokSparepart);
            mLimitStok=itemView.findViewById(R.id.limitStokSparepart);
            cardViewStok=itemView.findViewById(R.id.cardViewAdapterStokSparepart);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

    public void editSparepartCabang(OkHttpClient.Builder httpClientAdd, String BASE_URL, String idSpare, String code,
                                    String posistion, String sell, String buy, String stok, String limitStok) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientAdd.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiClient apiClient = retrofit.create(ApiClient.class);
        Call<StokSparepartDAO> orderDAOCall = apiClient.editSparepartCabang(idSpare,code,posistion,limitStok,sell,buy,
                stok);
        orderDAOCall.enqueue(new Callback<StokSparepartDAO>() {
            @Override
            public void onResponse(Call<StokSparepartDAO> call, Response<StokSparepartDAO> response) {
                Toasty.success(context, "Sparepart Berhasil Diedit",
                        Toast.LENGTH_SHORT, true).show();

                Intent intent = new Intent(context,DasboardActivity.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<StokSparepartDAO> call, Throwable t) {
                Toasty.error(context, "Gagal Edit Data",
                        Toast.LENGTH_SHORT, true).show();
            }
        });

    }
}
