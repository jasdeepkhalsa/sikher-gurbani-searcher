package sikher.searchengine;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 14.01.2006
 * Time: 12:05:46
 */
public class NumberedElement {
    private int _FNumber;
    private String _FValue;

    public NumberedElement( int number, String value ){

        _FNumber = number;
        _FValue = value;
    }

    public int getNumber() {
        return _FNumber;
    }

    public String getValue() {
        return _FValue;
    }

    public void setValue(String value) {
        _FValue = value;
    }

    public boolean equals( Object obj ){
        if( obj instanceof NumberedElement ){
            NumberedElement _elem = (NumberedElement) obj;
            return _elem.getNumber() == getNumber() && _elem.getValue().equals( getValue() );
        }
        return false;
    }

    public String toString(){
        return getValue();
    }
}
