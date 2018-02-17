package meenakshinagarajan.example.com.icsaugmentationtool;

/**
 * Created by meenakshinagarajan on 2/5/18.
 */

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info_window, null);
        ViewHolder holder;
        holder=new ViewHolder();
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        holder.name_tv = view.findViewById(R.id.title);
        holder.details_tv = view.findViewById(R.id.details);
        holder.exhibits = view.findViewById(R.id.exhibits);
        holder.mitigations = view.findViewById(R.id.mitigations);
        holder.label1 = view.findViewById(R.id.label1);

        String [] keywordsred={"Vehicular accident","Break","Bruise","Cuts","Air bag","Seat belt","Unconsciousness"};
        String [] keywordsblue={"Sodium Borohydride","Redness","Blistering","Scaling","Wheezing","Gloves", "Boots", "Self contained breathing apparatus", "Splash goggles"};
        String [] keywordsorange={"Ricin Toxin","Pulmonary Edema"};

        holder.details_tv.setText(infoWindowData.getDetails());
        holder.exhibits.setText(infoWindowData.getExhibits());
        holder.mitigations.setText(infoWindowData.getMitigations());

        if (holder.exhibits.getText().toString()==""){
            holder.label1.setText("Susceptible to:");
            holder.exhibits.setText("No symptoms");
            holder.mitigations.setText("NA");
        }

        for(String y:keywordsred)
        {
            Spannable raw=new SpannableString(holder.details_tv.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFF8B008B), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.details_tv.setText(raw);
        }
        for(String y:keywordsblue)
        {
            Spannable raw=new SpannableString(holder.details_tv.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFF00056f), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.details_tv.setText(raw);
        }
        for(String y:keywordsorange)
        {
            Spannable raw=new SpannableString(holder.details_tv.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFFEE7600), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.details_tv.setText(raw);
        }

        for(String y:keywordsred)
        {
            Spannable raw=new SpannableString(holder.exhibits.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFF8B008B), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.exhibits.setText(raw);
        }
        for(String y:keywordsblue)
        {
            Spannable raw=new SpannableString(holder.exhibits.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFF00056f), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.exhibits.setText(raw);
        }
        for(String y:keywordsorange)
        {
            Spannable raw=new SpannableString(holder.exhibits.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFFEE7600), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.exhibits.setText(raw);
        }
        for(String y:keywordsred)
        {
            Spannable raw=new SpannableString(holder.mitigations.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFF8B008B), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.mitigations.setText(raw);
        }
        for(String y:keywordsblue)
        {
            Spannable raw=new SpannableString(holder.mitigations.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFF00056f), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.mitigations.setText(raw);
        }
        for(String y:keywordsorange)
        {
            Spannable raw=new SpannableString(holder.mitigations.getText());
            int index= TextUtils.indexOf(raw, y);
            while (index >= 0)
            {
                raw.setSpan(new ForegroundColorSpan(0xFFEE7600), index, index + y.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                index= TextUtils.indexOf(raw, y, index + y.length());
            }
            holder.mitigations.setText(raw);
        }


        holder.name_tv.setText(marker.getTitle());




        return view;
    }
    private static class ViewHolder {
        TextView name_tv;
        TextView details_tv;
        TextView exhibits;
        TextView mitigations;
        TextView label1;
    }

}
