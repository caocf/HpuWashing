package com.edaixi.modle;

/**
 * 获取返回数据集的接口数据bean
 */
public class HttpCommonBean {

	private boolean ret;
	private long ts;
	private String version;
	private String data;
	private String error;

	public HttpCommonBean() {
		ret = false;
		error = "";
	}

	public boolean isRet() {
		return ret;
	}

	public void setRet(boolean ret) {
		this.ret = ret;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
