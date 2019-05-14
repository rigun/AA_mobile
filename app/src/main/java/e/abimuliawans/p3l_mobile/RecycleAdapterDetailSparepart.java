package e.abimuliawans.p3l_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class RecycleAdapterDetailSparepart extends RecyclerView.Adapter<RecycleAdapterDetailSparepart.MyViewHolder> {
    private List<TransactionDAO> result;
    private List<TransactionDAO> listFull;
    private Context context;

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
        TransactionDAO transactionDAO = result.get(i);
        myViewHolder.mId.setText(transactionDAO.getDetailSparepart().getIdDetailSpare());
        myViewHolder.mCode.setText(transactionDAO.getDetailSparepart().getCodeSpare());
        myViewHolder.mTotal.setText("Total : "+transactionDAO.getDetailSparepart().getTotalSpare());
        myViewHolder.mPrice.setText("Harga :"+transactionDAO.getDetailSparepart().getPrice());

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
                            }
                        });


                builderSal.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete
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
}
