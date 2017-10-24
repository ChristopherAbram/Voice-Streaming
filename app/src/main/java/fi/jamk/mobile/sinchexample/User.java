package fi.jamk.mobile.sinchexample;

public class User {

    public String showName;
    public String name;
    public String phone;
    public String password;

    public User(){}

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public User(String showName, String name, String phone, String password){
        this.showName = showName;
        this.name = name;
        this.phone = phone;
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

    @Override
    public String toString(){
        return name;
    }

}
