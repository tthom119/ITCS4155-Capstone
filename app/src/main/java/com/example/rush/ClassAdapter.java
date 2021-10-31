package com.example.rush;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private ArrayList<ClassInfo> classList;

    public ClassAdapter(ArrayList<ClassInfo> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.class_items,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassInfo classObj = classList.get(position);
        holder.className.setText(String.format("Class: %s", classObj.getClassName()));
        holder.instructorName.setText(String.format("Instructor: %s", classObj.getInstructor()));
        holder.classDescription.setText(String.format("Class Description: %s", classObj.getDescription()));
    }

    @Override
    public int getItemCount() {
        if (classList != null) {
            return classList.size();
        } else {
            return 0;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public TextView className;
        public TextView instructorName;
        public TextView classDescription;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            className = view.findViewById(R.id.classNameText);
            instructorName = view.findViewById(R.id.classInstructorName);
            classDescription = view.findViewById(R.id.classDescriptionText);
        }

    }
}
