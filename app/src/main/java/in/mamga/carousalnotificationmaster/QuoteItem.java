package in.mamga.carousalnotificationmaster;

/**
 * Created by Shailesh on 09/01/17.
 */

public class QuoteItem {

    /**
     * ID : 1305
     * title : Babe Ruth
     * content : <p>The way a team plays as a whole determines its success. You may have the greatest bunch of individual stars in the world, but if they don&#8217;t play together, the club won&#8217;t be worth a dime.  </p>

     * link : https://quotesondesign.com/babe-ruth/
     */

    private int ID;
    private String title;
    private String content;
    private String link;

    public QuoteItem(int ID, String title, String content) {
        this.ID = ID;
        this.title = title;
        this.content = content;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
