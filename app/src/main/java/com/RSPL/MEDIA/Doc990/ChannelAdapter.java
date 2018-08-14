package com.RSPL.MEDIA.Doc990;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.RSPL.MEDIA.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/*
public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.MyViewHolder> {
    Context context;
    ArrayList<DoctorPojo> doctor;
    ArrayList<DoctorPojo.Hospital>hospitals;
    ArrayList<DoctorPojo.Hospital.Specialization> specializations;
    String docsearch,hospsearch,specsearch;
    String fromDate,toDate;
    public ChannelAdapter(Context context, ArrayList<DoctorPojo> doctor,String fromDate,String toDate) {
        this.context = context;
        this.doctor = doctor;
        this.fromDate=fromDate;
        this.toDate=toDate;
    }
    AppCompatActivity activity;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_channel_layout,parent,false);
        activity = (AppCompatActivity) view.getContext();
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DoctorPojo doctors = doctor.get(position);
        */
/* final DoctorPojo.Hospital hospitals = doctors.hospitals.get(position); */
/*  final DoctorPojo.Hospital.Specialization specializatio =*//*

        holder.docchannel.setText(doctors.getName());
        // holder.spechannel.setText(specializatio.getName());
        holder.channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<DoctorPojo.Hospital>  hossss =doctors.getHospitals();
                final ArrayList<DoctorPojo.Hospital.Specialization>specializations = new ArrayList<DoctorPojo.Hospital.Specialization>();
                for(int i=0;i<hossss.size();i++) {
                    Log.d("%%%%RC%%%%", String.valueOf(doctors.getHospitals().get(i).getSpecialization()).replace("[","").replace("]",""));
                    specializations.addAll(doctors.getHospitals().get(i).getSpecialization());
                }
             */
/*   String doctor, hospital1, specialization,specializationid;
                doctor =doctors.getName();
                hospital1 = hospitals.getName();
                specialization = specializatio.getName();
                specializationid= specializatio.getId();
                Log.d("*********", doctor + " " + hospital1 + " " + specialization);
                Bundle intent = new Bundle();
                intent.putString("DOCTOR", doctor);
                intent.putString("SPECIALIZATION", specialization);
                intent.putString("SPECIALIZATION_ID", specializationid);
                intent.putString("HOSPITAL", hospital1);
                intent.putString("DOCTOR_ID", doctors.getId());
                intent.putString("HOSPITAL_ID", hospitals.getId());
                intent.putString("FROM_DATE", fromDate);
                intent.putString("TO_DATE",toDate);
                Log.d("***&&(())******", doctors.getId() + " " + hospitals.getId());
                DoctorSessions docSessions = new DoctorSessions();
                docSessions.setArguments(intent);
                activity.getFragmentManager().beginTransaction().replace(R.id.linearlayouts, docSessions).addToBackStack("my_fragment").commit();*//*

            }
        });
    }
    @Override
    public int getItemCount() {
        return doctor.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView docchannel,spechannel;
        Button channel;
        public MyViewHolder(View itemView) {
            super(itemView);
            docchannel = (TextView)itemView.findViewById(R.id.docchannel);
            spechannel = (TextView)itemView.findViewById(R.id.specchannel);
            channel = (Button)itemView.findViewById(R.id.channelbutton);
        }
    }
}
*/
public class ChannelAdapter extends ExpandableRecyclerViewAdapter<DoctorHolder, SpecHolder> {
    AppCompatActivity activity;

    String title;
    String from;
    String to;
    String specid;
    String hospitalId;
    String id;
    String doctor, hospital1, specialization, specializationid;

    public ChannelAdapter(List<? extends ExpandableGroup> groups, String from, String to) {
        super(groups);
        this.from = from;
        this.to = to;
    }

    @Override
    public DoctorHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_doctor, parent, false);
        activity = (AppCompatActivity) view.getContext();
        return new DoctorHolder(view);
    }

    @Override
    public SpecHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_artist, parent, false);
        return new SpecHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final SpecHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final Specializations artist = ((Doctors) group).getItems().get(childIndex);

        holder.setArtistName(artist.getName());
        holder.setHospitalName(artist.getHospital());
        holder.channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doctor = artist.getDrname();
                if (artist.getDrname() == null) {
                    doctor = "0";
                }
                hospital1 = artist.getHospital();
                if (artist.getHospital() == null) {
                    hospital1 = "0";
                }

                specialization = artist.getName();
                if (artist.getName() == null) {
                    specialization = "0";
                }
                specializationid = artist.getSpecializationId();
                if (specializationid == null) {
                    specializationid = "0";
                }
                hospitalId = artist.getHospitalId();
                if (hospitalId == null) {
                    hospitalId = "0";
                }
                id = artist.getDrID();
                if (id == null) {
                    id = "0";
                }
                title = artist.isFavorite();

                Bundle intent = new Bundle();
                intent.putString("title", title);
                intent.putString("DOCTOR", doctor);
                intent.putString("SPECIALIZATION", specialization);
                intent.putString("SPECIALIZATION_ID", specializationid);
                intent.putString("HOSPITAL", hospital1);
                intent.putString("DOCTOR_ID", id);
                intent.putString("HOSPITAL_ID", hospitalId);
                intent.putString("FROM_DATE", from);
                intent.putString("TO_DATE", to);

                Log.d("&&&&&&&&", title + " " + doctor + specialization + specializationid + hospital1 + id + hospitalId + from + to);

                DoctorSessions docSessions = new DoctorSessions();
                docSessions.setArguments(intent);
                activity.getFragmentManager().beginTransaction().replace(R.id.linearlayouts, docSessions).addToBackStack("my_fragment").commit();

            }
        });
    }

    @Override
    public void onBindGroupViewHolder(DoctorHolder holder, int flatPosition,
                                      ExpandableGroup group) {

        holder.setGenreTitle(group);


    }
}
