package notification.com.pushnotification;

import androidx.annotation.NonNull;

public class UserId {
  public String userId;
  public <T extends UserId> T WithId(@NonNull final String id)
  {
      this.userId = id;
      return (T)this;
  }
}
