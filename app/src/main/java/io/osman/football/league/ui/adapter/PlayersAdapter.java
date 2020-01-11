package io.osman.football.league.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.osman.football.league.databinding.ItemPlayerBinding;
import io.osman.football.league.model.Player;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder> {

    // The context we use to utility methods, app resources and layout inflaters
    private Context context;
    // The List of players that setter to views
    private List<Player> players;


    /**
     * Creates a PlayersAdapter.
     *
     * @param context Used to talk to the UI and app resources
     */
    public PlayersAdapter(Context context) {
        this.context = context;
        this.players = new ArrayList<>();
    }

    /**
     * set the list used by the CompetitionsAdapter for its weather data. This method is called by
     * {@link TeamInformationFragment} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param players the new list of forecasts to use as CompetitionsAdapter's data source
     */
    public PlayersAdapter setPlayers(List<Player> players) {
        this.players.clear();
        this.players.addAll(players);
        notifyDataSetChanged();
        return this;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item (like ours does) you
     *                 can use this viewType integer to provide a different layout. See
     *                 {@link RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details.
     * @return A new CompetitionAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlayerBinding binding = ItemPlayerBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PlayerViewHolder(binding);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        holder.binding.setPlayer(players.get(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return players.size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class PlayerViewHolder extends RecyclerView.ViewHolder {

        private ItemPlayerBinding binding;

        PlayerViewHolder(@NonNull ItemPlayerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
