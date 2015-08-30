package com.edaixi.modle;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BannerlistBean implements Serializable {
	private String url;
	private String url_type;
	private String title;
	private String image_url;
	private String inner_url;
	private String inner_type;
	private String inner_title;
	private int MagicIndex;
	private String MagicIndexKey;

	public int getMagicIndex() {
		return MagicIndex;
	}

	public void setMagicIndex(int magicIndex) {
		MagicIndex = magicIndex;
	}

	public String getMagicIndexKey() {
		return MagicIndexKey;
	}

	public void setMagicIndexKey(String magicIndexKey) {
		MagicIndexKey = magicIndexKey;
	}

	public String getInner_url() {
		return inner_url;
	}

	public void setInner_url(String inner_url) {
		this.inner_url = inner_url;
	}

	public String getInner_type() {
		return inner_type;
	}

	public void setInner_type(String inner_type) {
		this.inner_type = inner_type;
	}

	public String getInner_title() {
		return inner_title;
	}

	public void setInner_title(String inner_title) {
		this.inner_title = inner_title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl_type() {
		return url_type;
	}

	public void setUrl_type(String url_type) {
		this.url_type = url_type;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	@Override
	public String toString() {
		return "BannerlistBean [url=" + url + ", url_type=" + url_type
				+ ", title=" + title + ", image_url=" + image_url
				+ ", inner_url=" + inner_url + ", inner_type=" + inner_type
				+ ", inner_title=" + inner_title + ", MagicIndex=" + MagicIndex
				+ ", MagicIndexKey=" + MagicIndexKey + "]";
	}

}
