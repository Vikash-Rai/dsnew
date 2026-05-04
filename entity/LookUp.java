package com.equabli.collectprism.entity;

import java.util.List;

import jakarta.persistence.*;

import com.equabli.domain.helpers.CommonUtils;

@Entity
@Table(schema = "conf", name = "lookup")
public class LookUp {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "lookup_id")
	private Integer lookupId;

	@Column(name = "lookup_group_id")
	private Integer lookupGroupId;

	@Column(name = "keycode")
	private String keycode;

	@Column(name = "keyvalue")
	private String keyvalue;

	@Column(name = "group_sequence")
	private Integer groupSequence;

	@Column(name = "scrubtype")
	private Integer scrubType;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Transient
	private String lookupKey;

	public LookUp(Integer lookupId) {
		this.lookupId = lookupId;
	}

	public Integer getLookupId() {
		return lookupId;
	}

	public void setLookupId(Integer lookupId) {
		this.lookupId = lookupId;
	}

	public Integer getLookupGroupId() {
		return lookupGroupId;
	}

	public void setLookupGroupId(Integer lookupGroupId) {
		this.lookupGroupId = lookupGroupId;
	}

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	public String getKeyvalue() {
		return keyvalue;
	}

	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}

	public Integer getGroupSequence() {
		return groupSequence;
	}

	public void setGroupSequence(Integer groupSequence) {
		this.groupSequence = groupSequence;
	}

	public Integer getScrubType() {
		return scrubType;
	}

	public void setScrubType(Integer scrubType) {
		this.scrubType = scrubType;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public String getLookupKey() {
		return lookupKey;
	}

	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}

    public LookUp() {
    	
    }

    public LookUp(String lookupKey, Integer lookupId, String keycode, String keyvalue) {
        this.lookupKey = lookupKey;
        this.lookupId = lookupId;
        this.keycode = keycode;
        this.keyvalue = keyvalue;
    }

    public LookUp(Integer lookupId, String keycode, String keyvalue) {
        this.lookupId = lookupId;
        this.keycode = keycode;
        this.keyvalue = keyvalue;
    }

	public static String replaceCharsByKeyCode(List<LookUp> lookUp, String name) {
		for(LookUp lu:lookUp) {
			name = name.replaceAll("["+lu.getKeycode()+"]", " ");
		}
		return name;
	}

	public static String replaceCharsByKeyValue(List<LookUp> lookUp, String nameValue) {
		StringBuffer name = new StringBuffer();
		if(!CommonUtils.isStringNullOrBlank(nameValue)) {
			String[] nameVal = nameValue.split(" ");
			for(String val: nameVal) {
				Boolean contains = false;
				for(LookUp lu:lookUp) {
					if(!CommonUtils.isStringNullOrBlank(val) && !CommonUtils.isStringNullOrBlank(lu.getKeyvalue()) 
							&& val.equalsIgnoreCase(lu.getKeyvalue()))
						contains = true;
				}
				if(!contains)
					name.append(val).append(" ");
			}
		}
		return name.toString();
	}
	
	
}