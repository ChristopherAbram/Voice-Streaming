package fi.jamk.mobile.sinchexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<User> userlist;

    // get application context and phones data to adapter
    public ContactListAdapter(Context context, ArrayList<User> userlist) {
        super(context, R.layout.contactlist, R.id.textView, userlist);
        this.context = context;
        this.userlist = userlist;
    }

    // populate every row in ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get row
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contactlist, parent, false);
        // show phone name
        TextView textView = (TextView) rowView.findViewById(R.id.textView);
        TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
        textView.setText(userlist.get(position).showName);
        textView2.setText(userlist.get(position).phone);
        // return row view
        return rowView;
    }

}