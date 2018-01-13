package unit;

import android.os.Looper;

/**
 * Created by zyq on 2017/1/9.
 * 异步处理类
 * 默认使用线程池调度子线程
 */
public class LightAsyncExecutor {
    public static void execute(final LightWorker lightworker) {
        LightExcuter.getSingleInstent().excuteTreadPool(new Runnable() {
            @Override
            public void run() {
                final Object object = lightworker.doInBackground();
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        lightworker.onPostExecute(object);
                    }
                });
            }
        });
    }

    /**
     * 执行子线程任务
     */
    public static abstract class LightWorker<T, P> {
        /**
         * 相当于run方法
         */
        protected abstract T doInBackground();

        /**
         * 回调信息到主线程（选用可以不实现）
         */
        protected void onPostExecute(T data) {
        }

        /**
         * 用于同步修改状态例如进度条
         */
        protected final void pushProgress(final P arg) {
            if (!(Thread.currentThread().getName().equalsIgnoreCase("main"))) {

                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onProgressUpdate(arg);
                    }
                });
            } else {
                onProgressUpdate(arg);
            }
        }

        /**
         * 用于同步修改状态例如进度条等（选用可以不实现）
         */
        protected void onProgressUpdate(P arg) {
        }
    }
}
