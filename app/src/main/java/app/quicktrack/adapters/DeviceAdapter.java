package app.quicktrack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.quicktrack.R;
import app.quicktrack.models.DeviceData;
import app.quicktrack.utils.Utility;

/**
 * Created by rakhi
 * date: 19/12/2017
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DriverHolder>
{
    List<DeviceData.ResponseBean> devicelist;
    Context mcontext;
    int lastpositon=-1;
    String idd;
    private ClickListener clickListener;
    public interface ClickListener
    {
        void itemClicked(View view, int postion);
    }

    public DeviceAdapter(List<DeviceData.ResponseBean>devicelist, Context mcontext) {
        this.devicelist = devicelist;
        this.mcontext = mcontext;
    }

    @Override
    public DriverHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewrow,parent,false);
        return new DriverHolder(row);
    }

    @Override
    public void onBindViewHolder(DriverHolder holder, int position) {
        holder.dr_name.setText(devicelist.get(position).getDeviceid());
        String s = Utility.getAddress(mcontext, Double.parseDouble(devicelist.get(position).getLatitude()),
                Double.parseDouble(devicelist.get(position).getLongitude()));
        holder.txt_address.setText(s);
/*
        switch (devicelist.get(position).getType()){
            case "Truck":
                holder.dr_img.setBackgroundResource(R.drawable.ic_truck);
                break;
            case "Bus":
                holder.dr_img.setBackgroundResource(R.drawable.ic_bus);
                break;
            case "Car":
                holder.dr_img.setBackgroundResource(R.drawable.ic_car);
                break;
            case "Bike":
                holder.dr_img.setBackgroundResource(R.drawable.ic_bike);
                break;
            case "Humane":
                holder.dr_img.setBackgroundResource(R.drawable.ic_human);

                break;
        }
*/
      /*  if (position > 0) {
            holder.ar_img.setImageResource(R.drawable.ic_blue_arrow);
            holder.ar_img.setBackgroundColor(mcontext.getResources().getColor(R.color.Light_grey));
        }
           *//* Animation animation= AnimationUtils.loadAnimation(mcontext,(position>lastpositon)? R.anim.up_from_bottom:R.anim.bottom_from_up);
            holder.itemView.setAnimation(animation);
            lastpositon=position;*//*
           if (position>lastpositon) {
               AnimatioonUtils.animate(holder,true);
           }
           else
           {
               AnimatioonUtils.animate(holder,false);
           }
        lastpositon=position;*/
    }

    public void setClickListene(ClickListener clickListener)
    {
        this.clickListener=clickListener;
    }


    @Override
    public int getItemCount() {
        return devicelist.size();
    }

    class DriverHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dr_name,txt_address,dr_phnumber,dr_Age,dr_id;
        ImageView dr_img,ar_img;
        public DriverHolder(View itemView) {
            super(itemView);
            dr_name= (TextView) itemView.findViewById(R.id.devicename);
            dr_img = (ImageView) itemView.findViewById(R.id.imgdevice);

//            change by rakhi 02/07/2018 add address of device

            txt_address = itemView.findViewById(R.id.device_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null)
            {
                clickListener.itemClicked(itemView,getPosition());
            }
        }

      /*  @Override
        public void onClick(View v) {

            String name=dr_name.getText().toString();
            Toast.makeText(mcontext,""+name,Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onViewAttachedToWindow(DriverHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.itemView.clearAnimation();
    }

}
