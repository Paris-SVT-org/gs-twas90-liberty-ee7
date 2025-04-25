/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author JAGRAJ
 *
 */
@Named
@ApplicationScoped

public class GarageSaleProdctVideosCacheBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConcurrentHashMap<String,ByteBuffer> productDemoVideosMap;
	private ConcurrentHashMap<String,ByteBuffer> publishProductVideos;
	/**
	 * @return the productDemoVideosMap
	 */
	public ConcurrentHashMap<String, ByteBuffer> getProductDemoVideosMap() {
		return productDemoVideosMap;
	}
	/**
	 * @param productDemoVideosMap the productDemoVideosMap to set
	 */
	public void setProductDemoVideosMap(
			ConcurrentHashMap<String, ByteBuffer> productDemoVideosMap) {
		this.productDemoVideosMap = productDemoVideosMap;
	}
	/**
	 * @return the publishProductVideos
	 */
	public ConcurrentHashMap<String, ByteBuffer> getPublishProductVideos() {
		return publishProductVideos;
	}
	/**
	 * @param publishProductVideos the publishProductVideos to set
	 */
	public void setPublishProductVideos(
			ConcurrentHashMap<String, ByteBuffer> publishProductVideos) {
		this.publishProductVideos = publishProductVideos;
	}
	

}
