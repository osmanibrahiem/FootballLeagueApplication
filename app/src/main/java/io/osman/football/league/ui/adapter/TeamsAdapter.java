package io.osman.football.league.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.osman.football.league.databinding.ItemTeamBinding;
import io.osman.football.league.helper.CompetitionUtils;
import io.osman.football.league.helper.ConstantUtils;
import io.osman.football.league.model.Team;
import io.osman.football.league.ui.listener.OnRecyclerItemClickedListener;
import io.osman.football.league.ui.view.TeamsListFragment;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamViewHolder> {

    // The context we use to utility methods, app resources and layout inflaters
    private Context context;
    // The List of teams that setter to views
    private List<Team> teams;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our CompetitionAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    private OnRecyclerItemClickedListener<Team> listener;

    /**
     * Creates a TeamsAdapter.
     *
     * @param context Used to talk to the UI and app resources
     */
    public TeamsAdapter(Context context) {
        this.context = context;
        this.teams = new ArrayList<>();
    }

    /**
     * Creates a TeamsAdapter.
     *
     * @param listener The on-click handler for this adapter. This single handler is called
     *                 when an item is clicked.
     */
    public TeamsAdapter setOnRecyclerItemClickedListener(OnRecyclerItemClickedListener<Team> listener) {
        this.listener = listener;
        return this;
    }

    /**
     * set the list used by the TeamsAdapter for its weather data. This method is called by
     * {@link TeamsListFragment} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param teams the new list of forecasts to use as TeamsAdapter's data source
     */
    public TeamsAdapter setTeams(List<Team> teams) {
        this.teams.clear();
        this.teams.addAll(teams);
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
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeamBinding binding = ItemTeamBinding.inflate(LayoutInflater.from(context), parent, false);
        return new TeamViewHolder(binding);
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
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.bindTo(teams.get(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return teams.size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class TeamViewHolder extends RecyclerView.ViewHolder {

        private ItemTeamBinding binding;

        TeamViewHolder(@NonNull ItemTeamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(Team team) {
            String teamName = "";
            if (!TextUtils.isEmpty(team.getName())) {
                teamName += team.getName();
                if (!TextUtils.isEmpty(team.getTla()))
                    teamName += " (" + team.getTla() + ")";
            } else if (!TextUtils.isEmpty(team.getShortName())) {
                teamName += team.getShortName();
                if (!TextUtils.isEmpty(team.getTla()))
                    teamName += " (" + team.getTla() + ")";
            } else
                teamName += team.getTla();
            binding.competitionName.setText(teamName);
            binding.competitionCountry.setText(team.getArea().getName());
            if (!TextUtils.isEmpty(team.getCrestUrl()))
                ConstantUtils.loadImage(context, team.getCrestUrl(), binding.competitionCrest);

            binding.rootView.setCardBackgroundColor(context.getResources().getColor(CompetitionUtils.getColorResourceId(team.getCompetitionId())));


            /**
             * This gets called by the child views during a click. We fetch the date that has been
             * selected, and then call the onClick handler registered with this adapter, passing that
             * date.
             *
             * @param v the View that was clicked
             */
            binding.rootView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onClick(team);
            });
        }
    }
}
