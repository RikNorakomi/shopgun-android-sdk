/*******************************************************************************
 * Copyright 2015 ShopGun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.shopgun.android.sdk.test;

import android.graphics.Color;

import com.shopgun.android.sdk.model.Branding;
import com.shopgun.android.sdk.model.Catalog;
import com.shopgun.android.sdk.model.Country;
import com.shopgun.android.sdk.model.Dealer;
import com.shopgun.android.sdk.model.Dimension;
import com.shopgun.android.sdk.model.Hotspot;
import com.shopgun.android.sdk.model.HotspotMap;
import com.shopgun.android.sdk.model.Images;
import com.shopgun.android.sdk.model.Links;
import com.shopgun.android.sdk.model.Offer;
import com.shopgun.android.sdk.model.Pageflip;
import com.shopgun.android.sdk.model.Permission;
import com.shopgun.android.sdk.model.Pieces;
import com.shopgun.android.sdk.model.Pricing;
import com.shopgun.android.sdk.model.Quantity;
import com.shopgun.android.sdk.model.Session;
import com.shopgun.android.sdk.model.Share;
import com.shopgun.android.sdk.model.Shoppinglist;
import com.shopgun.android.sdk.model.ShoppinglistItem;
import com.shopgun.android.sdk.model.Si;
import com.shopgun.android.sdk.model.Size;
import com.shopgun.android.sdk.model.Store;
import com.shopgun.android.sdk.model.Subscription;
import com.shopgun.android.sdk.model.Typeahead;
import com.shopgun.android.sdk.model.Unit;
import com.shopgun.android.sdk.model.User;
import com.shopgun.android.sdk.model.interfaces.SyncState;
import com.shopgun.android.sdk.palette.SgnColor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ModelCreator {

    public static final String UUID_V4_FAKE = "de305d54-75b4-431b-adb2-eb6b9e546013";

    private static String getUrl(String id, String path) {
        return String.format("https://eta.dk/%s/%s", id, path);
    }

    private static String getUrl(String id) {
        return String.format("https://eta.dk/%s", id);
    }

    private static String getID(String type, String id) {
        return String.format("%s-%s", type, id);
    }

    /**
     * Returns a date, that has been rounded to the nearest day (set to 16:00:00)
     * @param dayOffset The number os days to off the given day
     * @return A date
     */
    public static Date getDate(int dayOffset) {
        Calendar c = GregorianCalendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH) + dayOffset;
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 16);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Country getCountry() {
        return getCountry("DK");
    }

    public static Country getCountry(String id) {
        Country c = new Country();
        c.setId(id);
        c.setUnsubscribePrintUrl(getUrl(id));
        return c;
    }

    public static Store getStore() {
        return getStore("fds893r");
    }

    public static Store getStore(String id) {
        Store s = new Store();
        s.setBranding(getBranding());
        s.setCity("fake-city");
        s.setContact("fake-email@fake.com");
        s.setCountry(getCountry());
        String dealerId = getID("dealer", id);
        s.setDealerId(dealerId);
        s.setDealerUrl(getUrl(dealerId));
        s.setId(id);
        s.setLatitude(12.5d);
        s.setLongitude(9.5d);
        s.setStreet("fake-street");
        s.setZipcode("fake-zipcode");
        s.setCategoryIds(getCategoryIds());
        return s;
    }

    public static Share getShare() {
        return getShare("fake-mail@eta.dk", Share.ACCESS_OWNER, "fake-accept-url");
    }

    public static Share getShare(String email, String access, String acceptUrl) {
        return new Share(email, access, acceptUrl);
    }

    public static User getUser() {
        return getUser(1932, "fake-mail@eta.dk", "female", "fake-user-name", getPermission(), 1992);
    }

    public static User getUser(int year, String email, String gender, String name, Permission permissions, int id) {
        User u = new User();
        u.setBirthYear(year);
        u.setEmail(email);
        u.setGender(gender);
        u.setName(name);
        u.setPermissions(permissions);
        u.setUserId(id);
        return u;
    }

    public static Branding getBranding() {
        return getBranding(Color.GREEN, "fake-logo-url", "fake-branding-name",
                getPageflip(), "fake-website-url", "fake-description");
    }

    public static Branding getBranding(int color, String logoUrl, String name,
                                       Pageflip pageflip, String website, String description) {
        Branding b = new Branding();
        b.setColor(color);
        b.setLogo(logoUrl);
        b.setName(name);
        b.setDescription(description);
        b.setPageflip(pageflip);
        b.setWebsite(website);
        return b;
    }

    public static Dimension getDimension() {
        return getDimension(1.30d, 1.0d);
    }

    public static Dimension getDimension(double height, double width) {
        Dimension d = new Dimension();
        d.setHeight(height);
        d.setWidth(width);
        return d;
    }

    public static Hotspot getHotspot() {
        return getHotspot(0, 100, 100, 0);
    }

    public static Hotspot getHotspot(int top, int right, int bottom, int left) {
        Hotspot h = new Hotspot();
        h.mAbsBottom = bottom;
        h.mAbsTop = top;
        h.mAbsLeft = left;
        h.mAbsRight = right;
        // A test failed on serialization due to Offer not being Seriliazable
        h.setOffer(getOffer());
        return h;
    }

    public static HotspotMap getHotspotMap() {
        return getHotspotMap("fake-logo-url", Color.GREEN);
    }

    public static HotspotMap getHotspotMap(String logoUrl, int color) {
        HotspotMap h = new HotspotMap();
        List<Hotspot> list = new ArrayList<Hotspot>();
        for (int i = 0; i < 10; i++) {
            list.add(getHotspot(i, 100 + i, 100 + i, i));
        }
        h.put(1, list);
        return h;
    }

    public static Pageflip getPageflip() {
        return getPageflip(getUrl("fake-id"), Color.GREEN);
    }

    public static Pageflip getPageflip(String logoUrl, int color) {
        Pageflip p = new Pageflip();
        p.setLogo(logoUrl);
        p.setColor(new SgnColor(color));
        return p;
    }

    public static Permission getPermission() {

        Permission p = new Permission();

        String group = "group";
        ArrayList<String> groupPermissions = new ArrayList<String>();
        groupPermissions.add("group-permission-read");
        groupPermissions.add("group-permission-write");
        p.put(group, groupPermissions);

        String user = "user";
        ArrayList<String> permissions = new ArrayList<String>();
        permissions.add("user-permission-read");
        permissions.add("user-permission-write");
        HashMap<String, ArrayList<String>> userPermissions = new HashMap<String, ArrayList<String>>();
        userPermissions.put(user, permissions);
        p.putAll(userPermissions);

        return p;
    }

    public static Typeahead getTypeahead() {
        return getTypeahead("fake-subject", 0, 3);
    }

    public static Typeahead getTypeahead(String subject, int offset, int length) {
        Typeahead t = new Typeahead();
        t.setSubject(subject);
        t.setOffset(offset);
        t.setLength(length);
        return t;
    }

    public static Images getImages() {
        return getImages("fake-id", 0);
    }

    public static Images getImages(String id, int page) {
        Images i = new Images();
        i.setThumb(getImageUrl(id, "thumb", page));
        i.setView(getImageUrl(id, "view", page));
        i.setZoom(getImageUrl(id, "zoom", page));
        return i;
    }

    private static String getImageUrl(String id, String path, int page) {
        return String.format("https://eta.dk/%s/%s-%s.jpg", id, path, page);
    }

    public static Links getLinks() {
        return getLinks("fake-id");
    }

    public static Links getLinks(String id) {
        Links l = new Links();
        l.setWebshop(getUrl(id, "webshop"));
        return l;
    }

    public static Pricing getPricing() {
        return getPricing("DKK", 20.00d, 15.00d);
    }

    public static Pricing getPricing(String currency, double prePrice, double price) {
        Pricing p = new Pricing();
        p.setCurrency(currency);
        p.setPrePrice(prePrice);
        p.setPrice(price);
        return p;
    }

    public static Pieces getPieces() {
        return getPieces(1, 1);
    }

    public static Pieces getPieces(int from, int to) {
        Pieces p = new Pieces();
        p.setFrom(from);
        p.setTo(to);
        return p;
    }


    public static Si getSi() {
        return getSi("FOOBAR", 1.0d);
    }

    public static Si getSi(String symbol, double factor) {
        Si s = new Si();
        s.setFactor(factor);
        s.setSymbol(symbol);
        return s;
    }

    public static Unit getUnit() {
        return getUnit("fake-symbol", getSi());
    }

    public static Unit getUnit(String symbol, Si si) {
        Unit u = new Unit();
        u.setSi(si);
        u.setSymbol(symbol);
        return u;
    }

    public static Size getSize() {
        return getSize(1.0d, 1.0d);
    }

    public static Size getSize(double from, double to) {
        Size s = new Size();
        s.setFrom(from);
        s.setTo(to);
        return s;
    }

    public static Subscription getSubscription() {
        return getSubscription("fake-dealer", true);
    }

    public static Subscription getSubscription(String dealerId, boolean subscribed) {
        Subscription s = new Subscription(dealerId);
        s.setSubscribed(subscribed);
        return s;
    }

    public static Quantity getQuantity() {
        return getQuantity(getPieces(), getUnit(), getSize());
    }

    public static Quantity getQuantity(Pieces pieces, Unit unit, Size size) {
        Quantity q = new Quantity();
        q.setPieces(pieces);
        q.setUnit(unit);
        q.setSize(size);
        return q;
    }

    public static HashSet<String> getCategoryIds() {
        return getCategoryIds(new String[]{"discount", "pets", "fashion", "sport"});
    }

    public static HashSet<String> getCategoryIds(String[] ids) {
        HashSet<String> s = new HashSet<String>();
        s.addAll(Arrays.asList(ids));
        return s;
    }

    public static ShoppinglistItem getShoppinglistItem() {
        return getShoppinglistItem("89azf82", "pizza");
    }

    public static ShoppinglistItem getShoppinglistItem(String id, String description) {
        Shoppinglist sl = getShoppinglist();
        ShoppinglistItem s = new ShoppinglistItem(sl, description);
        s.setComment("fake-comment");
        s.setCount(187);
        s.setCreator("fake-user@eta.dk");
        s.setDescription("fake-description");
        s.setId(id);
        JSONObject o = new JSONObject();
        try {
            o.put("metadata", "foobar");
        } catch (JSONException e) {
            // ignore
        }
        s.setMeta(o);
        s.setModified(getDate(1));
        s.setOffer(getOffer());
        s.setOfferId(s.getOffer().getId());
        s.setPreviousId(UUID_V4_FAKE);
        s.setShoppinglistId(sl.getId());
        s.setState(SyncState.TO_SYNC);
        s.setTick(true);
        s.setUserId(187);
        return s;
    }

    public static Shoppinglist getShoppinglist() {
        return getShoppinglist("489efdb2", "bents liste");
    }

    public static Shoppinglist getShoppinglist(String id, String name) {
        Shoppinglist s = Shoppinglist.fromName(name);
        s.setAccess(Shoppinglist.ACCESS_PRIVATE);
        s.setId(id);
        JSONObject o = new JSONObject();
        try {
            o.put("metadata", "foobar");
        } catch (JSONException e) {
            // ignore
        }
        s.setMeta(o);
        s.setModified(getDate(1));
        s.setName(name);
        s.setPreviousId(UUID_V4_FAKE);
        List<Share> shares = new ArrayList<Share>(10);
        for (int i = 0; i < 10; i++) {
            String email = String.format("danny%s@eta.dk", i);
            shares.add(getShare(email, Share.ACCESS_READWRITE, getUrl(id)));
        }
        s.setShares(shares);
        s.setState(SyncState.TO_SYNC);
        s.setType(Shoppinglist.TYPE_SHOPPING_LIST);
        s.setUserId(187);
        return s;
    }

    public static Session getSession() {
        return getSession(getDate(1).getTime(), "token-bafa555b");
    }

    public static Session getSession(long expires, String token) {
        Session s = new Session();
        s.setClientId("fake-cid");
        long exp = expires + TimeUnit.HOURS.toMillis(1);
        s.setExpires(new Date(exp));
        s.setPermission(getPermission());
        s.setProvider("fake-provider");
        s.setReference("fake-reference");
        s.setToken(token);
        s.setUser(getUser());
        return s;
    }

    public static Dealer getDealer() {
        return getDealer("bcd825a", "fake-netto");
    }

    public static Dealer getDealer(String id, String name) {
        Dealer d = new Dealer();
        d.setColor(Color.MAGENTA);
        d.setId(id);
        d.setLogo(getUrl(id, "fake-logo"));
        d.setName(name);
        d.setPageflip(getPageflip());
        d.setWebsite(getUrl(id, "website"));
        d.setCategoryIds(getCategoryIds());
        d.setCountry(getCountry());
        d.setDescription("fake-description");
        return d;
    }

    public static Catalog getCatalog() {
        return getCatalog("156fab7");
    }

    public static Catalog getCatalog(String id) {
        Catalog c = new Catalog();
        c.setBackground(Color.BLACK);
        c.setBranding(getBranding());
        c.setCategoryIds(getCategoryIds());
        c.setDimension(getDimension());
        c.setId(id);
        c.setHotspots(getHotspotMap());
        c.setImages(getImages());
        c.setLabel("fake-label");
        c.setOfferCount(32);
        c.setPageCount(9);
        List<Images> images = new ArrayList<Images>(10);
        for (int i = 0; i < 10; i++) {
            images.add(getImages(id, i));
        }
        c.setPages(images);
        c.setPdfUrl(getUrl("pdf"));

        c.setRunFrom(getDate(-3));
        c.setRunTill(getDate(3));

        String dealerId = getID("dealer", id);
        c.setDealerId(dealerId);
        c.setDealerUrl(getUrl(dealerId));

        String storeId = getID("store", id);
        c.setStoreId(storeId);
        c.setStoreUrl(getUrl(storeId));

        return c;
    }

    public static Offer getOffer() {
        String name = "Chicken Soup";
        String id = "32abf85";
        String description = "The great taste of homemade chicken soup.";
        return getOffer(name, description, id);
    }

    public static Offer getOffer(String name, String description, String id) {

        Offer o = new Offer();
        String offerId = getID("offer", id);
        o.setId(offerId);
        o.setHeading(name);
        o.setDescription(description);
        o.setCatalogPage(32);
        o.setImages(getImages(offerId, 0));
        o.setLinks(getLinks(offerId));
        o.setPricing(getPricing());
        o.setQuantity(getQuantity());

        o.setRunFrom(getDate(-3));
        o.setRunTill(getDate(3));

        String catalogId = getID("catalog", id);
        o.setCatalogId(catalogId);
        o.setCatalogUrl(getUrl(catalogId));

        String dealerId = getID("dealer", id);
        o.setDealerId(dealerId);
        o.setDealerUrl(getUrl(dealerId));

        String storeId = getID("store", id);
        o.setStoreId(storeId);
        o.setStoreUrl(getUrl(storeId));

        o.setCategoryIds(getCategoryIds());

        Pageflip pf = getPageflip("fake-logo-url", Color.BLUE);
        o.setBranding(getBranding(Color.BLUE, "fake-logo-url", "fake-name", pf, "fake-website", "fake-description"));

        return o;
    }

}
