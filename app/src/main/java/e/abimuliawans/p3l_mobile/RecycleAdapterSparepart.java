package e.abimuliawans.p3l_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RecycleAdapterSparepart extends RecyclerView.Adapter<RecycleAdapterSparepart.MyViewHolder> {

    private List<SparepartDAO> result;
    private List<SparepartDAO> listFull;
    private Context context;

    public RecycleAdapterSparepart(Context context,List<SparepartDAO> result){
        this.context = context;
        this.result = result;

        listFull= new ArrayList<>(result);
    }

    @NonNull
    @Override
    public RecycleAdapterSparepart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycle_adapter_sparepart,viewGroup,false);
        final RecycleAdapterSparepart.MyViewHolder holder = new RecycleAdapterSparepart.MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterSparepart.MyViewHolder myViewHolder, int i){
        SparepartDAO spr = result.get(i);
        myViewHolder.nameSparepart.setText(spr.getNamaSparepart());
        myViewHolder.codeSparepart.setText(spr.getCodeSparepart());
        myViewHolder.typeSparepart.setText(spr.getTypeSparepart());
        myViewHolder.merkSparepart.setText(spr.getMerkSparepart());


        byte[] decodedString = Base64.decode(spr.getPicSparepart(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.ic_cloud_upload);
        requestOptions.error(R.drawable.ic_cloud_upload);

        Glide.with(context)
                .load(decodedByte)
                .apply(requestOptions)
                .into(myViewHolder.gambarSpart);

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView codeSparepart,nameSparepart,merkSparepart,typeSparepart;
        private ImageView gambarSpart;
        private CardView cardSparepart;

        public MyViewHolder(@NonNull  View itemView){
            super(itemView);
            codeSparepart=(TextView) itemView.findViewById(R.id.codeSparepart);
            nameSparepart=(TextView) itemView.findViewById(R.id.nameSparepart);
            merkSparepart=(TextView) itemView.findViewById(R.id.merkSparepart);
            typeSparepart=(TextView) itemView.findViewById(R.id.typeSparepart);
            gambarSpart=(ImageView) itemView.findViewById(R.id.gambarSparepart);
        }

        @Override
        public void onClick(View v) {
            //Respon Click
        }
    }


}
