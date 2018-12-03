package com.share.web.config;

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
public class ESConfig implements InitializingBean,FactoryBean<TransportClient>{

	@Value("${cluster-name}")
	private String name;
	@Value("${cluster-nodes}")
	private String nodes;
	private TransportClient client;
	@Override
	public TransportClient getObject() throws Exception {
		// TODO Auto-generated method stub
		return client;
	}
	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return TransportClient.class;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		String[] node = nodes.split(",");
		Settings set=Settings.builder().put("cluster.name",name).build();
		client=new PreBuiltTransportClient(set);
		for (String hostAndPort : node) {
			String host=hostAndPort.split(":")[0];
			String port=hostAndPort.split(":")[1];
			client.addTransportAddress(new InetSocketTransportAddress(
	 				 InetAddress.getByName(host),Integer.parseInt(port)));
		}
		
	}
	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}
}
