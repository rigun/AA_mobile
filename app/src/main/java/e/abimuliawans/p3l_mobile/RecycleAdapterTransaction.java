package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterTransaction extends RecyclerView.Adapter<RecycleAdapterTransaction.MyViewHolder> implements Filterable {

    private List<TransactionByCabangDAO> result;
    private List<TransactionByCabangDAO> listFull;
    private Context context;

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
    public void onBindViewHolder(final RecycleAdapterTransaction.MyViewHolder myViewHolder, int i) {
        TransactionByCabangDAO transactionByCabangDAO = result.get(i);
        myViewHolder.mIdTrans.setText(transactionByCabangDAO.getIdTransaction());
        myViewHolder.mNumber.setText(transactionByCabangDAO.getTransactionNumber());
        myViewHolder.mService.setText("Total Service :"+transactionByCabangDAO.getTotalServices());
        myViewHolder.mSparepart.setText("Total Sparepart :"+transactionByCabangDAO.getTotalSpareparts());
        myViewHolder.mIDCustomer.setText(transactionByCabangDAO.getCustomer_id());
        myViewHolder.mPayment.setText("Payment :"+transactionByCabangDAO.getPayment());
        myViewHolder.mDiskon.setText("Diskon :"+transactionByCabangDAO.getDiskon());

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mNumber,mIDCustomer,mService,mIdTrans,mSparepart,mPayment,mDiskon;
        private CardView cardViewkon;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mIdTrans=itemView.findViewById(R.id.idTransaction);
            mNumber=itemView.findViewById(R.id.transNumber);
            mIDCustomer=itemView.findViewById(R.id.idCustomerTrans);
            mService=itemView.findViewById(R.id.totalServiceTrans);
            mSparepart=itemView.findViewById(R.id.totalSparepartTrans);
            mPayment=itemView.findViewById(R.id.paymentTrans);
            mDiskon=itemView.findViewById(R.id.diskonTrans);

            cardViewkon=itemView.findViewById(R.id.cardViewAdapterKon);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
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
