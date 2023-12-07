package all.muslimlife.view.chat.adapters;

public class ChatAIRecyclerModel {

    public static final int CHAT_MSG_AI = 1;
    public static final int CHAT_MSG_ME = 2;
    public static final int CHAT_MSG_LOADING = 3;

    private int type;

    private String msg;

    public ChatAIRecyclerModel(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
