package app.quicktrack.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import app.quicktrack.R;
import app.quicktrack.models.ExpDrawerModel;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class ExpandableAdapter extends BaseExpandableListAdapter {
    Context context;
    List<ExpDrawerModel> title;
    HashMap<ExpDrawerModel,List<String>> listchild;

    public ExpandableAdapter(Context context, List<ExpDrawerModel> title, HashMap<ExpDrawerModel, List<String>> listchild) {
        this.context = context;
        this.title = title;
        this.listchild = listchild;
    }

    @Override
    public int getGroupCount() {
        return title.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listchild.get(title.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return title.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listchild.get(title.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpDrawerModel expDrawerModel= (ExpDrawerModel) getGroup(groupPosition);
    //   String head= (String) getGroup(groupPosition);
        if (convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.drawerheader,null);
        }

       /* if(getGroup(groupPosition)==1)
        {
            Log.d("Pos",""+groupPosition);
        }*/
        TextView textView=(TextView) convertView.findViewById(R.id.submenu);
        textView.setText(expDrawerModel.getIconName());
        ImageView img=(ImageView) convertView.findViewById(R.id.iconimage);
        ImageView img1=(ImageView) convertView.findViewById(R.id.expaimg);

        img.setImageResource(expDrawerModel.getImageid());
        if((groupPosition)==1) {
            img1.setVisibility(View.VISIBLE);
            if (isExpanded) {
                img1.setImageResource(R.drawable.up);
            } else {
                img1.setImageResource(R.drawable.down);

            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String head= (String) getChild(groupPosition,childPosition);
        if (convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.drawerdetail,null);
        }
        TextView textView=(TextView) convertView.findViewById(R.id.deta);
        textView.setText(head);

        return convertView;    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
