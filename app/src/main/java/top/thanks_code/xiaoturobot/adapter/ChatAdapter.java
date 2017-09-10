package top.thanks_code.xiaoturobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import top.thanks_code.xiaoturobot.enumeration.MessageType;
import top.thanks_code.xiaoturobot.R;
import top.thanks_code.xiaoturobot.bean.Message;

/**
 * 主界面ListView的适配器类，负责将数据源（List<Message> mDatas）以一定的形式映射到ListView上
 * 该类可以作为模板代码保存起来，下次再用到ListView的时候就直接Copy，改一下数据源等
 * Created by Administrator on 2017/9/9.
 */

public class ChatAdapter extends BaseAdapter {

    //数据源
    private List<Message> mDatas;
    //上下文，主要负责初始化LayoutInflater
    private Context mContext;
    //LayoutInflater主要负责将xml文件转化为View
    private LayoutInflater mInflater;

    //构造方法，初始化数据
    public ChatAdapter(List<Message> datas, Context context) {
        mDatas = datas;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 这个方法是安卓特意为我们做聊天时设计的，这个我们用到了
     * @param position item在数据源的位置
     * @return 如果item里的getType方法返回了1，表明消息是来自图灵机器人，否则就是自己发的
     */
    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).getType() == MessageType.FROM_MSG){
            return 1;
        }else {
            return 2;
        }
    }

    //返回数据源的个数
    @Override
    public int getCount() {
        return mDatas.size();
    }

    //返回指定position的数据源对象（Message的实例）
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    //返回指定数据源的位置position
    @Override
    public long getItemId(int position) {
        return position;
    }

    //重点掌握！getView里面用到了Item的复用方案（搭配Viewholder，这些代码可以作为以后ListView的模板代码）
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null && getItemViewType(position) == 1) {
            //将item_from_msg转化为View
            convertView = mInflater.inflate(R.layout.item_from_msg, null);
            holder = new ViewHolder();
            //实例化在convertView里的TextView对象
            holder.textView = (TextView)convertView.findViewById(R.id.id_from_msg);
            convertView.setTag(holder);
        }if (convertView == null && getItemViewType(position) == 2) {
            convertView = mInflater.inflate(R.layout.item_to_msg, null);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.id_to_msg);
            convertView.setTag(holder);
        } else {
            //获得复用View
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(mDatas.get(position).getMsg());
        return convertView;
    }

    class ViewHolder{
        TextView textView;
    }
}
