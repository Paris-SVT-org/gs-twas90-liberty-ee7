/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author JAGRAJ
 *
 */
public class GarageSaleEndpointConfigurator extends	ServerEndpointConfig.Configurator {
	
	
	/**
	 * 
	 */
	@Override
	public void modifyHandshake(ServerEndpointConfig config,
			HandshakeRequest request, HandshakeResponse response) {
		HttpSession httpSession = (HttpSession) request.getHttpSession();
		ServletContext context = httpSession.getServletContext();
		String videosRealPath=context.getRealPath("resources/videos");
		if(videosRealPath ==null){
			videosRealPath= GarageSaleWebSocketsUtil.getGarageSaleVideosLocation();
		}
		config.getUserProperties().put("filePath", videosRealPath);
	}
}
