import org.aeonbits.owner.Config;

interface CartConfig extends Config {

    @Key("cart.default.size")
    @DefaultValue("3")
    int defaultSize();

    @Key("cart.max.size")
    @DefaultValue("20")
    int maxSize();
}