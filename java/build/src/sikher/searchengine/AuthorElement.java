package sikher.searchengine;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 14.01.2006
 * Time: 13:50:32
 * $Header$
 * $Log$
 * Description:
 */
public class AuthorElement extends NumberedElement{
    public static final int TYPE_UNDEF = 0;
    public static final int TYPE_GURU = 1;

    private int _FType = TYPE_UNDEF;

    public AuthorElement(int number, String value) {
        super(number, value);
    }

    public int getType() {
        return _FType;
    }

    public void setType(int type) {
        _FType = type;
    }
}
