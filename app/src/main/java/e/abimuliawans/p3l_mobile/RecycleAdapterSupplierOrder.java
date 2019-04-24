package e.abimuliawans.p3l_mobile;

import android.content.Context;
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

public class RecycleAdapterSupplierOrder extends RecyclerView.Adapter<RecycleAdapterSupplierOrder.MyViewHolder> {
    private List<SupplierDAO> result;
    private List<SupplierDAO> listFull;
    private Context context;

    public RecycleAdapterSupplierOrder(Context context,List<SupplierDAO> result) {
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSupplierOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_supplier,viewGroup,false);
        final RecycleAdapterSupplierOrder.MyViewHolder holder = new RecycleAdapterSupplierOrder.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSupplierOrder.MyViewHolder myViewHolder, final int i) {
        SupplierDAO spl = result.get(i);
        myViewHolder.mName.setText(spl.getNameSupplier());
        myViewHolder.mCity.setText(spl.getCitySupplier());
        myViewHolder.mAddress.setText(spl.getAddressSupplier());
        myViewHolder.mPhone.setText(spl.getPhoneSupplier());
        myViewHolder.mId.setText(spl.getIdSupplier());

        myViewHolder.cardViewSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save Asset Order
                SharedPreferences prefSupplier = context.getSharedPreferences("MyOrder", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefSupplier.edit();
                editor.putString("idSupplier", myViewHolder.mId.getText().toString());
                editor.commit();

                Intent intent = new Intent(context,SparepartBySupActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mName,mAddress,mCity,mId,mPhone;
        private CardView cardViewSup;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mName=itemView.findViewById(R.id.nameSupplier);
            mAddress=itemView.findViewById(R.id.addressSupplier);
            mId=itemView.findViewById(R.id.idSupplier);
            mPhone=itemView.findViewById(R.id.phoneSupplier);
            mCity=itemView.findViewById(R.id.citySupplier);
            cardViewSup=itemView.findViewById(R.id.cardViewAdapterSup);
            linearLayout=itemView.findViewById(R.id.linearSupplier);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }
}
