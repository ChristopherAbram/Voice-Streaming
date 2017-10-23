package fi.jamk.mobile.sinchexample;

import java.util.ArrayList;
import java.util.Collection;

public class UserList extends ArrayList<User> {

    public UserList(){ super(); }

    public UserList(Collection c){
        super(c);
    }
}
