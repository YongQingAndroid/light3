package unit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created by zyq on 2016/7/5.
 * 线程池管理
 */
public class LightExcuter {
    private static LightExcuter self;
    private static int max_thread=3;
    private static  ExecutorService singlethreadserver=null;
    private static  ExecutorService threadserver= null;
    public static LightExcuter getSingleInstent(){
        if(self==null){
            self=new LightExcuter();
        }
            return self;
    }
    /**
     * 单线程线程池
     * */
    public void excutesingleTreadPool(Runnable run){
        if(singlethreadserver==null)
            singlethreadserver=Executors.newSingleThreadExecutor();
        singlethreadserver.execute(run);
    }
    /**
     * 线程池中线程的最大数量
     * */
    public static void setMax_thread(int max_thread) {
        LightExcuter.max_thread = max_thread;
    }
    /**
     * 执行线程操作
     * */
    public void excuteTreadPool(Runnable run){
        if(threadserver==null)
            threadserver= Executors.newFixedThreadPool(max_thread);
        threadserver.execute(run);
    }
}
