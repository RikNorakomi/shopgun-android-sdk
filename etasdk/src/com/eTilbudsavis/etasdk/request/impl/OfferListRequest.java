package com.eTilbudsavis.etasdk.request.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import com.eTilbudsavis.etasdk.EtaObjects.Dealer;
import com.eTilbudsavis.etasdk.EtaObjects.Offer;
import com.eTilbudsavis.etasdk.EtaObjects.Store;
import com.eTilbudsavis.etasdk.Network.EtaError;
import com.eTilbudsavis.etasdk.Network.Request;
import com.eTilbudsavis.etasdk.Network.Response.Listener;
import com.eTilbudsavis.etasdk.Network.Impl.JsonArrayRequest;
import com.eTilbudsavis.etasdk.Utils.Api;
import com.eTilbudsavis.etasdk.Utils.Api.Endpoint;
import com.eTilbudsavis.etasdk.Utils.Api.Param;
import com.eTilbudsavis.etasdk.request.RequestAutoFill;

public class OfferListRequest extends ListRequest<List<Offer>> {
	
	private OfferListRequest(Listener<List<Offer>> l) {
		super(Endpoint.OFFER_LIST, l);
	}
	
	@Override
	protected void deliverResponse(JSONArray response, EtaError error) {
		List<Offer> offers = null;
		if (response != null) {
			offers = Offer.fromJSON(response);
		}
		runAutoFill(offers, error);
	}
	
	public static class Builder extends ListRequest.Builder<List<Offer>>{
		
		public Builder(Listener<List<Offer>> l) {
			super(new OfferListRequest(l));
		}
		
		public void setFilter(Filter filter) {
			super.setFilter(filter);
		}
		
		public void setOrder(Order order) {
			super.setOrder(order);
		}
		
		public void setParameters(Parameter params) {
			super.setParameters(params);
		}
		
		public void setAutoFill(OfferAutoFill filler) {
			super.setAutoFiller(filler);
		}
		
		@Override
		public ListRequest<List<Offer>> build() {
			
			if (getFilter() == null) {
				setFilter(new Filter());
			}
			
			if (getOrder() == null) {
				setOrder(new Order());
			}
			
			if (getParameters() == null) {
				setParameters(new Parameter());
			}
			
			if (getAutofill() == null) {
				setAutoFiller(new OfferAutoFill());
			}
			
			return super.build();
		}
		
	}
	
	public static class Filter extends ListRequest.Filter {

		public void addOfferFilter(Set<String> offerIds) {
			add(Api.Param.OFFER_IDS, offerIds);
		}

		public void addCatalogFilter(Set<String> catalogIds) {
			add(Api.Param.CATALOG_IDS, catalogIds);
		}
		
		public void addDealerFilter(Set<String> dealerIds) {
			add(Api.Param.DEALER_IDS, dealerIds);
		}
		
		public void addStoreFilter(Set<String> storeIds) {
			add(Api.Param.STORE_IDS, storeIds);
		}

		public void addOfferFilter(String offerId) {
			add(Api.Param.OFFER_IDS, offerId);
		}
		
		public void addCatalogFilter(String catalogId) {
			add(Api.Param.CATALOG_IDS, catalogId);
		}
		
		public void addDealerFilter(String dealerId) {
			add(Api.Param.DEALER_IDS, dealerId);
		}
		
		public void addStoreFilter(String storeId) {
			add(Api.Param.STORE_IDS, storeId);
		}
		
	}
	
	public static class Order extends ListRequest.Order {
		
		public Order() {
			super("-" + Api.Sort.POPULARITY);
		}

		public void byPopularity(boolean enable, boolean descending) {
			add(Api.Sort.POPULARITY, descending);
		}

		public void removePopularity() {
			remove(Api.Sort.POPULARITY);
		}

		public void byPage(boolean enable, boolean descending) {
			add(Api.Sort.PAGE, descending);
		}

		public void removePage() {
			remove(Api.Sort.PAGE);
		}

		public void byCreated(boolean enable, boolean descending) {
			add(Api.Sort.CREATED, descending);
		}

		public void removeCreated() {
			remove(Api.Sort.CREATED);
		}

		public void byPrice(boolean enable, boolean descending) {
			add(Api.Sort.PRICE, descending);
		}

		public void removePrice() {
			remove(Api.Sort.PRICE);
		}

		public void bySavings(boolean enable, boolean descending) {
			add(Api.Sort.SAVINGS, descending);
		}

		public void removeSavings() {
			remove(Api.Sort.SAVINGS);
		}

		public void byQuantity(boolean enable, boolean descending) {
			add(Api.Sort.QUANTITY, descending);
		}

		public void removeQuantity() {
			remove(Api.Sort.QUANTITY);
		}

		public void byCount(boolean enable, boolean descending) {
			add(Api.Sort.COUNT, descending);
		}

		public void removeCount() {
			remove(Api.Sort.COUNT);
		}

		public void byExpirationDate(boolean enable, boolean descending) {
			add(Api.Sort.EXPIRATION_DATE, descending);
		}

		public void removeExpirationDate() {
			remove(Api.Sort.EXPIRATION_DATE);
		}
		
		public void byPublicationDate(boolean enable, boolean descending) {
			add(Api.Sort.PUBLICATION_DATE, descending);
		}
		
		public void removePublicationDate() {
			remove(Api.Sort.PUBLICATION_DATE);
		}
		
		public void byValidDate(boolean enable, boolean descending) {
			add(Api.Sort.VALID_DATE, descending);
		}
		
		public void removeValidDate() {
			remove(Api.Sort.VALID_DATE);
		}
		
		public void byDealer(boolean enable, boolean descending) {
			add(Api.Sort.DEALER, descending);
		}
		
		public void removeDealer() {
			remove(Api.Sort.DEALER);
		}
		
		public void byDistance(boolean enable, boolean descending) {
			add(Api.Sort.DISTANCE, descending);
		}
		
		public void removeDistance() {
			remove(Api.Sort.DISTANCE);
		}

	}
	
	public static class Parameter extends ListRequest.Parameter {
		// TODO lookup API doc to find relevant filters
	}
	
	public static class OfferAutoFill extends RequestAutoFill<List<Offer>> {
		
		private boolean mCatalogs;
		private boolean mDealer;
		private boolean mStore;
		
		public OfferAutoFill() {
			this(false, false, false);
		}
		
		public OfferAutoFill(boolean catalogs, boolean dealer, boolean store) {
			mCatalogs = catalogs;
			mDealer = dealer;
			mStore = store;
		}
		
		@Override
		public List<Request<?>> createRequests(List<Offer> data) {
			
			List<Request<?>> reqs = new ArrayList<Request<?>>();
			
			if (!data.isEmpty()) {
				
				if (mStore) {
					reqs.add(getStoreRequest(data));
				}
				
				if (mDealer) {
					reqs.add(getDealerRequest(data));
				}
				
				if (mCatalogs) {
					reqs.add(getCatalogRequest(data));
				}
				
			}
			
			return reqs;
		}

	}

}
