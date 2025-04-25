/**
 * 
 */
package com.ibm.websphere.svt.gs.gsjsfweb;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;


/**
 * @author root
 *
 */
public class CustomScopeELResolver extends ELResolver {

    private static final String SCOPE_NAME = "customScope";
	
	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getCommonPropertyType(javax.el.ELContext, java.lang.Object)
	 */
	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getFeatureDescriptors(javax.el.ELContext, java.lang.Object)
	 */
	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {
        return Collections.<FeatureDescriptor>emptyList().iterator();
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getType(javax.el.ELContext, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Class<?> getType(ELContext context, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
        return Object.class;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#getValue(javax.el.ELContext, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object getValue(ELContext elContext, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
        if (property == null) {
            throw new PropertyNotFoundException();
        }
        if (base == null && SCOPE_NAME.equals(property.toString())) {
            // explicit scope lookup request
            CustomScope customScope = getScope(elContext);
            elContext.setPropertyResolved(true);
            return customScope;
        } else if (base != null && base instanceof CustomScope) {
            // We're dealing with the custom scope that has been explicity referenced
            // by an expression.  'property' will be the name of some entity
            // within the scope.
            return lookup(elContext, (CustomScope) base, property.toString());
        } else if (base == null) {
            // bean may have already been created and is in scope.
            // check to see if the bean is present
            return lookup(elContext, getScope(elContext), property.toString());
        }
        return null;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#isReadOnly(javax.el.ELContext, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property)
			throws NullPointerException, PropertyNotFoundException, ELException {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.el.ELResolver#setValue(javax.el.ELContext, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setValue(ELContext context, Object base, Object property,
			Object value) throws NullPointerException,
			PropertyNotFoundException, PropertyNotWritableException,
			ELException {
		// Do nothing.

	}
	
	
    public static void destroyScope(FacesContext ctx) {

        Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
        CustomScope customScope = (CustomScope) sessionMap.remove(SCOPE_NAME);
        customScope.notifyDestroy();

    }


    // --------------------------------------------------------- Private Methods


    private CustomScope getScope(ELContext elContext) {

        FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
        Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
        CustomScope customScope = (CustomScope) sessionMap.get(SCOPE_NAME);
        if (customScope == null) {
            customScope = new CustomScope();
            sessionMap.put(SCOPE_NAME, customScope);
            customScope.notifyCreate();
        }
        return customScope;

    }

    
    private Object lookup(ELContext elContext,
                          CustomScope scope,
                          String key) {

        Object value = scope.get(key);
        elContext.setPropertyResolved(value != null);
        return value;

    }


    // ---------------------------------------------------------- Nested Classes

    private static final class CustomScope extends ConcurrentHashMap<String,Object> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2074269488133222772L;
		/**
		 * 
		 */

        // -------------------------------------------------------- Constructors


        private CustomScope() {
        }


        // ------------------------------------------------------ Public Methods


        /**
         * Publishes <code>PostConstructCustomScopeEvent</code> to notify
         * interested parties that this scope is now available.
         */
        public void notifyCreate() {
        	
            ScopeContext context = new ScopeContext(SCOPE_NAME, this);
            
            ELContext elContext=FacesContext.getCurrentInstance().getELContext();
            FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
            Application application=ctx.getApplication();
            application.publishEvent(ctx,PostConstructCustomScopeEvent.class, context);

        }


        /**
         * Publishes <code>PreDestroyCustomScopeEvent</code> to notify
         * interested parties that this scope is being destroyed.
         */
        public void notifyDestroy() {

            // notify interested parties that this scope is being
            // destroyed
            ScopeContext scopeContext = new ScopeContext(SCOPE_NAME,
                                                         this);
            ELContext elContext=FacesContext.getCurrentInstance().getELContext();
            FacesContext ctx = (FacesContext) elContext.getContext(FacesContext.class);
            Application application=ctx.getApplication();
            application.publishEvent(ctx,PreDestroyCustomScopeEvent.class,
                                     scopeContext);

        }

    }
	

}
