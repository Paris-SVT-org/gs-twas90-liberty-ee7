/**
 * 
 */
package pagecode;


//import com.ibm.faces.component.html.HtmlScriptCollector;

import javax.faces.component.html.HtmlOutputText;
//import com.ibm.faces.component.html.HtmlOutputLinkEx;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="pc_Logout")
@RequestScoped
/**
 * @author Administrator
 *
 */
public class Logout extends PageCodeBase {

//	protected HtmlScriptCollector scriptCollector1;
	protected HtmlOutputText text1;
	protected HtmlOutputText text2;
//	protected HtmlOutputLinkEx linkEx1;

//	protected HtmlScriptCollector getScriptCollector1() {
	//	if (scriptCollector1 == null) {
		//	scriptCollector1 = (HtmlScriptCollector) findComponentInRoot("scriptCollector1");
	//	}
	//	return scriptCollector1;
//	}

	protected HtmlOutputText getText1() {
		if (text1 == null) {
			text1 = (HtmlOutputText) findComponentInRoot("text1");
		}
		return text1;
	}

	protected HtmlOutputText getText2() {
		if (text2 == null) {
			text2 = (HtmlOutputText) findComponentInRoot("text2");
		}
		return text2;
	}

	//protected HtmlOutputLinkEx getLinkEx1() {
	//	if (linkEx1 == null) {
	//		linkEx1 = (HtmlOutputLinkEx) findComponentInRoot("linkEx1");
	//	}
	//	return linkEx1;
//	}

}