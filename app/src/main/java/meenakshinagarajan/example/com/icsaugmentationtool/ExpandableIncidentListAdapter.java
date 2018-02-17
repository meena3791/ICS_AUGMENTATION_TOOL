package meenakshinagarajan.example.com.icsaugmentationtool;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by meenakshinagarajan on 10/25/17.
 */

public class ExpandableIncidentListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private int flag;



    public ExpandableIncidentListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);

    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        Log.d("childText",childText);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.incident_list_item, null);
            holder=new ViewHolder();
            holder.txtListChild=(TextView)convertView.findViewById(R.id.lblIncidentListItem);
            holder.txtListDateChild=(TextView)convertView.findViewById(R.id.supportingDetails);
            holder.progressbar=(ProgressBar)convertView.findViewById(R.id.progress_view);
            holder.progressbarinsidetext = (TextView) convertView.findViewById(R.id.progressBarinsideText);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }


        //TextView txtListChild = (TextView) convertView
              //  .findViewById(R.id.lblIncidentListItem);
        holder.txtListChild.setText(childText);

        if(childText.contains("Ricin Toxin") ){
            flag = 1;
            Log.d("Flag Value",String.valueOf(flag));

        }

        holder.txtListChild.setTextColor(Color.DKGRAY);

       if(groupPosition==0&&childPosition==0){
           holder.txtListDateChild.setText("Chemical truck, " +
                                        "Car, " +
                                        "Police Vehicle");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.INVISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(0);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText(" ");
           holder.progressbarinsidetext.setTextColor(Color.RED);
        }
        else if(groupPosition==0&&childPosition==1){
           holder.txtListDateChild.setText("Chemical Hazard");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.INVISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(45);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText("45% OF SYMPTOMS MATCHES WITH THE MATERIAL");


           holder.progressbarinsidetext.setTextColor(Color.RED);
        }
        else if(groupPosition==0&&childPosition==2){
           holder.txtListDateChild.setText("Chemical Hazard");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.INVISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(0);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText(" ");
           holder.progressbarinsidetext.setTextColor(Color.RED);
           holder.progressbarinsidetext.setText("60% OF SYMPTOMS MATCHES WITH THE MATERIAL");

       }
       else if(groupPosition==1&&childPosition==0){
           holder.txtListDateChild.setText("Truck Driver");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.VISIBLE);
           holder.progressbar.getLayoutParams().width=350;
           holder.progressbar.setProgress(100);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText("EXPOSED TO ACCIDENT AND SODIUM");
           holder.progressbarinsidetext.setTextColor(Color.WHITE);
        }
        else if(groupPosition==1&&childPosition==1){
           holder.txtListDateChild.setText("Car Driver");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.VISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(100);
           /*progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           progressbarinsidetext.setText("IMMEDIATE");*/
           if(Objects.equals(String.valueOf(flag), "1")){
               holder.progressbarinsidetext.setText("EXPOSED TO ACCIDENT, SODIUM AND RICIN TOXIN");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=450;
           }
           else{
               holder.progressbarinsidetext.setText("EXPOSED TO ACCIDENT AND SODIUM");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=350;
           }
           holder.progressbarinsidetext.setTextColor(Color.WHITE);
        }
        else if(groupPosition==1&&childPosition==2){
           holder.txtListDateChild.setText("Car Passenger");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.VISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(100);

           if(Objects.equals(String.valueOf(flag), "1")){
               holder.progressbarinsidetext.setText("EXPOSED TO ACCIDENT, SODIUM AND RICIN TOXIN");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=450;
           }
           else{
               holder.progressbarinsidetext.setText("EXPOSED TO ACCIDENT AND SODIUM");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=350;
           }
           holder.progressbarinsidetext.setTextColor(Color.WHITE);

        }
        else if((groupPosition==4&&childPosition==0)){

           if(Objects.equals(String.valueOf(flag), "1")){
               holder.txtListDateChild.setText("Ricin Toxin");
               holder.txtListDateChild.setTextColor(Color.GRAY);;
               holder.progressbar.setVisibility(View.INVISIBLE);
               holder.progressbar.getLayoutParams().width=200;
               holder.progressbar.setProgress(0);
               holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
               holder.progressbarinsidetext.setText(" ");
               holder.progressbarinsidetext.setTextColor(Color.RED);
           }else{
               holder.txtListDateChild.setText("Vehicular Accident");
               holder.txtListDateChild.setTextColor(Color.GRAY);;
               holder.progressbar.setVisibility(View.INVISIBLE);
               holder.progressbar.getLayoutParams().width=200;
               holder.progressbar.setProgress(0);
               holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
               holder.progressbarinsidetext.setText(" ");
               holder.progressbarinsidetext.setTextColor(Color.RED);
           }

       }
       else if((groupPosition==4&&childPosition==1)){
           if(Objects.equals(String.valueOf(flag), "1")){
               holder.txtListDateChild.setText("Vehicular Accident");
               holder.txtListDateChild.setTextColor(Color.GRAY);;
               holder.progressbar.setVisibility(View.INVISIBLE);
               holder.progressbar.getLayoutParams().width=200;
               holder.progressbar.setProgress(0);
               holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
               holder.progressbarinsidetext.setText(" ");
               holder.progressbarinsidetext.setTextColor(Color.RED);
           }else{
               holder.txtListDateChild.setText("Sodium Borohydride");
               holder.txtListDateChild.setTextColor(Color.GRAY);
               holder.progressbar.setVisibility(View.INVISIBLE);
               holder.txtListChild.getLayoutParams().height=80;
               holder.progressbar.getLayoutParams().width=200;
               holder.progressbar.setProgress(0);
               holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
               holder.progressbarinsidetext.setText(" ");
               holder.progressbarinsidetext.setTextColor(Color.RED);
           }

        }
       else if((groupPosition==4&&childPosition==2)){
           holder.txtListDateChild.setText("Sodium Borohydride");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.INVISIBLE);
           holder.txtListChild.getLayoutParams().height=80;
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(0);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText(" ");
           holder.progressbarinsidetext.setTextColor(Color.RED);
       }
       else if((groupPosition==2&&childPosition==0)){
           holder.txtListDateChild.setText(" ");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.VISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(100);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText("EXPOSED TO SODIUM");
           holder.progressbarinsidetext.setTextColor(Color.BLACK);
       }
       else if((groupPosition==2&&childPosition==1)){
           holder.txtListDateChild.setText(" ");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.VISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(100);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText("EXPOSED TO SODIUM");
           holder.progressbarinsidetext.setTextColor(Color.BLACK);
       }
       else if((groupPosition==2&&childPosition==2)){
           Log.d("Flag Value",String.valueOf(flag));
           holder.txtListDateChild.setText(" ");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.VISIBLE);

           holder.progressbar.setProgress(100);
           if(Objects.equals(String.valueOf(flag), "1")){
               holder.progressbarinsidetext.setText("SUSCEPTIBLE TO SODIUM AND RICIN EXPOSURE");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=450;
           }
           else{
               holder.progressbarinsidetext.setText("SUSCEPTIBLE TO SODIUM EXPOSURE");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=350;
           }

           holder.progressbarinsidetext.setTextColor(Color.BLACK);
       }
       else if((groupPosition==2&&childPosition==3)){
           Log.d("Flag Value inside if",String.valueOf(flag));
           holder.txtListDateChild.setText(" ");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.VISIBLE);
           holder.progressbar.setProgress(100);
           if(Objects.equals(String.valueOf(flag), "1")){
               holder.progressbarinsidetext.setText("SUSCEPTIBLE TO SODIUM AND RICIN EXPOSURE");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=450;
           }
           else{
               holder.progressbarinsidetext.setText("SUSCEPTIBLE TO SODIUM EXPOSURE");
               holder.progressbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
               holder.progressbar.getLayoutParams().width=350;
           }
           holder.progressbarinsidetext.setTextColor(Color.BLACK);
       }
        else if((groupPosition==3&&childPosition==0)){
           holder.txtListDateChild.setText(" ");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.INVISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(0);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText(" ");
           holder.progressbarinsidetext.setTextColor(Color.RED);
        }else if((groupPosition==3&&childPosition==1))     {
           holder.txtListDateChild.setText(" ");
           holder.txtListDateChild.setTextColor(Color.GRAY);
           holder.progressbar.setVisibility(View.INVISIBLE);
           holder.progressbar.getLayoutParams().width=200;
           holder.progressbar.setProgress(0);
           holder.progressbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
           holder.progressbarinsidetext.setText(" ");
           holder.progressbarinsidetext.setTextColor(Color.RED);
       }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        ViewHolder holder;
        String headerTitle = (String) getGroup(groupPosition);
        Log.d("headerTitle",headerTitle);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.incident_list_group, null);
            holder=new ViewHolder();
            holder.lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblIncidentListHeader);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }


        holder.lblListHeader.setTypeface(null, Typeface.BOLD);
        holder.lblListHeader.setText(headerTitle);
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private static class ViewHolder {
        TextView txtListChild;
        TextView txtListDateChild;
        ProgressBar progressbar;
        TextView progressbarinsidetext;
        TextView lblListHeader;
    }
}

