package com.example.b07_course_selection_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07_course_selection_project.Course.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Course> courseModel;
    private List<Course> filteredData;
    public CourseAdapter(Context context, List<Course> courseModel){
        this.context = context;
        this.courseModel = courseModel;
        this.filteredData = courseModel;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charSequence = charSequence.toString().toUpperCase(Locale.ROOT);
                FilterResults results = new FilterResults();
                if(charSequence == null || charSequence.length() == 0){
                    results.values = courseModel;
                    results.count = courseModel.size();
                }
                else{
                    List<Course> filterResultsData = new ArrayList<Course>();
                    for(Course i: courseModel){
                        if(i.getCode().contains(charSequence)){
                            filterResultsData.add(i);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredData = (ArrayList<Course>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder{
        protected TextView code, sessions, prereq;
    }
    @Override
    public int getViewTypeCount(){
        return getCount();
    }
    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null, true);
            holder.code = (TextView) view.findViewById(R.id.code);
            holder.prereq = (TextView) view.findViewById(R.id.prereq);
            holder.sessions = (TextView) view.findViewById(R.id.sessions);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.code.setText(filteredData.get(i).getCode());
        holder.sessions.setText(filteredData.get(i).getSessionString());
        holder.prereq.setText(filteredData.get(i).getPrereqString());
        return view;
    }
}

