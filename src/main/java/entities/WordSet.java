package entities;

public class WordSet {
    private int wordSetId;
    private String name;
    private int size;
    private boolean isMain;
    private byte [] image;

    public int getWordSetId() {
        return wordSetId;
    }

    public void setWordSetId(int wordSetId) {
        this.wordSetId = wordSetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "WordSet{" +
                "wordSetId=" + wordSetId +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", isMain=" + isMain +
                '}';
    }
}
