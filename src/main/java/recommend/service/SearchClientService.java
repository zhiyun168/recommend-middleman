package recommend.service;


import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SearchClientService implements InitializingBean, DisposableBean {
    private Logger logger = Logger.getLogger(SearchClientService.class);
    private Client searchClient = null;

    @Value("${elasticsearch.clusterName}")
    private String clusterName;
    @Value("${elasticsearch.clusterList}")
    private String clusterNodes;
    private List<String> clusterList = null;


	
    public void destroy() throws Exception {
        this.logger.info("关闭搜索客户端");
        this.close();
    }
    private void close() {
        if (this.searchClient == null) {
            return;
        }
        this.searchClient.close();
        this.searchClient = null;
    }
    public void afterPropertiesSet() throws Exception {
        this.logger.info("连接搜索服务器");

        this.open();
    }
    /**
     * 创建搜索客户端
     * tcp连接搜索服务器
     * 创建索引
     * 创建mapping
     * */
    private void open() {
		try {
			/* 如果10秒没有连接上搜索服务器，则超时 */
			Settings settings = null;
			if (this.clusterName == null || this.clusterName.isEmpty()) {//没设置集群名字
                settings = ImmutableSettings.settingsBuilder()
						.put("client.transport.ping_timeout", "10s")
						.put("client.transport.sniff", true)
						.build();
			} else {//设置了集群名字
                settings = ImmutableSettings
						.settingsBuilder()
						.put("client.transport.ping_timeout", "10s")
						.put("client.transport.sniff", true)
						.put("cluster.name", this.clusterName)
						// .put("client.transport.ignore_cluster_name", true)
						.build();
			}

			/* 创建搜索客户端 */
			this.searchClient = new TransportClient(settings);
			if (this.clusterList == null || this.clusterList.isEmpty()) {
				String cluster = clusterNodes;
				if (cluster != null) {
					this.clusterList = Arrays.asList(cluster.split(";"));
				}
			}
			for (String item : this.clusterList) {
				String address = item.split(":")[0];
				int port = Integer.parseInt(item.split(":")[1]);
				/* 通过tcp连接搜索服务器，如果连接不上，有一种可能是服务器端与客户端的jar包版本不匹配 */
				this.searchClient = ((TransportClient) this.searchClient)
						.addTransportAddress(new InetSocketTransportAddress(
								address, port));
			}
		} catch (Exception e) {
			this.logger.error("链接到es集群出错", e);
		}
	}
    
    public Client getSearchClient(){
    	return this.searchClient;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }
}
