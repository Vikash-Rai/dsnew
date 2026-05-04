package com.equabli.collectprism.entity;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ErrWarJson implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7355976388753677669L;

	private String key;
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public ErrWarJson() {
		
	}
	
	public ErrWarJson(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ErrWarJson other = (ErrWarJson) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
