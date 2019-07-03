package notification.com.pushnotification;

public class UserDetailModal extends UserId {
    private String name;
    private String imageUrl;
    UserDetailModal()
    {

    }

    public UserDetailModal(String imageUrl,String name) {
        this.name = name;

        this.imageUrl = imageUrl;

    }
    public UserDetailModal(String image_Url)
    {

    }
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
