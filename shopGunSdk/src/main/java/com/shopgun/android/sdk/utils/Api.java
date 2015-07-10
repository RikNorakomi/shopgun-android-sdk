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

package com.shopgun.android.sdk.utils;

import com.shopgun.android.sdk.model.Shoppinglist;
import com.shopgun.android.sdk.model.ShoppinglistItem;

/**
 * The {@link Api} class contains miscellaneous helper variables, and classes, which comes very handy when interacting with the API.
 * <p/>
 * <p>For a complete set of keys and their respective parameters, as well as detailed documentation,
 * in a given context, we will refer you to the <a href="http://docs.api.etilbudsavis.dk/">API documentation</a></p>
 *
 * @author Danny Hvam - danny@eTilbudsavis.dk
 */
public final class Api {

    /**
     * The delimiter used for separating multiple variables given to one parameter
     */
    public static final String DELIMITER = ",";
    /**
     * The current API environment in use
     */
    private static Environment mEnvironment = Environment.PRODUCTION;

    /**
     * The current API environment in use for themes (used for e.g. shoppinglists
     */
    private static ThemeEnvironment mThemeEnvironment = ThemeEnvironment.PRODUCTION;

    private Api() {
        // empty
    }

    /**
     * Returns the current {@link Environment} in use.
     *
     * @return The current {@link Environment}
     */
    public static Environment getEnvironment() {
        return mEnvironment;
    }

    /**
     * Set the API environment the API should use.
     * <p/>
     * <p>The environment will only be used, if you do not prefix your url's with another domain name.
     * it's therefore advised to use the url's exposed in {@link Endpoint}.</p>
     *
     * @param e An {@link Environment}
     */
    public static void setEnvironment(Environment e) {
        mEnvironment = e;
    }

    /**
     * Returns the current {@link ThemeEnvironment} in use.
     *
     * @return The current {@link Environment}
     */
    public static ThemeEnvironment getThemeEnvironment() {
        return mThemeEnvironment;
    }

    /**
     * Set the {@link ThemeEnvironment} the SDK will be using.
     */
    public static void setThemeEnvironment(ThemeEnvironment e) {
        mThemeEnvironment = e;
    }


    /**
     * The {@link Environment} contains information on how (and where) to contact the eTilbudsavis API.
     * It by default contains three predefined settings, which maps directly to it's counterparts in our API.
     * And furthermore there is an option to set a {@link Environment#CUSTOM} environment, this is for local
     * delevopment and testing.
     * <p/>
     * By default, the {@link Environment} is set to {@link Environment#PRODUCTION}.
     *
     * @author Danny Hvam - danny@etilbudsavis.dk
     */
    public enum Environment {

        PRODUCTION("https://api.etilbudsavis.dk"),
        EDGE("https://edge.api.etilbudsavis.dk"),
        STAGING("https://staging.api.etilbudsavis.dk"),
        CUSTOM("https://api.etilbudsavis.dk");

        private static final String HTTP = "http://";
        private static final String HTTPS = "https://";
        private static final String pathPrefix = "/";

        private String mEnvironment;

        private Environment(String env) {
            mEnvironment = env;
        }

        public static void setCustom(String env) {
            if (env == null || env.isEmpty()) {
                CUSTOM.mEnvironment = PRODUCTION.mEnvironment;
            } else {
                CUSTOM.mEnvironment = env;
            }
        }

        /**
         * Convert a string into an {@link Environment}.
         *
         * @param env An environment string
         * @return A matching {@link Environment} or {@link Environment#PRODUCTION} if no match was found
         */
        public static Environment fromString(String env) {
            if (env != null) {
                for (Environment e : Environment.values()) {
                    if (env.equalsIgnoreCase(e.mEnvironment)) {
                        return e;
                    }
                }
            }
            Environment.setCustom(env);
            return CUSTOM;
        }

        @Override
        public String toString() {
            return mEnvironment;
        }

        /**
         * Apply the environment to a given path/url.
         * <p/>
         * <p>
         * The {@link Environment} will only be applied, if the url given is only the path of an url.
         * In a {@link Environment#PRODUCTION PRODUCTION} environment we will translate as follows:
         * <ul>
         * <li>"/v2/catalogs" -> "https://api.etilbudsavis.dk/v2/catalogs"
         * <li><code>null</code> -> "https://api.etilbudsavis.dk/"
         * <li>"" -> "https://api.etilbudsavis.dk/"
         * </ul>
         * </p>
         *
         * @param path The path to apply the given Environment to
         * @return A url
         */
        public String apply(String path) {

            if (path == null || path.isEmpty()) {
                return mEnvironment;
            }

            if (path.startsWith(HTTP) || path.startsWith(HTTPS)) {
                // cannot prefix environment to existing full url
                return path;
            }

            if (!path.startsWith(pathPrefix)) {
                path = pathPrefix + path;
            }

            return mEnvironment + path;

        }

    }

    public enum ThemeEnvironment {

        PRODUCTION("https://etilbudsavis.dk/rest/v1/lists/themes/"),
        STAGING("https://staging.etilbudsavis.dk/rest/v1/lists/themes/");

        private final String mEnvironment;

        private ThemeEnvironment(String theme) {
            mEnvironment = theme;
        }

        /**
         * Convert a string into an {@link ThemeEnvironment}.
         *
         * @param env An environment string
         * @return A matching {@link ThemeEnvironment} or {@link ThemeEnvironment#PRODUCTION} if no match was found
         */
        public static ThemeEnvironment fromString(String env) {
            if (env != null) {
                for (ThemeEnvironment e : ThemeEnvironment.values()) {
                    if (env.equalsIgnoreCase(e.mEnvironment)) {
                        return e;
                    }
                }
            }
            return PRODUCTION;
        }

        @Override
        public String toString() {
            return mEnvironment;
        }

        public String build(String path) {
            return mEnvironment + path;
        }

    }

    /**
     * This class contains a sub-set of paths needed to request data from the eTilbudsavis API,
     * and has methods to generate paths, that dependent on information from objects (id's e.t.c).
     *
     * @author Danny Hvam - danny@etilbudsavis.dk
     */
    public static class Endpoint {

        public static final String CATALOG_LIST = "/v2/catalogs";
        public static final String CATALOG_ID = "/v2/catalogs/";
        public static final String CATALOG_SEARCH = "/v2/catalogs/search";
        public static final String CATALOG_TYPEAHEAD = "/v2/catalogs/typeahead";

        public static final String DEALER_LIST = "/v2/dealers";
        public static final String DEALER_ID = "/v2/dealers/";
        public static final String DEALER_SEARCH = "/v2/dealers/search";

        public static final String OFFER_LIST = "/v2/offers";
        public static final String OFFER_ID = "/v2/offers/";
        public static final String OFFER_SEARCH = "/v2/offers/search";
        public static final String OFFER_TYPEAHEAD = "/v2/offers/typeahead";

        public static final String STORE_LIST = "/v2/stores";
        public static final String STORE_ID = "/v2/stores/";
        public static final String STORE_SEARCH = "/v2/stores/search";
        public static final String STORE_QUICK_SEARCH = "/v2/stores/quicksearch";

        public static final String FAVORITES_DEALERS_ID = "/v2/favorites/dealers/";
        public static final String FAVORITES_DEALERS_LIST = "/v2/favorites/dealers";

        public static final String SESSIONS = "/v2/sessions";

        public static final String USER = "/v2/users";

        public static final String USER_RESET = "/v2/users/reset";

        public static final String CATEGORIES = "/v2/categories";

        public static final String COUNTRIES = "/v2/countries";

        public static final String PUSH_PAYLOAD_ID = "/v2/push/payloads/";

        public static final String PUSH_DEVICE_ID = "/v2/push/devices";

        /**
         * /v2/offers/{offer_id}
         */
        public static String offerId(String offerId) {
            return String.format("/v2/offers/%s", offerId);
        }

        /**
         * /v2/stores/{store_id}
         */
        public static String storeId(String storeId) {
            return String.format("/v2/stores/%s", storeId);
        }

        /**
         * /v2/dealers/{dealer_id}
         */
        public static String dealerId(String dealerId) {
            return String.format("/v2/dealers/%s", dealerId);
        }

        /**
         * /v2/catalogs/{catalog_id}
         */
        public static String catalogId(String catalogId) {
            return String.format("/v2/catalogs/%s", catalogId);
        }

        /**
         * /v2/catalogs/{catalog_id}/pages
         */
        public static String catalogPages(String catalogId) {
            return String.format("/v2/catalogs/%s/pages", catalogId);
        }

        /**
         * /v2/catalogs/{catalog_id}/hotspots
         */
        public static String catalogHotspots(String catalogId) {
            return String.format("/v2/catalogs/%s/hotspots", catalogId);
        }

        /**
         * /v2/catalogs/{catalog_id}/collect
         */
        public static String catalogCollect(String catalogId) {
            return String.format("/v2/catalogs/%s/collect", catalogId);
        }

        /**
         * /v2/offers/{offer_id}/collect
         */
        public static String offerCollect(String offerId) {
            return String.format("/v2/offers/%s/collect", offerId);
        }

        /**
         * /v2/stores/{offer_id}/collect
         */
        public static String storeCollect(String storeId) {
            return String.format("/v2/stores/%s/collect", storeId);
        }

        /**
         * /v2/users/{user_id}/facebook
         */
        public static String facebook(int userId) {
            return String.format("/v2/users/%s/facebook", userId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists
         */
        public static String lists(int userId) {
            return String.format("/v2/users/%s/shoppinglists", userId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}
         */
        public static String list(int userId, String listId) {
            return String.format("/v2/users/%s/shoppinglists/%s", userId, listId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}/modified
         */
        public static String listModified(int userId, String listId) {
            return String.format("/v2/users/%s/shoppinglists/%s/modified", userId, listId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}/empty
         */
        public static String listEmpty(int userId, String listId) {
            return String.format("/v2/users/%s/shoppinglists/%s/empty", userId, listId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}/shares
         */
        public static String listShares(int userId, String listId) {
            return String.format("/v2/users/%s/shoppinglists/%s/shares", userId, listId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}/shares/{email}
         */
        public static String listShareEmail(int userId, String listId, String email) {
            return String.format("/v2/users/%s/shoppinglists/%s/shares/%s", userId, listId, Utils.encode(email, "UTF-8"));
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}/items
         */
        public static String listitems(int userId, String listId) {
            return String.format("/v2/users/%s/shoppinglists/%s/items", userId, listId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}/items/{item_uuid}
         */
        public static String listitem(int userId, String listId, String itemId) {
            return String.format("/v2/users/%s/shoppinglists/%s/items/%s", userId, listId, itemId);
        }

        /**
         * /v2/users/{user_id}/shoppinglists/{list_uuid}/items/{item_uuid}/modified
         */
        public static String listitemModifiedById(int userId, String listId, String itemId) {
            return String.format("/v2/users/%s/shoppinglists/%s/items/%s/modified", userId, listId, itemId);
        }

        /**
         * /v2/push/payloads/{payload_id}
         */
        public static String pushPayload(String payloadId) {
            return String.format("/v2/push/payloads/%s", payloadId);
        }

        /**
         * https://etilbudsavis.dk/ern/{ern}/
         */
        public static String shareERN(String ern) {
            return String.format("https://etilbudsavis.dk/ern/%s/", ern);
        }

    }

    /**
     * This class contains sub-set of strings, that eTilbudsavis API v2 uses as keys, for the JSON data
     * that is being send to and from the API.
     *
     * @author Danny Hvam - danny@etilbudsavis.dk
     */
    public class JsonKey {

        public static final String SDK_DEALER = "sdk_dealer";
        public static final String SDK_STORE = "sdk_store";
        public static final String SDK_CATALOG = "sdk_catalog";
        public static final String SDK_PAGES = "sdk_pages";

        public static final String ID = "id";
        public static final String ERN = "ern";
        public static final String NAME = "name";
        public static final String RUN_FROM = "run_from";
        public static final String RUN_TILL = "run_till";
        public static final String DEALER_ID = "dealer_id";
        public static final String DEALER_URL = "dealer_url";
        public static final String STORE_ID = "store_id";
        public static final String STORE_URL = "store_url";
        public static final String IMAGES = "images";
        public static final String BRANDING = "branding";
        public static final String MODIFIED = "modified";
        public static final String DESCRIPTION = "description";
        public static final String WEBSITE = "website";
        public static final String LOGO = "logo";
        public static final String COLOR = "color";
        public static final String PAGEFLIP = "pageflip";
        public static final String COUNTRY = "country";
        public static final String ACCESS = "access";
        public static final String LABEL = "label";
        public static final String BACKGROUND = "background";
        public static final String PAGE_COUNT = "page_count";
        public static final String OFFER_COUNT = "offer_count";
        public static final String DIMENSIONS = "dimensions";
        public static final String PAGES = "pages";
        public static final String PAGE = "page";
        public static final String OWNER = "owner";
        public static final String TICK = "tick";
        public static final String OFFER_ID = "offer_id";
        public static final String COUNT = "count";
        public static final String SHOPPINGLIST_ID = "shopping_list_id";
        public static final String CREATOR = "creator";
        public static final String HEADING = "heading";
        public static final String CATALOG_PAGE = "catalog_page";
        public static final String PRICING = "pricing";
        public static final String QUANTITY = "quantity";
        public static final String LINKS = "links";
        public static final String CATALOG_URL = "catalog_url";
        public static final String CATALOG_ID = "catalog_id";
        public static final String STREET = "street";
        public static final String CITY = "city";
        public static final String ZIP_CODE = "zip_code";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String CONTACT = "contact";
        public static final String WEBSHOP = "webshop";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String CODE = "code";
        public static final String MESSAGE = "message";
        public static final String DETAILS = "details";
        public static final String FAILED_ON_FIELD = "failed_on_field";
        public static final String VIEW = "view";
        public static final String ZOOM = "zoom";
        public static final String THUMB = "thumb";
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String UNIT = "unit";
        public static final String SIZE = "size";
        public static final String PIECES = "pieces";
        public static final String USER = "user";
        public static final String ACCEPTED = "accepted";
        public static final String SYMBOL = "symbol";
        public static final String GENDER = "gender";
        public static final String BIRTH_YEAR = "birth_year";
        public static final String EMAIL = "email";
        public static final String PERMISSIONS = "permissions";
        public static final String PREVIOUS_ID = "previous_id";
        public static final String SI = "si";
        public static final String FACTOR = "factor";
        public static final String UNSUBSCRIBE_PRINT_URL = "unsubscribe_print_url";
        public static final String TYPE = "type";
        public static final String META = "meta";
        public static final String SHARES = "shares";
        public static final String TOKEN = "token";
        public static final String EXPIRES = "expires";
        public static final String PROVIDER = "provider";
        public static final String PRICE = "price";
        public static final String PREPRICE = "pre_price";
        public static final String CURRENCY = "currency";
        public static final String LENGTH = "length";
        public static final String OFFSET = "offset";
        public static final String SUBJECT = "subject";
        public static final String ACCEPT_URL = "accept_url";
        public static final String PDF_URL = "pdf_url";
        public static final String CATEGORY_IDS = "category_ids";
        public static final String OFFER = "offer";
        public static final String LOCATIONS = "locations";
        public static final String CLIENT_ID = "client_id";
        public static final String REFERENCE = "reference";
        public static final String SUBSCRIBED = "subscribed";
        public static final String PAYLOAD = "payload";
        public static final String DEALER = "dealer";
        public static final String CATALOGS = "catalogs";
        public static final String PAYLOAD_TYPE = "payload_type";

    }

    /**
     * <p>This class contains a sub-set of strings that eTilbudsavis uses as keys for the meta-object,
     * found on {@link Shoppinglist#getMeta()}, and {@link ShoppinglistItem#getMeta()}.</p>
     *
     * @author Danny Hvam - danny@etilbudsavis.dk
     */
    public class MetaKey {
        /**
         * for comments on shoppinglistitems
         */
        public static final String COMMENT = "eta_comment";
        /**
         * for themes on shoppinglists
         */
        public static final String THEME = "eta_theme";
    }

    /**
     * <p>This class contains a sub-set of strings that eTilbudsavis uses for sorting lists queries.
     * All sort types are in ascending order, but can be reverted to descending order by prepending
     * the string "-" to any of them.</p>
     *
     * @author Danny Hvam - danny@etilbudsavis.dk
     */
    public class Sort {

        /**
         * Sort a list by popularity in ascending order.
         */
        public static final String POPULARITY = "popularity";

        /**
         * Sort a list by distance in ascending order.
         */
        public static final String DISTANCE = "distance";

        /**
         * Sort a list by name in ascending order.
         */
        public static final String NAME = "name";

        /**
         * Sort a list by published in ascending order.
         */
        public static final String PUBLICATION_DATE = "publication_date";

        /**
         * Sort a list by expired in ascending order.
         */
        public static final String EXPIRATION_DATE = "expiration_date";

        /**
         * Sort a list by created in ascending order.
         */
        public static final String CREATED = "created";

        /**
         * Sort a list by page (in catalog) in ascending order.
         */
        public static final String PAGE = "page";

        /**
         * Sort a list by it's internal score in ascending order.
         */
        public static final String SCORE = "score";

        /**
         * Sort a list by price in ascending order.
         */
        public static final String PRICE = "price";

        public static final String DEALER = "dealer";

        public static final String SAVINGS = "savings";

        public static final String QUANTITY = "quantity";

        public static final String COUNT = "count";

        public static final String VALID_DATE = "valid_date";

    }

    /**
     * This class contains sub-set of strings, that eTilbudsavis API v2 uses as parameters,
     * for queries to the API.
     *
     * @author Danny Hvam - danny@etilbudsavis.dk
     */
    public class Param {

        /**
         * String identifying the order by parameter for all list calls to the API
         */
        public static final String ORDER_BY = "order_by";

        /**
         * API v2 parameter name for sensor.
         */
        public static final String SENSOR = "r_sensor";

        /**
         * API v2 parameter name for latitude.
         */
        public static final String LATITUDE = "r_lat";

        /**
         * API v2 parameter name for longitude.
         */
        public static final String LONGITUDE = "r_lng";

        /**
         * API v2 parameter name for radius.
         */
        public static final String RADIUS = "r_radius";

        /**
         * API v2 parameter name for bounds east.
         */
        public static final String BOUND_EAST = "b_east";

        /**
         * API v2 parameter name for bounds north.
         */
        public static final String BOUND_NORTH = "b_north";

        /**
         * API v2 parameter name for bounds south.
         */
        public static final String BOUND_SOUTH = "b_south";

        /**
         * API v2 parameter name for bounds west.
         */
        public static final String BOUND_WEST = "b_west";

        /**
         * API v2 parameter name for API Key
         */
        public static final String API_KEY = "api_key";

        /**
         * String identifying the offset parameter for all list calls to the API
         */
        public static final String OFFSET = "offset";

        /**
         * String identifying the limit parameter for all list calls to the API
         */
        public static final String LIMIT = "limit";

        /**
         * String identifying the run from parameter for all list calls to the API
         */
        public static final String RUN_FROM = "run_from";

        /**
         * String identifying the run till parameter for all list calls to the API
         */
        public static final String RUN_TILL = "run_till";

        /**
         * String identifying the color parameter for all list calls to the API
         */
        public static final String COLOR = "color";

        /**
         * Parameter for pdf file location
         */
        public static final String PDF = "pdf";

        /**
         * Parameter for a resource name, e.g. dealer name
         */
        public static final String NAME = "name";

        /**
         * Parameter for a dealer resource
         */
        public static final String DEALER = "dealer";

        /**
         * Parameter for the friendly name of a website
         */
        public static final String URL_NAME = "url_name";

        /** Parameter for pageflip color */
//		public static final String PAGEFLIP_COLOR = "pageflip_color";

        /**
         * Parameter for the absolute address of a website
         */
        public static final String WEBSITE = "website";

        /**
         * Parameter for a resource logo
         */
        public static final String LOGO = "logo";

        /**
         * Parameter for search
         */
        public static final String QUERY = "query";

        /** Parameter for pageflip logo location */
//		public static final String PAGEFLIP_LOGO = "pageflip_Logo";

        /**
         * Parameter for catalog id's
         */
        public static final String CATALOG_IDS = "catalog_ids";

        /**
         * Parameter for catalog id's
         */
        public static final String CATALOG_ID = "catalog_id";

        /**
         * Parameter for store id's
         */
        public static final String STORE_IDS = "store_ids";

        /**
         * Parameter for area id's
         */
        public static final String AREA_IDS = "area_ids";

        /**
         * Parameter for store id's
         */
        public static final String OFFER_IDS = "offer_ids";

        /**
         * Parameter for getting a list of specific dealer id's
         */
        public static final String DEALER_IDS = "dealer_ids";

        /**
         * Parameter for a resource e-mail
         */
        public static final String EMAIL = "email";

        /**
         * Parameter for a resource password
         */
        public static final String PASSWORD = "password";

        /**
         * Parameter for a resource birth year
         */
        public static final String BIRTH_YEAR = "birth_year";

        /**
         * Parameter for a resource gender
         */
        public static final String GENDER = "gender";

        /**
         * Parameter for a resource success redirect
         */
        public static final String SUCCESS_REDIRECT = "success_redirect";

        /**
         * Parameter for a resource error redirect
         */
        public static final String ERROR_REDIRECT = "error_redirect";

        /**
         * Parameter for a resource old password
         */
        public static final String OLD_PASSWORD = "old_password";

        /**
         * Parameter for a facebook token
         */
        public static final String FACEBOOK_TOKEN = "facebook_token";

        /**
         * Parameter for a delete filter
         */
        public static final String FILTER_DELETE = "filter";

        public static final String ID = "id";

        public static final String MODIFIED = "modified";

        public static final String ERN = "ern";

        public static final String ACCESS = "access";

        public static final String ACCEPT_URL = "accept_url";

        public static final String DESCRIPTION = "description";

        public static final String COUNT = "count";

        public static final String TICK = "tick";

        public static final String OFFER_ID = "offer_id";

        public static final String CREATOR = "creator";

        public static final String SHOPPING_LIST_ID = "shopping_list_id";

        /**
         * Parameter for a resource token time to live
         */
        public static final String TOKEN_TTL = "token_ttl";

        /**
         * Parameter for a v1 session migration
         */
        public static final String V1_AUTH_ID = "v1_auth_id";

        /**
         * Parameter for a v1 session migration
         */
        public static final String V1_AUTH_TIME = "v1_auth_time";

        /**
         * Parameter for a v1 session migration
         */
        public static final String V1_AUTH_HASH = "v1_auth_hash";

        /**
         * Parameter for locale
         */
        public static final String LOCALE = "locale";

        /**
         * Parameter for sending the app version for better app statistics in insight
         */
        public static final String API_AV = "api_av";

        /**
         * Parameter not yet integrated in the API
         */
        public static final String ACCURACY = "accuracy";

        /**
         * Parameter not yet integrated in the API
         */
        public static final String BEARING = "bearing";

        /**
         * Parameter not yet integrated in the API
         */
        public static final String ALTITUDE = "altitude";

        /**
         * Parameter not yet integrated in the API
         */
        public static final String PROVIDER = "provider";

        /**
         * Parameter not yet integrated in the API
         */
        public static final String SPEED = "speed";

        /**
         * Parameter not yet integrated in the API
         */
        public static final String TIME = "time";

        /**
         * Parameter not yet integrated in the API
         */
        public static final String ADDRESS = "address";

    }


}