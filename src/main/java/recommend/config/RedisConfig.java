package recommend.config;


import org.redisson.Config;
import org.redisson.Redisson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import recommend.redisson.StringCodecExt;

/**
 * Created by ouduobiao on 15/6/21.
 */
@Configuration
public class RedisConfig {

    private static Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${redis.address}")
    private String address;
    @Value("${redis.enable_cluster}")
    private String enableCluster;


    @Bean(destroyMethod="shutdown")
    @Scope("prototype")
    public Redisson redisson()
    {
        Config config = new Config();

        //config.setCodec(new StringCodecExt());
        log.info("redis address:" + address);
        if(enableCluster == null || enableCluster.equals("0"))
        {
            config.useSingleServer().setAddress(address);
        }
        else
        {
            String[] addressList = address.split(";");
            config.useClusterServers()
                    .setScanInterval(2000) // sets cluster state scan interval
                    .addNodeAddress(addressList);
        }
        return Redisson.create(config);
    }
}
