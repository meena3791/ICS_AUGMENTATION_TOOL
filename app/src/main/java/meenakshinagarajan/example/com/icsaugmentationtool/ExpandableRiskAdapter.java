package meenakshinagarajan.example.com.icsaugmentationtool;

import android.animation.ValueAnimator;
import android.content.Context;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        this._listDataChild = listChildData;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.risk_details_list_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("Hash map size", ((String.valueOf( _listDataChild.values().size()))));
        Log.d("Hash map values",_listDataChild.values().toString());
        for (Map.Entry<String, List<String>> entry : _listDataChild.entrySet()) {
            String key = entry.getKey();
            holder.textViewHeader.setText(key);
            List<String> values = entry.getValue();
            for(int i=0;i<values.size();i++){
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                holder.row.setLayoutParams(lp);
                holder.row= new TableRow(holder.itemView.getContext());
                //TextView textView =  (TextView) holder.row.findViewById(R.id.risk_list_item1);
                //TextView textView1 =  (TextView) holder.row.findViewById(R.id.risk_list_item2);
                holder.textView = new TextView(holder.itemView.getContext());
                holder.textView1 = new TextView(holder.itemView.getContext());
                    if(values.get(i).contains("can")){
                        Log.d("Values",values.get(i));
                        holder.textView.setText(values.get(i));
                        holder.textView1.setText("Symptoms");
                    }else{
                        holder.textView.setText(" ");
                        holder.textView1.setText(values.get(i));
                    }
                    holder.tableLayout.addView(holder.row);
                    holder.row.addView(holder.textView);
                    holder.row.addView(holder.textView1);


                }

            }

    }


    @Override
    public int getItemCount() {
        //Log.d("Hash map size", (String.valueOf(_listDataChild.size()) ));
        return _listDataChild.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView;
        protected TextView textView1;
        protected TextView textViewHeader;
        protected ToggleButton togglebutton;
        protected CardView cardView;
        protected TableLayout tableLayout;
        protected TableRow row;
        protected int minHeight;
        public ViewHolder(View itemView) {
            super(itemView);

            textViewHeader =  (TextView) itemView.findViewById(R.id.risk_list_item_header);
            cardView = (CardView)  itemView.findViewById(R.id.cardView);
            togglebutton = (ToggleButton) itemView.findViewById(R.id.toggleButton);
            tableLayout = (TableLayout)  itemView.findViewById(R.id.displayLinear);
            row=(TableRow)itemView.findViewById(R.id.display_row);
            TextView textView =  (TextView) row.findViewById(R.id.risk_list_item1);
            TextView textView1 =  (TextView) row.findViewById(R.id.risk_list_item2);

            minHeight = cardView.getMinimumHeight();


            togglebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        collapseView();
                    }else{
                        expandView(240);
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
