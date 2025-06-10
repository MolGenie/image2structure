package com.molgenie.i2s.models.common;

public class Markush {
	
	String smiles 		= null;
	float confidence 	= 0;
	int resolution 		= 0;
	String image 		= null;
	int page 			= 0;
	String position 	= null;
	String idCode 		= null;
	
	public String getSmiles() {
		return smiles;
	}
	public void setSmiles(String _smiles) {
		this.smiles = _smiles; 
	}
	
	public float getConfidence() {
		return confidence;
	}
	public void setConfidence(float _conf) {
		this.confidence = _conf;
	}
	
	public Integer getResolution() {
		return resolution;
	}
	public void setResolution(int _resolution) {
		this.resolution = _resolution;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String _image) {
		this.image = _image;
	}
	
	public Integer getPage() {
		return page;
	}
	public void setPage(int _page) {
		this.page = _page;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String _position) {
		this.position = _position;
	}
	
	public String getIDCode() {
		return idCode;
	}
	public void setIDCode(String _idcode) {
		this.idCode = _idcode;
	}

}
