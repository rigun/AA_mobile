package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterTransactionKonsumen extends RecyclerView.Adapter<RecycleAdapterTransactionKonsumen.MyViewHolder> {

    private List<TransactionByCabangDAO> result;
    private List<TransactionByCabangDAO> listFull;
    private Context context;
    private Spinner spinnerKedaraan,spinnerJenisTransEdit,spinnerCity;
    private EditText platNomor,nameKonsumen,phoneKonsumen,alamatKonsumen;
    private List<String> listSpinnerKendaraan = new ArrayList<String>();
    private List<String> listSpinner = new ArrayList<>();

    public RecycleAdapterTransactionKonsumen(Context context,List<TransactionByCabangDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterTransactionKonsumen.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_transaction_konsumen,viewGroup,false);
        final RecycleAdapterTransactionKonsumen.MyViewHolder holder = new RecycleAdapterTransactionKonsumen.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterTransactionKonsumen.MyViewHolder myViewHolder, final int i) {
        final TransactionByCabangDAO transactionByCabangDAO = result.get(i);
        myViewHolder.mNumber.setText(transactionByCabangDAO.getTransactionNumber() + "-" + transactionByCabangDAO.getIdTransaction());
        myViewHolder.mService.setText("Total Service :" + transactionByCabangDAO.getTotalServices());
        myViewHolder.mSparepart.setText("Total Sparepart :" + transactionByCabangDAO.getTotalSpareparts());
        myViewHolder.mPayment.setText("Payment :" + transactionByCabangDAO.getPayment());
        myViewHolder.mDiskon.setText("Diskon :" + transactionByCabangDAO.getDiskon());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNumber,mIDCustomer,mService,mSparepart,mPayment,mDiskon;
        private CardView cardView;
        private Button btnStatus;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mNumber=itemView.findViewById(R.id.transNumberKonsumen);
            mService=itemView.findViewById(R.id.totalServiceTransKonsumen);
            mSparepart=itemView.findViewById(R.id.totalSparepartTransKonsumen);
            mPayment=itemView.findViewById(R.id.paymentTransKonsumen);
            mDiskon=itemView.findViewById(R.id.diskonTransKonsumen);

        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

}
