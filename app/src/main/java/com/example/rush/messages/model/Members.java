package com.example.rush.messages.model;

import android.util.Log;

import java.util.ArrayList;

public class Members {

    ArrayList<Member> members;

    public Members(){
        members = new ArrayList<>();
    }

    public Members(ArrayList<Member> members) {
        this.members = members;
    }

    public ArrayList<Member> getAllMembers() {
        return members;
    }

    public Member getMember(int index) {
        return members.get(index);
    }


    public void addMembers(Member member) {
        this.members.add(member);
    }

    public String getAllMembersName() {
        String allMembers = "";
        for (Member member : this.members) {
            allMembers += member.getName() + " ";
        }
        return allMembers;
    }


}
