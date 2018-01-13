package qing.com.lightdatabinding;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据绑定框架
 * Created by dell on 2017/11/9.
 */
public class LightDataBingding<T> {
    private View viewGroup;
    private T dataSouce;
    private Map<Integer, Bindentity<T>> viewMap = new HashMap<>();
    private Map<Integer,View> extendView_map;
    public void bind_viewGroup( View viewGroup ) {
        this.viewGroup = viewGroup;
    }
    /**
     * 绑定View
     * @param id  View的id
     * @param call 数据回调
     * */
    public void bind_id( int id, dataCall<T> call ) {
        if (viewMap.containsKey(id)) {
            Bindentity<T> mbindentity = viewMap.get(id);
            mbindentity.textView.setText(mbindentity.dataCall.Call(dataSouce));
        } else {
            Bindentity<T> mbindentity = new Bindentity<>();
            mbindentity.textView = viewGroup.findViewById(id);
            mbindentity.dataCall = call;
            mbindentity.textView.setText(call.Call(dataSouce));
            viewMap.put(id, mbindentity);
        }
    }
    /**
     * 绑定View
     * @param id  View的id
     * @param call 数据回调
     * */
    public void bind_holder( int id, dataCall<T> call ) {
            Bindentity<T> mbindentity = new Bindentity<>();
            mbindentity.textView = viewGroup.findViewById(id);
            mbindentity.dataCall = call;
            viewMap.put(id, mbindentity);
    }
    public <M extends View>M getView(@IdRes int id){
        if(extendView_map==null)
            extendView_map=new HashMap<>();
        if(extendView_map.containsKey(id))
            return (M)extendView_map.get(id);
        View view=viewGroup.findViewById(id);
        extendView_map.put(id,view);
        return (M)view;
    }
    public <M extends View>M getView(Class<M> clazz,int id){
        return (M)getView(id);
    }
    /**
     * 绑定View
     * @param textView  View的id
     * @param call 数据回调
     * */
    public void bind_view( TextView textView, dataCall<T> call ) {

    }
    /**
     * @param obj 绑定数据源
     * */
    public void bind_data( T obj ) {
        dataSouce = obj;
    }

    /**
     * 数据发生变化更新UI
     * */
    public void dataNotify() {
        for (int key : viewMap.keySet()) {
            Bindentity mbindentity = viewMap.get(key);
            mbindentity.textView.setText(mbindentity.dataCall.Call(dataSouce));
        }
    }

    /**
     * 数据发生变化更新UI
     * @param obj 绑定数据源
     * */
    public void dataNotify( T obj ) {
        dataSouce = obj;
        dataNotify();
    }

    public interface dataCall<T> {
        String Call( T data );
    }

    private class Bindentity<M> {
        protected TextView textView;
        protected dataCall<M> dataCall;
    }
}
