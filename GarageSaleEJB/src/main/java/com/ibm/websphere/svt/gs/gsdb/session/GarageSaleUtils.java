package com.ibm.websphere.svt.gs.gsdb.session;
/**
 * 
 */


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

/**
 * @author JAGRAJ
 *
 */
public class GarageSaleUtils {
	
	private static String componentName = "com.ibm.websphere.svt.gs.jpamanager.util";
	private static Logger logger = Logger.getLogger(componentName);
	private static String className = GarageSaleUtils.class.getName();
	private static final String CLIENTSIDE = "<====CCCCC ";
	
	private static DataSource gsDs = null;
	private static DataSource ccDs = null;
	private static DataSource wsbankDs = null;

	
	static {
		try {
			Context ctx = new InitialContext();
			gsDs = (DataSource) ctx.lookup("java:comp/env/jdbc/gsdb");
			ccDs = (DataSource) ctx.lookup("java:comp/env/jdbc/ccdb");
			wsbankDs = (DataSource) ctx.lookup("java:comp/env/jdbc/wsbankdb");
		} catch (Exception e) {
			logger.log(Level.SEVERE, CLIENTSIDE + e.getMessage());
			e.printStackTrace();
		}
	}

		public static DataSource getGsDs(){
			return gsDs;
		}
		
		public static DataSource getCcDs(){
			return ccDs;
		}
		
		public static DataSource getWsbankDs(){
			return wsbankDs;
		}

		/**
		 * 
		 * @param utx
		 * @return
		 * @throws Exception
		 */
		public static UserTransaction getUserTransaction(UserTransaction utx) throws Exception{
			if(utx==null){
				InitialContext ctx = new InitialContext();
				UserTransaction ut = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
				
				return ut;
			}
			else {
				return utx;
			}
		}
}
	


