package com.next.openfeign.odata4.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ODataList<T> 
{


	
	@JsonProperty(value="value")   	
	protected List<T> value; 
	@JsonProperty(value="odata.metadata")
	protected String odataMetadata;
	@JsonProperty(value="@odata.context")
	protected String odataContext;	
	
	public String getOdataContext() {
		return odataContext;
	}

	public void setOdataContext(String odataContext) {
		this.odataContext = odataContext;
	}


	
	public String getOdataMetadata() {
		return odataMetadata;
	}

	public void setOdataMetadata(String odataMetadata) {
		this.odataMetadata = odataMetadata;
	}
	public void setValue(List<T> value) {
		this.value = value;
	}

	public List<T> getValue() 
	{
		return value;
	}	
}
