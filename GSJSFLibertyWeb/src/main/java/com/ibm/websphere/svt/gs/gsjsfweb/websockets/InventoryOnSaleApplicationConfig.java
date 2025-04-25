/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb.websockets;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author JAGRAJ
 *
 */
public class InventoryOnSaleApplicationConfig implements ServerApplicationConfig{
    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> arg0) {
        // Accept for usage any endpoint classes that have been found for this application.
        return arg0;
    }

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
        // Intialize and return the endpoint configuration object that will be used for our coded endpoint in this application.
        Set<ServerEndpointConfig> configs = new HashSet<ServerEndpointConfig>();
        InventoryOnSaleProgEndpointConfig config = new InventoryOnSaleProgEndpointConfig();
        configs.add(config);
        return configs;
    }


}
