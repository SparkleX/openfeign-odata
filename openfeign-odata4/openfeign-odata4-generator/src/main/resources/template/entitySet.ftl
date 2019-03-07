package ${edmx.dataServices.schema.namespace?lower_case}.entityset;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import ${edmx.dataServices.schema.namespace?lower_case}.entitytype.*;
import feign.Param;
import feign.RequestLine;
import feign.QueryMap;
import feign.Headers;
import com.next.openfeign.odata4.client.*;


@SuppressWarnings("unused")
@Headers("Content-Type: application/json; charset=utf-8")
public interface ${data.name}
{
	<#assign type=util.convertODataType(data.entityType)>
	<#assign idParams=util.getIdParams(data, edmx)>
	<#assign idFormat=util.getIdFormat(data, edmx)>

	@RequestLine("GET /${data.name}")
	ODataList<${type}> findAll();

	@RequestLine("GET /${data.name}?$filter={filter}")
	ODataList<${type}> find(@Param("filter") String filter);

	@RequestLine("GET /${data.name}")
	ODataList<${type}> find(@QueryMap Map<String, Object> params);


	@RequestLine("GET /${data.name}(${idFormat})")
	${type} get(${idParams});
	
	@RequestLine("POST /${data.name}")
	${type} create(${type} o);
	
	@RequestLine("PATCH /${data.name}(${idFormat})")
	void update(${idParams},${type} o);
	
	@RequestLine("DELETE /${data.name}(${idFormat})")
	void delete(${idParams});
	
	<#list function as function>
	@RequestLine("POST /${data.name}(${idFormat})/${function.name}")
	void ${util.decapitalize(function.name)}(${idParams});
    </#list>
}

