package example;

import com.next.openfeign.odata4.client.OData4Client;

import microsoft.odata.service.sample.trippininmemory.models.entityset.Airlines;

public class Example 
{
	public static void main(String []argc)
	{
		OData4Client client = OData4Client.defaultClient("https://services.odata.org/TripPinRESTierService/(S(hgtwtnfytvbevwv4i1ix340c))/");
		Airlines oAirLines = client.target(Airlines.class);
		oAirLines.findAll();
	}
}
