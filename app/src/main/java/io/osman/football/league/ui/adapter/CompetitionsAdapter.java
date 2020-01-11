package io.osman.football.league.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.osman.football.league.databinding.ItemCompetitionBinding;
import io.osman.football.league.helper.ConstantUtils;
import io.osman.football.league.model.Competition;
import io.osman.football.league.ui.listener.OnRecyclerItemClickedListener;
import io.osman.football.league.ui.view.CompetitionsListFragment;


public class CompetitionsAdapter extends RecyclerView.Adapter<CompetitionsAdapter.CompetitionViewHolder> {

    // The context we use to utility methods, app resources and layout inflaters
    private Context context;
    // The List of competitions that setter to views
    private List<Competition> competitions;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our CompetitionAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    private OnRecyclerItemClickedListener<Competition> listener;

    /**
     * Creates a CompetitionAdapter.
     *
     * @param context Used to talk to the UI and app resources
     */
    public CompetitionsAdapter(Context context) {
        this.context = context;
        this.competitions = new ArrayList<>();
    }

    /**
     * Creates a CompetitionsAdapter.
     *
     * @param listener The on-click handler for this adapter. This single handler is called
     *                 when an item is clicked.
     */
    public CompetitionsAdapter setOnRecyclerItemClickedListener(OnRecyclerItemClickedListener<Competition> listener) {
        this.listener = listener;
        return this;
    }

    /**
     * set the list used by the CompetitionsAdapter for its weather data. This method is called by
     * {@link CompetitionsListFragment} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param competitions the new list of forecasts to use as CompetitionsAdapter's data source
     */
    public CompetitionsAdapter setCompetitions(List<Competition> competitions) {
        this.competitions.clear();
        this.competitions.addAll(competitions);
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
    public CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCompetitionBinding binding = ItemCompetitionBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CompetitionViewHolder(binding);
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
    public void onBindViewHolder(@NonNull CompetitionViewHolder holder, int position) {
        holder.bindTo(competitions.get(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return competitions.size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class CompetitionViewHolder extends RecyclerView.ViewHolder {

        private ItemCompetitionBinding binding;

        CompetitionViewHolder(@NonNull ItemCompetitionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(Competition competition) {
            String competitionName = "";
            if (!TextUtils.isEmpty(competition.getName())) {
                competitionName += competition.getName();
                if (!TextUtils.isEmpty(competition.getCode()))
                    competitionName += " (" + competition.getCode() + ")";
            }
            binding.competitionName.setText(competitionName);
            binding.competitionCountry.setText(competition.getArea().getName());
            if (!TextUtils.isEmpty(competition.getEmblemUrl()))
                ConstantUtils.loadImage(context, competition.getEmblemUrl(), binding.competitionCrest);

            binding.rootView.setCardBackgroundColor(context.getResources().getColor(competition.getThemeColor()));


            /**
             * This gets called by the child views during a click. We fetch the date that has been
             * selected, and then call the onClick handler registered with this adapter, passing that
             * date.
             *
             * @param v the View that was clicked
             */
            binding.rootView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onClick(competition);
            });
        }
    }
}
