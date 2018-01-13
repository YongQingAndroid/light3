package demo;

import unit.LightAsyncExecutor;

/**
 * package Kotlin3:demo.demo.class
 * 作者：zyq on 2017/12/26 11:41
 * 邮箱：zyq@posun.com
 */

public class demo {
    private void test() {
        LightAsyncExecutor.execute(new LightAsyncExecutor.LightWorker() {
                                       @Override
                                       protected Object doInBackground() {
                                           return "workThread result";
                                       }

                                       @Override
                                       protected void onPostExecute(Object data) {
                                           super.onPostExecute(data);
                                       }

                                       @Override
                                       protected void onProgressUpdate(Object arg) {
                                           super.onProgressUpdate(arg);
                                       }
                                   }
        );
    }
}
