/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author JAGRAJ
 *
 */
public class InventoryOnSaleProgEndpointConfig implements ServerEndpointConfig{
	
	   String uriPath = "/inventoryOnSaleProgEndpoint";
	    HashMap<String,Object> properties=new HashMap<String,Object>();

	    public InventoryOnSaleProgEndpointConfig() {
	        // no-arg constructor
	    }

	    @Override
	    public Class<?> getEndpointClass() {
	        return InventoryOnSaleProgEndpoint.class;
	    }

	    @Override
	    public String getPath() {
	        return uriPath;
	    }

	    @Override
	    public Configurator getConfigurator() {
	        ServerEndpointConfig.Configurator x = new GarageSaleEndpointConfigurator();
	        return x;
	    }

	    @Override
	    public Map<String, Object> getUserProperties() {
	        return properties;
	    }

	    @Override
	    public List<Extension> getExtensions() {
	        return null;
	    }

	    @Override
	    public List<String> getSubprotocols() {
	        return null;
	    }

	    @Override
	    public List<Class<? extends Decoder>> getDecoders() {
	        return null;
	    }

	    @Override
	    public List<Class<? extends Encoder>> getEncoders() {
	        return null;
	    }


}
