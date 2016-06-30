package recommend.service.loader.v2;

import com.google.common.base.Joiner;

/**
 * 缓存key生成器
 * Created by ouduobiao on 15/10/30.
 */
public abstract class KeyBuilder {

    private static final String prefix = "feel:rec";

    /**
     * 推荐业务名
     * @return
     */
    public abstract String recName();

    public String recKey(String id)
    {
        String recName = recName();
        return Joiner.on(":").join(prefix, recName, id);
    }

    public String recTmpKey(String id)
    {
        String recName = recName();
        return Joiner.on(":").join(prefix, recName, "tmp", id);
    }

    public String recLoadKey(){
        String recName = recName();
        return Joiner.on(":").join(prefix, recName, "load");
    }
    public String recLoadLockKey(String id)
    {
        String recName = recName();
        return Joiner.on(":").join(prefix, recName, "load:lock", id);
    }

}
