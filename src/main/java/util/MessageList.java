package util;


import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageList {
    public static final Locale LocaleRussia = new Locale("ru","");
    private ResourceBundle fText;

    /* LANG CONSTANT LIST */
    static public enum Message{
        UnknownErrorOnServer,
        UsePost,
        WrongPassword,
        WrongSession,
        Ok,
        WrongAuth,
        UserAlreadyExists,
        CardNameAlreadyExists
    }
    /*    END LANG CONSTANT LIST */


    public MessageList(Locale aLanguage){
        if (aLanguage.equals(Locale.ENGLISH)) {
            fText = ResourceBundle.getBundle(this.getClass().getSimpleName(), Locale.ENGLISH);
        } else if (aLanguage.equals(LocaleRussia)) {
            fText = ResourceBundle.getBundle(this.getClass().getSimpleName(), LocaleRussia);
        } else {
            throw new IllegalStateException("Unknown language");
        }
    }

    public String getText(Message message){
        String result = null;
        try {
            String buf = fText.getString(message.toString());
            //todo Вместо getBytes будем использовать при сборке утилиту native2ascii
            try {
                buf = new String(buf.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Throwable e){
            }
            result = buf;
        } catch (MissingResourceException e) {
            LogFactory.getInstance().getLogger(this.getClass()).error("Util.MessageList/getText Translate text not found");
        }
        return result;
    }


}
