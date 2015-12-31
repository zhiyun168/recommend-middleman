package recommend.config;

import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.Settings;
import com.google.code.ssm.config.AddressProvider;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.CacheClientFactory;
import com.google.code.ssm.providers.CacheConfiguration;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by ouduobiao on 15/12/31.
 */
@Configuration
@ImportResource( { "classpath*:/simplesm-context.xml"} )
@EnableAspectJAutoProxy
public class MemcachedConfig {


    @Bean
    public CacheFactory defaultMemcachedClient(CacheClientFactory cacheClientFactory,
                                               AddressProvider addressProvider,
                                               CacheConfiguration configuration){
        CacheFactory cacheFactory = new CacheFactory();
        cacheFactory.setCacheName("default");
        cacheFactory.setCacheClientFactory(cacheClientFactory);
        cacheFactory.setAddressProvider(addressProvider);
        cacheFactory.setConfiguration(configuration);

        return cacheFactory;
    }

    @Bean
    public CacheClientFactory cacheClientFactory()
    {
        return  new MemcacheClientFactoryImpl();
    }

    @Bean
    public AddressProvider addressProvider(@Value("${memcached.hosts}") String hosts)
    {
        return  new DefaultAddressProvider(hosts);
    }

    @Bean
    public CacheConfiguration configuration()
    {
        CacheConfiguration configuration =  new CacheConfiguration();
        configuration.setConsistentHashing(true);
        return configuration;
    }

    @Bean
    public Settings settings()
    {
        Settings settings = new Settings();
        settings.setOrder(500);
        return settings;
    }

}
