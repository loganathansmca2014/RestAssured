package generalPojoSerailAndDeserial;

public class GetInfo {
    private User user;
    private RequestMetaData requestMeta;

    public User getUser()
    {
        return user;
    }
    public  void setUser(User user)
    {
        this.user=user;
    }
    public RequestMetaData getRequestMeta()
    {
        return requestMeta;
    }
    public void setRequestMeta(RequestMetaData requestMeta)
    {
        this.requestMeta=requestMeta;
    }

}
