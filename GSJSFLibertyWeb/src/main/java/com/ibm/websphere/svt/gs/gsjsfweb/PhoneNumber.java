package com.ibm.websphere.svt.gs.gsjsfweb;

public class PhoneNumber implements java.io.Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 434814853263987010L;
	private String _areaCode;
	private String _middleCode;
	private String _endCode;

	public PhoneNumber() {
	}

	public PhoneNumber(String areaCode, String middleCode, String endCode) {
		this._areaCode = areaCode;
		this._middleCode = middleCode;
		this._endCode = endCode;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		//sb.append("Phone: ");
		sb.append(_areaCode).append("-");
		sb.append(_middleCode).append("-");
		sb.append(_endCode);
		return sb.toString();
	}

	public String getAreaCode() {
		return _areaCode;
	}

	public void setAreaCode(String areaCode) {
		this._areaCode = areaCode;
	}

	public String getMiddleCode() {
		return _middleCode;
	}

	public void setMiddleCode(String middleCode) {
		this._middleCode = middleCode;
	}

	public String getEndCode() {
		return _endCode;
	}

	public void setEndCode(String endCode) {
		this._endCode = endCode;
	}
}

