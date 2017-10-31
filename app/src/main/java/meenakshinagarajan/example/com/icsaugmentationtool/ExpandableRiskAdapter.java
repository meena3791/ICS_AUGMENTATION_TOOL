package meenakshinagarajan.example.com.icsaugmentationtool;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by meenakshinagarajan on 10/26/17.
 */

public class ExpandableRiskAdapter extends RecyclerView.Adapter<ExpandableRiskAdapter.ViewHolder> {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    public ExpandableRiskAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData){
        this._context = context;
        this._listDataHeader = listDataHeader;
        Log.d("listDataHeader",listDataHeader.toString());
        this._listDataChild = listChildData;
        Log.d("listDataChild",listChildData.toString());

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.risk_details_list_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("Hash map size", ((String.valueOf(_listDataChild.values().size()))));
        Log.d("Hash map values", _listDataChild.values().toString());
        Log.d("Header values", _listDataHeader.toString());
        JSONObject jsonObj = null;

        if (position == 0) {
            String key = _listDataHeader.get(0);
           // if (key.contains("Vehicular Accident")) {
                List<String> values = (List<String>) _listDataChild.get(key);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                holder.row.setLayoutParams(lp);
                for (int i = 0; i < values.size(); i++) {
                    Log.d("values", values.get(i));
                    holder.row = new TableRow(holder.itemView.getContext());
                    holder.textView = new TextView(holder.itemView.getContext());
                    holder.textView1 = new TextView(holder.itemView.getContext());
                    holder.textViewHeader.setText(key);
                    if (values.get(i).contains("can")) {
                        holder.textView.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.textView1.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.textView.setText(values.get(i));
                        if(key=="Vehicular Accident"){
                            holder.textView1.setText("Symptoms");
                        }else if(key=="Chemical Truck"){
                            holder.textView1.setText("Chemicals");
                        }else if(key=="EMT"){
                            holder.textView1.setText("Symptoms");
                        }

                    } else if (values.get(i).contains("mitigatedBy")) {
                        holder.textView.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.textView1.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.textView.setText(values.get(i));
                        holder.textView1.setText("Protection Equipment");
                    } else if (values.get(i).contains("susceptibleTo")) {
                        holder.textView.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.textView1.setTypeface(Typeface.DEFAULT_BOLD);
                        holder.textView.setText(values.get(i));
                        holder.textView1.setText("Hazard");
                    }  else {
                        holder.textView.setTypeface(Typeface.DEFAULT);
                        holder.textView1.setTypeface(Typeface.DEFAULT);
                        holder.textView.setText(" ");
                        holder.textView1.setText(values.get(i));
                    }
                    holder.tableLayout.addView(holder.row);
                    holder.row.addView(holder.textView);
                    holder.row.addView(holder.textView1);
                }

            //}

        }
       if (position == 1) {
           String key = _listDataHeader.get(1);
           List<String> values = (List<String>) _listDataChild.get(key);
           TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
           holder.row.setLayoutParams(lp);
            for(int i=0;i<values.size();i++){
                Log.d("values", values.get(i));
                holder.row = new TableRow(holder.itemView.getContext());
                holder.textView = new TextView(holder.itemView.getContext());
                holder.textView1 = new TextView(holder.itemView.getContext());
                holder.textViewHeader.setText(key);
                if (values.get(i).contains("Cause")) {
                    holder.textView.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.textView1.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.textView.setText(values.get(i));
                    if (key.contains("Chemicals")) {
                        holder.textView1.setText("chemicalHazard");
                    }
                }else {
                    holder.textView.setTypeface(Typeface.DEFAULT);
                    holder.textView1.setTypeface(Typeface.DEFAULT);
                    holder.textView.setText(" ");
                    holder.textView1.setText(values.get(i));
                }
                holder.tableLayout.addView(holder.row);
                holder.row.addView(holder.textView);
                holder.row.addView(holder.textView1);
                }

           // }

        }
        if (position == 2) {
            String key = _listDataHeader.get(2);
            // if (key.contains("Vehicular Accident")) {
            List<String> values = (List<String>) _listDataChild.get(key);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            holder.row.setLayoutParams(lp);
            for (int i = 0; i < values.size(); i++) {
                Log.d("values", values.get(i));
                holder.row = new TableRow(holder.itemView.getContext());
                holder.textView = new TextView(holder.itemView.getContext());
                holder.textView1 = new TextView(holder.itemView.getContext());
                holder.textViewHeader.setText(key);
                if (values.get(i).contains("can")) {
                    holder.textView.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.textView1.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.textView.setText(values.get(i));
                    if(key=="Vehicular Accident"){
                        holder.textView1.setText("Symptoms");
                    }else if(key=="Chemical Hazard"){
                        holder.textView1.setText("Symptoms");
                    }
                }  else {
                    holder.textView.setTypeface(Typeface.DEFAULT);
                    holder.textView1.setTypeface(Typeface.DEFAULT);
                    holder.textView.setText(" ");
                    holder.textView1.setText(values.get(i));
                }
                holder.tableLayout.addView(holder.row);
                holder.row.addView(holder.textView);
                holder.row.addView(holder.textView1);
            }

            //}

        }

    }


    @Override
    public int getItemCount() {
        Log.d("Hash map size", (String.valueOf(_listDataChild.size()) ));
        return _listDataChild.size();

    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView;
        protected TextView textView1;
        //protected TextView textView2;
        protected TextView textViewHeader;
        protected ToggleButton togglebutton;
        protected CardView cardView;
        protected TableLayout tableLayout;
        protected TableRow row;
        //protected ScrollView scroll;
        protected int minHeight;
        public ViewHolder(View itemView) {
            super(itemView);

            textViewHeader =  (TextView) itemView.findViewById(R.id.risk_list_item_header);
            cardView = (CardView)  itemView.findViewById(R.id.cardView);
            togglebutton = (ToggleButton) itemView.findViewById(R.id.toggleButton);
            tableLayout = (TableLayout)  itemView.findViewById(R.id.displayLinear);
            row=(TableRow)itemView.findViewById(R.id.display_row);
            minHeight = cardView.getMinimumHeight();
            TextView textView = (TextView) itemView.findViewById(R.id.risk_list_item1);
            TextView textView1 = (TextView) itemView.findViewById(R.id.risk_list_item2);


            togglebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        collapseView();
                    }else{
                        expandView((CardView.LayoutParams.WRAP_CONTENT));
                    }
                }
            });


        }

        public void expandView(int height) {

            ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                    height);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = val;
                    cardView.setLayoutParams(layoutParams);
                }
            });
            anim.start();

        }
        public void collapseView() {

            ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                    minHeight);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    layoutParams.height = val;
                    cardView.setLayoutParams(layoutParams);

                }
            });
            anim.start();
        }
    }



}
