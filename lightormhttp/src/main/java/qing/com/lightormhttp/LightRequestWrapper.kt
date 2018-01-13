package qing.com.lightormhttp

/**
 * Created by dell on 2017/11/8.
 */
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.util.concurrent.TimeUnit
/**
 * Created by Tony Shen on 2017/6/1.
 */
class LightRequestWrapper {

    var url: String? = null

    var method: String? = null

    var body: RequestBody? = null

    var stringbody: String? = null

    var timeout: Long = 10

    var header: Headers.Builder? = null

    internal var _success: (String) -> Unit = {}
    internal var _fail: (Throwable) -> Unit = {}
    fun onSuccess(onSuccess: (String) -> Unit) {
        _success = onSuccess
    }

    fun onFail(onError: (Throwable) -> Unit) {
        _fail = onError
    }

    fun addHeader(key: String, value: String) {
        if (header == null)
            header = Headers.Builder()
        header!!.add(key, value)
    }
    object HttpContent{
        var header: Headers.Builder? = null
    }
}

fun lighthttp(init: LightRequestWrapper.() -> Unit) {
    val wrap = LightRequestWrapper()

    wrap.init()

    executeForResult(wrap)
}

private fun executeForResult(wrap: LightRequestWrapper) {

    Flowable.create<Response>({ e ->
        e.onNext(onExecute(wrap))
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .subscribe(
                    { resp ->
                        wrap._success(resp.body()!!.string())
                    },
                    { e -> wrap._fail(e) })
}


private fun onExecute(wrap: LightRequestWrapper): Response? {

    var req: Request? = null
    if (wrap.body == null&&wrap.stringbody!=null)
        wrap.body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), wrap.stringbody)
    if(wrap.header==null&&LightRequestWrapper.HttpContent.header!=null)
        wrap.header= LightRequestWrapper.HttpContent.header
    when (wrap.method) {

        "get", "Get", "GET" -> req = getbuilder(wrap).build()
        "post", "Post", "POST" -> req = getbuilder(wrap).post(wrap.body).build()
        "put", "Put", "PUT" -> req = getbuilder(wrap).put(wrap.body).build()
        "delete", "Delete", "DELETE" -> req = getbuilder(wrap).delete(wrap.body).build()
    }
    val http = OkHttpClient.Builder().connectTimeout(wrap.timeout, TimeUnit.SECONDS).build()
    val resp = http.newCall(req).execute()
    return resp
}

private fun getbuilder(wrap: LightRequestWrapper):Request.Builder{
    var mBuilder= Request.Builder().url(wrap.url)
    if(wrap.header!=null)
    mBuilder.headers(wrap.header!!.build())
   return mBuilder
}
