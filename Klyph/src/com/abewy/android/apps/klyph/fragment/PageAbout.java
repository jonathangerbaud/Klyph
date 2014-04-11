package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.fql.Page.Hours;
import com.abewy.android.apps.klyph.core.fql.Page.Parking;
import com.abewy.android.apps.klyph.core.fql.Page.PaymentOptions;
import com.abewy.android.apps.klyph.core.fql.Page.RestaurantServices;
import com.abewy.android.apps.klyph.core.fql.Page.RestaurantSpecialties;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.klyph.items.Item;
import com.abewy.klyph.items.Title;
import com.abewy.klyph.items.TitleTextItem;

public class PageAbout extends KlyphFakeHeaderGridFragment
{
	public PageAbout()
	{
		setRequestType(Query.PAGE);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		defineEmptyText(R.string.empty_list_no_data);

		setRequestType(Query.PAGE);

		setListVisible(false);

		super.onViewCreated(view, savedInstanceState);

		setListAdapter(new MultiObjectAdapter(getListView()));
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		if (data.size() > 0)
		{
			Page page = (Page) data.get(0);
			data.remove(0);

			// About
			addTitleTextItem(data, page.getAbout(), R.string.page_about);

			// Company Description
			addTitleTextItem(data, page.getCompany_overview(), R.string.company_overview);

			// Description
			addTitleTextItem(data, page.getDescription(), R.string.description);

			// Mission
			addTitleTextItem(data, page.getDescription(), R.string.mission);

			// G�n�ral infos
			addTitleTextItem(data, page.getGeneral_info(), R.string.general_infos);

			// Plot outline
			addTitleTextItem(data, page.getPlot_outline(), R.string.plot_outline);

			// Basic Infos
			SparseArray<String> basicInfo = new SparseArray<String>();

			// Band
			basicInfo.put(R.string.founded, page.getFounded());
			basicInfo.put(R.string.genre, page.getGenre());
			basicInfo.put(R.string.band_members, page.getMembers());
			basicInfo.put(R.string.band_hometown, page.getHometown());
			basicInfo.put(R.string.band_record_label, page.getRecord_label());
			basicInfo.put(R.string.band_current_location, page.getCurrent_location());
			basicInfo.put(R.string.band_press_contact, page.getPress_contact());
			basicInfo.put(R.string.band_booking_agent, page.getBooking_agent());
			basicInfo.put(R.string.band_general_manager, page.getGeneral_manager());

			// Company
			basicInfo.put(R.string.company_products, page.getProducts());

			// Restaurant
			String foodStyles = "";
			int n = page.getFood_styles().size();
			for (int i = 0; i < n; i++)
			{
				foodStyles += page.getFood_styles().get(i);

				if (i < n - 1)
					foodStyles += "\n";
			}
			basicInfo.put(R.string.restaurant_food_styles, foodStyles);
			basicInfo.put(R.string.restaurant_culinar_team, page.getCulinary_team());
			basicInfo.put(R.string.restaurant_prices, page.getPrice_range());
			basicInfo.put(R.string.restaurant_attire, page.getAttire());
			basicInfo.put(R.string.hours, hoursHelper(page.getHours()));
			basicInfo.put(R.string.restaurant_services, restaurantServicesHelper(page.getRestaurant_services()));
			basicInfo.put(R.string.restaurant_specialties, restaurantSpecialtiesHelper(page.getRestaurant_specialties()));
			basicInfo.put(R.string.restaurant_parking, parkingHelper(page.getParking()));
			basicInfo.put(R.string.public_transit, page.getPublic_transit());

			// Tv Show
			basicInfo.put(R.string.network, page.getNetwork());
			basicInfo.put(R.string.season, page.getSeason());
			basicInfo.put(R.string.schedule, page.getSchedule());
			basicInfo.put(R.string.starring, page.getStarring());
			basicInfo.put(R.string.written_by, page.getWritten_by());

			// Others
			basicInfo.put(R.string.awards, page.getAwards());
			basicInfo.put(R.string.personal_info, page.getPersonal_info());
			basicInfo.put(R.string.personal_interests, page.getPersonal_interests());
			basicInfo.put(R.string.affiliation, page.getAffiliation());
			basicInfo.put(R.string.built, page.getBuilt());
			basicInfo.put(R.string.features, page.getFeatures());
			basicInfo.put(R.string.mpg, page.getMpg());
			basicInfo.put(R.string.pharma_safety_info, page.getPharma_safety_info());

			// Film
			basicInfo.put(R.string.release_date, page.getRelease_date());
			basicInfo.put(R.string.studio, page.getStudio());
			basicInfo.put(R.string.screenplay_by, page.getScreenplay_by());
			basicInfo.put(R.string.directed_by, page.getDirected_by());
			addItemsForMap(R.string.page_base_infos, basicInfo, data);

			// ___ Band ___

			// Artist we like
			addTitleTextItem(data, page.getArtists_we_like(), R.string.artists_we_like);

			// Interests
			addTitleTextItem(data, page.getBand_interests(), R.string.band_interests);

			// Influences
			addTitleTextItem(data, page.getInfluences(), R.string.band_influences);

			// Coondinates
			SparseArray<String> coordinates = new SparseArray<String>();
			coordinates.put(R.string.phone, page.getPhone());
			coordinates.put(R.string.user_address, page.getLocation().getName());
			coordinates.put(R.string.user_website, page.getWebsite());
			addItemsForMap(R.string.coordinates, coordinates, data);
		}

		super.populate(data);
		setNoMoreData(true);
	}

	private void addTitleTextItem(List<GraphObject> list, String value, int resTitle)
	{
		addTitleTextItem(list, value, resTitle, true);
	}

	private void addTitleTextItem(List<GraphObject> list, String value, int resTitle, boolean shadow)
	{
		if (isNotEmpty(value))
		{
			TitleTextItem item = new TitleTextItem();
			item.setTitle(getResources().getString(resTitle));
			item.setText(value);
			item.setShadow(true);
			list.add(item);
		}
	}

	private boolean isNotEmpty(String string)
	{
		return string != null && string.length() > 0;
	}

	private boolean addItemsForMap(int resTitle, SparseArray<String> map, List<GraphObject> data)
	{
		int originalSize = data.size();

		int n = map.size();
		for (int i = 0; i < n; i++)
		{
			int key = map.keyAt(i);
			String value = map.get(key);

			if (isNotEmpty(value))
			{
				Item item = new Item();
				item.setName(getResources().getString(key));
				item.setDesc(value);
				data.add(item);
			}
		}

		int finalSize = data.size();

		if (finalSize > originalSize)
		{
			Item item = (Item) data.get(finalSize - 1);
			item.setShadow(true);

			Title titleItem = new Title();
			titleItem.setName(getResources().getString(resTitle));
			data.add(originalSize, titleItem);
		}

		return finalSize > originalSize;
	}

	/*@Override
	protected int getCustomLayout()
	{
		return R.layout.grid_simple;
	}*/

	@Override
	protected boolean updateNumColumnOnOrientationChange()
	{
		return false;
	}

	@Override
	protected int getNumColumn()
	{
		return 1;
	}

	// ___ Sub Page category string helpers

	private String getString(String day, String open1, String close1, String open2, String close2)
	{
		String s = "";

		if (open1.length() > 0 || open2.length() > 0)
		{
			s += day + ": ";

			if (open1.length() > 0)
				s += open1 + " - " + close1;

			if (open2.length() > 0)
			{
				if (open1.length() > 0)
					s += " / ";
				s += open2 + " - " + close2;
			}
		}
		return s;
	}

	private String hoursHelper(Hours hours)
	{
		String s = "";

		Resources res = getResources();

		if (isNotEmpty(hours.getMon_1_open()) || isNotEmpty(hours.getMon_2_open()))
		{
			s += getString(res.getString(R.string.monday), hours.getMon_1_open(), hours.getMon_1_close(), hours.getMon_2_open(),
					hours.getMon_2_close())
					+ "\n";
		}

		if (isNotEmpty(hours.getTue_1_open()) || isNotEmpty(hours.getTue_2_open()))
		{
			s += getString(res.getString(R.string.tuesday), hours.getTue_1_open(), hours.getTue_1_close(), hours.getTue_2_open(),
					hours.getTue_2_close())
					+ "\n";
		}

		if (isNotEmpty(hours.getWed_1_open()) || isNotEmpty(hours.getWed_2_open()))
		{
			s += getString(res.getString(R.string.wednesday), hours.getWed_1_open(), hours.getWed_1_close(), hours.getWed_2_open(),
					hours.getWed_2_close())
					+ "\n";
		}

		if (isNotEmpty(hours.getThu_1_open()) || isNotEmpty(hours.getThu_2_open()))
		{
			s += getString(res.getString(R.string.thursday), hours.getThu_1_open(), hours.getThu_1_close(), hours.getThu_2_open(),
					hours.getThu_2_close())
					+ "\n";
		}

		if (isNotEmpty(hours.getFri_1_open()) || isNotEmpty(hours.getFri_2_open()))
		{
			s += getString(res.getString(R.string.friday), hours.getFri_1_open(), hours.getFri_1_close(), hours.getFri_2_open(),
					hours.getFri_2_close())
					+ "\n";
		}

		if (isNotEmpty(hours.getSat_1_open()) || isNotEmpty(hours.getSat_2_open()))
		{
			s += getString(res.getString(R.string.saturday), hours.getSat_1_open(), hours.getSat_1_close(), hours.getSat_2_open(),
					hours.getSat_2_close())
					+ "\n";
		}

		if (isNotEmpty(hours.getSun_1_open()) || isNotEmpty(hours.getSun_2_open()))
		{
			s += getString(res.getString(R.string.sunday), hours.getSun_1_open(), hours.getSun_1_close(), hours.getSun_2_open(),
					hours.getSun_2_close());
		}

		if (s.length() > 0 && s.substring(s.length() - 1).equals("\n"))
			s = s.substring(0, s.length() - 2);

		return s;
	}

	private String parkingHelper(Parking parking)
	{
		String s = "";

		Resources res = getResources();

		if (parking.getStreet() > 0)
			s += res.getString(R.string.street) + " / ";

		if (parking.getLot() > 0)
			s += res.getString(R.string.lot) + " / ";

		if (parking.getValet() > 0)
			s += res.getString(R.string.valet) + " / ";

		if (s.length() > 0 && s.substring(s.length() - 2).equals("/ "))
			s = s.substring(0, s.length() - 2);

		return s;
	}

	private String paymentOptionsHelper(PaymentOptions options)
	{
		String s = "";

		Resources res = getResources();

		if (options.getVisa() > 0)
			s += res.getString(R.string.visa) + "\n";

		if (options.getMastercard() > 0)
			s += res.getString(R.string.mastercard) + "\n";

		if (options.getAmex() > 0)
			s += res.getString(R.string.amex) + "\n";

		if (options.getCash_only() > 0)
			s += res.getString(R.string.cash_only) + "\n";

		if (options.getDiscover() > 0)
			s += res.getString(R.string.discover) + "\n";

		if (s.length() > 0 && s.substring(s.length() - 1).equals("\n"))
			s = s.substring(0, s.length() - 1);

		return s;
	}

	private String restaurantServicesHelper(RestaurantServices services)
	{
		String s = "";

		Resources res = getResources();

		if (services.getReserve() > 0)
			s += res.getString(R.string.reserve) + "\n";

		if (services.getWalkins() > 0)
			s += res.getString(R.string.walkins) + "\n";

		if (services.getGroups() > 0)
			s += res.getString(R.string.groups) + "\n";

		if (services.getKids() > 0)
			s += res.getString(R.string.kids) + "\n";

		if (services.getTakeout() > 0)
			s += res.getString(R.string.takeout) + "\n";

		if (services.getDelivery() > 0)
			s += res.getString(R.string.delivery) + "\n";

		if (services.getWaiter() > 0)
			s += res.getString(R.string.waiter) + "\n";

		if (services.getCatering() > 0)
			s += res.getString(R.string.catering) + "\n";

		if (services.getOutdoor() > 0)
			s += res.getString(R.string.outdoor) + "\n";

		if (s.length() > 0 && s.substring(s.length() - 1).equals("\n"))
			s = s.substring(0, s.length() - 1);

		return s;
	}

	private String restaurantSpecialtiesHelper(RestaurantSpecialties specialties)
	{
		String s = "";

		Resources res = getResources();

		if (specialties.getBreakfast() > 0)
			s += res.getString(R.string.breakfast) + "\n";

		if (specialties.getLunch() > 0)
			s += res.getString(R.string.lunch) + "\n";

		if (specialties.getDinner() > 0)
			s += res.getString(R.string.dinner) + "\n";

		if (specialties.getCoffee() > 0)
			s += res.getString(R.string.coffee) + "\n";

		if (specialties.getDrinks() > 0)
			s += res.getString(R.string.drinks) + "\n";

		if (s.length() > 0 && s.substring(s.length() - 1).equals("\n"))
			s = s.substring(0, s.length() - 1);

		return s;
	}
}
