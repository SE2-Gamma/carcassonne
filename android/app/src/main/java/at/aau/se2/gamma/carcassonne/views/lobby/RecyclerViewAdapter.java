package at.aau.se2.gamma.carcassonne.views.lobby;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.aau.se2.gamma.carcassonne.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<LobbyPlayerDisplay> players;
    private RecyclerViewListener recyclerViewListener;
    private Context context;

    public RecyclerViewAdapter(List<LobbyPlayerDisplay> players, Context context, RecyclerViewListener recyclerViewListener) {
        this.players = players;
        this.context = context;
        this.recyclerViewListener = recyclerViewListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lobby_player_display_cardview, parent, false);
        return new ViewHolder(v, recyclerViewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LobbyPlayerDisplay item = players.get(position);

        holder.tv_player_name.setText(item.getPlayerName());
        if(item.getPlayerState()){
            holder.itemView.setBackgroundResource(R.color.tertiary);
        }else{
            holder.itemView.setBackgroundResource(R.color.white);
        }

    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_player_name;
        public Button btn_kick_player;
        RecyclerViewListener recyclerViewListener;

        public ViewHolder(View itemView, RecyclerViewListener recyclerViewListener) {
            super(itemView);

            tv_player_name = itemView.findViewById(R.id.tv_player_name);
            btn_kick_player = itemView.findViewById(R.id.btn_kick_player);
            this.recyclerViewListener = recyclerViewListener;
            btn_kick_player.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View view) {
            recyclerViewListener.onKickPlayerButtonClick(getAdapterPosition());
        }
    }

    public interface RecyclerViewListener {
        void onKickPlayerButtonClick(int position);
    }
}
