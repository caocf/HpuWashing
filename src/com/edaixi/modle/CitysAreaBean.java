package com.edaixi.modle;

import java.io.Serializable;
import java.util.List;

public class CitysAreaBean implements Serializable {
	private static final long serialVersionUID = 1897498977623841069L;

	public List<String> cities;
	public List<List<String>> areas;

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

	public List<List<String>> getAreas() {
		return areas;
	}

	public void setAreas(List<List<String>> areas) {
		this.areas = areas;
	}

	@Override
	public String toString() {
		return "CitysAreaBean [cities=" + cities + ", areas=" + areas + "]";
	}
}
