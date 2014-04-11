package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Location;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.fql.Page.Cover;
import com.abewy.android.apps.klyph.core.fql.Page.Hours;
import com.abewy.android.apps.klyph.core.fql.Page.Parking;
import com.abewy.android.apps.klyph.core.fql.Page.PaymentOptions;
import com.abewy.android.apps.klyph.core.fql.Page.RestaurantServices;
import com.abewy.android.apps.klyph.core.fql.Page.RestaurantSpecialties;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PageDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Page page = new Page();
		
		deserializePrimitives(page, data);
		page.setCategories(deserializeStringList(getJsonArray(data, "categories")));
		page.setFood_styles(deserializeStringList(getJsonArray(data, "food_styles")));
		page.setPic_cover((Cover) new CoverDeserializer().deserializeObject(getJsonObject(data, "pic_cover")));
		
		page.setLocation((Location) new LocationDeserializer().deserializeObject(getJsonObject(data, "location")));
		page.setHours((Hours) new HoursDeserializer().deserializeObject(getJsonObject(data, "hours")));
		page.setParking((Parking) new ParkingDeserializer().deserializeObject(getJsonObject(data, "parking")));
		page.setPayment_options((PaymentOptions) new PaymentOptionsDeserializer().deserializeObject(getJsonObject(data, "payment_options")));
		page.setRestaurant_services((RestaurantServices) new RestaurantServicesDeserializer().deserializeObject(getJsonObject(data, "restaurant_services")));
		page.setRestaurant_specialties((RestaurantSpecialties) new RestaurantSpecialtiesDeserializer().deserializeObject(getJsonObject(data, "restaurant_specialties")));
		
		return page;
	}
	
	private static class CoverDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Cover cover = new Cover();
			
			deserializePrimitives(cover, data);
			
			return cover;
		}
	}
	
	private static class HoursDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Hours hours = new Hours();
			
			deserializePrimitives(hours, data);
			
			return hours;
		}
	}
	
	private static class ParkingDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Parking parking = new Parking();
			
			deserializePrimitives(parking, data);
			
			return parking;
		}
	}
	
	private static class PaymentOptionsDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			PaymentOptions paymentOptions = new PaymentOptions();
			
			deserializePrimitives(paymentOptions, data);
			
			return paymentOptions;
		}
	}
	
	private static class RestaurantServicesDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			RestaurantServices restaurantServices = new RestaurantServices();
			
			deserializePrimitives(restaurantServices, data);
			
			return restaurantServices;
		}
	}
	
	private static class RestaurantSpecialtiesDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			RestaurantSpecialties restaurantSpecialties = new RestaurantSpecialties();
			
			deserializePrimitives(restaurantSpecialties, data);
			
			return restaurantSpecialties;
		}
	}
}
