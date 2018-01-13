package lightplugin.posun.com.fuchuang

import com.lightormdatabase.LightIgnore
import com.lightormdatabase.LightOrmBaseBean
import com.lightormdatabase.LightPrimaryKey
import com.lightormdatabase.LightTableName
import java.io.Serializable

/**
 * Created by dell on 2017/11/7.
 */
 @LightTableName("my_db") data class LightDbBean(@LightPrimaryKey var name: String, @LightIgnore var sex: String) : LightOrmBaseBean(), Serializable {
    constructor() : this("defult", "defult")
}