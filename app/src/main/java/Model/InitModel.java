package Model;

/**
 * Created by Murtuza on 5/8/2016.
 */
final class InitGetModel<T> {

    Class <T> country;
    private InitGetModel()
    {

    }
    private InitGetModel(Class<T> country)
    {
        this.country = country;
    }
}
final class Country
{
    String country_id;
    String cname_en;
    String country_call_code;

    private Country()
    {

    }
    private Country(String country_id,String cname_en,String country_call_code)
    {
        this.cname_en = cname_en;
        this.country_id =country_id;
        this.country_call_code =country_call_code;
    }
}