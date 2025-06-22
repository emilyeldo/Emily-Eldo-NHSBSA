package locators;

public class Search {
	public static final String SEARCH_LINK_XPATH = "//nav[@id='header-navigation']//a[@href='/candidate/search']";
    public static final String JOB_TITLE_ID = "keyword";
    public static final String LOCATION_ID = "location";
    public static final String DISTANCE_ID = "distance";
    public static final String JOB_REFERENCE_ID = "jobReference";
    public static final String PAY_RANGE_ID = "payRange";
    public static final String EMPLOYER_ID = "employer";
    public static final String SEARCH_OPTIONS_LINK_ID = "searchOptionsBtn";
    public static final String SEARCH_BUTTON_ID ="search";
    public static final String CLEAR_FILTER_BUTTON_ID ="clearFilters";
    public static final String LOCATION_LIST_ID="location__listbox";
    public static final String LOCATION_LIST_XPATH="//ul[@id='location__listbox']//li[contains(@id,location__option)]";
    public static final String JOB_RESULT_LIST_XPATH="//li[@data-test='search-result']";
    
    public static final String JOB_RESULT_SALARY_LIST_XPATH="//li[@data-test='search-result']//li[@data-test='search-result-salary']";
    public static final String JOB_RESULT_LOCATION_LIST_XPATH="//div[@data-test='search-result-location']//div[@class='location-font-size']";
    public static final String JOB_RESULT_EMPLOYER_LIST_XPATH="//div[@data-test='search-result-location']";
    public static final String JOB_RESULT_TITLE_LIST_XPATH ="//a[@data-test='search-result-job-title']";
    public static final String SORT_BY_ID ="sort";
    public static final String NO_RESULT_FOUND_ID ="no-result-title";
    public static final String DATE_PUBLISHED_XPATH ="//li[@data-test='search-result-publicationDate']//strong";
    public static final String DEFAULT_SORT_SELECTION ="//select[@id='sort']";
}