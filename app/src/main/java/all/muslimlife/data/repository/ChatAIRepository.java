package all.muslimlife.data.repository;

import java.util.ArrayList;
import java.util.List;

import all.muslimlife.data.model.chatai.ChatAIReq;
import all.muslimlife.data.network.ChatAIApi;
import all.muslimlife.view.chat.adapters.ChatAIRecyclerModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatAIRepository {

    private ChatAIApi api;

    private List<ChatAIRecyclerModel> items;

    public ChatAIRepository(ChatAIApi chatAIApi) {
        this.api = chatAIApi;
        items = new ArrayList<>();
    }

    public Single<String> sendMessage(String msg) {
        return api.sendMessage(new ChatAIReq(msg))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<ChatAIRecyclerModel> getItems() {
        return items;
    }
}
