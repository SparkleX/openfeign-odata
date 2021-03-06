package ${edmx.dataServices.schema.namespace?lower_case}.enums;

import java.util.HashMap;
import java.util.Map;

import com.next.openfeign.odata4.client.EnumUtil;

public enum ${data.name}
{
    <#list data.member as member>
    ${member.name}("${member.value}")${member?has_next?string(',',';')}    
    </#list>
    
	${data.name}(String val)
	{
		this.value = val;
	}
	
	public String getValue() 
	{
		return value;
	}
	
	private String value;
	
	private static final Map<String, ${data.name}> lookup = new HashMap<>();
	
	public static ${data.name} get(String val)
	{ 
		return (${data.name}) EnumUtil.get(lookup, val);
	}
	
	static
	{
		for(${data.name} o : values())
		{ 
			lookup.put(o.getValue(), o);
		}
	}
	
}
