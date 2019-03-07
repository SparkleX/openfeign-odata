package com.next.openfeign.odata4.client;
import feign.Param;


public class ODataKeyExpander implements Param.Expander
{
	@Override
	public String expand(Object value) 
	{
		String str = value.toString();
		str = str.replaceAll("'", "''");
		return str;
	}
}
