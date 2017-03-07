package com.tab28.jaawartou;

/**
 * @author xadimouSALIH
 * 
 */

public class RowItem {
	private int imageId;
	private String arab;
	private String fran;
	private String tran;

	public RowItem() {

	}

	public RowItem(int imageId, String arab, String fran, String tran) {
		super();
		this.imageId = imageId;
		this.arab = arab;
		this.fran = fran;
		this.tran = tran;
	}

	public RowItem(String arab, String fran, String tran) {
		this.arab = arab;
		this.fran = fran;
		this.tran = tran;
	}

	public RowItem(String arab, String fran) {
		this.arab = arab;
		this.fran = fran;
	}

	public RowItem(String arab) {
		this.arab = arab;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getArab() {
		return arab;
	}

	public void setArab(String arab) {
		this.arab = arab;
	}

	public String getFran() {
		return fran;
	}

	public void setFran(String fran) {
		this.fran = fran;
	}

	public String getTran() {
		return tran;
	}

	public void setTran(String tran) {
		this.tran = tran;
	}

	@Override
	public String toString() {
		return "RowItem [imageId=" + imageId + ", arab=" + arab + ", fran="
				+ fran + ", tran=" + tran + "]";
	}

}