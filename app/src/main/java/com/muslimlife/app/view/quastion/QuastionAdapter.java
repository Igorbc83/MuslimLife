package com.muslimlife.app.view.quastion;

import static com.muslimlife.app.utils.Constants.IMAM_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.databinding.ItemRadioBinding;


public class QuastionAdapter extends RecyclerView.Adapter<QuastionAdapter.ViewHolder> {


    private final View view;
    private final ImamResMain[] imams;
    private final String filter;
    private final QuaistionView quastionView;

    public QuastionAdapter(View view, ImamResMain[] imams, String filter, QuaistionView quastionView) {
        this.view = view;
        this.imams = imams;
        this.filter=filter;
        this.quastionView=quastionView;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemRadioBinding binding = ItemRadioBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        for(String type:imams[position].getTypes())
            if(type.equals(filter)||filter.equals("-1")){
                holder.binding.dialogTitel.setText(imams[position].getFirst_name()+" "+imams[position].getLast_name());

                Glide.with(holder.binding.getRoot().getContext()).load(imams[position].getAvatar()).into(holder.binding.dialogIcon);

                holder.itemView.setOnClickListener(v->{
                    Bundle bundle = new Bundle();
                    bundle.putString(IMAM_ID, imams[position].getId());
                    NavDirections action= QuastionFragmentDirections.quastionFragmentToImamFragment();
                    Navigation.findNavController(view).navigate(action);
                });

                holder.itemView.setOnClickListener(v->quastionView.chooseImam(imams[position]));

                for (int i=0; i<imams[position].getLanguages().length;i++)
                    if(i+1!=imams[position].getLanguages().length)
                        holder.binding.dialogBottomText.setText(holder.binding.dialogBottomText.getText()+""+imams[position].getLanguages()[i]+", ");
                    else{
                        holder.binding.dialogBottomText.setText(holder.binding.dialogBottomText.getText()+""+imams[position].getLanguages()[i]);
                        holder.itemView.setVisibility(View.VISIBLE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        return;
                    }
            }else{
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
    }

    @Override
    public int getItemCount() {
        return imams.length;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ItemRadioBinding binding;


        public ViewHolder(ItemRadioBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
