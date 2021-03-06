package fi.jamk.mobile.sinchexample;



public class UserManager {

    private UserList mUserList;

    public UserManager(){
        mUserList = new UserList();
        init();
    }

    protected void init(){
        mUserList.add(new User("Christopher", "christopher_user", "111-111-111", "password"));
        mUserList.add(new User("Kob", "jukrapopk", "222-222-222", "password"));
        mUserList.add(new User("Lucas", "lucas_id", "333-333-333", "password"));
        mUserList.add(new User("Lisa", "lisalisa", "444-444-444", "password"));
        mUserList.add(new User("Maxim", "maxim123", "555-555-555", "password"));
        mUserList.add(new User("Lee", "leethompson", "666-666-666", "password"));
        mUserList.add(new User("John", "johnsnow", "777-777-777", "password"));
    }

    public boolean authorize(User user){
        return mUserList.contains(user);
    }

    public User findUserByPhone(String phone){
        for(User u : mUserList)
            if(u.phone.equals(phone))
                return u;
        return null;
    }

    public UserList getUserList(){
        return mUserList;
    }

}
