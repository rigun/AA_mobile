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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecycleAdapterTransactionDetail extends RecyclerView.Adapter<RecycleAdapterTransactionDetail.MyViewHolder>{
    private List<TransactionDetailDAO> result;
    private List<TransactionDetailDAO> listFull;
    private Context context;
    private CardView cardViewSparepart,cardViewLayanan,cardViewEdit,cardViewDelete;

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
                        Intent intent = new Intent(context,DetailLayananActivity.class);
                        context.startActivity(intent);
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
}
