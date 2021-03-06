package ${edmx.dataServices.schema.namespace?lower_case}.complextype;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.*;
import ${edmx.dataServices.schema.namespace?lower_case}.enums.*;
import com.next.openfeign.odata4.client.*;
import java.time.LocalTime;
import java.time.LocalDate;


@SuppressWarnings("unused")
public class ${data.name} extends UserFieldsSupport
{
    <#list data.property as prop>
    
    <#assign x = "">
    <#assign writeable = false>
   	<#if prop.isWritable()??>
   		<#if prop.isWritable()>
   			<#assign writeable = true>
	   	<#else>
	   		<#assign x = ", access = Access.WRITE_ONLY">
   		</#if>
   	<#else>
   	</#if>
   	<#assign type=util.convertODataType(prop.type)>
	<#assign lowerName=util.decapitalize(prop.name)>
	<#assign upperName=prop.name?cap_first>
   	@JsonProperty(value="${prop.name}"${x})   	
	protected ${type} ${lowerName};
	/**
 	 * Getter<p>
	<@propComments title="Getter" prop=prop writeable=writeable/>
 	 * @return field "${prop.name}"
 	 */
	public ${type} get${upperName}() {return ${lowerName};}
	/**
 	 * Setter<p>
	<@propComments title="Getter" prop=prop writeable=writeable/>	
 	 * @param val the value to set
 	 */
	public void set${upperName}(${type} val) {${lowerName}=val;}
    
    </#list>
}
<#macro propComments title prop writeable>
 	 * ${title}<p>
 	 * <strong>OData Infromation</strong><p>
 	 * Name: "${prop.name}"<br>
 	 * Type: ${prop.type}<br>
 	 * Writable: ${writeable?c}<br>
 	 * Nullable: ${prop.nullable?c}<p> 	 
</#macro>