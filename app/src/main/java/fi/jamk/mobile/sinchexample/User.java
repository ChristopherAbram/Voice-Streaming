package fi.jamk.mobile.sinchexample;

public class User {

    public String name;
    public String password;

    public User(){}

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof User)) return false;

        User user = (User) o;
        if(user != null && name.equals(user.name) && password.equals(user.password))
            return true;

        return false;
    }
}
