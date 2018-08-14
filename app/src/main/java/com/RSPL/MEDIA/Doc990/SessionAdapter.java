package com.RSPL.MEDIA.Doc990;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.RSPL.MEDIA.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rspl-richa on 29/11/17.
 */

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MyViewHolder> {
    Context context;
    List<SessionPojo> session;

    public SessionAdapter(Context context, List<SessionPojo> session) {
        this.context = context;
        this.session = session;
    }

    AppCompatActivity activity;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_layout, parent, false);
        activity = (AppCompatActivity) view.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SessionPojo doctor = session.get(position);
        String str = doctor.getSessionAt();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*String dateInString = "2013-11-01 00:00:00";
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/
        try {
            Date date = formatter.parse(str);
            DateFormat out = new SimpleDateFormat("dd MMM yyyy");
            holder.date.setText(out.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] splitStr = str.split("\\s+");
        String time = splitStr[1];

        holder.time.setText(time);

        holder.nextpatient.setText(doctor.getNextPatient());
        if (doctor.getStatus().equals("1")) {
            holder.status.setText("AVAILABLE");
            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = doctor.getId();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", id);
                    SessionDetails sessionDetails = new SessionDetails();
                    sessionDetails.setArguments(bundle);
                    activity.getFragmentManager().beginTransaction().replace(R.id.linearlayouts, sessionDetails).addToBackStack("my_fragmn").commit();
                }
            });
        }
        if (doctor.getStatus().equals("2")) {
            holder.status.setText("STARTED");
        }
        if (doctor.getStatus().equals("3")) {
            holder.status.setText("ENDED");

        }
        if (doctor.getStatus().equals("4")) {
            holder.status.setText("CLOSED");

        }
        if (doctor.getStatus().equals("5")) {
            holder.status.setText("HOLD");

        }
        if (doctor.getStatus().equals("6")) {
            holder.status.setText("SESSION FULL");

        }
        if (doctor.getStatus().equals("7")) {
            holder.status.setText("HOLIDAY");

        }
        if (doctor.getStatus().equals("8")) {
            holder.status.setText("CANCELLED");
        }

        if (doctor.getcanBook().equals("true")) {
            holder.book.setEnabled(true);
        } else {
            holder.book.setEnabled(false);
            holder.book.setBackgroundColor(0xffcccccc);//0x0000FF00 green  0x15134720 transparent
        }

    }

    @Override
    public int getItemCount() {
        return session.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, nextpatient, status;
        Button book;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            nextpatient = (TextView) itemView.findViewById(R.id.nextpatient);
            status = (TextView) itemView.findViewById(R.id.status);
            book = (Button) itemView.findViewById(R.id.book);
        }

    }
}
