package fi.jamk.mobile.sinchexample;



public class UserManager {

    private UserList mUserList;

    public UserManager(){
        mUserList = new UserList();
        init();
    }

    protected void init(){
        mUserList.add(new User("Christopher", "password"));
        mUserList.add(new User("Kob", "password"));
        mUserList.add(new User("Lucas", "password"));
        mUserList.add(new User("Lisa", "password"));
        mUserList.add(new User("Maxim", "password"));
        mUserList.add(new User("Lee", "password"));
        mUserList.add(new User("John", "password"));
    }

    public boolean authorize(User user){
        return mUserList.contains(user);
    }

    public UserList getUserList(){
        return mUserList;
    }

}
