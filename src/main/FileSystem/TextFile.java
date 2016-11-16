package main.FileSystem;

/**
 * Created by dclifford on 11/15/16.
 */
public class TextFile extends Type {
    String content = "";

    TextFile(String name, String content) {
        super(FileType.TEXTFILE, name);
        updateContent(content);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int size() {
        return content.getBytes().length;
    }
}
