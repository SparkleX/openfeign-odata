package com.next.openfeign.odata4.generator;

import java.beans.Introspector;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.next.openfeign.odata4.jaxb.EntitySet;
import com.next.openfeign.odata4.jaxb.EntityType;
import com.next.openfeign.odata4.jaxb.Property;
import com.next.openfeign.odata4.jaxb.PropertyRef;
import com.next.openfeign.odata4.jaxb.Edmx;


@Component
public class ODataUtil 
{
	@Autowired
	Edmx edmx;
	public String getNamespace()
	{
		return edmx.getDataServices().getSchema().getNamespace();
	}
	public String getNamespaceLastDot()
	{
		return getNamespace()+".";
	}
	public String convertODataType(String type) 
	{
		String nsWithDot = getNamespaceLastDot();
		if(type==null)
		{
			return "void";
		}
		if(type.startsWith("Collection("))
		{
			String[] a = type.split("[()]");
			String innerType = convertODataType(a[1]);
			return "List<" + innerType + ">";
		}
		if(type.startsWith(nsWithDot))
		{
			return type.substring(nsWithDot.length());
		}
		switch(type)
		{
		case "Edm.String": return "String";
		case "Edm.Int32": return "Integer";
		case "Edm.Int64": return "Long";		
		case "Edm.Double": return "Double";
		case "Edm.DateTime": return "LocalDate";
		case "Edm.Time":return "LocalTime";
		case "Edm.GeographyPoint": return "String";
		case "Edm.Guid": return "String";
		case "Edm.Single": return "Float";
		case "Edm.DateTimeOffset":  return "String";
		case "Edm.Duration":  return "String";
		
		}
		throw new RuntimeException(type + " is not supported");
	}
	public String getIdParams(EntitySet entitySet,Edmx edmx) 
	{
		String nsWithDot = getNamespaceLastDot();
		String type = entitySet.getEntityType().substring(nsWithDot.length());
		Optional<EntityType> entityTypeTemp = edmx.getDataServices().getSchema().getEntityType().stream().filter(a->a.getName().equals(type)).findFirst();
		EntityType entityType = entityTypeTemp.get();
		
		StringBuilder rt  =new StringBuilder();
		for(PropertyRef propRef:entityType.getKey().getPropertyRef())
		{
			String columnName = propRef.getName();
			Property prop = entityType.getProperty().stream().filter(a->a.getName().equals(columnName)).findFirst().get();
			String odataType = prop.getType();
			String javaType = convertODataType(odataType);
			rt.append(String.format("@Param(value=\"%s\",expander=ODataKeyExpander.class) %s %s,",columnName, javaType,columnName));
			
		}
		rt.setLength(rt.length()-1);
		return rt.toString();
	}
	public String getIdFormat(EntitySet entitySet,Edmx edmx) 
	{
		String nsWithDot = getNamespaceLastDot();
		String type = entitySet.getEntityType().substring(nsWithDot.length());
		Optional<EntityType> entityTypeTemp = edmx.getDataServices().getSchema().getEntityType().stream().filter(a->a.getName().equals(type)).findFirst();
		EntityType entityType = entityTypeTemp.get();
		
		StringBuilder rt  =new StringBuilder();
		for(PropertyRef propRef:entityType.getKey().getPropertyRef())
		{
			String columnName = propRef.getName();
			Property prop = entityType.getProperty().stream().filter(a->a.getName().equals(columnName)).findFirst().get();
			String odataType = prop.getType();
			
			switch(odataType)
			{
			case "Edm.String": 
				rt.append(String.format("%s='{%s}',",columnName,columnName));
				break;
			case "Edm.Int32": 
				rt.append(String.format("%s={%s},",columnName,columnName));
				break;
			default:
				throw new RuntimeException(odataType + " is not supported");
			}
			
		}
		rt.setLength(rt.length()-1);
		return rt.toString();
	}
	static String [] keywords = {"import","protected","break"};
	public boolean isKeyWords(String words)
	{
		for(String key:keywords)
		{
			if(key.equals(words))
			{
				return true;
			}
		}
		return false;
	}	
	public String convertKeyWords(String words)
	{
		if(isKeyWords(words))
		{
			return words+"Z";
		}
		return words;
	}
	public String decapitalize(String words)
	{
		String rt = Introspector.decapitalize(words);
		rt = convertKeyWords(rt);
		return rt;
	}
}
