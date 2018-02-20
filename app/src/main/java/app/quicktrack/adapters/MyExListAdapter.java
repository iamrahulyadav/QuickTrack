package app.quicktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import app.quicktrack.R;
import app.quicktrack.models.ExpDrawerModel;

/**
 * Created by Muhib.
 * Contact Number : +91 9796173066
 */
public class MyExListAdapter extends BaseExpandableListAdapter {
    Context excessProtection;
    List<ExpDrawerModel> listparent;
    HashMap<ExpDrawerModel, List<String>> listchild;
    public MyExListAdapter(Context excessProtection, List<ExpDrawerModel> listparent, HashMap<ExpDrawerModel, List<String>> listchild) {
        this.excessProtection=excessProtection;
        this.listparent=listparent;
        this.listchild=listchild;
    }

    @Override
    public int getGroupCount() {
        return listparent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listchild.get(listparent.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listparent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listchild.get(listparent.get(groupPosition)).get(childPosition);
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
        ExpDrawerModel head= (ExpDrawerModel) getGroup(groupPosition);
      //  String head= (String) getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) excessProtection.getSystemService(excessProtection.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.drawerheader,null);
        }
        TextView textView= (TextView) convertView.findViewById(R.id.submenu);
        textView.setText(head.getIconName());
        if(isExpanded)
        {
       //     groupHolder.img.setImageResource(R.drawable.ic_droppro);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child= (String) getChild(groupPosition,childPosition);
        if(convertView==null)
        {
            LayoutInflater inflater= (LayoutInflater) excessProtection.getSystemService(excessProtection.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.drawerdetail,null);
        }
        TextView textView= (TextView) convertView.findViewById(R.id.deta);
        textView.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
