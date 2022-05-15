package at.aau.se2.gamma.carcassonne.views.lobby;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.databinding.ActivityLobbyKickDialogBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.KickPlayerCommand;

public class LobbyActivityDialog extends AppCompatDialogFragment {
    public ActivityLobbyKickDialogBinding binding;
    private String playerToKick;
    private TextView tvKickPlayer;
    private Button btVoteYes;
    private Button btVoteNo;

    public LobbyActivityDialog(String playerToKick) {
        this.playerToKick = playerToKick;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_lobby_kick_dialog, null);
        dialogBuilder.setView(view)
                .setTitle("Vote Kick");

        tvKickPlayer = view.findViewById(R.id.tv_dialog_player_kick);
        btVoteNo = view.findViewById(R.id.btn_voteNo);
        btVoteYes = view.findViewById(R.id.btn_voteYes);

        tvKickPlayer.setText("Kick Player " + playerToKick + "?");

        btVoteYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerThread.instance.sendCommand(new KickPlayerCommand(playerToKick), new ServerThread.RequestResponseHandler() {
                    @Override
                    public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                        dismiss();
                        Toast.makeText(getContext(), "Vote sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                        dismiss();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btVoteNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialogBuilder.create();
    }
}
