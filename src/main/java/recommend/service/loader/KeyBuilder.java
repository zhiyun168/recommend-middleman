package recommend.service.loader;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import recommend.utils.StringHelper;


/**
 * 缓存key生成器
 * Created by ouduobiao on 15/10/30.
 */
public abstract class KeyBuilder {

    /**
     * 推荐业务名
     * @return
     */
    public abstract String recName();

    public String recKey(Long id)
    {
        String recName = recName();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(recName), "推荐业务名不可空");
        return StringHelper.DotJoiner.join("feel:rec", recName, id);
    }
    public String recTmpKey(Long id)
    {
        String recName = recName();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(recName), "推荐业务名不可空");
        return StringHelper.DotJoiner.join("feel:rec", recName, "tmp", id);
    }
    public String recReasonKey(Long id)
    {
        String recName = recName();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(recName), "推荐业务名不可空");
        return StringHelper.DotJoiner.join("feel:rec", recName, "reason", id);
    }
    public String recReasonTmpKey(Long id){
        String recName = recName();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(recName), "推荐业务名不可空");
        return StringHelper.DotJoiner.join("feel:rec", recName, "reason:tmp",  id);
    }
    public String recLoadKey(){
        String recName = recName();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(recName), "推荐业务名不可空");
        return StringHelper.DotJoiner.join("feel:rec", recName, "load");
    }
    public String recLoadLockKey(Long id)
    {
        String recName = recName();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(recName), "推荐业务名不可空");
        return StringHelper.DotJoiner.join("feel:rec", recName, "load:lock",id);
    }
}
