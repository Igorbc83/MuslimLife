package all.muslimlife.view.chat.adapters;

import static all.muslimlife.view.chat.adapters.ChatAIRecyclerModel.CHAT_MSG_AI;
import static all.muslimlife.view.chat.adapters.ChatAIRecyclerModel.CHAT_MSG_LOADING;
import static all.muslimlife.view.chat.adapters.ChatAIRecyclerModel.CHAT_MSG_ME;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.muslimlife.app.databinding.ItemChatMsgAiBinding;
import com.muslimlife.app.databinding.ItemChatMsgLoadingBinding;
import com.muslimlife.app.databinding.ItemChatMsgMeBinding;

public class ChatAIAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ChatAIRecyclerModel> items;

    public ChatAIAdapter(List<ChatAIRecyclerModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CHAT_MSG_AI:
               return new MsgAIHolder(ItemChatMsgAiBinding.inflate(
                       LayoutInflater.from(parent.getContext()), parent, false));
            case CHAT_MSG_ME:
                return new MsgMeHolder(ItemChatMsgMeBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false));
            default:
                return new MsgLoadingHolder(ItemChatMsgLoadingBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        switch (items.get(position).getType()) {
            case CHAT_MSG_AI:
                ((MsgAIHolder) holder).init(items.get(position).getMsg());
                break;
            case CHAT_MSG_ME:
                ((MsgMeHolder) holder).init(items.get(position).getMsg());
                break;
            case CHAT_MSG_LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addMessage(ChatAIRecyclerModel chatModel){
        if(chatModel.getType() == CHAT_MSG_AI){
            if(items.isEmpty()){
                items.add(chatModel);
                notifyItemChanged(0);
            }else {
                items.set(items.size() - 1, chatModel);
                notifyItemChanged(items.size() - 1);
            }
        }else{
            items.add(chatModel);
            notifyItemInserted(items.size() - 1);
        }
    }

    public class MsgAIHolder extends  RecyclerView.ViewHolder{

        ItemChatMsgAiBinding binding;

        public MsgAIHolder(ItemChatMsgAiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void init(String msg){
            binding.msg.setText(msg);
        }
    }

    public class MsgMeHolder extends  RecyclerView.ViewHolder{

        ItemChatMsgMeBinding binding;

        public MsgMeHolder(ItemChatMsgMeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void init(String msg){
            binding.msg.setText(msg);
        }
    }

    public class MsgLoadingHolder extends  RecyclerView.ViewHolder{

        ItemChatMsgLoadingBinding binding;

        public MsgLoadingHolder(ItemChatMsgLoadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void init(){

        }
    }
}
