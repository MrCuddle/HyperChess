package hyperchessab.hyperchess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Perlwin on 11/01/2015.
 */
public class LobbyAdapter extends ArrayAdapter<GameListing> {

    private LayoutInflater layoutInflater;

    public LobbyAdapter(Context context, ArrayList<GameListing> objects){
        super(context, R.layout.lobby_listview_item, objects);

        layoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.lobby_listview_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.game_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(getItem(position).getName());

        return convertView;
    }

    private class ViewHolder {
        TextView name;
    }

}
