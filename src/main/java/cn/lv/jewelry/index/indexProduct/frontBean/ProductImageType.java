package cn.lv.jewelry.index.indexProduct.frontBean;

/**
 * Created by lixing on 16/4/27.
 */
public enum ProductImageType {

    IMAGE("0"), IMAGE_TWO("1"),IMAGE_THREE("2"),IMAGE_FOUR("3"),IMAGE_FIVE("4"),IMAGE_6("5"),IMAGE_7("6"),IMAGE_8("7"),IMAGE_9("8"),IMAGE_10("9"),IMAGE_11("11"),IMAGE_12("12"),IMAGE_13("13");

    private String type;

    ProductImageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean isExist(String type) {
        switch (type) {
            case "0":
                return true;
            case "1":
                return true;
            default:
                return true;
        }
    }
}
