package recommend.redis.beanfactory;

import org.springframework.data.redis.connection.RedisSentinelConfiguration;

public class RedisSentinelConfigurationFactory {
	/**
	 * redis集群名字
	 */
	private String clusterName;
	/**
	 * redis集群列表
	 */
	private String clusterList;
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getClusterList() {
		return clusterList;
	}
	public void setClusterList(String clusterList) {
		this.clusterList = clusterList;
	}
    
    /**
     * 获取redis链接配置项factory
     * @return
     */
    public RedisSentinelConfiguration getSentinelConfig() {
    	String []cluster_list = clusterList.split(";");
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(clusterName);
        for(String node : cluster_list){
        	String []host_port = node.split(":");
        	sentinelConfig.sentinel(host_port[0], Integer.parseInt(host_port[1]));
        }
        return sentinelConfig;
    }
}
