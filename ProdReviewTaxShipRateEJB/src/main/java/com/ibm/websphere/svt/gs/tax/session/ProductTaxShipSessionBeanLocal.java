package com.ibm.websphere.svt.gs.tax.session;
import java.util.List;

import javax.ejb.Local;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ibm.websphere.svt.gs.tax.entity.ProdReview;
import com.ibm.websphere.svt.gs.tax.entity.ShipRate;
import com.ibm.websphere.svt.gs.tax.entity.TaxRate;

/**
 * 
 * @author jagraj
 *
 */
@Path("/productTaxShipService")
@Local
public interface ProductTaxShipSessionBeanLocal {
	@GET
	@Path("getAllStatesTaxInfo")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public List<TaxRate> getAllStatesTaxInfo() throws Exception;
	
	@GET
	@Path("getAllProductsShippingInfo")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public List<ShipRate> getAllProductsShippingInfo() throws Exception;
	
	@GET
	@Path("getShipRateByItemID")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public ShipRate getShipRateByItemID(@QueryParam("itemID")String itemID) throws Exception;
	
	@PUT
	@Path("saveProductReview")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public void saveProductReview(ProdReview prodReview) throws Exception;
	
}
