package sikher.searchengine;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 30.11.2005
 * Time: 15:46:04
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/ISOLang.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: ISOLang.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public enum ISOLang {
    NONE("None"),
    AA("Afar"),        AB("Abkhazian"),      AF("Afrikaans"),    AM("Amharic"),     AR("Arabic"),
    AS("Assamese"),    AY("Aymara"),         AZ("Azerbaijani"),  BA("Bashkir"),     BE("Byelorussian"),
    BG("Bulgarian"),   BH("Bihari"),         BI("Bislama"),      BN("Bengali"),     BO("Tibetan"),
    BR("Breton"),      CA("Catalan"),        CO("Corsican"),     CS("Czech"),       CY("Welsh"),
    DA("Danish"),      DE("German"),         DZ("Bhutani"),      EL("Greek"),       EN("English"),
    EO("Esperanto"),   ES("Spanish"),        ET("Estonian"),     EU("Basque"),      FA("Persian"),
    FI("Finnish"),     FJ("Fiji"),           FO("Faeroese"),     FR("French"),      FY("Frisian"),
    GA("Irish"),       GD("Gaelic"),         GL("Galician"),     GN("Guarani"),     GU("Gujarati"),
    HA("Hausa"),       HI("Hindi"),          HR("Croatian"),     HU("Hungarian"),   HY("Armenian"),
    IA("Interlingua"), IE("Interlingue"),    IK("Inupiak"),      IN("Indonesian"),  IS("Icelandic"),
    IT("Italian"),     IW("Hebrew"),         JA("Japanese"),     JI("Yiddish"),     JW("Javanese"),
    KA("Georgian"),    KK("Kazakh"),         KL("Greenlandic"),  KM("Cambodian"),   KN("Kannada"),
    KO("Korean"),      KS("Kashmiri"),       KU("Kurdish"),      KY("Kirghiz"),     LA("Latin"),
    LN("Lingala"),     LO("Laothian"),       LT("Lithuanian"),   LV("Latvian"),     MG("Malagasy"),
    MI("Maori"),       MK("Macedonian"),     ML("Malayalam"),    MN("Mongolian"),   MO("Moldavian"),
    MR("Marathi"),     MS("Malay"),          MT("Maltese"),      MY("Burmese"),     NA("Nauru"),
    NE("Nepali"),      NL("Dutch"),          NO("Norwegian"),    OC("Occitan"),     OM("Oromo"),
    OR("Oriya"),       PA("Gurmukhi"),       PL("Polish"),       PS("Pashto"),      PT("Portuguese"),    //PA("Punjabi")
    QU("Quechua"),     RM("Rhaeto-Romance"), RN("Kirundi"),      RO("Romanian"),    RU("Russian"),
    RW("Kinyarwanda"), SA("Sanskrit"),       SD("Sindhi"),       SG("Sangro"),      SH("Serbo-Croatian"),
    SI("Singhalese"),  SK( "Slovak"),        SL("Slovenian"),    SM("Samoan"),      SN("Shona"),
    SO("Somali"),      SQ("Albanian"),       SR("Serbian"),      SS("Siswati"),     ST("Sesotho"),
    SU("Sudanese"),    SV("Swedish"),        SW("Swahili"),      TA("Tamil"),       TE("Tegulu"),
    TG("Tajik"),       TH("Thai"),           TI("Tigrinya"),     TK("Turkmen"),     TL("Tagalog"),
    TN("Setswana"),    TO("Tonga"),          TR("Turkish"),      TS("Tsonga"),      TT("Tatar"),
    TW("Twi"),         UK("Ukrainian"),      UR("Urdu"),         UZ("Uzbek"),       VI("Vietnamese"),
    VO("Volapuk"),     WO("Wolof"),          XH("Xhosa"),        YO("Yoruba"),      ZH("Chinese"),
    ZU("Zulu");

    final String language;
    ISOLang(String language){
        this.language = language;
    };

    String getLang(){
        return language;
    }

    public String toString(){
        //TODO: get properties
        return getLang();
    }

    public String getCode(){
        return super.toString();
    }
}
