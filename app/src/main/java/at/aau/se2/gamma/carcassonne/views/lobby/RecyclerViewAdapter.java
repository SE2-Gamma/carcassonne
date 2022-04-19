package at.aau.se2.gamma.carcassonne.views.lobby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.aau.se2.gamma.carcassonne.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<LobbyPlayerDisplay> players;
    private Context context;

    public RecyclerViewAdapter(List<LobbyPlayerDisplay> players, Context context) {
        this.players = players;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lobby_player_display_cardview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LobbyPlayerDisplay item = players.get(position);

        holder.tv_player_name.setText(item.getPlayerName());
        holder.btn_kick_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), item.getPlayerName() + " kicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_player_name;
        public Button btn_kick_player;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_player_name = itemView.findViewById(R.id.tv_player_name);
            btn_kick_player = itemView.findViewById(R.id.btn_kick_player);
        }
    }
}
