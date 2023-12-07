package all.muslimlife.data.network;

import all.muslimlife.data.model.chatai.ChatAIReq;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatAIApi {

    @POST("xhr_endpoint")
    Single<String> sendMessage(@Body ChatAIReq chatAiReq);
}
