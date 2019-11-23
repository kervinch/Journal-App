package ui;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.absolutely.self.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Journal;

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Journal> journalList;

    public JournalRecyclerAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public JournalRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.journal_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerAdapter.ViewHolder holder, int position) {
        final Journal journal = journalList.get(position);
        String imageUrl;

        holder.title.setText(journal.getTitle());
        holder.thoughts.setText(journal.getThoughts());
        holder.name.setText(journal.getUsername());
        imageUrl = journal.getUrl();
        Picasso.get().load(imageUrl).placeholder(R.drawable.journal_three).fit().into(holder.image);

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(journal.getTimestamp().getSeconds() * 1000);
        holder.dateCreated.setText(timeAgo);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra("title", journal.getTitle());
                sendIntent.putExtra("thoughts", journal.getThoughts());
                sendIntent.putExtra("image", journal.getUrl());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,thoughts, dateCreated, name;
        public ImageView image;
        public ImageButton share;
        String userId;
        String userName;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.journal_title_list);
            thoughts = itemView.findViewById(R.id.journal_thought_list);
            dateCreated = itemView.findViewById(R.id.journal_timestamp_list);
            name = itemView.findViewById(R.id.journal_row_username);
            image = itemView.findViewById(R.id.journal_image_list);
            share = itemView.findViewById(R.id.journal_row_share_button);
        }
    }
}
