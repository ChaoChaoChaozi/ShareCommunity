package com.share.config;
/*package com.jt.common.config;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ElasticSearchConfiguration implements FactoryBean<TransportClient>,
InitializingBean{
	@Value("${cluster-nodes:null}")
	private String clusterNodes;
	@Value("${cluster-name:null}")
	private String clusterName;
	private TransportClient client;
	
	 * 属于InitializingBean接口的,在这个方法中初始化对象 TransportClient
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Settings settings=Settings.builder().
				put("cluster.name", clusterName).build();
		client=new 
				PreBuiltTransportClient(settings);
		//截取nodes数据
		String[] nodes = clusterNodes.split(",");
		for (String node : nodes) {
			//在集群的es中,连续add节点信息
			//配置链接信息
			String[] hostAndPort = node.split(":");
			client.addTransportAddress(
				new InetSocketTransportAddress(
				InetAddress.getByName(hostAndPort[0]), 
				Integer.parseInt(hostAndPort[1])));
		}
		
	}
	
	 * 这个方法,返回数据就是初始化的对象transportClient
	 
	@Override
	public TransportClient getObject() throws Exception {
		//spring框架管理这个方法的返回对象
		int a=1;
		return client;
	}
	
	 * 配合getObject方法,必须返回泛型的反射对象,TransportClient.class
	 
	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return TransportClient.class;
	}
	
	 * 对象的单例使用,保持默认false 非单例
	 
	@Override
	public boolean isSingleton() {
		
		return false;
	}

}
*/