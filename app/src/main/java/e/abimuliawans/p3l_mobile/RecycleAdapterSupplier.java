package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterSupplier extends RecyclerView.Adapter<RecycleAdapterSupplier.MyViewHolder> implements Filterable {

    private List<SupplierDAO> result;
    private List<SupplierDAO> listFull;
    private Context context;

    public RecycleAdapterSupplier(Context context,List<SupplierDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSupplier.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_supplier,viewGroup,false);
        final RecycleAdapterSupplier.MyViewHolder holder = new RecycleAdapterSupplier.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleAdapterSupplier.MyViewHolder myViewHolder, int i) {
        SupplierDAO spl = result.get(i);
        myViewHolder.mName.setText(spl.getNameSupplier());
        myViewHolder.mCity.setText(spl.getCitySupplier());
        myViewHolder.mAddress.setText(spl.getAddressSupplier());
        myViewHolder.mPhone.setText(spl.getPhoneSupplier());
        myViewHolder.mId.setText(spl.getIdSupplier());

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mName,mAddress,mCity,mId,mPhone;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mName=itemView.findViewById(R.id.nameSupplier);
            mAddress=itemView.findViewById(R.id.addressSupplier);
            mId=itemView.findViewById(R.id.idSupplier);
            mPhone=itemView.findViewById(R.id.phoneSupplier);
            mCity=itemView.findViewById(R.id.citySupplier);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }


    @Override
    public Filter getFilter() {
        return supplierFilter;
    }

    private Filter supplierFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SupplierDAO> filterList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filterList.addAll(listFull);
            }else {
                String filterPatten = constraint.toString().toLowerCase().trim();

                for(SupplierDAO supplierDAO : listFull){
                    if (supplierDAO.getNameSupplier().toLowerCase().contains(filterPatten)){
                        filterList.add(supplierDAO);
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
