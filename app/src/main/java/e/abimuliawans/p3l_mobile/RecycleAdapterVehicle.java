package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecycleAdapterVehicle extends RecyclerView.Adapter<RecycleAdapterVehicle.MyViewHolder> {

    private List<VehicleDAO> result;
    private Context context;

    public RecycleAdapterVehicle(Context context,List<VehicleDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_vehicle,viewGroup,false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        VehicleDAO vhc = result.get(i);
        myViewHolder.mMerk.setText(vhc.getMerkVehicle());
        myViewHolder.mType.setText(vhc.getTypeVehicle());
        myViewHolder.mId.setText(vhc.getIdVehicle());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mType,mMerk,mId;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            mType=itemView.findViewById(R.id.typeVehicle);
            mMerk=itemView.findViewById(R.id.merkVehicle);
            mId=itemView.findViewById(R.id.idVehicle);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }

}
