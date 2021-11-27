package com.example.rush.View.fragments.classes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rush.Model.ClassInfo;
import com.example.rush.Model.Member;
import com.example.rush.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class ClassDetailsFragment extends Fragment {
    private String name, instructor, description, id, userID, professor, professorID;
    private FirebaseFirestore database;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RecyclerView recycle;
    private DetailsAdapter adapter;
    private ArrayList<Member> students;

    public ClassDetailsFragment() {

    }

    public ClassDetailsFragment(String name, String instructor, String description, String id) {
        this.name = name;
        this.instructor = instructor;
        this.description = description;
        this.id = id;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_details, container, false);
        students = new ArrayList<>();
        recycle = (RecyclerView) view.findViewById(R.id.studentsInClass);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recycle.setLayoutManager(manager);
        recycle.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        database.collection("classes").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    ClassInfo obj = doc.toObject(ClassInfo.class);
                    professor = obj.getInstructor();
                    professorID = obj.getCreatedBy();
                    Member p = new Member(professor, professorID);
                    students.add(p);
                    adapter = new DetailsAdapter(students);
                    recycle.setAdapter(adapter);
                }
            }
        });

        database.collection("classes").document(id).collection("Students").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String) document.getData().get("Name");
                                String studentID = (String) document.getData().get("ID");
                                String studentEmail = (String) document.getData().get("Email");

                                Member m = new Member(name, studentID);
                                students.add(m);
                                adapter = new DetailsAdapter(students);
                                recycle.setAdapter(adapter);

                            }

                        }
                    }
                });
        return view;
    }

    public class DetailsAdapter extends RecyclerView.Adapter<ClassDetailsFragment.DetailsAdapter.ViewHolder> {
        private ArrayList<Member> students;

        public DetailsAdapter(ArrayList<Member> students) {
            this.students = students;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.students_list,
                    parent, false);
            return new ClassDetailsFragment.DetailsAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Member obj = students.get(position);
            holder.studentName.setText(obj.getName());

            if (obj.getName().equals(professor) && obj.getUid().equals(professorID)) {
                holder.status.setVisibility(View.VISIBLE);
            } else {
                holder.status.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            if (students != null) {
                return students.size();
            } else {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View view;
            private TextView studentName;
            private ImageView status;


            public ViewHolder(View view) {
                super(view);
                this.view = view;
                studentName = view.findViewById(R.id.nameOfStudent);
                status = view.findViewById(R.id.professorStatus);
            }
        }
    }
}