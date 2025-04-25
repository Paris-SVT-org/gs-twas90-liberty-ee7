/**
 * 
 */
package pagecode;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="pc_ShowItem")
@RequestScoped

/**
 * @author Administrator
 *
 */
public class ShowItem extends PageCodeBase {

	protected HtmlOutputText description1;
	protected HtmlOutputText unitPrice1;

	protected HtmlOutputText getDescription1() {
		if (description1 == null) {
			description1 = (HtmlOutputText) findComponentInRoot("description1");
		}
		return description1;
	}

	protected HtmlOutputText getUnitPrice1() {
		if (unitPrice1 == null) {
			unitPrice1 = (HtmlOutputText) findComponentInRoot("unitPrice1");
		}
		return unitPrice1;
	}

}