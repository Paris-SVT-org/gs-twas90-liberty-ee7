/**
 * 
 */
package pagecode.resources.components;

import pagecode.PageCodeBase;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlForm;

/**
 * @author JAGRAJ
 *
 */
public class OnSaleContent extends PageCodeBase {

	protected HtmlOutputText text2;
	protected HtmlForm playProductDemoVideoForm;
	protected HtmlOutputText fileName;
	protected HtmlOutputText getText2() {
		if (text2 == null) {
			text2 = (HtmlOutputText) findComponentInRoot("text2");
		}
		return text2;
	}

	protected HtmlForm getPlayProductDemoVideoForm() {
		if (playProductDemoVideoForm == null) {
			playProductDemoVideoForm = (HtmlForm) findComponentInRoot("playProductDemoVideoForm");
		}
		return playProductDemoVideoForm;
	}

	protected HtmlOutputText getFileName() {
		if (fileName == null) {
			fileName = (HtmlOutputText) findComponentInRoot("fileName");
		}
		return fileName;
	}

}