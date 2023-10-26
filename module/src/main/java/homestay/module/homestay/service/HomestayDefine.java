package homestay.module.homestay.service;

public enum HomestayDefine {
    ARTICLE_CONTENT_TYPE_TEXT(1,"text"),
    ARTICLE_CONTENT_TYPE_IMAGE(2,"image"),
    ARTICLE_CONTENT_TYPE_VIDEO(3,"video");

    private final int code;
    private final String name;

    private HomestayDefine(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static boolean isArticleContentType(String name) {
        if (ARTICLE_CONTENT_TYPE_TEXT.getName().equals(name)) {
            return true;
        }
        if (ARTICLE_CONTENT_TYPE_IMAGE.getName().equals(name)) {
            return true;
        }
        return ARTICLE_CONTENT_TYPE_VIDEO.getName().equals(name);
    }

}
