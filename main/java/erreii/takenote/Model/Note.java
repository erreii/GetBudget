package erreii.takenote.Model;

/**
 * Created by Asus on 9/3/2016.
 */

public class Note {

    private String subject;
    private String note;
    private String createTime;
    private String category;
    private String imgPath;

    public Note(){

    }

    public Note(String subject, String note, String createTime,String category, String imgPath) {
        this.subject = subject;
        this.note = note;
        this.createTime = createTime;
        this.category = category;
        this.imgPath = imgPath;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "Note{" +
                "subject='" + subject + '\'' +
                ", note='" + note + '\'' +
                ", createTime='" + createTime + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
