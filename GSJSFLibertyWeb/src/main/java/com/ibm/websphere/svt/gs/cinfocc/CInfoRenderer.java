package com.ibm.websphere.svt.gs.cinfocc;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;


@FacesRenderer(rendererType = "cinfocc", componentFamily = "javax.faces.Input")
@ResourceDependencies({
	@ResourceDependency(name = "cinfo.css", library = "org.was.svt", target = "head"),
    @ResourceDependency(name = "jsf.js", library = "javax.faces", target = "body")})
public class CInfoRenderer extends Renderer {
	private static String componentName = "com.ibm.websphere.svt.gs.cinfocc";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = CInfoRenderer.class.getName();
	
	public CInfoRenderer() {
		super();
	}
	
	@Override
    public void encodeBegin(FacesContext facesContext, UIComponent comp) throws IOException {
		String methodName = "encodeBegin";
		
		CInfoUI cinfoui = (CInfoUI) comp;
	    logger.logp(Level.FINE, className, methodName, "starting up...with cinfo : " + cinfoui);
	    
	    String clientID = cinfoui.getClientId();
	    logger.logp(Level.FINE, className, methodName, "working on clientID " + clientID);
	    
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<table id=\"" + clientID +"\" class=\"mctable\">");
        renderFooter(writer, 1, cinfoui);
        renderCells(facesContext, writer, cinfoui);
        
        writer.write("</table>");
    }
	
	private void renderCells(FacesContext facesContext, ResponseWriter writer, CInfoUI cinfoui) throws IOException {
        String methodName = "renderCells";
		int i = 0;
        int rno = 0;  // this should start from 0
        //int r = sinek.getWorkSheet().getNumOfColumns();
        int r = 1; // we have only 1 column
        List<UIComponent> rows = cinfoui.getChildren();
        logger.logp(Level.FINER, className, methodName, "we have " + rows.size() + " children");
        for (UIComponent c : rows) {
        	logger.logp(Level.FINER, className, methodName, "working on row : " + i);
            if (i % r == 0) {
                if (i % 2 == 1) {
                    writer.write("<tr class=\"altrow\">");
                } else {
                    writer.write("<tr>");
                }

                rno = renderRowHeader(writer, rno);
            }

            writer.write("<td>");
            c.encodeAll( facesContext );
            c.setRendered(true);
            writer.write("</td>");

            if (i % r == (r - 1)) {
                writer.write("</tr>");
            }

            i++;
        }
    }
	
	private int renderRowHeader(ResponseWriter writer, int rno) throws IOException {
		String methodName = "renderRowHeader";
		
		String outputString = "";
		if (rno == 0) {
			outputString = "Customer ID";
			
		} else if (rno == 1) {
			outputString = "Address";
			
		} else if (rno == 2) {
			outputString = "Email";
			
		} else if (rno == 3) {
			outputString = "Phone Number";
		}
		
		writer.write("<th>" + outputString + "</th>");
		logger.logp(Level.FINER, className, methodName, "write " + outputString + " when rno = " + rno);
        // here we are to increase
		rno++;
        
        return rno;
    }
	
	
	private void renderFooter(ResponseWriter writer, int r, CInfoUI cinfo) throws IOException {
        //int i = 0;
		String methodName = "renderFooter";
		
        writer.write("<tfoot><tr class=\"footer\">");
        String execute = cinfo.getClientId();
        String render = cinfo.getClientId();
        writer.write("<th colspan=\"" + (r + 1) + "\">" +
                "<a id=\"" + cinfo.getClientId() + "_update\""+ 
                " href=\"#\" onclick=\"jsf.ajax.request(this,event,{execute:'" +
                execute +
                "',render:'" +
                render +
                "'});return false;\">[Update]</a> " +
                "</th>");
        writer.write("</tr></tfoot>");
        
        logger.logp(Level.FINE, className, methodName, "done rendering footer" );
    } 
	
	@Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
