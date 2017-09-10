package top.thanks_code.xiaoturobot.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.TuringManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import top.thanks_code.xiaoturobot.enumeration.MessageType;
import top.thanks_code.xiaoturobot.R;
import top.thanks_code.xiaoturobot.adapter.ChatAdapter;
import top.thanks_code.xiaoturobot.bean.Message;
import top.thanks_code.xiaoturobot.constant.Constants;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private TuringManager m;
    //ListView的初始化需要一个adapter
    private ChatAdapter mAdapter;
    //adapter的初始化需要一个List集合，这个集合是用来存放当前聊天界面所有的消息
    private List<Message> mMessages = new ArrayList<>();
    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initPermissions();
        initViews();
        initDatas();
    }

    //初始化组件
    private void initViews() {
        mEditText = (EditText) findViewById(R.id.id_et);
        mListView = (ListView) findViewById(R.id.id_lv);
        mButton = (Button) findViewById(R.id.button);
        //添加EditText文字变化监听
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果输入框里的文字为空，那么就设置Button（发送按钮）的背景为btn_no_text_bg，反之设为btn_text_bg
                if (s.toString().equals("")){
                    mButton.setBackgroundResource(R.drawable.btn_no_text_bg);
                }else {
                    mButton.setBackgroundResource(R.drawable.btn_text_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //自己构造一个消息
        Message message = makeMsg(MessageType.FROM_MSG,"你好啊，我是小图！");
        //把这条消息放到List集合里面
        mMessages.add(message);
        //初始化adapter
        mAdapter = new ChatAdapter(mMessages,this);
        //设置adapter到ListView
        mListView.setAdapter(mAdapter);
        //ListView滑动到底部
        mListView.smoothScrollToPosition(mAdapter.getCount());
    }

    //因为后期我们没有用百度语音识别的功能，所以这里就注释了整个方法
    /*//首先动态申请危险权限，这里为了简介，我们就不去重写onRequestPermissionsResult方法了，反正记得在运行程序的时候选择同意授权就好了
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }*/

    //初始化数据
    private void initDatas() {
        //实例化 TuringManager
        m = new TuringManager (this, Constants.APIKEY, Constants.SECRETKEY);
        m. setHttpRequestListener(new HttpRequestListener() {
            @Override
            public void onSuccess(String s) {
                //成功后回调，根据官方文档，返回的s是一个Json字符串
                try {
                    JSONObject obj = new JSONObject(s);  //将Json字符串转换为Json对象
                    //获取返回的消息字符串
                    String msg = (String) obj.get("text");
                    Message message = makeMsg(MessageType.FROM_MSG,msg);
                    mMessages.add(message);
                    mAdapter.notifyDataSetChanged();
                    mListView.smoothScrollToPosition(mAdapter.getCount());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int i, String s) {
                //失败后回调并且提示哪里出错
                Toast.makeText(MainActivity.this, "初始化失败！理由是："+s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //点击“发送”按钮来发送消息（点击按钮后安卓会自动回调这个方法，原因在于Button里有个属性：android:onClick="sendMsg"）
    public void sendMsg(View view) {
        if (mEditText.getText().toString().equals("")){
            //内容为空时
            Toast.makeText(this, "请输入消息！", Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = mEditText.getText().toString();
        Message message = makeMsg(MessageType.TO_MSG,msg);
        mMessages.add(message);
        //刷新消息列表
        mAdapter.notifyDataSetChanged();
        //消息列表自动滑动到底部
        mListView.smoothScrollToPosition(mAdapter.getCount());
        //向图灵机器人服务器提交消息，等待返回Json字符串，如果成功就会回调第100行的onSuccess方法，失败的话就回调116行的onFail方法
        m.requestTuring(msg);
        //消息发送完毕，清空输入框
        mEditText.setText("");
    }

    /**
     * 创建一个Message
     * @param type 消息类型
     * @param msg 消息内容
     * @return 返回构造实例
     */
    private Message makeMsg(MessageType type,String msg){
        Message message = new Message();
        message.setMsg(msg);
        message.setType(type);
        return message;
    }
}
