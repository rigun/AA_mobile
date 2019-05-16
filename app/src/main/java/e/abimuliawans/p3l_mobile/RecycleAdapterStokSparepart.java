package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterStokSparepart extends RecyclerView.Adapter<RecycleAdapterStokSparepart.MyViewHolder> {

    private List<StokSparepartDAO> result;
    private List<StokSparepartDAO> listFull;
    private Context context;

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
}
