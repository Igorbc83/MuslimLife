package all.muslimlife.view.migrants.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemMigrantBinding;
import com.muslimlife.app.view.adapters.OnItemSelectListener;

import java.util.List;

import com.muslimlife.app.R;
import all.muslimlife.data.model.migrants.MigrantModel;
import com.muslimlife.app.databinding.ItemMigrantBinding;
import com.muslimlife.app.view.adapters.OnItemSelectListener;

public class MigrantsAdapter extends RecyclerView.Adapter<MigrantsAdapter.MigrantViewHolder> {

    private List<MigrantModel> items;

    private OnItemSelectListener listener;

    private int selectedPosition = -1, lastSelectedPosition;

    public MigrantsAdapter(List<MigrantModel> items, OnItemSelectListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MigrantsAdapter.MigrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMigrantBinding binding = ItemMigrantBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new MigrantsAdapter.MigrantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MigrantsAdapter.MigrantViewHolder holder, int position) {
        MigrantModel item = items.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MigrantViewHolder extends RecyclerView.ViewHolder {

        private final ItemMigrantBinding binding;

        public MigrantViewHolder(ItemMigrantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MigrantModel item, int position) {
            Glide.with(binding.migrantIcon.getContext())
                    .load(item.getImgLink())
                    .into(binding.migrantIcon);

            if(item.getName() != null) {
                binding.name.setVisibility(View.VISIBLE);
                binding.name.setText(item.getName());
            }else{
                binding.name.setVisibility(View.GONE);
            }

            if(item.getWebsite() != null) {
                binding.site.setVisibility(View.VISIBLE);
                binding.site.setText(item.getWebsite());
                binding.site.setOnClickListener(view -> {
                    listener.openWebsite(item.getWebsite());
                });
            }else{
                binding.site.setVisibility(View.GONE);
            }

            if(item.getDescription() != null) {
                binding.desc.setVisibility(View.VISIBLE);
                binding.desc.setText(item.getDescription());
            }else{
                binding.desc.setVisibility(View.GONE);
            }

            if(item.getOfficeEmail() != null) {
                binding.email.setVisibility(View.VISIBLE);
                binding.email.setText(item.getOfficeEmail());

                binding.btnWrite.setVisibility(View.VISIBLE);
                binding.btnWrite.setOnClickListener(view -> {
                    listener.sendEmail(item.getOfficeEmail());
                });
            }else{
                binding.email.setVisibility(View.GONE);
                binding.btnWrite.setVisibility(View.GONE);
            }

            if(item.getOfficeAddress() != null) {
                binding.address.setVisibility(View.VISIBLE);
                binding.address.setText(item.getOfficeAddress());
            }else{
                binding.address.setVisibility(View.GONE);
            }

            if(item.getContactPhone() != null) {
                binding.phone.setVisibility(View.VISIBLE);
                binding.phone.setText(item.getContactPhone());

                binding.btnCall.setVisibility(View.VISIBLE);
                binding.btnCall.setOnClickListener(view -> {
                    listener.callPhone(item.getContactPhone());
                });
            }else{
                binding.phone.setVisibility(View.GONE);
                binding.btnCall.setVisibility(View.GONE);
            }

            if(selectedPosition == position){
                binding.view.setStrokeWidth(
                        binding.getRoot().getResources().getDimensionPixelOffset(R.dimen.border_migrant));
                binding.infoView.setVisibility(View.VISIBLE);
            }else{
                binding.view.setStrokeWidth(0);
                binding.infoView.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(view -> {
                lastSelectedPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(position);
                notifyItemChanged(lastSelectedPosition);
            });

            binding.btnInMap.setVisibility(View.GONE);
            binding.btnInMap.setOnClickListener(view -> {

            });
        }
    }

    public void setItems(List<MigrantModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
